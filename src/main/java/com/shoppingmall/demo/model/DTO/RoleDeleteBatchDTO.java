package com.shoppingmall.demo.model.DTO;


import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.List;

/**
 * @TableName tag
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class RoleDeleteBatchDTO implements Serializable {

    /**
     * 角色id列表
     */
    @NotNull(message = "[角色id列表]不能为空")
    @ApiModelProperty("角色id列表")
    private List<Long> roleIds;

}
