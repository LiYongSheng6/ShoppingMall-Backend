package com.shoppingmall.demo.model.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户关注关系实体类
 *
 * @author redmi k50 ultra
 * * @date 2024/7/19
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("user_follower")
public class UserFollower implements Serializable {
    /**
     * 主键id
     */
    @NotNull(message = "[主键id]不能为空")
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 用户id
     */
    @NotNull(message = "[用户id]不能为空")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 关注用户id
     */
    @NotNull(message = "[关联的用户id]不能为空")
    @ApiModelProperty("关联的用户id")
    private Long followerId;

    /**
     * 关注状态  0-普通关注 1-特别关注
     */
    @NotNull(message = "[关注状态]不能为空")
    @ApiModelProperty("关注状态  0-普通关注 1-特别关注")
    private Integer state;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

}


