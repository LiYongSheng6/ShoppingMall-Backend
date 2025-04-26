package com.shoppingmall.demo.model.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.config.deserializer.StringListToLongListDeserializer;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 用于给用户分配角色时保存选中的角色数据
 *
 * @author redmi k50 ultra
 * * @date 2024/8/8
 */
@Data
public class UserRoleDTO {

    /**
     * 用户id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message = "用户id不能为空")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 角色id集合
     */
    @JsonDeserialize(using = StringListToLongListDeserializer.class)
    @NotNull(message = "角色id集合不能为空")
    @ApiModelProperty("角色id集合")
    private List<Long> roleIds;
}
