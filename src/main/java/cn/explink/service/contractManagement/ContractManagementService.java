/**
 *
 */
package cn.explink.service.contractManagement;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.contractManagement.CustomerContractDAO;
import cn.explink.domain.customerCoutract.CustomerContractManagement;
import cn.explink.domain.customerCoutract.DepositInformation;
import cn.explink.util.BeanUtilsSelfDef;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;

/**
 * @author wangqiang
 */
@Service
public class ContractManagementService {
	@Autowired
	private CustomerContractDAO customerContractDAO;

	public List<CustomerContractManagement> getCustomerContractList(CustomerContractManagement contractManagement, String createStatrtTime, String createEndTime, String overStartTime,
			String overEndTime, String sort, String method) {
		return this.customerContractDAO.getCustomerContractList(contractManagement, createStatrtTime, createEndTime, overStartTime, overEndTime, sort, method);
	}

	public String loadexceptfile(MultipartFile file) {
		String name = "";
		try {
			if ((file != null) && !file.isEmpty()) {
				String filePath = ResourceBundleUtil.FILEPATH;
				name = file.getOriginalFilename();
				if (name.indexOf(".") != -1) {
					String suffix = name.substring(name.lastIndexOf("."));
					name = System.currentTimeMillis() + suffix;
				} else {
					name = System.currentTimeMillis() + "";
				}
				ServiceUtil.uploadWavFile(file, filePath, name);
			}
		} catch (Exception e) {
			// this.logger.error("问题件添加到指定路径下出现错误");
		}
		return name;
	}

	@Transactional
	public void updateContract(CustomerContractManagement contractManagement) {
		if (contractManagement != null) {
			this.customerContractDAO.modificationContract(contractManagement);
			this.customerContractDAO.deleteContractId(contractManagement.getId());

			String depositInformationStr = contractManagement.getDepositInformationStr();
			JSONArray jsonArray = JSONArray.fromObject(depositInformationStr);
			List<DepositInformation> depositInformationList = (List<DepositInformation>) JSONArray.toCollection(jsonArray, DepositInformation.class);

			DepositInformation depositInformation = null;
			if ((depositInformationList != null) && !depositInformationList.isEmpty()) {
				for (int i = 0; i < depositInformationList.size(); i++) {
					depositInformation = depositInformationList.get(i);
					if (depositInformation != null) {
						this.customerContractDAO.createContractDetail(depositInformation);
					}
				}
			}
		}
	}

	public CustomerContractManagement getBranchContractVO(long id) {
		// 返回值
		List<CustomerContractManagement> rtnVOList = new ArrayList<CustomerContractManagement>();
		CustomerContractManagement rtnEntity = null;
		List<DepositInformation> rtnDetailEntityList = null;
		DepositInformation rtnDetailEntity = null;

		List<CustomerContractManagement> contractManagementList = this.customerContractDAO.queryByid(id);
		if ((contractManagementList != null) && !contractManagementList.isEmpty()) {
			CustomerContractManagement branchContract = null;
			List<DepositInformation> branchContractDetailList = null;
			DepositInformation branchContractDetail = null;
			for (int i = 0; i < contractManagementList.size(); i++) {
				branchContract = contractManagementList.get(i);
				if (branchContract != null) {
					rtnEntity = new CustomerContractManagement();
					BeanUtilsSelfDef.copyPropertiesIgnoreException(rtnEntity, branchContract);
					branchContractDetailList = this.customerContractDAO.getContractDetailListByContractId(id);
					if ((branchContractDetailList != null) && !branchContractDetailList.isEmpty()) {
						rtnDetailEntityList = new ArrayList<DepositInformation>();
						for (int j = 0; j < branchContractDetailList.size(); j++) {
							branchContractDetail = branchContractDetailList.get(j);
							if (branchContractDetail != null) {
								rtnDetailEntity = new DepositInformation();
								BeanUtilsSelfDef.copyPropertiesIgnoreException(rtnDetailEntity, branchContractDetail);
								rtnDetailEntityList.add(rtnDetailEntity);
							}
						}
						rtnEntity.setDepositInformationList(rtnDetailEntityList);
					}
					rtnVOList.add(rtnEntity);
				}
			}
		}

		if ((rtnVOList != null) && !rtnVOList.isEmpty() && (rtnVOList.get(0) != null)) {
			return rtnVOList.get(0);
		} else {
			return null;
		}
	}

	// 自动生成编号
	public String getNumber() {
		String number = "";
		List<CustomerContractManagement> contractList = this.customerContractDAO.getMaxNumber();
		if ((contractList != null) && !contractList.isEmpty()) {
			for (int i = 0; i < contractList.size(); i++) {
				CustomerContractManagement contract = contractList.get(i);
				String maxNumber = contract.getNumber();
				if ((maxNumber.length() == 14) && "C_J".equals(maxNumber.substring(0, 3))) {
					String partContractNo = maxNumber.substring(0, 11);
					String maxOrderStr = maxNumber.substring(11);
					int maxOrderInt = Integer.valueOf(maxOrderStr);
					maxOrderInt++;
					number = partContractNo + maxOrderInt;
					break;
				}
			}
		}
		if (StringUtils.isBlank(number)) {
			String rule = "C_J";
			String date = DateTimeUtil.getNowDate();
			String orderStr = "001";
			number = rule + date + orderStr;
		}
		return number;
	}

}
