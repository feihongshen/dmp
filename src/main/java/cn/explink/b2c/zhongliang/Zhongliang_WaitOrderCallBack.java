package cn.explink.b2c.zhongliang;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.yihaodian.RestTemplateClient;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbDAO;

/**
 * 下载数据成功后回传
 * 
 * @author Administrator
 *
 */
@Service
public class Zhongliang_WaitOrderCallBack extends ZhongliangService {
	private Logger logger = LoggerFactory.getLogger(Zhongliang_WaitOrderCallBack.class);
	@Autowired
	RestTemplateClient restTemplate;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;

	/**
	 * 回传下载成功记录
	 */
	public void ExportCallBackByZhongliang() {
		Zhongliang zl = getZhongliang(B2cEnum.Zhongliang.getKey());
		try {
			List<CwbOrderDTO> datalist = dataImportDAO_B2c.getCwbOrderByCustomerIdAndPageCount(Long.parseLong(zl.getCustomerid()), Long.parseLong(zl.getNums()));
			if (datalist == null || datalist.size() == 0) {
				return;
			}

			for (CwbOrderDTO cDto : datalist) {
				String str = "<Request  service=\"WaitOrder\" lang=\"zh-CN\">" + "<Head>client</Head>" + "<Body>" + "<Order>" + "<SendOrderID>" + cDto.getCwb() + "</SendOrderID>" + "<MailNO>"
						+ cDto.getTranscwb() + "</MailNO>" + "<DealStatus>1</DealStatus>" + "<Reason></Reason>" + "</Order>" + "</Body>" + "</Request>";
				Map<String, String> map = checkData(zl, "up");
				map.put("XML", str);
				String back = RestHttpServiceHanlder.sendHttptoServer(map, zl.getWaitOrder_url());
				logger.info("返回 back={},cwb={}", back, cDto.getCwb());
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}

}
