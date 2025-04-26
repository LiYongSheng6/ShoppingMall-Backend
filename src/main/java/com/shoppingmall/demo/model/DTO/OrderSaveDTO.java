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
* 
* @TableName tag
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class OrderSaveDTO implements Serializable {

    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message="[用户id]不能为空")
    @ApiModelProperty("用户id")
    private Long userId;

    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message="[商品id]不能为空")
    @ApiModelProperty("商品id")
    private Long goodId;

    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message="[收货信息id]不能为空")
    @ApiModelProperty("收货信息id")
    private Long deliveryId;

    @NotNull(message="[商品购买数]不能为空")
    @ApiModelProperty("商品购买数")
    private Integer purchaseNum;

}
