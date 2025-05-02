package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.GoodRankType;
import com.shoppingmall.demo.enums.GoodStatus;
import com.shoppingmall.demo.enums.GoodType;
import com.shoppingmall.demo.enums.UserType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.GoodMapper;
import com.shoppingmall.demo.model.DO.GoodDO;
import com.shoppingmall.demo.model.DTO.GoodDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.GoodSaveDTO;
import com.shoppingmall.demo.model.DTO.GoodUpdateDTO;
import com.shoppingmall.demo.model.DTO.GoodUpdateStockNumDTO;
import com.shoppingmall.demo.model.Query.GoodQuery;
import com.shoppingmall.demo.model.VO.GoodVO;
import com.shoppingmall.demo.model.VO.PageVO;
import com.shoppingmall.demo.service.ICategoryService;
import com.shoppingmall.demo.service.IGoodService;
import com.shoppingmall.demo.service.ITagService;
import com.shoppingmall.demo.service.IUserService;
import com.shoppingmall.demo.service.common.LoginInfoService;
import com.shoppingmall.demo.service.common.RedisCacheService;
import com.shoppingmall.demo.utils.RedisIdWorker;
import com.shoppingmall.demo.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodServiceImpl extends ServiceImpl<GoodMapper, GoodDO> implements IGoodService {

    private final RedisIdWorker redisIdWorker;
    private final RedisCacheService redisCacheService;
    private final LoginInfoService loginInfoService;
    private final ICategoryService categoryService;
    private final ITagService tagService;
    private final IUserService userService;

    @Override
    public Result saveGood(GoodSaveDTO goodSaveDTO) {
        return save(BeanUtil.copyProperties(goodSaveDTO, GoodDO.class)
                .setId(redisIdWorker.nextId(CacheConstants.GOOD_ID_PREFIX))
                .setCreatorId((loginInfoService.getLoginId()))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateGood(GoodUpdateDTO goodUpdateDTO) {
        if (!Objects.equals(UserType.ADMIN, userService.getById(loginInfoService.getLoginId()).getType())) {
            loginInfoService.CheckLoginUserObject(getCreatorIdByGoodId(goodUpdateDTO.getId()));
        }
        return updateById(BeanUtil.copyProperties(goodUpdateDTO, GoodDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    @Retryable(value = ServiceException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public Result updateGoodStockNum(GoodUpdateStockNumDTO updateDTO) {
        GoodDO goodDO = getById(updateDTO.getId());
        if (goodDO == null) throw new ServiceException(MessageConstants.NO_FOUND_GOOD_ERROR);

        loginInfoService.CheckLoginUserObject((goodDO.getCreatorId()));

        if (!GoodStatus.REMOVE.equals(goodDO.getStatus()))
            throw new ServiceException(MessageConstants.GOOD_NOT_REMOVE_ERROR);

        if (goodDO.getStockNum() + updateDTO.getStockNum() < 0)
            throw new ServiceException(MessageConstants.NUMBER_NEGATIVE_ERROR);

        return Db.lambdaUpdate(GoodDO.class).setSql("stock_num=stock_num+(" + updateDTO.getStockNum() + ")")
                .set(GoodDO::getUpdateTime, LocalDateTime.now())
                .eq(GoodDO::getId, updateDTO.getId()).update() ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    public Result deleteGoodById(Long id) {
        if (!Objects.equals(UserType.ADMIN, userService.getById(loginInfoService.getLoginId()).getType())) {
            loginInfoService.CheckLoginUserObject(getCreatorIdByGoodId(id));
        }
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

    @Override
    public Result deleteGoodBatch(GoodDeleteBatchDTO deleteBatchDTO) {
        List<GoodDO> list;
        if (!Objects.equals(UserType.ADMIN, userService.getById(loginInfoService.getLoginId()).getType())) {
            list = lambdaQuery().in(GoodDO::getId, deleteBatchDTO.getGoodIds()).eq(GoodDO::getCreatorId, loginInfoService.getLoginId()).list();
        } else list = lambdaQuery().in(GoodDO::getId, deleteBatchDTO.getGoodIds()).list();

        if (CollectionUtils.isEmpty(list)) throw new ServiceException(MessageConstants.NO_FOUND_GOOD_ERROR);
        return Db.removeByIds(list, GoodDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    private Long getCreatorIdByGoodId(Long id) {
        GoodDO goodDO = getById(id);
        return goodDO != null ? goodDO.getCreatorId() : null;
    }

    @Override
    public Result getGoodInfoById(Long id) {
        GoodDO goodDO = getById(id);
        Optional.ofNullable(goodDO).orElseThrow(() -> new ServiceException(MessageConstants.NO_FOUND_GOOD_ERROR));

        return Result.success(new GoodVO(getById(id))
                .setCreatorName(userService.getUserNameById(goodDO.getCreatorId()))
                .setCreatorAvatar(userService.getUserAvatarById(goodDO.getCreatorId()))
                .setTagName(tagService.getTagNameById((goodDO.getTagId())))
                .setCategoryName(categoryService.getCategoryNameById(String.valueOf(goodDO.getCategoryId()))));
    }

    @Override
    public Result getMyGoodListPage(GoodQuery goodQuery) {
        goodQuery.setCreatorId(loginInfoService.getLoginId());
        return pageGoodListByCondition(goodQuery);
    }

    @Override
    public Result pageGoodListByCondition(GoodQuery goodQuery) {
        // 分页参数
        Page<GoodDO> page = goodQuery.toMpPageDefaultSortByUpdateTime();
        // 查询参数
        Long creatorId = goodQuery.getCreatorId();
        List<Long> tagId = goodQuery.getTagIds();
        List<Long> categoryIds = goodQuery.getCategoryIds();
        Integer minimizePrice = goodQuery.getMinimizePrice();
        Integer maximizePrice = goodQuery.getMaximizePrice();
        GoodStatus status = goodQuery.getStatus();
        GoodType type = goodQuery.getType();
        String goodName = goodQuery.getGoodName();
        //GoodRankType rankType = goodQuery.getRankType();
        // 查找条件参数
        Page<GoodDO> pageDO = lambdaQuery()
                .eq(creatorId != null, GoodDO::getCreatorId, creatorId)
                .in(CollectionUtils.isNotEmpty(tagId), GoodDO::getTagId, tagId)
                .in(CollectionUtils.isNotEmpty(categoryIds), GoodDO::getCategoryId, categoryIds)
                .ge(minimizePrice != null && minimizePrice > 0, GoodDO::getPrice, minimizePrice)
                .le(maximizePrice != null && maximizePrice > 0, GoodDO::getPrice, maximizePrice)
                .eq(status != null, GoodDO::getStatus, status)
                .eq(type != null, GoodDO::getType, type)
                .like(StringUtils.hasLength(goodName), GoodDO::getGoodName, goodName)
                .page(page);
        // 非空集合返回
        if (CollectionUtils.isEmpty(pageDO.getRecords()))
            return Result.success(new PageVO<>(0L, 0L, 0L, new ArrayList<GoodVO>()));
        // 封装转换并返回
        return Result.success(PageVO.of(pageDO, GoodDO -> new GoodVO(GoodDO)
                .setCreatorName(userService.getUserNameById((GoodDO.getCreatorId())))
                .setCreatorAvatar(userService.getUserAvatarById((GoodDO.getCreatorId())))
                .setTagName(tagService.getTagNameById((GoodDO.getTagId())))
                .setCategoryName(categoryService.getCategoryNameById(String.valueOf(GoodDO.getCategoryId())))));
    }

    @Override
    public Result getGoodRankList(GoodQuery goodQuery) {
        GoodType type = goodQuery.getType();
        GoodRankType goodRankType = goodQuery.getRankType();

        String hashKey;
        if (GoodRankType.DAY.equals(goodRankType)) hashKey = CacheConstants.GOOD_SALES_NUM_DAY_KEY;
        else if (GoodRankType.WEEK.equals(goodRankType)) hashKey = CacheConstants.GOOD_SALES_NUM_WEEK_KEY;
        else if (GoodRankType.YEAR.equals(goodRankType)) hashKey = CacheConstants.GOOD_SALES_NUM_YEAR_KEY;
        else if (GoodRankType.OVERALL.equals(goodRankType)) hashKey = CacheConstants.GOOD_SALES_NUM_OVERALL_KEY;
        else return Result.error(MessageConstants.GOOD_RANK_TYPE_NULL_ERROR);

        List<GoodDO> goodDOList = new ArrayList<>();
        Map<Object, Object> hashAll = redisCacheService.getHashAll(hashKey);
        hashAll.forEach((key, value) -> {
            goodDOList.add(lambdaQuery().eq(GoodDO::getId, key).eq(GoodDO::getType, type).one().setSaleNum(Integer.parseInt(value.toString())));
        });

        if (CollectionUtils.isEmpty(goodDOList)) return Result.error(MessageConstants.NO_FOUND_GOOD_ERROR);
        else
            return Result.success(BeanUtil.copyToList(goodDOList.stream().sorted(Comparator.comparing(GoodDO::getSaleNum).reversed()).limit(50).toList(), GoodVO.class));
    }


}
