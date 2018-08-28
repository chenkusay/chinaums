package com.chinaums.sys.controller;


import com.chinaums.annotation.SysLogAnnotation;
import com.chinaums.sys.entity.SysMenu;
import com.chinaums.consolebase.controller.AbstractController;
import com.chinaums.sys.entity.SysRoleMenu;
import com.chinaums.utils.Constant;
import com.chinaums.utils.Page;
import com.chinaums.utils.R;
import com.chinaums.utils.RRException;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 系统菜单
 *
 * @author rachel.li
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {

	/**
	 * 所有菜单列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public R list(SysMenu sysMenu, Page page){
		//查询列表数据
		if (StringUtils.isBlank(page.getSidx())){
			page.setSortRule("sort.asc,type.asc");
		}
		PageList<SysMenu> list = sysMenuService.getList(sysMenu, page);
		if (list != null){
			page.setTotalRows(list.getPaginator().getTotalCount());
		}
		page.setList(list);
		return R.ok().put("page", page);
	}

	/**
	 * 用户菜单列表
	 */
	@RequestMapping("/user")
	public R user(){
		List<SysMenu> menuList = sysMenuService.getListByUser(getUserId());
		return R.ok().put("menuList", menuList);
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public R select(){
		//查询列表数据
		List<SysMenu> list = sysMenuService.getNotButtonList();
		//添加顶级菜单
		SysMenu root = new SysMenu();
		root.setId(0L);
		root.setName("一级菜单");
		root.setPid(-1L);
		list.add(root);
		return R.ok().put("menuList", list);
	}
	/**
	 * 选择菜单&按钮(授权)
	 */
	@RequestMapping("/grantMenus")
	@RequiresPermissions("sys:menu:select")
	public R grantMenus(){

		//查询列表数据
		Page page = new Page();
		page.setSortRule("sort.asc,type.asc");
		SysMenu sysMenu = new SysMenu();
		PageList<SysMenu> list = sysMenuService.getListAnd(sysMenu, page);
		//添加顶级菜单
		sysMenu.setId(0L);
		sysMenu.setName("一级菜单");
		sysMenu.setPid(-1L);
		list.add(sysMenu);
		return R.ok().put("menuList", list);
	}
	/**
	 * 用户菜单列表
	 */
	@RequestMapping("/grantMenus/{roleId}")
	public R grantMenusIdByRoleId(@PathVariable("roleId") Long roleId){
		SysRoleMenu sysRoleMenu = new SysRoleMenu();
		sysRoleMenu.setRoleId(roleId);
		List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.getListAnd(sysRoleMenu,new Page());
		return R.ok().put("sysRoleMenuList", sysRoleMenuList);
	}
	/**
	 * 菜单信息
	 */
	@RequestMapping("/info/{menuId}")
	@RequiresPermissions("sys:menu:info")
	public R info(@PathVariable("menuId") Long menuId){
		SysMenu menu = sysMenuService.getById(menuId);
		return R.ok().put("menu", menu);
	}

	/**
	 * 保存
	 */
	@SysLogAnnotation("保存菜单")
	@RequestMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public R save(@RequestBody SysMenu menu){
		//数据校验
		verifyForm(menu);
		sysMenuService.add(menu);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@SysLogAnnotation("修改菜单")
	@RequestMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public R update(@RequestBody SysMenu menu){
		//数据校验
		verifyForm(menu);
		sysMenuService.update(menu);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLogAnnotation("删除菜单")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public R delete(@RequestBody Long[] menuIds){
//		for(Long menuId : menuIds){
//			if(menuId.longValue() <= 30){
//				return R.error("系统菜单，不能删除");
//			}
//		}
		sysMenuService.deleteBatch(StringUtils.join(menuIds, ","));
		return R.ok();
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenu menu){
		if(StringUtils.isBlank(menu.getName())){
			throw new RRException("菜单名称不能为空");
		}

		if(menu.getPid() == null){
			throw new RRException("上级菜单不能为空");
		}

		//菜单
		if(menu.getType() == Constant.MenuType.MENU.getValue()){
			if(StringUtils.isBlank(menu.getUrl())){
				throw new RRException("菜单URL不能为空");
			}
		}

		//上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if(menu.getPid() != 0){
			SysMenu parentMenu = sysMenuService.getById(menu.getPid());
			parentType = parentMenu.getType();
		}

		//目录、菜单
		if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue()){
				throw new RRException("上级菜单只能为目录类型");
			}
			return ;
		}

		//按钮
		if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue()){
				throw new RRException("上级菜单只能为菜单类型");
			}
			return ;
		}
	}
}
