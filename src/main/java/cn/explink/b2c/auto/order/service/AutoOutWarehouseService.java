package cn.explink.b2c.auto.order.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
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

	
	@Transactional
	public void autOutWarehouse(AutoPickStatusVo data,User user){
			String cwb=null;//
			//String scancwb = null;
			long driverid=0;
			long branchid=0;//下一站点 ?????
			long truckid=0;
			String requestbatchno="";
			long confirmflag=1;//强制出库判断，0为非，1为强制
			String comment="";
			long reasonid=0;
			
			String baleno="";
			BigDecimal cargorealweight=null;
			BigDecimal cargovolume = null;
			String deliveryBranchCode=null;


			//try{
				cwb=data.getOrder_sn();//
				
				cargorealweight=data.getOriginal_weight()==null||data.getOriginal_weight().length()<1?null:new BigDecimal(data.getOriginal_weight());
				cargovolume=data.getOriginal_volume()==null||data.getOriginal_volume().length()<1?null:new BigDecimal(data.getOriginal_volume());
				baleno=data.getPackage_no()==null||data.getPackage_no().length()<1?null:data.getPackage_no();
				deliveryBranchCode=data.getDestination_org()==null||data.getDestination_org().length()<1?"":data.getDestination_org();
				deliveryBranchCode=deliveryBranchCode.trim();
				
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
				
				if(deliveryBranchCode.length()==0){
					throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"出库报文里目的地站点不能为空");
				}
				
				cwb=cwb.trim();
				cwb = this.cwborderService.translateCwb(cwb);//???
				
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if(cwbOrder==null){
					throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"不存在此订单");
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
				
				//更新体积、重量、配送站点、包号;有才更新
				//要try-catch ?
				this.autoOrderStatusService.updateAutoOrder(cwb,cargovolume,cargorealweight,baleno,deliveryBranchId);
				
				
				String transcwb=cwbOrder.getTranscwb()==null?null:cwbOrder.getTranscwb().trim();
				if(transcwb!=null&&transcwb.length()>0){
					String [] transcwbArr=transcwb.split(",");
					for(String boxno:transcwbArr){
						String scancwb=boxno.trim();
						//参数值正确？
						cwbOrder = this.cwborderService.outWarehous(user, cwb, scancwb, driverid, truckid, branchid,
								requestbatchno == null ? 0 : requestbatchno.length() == 0 ? 0 : Long.parseLong(requestbatchno), confirmflag == 1, comment, baleno, reasonid, false, false);
					}
				}else{
					//throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"出库时没找到箱号");
					String scancwb=cwb;
					cwbOrder = this.cwborderService.outWarehous(user, cwb, scancwb, driverid, truckid, branchid,
							requestbatchno == null ? 0 : requestbatchno.length() == 0 ? 0 : Long.parseLong(requestbatchno), confirmflag == 1, comment, baleno, reasonid, false, false);

				}
				
			/*} catch (Exception e) {
				logger.error("自动化出库出错.",e);
				CwbException ex=null;
				long flowordertye=FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
				if(e instanceof CwbException){
					flowordertye=((CwbException)e).getFlowordertye();
					ex=(CwbException) e;
				}else{
					ex=new CwbException(cwb,flowordertye,e.getMessage());
				}
				this.autoExceptionService.createExceptionCwbScan(cwb, flowordertye, ex.getMessage(), user.getBranchid(), user.getUserid(),
							0, 0, 0, 0, "",scancwb);
				throw ex;
			}*/

		}

	


}
