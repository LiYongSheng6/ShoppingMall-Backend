package com.shoppingmall.demo.model.DTO;


import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.constant.RegexConstants;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
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
public class AddressUpdateDTO implements Serializable {

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

}
