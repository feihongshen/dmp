package cn.explink.b2c.explink.code_down;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.xmldto.CoreMarchal;
import cn.explink.b2c.explink.xmldto.CoreUnmarchal;
import cn.explink.b2c.explink.xmldto.OrderFlowDto;
import cn.explink.b2c.explink.xmldto.ReturnDto;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.util.RestHttpServiceHanlder;

/**
 * 系统之间的对接（下游）
 * 
 * @author Administrator
 *
 */
@Service
public class EpaiApiService {
	private Logger logger = LoggerFactory.getLogger(EpaiApiService.class);

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;

	/**
	 * 获取dmp项目的配置
	 * 
	 * @return
	 */
	public List<EpaiApi> getEpaiAPIList() {
		List<EpaiApi> epailist = getDmpdao.getDMPEpaiAPI();
		return epailist;
	}

	public String getEpaiFlowEnum(long flowordertype) {

		for (EPaiFlowEnum TEnum : EPaiFlowEnum.values()) {
			if (flowordertype == TEnum.getOwn_code()) {
				return TEnum.getRequest_code();
			}
		}

		return null;

	}

	/**
	 * 状态自动反馈入口
	 */
	public long feedback_status() {
		long calcCount = 0;
		int check = 0;
		if (getEpaiAPIList() == null || getEpaiAPIList().size() == 0) {
			logger.warn("易派状态反馈-未进行接口设置");
			return -1;
		}
		for (EpaiApi epai : getEpaiAPIList()) {
			if (epai.getIsfeedbackflag() == 1) {
				calcCount += excuteFeedback_status(epai);
				check++;
			} else {
				logger.info("下游未开启状态反馈接口userCode={}", epai.getUserCode());
			}
		}
		if (check == 0) {
			return -1;
		}
		return calcCount;
	}

	private long excuteFeedback_status(EpaiApi epai) {
		long calcCount = 0;
		try {
			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(String.valueOf(epai.getCustomerid()), epai.getPageSize());
				i++;
				if (i > 300) {
					logger.warn("查询0易派系统下游0状态反馈已经超过300次循环，可能存在程序未知异常,请及时查询并处理!");
					return 0;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送0易派系统下游0的数据");
					return 0;
				}

				for (B2CData data : datalist) {

					try {
						String jsoncontent = data.getJsoncontent();

						OrderFlowDto note = getEPaiXMLNote(jsoncontent);
						note.setCustid(data.getB2cid() + ""); // 订单状态不重复标识

						String content = CoreMarchal.Marchal_OrderFlow(note); // 通过jaxb
																				// 对象对XML传输

						logger.info("当前易派接口下游发送信息-cwb={},flowordertype={},request=" + content, data.getCwb(), data.getFlowordertype());

						String responseContent = "";
						if (epai.getIspostflag() == 0) { // 使用数据流获取
							responseContent = RestHttpServiceHanlder.sendHttptoServer(content, epai.getFeedback_url());
						} else {
							Map<String, String> params = new HashMap<String, String>();
							params.put("content", content);
							responseContent = RestHttpServiceHanlder.sendHttptoServer(params, epai.getFeedback_url());
						}

						logger.info("当前易派接口上游返回信息-cwb={},flowordertype={},返回信息=" + responseContent, data.getCwb(), data.getFlowordertype());

						ReturnDto returnDto = CoreUnmarchal.Unmarchal_ExportCallBack(responseContent); // xml转化为bean对象
						String returnMsg = returnDto.getErrMsg() != null && returnDto.getErrMsg().length() > 480 ? returnDto.getErrMsg().substring(0, 480) : returnDto.getErrMsg();

						b2cDataDAO.updateB2cIdSQLResponseStatus(data.getB2cid(), returnDto.getErrCode().equals(EpaiExpEmum.Success.getErrCode()) ? 1 : 2, returnMsg);

						// 发送给dmp
						flowFromJMSB2cService.sendTodmp(String.valueOf(data.getB2cid()));
					} catch (Exception e) {
						logger.error("下游状态回传发生未知异常cwb=" + data.getCwb(), e);
					}
				}
				calcCount += datalist.size();

			}

		} catch (Exception e) {
			logger.error("查询0易派系统下游0状态反馈执行回传发生未知异常", e);

		}
		return calcCount;
	}

	public OrderFlowDto getEPaiXMLNote(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, OrderFlowDto.class);
	}

}
