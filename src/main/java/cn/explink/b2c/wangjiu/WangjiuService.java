package cn.explink.b2c.wangjiu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import cn.explink.b2c.wangjiu.xmldto.OrderItem;
import cn.explink.b2c.wangjiu.xmldto.WangjiuOrder;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.service.CustomerService;

@Service
public class WangjiuService {
	private Logger logger = LoggerFactory.getLogger(WangjiuService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	CustomerService customerService;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Wangjiu getWangjiu(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Wangjiu smile = (Wangjiu) JSONObject.toBean(jsonObj, Wangjiu.class);
		return smile;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Wangjiu wangjiu = new Wangjiu();
		String customerid = request.getParameter("customerid");
		String warehouseid = request.getParameter("warehouseid");
		String maxcount = "".equals(request.getParameter("maxcount")) ? "0" : request.getParameter("maxcount");

		wangjiu.setCustomerid(customerid);
		wangjiu.setFeedbackUrl(request.getParameter("feedbackUrl"));
		wangjiu.setImportUrl(request.getParameter("importUrl"));
		wangjiu.setMaxCount(Integer.parseInt(maxcount));
		wangjiu.setWarehouseid(Long.valueOf(warehouseid));
		wangjiu.setParternID(request.getParameter("parternID"));
		wangjiu.setPrivate_key(request.getParameter("private_key"));

		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(wangjiu);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getWangjiu(joint_num).getCustomerid();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public int getStateForYihaodian(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	/**
	 * 
	 * @param result
	 *            True or False
	 * @param remark
	 *            备注信息，如果订单重复， 则True ,remark="运单重复"
	 * @return
	 */
	public String buildResponseXML(String result, String remark) {
		String info = "<ResponseOrder>" + "<success>" + result + "</success>" + "<reason>" + remark + "</reason>" + "</ResponseOrder>";

		logger.info("返回网酒网={}", info);

		return info;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {

		String str = "+raD6uTwET5NKrNlU8mnJg==";
		String sign = URLDecoder.decode(str, "UTF-8");
		System.out.println(sign);
	}

	/**
	 * 处理思迈请求数据，并且导入系统 数据，一单一单的导入
	 * 
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public String processRequestOrders(String logistics_interface, String data_digest) throws NumberFormatException, Exception {

		int smileKey = B2cEnum.Wangjiu.getKey();
		int isOpenFlag = jiontDAO.getStateByJointKey(smileKey);
		Wangjiu wj = this.getWangjiu(smileKey);
		if (isOpenFlag == 0) {
			return buildResponseXML("false", "未开启接口");
		}

		// String xml=URLDecoder.decode(logistics_interface, "UTF-8");
		// String sign=URLDecoder.decode( data_digest, "UTF-8");

		String xml = logistics_interface;
		String sign = data_digest;

		// logger.info("网酒网请求信息-解码后logistics_interface={}",xml);

		String localSign = WangjiuConfig.base64Md5Result(xml, wj.getParternID(), wj.getPrivate_key());

		if (!localSign.equalsIgnoreCase(sign)) {
			logger.info("网酒网签名验证失败!");
			return buildResponseXML("false", "签名验证失败");

		}

		WangjiuOrder order = WangjiuUnmarchal.Unmarchal(xml);

		List<Map<String, String>> orderlist = parseCwbArrByOrderDto(order, wj);

		if (orderlist == null || orderlist.size() == 0) {
			logger.warn("网酒网-请求没有封装参数，订单号可能为空");
			return buildResponseXML("success", "运单重复");
		}

		long warehouseid = wj.getWarehouseid(); // 订单导入的库房Id
		dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(wj.getCustomerid()), B2cEnum.Wangjiu.getMethod(), orderlist, warehouseid, true);

		return buildResponseXML("success", "成功");
	}

	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String, String>> parseCwbArrByOrderDto(WangjiuOrder order, Wangjiu wj) {
		List<Map<String, String>> cwbList = new ArrayList<Map<String, String>>();

		if (order != null) {

			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getMailNo());
			if (cwbOrder != null) {
				logger.warn("获取0网酒网0订单中含有重复数据cwb={}", order.getMailNo());
				return null;
			}

			int orderType = order.getOrderType();
			int cwbordertypeid = 1;
			if (orderType == 0 || orderType == 1) {
				cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue();
			} else {
				cwbordertypeid = CwbOrderTypeIdEnum.Shangmentui.getValue();
			}

			String consigneeaddress = order.getReceiver().getProv() + order.getReceiver().getCity() + order.getReceiver().getAddress();
			consigneeaddress = consigneeaddress.replaceAll("null", "");

			Map<String, String> cwbMap = new HashMap<String, String>();
			cwbMap.put("cwb", order.getMailNo()); // cwb 订单号
			cwbMap.put("consigneename", order.getReceiver().getName());
			cwbMap.put("consigneepostcode", order.getReceiver().getPostCode());
			cwbMap.put("consigneephone", order.getReceiver().getPhone());
			cwbMap.put("consigneemobile", order.getReceiver().getMobile());
			cwbMap.put("cargorealweight", "0"); // 订单重量(KG)
			cwbMap.put("cwbprovince", order.getReceiver().getProv());// 省
			cwbMap.put("cwbcity", order.getReceiver().getCity());// 市
			cwbMap.put("consigneeaddress", consigneeaddress);
			cwbMap.put("caramount", order.getGoodsValue());
			cwbMap.put("customercommand", order.getRemark()); // 备注信息
			cwbMap.put("remark1", "总服务费:" + order.getTotalValue());
			cwbMap.put("remark2", "总服务费:" + order.getTotalServiceFee()); // 服务费
			cwbMap.put("receivablefee", order.getTotalValue() == null ? "0" : order.getTotalValue()); // COD代收款
			cwbMap.put("cwbordertypeid", String.valueOf(cwbordertypeid));

			cwbMap.put("sendcarnum", "1"); // 发货商品

			List<OrderItem> items = order.getOrderItems().getItem();
			String sendcarname = "";
			for (OrderItem good : items) {
				sendcarname += good.getItemName() + "x" + good.getNumber() + "/" + good.getItemValue();
				sendcarname += ";";
			}
			cwbMap.put("sendcarname", sendcarname); // 发货货物名称

			cwbList.add(cwbMap);

		}
		return cwbList;
	}

}
