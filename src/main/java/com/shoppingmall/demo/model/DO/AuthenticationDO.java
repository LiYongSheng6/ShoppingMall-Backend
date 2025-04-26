package com.shoppingmall.demo.model.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.constant.RegexConstants;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实名信息实体类
 *
 * @author redmi k50 ultra
 * * @date 2024/7/19
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("authentication")
public class AuthenticationDO implements Serializable {
    /**
     * 主键id
     */
    @NotNull(message = "[主键id]不能为空")
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 用户学号
     */
    @Length(min = 10, max = 10, message = "学号长度为10位数字")
    @ApiModelProperty("用户学号")
    @Pattern(regexp = RegexConstants.STUDENT_ID_REGEX, message = MessageConstants.STUDENT_ID_REGEX_MESSAGE)
    private String studentId;

    /**
     * 真实姓名
     */
    @Length(max = 255, message = "名称长度不能超过255")
    @ApiModelProperty("真实姓名")
    private String realName;

    /**
     * 用户id
     */
    @NotNull(message = "[用户id]不能为空")
    @ApiModelProperty("用户id")
    private Long userId;

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
