package cn.explink.b2c.tps;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderServiceHelper;
import com.pjbest.deliveryorder.service.DoTrackBoxInfo;
import com.pjbest.deliveryorder.service.DoTrackFeedbackRequest;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;

import cn.explink.b2c.auto.order.service.AutoUserService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TpsCwbFlowVo;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.service.CwbOrderService;


@Service
public class TpsCwbFlowPushService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TpsCwbFlowService tpsCwbFlowService;
	@Autowired
	private JointService jointService;
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private CwbOrderService cwbOrderService;
	@Autowired
	private TranscwbOrderFlowDAO transcwborderFlowDAO;
	@Autowired
	private OrderFlowDAO orderFlowDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private BranchDAO branchDAO;
	@Autowired
	private TransCwbDetailDAO transCwbDetailDAO;
	@Autowired
	private AutoUserService autoUserService;
	
	private String today="";
	
	private static final String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	
	public void process(){
		this.logger.info("订单重量体积推送tps开始...");

		try {
			int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.TPS_Cwb_Flow.getKey());//
			if(isOpenFlag!=1){
				this.logger.info("订单体积重量推送tps开关未开启.");
				return;
			}
			
			TpsCwbFlowCfg cfg= tpsCwbFlowService.getTpsCwbFlowCfg();
			if(cfg==null||cfg.getOpenFlag()!=1){
				this.logger.info("订单体积重量推送tps开关未开启.");
				return;
			}
			
			List<TpsCwbFlowVo> dataList= tpsCwbFlowService.retrieveData(cfg.getMaxDataSize(),cfg.getMaxTryTime());
			handleData(dataList);
			housekeepData(cfg);
		} catch (Exception ex) {
			logger.error("处理订单体积重量推送tps临时表数据出错.", ex);
		}
	}
	
	private void handleData(List<TpsCwbFlowVo> dataList){
		long cnt=0;
		if(dataList!=null){
			cnt=dataList.size();
		}
		logger.info("订单体积重量推送tps时读取行数:"+cnt);
		
		if(cnt==0){
			return;
		}
		for(TpsCwbFlowVo vo:dataList){
			try {
				List<DoTrackFeedbackRequest> reqList=prepareRequest(vo);
				
				if(reqList!=null&&reqList.size()>0){
					for(DoTrackFeedbackRequest req:reqList){
						send(req,6000);
					}
					this.tpsCwbFlowService.complete(vo);
					
				}else{
					throw new RuntimeException("请求对象为空.");
				}

				logger.info("体积重量推送tps成功.cwb="+vo.getCwb()+",scancwb="+vo.getScancwb());
			} catch (Exception e) {
				logger.error("体积重量推送tps时出错.cwb="+vo.getCwb()+",scancwb="+vo.getScancwb(),e);
				this.tpsCwbFlowService.comleteWithError(vo,"体积重量推送tps时出错."+e.getMessage());
			}
		}
	}
	
	
	//按箱来发送
	private List<DoTrackFeedbackRequest> prepareRequest(TpsCwbFlowVo vo){
		CwbOrder co=this.cwbDAO.getCwbByCwb(vo.getCwb());
		if(co==null){
			throw new RuntimeException("没找到此订单.");
		}

		String transportNo=cwbDAO.getTpsTransportNoByCwb(vo.getCwb());
		if(transportNo==null||transportNo.length()<1){
			throw new RuntimeException("没找到此订单的TPS运单号.");
		}
		
		List<DoTrackFeedbackRequest> reqList=new ArrayList<DoTrackFeedbackRequest>();
		if(co.getIsmpsflag()==IsmpsflagEnum.no.getValue()){
			long userid=0;
			long branchid=0;
			Date createDate=null;
			List<String> transcwbList=new ArrayList<String>();
			if(vo.getCwb().equals(vo.getScancwb())){
				if(co.getTranscwb()==null||co.getTranscwb().trim().length()<1){
					transcwbList.add(vo.getCwb());
				}else{
					transcwbList=cwbOrderService.getTranscwbList(co.getTranscwb());
					if(transcwbList==null||transcwbList.size()<1){
						throw new RuntimeException("没找到此订单号的相关运单号.");
					}
				}
				OrderFlow orderFlow=orderFlowDAO.queryFlow(vo.getCwb(), FlowOrderTypeEnum.getByValue((int)vo.getFlowordertype()));
				if(orderFlow==null){
					if(vo.getSendemaildate()==1){
						logger.info("feijidan0 autouser,cwb="+vo.getCwb()+",scancwb="+vo.getScancwb());
						//自动化入库异常时才会来到这里
						User autoUser=this.autoUserService.getSessionUser();
						orderFlow=new OrderFlow();
						orderFlow.setBranchid(autoUser.getBranchid());
						orderFlow.setUserid(autoUser.getUserid());
						orderFlow.setCredate(new Date());
					}else{
						throw new RuntimeException("没找到此订单号的操作明细1.");
					}
				}
				userid=orderFlow.getUserid();
				branchid=orderFlow.getBranchid();
				createDate=orderFlow.getCredate();
			}else{
				TranscwbOrderFlow transcwbOrderFlow=transcwborderFlowDAO.getTranscwbOrderFlowByCwbAndFlowordertype(vo.getScancwb(), vo.getCwb(), vo.getFlowordertype());
				if(transcwbOrderFlow!=null){
					transcwbList.add(vo.getScancwb());
					userid=transcwbOrderFlow.getUserid();
					branchid=transcwbOrderFlow.getBranchid();
					createDate=transcwbOrderFlow.getCredate();
				}else{
					OrderFlow orderFlow=orderFlowDAO.queryFlow(vo.getCwb(),  FlowOrderTypeEnum.getByValue((int)vo.getFlowordertype()));
					if(orderFlow==null){
						if(vo.getSendemaildate()==1){
							logger.info("feijidan1 autouser,cwb="+vo.getCwb()+",scancwb="+vo.getScancwb());
							//自动化入库异常时才会来到这里
							User autoUser=this.autoUserService.getSessionUser();
							orderFlow=new OrderFlow();
							orderFlow.setBranchid(autoUser.getBranchid());
							orderFlow.setUserid(autoUser.getUserid());
							orderFlow.setCredate(new Date());
						}else{
							throw new RuntimeException("没找到此订单号的操作明细2.");
						}
					}
					transcwbList.add(vo.getScancwb());
					userid=orderFlow.getUserid();
					branchid=orderFlow.getBranchid();
					createDate=orderFlow.getCredate();
				}
			}

			User operateUser = userDAO.getUserByUserid(userid);
			Branch operateBrach=branchDAO.getBranchById(branchid);
			
			if(transcwbList!=null&&transcwbList.size()>0){
				Date emaildate=null;
				for(String trancwb:transcwbList){
					DoTrackFeedbackRequest req=new DoTrackFeedbackRequest();
					req.setTransportNo(transportNo);
					req.setOperateType(vo.getFlowordertype()==FlowOrderTypeEnum.RuKu.getValue()?1:3);//出库对应tps的出站扫描3,入库对应是入站1
					req.setOperateOrg(operateBrach==null?null:operateBrach.getTpsbranchcode());
					req.setOperater(operateUser==null?null:operateUser.getRealname());
					req.setOperateTime(createDate);
					DoTrackBoxInfo boxInfo=new DoTrackBoxInfo();
					boxInfo.setBoxNo(trancwb);
					boxInfo.setVolume(vo.getVolume().doubleValue());
					boxInfo.setWeight(vo.getWeight().doubleValue());
					req.setBoxInfo(boxInfo);
					if(vo.getSendemaildate()==1){//不设置emaildate则tps不处理
						if(emaildate==null){
							emaildate=parseEmaildate(co.getEmaildate());
						}
						req.setOqcDate(emaildate);
					}
					reqList.add(req);
				}
			}
		}else{
			TranscwbOrderFlow flow=transcwborderFlowDAO.getTranscwbOrderFlowByCwbAndFlowordertype(vo.getScancwb(), vo.getCwb(), vo.getFlowordertype());
			if(flow==null){
				if(vo.getSendemaildate()==1){
					logger.info("jidan autouser,cwb="+vo.getCwb()+",scancwb="+vo.getScancwb());
					//自动化入库异常时才会来到这里
					User autoUser=this.autoUserService.getSessionUser();
					flow=new TranscwbOrderFlow();
					flow.setBranchid(autoUser.getBranchid());
					flow.setUserid(autoUser.getUserid());
					flow.setCredate(new Date());
				}else{
					throw new RuntimeException("没找到此运单号的操作明细.");
				}
			}
			
			User operateUser = userDAO.getUserByUserid(flow.getUserid());
			Branch operateBrach=branchDAO.getBranchById(flow.getBranchid());
			
			DoTrackFeedbackRequest req=new DoTrackFeedbackRequest();
			req.setTransportNo(transportNo);
			req.setOperateType(vo.getFlowordertype()==FlowOrderTypeEnum.RuKu.getValue()?1:3);//出库对应tps的出站扫描3
			req.setOperateOrg(operateBrach==null?null:operateBrach.getTpsbranchcode());
			req.setOperater(operateUser==null?null:operateUser.getRealname());
			req.setOperateTime(flow.getCredate());
			
			
			DoTrackBoxInfo boxInfo=new DoTrackBoxInfo();
			boxInfo.setBoxNo(vo.getScancwb());
			boxInfo.setVolume(vo.getVolume().doubleValue());
			boxInfo.setWeight(vo.getWeight().doubleValue());
			req.setBoxInfo(boxInfo);
			if(vo.getSendemaildate()==1){
				req.setOqcDate(parseEmaildate(co.getEmaildate()));
			}
			
			reqList.add(req);
		}

		return reqList;
	}
	
	private Date parseEmaildate(String date){
		String emaildate=(date==null)?null:date.trim();
		if(emaildate==null||emaildate.length()<1){
			throw new RuntimeException("订单表没有出仓时间.");
		}
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT);
		Date ed=null;
		try {
			ed=sdf.parse(emaildate);
		} catch (ParseException e) {
			throw new RuntimeException("解析出仓时间出错.emaildate="+date);
		}
		return ed;
	}

	private void housekeepData(TpsCwbFlowCfg cfg){
		try{
			int hour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if(hour==3){//每天3点只运行一次
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String now=sdf.format(Calendar.getInstance().getTime());
				if(!now.equals(this.today)){
					logger.info("清理重量体积临时表");
					this.today=now;
					int day=(cfg==null||cfg.getHousekeepDay()<2)?2:cfg.getHousekeepDay();
					this.tpsCwbFlowService.housekeep(day);
				}
			}
		} catch (Exception ex) {
			logger.error("处理体积重量推送tps临时表的timeout数据时出错.", ex);
		}
	}
	
	private void send(DoTrackFeedbackRequest request,long timeout) throws OspException{
		InvocationContext.Factory.getInstance().setTimeout(timeout);
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
			
		pjDeliveryOrderService.feedbackDoTrack(request);//
	}

}
