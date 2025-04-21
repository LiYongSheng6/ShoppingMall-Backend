package com.shoppingmall.demo.model.DTO;


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
public class DeliveryDeleteBatchDTO implements Serializable {

    /**
     * 收货信息id列表
     */
    @NotNull(message = "[收货信息id列表]不能为空")
    @ApiModelProperty("收货信息id列表")
    private List<Long> deliveryIds;

}
