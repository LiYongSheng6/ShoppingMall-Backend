package com.shoppingmall.demo.model.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.config.deserializer.StringListToLongListDeserializer;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于给角色分配权限时保存选中的权限数据
 *
 * @author redmi k50 ultra
 * * @date 2024/8/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionDTO {

    /**
     * 角色id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message = "角色id不能为空")
    @ApiModelProperty("角色id")
    private Long roleId;

    /**
     * 权限资源id集合
     */
    @JsonDeserialize(using = StringListToLongListDeserializer.class)
    @NotNull(message = "权限资源id集合")
    @ApiModelProperty("权限资源id集合")
    private List<Long> permissionIds;
}
