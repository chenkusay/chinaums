package com.chinaums.gen.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * 
 * @author rachel.li
 */
@Repository
public interface SysGeneratorDao {

	PageList<Map<String, Object>> getList(Map<String, Object> map, PageBounds pageBounds);

    Map<String,String> queryTable(String tableName);

    List<Map<String,String>> queryColumns(String tableName);
}
