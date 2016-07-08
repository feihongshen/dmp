package cn.explink.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.CurrentUserHelper;
import cn.explink.util.JsonUtil;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.StringUtil;

import com.pjbest.pjorganization.bizservice.service.SbOrgModel;
import com.pjbest.pjorganization.bizservice.service.SbOrgRequest;
import com.pjbest.pjorganization.bizservice.service.SbOrgResponse;
import com.pjbest.pjorganization.bizservice.service.SbOrgService;
import com.pjbest.pjorganization.bizservice.service.SbOrgServiceHelper;

@Service
public class BranchSyncToOspHelper {

	@Autowired
	private SystemInstallDAO systemInstallDAO;

	@Autowired
	private ProvinceDAO provinceDAO;

	private final static String SYSTEM_INSTALL_CARRIERCODE = "CARRIERCODE";

	private final static String OSP_ORG_SOURCE_DMP = "DMP";
	private final static String OSP_LESSEE_CODE_PJBEST = "001";
	private final static byte IS_CARRIERS_DEFAULT = 1;
	
	private final static String RETURN_CODE_SUCCESS = "1";
	
	private final static byte OSP_ORG_TYPE_CARRIER_RDC = 4;		//承运商分拨中心
	private final static byte OSP_ORG_TYPE_CARRIER_SITE = 5;	//承运商站点
	private final static byte OSP_ORG_TYPE_ADMINISTRATION = 6;			//行政机构	

	private String provinceName;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void saveBranchSyncToOsp(Branch branch) throws Exception {
		SbOrgService sbOrgService = getSbOrgService();
		String carrierCode = getContractCarrierCode();
		String carrierSiteCode = branch.getTpsbranchcode();
		
		SbOrgModel model = sbOrgService.findOrgByCarrierAndSiteCode(
				carrierCode, carrierSiteCode);
		if (model != null) {
			updateBranchSyncToOsp(branch);
		} else {
			addBranchSyncToOsp(branch);
		}
	}

	private SbOrgResponse addBranchSyncToOsp(Branch branch) throws Exception {
		SbOrgService sbOrgService = getSbOrgService();
		SbOrgModel model = getSbOrgModelFromBranch(branch);
		logger.info("同步到机构服务：" + JsonUtil.translateToJson(model));
		SbOrgResponse result = sbOrgService.addSbOrg(model);
		if (result.getResultCode().equals(RETURN_CODE_SUCCESS)) {
			return result;
		} else {
			throw new Exception(result.getResultMsg());
		}
	}

	private SbOrgResponse updateBranchSyncToOsp(Branch branch) throws Exception {
		SbOrgService sbOrgService = getSbOrgService();
		SbOrgModel model = getSbOrgModelFromBranch(branch);
		
		logger.info("同步到机构服务：" + JsonUtil.translateToJson(model));
		SbOrgResponse result = sbOrgService
				.updateSbOrgByCarrierAndSiteCode(model);
		if (result.getResultCode().equals(RETURN_CODE_SUCCESS)) {
			return result;
		} else {
			throw new Exception(result.getResultMsg());
		}
	}

	private SbOrgResponse deleteBranchSyncToOsp(Branch branch) throws Exception {
		SbOrgService sbOrgService = getSbOrgService();
		SbOrgRequest model = new SbOrgRequest();
		model.setOrgSource(OSP_ORG_SOURCE_DMP);
		model.setCarrierCode(getContractCarrierCode());
		model.setCarrierSiteCode(branch.getTpsbranchcode());

		model.setUpdatedByUser(CurrentUserHelper.getInstance().getUserName());
		model.setUpdatedDtmLoc((new Date()).getTime());

		logger.info("同步到机构服务：" + JsonUtil.translateToJson(model));
		SbOrgResponse result = sbOrgService
				.deleteSbOrgByCarrierAndSiteCode(model);
		if (result.getResultCode().equals(RETURN_CODE_SUCCESS)) {
			return result;
		} else {
			throw new Exception(result.getResultMsg());
		}
	}

	private SbOrgService getSbOrgService() {
		SbOrgService sbOrgService = new SbOrgServiceHelper.SbOrgServiceClient();
		return sbOrgService;
	}

	private SbOrgModel getSbOrgModelFromBranch(Branch branch) {
		SbOrgModel model = new SbOrgModel();
		model.setOrgSource(OSP_ORG_SOURCE_DMP);
		model.setCarrierCode(getContractCarrierCode());
		model.setSuperSiteCode(model.getCarrierCode());
		model.setCarrierSiteCode(branch.getTpsbranchcode());
		model.setOrgName(branch.getBranchname());
		model.setOrgType(getOspOrgTypeByBranch(branch));
		model.setPickingCode(branch.getBranchcode());
		model.setContactor(branch.getBranchcontactman());
		model.setTelephone(branch.getBranchphone());
		model.setAddress(branch.getBranchaddress());
		model.setFax(branch.getBranchfax());
		model.setEmail(branch.getBranchemail());
		model.setProvinceName(getProvinceName());
		model.setIsCarriers(IS_CARRIERS_DEFAULT);
		model.setIsActive(getOspIsActiveByBranch(branch));
		model.setLesseeCode(OSP_LESSEE_CODE_PJBEST);
		model.setCreatedByUser(branch.getBranchcontactman());		

		model.setUpdatedByUser(CurrentUserHelper.getInstance().getUserName());
		model.setUpdatedDtmLoc((new Date()).getTime());
		return model;
	}

	private String getProvinceName() {
		if (StringUtil.isEmpty(provinceName)) {
			AdressVO province = provinceDAO
					.getProvinceByCode(ResourceBundleUtil.provinceCode);
			provinceName = (province == null ? "" : province.getName());
		}
		return provinceName;
	}

	private String getContractCarrierCode() {
		String carrierCode = "";
		SystemInstall carriorCodeSystemInstall = systemInstallDAO
				.getSystemInstall(SYSTEM_INSTALL_CARRIERCODE);
		carrierCode = (carriorCodeSystemInstall == null ? ""
				: carriorCodeSystemInstall.getValue());
		return carrierCode;
	}

	private byte getOspOrgTypeByBranch(Branch branch) {
		byte orgType = 0;
		if (branch != null) {
			int siteType = branch.getSitetype();
			if (siteType == BranchEnum.KuFang.getValue()
					|| siteType == BranchEnum.TuiHuo.getValue()
					|| siteType == BranchEnum.ZhongZhuan.getValue()) {
				// dmp的库房、退货站、中转站，对应机构服务的“4-承运商分拨中心”
				orgType = OSP_ORG_TYPE_CARRIER_RDC;
			} else if (siteType == BranchEnum.ZhanDian.getValue()) {
				// dmp的站点，对应机构服务的“5-承运商站点”
				orgType = OSP_ORG_TYPE_CARRIER_SITE;
			} else if (siteType == BranchEnum.KeFu.getValue()
					|| siteType == BranchEnum.YunYing.getValue()
					|| siteType == BranchEnum.CaiWu.getValue()
					|| siteType == BranchEnum.QiTa.getValue()) {
				// dmp的客服部、运营部、结算部、其它，对应机构服务的“6-行政机构”
				orgType = OSP_ORG_TYPE_ADMINISTRATION;
			}
		}
		return orgType;
	}

	private byte getOspIsActiveByBranch(Branch branch) {
		byte isActive = 1;
		if (branch != null) {
			String brancheffectflag = branch.getBrancheffectflag();
			if (brancheffectflag.equals("0")) {
				isActive = 1;
			} else if (brancheffectflag.equals("1")) {
				isActive = 0;
			}
		}
		return isActive;
	}
}
