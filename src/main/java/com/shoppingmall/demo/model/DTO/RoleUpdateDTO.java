package com.shoppingmall.demo.model.DTO;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

/**
 * @author redmi k50 ultra
 * * @date 2024/7/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class RoleUpdateDTO {
    /**
     * 主键id
     */
    @NotNull(message = "[主键id]不能为空")
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 角色名称
     */
    @Length(max = 255, message = "角色名称长度不能超过255")
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色编码
     */
    @Length(max = 255, message = "角色编码长度不能超过255")
    @ApiModelProperty("角色编码")
    private String code;

    /**
     * 数据范围权限
     */
    @NotNull(message = "[数据范围权限]不能为空")
    @ApiModelProperty("数据范围权限")
    private Integer dataScope;

    /**
     * 角色描述
     */
    @Length(max = 255, message = "角色描述长度不能超过255")
    @ApiModelProperty("角色描述")
    private String description;

    /**
     * 父类id
     */
    @NotNull(message = "[父类id]不能为空")
    @ApiModelProperty("父类id")
    private Long parentId;

    /**
     * 创建者id
     */
    @NotNull(message = "[创建者id]不能为空")
    @ApiModelProperty("创建者id")
    private Long creatorId;

    /**
     * 启用状态
     */
    @NotNull(message = "[启用状态]不能为空")
    @ApiModelProperty("启用状态")
    private Integer status;

}
