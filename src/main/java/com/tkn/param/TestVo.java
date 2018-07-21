package com.tkn.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class TestVo {

    /**
     *  字符串校验注解
     */
    @NotBlank
    private String msg;

    /**
     *  对象类型的校验注解
     */
    @NotNull(message = "id不可以为空")
    @Max(value = 10, message = "id 不能大于10")
    @Min(value = 0, message = "id 至少大于等于0")
    private Integer id;

    /**
     *  数据类型的校验 注解
     */
    @NotEmpty
    private List<String> str;
}