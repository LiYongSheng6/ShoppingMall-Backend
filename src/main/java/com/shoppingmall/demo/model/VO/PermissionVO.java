package com.shoppingmall.demo.model.VO;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.shoppingmall.demo.annotation.PermissionTypePattern;
import com.shoppingmall.demo.enums.PermissionType;
import com.shoppingmall.demo.model.DO.PermissionDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PermissionVO {

    /**
     * 权限id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("权限id")
    private Long id;

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

    /**
     * 父类名称
     */
    @ApiModelProperty("父类名称")
    private String parentName;

    /**
     * 父类id
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty("父类id")
    private Long parentId;

    /**
     * 是否拥有
     */
    @TableField(exist = false)
    @ApiModelProperty("是否拥有")
    private Boolean isHave;

    /**
     * 子分类
     */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PermissionVO> children = new ArrayList<PermissionVO>();

    public PermissionVO(PermissionDO permissionDO){
        BeanUtil.copyProperties(permissionDO,this);
    }


}
