package cn.explink.service;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.PostConstruct;

import net.sf.json.JSONObject;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.dangdang.DangDang;
import cn.explink.b2c.dangdang.DangDangService;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.dao.BranchPayamountDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbOrderTailDao;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.GotoClassDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PosPayDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ReasonTypeEnum;

@Service
public class UpdateOrderFromJMSService {

	@Autowired
	private CamelContext camelContext;
	@Autowired
	private OrderFlowDAO orderFlowDAO;
	@Autowired
	private GetDmpDAO getDmpDAO;
	@Autowired
	private DeliveryStateDAO deliveryStateDAO;
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private BranchPayamountDAO branchPayamountDAO;
	@Autowired
	private GotoClassDAO gotoClassDAO;
	@Autowired
	PosPayService posPayService;
	@Autowired
	B2CDataDAO b2CDataDAO;
	@Autowired
	DangDangService dangdangService;
	@Autowired
	PosPayDAO posPayDAO;
	@Autowired
	CwbOrderTailDao cwbOrderTailDao;

	private Logger logger = LoggerFactory.getLogger(UpdateOrderFromJMSService.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

	@PostConstruct
	public void init() {
		try {
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {

					// from("jms:queue:VirtualTopicConsumers.oms1.pdaDeliverPod").to("bean:updateOrderFromJMSService?method=saveDeliverState").routeId("saveDeliverState_topic");
					from("jms:queue:VirtualTopicConsumers.oms1.gotoClass").to("bean:updateOrderFromJMSService?method=saveGotoClassAuditing").routeId("saveGotoClassAuditing_topic");
					from("jms:queue:VirtualTopicConsumers.oms1.PayUp").to("bean:updateOrderFromJMSService?method=saveBranchPayamount").routeId("saveBranchPayamount_topic");
					from("jms:queue:VirtualTopicConsumers.oms1.loseCwb").to("bean:updateOrderFromJMSService?method=updateLoseOrder").routeId("updateLoseOrder");
					// from("jms:queue:VirtualTopicConsumers.oms1.datachangerow").to("bean:updateOrderFromJMSService?method=updatedatachangerow").routeId("updatedatachangerow");
					from("jms:queue:VirtualTopicConsumers.oms1.batchedit").to("bean:updateOrderFromJMSService?method=updatebatchedit").routeId("updatebatchedit");
					// from("jms:queue:VirtualTopicConsumers.oms1.editexcel").to("bean:updateOrderFromJMSService?method=editexcel").routeId("editexcel");
					// from("jms:queue:VirtualTopicConsumers.oms1.editbackreason").to("bean:updateOrderFromJMSService?method=editbackreason").routeId("editbackreason_topic");
					// from("jms:queue:VirtualTopicConsumers.oms1.editleavereason").to("bean:updateOrderFromJMSService?method=editleavereason").routeId("editleavereason_topic");
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * jms 换列操作
	 * 
	 * @param parm
	 *            "{\"emaildateid\":1,\"changefield\":\"1111\",\"changefield1\":2222}"
	 */
	public void updatedatachangerow(@Header("row") String parm) {
		logger.info("RE：jms:queue:VirtualTopicConsumers.oms1.datachangerow ");
		try {
			JSONObject jsonValue = JSONObject.fromObject(parm);
			if (jsonValue != null) {
				int emaildateid = jsonValue.getInt("emaildateid");
				String changefield = jsonValue.getString("changefield");
				String changefield1 = jsonValue.getString("changefield1");
				cwbDAO.giveReserve(changefield, changefield1, emaildateid);
				cwbDAO.UpdateFlowOrderIfExistAmountLine(changefield, changefield1, emaildateid);
				logger.info("接收了数据换列操作，当前邮件批次号:" + emaildateid + ",changefield：" + changefield + ",changefield1:" + changefield1 + ";" + "更新了:"
						+ cwbDAO.changeField(changefield, changefield1, emaildateid) + "条数据");

			}
		} catch (Exception e) {
			logger.error("接收了数据换列处理异常");
			e.printStackTrace();
		}
	}

	/**
	 * 批量更新订单
	 * 
	 * @param parm
	 *            "{\"emaildateid\":1,\"editemaildate\":\"2012-09-02 15:00:00\",\"warehouseid\":1,\"areaid\":2}"
	 */
	public void updatebatchedit(@Header("emaildate") String parm) {
		logger.info("RE:jms:queue:VirtualTopicConsumers.oms1.batchedit ");
		try {
			JSONObject jsonValue = JSONObject.fromObject(parm);
			if (jsonValue != null) {
				int emaildateid = jsonValue.getInt("emaildateid");
				String editemaildate = jsonValue.getString("editemaildate");
				long warehouseid = jsonValue.getInt("warehouseid");
				long areaid = jsonValue.getInt("areaid");
				cwbDAO.dataBatchEditForDetail(warehouseid, areaid, editemaildate, emaildateid);
				cwbDAO.dataBatchEditForOrderFlow(editemaildate, emaildateid);
				logger.info("接收了批量更新的通知，当前邮件批次：" + emaildateid);
			}
		} catch (Exception e) {
			logger.error("接收了批量更新的通知处理异常");
			e.printStackTrace();
		}
	}

	/**
	 * 反馈表保存
	 * 
	 * @param parm
	 *            反馈的jms信息
	 *            "{"backcarname":"","backcarnum":0,"backreason":"","businessfee
	 *            ":12,
	 *            "cash":0,"checkfee":0,"checkremark":"","consigneeaddress":"",
	 *            "createtime":"2012-10-24 14:17:33","customerid":0,"cwb":"43",
	 *            "cwbordertypeid"
	 *            :"2","deliverealname":"蓝生","deliverstateremark":"",
	 *            "deliveryid"
	 *            :1003,"deliverystate":2,"difference":-12,"emaildate":"",
	 *            "flowordertype"
	 *            :0,"id":0,"isout":0,"leavedreason":"","mobilepodtime":null,
	 *            "otherfee"
	 *            :0,"paymentPattern":"现金","podremarkid":3,"pos":0,"posremark"
	 *            :"",
	 *            "receivedfee":0,"receivedfeeuser":0,"remarks":"　自定义：","returnedfee"
	 *            :12,
	 *            "sendcarname":"","sendcarnum":0,"statisticstate":1,"userid"
	 *            :1002}"
	 * 
	 */
	public void saveDeliverState(@Header("returnSuper") String parm) {
		logger.info("RE:jms:queue:returnSuper 反馈 pram:" + parm);
		return;
	}

	/**
	 * 保存交款jms消息
	 * 
	 * @param parm
	 *            "{\"should_amount\":43,\"payup_amount\":32,\"arrearage_huo_amount\":0,\"arrearage_fa_amount\":11,\"payup_type\":1
	 *            , \
	 *            "gcaids\":\"(1,2,3)\",\"remark\":\"撒打开飞洒\",\"credatetime\":\"2012-07-17 10:52:57\",\"branchid\":1}"
	 */
	public void saveBranchPayamount(@Header("PayUp") String parm) {
		logger.info("RE:jms:queue:PayUp 上交款 pram:" + parm);
		return;
	}

	/**
	 * 保存归班表jms消息
	 * 
	 * @param parm
	 *            "{"auditingtime":"2012-10-25
	 *            15:08:35","branchid":190,"deliverealuser
	 *            ":1003,"deliverealuser_name":"",
	 *            "id":46,"payupamount":94,"payupamount_pos"
	 *            :0,"payupid":0,"receivedfeeuser"
	 *            :1002,"receivedfeeuser_name":"","cwbs":"'21','22','23','24'"}"
	 */
	public void saveGotoClassAuditing(@Header("GotoClassAuditing") String parm) {
		logger.info("RE:jms:queue:gotoClass 归班parm：" + parm);
		return;

	}

	/**
	 * 保存失效订单jms
	 * 
	 * @param parm
	 *            "{\"emaildateid\":10}"
	 */
	public void updateLoseOrder(@Header("loseCwbByEmaildateid") String parm) {
		logger.info("RE:jms:queue:VirtualTopicConsumers.oms1.loseCwb ");
		JSONObject emaildateidJSON = JSONObject.fromObject(parm);
		int emaildateid = emaildateidJSON.getInt("emaildateid");
		cwbDAO.updateLoseCwbOrder(emaildateid);
		try {
			List<String> cwbs = cwbDAO.getCwbListByEmaildateid(emaildateid);
			if (cwbs != null && cwbs.size() > 0) {
				String cwb = "";
				for (String str : cwbs) {
					cwb += "'" + str + "',";
				}
				cwb = cwb.substring(0, cwb.length() - 1);
				cwbOrderTailDao.delTailByCwbs(cwb);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 修改匹配站点
	 * 
	 * @param parm
	 *            "{\"excelbranch\":"+excelbranch+",\"branchid\":\""+branchid+
	 *            "\",
	 *            \"deliverid\":\""+deliverid+"\",\"exceldeliver\":\""+exceldeliver
	 *            +"\",\"cwb\":\""+cwb+"\"}"
	 */
	public void editexcel(@Header("cwbForBranchAndDeliverid") String parm) {
		logger.info("RE:jms:queue:VirtualTopicConsumers.oms1.editexcel ");
		try {
			JSONObject branchAndDeliverJSON = JSONObject.fromObject(parm);
			logger.info(parm);
			int branchid = branchAndDeliverJSON.getInt("branchid");
			long deliverid = branchAndDeliverJSON.getLong("deliverid");
			String excelbranch = branchAndDeliverJSON.getString("excelbranch");
			String exceldeliver = branchAndDeliverJSON.getString("exceldeliver");
			String cwb = branchAndDeliverJSON.getString("cwb");
			cwbDAO.updateBranchAndDeliverid(branchid, deliverid, excelbranch, exceldeliver, cwb);
			orderFlowDAO.updateOrderFlowBranchAndDeliverid(branchid, cwb);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改匹配站点 异常！");
		}
	}

	/**
	 * 保存滞留原因
	 * 
	 * @param parm
	 */
	public void editleavereason(@Header("cwbAndLeavereason") String parm) {
		logger.info("RE:jms:queue:editleavereason ");
		try {
			JSONObject branchAndDeliverJSON = JSONObject.fromObject(parm);

			String cwb = branchAndDeliverJSON.getString("cwb");
			String leavedreason = branchAndDeliverJSON.getString("leavedreason");
			long leaveid = branchAndDeliverJSON.getLong("leaveid");
			// 获取订单的 供货商
			long customerid = cwbDAO.getcustomeridByCwb(cwb);
			// 获取关联的滞留原因
			ExptReason exp = getExptReasonByB2c(leaveid, 0, String.valueOf(customerid));
			String code = exp.getExpt_code();
			String msg = exp.getExpt_msg();
			cwbDAO.updateleavereason(leavedreason, leaveid, code, msg, cwb);
			logger.info("cwb：" + cwb + "保存滞留原因 leaveid:" + leaveid + " 供货商异常编码：" + code + " 成功");
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("保存滞留原因  异常！");
			e.printStackTrace();
		}
	}

	/**
	 * 保存退货原因
	 * 
	 * @param parm
	 */
	public void editbackreason(@Header("cwbAndBackreason") String parm) {
		logger.info("保存退货原因 RE:jms:queue:editbackreason ");
		try {
			JSONObject branchAndDeliverJSON = JSONObject.fromObject(parm);

			String cwb = branchAndDeliverJSON.getString("cwb");
			String backreason = branchAndDeliverJSON.getString("backreason");
			long backid = branchAndDeliverJSON.getLong("backid");
			// 获取订单的 供货商
			long customerid = cwbDAO.getcustomeridByCwb(cwb);
			// 获取关联的滞留原因
			ExptReason exp = getExptReasonByB2c(0, backid, String.valueOf(customerid));
			String code = exp.getExpt_code();
			String msg = exp.getExpt_msg();
			cwbDAO.updatebackreason(backreason, backid, code, msg, cwb);
			logger.info("cwb：" + cwb + "保存退货原因 backid:" + backid + " 供货商异常编码：" + code + " 成功");
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("保存退货原因  异常！");
			e.printStackTrace();
		}
	}

	private ExptReason getExptReasonByB2c(long leavedreasonid, long backreasonid, String customerid) {
		ExptReason exptreason = null;
		if (leavedreasonid != 0) { // 滞留
			exptreason = getDmpDAO.getExptCodeJointByB2c(leavedreasonid, ReasonTypeEnum.BeHelpUp.getValue(), customerid);
			if (exptreason != null && exptreason.getExpt_code() != null && !exptreason.getExpt_code().equals("")) {
				return exptreason;
			} else {
				exptreason = new ExptReason();
				exptreason.setExpt_code("299");
				exptreason.setExpt_msg("其他原因");
			}

		} else if (backreasonid != 0) { // 拒收
			exptreason = getDmpDAO.getExptCodeJointByB2c(backreasonid, ReasonTypeEnum.ReturnGoods.getValue(), customerid);
			if (exptreason != null && exptreason.getExpt_code() != null && !exptreason.getExpt_code().equals("")) {
				return exptreason;
			} else {
				exptreason = new ExptReason();
				exptreason.setExpt_code("103");
				exptreason.setExpt_msg("其他原因");
			}

		}

		return exptreason == null ? new ExptReason() : exptreason;
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
			exptreason = getDmpDAO.getExptCodeJointByB2c(leavedreasonid, ReasonTypeEnum.BeHelpUp.getValue(), dangdang.getCustomerids());
			if (exptreason.getExpt_code() == null || exptreason.getExpt_code().equals("")) {
				exptreason.setExpt_code("299");
				exptreason.setExpt_msg("其他原因");
			}
		} else if ("103".equals(reasontype)) { // 拒收
			exptreason = getDmpDAO.getExptCodeJointByB2c(backreasonid, ReasonTypeEnum.ReturnGoods.getValue(), dangdang.getCustomerids());
			if (exptreason.getExpt_code() == null || exptreason.getExpt_code().equals("")) {
				exptreason.setExpt_code("103");
				exptreason.setExpt_msg("客户拒收");
			}
		}

		return exptreason == null ? new ExptReason() : exptreason;
	}

}
