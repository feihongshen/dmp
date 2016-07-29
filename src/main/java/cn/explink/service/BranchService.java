package cn.explink.service;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.core.utils.StringUtils;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.OrgPayInTypeEnum;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StringUtil;

@Service
public class BranchService {

	@Autowired
	private BranchDAO branchDao;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public Branch loadFormForBranch(HttpServletRequest request, MultipartFile file, List<String> functionids) {
		Branch bh = this.loadFormForBranch(request);
		if ((file != null) && !file.isEmpty()) {
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
		Branch bh = this.loadFormForBranch(request);
		if ((file != null) && !file.isEmpty()) {
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
		Branch bh = this.loadFormForBranch(request);
		if ((file != null) && !file.isEmpty()) {
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
		branch.setBranchinsurefee(BigDecimal.valueOf(Double.parseDouble(isNullOrUndefined(request.getParameter("branchinsurefee")) ? "0" : request.getParameter("branchinsurefee"))));
		branch.setBranchwavfile(StringUtil.nullConvertToEmptyString(request.getParameter("branchwavfile")));
		branch.setCreditamount(BigDecimal.valueOf(Double.parseDouble(isNullOrUndefined(request.getParameter("creditamount")) ? "0" : request.getParameter("creditamount"))));
		branch.setBrancheffectflag(StringUtil.nullConvertToEmptyString(request.getParameter("brancheffectflag")));
		branch.setContractrate(BigDecimal.valueOf(Double.parseDouble(isNullOrUndefined(request.getParameter("contractrate")) ? "0" : request.getParameter("contractrate"))));
		branch.setBranchcode(StringUtil.nullConvertToEmptyString(request.getParameter("branchcode")));
		//为快递使用的tps站点编码
		branch.setTpsbranchcode(StringUtil.nullConvertToEmptyString(isNullOrUndefined(request.getParameter("tpsbranchcode")) ? null : request.getParameter("tpsbranchcode").trim()));
		branch.setNoemailimportflag(StringUtil.nullConvertToEmptyString(request.getParameter("noemailimportflag")));
		branch.setErrorcwbdeliverflag(StringUtil.nullConvertToEmptyString(request.getParameter("errorcwbdeliverflag")));
		branch.setErrorcwbbranchflag(StringUtil.nullConvertToEmptyString(request.getParameter("errorcwbbranchflag")));
		branch.setBranchcodewavfile(StringUtil.nullConvertToEmptyString(request.getParameter("branchcodewavfile")));
		branch.setImportwavtype(StringUtil.nullConvertToEmptyString(request.getParameter("importwavtype")));
		branch.setExportwavtype(StringUtil.nullConvertToEmptyString(request.getParameter("exportwavtype")));
		branch.setNoemaildeliverflag(StringUtil.nullConvertToEmptyString(request.getParameter("noemaildeliverflag")));
		branch.setSendstartbranchid(Integer.parseInt(isNullOrUndefined(request.getParameter("sendstartbranchid")) ? "0" : request.getParameter("sendstartbranchid")));
		branch.setFunctionids(StringUtil.nullConvertToEmptyString(request.getParameter("functionids")));
		branch.setSitetype(Integer.parseInt(isNullOrUndefined(request.getParameter("sitetype")) ? "0" : request.getParameter("sitetype")));
		branch.setCheckremandtype(Integer.parseInt(isNullOrUndefined(request.getParameter("remandtype")) ? "0" : request.getParameter("remandtype")));
		branch.setBranchmatter(StringUtil.nullConvertToEmptyString(request.getParameter("branchmatter")));
		// branch.setAccountareaid(Integer.parseInt((request.getParameter("accountarea")==null||"".equals(request.getParameter("accountarea")))?"0":request.getParameter("accountarea")));
		branch.setBranchid(Integer.parseInt(isNullOrUndefined(request.getParameter("branchid")) ? "0" : request.getParameter("branchid")));

		branch.setZhongzhuanid(Integer.parseInt(isNullOrUndefined(request.getParameter("zhongzhuanid")) ? "0" : request.getParameter("zhongzhuanid")));
		branch.setTuihuoid(Integer.parseInt(isNullOrUndefined(request.getParameter("tuihuoid")) ? "0" : request.getParameter("tuihuoid")));
		branch.setCaiwuid(Integer.parseInt(isNullOrUndefined(request.getParameter("caiwuid")) ? "0" : request.getParameter("caiwuid")));
		branch.setBindmsksid(Integer.parseInt(isNullOrUndefined(request.getParameter("bindmsksid")) ? "0" : request.getParameter("bindmsksid")));
		// 结算业务设置
		branch.setAccounttype(Integer.parseInt(isNullOrUndefined(request.getParameter("accounttype")) ? "0" : request.getParameter("accounttype")));
		branch.setAccountexcesstype(Integer.parseInt(isNullOrUndefined(request.getParameter("accountexcesstype")) ? "0" : request.getParameter("accountexcesstype")));
		branch.setPfruleid(Long.parseLong(isNullOrUndefined(request.getParameter("pfruleid")) ? "0" : request.getParameter("pfruleid")));
		
		
		String payMethodType = isNullOrUndefined(request.getParameter("payMethodType")) ? "" : request.getParameter("payMethodType");
		// 0为通联，1为财付通
		if ("0".equals(payMethodType)){
			//保存通联
			branch.setBankCardNo(isNullOrUndefined(request.getParameter("bankCardNo")) ? "" : request.getParameter("bankCardNo"));
			branch.setBankCode(isNullOrUndefined(request.getParameter("bankCode")) ? "" : request.getParameter("bankCode"));
			branch.setOwnerName(isNullOrUndefined(request.getParameter("ownerName")) ? "" : request.getParameter("ownerName"));
			branch.setBankAccountType(isNullOrUndefined(request.getParameter("bankAccountType")) ? 1 : Integer.parseInt(request.getParameter("bankAccountType")));
			//清空财付通
			branch.setCftAccountNo("");
			branch.setCftBankCode("");
			branch.setCftAccountName("");
			branch.setCftAccountProp(2);
			branch.setCftCertId("");
			branch.setCftCertType(1);
		} else if ("1".equals(payMethodType)){
			//清空通联
			branch.setBankCardNo("");
			branch.setBankCode("");
			branch.setOwnerName("");
			branch.setBankAccountType(1);
			//保存财付通
			branch.setCftAccountNo(isNullOrUndefined(request.getParameter("cftAccountNo")) ? "" : request.getParameter("cftAccountNo"));
			branch.setCftBankCode(isNullOrUndefined(request.getParameter("cftBankCode")) ? "" : request.getParameter("cftBankCode"));
			branch.setCftAccountName(isNullOrUndefined(request.getParameter("cftAccountName")) ? "" : request.getParameter("cftAccountName"));
			branch.setCftAccountProp(isNullOrUndefined(request.getParameter("cftAccountProp")) ? 2 : Integer.parseInt(request.getParameter("cftAccountProp")));
			branch.setCftCertId(isNullOrUndefined(request.getParameter("cftCertId")) ? "" : request.getParameter("cftCertId"));
			branch.setCftCertType(isNullOrUndefined(request.getParameter("cftCertType")) ? 1 : Integer.parseInt(request.getParameter("cftCertType")));
		}
		

		//自动分拣线的滑槽口号
		branch.setOutputno(isNullOrUndefined(request.getParameter("outputno")) ? null : request.getParameter("outputno"));

		if ((isNullOrUndefined(request.getParameter("accountexcessfee"))) || request.getParameter("accountexcessfee").toString().equals("")) {
			branch.setAccountexcessfee(BigDecimal.valueOf(Double.parseDouble("0")));
		} else {
			branch.setAccountexcessfee(BigDecimal.valueOf(Double.parseDouble(request.getParameter("accountexcessfee"))));
		}

		branch.setAccountbranch(Long.parseLong(isNullOrUndefined(request.getParameter("accountbranch")) ? "0" : request.getParameter("accountbranch")));

		if ((isNullOrUndefined(request.getParameter("credit"))) || request.getParameter("credit").toString().equals("")) {
			branch.setCredit(BigDecimal.valueOf(Double.parseDouble("0")));
		} else {
			branch.setCredit(BigDecimal.valueOf(Double.parseDouble(request.getParameter("credit"))));
		}

		if ((isNullOrUndefined(request.getParameter("prescription24"))) || request.getParameter("prescription24").toString().equals("")) {
			branch.setPrescription24(Long.parseLong("0"));
		} else {
			branch.setPrescription24(Long.parseLong(request.getParameter("prescription24")));
		}

		if ((isNullOrUndefined(request.getParameter("prescription48"))) || request.getParameter("prescription48").toString().equals("")) {
			branch.setPrescription48(Long.parseLong("0"));
		} else {
			branch.setPrescription48(Long.parseLong(request.getParameter("prescription48")));
		}

		branch.setBacktime(Long.parseLong(isNullOrUndefined(request.getParameter("backtime")) ? "0" : request.getParameter("backtime")));

		//站点保证金
		if (StringUtils.isEmpty(request.getParameter("branchBail"))) {
			branch.setBranchBail(BigDecimal.ZERO);
		} else {
			String branchBail = request.getParameter("branchBail");
			if ((branchBail != null) && !branchBail.equals("null")) {
				try {
					branch.setBranchBail(new BigDecimal(branchBail));
				} catch (NumberFormatException e) {
					branch.setBranchBail(new BigDecimal("0"));
				}
			}
		}
		//站点缴款方式
		branch.setPayinType(Integer.parseInt(StringUtils.isEmpty(request.getParameter("payinType")) ? OrgPayInTypeEnum.StationPay.getValue()+"" : request.getParameter("payinType")));
		return branch;
	}
	
	private boolean isNullOrUndefined(String parameter) {
		return parameter == null || parameter.equals("undefined");
	}

	@Produce(uri = "jms:topic:addzhandian")
	ProducerTemplate addzhandian;
	@Produce(uri = "jms:topic:savezhandian")
	ProducerTemplate savezhandian;

	public void addzhandianToAddress(long branchid, Branch branch,String oldtpsbranchcode) {
		JSONObject branchToJson = new JSONObject();
		try {
			this.logger.info("消息发送端：addzhandian, branchid={}", branchid);
			this.addzhandian.sendBodyAndHeader(null, "branchid", branchid);
			
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
			branchToJson.put("tpsbranchcode", branch.getTpsbranchcode());
			branchToJson.put("oldtpsbranchcode", oldtpsbranchcode==null?"":oldtpsbranchcode);
			
			this.logger.info("消息发送端：savezhandian, branch={}", branchToJson.toString());
			this.savezhandian.sendBodyAndHeader(null, "branch", branchToJson.toString());
		} catch (Exception e) {
			logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".addzhandianToAddress")
					.buildExceptionInfo(e.toString()).buildTopic(this.addzhandian.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("branchid", branchid + "").getMqException());
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".addzhandianToAddress")
					.buildExceptionInfo(e.toString()).buildTopic(this.savezhandian.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("branch", branchToJson.toString()).getMqException());
		}
	}

	@Produce(uri = "jms:topic:delzhandian")
	ProducerTemplate delzhandian;

	public void delBranch(long branchid) {
		try {
			this.logger.info("消息发送端：delzhandian, branchid={}", branchid);
			this.delzhandian.sendBodyAndHeader(null, "branchid", branchid);
		} catch (Exception e) {
			logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".delBranch")
					.buildExceptionInfo(e.toString()).buildTopic(this.delzhandian.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("branchid", branchid + "").getMqException());
		}
	}
	
	public Branch getBranchByBranchid(long branchid) {
		Branch branch = this.branchDao.getBranchByBranchid(branchid);
		if (branch == null || branch.getBranchid() == 0) {
			return null;
		}
		return branch;
	}
	
	public List<Branch> getBranchByTpsBranchcode(String tpsbranchcode) {
		return this.branchDao.getBranchByTpsBranchcode(tpsbranchcode);
	}

	/**
	 * 获取页面数据渲染缓存
	 * @return
	 */
	public List<Branch> getPageCash() {

		return this.branchDao.getAllEffectBranches();
	}
	public List<Branch> getBranchs() {

		return this.branchDao.getAllBranches();
	}
	
    public List<Branch> getPageCashs() {
		return this.branchDao.getAllBranches();
	}
    
    public Branch getBranchByBranchcode(String branchcode) {
    	List<Branch> branchList = this.branchDao.getBranchByBranchcode(branchcode);
    	if(branchList == null || branchList.size() == 0) {
    		return null;
    	}
		return branchList.get(0);
    }
    
    /**
     * 获取站点
     * 2016年6月21日 下午4:37:05
     * @param branchId
     * @return
     */
    public Branch getZhanDianByBranchId(long branchid) {
    	Branch branch = this.getBranchByBranchid(branchid);
    	if(branch != null && branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
    		return branch;
    	}
    	return null;
    }
    
    public Branch getBranchByBranchname(String branchname) {
    	Branch branch = this.branchDao.getBranchByBranchname(branchname);
    	if(branch != null && branch.getBranchid() == 0) {
    		return null;
    	}
    	return branch;
    }
}
