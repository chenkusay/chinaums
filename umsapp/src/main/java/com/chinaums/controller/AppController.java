package com.chinaums.controller;


import com.chinaums.annotation.IgnoreUTokenAuth;
import com.chinaums.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 小程序入口
 */
@RestController
@RequestMapping(value = "app")
public class AppController{

    /**
     * 信息
     */
    @IgnoreUTokenAuth
    @RequestMapping(value = "/shop/info", produces = "application/json")
    public R getShopInfo(HttpServletRequest request){
        return R.ok().put("shop", "xxxxx");
    }


}
