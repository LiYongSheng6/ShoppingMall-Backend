package com.shoppingmall.demo.model.DTO;

import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.constant.RegexConstants;
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
    @Pattern(regexp = RegexConstants.USERNAME_REGEX, message = MessageConstants.USERNAME_REGEX_MESSAGE)
    private String username;

    @ApiModelProperty("用户密码")
    @NotBlank(message = MessageConstants.PARAM_MISSING)
    @Pattern(regexp = RegexConstants.PASSWORD_REGEX, message = MessageConstants.PASSWORD_REGEX_MESSAGE)
    private String password;

/*    @ApiModelProperty("用户电话")
    @NotBlank(message = MessageConstants.PARAM_MISSING)
    @Pattern(regexp = RegexConstants.PHONE_REGEX, message = MessageConstants.PHONE_REGEX_MESSAGE)
    private String phone;*/

    @ApiModelProperty("用户邮箱")
    @NotBlank(message = MessageConstants.PARAM_MISSING)
    @Pattern(regexp = RegexConstants.EMAIL_REGEX, message = MessageConstants.EMAIL_REGEX_MESSAGE)
    private String email;

    @ApiModelProperty("验证码")
    @NotBlank(message = MessageConstants.PARAM_MISSING)
    @Pattern(regexp = RegexConstants.CODE_REGEX, message = MessageConstants.CODE_LENGTH_MESSAGE)
    private String code;

    @ApiModelProperty("验证码key")
    private String verifyCodeKey;
}
