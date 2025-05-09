package com.shoppingmall.demo.model.DTO;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.annotation.GenderPattern;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.GenderType;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class UserUpdateDTO {

    /**
     * 用户id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty("用户id")
    private Long id;

    /**
     * 用户名称
     */
    @Length(max= 20,message="用户名称长度不能超过20")
    @ApiModelProperty("用户名称")
    @Pattern(regexp = RegexConstants.USERNAME_REGEX, message = MessageConstants.USERNAME_REGEX_MESSAGE)
    private String username;

    /**
     * 用户手机号码
     */
    @Length(min= 11, max= 11,message="手机号长度为11位")
    @ApiModelProperty("用户手机号码")
    @Pattern(regexp = RegexConstants.PHONE_REGEX, message = MessageConstants.PHONE_REGEX_MESSAGE)
    private String phone;

    /**
     * 用户头像
     */
    @Length(max= 255,message="头像链接长度不能超过255")
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 用户简介
     */
    @Length(max= 512,message="简介内容长度不能超过512")
    @ApiModelProperty("用户简介")
    private String description;

    /**
     * 用户性别(0女，1男)
     */
    @ApiModelProperty("用户性别(0女，1男)")
    @GenderPattern
    private GenderType gender;

}