package com.shoppingmall.demo.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.ChatHistoryDO;
import com.shoppingmall.demo.model.DTO.ChatHistoryDTO;
import com.shoppingmall.demo.model.Query.ChatHistoryQuery;
import com.shoppingmall.demo.model.Query.PageQuery;
import com.shoppingmall.demo.model.VO.ChatHistoryVO;
import com.shoppingmall.demo.model.VO.PageVO;
import com.shoppingmall.demo.model.VO.UserChatVO;
import com.shoppingmall.demo.utils.Result;

public interface IChatHistoryService extends IService<ChatHistoryDO> {
    Result<PageVO<ChatHistoryVO>> getChatHistory(ChatHistoryQuery chatHistoryQuery);

    Result sendMessage(ChatHistoryDTO chatHistoryDTO);

    Long countTotalUnreadMessage(Long toUserId);

    Result<Long> countTotalUnreadChatMessage();
    Result<PageVO<UserChatVO>> countSeparateUnreadChatMessage(PageQuery pageQuery);
}
