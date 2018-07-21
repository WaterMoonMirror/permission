package com.tkn.param;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Auther: tkn
 * @Date: 2018/7/21 16:51
 * @Description: 部门参数类
 */
@Data  // 自动添加字段  get set 方法
@ToString // 自动添加tostring方法
public class DeptParam {
    private Integer id;

    @NotBlank(message = "部门名称不能为空")
    @Length(max = 15,min = 2,message = "部门名称长度需要2-15个字")
    private String name;

    private Integer parentId;

    // 排序
    @NotNull(message = "展示顺序不能为空")
    private Integer seq;

    // 备注
    @Length(max = 150,message = "备注长度需要在150字以内")
    private String remark;
}
