package com.shoppingmall.demo.model.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.shoppingmall.demo.annotation.UserTypePattern;
import com.shoppingmall.demo.enums.UserType;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源权限信息实体类
 *
 * @author redmi k50 ultra
 * * @date 2024/7/19
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("permission")
public class PermissionDO implements Serializable {

    /**
     * 权限id
     */
    @NotNull(message="[权限id]不能为空")
    @ApiModelProperty("权限id")
    private Long id;

    /**
     * 父类id
     */
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
    @UserTypePattern
    private UserType type;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

}
