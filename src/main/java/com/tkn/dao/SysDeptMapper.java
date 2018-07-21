package com.tkn.dao;

import com.google.common.collect.Lists;
import com.tkn.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    /**
     *  获取全部部门
     * @return
     */
    List<SysDept> getAllDept();

    /**
     *  获取所以该层级下的所以部门
     * @param level
     * @return
     */
    List<SysDept> getChildDeptListByLevel(@Param("level") String level,@Param("id") Integer id);

    /**
     *  批量更新部门Level
     * @param depts
     */
    void batchUpdateLevel(@Param("depts") List<SysDept> depts);

    /**
     *  检查同级部门是否名字相同
     * @param parentId
     * @param deptName
     * @param deptId
     */
    int countByNameAndParentId(@Param("parentId") Integer parentId,@Param("deptName") String deptName,
                                @Param("deptId") Integer deptId);
}