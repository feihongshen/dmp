package cn.explink.service;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.domain.Branch;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StringUtil;

@Service
public class BranchService {

	public Branch loadFormForBranch(HttpServletRequest request, MultipartFile file, String wavh) {
		Branch bh = loadFormForBranch(request);
		if (file != null && !file.isEmpty()) {
			String filePath = request.getSession().getServletContext().getRealPath("/");
			String name = System.currentTimeMillis() + ".wav";
			ServiceUtil.uploadWavFile(file, filePath, ServiceUtil.wavPath + name);
			bh.setBranchwavfile(name);
		} else {
			String name = wavh;
			bh.setBranchwavfile(name);
		}
		return bh;
	}

	public Branch loadFormForBranch(HttpServletRequest request, MultipartFile file) {
		Branch bh = loadFormForBranch(request);
		if (file != null && !file.isEmpty()) {
			String filePath = request.getSession().getServletContext().getRealPath("/");
			String name = System.currentTimeMillis() + ".wav";
			ServiceUtil.uploadWavFile(file, filePath, ServiceUtil.wavPath + name);
			bh.setBranchwavfile(name);
		}

		return bh;
	}

	public Branch loadFormForBranch(HttpServletRequest request) {
		Branch branch = new Branch();
		branch.setBranchname(StringUtil.nullConvertToEmptyString(request.getParameter("branchname")));
		branch.setBranchprovince(StringUtil.nullConvertToEmptyString(request.getParameter("branchprovince")));
		branch.setBranchcity(StringUtil.nullConvertToEmptyString(request.getParameter("branchcity")));
		branch.setBranchaddress(StringUtil.nullConvertToEmptyString(request.getParameter("branchaddress")));
		branch.setBranchcontactman(StringUtil.nullConvertToEmptyString(request.getParameter("branchcontactman")));
		branch.setBranchphone(StringUtil.nullConvertToEmptyString(request.getParameter("branchphone")));
		branch.setBranchmobile(StringUtil.nullConvertToEmptyString(request.getParameter("branchmobile")));
		branch.setBranchfax(StringUtil.nullConvertToEmptyString(request.getParameter("branchfax")));
		branch.setBranchemail(StringUtil.nullConvertToEmptyString(request.getParameter("branchemail")));
		branch.setContractflag(StringUtil.nullConvertToEmptyString(request.getParameter("contractflag")));
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
		return branch;
	}

}
