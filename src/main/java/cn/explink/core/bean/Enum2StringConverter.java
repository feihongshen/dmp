package cn.explink.core.bean;

import cn.explink.core.enums.IEnum;

/**
 * IEnum到String转换器
 * @author gaoll
 *
 */
public class Enum2StringConverter implements TypeConverter {

    @Override
    public Object convert(Class<?> sourceClass, Class<?> targetClass, Object value) {

        if (value == null) {
            return null;
        }
        if (targetClass.equals(String.class) && IEnum.class.isAssignableFrom(sourceClass)) {
            return ((IEnum) value).getCode();
        }
        return value;
    }
}
