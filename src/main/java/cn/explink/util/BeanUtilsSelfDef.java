package cn.explink.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 封装的BeanUtil
 * @author jiangyu 2015年5月19日
 *
 */
public class BeanUtilsSelfDef {

	private static Logger logger = LoggerFactory.getLogger(BeanUtilsSelfDef.class);
	
	public static void copyPropertiesIgnoreException(Object dest, Object src) {
		try {
		    ConvertUtils.register(new BigDecimalConverter(BigDecimal.ZERO), java.math.BigDecimal.class);    
			org.apache.commons.beanutils.BeanUtils.copyProperties(dest, src);
		} catch (Exception e) {
		}
	}
	
	public static Map<String, Object> transBean2Map(Object obj) {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor property : propertyDescriptors){
                String key = property.getName();
                // 过滤class属性  
                if(!key.equals("class")){
                    // 得到property对应的getter方法  
                    Method getter = property.getReadMethod();  
                    Object value = getter.invoke(obj);
                    map.put(key, value);  
                }
            }
        } catch (Exception e){
            logger.error("transBean2Map Error ", e);
        }
        return map;
    }
}
