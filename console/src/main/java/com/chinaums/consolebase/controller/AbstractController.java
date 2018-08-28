package com.chinaums.consolebase.controller;



import com.chinaums.common.service.SysConfigService;
import com.chinaums.shiro.ShiroUtils;
import com.chinaums.sys.entity.SysUser;
import com.chinaums.sys.service.*;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controller公共组件
 *
 * @author rachel.li
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected SysUserService sysUserService;
	@Autowired
	protected SysMenuService sysMenuService;
	@Autowired
	protected SysRoleService sysRoleService;
	@Autowired
	protected SysRoleMenuService sysRoleMenuService;
	@Autowired
	protected SysRoleUserService sysRoleUserService;
	@Autowired
	protected SysConfigService sysConfigService;

	protected SysUser getUser() {
		return (SysUser) SecurityUtils.getSubject().getPrincipal();
	}
	protected Long getUserId() {
		return getUser().getId();
	}

}
