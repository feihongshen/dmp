package cn.explink.b2c.auto.order.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tps.TpsCwbFlowService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.User;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;



@Service
public class AutoInWarehouseService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CwbOrderService cwborderService;
	@Autowired
	private BranchDAO branchDAO;

	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private TransCwbDetailDAO transCwbDetailDAO;
	@Autowired
	private TpsCwbFlowService tpsCwbFlowService;
	@Autowired
	private AutoOrderStatusService autoOrderStatusService;
	
	@Transactional
	public void autoInWarehouse(AutoPickStatusVo data,User user){


			 String cwb=null;//?????????
			 //String scancwb=null;
			 long customerid=-1;
			 long driverid=-1;
			 long requestbatchno=0;
			 String comment="";
			 long emaildate=0;
			 String youhuowudanflag="-1";//value has -1,0,1  ?????????
			 String boxno=data.getBox_no();
			 if(boxno!=null){
				boxno=boxno.trim();
				if(boxno.length()<1){
					boxno=null;
				}
			 }
			
			//try{
				cwb=data.getOrder_sn();
				
				if(cwb==null||cwb.length()<1){
					throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"入库报文里订单号不能为空");
				}
				
				if(data.getOperate_type()==null||!data.getOperate_type().equals(AutoDispatchStatusService.OPERATE_TYPE_IN)){
					throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"不是入库操作类型");
				}
				
				cwb=cwb.trim();
				cwb = this.cwborderService.translateCwb(cwb);
				
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if(cwbOrder==null){
					throw new AutoWaitException("模拟入库时没找到此订单");
				}
				
				validateTranscwb(cwbOrder,boxno);
				
				Branch userbranch = this.branchDAO.getBranchById(user.getBranchid());
				if ((userbranch.getBranchid() != 0) && (userbranch.getSitetype() != BranchEnum.KuFang.getValue())) {
					throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"不是入库库房类型");
				}

				//BigDecimal cargorealweight=data.getOriginal_weight()==null||data.getOriginal_weight().length()<1?null:new BigDecimal(data.getOriginal_weight());
				//BigDecimal cargovolume=data.getOriginal_volume()==null||data.getOriginal_volume().length()<1?null:new BigDecimal(data.getOriginal_volume());
				String baleno=data.getPackage_no()==null||data.getPackage_no().length()<1?null:data.getPackage_no();
				String deliveryBranchCode=data.getDestination_org()==null||data.getDestination_org().length()<1?"":data.getDestination_org();
				deliveryBranchCode=deliveryBranchCode.trim();
				String operateTime=data.getOperate_time()==null||data.getOperate_time().trim().length()<1?null:data.getOperate_time();
				
				if(operateTime==null){
					throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"模拟入库时操作时间不能为空");
				}
				
				BigDecimal cargorealweight=data.getOriginal_weight()==null||data.getOriginal_weight().trim().length()<1?null:new BigDecimal(data.getOriginal_weight());
				BigDecimal cargovolume=data.getOriginal_volume()==null||data.getOriginal_volume().trim().length()<1?null:new BigDecimal(data.getOriginal_volume());

				
				/*
				long deliveryBranchId=autoOrderStatusService.getDeliveryBranchId(deliveryBranchCode);
				if(deliveryBranchCode.length()>0&&deliveryBranchId==0){
					throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"没找到此目的地站");
				}
				
				//when in, no need update deliveryBranchId to caculat next branch????
				this.autoOrderStatusService.updateAutoOrder(cwb,null,null,null,baleno,deliveryBranchId,false);
				*/
				
				long isypdjusetranscwb =0;
				Customer customer=customerDAO.getCustomerById(cwbOrder.getCustomerid());
				if(customer.getCustomerid()>0){
					isypdjusetranscwb=customer.getIsypdjusetranscwb();
				}
				
				//先测非集单，再测集单
		
				//validateIsSubCwb 验证箱号是否正确? //catch the ExceptionCwbErrorTypeEnum.Qing_SAO_MIAO_YUN_DAN_HAO ?
				//集单也用这个方法去出库？？？
				//不区分扫箱号标志？？？
				String scancwb=cwb;
				if(boxno!=null&&isypdjusetranscwb==1){
					scancwb=boxno;
				}
				
				//自动入库前，对订单进行校验，只有订单/运单处于订单导入的状态才能做自动化入库--- 刘武强20160928
				this.autoOrderStatusService.getIntoWarePermitFlag(cwbOrder, scancwb);
				
				if(boxno!=null&&isypdjusetranscwb==1){
					//一票多件有箱号且要扫箱号;
					//一票一件有箱号且要扫箱号;
					//scancwb=boxno;
					this.cwborderService.intoWarehous(user, cwb,scancwb, customerid, driverid, requestbatchno, comment, "", false);
				}else{
					//测试用例：
					//一票多件没箱号且不扫箱号;
					//一票多件没箱号且要扫箱号;
					//一票一件没箱号且不扫箱号;
					//一票一件没箱号且要扫箱号;
					//一票多件有箱号且不要扫箱号;
					//一票一件有箱号且不要扫箱号;
					//throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"入库时没找到箱号");
					//scancwb=cwb;
					this.cwborderService.intoWarehous(user, cwb,scancwb, customerid, driverid, requestbatchno, comment, "", false);
					
				}

				//保存到eamildate,看日期格式是否需要转换
				this.tpsCwbFlowService.save(cwbOrder, scancwb, FlowOrderTypeEnum.RuKu,user.getBranchid(),operateTime,false,cargorealweight,cargovolume);
		}

	//看运单数据是否已经到达
	private void validateTranscwb(CwbOrder cwbOrder,String boxno){
		//外单没箱号
		if(boxno==null){
			return;
		}
		long totalNum=cwbOrder.getSendcarnum();//total  num ????
		
		if(totalNum>1){
			if((cwbOrder.getTranscwb()==null)||(
					cwbOrder.getTranscwb()!=null&&cwbOrder.getTranscwb().indexOf(boxno)<0)){
				throw new AutoWaitException("模拟入库时没找到相关运单号数据,cwb="+cwbOrder.getCwb());
			}
			
			if(cwbOrder.getIsmpsflag()==IsmpsflagEnum.yes.getValue()){
				TransCwbDetail transCwbDetail = transCwbDetailDAO.findTransCwbDetailByTransCwb(boxno);
				if(transCwbDetail==null){
					throw new AutoWaitException("集单模式下模拟入库时没找到此运单("+boxno+")数据)");
				}
			}
			
		}
	}
	
}
