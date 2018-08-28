package com.chinaums.utils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 类反射用的工具类
 */
public class ClassUtils {


	/**
	 * 获取小写类名
	 * 
	 * @param obj
	 *            实例对象
	 * @return 小写类名
	 */
	public static String getLowerCaseClassName(Object obj) {
		return obj.getClass().getSimpleName().toLowerCase();
	}

	/**
	 * 获取小写类名
	 * 
	 * @param obj
	 *            类名.class
	 * @return 小写类名
	 */
	public static String getLowerCaseClassName(Class<?> obj) {
		return obj.getSimpleName().toLowerCase();
	}

	/**
	 * 获取类名
	 * 
	 * @param obj
	 *            实例对象
	 * @return 类名
	 */
	public static String getClassName(Object obj) {
		return obj.getClass().getSimpleName();
	}

	/**
	 * 获取类名
	 * 
	 * @param obj
	 *            类名.Class
	 * @return 类名
	 */
	public static String getClassName(Class<?> obj) {
		return obj.getSimpleName();
	}

	/**
	 * 获取包路径
	 * @param obj
	 * @return
	 */
	public static String getClassPackageName(Class<?> obj){
		return obj.getPackage().getName();
	}

	/**
	 * 获取业务包路径
	 * @param obj
	 * @return eg:com.gizhi.goods.dao.xxxx---->com.gizhi.goods
	 */
	public static String getWorkPackageName(Class<?> obj){
		String packageName = obj.getPackage().getName();
		String[] packageArr = packageName.split("\\.");
		String packageNamStr = packageArr[0]+"."+packageArr[1]+"."+packageArr[2];
		return packageNamStr;
	}
	/**
	 * 获取全类名
	 * 
	 * @param obj
	 *            实例对象
	 * @return 全类名
	 */
	public static String getCanonicalName(Object obj) {
		return obj.getClass().getCanonicalName();
	}

	/**
	 * 获取全类名
	 * 
	 * @param obj
	 *            类名.Class
	 * @return 全类名
	 */
	public static String getCanonicalName(Class<?> obj) {
		return obj.getCanonicalName();
	}

	/**
	 * 根据全类名获取新的实例
	 * 
	 * @param name
	 *            全类名
	 * @return 类实例对象
	 */
	public static Object createInstance(String name) {
		try {
			return Class.forName(name).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据class<?>获取新实例
	 * 
	 * @author jesse@gizhi.com
	 * @date 2015年3月25日
	 * @param claz
	 * @return
	 */
	public static Object createInstance(Class<?> claz) {
		try {
			return Class.forName(getCanonicalName(claz)).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据实例获取新的实例
	 * 
	 * @param obj
	 *            实例对象
	 * @return 类新实例对象
	 */
	public static Object createInstance(Object obj) {
		try {
			return Class.forName(getCanonicalName(obj)).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取类的所有属性-值集合（包括第一级父类属性）
	 * 
	 * @param obj
	 *            实例对象
	 * @return 属性值-属性名的map集合
	 */
	public static Map<String, Object> getAttributes(Object obj) {
		// 存储属性集合
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 获取父类属性
//		Field[] superFields = obj.getClass().getSuperclass()
//				.getDeclaredFields();
//		// 循环添加属性
//		for (Field field : superFields) {
//			String name = field.getName();
//			Object value = getAttributeValue(obj, name);
//			map.put(name, value);
//		}
		// 获取本类属性
		Field[] fields = obj.getClass().getDeclaredFields();
		// 循环添加属性
		for (Field field : fields) {
			String name = field.getName();
			Object value = getAttributeValue(obj, name);
			map.put(name, value);
		}
		return map;
	}

	/**
	 * 获取类的所有属性名集合（包括第一级父类属性）
	 * 
	 * @param obj
	 *            类名.class
	 * @return 属性值-属性名的map集合
	 */
	public static Set<String> getAttributesName(Class<?> obj) {
		// 存储属性集合
		Set<String> set = new HashSet<String>();
		// 获取父类属性
//		Field[] superFields = obj.getSuperclass().getDeclaredFields();
//		// 循环添加属性
//		for (Field field : superFields) {
//			String name = field.getName();
//			set.add(name);
//		}
		// 获取本类属性
		Field[] fields = obj.getDeclaredFields();
		// 循环添加属性
		for (Field field : fields) {
			String name = field.getName();
			set.add(name);
		}
		return set;
	}

	/**
	 * 获取类属性的数据类型
	 * 
	 * @param obj
	 *            实例对象
	 * @param AttributeName
	 *            属性名
	 * @return 属性类型
	 */
	public static Class<?> getAttributeType(Object obj, String AttributeName) {
		Field field = null;
		try {
			// 获取本类field
			field = obj.getClass().getDeclaredField(AttributeName);
		} catch (NoSuchFieldException e) {

			// 无法找到时获取父类field
			try {
				field = obj.getClass().getSuperclass()
						.getDeclaredField(AttributeName);
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回类型Class
		return field.getType();
	}

	/**
	 * 获取类属性的数据类型
	 * 
	 * @param obj
	 *            实例对象
	 * @param AttributeName
	 *            属性名
	 * @return 属性类型
	 */
	public static String getAttributeTypeStr(Object obj, String AttributeName) {
		Field field = null;
		try {
			// 获取本类field
			field = obj.getClass().getDeclaredField(AttributeName);
		} catch (NoSuchFieldException e) {

			// 无法找到时获取父类field
			try {
				field = obj.getClass().getSuperclass()
						.getDeclaredField(AttributeName);
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回类型Class
		return subTypeName(field.getType().toString());
	}

	/**
	 * 获取类属性的数据类型
	 * 
	 * @param obj
	 *            类名.class
	 * @param AttributeName
	 *            属性名
	 * @return 属性类型
	 */
	public static Class<?> getAttributeType(Class<?> obj, String AttributeName) {
		Field field = null;
		try {
			// 获取本类field
			field = obj.getDeclaredField(AttributeName);
		} catch (NoSuchFieldException e) {

			// 无法找到时获取父类field
			try {
				field = obj.getSuperclass().getDeclaredField(AttributeName);
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回类型Class
		return field.getType();
	}

	/**
	 * 获取类属性的数据类型
	 * 
	 * @param obj
	 *            类名.class
	 * @param AttributeName
	 *            属性名
	 * @return 属性类型
	 */
	public static String getAttributeTypeStr(Class<?> obj, String AttributeName) {
		Field field = null;
		try {
			// 获取本类field
			field = obj.getDeclaredField(AttributeName);
		} catch (NoSuchFieldException e) {

			// 无法找到时获取父类field
			try {
				field = obj.getSuperclass().getDeclaredField(AttributeName);
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回类型Class
		return subTypeName(field.getType().toString());
	}

	/**
	 * 根据属性名获取属性值
	 * 
	 * @param obj
	 *            实例对象
	 * @param attributeName
	 *            属性名
	 * @return 属性值
	 */
	public static Object getAttributeValue(Object obj, String attributeName) {
		Object value = null;
		Field field = null;
		try {
			// 获取本类field
			field = obj.getClass().getDeclaredField(attributeName);
			// 开启私有属性访问权限
			field.setAccessible(true);
			value = field.get(obj);
		} catch (NoSuchFieldException e) {
			try {
				// 获取父类field
				field = obj.getClass().getSuperclass()
						.getDeclaredField(attributeName);
				// 开启私有属性访问权限
				field.setAccessible(true);
				value = field.get(obj);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 设置属性值
	 * 
	 * @param obj
	 *            对象实例
	 * @param attributeName
	 *            属性名
	 * @param value
	 *            属性值
	 */
	public static void setAttributeValue(Object obj, String attributeName,
			Object value) {
		// 类field存储
		Field field = null;
		try {

			field = obj.getClass().getDeclaredField(attributeName);
			field.setAccessible(true);
			field.set(obj, value);
		}catch (NoSuchFieldException e) {
			try {
				// 获取父类field
				field = obj.getClass().getSuperclass()
						.getDeclaredField(attributeName);
				// 开启私有属性访问权限
				field.setAccessible(true);
				value = field.get(obj);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取ID的属性名
	 * 
	 * @param obj
	 * @return
	 */
	public static String getIdName(Object obj) {
		Map<String, Object> map = getAttributes(obj);
		Set<String> keySet = map.keySet();
		for (String key : keySet)
			if (key.endsWith("ID")) {
				return key;
			}
		return null;
	}

	/**
	 * 获取ID的值
	 * 
	 * @param obj
	 * @return
	 */
	public static int getIdValue(Object obj) {
		String idName = null;
		Integer idValue = null;
		Map<String, Object> map = getAttributes(obj);
		Set<String> keySet = map.keySet();
		for (String key : keySet)
			if (key.equals("ID")) {
				idName = key;
				break;
			}
		idValue = (Integer) map.get(idName);
		return idValue;
	}

	/**
	 * 移除属性中的ID属性
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> removeID(Map<String, Object> map) {
		String idName = null;
		Set<String> keySet = map.keySet();
		for (String key : keySet)
			if (key.equals("ID")) {
				idName = key;
				break;
			}
		map.remove(idName);

		return map;
	}

	/**
	 * 将ID移到最后一个
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> replaceIDEnd(Map<String, Object> map) {
		String idName = null;
		Integer idValue = null;
		Set<String> keySet = map.keySet();
		for (String key : keySet)
			if (key.endsWith("ID")) {
				idName = key;
				break;
			}
		idValue = (Integer) map.get(idName);
		map.remove(idName);
		map.put(idName, idValue);

		return map;
	}

	/**
	 * 格式化类名为短类名
	 * 
	 * @author jesse@gizhi.com
	 * @date 2014年12月1日
	 * @param name
	 * @return
	 */
	public static String subTypeName(String name) {
		return name.substring(name.lastIndexOf(".") + 1);
	}

	/**
	 * 返回首字母大写属性名
	 * 
	 * @author jesse@gizhi.com
	 * @date 2014年12月1日
	 * @param name
	 * @return
	 */
	public static String upperCaseFirst(String name) {
		String first = name.substring(0, 1);
		first = first.toUpperCase();
		return first + name.substring(1, name.length());
	}

	/**
	 * 返回首字母小写写属性名
	 *
	 * @author jesse@gizhi.com
	 * @date 2014年12月1日
	 * @param name
	 * @return
	 */
	public static String lowerCaseFirst(String name) {
		String first = name.substring(0, 1);
		first = first.toLowerCase();
		return first + name.substring(1, name.length());
	}

	/**
	 * 属性名转换成下划线写法
	 * @param attr
	 * @return
	 */
	public static String changeAttrToDatabase(String attr){

		StringBuffer newAttr = new StringBuffer();

		for (char c : attr.toCharArray())
			if (Character.isUpperCase(c))
				newAttr.append("_" + Character.toLowerCase(c));
			else
				newAttr.append(c);

		return newAttr.toString();
	}

	/**
	 * 获取首字母小写类名
	 * @param clas
	 * @return
	 */
	public static String lowerCaseFirstClassName(Class<?> clas){
		String name = getClassName(clas);
		String first = name.substring(0, 1);
		first = first.toLowerCase();
		return first + name.substring(1, name.length());
	}


}
