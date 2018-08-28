package com.chinaums.common.entity;


import com.chinaums.base.entity.BaseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 系统配置信息
 * 
 * @author rachel.li
 */
@Data
public class SysConfig extends BaseEntity {
	private Long id;
	@NotBlank(message="参数名不能为空")
	private String keyword;
	@NotBlank(message="参数值不能为空")
	private String value; 
	private String remark;

}
