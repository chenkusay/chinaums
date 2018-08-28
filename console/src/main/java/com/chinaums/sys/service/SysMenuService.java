package com.chinaums.sys.service;


import com.chinaums.base.service.BaseService;
import com.chinaums.sys.dao.SysMenuDao;
import com.chinaums.sys.entity.SysMenu;
import com.chinaums.utils.Constant;
import com.chinaums.utils.Page;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SysMenuService extends BaseService {

    @Autowired
    private SysMenuDao sysMenuDao;

    public PageList<SysMenu> getList(SysMenu sysMenu, Page page){
        if (page == null){
            page = new Page("sort.asc");
        }
        PageList<SysMenu> list = null;
        try {
            list = sysMenuDao.getList(sysMenu, page.gainPageBounds());
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public List<SysMenu> getNotButtonList() {
        return sysMenuDao.getNotButtonList();
    }

    public List<SysMenu> getListByUser(Long userId) {
        // 系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            //查询根菜单列表
            PageList<SysMenu> menuList = getChildList(0L,null, null, null);
            //递归获取子菜单
            getMenuTreeList(menuList,null, null);
            return menuList;
        }else {
            //查询根菜单列表
            PageList<SysMenu> menuList = getChildList(0L,userId, null, null);
            //递归获取子菜单
            getMenuTreeList(menuList, null,userId);
            return menuList;
        }
    }

    public PageList<SysMenu> getChildList(Long pid,Long userId, String menuIds, Page page){
        SysMenu sysMenu = new SysMenu();
        sysMenu.setPid(pid);
        sysMenu.setMenuIds(menuIds);
        sysMenu.setUserId(userId);
        return getList(sysMenu, page);
    }

    /**
     * 递归
     */
    private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, String menuIds,Long userId){
        List<SysMenu> subMenuList = new ArrayList();
        for(SysMenu entity : menuList){
            if(entity.getType() == Constant.MenuType.CATALOG.getValue()){//目录
                entity.setList(getMenuTreeList(getChildList(entity.getId(),userId, menuIds, null), menuIds,userId));
            }
            entity.setIcon(entity.getIcon()+ (entity.getIconColor()==null?"":(" "+entity.getIconColor())));
            subMenuList.add(entity);
        }
        return subMenuList;
    }

    /**
     * 级联删除
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteBatch(String menuIds) {
        sysMenuDao.deleteBatch(menuIds);
    }

}
