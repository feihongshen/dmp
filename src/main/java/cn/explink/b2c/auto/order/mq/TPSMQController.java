package cn.explink.b2c.auto.order.mq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vip.platform.middleware.vms.IPublisher;
import com.vip.platform.middleware.vms.Message;
import com.vip.platform.middleware.vms.VMSClient;

import cn.explink.b2c.vipshop.VipShopGetCwbDataService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/tPSMQ")
public class TPSMQController {

	@Autowired
	VipShopGetCwbDataService vipshopService;
	@Autowired
	BranchDAO branchDAO;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("vipshopObject", this.vipshopService.getVipShop(key));
		model.addAttribute("warehouselist", this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/tpsMQ";

	}

	@RequestMapping("/sendMQInfo")
	public String sendMQInfo(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String content = request.getParameter("info");
		String topic = request.getParameter("topic");
		String routingKey = request.getParameter("routingKey");
		try {
			if(!content.trim().equals("")){
				//InfDmpOrderSendVO orderSend = objectMapper.readValue(request.getParameter("info"), InfDmpOrderSendVO[].class)[0];
				VMSClient client = VMSClient.getDefault();
				Message msg = Message.from(content);
				msg.addRoutingKey(routingKey);
		        msg.qos().durable(true); // 非持久化的消息在宕机后消息会丢失。对于订单/运单类消息，必须设置为持久化。
		        // msg.qos().priority(0); // 数字大的表示优先级高。 在同一个topic中，优先级高的消息先于优先级低的消息被消费。可选设置。
		        // 推送到MQ
		        client.options().setConfirmable(true).setWaitingTimeout(2000).setFailFastEnabled(false);
		        IPublisher publisher = client.publish(topic, msg);// 要改回异常channel
		        return "sendMQInfo";
			}else{
				return "sendMQInfo";
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
			return "sendMQInfo";
		}
	}
}
