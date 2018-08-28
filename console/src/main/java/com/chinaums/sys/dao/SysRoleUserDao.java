package com.chinaums.sys.dao;

import com.chinaums.base.dao.BaseDao;
import com.chinaums.sys.entity.SysRoleUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户角色表
 * 
 * @author 
 */
@Repository
public interface SysRoleUserDao extends BaseDao<SysRoleUser> {

    void deleteBatch(@Param("ids") String ids);
}
