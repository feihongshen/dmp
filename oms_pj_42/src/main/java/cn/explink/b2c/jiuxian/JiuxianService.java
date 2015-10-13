package cn.explink.b2c.jiuxian;

import java.io.IOException;
import java.util.List;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.util.WebServiceHandler;
import cn.explink.util.MD5.MD5Util;

@Service
public class JiuxianService {
	@Autowired
	B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	GetDmpDAO getdmpDAO;

	private Logger logger = LoggerFactory.getLogger(JiuxianService.class);

	public long feedback_status(int key) {
		long calcCount = 0;

		try {
			JiuxianWang jiuxian = getJiuxianwang(key);
			WebServiceHandler wshande = new WebServiceHandler();

			if (jiuxian.getTrack_url().equals("") || jiuxian.getTrack_url() == null) {
				return -1;
			}
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataForBenlai(jiuxian.getCustomerid(), "2000");
				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送[酒仙网]的数据,状态:{}");
					return 0;
				}
				calcCount += datalist.size();
				String cwb = "";
				int i = 0;
				String orderxml = "";
				for (B2CData b : datalist) {
					// 用订单号查询出4.7.9.36的循环xml,给cwb赋值，下个和cwb比较，相同continue
					if (b.getCwb().equals(cwb)) {
						continue;
					}
					if (i == 500) {
						break;
					}
					List<B2CData> cwblist = b2cDataDAO.getCwB(b.getCwb());
					String callback = "";
					if (cwblist.size() > 0 && cwblist != null) {
						callback = PushXmlForReturn(b, orderxml, cwblist);
						i++;
					}
					orderxml += callback;
					cwb = b.getCwb();
				}
				if (orderxml.equals("")) {
					return 0;
				}
				String requestxml = "<BatchOrdersUpdate>" + "<waybills>" + orderxml + "</waybills>" + "</BatchOrdersUpdate>";

				String password = MD5Util.md5(jiuxian.getsShippedCode() + jiuxian.getUserkey() + requestxml);
				try {
					logger.info("需要发送给【酒仙网】的xml={}", requestxml);
					Object parms[] = { requestxml, jiuxian.getsShippedCode(), password };
					String returnxml = (String) wshande.invokeWs(jiuxian.getTrack_url(), "ReceiveExpress", parms);
					logger.info("【酒仙网】返回的xml={}", returnxml);
					return 0;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calcCount;

	}

	private String PushXmlForReturn(B2CData b, String callback, List<B2CData> cwblist) {
		String Status = "";
		String status_desc = "";
		StringBuffer sb = new StringBuffer();
		String statusTime = "";
		String transcwb = "";
		for (B2CData l : cwblist) {
			String json_content = l.getJsoncontent();
			JSONObject job = JSONObject.fromObject(json_content);
			Status = job.getString("type");
			statusTime = job.getString("acceptTime");
			sb.append("<step>" + "<times>" + statusTime + "</times>" + "<info>" + job.getString("state_desc") + "</info>" + "<opuser>" + job.getString("courier") + "</opuser></step>");

			b2cDataDAO.UpdateBenlaiSql(l.getCwb(), "", "1", String.valueOf(l.getB2cid()));
			status_desc = job.getString("status");
			transcwb = job.getString("transcwb");
		}
		sb.toString();
		callback = "<waybill>" + "<invoiceNo>" + b.getCwb() + "</invoiceNo>" + "<orderSn>" + transcwb + "</orderSn>" + "<state>" + Status + "</state>" + "<state_desc>" + status_desc + "</state_desc>"
				+ "<steps>" + sb + "</steps></waybill>";
		return callback;
	}

	/**
	 * 状态反馈 查询单独的表的数据
	 * 
	 * @param saohuobang
	 *            在express_b2cdata_search_saohuobang
	 */
	public JiuxianWang getJiuxianwang(int key) {
		JiuxianWang jiuxian = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			jiuxian = (JiuxianWang) JSONObject.toBean(jsonObj, JiuxianWang.class);
		} else {
			jiuxian = new JiuxianWang();
		}
		return jiuxian;
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

	/*
	 * 发送方式
	 */
	public String getPostMethodResult(String url, String token, String post_xml, String sShippedCode) throws IOException, HttpException {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(40000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("xml", post_xml), new NameValuePair("sign", token), new NameValuePair("shipping_code", sShippedCode), }; // 原始信息
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		httpClient.executeMethod(postMethod); // post数据
		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();
		return str;
	}
}
