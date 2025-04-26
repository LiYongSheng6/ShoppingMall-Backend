package com.shoppingmall.demo.model.Query;

import com.shoppingmall.demo.enums.TagType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class TagQuery extends PageQuery {

    @Length(max = 255, message = "名称长度不能超过255")
    @ApiModelProperty("标签名称")
    private String tagName;

    @ApiModelProperty("标签类型")
    private TagType type;
}
