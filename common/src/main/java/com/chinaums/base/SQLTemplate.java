package com.chinaums.base;

import com.chinaums.base.entity.BaseEntity;
import com.chinaums.utils.ClassUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * 语句模板类
 */
@Component
public class SQLTemplate<T extends BaseEntity> {

//    private static final Logger logger = Logger.getLogger(SQLTemplate.class);

    /**
     * 删除语句创建
     *
     * @param object
     * @return
     */
    public String deleteById(T object) {
        // 开始创建sql
        BEGIN();
        DELETE_FROM(object.gainTableName());
        WHERE(object.gainPrimaryKey() + "=#{" + object.gainPrimaryKey() + "}");

        String SQL = SQL();
//        logger.debug("delete SQL:" + SQL);
//        System.out.println("delete SQL:" + SQL);
        return SQL;
    }

    /**
     * 删除语句创建
     *
     * @param object
     * @return
     */
    public String deleteAnd(T object) {
        // 获取属性名集合
        Set<String> attrSet = object.gainAttributesNameSet();
        Map<String, String> attrMap = object.gainAttributesToDatabaseMap(attrSet);
        // 开始创建sql
        BEGIN();
        DELETE_FROM(object.gainTableName());
        for (String attrName : attrSet) {
            if (!object.isNull(attrName)) {
                WHERE(attrMap.get(attrName) + "=#{" + attrName + "}");
            }
        }

        String SQL = SQL();
//        logger.debug("delete SQL:" + SQL);
//        System.out.println("delete SQL:" + SQL);
        return SQL;
    }

    /**
     * 插入语句生成
     *
     * @param object
     * @return
     */
    public String insert(T object) {
        // 获取属性名集合
        Set<String> attrSet = object.gainAttributesNameSet();
        Map<String, String> attrMap = object.gainAttributesToDatabaseMap(attrSet);

        BEGIN();
        INSERT_INTO(object.gainTableName());
        for (String attrName : attrSet)
            if (!object.isNull(attrName)) {
                VALUES(attrMap.get(attrName), "#{" + attrName + "}");
            }else {
                if(object.gainPrimaryKey().equals(attrName)){
                    VALUES(attrMap.get(attrName), object.generateId().toString());
                }
            }
        String SQL = SQL();
//        logger.debug("insert SQL:" + SQL);
//        System.out.println("insert SQL:" + SQL);
        return SQL;
    }

    /**
     * 更新语句生成
     *
     * @param object
     * @return
     */
    public String update(T object) {
        // 获取属性名集合
        Set<String> attrSet = object.gainAttributesNameSet();
        Map<String, String> attrMap = object.gainAttributesToDatabaseMap(attrSet);

        BEGIN();
        UPDATE(object.gainTableName());
        for (String attrName : attrSet)
        if (!object.isNull(attrName) && !object.gainPrimaryKey().equals(attrName)) {
            SET(attrMap.get(attrName) + "=#{" + attrName + "}");
        }
        WHERE(object.gainPrimaryKey() + "=#{" + object.gainPrimaryKey() + "}");
        // 空id判断
        if (object.isNull(object.gainPrimaryKey())) {
            throw new RuntimeException("Entity id is null");
        }

        String SQL = SQL();

        if (!SQL.contains("SET")) {
            BEGIN();
            UPDATE(object.gainTableName());
            for (String attrName : attrSet) {
                if (!object.isNull(attrName)) {
                    SET(attrMap.get(attrName) + "=#{" + attrName + "}");
                }
            }
            if (!object.isNull(object.gainPrimaryKey())){
                WHERE(object.gainPrimaryKey() + "=#{" + object.gainPrimaryKey() + "}");
            }
            SQL = SQL();
        }

//        logger.debug("update SQL:" + SQL);
//        System.out.println("update SQL:" + SQL);
        return SQL;
    }


    /**
     * 更新语句生成(包含空字符串)
     *
     * @param object
     * @return
     */
    public String updateNull(T object) {
        // 获取属性名集合
        Set<String> attrSet = object.gainAttributesNameSet();
        Map<String, String> attrMap = object.gainAttributesToDatabaseMap(attrSet);

        BEGIN();
        UPDATE(object.gainTableName());
        for (String attrName : attrSet) {
            if (!object.gainPrimaryKey().equals(attrName)) {
                SET(attrMap.get(attrName) + "=#{" + attrName + "}");
            }
        }
        WHERE(object.gainPrimaryKey() + "=#{" + object.gainPrimaryKey() + "}");
        // 空id判断
        if (object.isNull(object.gainPrimaryKey())) {
            throw new RuntimeException("Entity id is null");
        }

        String SQL = SQL();
//        logger.debug("update SQL:" + SQL);
//        System.out.println("update SQL:" + SQL);
        return SQL;
    }

    /**
     * id查询语句创建
     *
     * @param object
     * @return
     */
    public String selectById(T object) {
        BEGIN();
        SELECT(object.gainAllAttributesMapperToDatabase());
        FROM(object.gainTableName());
        WHERE(object.gainPrimaryKey() + "=" + ClassUtils.getAttributeValue(object, object.gainPrimaryKey()));
        // 空id判断
        if (object.isNull(object.gainPrimaryKey())) {
            throw new RuntimeException("Entity id is null");
        }

        String SQL = SQL();
//        logger.debug("selectById SQL:" + SQL);
//        System.out.println("selectById SQL:" + SQL);
        return SQL;
    }

    /**
     * or条件查询语句生成
     *
     * @param object
     * @return
     */
    public String selectOr(T object) {
        // 获取属性名集合
        Set<String> attrSet = object.gainAttributesNameSet();
        Map<String, String> attrMap = object.gainAttributesToDatabaseMap(attrSet);

        BEGIN();
        SELECT(object.gainAllAttributesMapperToDatabase());
        FROM(object.gainTableName());
        for (String attrName : attrSet) {
            if (!object.isNull(attrName)) {
                OR();
                WHERE(attrMap.get(attrName) + "=#{" + attrName + "}");
            }
        }
        String SQL = SQL();
//        logger.debug("selectOr SQL:" + SQL);
//        System.out.println("selectOr SQL:" + SQL);
        return SQL;
    }

    /**
     * and条件查询语句生成
     *
     * @param object
     * @return
     */
    public String selectAnd(T object) {
        // 获取属性名集合
        Set<String> attrSet = object.gainAttributesNameSet();
        Map<String, String> attrMap = object.gainAttributesToDatabaseMap(attrSet);

        BEGIN();
        SELECT(object.gainAllAttributesMapperToDatabase());
        FROM(object.gainTableName());
        for (String attrName : attrSet) {
            if (!object.isNull(attrName)) {
                WHERE(attrMap.get(attrName) + "=#{" + attrName + "}");
            }
        }
        String SQL = SQL();
//        logger.debug("selectOr SQL:" + SQL);
//        System.out.println("selectOr SQL:" + SQL);
        return SQL;
    }

    /**
     * or条件查询Like语句生成
     *
     * @param object
     * @return
     */
    public String selectOrLike(T object) {
        // 获取属性名集合
        Set<String> attrSet = object.gainAttributesNameSet();
        Map<String, String> attrMap = object.gainAttributesToDatabaseMap(attrSet);

        BEGIN();
        SELECT(object.gainAllAttributesMapperToDatabase());
        FROM(object.gainTableName());
        for (String attrName : attrSet) {
            if (!object.isNull(attrName)) {
                OR();
                if (ClassUtils.getAttributeTypeStr(object, attrName).equals("String")) {
                    if (!object.isBlank(attrName)){
                        WHERE(attrMap.get(attrName) + " like CONCAT( '%',#{" + attrName + "},'%' )");
                    }
                } else{
                    WHERE(attrMap.get(attrName) + "=#{" + attrName + "}");
                }
            }
        }
        String SQL = SQL();
//        logger.debug("selectOr SQL:" + SQL);
//        System.out.println("selectOr SQL:" + SQL);
        return SQL;
    }

    /**
     * and条件查询Lile语句生成
     *
     * @param object
     * @return
     */
    public String selectAndLike(T object) {
        // 获取属性名集合
        Set<String> attrSet = object.gainAttributesNameSet();
        Map<String, String> attrMap = object.gainAttributesToDatabaseMap(attrSet);

        BEGIN();
        SELECT(object.gainAllAttributesMapperToDatabase());
        FROM(object.gainTableName());
        for (String attrName : attrSet) {
            if (!object.isNull(attrName)) {
                if (ClassUtils.getAttributeTypeStr(object, attrName).equals("String")) {
                    if (!object.isBlank(attrName)){
                        WHERE(attrMap.get(attrName) + " like CONCAT( '%',#{" + attrName + "},'%' )");
                    }
                } else {
                    WHERE(attrMap.get(attrName) + "=#{" + attrName + "}");
                }
            }
        }
        String SQL = SQL();
//        logger.debug("selectOr SQL:" + SQL);
//        System.out.println("selectOr SQL:" + SQL);
        return SQL;
    }

    /**
     * and条件查询Lile总记录数语句生成
     *
     * @param object
     * @return
     */
    public String selectAndLikeCount(T object) {
        // 获取属性名集合
        Set<String> attrSet = object.gainAttributesNameSet();
        Map<String, String> attrMap = object.gainAttributesToDatabaseMap(attrSet);

        BEGIN();
        SELECT("count(*)");
        FROM(object.gainTableName());
        for (String attrName : attrSet) {
            if (!object.isNull(attrName)) {
                if (ClassUtils.getAttributeTypeStr(object, attrName).equals("String")) {
                    if (!object.isBlank(attrName)){
                        WHERE(attrMap.get(attrName) + " like CONCAT( '%',#{" + attrName + "},'%' )");
                    }
                } else {
                    WHERE(attrMap.get(attrName) + "=#{" + attrName + "}");
                }
            }
        }
        String SQL = SQL();
//        logger.debug("selectOr SQL:" + SQL);
//        System.out.println("selectOr SQL:" + SQL);
        return SQL;
    }
}
