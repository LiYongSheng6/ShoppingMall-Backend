package com.shoppingmall.demo.model.VO;


import cn.hutool.core.bean.BeanUtil;
import com.shoppingmall.demo.enums.AddressType;
import com.shoppingmall.demo.model.DO.AddressDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AddressVO implements Serializable {

    /**
     * 地名id
     */
    @ApiModelProperty("地名id")
    private Long id;

    /**
     * 父级地名id
     */
    @ApiModelProperty("父级地名id")
    private Long parentId;

    /**
     * 地名名称
     */
    @ApiModelProperty("地名名称")
    private String addressName;

    /**
     * 地名类型
     */
    @ApiModelProperty("地名类型")
    private AddressType type;

    public AddressVO(AddressDO addressDO){
        BeanUtil.copyProperties(addressDO,this);
    }

}
