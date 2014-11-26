package cn.explink.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbDiuShi;
import cn.explink.domain.CwbDiuShiView;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;

@Service
public class CwbDiuShiService {
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	DataStatisticsService dataStatisticsService;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 货物丢失处理页面的显示
	public List<CwbDiuShiView> getCwbDiuShiViewCount10(List<CwbOrder> clist, List<CwbDiuShi> cwbDiuShilist, List<Customer> customerList, List<User> userList) {
		List<CwbDiuShiView> cwbDiuShiViewList = new ArrayList<CwbDiuShiView>();
		if (clist.size() > 0) {
			for (CwbOrder c : clist) {
				CwbDiuShiView cwbDiuShiView = new CwbDiuShiView();
				CwbDiuShi cds = this.getQueryCwbDiuShi(cwbDiuShilist, c.getCwb());

				cwbDiuShiView.setCwb(c.getCwb());
				cwbDiuShiView.setCustomername(dataStatisticsService.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
				cwbDiuShiView.setConsigneename(c.getConsigneename());
				cwbDiuShiView.setReceivablefee(cds.getReceivablefee());
				cwbDiuShiView.setPaybackfee(cds.getPaybackfee());
				cwbDiuShiView.setCaramount(cds.getCaramount());
				cwbDiuShiView.setPayamount(cds.getPayamount());
				cwbDiuShiView.setShenhetime(cds.getShenhetime());
				cwbDiuShiView.setShenheName(dataStatisticsService.getQueryUserName(userList, cds.getUserid()));
				cwbDiuShiView.setShenheRole(getQueryUserRole(userList, cds.getUserid()));
				cwbDiuShiView.setFlowordertype(c.getFlowordertype());// 订单的当前最新状态
				cwbDiuShiView.setDeliverystate(c.getDeliverystate());
				cwbDiuShiView.setIsendstate(cds.getIsendstate());
				cwbDiuShiView.setIshandle(cds.getIshandle());
				cwbDiuShiView.setDiushiid(cds.getId());

				cwbDiuShiViewList.add(cwbDiuShiView);
			}
		}
		return cwbDiuShiViewList;
	}

	public CwbDiuShi getQueryCwbDiuShi(List<CwbDiuShi> cwbDiuShiList, String cwb) {
		CwbDiuShi cwbDiuShi = new CwbDiuShi();
		for (CwbDiuShi cd : cwbDiuShiList) {
			if (cd.getCwb().equals(cwb)) {
				cwbDiuShi = cd;
				break;
			}
		}
		return cwbDiuShi;
	}

	public long getQueryUserRole(List<User> userList, long userid) {
		long role = -1;
		for (User u : userList) {
			if (u.getUserid() == userid) {
				role = u.getRoleid();
				break;
			}
		}
		return role;
	}

}
