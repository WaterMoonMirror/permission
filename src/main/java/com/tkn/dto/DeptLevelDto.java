package com.tkn.dto;

import com.google.common.collect.Lists;
import com.tkn.model.SysDept;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Auther: tkn
 * @Date: 2018/7/21 17:53
 * @Description: 层级类传输
 */
@Data
@ToString
public class DeptLevelDto extends SysDept {

    private List<DeptLevelDto> deptLis= Lists.newArrayList();

    /**
     *  适配器
     * @param sysDept
     * @return
     */
    public static DeptLevelDto adapt(SysDept sysDept){
        DeptLevelDto dto=new DeptLevelDto();
        BeanUtils.copyProperties(sysDept,dto);
        return dto;
    }

}
