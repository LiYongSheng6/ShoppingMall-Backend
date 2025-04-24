package com.shoppingmall.demo.model.VO;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.demo.model.DO.RoleDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleVO {
    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色编码
     */
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
    @ApiModelProperty("角色描述")
    private String description;

    /**
     * 父类名称
     */
    @ApiModelProperty("父类名称")
    private String parentName;

    /**
     * 父类id
     */
    @ApiModelProperty("父类id")
    private Long parentId;

    /**
     * 创建者id
     */
    @ApiModelProperty("创建者id")
    private Long creatorId;

    /**
     * 启用状态
     */
    @ApiModelProperty("启用状态")
    private Integer status;

    /**
     * 子分类
     */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RoleVO> children = new ArrayList<RoleVO>();

    public RoleVO(RoleDO roleDO) {
        BeanUtil.copyProperties(roleDO, this);
    }

}
