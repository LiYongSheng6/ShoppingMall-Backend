package com.shoppingmall.demo.model.Query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class DeliveryQuery extends PageQuery {
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty("用户id")
    private Long userId;

}
