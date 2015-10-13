package cn.explink.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import cn.explink.controller.MonitorDTO;
import cn.explink.controller.MonitorOrderDTO;
import cn.explink.dao.MonitorDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.util.DateDayUtil;

@Service
public class MonitorService {
	@Autowired
	MonitorDAO monitorDAO;

	public MonitorDTO getMonitorDate(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String flowordertype, String customerid, int isnow, long branchid) {
		// 处理时间数据
		setDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
		MonitorDTO mon = monitorDAO.getMon(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowordertype, customerid, isnow, branchid);
		return mon;
	}

	public MonitorDTO getMonitorDeliveryDate(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String orderResultType, String flowordertype, String customerid, int isnow, long branchid) {
		// 处理时间数据
		setDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
		MonitorDTO mon = monitorDAO.getMonDelivery(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, orderResultType, flowordertype, customerid,
				isnow, branchid);
		return mon;
	}

	// 库房监控
	public MonitorDTO getHouseDate(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String flowordertype, String customerid, int isnow, long branchid) {
		// 处理时间数据
		setDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
		MonitorDTO mon = monitorDAO.getHouse(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowordertype, customerid, isnow, branchid);
		return mon;
	}

	// 分页显示
	public List<MonitorOrderDTO> getMonitorOrderList(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime,
			String emaildateid, String flowordertype, String customerid, int isnow, long branchid, long page) {
		setDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
		List<MonitorOrderDTO> monList = monitorDAO.getMonOrderList(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowordertype, customerid,
				isnow, branchid, page);
		return monList;
	}

	public long getMonitorOrderListCount(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String flowordertype, String customerid, int isnow, long branchid) {
		setDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
		return monitorDAO.getMonOrderListCount(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowordertype, customerid, isnow, branchid);

	}

	public MonitorDTO getMonitorDateYoudanwuhuoHouse(String emaildateid, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime) {
		MonitorDTO mon = new MonitorDTO();
		String emaildateStr = "";
		long countsum = 0;
		BigDecimal caramountsum = BigDecimal.ZERO;
		if (emaildateid.indexOf(",") > -1) {
			for (int i = 0; i < emaildateid.split(",").length; i++) {
				MonitorDTO mon1 = monitorDAO.getMonYoudanwuhuoHouse(emaildateid.split(",")[i], branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
						eamilEndTime);
				countsum += mon1.getCountsum();
				caramountsum = caramountsum.add(mon1.getCaramountsum());
			}
		} else {
			emaildateStr = emaildateid.length() < 1 ? "''" : emaildateid;
			MonitorDTO mon1 = monitorDAO.getMonYoudanwuhuoHouse(emaildateStr, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			countsum += mon1.getCountsum();
			caramountsum = caramountsum.add(mon1.getCaramountsum());
		}
		mon.setCountsum(countsum);
		mon.setCaramountsum(caramountsum);
		return mon;
	}

	public MonitorDTO getMonitorDateYoudanwuhuo(String emaildateid, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime) {
		MonitorDTO mon = new MonitorDTO();
		String emaildateStr = "";
		long countsum = 0;
		BigDecimal caramountsum = BigDecimal.ZERO;
		if (emaildateid.indexOf(",") > -1) {
			for (int i = 0; i < emaildateid.split(",").length; i++) {
				MonitorDTO mon1 = monitorDAO.getMonYoudanwuhuo(emaildateid.split(",")[i], branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
				countsum += mon1.getCountsum();
				caramountsum = caramountsum.add(mon1.getCaramountsum());
			}
		} else {
			emaildateStr = emaildateid.length() < 1 ? "''" : emaildateid;
			MonitorDTO mon1 = monitorDAO.getMonYoudanwuhuo(emaildateStr, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			countsum += mon1.getCountsum();
			caramountsum = caramountsum.add(mon1.getCaramountsum());
		}
		mon.setCountsum(countsum);
		mon.setCaramountsum(caramountsum);
		return mon;
	}

	public List<MonitorOrderDTO> getMonitorOrderYoudanwuhuoList(String emaildateid, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime,
			String shipEndTime, String emailStartTime, String eamilEndTime, long page) {
		List<MonitorOrderDTO> monList = new ArrayList<MonitorOrderDTO>();
		String emaildateStr = "";
		if (emaildateid.indexOf(",") > -1) {
			for (int i = 0; i < emaildateid.split(",").length; i++) {
				List<MonitorOrderDTO> monList1 = monitorDAO.getMonOrderYoudanwuhuoList(emaildateid.split(",")[i], branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
						emailStartTime, eamilEndTime, -1);
				monList.addAll(monList1);
			}
		} else {
			emaildateStr = emaildateid;
			List<MonitorOrderDTO> monList1 = monitorDAO.getMonOrderYoudanwuhuoList(emaildateStr, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
					eamilEndTime, page);
			monList.addAll(monList1);
		}
		int page1 = 1;

		return monList;
	}

	public long getMonitorOrderYoudanwuhuoListCount(String emaildateid, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime) {
		long count = 0;
		String emaildateStr = "";
		if (emaildateid.indexOf(",") > -1) {
			for (int i = 0; i < emaildateid.split(",").length; i++) {
				count += monitorDAO.getMonOrderYoudanwuhuoListCount(emaildateid.split(",")[i], branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
						eamilEndTime);
			}
		} else {
			emaildateStr = emaildateid;
			count += monitorDAO.getMonOrderYoudanwuhuoListCount(emaildateStr, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
		}
		return count;
	}

	public MonitorDTO getMonitorDateWeirukuHouse(String emaildateid, long branchid, String flowordertype, String customerid, String crateStartdate, String crateEnddate, String shipStartTime,
			String shipEndTime, String emailStartTime, String eamilEndTime) {
		MonitorDTO mon = monitorDAO.getMonWeirukuHouse("", branchid, flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
		return mon;
	}

	public MonitorDTO getMonitorDateWeiruku(String emaildateid, long branchid, String flowordertype, String customerid, String crateStartdate, String crateEnddate, String shipStartTime,
			String shipEndTime, String emailStartTime, String eamilEndTime) {
		MonitorDTO mon = new MonitorDTO();
		String emaildateStr = "";
		long countsum = 0;
		BigDecimal caramountsum = BigDecimal.ZERO;
		if (emaildateid.indexOf(",") > -1) {
			for (int i = 0; i < emaildateid.split(",").length; i++) {
				MonitorDTO mon1 = monitorDAO.getMonWeiruku(emaildateid.split(",")[i], branchid, flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
						eamilEndTime);
				countsum += mon1.getCountsum();
				caramountsum = caramountsum.add(mon1.getCaramountsum());
			}
		} else {
			emaildateStr = emaildateid;
			MonitorDTO mon1 = monitorDAO.getMonWeiruku(emaildateStr, branchid, flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			countsum += mon1.getCountsum();
			caramountsum = caramountsum.add(mon1.getCaramountsum());
		}
		mon.setCountsum(countsum);
		mon.setCaramountsum(caramountsum);
		return mon;
	}

	public List<MonitorOrderDTO> getMonitorOrderWeirukuList(String emaildateid, long branchid, String flowordertype, String customerid, String crateStartdate, String crateEnddate,
			String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, long page) {
		List<MonitorOrderDTO> monList = new ArrayList<MonitorOrderDTO>();
		String emaildateStr = "";
		if (emaildateid.indexOf(",") > -1) {
			for (int i = 0; i < emaildateid.split(",").length; i++) {
				List<MonitorOrderDTO> monList1 = monitorDAO.getMonOrderWeirukuList(emaildateid.split(",")[i], branchid, flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime,
						shipEndTime, emailStartTime, eamilEndTime, page);
				monList.addAll(monList1);
			}
		} else {
			emaildateStr = emaildateid;
			List<MonitorOrderDTO> monList1 = monitorDAO.getMonOrderWeirukuList(emaildateStr, branchid, flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
					emailStartTime, eamilEndTime, page);
			monList.addAll(monList1);
		}
		return monList;
	}

	public long getMonitorOrderWeirukuListCount(String emaildateid, long branchid, String flowordertype, String customerid, String crateStartdate, String crateEnddate, String shipStartTime,
			String shipEndTime, String emailStartTime, String eamilEndTime) {
		long count = 0;
		String emaildateStr = "";
		if (emaildateid.indexOf(",") > -1) {
			for (int i = 0; i < emaildateid.split(",").length; i++) {
				count += monitorDAO.getMonOrderWeirukuListCount(emaildateid.split(",")[i], branchid, flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
						emailStartTime, eamilEndTime);
			}
		} else {
			emaildateStr = emaildateid;
			count += monitorDAO.getMonOrderWeirukuListCount(emaildateStr, branchid, flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
		}
		return count;
	}

	// 投递实效监控
	public List getDeliveryList(long brandid, String controlStrType, int timeToid) {
		List list = new ArrayList();
		String ctrdate = getOneHoursAgoTime(timeToid);
		if (controlStrType.length() > 0) {
			String[] dayStrl = controlStrType.split(",");
			for (int i = 0; i < dayStrl.length; i++) {
				Map map = new HashMap();
				MonitorDTO mon = monitorDAO.getMonByFlowtype(brandid, Long.parseLong(dayStrl[i]), ctrdate);
				map.put(dayStrl[i], mon.getCountsum());
				list.add(map);
			}
		}
		return list;
	}

	// 投递实效监控
	public List<MonitorOrderDTO> getDeliveryShowListExp(long brandid, long flowtype, int timeToid) {
		String ctrdate = getOneHoursAgoTime(timeToid);
		List<MonitorOrderDTO> list = monitorDAO.getMonByFlowtypeListExp(brandid, flowtype, ctrdate);
		return list;
	}

	public List<CwbOrder> getDeliveryShowList(long page, long brandid, long flowtype, int timeToid) {
		String ctrdate = getOneHoursAgoTime(timeToid);
		List<CwbOrder> list = monitorDAO.getMonByFlowtypeList(page, brandid, flowtype, ctrdate);
		return list;
	}

	public SqlRowSet getDeliveryRusulset(long page, long brandid, long flowtype, int timeToid) {
		String ctrdate = getOneHoursAgoTime(timeToid);
		return monitorDAO.getMonByResultSet(page, brandid, flowtype, ctrdate);
	}

	public long getCountDeliveryShowList(long page, long brandid, long flowtype, int timeToid) {
		String ctrdate = getOneHoursAgoTime(timeToid);
		long contMonBYFlowtype = monitorDAO.getCountMonByFlowtypeList(page, brandid, flowtype, ctrdate);
		return contMonBYFlowtype;
	}

	// 异常信息监控
	public List<MonitorDTO> getExpMonitorDate(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String flowordertype, String customerid, int isnow, long branchid) {
		// 处理时间数据
		setDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
		List<MonitorDTO> mon = monitorDAO.getExpMon(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowordertype, customerid, isnow, branchid);
		return mon;
	}

	// //异常信息（有单无货）监控
	public List<MonitorDTO> getExpMonitorDateYoudanwuhuo(String emaildateid, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime) {
		List<MonitorDTO> mon = new ArrayList<MonitorDTO>();
		String emaildateStr = "";
		if (emaildateid.indexOf(",") > -1) {
			for (int i = 0; i < emaildateid.split(",").length; i++) {
				List<MonitorDTO> mon1 = monitorDAO.getExpMonYoudanwuhuo(emaildateid.split(",")[i], branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
						eamilEndTime);
				mon.addAll(mon1);
			}
		} else {
			emaildateStr = emaildateid;
			List<MonitorDTO> mon1 = monitorDAO.getExpMonYoudanwuhuo(emaildateStr, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			mon.addAll(mon1);
		}
		return mon;
	}

	/**
	 * 获得当前时间一小时前的时间，格式化成yyyy-MM-dd HH:mm:ss:SS<br>
	 *
	 * @return 当前时间几小时小时前的时间
	 */
	private String getOneHoursAgoTime(int hour) {
		String oneHoursAgoTime = "";
		Date now = new Date();
		now.setHours(now.getHours() - hour);
		oneHoursAgoTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
		return oneHoursAgoTime;
	}

	private void setDate(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime) {
		if (crateStartdate.length() == 0 && crateEnddate.length() > 0) {
			crateStartdate = crateEnddate;
		} else if (crateStartdate.length() > 0 && crateEnddate.length() == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			crateEnddate = sdf.format(new Date());
		} else if (crateStartdate.length() == 0 && crateEnddate.length() == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			crateEnddate = sdf.format(new Date()) + " 23:59:59";
			crateStartdate = DateDayUtil.getDayCum(crateEnddate, -6) + " 00:00:00";
		}
		if (shipStartTime.length() == 0 && shipEndTime.length() > 0) {
			shipStartTime = shipEndTime;
		} else if (shipStartTime.length() > 0 && shipEndTime.length() == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			shipEndTime = sdf.format(new Date());
		} else if (shipStartTime.length() == 0 && shipEndTime.length() == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			shipEndTime = sdf.format(new Date()) + " 23:59:59";
			shipStartTime = DateDayUtil.getDayCum(crateEnddate, -6) + " 00:00:00";
		}
		if (emailStartTime.length() == 0 && eamilEndTime.length() > 0) {
			shipStartTime = eamilEndTime;
		} else if (emailStartTime.length() > 0 && eamilEndTime.length() == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			eamilEndTime = sdf.format(new Date());
		} else if (emailStartTime.length() == 0 && eamilEndTime.length() == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			eamilEndTime = sdf.format(new Date()) + " 23:59:59";
			emailStartTime = DateDayUtil.getDayCum(crateEnddate, -6) + " 00:00:00";
		}
	}

}
