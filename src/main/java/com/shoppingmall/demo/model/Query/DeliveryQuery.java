package com.shoppingmall.demo.model.Query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeliveryQuery extends PageQuery {

    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty("用户id")
    private Long userId;

}
