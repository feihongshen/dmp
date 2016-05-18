package cn.explink.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.BranchContractDAO;
import cn.explink.dao.BranchContractDetailDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.ExpressSetBranchContract;
import cn.explink.domain.ExpressSetBranchContractDetail;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.VO.ExpressSetBranchContractDetailVO;
import cn.explink.domain.VO.ExpressSetBranchContractVO;
import cn.explink.util.BeanUtilsSelfDef;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StringUtil;

@Service
public class BranchContractService {

	@Autowired
	BranchContractDAO branchContractDAO;
	@Autowired
	BranchContractDetailDAO branchContractDetailDAO;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public ExpressSetBranchContractVO getBranchContractVO(int id) {
		// 返回加盟商合同主表VO的list
		List<ExpressSetBranchContractVO> rtnVOList = new ArrayList<ExpressSetBranchContractVO>();
		// 返回加盟商合同主表VO
		ExpressSetBranchContractVO rtnVO = null;
		// 返回加盟商合同子表VO的list
		List<ExpressSetBranchContractDetailVO> rtnDetailVOList = null;
		// 返回加盟商合同子表VO
		ExpressSetBranchContractDetailVO rtnDetailVO = null;
		// 加盟商合同主表list
		List<ExpressSetBranchContract> branchContractList = this.branchContractDAO
				.getBranchContractListById(id);
		if (branchContractList != null && !branchContractList.isEmpty()) {
			ExpressSetBranchContract branchContract = null;
			List<ExpressSetBranchContractDetail> branchContractDetailList = null;
			ExpressSetBranchContractDetail branchContractDetail = null;
			for (int i = 0; i < branchContractList.size(); i++) {
				branchContract = branchContractList.get(i);
				if (branchContract != null) {
					rtnVO = new ExpressSetBranchContractVO();
					// 将主表实体数据复制到主表VO
					BeanUtilsSelfDef.copyPropertiesIgnoreException(rtnVO,
							branchContract);
					branchContractDetailList = this.branchContractDetailDAO
							.getBranchContractDetailListByBranchId(branchContract
									.getId());
					if (branchContractDetailList != null
							&& !branchContractDetailList.isEmpty()) {
						rtnDetailVOList = new ArrayList<ExpressSetBranchContractDetailVO>();
						for (int j = 0; j < branchContractDetailList.size(); j++) {
							branchContractDetail = branchContractDetailList
									.get(j);
							if (branchContractDetail != null) {
								rtnDetailVO = new ExpressSetBranchContractDetailVO();
								BeanUtilsSelfDef.copyPropertiesIgnoreException(
										rtnDetailVO, branchContractDetail);
								rtnDetailVOList.add(rtnDetailVO);
							}
						}
						// 子表数据VO的list作为一个属性添加到主表VO，方便前台获取
						rtnVO.setBranchContractDetailVOList(rtnDetailVOList);
					}
					rtnVOList.add(rtnVO);
				}
			}
		}

		if (rtnVOList != null && !rtnVOList.isEmpty()
				&& rtnVOList.get(0) != null) {
			return rtnVOList.get(0);
		} else {
			return null;
		}
	}

	@Transactional
	public void updateBranchContractVO(
			ExpressSetBranchContractVO branchContractVO) {
		ExpressSetBranchContract branchContract = new ExpressSetBranchContract();
		if (branchContractVO != null) {
			BeanUtilsSelfDef.copyPropertiesIgnoreException(branchContract,
					branchContractVO);
			this.branchContractDAO.updateBranchContract(branchContract);
			String idStr = "'" + branchContract.getId() + "'";
			// 修改主表信息时，先删除子表所有相关信息
			this.branchContractDetailDAO.deleteBranchContractDetailByBranchId(idStr);
			
			String branchContractDetailVOStr = branchContractVO.getBranchContractDetailVOStr();
			JSONArray jsonArray = JSONArray.fromObject(branchContractDetailVOStr);
			List<ExpressSetBranchContractDetail> branchContractDetailList = (List<ExpressSetBranchContractDetail>)JSONArray.toCollection(jsonArray, ExpressSetBranchContractDetail.class);
			
			ExpressSetBranchContractDetail branchContractDetail = null;
			if (branchContractDetailList != null
					&& !branchContractDetailList.isEmpty()) {
				for (int i = 0; i < branchContractDetailList.size(); i++) {
					branchContractDetail = branchContractDetailList.get(i);
					if (branchContractDetail != null) {
						branchContractDetail.setCreator(branchContractVO.getModifyPerson());
						branchContractDetail.setCreateTime(branchContractVO.getModifyTime());
						branchContractDetail.setModifyPerson(branchContractVO.getModifyPerson());
						branchContractDetail.setModifyTime(branchContractVO.getModifyTime());
						// 然后根据前台返回数据，逐一生成子表信息
						this.branchContractDetailDAO
								.createBranchContractDetail(branchContractDetail);
					}
				}
			}
		}
	}

	@Transactional
	public void deleteBranchContract(String ids) {
		ids = StringUtil.getStringsByStringList(Arrays.asList(ids.split(",")));
		this.branchContractDetailDAO.deleteBranchContractDetailByBranchId(ids);
		this.branchContractDAO.deleteBranchContract(ids);
	}

	public String generateContractNo(){
		// 系统默认生成“C_J”+8位年月日+三位流水
		String contractNo = "";
		List<ExpressSetBranchContract> contractList = this.branchContractDAO.getMaxContractNo();
		if(contractList != null && !contractList.isEmpty()){
			for(int i = 0; i < contractList.size(); i++){
				ExpressSetBranchContract contract = contractList.get(i);
				String maxContractNo = contract.getContractNo();
				if(maxContractNo.length() == 14 && "C_J".equals(maxContractNo.substring(0, 3))){
					String partContractNo = maxContractNo.substring(0, 11);
					String maxOrderStr = maxContractNo.substring(11);
					int maxOrderInt = Integer.valueOf(maxOrderStr);
					maxOrderInt++;
					String orderStr = String.valueOf(maxOrderInt);
					while(orderStr.length() < 3){
						orderStr = "0" + orderStr;
					}
					contractNo = partContractNo + orderStr;
					break;
				}
			}
		}
		if(StringUtils.isBlank(contractNo)){
			String rule = "C_J";
			String date = DateTimeUtil.getCurrentDate();
			String orderStr = "001";
			contractNo = rule + date + orderStr;
		}
		return contractNo;
	}
	
	public Branch loadFormForBranch(HttpServletRequest request,
			MultipartFile file, List<String> functionids) {
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

	public Branch loadFormForBranch(HttpServletRequest request,
			MultipartFile file, String wavh, List<String> functionids) {
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

	public Branch loadFormForBranch(HttpServletRequest request,
			MultipartFile file) {
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
		branch.setBranchname(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchname")));
		branch.setBranchprovince(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchprovince")));
		branch.setBranchcity(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchcity")));
		branch.setBrancharea(StringUtil.nullConvertToEmptyString(request
				.getParameter("brancharea")));
		branch.setBranchstreet(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchstreet")));
		branch.setBranchaddress(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchaddress")));
		branch.setBranchcontactman(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchcontactman")));
		branch.setBranchphone(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchphone")));
		branch.setBranchmobile(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchmobile")));
		branch.setBranchfax(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchfax")));
		branch.setBranchemail(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchemail")));
		branch.setContractflag(StringUtil.nullConvertToEmptyString(request
				.getParameter("contractflag")));
		branch.setBankcard(StringUtil.nullConvertToEmptyString(request
				.getParameter("bankcard")));
		branch.setCwbtobranchid(StringUtil.nullConvertToEmptyString(request
				.getParameter("cwbtobranchid")));
		branch.setPayfeeupdateflag(StringUtil.nullConvertToEmptyString(request
				.getParameter("payfeeupdateflag")));
		branch.setBacktodeliverflag(StringUtil.nullConvertToEmptyString(request
				.getParameter("backtodeliverflag")));
		branch.setBranchpaytoheadflag(StringUtil
				.nullConvertToEmptyString(request
						.getParameter("branchpaytoheadflag")));
		branch.setBranchfinishdayflag(StringUtil
				.nullConvertToEmptyString(request
						.getParameter("branchfinishdayflag")));
		branch.setBranchinsurefee(BigDecimal.valueOf(Double.parseDouble(request
				.getParameter("branchinsurefee") == null ? "0" : request
				.getParameter("branchinsurefee"))));
		branch.setBranchwavfile(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchwavfile")));
		branch.setCreditamount(BigDecimal.valueOf(Float.parseFloat(request
				.getParameter("creditamount") == null ? "0" : request
				.getParameter("creditamount"))));
		branch.setBrancheffectflag(StringUtil.nullConvertToEmptyString(request
				.getParameter("brancheffectflag")));
		branch.setContractrate(BigDecimal.valueOf(Float.parseFloat(request
				.getParameter("contractrate") == null ? "0" : request
				.getParameter("contractrate"))));
		branch.setBranchcode(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchcode")));
		branch.setNoemailimportflag(StringUtil.nullConvertToEmptyString(request
				.getParameter("noemailimportflag")));
		branch.setErrorcwbdeliverflag(StringUtil
				.nullConvertToEmptyString(request
						.getParameter("errorcwbdeliverflag")));
		branch.setErrorcwbbranchflag(StringUtil
				.nullConvertToEmptyString(request
						.getParameter("errorcwbbranchflag")));
		branch.setBranchcodewavfile(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchcodewavfile")));
		branch.setImportwavtype(StringUtil.nullConvertToEmptyString(request
				.getParameter("importwavtype")));
		branch.setExportwavtype(StringUtil.nullConvertToEmptyString(request
				.getParameter("exportwavtype")));
		branch.setNoemaildeliverflag(StringUtil
				.nullConvertToEmptyString(request
						.getParameter("noemaildeliverflag")));
		branch.setSendstartbranchid(Integer.parseInt(request
				.getParameter("sendstartbranchid") == null ? "0" : request
				.getParameter("sendstartbranchid")));
		branch.setFunctionids(StringUtil.nullConvertToEmptyString(request
				.getParameter("functionids")));
		branch.setSitetype(Integer
				.parseInt(request.getParameter("sitetype") == null ? "0"
						: request.getParameter("sitetype")));
		branch.setCheckremandtype(Integer.parseInt(request
				.getParameter("remandtype") == null ? "0" : request
				.getParameter("remandtype")));
		branch.setBranchmatter(StringUtil.nullConvertToEmptyString(request
				.getParameter("branchmatter")));
		// branch.setAccountareaid(Integer.parseInt((request.getParameter("accountarea")==null||"".equals(request.getParameter("accountarea")))?"0":request.getParameter("accountarea")));
		branch.setBranchid(Integer
				.parseInt(request.getParameter("branchid") == null ? "0"
						: request.getParameter("branchid")));

		branch.setZhongzhuanid(Integer.parseInt(request
				.getParameter("zhongzhuanid") == null ? "0" : request
				.getParameter("zhongzhuanid")));
		branch.setTuihuoid(Integer
				.parseInt(request.getParameter("tuihuoid") == null ? "0"
						: request.getParameter("tuihuoid")));
		branch.setCaiwuid(Integer
				.parseInt(request.getParameter("caiwuid") == null ? "0"
						: request.getParameter("caiwuid")));
		branch.setBindmsksid(Integer.parseInt(request
				.getParameter("bindmsksid") == null ? "0" : request
				.getParameter("bindmsksid")));
		// 结算业务设置
		branch.setAccounttype(Integer.parseInt(request
				.getParameter("accounttype") == null ? "0" : request
				.getParameter("accounttype")));
		branch.setAccountexcesstype(Integer.parseInt(request
				.getParameter("accountexcesstype") == null ? "0" : request
				.getParameter("accountexcesstype")));
		if ((request.getParameter("accountexcessfee") == null)
				|| request.getParameter("accountexcessfee").toString()
						.equals("")) {
			branch.setAccountexcessfee(BigDecimal.valueOf(Float.parseFloat("0")));
		} else {
			branch.setAccountexcessfee(BigDecimal.valueOf(Float
					.parseFloat(request.getParameter("accountexcessfee"))));
		}

		branch.setAccountbranch(Long.parseLong(request
				.getParameter("accountbranch") == null ? "0" : request
				.getParameter("accountbranch")));

		if ((request.getParameter("credit") == null)
				|| request.getParameter("credit").toString().equals("")) {
			branch.setCredit(BigDecimal.valueOf(Float.parseFloat("0")));
		} else {
			branch.setCredit(BigDecimal.valueOf(Float.parseFloat(request
					.getParameter("credit"))));
		}

		if ((request.getParameter("prescription24") == null)
				|| request.getParameter("prescription24").toString().equals("")) {
			branch.setPrescription24(Long.parseLong("0"));
		} else {
			branch.setPrescription24(Long.parseLong(request
					.getParameter("prescription24")));
		}

		if ((request.getParameter("prescription48") == null)
				|| request.getParameter("prescription48").toString().equals("")) {
			branch.setPrescription48(Long.parseLong("0"));
		} else {
			branch.setPrescription48(Long.parseLong(request
					.getParameter("prescription48")));
		}

		branch.setBacktime(Long
				.parseLong(request.getParameter("backtime") == null ? "0"
						: request.getParameter("backtime")));

		return branch;
	}

	@Produce(uri = "jms:topic:addzhandian")
	ProducerTemplate addzhandian;
	@Produce(uri = "jms:topic:savezhandian")
	ProducerTemplate savezhandian;
	
	public void addzhandianToAddress(long branchid, Branch branch) {
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

			this.logger.info("消息发送端：savezhandian, branch={}", branchToJson.toString());
			this.savezhandian.sendBodyAndHeader(null, "branch",
					branchToJson.toString());
		} catch (Exception e) {
			logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".addzhandianToAddress")
					.buildExceptionInfo(e.toString()).buildTopic(this.addzhandian.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("branchid", branchid + "").getMqException());
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".addzhandianToAddress")
					.buildExceptionInfo(e.toString()).buildTopic(this.savezhandian.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("branch", branchToJson.toString()).getMqException());
		}
	}
	
	
	public String loadexceptfile(MultipartFile file) {
		String name = "";
		try {
			if ((file != null) && !file.isEmpty()) {
				String filePath = ResourceBundleUtil.FILEPATH;
				name = file.getOriginalFilename();
				/*if (name.indexOf(".") != -1) {
					String suffix = name.substring(name.lastIndexOf("."));
					name =System.currentTimeMillis() + suffix;
				} else {
					name = System.currentTimeMillis() + "";
				}*/
				ServiceUtil.uploadWavFile(file, filePath, name);
			}
		} catch (Exception e) {
			// this.logger.error("问题件添加到指定路径下出现错误");
		}
		return name;
	}

}
