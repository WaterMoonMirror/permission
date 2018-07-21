package com.tkn.utills;

import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: tkn
 * @Date: 2018/7/21 17:13
 * @Description: 获取节点等级工具
 */
public class LevelUtil {

    // 各节点等级分割符
    public final static String SEPARATOR=".";

    // 默认根节点的等级
    public final static String ROOT="0";

    public static String calculateLevel(String parentLevel,Integer parentId){
        // 如果父节点等级为空，则为父节点
        if (StringUtils.isBlank(parentLevel)){
            return ROOT;
        }else {
            // 不为空，则level为父等级.父节点id
            // 0
            // 0.1
            // 0.2
            // 0.1.1
            // ...
            return StringUtils.join(parentLevel,SEPARATOR,parentId);
        }


    }
}
