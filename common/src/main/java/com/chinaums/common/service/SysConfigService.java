package com.chinaums.common.service;

import com.alibaba.fastjson.JSON;
import com.chinaums.base.service.BaseService;
import com.chinaums.common.dao.SysConfigDao;
import com.chinaums.utils.RRException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统配置信息
 */
@Service
@Transactional(readOnly = true)
public class SysConfigService extends BaseService {

    @Autowired
    private SysConfigDao dao;

    public void deleteBatch(String ids) {
        dao.deleteBatch(ids);
    }

    public String getValue(String key, String defaultValue) {
        String value = dao.getByKey(key);
        if(StringUtils.isBlank(value)){
            return defaultValue;
        }
        return value;
    }

    public <T> T getConfigObject(String key, Class<T> clazz) {
        String value = getValue(key, null);
        if(StringUtils.isNotBlank(value)){
            return JSON.parseObject(value, clazz);
        }
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RRException("获取参数失败");
        }
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateValueByKey(String key, String value) {
        dao.updateValueByKey(key, value);
    }
}
