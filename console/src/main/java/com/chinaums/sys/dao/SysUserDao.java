package com.chinaums.sys.dao;

import com.chinaums.base.dao.BaseDao;
import com.chinaums.sys.entity.SysUser;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface SysUserDao extends BaseDao<SysUser> {

    int updatePassword(Map<String, Object> map);

    PageList<SysUser> getList(SysUser sysUser, PageBounds pageBounds);

    void deleteBatch(@Param("ids") String userIds);
}
