package cn.explink.b2c.auto.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BaleDao;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.BaleService;
import cn.explink.service.CwbOrderService;

@Service
public class AutoOutWarehouseService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CwbOrderService cwborderService;
	

	
	@Autowired
	private AutoOrderStatusService autoOrderStatusService;
	
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private BaleService baleService;
	@Autowired
	private BaleDao baleDAO;
	
	@Transactional
	public void autOutWarehouse(AutoPickStatusVo data,User user){
			String cwb=null;//
			//String scancwb = null;
			long driverid=0;
			long branchid=0;//下一站点
			long truckid=0;
			String requestbatchno="";
			long confirmflag=1;//强制出库判断，0为非，1为强制
			String comment="";
			long reasonid=0;
			
			String baleno="";
			BigDecimal cargorealweight=null;
			BigDecimal cargovolume = null;
			String deliveryBranchCode=null;
			String boxno=null;
			boolean anbaochuku=true;


				cwb=data.getOrder_sn();
				cargorealweight=data.getOriginal_weight()==null||data.getOriginal_weight().length()<1?null:new BigDecimal(data.getOriginal_weight());
				cargovolume=data.getOriginal_volume()==null||data.getOriginal_volume().length()<1?null:new BigDecimal(data.getOriginal_volume());
				baleno=data.getPackage_no()==null||data.getPackage_no().trim().length()<1?null:data.getPackage_no().trim();
				deliveryBranchCode=data.getDestination_org()==null||data.getDestination_org().length()<1?"":data.getDestination_org();
				deliveryBranchCode=deliveryBranchCode.trim();
				boxno=data.getBox_no();
				if(boxno!=null){
					boxno=boxno.trim();
					if(boxno.length()<1){
						boxno=null;
					}
				}
				
				if(cwb==null||cwb.length()<1){
					throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"出库报文里订单号不能为空");
				}
				
				if(data.getOperate_type()==null||!data.getOperate_type().equals(AutoDispatchStatusService.OPERATE_TYPE_OUT)){
					throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"不是出库操作类型");
				}
				
				if(cargorealweight==null){
					throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"出库报文里重量不能为空");
				}
				
				if(cargovolume==null){
					throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"出库报文里体积不能为空");
				}
				
				if(baleno==null){
					anbaochuku=false;
					//throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"出库报文里包号不能为空");
				}
				
				if(deliveryBranchCode.length()==0){
					throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"出库报文里目的地站点不能为空");
				}
				
				cwb=cwb.trim();
				cwb = this.cwborderService.translateCwb(cwb);
				
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if(cwbOrder==null){
					throw new AutoWaitException("模拟出库时没找到此订单");
				}
				
				long deliveryBranchId=autoOrderStatusService.getDeliveryBranchId(deliveryBranchCode);
				if(deliveryBranchCode.length()>0&&deliveryBranchId==0){
					throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"没找到此目的地站");
				}
				
				//根据TPS目的站算出下一站
				if(deliveryBranchId>0){
					branchid=autoOrderStatusService.getNextBranch(user.getBranchid(),deliveryBranchCode);
					if(branchid==0){
						throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"根据目的地站找不到下一站");
					}
				}

				boolean isJidan=false;
				if(cwbOrder.getIsmpsflag()==IsmpsflagEnum.yes.getValue()){
					isJidan=true;
					if(boxno==null){
						throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"集单模式下模拟出库时箱号不能为空");
					}
				}
				
				//更新体积、重量、配送站点、目的站,包号按包出库时会自动更新
				this.autoOrderStatusService.updateAutoOrder(cwb,boxno,cargovolume,cargorealweight,deliveryBranchId,isJidan);
				
				long isypdjusetranscwb =0;
				Customer customer=customerDAO.getCustomerById(cwbOrder.getCustomerid());
				if(customer.getCustomerid()>0){
					isypdjusetranscwb=customer.getIsypdjusetranscwb();
				}
				
				//有箱号则扫箱号，没则扫订单号
				String scannum=cwb;
				if(boxno!=null&&isypdjusetranscwb==1){
					scannum=boxno;
				}
				if(baleno==null){
					//非按包出库
					cwbOrder = this.cwborderService.outWarehous(user, cwb, scannum, driverid, truckid, branchid,
								requestbatchno == null ? 0 : requestbatchno.length() == 0 ? 0 : Long.parseLong(requestbatchno), confirmflag == 1, comment, baleno, reasonid, false, anbaochuku);
				}else{
					//按包出库
					this.baleService.baleaddcwbChukuCheck(user, baleno, scannum, confirmflag == 1, user.getBranchid(), branchid);
					this.baleService.baleaddcwb(user, baleno, scannum, branchid);
					this.baleDAO.updateAddBaleScannum(baleno);
				}
		}
	





}
