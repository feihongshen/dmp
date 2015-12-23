package cn.explink.service.express;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.express.PreOrderDao;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExpressQuickQueryView;
import cn.explink.domain.express.ExpressPreOrder;
import cn.explink.enumutil.express.ExcuteStateEnum;

/**
 * 快递中预订单的快速查询
 * 
 * @author jiangyu 2015年8月8日
 *
 */
@Service
public class ExpressQuickQueryService {
	@Autowired
	private PreOrderDao preOrderDao;

	@Autowired
	private BranchDAO branchDAO;
	/**
	 * 查询预订单信息
	 * @param preOrderNo
	 * @param user
	 * @return
	 */
	public Map<String, Object> transferPreOrder2View(String preOrderNo, User user) {
		// 封装参数的map
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询预订单号
		ExpressPreOrder preOrder = preOrderDao.getPreOrderByOrderNo(preOrderNo);
		// 转换为显示
		ExpressQuickQueryView view = (ExpressQuickQueryView) transferDate2StringFactory(ExpressQuickQueryView.class,ExpressPreOrder.class,preOrder);
		//执行状态的单独转换
		Integer value = preOrder.getExcuteState();
		if (null!=value) {
			view.setExcuteState(ExcuteStateEnum.getTextByValue(value));
		}else {
			view.setExcuteState("");
		}
		Integer siteType = this.branchDAO.getBranchByBranchid(user.getBranchid()).getSitetype();
		map.put("view", view);
		map.put("siteType", siteType);
		return map;
	}

	/**
	 * 
	 * 将源对象中的时间类型全部转换为string类型工具【目前凑合着用，后期提取公共】
	 * @param classDest 目的对象
	 * @param classSource 源对象
	 * @param source 源实体对象
	 * @return 目标对象的实体
	 */
	private Object transferDate2StringFactory(Class<?> classDest, Class<?> classSource, Object source) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Object tarObj = null;
		try {
			Object target = classDest.newInstance();
			Field[] fields = classSource.getDeclaredFields();
			for (Field field : fields) {
				java.lang.reflect.Method method = source.getClass().getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), null);
				String dateStr = "";
				if ("Date".equals(field.getType().getSimpleName())) {
					Date date = (Date) method.invoke(source, null);
					if (date != null) {
						dateStr = df.format(date);
					}
					target.getClass().getDeclaredMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), String.class).invoke(target, dateStr);
				} else if("String".equals(field.getType().getSimpleName())){
					Object value = method.invoke(source, null) == null ? "" : method.invoke(source, null);
					target.getClass().getDeclaredMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), String.class).invoke(target, value.toString());
				}else{
					continue;
				}
			}
			tarObj = target;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tarObj;
	}

}
