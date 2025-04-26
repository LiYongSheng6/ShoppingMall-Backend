package com.shoppingmall.demo.model.DTO;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.annotation.AddressTypePattern;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.enums.AddressType;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
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
public class AddressUpdateDTO implements Serializable {

    /**
     * 地名id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message="[地名id]不能为空")
    @ApiModelProperty("地名id")
    private Long id;

    /**
     * 父级地名id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
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
    @AddressTypePattern
    @ApiModelProperty("地名类型")
    private AddressType type;

}
