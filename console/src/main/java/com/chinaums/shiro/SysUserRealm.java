package com.chinaums.shiro;


import com.chinaums.sys.entity.SysMenu;
import com.chinaums.sys.entity.SysUser;
import com.chinaums.sys.service.SysMenuService;
import com.chinaums.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 认证
 * 
 * @author rachel.li
 */
public class SysUserRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;
    
    /**
     * 授权(验证权限时调用)
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUser user = (SysUser)principals.getPrimaryPrincipal();
		Long userId = user.getId();
		
		List<String> permsList = null;
		
		//系统管理员，拥有最高权限
		if(userId == 1){
			List<SysMenu> menuList = sysMenuService.getList(null, null);
			permsList = new ArrayList<>(menuList.size());
			for(SysMenu menu : menuList){
				permsList.add(menu.getPerms());
			}
		}else{
			SysMenu sysMenu = new SysMenu();
			sysMenu.setUserId(userId);
			List<SysMenu> menuList = sysMenuService.getList(sysMenu, null);
			permsList = new ArrayList<>(menuList.size());
			for(SysMenu menu : menuList){
				permsList.add(menu.getPerms());
			}
		}

		//用户权限列表
		Set<String> permsSet = new HashSet<String>();
		for(String perms : permsList){
			if(StringUtils.isBlank(perms)){
				continue;
			}
			permsSet.addAll(Arrays.asList(perms.trim().split(",")));
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permsSet);
		return info;
	}

	/**
	 * 认证(登录时调用)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        
        //查询用户信息
        SysUser user = sysUserService.getByName(username);
        
        //账号不存在
        if(user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        
        //密码错误
        if(!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确");
        }
        
        //账号锁定
        if(user.getFrozen() == 0){
        	throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        return info;
	}

}
