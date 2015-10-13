package cn.explink.b2c.explink.core_up;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.bind.JAXBException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.code_down.EpaiExpEmum;
import cn.explink.b2c.explink.xmldto.CoreMarchal;
import cn.explink.b2c.explink.xmldto.OrderExportCallbackDto;
import cn.explink.b2c.explink.xmldto.ReturnDto;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.Common;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 系统之间的对接（上游）
 * 
 * @author Administrator
 *
 */
@Service
public class EpaiCoreService_Callback {
	private Logger logger = LoggerFactory.getLogger(EpaiCoreService_Callback.class);

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	WarehouseCommenDAO warehouseCommenDAO;
	@Autowired
	GetDmpDAO getDmpDAO;

	/**
	 * 接收请求订单导出接口
	 * 
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public String requestOrdersExportCallBack(OrderExportCallbackDto orderExportCallbackDto) throws JAXBException, UnsupportedEncodingException {
		ReturnDto returnDto = new ReturnDto();

		try {

			String userCode = orderExportCallbackDto.getUserCode();
			String requestTime = orderExportCallbackDto.getRequestTime();
			String sign = orderExportCallbackDto.getSign();
			String cwbarrs = orderExportCallbackDto.getCwbs();

			try {
				ValidatorRequestParams(userCode, requestTime, sign, returnDto);
			} catch (RuntimeException e) {
				logger.error("验证下游请求业务逻辑异常", e);

				return CoreMarchal.Marchal_rep_ExportCallBack(returnDto);// 通过jaxb状态对象对XML传输
																			// 回传至下游订单详细信息
			}

			String cwbstrs = "";
			for (String cwb : cwbarrs.split(",")) {
				cwbstrs += "'" + cwb + "',";
			}

			warehouseCommenDAO.updateCommenCwbListBycwbs(cwbstrs.length() > 0 ? cwbstrs.substring(0, cwbstrs.length() - 1) : "-1", DateTimeUtil.getNowTime());

			returnDto.setErrCode(EpaiExpEmum.Success.getErrCode());
			returnDto.setErrMsg(EpaiExpEmum.Success.getErrMsg());

			return CoreMarchal.Marchal_rep_ExportCallBack(returnDto);// 通过jaxb状态对象对XML传输
																		// 回传至下游订单详细信息

		} catch (Exception e) {

			returnDto.setErrCode(EpaiExpEmum.XiTongYiChang.getErrCode());
			returnDto.setErrMsg(EpaiExpEmum.XiTongYiChang.getErrMsg());
			logger.error("上游处理下载订单程序未知异常", e);
			return CoreMarchal.Marchal_rep_ExportCallBack(returnDto);// 通过jaxb状态对象对XML传输
																		// 回传至下游订单详细信息

		}

	}

	private String getRequestDMPCwbArrs(List<WarehouseToCommen> datalist) {
		JSONArray jsonArr = new JSONArray();
		for (WarehouseToCommen common : datalist) {
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("cwb", common.getCwb());
			jsonArr.add(jsonobj);
		}
		String requestCwbs = jsonArr.toString();
		return requestCwbs;
	}

	private void ValidatorRequestParams(String userCode, String requestTime, String sign, ReturnDto returnDto) throws RuntimeException {
		if (userCode == null || userCode.isEmpty()) {

			returnDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
			returnDto.setErrMsg("请求参数userCode不能为空");
			throw new RuntimeException("请求参数userCode不能为空");
		}
		if (requestTime == null || requestTime.isEmpty()) {
			returnDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
			returnDto.setErrMsg("请求参数requestTime不能为空");
			throw new RuntimeException("请求参数requestTime不能为空");
		}
		if (sign == null || sign.isEmpty()) {
			returnDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
			returnDto.setErrMsg("请求参数sign不能为空");
			throw new RuntimeException("请求参数sign不能为空");
		}

		Common common = getDMPCommon(userCode);
		if (common == null) {
			returnDto.setErrCode(EpaiExpEmum.YongHuBuCunZai.getErrCode());
			returnDto.setErrMsg("用户" + userCode + "不存在");
			throw new RuntimeException("用户" + userCode + "不存在");
		}

		String localsign = MD5Util.md5(userCode + requestTime + common.getPrivate_key());
		if (!localsign.equalsIgnoreCase(sign)) {
			returnDto.setErrCode(EpaiExpEmum.QianMingCuoWu.getErrCode());
			returnDto.setErrMsg("签名验证异常");
			throw new RuntimeException("签名验证异常");
		}
	}

	private Common getDMPCommon(String userCode) {
		for (Common common : this.getAllCommons()) {
			if (common.getCommonnumber().equals(userCode)) {
				return common;
			}
		}
		return null;
	}

	/**
	 * 获取dmp承运商设置表
	 * 
	 * @return
	 */
	public List<Common> getAllCommons() {
		return getDmpdao.getAllCommons();
	}

	/**
	 * 回传订单下载接口
	 * 
	 * @return
	 */
	public static String responseXML() {

		return null;
	}

}
