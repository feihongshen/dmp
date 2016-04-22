package cn.explink.b2c.tps;


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
	
	private String today="";
	
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
			
			List<TpsCwbFlowVo> dataList= tpsCwbFlowService.retrieveData(cfg.getMaxDataSize(),cfg.getMaxTryTime(),FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
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
					if(reqList.size()<2){
						DoTrackFeedbackRequest req=reqList.get(0);
						send(req,6000);
						vo.setErrinfo("");
						vo.setState(1);//0等待处理，1成功处理，2错误
						this.tpsCwbFlowService.update(vo);
					}else{
						List<String> transcwbList=new ArrayList<String>();
						for(DoTrackFeedbackRequest req:reqList){
							send(req,6000);
							transcwbList.add(req.getBoxInfo().getBoxNo());
						}
						this.tpsCwbFlowService.complete(vo,transcwbList,1);
					}
				}else{
					throw new RuntimeException("请求对象为空.");
				}

				logger.info("体积重量推送tps成功.cwb="+vo.getCwb()+",scancwb="+vo.getScancwb());
			} catch (Exception e) {
				logger.error("体积重量推送tps时出错.cwb="+vo.getCwb()+",scancwb="+vo.getScancwb(),e);
				vo.setErrinfo("push tps error."+e.getMessage());
				vo.setState(2);
				this.tpsCwbFlowService.update(vo);
			}
		}
	}
	
	
	
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
			List<String> transcwbList=new ArrayList<String>();;
			if(vo.getCwb().equals(vo.getScancwb())){
				if(co.getTranscwb()==null||co.getTranscwb().trim().length()<1){
					transcwbList.add(vo.getCwb());
				}else{
					transcwbList=cwbOrderService.getTranscwbList(co.getTranscwb());
				}
				OrderFlow orderFlow=orderFlowDAO.getOrderFlowByCwbAndFlowtype(vo.getCwb(), ""+vo.getFlowordertype());
				if(orderFlow==null){
					throw new RuntimeException("没找到此订单号的操作明细1.");
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
					OrderFlow orderFlow=orderFlowDAO.getOrderFlowByCwbAndFlowtype(vo.getCwb(), ""+vo.getFlowordertype());
					if(orderFlow==null){
						throw new RuntimeException("没找到此订单号的操作明细2.");
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
				for(String trancwb:transcwbList){
					DoTrackFeedbackRequest req=new DoTrackFeedbackRequest();
					req.setTransportNo(transportNo);
					req.setOperateType(1);//入库对应tps的入站扫描1
					req.setOperateOrg(operateBrach==null?null:operateBrach.getTpsbranchcode());
					req.setOperater(operateUser==null?null:operateUser.getRealname());
					req.setOperateTime(createDate);
					DoTrackBoxInfo boxInfo=new DoTrackBoxInfo();
					boxInfo.setBoxNo(trancwb);
					boxInfo.setVolume(new Double("0.01"));
					boxInfo.setWeight(new Double("0.01"));
					req.setBoxInfo(boxInfo);
					reqList.add(req);
				}
			}
		}else{
			TranscwbOrderFlow flow=transcwborderFlowDAO.getTranscwbOrderFlowByCwbAndFlowordertype(vo.getScancwb(), vo.getCwb(), vo.getFlowordertype());
			if(flow==null){
				throw new RuntimeException("没找到此运单号的操作明细.");
			}
			List<TransCwbDetail> transCwbDetailList=transCwbDetailDAO.getTransCwbDetailListByTransCwbs("('"+vo.getScancwb()+"')");
			TransCwbDetail transCwbDetail=null;
			if(transCwbDetailList!=null&&transCwbDetailList.size()>0){
				for(TransCwbDetail detail:transCwbDetailList){
					if(detail.getCwb().equals(vo.getCwb())){
						transCwbDetail=detail;
						break;
					}
				}
			}
			if(transCwbDetail==null){
				throw new RuntimeException("没找到此运单号的明细数据.");
			}
			
			User operateUser = userDAO.getUserByUserid(flow.getUserid());
			Branch operateBrach=branchDAO.getBranchById(flow.getBranchid());
			
			DoTrackFeedbackRequest req=new DoTrackFeedbackRequest();
			req.setTransportNo(transportNo);
			req.setOperateType(1);//入库对应tps的入站扫描1
			req.setOperateOrg(operateBrach==null?null:operateBrach.getTpsbranchcode());
			req.setOperater(operateUser==null?null:operateUser.getRealname());
			req.setOperateTime(flow.getCredate());
			
			Double volume=null;
			if(transCwbDetail.getVolume()==null||transCwbDetail.getVolume().toString().equals("0.0000")){
				volume=new Double("0.01");
			}else{
				volume=transCwbDetail.getVolume().doubleValue();
			}
			Double weight=null;
			if(transCwbDetail.getWeight()==null||transCwbDetail.getWeight().toString().equals("0.000")){
				weight=new Double("0.01");
			}else{
				weight=transCwbDetail.getWeight().doubleValue();
			}
			
			DoTrackBoxInfo boxInfo=new DoTrackBoxInfo();
			boxInfo.setBoxNo(vo.getScancwb());
			boxInfo.setVolume(volume);
			boxInfo.setWeight(weight);
			req.setBoxInfo(boxInfo);
			
			reqList.add(req);
		}

		return reqList;
	}

	private void housekeepData(TpsCwbFlowCfg cfg){
		try{
			int hour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if(hour==3){//每天3点只运行一次
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String now=sdf.format(Calendar.getInstance().getTime());
				if(!now.equals(this.today)){
					this.today=now;
					int day=(cfg==null||cfg.getHousekeepDay()<7)?7:cfg.getHousekeepDay();
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
