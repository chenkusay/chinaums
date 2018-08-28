package com.chinaums.sys.service;


import com.chinaums.base.service.BaseService;
import com.chinaums.sys.dao.SysRoleDao;
import com.chinaums.sys.dao.SysRoleMenuDao;
import com.chinaums.sys.entity.SysRole;
import com.chinaums.sys.entity.SysRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 * 
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class SysRoleService extends BaseService {

    @Autowired
    private SysRoleDao dao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteBatch(String ids){
		dao.deleteBatch(ids);
	}

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void save(SysRole sysRole) {
        this.add(sysRole);
        if (sysRole.getMenuIds()!=null){
            for(Long menuId : sysRole.getMenuIds()){
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenu.setRoleId(sysRole.getId());
                sysRoleMenuDao._insert(sysRoleMenu);
            }
        }
    }
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateEdit(SysRole sysRole) {
        //更新角色信息
        this.update(sysRole);
        //删除授权菜单
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        sysRoleMenu.setRoleId(sysRole.getId());
        sysRoleMenuDao._deleteAnd(sysRoleMenu);
        //插入新的授权菜单
        if (sysRole.getMenuIds() != null){
            for(Long menuId : sysRole.getMenuIds()){
                SysRoleMenu param = new SysRoleMenu();
                param.setMenuId(menuId);
                param.setRoleId(sysRole.getId());
                sysRoleMenuDao._insert(param);
            }
        }
    }

    public List<Map<String,String>> getTreeList() {
        return dao.getTreeList();
    }
}
