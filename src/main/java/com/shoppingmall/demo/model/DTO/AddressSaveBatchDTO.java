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
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.List;

/**
 * @TableName tag
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class AddressSaveBatchDTO implements Serializable {

    /**
     * 父级地名id
     */
    @NotNull(message = "[父级地名id]不能为空")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty("父级地名id")
    private Long parentId;

    /**
     * 地名名称
     */
    @NotNull(message = "[地名名称列表]不能为空")
    @ApiModelProperty("地名名称列表")
    private List<String> addressNameList;

    /**
     * 地名类型
     */
    @AddressTypePattern
    @ApiModelProperty("地名类型")
    private AddressType type;

}
