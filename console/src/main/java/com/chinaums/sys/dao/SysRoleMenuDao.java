package com.chinaums.sys.dao;

import com.chinaums.base.dao.BaseDao;
import com.chinaums.sys.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 角色菜单表
 * 
 * @author 
 */
@Repository
public interface SysRoleMenuDao extends BaseDao<SysRoleMenu> {

    void deleteBatch(@Param("ids") String ids);
}
