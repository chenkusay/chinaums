package com.chinaums.sys.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.chinaums.annotation.NoColumn;
import com.chinaums.base.entity.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 菜单管理
 */
@Data
public class SysMenu extends BaseEntity {

	/**
	 * 菜单ID
	 */
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long id;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long pid;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单URL
	 */
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	private String perms;

	/**
	 * 类型     0：目录   1：菜单   2：按钮
	 */
	private Integer type;

	/**
	 * 菜单图标
	 */
	private String icon;
	/**
	 * 图标颜色
	 */
	private String iconColor;

	/**
	 * 排序
	 */
	private Integer sort;

	@NoColumn
	private Long userId;//用户
	
	@NoColumn
	private String pname;
	@NoColumn
	private String menuIds; // 菜单id集合："1,2,3"

	@NoColumn
	private List<SysMenu> list; // 子菜单
}
