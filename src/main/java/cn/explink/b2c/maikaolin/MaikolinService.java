package cn.explink.b2c.maikaolin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.maikaolin.xml.MaiKoLinUnmarchal;
import cn.explink.b2c.maikaolin.xml.TMS;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CustomerService;
import cn.explink.util.DateTimeUtil;
import cn.explink.b2c.maikaolin.xml.Package;

@Service
public class MaikolinService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
	@Autowired
	MaikaolinInsertCwbDetailTimmer maikaolinInsertCwbDetailTimmer;
	@Autowired
	CustomerService customerService;
	private Logger logger = LoggerFactory.getLogger(Maikolin.class);
	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String PackageTodoList(String userid, String userkey, String time) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<tms>" + "<request>" + "<user_id>" + userid + "</user_id>" + "<user_key>" + userkey + "</user_key>"
				+ "<method>PackageTodoList</method>" + "<request_time>" + time + "</request_time>" + "</request>" + "</tms>";
		return xml;
	}

	public long excute_getMaikolinTask() {
		long count = 0;
		String customerids = "";
		String remark = "";
		String cretime = DateTimeUtil.getNowTime();

		try {
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.Maikaolin.getKey());
			if (isOpenFlag == 0) {
				logger.info("未开启麦考林对接！");
				return -1;
			}

			count = getOrdersByMaikolin(B2cEnum.Maikaolin.getKey()); // 下载

			getMaikaolinDataCallBack(); // 回传
			maikaolinInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.Maikaolin.getKey());

			Maikolin maikolin = getMaikaolin(B2cEnum.Maikaolin.getKey());
			customerids = maikolin.getCustomerids();

		} catch (Exception e) {
			remark = "未知异常" + e.getMessage();
			logger.error(remark, e);
		}
		remark = !remark.isEmpty() && count == 0 ? "未下载到数据" : remark;
		if (count > 0) {
			remark = "下载完成";
		}

		b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(customerids.indexOf(",") > 0 ? customerids.substring(0, customerids.length() - 1) : customerids, cretime, count, remark);
		return count;
	}

	public long getOrdersByMaikolin(int key) {
		Maikolin maikolin = getMaikaolin(key);
		int isOpenFlag = jointService.getStateForJoint(key);
		if (isOpenFlag == 0) {
			logger.info("未开启[麦考林][" + key + "]对接！---当前获取订单详情-----");
			return -1;
		}
		if (maikolin.getIsopendownload() == 0) {
			logger.info("未开启[麦考林][" + key + "]订单下载接口");
			return -1;
		}
		String request_time = DateTimeUtil.getNowTime("yyyyMMddHHmmss");
		/*
		 * 1.拼接requestXML是发送给麦考林的xml信息 2.requestXML发送给麦考林
		 */
		String requestXML = PackageTodoList(maikolin.getUserCode(), maikolin.getPrivate_key(), request_time);
		String endpointUrl = maikolin.getPushCwb_URL();
		String response_XML = null;

		logger.info("请求[麦考林]订单下载接口XML={},url{}", requestXML, endpointUrl);

		try {
			/*
			 * 
			 * resquest_XML是接收到的xml
			 */
			String resquest_XML = HTTPInvokeWs(requestXML, endpointUrl);
			logger.info("[麦考林]订单下载接口返回xml{}", resquest_XML);

			if (resquest_XML.contains("<success>false</success>")) {
				logger.info("[maikaolin]失败了<success>false</success>");
				return 0;
			}
			if (!resquest_XML.contains("<package>")) {
				logger.info("当前获取麦考林订单信息为空!");
				return 0;
			}

			// 3.成功了,解析xml
			TMS rootnote = MaiKoLinUnmarchal.Unmarchal(resquest_XML);
			List<Package> packageOne = rootnote.getRequest_body().getListPackage();
			if (packageOne == null || packageOne.size() == 0) {
				logger.warn("请求[麦考林]没有下载到订单数据!");
				return 0;
			}

			long warehouseid = maikolin.getWarehouseid(); // 订单导入的库房Id

			List<Map<String, String>> packageList = buildParmsList(packageOne, warehouseid); // 构建对象

			// 把数据存在临时表里
			dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(maikolin.getCustomerids()), B2cEnum.Maikaolin.getMethod(), packageList, warehouseid, true);
			logger.info("[麦考林]下载订单信息调用数据导入接口-插入数据库成功!");

			return packageList.size();

		} catch (Exception e) {
			logger.error("处理[麦考林]订单请求异常！返回信息：" + response_XML + ",异常原因：" + e.getMessage(), e);
			return 0;
		}

	}

	/**
	 * 查询临时表数据回传 麦考林
	 * 
	 * @param maikolin
	 * @param request_time
	 * @throws HttpException
	 * @throws IOException
	 */
	public void getMaikaolinDataCallBack() {
		try {

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.Maikaolin.getKey());
			if (isOpenFlag == 0) {
				logger.info("未开启[麦考林][订单回传]对接！---当前获取订单详情-----");
				return;
			}

			Maikolin maikolin = getMaikaolin(B2cEnum.Maikaolin.getKey());
			String request_time = DateTimeUtil.getNowTime("yyyyMMddHHmmss");
			/*
			 * 5.查询表中是否有数据，把数据的cwb拼接xml给麦考林
			 */
			List<CwbOrderDTO> list = dataImportDAO_B2c.getCwbOrderByCustomerIdAndPageCount(Long.valueOf(maikolin.getCustomerids()), maikolin.getCallBackCount());
			String retrunxml = "";
			for (CwbOrderDTO l : list) {
				dataImportDAO_B2c.updateIsB2cSuccessFlagByCwbs(l.getCwb());
				String pacakgexml = "<package>" + "<package_id>" + l.getCwb() + "</package_id>" + "<success>true</success>" + "<reason></reason>" + "</package>";
				retrunxml += pacakgexml;
			}
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<tms>" + "<response_header>" + "<user_id>" + maikolin.getUserCode() + "</user_id>" + "<user_key>" + maikolin.getPrivate_key()
					+ "</user_key>" + "<method>PackageTodoList</method>" + "<response_time>" + request_time + "</response_time>" + "</response_header>" + "<response_body>" + "<operation_result>"
					+ retrunxml + "</operation_result>" + "</response_body>" + "</tms>";
			logger.info("麦考林[订单回传]推送xml={}", xml);

			String resposexml = HTTPInvokeWs(xml, maikolin.getPushCwb_URL());

			logger.info("麦考林[订单回传]返回xml={}", resposexml);

		} catch (Exception e) {
			logger.error("订单回传麦考林发生未知异常", e);
		}
	}

	private List<Map<String, String>> buildParmsList(List<Package> packageOne, long warehouseid) {

		List<Map<String, String>> packageList = new ArrayList<Map<String, String>>();

		for (Package p1 : packageOne) {
			Map<String, String> map = new HashMap<String, String>();
			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(p1.getPackage_id());
			if (cwbOrder != null) {
				logger.warn("获取[maikaolin]临时表订单中含有重复数据cwb={}", p1.getPackage_id());
				continue;
			}
			map.put("cwb", p1.getPackage_id());//
			String type = "1";
			if (p1.getPackage_type().equals("2")) {
				type = "3";
			}
			if (p1.getPackage_type().equals("3")) {
				type = "2";
			}
			map.put("cwbordertypeid", type);//
			map.put("consigneename", p1.getContact_name());//
			map.put("consigneephone", p1.getContact_phone());//
			map.put("consigneemobile", p1.getContact_phone2());//
			map.put("consigneepostcode", p1.getPostal_code());//
			map.put("cwbprovince", p1.getProvince());//
			map.put("cwbcity", p1.getCity());//
			map.put("cwbcounty", p1.getZone());//
			map.put("consigneeaddress", p1.getAddress());//
			map.put("receivablefee", String.valueOf(p1.getCod_amount()));//
			map.put("caramount", String.valueOf(p1.getShip_amount()));//
			map.put("paybackfee", String.valueOf(p1.getReturnproduct_amount()));
			map.put("sendcarnum", "1");// 默认为1,防止订单扫描时定义为一票多件
			String Weight = String.valueOf(p1.getWeight());
			if (Weight == null) {
				Weight = "0";
			}
			map.put("cargorealweight", Weight);//
			map.put("cwbremark", p1.getRemark());//
			map.put("customerwarehouseid", String.valueOf(warehouseid));
			String sendcarname = "";
			String backcargoname = "";
			for (int i = 0; i < p1.getListitems().size(); i++) {
				for (int h = 0; h < p1.getListitems().get(i).getItemlist().size(); h++) {
					String m = "";
					String n = "";
					if (p1.getListitems().get(i).getItemlist().get(h).getLntype().equals("R")) {
						n += p1.getListitems().get(i).getItemlist().get(h).getProduct_name();
						backcargoname += n + ",";
					} else {
						m += p1.getListitems().get(i).getItemlist().get(h).getProduct_name();
						sendcarname += m + ",";
					}
				}

			}
			map.put("backcarname", backcargoname.isEmpty() ? "" : backcargoname.substring(0, backcargoname.length() - 1));
			map.put("sendcarname", sendcarname.isEmpty() ? "" : sendcarname.substring(0, sendcarname.length() - 1));
			packageList.add(map);

		}
		return packageList;
	}

	private String HTTPInvokeWs(String requestXML, String url) throws HttpException, IOException {
		URL url1 = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
		httpURLConnection.setRequestProperty("content-type", "text/xml");
		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("contentType", "utf-8");
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setConnectTimeout(30000);
		httpURLConnection.setReadTimeout(30000);
		httpURLConnection.connect();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
		out.write(requestXML);
		out.flush();
		// 接收服务器的返回：
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
		StringBuilder buffer = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public Maikolin getMaikaolin(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Maikolin masker = (Maikolin) JSONObject.toBean(jsonObj, Maikolin.class);
		return masker;

	}

	private Object getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Maikolin maikolin = new Maikolin();
		String customerid = request.getParameter("customerids");
		maikolin.setCustomerids(customerid);
		maikolin.setUserCode(request.getParameter("userCode"));
		maikolin.setPrivate_key(request.getParameter("key"));
		maikolin.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		maikolin.setCallBackCount(Long.parseLong(request.getParameter("callBackCount")));
		maikolin.setPushCwb_URL(request.getParameter("pushCwb_URL"));
		maikolin.setIsopendownload(Integer.parseInt(request.getParameter("isopenDataDownload")));
		maikolin.setExpress_id(request.getParameter("express_id"));
		JSONObject jsonObj = JSONObject.fromObject(maikolin);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getMaikaolin(joint_num).getCustomerids();

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

	/*
	 * 测试用的
	 */
	public String retrun(String x) {
		int i = (int) (100 * Math.random());
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<tms>" + "<request_header>" + "<user_id>20060</user_id>" + "<user_key>1234567890</user_key>" + "<method>PackageTodoList</method>"
				+ "<request_time>20131011121212</request_time>" + "</request_header>" + "<request_body>" + "<package>" + " <express_id>L4536755</express_id>" + "<express_name>explink</express_name>"
				+ " <package_id>Lb45367535"
				+ i
				+ "</package_id>"
				+ " <package_type>1</package_type >"
				+ " <contact_name>张三</contact_name>"
				+ " <contact_phone>123345454252</contact_phone>"
				+ "<contact_phone2>542573242</contact_phone2>"
				+ " <postal_code>4242375454</postal_code>"
				+ "<province>北京</province>"
				+ "<city>北京</city>"
				+ "<zone>朝阳</zone>"
				+ "<address>朝阳区十八里店网址额发的</address>"
				+ "<cod_amount>42.41</cod_amount>"
				+ "<return_amount>0.00</return_amount> "
				+ "<ship_amount>10.1</ship_amount> "
				+ "<returnproduct_amount>0.00</returnproduct_amount> "
				+ "<itemtotal>1</itemtotal>"
				+ "<weight>5.0</weight>"
				+ "<warehousearea>北京仓库</warehousearea>"
				+ "<remark>将覅额房间呢买家反馈数据免费送</remark>"
				+ "<Items>"
				+ "<Item>"
				+ "<product_id>1</product_id>"
				+ "<product_name>手套</product_name>"
				+ "<quantity>1</quantity>"
				+ "<lntype>衣物</lntype>"
				+ "</Item>"
				+ "<Item>"
				+ "<product_id>2</product_id>"
				+ "<product_name>袜子</product_name>"
				+ "<quantity>2</quantity>"
				+ "<lntype>衣物</lntype>"
				+ "</Item>"
				+ "</Items>"
				+ "</package>"
				+ "<package>"
				+ "<express_id>4535</express_id>"
				+ "<express_name>explink</express_name>"
				+ "<package_id>L53454356"
				+ i
				+ "</package_id>"
				+ "<package_type>1</package_type >"
				+ "<contact_name>李四</contact_name>"
				+ "<contact_phone>5345345345</contact_phone>"
				+ "<contact_phone2>53455435435</contact_phone2>"
				+ "<postal_code>4534524534</postal_code>"
				+ "<province>湖北省</province>"
				+ "<city>武汉市</city>"
				+ "<zone>洪山区</zone>"
				+ "<address>狮子山广场1号华中农业大学</address>"
				+ "<cod_amount>5335.25</cod_amount>"
				+ "<return_amount>0.00</return_amount>"
				+ "<ship_amount>5335.25</ship_amount>"
				+ "<returnproduct_amount>0.00</returnproduct_amount>"
				+ "<itemtotal>1</itemtotal>"
				+ "<weight>5.00</weight>"
				+ "<warehousearea>北京仓库</warehousearea>"
				+ "<remark>让他乖乖热歌</remark>"
				+ "<Items>"
				+ "<Item>"
				+ "<product_id>1</product_id>"
				+ "<product_name>耳朵</product_name>"
				+ "<quantity>2</quantity>"
				+ "<lntype>挖坟</lntype>"
				+ "</Item>"
				+ "<Item>"
				+ "<product_id>2</product_id>"
				+ "<product_name>眼睛</product_name>"
				+ "<quantity>2</quantity>"
				+ "<lntype>仍然</lntype>" + "</Item>" + "</Items>" + "</package>" + "</request_body>" + "</tms>";
		return xml;
	}

}