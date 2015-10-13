package cn.explink.b2c.gome;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.domain.B2CData;

@RequestMapping("/gome")
@Controller
public class GomeTest {
	@Autowired
	GomeService_CommitDeliverInfo gomeService_CommitDeliverInfo;
	@Autowired
	GuomeiService guomeiService;
	@Autowired
	private B2CDataDAO b2CDataDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/test")
	public void gome_Task() {
		gomeService_CommitDeliverInfo.commitDeliverInfo_interface();
	}

	@RequestMapping("/xiufu")
	public String xiufu(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwbs, @RequestParam(value = "type", required = false, defaultValue = "0") long type) {
		String[] cwbArray = cwbs.trim().split("\r\n");
		String s = "'";
		String s1 = "',";
		StringBuffer cwbsSqlBuffer = new StringBuffer();
		for (String cwb : cwbArray) {
			if (cwb.length() > 0) {
				cwbsSqlBuffer = cwbsSqlBuffer.append(s).append(cwb).append(s1);
			}
		}
		if (cwbsSqlBuffer.length() == 0) {
			return "/xiufu/gomexiufu";
		}
		// 整理sql要读取的cwb end
		model.addAttribute("cwbArray", cwbArray);
		String cwbsSql = cwbsSqlBuffer.substring(0, cwbsSqlBuffer.length() - 1);
		gomeService_CommitDeliverInfo.sendByCwbs(cwbsSql, type);
		model.addAttribute("result", "");
		return "/xiufu/gomexiufu";
	}

	@RequestMapping("/clrv")
	public String clrv(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwbs) {
		String[] cwbArray = cwbs.trim().split("\r\n");
		String s = "'";
		String s1 = "',";
		StringBuffer cwbsSqlBuffer = new StringBuffer();
		for (String cwb : cwbArray) {
			if (cwb.length() > 0) {
				cwbsSqlBuffer = cwbsSqlBuffer.append(s).append(cwb).append(s1);
			}
		}

		if (cwbsSqlBuffer.length() == 0) {
			return "/xiufu/gomelist";
		}

		// 整理sql要读取的cwb end
		String cwbsSql = cwbsSqlBuffer.substring(0, cwbsSqlBuffer.length() - 1);

		List<B2CData> datalist = b2CDataDAO.getDataListByCwbAndType(cwbsSql, "RV");
		String cwbss = "";
		if (datalist != null && datalist.size() > 0) {

			for (B2CData b2cData : datalist) {
				cwbss += "'" + b2cData.getCwb() + "',";
			}
			cwbss = cwbss.substring(0, cwbss.length() - 1);
		}
		List<B2CData> datalist2 = b2CDataDAO.getDataListByCwbAndType(cwbsSql, "DL");

		model.addAttribute("datalist", datalist2);
		return "/xiufu/gomelist";
	}

	@RequestMapping("/update")
	public String update(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwbs) {
		String cwbsSql = cwbs;
		List<B2CData> datalist = b2CDataDAO.getDataListByCwbAndType(cwbsSql, "RV");
		List<B2CData> rvDatalist = new ArrayList<B2CData>();
		if (datalist != null && datalist.size() > 0) {
			for (B2CData b2cData : datalist) {
				if (b2cData.getSend_b2c_flag() == 0) {// 把未推送的查出来
					rvDatalist.add(b2cData);
				}
			}
		}
		if (rvDatalist != null && rvDatalist.size() > 0) {// 先把未推送的推送一遍
			String cwbsS = "";
			for (B2CData b2cData : rvDatalist) {
				cwbsS += "'" + b2cData.getCwb() + "',";
			}
			cwbsS = cwbsS.substring(0, cwbsS.length() - 1);
			gomeService_CommitDeliverInfo.sendByCwbs(cwbsS, 0);
		}
		if (datalist != null && datalist.size() > 0) {
			for (B2CData b2cData : datalist) {
				b2CDataDAO.updateSendB2cFlagAndRV(b2cData.getB2cid());
			}
		}
		List<B2CData> datalist2 = b2CDataDAO.getDataListByCwbAndType(cwbsSql, "DL");
		if (datalist2 != null && datalist2.size() > 0) {// 先把妥投的改成未推送
			String b2cids = "";
			String cwbsS = "";
			for (B2CData b2cData : rvDatalist) {
				b2cids += "'" + b2cData.getCwb() + "',";
				cwbsS += "'" + b2cData.getCwb() + "',";
			}
			b2cids = b2cids.substring(0, b2cids.length() - 1);
			cwbsS = cwbsS.substring(0, cwbsS.length() - 1);
			b2CDataDAO.updateSendB2cByIds(b2cids);
			gomeService_CommitDeliverInfo.sendByCwbs(cwbsS, 0);
		}

		return clrv(model, cwbsSql);
	}

}
