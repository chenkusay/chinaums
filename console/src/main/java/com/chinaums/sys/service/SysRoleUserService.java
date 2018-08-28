package com.chinaums.sys.service;


import com.chinaums.base.service.BaseService;
import com.chinaums.sys.dao.SysRoleUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户角色表
 * 
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class SysRoleUserService extends BaseService {

    @Autowired
    private SysRoleUserDao dao;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteBatch(String ids){
		dao.deleteBatch(ids);
	}
}
