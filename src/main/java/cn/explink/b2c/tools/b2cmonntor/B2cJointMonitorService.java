package cn.explink.b2c.tools.b2cmonntor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.dangdang.DangDangFlowEnum;
import cn.explink.b2c.tmall.TmallFlowEnum;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.vipshop.VipShopFlowEnum;
import cn.explink.b2c.yihaodian.YihaodianFlowEnum;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.util.JMath;

/**
 * 针对B2C对接的监控，若OMS没有存入数据或发生推送失败等等，则可以用此类来监控
 *
 * @author Administrator
 *
 */
@Service
public class B2cJointMonitorService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static List<Integer> flowList = new ArrayList<Integer>(); // 存储
																		// 对接所用的环节
	static {
		B2cJointMonitorService.flowList.add(FlowOrderTypeEnum.RuKu.getValue()); // 入库
		B2cJointMonitorService.flowList.add(FlowOrderTypeEnum.ChuKuSaoMiao.getValue()); // 出库扫描
		B2cJointMonitorService.flowList.add(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()); // 分站到货扫描
		B2cJointMonitorService.flowList.add(FlowOrderTypeEnum.FenZhanLingHuo.getValue()); // 分站领货
		B2cJointMonitorService.flowList.add(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()); // 退货站入库
		B2cJointMonitorService.flowList.add(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
		B2cJointMonitorService.flowList.add(FlowOrderTypeEnum.YiShenHe.getValue()); // 已审核（反馈部分）
	}

	@Autowired
	B2cJointMonitorDAO b2cdataDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	B2cJointMonitorDAO b2cJointMonitorDAO;
	@Autowired
	private CamelContext camelContext;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private static final String MQ_FROM_URI_B2C_DATA_SEND_RESULT_MONITOR = "jms:queue:VirtualTopicConsumers.dmpmointor1.b2cDataSendResultMonitor";

	@PostConstruct
	public void init() {
		this.logger.info("init addnum camel routes");
		try {
			this.camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					this.from(MQ_FROM_URI_B2C_DATA_SEND_RESULT_MONITOR + "?concurrentConsumers=5").to("bean:b2cJointMonitorService?method=updateDMPB2cDataMonitor")
							.routeId("updateDMPB2cData");
				}

			});
		} catch (Exception e) {
			this.logger.error("init addnum camel routes", e);
		}
	}

	public void saveMonitorB2c(OrderFlow of) {
		this.logger.info("dmp monitor save b2c,cwb={},flowordertype={}", of.getCwb(), of.getFlowordertype());
		try {

			if (!B2cJointMonitorService.flowList.contains(of.getFlowordertype())) {
				return;
			}

			if (this.b2cdataDAO.checkIsRepeatDataFlag(of.getCwb(), of.getFlowordertype(), of.getCredate().toString()) > 0) {
				this.logger.info("RE: dmp monitor save b2c 环节信息重复,已过滤,cwb={},flowordertype={}", of.getCwb(), of.getFlowordertype());
				return;
			}
			// 存入正常流程的状态信息
			this.saveDmpB2cDataMonitor(of);
		} catch (Exception e1) {
			this.logger.error("dmp monitor save b2c happend exception", e1);
		}
	}

	ObjectMapper objectMapper = new ObjectMapper();

	private void saveDmpB2cDataMonitor(OrderFlow of) throws Exception {
		CwbOrderWithDeliveryState cwbOrderWithDeliveryState = this.objectMapper.readValue(of.getFloworderdetail(), CwbOrderWithDeliveryState.class);
		CwbOrder cwbOrder = cwbOrderWithDeliveryState.getCwbOrder();

		long delivery_state = cwbOrderWithDeliveryState.getDeliveryState() == null ? 0 : cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate(); // 反馈状态
		Customer customer = this.customerDAO.getCustomerById(cwbOrder.getCustomerid());
		if ((customer.getCustomerid() == 0) || (customer == null)) {
			this.logger.error("dmp monitor save b2c:获取customer对象为空,当前订单号=" + cwbOrder.getCwb() + ",customerid=" + cwbOrder.getCustomerid() + ",flowOrdertype=" + cwbOrder.getFlowordertype());
			return;
		}

		if (customer.getB2cEnum().equals(B2cEnum.JuMeiYouPin.getKey() + "") || (customer.getCustomername().indexOf("聚美") > -1)) {
			if ((cwbOrder.getFlowordertype() != FlowOrderTypeEnum.RuKu.getValue())
			// &&cwbOrder.getFlowordertype()!=FlowOrderTypeEnum.ChuKuSaoMiao.getValue()
					&& (cwbOrder.getFlowordertype() != FlowOrderTypeEnum.YiShenHe.getValue())) {
				return;
			}

		} else if (customer.getB2cEnum().equals(B2cEnum.Tmall.getKey()) || (customer.getCustomername().indexOf("天猫") > -1)) {
			String TmallReceiveStatus = this.getTmallFlowEnum(cwbOrder.getFlowordertype(), delivery_state);
			if (TmallReceiveStatus == null) {
				return;
			}
		} else if (customer.getB2cEnum().equals(B2cEnum.DangDang.getKey()) || (customer.getCustomername().indexOf("当当") > -1)) {
			String DangDangReceiveStatus = this.getDangDangFlowEnum(cwbOrder.getFlowordertype());
			if (DangDangReceiveStatus == null) {
				return;
			}
		} else if (customer.getB2cEnum().equals(B2cEnum.VipShop_beijing.getKey()) || customer.getB2cEnum().equals(B2cEnum.VipShop_shanghai.getKey())
				|| customer.getB2cEnum().equals(B2cEnum.VipShop_nanhai.getKey()) || customer.getB2cEnum().equals(B2cEnum.VipShop_huabei.getKey())
				|| customer.getB2cEnum().equals(B2cEnum.VipShop_huanan.getKey()) || (customer.getCustomername().indexOf("唯品会") > -1)) {
			if (this.getVipShopFlowEnum(cwbOrder.getFlowordertype(), delivery_state) == null) {
				return;
			}

		} else if (customer.getB2cEnum().equals(B2cEnum.Yihaodian.getKey()) || (customer.getCustomername().indexOf("一号店") > -1)) {
			if (this.getYihaodianFlowEnum(cwbOrder.getFlowordertype(), delivery_state) == null) {
				return;
			}
		}

		if ((customer.getB2cEnum() != null) && !"".equals(customer.getB2cEnum())) {
			this.b2cdataDAO.saveB2cDataMonitor(of.getCwb(), cwbOrder.getFlowordertype(), cwbOrder.getCustomerid(), of.getCredate().toString());
		}
	}

	public String getDangDangFlowEnum(long flowordertype) {
		for (DangDangFlowEnum dd : DangDangFlowEnum.values()) {
			if (flowordertype == dd.getFlowordertype()) {
				return dd.getFlowordertype() + "";
			}
		}
		return null;
	}

	public String getTmallFlowEnum(long flowordertype, long deliverystate) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (TmallFlowEnum TEnum : TmallFlowEnum.values()) {
				if (flowordertype == TEnum.getFlowordertype()) {
					return TEnum.getTmall_state();
				}
			}
		}
		if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) && (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue())) {
			return TmallFlowEnum.TMS_SIGN.getTmall_state();
		}
		if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) && (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue())) {
			return TmallFlowEnum.TMS_ERROR.getTmall_state();
		}
		if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) && ((deliverystate == DeliveryStateEnum.JuShou.getValue()) || (deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()))) {
			return TmallFlowEnum.TMS_FAILED.getTmall_state();
		}
		return null;

	}

	public String getVipShopFlowEnum(long flowordertype, long delivery_state) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (VipShopFlowEnum TEnum : VipShopFlowEnum.values()) {
				if (flowordertype == TEnum.getFlowordertype()) {
					return TEnum.getVipshop_state() + "";
				}
			}
		}
		if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return VipShopFlowEnum.PeiSongChengGong.getVipshop_state() + "";
		}
		if (delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return VipShopFlowEnum.ShangMenTuiChengGong.getVipshop_state() + "";
		}
		if (delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			return VipShopFlowEnum.ShangMenHuanChengGong.getVipshop_state() + "";
		}
		if (delivery_state == DeliveryStateEnum.JuShou.getValue()) {
			return VipShopFlowEnum.JuShou.getVipshop_state() + "";
		}
		if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return VipShopFlowEnum.FenZhanZhiLiu.getVipshop_state() + "";
		}
		if (delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			return VipShopFlowEnum.HuoWuDiuShi.getVipshop_state() + "";
		}
		return null;

	}

	public YihaodianFlowEnum getYihaodianFlowEnum(long flowordertype, long delivery_state) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (YihaodianFlowEnum TEnum : YihaodianFlowEnum.values()) {
				if (flowordertype == TEnum.getFlowordertype()) {
					return TEnum;
				}
			}
		}
		if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return YihaodianFlowEnum.PeiSongChengGong;
		}
		if (delivery_state == DeliveryStateEnum.JuShou.getValue()) {
			return YihaodianFlowEnum.JuShou;
		}
		if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return YihaodianFlowEnum.FenZhanZhiLiu;
		}

		if (delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			return YihaodianFlowEnum.BuFenShiBai;
		}
		return null;

	}

	/**
	 * 监听oms中的方法，修改send_b2c_flag=1，2
	 */
	public void updateDMPB2cDataMonitor(@Header("monitorb2cdata") String parm, @Header("MessageHeaderUUID") String messageHeaderUUID) {
		this.logger.debug("接收OMS-JMS:" + parm);
		try {
			JSONArray array = JSONArray.fromObject(parm);
			for (int i = 0; i < array.size(); i++) {
				B2CMonitorData bdata;
				bdata = this.objectMapper.readValue(array.get(i).toString(), B2CMonitorData.class);
				this.b2cdataDAO.updateB2cDataMonitor(bdata.getCwb(), bdata.getFlowordertype(), bdata.getSend_b2c_flag(), bdata.getExpt_reason());
			}
		} catch (Exception e) {
			this.logger.error("DMP接收OMS发送B2cData数据异常", e);
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass() + "updateDMPB2cDataMonitor")
					.buildExceptionInfo(e.toString()).buildTopic(MQ_FROM_URI_B2C_DATA_SEND_RESULT_MONITOR)
					.buildMessageHeader("monitorb2cdata", parm)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}

	public void exportB2cDataExptInfo(String cwb, String customerid, long flowordertype, String starttime, String endtime, long send_b2c_flag, long hand_deal_flag, HttpServletResponse response) {

		List<B2CMonitorData> b2cdatalist = this.b2cJointMonitorDAO.selectB2cIdsByB2cMonitorDataList(cwb, customerid, flowordertype, send_b2c_flag, hand_deal_flag, starttime, endtime);
		String excelbranch = "B2C对接异常订单列表";
		JMath.CreateExcelHeader(response, excelbranch);
		// 创建excel 新的工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("对接异常订单列表"); // Sheet1没个新建的Excel都是这个默认值
		sheet.getPrintSetup().setLandscape(true); // true：横向 false：纵向
		int rows = 0; // 第一行
		int c = 0; // 第一列

		HSSFRow row = sheet.createRow((short) rows++);
		JMath.setCellValue(row, c++, "序号");
		JMath.setCellValue(row, c++, "供货商");
		JMath.setCellValue(row, c++, "订单号");
		JMath.setCellValue(row, c++, "记录时间");
		JMath.setCellValue(row, c++, "当前状态");
		JMath.setCellValue(row, c++, "推送状态");
		JMath.setCellValue(row, c++, "是否手动处理");
		JMath.setCellValue(row, c++, "异常原因");

		int k = 0;
		for (B2CMonitorData pe : b2cdatalist) {
			k++;
			row = sheet.createRow((short) rows++);
			c = 0; // 第一列
			String customername = "";
			List<Customer> customerlist = this.customerDAO.getAllCustomers();
			for (Customer cu : customerlist) {
				if (cu.getCustomerid() == pe.getCustomerid()) {
					customername = cu.getCustomername();
				}
			}
			String method = "";
			for (CwbFlowOrderTypeEnum cc : CwbFlowOrderTypeEnum.values()) {
				if (cc.getValue() == pe.getFlowordertype()) {
					method = cc.getText();
				}
			}

			JMath.setCellValue(row, c++, "" + (k + 1));
			JMath.setCellValue(row, c++, customername);
			JMath.setCellValue(row, c++, pe.getCwb());
			JMath.setCellValue(row, c++, pe.getPosttime());
			JMath.setCellValue(row, c++, method);
			JMath.setCellValue(row, c++, pe.getSend_b2c_flagStr() + "");
			JMath.setCellValue(row, c++, pe.getHand_deal_flagStr() + "");
			JMath.setCellValue(row, c++, pe.getExpt_reason());
		}
		row = sheet.createRow((short) rows++);
		c = 0; // 第一列
		JMath.setCellValue(row, c++, "合计");
		JMath.setCellValue(row, c++, b2cdatalist.size() + "[条]");

		JMath.CreateOutPutExcel(response, workbook);
	}

}
