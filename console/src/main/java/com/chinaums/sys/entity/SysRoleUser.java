package com.chinaums.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.chinaums.base.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 用户角色表
 * 
 * @author 
 */
@Data
@NoArgsConstructor
public class SysRoleUser extends BaseEntity {

	//
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long id;
	// 角色编码
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long roleId;
	// 用户编码
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long userId;
	// 
	private Date createTime;
	// 
	private Date updateTime;
}
