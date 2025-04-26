package com.shoppingmall.demo.model.VO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.shoppingmall.demo.enums.GenderType;
import com.shoppingmall.demo.enums.UserType;
import com.shoppingmall.demo.model.DO.UserDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("用户id")
    private Long id;

    /**
     * 用户学号
     */
    @ApiModelProperty("用户学号")
    private String studentId;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String username;

    /**
     * 用户手机号码
     */
    @ApiModelProperty("用户手机号码")
    private String phone;

    /**
     * 用户邮箱地址
     */
    @ApiModelProperty("用户邮箱地址")

    private String email;
    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 用户简介
     */
    @ApiModelProperty("用户简介")
    private String description;

    /**
     * 用户性别(0女，1男)
     */
    @ApiModelProperty("用户性别(0女，1男)")
    private GenderType gender;

    /**
     * 用户类型(普通用户，超管)
     */
    @ApiModelProperty("用户类型(0普通用户，1超管)")
    private UserType type;

    public UserVO(UserDO userDO){
        BeanUtils.copyProperties(userDO,this);
    }

}
