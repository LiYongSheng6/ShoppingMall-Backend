package com.shoppingmall.demo.model.DTO;


import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.constant.RegexConstants;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
     * 创建者id
     */
    @NotNull(message="[创建者id]不能为空")
    @ApiModelProperty("创建者id")
    private Long userId;

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
    @NotNull(message="[省份]不能为空")
    @ApiModelProperty("省份")
    private Long provinceId;

    /**
     * 城市
     */
    @NotNull(message="[城市]不能为空")
    @ApiModelProperty("城市")
    private Long cityId;

    /**
     * 区县
     */
    @NotNull(message="[区县]不能为空")
    @ApiModelProperty("区县")
    private Long countyId;

    /**
     * 详细地址
     */
    @Length(max= 512,message="详细地址长度不能超过512")
    @ApiModelProperty("详细地址")
    private String address;

}
