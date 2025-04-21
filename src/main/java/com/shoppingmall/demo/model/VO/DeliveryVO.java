package com.shoppingmall.demo.model.VO;


import cn.hutool.core.bean.BeanUtil;
import com.shoppingmall.demo.model.DO.DeliveryDO;
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
public class DeliveryVO implements Serializable {

    /**
     * 收货地址id
     */
    @ApiModelProperty("收货地址id")
    private Long id;

    /**
     * 创建者名称
     */
    @ApiModelProperty("创建者名称")
    private String username;

    /**
     * 收货人姓名
     */
    @ApiModelProperty("收货人姓名")
    private String consigneeName;

    /**
     * 收货人电话
     */
    @ApiModelProperty("收货人电话")
    private String phone;

    /**
     * 省份
     */
    @ApiModelProperty("省份")
    private String province;

    /**
     * 城市
     */
    @ApiModelProperty("城市")
    private String city;

    /**
     * 区县
     */
    @ApiModelProperty("区县")
    private String county;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;

    public DeliveryVO(DeliveryDO deliveryDO){
        BeanUtil.copyProperties(deliveryDO,this);
    }

}
