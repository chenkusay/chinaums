package com.chinaums.sys.dao;

import com.chinaums.base.dao.BaseDao;
import com.chinaums.sys.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 * 
 * @author 
 */
@Repository
public interface SysRoleDao extends BaseDao<SysRole> {

    void deleteBatch(@Param("ids") String ids);

    List<Map<String,String>> getTreeList();
}
