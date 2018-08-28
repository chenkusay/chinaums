package com.chinaums.common.dao;


import com.chinaums.base.dao.BaseDao;
import com.chinaums.common.entity.SysConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 系统配置信息
 * 
 * @author rachel.li
 */
@Repository
public interface SysConfigDao extends BaseDao<SysConfig> {
	
	/**
	 * 根据key，查询value
	 */
	String getByKey(@Param("key") String key);
	
	/**
	 * 根据key，更新value
	 */
	int updateValueByKey(@Param("key") String key, @Param("value") String value);

	void deleteBatch(@Param("ids") String ids);
}
