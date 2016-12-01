package cn.explink.b2c.smile;

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
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.service.CustomerService;
import cn.explink.util.MD5.MD5Util;

@Service
public class SmileService {
	private static Logger logger = LoggerFactory.getLogger(SmileService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomerService customerService;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Smile getSmile(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Smile smile = (Smile) JSONObject.toBean(jsonObj, Smile.class);
		return smile;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Smile smile = new Smile();
		String customerids = request.getParameter("customerids");
		smile.setCustomerids(customerids);
		smile.setFeedback_url(request.getParameter("feedback_url"));
		String maxcount = "".equals(request.getParameter("maxcount")) ? "0" : request.getParameter("maxcount");
		smile.setMaxCount(Integer.parseInt(maxcount));
		smile.setSecretKey(request.getParameter("secretKey"));
		smile.setSendClientLoge(request.getParameter("sendClientLoge"));
		smile.setActionName(request.getParameter("actionName"));
		String warehouseid = request.getParameter("warehouseid");
		smile.setWarehouseid(Long.valueOf(warehouseid));

		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(smile);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getSmile(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
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
		String xml = "<Response>" + "<Success>" + result + "</Success>" + "<Remark>" + remark + "</Remark>" + "</Response>";
		logger.info("返回思迈={}", xml);
		return xml;
	}

	public static void main(String[] args) {

		String strs = "<RequestOrder><WaybillNo>YT9067930</WaybillNo><ClientCode>YT9067930</ClientCode><Holiday>1</Holiday><ReplCost>449.00</ReplCost><StmtForm>1</StmtForm><TrustClientCode>100058</TrustClientCode><TrustPerson>君联速递</TrustPerson><TrustUnit>广州君联速递有限公司</TrustUnit><TrustZipCode>510000</TrustZipCode><TrustCity>广东省广州市</TrustCity><TrustAddress>广东省广州市白云区新科村新科工业区弘森国际物流中心A115号</TrustAddress><TrustMobile></TrustMobile><TrustTel>020-36539611</TrustTel><GetPerson>王有亮</GetPerson><GetUnit>0</GetUnit><GetZipCode>0</GetZipCode><GetCity>山西省忻州市</GetCity><GetAddress>山西省忻州市忻府区长征西街包天下（电话联系）</GetAddress><GetTel>18735071725</GetTel><GetMobile>0</GetMobile><InsForm>62</InsForm><InsureValue>449.00</InsureValue><GoodsValue>449.00</GoodsValue><WorkType>0</WorkType><OrderType></OrderType><GoodsInfo><Good><GoodsName>电子产品</GoodsName><GoodsValue>449.00</GoodsValue><GoodsBarCode></GoodsBarCode><ListType>0</ListType><ISInvoice>0</ISInvoice></Good></GoodsInfo><GoodsNum>1</GoodsNum><GoodsHav>0.48</GoodsHav></RequestOrder>2011";
		logger.info(MD5Util.md5(strs));
	}

	/**
	 * 处理思迈请求数据，并且导入系统 数据，一单一单的导入
	 * 
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public String requestSiMaiMakeOrders(String Request, String MD5, String Action) throws NumberFormatException, Exception {
		int smileKey = B2cEnum.Smile.getKey();
		int isOpenFlag = jiontDAO.getStateByJointKey(smileKey);
		if (isOpenFlag == 0) {
			return buildResponseXML("False", "未开启接口");
		}
		Smile smile = this.getSmile(smileKey);
		String actionName = smile.getActionName();
		if (actionName != null && !actionName.contains(Action)) {
			return buildResponseXML("False", "请求指令不一致" + actionName);
		}
		String localSign = MD5Util.md5(Request + smile.getSecretKey(), "UTF-8");
		if (!localSign.equalsIgnoreCase(MD5)) {
			return buildResponseXML("False", "签名验证失败");
		}

		SmileOrder smileOrder = SmileUnmarchal.Unmarchal(Request);

		List<Map<String, String>> orderlist = parseCwbArrByOrderDto(smileOrder);

		if (orderlist == null || orderlist.size() == 0) {
			logger.warn("广州思迈速递-请求没有封装参数，订单号可能为空");
			return buildResponseXML("True", "运单重复");
		}

		long warehouseid = smile.getWarehouseid(); // 订单导入的库房Id
		List<CwbOrderDTO> resultList = dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(smile.getCustomerids()), B2cEnum.Smile.getMethod(), orderlist, warehouseid, true);
		
		if(resultList.isEmpty()){
			logger.info("广州思迈-订单导入出错");
			return buildResponseXML("False", "dmp处理失败，请重推");
		} else {
			logger.info("广州思迈-订单导入成功");
		}

		return buildResponseXML("True", "成功");
	}

	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String, String>> parseCwbArrByOrderDto(SmileOrder smileOrder) {
		List<Map<String, String>> cwbList = new ArrayList<Map<String, String>>();

		if (smileOrder != null) {

			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(smileOrder.getWaybillNo());
			if (cwbOrder != null) {
				logger.warn("获取0中兴云购ERP0订单中含有重复数据cwb={}", smileOrder.getWaybillNo());
				return null;
			}

			int WorkType = smileOrder.getWorkType(); // 0:正常，1：换货运单，2：退货单（拒收）
			int cwbordertypeid = 1;
			cwbordertypeid = getCwbOrdertypeId(WorkType, cwbordertypeid);
			String StmtForms = "结算方式:" + (smileOrder.getStmtForm() == 0 ? "月结" : "现结"); // 结算方式
																						// ，存备注1
			double replCost = smileOrder.getReplCost().doubleValue();
			String remark2 = getRemark2Str(smileOrder);

			String detailAddress = smileOrder.getGetCity() + smileOrder.getGetAddress();

			Map<String, String> cwbMap = new HashMap<String, String>();
			cwbMap.put("transcwb", smileOrder.getClientCode()); // transcwb 客户单号
			cwbMap.put("cwb", smileOrder.getWaybillNo()); // cwb 运单号
			cwbMap.put("consigneename", smileOrder.getGetPerson());
			cwbMap.put("consigneeaddress", detailAddress);
			cwbMap.put("consigneepostcode", smileOrder.getGetZipCode());
			cwbMap.put("consigneephone", smileOrder.getGetTel());
			cwbMap.put("consigneemobile", smileOrder.getGetMobile());

			cwbMap.put("receivablefee", String.valueOf(replCost));
			cwbMap.put("cwbordertypeid", String.valueOf(cwbordertypeid));

			String sendcarname = "";
			double cargoAmount = 0;
			List<Good> goods = smileOrder.getGoodsInfo().getGoods();
			for (Good good : goods) {
				sendcarname += good.getGoodsName() + ",";
				cargoAmount += good.getGoodsValue().doubleValue();
			}

			cwbMap.put("sendcarname", sendcarname); // 发货货物名称
			cwbMap.put("caramount", String.valueOf(cargoAmount));
			cwbMap.put("sendcarnum", smileOrder.getGoodsNum());
			cwbMap.put("cargorealweight", smileOrder.getGoodsHav());
			cwbMap.put("remark1", StmtForms);
			cwbMap.put("remark2", remark2);

			cwbList.add(cwbMap);

		}
		return cwbList;
	}

	private String getRemark2Str(SmileOrder smileOrder) {
		String TrustPerson = smileOrder.getTrustPerson(); // 发件人
		String TrustAddress = smileOrder.getTrustAddress(); // 发件地址
		String TrustMobile = smileOrder.getTrustMobile(); // 发件手机
		String remark2 = "发件人:" + TrustPerson + ",发件地址:" + TrustAddress + ",发件手机:" + TrustMobile;
		return remark2;
	}

	private int getCwbOrdertypeId(int WorkType, int cwbordertypeid) {
		if (WorkType == 0) {
			cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue();
		} else if (WorkType == 1) {
			cwbordertypeid = CwbOrderTypeIdEnum.Shangmenhuan.getValue();
		} else if (WorkType == 2) {
			cwbordertypeid = CwbOrderTypeIdEnum.Shangmentui.getValue();
		}
		return cwbordertypeid;
	}

}
