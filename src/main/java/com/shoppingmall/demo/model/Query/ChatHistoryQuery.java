package com.shoppingmall.demo.model.Query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class ChatHistoryQuery extends PageQuery{
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message = "接受者id不能为null")
    @ApiModelProperty("接受者的id")
    private Long receiverId;

}
