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
public class UserLoginDTO {

    @ApiModelProperty("用户邮箱")
    @NotBlank(message = MessageConstants.PARAM_MISSING)
    @Pattern(regexp = RegexConstants.EMAIL_REGEX, message = MessageConstants.EMAIL_REGEX_MESSAGE)
    private String email;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("验证码")
    private String code;

}