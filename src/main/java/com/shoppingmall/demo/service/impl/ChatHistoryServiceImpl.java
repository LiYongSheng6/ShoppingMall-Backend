package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;

import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.MessageStatus;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.ChatHistoryMapper;
import com.shoppingmall.demo.model.DO.ChatHistoryDO;
import com.shoppingmall.demo.model.DO.UserDO;
import com.shoppingmall.demo.model.DTO.ChatHistoryDTO;
import com.shoppingmall.demo.model.Query.ChatHistoryQuery;
import com.shoppingmall.demo.model.Query.PageQuery;
import com.shoppingmall.demo.model.VO.ChatHistoryVO;
import com.shoppingmall.demo.model.VO.PageVO;
import com.shoppingmall.demo.model.VO.UserChatVO;
import com.shoppingmall.demo.service.IChatHistoryService;
import com.shoppingmall.demo.service.common.LoginInfoService;
import com.shoppingmall.demo.service.common.RedisCacheService;
import com.shoppingmall.demo.utils.RedisIdWorker;
import com.shoppingmall.demo.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistoryDO> implements IChatHistoryService {
    private final LoginInfoService loginInfoService;
    private final RedisIdWorker redisIdWorker;
    private final RedisCacheService redisCacheService;
    private final RedissonClient redissonClient;

    @Override
    public Result<PageVO<ChatHistoryVO>> getChatHistory(ChatHistoryQuery chatHistoryQuery) {
        Long fromUserId = chatHistoryQuery.getToUserId();
        Long toUserId = loginInfoService.getLoginId();
        Page<ChatHistoryDO> page = chatHistoryQuery.toMpPageDefaultSortByCreateTime();
        Page<ChatHistoryDO> pageDO = lambdaQuery()
                .eq(ChatHistoryDO::getFromUserId, fromUserId)
                .eq(ChatHistoryDO::getToUserId, toUserId)
                .or()
                .eq(ChatHistoryDO::getFromUserId, toUserId)
                .eq(ChatHistoryDO::getToUserId, fromUserId)
                .page(page);
        //将未读消息设置成已读
        lambdaUpdate()
                .eq(ChatHistoryDO::getFromUserId, fromUserId)
                .eq(ChatHistoryDO::getToUserId, toUserId)
                .eq(ChatHistoryDO::getStatus, MessageStatus.UNREAD)
                .set(ChatHistoryDO::getStatus, MessageStatus.READ)
                .update();

        RLock lock = redissonClient.getLock(CacheConstants.CHAT_HISTORY_LOCK);
        if (!lock.tryLock()) {
            throw new ServiceException(MessageConstants.OPERATION_ERROR);
        }
        try {
            String hashKey = toUserId + "::" + fromUserId;
            long count = getUnreadMessageCount(CacheConstants.NO_READ_CHAT_HISTORY_SEPARATE_NUM_KEY, hashKey);
            redisCacheService.incrementHash(CacheConstants.NO_READ_CHAT_HISTORY_TOTAL_NUM_KEY, JSON.toJSONString(toUserId), -1L * count);
            redisCacheService.incrementHash(CacheConstants.NO_READ_TOTAL_NUM_KEY, JSON.toJSONString(toUserId), -1L * count);

            redisCacheService.removeHashKey(CacheConstants.NO_READ_CHAT_HISTORY_SEPARATE_NUM_KEY, hashKey);
        } finally {
            lock.unlock();
        }
        return Result.success(PageVO.of(pageDO, chatHistoryDO -> BeanUtil.copyProperties(chatHistoryDO, ChatHistoryVO.class)));
    }

    @Transactional
    @Override
    public Result sendMessage(ChatHistoryDTO chatHistoryDTO) {
        List<Long> toUserIds = chatHistoryDTO.getToUserIds();
        String content = chatHistoryDTO.getContent();
        List<UserDO> userDOList = Db.listByIds(toUserIds, UserDO.class);
        if (userDOList.size() != toUserIds.size()) {
            throw new ServiceException(MessageConstants.NO_FOUND_USER_ERROR);
        }
        Long fromUserId = loginInfoService.getLoginId();
        ArrayList<ChatHistoryDO> chatHistoryDoList = new ArrayList<>();
        toUserIds.forEach(toUserId -> {
            chatHistoryDoList.add(
                    new ChatHistoryDO().setId(redisIdWorker.nextId(CacheConstants.CHAT_HISTORY_ID))
                            .setContent(content)
                            .setCreateTime(LocalDateTime.now())
                            .setToUserId(toUserId)
                            .setFromUserId(fromUserId)
                            .setStatus(MessageStatus.UNREAD)
            );
            redisCacheService.incrementHash(CacheConstants.NO_READ_CHAT_HISTORY_SEPARATE_NUM_KEY, toUserId + "::" + fromUserId, 1);
            redisCacheService.incrementHash(CacheConstants.NO_READ_CHAT_HISTORY_TOTAL_NUM_KEY, JSON.toJSONString(toUserId), 1);
            redisCacheService.incrementHash(CacheConstants.NO_READ_TOTAL_NUM_KEY, JSON.toJSONString(toUserId), 1);
        });
        return saveBatch(chatHistoryDoList) ? Result.success() : Result.error();
    }

    private long getUnreadMessageCount(String key, String toUserId) {
        Object hashValue = redisCacheService.getHashValue(key, toUserId);
        Long count;
        if (hashValue != null) {
            return Long.parseLong(hashValue.toString());
        } else {
            count = lambdaQuery().eq(ChatHistoryDO::getToUserId, toUserId).eq(ChatHistoryDO::getStatus, MessageStatus.UNREAD).count();
            redisCacheService.incrementHash(key, toUserId, count);
            return count;
        }
    }

    @Override
    public Long countTotalUnreadMessage(Long toUserId) {
        return getUnreadMessageCount(CacheConstants.NO_READ_TOTAL_NUM_KEY, JSON.toJSONString(toUserId));
    }

    @Override
    public Result<Long> countTotalUnreadChatMessage() {
        return Result.success(getUnreadMessageCount(CacheConstants.NO_READ_CHAT_HISTORY_TOTAL_NUM_KEY, JSON.toJSONString(loginInfoService.getLoginId())));
    }

    @Override
    public Result<PageVO<UserChatVO>> countSeparateUnreadChatMessage(PageQuery pageQuery) {
        Page<UserDO> page = pageQuery.toMpPageDefaultSortByUpdateTime();

        Long loginUserId = loginInfoService.getLoginId();
        List<Long> fromUserIds = Db.lambdaQuery(ChatHistoryDO.class).eq(ChatHistoryDO::getToUserId, loginUserId).list()
                .stream().map(ChatHistoryDO::getFromUserId).toList();

        Page<UserDO> page1 = Db.lambdaQuery(UserDO.class).in(UserDO::getId, fromUserIds).page(page);
        if (CollectionUtils.isEmpty(page1.getRecords()))
            throw new ServiceException(MessageConstants.NO_FOUND_USER_ERROR);

        return Result.success(PageVO.of(page1, userDO -> {
            UserChatVO userChatVO = BeanUtil.copyProperties(userDO, UserChatVO.class).setCount(0L);

            redisCacheService.getHashAll(CacheConstants.NO_READ_CHAT_HISTORY_SEPARATE_NUM_KEY).forEach((key, value) -> {
                String[] split = key.toString().split(":");
                Long toUserId = Long.parseLong(split[0]);
                if (!toUserId.equals(loginUserId)) return;

                Long fromUserId = Long.parseLong(split[1]);
                if ((Db.getById(fromUserId, UserDO.class)) == null) {
                    log.error("用户id:{}不存在或已被删除", fromUserId);
                } else {
                    if (userDO.getId().equals(fromUserId)) {
                        userChatVO.setCount(Long.parseLong(value.toString()));
                    }
                }
            });
            return userChatVO;
        }));
    }

}