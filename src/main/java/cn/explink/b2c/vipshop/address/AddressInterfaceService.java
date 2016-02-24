package cn.explink.b2c.vipshop.address;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.vipshop.address.domain.request.RequestHead;
import cn.explink.b2c.vipshop.address.domain.request.RequestItem;
import cn.explink.b2c.vipshop.address.domain.request.RequestXML;
import cn.explink.b2c.vipshop.address.domain.response.ResponseItem;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.util.MD5.MD5Util;

@Service
public class AddressInterfaceService {
	@Autowired
	AddressMatchService addressMatchService;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	JiontDAO jiontDAO;

	private Logger logger = LoggerFactory.getLogger(AddressInterfaceService.class);

	public String getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public VipShopAddress getVipShopAdrress(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		VipShopAddress vipshop = (VipShopAddress) JSONObject.toBean(jsonObj, VipShopAddress.class);
		return vipshop;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		VipShopAddress vipshop = new VipShopAddress();
		vipshop.setShipper_no(request.getParameter("shipper_no"));
		vipshop.setPrivate_key(request.getParameter("private_key"));
		vipshop.setGetMaxCount(Integer.parseInt(request.getParameter("getMaxCount")));
		vipshop.setPrintflag(Integer.parseInt(request.getParameter("printflag")));
		JSONObject jsonObj = JSONObject.fromObject(vipshop);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {

			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	public String getAddress(String xml, VipShopAddress vip) {
		try {
			if (vip == null) {
				return this.returnExp("未开启唯品会请求地址库对接");
			}
			RequestXML requestXML = AddressUnmarchal.Unmarchal(xml);
			if (!this.checkKey(requestXML.getHead(), vip.getPrivate_key())) {
				this.logger.info("唯品会请求地址库 key错误！");
				return this.returnExp("key错误");
			} else {// 请求地址库
				if (!vip.getShipper_no().equals(requestXML.getHead().getUsercode())) {
					this.logger.info("唯品会匹配站点: 地址：唯品会请求地址库账号错误 ,Usercode:{}", requestXML.getHead().getUsercode());
					return this.returnExp("唯品会请求地址库账号错误");
				}
				int count = 100;
				try {
					count = vip.getGetMaxCount();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ((requestXML.getItems().getItem() == null) || (requestXML.getItems().getItem().size() > count)) {
					if (requestXML.getItems().getItem() == null) {
						return this.returnExp("请求的地址为空");
					}
					return this.returnExp("请求地址数量单次超过" + count);
				}
				List<ResponseItem> items = this.getBranchByAddress(requestXML.getItems().getItem(), vip);
				if ((items != null) && (items.size() > 0)) {
					for (ResponseItem responseItem : items) {
						if (!responseItem.getNetid().equals("")) {
							return this.returnXml("", items);
						}
					}
					return this.returnExp("没有匹配到任何站点");
				} else {
					return this.returnExp("没有匹配到任何站点");
				}
			}
		} catch (UnsupportedEncodingException e) {
			this.logger.error("唯品会匹配站点: 地址：唯品会请求地址库 xml异常", e);
			return this.returnExp("xml报文格式不正确");
		} catch (JAXBException e) {
			this.logger.error("唯品会匹配站点: 地址：唯品会请求地址库 xml异常", e);
			return this.returnExp("xml报文格式不正确");
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
		this.logger.info("唯品会匹配站点: 地址：唯品会请求地址库 返回xml:{}", xml);
		return xml;
	}

	public List<ResponseItem> getBranchByAddress(List<RequestItem> items, VipShopAddress vip) {
		List<ResponseItem> reItems = new ArrayList<ResponseItem>();
		for (RequestItem requestItem : items) {
			ResponseItem res = new ResponseItem();
			// modified by songkaojun 2016-02-18 苏州品骏--分拨中心出现大量白单 去掉重复的省市区
			// String address = requestItem.getProvince() +
			// requestItem.getCity() + requestItem.getArea() +
			// requestItem.getAddress();
			String address = requestItem.getAddress();
			JSONObject json = this.addressMatchService.matchAddressByInterface(requestItem.getItemno(), address, vip.getPrintflag());
			res.setItemno(json.getString("itemno"));
			res.setNetid(json.getString("netid"));
			res.setNetpoint(json.getString("netpoint"));
			res.setRemark(json.getString("remark"));
			reItems.add(res);
		}
		return reItems;
	}

	/**
	 * 返回头部信息+站点匹配情况
	 *
	 * @param expMsg
	 * @param responseItems
	 * @return
	 */
	public String returnXml(String expMsg, List<ResponseItem> responseItems) {
		String xmlbegin = "<response><head>" + "<msg><![CDATA[" + expMsg + "]]></msg>" + "</head>" + "<items>";
		for (ResponseItem responseItem : responseItems) {
			xmlbegin += "<item>" + "<itemno>" + responseItem.getItemno() + "</itemno>" + "<netid>" + responseItem.getNetid() + "</netid>" + "<netpoint><![CDATA[" + responseItem.getNetpoint()
					+ "]]></netpoint>" + "<remark><![CDATA[" + responseItem.getRemark() + "]]></remark>" + "</item>";
		}
		String xmlEnd = "</items>" + "</response>";
		this.logger.info("唯品会请求地址库 返回xml:{}", xmlbegin + xmlEnd);
		return xmlbegin + xmlEnd;
	}

	public boolean checkKey(RequestHead head, String key) {
		String usercode = head.getUsercode();
		String batchno = head.getBatchno();
		String mac = head.getKey();
		// SystemInstall systemInstall =
		// systemInstallDAO.getSystemInstallByName("vipshopAddressKey");
		// String key = systemInstall.getValue();//后期在页面中传进来
		String checkMac = MD5Util.md5(usercode + key + batchno);
		this.logger.info("唯品会请求地址库  正确的mac:" + checkMac);
		return checkMac.equals(mac);
	}

}
