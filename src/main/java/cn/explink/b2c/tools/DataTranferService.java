package cn.explink.b2c.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;
import cn.explink.util.DateTimeUtil;

@Service
public class DataTranferService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public long lastFlowIdVar;
	public long firstFlowIdVar;
	public long endIdVar;
	public long startIdVar;
	public boolean stop=false;
	public Date startDate=null;
	public Date endDate=null;
	
	public void transferData(){
		SystemInstall systemInstall = this.systemInstallDAO.getSystemInstallByName("branchdaohuoqianyi");
		int isqianyi = 0;
		if (systemInstall != null) {
			try {
				isqianyi = Integer.parseInt(systemInstall.getValue());
			} catch (NumberFormatException e) {
				isqianyi = 0;
			}
		}
		this.logger.info("准备站点到货数据迁移.isqianyi="+isqianyi);
		if(isqianyi==0){
			this.logger.info("站点到货数据迁移开关没打开.");
			return;
		}
		if(isqianyi==2){
			this.logger.info("站点到货数据迁移任务运行中.");
			return;
		}
		
		lastFlowIdVar=0;
		firstFlowIdVar=0;
		endIdVar=0;
		startIdVar=0;
		stop=false;
		
		long startTime = System.currentTimeMillis();
		startDate=new Date();
		endDate=null;
		try{
			updateState("2");//防止重复执行
			doTransfer();
		} catch (Exception e) {
    		this.logger.error("站点到货数据迁移出错.",e);
    		updateState("4");
    	}finally{
    		endDate=new Date();
    		long usedTime = System.currentTimeMillis() - startTime;
            int seconds = (int) (usedTime / 1000) % 60;
            int minutes = (int) ((usedTime / (1000 * 60)) % 60);
            int hours = (int) ((usedTime / (1000 * 60 * 60)) % 24);
            logger.info("站点到货数据迁移结束，时间为：" + DateTimeUtil.getNowTime() + " 耗时：" + hours + ":" + minutes + ":" + seconds);
    	}
	}
	
	private void doTransfer(){
		this.logger.info("站点到货迁移数据执行开始.");
		String sql="select min(credate) as credate from express_ops_branch_daohuo";
		List list=this.jdbcTemplate.queryForList(sql);
		long firstFlowId=0;
		long lastFlowId=0;
		if(list!=null&&list.size()>0){
			Map map=(Map) list.get(0);
			Date lastTime=(Date) map.get("credate");
			if(lastTime!=null){
				logger.info("站点到货表有数据,lastTime="+lastTime);
				String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastTime);
				sql="select min(floworderid) as floworderid from express_ops_order_flow where credate>=str_to_date('"+time+"','%Y-%m-%d %H:%i:%s')";
				list=this.jdbcTemplate.queryForList(sql);
				if(list!=null&&list.size()>0){
					map=(Map) list.get(0);
					Long flowIdObj=(Long) map.get("floworderid");
					if(flowIdObj!=null){
						lastFlowId=flowIdObj.longValue();
						logger.info("站点到货表有数据,lastId="+lastFlowId);
					}
				}
			}
		}

		if(lastFlowId==0){
			sql="select max(floworderid) as floworderid from express_ops_order_flow";
			lastFlowId=this.jdbcTemplate.queryForLong(sql);
			logger.info("站点到货表没有数据,lastId="+lastFlowId);
		}
		
		logger.info("站点到货lastId="+lastFlowId);
		if(lastFlowId>0){
			sql="select min(floworderid) as floworderid from express_ops_order_flow";
			firstFlowId=this.jdbcTemplate.queryForLong(sql);
			logger.info("操作记录,firstFlowId="+firstFlowId+",lastFlowId="+lastFlowId);
			
			long startId=0;
			long endId=0;

			long size=10000;

				sql=
					"INSERT IGNORE INTO express_ops_branch_daohuo(cwb,branchid,credate)"+
					"(SELECT cwb,branchid,credate FROM express_ops_order_flow  WHERE floworderid<=? AND floworderid>? AND flowordertype IN(7,8))"
					;
				endId=lastFlowId;
				startId=endId-size;
				logger.info("站点到货迁移,endId页="+endId+",startId="+startId+",lastFlowId="+lastFlowId+",firstFlowId="+firstFlowId);
				while(endId<=lastFlowId&&endId>=firstFlowId&&endId>0&&(!stop)){
					lastFlowIdVar=lastFlowId;
					firstFlowIdVar=firstFlowId;
					endIdVar=endId;
					startIdVar=startId;
					
					this.jdbcTemplate.update(sql, endId,startId);//不加事务
					endId=startId;
					startId=endId-size;
					logger.info("站点到货迁移,endId页="+endId+",startId="+startId+",lastFlowId="+lastFlowId+",firstFlowId="+firstFlowId);
				}
			
				if(stop){
					updateState("3");
					this.logger.info("中断了站点到货迁移任务.");
				}else{
					updateState("0");
					this.logger.info("关闭了站点到货迁移数据标志.");
				}
		}
		
		this.logger.info("站点到货迁移数据执行完成.");
	}
	
	private void updateState(String state){
		String sql = "update express_set_system_install set value='"+state+"' where name='branchdaohuoqianyi'";
		this.jdbcTemplate.update(sql);
	}
	
	public String queryState(){
		String state="";
		SystemInstall systemInstall = this.systemInstallDAO.getSystemInstallByName("branchdaohuoqianyi");
		if(systemInstall!=null){
			state=systemInstall.getValue();
		}
		if(state.equals("0")){
			state="已完成";
		}else if(state.equals("1")){
			state="未启动";
		}else if(state.equals("2")){
			state="运行中";
		}else if(state.equals("3")){
			state="中断";
		}else if(state.equals("4")){
			state="异常";
		}
		return state;
	}
}
