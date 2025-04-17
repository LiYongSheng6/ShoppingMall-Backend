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
public class UserResetDTO {

    @ApiModelProperty("用户重置密码/邮箱")
    @NotBlank(message = MessageConstants.PARAM_MISSING)
    private String reset;

    @ApiModelProperty("验证码")
    @NotBlank(message = MessageConstants.PARAM_MISSING)
    @Pattern(regexp = CacheConstants.CODE_REGEX, message = MessageConstants.CODE_LENGTH_MESSAGE)
    private String code;

}