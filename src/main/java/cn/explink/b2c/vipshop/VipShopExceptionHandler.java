package cn.explink.b2c.vipshop;

public class VipShopExceptionHandler {

	public static void respValidateMessage(String sys_response_code, String sys_response_msg, VipShop vipShop) {

		String expt_event = null;
		if ("S01".equals(sys_response_code)) {
			expt_event = "后台打印：返回码：[S01],原因:[非法的XML格式],说明：[当TMS解析XML出错时返回],sys_response_msg：" + sys_response_msg;
		} else if ("S02".equals(sys_response_code)) {
			expt_event = "后台打印：返回码：[S02],原因:[非法的数字签名],说明：[当通过接口参数sign验证签名不正确时返回],sys_response_msg：" + sys_response_msg;
		} else if ("S03".equals(sys_response_code)) {
			expt_event = "后台打印：返回码：[S03],原因:[非法的物流公司],说明：[当请求字段custCode在TMS中不存在时返回],当前编码：[" + vipShop.getShipper_no() + "],sys_response_msg：" + sys_response_msg;
		} else if ("S04".equals(sys_response_code)) {
			expt_event = "后台打印：返回码：[S04],原因:[请求数据量超限],说明：[当批量传输数据时，业务数据量超过最大限制时返回],当前请求数量：[" + vipShop.getGetMaxCount() + "],sys_response_msg：" + sys_response_msg;
		} else if ("S05".equals(sys_response_code)) {
			expt_event = "后台打印：返回码：[S05],原因:[Head节点中某字段为空],说明：[当请求的不为空字段，传空值时返回],sys_response_msg：" + sys_response_msg;
		} else if ("S98".equals(sys_response_code)) {
			expt_event = "后台打印：返回码：[S98],原因:[一天中请求次数过多],说明：[当请求次数超过系统配额后，返回],sys_response_msg：" + sys_response_msg;
		} else if ("S99".equals(sys_response_code)) {
			expt_event = "后台打印：返回码：[S99],原因:[系统出现不可预见异常],说明：[TMS其它问题导致出错时返回],sys_response_msg：" + sys_response_msg;
		}
		if (!"S00".equals(sys_response_code)) {
			throw new RuntimeException(expt_event);
		}

	}
}
