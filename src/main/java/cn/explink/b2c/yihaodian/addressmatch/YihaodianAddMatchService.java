package cn.explink.b2c.yihaodian.addressmatch;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.b2c.yihaodian.RestTemplateClient;
import cn.explink.b2c.yihaodian.YihaodianInsertCwbDetailTimmer;
import cn.explink.b2c.yihaodian.Yihaodian_Master;
import cn.explink.b2c.yihaodian.addressmatch.dto.DepotParse;
import cn.explink.b2c.yihaodian.addressmatch.dto.DepotParseDetail;
import cn.explink.b2c.yihaodian.addressmatch.dto.DepotParseDetailList;
import cn.explink.b2c.yihaodian.addressmatch.dto.DepotParseResult;
import cn.explink.b2c.yihaodian.addressmatch.dto.DepotParseResultDetail;
import cn.explink.b2c.yihaodian.addressmatch.dto.DepotParseResultList;
import cn.explink.b2c.yihaodian.addressmatch.dto.YhdAddmatchUnmarchal;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Branch;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.util.MD5.MD5Util;

@Service
public class YihaodianAddMatchService {
	private Logger logger = LoggerFactory.getLogger(YihaodianAddMatchService.class);
	@Autowired
	RestTemplateClient restTemplate;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	Yihaodian_Master yihaodian_Master;
	@Autowired
	YihaodianInsertCwbDetailTimmer yihaodianTimmer;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;

	@Autowired
	AddressMatchService addressMatchService;
	@Autowired
	BranchDAO branchDAO;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public YihaodianAddMatch getYihaodian(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		YihaodianAddMatch Yihaodian = (YihaodianAddMatch) JSONObject.toBean(jsonObj, YihaodianAddMatch.class);
		return Yihaodian;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		String maxCount = request.getParameter("maxCount").isEmpty() ? "0" : request.getParameter("maxCount");

		YihaodianAddMatch yihaodian = new YihaodianAddMatch();
		yihaodian.setUserCode(request.getParameter("userCode"));
		yihaodian.setPrivate_key(request.getParameter("private_key"));
		yihaodian.setMaxCount(Long.valueOf(maxCount));
		yihaodian.setReceiver_url(request.getParameter("receiver_url"));

		JSONObject jsonObj = JSONObject.fromObject(yihaodian);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {

			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}

	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public int getStateForYihaodian(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	/**
	 * 接收处理一号店站点匹配的方法
	 * 
	 * @param yhd_key
	 * @return
	 * @throws Exception
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public String invokeYihaodianAddressmatch(String requestXml) {

		try {

			YihaodianAddMatch yihaodian = getYihaodian(B2cEnum.yhdAddmatch.getKey());

			DepotParse depotParse = (DepotParse) YhdAddmatchUnmarchal.Unmarchal(requestXml); // 解析XML为bean对象

			String userCode = depotParse.getUserCode();
			String sign = depotParse.getSign();
			String requestTime = depotParse.getRequestTime();
			DepotParseDetailList depotParseDetailList = depotParse.getDepotParseDetailList();

			try {
				validatorParams(yihaodian, userCode, sign, requestTime, depotParseDetailList); // 验证是否合法
			} catch (YihaodianAddMatchException e) {

				logger.error("一号店站点匹配-请求验证异常," + e.getContent(), e);
				return getReponsefailed(e.getError().getErrCode(), e.getContent());

			}

			List<DepotParseDetail> addresslist = depotParseDetailList.getDepotParseDetailList();

			// 构建返回信息的bean
			DepotParseResult depotParseResult = new DepotParseResult();
			DepotParseResultList depotParseResultList = new DepotParseResultList();
			depotParseResult.setErrCode(YihaodianAddEmum.Success.getErrCode());
			depotParseResult.setErrMsg(YihaodianAddEmum.Success.getErrMsg());
			List<DepotParseResultDetail> depotParseResultDetails = new ArrayList<DepotParseResultDetail>();

			for (DepotParseDetail depotParseDetail : addresslist) {
				// 详细地址=省+市+区/县
				String consigneeaddress = depotParseDetail.getProvince() + depotParseDetail.getCity() + depotParseDetail.getDistrict() + depotParseDetail.getConsigneeAddress();
				if (consigneeaddress == null || consigneeaddress.isEmpty()) {
					continue;
				}

				JSONObject addressJson = addressMatchService.matchAddressByPublicInterface(depotParseDetail.getDoCode(), consigneeaddress);

				DepotParseResultDetail depotParseResultDetail = getDepotParseResultDetail(depotParseDetail, addressJson, yihaodian); // 构建每个订单号的返回结果

				if (depotParseResultDetail == null) {
					continue;
				}
				depotParseResultDetails.add(depotParseResultDetail);

			}

			if (depotParseResultDetails != null) { // 当有单子匹配，加入对象
													// depotParseResultList
				depotParseResultList.setDepotParseResultDetail(depotParseResultDetails);
				depotParseResult.setDepotParseResultList(depotParseResultList);
			}

			String reponseXml = YhdAddmatchUnmarchal.Marchal(depotParseResult);

			logger.info("返回一号店站点匹配结果XML={}", reponseXml);

			return reponseXml;

		} catch (Exception e) {
			logger.error("一号店站点匹配-系统未知异常", e);
			return getReponsefailed(YihaodianAddEmum.XiTongYiChang.getErrCode(), e.getMessage());
		}

	}

	/**
	 * 返回失败信息的公共类
	 * 
	 * @param errorCode
	 * @return
	 * @throws Exception
	 */
	public String getReponsefailed(String errorCode, String errorMsg) {
		try {
			DepotParseResult depotParseResult = new DepotParseResult();
			depotParseResult.setErrCode(errorCode);
			depotParseResult.setErrMsg(errorMsg);

			return YhdAddmatchUnmarchal.Marchal(depotParseResult);
		} catch (Exception e) {
			return "bean转化xml异常" + e.getMessage();
		}
	}

	private DepotParseResultDetail getDepotParseResultDetail(DepotParseDetail depotParseDetail, JSONObject addressJson, YihaodianAddMatch yihaodian) {
		// String id=addressJson.getString("id"); //请求唯一编码
		String station = addressJson.getString("station"); // 对应返回的站点ID
		if (station.isEmpty()) {
			return null;
		}

		Branch branch = branchDAO.getBranchByBranchid(Long.valueOf(station));

		DepotParseResultDetail depotParseResultDetail = new DepotParseResultDetail();
		depotParseResultDetail.setDoCode(depotParseDetail.getDoCode()); // 配送单号
		depotParseResultDetail.setDepotCode(branch.getBranchcode()); // 站点编码
		depotParseResultDetail.setDepotName(branch.getBranchname() == null ? "" : branch.getBranchname()); // 站点名称
		depotParseResultDetail.setPrintCode(branch.getBranchcode()); // 打印名称-和站点一致

		return depotParseResultDetail;

	}

	private void validatorParams(YihaodianAddMatch yihaodian, String userCode, String sign, String requestTime, DepotParseDetailList depotParseDetailList) {
		if (userCode == null || userCode.isEmpty()) {

			throw new YihaodianAddMatchException(YihaodianAddEmum.CanShuCuoWu, "userCode不能为空");
		}
		if (sign == null || sign.isEmpty()) {
			throw new YihaodianAddMatchException(YihaodianAddEmum.CanShuCuoWu, "sign不能为空");
		}
		if (requestTime == null || requestTime.isEmpty()) {
			throw new YihaodianAddMatchException(YihaodianAddEmum.CanShuCuoWu, "requestTime不能为空");
		}
		if (!userCode.equals(yihaodian.getUserCode())) {
			throw new YihaodianAddMatchException(YihaodianAddEmum.YongHuBuCunZai, "指定userCode=" + userCode + "不存在");
		}
		String local_sign = MD5Util.MD5Encode(userCode + requestTime + yihaodian.getPrivate_key(), "UTF-8");
		if (!sign.equalsIgnoreCase(local_sign)) {
			throw new YihaodianAddMatchException(YihaodianAddEmum.QianMingCuoWu, "签名验证异常");
		}

		if (depotParseDetailList == null) {
			throw new YihaodianAddMatchException(YihaodianAddEmum.CanShuCuoWu, "depotParseDetailList节点不能为空");
		}
		List<DepotParseDetail> addresslist = depotParseDetailList.getDepotParseDetailList();
		if (addresslist == null || addresslist.size() == 0) {
			throw new YihaodianAddMatchException(YihaodianAddEmum.CanShuCuoWu, "DepotParseDetail节点不能为空");
		}

		long maxCount = addresslist.size();

		if (maxCount > yihaodian.getMaxCount()) {
			throw new YihaodianAddMatchException(YihaodianAddEmum.YeWuYiChang, "请求节点数不能超过" + yihaodian.getMaxCount() + "个");
		}
	}

}
