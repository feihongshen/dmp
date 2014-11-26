package cn.explink.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;
import cn.explink.util.JMath;

@Component
public class TransCwbValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 发送单号的非空验证
		if (!StringUtils.hasLength(cwbOrder.getTranscwb())) {
			throw new RuntimeException("运单号为空");
		}
		// 发送单号的非法字符验证,2013-6-25，因为加入了一票多件的需求，现决定将该验证屏蔽掉
		/*
		 * if (!JMath.checknumletter(cwbOrder.getTranscwb())) { throw new
		 * RuntimeException("运单号含有非数字或非字母字符"); }
		 */
		// 验证承运商是否对应订单记录,2013-6-25，因为加入了一票多件的需求，现决定将该验证屏蔽掉
		/*
		 * if(cwbOrder.getCommon()!=null&&cwbOrder.getCommon().getOrderprefix()
		 * != null && cwbOrder.getCommon().getOrderprefix().length()>0){
		 * String[] str = cwbOrder.getCommon().getOrderprefix().split(","); for
		 * (int j = 0; j < str.length; j++) {
		 * if(str[j].equalsIgnoreCase(cwbOrder.getTranscwb().substring(0,
		 * str[j].length()))){ return; } } throw new
		 * RuntimeException("承运商运单号与承运商单号前缀不一致："+cwbOrder.getCwb()); }else
		 * if(cwbOrder.getCommon()==null){ throw new
		 * RuntimeException("没有找到该承运商："+cwbOrder.getCwb()); }
		 */
	}

}
