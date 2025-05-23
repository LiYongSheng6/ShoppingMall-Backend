package com.shoppingmall.demo.model.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ChatHistoryVO {
    /**
     * 聊天记录id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("聊天记录id")
    private Long id;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 发送者id
     */
    @ApiModelProperty("发送者id")
    private String senderId;

    /**
     * 接受者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("接收者id")
    private Long receiverId;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("发送时间")
    private LocalDateTime  createTime;
}
