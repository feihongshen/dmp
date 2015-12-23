package cn.explink.service.express;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pjbest.deliveryorder.bizservice.PjDeliveryTrackInfo;

/**
 *
 * @description 快递轨迹获取的相关方法类
 * @author  刘武强
 * @data   2015年10月20日
 */
@Transactional
@Service
public class DeliveryOrderTrackingService {

	public List<PjDeliveryTrackInfo> orderList(List<PjDeliveryTrackInfo> list) {
		Collections.sort(list, new MyComparator());
		return list;
	}

	class MyComparator implements Comparator {
		//这里的o1和o2就是list里任意的两个对象，然后按需求把这个方法填完整就行了
		@Override
		public int compare(Object o1, Object o2) {
			PjDeliveryTrackInfo temp1 = (PjDeliveryTrackInfo) o1;
			PjDeliveryTrackInfo temp2 = (PjDeliveryTrackInfo) o2;
			if (DeliveryOrderTrackingService.this.compareDate(temp1.getOperateTime(), temp2.getOperateTime())) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	/**
	 *
	 * @Title: compareDate
	 * @description 时间比较
	 * @author 刘武强
	 * @date  2015年10月20日下午3:52:49
	 * @param  @return
	 * @return  boolean
	 * @throws
	 */
	public final boolean compareDate(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return true;
			} else if (dt1.getTime() < dt2.getTime()) {
				System.out.println("dt1在dt2后");
				return false;
			} else {
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}
}
