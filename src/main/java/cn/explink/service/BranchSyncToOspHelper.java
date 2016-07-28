package cn.explink.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchSyncResultVo;
import cn.explink.domain.SystemInstall;
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
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;

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

	private final static byte OSP_ORG_TYPE_CARRIER_RDC = 4; // 承运商分拨中心
	private final static byte OSP_ORG_TYPE_CARRIER_SITE = 5; // 承运商站点
	private final static byte OSP_ORG_TYPE_ADMINISTRATION = 6; // 行政机构

	private String provinceName;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void saveBranchSyncToOsp(Branch branch) throws Exception {
		String carrierCode = getContractCarrierCode();
		String carrierSiteCode = branch.getTpsbranchcode();
		List<SbOrgModel> models = findOrgByCarrierAndSiteCode(carrierCode,
				carrierSiteCode);
		SbOrgResponse result = null;
		if (models != null && models.size() > 0) {
			result = updateBranchSyncToOsp(branch);
		} else {
			result = addBranchSyncToOsp(branch);
		}
		if (result != null
				&& !result.getResultCode().equals(RETURN_CODE_SUCCESS)) {
			throw new Exception(result.getResultMsg());
		}
	}

	private List<SbOrgModel> findOrgByCarrierAndSiteCode(String carrierCode,
			String carrierSiteCode) throws Exception {
		SbOrgService sbOrgService = getSbOrgService();
		logger.info("查询机构服务 - 请求：carrierCode={}，carrierSiteCode={}",
				new Object[] { carrierCode, carrierSiteCode });
		List<SbOrgModel> models = sbOrgService
				.findSbOrgByCarrierAndSelfStation(carrierCode, carrierSiteCode);
		logger.info("查询机构服务 - 返回：" + JsonUtil.translateToJson(models));
		return models;
	}

	private SbOrgResponse addBranchSyncToOsp(Branch branch) throws Exception {
		SbOrgService sbOrgService = getSbOrgService();
		SbOrgModel model = getSbOrgModelFromBranch(branch);
		logger.info("同步到机构服务 - 请求：" + JsonUtil.translateToJson(model));
		SbOrgResponse result = sbOrgService.addSbOrg(model);
		logger.info("同步到机构服务 - 返回：" + JsonUtil.translateToJson(result));
		return result;
	}

	private SbOrgResponse updateBranchSyncToOsp(Branch branch) throws Exception {
		SbOrgService sbOrgService = getSbOrgService();
		SbOrgModel model = getSbOrgModelFromBranch(branch);

		logger.info("同步到机构服务 - 请求：" + JsonUtil.translateToJson(model));
		SbOrgResponse result = sbOrgService
				.updateSbOrgByCarrierAndSiteCode(model);
		logger.info("同步到机构服务 - 返回：" + JsonUtil.translateToJson(result));
		return result;
	}

	private SbOrgResponse deleteBranchSyncToOsp(Branch branch) throws Exception {
		SbOrgService sbOrgService = getSbOrgService();
		SbOrgRequest model = new SbOrgRequest();
		model.setOrgSource(OSP_ORG_SOURCE_DMP);
		model.setCarrierCode(getContractCarrierCode());
		model.setCarrierSiteCode(branch.getTpsbranchcode());

		model.setUpdatedByUser(CurrentUserHelper.getInstance().getUserName());
		model.setUpdatedDtmLoc((new Date()).getTime());

		logger.info("同步到机构服务 - 请求：" + JsonUtil.translateToJson(model));
		SbOrgResponse result = sbOrgService
				.deleteSbOrgByCarrierAndSiteCode(model);
		logger.info("同步到机构服务 - 返回：" + JsonUtil.translateToJson(result));
		return result;
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
		model.setEmail(getEmail(branch));
		model.setProvinceName(getProvinceName());
		model.setCityName(branch.getBranchcity());
		model.setRegionName(branch.getBrancharea());
		model.setIsCarriers(IS_CARRIERS_DEFAULT);
		model.setIsActive(getOspIsActiveByBranch(branch));
		model.setLesseeCode(OSP_LESSEE_CODE_PJBEST);
		model.setCreatedByUser(branch.getBranchcontactman());

		model.setUpdatedByUser(CurrentUserHelper.getInstance().getUserName());
		model.setUpdatedDtmLoc((new Date()).getTime());
		return model;
	}

	private String getEmail(Branch branch) {
		String email = "";
		int siteType = branch.getSitetype();
		if (siteType == BranchEnum.ZhanDian.getValue()) {
			email = branch.getBranchemail();
		} else if (siteType == BranchEnum.KeFu.getValue()
				|| siteType == BranchEnum.YunYing.getValue()
				|| siteType == BranchEnum.CaiWu.getValue()) {
			email = branch.getBranchmatter();
		}
		return email;
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
			if (brancheffectflag.equals("1")) {
				isActive = 1;
			} else if (brancheffectflag.equals("0")) {
				isActive = 0;
			}
		}
		return isActive;
	}

	public List<BranchSyncResultVo> batchSyncBranchOsp(List<Branch> branchs)
			throws Exception {
		List<BranchSyncResultVo> batchSyncResultVos = new ArrayList<BranchSyncResultVo>();
		for (Branch branch : branchs) {
			String carrierCode = getContractCarrierCode();
			String carrierSiteCode = branch.getTpsbranchcode();
			BranchSyncResultVo resultVo = new BranchSyncResultVo();
			resultVo.setBranchName(branch.getBranchname());
			resultVo.setCarrierCode(carrierCode);
			resultVo.setCarrierSiteCode(carrierSiteCode);

			List<SbOrgModel> models = findOrgByCarrierAndSiteCode(carrierCode,
					carrierSiteCode);
			if (models != null && models.size() > 0) {
				// osp中已经存在的机构不需要同步
				resultVo.setResult("机构已存在，不需要同步");
			} else {
				// osp中未存在此机构，新增
				try {
					SbOrgResponse result = addBranchSyncToOsp(branch);
					if (result.getResultCode().equals(RETURN_CODE_SUCCESS)) {
						// 同步新增成功
						resultVo.setResult("机构未存在，同步成功");
					} else {
						// 同步新增失败
						resultVo.setResult("机构未存在，同步但不成功");
						resultVo.setMessage(result.getResultMsg());
					}
				} catch (Exception e) {
					resultVo.setResult("机构未存在，同步但不成功");
					resultVo.setMessage(e.getMessage());
				}
				
			}
			batchSyncResultVos.add(resultVo);
		}
		return batchSyncResultVos;
	}
}
