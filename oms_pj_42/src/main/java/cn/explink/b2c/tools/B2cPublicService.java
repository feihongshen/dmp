package cn.explink.b2c.tools;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.amazon.AmazonService_CommitDeliverInfo;
import cn.explink.b2c.dangdang.DangDangService;
import cn.explink.b2c.gome.GomeService_CommitDeliverInfo;
import cn.explink.b2c.jumeiyoupin.JumeiYoupinService;
import cn.explink.b2c.rufengda.RufengdaService_CommitDeliverInfo;
import cn.explink.b2c.smile.SmileService;
import cn.explink.b2c.tmall.TmallService;
import cn.explink.b2c.vipshop.VipShopCwbFeedBackService;
import cn.explink.b2c.yangguang.YangGuangService_upload;
import cn.explink.b2c.yihaodian.YihaodianService;
import cn.explink.domain.Customer;

@Service
public class B2cPublicService {
	@Autowired
	JumeiYoupinService jumeiYoupinService;
	@Autowired
	DangDangService dangdangService;
	@Autowired
	TmallService tmallService;
	@Autowired
	VipShopCwbFeedBackService vipshopService;
	@Autowired
	RufengdaService_CommitDeliverInfo rufengdaService_CommitDeliverInfo;
	@Autowired
	SmileService smileService;
	@Autowired
	YihaodianService yihaodianService;
	@Autowired
	GomeService_CommitDeliverInfo gomeService_CommitDeliverInfo;
	@Autowired
	YangGuangService_upload yangGuangService_upload;
	@Autowired
	AmazonService_CommitDeliverInfo amazonService_CommitDeliverInfo;

	private Logger logger = LoggerFactory.getLogger(B2cPublicService.class);

	/**
	 * 手工推送,成功返回条数
	 * 
	 * @param customer
	 * @param b2cEnumkey
	 * @param cwbs
	 * @return
	 */
	public int sendPublic(Customer customer, String b2cEnumkey, List<String> cwbs, long send_b2c_flag) {
		int count = 0;
		if (b2cEnumkey.equals(B2cEnum.JuMeiYouPin.getKey())) {
			count = jumeiYoupinService.sendByCwbs(getCwbs(cwbs), send_b2c_flag);
		}
		if (b2cEnumkey.equals(B2cEnum.DangDang.getKey())) {
			// count = dangdangService.sendByCwbs(getCwbs(cwbs),send_b2c_flag);
		}
		if (b2cEnumkey.equals(getB2cEnumKeys(customer, "tmall"))) {
			count = tmallService.sendByCwbs(getCwbs(cwbs), send_b2c_flag, b2cEnumkey);
		}
		if (b2cEnumkey.equals(getB2cEnumKeys(customer, "vipshop"))) {
			count = vipshopService.sendByCwbs(getCwbs(cwbs), send_b2c_flag, b2cEnumkey);
		}
		if (b2cEnumkey.equals(B2cEnum.Rufengda.getKey())) {
			// count =
			// rufengdaService_CommitDeliverInfo.sendByCwbs(getCwbs(cwbs),send_b2c_flag);
		}
		if (b2cEnumkey.equals(B2cEnum.Smile.getKey())) {
			// count = smileService.sendByCwbs(getCwbs(cwbs),send_b2c_flag);
		}
		if (b2cEnumkey.equals(getB2cEnumKeys(customer, "yihaodian"))) {
			// count = yihaodianService.sendByCwbs(getCwbs(cwbs),send_b2c_flag);
		}
		if (b2cEnumkey.equals(B2cEnum.Gome.getKey())) {
			count = gomeService_CommitDeliverInfo.sendByCwbs(getCwbs(cwbs), send_b2c_flag);
		}
		if (b2cEnumkey.equals(B2cEnum.YangGuang.getKey())) {
			// count =
			// yangGuangService_upload.sendByCwbs(getCwbs(cwbs),send_b2c_flag);
		}
		return count;
	}

	public void sendPublic(Customer customer, String b2cEnumkey) {
		if (b2cEnumkey.equals(B2cEnum.JuMeiYouPin.getKey() + "")) {
			jumeiYoupinService.feedback_status();
		}
		if (b2cEnumkey.equals(B2cEnum.DangDang.getKey() + "")) {
			dangdangService.feedback_status();
		}
		if (b2cEnumkey.equals(getB2cEnumKeys(customer, "tmall"))) {
			tmallService.feedback_status(Integer.parseInt(b2cEnumkey));
		}
		if (b2cEnumkey.equals(getB2cEnumKeys(customer, "vipshop"))) {
			vipshopService.feedback_status(Integer.parseInt(b2cEnumkey));
		}
		if (b2cEnumkey.equals(B2cEnum.Rufengda.getKey() + "")) {
			rufengdaService_CommitDeliverInfo.CommitDeliverInfo_interface();
		}
		if (b2cEnumkey.equals(B2cEnum.Smile.getKey())) {
			smileService.feedback_status();
		}
		if (b2cEnumkey.equals(getB2cEnumKeys(customer, "yihaodian"))) {
			yihaodianService.YiHaoDianInterfaceInvoke(Integer.parseInt(b2cEnumkey));
		}
		if (b2cEnumkey.equals(B2cEnum.Gome.getKey() + "")) {
			gomeService_CommitDeliverInfo.commitDeliverInfo_interface();
		}
		if (b2cEnumkey.equals(B2cEnum.YangGuang.getKey() + "")) {
			yangGuangService_upload.createTxtFile_DeliveryFinishMethod();
		}
		if (b2cEnumkey.equals(B2cEnum.Amazon.getKey() + "")) {
			amazonService_CommitDeliverInfo.commitDeliverInfo_interface();
			amazonService_CommitDeliverInfo.commitDeliver_Cod();
		}
	}

	public String getCwbs(List<String> cwbs) {
		String cwb = "";
		if (cwbs != null && cwbs.size() > 0) {
			for (String str : cwbs) {
				cwb += "'" + str + "',";
			}
		}
		cwb = cwb.length() > 0 ? cwb.substring(0, cwb.length() - 1) : "'--'";
		return cwb;
	}

	private String getB2cEnumKeys(Customer customer, String constainsStr) {
		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains(constainsStr)) {
				if (customer.getB2cEnum().equals(String.valueOf(enums.getKey()))) {
					return enums.getKey() + "";
				}
			}
		}
		return "";
	}
}
