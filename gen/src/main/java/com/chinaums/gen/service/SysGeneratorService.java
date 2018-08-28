package com.chinaums.gen.service;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

import com.chinaums.gen.dao.SysGeneratorDao;
import com.chinaums.gen.utils.GenUtils;
import com.chinaums.gen.utils.Page;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Service
public class SysGeneratorService {

	@Autowired
	private SysGeneratorDao dao;

	public PageList<Map<String, Object>> getList(Map<String, Object> map, Page page) {
		if (page == null){
			page = new Page("createTime.desc");
		}
		return dao.getList(map, page.gainPageBounds());
	}

	public byte[] generatorCode(String[] tableNames,String packageName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		for(String tableName : tableNames){
			//查询表信息
			Map<String, String> table = queryTable(tableName);
			//查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			//生成代码
			GenUtils.generatorCode(table, columns, zip,packageName);
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	private List<Map<String,String>> queryColumns(String tableName) {
		return dao.queryColumns(tableName);
	}

	private Map<String,String> queryTable(String tableName) {
		return dao.queryTable(tableName);
	}

}
