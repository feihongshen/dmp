package cn.explink.b2c.maisike;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.URLEditor;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.MD5.MD5Util;

@Service
public class MaisikeService_Search {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CwbDAO cwbDao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public String Search_maisike_info(Maisike mInfo, String cwb, String sign, String time) throws UnsupportedEncodingException {

		CwbOrder dto = cwbDao.getCwbByCwb(cwb);
		if (dto == null) {
			return "未检索到数据";
		}
		SearchOrder order = new SearchOrder();
		order.setBackcargoname(URLEncoder.encode(dto.getBackcarname(), "UTF-8"));
		order.setCargoamount(dto.getCaramount() == null ? BigDecimal.ZERO : dto.getCaramount());
		order.setConsigneephone(dto.getConsigneephone());
		order.setConsigneename(URLEncoder.encode(dto.getConsigneename(), "UTF-8"));
		order.setConsigneemobile(dto.getConsigneemobile());
		order.setConsigneeaddress(URLEncoder.encode(dto.getConsigneeaddress(), "UTF-8"));
		order.setTranscwb(dto.getTranscwb());
		order.setConsigneepostcode(dto.getConsigneepostcode());
		order.setCustomercommand(URLEncoder.encode(dto.getCustomercommand(), "UTF-8"));
		order.setCwb(cwb);
		order.setTranscwb(dto.getTranscwb());
		order.setReceivablefee(dto.getReceivablefee() == null ? BigDecimal.ZERO : dto.getReceivablefee());
		order.setSendcargoname(URLEncoder.encode(dto.getSendcarname(), "UTF-8"));
		order.setSendtime(dto.getPrinttime());
		order.setPaywayid(dto.getPaywayid());
		order.setCwbordertypeid(dto.getCwbordertypeid());
		order.setSendcargonum(dto.getSendcarnum());
		order.setCargorealweight(dto.getCarrealweight() == null ? BigDecimal.ZERO : dto.getCarrealweight());
		order.setPaybackfee(dto.getPaybackfee() == null ? BigDecimal.ZERO : dto.getPaybackfee());
		// JSON s=JSONObject.fromObject(dto);
		String s;
		try {
			s = JacksonMapper.getInstance().writeValueAsString(order);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return "异常" + e.getMessage();
		}

	}

	public static void main(String[] args) {
		CwbOrderDTO dto = new CwbOrderDTO();
		dto.setAccountarea("4234");
		dto.setBackcargoamount("44");
		dto.setAccountareaid(3432);
		dto.setCargoamount("56");
		JSON s = JSONObject.fromObject(dto);
		System.out.println(s);

	}

	public Maisike getMaisike(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Maisike maisike = (Maisike) JSONObject.toBean(jsonObj, Maisike.class);
		return maisike;
	}

	private Object getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

}
