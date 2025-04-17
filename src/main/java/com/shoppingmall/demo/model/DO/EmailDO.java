package com.shoppingmall.demo.model.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
* 
* @TableName user
*/
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("email")
public class EmailDO implements Serializable {

    /**
    * 邮件id
    */
    @NotNull(message="[邮件id]不能为空")
    @ApiModelProperty("邮件id")
    private Long id;

    /**
     * 邮件标题
     */
    @Length(max= 255,message="邮件标题长度不能超过255")
    @ApiModelProperty("邮件标题")
    private String title;


    /**
     * 邮件内容
     */
    @Length(max= 512,message="邮件内容长度不能超过512")
    @ApiModelProperty("邮件内容")
    private String content;

}
