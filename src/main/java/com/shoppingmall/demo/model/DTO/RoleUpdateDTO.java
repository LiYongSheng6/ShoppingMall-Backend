package com.shoppingmall.demo.model.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
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
    @JsonDeserialize(using = StringToLongDeserializer.class)
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
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty("父类id")
    private Long parentId;

    /**
     * 启用状态
     */
    @ApiModelProperty("启用状态")
    private Integer status;

}
