package com.shoppingmall.demo.model.VO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserChatVO {
    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message="[用户id]不能为空")
    @ApiModelProperty("用户id")
    private Long id;

    /**
     * 用户名称
     */
    @Size(max= 255,message="编码长度不能超过32")
    @ApiModelProperty("用户名称")
    @Length(max= 255,message="编码长度不能超过32")
    private String username;

    /**
     * 用户头像
     */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("用户头像")
    @Length(max= 255,message="编码长度不能超过255")
    private String avatar;

    /**
     * 用户未读消息数
     */
    @ApiModelProperty("用户未读消息数")
    private Long count;

}
