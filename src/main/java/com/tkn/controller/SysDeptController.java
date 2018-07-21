package com.tkn.controller;

import com.tkn.common.JsonData;
import com.tkn.dto.DeptLevelDto;
import com.tkn.param.DeptParam;
import com.tkn.service.SysDeptService;
import com.tkn.service.SysTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: tkn
 * @Date: 2018/7/21 17:48
 * @Description:
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping("/save.json")
    public JsonData save(DeptParam deptParam){
        sysDeptService.save(deptParam);
        return JsonData.success();
    }

    @RequestMapping("/tree.json")
    public JsonData getDeptTree(){
        List<DeptLevelDto> dtoList = sysTreeService.deptTree();
        return JsonData.success(dtoList);
    }

    @RequestMapping("/update.json")
    public JsonData update(DeptParam deptParam){
        sysDeptService.update(deptParam);
        return JsonData.success();
    }
}
