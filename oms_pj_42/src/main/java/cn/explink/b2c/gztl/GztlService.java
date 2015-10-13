package cn.explink.b2c.gztl;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.sf.json.JSONObject;

import org.apache.axis.client.Service;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.explink.b2c.gztl.returnData.TmsFeedback;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.CacheBaseListener;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.util.MD5.MD5Util;

import com.fyps.web.webservice.monternet.EcisServiceHttpBindingStub;
import com.fyps.web.webservice.monternet.TraceArgs;

/**
 * 广州通路Service
 *
 * @author Administrator
 *
 */
@org.springframework.stereotype.Service
public class GztlService {
	@Autowired
	private GetDmpDAO getdmpDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	CacheBaseListener cacheBaseListener;
	//getNowBrancheId
	
	
	private static final String FIRSTPEISONGSUCESS = "第一次配送成功";
	private static final String QITAYUANYIN = "其他原因";
	private static final String GONGYINGSAHNGJUSHOUFANKU="供应商拒收返库";
	private static final String TuiGonghuoshangChengGong="退供应商成功";
	private Logger logger = LoggerFactory.getLogger(GztlService.class);

	/**
	 * 获取反库状态
	 */
	public GztlEnum filterFlowState(long delivery_state, DmpCwbOrder cwbOrder, long flowordertype, long deliverystate, int cwbordertype) {
		
		if (flowordertype==FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) {
			GztlEnum.GongYingShangJuShouFanKu.setReturnMsg(GztlService.GONGYINGSAHNGJUSHOUFANKU);
			return GztlEnum.GongYingShangJuShouFanKu;
		}
		if (flowordertype==FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()) {
			GztlEnum.TuiGonghuoshangChengGong.setReturnMsg(GztlService.TuiGonghuoshangChengGong);
			return GztlEnum.TuiGonghuoshangChengGong;
		}
		
		for (GztlEnum e : GztlEnum.values()) {
		
			
			if ((flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) && (e.getFlowtype() == flowordertype)) {
				if ((flowordertype==6&&cwbOrder.getCwbstate()==6)) {
					GztlEnum.ZhongZhuanChuZhan.setReturnMsg(cwbOrder.getZhongzhuanreason());
					return GztlEnum.ZhongZhuanChuZhan;
				}
				if (flowordertype==4&&cwbOrder.getCwbstate()==6) {
					return GztlEnum.ZhongzhuanZhanRuKu;
				}
				if (flowordertype==9) {
					long flowType=this.b2cDataDAO.getNearFlowOrdertypeByCwb(cwbOrder.getCwb());
					if (flowType==36) {
						return null;
					}
				}
				return e;
			}
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
			if ((deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) || (deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue())
					|| (deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
				GztlEnum.Peisongchenggong.setReturnMsg(GztlService.FIRSTPEISONGSUCESS);
				return GztlEnum.Peisongchenggong;
			}
			if ((deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
				GztlEnum.BufenJushou.setReturnMsg(GztlService.QITAYUANYIN);
				return GztlEnum.BufenJushou;
			}
			if (deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
				GztlEnum.ShouGongdiushi.setReturnMsg(GztlService.QITAYUANYIN);
				return GztlEnum.ShouGongdiushi;
			}

			if ((deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue()) || (deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue())) {
				ExptReason exptReason = this.b2ctools.getExptReasonByB2c(0, cwbOrder.getBackreasonid(), String.valueOf(-2), delivery_state);
				
				String reasonString = exptReason.getExpt_code();
				
					//System.out.println(reasonString);
					String[] reason = reasonString.split("_");
					//System.out.println(exptReason.getExpt_msg());
					GztlEnum.Peisongshibai.setReturnMsg(reason[reason.length - 1]);
				
				return GztlEnum.Peisongshibai;
			}

			if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {

				ExptReason exptReason = this.b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), 0, String.valueOf(-2), delivery_state);
				String reasonString = exptReason.getExpt_code();
				
					//System.out.println(reasonString);
					String[] reason = reasonString.split("_");
					if (reason[0].equals(GztlEnum.KehuYanqi.getState())) {
						GztlEnum.KehuYanqi.setReturnMsg(reason[reason.length - 1]);
						//System.out.println("第一个参数：" + reason[0]);
						//System.out.println("第二个参数：" + reason[1]);
						return GztlEnum.KehuYanqi;
					} else {
						GztlEnum.Peisongyanchi.setReturnMsg(reason[reason.length - 1]);
						return GztlEnum.Peisongyanchi;
					}

				
				
			}

		}
		return null;
	}

	/**
	 * 获取广州通路配置信息并转化成为Gztl这个类中的信息
	 *
	 * @param key
	 * @return
	 */
	public Gztl getGztl(int key) {
		Gztl gztl = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
			gztl = (Gztl) JSONObject.toBean(jsonObj, Gztl.class);
		} else {
			gztl = new Gztl();
		}
		return gztl;
	}

	/**
	 * 获取广州通路配置信息的接口
	 *
	 * @param key
	 * @return
	 */
	private String getObjectMethod(int key) {
		try {
			JointEntity obj = this.getdmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将订单状态返回给广州通路
	 */
	public void feedback_status() {

		Gztl gztl = this.getGztl(B2cEnum.Guangzhoutonglu.getKey());
		if (!this.b2ctools.isB2cOpen(B2cEnum.Guangzhoutonglu.getKey())) {
			this.logger.info("未开0广州通路0的对接!");
			return;
		}
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.DaoRuShuJu.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.RuKu.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.ZhongZhuanChuKuSaoMiao.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.YiShenHe.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.TuiHuoChuZhan.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
		
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue());
		this.sendCwbStatus_To_gztl(gztl, FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue());
	}

	/**
	 * 状态反馈接口开始(广州通路)
	 *
	 * @param smile
	 */
	private void sendCwbStatus_To_gztl(Gztl gztl, long flowordertype) {
		try {

			int i = 0;
			while (true) {
				List<B2CData> gztlDataList = this.b2cDataDAO.getDataListByFlowStatus(flowordertype, gztl.getCustomerids(), gztl.getSearch_number());
				i++;
				if (i > 100) {
					String warning = "查询0广州通路0状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!";
					this.logger.warn(warning);
					return;
				}

				if ((gztlDataList == null) || (gztlDataList.size() == 0)) {
					this.logger.info("当前没有要推送0广州通路0的数据");
					return;
				}
				this.DealWithBuildXMLAndSending(gztl, gztlDataList);

			}

		} catch (Exception e) {
			String errorinfo = "发送0广州通路0状态反馈遇到不可预知的异常";
			this.logger.error(errorinfo, e);
		}

	}

	/**
	 * 拼成广州通路所需要的xml格式字符串，并通过webservice发过去，并对返回的信息对数据库中的记录的接收成功与否进行修改
	 *
	 * @param gztl
	 * @param datalist
	 * @throws Exception
	 */
	private void DealWithBuildXMLAndSending(Gztl gztl, List<B2CData> datalist) throws Exception {
		String failb2cids="";
		String b2cidsString = "";
		StringBuffer subBuffer = new StringBuffer();
		subBuffer.append("<TMS>");
		subBuffer.append("<TMSFeedbacks>");
		for (B2CData b2cData : datalist) {
			
			String jsoncontent = b2cData.getJsoncontent();
			GztlXmlNote note = this.getXMLNoteMethod(jsoncontent);
			
			b2cidsString += b2cData.getB2cid() + ",";
			subBuffer.append("<TMSFeedback>");
			subBuffer.append("<id>" + b2cData.getB2cid() + "</id>");// 序列号，用于接收成功后返回标识
			subBuffer.append("<myNo>" + note.getMyNo() + "</myNo>");// 运单编号
			subBuffer.append("<logisticid>" + note.getLogisticid() + "</logisticid>");// 订单号
			subBuffer.append("<custorderno>" + note.getCustorderno() + "</custorderno>");// 客户订单号
			subBuffer.append("<opType>" + note.getOpType() + "</opType>");// 反馈类型(由飞远提供)
			subBuffer.append("<state>" + note.getState() + "</state>");// 订单状态(由飞远提供)
			subBuffer.append("<returnState>" + note.getReturnState() + "</returnState>");// 网点反馈状态(由飞远提供)
			subBuffer.append("<returnCause>" + note.getReturnCause() + "</returnCause>");// 网点反馈原因(由飞远提供)
			subBuffer.append("<returnRemark>" + note.getReturnRemark() + "</returnRemark>");// 网点反馈备注(由飞远提供)
			subBuffer.append("<signname>" + note.getSignname() + "</signname>");// 签收人
			subBuffer.append("<opDt>" + note.getOpDt() + "</opDt>");// 网点反馈时间/签收时间/导入时间/出入库时间
			subBuffer.append("<emp>" + note.getEmp() + "</emp>");// 网点反馈用户??d
			subBuffer.append("<unit>" + note.getUnit() + "</unit>");// 反馈网点名称??
			subBuffer.append("<empSend>" + note.getEmpSend() + "</empSend>");// 派件员/反馈人/导入信息人员/出入库人
			subBuffer.append("<returnStatedesc>" + note.getReturnStatedesc() + "</returnStatedesc>");// 订单状态详情
			subBuffer.append("<cuscode>" + note.getCuscode() + "</cuscode>");// 供货商代码(由飞远提供)??
			String receivename=note.getReceiverName();
			if (receivename.contains("+")) {
				receivename=receivename.replace("+", "Q");
			}
			String receivephone=note.getReceiverMobile();
			if (receivephone.contains("+")) {
				receivephone=receivephone.replace("+", "Q");
			}
			subBuffer.append("<receiverName>" + receivename + "</receiverName>");// 收件人
			subBuffer.append("<receiverMobile>" + receivephone + "</receiverMobile>");// 收件人电话
			subBuffer.append("<customername>" + note.getCustomername() + "</customername>");// 供货商(由飞远提供)
			subBuffer.append("<senderName>" + "" + "</senderName>");// 寄件人
			subBuffer.append("<senderMobile>" + "" + "</senderMobile>");// 寄件手机
			subBuffer.append("<payinamount>" + note.getPayinamount() + "</payinamount>");// 代收货款
			subBuffer.append("<arrivedate>" + note.getArrivedate() + "</arrivedate>");// 最初扫描时间
			subBuffer.append("<lspabbr>" + note.getLspabbr() + "</lspabbr>");// 配送区域
			subBuffer.append("<pcs>" + note.getPcs() + "</pcs>");
			subBuffer.append("<business>" + note.getBusiness() + "</business>");
			subBuffer.append("</TMSFeedback>");
		}
		subBuffer.append("</TMSFeedbacks>");
		subBuffer.append("</TMS>");
		this.logger.info("生成符合广州通路状态回传的xml数据(发送给对方的xml数据)：{}", subBuffer.toString());
		b2cidsString = b2cidsString.length() > 0 ? b2cidsString.substring(0, b2cidsString.length() - 1) : b2cidsString;
		String url = gztl.getSearch_url();

		EcisServiceHttpBindingStub ce = new EcisServiceHttpBindingStub(new URL(url), new Service());
		TraceArgs traceArgs = new TraceArgs();
		traceArgs.setCode(gztl.getCode());
		traceArgs.setInvokeMethod(gztl.getInvokeMethod());
		String sign = MD5Util.md5(subBuffer.toString() + gztl.getPrivate_key());
		//System.out.println(gztl.getPrivate_key());
		//System.out.println(sign);
		traceArgs.setSign(sign);
		traceArgs.setXml(subBuffer.toString());
		String responseString = URLDecoder.decode(ce.orderAndFeedbackApi(traceArgs), "UTF-8");
		// WebserviceImp wImp = new WebserviceImp();
		// String responseString = wImp.getSendWs(traceArgs);
		//System.out.println(responseString);
		
		logger.info("广州通路状态回传返回信息xml={}",responseString);
		TmsFeedback tmsFeedback = (TmsFeedback) this.xmlToObject(responseString, new TmsFeedback());
		if (tmsFeedback == null) {
			this.logger.warn("请求0广州通路0解析xml为空，跳出循环,throw Exception,xml={}", responseString);
			//System.out.println("请求0广州通路0解析xml为空，跳出循环");
			return;
		}
		logger.info("广州通路状态回传返回信息send表里面的b2cids={}", tmsFeedback.getId());
		if (tmsFeedback.getSuccess().equals("true")) {
			if (b2cidsString.length()>0) {
				for (String b2cids: b2cidsString.split(",")) {
					if (tmsFeedback.getId().contains(b2cids)) {
						this.b2cDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(b2cids);
					}else {
						this.b2cDataDAO.updateMultiB2cIdSQLResponseStatus_AllFaild(b2cids);
						failb2cids+=b2cids+",";
					}
				}
			}
		
			//this.b2cDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(b2cidsString);
			//System.out.println("成功");
			this.logger.info("广州通路状态回传返回成功消息：remark={}" + tmsFeedback.getRemark());
			if (failb2cids.length()>0) {
				this.logger.info("广州通路状态回传返回失败的send表中的b2cids={}" + failb2cids.substring(0, failb2cids.length()-1));
			}
			this.logger.info("广州通路状态回传返回成功消息的cwbs：xml={}" + b2cidsString);

		} else if (tmsFeedback.getSuccess().equals("false")) {
			this.b2cDataDAO.updateMultiB2cIdSQLResponseStatus_AllFaild(b2cidsString);
			//System.out.println("失败" + tmsFeedback.getRemark());
			this.logger.info("广州通路状态回传返回失败消息：remark={}" + tmsFeedback.getRemark());
			this.logger.info("广州通路状态回传返回失败消息的send表里面的信息：b2cids={}" + b2cidsString);

		}
	}

	/**
	 * xml字符串转化为相应的类，可通用
	 *
	 * @param xml
	 * @param object
	 * @return
	 */
	public Object xmlToObject(String xml, Object object) {
		StringReader stringReader = new StringReader(xml);
		Object obj = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			obj = unmarshaller.unmarshal(stringReader);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			this.logger.error("将xml解析成GztlXmlElement类时出错", e);
			this.logger.error("将xml解析成GztlXmlElement类时出错，其中xml=" + xml, e);
		}
		return obj;
	}

	/**
	 * 将广州通路相关的节点的字段的信息由json转化为GztlXmlNote
	 *
	 * @param jsoncontent
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public GztlXmlNote getXMLNoteMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return JacksonMapper.getInstance().readValue(jsoncontent, GztlXmlNote.class);
	}
	public  String  removezero(String content){
		StringBuffer buffer=new StringBuffer();
		if (content.indexOf(".0")!=-1) {
			String[] arrayContent=content.split("\\.0");
			for (int i = 0; i < arrayContent.length; i++) {
				buffer.append(arrayContent[i]);
			}
		}else {
			buffer.append(content);
		}
		return buffer.toString();
	}

/*	public static void main(String[] args) {

		String string = "配送成功_配送延期";
		String[] aaStrings = string.split("_");
		System.out.println(aaStrings[0]);
		System.out.println(aaStrings[aaStrings.length - 1]);

	}*/
}
