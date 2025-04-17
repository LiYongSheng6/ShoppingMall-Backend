package com.shoppingmall.demo.model.DTO;

import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class UserRgsDTO {

    @ApiModelProperty("用户名")
    @NotBlank(message = MessageConstants.PARAM_MISSING)
    @Pattern(regexp = CacheConstants.USERNAME_REGEX, message = MessageConstants.USERNAME_REGEX_MESSAGE)
    private String username;

    @ApiModelProperty("用户密码")
    @NotBlank(message = MessageConstants.PARAM_MISSING)
    @Pattern(regexp = CacheConstants.PASSWORD_REGEX, message = MessageConstants.PASSWORD_REGEX_MESSAGE)
    private String password;

    @ApiModelProperty("用户邮箱")
    @NotBlank(message = MessageConstants.PARAM_MISSING)
    @Pattern(regexp = CacheConstants.EMAIL_REGEX, message = MessageConstants.EMAIL_REGEX_MESSAGE)
    private String email;

    @ApiModelProperty("验证码")
    @NotBlank(message = MessageConstants.PARAM_MISSING)
    @Pattern(regexp = CacheConstants.CODE_REGEX, message = MessageConstants.CODE_LENGTH_MESSAGE)
    private String code;

}
