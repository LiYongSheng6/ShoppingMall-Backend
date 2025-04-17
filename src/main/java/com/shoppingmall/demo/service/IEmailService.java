package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.EmailDO;
import com.shoppingmall.demo.model.DTO.EmailDTO;
import com.shoppingmall.demo.utils.Result;

import java.io.IOException;

/**
 * @author redmi k50 ultra
 * * @date 2024/7/17
 */
public interface IEmailService extends IService<EmailDO> {
    /**
     * 发送邮箱验证码
     *
     * @param email       收件人邮箱
     * @param keyTypeName 邮箱验证码key类型名称
     * @return
     * @throws IOException
     */
    Result sendCode(String email, String keyTypeName) throws IOException;

}
