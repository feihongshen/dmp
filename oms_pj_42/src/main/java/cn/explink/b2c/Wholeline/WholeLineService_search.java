package cn.explink.b2c.Wholeline;

import java.net.URL;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CommonSendDataDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.Common;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;

/**
 * 执行全线快递查询跟踪详情的功能
 * 
 * @author Administrator
 *
 */
@org.springframework.stereotype.Service
public class WholeLineService_search {
	@Autowired
	WholeLineDao wholeLineDao;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	GetDmpDAO getdmpDAO;
	@Autowired
	CommonSendDataDAO commonSendDataDAO;
	@Autowired
	WarehouseCommenDAO warehouseCommenDAO;
	private Logger logger = LoggerFactory.getLogger(WholeLineController.class);

	/**
	 * 查询新表得到订单号去webservice请求全线快递
	 * 
	 * @param key
	 *            b2c的key
	 * @param whole
	 *            WholeLine实体
	 */
	public void searchQuanxianRoute(int key, WholeLine whole) {
		if (!isWholeLineOpen(key)) {
			logger.info("未开启全线快递对接");
			return;
		}

		Common common = getdmpDAO.getCommonByCommonnumber(whole.getUsercode());
		if (common == null) {
			logger.info("全线快递得到common是空，usercode={}", whole.getUsercode());
			return;
		}

		int count = wholeLineDao.getCountWholeLines();
		if (count == 0) {
			logger.info("当前没有请求全线快递的查询数据");
			return;
		}

		long page = common.getPageSize();
		if (page == 0) {
			page = 100;
		}
		int i = (int) (count / page);
		int m = (int) (count % page);
		if (m > 0 || i == 0) {
			i = i + 1;
		}

		long success = 0;
		for (int j = 0; j < i; j++) {
			long countAll = (j * page);
			countAll = countAll - success;
			// 查询express_b2cdatadown_wholeline得到订单号，state=''
			List<WholelineSearch> lineList = wholeLineDao.getWholeLinesCwBForList(countAll, common.getPageSize());
			logger.info("查询出库订单号，获得list,size={}", lineList.size());

			for (WholelineSearch wlist : lineList) {
				success = invokeWebServiceAndDealWithResult(key, whole, success, wlist);
			}
		}
	}

	private long invokeWebServiceAndDealWithResult(int key, WholeLine whole, long success, WholelineSearch wlist) {

		String returnxml = getWebserviceConnet(key, whole, wlist.getCwb());// webservice请求
		logger.info("查询全线快递返回xml={},订单号={}", returnxml, key);

		if (returnxml == null || returnxml.isEmpty() || JSONArray.fromObject(returnxml).isEmpty()) {
			logger.info("查询全线快递返回空cwb={}", wlist.getCwb());
			return success;
		}
		List<JsonContext> list = readValue(returnxml.toString(), JsonContext.class);// 把json转换成list

		for (JsonContext context : list) {
			String cwb = context.getWaybillNo();
			try {

				// 如果不是到货，领货，滞留，拒收的状态就不存
				if (!filterOperatorNote(Integer.parseInt(context.getOperTypeCode()))) {
					logger.info("状态[不是]全线快递需要的状态{}，订单号{}", context.getOperTypeCode(), cwb);
					continue;
				}
				// 封装实体
				logger.info("进入一条全线快递需要的状态{},订单号{}", context.getOperTypeCode(), cwb);

				OrderFlowDto dto = new OrderFlowDto();
				flowOrderAndDeliveryOne(context, dto, whole);

				// 查询commen_send_data 看是否有这条数据
				long isrepeatFlag = commonSendDataDAO.isExistsCwbFlag(cwb, whole.getUsercode(), context.getDisOperTm(), dto.getFlowordertype());
				if (isrepeatFlag > 0) {// 有就跳出循环
					logger.info("commen_send_data 已经有这条数据{},type={}", cwb, dto.getFlowordertype());
					continue;
				}

				buildOrderFlowDto(whole, context, dto); // 构建orderFlowDto对象
				long flowordertype = Long.valueOf(dto.getFlowordertype());
				long delivery = dto.getDeliverystate();
				dto.setDeliverystate(delivery);

				String dataJson = JacksonMapper.getInstance().writeValueAsString(dto);

				// 插入上游OMS临时表
				commonSendDataDAO.creCommenSendData(cwb, 0, whole.getUsercode(), DateTimeUtil.getNowTime(), context.getDisOperTm(), dataJson, delivery, flowordertype, "");

				// 自动补审核
				if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) {
					// 插入上游OMS临时表
					flowordertype = FlowOrderTypeEnum.YiShenHe.getValue();
					dto.setFlowordertype(String.valueOf(flowordertype));
					String backJson = JacksonMapper.getInstance().writeValueAsString(dto);
					commonSendDataDAO.creCommenSendData(cwb, 0, whole.getUsercode(), DateTimeUtil.getNowTime(), context.getDisOperTm(), backJson, delivery, flowordertype, "0");

					// 判断是拒收和配送成功的订单
					if (dto.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() || dto.getDeliverystate() == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
						// update新表，下次不再请求这个订单
						wholeLineDao.getUpdateByCwb(cwb, DateTimeUtil.getNowTime());
						success++;
					}
				}
			} catch (Exception e) {
				logger.error("处理全线快递结果存储状态表异常cwb=" + cwb, e);
			}

		}

		return success;
	}

	private boolean isWholeLineOpen(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getState() != 0;
		} catch (Exception e) {
			logger.warn("error while getting dangdang status with key {}, will defualt false", key);
			e.printStackTrace();
		}
		return false;
	}

	private String getWebserviceConnet(int key, WholeLine whole, String cwb) {
		Object[] params = new Object[] { whole.getComeCode(), cwb };
		Service service = new Service();
		Call call;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(whole.getUrl()));
			call.setTimeout(30000);
			call.setOperationName(whole.getMethod());
			call.addParameter("compCode", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			call.addParameter("waybillNos", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
			String returnValue = (String) call.invoke(params);
			logger.info("请求全线得到的json串{}", returnValue);
			return returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private void buildOrderFlowDto(WholeLine whole, JsonContext context, OrderFlowDto dto) {
		dto.setCustid("");
		dto.setCwb(context.getWaybillNo());
		dto.setFloworderdetail(context.getContentValue());// 操作描述
		dto.setOperatortime(context.getDisOperTm());// 操作时间
		dto.setRequestTime(DateTimeUtil.getNowTime());// 请求时间
		dto.setUserCode(whole.getUsercode());
	}

	private void flowOrderAndDeliveryOne(JsonContext context, OrderFlowDto dto, WholeLine whole) {
		int typeCode = Integer.parseInt(context.getOperTypeCode());
		if (typeCode == EXPTmsgEnum.zhiliujianrucang.getValue()) { // 滞留件入仓

			String refuse_cde = whole.getExpt_code();
			String[] bitCode = refuse_cde.split(",");

			dto.setFlowordertype(String.valueOf(FlowOrderTypeEnum.YiFanKui.getValue()));
			logger.info("订单状态为已反馈，订单号={}，异常码={}", context.getWaybillNo(), context.getStayWayCode());
			if (getBooleanByCode(bitCode, context.getStayWayCode())) {
				dto.setDeliverystate(DeliveryStateEnum.QuanBuTuiHuo.getValue());// 拒收
			} else {
				dto.setDeliverystate(DeliveryStateEnum.FenZhanZhiLiu.getValue());// 滞留
			}
			ExptReason reason = getdmpDAO.getReasonidJointByB2c(Long.valueOf(context.getStayWayCode()), whole.getUsercode(), context.getWaybillNo());
			String exptCode = reason.getExpt_code();
			dto.setExptcode(exptCode);// 滞留原因代码
		}

		if (typeCode == EXPTmsgEnum.peisongchenggong.getValue()) {// 配送成功
			dto.setDeliverystate(DeliveryStateEnum.PeiSongChengGong.getValue());
			dto.setFlowordertype(String.valueOf(FlowOrderTypeEnum.YiFanKui.getValue()));
		}
		if (typeCode == EXPTmsgEnum.daohuosaomiao.getValue()) {// 分站到货扫描
			dto.setDeliverystate(0);
			dto.setFlowordertype(String.valueOf(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()));
		}
		if (typeCode == EXPTmsgEnum.linghuocaomiao.getValue()) {// 分站领货
			dto.setDeliverystate(0);
			dto.setFlowordertype(String.valueOf(FlowOrderTypeEnum.FenZhanLingHuo.getValue()));
		}
	}

	/**
	 * 循环得出是否为需要的状态
	 * 
	 * @param value
	 *            操作码
	 * @return boolean
	 */
	public boolean filterOperatorNote(int value) {
		for (EXPTmsgEnum Enum : EXPTmsgEnum.values()) {
			if (value == Enum.getValue()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 查询拒收异常码
	 * 
	 * @param a
	 *            页面设置的拒收数组
	 * @param code
	 *            得到的异常码
	 * @return boolean
	 */
	public boolean getBooleanByCode(String[] a, String code) {
		for (String b : a) {
			if (b.equals(code)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 得到dmp的whole实体
	 * 
	 * @param key
	 *            b2c设置的key
	 * @return WholeLine对象
	 */
	public WholeLine getWholeline(int key) {
		WholeLine wholeline = new WholeLine();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			wholeline = (WholeLine) JSONObject.toBean(jsonObj, WholeLine.class);
		} else {
			wholeline = new WholeLine();
		}
		return wholeline == null ? new WholeLine() : wholeline;
	}

	private String getObjectMethod(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 得到最近的状态
	 * 
	 * @param returnxml
	 * @param key
	 * @param whole
	 */
	public void getWS(Object returnxml, int key, WholeLine whole) {
		try {
			// JsonContext
			// context=JacksonMapper.getInstance().readValue(returnxml.toString(),
			// JsonContext.class);
			if (returnxml.toString().length() == 0) {
				return;
			}
			List<JsonContext> list = readValue(returnxml.toString(), JsonContext.class);
			String date = "2013-01-01 00:00";
			for (JsonContext context : list) {
				String time = context.getDisOperTm();
				// 比较操作时间得到最近一次操作
				if (date.compareTo(time) < 0) {
					date = time;
				}
			}
			for (JsonContext context : list) {
				if (context.getDisOperTm().equals(date)) {
					OrderFlowDto dto = new OrderFlowDto();
					buildOrderFlowDto(whole, context, dto);
					long flowordertype = Long.valueOf(dto.getFlowordertype());
					long delivery = Long.valueOf(dto.getDeliverystate());
					String dataJson = JacksonMapper.getInstance().writeValueAsString(dto);
					// 插入上游OMS临时表
					commonSendDataDAO.creCommenSendData(context.getWaybillNo(), 0, whole.getComeCode(), DateTimeUtil.getNowTime(), context.getDisOperTm(), dataJson, delivery, flowordertype, "");
				}
			}
		} catch (Exception e) {
			logger.error("全线快递订单查询接口处理数据时发生异常{}" + e);
		}

	}

	/**
	 * 通用方法，json变为list
	 * 
	 * @param s
	 *            json
	 * @param clazz
	 *            公用
	 * @return
	 */
	public static List readValue(String s, Class clazz) {
		JSONArray jarr = JSONArray.fromObject(s);
		return (List) jarr.toCollection(jarr, clazz);
	}

}
