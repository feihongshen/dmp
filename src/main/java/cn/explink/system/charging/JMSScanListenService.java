package cn.explink.system.charging;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.camel.CamelContext;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.MD5.MD5Util;

@Service("jMSScanListenService")
public class JMSScanListenService {
	private Logger logger = LoggerFactory.getLogger(JMSScanListenService.class);

	@Autowired
	private CamelContext camelContext;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// @PostConstruct
	// public void init() {
	// try {
	// camelContext.addRoutes(new RouteBuilder() {
	// @Override
	// public void configure() throws Exception {
	// from("jms:queue:VirtualTopicConsumers.scanListen.orderFlow?concurrentConsumers="
	// +
	// 5).to("bean:jMSScanListenService?method=scanListenService").routeId("scanListen");
	// }
	// });
	//
	// } catch (Exception e) {
	// logger.error("SCAN LISTEN SERVICE ERROR! ");
	// e.printStackTrace();
	// }
	// }
	/**
	 * 按单计费统计服务
	 * 
	 * @param parm
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private ObjectMapper objectMapper = JacksonMapper.getInstance();

	// public void scanListenService(@Header("orderFlow")String parm) throws
	// IOException{
	// OrderFlow orderFlow=objectMapper.readValue(parm, OrderFlow.class);
	// if(orderFlow.getFlowordertype()!=FlowOrderTypeEnum.DaoRuShuJu.getValue()
	// &&orderFlow.getFlowordertype()!=FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()
	// &&orderFlow.getFlowordertype()!=FlowOrderTypeEnum.BeiZhu.getValue()){
	// int scanCwbIsExist =
	// jdbcTemplate.queryForInt("select count(1) from express_sys_scan where cwb=?",orderFlow.getCwb());
	// if(scanCwbIsExist==0){//如果不存在这个订单号
	// logger.info("cwb:"+orderFlow.getCwb()+",flowordertype:"+orderFlow.getFlowordertype());
	// jdbcTemplate.update("insert into express_sys_scan (cwb,flowordertype) values (?,?)",orderFlow.getCwb(),orderFlow.getFlowordertype());
	// deductFrom();
	// }
	// }
	// }

	private final class On_OffRowMapper implements RowMapper<On_Off> {
		@Override
		public On_Off mapRow(ResultSet rs, int rowNum) throws SQLException {
			On_Off on_off = new On_Off();
			on_off.setId(rs.getLong("id"));
			on_off.setNumber(rs.getLong("number"));
			on_off.setOn_or_off(rs.getString("on_off"));
			on_off.setType(rs.getString("type"));
			on_off.setMac(rs.getString("mac"));
			return on_off;
		}
	}

	private synchronized void deductFrom() {
		String type = "SYSTEM_ON_OFF";// 开关类型
		On_Off onoff = jdbcTemplate.queryForObject("select * from express_sys_on_off where type=?", new On_OffRowMapper(), type);

		String mac_old_value = onoff.getNumber() + onoff.getOn_or_off() + onoff.getType();

		if (!onoff.compareMac(onoff.getMac(), mac_old_value)) {
			jdbcTemplate.update("update express_sys_on_off set number=0,on_off='off',mac=? ", MD5Util.md5("0off" + onoff.getType() + onoff.mac_key));
			logger.error("按单计费数据被篡改，造成余额自动清零修复");
			// }else if(onoff.getNumber()<=1){ 暂时不开启按单计费
			// jdbcTemplate.update("update express_sys_on_off set number=?,on_off=?,mac=? ",onoff.getNumber()-1,"off",MD5Util.md5((onoff.getNumber()-1)+"off"+onoff.getType()+onoff.mac_key));
			// logger.error("余额不足，关闭系统");
		} else {
			jdbcTemplate.update("update express_sys_on_off set number=?,mac=? ", onoff.getNumber() - 1, MD5Util.md5((onoff.getNumber() - 1) + onoff.getOn_or_off() + onoff.getType() + onoff.mac_key));
		}

	}

	// public static void main(String[] args) {
	// System.out.println(MD5Util.md5("0onSYSTEM_ON_OFF"+On_Off.mac_key));
	// System.out.println(MD5Util.md5("100000onSYSTEM_ON_OFF"+On_Off.mac_key));
	// System.out.println(MD5Util.md5("5onSYSTEM_ON_OFF"+On_Off.mac_key));
	// }
}
