package com.shoppingmall.demo.model.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.annotation.PermissionTypePattern;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.enums.PermissionType;
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
 * 资源权限信息实体类
 *
 * @author redmi k50 ultra
 * * @date 2024/7/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class PermissionUpdateDTO {

    /**
     * 权限id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty("权限id")
    @NotNull(message="[权限id]不能为空")
    private Long id;

    /**
     * 父类id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message = "[父类id]不能为空")
    @ApiModelProperty("父类id")
    private Long parentId;

    /**
     *
     * 权限标签名称
     */
    @Length(max= 255, message="权限标签名称长度不能超过255")
    @ApiModelProperty("权限标签名称")
    private String name;

    /**
     * 授权标识符
     */
    @Length(max= 255,message="授权标识符长度不能超过255")
    @ApiModelProperty("授权标识符")
    private String code;

    /**
     * 权限描述
     */
    @Length(max= 512,message="权限描述长度不能超过512")
    @ApiModelProperty("权限描述")
    private String description;

    /**
     * 路由地址
     */
    @Length(max= 255,message="路由地址长度不能超过255")
    @ApiModelProperty("路由地址")
    private String path;

    /**
     * 菜单类型
     */
    @ApiModelProperty("菜单类型")
    private Integer menu;

    /**
     * 图标
     */
    @Length(max= 255,message="图标长度不能超过255")
    @ApiModelProperty("图标")
    private String icon;

    /**
     * 前端组件资源
     */
    @Length(max= 255,message="前端组件资源长度不能超过255")
    @ApiModelProperty("前端组件资源")
    private String component;

    /**
     * 权限类型(0-后端,1-前端)
     */
    @ApiModelProperty("权限类型(0-后端,1-前端)")
    @PermissionTypePattern
    private PermissionType type;
}
