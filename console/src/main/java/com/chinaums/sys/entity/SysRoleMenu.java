package com.chinaums.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.chinaums.base.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 角色菜单表
 * 
 * @author 
 */
@Data
@NoArgsConstructor
public class SysRoleMenu extends BaseEntity {

	//
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long id;
	// 角色编码
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long roleId;
	// 菜单编码
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long menuId;
	// 创建时间
	private Date createTime;
}
