package com.chinaums.sys.dao;

import com.chinaums.base.dao.BaseDao;
import com.chinaums.sys.entity.SysMenu;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单管理
 */
@Repository
public interface SysMenuDao extends BaseDao<SysMenu> {

	PageList<SysMenu> getList(SysMenu sysMenu, PageBounds pageBounds);

    void deleteBatch(@Param("menuIds") String menuIds);

    List<SysMenu> getNotButtonList();
}
