package cn.explink.b2c.auto.order.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.explink.b2c.auto.order.dao.ExpressOrderDao;
import cn.explink.b2c.auto.order.dao.TPSDataImportDAO_B2c;
import cn.explink.b2c.auto.order.vo.InfDmpOrderSendVO;
import cn.explink.b2c.tmall.CwbColumnSet;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tps.CwbColumnSetTPS;
import cn.explink.b2c.vipshop.ReaderXMLHandler;
import cn.explink.b2c.vipshop.SOAPHandler;
import cn.explink.b2c.vipshop.VipGathercompEnum;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.b2c.vipshop.VipShopGetCwbDataService;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.controller.MQCwbOrderDTO;
import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ExcelColumnSet;
import cn.explink.domain.ImportValidationManager;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.enumutil.MpsTypeEnum;
import cn.explink.enumutil.MpsswitchTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderValidator;
import cn.explink.service.DataImportService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

@Service
public class MQGetOrderDataService {
	
	@Autowired
	ReaderXMLHandler readXMLHandler;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	TPSDataImportDAO_B2c tpsDataImportDAO_B2c;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	CwbDAO cwbDAO;
	@Produce(uri = "jms:topic:addressmatchOXO")
	ProducerTemplate addressmatch;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;
	@Autowired
	OrderGoodsDAO orderGoodsDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	VipShopGetCwbDataService vipShopGetCwbDataService;
	@Autowired
	TPSOrderImportService_B2c tPSOrderImportService_B2c;
	@Autowired
	SOAPHandler soapHandler;
	@Autowired
	CwbColumnSet cwbColumnSet;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	@Qualifier("inter.expressOrderDao")
	ExpressOrderDao expressOrderDao;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	ImportValidationManager importValidationManager;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	@Autowired
	CwbColumnSetTPS cwbColumnSetTPS;
	
	private Logger logger = LoggerFactory.getLogger(MQGetOrderDataService.class);
    
	/**
	 * 验证返回的sign
	 *
	 * @param paseSign
	 * @return
	 */
	public boolean checkSignResponseInfo(String paseSign, String MD5Str) {
		if (paseSign.equals(MD5Str)) {
			return true;
		}
		return true;
	}

	//普通单在没有开启托运单模式下，数据插入临时表
	public void insertTempOnAttemperOpen(VipShop vipshop, MQCwbOrderDTO order) {
		long customerid = Long.valueOf(order.getCustomerid());
		try {
			long warehouseid = vipshop.getWarehouseid();
			long ewarehouseid = warehouseid == 0 ? dataImportService_B2c.getTempWarehouseIdForB2c() : warehouseid;
			//开启以出仓时间作为批次标记
			String emaildate = order.getRemark2();
			 
			//如果传过来的出仓时间为空，则使用当前日期作为批次时间
			if(emaildate == null || emaildate.trim().equals("")){
				emaildate = DateTimeUtil.getNowDate() + " 00:00:00";
				order.setRemark2(emaildate);
			}

			//导入批次表(express_ops_emaildate)emaildatetime使用当天第一条进入DMP系统订单的创建时间
			EmailDate ed = dataImportService.getOrCreateEmailDate(customerid, 0, ewarehouseid);
			//临时表(express_ops_cwb_detail_b2ctemp)和正式表(express_ops_cwb_detail)的emaildate(发货时间)取TMS的出仓时间
			ed.setEmaildatetime(emaildate);
			//数据导入系统入口
			this.Analizy_DataDealByB2c(customerid, B2cEnum.VipShop_beijing.getMethod(), order, warehouseid,ed);
			this.logger.info("TPS订单下发接口普通单在没有开启托运单模式下，数据插入临时表处理成功！");
		} catch (Exception e) {
			this.logger.error("TPS订单下发接口普通单在没有开启托运单模式下，数据插入临时表异常,cwb=" + order.getCwb() + "message:", e);
			CwbException ex=null;
			long flowordertye=FlowOrderTypeEnum.DaoRuShuJu.getValue();
			if(e instanceof CwbException){
				flowordertye=((CwbException)e).getFlowordertye();
				ex=(CwbException)e;
			}else{
				ex=new CwbException(order.getCwb(),flowordertye,e.getMessage());
			}
			throw ex;
		}
	}
	
	//普通单在开启托运单模式下，数据插入临时表
	public void insertTempOnAttemperClose(
			VipShop vipshop, MQCwbOrderDTO order){
		long customerid = Long.valueOf(order.getCustomerid());
		try {
			String emaildate = order.getRemark4();
			if(emaildate == null || emaildate.trim().equals("")){
				emaildate = DateTimeUtil.getNowDate() + " 00:00:00";				
				order.setRemark4(emaildate);
			}
			long warehouseid = vipshop.getWarehouseid();
			long ewarehouseid = warehouseid == 0 ? dataImportService_B2c.getTempWarehouseIdForB2c() : warehouseid;
			EmailDate ed = dataImportService.getEmailDate_B2CByEmaildate(customerid, 0, ewarehouseid, emaildate);
			//数据导入系统入口
			this.Analizy_DataDealByB2c(customerid, B2cEnum.VipShop_beijing.getMethod(), order, ewarehouseid, ed);
			this.logger.error("TPS订单下发接口在开启托运单模式下，数据插入临时表成功!cwb=" + order.getCwb());
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("TPS订单下发接口在开启托运单模式下，数据插入临时表异常!cwb=" + order.getCwb(), e);
			CwbException ex=null;
			long flowordertye=FlowOrderTypeEnum.DaoRuShuJu.getValue();
			if(e instanceof CwbException){
				flowordertye=((CwbException)e).getFlowordertye();
				ex=(CwbException)e;
			}else{
				ex=new CwbException(order.getCwb(),flowordertye,e.getMessage());
			}
			throw ex;
		}
	}
	
	
	
	//根据订单模板字段获取对象
	public CwbOrderDTO getCwbOrderAccordingtoConf(ExcelColumnSet excelColumnSet, MQCwbOrderDTO order, long branchid) throws Exception {
		CwbOrderDTO cwbOrder = new CwbOrderDTO();
		cwbOrder.setCwb(order.getCwb());// 订单号
		if (excelColumnSet.getTranscwbindex() != 0) {
			cwbOrder.setTranscwb(order.getTranscwb());
			;// 运单号
		}
		if (excelColumnSet.getConsigneenameindex() != 0) {
			cwbOrder.setConsigneename(order.getConsigneename());// 收件人名称
		}
		if (excelColumnSet.getConsigneeaddressindex() != 0) {
			cwbOrder.setConsigneeaddress(order.getConsigneeaddress());// 收件人地址
		}
		if (excelColumnSet.getConsigneepostcodeindex() != 0) {
			cwbOrder.setConsigneepostcode(order.getConsigneepostcode());// 收件人邮编
		}
		if (excelColumnSet.getConsigneephoneindex() != 0) {
			cwbOrder.setConsigneephone(order.getConsigneephone());// 收件人电话
		}
		if (excelColumnSet.getSendcargonameindex() != 0) {
			cwbOrder.setSendcargoname(order.getSendcargoname());// 发出商品名称
		}
		if (excelColumnSet.getBackcargonameindex() != 0) {
			cwbOrder.setBackcargoname(order.getBackcargoname());
		}
		if (excelColumnSet.getReceivablefeeindex() != 0) {
			cwbOrder.setReceivablefee(order.getReceivablefee());// 代收货款应收金额
		}
		if (excelColumnSet.getPaybackfeeindex() != 0) {
			cwbOrder.setPaybackfee(order.getPaybackfee());// 上门退货应退金额
		}
		if (excelColumnSet.getCargorealweightindex() != 0) {
			cwbOrder.setCargorealweight(order.getCargorealweight());// 货物重量kg
		}
		if (excelColumnSet.getExcelbranchindex() != 0) {
			cwbOrder.setExcelbranch(order.getExcelbranch() == null ? "" : order.getExcelbranch());// 站点
		}
		if (excelColumnSet.getCwbremarkindex() != 0) {
			cwbOrder.setCwbremark(order.getCwbremark());// 订单备注
		}

		if (excelColumnSet.getEmaildateindex() != 0) {
			cwbOrder.setEmaildate(order.getEmaildate());// 发货时间Id
		} else {
			cwbOrder.setEmaildate(DateTimeUtil.getNowTime());
		}

		if (excelColumnSet.getConsigneenoindex() != 0) {
			cwbOrder.setConsigneeno(order.getConsigneeno());// 收件人编号
		}

		if (excelColumnSet.getCargoamountindex() != 0) {
			cwbOrder.setCargoamount(order.getCargoamount());// 货物金额
		}
		if (excelColumnSet.getCustomercommandindex() != 0) {
			cwbOrder.setCustomercommand(order.getCustomercommand());// 客户要求
		}
		if (excelColumnSet.getCargotypeindex() != 0) {

			cwbOrder.setCargotype(order.getCargotype());// 货物类别
		}
		if (excelColumnSet.getCargosizeindex() != 0) {
			cwbOrder.setCargosize(order.getCargosize());// 商品尺寸
		}
		if (excelColumnSet.getBackcargoamountindex() != 0) {
			cwbOrder.setBackcargoamount(order.getCargoamount());
		}
		if (excelColumnSet.getDestinationindex() != 0) {
			cwbOrder.setDestination(order.getDestination());// 目的地
		}
		if (excelColumnSet.getTranswayindex() != 0) {
			cwbOrder.setTransway(order.getTranscwb());// 运输方式
		}

		if (excelColumnSet.getSendcargonumindex() != 0) {
			cwbOrder.setSendcargonum(order.getSendcargonum());// 发货数量
		}
		if (excelColumnSet.getCommoncwb() != 0) {
			cwbOrder.setCommoncwb(order.getCommoncwb());// commoncwb快乐购
		}
		if (excelColumnSet.getBackcargonumindex() != 0) {
			cwbOrder.setBackcargonum(order.getBackcargonum());// 取货数量
		}
		if (excelColumnSet.getCwbordertypeindex() != 0) {
			cwbOrder.setCwbordertypeid(order.getCwbordertypeid());// 订单类型（1配送
		} else {
			cwbOrder.guessCwbordertypeid();
		}

		if (excelColumnSet.getPaywayindex() != 0) {
			cwbOrder.setPaywayid(order.getPaywayid());
			cwbOrder.setNewpaywayid(String.valueOf(order.getPaywayid()));
		} else {
			cwbOrder.guessPaywayid();
			cwbOrder.setNewpaywayid(PaytypeEnum.Xianjin.getValue() + "");
		}
		if (excelColumnSet.getCwbdelivertypeindex() != 0) {
			cwbOrder.setCwbdelivertypeid(order.getCwbdelivertypeid());
		}
		if (excelColumnSet.getCwbprovinceindex() != 0) {
			cwbOrder.setCwbprovince(order.getCwbprovince());
		}
		if (excelColumnSet.getCwbcityindex() != 0) {
			cwbOrder.setCwbcity(order.getCwbcity());
		}
		if (excelColumnSet.getCwbcountyindex() != 0) {
			cwbOrder.setCwbcounty(order.getCwbcounty());
		}
		if (excelColumnSet.getConsigneemobileindex() != 0) {
			cwbOrder.setConsigneemobile(order.getConsigneemobile());
		}

		if (excelColumnSet.getShipcwbindex() != 0) {
			cwbOrder.setShipcwb(order.getShipcwb());
		}
		if (excelColumnSet.getWarehousenameindex() != 0) {
			cwbOrder.setCustomerwarehouseid(order.getCustomerwarehouseid());

		}
		if (excelColumnSet.getIsaudit() != 0) {
			cwbOrder.setIsaudit(String.valueOf(order.getIsaudit()) == null ? 0 : order.getIsaudit());
		}
		cwbOrder.setStartbranchid(branchid);

		if (excelColumnSet.getModelnameindex() != 0) {
			cwbOrder.setModelname(order.getModelname());
		}

		if (excelColumnSet.getCargovolumeindex() != 0) {
			cwbOrder.setCargovolume(order.getCargovolume());
		}
		if (excelColumnSet.getConsignoraddressindex() != 0) {
			cwbOrder.setConsignoraddress(order.getConsignoraddress());
		}
		if (excelColumnSet.getTmall_notifyidindex() != 0) {
			cwbOrder.setTmall_notifyid(order.getTmall_notifyid());
		}

		if (excelColumnSet.getMulti_shipcwbindex() != 0) {
			cwbOrder.setMulti_shipcwb(order.getMulti_shipcwb());
		}

		if (excelColumnSet.getRemark1index() != 0) {
			cwbOrder.setRemark1(order.getRemark1() == null ? "" : order.getRemark1());
		}
		if (excelColumnSet.getRemark2index() != 0) {
			cwbOrder.setRemark2(order.getRemark2() == null ? "" : order.getRemark2());
		}
		if (excelColumnSet.getRemark3index() != 0) {
			cwbOrder.setRemark3(order.getRemark3() == null ? "" : order.getRemark3());
		}
		if (excelColumnSet.getRemark4index() != 0) {
			cwbOrder.setRemark4(order.getRemark4() == null ? "" : order.getRemark4());
		}
		if (excelColumnSet.getRemark5index() != 0) {
			cwbOrder.setRemark5(order.getRemark5() == null ? "" : order.getRemark5());
		}

		if (excelColumnSet.getCwbordertypeidindex() != 0) {
			cwbOrder.setCwbordertypeid((String.valueOf(order.getCwbordertypeid())) == null ? 1 : order.getCwbordertypeid());
		}

		if (excelColumnSet.getShouldfareindex() != 0) {
			BigDecimal b = new BigDecimal(0);
			cwbOrder.setShouldfare(String.valueOf(order.getShouldfare()) == null ? b : order.getShouldfare());
		}
		if (excelColumnSet.getMpsallarrivedflagindex() != 0) {
			cwbOrder.setMpsallarrivedflag(Integer.valueOf(order.getMpsallarrivedflag())==null ? 0 : order.getMpsallarrivedflag());
		}
		if (excelColumnSet.getIsmpsflagindex() != 0) {
			cwbOrder.setIsmpsflag(Integer.valueOf(order.getIsmpsflag()) == null ? 0 : order.getIsmpsflag());
		}
		//团购标识
		if (excelColumnSet.getVipclubindex() != 0) {
			cwbOrder.setVipclub(Integer.valueOf(order.getVipclub()));
		}
		cwbOrder.setDefaultCargoName();

		return cwbOrder;
	}
	
	//集单逻辑
	public void  mpsallPackage(VipShop vipshop, String cust_order_no,
			int is_gatherpack, int is_gathercomp, String pack_nos,
			int total_pack, CwbOrderDTO cwbOrderDTO,int mpsswitch,
			MQCwbOrderDTO orderDTO,String order_batch_no) {
		if(mpsswitch==MpsswitchTypeEnum.WeiKaiQiJiDan.getValue()){
			return;
		}
		if(is_gatherpack!=1){ //是否集包模式
			return;
		}
		//订单不存在，则不需要处理
		if(cwbOrderDTO==null){
			return;
		}
		
		/**更新发货时间
		 * 产品层面要改成的是这样的：
		 * 如果TMS推过来的数据有最后一件标志了就把发货时间写入订单表里面&运单表，
		 * 如果TMS推过来的数据没有最后一件标志那就把发货时间写到运单表里面
		 */
		String emaildate = orderDTO.getRemark2(); //paraMap.get("remark2"); //发货时间
		String[] arrTranscwb = pack_nos.split(",");
		if(is_gatherpack==1 && (arrTranscwb.length==total_pack)){
			//更新临时表的发货时间
			dataImportDAO_B2c.update_CwbDetailTempEmaildateByCwb(cust_order_no, emaildate);
			
			//把发货时间写入订单表
			cwbDAO.updateEmaildate(cust_order_no, emaildate);
			
			//把发货时间写入运单表
			if(arrTranscwb != null && arrTranscwb.length > 0){
				dataImportService.updateEmaildate(Arrays.asList(arrTranscwb), emaildate);
			}
		}else if(is_gatherpack==1 && arrTranscwb.length!=total_pack){
			//把发货时间写入运单表
			if(arrTranscwb != null && arrTranscwb.length > 0){
				dataImportService.updateEmaildate(Arrays.asList(arrTranscwb), emaildate);
			}
		}
		//更新发货时间结束
		
		//一票多件，并且到齐了，排重returen
		if(cwbOrderDTO!=null&&cwbOrderDTO.getMpsallarrivedflag()==MPSAllArrivedFlagEnum.YES.getValue()){
			return;
		}
		
		/**
		     兼容以下情况，故不做拦截而是改为一票一件：
	            先产生is_gatherpack=1,is_gathercomp=0,total_pack=1的订单数据。
	           后面又产生一条is_gatherpack=1,is_gathercomp=1,total_pack=1的订单数据
	   **/
		if(is_gatherpack==1&&is_gathercomp==1&&total_pack==1){
			dataImportDAO_B2c.updateTmsPackageCondition(cust_order_no, pack_nos, Integer.valueOf(total_pack), MPSAllArrivedFlagEnum.NO.getValue(), MpsTypeEnum.PuTong.getValue());
			if(cwbOrderDTO.getGetDataFlag() != 0){
				cwbDAO.updateTmsPackageCondition(cust_order_no, pack_nos, Integer.valueOf(total_pack), MPSAllArrivedFlagEnum.NO.getValue(),MpsTypeEnum.PuTong.getValue());
			}
			return;
		}
		
		/*
		 * 由于tps下发dmp数据顺序可能错乱，在is_gathercomp标志为1（获取已集齐）之前，total_pack值都为0，
		 * 在is_gathercomp标志为1及之后，total_pack值都为订单总箱数
		 */
		int mpsallarrivedflag=0;
		if(arrTranscwb.length==total_pack){ //到齐
			mpsallarrivedflag=MPSAllArrivedFlagEnum.YES.getValue();
		}
		
		String oldTranscwb = cwbOrderDTO.getTranscwb();
		String currentTranscwb = orderDTO.getTranscwb();
		//后者大于前者，覆盖前者
		if(oldTranscwb.split(",").length<currentTranscwb.split(",").length){
			this.changeInfoOfOrder(cwbOrderDTO,cust_order_no,pack_nos,
					orderDTO.getSendcargonum(),mpsallarrivedflag,orderDTO.getRemark1());
		}
		//后者==前者，移除不是最后一箱的
		if(oldTranscwb.split(",").length==currentTranscwb.split(",").length){
			if((Integer.valueOf(orderDTO.getMpsallarrivedflag())==VipGathercompEnum.Default.getValue()
					&&Integer.valueOf(cwbOrderDTO.getMpsallarrivedflag())==VipGathercompEnum.Default.getValue())
					||Integer.valueOf(orderDTO.getMpsallarrivedflag())==VipGathercompEnum.Last.getValue()){
				this.changeInfoOfOrder(cwbOrderDTO,cust_order_no,pack_nos,
						orderDTO.getSendcargonum(),mpsallarrivedflag,orderDTO.getRemark1());
			}
		}
		//后者小于前者，不做修改
		if(oldTranscwb.split(",").length>currentTranscwb.split(",").length){
			return;
		}
	}
	
	//更行临时表或者订单表集单相关信息
	private void changeInfoOfOrder(CwbOrderDTO cwbOrderDTO,String cust_order_no,String pack_nos,
			int total_pack,int mpsallarrivedflag,String remark1){
		long b2cTempOpscwbid = cwbOrderDTO.getOpscwbid();
		dataImportDAO_B2c.updateTmsCollectionInfo(b2cTempOpscwbid, pack_nos, Integer.valueOf(total_pack), mpsallarrivedflag, IsmpsflagEnum.yes.getValue(),remark1);
		dataImportDAO_B2c.updateCwbDetailTempByCwb(0, b2cTempOpscwbid,remark1);
	}
	
	public int choseIsmpsflag(int is_gatherpack,int is_gathercomp,int sendcarnum,int mpsswitch) {
		int ismpsflag=IsmpsflagEnum.no.getValue(); //'是否一票多件(集包模式)：0默认；1是一票多件'; 
		
		//开启集单，并且运单为多个，则默认为一票多件
		if(mpsswitch!=MpsswitchTypeEnum.WeiKaiQiJiDan.getValue()){
			
			//拦截 开启集单模式，总件数只有一件的数据 is_gatherpack=1，is_gathercomp=1，total_pack=1  --->这就是个单包裹(罗冬确认)
			if(is_gatherpack==1&&is_gathercomp==1&&sendcarnum==1){
				return IsmpsflagEnum.no.getValue();
			}
			if(is_gatherpack==1&&Long.valueOf(sendcarnum)>1){
				return IsmpsflagEnum.yes.getValue();
			}
			if(is_gatherpack==1&&is_gathercomp==0){
				return IsmpsflagEnum.yes.getValue();
			}
			
		}
		return ismpsflag;
	}
	
	public Integer choseMspallarrivedflag(int is_gathercomp,int is_gatherpack,int sendcarnum,int mpsswitch) {
			
		if(mpsswitch!=MpsswitchTypeEnum.WeiKaiQiJiDan.getValue()){
			//集包并且是最后一箱并且箱号大于1
			if(is_gatherpack==1&&is_gathercomp==1&&Long.valueOf(sendcarnum)>1){ 
				return VipGathercompEnum.Last.getValue();
			}
		}
		return VipGathercompEnum.Default.getValue();
	}
	
	/**
	 * 提供数据导入接口-对接用到
	 * @param customerid
	 * @param branchid
	 * @param ed
	 * @param b2cFlag
	 * @param xmlList
	 * @return
	 */
	public void Analizy_DataDealByB2c(long customerid, String b2cFlag, MQCwbOrderDTO cwbOrder, long warehouse_id, // 对接设置中传过来的ID
			EmailDate ed) throws Exception {
		
		cwbOrder.setStartbranchid(warehouse_id);
		ExcelColumnSet excelColumnSet = cwbColumnSetTPS.getTPSOrderColumnSetByB2c(b2cFlag);
		CwbOrderDTO cwbOrderDTO = getCwbOrderAccordingtoConf(excelColumnSet, cwbOrder, warehouse_id);
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouse_id);

		 // 导入临时表，然后定时器处理
		try {
			String remark1 = (cwbOrder.getRemark1()==null?"":cwbOrder.getRemark1().trim());
			if(remark1.length() > 100){
				remark1 = remark1.substring(0, 100);
			}
			cwbOrder.setRemark1(remark1);
			
			if (b2cFlag.contains("tps")) {
				List<CwbOrderValidator> vailidators = importValidationManager.getVailidators(excelColumnSet);
				for (CwbOrderValidator cwbOrderValidator : vailidators) {
					cwbOrderValidator.validate(cwbOrderDTO);
				}
			}
			//接口数据插入临时表
			dataImportDAO_B2c.insertCwbOrderToTempTable(cwbOrder, customerid, warehouse_id, user, ed);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(b2cFlag + "tps订单数据插入临时表发生未知异常cwb=" + cwbOrder.getCwb(), e);
			Exception ex=null;
			long flowordertye=FlowOrderTypeEnum.DaoRuShuJu.getValue();
			if(e instanceof CwbException){
				flowordertye=((CwbException)e).getFlowordertye();
				ex=e;
			}else{
				ex=new CwbException(cwbOrder.getCwb(),flowordertye,e.getMessage());
			}
			throw ex;
		}
	}

	public String toDateForm(Long data){
		String dataStr="";
		if (0 == data) {
			data = System.currentTimeMillis();
		}
		if(data!=null){
			Timestamp scurrtest = new Timestamp(data);
			dataStr = String.valueOf(scurrtest).substring(0,19);// 简易最迟配送时间
		}
		return dataStr;
	}
}
