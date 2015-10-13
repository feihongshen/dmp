package cn.explink.b2c.moonbasa;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.cwbsearch.B2cDatasearch;
import cn.explink.b2c.cwbsearch.B2cDatasearchDAO;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class MoonbasaService {
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2cDatasearchDAO b2cDatasearchDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 获取配置信息
	public Moonbasa getMoonbasa(int key) {
		Moonbasa mbs = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			mbs = (Moonbasa) JSONObject.toBean(jsonObj, Moonbasa.class);
		} else {
			mbs = new Moonbasa();
		}
		return mbs;
	}

	/**
	 * 梦芭莎请求接口开始
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */

	public String requestCwbSearchInterface(Moonbasa mbs, String from, String to) throws Exception {

		try {
			to = to == null || to.isEmpty() ? DateTimeUtil.getNowTime() : to;

			long dataSize = b2cDatasearchDAO.getDataCountByKey(Long.valueOf(mbs.getCustomerid()), from, to);

			if (dataSize == 0) {
				return "-2";
			}

			if (dataSize > 5000) {
				return "-3";
			}

			List<B2cDatasearch> datalist = b2cDatasearchDAO.getDataListByKey(Long.valueOf(mbs.getCustomerid()), from, to);

			// 构建Xml
			StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			sub.append("<ArrayOfDeliveryInfo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
			for (B2cDatasearch data : datalist) {
				sub.append("<DeliveryInfo>");
				sub.append("<DeliveryCode>" + data.getCwb() + "</DeliveryCode>");
				sub.append("<Status>" + getStatus(data.getFlowordertype(), data.getDeliverystate()) + "</Status>");
				sub.append("<Desc>" + data.getContent() + "</Desc>");
				sub.append("<SignDate>" + getSignDate(data.getCretime(), data.getDeliverystate()) + "</SignDate>");
				sub.append("<Subscriber>" + getSignMan(data.getSignname(), data.getDeliverystate()) + "</Subscriber>");
				sub.append("<Carrier>" + data.getOperatorname() + "</Carrier>");
				sub.append("<Phone>" + data.getMobilephone() + "</Phone>");
				sub.append("</DeliveryInfo>");
			}

			sub.append("</ArrayOfDeliveryInfo>");
			return sub.toString();

		} catch (Exception e) {
			String error = "处理[梦芭莎]查询请求发生未知异常:" + e.getMessage();
			logger.error(error, e);
			return "-2";
		}

	}

	private String getStatus(long flowordertype, long deliverystate) {
		if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			return "2";
		}
		if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return "1";
		}
		if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return "3";
		}
		if (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			return "0";
		}
		return "1";
	}

	private String getSignDate(String datetime, long deliverystate) {
		if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return datetime;
		}

		return "";
	}

	private String getSignMan(String signName, long deliverystate) {
		if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return signName;
		}

		return "";
	}

}
