package com.shoppingmall.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shoppingmall.demo.config.property.EmailConfigProperties;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.mapper.EmailMapper;
import com.shoppingmall.demo.model.DO.EmailDO;
import com.shoppingmall.demo.service.IEmailService;
import com.shoppingmall.demo.utils.CheckCodeUtil;
import com.shoppingmall.demo.utils.MailUtil;
import com.shoppingmall.demo.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.concurrent.TimeUnit;

/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl extends ServiceImpl<EmailMapper, EmailDO> implements IEmailService {
    private final EmailConfigProperties emailConfigProperties;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 发送邮箱验证码
     *
     * @param toUserEmail 收件人邮箱
     * @return
     */
    //@Async("taskExecutor")
    public Result sendCode(String toUserEmail, String keyTypeName) throws IOException {
        // 从redis获取验证码
        String key = CheckCodeUtil.generateKey(keyTypeName, toUserEmail);
        String value = stringRedisTemplate.opsForValue().get(key);

        // 如果获取到直接返回
        if (StringUtils.hasLength(value)) {
            //log.info("您的邮箱验证码仍然有效：" + CheckCodeUtil.parseVerifyCodeFromValue(value));
            return Result.success(MessageConstants.EMAIL_CODE_STILL_VALID);
        }

        //邮件标题
        String title = MessageConstants.EMAIL_CODE_TITLE;

        //邮箱验证码
        int expireTime = CacheConstants.EMAIL_CODE_EXPIRE_TIME;
        String verifyCode = CheckCodeUtil.generateVerifyCode(CacheConstants.EMAIL_CODE_LENGTH, CacheConstants.NUMBER_VERIFY_CODE_SOURCE);
        String content = "您的邮箱验证码是：" + verifyCode + "，该验证码" + expireTime + "秒内有效！如非本人操作，请忽略！请勿回复此邮箱";

        //打印发送邮件相关信息
        log.info("==>收件人：" + toUserEmail + "  标题：" + title + "  正文：" + content);

        //发送邮箱
        if (!MailUtil.sendMail(emailConfigProperties, toUserEmail, title, content)) {
            throw new ServerException(MessageConstants.EMAIL_CODE_SEND_ERROR);
        }

        //如果发送成功，就把验证码存到redis里，设置有效时间
        long timeout = System.currentTimeMillis() + expireTime * 1000L;
        String values = CheckCodeUtil.generateValue(verifyCode, timeout);
        stringRedisTemplate.opsForValue().set(key, values, expireTime, TimeUnit.SECONDS);

        return Result.success(MessageConstants.EMAIL_CODE_SEND_SUCCESS);
    }
}