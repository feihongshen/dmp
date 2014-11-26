package cn.explink.b2c.gome;

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
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.User;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.DES3Utils;

@Service
public class GomeService {
	private Logger logger = LoggerFactory.getLogger(GomeService.class);

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
	Gome_Master gome_Master;
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

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Gome getGome(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Gome smile = (Gome) JSONObject.toBean(jsonObj, Gome.class);
		return smile;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Gome gome = new Gome();
		String customerid = request.getParameter("customerid");
		gome.setCustomerid(customerid);
		gome.setTnt_url(request.getParameter("tnt_url"));
		gome.setFull_url(request.getParameter("full_url"));
		gome.setUsername(request.getParameter("username"));
		gome.setPassword(request.getParameter("pwd"));
		gome.setBusinessCode(request.getParameter("businessCode"));
		gome.setLspCode(request.getParameter("lspCode"));
		gome.setLspAbbr(request.getParameter("lspAbbr"));
		gome.setBuid(request.getParameter("buid"));
		gome.setMaxCount(Integer.parseInt(request.getParameter("maxCount")));
		gome.setCheckCount(Integer.parseInt(request.getParameter("checkCount")));
		gome.setLspName(request.getParameter("lspName"));
		gome.setLspPhoneNumber(request.getParameter("lspPhoneNumber"));
		gome.setWarehouseid(Integer.valueOf(request.getParameter("warehouseid")));

		String oldCustomerid = "";

		JSONObject jsonObj = JSONObject.fromObject(gome);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerid = getGome(joint_num).getCustomerid();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerid, joint_num);
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 国美定时器，查询临时表，插入数据到detail表中。
	 */
	public void selectTempAndInsertToCwbDetail() {
		try {
			Gome gome = getGome(B2cEnum.Gome.getKey());
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.Gome.getKey());

			if (isOpenFlag == 0) {
				logger.info("未开启[国美]对接！---当前获取订单插入临时表-----");
				return;
			}
			List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getCwbOrderTempByKeys(gome.getCustomerid());
			if (cwbOrderList != null && cwbOrderList.size() > 0) {
				for (CwbOrderDTO cwbOrder : cwbOrderList) {
					CwbOrder order = cwbDAO.getCwbByCwb(cwbOrder.getCwb());
					if (order != null) { // 要合单子
						logger.warn("[国美]查询临时表-检测到有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getTranscwb());
					} else {
						User user = new User();
						user.setUserid(1);
						long warehouse_id = gome.getWarehouseid();
						long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
																																// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
						user.setBranchid(warehouseid);
						EmailDate ed = dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(), 0, warehouseid);

						emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
						cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), ed.getWarehouseid(), user, ed);
						logger.info("[国美]定时器临时表插入detail表成功!cwb={},trancwb={}", cwbOrder.getCwb(), cwbOrder.getTranscwb());

						if (cwbOrder.getExcelbranch() == null || cwbOrder.getExcelbranch().length() == 0) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("cwb", cwbOrder.getCwb());
							map.put("userid", "1");
							addressmatch.sendBodyAndHeaders(null, map);
						}
					}
					dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
				}
			}
		} catch (Exception e) {
			logger.error("[国美]定时器临时表插入或修改方法执行异常!", e);
			e.printStackTrace();
		}
	}

	/**
	 * 国美定时器的总开关方法
	 */
	public long gomeInterfaceInvoke() {
		long count = 0;

		Gome gome = getGome(B2cEnum.Gome.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Gome.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启[国美]对接定时器");
			return -1;
		}
		for (int i = 0; i < (gome.getCheckCount() == 0 ? 1 : gome.getCheckCount()); i++) {
			count += gome_Master.getGomeService_GetOrders().getSoAndAsnCwbs(); // 获取订单
			gome_Master.getGomeService_GetOrders().getOrders(); // 获取订单

			selectTempAndInsertToCwbDetail(); // 临时表数据插入到detail中
			logger.info("[国美]对接定时器,循环获取第{}次", i);
		}

		b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(gome.getCustomerid(), DateTimeUtil.getNowTime(), count, "下载成功");

		return count;

	}

	/**
	 * 国美定时器的总开关方法
	 */
	public void gomeInterfaceSuccessOrderInvoke() {
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Gome.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启[国美]对接定时器");
			return;
		}
		// gome_Master.getGomeService_SuccessOrders().SuccessOrders();
		// //获取完成通知接口

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
