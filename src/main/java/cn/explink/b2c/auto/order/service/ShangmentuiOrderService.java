package cn.explink.b2c.auto.order.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.auto.order.dao.ExpressOrderDao;
import cn.explink.b2c.auto.order.dao.TPSOrderDao;
import cn.explink.b2c.auto.order.util.MqOrderBusinessUtil;
import cn.explink.b2c.auto.order.vo.InfDmpOrderSendDetailVO;
import cn.explink.b2c.auto.order.vo.InfDmpOrderSendVO;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.controller.MQCwbOrderDTO;
import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.OrderGoods;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

/**
 * 配送单处理
 */
@Service
public class ShangmentuiOrderService {
	@Autowired
	MQGetOrderDataService mQGetOrderDataService;
	@Autowired
	ExpressOrderDao expressOrderDao;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	TPSOrderDao tPSOrderDao;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	OrderGoodsDAO orderGoodsDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;
	
	private Logger logger = LoggerFactory.getLogger(ShangmentuiOrderService.class);
	//上门退订单数据解析
	public MQCwbOrderDTO ShangmentuiJsonDetailInfo(VipShop vipshop, InfDmpOrderSendVO order, int mpsswitch) {
		MQCwbOrderDTO orderDTO = new MQCwbOrderDTO();
		String cwb = null;
		try {
			cwb = order.getCustOrderNo();//订单号
			if(StringUtil.isEmpty(cwb)){
				this.logger.info("订单号为空");
				throw new CwbException(order.getCustOrderNo(),FlowOrderTypeEnum.DaoRuShuJu.getValue(),"订单号为空");
			}
			//用于判断是否为乐峰订单（乐峰客户）：1-乐峰订单,2-海淘订单,3-OXO订单,4-普通订单
			int do_type = order.getDoType()==null?999 : order.getDoType();
			if(vipshop.getIsOpenLefengflag()==1){//开启只接收乐峰网订单
				if(do_type != 1){
					this.logger.info("开启只接收乐峰网订单开关,但非乐峰订单cwb={}则返回null!", cwb);
					return null;
				}
			}
			orderDTO.setDoType(do_type);//订单类型
			String tpsTranscwbNo = order.getTransportNo();//tps运单号
			orderDTO.setTpsTranscwb(tpsTranscwbNo);
			
			orderDTO.setCwb(cwb);
			orderDTO.setCwbprovince(order.getCnorProv());//寄件人省份
			orderDTO.setCwbcity(order.getCnorCity());//寄件人城市
			orderDTO.setCwbcounty(order.getCnorRegion());//寄件人区
			orderDTO.setConsigneename(order.getCnorContacts());//寄件人
			orderDTO.setConsigneeaddress(order.getCnorAddr());//寄件人地址
			orderDTO.setConsigneephone(order.getCnorTel());//寄件人电话
			orderDTO.setConsigneemobile(order.getCnorMobile());//寄件人手机
			orderDTO.setConsigneepostcode(order.getPostCode());//寄件人邮编
			String warehouseAddr = order.getWarehouseAddr();//仓库地址，拼接到remark5
			//重量、体积自动化订单时设置为0，其余取上游数据
			int isAutoInterface = vipshop.getIsAutoInterface();//是否自动化接口
			BigDecimal original_weight = BigDecimal.ZERO;
			BigDecimal original_volume = BigDecimal.ZERO;
			if(isAutoInterface!=1){
				original_weight = new BigDecimal(order.getOriginalWeight());
				original_volume = new BigDecimal(order.getOriginalVolume());
			}
			orderDTO.setCargorealweight(original_weight);// 重量
			orderDTO.setCargovolume(original_volume);//体积
			orderDTO.setCargoamount((null==order.getOrderSum()||"".equals(order.getOrderSum().toString())) ? BigDecimal.ZERO : new BigDecimal(order.getOrderSum()));//货物金额
			orderDTO.setReceivablefee(new BigDecimal(order.getCodAmount()));
			orderDTO.setPaybackfee(new BigDecimal(order.getReturnCredit()));//应退金额
			orderDTO.setShouldfare(order.getFreight()==null?BigDecimal.ZERO:new BigDecimal(order.getFreight()));//运费
			String required_time = order.getRequiredTime();//要求提货时间，customercommand组合之一
			orderDTO.setAnnouncedvalue(new BigDecimal(order.getValuationValue()));//保价价值
			orderDTO.setPaywayid(MqOrderBusinessUtil.getPayTypeValue(order.getPayment()));//支付方式
			orderDTO.setNewpaywayid(MqOrderBusinessUtil.getPayTypeValue(order.getPayment())+"");
			//orderDTO.setTranscwb(order.getVip().getBoxNo());//运单号
			//调试的时候注意时间格式是否符合要求
			String add_time = mQGetOrderDataService.toDateForm(order.getVip().getAddTime());// 出仓时间
			orderDTO.setVipclub(Integer.parseInt(order.getVip().getVipClub()));//团购标识
			String order_delivery_batch = order.getVip().getOrderDeliveryBatch(); // 1（默认）-一配订单：2-二配订单
			if ("1".equals(order_delivery_batch)) {
				order_delivery_batch = "一配订单";
			} else if ("2".equals(order_delivery_batch)) {
				order_delivery_batch = "二配订单";
			} else {
				order_delivery_batch = "普通订单";
			}
			// 服务类型：1.B2C， 2.仓配服务，3.配送服务
			/*String service_type = order.getVip().getServiceType().toString();
			String cargotype = "";
			if ("1".equals(service_type)) {
				cargotype = "B2C";
			} else if ("2".equals(service_type)) {
				cargotype = "仓配服务";
			} else if ("3".equals(service_type)) {
				cargotype = "配送服务";
			}
			orderDTO.setCargotype(cargotype);*/
			//String createdTime = mQGetOrderDataService.toDateForm(order.getVip().getCreateTime());//记录生成时间
			//orderDTO.setRemark2(vipshop.getIsCreateTimeToEmaildateFlag()==1?add_time:createdTime);// 如果开启生成批次，则remark2是出仓时间，否则是订单生成时间
			//String order_batch_no = order.getVip().getOrderBatchNo();//交接单号
			//orderDTO.setRemark1(order_batch_no);
			String attemper_no = order.getVip().getAttemperNo();//托运单号
			orderDTO.setRemark3("托运单号:" + attemper_no);
			String attemper_no_create_time = mQGetOrderDataService.toDateForm(order.getVip().getAttemperNoCreateTime());
			if ((attemper_no_create_time == null) || attemper_no_create_time.isEmpty()) {
				attemper_no_create_time = DateTimeUtil.getNowDate() + " 00:00:00";
			}
			orderDTO.setRemark4(attemper_no_create_time);
			int is_gatherpack = order.getVip().getIsGatherPack(); //1：表示此订单需要承运商站点集包 0：表示唯品会仓库整单出仓
			int is_gathercomp = order.getVip().getIsGatherComp();//最后一箱:1最后一箱 ，0默认 
			
			
			
			
			Integer total_pack = order.getTotalPack(); // 新增箱数
			int sendcarnum = order.getTotalPack();
			
			int cwbordertype = CwbOrderTypeIdEnum.Shangmentui.getValue();
			
			
			String customer_name = order.getCustomerName();
			String customerid=vipshop.getCustomerids();  //默认选择唯品会customerid
			if(do_type == 1){
				customerid = (vipshop.getLefengCustomerid()==null || vipshop.getLefengCustomerid().isEmpty()?vipshop.getCustomerids() : vipshop.getLefengCustomerid());
			}
			//发货件数
			/*if(isAutoInterface==1){
				if(is_gatherpack==1 && total_pack!=null && total_pack==0){
					int transcwbLength = order.getVip().getBoxNo().split(",").length;
					orderDTO.setSendcargonum(transcwbLength);
				}else{
					orderDTO.setSendcargonum(total_pack.toString().isEmpty() ? 1 : total_pack);
				}
			}else{
				orderDTO.setSendcargonum(total_pack==0 ? 1:total_pack);
			}*/
			orderDTO.setCustomercommand("送货时间要求:" + required_time + ",订单配送批次:" + order_delivery_batch );
			orderDTO.setSendcargoname("[发出商品]");
			orderDTO.setCustomerid(Integer.parseInt(customerid));
			
			orderDTO.setRemark5(customer_name+"/"+warehouseAddr); // 仓库地址
			orderDTO.setCwbordertypeid(cwbordertype);
			
			orderDTO.setExcelbranch(orderDTO.getExcelbranch()==null?"":orderDTO.getExcelbranch());//站点
			orderDTO.setRemark1(orderDTO.getRemark1()==null?"":orderDTO.getRemark1());
			orderDTO.setRemark3(orderDTO.getRemark3()==null?"":orderDTO.getRemark3());
			orderDTO.setRemark4(orderDTO.getRemark4()==null?"":orderDTO.getRemark4());
			orderDTO.setIsaudit(orderDTO.getIsaudit());
			
			orderDTO.setIsmpsflag(mQGetOrderDataService.choseIsmpsflag(is_gatherpack,is_gathercomp,sendcarnum,mpsswitch));
			orderDTO.setMpsallarrivedflag(mQGetOrderDataService.choseMspallarrivedflag(is_gathercomp,is_gatherpack,sendcarnum,mpsswitch));
			
			CwbOrderDTO cwbOrderDTO = dataImportDAO_B2c.getCwbB2ctempByCwb(cwb);
			//集包相关代码处理
			//mQGetOrderDataService.mpsallPackage(vipshop, cwb, is_gatherpack, is_gathercomp,order.getVip().getBoxNo(), total_pack, cwbOrderDTO,mpsswitch,orderDTO);
			
			String cmd_type = order.getCmdType(); // 操作指令new
			
			//修改
			if ("090".equalsIgnoreCase(cmd_type)) {
				//修改订单表
				this.tPSOrderDao.updateBycwb(orderDTO);
				//修改临时表
				this.tPSOrderDao.updateTempBycwb(orderDTO);
				return null;
			}else if ("023".equalsIgnoreCase(cmd_type)) {// 订单取消
				
				if(vipshop.getCancelOrIntercept()==0){ //取消
					//cust_order_no订单号，根据订单号失效临时中订单数据
					this.dataImportDAO_B2c.dataLoseB2ctempByCwb(cwb);
					// 根据订单号失效订单表中对应订单数据
					this.cwbDAO.dataLoseByCwb(cwb);
					//根据订单号，删除对应商品表中商品数据
					orderGoodsDAO.loseOrderGoods(cwb);
					//处理订单失效相关信息
					cwbOrderService.datalose_vipshop(cwb);
					// add by bruce shangguan 20160608  报障编号:1729 ,揽退成功之后失效的订单在运费交款存在
					this.accountCwbFareDetailDAO.deleteAccountCwbFareDetailByCwb(cwb) ;
					// end 20160608  报障编号:1729
				}else{ //拦截
					//cwbOrderService.auditToTuihuo(userDAO.getAllUserByid(1), order_sn, order_sn, FlowOrderTypeEnum.DingDanLanJie.getValue(),1);
					cwbOrderService.tuihuoHandleVipshop(userDAO.getAllUserByid(1), cwb, cwb,0);
				}
				return null;
			}else if ("003".equalsIgnoreCase(cmd_type)) {//新增
				// 插入商品列表,try防止异常
				this.insertOrderGoods(order.getDetails(), cwb);
			}
			if (cwbOrderDTO != null ) {
				if(is_gatherpack==0){
					this.logger.info("获取唯品会订单有重复,已过滤...cwb={}", cwb);
					return null;
				//集单模式校验重复
				}else if(is_gatherpack==1){
					return null;
				}
			}
			if ("".equals(cwb)) { // 若订单号为空，则继续。
				this.logger.info("获取订单信息为空");
				return null;
			}
			this.logger.info("TPS订单下发接口，订单号：cwb={}", cwb);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("TPS订单下发处理转订单对象异常,cwb=" + cwb, e);
			CwbException ex=null;
			long flowordertye=FlowOrderTypeEnum.DaoRuShuJu.getValue();
			if(e instanceof CwbException){
				flowordertye=((CwbException)e).getFlowordertye();
				ex=(CwbException)e;
			}else{
				ex=new CwbException(order.getCustOrderNo(),flowordertye,e.getMessage());
			}
			throw ex;
		}
		return orderDTO;
	}
	
	
	//将接收的货物信息存入商品表
	private void insertOrderGoods(List<InfDmpOrderSendDetailVO> goodslist, String cwb) {
		try {
			if ((goodslist != null) && (goodslist.size() > 0)) {
				List<OrderGoods> orderGoodsList = null;
				orderGoodsList = orderGoodsDAO.getOrderGoodsList(cwb);
				if(orderGoodsList.size()!=0){
					return;
				}
				for (InfDmpOrderSendDetailVO good : goodslist) {
					OrderGoods ordergoods = new OrderGoods();
					ordergoods.setCwb(cwb);
					ordergoods.setCretime(DateTimeUtil.getNowTime());
					ordergoods.setGoods_brand(good.getGoodsBrand());
					ordergoods.setGoods_code(good.getGoodsCode());
					ordergoods.setGoods_name(good.getGoodsName());
					ordergoods.setGoods_num(good.getGoodsNum().toString());
					ordergoods.setGoods_pic_url(good.getGoodsPicUrl());
					ordergoods.setGoods_spec(good.getGoodsSize());
					ordergoods.setReturn_reason(good.getRemark());
					ordergoods.setCretime(DateTimeUtil.getNowTime());
					this.orderGoodsDAO.CreateOrderGoods(ordergoods);

				}
			}
		} catch (Exception e) {
			this.logger.error("获取商品列表异常,单号=" + cwb, e);
		}
	}
	
}
