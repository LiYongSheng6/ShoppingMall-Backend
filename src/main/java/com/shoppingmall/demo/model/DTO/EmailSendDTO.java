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

/**
 * @author redmi k50 ultra
 * * @date 2024/7/17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class EmailSendDTO {

    /**
     * 收件人邮箱
     */
    @ApiModelProperty("收件人邮箱")
    @NotBlank(message = "收件人邮箱不能为空")
    @Pattern(regexp = CacheConstants.EMAIL_REGEX, message = MessageConstants.EMAIL_REGEX_MESSAGE)
    public String toUserEmail;

}
