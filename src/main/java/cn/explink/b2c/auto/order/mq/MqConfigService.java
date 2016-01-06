package cn.explink.b2c.auto.order.mq;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.auto.order.vo.MqConfigVo;
import cn.explink.controller.CwbOrderDTO;

@Transactional
@Service
public class MqConfigService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String MQ_CONFIG_SQL="select text from express_auto_param_config where name=?";
	
	private static final String MQ_CONFIG_SAVE_SQL="insert into express_auto_param_config (name,text,remark) values(?,?,?)";
	private static final String MQ_CONFIG_UPDATE_SQL="update express_auto_param_config set text=?,remark=? where name=?";
	private static final String MQ_CONFIG_QUERY_ALL_SQL="select * from express_auto_param_config";
	private static final String MQ_CONFIG_QUERY_BY_NAME_SQL="select * from express_auto_param_config where name=?";
	private static final String MQ_CONFIG_DELETE_SQL="delete from express_auto_param_config where name=?";
	
	public void initMqConfig(List<ConsumerTemplate> callBackList, List<AutoExceptionSender> senderList){
		for(ConsumerTemplate callBack:callBackList){
			//channel
			String channel=getValue(callBack.getExchangeKey());
			if(channel==null||channel.trim().length()<1){
				throw new RuntimeException("MQ parameter "+callBack.getExchangeKey()+"is not found");
			}else{
				callBack.setExchangeName(channel);
			}
			
			//queue
			String queue=getValue(callBack.getQueueKey());
			if(queue==null||queue.trim().length()<1){
				throw new RuntimeException("MQ parameter "+callBack.getQueueKey()+"is not found");
			}else{
				callBack.setQueueName(queue);
			}
		}
		
		for(AutoExceptionSender sender:senderList){
			//channel
			String channel=getValue(sender.getChannelKey());
			if(channel==null||channel.trim().length()<1){
				throw new RuntimeException("MQ parameter "+sender.getChannelKey()+"is not found");
			}else{
				sender.setChannel(channel);
			}
		}
	}
	
	public String getValue(String name){
		String value=jdbcTemplate.queryForObject(MQ_CONFIG_SQL,new Object[]{name}, String.class);
		logger.info("mq parameter name={},value={}",name,value);
		return value;
	}
	
	public int save(MqConfigVo vo){
		trimData(vo);
		if(vo.getName()==null||vo.getName().length()<1){
			return 0;
		}
		int cnt=jdbcTemplate.update(MQ_CONFIG_SAVE_SQL, vo.getName(),vo.getText(),vo.getRemark());
		return cnt;
	}
	
	public int update(MqConfigVo vo){
		trimData(vo);
		int cnt= jdbcTemplate.update(MQ_CONFIG_UPDATE_SQL,vo.getText(),vo.getRemark(),vo.getName());
		return cnt;
	}
	
	public int saveOrUpdate(MqConfigVo vo){
		trimData(vo);
		MqConfigVo data=this.queryByName(vo.getName());
		int cnt=0;
		if(data==null){
			cnt=this.save(vo);
		}else{
			cnt= this.update(vo);
		}
		
		return cnt;
	
	}
	
	public int delete(MqConfigVo vo){
		int cnt= jdbcTemplate.update(MQ_CONFIG_DELETE_SQL,vo.getName());
		return cnt;
	}
	
	public MqConfigVo queryByName(String name){
		List<MqConfigVo> list=jdbcTemplate.query(MQ_CONFIG_QUERY_BY_NAME_SQL, new MqConfigVoMapper(),name);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
		
	}
	
	private void trimData(MqConfigVo vo){
		vo.setName(vo.getName()==null?null:vo.getName().trim());
		vo.setText(vo.getText()==null?null:vo.getText().trim());
		vo.setRemark(vo.getRemark()==null?null:vo.getRemark().trim());
	}
	
	public List<MqConfigVo> querAll(){
		List<MqConfigVo> list=jdbcTemplate.query(MQ_CONFIG_QUERY_ALL_SQL, new MqConfigVoMapper());
		return list;
	}
	
	private final class MqConfigVoMapper implements RowMapper<MqConfigVo> {

		@Override
		public MqConfigVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			MqConfigVo vo=new MqConfigVo();
			vo.setName(rs.getString("name"));
			vo.setText(rs.getString("text"));
			vo.setRemark(rs.getString("remark"));
			return vo;
		}
		
	}
}
