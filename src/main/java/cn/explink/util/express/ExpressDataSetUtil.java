package cn.explink.util.express;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.domain.VO.express.ExpressImportFiled;
import cn.explink.domain.express.ExpressFreightBillImportDetail;

/**
 * 导入时候的校验类工具
 * 
 * @author jiangyu 2015年8月21日
 *
 */
public class ExpressDataSetUtil {

	private static List<ExpressImportFiled> list = new ArrayList<ExpressImportFiled>();

	private static Logger logger = LoggerFactory.getLogger(ExpressDataSetUtil.class);
	
	static {
		ExpressImportFiled filed1 = new ExpressImportFiled("运单号", "orderNo", "java.lang.String", "", "");
		ExpressImportFiled filed2 = new ExpressImportFiled("件数", "goodNum", "java.lang.Integer", "", "");
		ExpressImportFiled filed3 = new ExpressImportFiled("揽件员", "collectPerson", "java.lang.String", "", "");
		ExpressImportFiled filed4 = new ExpressImportFiled("派件员", "deliveryPerson", "java.lang.String", "", "");
		ExpressImportFiled filed5 = new ExpressImportFiled("费用合计", "sumFee", "java.math.BigDecimal", "", "");
		ExpressImportFiled filed6 = new ExpressImportFiled("运费", "deliveryFee", "java.math.BigDecimal", "", "");
		ExpressImportFiled filed7 = new ExpressImportFiled("包装费用", "packageFee", "java.math.BigDecimal", "", "");
		ExpressImportFiled filed8 = new ExpressImportFiled("保价费用", "insuredFee", "java.math.BigDecimal", "", "");
		list.add(filed1);
		list.add(filed2);
		list.add(filed3);
		list.add(filed4);
		list.add(filed5);
		list.add(filed6);
		list.add(filed7);
		list.add(filed8);
	}

	private ExpressDataSetUtil() {}

	private static class DataSetHolder {
		private static final ExpressDataSetUtil INSTANCE = new ExpressDataSetUtil();
	}

	public static ExpressDataSetUtil getInstance() {
		return DataSetHolder.INSTANCE;
	}

	/**
	 * 每一个导入的字段都对应一个DataSetHelper
	 *
	 * @param template
	 * @return
	 * @throws Exception
	 */
	private List<DataSetHelper> createHelper() {
		List<DataSetHelper> hList = new ArrayList<DataSetHelper>();
		try {
			Integer i = 0;// 计数使用
			for (ExpressImportFiled f : list) {
				DataSetHelper data_helper = new DataSetHelper();
				data_helper.index = ++i;
				data_helper.field = f;
				data_helper.clazz = Class.forName(f.getFiledType());
				data_helper.method = ExpressFreightBillImportDetail.class.getMethod(this.transSetMethod(f.getFiledCode()), data_helper.clazz);
				hList.add(data_helper);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return hList;
	}

	/**
	 * 转换为set方法
	 *
	 * @param field
	 * @return
	 */
	private String transSetMethod(String field) {
		String setMethod = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
		return setMethod;
	}

	public List<DataSetHelper> getDataSetHelper() {
		return createHelper();
	}

}
