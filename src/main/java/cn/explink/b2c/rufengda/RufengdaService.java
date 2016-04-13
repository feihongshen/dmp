package cn.explink.b2c.rufengda;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.User;
import cn.explink.service.CustomerService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.DES3Utils;

@Service
public class RufengdaService {
	private Logger logger = LoggerFactory.getLogger(RufengdaService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	JointService jointService;
	@Autowired
	Rufengda_Master rufengda_Master;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	UserDAO userDAO;
	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
	@Autowired
	CustomerService customerService;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Rufengda getRufengda(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Rufengda smile = (Rufengda) JSONObject.toBean(jsonObj, Rufengda.class);
		return smile;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Rufengda rfd = new Rufengda();
		String customerid = request.getParameter("customerid");
		rfd.setCustomerid(customerid);
		rfd.setLcId(request.getParameter("lcId"));
		rfd.setWs_url(request.getParameter("ws_url"));
		rfd.setDes_key(request.getParameter("des_key"));
		rfd.setExponentPublic(request.getParameter("exponentPublic"));
		rfd.setExponetPrivate(request.getParameter("exponetPrivate"));
		rfd.setIsopensignflag(Integer.parseInt(request.getParameter("isopensignflag")));
		rfd.setMaxCount(Integer.parseInt(request.getParameter("maxCount")));
		rfd.setModulePrk(request.getParameter("modulePrk"));
		rfd.setModulePuk(request.getParameter("modulePuk"));
		rfd.setNowtime(request.getParameter("nowtime"));
		rfd.setWarehouseid(Integer.valueOf(request.getParameter("warehouseid")));
		rfd.setLoopcount(Integer.valueOf(request.getParameter("loopcount")));

		String oldCustomerid = "";

		JSONObject jsonObj = JSONObject.fromObject(rfd);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerid = getRufengda(joint_num).getCustomerid();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerid, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	// /**
	// * 如风达定时器，查询临时表，插入数据到detail表中。
	// */
	// public void selectTempAndInsertToCwbDetail_temp(int rfd_key){
	// try {
	// Rufengda rfd=getRufengda(rfd_key);
	// int isOpenFlag=jointService.getStateForJoint(rfd_key);
	//
	// if (isOpenFlag == 0) {
	// logger.info("未开启[如风达]对接！---当前获取订单插入临时表-----");
	// return;
	// }
	// List<CwbOrderDTO>
	// cwbOrderList=dataImportDAO_B2c.getCwbOrderTempByKeys(rfd.getCustomerid());
	// if(cwbOrderList!=null&&cwbOrderList.size()>0){
	// for(CwbOrderDTO cwbOrder:cwbOrderList){
	// CwbOrder order=cwbDAO.getCwbByCwb(cwbOrder.getCwb());
	// if (order!=null) { // 要合单子
	// logger.warn("[如风达]查询临时表-检测到有重复数据,已过滤!订单号：{},运单号:{}",cwbOrder.getCwb(),cwbOrder.getShipcwb());
	// }else{
	// User user=new User();
	// user.setUserid(1);
	// long warehouse_id=rfd.getWarehouseid();
	// long warehouseid
	// =warehouse_id!=0?warehouse_id:dataImportService_B2c.getTempWarehouseIdForB2c();
	// //获取虚拟库房 Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
	// user.setBranchid(warehouseid);
	// EmailDate ed =
	// dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(),
	// 0,warehouseid);
	//
	// emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
	//
	// cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(),
	// ed.getWarehouseid(), user, ed);
	// logger.info("[如风达]定时器临时表插入detail表成功!cwb={},shipcwb={}",cwbOrder.getCwb(),cwbOrder.getShipcwb());
	//
	// if(cwbOrder.getExcelbranch()==null||cwbOrder.getExcelbranch().length()==0){
	// HashMap<String, Object> map=new HashMap<String, Object>();
	// map.put("cwb", cwbOrder.getCwb());
	// map.put("userid", "1");
	// addressmatch.sendBodyAndHeaders(null, map);
	// }
	// }
	// dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
	// }
	// }
	// } catch (Exception e) {
	// logger.error("[如风达]定时器临时表插入或修改方法执行异常!",e);
	// e.printStackTrace();
	// }
	// }

	public void selectTempAndInsertToCwbDetail(int rfd_key) {
		Rufengda rfd = getRufengda(rfd_key);
		int isOpenFlag = jointService.getStateForJoint(rfd_key);

		if (isOpenFlag == 0) {
			logger.info("未开启[如风达]对接！---当前获取订单插入临时表-----");
			return;
		}
		List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getCwbOrderTempByKeys(rfd.getCustomerid());

		if (cwbOrderList == null) {
			return;
		}
		if (cwbOrderList.size() == 0) {
			return;
		}

		int k = 1;
		int batch = 50;
		while (true) {

			int fromIndex = (k - 1) * batch;
			if (fromIndex >= cwbOrderList.size()) {
				break;
			}
			int toIdx = k * batch;
			if (k * batch > cwbOrderList.size()) {
				toIdx = cwbOrderList.size();
			}
			List<CwbOrderDTO> subList = cwbOrderList.subList(fromIndex, toIdx);
			ImportSubList(rfd.getCustomerid(), rfd.getWarehouseid(), subList);
			k++;
		}
		logger.info("执行了如风达定时器结束");

	}

	@Transactional
	public void ImportSubList(String customerid, long warehouseId, List<CwbOrderDTO> cwbOrderList) {

		for (CwbOrderDTO cwbOrder : cwbOrderList) {
			try {
				ImportSignOrder(customerid, warehouseId, cwbOrder);
			} catch (Exception e) {
				logger.error("定时器临时表插入或修改方法执行异常!customerid=" + customerid, e);
			}
		}
	}

	private void ImportSignOrder(String customerid, long warehouseId, CwbOrderDTO cwbOrder) {

		CwbOrder order = cwbDAO.getCwbByCwb(cwbOrder.getCwb());
		if (order != null) { // 要合单子
			logger.warn("[如风达]查询临时表-检测到有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
		} else {
			User user = new User();
			user.setUserid(1);
			long warehouse_id = warehouseId;
			long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
																													// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
			user.setBranchid(warehouseid);
			EmailDate ed = dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(), 0, warehouseid);

			emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());

			cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), ed.getWarehouseid(), user, ed);
			logger.info("[如风达]定时器临时表插入detail表成功!cwb={},shipcwb={}", cwbOrder.getCwb(), cwbOrder.getShipcwb());

			if (cwbOrder.getExcelbranch() == null || cwbOrder.getExcelbranch().length() == 0) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("cwb", cwbOrder.getCwb());
				map.put("userid", "1");
				try{
					addressmatch.sendBodyAndHeaders(null, map);
				}catch(Exception e){
					logger.error("", e);
					//写MQ异常表
					this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass() + "ImportSignOrder")
							.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
							.buildMessageHeaderObject(map).getMqException());
				}
			}
		}
		dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
	}

	/**
	 * 如风达定时器的总开关方法
	 */
	public long RufengdaInterfaceInvoke() {
		String customerids = "";
		long count = 0;
		int check = 0;
		String remark = "";
		String cretime = DateTimeUtil.getNowTime();
		try {

			for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
				if (enums.getMethod().contains("rufengda")) {
					int rfd_key = enums.getKey();
					int isOpenFlag = jointService.getStateForJoint(rfd_key);
					if (isOpenFlag == 0) {
						logger.warn("未开启[如风达]对接定时器rfd_key={}", rfd_key);
						continue;
					}
					Rufengda rfd = getRufengda(rfd_key);
					int loopcount = rfd.getLoopcount() == 0 ? 20 : rfd.getLoopcount(); // 查询循环次数
					for (int i = 0; i < loopcount; i++) {
						count += rufengda_Master.getRufengdaService_GetOrders().GetOrders(rfd_key, i + 1); // 获取订单
						rufengda_Master.getRufengdaService_SuccessOrders().SuccessOrders(rfd_key, i + 1); // 获取完成通知接口

					}

					selectTempAndInsertToCwbDetail(rfd_key); // 临时表数据插入到detail中
					check++;
					customerids += rfd.getCustomerid() + ",";
				}
			}
			if (check == 0) {
				return -1;
			}
		} catch (Exception e) {
			remark = "下载遇未知异常" + e.getMessage();
			logger.error(remark, e);
		}

		remark = !remark.isEmpty() && count == 0 ? "未下载到数据" : remark;
		if (count > 0) {
			remark = "下载完成";
		}
		b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(customerids.indexOf(",") > 0 ? customerids.substring(0, customerids.length() - 1) : customerids, cretime, count, remark);
		return count;

	}

	/**
	 * 如风达定时器的总开关方法
	 */
	public void RufengdaInterfaceSuccessOrderInvoke() {
		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("rufengda")) {
				int rfd_key = enums.getKey();
				int isOpenFlag = jointService.getStateForJoint(rfd_key);
				if (isOpenFlag == 0) {
					logger.warn("未开启[如风达]对接定时器rfd_key={}", rfd_key);
					continue;
				}
				rufengda_Master.getRufengdaService_SuccessOrders().SuccessOrders(rfd_key, 1); // 获取完成通知接口
			}
		}

	}

	/**
	 * 如风达 获取订单接口
	 * 
	 */
	public void RufengdaInterfaceGetOrdersInvoke() {

		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("rufengda")) {
				int rfd_key = enums.getKey();
				int isOpenFlag = jointService.getStateForJoint(rfd_key);
				if (isOpenFlag == 0) {
					logger.warn("未开启[如风达]对接定时器rfd_key={}", rfd_key);
					continue;
				}
				rufengda_Master.getRufengdaService_GetOrders().GetOrders(rfd_key, 1); // 获取订单
			}
		}

	}

	/**
	 * 如风达 配送员信息同步接口
	 * 
	 */
	public long RufengdaInterfaceSynUserInfoInvoke() {
		long calcCount = 0;
		int check = 0;
		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("rufengda")) {
				int rfd_key = enums.getKey();
				int isOpenFlag = jointService.getStateForJoint(rfd_key);
				if (isOpenFlag == 0) {
					logger.warn("未开启[如风达]对接定时器rfd_key={}", rfd_key);
					continue;
				}
				calcCount += rufengda_Master.getRufengdaService_SynUserInfo().SynUserInfo(rfd_key); // 配送信息同步
				check++;
			}
		}
		if (check == 0) {
			return -1;
		}
		return calcCount;

	}

	public void Reason() {
		String address = "PugG91vm04Mss310JInuOLtYbSd4ELoKWMw8wr/owgc=";
		// +PD4xvd8TwBinMMb6g6RtwenjbHxZgLrt9QB8Eh2SnHA5H90vvxN+Q==

		String key = "yBMiXHY34TwhuBv5gqllfZerg1qKXLSL" + "50" + "d7952104-dd28-44db-8fbd-94f78edcc0e6";

		// 解密后再加密

		String reAddress = "";
		try {
			reAddress = DES3Utils.decryptMode(address, key);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(reAddress);

	}

}
