package com.shoppingmall.demo.model.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
 * 角色信息实体类
 *
 * @author redmi k50 ultra
 * * @date 2024/7/19
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("role")
public class RoleDO implements Serializable {
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

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
