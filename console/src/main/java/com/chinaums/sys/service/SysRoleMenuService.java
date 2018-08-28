package com.chinaums.sys.service;

import com.chinaums.base.service.BaseService;
import com.chinaums.sys.dao.SysRoleMenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色菜单表
 * 
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class SysRoleMenuService extends BaseService {

    @Autowired
    private SysRoleMenuDao dao;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteBatch(String ids){
		dao.deleteBatch(ids);
	}
}
