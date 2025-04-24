package com.shoppingmall.demo.model.Query;

import com.shoppingmall.demo.enums.UserType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserQuery extends PageQuery{

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("用户类型")
    private UserType type;
}
