package com.chinaums.sys.controller;


import com.chinaums.annotation.SysLogAnnotation;
import com.chinaums.consolebase.controller.AbstractController;
import com.chinaums.shiro.ShiroUtils;
import com.chinaums.sys.entity.SysUser;
import com.chinaums.utils.Page;
import com.chinaums.utils.R;
import com.chinaums.validator.Assert;
import com.chinaums.validator.ValidatorUtils;
import com.chinaums.validator.group.AddGroup;
import com.chinaums.validator.group.UpdateGroup;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户
 * 
 * @author rachel.li
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(SysUser sysUser, Page page){
		//查询列表数据
		PageList<SysUser> list = sysUserService.getList(sysUser, page);
		if (list != null){
			page.setTotalRows(list.getPaginator().getTotalCount());
		}
		page.setList(list);
		return R.ok().put("page", page);
	}
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
	}

	/**
	 * 修改登录用户密码
	 */
	@SysLogAnnotation("修改密码")
	@RequestMapping("/password")
	public R password(@RequestBody SysUser sysUser){
		String password=sysUser.getPassword();
		String newPassword=sysUser.getNewPassword();
		Assert.isBlank(newPassword, "新密码不为能空");
		//sha256加密
		password = new Sha256Hash(password).toHex();
		//sha256加密
		newPassword = new Sha256Hash(newPassword).toHex();
		//更新密码
		int count = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(count == 0){
			return R.error("原密码不正确");
		}
		//退出
		ShiroUtils.logout();
		return R.ok();
	}
	
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUser user = sysUserService.getById(userId);
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLogAnnotation("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUser user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		user.setCreateUserId(getUserId());
		sysUserService.save(user);
		return R.ok();
	}
	/**
	 * 修改用户
	 */
	@SysLogAnnotation("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUser user){
		if(user.getId().equals(1L)){
			return R.error("系统管理员不能修改");
		}
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		sysUserService.updateEdit(user);
		return R.ok();
	}
	/**
	 * 删除用户
	 */
	@SysLogAnnotation("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		sysUserService.deleteBatch(StringUtils.join(userIds, ","));
		return R.ok();
	}
}
