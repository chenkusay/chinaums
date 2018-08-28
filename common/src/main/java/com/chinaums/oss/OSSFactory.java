package com.chinaums.oss;


import com.chinaums.common.service.SysConfigService;
import com.chinaums.utils.ConfigConstant;
import com.chinaums.utils.Constant;
import com.chinaums.utils.SpringContextUtils;

/**
 * 文件上传Factory
 */
public final class OSSFactory {
    private static SysConfigService sysConfigService;

    static {
        OSSFactory.sysConfigService = (SysConfigService) SpringContextUtils.getBean("sysConfigService");
    }

    public static CloudStorageService build(){
        //获取云存储配置信息
        CloudStorageConfig config = sysConfigService.getConfigObject(ConfigConstant.CLOUD_STORAGE_CONFIG_KEY, CloudStorageConfig.class);

        if(config.getType() == Constant.CloudService.QINIU.getValue()){
            return new QiniuCloudStorageService(config);
        }else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
//            return new AliyunCloudStorageService(config);
        }else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
//            return new QcloudCloudStorageService(config);
        }

        return null;
    }

}
