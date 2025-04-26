package com.shoppingmall.demo.model.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
@Validated
public class ChatHistoryDTO {
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message = "接收者id列表")
    @Size(max = 10, message = "最多选择十位用户")
    @Size(min = 1, message = "至少选择一位用户")
    @ApiModelProperty("接收者id列表")
    List<Long> receiverIds;

    @NotBlank(message = "消息不能为空")
    @ApiModelProperty("消息内容")
    String content;
}
