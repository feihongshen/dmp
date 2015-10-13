package cn.explink.controller;

import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.b2c.dangdang.DangDang;
import cn.explink.b2c.dangdang.DangDangService;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.B2CData;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.service.FlowFromJMSService;

@Controller
@RequestMapping("/orderflow")
public class OrderFlowController {

	@Autowired
	CwbDAO cwbDao;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	B2CDataDAO b2CDataDAO;
	@Autowired
	GetDmpDAO getDmpDao;
	@Autowired
	DangDangService dangdangService;

	@Autowired
	private FlowFromJMSB2cService flowFromJMSB2cService;

	@Autowired
	private FlowFromJMSService flowFromJMSService;

	private Logger logger = LoggerFactory.getLogger(OrderFlowController.class);

	@RequestMapping("/view/{cwb}")
	public String view(@PathVariable("cwb") String cwb, Model model) {
		model.addAttribute("orderFlowList", orderFlowDAO.getOrderFlowListByCwb(cwb));
		model.addAttribute("cwb", cwbDao.getCwbByCwb(cwb));
		return "orderflow/view";
	}

	@RequestMapping("/ruku")
	public String ruku() {
		try {
			List<CwbOrder> list = cwbDao.getCwbsByDuijieType(1, 1);// 入库
			if (list != null && list.size() > 0) {
				for (CwbOrder c : list) {
					if (b2CDataDAO.check(c.getCwb(), FlowOrderTypeEnum.RuKu.getValue()) == 0) {
						List<OrderFlow> listlow = orderFlowDAO.getOrderFlowByFlowordertypeAndCwbAndIsgo(FlowOrderTypeEnum.RuKu.getValue(), c.getCwb());
						String userName = "";
						if (listlow != null && listlow.size() > 0) {
							userName = listlow.get(0).getUsername();
						}
						logger.info("cwb:" + c.getCwb() + " 更新入库扫描 存入对接需要的数据表");
						B2CData b2CData = new B2CData();
						b2CData.setCwb(c.getCwb());
						b2CData.setCustomerid(c.getCustomerid());
						b2CData.setFlowordertype(FlowOrderTypeEnum.RuKu.getValue());
						b2CData.setPosttime(c.getInstoreroomtime());
						b2CData.setSend_b2c_flag(0);
						JSONObject jsoncontent = new JSONObject();
						jsoncontent.put("cwb", c.getCwb());
						jsoncontent.put("in_storage_date", c.getInstoreroomtime());
						jsoncontent.put("operator", userName);
						b2CData.setJsoncontent(jsoncontent.toString());
						b2CDataDAO.saveB2CData(b2CData);
					}
				}

			}
			logger.info(" 更新入库扫描 存入对接需要的数据表 完成");
		} catch (Exception e) {
			logger.info("更新入库扫描 存入对接需要的数据表  异常");
			e.printStackTrace();
		}
		return "ok";
	}

	@RequestMapping("/linghuo")
	public String linghuo() {
		try {
			List<CwbOrder> list = cwbDao.getCwbsByDuijieType(2, 1);// 领货
			if (list != null && list.size() > 0) {
				for (CwbOrder c : list) {
					if (b2CDataDAO.check(c.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue()) == 0) {
						logger.info("cwb:" + c.getCwb() + " 更新小件员领货 存入对接需要的数据表");
						List<OrderFlow> listlow = orderFlowDAO.getOrderFlowByFlowordertypeAndCwbAndIsgo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), c.getCwb());
						String userName = "";
						if (listlow != null && listlow.size() > 0) {
							userName = listlow.get(0).getUsername();
						}
						B2CData b2CData = new B2CData();
						b2CData.setCwb(c.getCwb());
						b2CData.setCustomerid(c.getCustomerid());
						b2CData.setFlowordertype(FlowOrderTypeEnum.FenZhanLingHuo.getValue());
						b2CData.setPosttime(c.getPickGoodstime());
						b2CData.setSend_b2c_flag(0);
						JSONObject jsoncontent = new JSONObject();
						jsoncontent.put("cwb", c.getCwb());
						jsoncontent.put("out_storage_date", c.getPickGoodstime());
						jsoncontent.put("operator", userName);
						String delivername = "";
						String deliverphone = "";
						try {
							String deliverJSON = getDmpDao.getDeliverById(c.getDeliverid());
							JSONObject jobject = JSONObject.fromObject(deliverJSON);
							delivername = jobject.getString("realname");
							deliverphone = jobject.getString("usermobile");
						} catch (Exception e) {
						}
						jsoncontent.put("express_operator_name", delivername);
						jsoncontent.put("express_operator_tel", deliverphone);
						jsoncontent.put("express_operator_id", c.getDeliverid());
						b2CData.setJsoncontent(jsoncontent.toString());
						b2CDataDAO.saveB2CData(b2CData);
					}
				}

			}
			logger.info("更新小件员领货 存入对接需要的数据表  完成");
		} catch (Exception e) {
			logger.info("更新小件员领货 存入对接需要的数据表  异常");
			e.printStackTrace();
		}
		return "ok";
	}

	@RequestMapping("/ruku2")
	public String ruku2() {
		try {
			List<CwbOrder> list = cwbDao.getCwbsByDuijieType(1, 2);// 入库
			if (list != null && list.size() > 0) {
				for (CwbOrder c : list) {
					if (b2CDataDAO.check(c.getCwb(), FlowOrderTypeEnum.RuKu.getValue()) == 0) {
						List<OrderFlow> listlow = orderFlowDAO.getOrderFlowByFlowordertypeAndCwbAndIsgo(FlowOrderTypeEnum.RuKu.getValue(), c.getCwb());
						String userName = "";
						if (listlow != null && listlow.size() > 0) {
							userName = listlow.get(0).getUsername();
						}
						logger.info("cwb:" + c.getCwb() + " 更新入库扫描 存入对接需要的数据表");
						B2CData b2CData = new B2CData();
						b2CData.setCwb(c.getCwb());
						b2CData.setCustomerid(c.getCustomerid());
						b2CData.setFlowordertype(FlowOrderTypeEnum.RuKu.getValue());
						b2CData.setPosttime(c.getInstoreroomtime());
						b2CData.setSend_b2c_flag(1);
						JSONObject jsoncontent = new JSONObject();
						jsoncontent.put("cwb", c.getCwb());
						jsoncontent.put("in_storage_date", c.getInstoreroomtime());
						jsoncontent.put("operator", userName);
						b2CData.setJsoncontent(jsoncontent.toString());
						b2CDataDAO.saveB2CData(b2CData);
					}
				}

			}
			logger.info(" 更新入库扫描 存入对接需要的数据表 完成");
		} catch (Exception e) {
			logger.info("更新入库扫描 存入对接需要的数据表  异常");
			e.printStackTrace();
		}
		return "ok";
	}

	@RequestMapping("/linghuo2")
	public String linghuo2() {
		try {
			List<CwbOrder> list = cwbDao.getCwbsByDuijieType(2, 2);// 领货
			if (list != null && list.size() > 0) {
				for (CwbOrder c : list) {
					if (b2CDataDAO.check(c.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue()) == 0) {
						logger.info("cwb:" + c.getCwb() + " 更新小件员领货 存入对接需要的数据表");
						List<OrderFlow> listlow = orderFlowDAO.getOrderFlowByFlowordertypeAndCwbAndIsgo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), c.getCwb());
						String userName = "";
						if (listlow != null && listlow.size() > 0) {
							userName = listlow.get(0).getUsername();
						}
						B2CData b2CData = new B2CData();
						b2CData.setCwb(c.getCwb());
						b2CData.setCustomerid(c.getCustomerid());
						b2CData.setFlowordertype(FlowOrderTypeEnum.FenZhanLingHuo.getValue());
						b2CData.setPosttime(c.getPickGoodstime());
						b2CData.setSend_b2c_flag(1);
						JSONObject jsoncontent = new JSONObject();
						jsoncontent.put("cwb", c.getCwb());
						jsoncontent.put("out_storage_date", c.getPickGoodstime());
						jsoncontent.put("operator", userName);
						String delivername = "";
						String deliverphone = "";
						try {
							String deliverJSON = getDmpDao.getDeliverById(c.getDeliverid());
							JSONObject jobject = JSONObject.fromObject(deliverJSON);
							delivername = jobject.getString("realname");
							deliverphone = jobject.getString("usermobile");
						} catch (Exception e) {
						}
						jsoncontent.put("express_operator_name", delivername);
						jsoncontent.put("express_operator_tel", deliverphone);
						jsoncontent.put("express_operator_id", c.getDeliverid());
						b2CData.setJsoncontent(jsoncontent.toString());
						b2CDataDAO.saveB2CData(b2CData);
					}
				}

			}
			logger.info("更新小件员领货 存入对接需要的数据表  完成");
		} catch (Exception e) {
			logger.info("更新小件员领货 存入对接需要的数据表  异常");
			e.printStackTrace();
		}
		return "ok";
	}

	@RequestMapping("/fankui")
	public String fankui() {
		try {
			List<CwbOrder> list = cwbDao.getCwbsByDuijieType(3, 1);// 领货
			if (list != null && list.size() > 0) {
				for (CwbOrder c : list) {
					if (b2CDataDAO.check(c.getCwb(), c.getOrderResultType()) == 0) {
						List<OrderFlow> listlow = orderFlowDAO.getOrderFlowByFlowordertypeAndCwbAndIsgo(c.getOrderResultType(), c.getCwb());
						String userName = "";
						if (listlow != null && listlow.size() > 0) {
							userName = listlow.get(0).getUsername();
						}
						B2CData b2CData = new B2CData();
						b2CData.setCwb(c.getCwb());
						b2CData.setCustomerid(c.getCustomerid());
						b2CData.setFlowordertype(c.getFlowordertype());
						b2CData.setPosttime(c.getGoclasstime());
						b2CData.setSend_b2c_flag(0);
						JSONObject jsoncontent = new JSONObject();
						jsoncontent.put("cwb", c.getCwb());
						jsoncontent.put("sign_date", c.getSignintime());
						jsoncontent.put("sign_person", c.getSigninman());
						jsoncontent.put("operator", userName);
						String order_status = orderDeliverStateByStatus(c.getOrderResultType());
						jsoncontent.put("order_status", order_status);
						String reason_code = "0";
						try {
							DangDang dangdang = dangdangService.getDangDangSettingMethod(B2cEnum.DangDang.getKey());
							reason_code = getExptReasonBydangdang(order_status, c.getLeavedreasonid(), c.getBackreasonid(), dangdang).getExpt_code();
						} catch (Exception e) {
							// TODO Auto-generated catch block
						}
						jsoncontent.put("reason", reason_code == null ? "0" : reason_code);
						b2CData.setJsoncontent(jsoncontent.toString());
						b2CDataDAO.saveB2CData(b2CData);
					}
				}

			}
			logger.info("更新反馈 存入对接需要的数据表  完成");
		} catch (Exception e) {
			logger.info("更新反馈 存入对接需要的数据表  异常");
			e.printStackTrace();
		}
		return "ok";
	}

	@RequestMapping("/fankui2")
	public String fankui2() {
		try {
			List<CwbOrder> list = cwbDao.getCwbsByDuijieType(3, 2);// 领货
			if (list != null && list.size() > 0) {
				for (CwbOrder c : list) {
					if (b2CDataDAO.check(c.getCwb(), c.getOrderResultType()) == 0) {
						List<OrderFlow> listlow = orderFlowDAO.getOrderFlowByFlowordertypeAndCwbAndIsgo(c.getOrderResultType(), c.getCwb());
						String userName = "";
						if (listlow != null && listlow.size() > 0) {
							userName = listlow.get(0).getUsername();
						}
						B2CData b2CData = new B2CData();
						b2CData.setCwb(c.getCwb());
						b2CData.setCustomerid(c.getCustomerid());
						b2CData.setFlowordertype(c.getFlowordertype());
						b2CData.setPosttime(c.getGoclasstime());
						b2CData.setSend_b2c_flag(0);
						JSONObject jsoncontent = new JSONObject();
						jsoncontent.put("cwb", c.getCwb());
						jsoncontent.put("sign_date", c.getSignintime());
						jsoncontent.put("sign_person", c.getSigninman());
						jsoncontent.put("operator", userName);
						String order_status = orderDeliverStateByStatus(c.getOrderResultType());
						jsoncontent.put("order_status", order_status);
						String reason_code = "0";
						try {
							DangDang dangdang = dangdangService.getDangDangSettingMethod(B2cEnum.DangDang.getKey());
							reason_code = getExptReasonBydangdang(order_status, c.getLeavedreasonid(), c.getBackreasonid(), dangdang).getExpt_code();
						} catch (Exception e) {
							// TODO Auto-generated catch block
						}
						jsoncontent.put("reason", reason_code == null ? "0" : reason_code);
						b2CData.setJsoncontent(jsoncontent.toString());
						b2CDataDAO.saveB2CData(b2CData);
					}
				}

			}
			logger.info("更新反馈 存入对接需要的数据表  完成");
		} catch (Exception e) {
			logger.info("更新反馈 存入对接需要的数据表  异常");
			e.printStackTrace();
		}
		return "ok";
	}

	@RequestMapping("/fankui_byOrder")
	public String xiufutuisong(Model model, @RequestParam(value = "cwbo", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "typeo", required = false, defaultValue = "1") long type) {
		StringBuffer w = new StringBuffer();
		w.append("");
		if (!cwbs.equals("")) {
			for (String cwb : cwbs.split("\r\n")) {
				w.append("'" + cwb.trim() + "',");
			}
			if (type == 1) {
				try {
					List<CwbOrder> list = cwbDao.getCwbsByDuijieType(1, 2);// 入库
					if (list != null && list.size() > 0) {
						for (CwbOrder c : list) {
							if (b2CDataDAO.check(c.getCwb(), FlowOrderTypeEnum.RuKu.getValue()) == 0) {
								List<OrderFlow> listlow = orderFlowDAO.getOrderFlowByFlowordertypeAndCwbAndIsgo(FlowOrderTypeEnum.RuKu.getValue(), c.getCwb());
								String userName = "";
								if (listlow != null && listlow.size() > 0) {
									userName = listlow.get(0).getUsername();
								}
								logger.info("cwb:" + c.getCwb() + " 更新入库扫描 存入对接需要的数据表");
								B2CData b2CData = new B2CData();
								b2CData.setCwb(c.getCwb());
								b2CData.setCustomerid(c.getCustomerid());
								b2CData.setFlowordertype(FlowOrderTypeEnum.RuKu.getValue());
								b2CData.setPosttime(c.getInstoreroomtime());
								b2CData.setSend_b2c_flag(1);
								JSONObject jsoncontent = new JSONObject();
								jsoncontent.put("cwb", c.getCwb());
								jsoncontent.put("in_storage_date", c.getInstoreroomtime());
								jsoncontent.put("operator", userName);
								b2CData.setJsoncontent(jsoncontent.toString());
								b2CDataDAO.saveB2CData(b2CData);
							}
						}

					}
					logger.info(" 更新入库扫描 存入对接需要的数据表 完成");
				} catch (Exception e) {
					logger.info("更新入库扫描 存入对接需要的数据表  异常");
					e.printStackTrace();
				}
			} else if (type == 2) {
				try {
					List<CwbOrder> list = cwbDao.getCwbOrderByCwbs(w.substring(0, w.length() - 1).toString());
					if (list != null && list.size() > 0) {
						for (CwbOrder c : list) {
							if (b2CDataDAO.check(c.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue()) == 0) {
								logger.info("cwb:" + c.getCwb() + " 更新小件员领货 存入对接需要的数据表");
								List<OrderFlow> listlow = orderFlowDAO.getOrderFlowByFlowordertypeAndCwbAndIsgo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), c.getCwb());
								String userName = "";
								if (listlow != null && listlow.size() > 0) {
									userName = listlow.get(0).getUsername();
								}
								B2CData b2CData = new B2CData();
								b2CData.setCwb(c.getCwb());
								b2CData.setCustomerid(c.getCustomerid());
								b2CData.setFlowordertype(FlowOrderTypeEnum.FenZhanLingHuo.getValue());
								b2CData.setPosttime(c.getPickGoodstime());
								b2CData.setSend_b2c_flag(0);
								JSONObject jsoncontent = new JSONObject();
								jsoncontent.put("cwb", c.getCwb());
								jsoncontent.put("out_storage_date", c.getPickGoodstime());
								jsoncontent.put("operator", userName);
								String delivername = "";
								String deliverphone = "";
								try {
									String deliverJSON = getDmpDao.getDeliverById(c.getDeliverid());
									JSONObject jobject = JSONObject.fromObject(deliverJSON);
									delivername = jobject.getString("realname");
									deliverphone = jobject.getString("usermobile");
								} catch (Exception e) {
								}
								jsoncontent.put("express_operator_name", delivername);
								jsoncontent.put("express_operator_tel", deliverphone);
								jsoncontent.put("express_operator_id", c.getDeliverid());
								b2CData.setJsoncontent(jsoncontent.toString());
								b2CDataDAO.saveB2CData(b2CData);
							}
						}

					}
					logger.info("更新小件员领货 存入对接需要的数据表  完成");
				} catch (Exception e) {
					logger.info("更新小件员领货 存入对接需要的数据表  异常");
					e.printStackTrace();
				}
			} else {
				try {
					List<CwbOrder> list = cwbDao.getCwbOrderByCwbs(w.substring(0, w.length() - 1).toString());// 领货
					if (list != null && list.size() > 0) {
						for (CwbOrder c : list) {
							if (b2CDataDAO.check(c.getCwb(), c.getOrderResultType()) == 0) {
								List<OrderFlow> listlow = orderFlowDAO.getOrderFlowByFlowordertypeAndCwbAndIsgo(c.getOrderResultType(), c.getCwb());
								String userName = "";
								if (listlow != null && listlow.size() > 0) {
									userName = listlow.get(0).getUsername();
								}
								B2CData b2CData = new B2CData();
								b2CData.setCwb(c.getCwb());
								b2CData.setCustomerid(c.getCustomerid());
								b2CData.setFlowordertype(c.getFlowordertype());
								b2CData.setPosttime(c.getGoclasstime());
								b2CData.setSend_b2c_flag(0);
								JSONObject jsoncontent = new JSONObject();
								jsoncontent.put("cwb", c.getCwb());
								jsoncontent.put("sign_date", c.getSignintime());
								jsoncontent.put("sign_person", c.getSigninman());
								jsoncontent.put("operator", userName);
								String order_status = orderDeliverStateByStatus(c.getOrderResultType());
								jsoncontent.put("order_status", order_status);
								String reason_code = "0";
								try {
									DangDang dangdang = dangdangService.getDangDangSettingMethod(B2cEnum.DangDang.getKey());
									reason_code = getExptReasonBydangdang(order_status, c.getLeavedreasonid(), c.getBackreasonid(), dangdang).getExpt_code();
								} catch (Exception e) {
									// TODO Auto-generated catch block
								}
								jsoncontent.put("reason", reason_code == null ? "0" : reason_code);
								b2CData.setJsoncontent(jsoncontent.toString());
								b2CDataDAO.saveB2CData(b2CData);
							}
						}

					}
					logger.info("更新反馈 存入对接需要的数据表  完成");
				} catch (Exception e) {
					logger.info("更新反馈 存入对接需要的数据表  异常");
					e.printStackTrace();
				}

			}

		}
		model.addAttribute("result", "ok");
		return "/xiufu/xiufu";
	}

	@RequestMapping("/xiufu")
	public String xiufu(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwbs, @RequestParam(value = "type", required = false, defaultValue = "18") long type) {
		StringBuffer w = new StringBuffer();
		StringBuffer result = new StringBuffer();

		w.append("");
		if (!cwbs.equals("")) {
			for (String cwb : cwbs.split("\r\n")) {
				w.append("'" + cwb.trim() + "',");
			}
			result.append("/*===订单查询 oms 领货了但是还没有该状态的 ===*/\n");
			result.append("SELECT DISTINCT cwb FROM express_ops_order_flow WHERE cwb IN(" + w.substring(0, w.length() - 1).toString() + ") AND flowordertype = 9 AND flowordertype<>" + type
					+ " AND isgo=1 ;\n\n\n");

		}
		model.addAttribute("result", result.toString());
		model.addAttribute("password", getDmpDao.getUserByUserName("admin"));
		return "/xiufu/xiufu";
	}

	@RequestMapping("/codxiufu")
	public String guibanxiufu(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwbs, @RequestParam(value = "cod", required = false, defaultValue = "") String cod) {
		StringBuffer w = new StringBuffer();
		StringBuffer result = new StringBuffer();
		StringBuffer updateFlow = new StringBuffer();
		w.append("");

		if (!cwbs.equals("") && !cod.equals("")) {
			String[] cods = cod.split("\r\n");
			String[] cwbstrs = cwbs.split("\r\n");
			for (int i = 0; i < cwbstrs.length; i++) {
				w.append("'" + cwbstrs[i].trim() + "',");

				updateFlow.append("UPDATE `sxymj_dmp`.`express_ops_cwb_detail` SET `receivablefee`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");
				updateFlow.append("UPDATE `sxymj_oms`.`express_ops_cwb_detail` SET `receivablefee`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");
				updateFlow.append("UPDATE `sxymj_dmp`.`express_ops_delivery_state` SET `businessfee`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");
				updateFlow.append("UPDATE `sxymj_oms`.`express_ops_delivery_state` SET `businessfee`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");
				updateFlow.append("UPDATE `sxymj_oms`.`ops_cwb_tihuo` SET `receivablefee`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");
				updateFlow.append("UPDATE `sxymj_oms`.`ops_delivery_chuku` SET `receivablefee`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");
				updateFlow.append("UPDATE `sxymj_oms`.`ops_delivery_daohuo` SET `receivablefee`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");
				updateFlow.append("UPDATE `sxymj_oms`.`ops_delivery_jushou` SET `receivablefee`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");
				updateFlow.append("UPDATE `sxymj_oms`.`ops_delivery_successful` SET `receivablefee`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");
				updateFlow.append("UPDATE `sxymj_oms`.`ops_delivery_zhiliu` SET `receivablefee`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");
				updateFlow.append("UPDATE `sxymj_oms`.`ops_zhongzhuan` SET `receivablefee`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");

			}

			result.append("SELECT b.`payupid` ,a.cwb,a.`gcaid`,a.`deliverystate`,a.`businessfee`,a.`cash`,a.`pos` FROM `xhm_dmp`.`express_ops_delivery_state` AS a ,`xhm_dmp`.express_ops_goto_class_auditing b "
					+ " WHERE  a.`gcaid` = b.id AND a.`state`=1 AND a.cwb IN(" + w.substring(0, w.length() - 1).toString() + ") ;\n\n\n");

			result.append("/*===修改订单金额===*/\n");
			result.append(updateFlow);

		}
		model.addAttribute("result", result.toString());
		return "/xiufu/codxiufu";
	}

	@RequestMapping("/adrxiufu")
	public String adrxiufu(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwbs, @RequestParam(value = "cod", required = false, defaultValue = "") String cod) {
		StringBuffer w = new StringBuffer();
		StringBuffer result = new StringBuffer();
		StringBuffer updateFlow = new StringBuffer();
		w.append("");

		if (!cwbs.equals("") && !cod.equals("")) {
			String[] cods = cod.split("\r\n");
			String[] cwbstrs = cwbs.split("\r\n");
			for (int i = 0; i < cwbstrs.length; i++) {
				w.append("'" + cwbstrs[i].trim() + "',");

				updateFlow.append("UPDATE `sxymj_dmp`.`express_ops_cwb_detail` SET `consigneeaddress`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");
				updateFlow.append("UPDATE `sxymj_oms`.`express_ops_cwb_detail` SET `consigneeaddress`='" + cods[i].trim() + "'    WHERE cwb='" + cwbstrs[i].trim() + "';\n");

			}

			result.append("/*===修改订单地址===*/\n");
			result.append(updateFlow);

		}
		model.addAttribute("result", result.toString());
		return "/xiufu/adrxiufu";
	}

	@RequestMapping("/userinsert")
	public String userinsert(Model model, @RequestParam(value = "realname", required = false, defaultValue = "") String realname,
			@RequestParam(value = "branchid", required = false, defaultValue = "") String branchid, @RequestParam(value = "username", required = false, defaultValue = "") String username,
			@RequestParam(value = "password", required = false, defaultValue = "") String password, @RequestParam(value = "roleid", required = false, defaultValue = "") String roleid,
			@RequestParam(value = "usermobile", required = false, defaultValue = "") String usermobile) {
		StringBuffer w = new StringBuffer();
		StringBuffer result = new StringBuffer();
		StringBuffer updateFlow = new StringBuffer();
		w.append("");

		if (!username.equals("") && !username.equals("")) {
			String[] realnametrs = realname.split("\r\n");
			String[] branchidtrs = branchid.split("\r\n");
			String[] usernametrs = username.split("\r\n");
			String[] passwordtrs = password.split("\r\n");
			String[] roleidtrs = roleid.split("\r\n");
			String[] usermobiletrs = usermobile.split("\r\n");
			for (int i = 0; i < realnametrs.length; i++) {

				updateFlow.append("insert into `express_set_user` (`realname`, `branchid`, `username`, `password`, `roleid`, `usermobile`,`showphoneflag`, `employeestatus`,`isImposedOutWarehouse`) "
						+ "values('" + realnametrs[i] + "','" + branchidtrs[i] + "','" + usernametrs[i] + "','" + passwordtrs[i] + "','" + roleidtrs[i] + "','" + usermobiletrs[i]
						+ "','1','1','1');\n");

			}

			result.append("/*===修改订单地址===*/\n");
			result.append(updateFlow);
			String dd = "UPDATE express_set_user SET usermobile='' WHERE usermobile=0;";
			result.append(dd);

		}
		model.addAttribute("result", result.toString());
		return "/xiufu/userinsert";
	}

	// 根据 配送结果 转化 为 当当提供的反馈码
	private String orderDeliverStateByStatus(long podresultid) {
		if (podresultid == DeliveryStateEnum.PeiSongChengGong.getValue() || podresultid == DeliveryStateEnum.ShangMenTuiChengGong.getValue()
				|| podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			return "101";
		} else if (podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return "102";
		} else if (podresultid == DeliveryStateEnum.QuanBuTuiHuo.getValue() || podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue() || podresultid == DeliveryStateEnum.HuoWuDiuShi.getValue()
				|| podresultid == DeliveryStateEnum.ShangMenJuTui.getValue() || podresultid == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			return "103";
		} else {
			return "error";
		}
	}

	private ExptReason getExptReasonBydangdang(String reasontype, long leavedreasonid, long backreasonid, DangDang dangdang) {
		ExptReason exptreason = null;
		if ("102".equals(reasontype)) { // 滞留
			exptreason = getDmpDao.getExptCodeJointByB2c(leavedreasonid, ReasonTypeEnum.BeHelpUp.getValue(), dangdang.getCustomerids());
			if (exptreason.getExpt_code() == null || exptreason.getExpt_code().equals("")) {
				exptreason.setExpt_code("299");
				exptreason.setExpt_msg("其他原因");
			}
		} else if ("103".equals(reasontype)) { // 拒收
			exptreason = getDmpDao.getExptCodeJointByB2c(backreasonid, ReasonTypeEnum.ReturnGoods.getValue(), dangdang.getCustomerids());
			if (exptreason.getExpt_code() == null || exptreason.getExpt_code().equals("")) {
				exptreason.setExpt_code("103");
				exptreason.setExpt_msg("客户拒收");
			}
		}

		return exptreason == null ? new ExptReason() : exptreason;
	}

	@RequestMapping("/yhdxiufu")
	public String yhdguibanxiufu(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String content,
			@RequestParam(value = "cod", required = false, defaultValue = "") String cod) {
		StringBuffer w = new StringBuffer();
		StringBuffer result = new StringBuffer();
		StringBuffer updateFlow = new StringBuffer();
		w.append("");
		if (!content.equals("")) {
			content = content.replaceAll("\\},\\{\"carrierShipmentCode\":\"", "\\},\n\\{\"carrierShipmentCode\":\""); // 替换},{"carrierShipmentCode":"
			content = content.replaceAll("\"shipmentCode\"", "\t\"shipmentCode\""); // 替换
																					// "shipmentCode"
			content = content.replaceAll(",\"toCollectAmount\"", "\t,\"toCollectAmount\""); // 替换
																							// \t,"toCollectAmount"
			content = content.replaceAll("\"toLocationName\":\"\"", "\t\"toLocationName\":\"\""); // 替换
																									// \t"toLocationName":""

			// content=content.replaceAll("\t\"shipmentCode\"", ""); //替换
			// \t"toLocationName":""
			int counts = 0;
			String content1[] = content.split("\n");

			System.out.println("长度=========" + content1.length);

			for (int i = 0; i < content1.length; i++) {
				String arrys[] = content1[i].split("\t");
				counts++;
				if (arrys == null || arrys.length <= 3) {
					continue;
				}

				String repacels = arrys[1] + "======" + arrys[3].replaceAll("toLocationName\":\"\"", "");
				if (!repacels.contains("shipmentCode") && !repacels.contains("weight") && !repacels.contains("======")) {
					continue;
				}

				String finishstr = repacels.replaceAll("shipmentCode", "").replaceAll(",", "").replaceAll("weight", "").replaceAll("\"", "").replaceAll(":", "").replaceAll("\\}", "")
						.replaceAll("]", "");

				String endstrs[] = finishstr.split("\n");

				String finishstr_arrs[] = endstrs[0].split("======");

				updateFlow.append("UPDATE `express_ops_cwb_detail` SET `carrealweight`=" + finishstr_arrs[1].trim() + "    WHERE cwb='" + finishstr_arrs[0].trim() + "';\n");

			}

			System.out.println("一号店订单查询大小 count=" + counts);
			result.append("/*===修改订单重量===*/\n");
			result.append(updateFlow);

		}
		model.addAttribute("result", result.toString());
		return "/xiufu/yhdxiufu";
	}

	@RequestMapping("/saveFlowB2cSend")
	public String saveFlowB2cSend(Model model, @RequestParam(value = "orderFlow", required = true) String orderFlow) {
		logger.info("in saveFlowB2cSend. orderFlow = " + orderFlow);
		try {
			flowFromJMSB2cService.saveFlowB2cSend(orderFlow);
			model.addAttribute("result", "success");
		} catch (Exception e) {
			model.addAttribute("result", "failure");
		}

		return "/common/httpCommunicationResult";
	}

	@RequestMapping("/saveFlow")
	public String saveFlow(Model model, @RequestParam(value = "orderFlow", required = true) String orderFlow) {
		logger.info("in saveFlow. orderFlow = " + orderFlow);
		try {
			flowFromJMSService.saveFlow(orderFlow);
			model.addAttribute("result", "success");
		} catch (Exception e) {
			model.addAttribute("result", "failure");
		}
		return "/common/httpCommunicationResult";
	}

}