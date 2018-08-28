package com.chinaums.interceptors;


import com.chinaums.redis.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 小程序拦截器
 */
public class AppInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(AppInterceptor.class);
    @Autowired
    RedisUtil redisUtil;

    /**
     * 执行完控制器后调用，即离开时
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception e) throws Exception {
        logger.info("className--->" + arg2);
        logger.info("request--->" + request);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isNotBlank(token)) {
            /*Shop shop = shopService.getByOriginalId(token);
            if (shop == null || shop.getFrozen()== Constant.FRONZEN){
                sendResponse(response, "店铺不存在或已无法使用");
                return false;
            }
            request.setAttribute("shopId", shop.getId());
            IgnoreUTokenAuth annotation;
            if(handler instanceof HandlerMethod) {
                annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreUTokenAuth.class);
            }else{
                return true;
            }
            //如果有@IgnoreUTokenAuth注解，则不验证utoken
            if(annotation != null){
                logger.info("utoken pass： {}", ((HandlerMethod) handler).getMethod());
                return true;
            }
            // 本地测试，关掉
            String utoken = request.getHeader("utoken");
            if (StringUtils.isBlank(utoken)){
                sendResponse(response, "用户不存在");
                return false;
            }
            String uid = (String) redisUtil.get(utoken);
//            String uid = "98194411776";
            if (uid == null){
                sendResponse(response, "用户凭证已失效");
                return false;
            }
            request.setAttribute("uid", uid);*/
            return true;
        } else {
            sendResponse(response, "商户号不存在");
            return false;
        }
    }
    private void sendResponse(HttpServletResponse response, String errorMsg){
        response.setStatus(403);
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
//        response.setHeader("WWW-authenticate", "Basic Realm=\"商户号不存在\"");
        logger.debug("================================================================");
        logger.debug("state        : " + "403");
        logger.debug("error        : " + errorMsg);
        logger.debug("================================================================");
    }
}
