package com.shoppingmall.demo.model.DTO;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.config.deserializer.StringListToLongListDeserializer;
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
public class OrderDeleteBatchDTO implements Serializable {

    /**
     * 订单id列表
     */
    @JsonDeserialize(using = StringListToLongListDeserializer.class)
    @NotNull(message = "[订单id列表]不能为空")
    @ApiModelProperty("订单id列表")
    private List<Long> orderIds;

}
