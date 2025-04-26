package com.shoppingmall.demo.model.Query;

import com.shoppingmall.demo.enums.AddressType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AddressQuery extends PageQuery {

    @Length(max = 255, message = "名称长度不能超过255")
    @ApiModelProperty("地名名称")
    private String addressName;

    @ApiModelProperty("地名类型")
    private AddressType type;
}
