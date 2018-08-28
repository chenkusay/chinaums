package com.chinaums.sys.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.chinaums.annotation.NoColumn;
import com.chinaums.base.entity.BaseEntity;
import com.chinaums.validator.group.AddGroup;
import com.chinaums.validator.group.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.List;

/**
 * 系统用户
 */
@Data
public class SysUser extends BaseEntity {

	/**
	 * 用户ID
	 */
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long id;

	/**
	 * 用户名
	 */
	@NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message="密码不能为空", groups = AddGroup.class)
	private transient String password;

	/**
	 * 邮箱
	 */
	@NotBlank(message="邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer frozen;
	
	/**
	 * 创建者ID
	 */
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long createUserId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建人名称
	 */
	@NoColumn
	private String createUserName;
	/**
	 * 用户角色
	 */
	@NoColumn
	private List<Long> roleIds;

	@NoColumn
	private String newPassword;
}
