package cn.explink.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import cn.explink.controller.CwbOrderView;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PayWayDao;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.WarehouseToCommenDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.StreamingStatementCreator;

@Service
public class WhareHouseToCommonService {
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	SetExportFieldDAO setExportFieldDAO;
	@Autowired
	SetExportFieldDAO setexportfieldDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	PayWayDao payWaydao;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	WarehouseToCommenDAO warehouseToCommenDAO;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ObjectMapper om = JacksonMapper.getInstance();

	// 给CwbOrderView赋值
	public List<CwbOrderView> getCwbOrderView(List<CwbOrder> clist, List<WarehouseToCommen> wcommenList, List<Branch> branchList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if (clist.size() > 0) {
			for (CwbOrder c : clist) {
				for (WarehouseToCommen comm : wcommenList) {
					if (!c.getCwb().equals(comm.getCwb())) {
						continue;
					}
					CwbOrderView cwbOrderView = new CwbOrderView();
					cwbOrderView.setCwb(c.getCwb());
					cwbOrderView.setEmaildate(c.getEmaildate());
					cwbOrderView.setCarrealweight(c.getCarrealweight());
					cwbOrderView.setCarsize(c.getCarsize());
					cwbOrderView.setSendcarnum(c.getSendcarnum());
					cwbOrderView.setCwbprovince(c.getCwbprovince());
					cwbOrderView.setCwbcity(c.getCwbcity());
					cwbOrderView.setCwbcounty(c.getCwbcounty());
					cwbOrderView.setConsigneeaddress(c.getConsigneeaddress());
					cwbOrderView.setConsigneename(c.getConsigneename());
					cwbOrderView.setConsigneemobile(c.getConsigneemobile());
					cwbOrderView.setConsigneephone(c.getConsigneephone());
					cwbOrderView.setConsigneepostcode(c.getConsigneepostcode());
					cwbOrderView.setResendtime(c.getResendtime() == null ? "" : c.getResendtime());
					cwbOrderView.setOutstoreroomtime(comm.getCredate());
					cwbOrderView.setCustomerid(c.getCustomerid());
					cwbOrderView.setCustomerwarehouseid(c.getCustomerwarehouseid());
					cwbOrderView.setInhouse(this.getQueryBranchName(branchList, Integer.parseInt(c.getCarwarehouse() == "" ? "0" : c.getCarwarehouse())));// 入库仓库
					cwbOrderView.setCurrentbranchname(this.getQueryBranchName(branchList, c.getCurrentbranchid()));// 当前所在机构名称
					cwbOrderView.setStartbranchname(this.getQueryBranchName(branchList, c.getStartbranchid()));// 上一站机构名称
					cwbOrderView.setNextbranchname(this.getQueryBranchName(branchList, c.getNextbranchid()));// 下一站机构名称
					cwbOrderView.setDeliverybranch(this.getQueryBranchName(branchList, c.getDeliverybranchid()));// 配送站点
					cwbOrderView.setRealweight(c.getCarrealweight());
					cwbOrderView.setCwbremark(c.getCwbremark());
					cwbOrderView.setReceivablefee(c.getReceivablefee());
					cwbOrderView.setCaramount(c.getCaramount());
					cwbOrderView.setPaybackfee(c.getPaybackfee());

					cwbOrderViewList.add(cwbOrderView);
				}

			}
		}
		return cwbOrderViewList;
	}

	public String getQueryBranchName(List<Branch> branchList, long branchid) {
		String branchname = "";
		for (Branch b : branchList) {
			if (b.getBranchid() == branchid) {
				branchname = b.getBranchname();
				break;
			}
		}
		return branchname;
	}

	public String auditCommen(String commencodes, long startbranchid, int outbranchflag,int maxCount) {
		//SystemInstall omsPathUrl = systemInstallDAO.getSystemInstallByName("omsPathUrl");
		SystemInstall omsUrl = systemInstallDAO.getSystemInstallByName("omsUrl");
		String url1 = "";
		if (omsUrl != null) {
			url1 =  omsUrl.getValue();
		} else {
			url1 = "http://127.0.0.1:8080/oms/";
		}
		final String url = url1;

		List<Customer> cList = customerDAO.getAllCustomersByYPDJ();
		String custids = "";
		if (cList != null && cList.size() > 0) {
			for (Customer customer : cList) {
				custids += "'" + customer.getCustomerid() + "',";
			}
		}
		custids = custids.length() > 0 ? custids.substring(0, custids.length() - 1) : "";
		String cwbStr = "";
		String quejiancwbStr = "";

		List<String> cwbList = new ArrayList<String>();
		if (custids.length() > 0) {
			List<WarehouseToCommen> commenList = warehouseToCommenDAO.getCommenCwbListByCommencodes(commencodes, startbranchid, outbranchflag,maxCount);
			if (commenList != null && commenList.size() > 0) {
				for (WarehouseToCommen warehouseToCommen : commenList) {
					cwbStr += "'" + warehouseToCommen.getCwb() + "',";
				}
			}
			cwbStr = cwbStr.length() > 0 ? cwbStr.substring(0, cwbStr.length() - 1) : "";
			if (cwbStr.length() > 0) {
				cwbList = cwbDAO.getCwbByYPDJ(custids, cwbStr);
			}
		}
		if (cwbList != null && cwbList.size() > 0) {
			for (String str : cwbList) {
				quejiancwbStr += "'" + str + "',";
			}
		}
		String msg = "确认出库给承运商成功";
		quejiancwbStr = quejiancwbStr.length() > 0 ? quejiancwbStr.substring(0, quejiancwbStr.length() - 1) : "";
		if (quejiancwbStr.length() > 0) {
			//承运商出库时缺件补件数
			cwbDAO.updateSendCarNumEqualScannum(quejiancwbStr);
			//msg = "存在一票多件的订单没有把所有运单号做完出库扫描，请查看明细！";
		}
		for (String commencode : commencodes.split(",")) {
			final String emaildate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			JSONObject json = new JSONObject();
			String code = commencode.substring(1, commencode.length() - 1);
			json.put("commencode", code);
			json.put("emaildate", emaildate);
			long emaildateid1 = 0;
			try {
				String result = JSONReslutUtil.getResultMessageChangeLog(url + "/OMSExplink/emaildate", "pram=" + json.toString(), "POST").toString();
				JSONObject rjson = JSONObject.fromObject(result);
				emaildateid1 = rjson.getLong("emaildateid");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				return "oms路径错误！请修改一下系统设置中的omsPathUrl";
			}
			final long emaildateid = emaildateid1;
			if (emaildateid == -1) {
				continue;
			}

			// 过滤非配送、上门退、上门换的订单  modify by jian_xie 2016-08-09
			String sql = "select o.id as id, o.cwb as cwb, o.customerid as customerid, o.startbranchid as startbranchid "
					+ " , o.commencode as commencode, o.credate as credate, o.statetime as statetime, o.nextbranchid as nextbranchid "
					+ " from  commen_cwb_order o inner join express_ops_cwb_detail d on o.cwb=d.cwb " 
					+ " where d.cwbordertypeid not in (1,2,3) and o.commencode =" + commencode + " and o.stateTime='' and o.outbranchflag=" + outbranchflag;
//			String sql = "select * from  commen_cwb_order  " + " where commencode =" + commencode + " and stateTime='' and outbranchflag=" + outbranchflag;
			/*if (quejiancwbStr.length() > 0) {
				sql = sql + " and cwb not in(" + quejiancwbStr + ")";
			}*/
			if (startbranchid > 0) {
				sql += " and o.startbranchid=" + startbranchid;
			}
			jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					try {
						WarehouseToCommen warehtoCommen = new WarehouseToCommen();
						long id = rs.getLong("id");
						warehtoCommen.setId(id);
						String cwb = rs.getString("cwb");
						warehtoCommen.setCwb(cwb);
						warehtoCommen.setCustomerid(rs.getLong("customerid"));
						warehtoCommen.setStartbranchid(rs.getLong("startbranchid"));
						warehtoCommen.setCommencode(rs.getString("commencode"));
						warehtoCommen.setCredate(rs.getString("credate"));
						warehtoCommen.setStatetime(rs.getString("statetime"));
						warehtoCommen.setEmaildateid(emaildateid);
						warehtoCommen.setNextbranchid(rs.getInt("nextbranchid"));

						try {
							String str = JSONReslutUtil.getResultMessageChangeLog(url + "/OMSExplink/postdata", "pram=" + om.writeValueAsString(warehtoCommen), "POST").toString();
							logger.info("{}返回：{}", cwb, str);
							if (str.indexOf("00") > -1) {
								warehouseToCommenDAO.updateCommenCwbListById(id, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							}
						} catch (Exception e) {
							logger.error("获取当前用户信息异常", e);
						}

					} catch (Exception e) {
						logger.error("", e);
					}
				}
			});

		}
		return msg;
	}

	/**
	 * 模拟补发承运商确认出库
	 * 
	 * @param commencodes
	 * @return
	 */
	public String auditCommen_imitate(String commencodes, WarehouseToCommen warehtoCommen, long startbranchid, int outbranchflag) {
		//SystemInstall omsPathUrl = systemInstallDAO.getSystemInstallByName("omsPathUrl");
		SystemInstall omsUrl = systemInstallDAO.getSystemInstallByName("omsUrl");
		String url1 = "";
		if (omsUrl != null) {
			url1 =omsUrl.getValue();
		} else {
			url1 = "http://127.0.0.1:8080/oms/";
		}
		final String url = url1;

		List<Customer> cList = customerDAO.getAllCustomersByYPDJ();
		String custids = "";
		if (cList != null && cList.size() > 0) {
			for (Customer customer : cList) {
				custids += "'" + customer.getCustomerid() + "',";
			}
		}
		custids = custids.length() > 0 ? custids.substring(0, custids.length() - 1) : "";
		String cwbStr = "";
		String quejiancwbStr = "";

		List<String> cwbList = new ArrayList<String>();
		if (custids.length() > 0) {
			List<WarehouseToCommen> commenList = warehouseToCommenDAO.getCommenCwbListByCommencodes(commencodes, startbranchid, outbranchflag,0);
			if (commenList != null && commenList.size() > 0) {
				for (WarehouseToCommen warehouseToCommen : commenList) {
					cwbStr += "'" + warehouseToCommen.getCwb() + "',";
				}
			}
			cwbStr = cwbStr.length() > 0 ? cwbStr.substring(0, cwbStr.length() - 1) : "";
			if (cwbStr.length() > 0) {
				cwbList = cwbDAO.getCwbByYPDJ(custids, cwbStr);
			}
		}
		if (cwbList != null && cwbList.size() > 0) {
			for (String str : cwbList) {
				quejiancwbStr += "'" + str + "',";
			}
		}
		String msg = "确认出库给承运商成功";
		quejiancwbStr = quejiancwbStr.length() > 0 ? quejiancwbStr.substring(0, quejiancwbStr.length() - 1) : "";
		if (quejiancwbStr.length() > 0) {
			msg = "存在一票多件的订单没有把所有运单号做完出库扫描，请查看明细！";
		}
		for (String commencode : commencodes.split(",")) {
			final String emaildate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			JSONObject json = new JSONObject();
			json.put("commencode", commencode);
			json.put("emaildate", emaildate);
			long emaildateid1 = 0;
			try {
				String result = JSONReslutUtil.getResultMessageChangeLog(url + "/OMSExplink/emaildate", "pram=" + json.toString(), "POST").toString();
				JSONObject rjson = JSONObject.fromObject(result);
				emaildateid1 = rjson.getLong("emaildateid");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				return "oms路径错误！请修改一下系统设置中的omsPathUrl";
			}
			final long emaildateid = emaildateid1;
			if (emaildateid == -1) {
				continue;
			}

			warehtoCommen.setEmaildateid(emaildateid);

			try {
				String str = JSONReslutUtil.getResultMessageChangeLog(url + "/OMSExplink/postdata", "pram=" + om.writeValueAsString(warehtoCommen), "POST").toString();
				logger.info("{}返回：{}", warehtoCommen.getCwb(), str);

			} catch (Exception e) {
				logger.error("获取当前用户信息异常", e);
			}

		}
		return msg;
	}

}
