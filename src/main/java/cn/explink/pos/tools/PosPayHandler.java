package cn.explink.pos.tools;

import java.math.BigDecimal;

public class PosPayHandler {

	/**
	 * 返回签收类型 0款，非0款
	 * 
	 * @param receivablefee
	 * @param Sign_Remark
	 * @return
	 */
	public static String getSignPayAmountType(BigDecimal receivablefee, String Sign_Remark) {
		if (receivablefee.compareTo(BigDecimal.ZERO) == 0) {
			Sign_Remark += SignPayAmountTypeEnum.LingKuanQianShou.getSign_text();
		} else {
			Sign_Remark += SignPayAmountTypeEnum.FeiLingKuanQianShou.getSign_text();
		}
		return Sign_Remark;
	}

}
