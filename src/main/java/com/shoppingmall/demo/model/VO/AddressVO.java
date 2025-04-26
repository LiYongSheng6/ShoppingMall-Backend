package com.shoppingmall.demo.model.VO;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.shoppingmall.demo.enums.AddressType;
import com.shoppingmall.demo.model.DO.AddressDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AddressVO implements Serializable {

    /**
     * 地名id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("地名id")
    private Long id;

    /**
     * 父级地名id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("父级地名id")
    private Long parentId;

    /**
     * 地名名称
     */
    @ApiModelProperty("地名名称")
    private String addressName;

    /**
     * 父级地名名称
     */
    @ApiModelProperty("父级地名名称")
    private String parentName;

    /**
     * 地名类型
     */
    @ApiModelProperty("地名类型")
    private AddressType type;

    /**
     * 子分类
     */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AddressVO> children = new ArrayList<AddressVO>();


    public AddressVO(AddressDO addressDO){
        BeanUtil.copyProperties(addressDO,this);
    }

}
