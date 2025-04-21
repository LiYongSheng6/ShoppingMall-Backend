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
public class AddressDeleteBatchDTO implements Serializable {

    /**
     * 地名名称
     */
    @NotNull(message = "[地名名称列表]不能为空")
    @ApiModelProperty("地名名称列表")
    private List<String> addressNameList;

}
