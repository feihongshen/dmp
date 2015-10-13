package cn.explink.b2c.tools.b2cmonitor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.domain.B2CData;

/**
 * OMS发送 Dmp的公共类
 * 
 * @author Administrator
 *
 */
@Service
public class B2cSendMointorService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	// @Produce(uri="jms:topic:b2cDataSendResultMonitor")
	// ProducerTemplate b2cDataResultSend;
	@Autowired
	B2CDataDAO b2cDataDAO;

	/**
	 * 发送DMP对接信息的方法
	 */
	public int SendB2cDataJMS(List<B2CMonitorData> b2cdatalist) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			// b2cDataResultSend.sendBodyAndHeader(null, "monitorb2cdata",
			// objectMapper.writeValueAsString(b2cdatalist));
			logger.info("OMS Send Dmp b2cData success");
			return 1;
		} catch (Exception e) {
			logger.error("OMS发送DMP监控JMS Send Method发生异常", e);
			return 0;
		}
	}

	public void BuildB2cMonitorArray() throws Exception {
		List<B2CMonitorData> bmonitorlist = new ArrayList<B2CMonitorData>();
		List<B2CData> b2cdatalist = b2cDataDAO.getDataListByB2cSendDMP(100);

		if (b2cdatalist == null || b2cdatalist.size() == 0) {
			return;
		} else {
			for (B2CData bd : b2cdatalist) {
				B2CMonitorData bdata = new B2CMonitorData();
				bdata.setCwb(bd.getCwb());
				bdata.setFlowordertype(bd.getFlowordertype());
				bdata.setSend_b2c_flag(bd.getSend_b2c_flag());
				bmonitorlist.add(bdata);

			}
			if (SendB2cDataJMS(bmonitorlist) == 1) {
				for (B2CData bd : b2cdatalist) {
					b2cDataDAO.updateSendDMPSuccessFlag(bd.getB2cid(), 1);
				}
			}
		}
		if (b2cdatalist != null && b2cdatalist.size() > 0) {
			BuildB2cMonitorArray();
		}

	}

	public void parseB2cMonitorData_timmer() {
		try {
				BuildB2cMonitorArray();
		} catch (Exception e) {
			logger.error("OMS查询DMP方法遇到不可预知的异常", e);
		}
	}

}
