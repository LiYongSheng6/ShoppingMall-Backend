package com.shoppingmall.demo.model.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户学号")
    private String studentId;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("用户id")
    private Long userId;

}
