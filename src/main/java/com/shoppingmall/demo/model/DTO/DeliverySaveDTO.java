package com.shoppingmall.demo.model.DTO;


import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.constant.RegexConstants;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
* 
* @TableName tag
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class DeliverySaveDTO implements Serializable {

    /**
     * 收货人姓名
     */
    @Length(max= 255,message="收货人姓名长度不能超过255")
    @ApiModelProperty("收货人姓名")
    private String consigneeName;

    /**
     * 收货人电话
     */
    @Pattern(regexp = RegexConstants.PHONE_REGEX, message = MessageConstants.PHONE_REGEX_MESSAGE)
    @ApiModelProperty("收货人电话")
    private String phone;

    /**
     * 省份
     */
    @Length(max = 255, message = "省份名称长度不能超过255")
    @ApiModelProperty("省份")
    private String province;

    /**
     * 城市
     */
    @Length(max = 255, message = "城市名称长度不能超过255")
    @ApiModelProperty("城市")
    private String city;

    /**
     * 区县
     */
    @Length(max = 255, message = "区县名称长度不能超过255")
    @ApiModelProperty("区县")
    private String county;

    /**
     * 详细地址
     */
    @Length(max= 512,message="详细地址长度不能超过512")
    @ApiModelProperty("详细地址")
    private String address;

}
