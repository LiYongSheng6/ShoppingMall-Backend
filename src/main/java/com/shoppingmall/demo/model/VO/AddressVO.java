package com.shoppingmall.demo.model.VO;


import cn.hutool.core.bean.BeanUtil;
import com.shoppingmall.demo.model.DO.AddressDO;
import com.shoppingmall.demo.model.DO.DeliveryDO;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

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
    @NotNull(message="[地名id]不能为空")
    @ApiModelProperty("地名id")
    private Long id;

    /**
     * 父级地名id
     */
    @NotNull(message="[父级地名id]不能为空")
    @ApiModelProperty("父级地名id")
    private Long parentId;

    /**
     * 地名名称
     */
    @Length(max= 255,message="地名名称长度不能超过255")
    @ApiModelProperty("地名名称")
    private String addressName;

    /**
     * 地名类型
     */
    @NotNull(message="[地名类型]不能为空")
    @ApiModelProperty("地名类型")
    private Integer type;

    public AddressVO(AddressDO addressDO){
        BeanUtil.copyProperties(addressDO,this);
    }

}
