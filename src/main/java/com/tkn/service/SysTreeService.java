package com.tkn.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.tkn.dao.SysDeptMapper;
import com.tkn.dto.DeptLevelDto;
import com.tkn.model.SysDept;
import com.tkn.utills.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Auther: tkn
 * @Date: 2018/7/21 18:03
 * @Description: 层级处理类
 */
@Service
public class SysTreeService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 获取部门树
     *
     * @return
     */
    public List<DeptLevelDto> deptTree() {
        List<SysDept> allDept = sysDeptMapper.getAllDept();
        List<DeptLevelDto> deptLevelDtoList = Lists.newArrayList();

        for (SysDept sysDept : allDept) {
            deptLevelDtoList.add(DeptLevelDto.adapt(sysDept));
        }
        return deptListToTree(deptLevelDtoList);

    }

    private List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptList) {
        if (CollectionUtils.isEmpty(deptList)) {
            return Lists.newArrayList();
        }
        // level -> [dept1, dept2, ...] Map<String, List<Object>>
        /**
         * value是一个集合,可以直接通过key添加元素
         *
         *
         *  public boolean put(@Nullable K key, @Nullable V value) {
         *         Collection<V> collection = (Collection)this.map.get(key);
         *         if (collection == null) {
         *             collection = this.createCollection(key);
         *             if (collection.add(value)) {
         *                 ++this.totalSize;
         *                 this.map.put(key, collection);
         *                 return true;
         *             } else {
         *                 throw new AssertionError("New Collection violated the Collection spec");
         *             }
         *         } else if (collection.add(value)) {
         *             ++this.totalSize;
         *             return true;
         *         } else {
         *             return false;
         *         }
         *     }
         *
         */
        Multimap<String, DeptLevelDto> levelDtoMultiMap = ArrayListMultimap.create();

        List<DeptLevelDto> rootList=Lists.newArrayList();

        //  添加levelDtoMultiMap ,获取全部rootList
        for (DeptLevelDto deptLevelDto :deptList){
            levelDtoMultiMap.put(deptLevelDto.getLevel(),deptLevelDto);
            if (LevelUtil.ROOT.equals(deptLevelDto.getLevel())){
                rootList.add(deptLevelDto);
            }


        }

        // 按照seq从小到大排列
        Collections.sort(rootList, new Comparator<DeptLevelDto>() {

            @Override
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                return o1.getSeq()-o2.getSeq();
            }
        });

        // 递归生成树
          TransformDepTree(rootList,LevelUtil.ROOT,levelDtoMultiMap);
          return rootList;


    }

    /**
     *  递归生成树
     * @param deptLevelDtos 根层级集合
     * @param level 根层级level
     * @param levelDtoMultiMap 部门集合
     *
     *    level:0, 0, all 0->0.1,0.2
     *    level:0.1
     *    level:0.2
     */
    private void TransformDepTree(List<DeptLevelDto> deptLevelDtos,String level,
                                  Multimap<String, DeptLevelDto> levelDtoMultiMap){
        // 从root开始遍历
        for (int i = 0; i <deptLevelDtos.size(); i++) {

            // 遍历当前层级数据
            DeptLevelDto deptLevelDto = deptLevelDtos.get(i);
            // 处理当前层数据
            String nextLevel = LevelUtil.calculateLevel(deptLevelDto.getLevel(), deptLevelDto.getId());
            // 处理下一层
            List<DeptLevelDto> deptLevelDtoList=(List<DeptLevelDto>)levelDtoMultiMap.get(nextLevel);

            if (CollectionUtils.isNotEmpty(deptLevelDtoList)){
                // 排序
                Collections.sort(deptLevelDtoList,deptSeqComparator );
                // 设置下一层
                deptLevelDto.setDeptLis(deptLevelDtoList);
                // 进入到下一层处理
                TransformDepTree(deptLevelDtoList,nextLevel,levelDtoMultiMap);
            }
        }

    }

    private Comparator<DeptLevelDto> deptSeqComparator =new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq()-o2.getSeq();
        }
    };
}
