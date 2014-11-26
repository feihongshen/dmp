package cn.explink.b2c.vipshop.addresspre;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.vipshop.addresspre.AddressUnmarchal;
import cn.explink.b2c.vipshop.addresspre.request.RequestItem;
import cn.explink.b2c.vipshop.addresspre.request.RequestXML;
import cn.explink.b2c.vipshop.addresspre.response.ResponseItem;
import cn.explink.service.addressmatch.AddressMatchService;

@Service
public class AddressInterService {
	@Autowired
	AddressMatchService addressMatchService;
	private Logger logger = LoggerFactory.getLogger(AddressInterService.class);

	public String getAddress(String xml) {
		try {
			RequestXML requestXML = AddressUnmarchal.Unmarchal(xml);
			// 请求地址库
			List<ResponseItem> items = getBranchByAddress(requestXML.getItems().getItem());
			String batchno = requestXML.getHead().getBatchno();
			String usercode = requestXML.getHead().getUsercode();
			if (items != null && items.size() > 0) {
				return returnXml("", items, batchno, usercode);
			} else {
				return returnExp("没有匹配到任何站点");
			}

		} catch (UnsupportedEncodingException e) {
			logger.error("唯品会匹配站点: 地址：唯品会请求地址库 xml异常", e);
			return returnExp("xml报文格式不正确");
		} catch (JAXBException e) {
			logger.error("唯品会匹配站点: 地址：唯品会请求地址库 xml异常", e);
			return returnExp("xml报文格式不正确");
		}
	}

	/**
	 * 返回异常的xml
	 * 
	 * @param expMsg
	 * @return
	 */
	public String returnExp(String expMsg) {
		String xml = "<response><head>" + "<msg><![CDATA[" + expMsg + "]]></msg>" + "</head>" + "</response>";
		logger.info("唯品会匹配站点: 地址：唯品会请求地址库 返回xml:{}", xml);
		return xml;
	}

	public List<ResponseItem> getBranchByAddress(List<RequestItem> items) {
		List<ResponseItem> reItems = new ArrayList<ResponseItem>();
		// AddressMatchService addressMatchService1 = new AddressMatchService();
		for (RequestItem requestItem : items) {
			ResponseItem res = new ResponseItem();
			String address = requestItem.getAddress();
			JSONObject json = addressMatchService.matchAddressByPre(requestItem.getItemno(), address);
			res.setItemno(json.getString("itemno"));
			res.setNetid(json.getString("netid"));
			res.setNetpoint(json.getString("netpoint"));
			res.setProvince(json.getString("province"));
			res.setCity(json.getString("city"));
			res.setDistrict(json.getString("district"));
			res.setFinaladdress(json.getString("finaladdress"));
			res.setRemark(json.getString("remark"));
			reItems.add(res);
		}
		return reItems;
	}

	/**
	 * 返回头部信息+站点匹配情况 *
	 * 
	 * @param expMsg
	 * @param responseItems
	 * @param batchno
	 * @param usercode
	 * @return
	 */
	public String returnXml(String expMsg, List<ResponseItem> responseItems, String batchno, String usercode) {
		String xmlbegin = "<response><head>" + "<msg><![CDATA[" + expMsg + "]]></msg>" + "<batchno>" + batchno + "</batchno>" + "<usercode>" + usercode + "</usercode>" + "</head>" + "<items>";
		for (ResponseItem responseItem : responseItems) {
			xmlbegin += "<item>" + "<itemno>" + responseItem.getItemno() + "</itemno>" + "<province><![CDATA[" + responseItem.getProvince() + "]]></province>" + "<city><![CDATA["
					+ responseItem.getCity() + "]]></city>" + "<district><![CDATA[" + responseItem.getDistrict() + "]]></district>" + "<finaladdress<![CDATA[>" + responseItem.getFinaladdress()
					+ "]]></finaladdress>" + "<remark><![CDATA[" + responseItem.getRemark() + "]]></remark>" + "</item>";
		}
		// + "<netid>" + responseItem.getNetid()+ "</netid>"
		// + "<netpoint><![CDATA[" + responseItem.getNetpoint()+
		// "]]></netpoint>"
		String xmlEnd = "</items>" + "</response>";
		logger.info("唯品会请求地址库 返回xml:{}", xmlbegin + xmlEnd);
		return xmlbegin + xmlEnd;
	}

}
