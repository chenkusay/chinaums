package com.chinaums.sys.service;


import com.chinaums.base.service.BaseService;
import com.chinaums.sys.dao.SysUserDao;
import com.chinaums.sys.entity.SysRoleUser;
import com.chinaums.sys.entity.SysUser;
import com.chinaums.utils.Page;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class SysUserService extends BaseService {

    @Autowired
    private SysUserDao dao;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    public PageList<SysUser> getList(SysUser sysUser, Page page) {
        return dao.getList(sysUser,page.gainPageBounds());
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int updatePassword(Long userId, String password, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", password);
        map.put("newPassword", newPassword);
        return dao.updatePassword(map);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void save(SysUser user) {
        user.setCreateTime(new Date());
        //sha256加密
        user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        this.add(user);
        addSysRoleUser(user);
    }
    private void addSysRoleUser(SysUser user){
        if(user.getRoleIds() !=null && user.getRoleIds().size()>0){
            for(Long roleId : user.getRoleIds()){
                SysRoleUser sysRoleUser = new SysRoleUser();
                sysRoleUser.setUserId(user.getId());
                sysRoleUser.setRoleId(roleId);
                sysRoleUser.setCreateTime(new Date());
                sysRoleUserService.add(sysRoleUser);
            }
        }
    }
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateEdit(SysUser user) {
        Map<String, Object> dbUser = dao._selectById(user);
        if(StringUtils.isNotBlank(user.getPassword())){
            user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        }else{
            user.setPassword(dbUser.get("password").toString());
        }
        user.setCreateUserId((Long) dbUser.get("createUserId"));
        user.setCreateTime((Date) dbUser.get("createTime"));
        this.update(user);
        //删除已有的角色
        SysRoleUser sysRoleUser = new SysRoleUser();
        sysRoleUser.setUserId(user.getId());
        sysRoleUserService.deleteAnd(sysRoleUser);
        //插入新的角色
        addSysRoleUser(user);
    }

    public SysUser getByName(String username) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        return getObject(sysUser);
    }
    public SysUser getObject(SysUser sysUser){
        List<SysUser> list = getListAnd(sysUser, null);
        return list==null?null:list.get(0);
    }
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteBatch(String userIds) {
        dao.deleteBatch(userIds);
    }
}
