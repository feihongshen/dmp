package cn.explink.b2c.yonghui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.b2c.yonghui.domain.Content;
import cn.explink.b2c.yonghui.domain.Order;
import cn.explink.b2c.yonghui.domain.OrderBack;
import cn.explink.b2c.yonghui.domain.YongHui;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.service.CustomerService;
import cn.explink.service.CwbOrderService;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class YongHuiServices {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
	@Autowired
	JointService jointService;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	CustomerService customerService;

	public YongHui getYonghui(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		YongHui smile = (YongHui) JSONObject.toBean(jsonObj, YongHui.class);
		return smile;
	}

	public String getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		YongHui yh = new YongHui();
		String customerid = request.getParameter("customerid");
		yh.setCustomerid(Long.parseLong(request.getParameter("customerid")));
		yh.setWarehouserid(Long.parseLong(request.getParameter("warehouserid")));
		yh.setUserCode(request.getParameter("userCode"));
		yh.setPrivate_key(request.getParameter("private_key"));
		yh.setOrderState_url(request.getParameter("orderState_url"));
		yh.setOrderStateCount(Long.parseLong(request.getParameter("orderStateCount")));
		yh.setClient_id(request.getParameter("clientID"));
		yh.setSecret(request.getParameter("clientSecret"));
		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(yh);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = this.getYonghui(joint_num).getCustomerid() + "";
			} catch (Exception e) {
				this.logger.error("设置-永辉-对接异常", e);
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
			this.logger.info("设置-永辉-对接成功！");
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	public OrderBack orderBack(Content content) {
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Yonghui.getKey());
		OrderBack back = new OrderBack();
		if (isOpenFlag == 0) {
			this.logger.info("未开-永辉-我买网对接！");
			back.setErrCode(YongHuiExpEmum.XiTongYiChang.getErrCode());
			back.setErrMsg(YongHuiExpEmum.XiTongYiChang.getErrMsg());
			return back;
		}
		YongHui yh = this.getYonghui(B2cEnum.Yonghui.getKey());
		String usercode = content.getUserCode();
		String sign = content.getSign();
		String requestTime = content.getRequestTime();
		String md5Sign = MD5Util.md5(usercode + requestTime + yh.getPrivate_key());
		if (!usercode.equals(yh.getUserCode())) {
			back.setErrCode(YongHuiExpEmum.YongHuBuCunZai.getErrCode());
			back.setErrMsg(YongHuiExpEmum.YongHuBuCunZai.getErrMsg());
			return back;
		} else if (!md5Sign.equals(sign)) {
			back.setErrCode(YongHuiExpEmum.QianMingCuoWu.getErrCode());
			back.setErrMsg(YongHuiExpEmum.QianMingCuoWu.getErrMsg());
			return back;
		} else {
			List<Order> oList = content.getOrderList();
			List<Map<String, String>> cwbOrderList = this.parseCwbArrByOrderDto(oList);
			if ((cwbOrderList == null) || (cwbOrderList.size() == 0)) {
				this.logger.warn("请求-永辉-超市没有下载到订单数据!");
				back.setErrCode(YongHuiExpEmum.YeWuYiChang.getErrCode());
				back.setErrMsg(YongHuiExpEmum.YeWuYiChang.getErrMsg());
				return back;
			}
			try {
				long warehouseid = yh.getWarehouserid(); // 订单导入的库房Id
				this.dataImportInterface.Analizy_DataDealByB2c(yh.getCustomerid(), B2cEnum.Yonghui.getMethod(), cwbOrderList, warehouseid, true);

				List<String> cwbs = new ArrayList<String>();
				for (Order order : oList) {
					CwbOrderDTO cdo = this.dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getShipmentId());
					if (cdo == null) {
						cwbs.add(order.getShipmentId());
					}
				}
				if (cwbs.size() > 0) {
					this.logger.info("[-永辉-超市]下载订单信息调用数据导入接口-插入数据库部分成功!");
					back.setErrCode(YongHuiExpEmum.Bufenshibai.getErrCode());
					back.setErrMsg(YongHuiExpEmum.Bufenshibai.getErrMsg());
				} else {
					this.logger.info("[-永辉-超市]下载订单信息调用数据导入接口-插入数据库全部成功!");
					back.setErrCode(YongHuiExpEmum.Success.getErrCode());
					back.setErrMsg(YongHuiExpEmum.Success.getErrMsg());
				}
				back.setErrlist(cwbs);
				return back;

			} catch (Exception e) {
				this.logger.error("[-永辉-超市]调用数据导入接口异常!,订单List信息:" + cwbOrderList + "exptMessage=:", e);
				back.setErrCode(YongHuiExpEmum.XiTongYiChang.getErrCode());
				back.setErrMsg(YongHuiExpEmum.XiTongYiChang.getErrMsg());
				return back;
			}
		}

	}

	private List<Map<String, String>> parseCwbArrByOrderDto(List<Order> oList) {
		List<Map<String, String>> cwbList = null;

		if ((oList != null) && (oList.size() > 0)) {
			cwbList = new ArrayList<Map<String, String>>();
			for (Order order : oList) {
				CwbOrderDTO cwbOrder = this.dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getShipmentId());
				if (cwbOrder != null) {
					this.logger.warn("-永辉-接收订单接口_获取-永辉-订单中含有重复数据,cwb={}", cwbOrder.getCwb());
					continue;
				}
				String address = "";
				address += StringUtil.nullConvertToEmptyString(order.getDeliveryAddressProvince());
				address += StringUtil.nullConvertToEmptyString(order.getDeliveryAddressCity());
				address += StringUtil.nullConvertToEmptyString(order.getDeliveryAddressDistrict());
				address += StringUtil.nullConvertToEmptyString(order.getDeliveryAddressDetail());
				Map<String, String> cwbMap = new HashMap<String, String>();
				cwbMap.put("cwb", StringUtil.nullConvertToEmptyString(order.getShipmentId()));
				cwbMap.put("transcwb", StringUtil.nullConvertToEmptyString(order.getPackageNum()));
				cwbMap.put("consigneename", StringUtil.nullConvertToEmptyString(order.getRecipientName()));
				cwbMap.put("consigneeaddress", address);
				cwbMap.put("consigneemobile", StringUtil.nullConvertToEmptyString(order.getRecipientPhone()));
				cwbMap.put("cargorealweight", order.getCommodityWeight().toString());
				cwbMap.put("receivablefee", "0");
				cwbMap.put("caramount", order.getTotalPrice() + "");
				cwbMap.put("sendcarnum", "1");
				cwbMap.put("customercommand", StringUtil.nullConvertToEmptyString(order.getLogistics_name()));
				cwbMap.put("cwbordertypeid", "1");
				cwbMap.put("cwbremark", StringUtil.nullConvertToEmptyString(order.getPackageRemark()));
				cwbMap.put("remark1", StringUtil.nullConvertToEmptyString(order.getOrderType()));
				cwbMap.put("remark2", StringUtil.nullConvertToEmptyString(order.getBaseStore()));

				// cwbMap.put("multi_shipcwb", order.getBagno()); // 标识oms存储多次推送
				cwbList.add(cwbMap);
			}
		}
		return cwbList;
	}

}
