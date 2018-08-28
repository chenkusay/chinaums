package com.chinaums.base.service;

import com.chinaums.base.dao.BaseDao;
import com.chinaums.base.entity.BaseEntity;
import com.chinaums.common.ParamConstants;
import com.chinaums.utils.BeanUtils;
import com.chinaums.utils.ClassUtils;
import com.chinaums.utils.Page;
import com.chinaums.utils.SpringContextUtils;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@Transactional(readOnly = true)
public class BaseService {

    /**
     * 类名
     */
    private String serviceClassName = ClassUtils.getClassName(this.getClass());

    /**
     * 根据id获取对象
     * @param id
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> E getById(Long id){

        if (id == null || id <= 0){
            return null;
        }
        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        // 通过反射创建实体
        E entity = (E) ClassUtils.createInstance(ClassUtils.getWorkPackageName(this.getClass()) + ".entity." + entityName);
        // 设置id
        ClassUtils.setAttributeValue(entity, ParamConstants.PRIMARY_KEY.toString(), id);

        return BeanUtils.changeToBean(dao._selectById(entity),entity.getClass());
    }

    /**
     * 根据id获取对象
     * @param id
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> Map<String, Object> getByIdMap(Integer id){

        if (id == null || id <= 0){
            return null;
        }
        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        // 通过反射创建实体
        E entity = (E) ClassUtils.createInstance(ClassUtils.getWorkPackageName(this.getClass()) + ".entity." + entityName);
        // 设置id
        ClassUtils.setAttributeValue(entity, ParamConstants.PRIMARY_KEY.toString(), id);

        return dao._selectById(entity);
    }

    /**
     * 根据id删除对象
     * @param id
     * @param <E>
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public <E extends BaseEntity> Boolean deleteById(Long id){
        if (id == null || id <= 0){
            return null;
        }
        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        // 通过反射创建实体
        E entity = (E) ClassUtils.createInstance(ClassUtils.getWorkPackageName(this.getClass()) + ".entity." + entityName);
        // 设置id
        ClassUtils.setAttributeValue(entity, ParamConstants.PRIMARY_KEY.toString(), id);

        return dao._deleteById(entity) > 0 ? true :  false;
    }

    /**
     * 根据id删除对象
     * @param e
     * @param <E>
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public <E extends BaseEntity> Boolean deleteAnd(E e){

        if (e == null){
            return false;
        }

        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        return dao._deleteAnd(e) > 0 ? true :  false;
    }

    /**
     * 插入对象 返回id
     * @param e
     * @param <E>
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public <E extends BaseEntity> Long add(E e){
        if (e == null){
            return 0L;
        }
        ClassUtils.setAttributeValue(e,ParamConstants.PRIMARY_KEY,BaseEntity.generateId());
        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);
        dao._insert(e);
        return (Long) ClassUtils.getAttributeValue(e, ParamConstants.PRIMARY_KEY.toString());
    }

    /**
     * 获取总记录数
     * @param e
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> Integer getAndLikeCount(E e){
        if (e == null){
            return 0;
        }
        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        return dao._selectAndLikeCount(e);
    }

    /**
     * 更新对象
     * @param e
     * @param <E>
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public <E extends BaseEntity> Boolean update(E e){
        if (e == null || (Long) ClassUtils.getAttributeValue(e, ParamConstants.PRIMARY_KEY.toString()) == null || (Long) ClassUtils.getAttributeValue(e, ParamConstants.PRIMARY_KEY.toString()) <= 0){
            return false;
        }
        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        return dao._update(e) > 0 ? true :  false;
    }

    /**
     * 查询 And
     * @param e
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> PageList<E> getListAnd(E e, Page page){

        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        if (e == null ){
            e = (E) ClassUtils.createInstance(ClassUtils.getWorkPackageName(this.getClass()) + ".entity." + entityName);
        }
        if (page == null){
            page = new Page();
        }

        return BeanUtils.changeToBeanList(dao._selectAnd(e, page.gainPageBounds()), e.getClass());
    }

    /**
     * 查询 And
     * @param e
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> PageList<Map<String, Object>> getListAndMap(E e, Page page){

        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        if (e == null ){
            e = (E) ClassUtils.createInstance(ClassUtils.getWorkPackageName(this.getClass()) + ".entity." + entityName);
        }
        if (page == null){
            page = new Page();
        }

        return dao._selectAnd(e, page.gainPageBounds());
    }

    /**
     * 模糊查询 And
     * @param e
     * @param page
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> PageList<E> getListAndLike(E e, Page page){

        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        if (e == null ){
            e = (E) ClassUtils.createInstance(ClassUtils.getWorkPackageName(this.getClass()) + ".entity." + entityName);
        }
        if (page == null){
            page = new Page();
        }

        return BeanUtils.changeToBeanList(dao._selectAndLike(e, page.gainPageBounds()), e.getClass());
    }

    /**
     * 模糊查询 And
     * @param e
     * @param page
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> PageList<Map<String, Object>> getListAndLikeMap(E e, Page page){

        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        if (e == null ){
            e = (E) ClassUtils.createInstance(ClassUtils.getWorkPackageName(this.getClass()) + ".entity." + entityName);
        }
        if (page == null){
            page = new Page();
        }

        return dao._selectAndLike(e, page.gainPageBounds());
    }

    /**
     * 查询 Or
     * @param e
     * @param page
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> PageList<E> getListOr(E e, Page page){

        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        if (e == null ){
            e = (E) ClassUtils.createInstance(ClassUtils.getWorkPackageName(this.getClass()) + ".entity." + entityName);
        }
        if (page == null){
            page = new Page();
        }

        return BeanUtils.changeToBeanList(dao._selectOr(e, page.gainPageBounds()), e.getClass());
    }

    /**
     * 查询 Or
     * @param e
     * @param page
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> PageList<Map<String, Object>> getListOrMap(E e, Page page){

        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        if (e == null ){
            e = (E) ClassUtils.createInstance(ClassUtils.getWorkPackageName(this.getClass()) + ".entity." + entityName);
        }
        if (page == null){
            page = new Page();
        }

        return dao._selectOr(e, page.gainPageBounds());
    }

    /**
     * 模糊查询 Or
     * @param e
     * @param page
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> PageList<E> getListOrLike(E e, Page page){

        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        if (e == null ){
            e = (E) ClassUtils.createInstance(ClassUtils.getWorkPackageName(this.getClass()) + ".entity." + entityName);
        }
        if (page == null){
            page = new Page();
        }

        return BeanUtils.changeToBeanList(dao._selectOrLike(e, page.gainPageBounds()), e.getClass());
    }

    /**
     * 模糊查询 Or
     * @param e
     * @param page
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> PageList<Map<String, Object>> getListOrLikeMap(E e, Page page){

        String entityName = replaceServicePrefix(serviceClassName, ParamConstants.SERVICE_PREFIX);
        String daoName = ClassUtils.lowerCaseFirst(entityName) + "Dao";
        BaseDao<E> dao = (BaseDao<E>) SpringContextUtils.getBean(daoName);

        if (e == null ){
            e = (E) ClassUtils.createInstance(ClassUtils.getWorkPackageName(this.getClass()) + ".entity." + entityName);
        }
        if (page == null){
            page = new Page();
        }

        return dao._selectOrLike(e, page.gainPageBounds());
    }

    /**
     * 去除service后缀
     * @param serviceName
     * @param prefix
     * @return
     */
    private static String replaceServicePrefix(String serviceName, String prefix){

        Integer lastIndex = serviceName.lastIndexOf(prefix);

        String name = serviceName.substring(0, lastIndex);

        return name;
    }
}