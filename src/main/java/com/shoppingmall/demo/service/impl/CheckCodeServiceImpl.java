package com.shoppingmall.demo.service.impl;

import com.shoppingmall.demo.config.property.ValidTimeConfigProperties;
import com.shoppingmall.demo.config.property.VerifyCodeImgConfigProperties;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.model.VO.VerifyCodeVO;
import com.shoppingmall.demo.service.ICheckCodeService;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 手机验证码
 *
 * @author redmi k50 ultra
 * * @date 2024/7/24
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckCodeServiceImpl implements ICheckCodeService {
    private final VerifyCodeImgConfigProperties verifyCodeImg;
    private final ValidTimeConfigProperties validTime;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 生成Base64编码的图片验证码
     *
     * @return
     */
    @Override
    public VerifyCodeVO generateVerifyCode() {
        // 创建验证码对象
        //Captcha captcha = new ArithmeticCaptcha();

        //png类型 指定验证码图片大小及字符个数
        SpecCaptcha captcha = new SpecCaptcha(Integer.parseInt(verifyCodeImg.width), Integer.parseInt(verifyCodeImg.height), Integer.parseInt(verifyCodeImg.length));
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);

        // 生成验证码key值以及文本值
        String verifyCodeKey = UUID.randomUUID().toString();
        String verifyCode = captcha.text();

        //日志记录验证码信息
        log.info("您的图片验证码是：" + verifyCode);
        log.info("verifyCodeKey是：" + verifyCodeKey);

        // 获取验证码图片，构造响应结果
        VerifyCodeVO verifyCodeVO = new VerifyCodeVO(verifyCodeKey, captcha.toBase64(), verifyCode);

        // 存入Redis，设置过期时间
        String key = CacheConstants.REGISTER_IMAGE_CODE_KEY + verifyCodeKey;
        stringRedisTemplate.opsForValue().set(key, verifyCode, Integer.parseInt(validTime.imageCode), TimeUnit.SECONDS);

        return verifyCodeVO;
    }

}


