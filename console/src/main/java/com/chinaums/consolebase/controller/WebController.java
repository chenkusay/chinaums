package com.chinaums.consolebase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面视图
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class WebController {

	@RequestMapping("")
	public String page(){
		return "redirect:/index.html";
	}
	@RequestMapping("/{url}.html")
	public String page(@PathVariable("url") String url){
		return "/" + url + ".html";
	}

	@RequestMapping("/{module}/{url}.html")
	public String page(@PathVariable("module") String module, @PathVariable("url") String url){
		return "/"+module+"/" + url + ".html";
	}

}
