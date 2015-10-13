package cn.explink.b2c.amazon;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;
import cn.explink.domain.B2CCodData;
import cn.explink.enumutil.DeliveryStateEnum;

public class AmazonDataToTxtUtil {
	/**
	 * cod回传文件头信息
	 * 
	 * @param count
	 * @return
	 */
	public static String getHeader(long count) {
		StringBuffer header = new StringBuffer();
		String headStr = "HAmazon";// 固定开头
		header.append(headStr);
		String zero = "000000";
		if (6 - (count + "").length() > 0) {// 文件递增数量
			header.append(zero.substring(0, 6 - (count + "").length()) + count + "");
		} else {
			header.append(count + "");
		}
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		header.append(date);// 文件生成日期
		String space = "";
		for (int i = 0; i < 165; i++) {// 连接空格数量
			space += " ";
		}
		header.append(space);
		return header.toString();
	}

	public static String getDetail(B2CCodData b2cCodData) {
		StringBuffer detailStr = new StringBuffer();
		String headStr = "D";// 固定开头
		detailStr.append(headStr);
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		detailStr.append(date);// 款项生成日期
		String cwbSpace = "";
		for (int i = 0; i < 24; i++) {// 订单位数
			cwbSpace += " ";
		}
		if (cwbSpace.length() - b2cCodData.getCwb().length() > 0) {// 需要补充空格数
			detailStr.append(b2cCodData.getCwb() + cwbSpace.substring(0, cwbSpace.length() - b2cCodData.getCwb().length()));
		} else {
			detailStr.append(b2cCodData.getCwb());
		}
		String gudingSpace = "";// 固定空格
		for (int i = 0; i < 70; i++) {// 固定空格位数
			gudingSpace += " ";
		}
		detailStr.append(gudingSpace);

		String emaildate = b2cCodData.getCretime().substring(0, 10).replace("-", "");
		detailStr.append(emaildate);
		String posttime = b2cCodData.getPosttime().substring(0, 10).replace("-", "");// 包裹终结(成功,失败)日期
		detailStr.append(posttime);
		detailStr.append("+");// 固定“+”
		JSONObject json = JSONObject.fromObject(b2cCodData.getDatajson());

		String codMoney = json.getString("businessfee");
		if (codMoney.indexOf(".") < 0) {
			codMoney = codMoney + "00";
		} else {
			if (codMoney.substring(codMoney.indexOf("."), codMoney.length()).length() < 3) {
				codMoney = codMoney.replace(".", "") + "0";
			}
			codMoney = codMoney.replace(".", "");
		}
		String codzero = "00000000";
		if (8 - codMoney.length() > 0) {// 代收款金额 占8位
			codMoney = codzero.substring(0, 8 - codMoney.length()) + codMoney;
			detailStr.append(codMoney);
		} else {
			detailStr.append(codMoney);
		}
		detailStr.append("+00000000+00000000+");

		if (json.getDouble("receivedfee") > 0 && json.getInt("deliverystate") == DeliveryStateEnum.PeiSongChengGong.getValue()) {// 妥投
			detailStr.append("000000001");
		} else {// 失败
			detailStr.append(codMoney + "2");
		}
		if (json.getDouble("receivedfee") > 0 && json.getInt("deliverystate") == DeliveryStateEnum.PeiSongChengGong.getValue()) {// 妥投
			detailStr.append("+" + codMoney);
		} else {// 失败
			detailStr.append("+00000000");
		}
		String space = "";
		for (int i = 0; i < 7; i++) {// 连接空格数量
			space += " ";
		}
		detailStr.append(space);
		detailStr.append(posttime);
		String endspace = "";
		for (int i = 0; i < 34; i++) {// 结束连接空格数量
			endspace += " ";
		}
		detailStr.append(endspace);

		return detailStr.toString();
	}

	public static String getEnd(String orderCount, String codMoney, String susMoney, String filMoney) {
		StringBuffer endStr = new StringBuffer();
		String headStr = "T";// 固定开头
		endStr.append(headStr);
		String zero = "000000000";
		if (9 - orderCount.length() > 0) {// 订单数
			endStr.append(zero.substring(0, 9 - orderCount.length()) + orderCount);
		} else {
			endStr.append(orderCount);
		}
		String codMoneyzero = "0000000000";
		if (10 - codMoney.length() > 0) {// cod代收款总和
			endStr.append(codMoneyzero.substring(0, 10 - codMoney.length()) + codMoney);
		} else {
			endStr.append(codMoney);
		}
		endStr.append("00000000000000000000");

		String filMoneyzero = "0000000000";
		if (10 - filMoney.length() > 0) {// 未妥投cod代收款总和
			endStr.append(filMoneyzero.substring(0, 10 - filMoney.length()) + filMoney);
		} else {
			endStr.append(filMoney);
		}

		String susMoneyzero = "0000000000";
		if (10 - susMoney.length() > 0) {// 妥投cod代收款总和
			endStr.append(susMoneyzero.substring(0, 10 - susMoney.length()) + susMoney);
		} else {
			endStr.append(susMoney);
		}

		String endspace = "";
		for (int i = 0; i < 132; i++) {// 结束连接空格数量
			endspace += " ";
		}
		endStr.append(endspace);

		return endStr.toString();
	}

}
