package cn.explink.util.express;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.explink.domain.VO.express.ExpressImportFiled;
import cn.explink.util.Tools;


/**
 * Excel导入帮助类
 * 
 * @author jiangyu
 *
 */
public class DataSetHelper {
	/**
	 * 默认的时间格式
	 */
	public final static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 列标 导入的第几个字段
	 */
	public Integer index = 0;
	/**
	 * 反射方法 比如setOrderNo
	 */
	public Method method;
	/**
	 * 订单字段
	 */
	public ExpressImportFiled field;
	
	public Class<?> clazz;// 类型如public void
							// cn.explink.citydistribution.entity.CityOrderDetail.setOrderNo(java.lang.String)
							// String.class
	
	/**
	 * 解析excel数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Object parse(String data) throws Exception {
		if (field != null) {
			if (StringUtils.isBlank(data)) {
				return null;
			}
			String type = field.getFiledType();
			if ("java.lang.String".equals(type)) {
				return data;
			}
			if ("java.lang.Integer".equals(type)) {
				if (StringUtils.isNotBlank(field.getExtraRule())) {//比如枚举或者是什么类型的整形转换
					Map<String, Integer> map = Tools.parseKvMap(field.getExtraRule());
					if (map.get(data) == null) {
						throw new RuntimeException("不存在的类型！");
					} else {
						return map.get(data);
					}
				}
				return Integer.parseInt(data);
			}
			
			if ("java.math.BigDecimal".equals(type)) {
				return new BigDecimal(data);
			}
			
			if ("java.util.Date".equals(type)) {
				if (StringUtils.isNotBlank(data)) {
					return DEFAULT_DATE_FORMAT.parse(data);
				} else {
					return null;
				}

			}
		}
		return data;
	}
	
	@Override
	public boolean equals(Object obj) {
		DataSetHelper d = (DataSetHelper) obj;
		return index.equals(d.index);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	
}