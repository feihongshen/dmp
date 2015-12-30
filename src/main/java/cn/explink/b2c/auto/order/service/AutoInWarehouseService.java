package cn.explink.b2c.auto.order.service;



import java.math.BigDecimal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.auto.order.mq.AutoDispatchStatusCallback;
import cn.explink.dao.BranchDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.dao.CwbDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
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
			 
			
			//try{
				cwb=data.getOrder_sn();
				
				if(cwb==null||cwb.length()<1){
					throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"入库报文里订单号不能为空");
				}
				
				if(data.getOperate_type()==null||!data.getOperate_type().equals(AutoDispatchStatusCallback.OPERATE_TYPE_IN)){
					throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"不是入库操作类型");
				}
				
				cwb=cwb.trim();
				cwb = this.cwborderService.translateCwb(cwb);
				
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if(cwbOrder==null){
					throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"不存在此订单");
				}
				
				Branch userbranch = this.branchDAO.getBranchById(user.getBranchid());
				if ((userbranch.getBranchid() != 0) && (userbranch.getSitetype() != BranchEnum.KuFang.getValue())) {
					throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"不是入库库房类型");
				}

				BigDecimal cargorealweight=data.getOriginal_weight()==null||data.getOriginal_weight().length()<1?null:new BigDecimal(data.getOriginal_weight());
				BigDecimal cargovolume=data.getOriginal_volume()==null||data.getOriginal_volume().length()<1?null:new BigDecimal(data.getOriginal_volume());
				String baleno=data.getPackage_no()==null||data.getPackage_no().length()<1?null:data.getPackage_no();
				int deliveryBranchId=data.getDestination_org()==null||data.getDestination_org().length()<1?0:Integer.parseInt(data.getDestination_org());

				this.autoOrderStatusService.updateAutoOrder(cwb,cargovolume,cargorealweight,baleno,deliveryBranchId);
				
				String transcwb=cwbOrder.getTranscwb()==null?null:cwbOrder.getTranscwb().trim();
				if(transcwb!=null&&transcwb.length()>0){
					String [] transcwbArr=transcwb.split(",");
					for(String boxno:transcwbArr){
						String scancwb=boxno.trim();
						this.cwborderService.intoWarehous(user, cwb,scancwb, customerid, driverid, requestbatchno, comment, "", false);
					}
				}else{
					//throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"入库时没找到箱号");
					String scancwb=cwb;
					this.cwborderService.intoWarehous(user, cwb,scancwb, customerid, driverid, requestbatchno, comment, "", false);
					
				}
				
				
				/*
				CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
				if (emaildate > 0) {
					if (null == co) {
						throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"查无此单:"+cwb);
					}
					if (co.getEmaildateid() != emaildate) {
						EmailDate ed = this.emaildateDAO.getEmailDateById(co.getEmaildateid());
						throw new CwbException(cwb,FlowOrderTypeEnum.RuKu.getValue(),"订单号不在本批次中，请选择" + ed.getEmaildatetime() + "的批次,订单号:"+cwb);
					}
				}
				
				//---------
				
				Branch userbranch = this.branchDAO.getBranchById(user.getBranchid());
				//站点？？？？
				if ((userbranch.getBranchid() != 0) && (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
					cwbOrder = this.cwborderService.substationGoods(user, cwb, scancwb, driverid, requestbatchno, comment, "", false);
				} else {
					if (youhuowudanflag.equals("0")) {
						this.checkyouhuowudan(user, cwb, customerid, user.getBranchid());
						if (co.getDeliverybranchid() == 0) {
							FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.RuKu;
							String bupipeishifouruku = "0"; // 0：不验证 1：验证 2：弹框
							bupipeishifouruku = this.systemInstallDAO.getSystemInstall("bupipeishifouruku") == null ? "0" : this.systemInstallDAO.getSystemInstall("bupipeishifouruku").getValue();
							if ("2".equals(bupipeishifouruku.trim())) {
								throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.ShangWeiPiPeiZhanDian);
							} else if ("1".equals(bupipeishifouruku.trim())) {
								throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.ShangWeiPiPeiZhanDianBuyunxuRuku);
							}
						}
					}
					cwbOrder = this.cwborderService.intoWarehous(user, cwb, scancwb, customerid, driverid, requestbatchno, comment, "", false);
				}
				*/
					
			/*} catch (Exception e) {
				logger.error("自动化入库出错.",e);
				CwbException ex=null;
				long flowordertye=FlowOrderTypeEnum.RuKu.getValue();
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

/*
	private String checkyouhuowudan(User user, String cwb, long customerid, long currentbranchid) {
		cwb = this.cwborderService.translateCwb(cwb);
		FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.RuKu;

		if (this.jdbcTemplate.queryForInt("select count(1) from express_sys_on_off where type='SYSTEM_ON_OFF' and on_off='on' ") == 0) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.SYS_SCAN_ERROR);
		}

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if ((customerid > 0) && (co != null)) {
			// TODO 因为客户的货物会混着扫描
			customerid = co.getCustomerid();

		}

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);

		if (co == null) {
			if (customerid < 1) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Y_H_W_D_WEI_XUAN_GONG_HUO_SHANG);
			}

			if ((userbranch.getSitetype() == BranchEnum.KuFang.getValue())
					&& (this.cwbALLStateControlDAO.getCwbAllStateControlByParam(CwbStateEnum.WUShuju.getValue(), FlowOrderTypeEnum.RuKu.getValue()) != null)) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.WuShuJuYouHuoWuDanError);

			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}
		}
		return "";
	}
*/

	
}
