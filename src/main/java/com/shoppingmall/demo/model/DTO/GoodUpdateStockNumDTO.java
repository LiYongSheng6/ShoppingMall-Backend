package com.shoppingmall.demo.model.DTO;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * @TableName tag
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class GoodUpdateStockNumDTO implements Serializable {

    /**
     * 商品id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message = "[商品id]不能为空")
    @ApiModelProperty("商品id")
    private Long id;

    /**
     * 商品存货添改数量
     */
    @NotNull(message = "[商品存货添改数量]不能为空")
    @ApiModelProperty("商品存货添改数量")
    private Integer stockNum;

}
