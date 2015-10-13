package cn.explink.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.controller.DeliveryDTO;
import cn.explink.controller.EmaildateTDO;
import cn.explink.dao.DeliveryDAO;
import cn.explink.dto.DeliveryNewDTO;
import cn.explink.util.DateDayUtil;

@Service
public class DeliveryService {
	@Autowired
	DeliveryDAO deliveryDAO;

	// /确定需求后

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getAllByDay(String dayStr, String emaildateStr, String[] cumstr, long timeLong, long dateType) {
		Map map2 = new HashMap();
		if (dayStr.length() > 0) {
			String[] dayStrl = dayStr.split(",");
			for (int i = 0; i < dayStrl.length; i++) {
				Map map = new HashMap();
				if (cumstr != null && cumstr.length > 0) {
					for (int j = 0; j < cumstr.length; j++) {
						List<DeliveryNewDTO> list1 = deliveryDAO.getAllBydayNew(new Long(cumstr[j]), dayStrl[i], dayStrl[i], emaildateStr, timeLong, dateType, 0);

						if (!list1.isEmpty()) {

							map.put(new Long(cumstr[j]), ((DeliveryNewDTO) list1.get(0)).getRuKucount());
						}
					}
				}
				map2.put(dayStrl[i], map);
			}
		}
		return map2;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getAllByDayAndBranchid(String dayStr, String emaildateStr, String[] cumstr, long branchid, long flowordertype, long timeLong, long dateType) {
		Map map2 = new HashMap();
		if (dayStr.length() > 0) {
			String[] dayStrl = dayStr.split(",");
			for (int i = 0; i < dayStrl.length; i++) {
				Map map = new HashMap();
				if (cumstr != null && cumstr.length > 0) {
					for (int j = 0; j < cumstr.length; j++) {
						List<DeliveryNewDTO> list1 = deliveryDAO.getAllBydayAndBrandid(new Long(cumstr[j]), dayStrl[i], dayStrl[i], emaildateStr, branchid, timeLong, dateType, 0);
						if (!list1.isEmpty()) {
							map.put(new Long(cumstr[j]), ((DeliveryNewDTO) list1.get(0)).getRuKucount());
						}
					}
				}
				map2.put(dayStrl[i], map);
			}
		}
		return map2;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getAllByDayAndType(String dayStr, String emaildateStr, String[] cumstr, int isTuotou, long timeLong, long dateType) {
		Map map2 = new HashMap();
		if (dayStr.length() > 0) {
			String[] dayStrl = dayStr.split(",");
			for (int i = 0; i < dayStrl.length; i++) {
				Map map = new HashMap();
				if (cumstr != null && cumstr.length > 0) {
					for (int j = 0; j < cumstr.length; j++) {
						List<DeliveryNewDTO> list1 = deliveryDAO.getAllBydayNew(new Long(cumstr[j]), dayStrl[i], dayStrl[i], emaildateStr, timeLong, dateType, isTuotou);
						List<DeliveryNewDTO> list2 = deliveryDAO.getAllBydayNew(new Long(cumstr[j]), dayStrl[i], dayStrl[i], emaildateStr, timeLong, dateType, 0);
						if (list1 != null && !list1.isEmpty()) {
							if (isTuotou == 1) {
								if (((DeliveryNewDTO) list1.get(0)).getTuoToucount() == 0) {
									map.put(new Long(cumstr[j]), 0);
								} else {
									map.put(new Long(cumstr[j]), ((DeliveryNewDTO) list1.get(0)).getTuoToucount() * 100 / ((DeliveryNewDTO) list2.get(0)).getRuKucount());
								}
							} else {
								if (((DeliveryNewDTO) list1.get(0)).getYoujiekucount() == 0) {
									map.put(new Long(cumstr[j]), 0);
								} else {
									map.put(new Long(cumstr[j]), ((DeliveryNewDTO) list1.get(0)).getYoujiekucount() * 100 / ((DeliveryNewDTO) list2.get(0)).getRuKucount());
								}
							}

						}
					}
				}
				map2.put(dayStrl[i], map);
			}
		}
		return map2;
	}

	public List<EmaildateTDO> getEmaildateList(String startdate, String enddate) {
		if (startdate.length() == 0 && enddate.length() > 0) {
			startdate = enddate;
		} else if (startdate.length() > 0 && enddate.length() == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			enddate = sdf.format(new Date());
		} else if (startdate.length() == 0 && enddate.length() == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			enddate = sdf.format(new Date());
			startdate = DateDayUtil.getDayCum(enddate, 0);
		}
		List<EmaildateTDO> eamilDateList = deliveryDAO.getEmailDate(startdate, enddate);
		return eamilDateList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getAllByDayAndTypeAndBranchId(String dayStr, String emaildateStr, String[] cumstr, long branchid, long flowordertype, long timeLong, long dateType) {
		Map map2 = new HashMap();
		if (dayStr.length() > 0) {
			String[] dayStrl = dayStr.split(",");
			for (int i = 0; i < dayStrl.length; i++) {
				Map map = new HashMap();
				if (cumstr != null && cumstr.length > 0) {
					for (int j = 0; j < cumstr.length; j++) {
						List<DeliveryNewDTO> list1 = deliveryDAO.getAllBydayAndBrandid(new Long(cumstr[j]), dayStrl[i], dayStrl[i], emaildateStr, branchid, timeLong, dateType, flowordertype);
						List<DeliveryNewDTO> list2 = deliveryDAO.getAllBydayAndBrandid(new Long(cumstr[j]), dayStrl[i], dayStrl[i], emaildateStr, branchid, timeLong, dateType, 0);
						if (!list1.isEmpty()) {
							if (((DeliveryNewDTO) list1.get(0)).getRuKucount() == 0) {
								map.put(new Long(cumstr[j]), 0);
							} else {
								if (flowordertype == 1) {// 妥投
									map.put(new Long(cumstr[j]), ((DeliveryNewDTO) list1.get(0)).getTuoToucount() * 100 / ((DeliveryNewDTO) list2.get(0)).getRuKucount());
								} else {// 有结果率
									map.put(new Long(cumstr[j]), ((DeliveryNewDTO) list1.get(0)).getYoujiekucount() * 100 / ((DeliveryNewDTO) list2.get(0)).getRuKucount());
								}
							}
						}
					}
				}
				map2.put(dayStrl[i], map);
			}
		}
		return map2;
	}

	public List<EmaildateTDO> getEmaildateAndBrandidList(String startdate, String enddate, long branchid) {
		if (startdate.length() == 0 && enddate.length() > 0) {
			startdate = enddate;
		} else if (startdate.length() > 0 && enddate.length() == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			enddate = sdf.format(new Date());
		} else if (startdate.length() == 0 && enddate.length() == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			enddate = sdf.format(new Date()) + " 23:59:59";
			startdate = DateDayUtil.getDayCum(enddate, 0) + " 00:00:00";
		}
		List<EmaildateTDO> eamilDateList = deliveryDAO.getEmailDateAndBrandId(startdate, enddate, branchid);
		return eamilDateList;
	}

}
