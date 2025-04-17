package com.shoppingmall.demo.model.DTO;


import com.shoppingmall.demo.annotation.ForbiddenPattern;
import com.shoppingmall.demo.annotation.GenderPattern;
import com.shoppingmall.demo.annotation.UserTypePattern;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.ForbiddenType;
import com.shoppingmall.demo.enums.GenderType;
import com.shoppingmall.demo.enums.UserType;
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
public class UserInfoDTO {

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long id;

    /**
     * 用户学号
     */
    @Length(min = 10, max= 10, message="学号长度为10位数字")
    @ApiModelProperty("用户学号")
    @Pattern(regexp = RegexConstants.STUDENT_ID_REGEX, message = MessageConstants.STUDENT_ID_REGEX_MESSAGE)
    private String studentId;

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
     * 用户邮箱地址
     */
    @Length(max= 32,message="邮箱长度不能超过32")
    @ApiModelProperty("用户邮箱地址")
    @Pattern(regexp = RegexConstants.EMAIL_REGEX, message = MessageConstants.EMAIL_REGEX_MESSAGE)
    private String email;

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

    /**
     * 用户类型(普通用户，超管)
     */
    @ApiModelProperty("用户类型(0普通用户，1超管)")
    @UserTypePattern
    private UserType type;

    /**
     * 封禁判定字段(0-未封禁，1-已封禁)
     */
    @ApiModelProperty("封禁判定字段(0-未封禁，1-已封禁)")
    @ForbiddenPattern
    private ForbiddenType isForbidden;

}