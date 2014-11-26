package cn.explink.pos.alipay.searchpos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.pos.alipay.AliPay;
import cn.explink.pos.alipay.AlipayService;
import cn.explink.pos.alipay.xml.AlipayUnmarchal;
import cn.explink.pos.tools.PosEnum;

/**
 * 根据订单号查询支付交易结果
 * 
 * @author Administrator
 *
 */
@Service
public class TenPayService_searchPos extends AlipayService {
	private Logger logger = LoggerFactory.getLogger(TenPayService_searchPos.class);

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	JiontDAO jointDAO;

	/**
	 * 根据订单号查询荣邦银联
	 * 
	 * @return
	 */
	public String cwbSearchtoPos(String cwb) {
		try {
			String returnCode = "";

			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.AliPay.getKey());
			if (isOpenFlag == 0) {
				returnCode = "未开启UnionPay对接";
			}

			AliPay tlmpos = getAlipaySettingMethod(PosEnum.AliPay.getKey());
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
			if (cwbOrder == null) {
				returnCode = "订单号" + cwb + "在系统中不存在";
			}

			String requestXML = "<root>" + "<sign_type>MD5</sign_type>" + "<input_charset>UTF-8</input_charset>" + "<sign>" + 1111 + "</sign>" + "<sign_key_index>1</sign_key_index>" + "<partner>"
					+ tlmpos.getPartner() + "</partner>" + "<out_trade_no>" + cwb + "</out_trade_no>" + "<transaction_id>out_trade_no</transaction_id>" + "<retri_ref_num></retri_ref_num>" // 系统参考号
																																															// ?这个填什么
					+ "<account_no></account_no>" // 银行卡号，暂无
					+ "<trade_date></trade_date>" // 订单交易日期
					+ "<total_fee></total_fee>" // 订单交易日期
					+ "</root>";

			String responseXML = RestHttpServiceHanlder.sendHttptoServer(requestXML, tlmpos.getSearchPosUrl());

			TenPayBean tenpayBean = AlipayUnmarchal.UnmarchalTenPayBean(responseXML);

			/**
			 * RespContent content =respdata.getRespContent(); 明细字段，这里就不做详细解析了。
			 */

		} catch (Exception e) {
			logger.error("请求unionpay查询POS未知异常", e);
		}

		return null;
	}

}
