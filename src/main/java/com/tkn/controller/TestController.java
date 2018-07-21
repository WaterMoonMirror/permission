package com.tkn.controller;

import com.tkn.common.ApplicationContextHelper;
import com.tkn.common.JsonData;
import com.tkn.dao.SysAclModuleMapper;
import com.tkn.exception.ParamException;
import com.tkn.exception.PermissionException;
import com.tkn.model.SysAclModule;
import com.tkn.param.TestVo;
import com.tkn.utills.BeanValidator;
import com.tkn.utills.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello() {
        log.info("hello");
        throw new PermissionException("test exception");
        // return JsonData.success("hello, permission");
    }

    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVo vo) throws ParamException {
        log.info("validate");
        SysAclModuleMapper moduleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule module = moduleMapper.selectByPrimaryKey(1);
        log.info(JsonMapper.obj2String(module));
        BeanValidator.check(vo);
        return JsonData.success("test validate");
    }


}