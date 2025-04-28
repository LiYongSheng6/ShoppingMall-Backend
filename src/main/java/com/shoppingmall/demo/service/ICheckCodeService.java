package com.shoppingmall.demo.service;

import com.shoppingmall.demo.model.VO.VerifyCodeVO;

/**
 * @author redmi k50 ultra
 * * @date 2024/7/24
 */
public interface ICheckCodeService {

    /**
     * 生成图片验证码
     *
     * @return
     */
    VerifyCodeVO generateVerifyCode();

}
