package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.GoodRankType;
import com.shoppingmall.demo.enums.GoodType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.GoodMapper;
import com.shoppingmall.demo.model.DO.TagDO;
import com.shoppingmall.demo.model.DO.CategoryDO;
import com.shoppingmall.demo.model.DO.GoodDO;
import com.shoppingmall.demo.model.DO.UserDO;
import com.shoppingmall.demo.model.DTO.GoodSaveDTO;
import com.shoppingmall.demo.model.DTO.GoodUpdateDTO;
import com.shoppingmall.demo.model.Query.GoodQuery;
import com.shoppingmall.demo.model.VO.GoodVO;
import com.shoppingmall.demo.model.VO.PageVO;
import com.shoppingmall.demo.service.IGoodService;
import com.shoppingmall.demo.service.common.RedisCacheService;
import com.shoppingmall.demo.utils.RedisIdWorker;
import com.shoppingmall.demo.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodServiceImpl extends ServiceImpl<GoodMapper, GoodDO> implements IGoodService {

    private final RedisIdWorker redisIdWorker;
    private final RedisCacheService redisCacheService;

    @Override
    public Result saveGood(GoodSaveDTO goodSaveDTO) {
        return save(BeanUtil.copyProperties(goodSaveDTO, GoodDO.class).setId(redisIdWorker.nextId(CacheConstants.GOOD_ID_PREFIX))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateGood(GoodUpdateDTO goodUpdateDTO) {
        return updateById(BeanUtil.copyProperties(goodUpdateDTO, GoodDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    public Result deleteGoodById(Long id) {
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

    @Override
    public Result getGoodById(Long id) {
        GoodDO goodDO = getById(id);
        Optional.ofNullable(goodDO).orElseThrow(() -> new ServiceException(MessageConstants.NO_FOUND_GOOD_ERROR));

        UserDO creator = Db.lambdaQuery(UserDO.class).eq(UserDO::getId, goodDO.getCreatorId()).one();
        return Result.success(new GoodVO(getById(id))
                        .setCategoryName(creator.getUsername()).setCreatorAvatar(creator.getAvatar())
                        .setTagName(Db.lambdaQuery(TagDO.class).eq(TagDO::getId, goodDO.getTagId()).one().getTagName())
                        .setCategoryName(Db.lambdaQuery(CategoryDO.class).eq(CategoryDO::getId, goodDO.getCategoryId()).one().getCategoryName()));
    }

    @Override
    public Result pageGoodListByCondition(GoodQuery goodQuery) {
        Page<GoodDO> page = goodQuery.toMpPageDefaultSortByUpdateTime();

        List<Long> tagId = goodQuery.getTagIds();
        List<Long> categoryIds = goodQuery.getCategoryIds();
        Integer minimizePrice = goodQuery.getMinimizePrice();
        Integer maximizePrice = goodQuery.getMaximizePrice();
        GoodType type = goodQuery.getType();

        Page<GoodDO> pageDO = lambdaQuery().in(CollectionUtils.isNotEmpty(tagId), GoodDO::getTagId, tagId)
                .in(CollectionUtils.isNotEmpty(categoryIds), GoodDO::getCategoryId, categoryIds)
                .ge(minimizePrice != null, GoodDO::getPrice, minimizePrice)
                .le(maximizePrice != null, GoodDO::getPrice, maximizePrice)
                .eq(type != null, GoodDO::getType, type)
                .page(page);

        if(CollectionUtils.isEmpty(pageDO.getRecords())) return Result.error(MessageConstants.NO_FOUND_GOOD_ERROR);

        return Result.success(PageVO.of(pageDO, GoodDO -> BeanUtil.copyProperties(GoodDO, GoodVO.class)
                .setTagName(Db.getById(GoodDO.getTagId(), TagDO.class).getTagName())
                .setCategoryName(Db.getById(GoodDO.getCategoryId(), CategoryDO.class).getCategoryName())));
    }

    @Override
    public Result getGoodRankList(GoodQuery goodQuery) {
        GoodType type = goodQuery.getType();
        GoodRankType goodRankType = goodQuery.getRankType();

        String hashKey;
        if(GoodRankType.DAY.equals(goodRankType)) hashKey = CacheConstants.GOOD_SALES_NUM_DAY_KEY;
        else if (GoodRankType.WEEK.equals(goodRankType)) hashKey = CacheConstants.GOOD_SALES_NUM_WEEK_KEY;
        else if (GoodRankType.YEAR.equals(goodRankType)) hashKey = CacheConstants.GOOD_SALES_NUM_YEAR_KEY;
        else if (GoodRankType.OVERALL.equals(goodRankType)) hashKey = CacheConstants.GOOD_SALES_NUM_OVERALL_KEY;
        else return Result.error(MessageConstants.GOOD_RANK_TYPE_NULL_ERROR);

        List<GoodDO> goodDOList = new ArrayList<>();
        Map<Object, Object> hashAll = redisCacheService.getHashAll(hashKey);
        hashAll.forEach((key,value)->{
            goodDOList.add(lambdaQuery().eq(GoodDO::getId, key).eq(GoodDO::getType,type).one().setSalesNum(Integer.parseInt(value.toString())));
        });

        if(CollectionUtils.isEmpty(goodDOList))return Result.error(MessageConstants.NO_FOUND_GOOD_ERROR);
        else return Result.success(BeanUtil.copyToList(goodDOList.stream().sorted(Comparator.comparing(GoodDO::getSalesNum).reversed()).limit(50).toList(), GoodVO.class));
    }

}
