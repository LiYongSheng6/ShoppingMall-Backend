package com.shoppingmall.demo.model.DTO;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class UserCommentDTO {

    @ApiModelProperty("用户ID集合")
    @NotNull(message = "用户ID集合不能为空")
    private List<Long> ids;

    @ApiModelProperty("评价内容")
    @NotBlank(message = "评价内容不能为空")
    @Length(max = 512, message = "评价内容长度不能超过512")
    private String content;

}