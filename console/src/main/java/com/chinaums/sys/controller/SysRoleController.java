package com.chinaums.sys.controller;

import com.chinaums.annotation.SysLogAnnotation;
import com.chinaums.consolebase.controller.AbstractController;
import com.chinaums.sys.entity.SysRole;
import com.chinaums.sys.entity.SysRoleUser;
import com.chinaums.utils.Page;
import com.chinaums.utils.R;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 * 
 * @author 
 */
@RestController
@RequestMapping("sys/role")
public class SysRoleController extends AbstractController {
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:role:list")
	public R list(SysRole sysRole, Page page){
        //查询列表数据
        PageList<SysRole> list = sysRoleService.getListAnd(sysRole, page);
        if (list != null){
            page.setTotalRows(list.getPaginator().getTotalCount());
        }
        page.setList(list);
        return R.ok().put("page", page);
	}
	/**
	 * 角色树
	 */
	@RequestMapping("/treeList")
	@RequiresPermissions("sys:role:list")
	public R list(){
		//查询列表数据
		List<Map<String,String>> list = sysRoleService.getTreeList();
		return R.ok().put("list", list);
	}
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:role:info")
	public R info(@PathVariable("id") Long id){
		SysRole sysRole = sysRoleService.getById(id);
		
		return R.ok().put("sysRole", sysRole);
	}
	/**
	 * 信息
	 */
	@RequestMapping("/grantRoles/{userId}")
	@RequiresPermissions("sys:role:info")
	public R grantRoles(@PathVariable("userId") Long userId){
		SysRoleUser sysRoleUser = new SysRoleUser();
		sysRoleUser.setUserId(userId);
		PageList<SysRoleUser> list = sysRoleUserService.getListAnd(sysRoleUser,new Page());
		return R.ok().put("list", list);
	}
	
	/**
	 * 保存
	 */
    @SysLogAnnotation("保存角色管理")
	@RequestMapping("/save")
	@RequiresPermissions("sys:role:save")
	public R save(@RequestBody SysRole sysRole){
		sysRoleService.save(sysRole);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
    @SysLogAnnotation("修改角色管理")
	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public R update(@RequestBody SysRole sysRole){
		sysRoleService.updateEdit(sysRole);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
    @SysLogAnnotation("删除角色管理")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public R delete(@RequestBody Long[] ids){
		sysRoleService.deleteBatch(StringUtils.join(ids, ","));
		return R.ok();
	}
	
}
