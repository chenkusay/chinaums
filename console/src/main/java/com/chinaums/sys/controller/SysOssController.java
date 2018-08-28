package com.chinaums.sys.controller;

import com.alibaba.fastjson.JSON;
import com.chinaums.common.service.SysConfigService;
import com.chinaums.oss.CloudStorageConfig;
import com.chinaums.utils.ConfigConstant;
import com.chinaums.utils.Constant;
import com.chinaums.utils.R;
import com.chinaums.validator.ValidatorUtils;
import com.chinaums.validator.group.QiniuGroup;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件云存储配置
 * 
 * @author rachel.li
 */
@RestController
@RequestMapping("/oss")
public class SysOssController {
    @Autowired
    private SysConfigService sysConfigService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;
	
    /**
     * 云存储配置信息
     */
    @RequestMapping("/config")
    @RequiresPermissions("sys:oss:all")
    public R config(){
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);
        return R.ok().put("config", config);
    }

	/**
	 * 保存云存储配置信息
	 */
	@RequestMapping("/saveConfig")
	@RequiresPermissions("sys:oss:all")
	public R saveConfig(@RequestBody CloudStorageConfig config){
		//校验类型
		ValidatorUtils.validateEntity(config);
		if(config.getType() == Constant.CloudService.QINIU.getValue()){
			//校验七牛数据
			ValidatorUtils.validateEntity(config, QiniuGroup.class);
		}else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
			//校验阿里云数据
//			ValidatorUtils.validateEntity(config, AliyunGroup.class);
		}else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
			//校验腾讯云数据
//			ValidatorUtils.validateEntity(config, QcloudGroup.class);
		}
        sysConfigService.updateValueByKey(KEY, JSON.toJSONString(config));

		return R.ok();
	}
	
}
