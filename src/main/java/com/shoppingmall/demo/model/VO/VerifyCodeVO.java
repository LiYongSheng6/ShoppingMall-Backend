package com.shoppingmall.demo.model.VO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 验证码实体
 *
 * @author RedmiK50Ultra
 * @date 2024/3/22 11:18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCodeVO implements Serializable {
    /**
     * 验证码Key
     */
    private String key;

    /**
     * 验证码图片，base64压缩后的字符串
     */
    private String image;

    /**
     * 验证码文本值
     */
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String text;
}
