package com.chinaums.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import com.chinaums.annotation.NoColumn;
import com.chinaums.base.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


/**
 * 角色管理
 * 
 * @author 
 */
@Data
@NoArgsConstructor
public class SysRole extends BaseEntity {

	// 角色编码
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long id;
	// 角色名称
	private String roleName;
	// 备注
	private String roleRemark;
	// 创建时间
	private Date createTime;

	@NoColumn
	private List<Long> menuIds;
}
