package com.shoppingmall.demo.model.DO;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.shoppingmall.demo.enums.MessageStatus;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName chat_history
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
@TableName("chat_history")
public class ChatHistoryDO implements Serializable {

    /**
     * 聊天记录id
     */
    @NotNull(message = "[聊天记录id]不能为空")
    @ApiModelProperty("聊天记录id")
    private Long id;

    /**
     * 内容
     */
    @Length(max = 300, message = "编码长度不能超过300")
    @ApiModelProperty("内容")
    private String content;

    /**
     * 发送者id
     */
    @ApiModelProperty("发送者id")
    private Long fromUserId;

    /**
     * 接受者id
     */
    @ApiModelProperty("接收者id")
    private Long toUserId;

    /**
     * 消息状态
     */
    @ApiModelProperty("消息状态")
    private MessageStatus status;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("发送时间")
    private LocalDateTime createTime;

}
