package com.chinaums.gen.controller;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.chinaums.gen.service.SysGeneratorService;
import com.chinaums.gen.utils.Page;
import com.chinaums.gen.utils.R;
import com.chinaums.gen.xss.XssHttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 代码生成器
 * 
 * @author rachel.li
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {

	@Autowired
	private SysGeneratorService sysGeneratorService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params, Page page){
		//查询列表数据
		PageList<Map<String, Object>> list = sysGeneratorService.getList(params, page);
		if (list != null){
			page.setTotalRows(list.getPaginator().getTotalCount());
		}
		page.setList(list);
		return R.ok().put("page", page);
	}
	
	/**
	 * 生成代码
	 */
	@RequestMapping("/code")
	public void code(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String[] tableNames = new String[]{};
		//获取表名，不进行xss过滤
		HttpServletRequest orgRequest = XssHttpServletRequestWrapper.getOrgRequest(request);
		String tables = orgRequest.getParameter("tables");
		String packageName = orgRequest.getParameter("packageName");
		tableNames = JSON.parseArray(tables).toArray(tableNames);

		byte[] data = sysGeneratorService.generatorCode(tableNames,packageName);

		response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"chinaums.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
	}
}
