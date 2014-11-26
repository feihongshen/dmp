package cn.explink.service;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.domain.Branch;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StringUtil;

@Service
public class BranchService {

	public Branch loadFormForBranch(HttpServletRequest request, MultipartFile file, List<String> functionids) {
		Branch bh = loadFormForBranch(request);
		if (file != null && !file.isEmpty()) {
			String filePath = ResourceBundleUtil.WAVPATH;
			String name = System.currentTimeMillis() + ".wav";
			ServiceUtil.uploadWavFile(file, filePath, name);
			bh.setBranchwavfile(name);
		}
		if (functionids != null) {
			StringBuffer str = new StringBuffer();
			for (String function : functionids) {
				str.append(function).append(",");
			}
			String functions = str.substring(0, str.length() - 1);
			bh.setFunctionids(functions);
		}
		return bh;
	}

	public Branch loadFormForBranch(HttpServletRequest request, MultipartFile file, String wavh, List<String> functionids) {
		Branch bh = loadFormForBranch(request);
		if (file != null && !file.isEmpty()) {
			String filePath = ResourceBundleUtil.WAVPATH;
			String name = System.currentTimeMillis() + ".wav";
			ServiceUtil.uploadWavFile(file, filePath, name);
			bh.setBranchwavfile(name);
		} else {
			String name = wavh;
			bh.setBranchwavfile(name);
		}
		if (functionids != null) {
			StringBuffer str = new StringBuffer();
			for (String function : functionids) {
				str.append(function).append(",");
			}
			String functions = str.substring(0, str.length() - 1);
			bh.setFunctionids(functions);
		}

		return bh;
	}

	public Branch loadFormForBranch(HttpServletRequest request, MultipartFile file) {
		Branch bh = loadFormForBranch(request);
		if (file != null && !file.isEmpty()) {
			String filePath = ResourceBundleUtil.WAVPATH;
			String name = System.currentTimeMillis() + ".wav";
			ServiceUtil.uploadWavFile(file, filePath, name);
			bh.setBranchwavfile(name);
		}

		return bh;
	}

	public Branch loadFormForBranch(HttpServletRequest request) {
		Branch branch = new Branch();
		branch.setBranchname(StringUtil.nullConvertToEmptyString(request.getParameter("branchname")));
		branch.setBranchprovince(StringUtil.nullConvertToEmptyString(request.getParameter("branchprovince")));
		branch.setBranchcity(StringUtil.nullConvertToEmptyString(request.getParameter("branchcity")));
		branch.setBrancharea(StringUtil.nullConvertToEmptyString(request.getParameter("brancharea")));
		branch.setBranchstreet(StringUtil.nullConvertToEmptyString(request.getParameter("branchstreet")));
		branch.setBranchaddress(StringUtil.nullConvertToEmptyString(request.getParameter("branchaddress")));
		branch.setBranchcontactman(StringUtil.nullConvertToEmptyString(request.getParameter("branchcontactman")));
		branch.setBranchphone(StringUtil.nullConvertToEmptyString(request.getParameter("branchphone")));
		branch.setBranchmobile(StringUtil.nullConvertToEmptyString(request.getParameter("branchmobile")));
		branch.setBranchfax(StringUtil.nullConvertToEmptyString(request.getParameter("branchfax")));
		branch.setBranchemail(StringUtil.nullConvertToEmptyString(request.getParameter("branchemail")));
		branch.setContractflag(StringUtil.nullConvertToEmptyString(request.getParameter("contractflag")));
		branch.setBankcard(StringUtil.nullConvertToEmptyString(request.getParameter("bankcard")));
		branch.setCwbtobranchid(StringUtil.nullConvertToEmptyString(request.getParameter("cwbtobranchid")));
		branch.setPayfeeupdateflag(StringUtil.nullConvertToEmptyString(request.getParameter("payfeeupdateflag")));
		branch.setBacktodeliverflag(StringUtil.nullConvertToEmptyString(request.getParameter("backtodeliverflag")));
		branch.setBranchpaytoheadflag(StringUtil.nullConvertToEmptyString(request.getParameter("branchpaytoheadflag")));
		branch.setBranchfinishdayflag(StringUtil.nullConvertToEmptyString(request.getParameter("branchfinishdayflag")));
		branch.setBranchinsurefee(BigDecimal.valueOf(Double.parseDouble(request.getParameter("branchinsurefee") == null ? "0" : request.getParameter("branchinsurefee"))));
		branch.setBranchwavfile(StringUtil.nullConvertToEmptyString(request.getParameter("branchwavfile")));
		branch.setCreditamount(BigDecimal.valueOf(Float.parseFloat(request.getParameter("creditamount") == null ? "0" : request.getParameter("creditamount"))));
		branch.setBrancheffectflag(StringUtil.nullConvertToEmptyString(request.getParameter("brancheffectflag")));
		branch.setContractrate(BigDecimal.valueOf(Float.parseFloat(request.getParameter("contractrate") == null ? "0" : request.getParameter("contractrate"))));
		branch.setBranchcode(StringUtil.nullConvertToEmptyString(request.getParameter("branchcode")));
		branch.setNoemailimportflag(StringUtil.nullConvertToEmptyString(request.getParameter("noemailimportflag")));
		branch.setErrorcwbdeliverflag(StringUtil.nullConvertToEmptyString(request.getParameter("errorcwbdeliverflag")));
		branch.setErrorcwbbranchflag(StringUtil.nullConvertToEmptyString(request.getParameter("errorcwbbranchflag")));
		branch.setBranchcodewavfile(StringUtil.nullConvertToEmptyString(request.getParameter("branchcodewavfile")));
		branch.setImportwavtype(StringUtil.nullConvertToEmptyString(request.getParameter("importwavtype")));
		branch.setExportwavtype(StringUtil.nullConvertToEmptyString(request.getParameter("exportwavtype")));
		branch.setNoemaildeliverflag(StringUtil.nullConvertToEmptyString(request.getParameter("noemaildeliverflag")));
		branch.setSendstartbranchid(Integer.parseInt(request.getParameter("sendstartbranchid") == null ? "0" : request.getParameter("sendstartbranchid")));
		branch.setFunctionids(StringUtil.nullConvertToEmptyString(request.getParameter("functionids")));
		branch.setSitetype(Integer.parseInt(request.getParameter("sitetype") == null ? "0" : request.getParameter("sitetype")));
		branch.setCheckremandtype(Integer.parseInt(request.getParameter("remandtype") == null ? "0" : request.getParameter("remandtype")));
		branch.setBranchmatter(StringUtil.nullConvertToEmptyString(request.getParameter("branchmatter")));
		// branch.setAccountareaid(Integer.parseInt((request.getParameter("accountarea")==null||"".equals(request.getParameter("accountarea")))?"0":request.getParameter("accountarea")));
		branch.setBranchid(Integer.parseInt(request.getParameter("branchid") == null ? "0" : request.getParameter("branchid")));

		branch.setZhongzhuanid(Integer.parseInt(request.getParameter("zhongzhuanid") == null ? "0" : request.getParameter("zhongzhuanid")));
		branch.setTuihuoid(Integer.parseInt(request.getParameter("tuihuoid") == null ? "0" : request.getParameter("tuihuoid")));
		branch.setCaiwuid(Integer.parseInt(request.getParameter("caiwuid") == null ? "0" : request.getParameter("caiwuid")));
		branch.setBindmsksid(Integer.parseInt(request.getParameter("bindmsksid") == null ? "0" : request.getParameter("bindmsksid")));
		// 结算业务设置
		branch.setAccounttype(Integer.parseInt(request.getParameter("accounttype") == null ? "0" : request.getParameter("accounttype")));
		branch.setAccountexcesstype(Integer.parseInt(request.getParameter("accountexcesstype") == null ? "0" : request.getParameter("accountexcesstype")));
		if (request.getParameter("accountexcessfee") == null || request.getParameter("accountexcessfee").toString().equals("")) {
			branch.setAccountexcessfee(BigDecimal.valueOf(Float.parseFloat("0")));
		} else {
			branch.setAccountexcessfee(BigDecimal.valueOf(Float.parseFloat(request.getParameter("accountexcessfee"))));
		}

		branch.setAccountbranch(Long.parseLong(request.getParameter("accountbranch") == null ? "0" : request.getParameter("accountbranch")));

		if (request.getParameter("credit") == null || request.getParameter("credit").toString().equals("")) {
			branch.setCredit(BigDecimal.valueOf(Float.parseFloat("0")));
		} else {
			branch.setCredit(BigDecimal.valueOf(Float.parseFloat(request.getParameter("credit"))));
		}

		if (request.getParameter("prescription24") == null || request.getParameter("prescription24").toString().equals("")) {
			branch.setPrescription24(Long.parseLong("0"));
		} else {
			branch.setPrescription24(Long.parseLong(request.getParameter("prescription24")));
		}

		if (request.getParameter("prescription48") == null || request.getParameter("prescription48").toString().equals("")) {
			branch.setPrescription48(Long.parseLong("0"));
		} else {
			branch.setPrescription48(Long.parseLong(request.getParameter("prescription48")));
		}

		branch.setBacktime(Long.parseLong(request.getParameter("backtime") == null ? "0" : request.getParameter("backtime")));

		return branch;
	}

	@Produce(uri = "jms:topic:addzhandian")
	ProducerTemplate addzhandian;
	@Produce(uri = "jms:topic:savezhandian")
	ProducerTemplate savezhandian;

	public void addzhandianToAddress(long branchid, Branch branch) {
		try {
			addzhandian.sendBodyAndHeader(null, "branchid", branchid);
			JSONObject branchToJson = new JSONObject();
			branchToJson.put("branchid", branchid);
			branchToJson.put("branchname", branch.getBranchname());
			branchToJson.put("branchphone", branch.getBranchphone());
			branchToJson.put("branchmobile", branch.getBranchmobile());
			branchToJson.put("branchcontactman", branch.getBranchcontactman());
			branchToJson.put("caiwuid", branch.getCaiwuid());
			branchToJson.put("contractflag", branch.getContractflag());
			branchToJson.put("bankcard", branch.getBankcard());
			branchToJson.put("branchcity", branch.getBranchcity());
			branchToJson.put("branchprovince", branch.getBranchprovince());
			branchToJson.put("brancharea", branch.getBrancharea());

			savezhandian.sendBodyAndHeader(null, "branch", branchToJson.toString());
		} catch (Exception e) {
		}
	}

	/*
	 * @Produce(uri="jms:topic:delzhandian") ProducerTemplate delzhandian;
	 * public void delBranch(long branchid){ try {
	 * delzhandian.sendBodyAndHeader(null, "branchid", branchid); } catch
	 * (Exception e) {} }
	 */

}
