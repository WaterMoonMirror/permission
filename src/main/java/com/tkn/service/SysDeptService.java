package com.tkn.service;

import com.google.common.base.Preconditions;
import com.tkn.dao.SysDeptMapper;
import com.tkn.exception.ParamException;
import com.tkn.model.SysDept;
import com.tkn.param.DeptParam;
import com.tkn.utills.BeanValidator;
import com.tkn.utills.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Auther: tkn
 * @Date: 2018/7/21 17:27
 * @Description: 部门业务类
 */
@Service
public class SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    public void save(DeptParam deptParam) {
        BeanValidator.check(deptParam);
        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("同一层级下存在相同的部门名称");
        }
        // 使用构造器添加数据
        SysDept sysDept = SysDept.builder()
                .name(deptParam.getName())
                .seq(deptParam.getSeq())
                .parentId(deptParam.getParentId())
                .remark(deptParam.getRemark()).build();


        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));
        sysDept.setOperateTime(new Date());
        sysDept.setOperateIp("system");// TODO:
        sysDept.setOperator("127.0.0.1");// TODO:
        sysDeptMapper.insertSelective(sysDept);
    }

    public void update(DeptParam deptParam) {
        BeanValidator.check(deptParam);
        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("同一层级下存在相同的部门名称");
        }

        SysDept before = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        Preconditions.checkNotNull(before,"待更新的部门不能为空");
        // 使用构造器添加数据
        SysDept after = SysDept.builder().id(deptParam.getId())
                .parentId(deptParam.getParentId())
                .name(deptParam.getName())
                .seq(deptParam.getSeq())
                .remark(deptParam.getRemark()).build();



        after.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));
        after.setOperateTime(new Date());
        after.setOperateIp("system-update");// TODO:
        after.setOperator("127.0.0.1");// TODO:
        updateWithChild(before,after);
    }


    /**
     *  更新部门下的子部门
     * @param before
     * @param after
     */
    @Transactional
    private void  updateWithChild(SysDept before,SysDept after){

        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (! newLevelPrefix.equals(oldLevelPrefix)){
            // 获取待更改的层级下所以 的部门
            List<SysDept> childDeptList = sysDeptMapper.getChildDeptListByLevel(oldLevelPrefix,before.getId());
            if (CollectionUtils.isNotEmpty(childDeptList)){
                // 循环修改部门的层级
                for (SysDept dept: childDeptList){
                    String level=dept.getLevel();
                    if (dept.getLevel().indexOf(oldLevelPrefix)==0){
                        // 拼装部门新level
                        level=newLevelPrefix+level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                // 批量更新
                sysDeptMapper.batchUpdateLevel(childDeptList);
            }

        }

        sysDeptMapper.updateByPrimaryKey(after);

    }


    /**
     * 判断同级下面是否部门名称相同
     *
     * @param parentId
     * @param deptName
     * @param deptId
     * @return
     */
    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return  sysDeptMapper.countByNameAndParentId(parentId,deptName,deptId)>0;
    }

    /**
     *  获取层级
     * @param deptId
     * @return
     */
    private String getLevel(Integer deptId){

        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept ==null){
            return  null;
        }
        return dept.getLevel();
    }
}
