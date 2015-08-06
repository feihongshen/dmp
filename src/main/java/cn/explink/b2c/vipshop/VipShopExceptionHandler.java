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
	
	/**
	 * 校验 唯品会的业务响应编码
	 *  B00	数据处理无异常	
	 *	B01	找不到订单	order_sn对应的记录不存在时返回
	 *	B02	Bind节点中字段校验	如：当请求的不为空字段，传空值时返回。
	 *	B03	数据重复异常	当同一承运商传递的cust_data_id重复时返回
	 *  B04	非承运商承运的订单	当相应的单据不是该承运商承运时返回此编码
	 *	B99	数据处理出现不可预见异常	处理某数据出现异常时返回
	 * @param biz_response_code
	 * @param biz_response_msg
	 * @param vipShop
	 */
	public static void validateBizResponseCode(String biz_response_code,String biz_response_msg, VipShop vipShop){
		String expt_event = null;
		if("B01".equals(biz_response_code)){
			expt_event = "后台打印：返回码：[B01],原因：[找不到订单]，说明：[order_sn对应的记录不存在时返回]，biz_response_msg："+biz_response_msg;
		} else if("B02".equals(biz_response_code)){
			expt_event = "后台打印：返回码：[B02],原因：[Bind节点中字段校验]，说明：[如：当请求的不为空字段，传空值时返回]，biz_response_msg："+biz_response_msg;
		} else if("B03".equals(biz_response_code)){
			expt_event = "后台打印：返回码：[B03],原因：[数据重复异常]，说明：[	当同一承运商传递的cust_data_id重复时返回]，biz_response_msg："+biz_response_msg;
		} else if("B04".equals(biz_response_code)){
			expt_event = "后台打印：返回码：[B04],原因：[非承运商承运的订单]，说明：[当相应的单据不是该承运商承运时返回此编码]，当前承运商编码为：[" + vipShop.getShipper_no() + "]，biz_response_msg："+biz_response_msg;
		} else if("B99".equals(biz_response_code)){
			expt_event = "后台打印：返回码：[B99],原因：[数据处理出现不可预见异常]，说明：[处理某数据出现异常时返回]，biz_response_msg："+biz_response_msg;
		}
		
		if (!"B00".equals(biz_response_code)) {
			throw new RuntimeException(expt_event);
		}
		
	}
}
