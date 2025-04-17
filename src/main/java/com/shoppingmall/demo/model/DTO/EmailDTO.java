package com.shoppingmall.demo.model.DTO;


import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
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
public class EmailDTO {
    public interface Msg extends Default {
    }

    public interface Code extends Default {
    }

    /**
     * 收件人邮箱
     */
    @ApiModelProperty("收件人邮箱")
    @NotBlank(message = "收件人邮箱不能为空")
    @Pattern(regexp = CacheConstants.EMAIL_REGEX, message = MessageConstants.EMAIL_REGEX_MESSAGE)
    public String toUserEmail;

    /**
     * 邮件标题
     */
    @ApiModelProperty("邮件标题")
    @NotBlank(groups = Msg.class, message = "邮件标题不能为空")
    public String title;

    /**
     * 邮件正文
     */
    @ApiModelProperty("邮件正文")
    @NotBlank(groups = Msg.class, message = "邮件正文不能为空")
    public String content;

}
