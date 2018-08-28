package com.chinaums.base.dao;

import com.chinaums.base.SQLTemplate;
import com.chinaums.base.entity.BaseEntity;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.Map;

/**
* 通用Mapper接口（使用时需继承该接口）
*/
public interface BaseDao<T extends BaseEntity> {

//****************各数据库查询自增主键方法**************************
//    Cloudscape  VALUES IDENTITY_VAL_LOCAL()
//    DB2         VALUES IDENTITY_VAL_LOCAL()
//    Derby       VALUES IDENTITY_VAL_LOCAL()
//    HSQLDB      CALL IDENTITY()
//    MySql       SELECT LAST_INSERT_ID()
//    SqlServer   SELECT SCOPE_IDENTITY()
//    SYBASE      SELECT @@IDENTITY
//    ORACLE      SELECT CUSTOM_SQL.NEXTVAL AS ID FROM DUAL

    // MySQL主键查询语句
    static final String SELECT_INSERT_PRIMARY_KEY = "SELECT LAST_INSERT_ID()";

    /**
     * 通用删除操作
     * @param object
     * @return
     */
    @DeleteProvider(type = SQLTemplate.class, method = "deleteById")
    public Integer _deleteById(T object);

    /**
     * 通用多条件删除操作
     * @param object
     * @return
     */
    @DeleteProvider(type = SQLTemplate.class, method = "deleteAnd")
    public Integer _deleteAnd(T object);

    /**
     * 通用添加操作
     * @param object
     * @return
     */
    @InsertProvider(type = SQLTemplate.class, method = "insert")
//    @SelectKey(before = false, keyProperty = "id", resultType = Long.class, statement = SELECT_INSERT_PRIMARY_KEY)
    public Integer _insert(T object);

    /**
     * 通用更新操作
     * @param object
     * @return
     */
    @UpdateProvider(type = SQLTemplate.class, method = "update")
    public Integer _update(T object);

    /**
     * 通用更新操作(包含空属性)
     * @param object
     * @return
     */
    @UpdateProvider(type = SQLTemplate.class, method = "updateNull")
    public Integer _updateNull(T object);

    /**
     * 通用like多条件and查询操作获取总记录数
     * @param object
     * @return
     */
    @SelectProvider(type = SQLTemplate.class, method = "selectAndLikeCount")
    public Integer _selectAndLikeCount(T object);

    /**
     * 通用id查询操作
     * @param object
     * @return
     */
    @SelectProvider(type = SQLTemplate.class, method = "selectById")
    public Map<String, Object> _selectById(T object);

    /**
     * 通用多条件or查询操作
     * @param object
     * @param page
     * @return
     */
    @SelectProvider(type = SQLTemplate.class, method = "selectOr")
    public PageList<Map<String, Object>> _selectOr(T object, PageBounds page);

    /**
     * 通用多条件and查询操作
     * @param object
     * @param page
     * @return
     */
    @SelectProvider(type = SQLTemplate.class, method = "selectAnd")
    public PageList<Map<String, Object>> _selectAnd(T object, PageBounds page);

    /**
     * 通用like多条件or查询操作
     * @param object
     * @param page
     * @return
     */
    @SelectProvider(type = SQLTemplate.class, method = "selectOrLike")
    public PageList<Map<String, Object>> _selectOrLike(T object, PageBounds page);

    /**
     * 通用like多条件and查询操作
     * @param object
     * @param page
     * @return
     */
    @SelectProvider(type = SQLTemplate.class, method = "selectAndLike")
    public PageList<Map<String, Object>> _selectAndLike(T object, PageBounds page);

}
