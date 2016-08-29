package cn.explink.b2c.emsSmallPakage;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.explink.b2c.ems.EMSDAO;
import cn.explink.b2c.ems.SendToEMSOrder;
import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbTranslator;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;


@Service
public class EmsSmallPakageService {
	private Logger logger = LoggerFactory.getLogger(EmsSmallPakageService.class);
	@Autowired
	EMSDAO eMSDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	TranscwbOrderFlowDAO transcwbOrderFlowDao;
	@Autowired
	List<CwbTranslator> cwbTranslators;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CustomerDAO customerDao;
	
	/*
	 * @author zhouhuan
	 * @param order:订单信息，transcwb:dmp运单号,emailno:邮政运单号
	 * @remark:保存需要发送给邮政小包的订单信息
	 * @add time 2016-07-20
	 */
	public void  saveOrderForEmsSmallPakage(CwbOrder order,String transcwb,String emailno)throws Exception{
	
		try {
			if(order==null){
				this.logger.info("dmp订单号不存在，订单号为：{}",order.getCwb());
				return;
			}
			//校验订单是否重复插入
			int resaveFlag = validateOrderResave(order.getCwb());
			
			if(resaveFlag==1){
				this.logger.info("邮政小包订单下发接口临时表中已存在该订单对应的数据：订单号为：{}",order.getCwb());
				return;
			}
			
			this.logger.info("保存EMS订单消息开始：" + System.currentTimeMillis());
			int addTranscwbFlag = 0; 
			//处理订单信息，将dmp订单信息解析成发送给邮政小包的数据格式
			String data = getStringToEMS(order,transcwb,emailno);
			if(StringUtil.isEmpty(data)){
				return;
			}
			List<SendToEMSOrder> oldOrder = eMSDAO.getSendOrderByNo(transcwb,emailno);
			if(oldOrder.size()!=0){
				this.logger.info("发送给邮政小包的对应数据在接口临时表中已存在，运单号为：{}",transcwb);
				return;
			}
			String credate = Tools.getCurrentTime("yyyy-MM-dd HH:mm:ss");
			//每个运单号对应临时表中一条记录
			eMSDAO.saveOrderInfo(order.getCwb(),transcwb,credate,addTranscwbFlag,data,0l);
			
		} catch (Exception e1) {
			this.logger.error("error while handle orderflow", e1);
			throw e1;
		}
		this.logger.info("保存邮政小包订单消息结束：" + System.currentTimeMillis());
	}
	
	/*
	 * @author zhouhuan
	 * @param cwb:订单号
	 * @remark:校验订单是否重复插入
	 * @add time 2016-07-20
	 */
	public int validateOrderResave(String cwb){
		int flag = 0;
		List<SendToEMSOrder> list = eMSDAO.getOrderInfoByTranscwb(cwb);
		if(list.size()!=0){
			flag=1;
		}
		return flag;
	}
	
	/*
	 * @author zhouhuan
	 * @param cwb:订单号
	 * @remark:将dmp订单信息解析为发送的xml格式
	 * @add time 2016-07-20
	 */
	//
	public static String getStringToEMS(CwbOrder order,String transcwb,String mailno) throws JAXBException {
		UserInfo sender = new UserInfo();
		UserInfo receiver = new UserInfo();
		RequestOrder orderInfo = new RequestOrder();
		@SuppressWarnings("rawtypes")
		List<Item> items = new ArrayList();
		Item item = new Item();
		item.setItemName("办公用品");
		item.setNumber(1);
		item.setItemValue(0);
		items.add(item);
		sender.setName("徐得谱");
		sender.setPostCode("430000");
		sender.setPhone("027-82668066");
		receiver.setName(order.getConsigneename());
		receiver.setPostCode(order.getConsigneepostcode());
		receiver.setMobile(order.getConsigneemobile());
		receiver.setPhone(order.getConsigneephone());
		receiver.setProv(order.getCwbprovince());
		receiver.setCity(order.getCwbcity());
		receiver.setAddress(order.getConsigneeaddress());
		orderInfo.setEcCompanyId("PINJUN");
		orderInfo.setLogisticProviderID("2016051839597");
		orderInfo.setCustomerId(order.getCustomerid()+"");
		orderInfo.setTxLogisticID(order.getTranscwb());
		orderInfo.setTradeNo("259");
		orderInfo.setMailNo(mailno);
		orderInfo.setTotalServiceFee(0l);
		orderInfo.setCodSplitFee(0l);
		orderInfo.setBuyServiceFee(0l);
		orderInfo.setOrderType(1);
		orderInfo.setServiceType(0);
		orderInfo.setSender(sender);
		orderInfo.setReceiver(receiver);
		if(order.getAnnouncedvalue()!=null && order.getAnnouncedvalue()!=BigDecimal.ZERO){
			orderInfo.setGoodsValue(new Double(order.getAnnouncedvalue().doubleValue()*100).longValue());
		}else{
			orderInfo.setGoodsValue(0l);
		}
		orderInfo.setItems(items);
		orderInfo.setSpecial(0);
	    StringWriter sw = new StringWriter();
        JAXBContext jAXBContext = JAXBContext.newInstance(orderInfo.getClass());
        Marshaller marshaller = jAXBContext.createMarshaller();
        marshaller.marshal(orderInfo, sw);
		return sw.toString();
	}
	
	public static void main(String[] args){
		CwbOrder order = new CwbOrder();
		order.setAnnouncedvalue(new BigDecimal(12.1));
		order.setConsigneename("xiaoh");
		try {
			getStringToEMS(order,"12","45");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	
	public CwbOrder getCwbByCwb(String cwb) {
		return this.cwbDAO.getCwbByCwb(cwb);
	}
	
	public String translateCwb(String cwb) {
		for (CwbTranslator cwbTranslator : this.cwbTranslators) {
			String translateCwb = cwbTranslator.translate(cwb);
			if (StringUtils.hasLength(translateCwb)) {
				cwb = translateCwb;
			}
		}
		return cwb;
	}

	/**
	 * 根据订单查询订单邮政运单关系集合   add by vic.liang@pjbest.com 2016-07-26
	 * @param cwb
	 * @return
	 */
	public List<SendToEMSOrder> getTransListByCwb(String scancwb, String cwb) {
		if (scancwb.equals(cwb)) {//订单
			return this.eMSDAO.getTransListByCwb(cwb);
		} else {//运单
			return this.eMSDAO.getTransListByTransCwb(scancwb);
		}
	} 

	/**
	 * 订单/运单号绑定邮政运单号   add by vic.liang@pjbest.com 2016-07-25
	 * @param cwb 订单号
	 * @param transcwb 扫描号码
	 * @param emscwb 扫描ems运单号
	 */
	public void bingCwb(User user, String cwb, String scancwb, String emscwb) {
		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
		String bingTime = DateTimeUtil.getNowTime();
		if (cwbOrder.getSendcarnum() > 1) { //一票多件扫描运单号检验
			if (cwb.equals(scancwb)) {//一票多件扫订单号
				List<SendToEMSOrder> transcwbs = this.eMSDAO.getTransListByCwb(cwb);
				if (transcwbs.size() >= cwbOrder.getSendcarnum())
					throw new CwbException(scancwb,
							ExceptionCwbErrorTypeEnum.DING_DAN_BANG_DING_SHU_CHAO_CHU);
				else 
					saveTransAndCreFlow(user, cwbOrder, cwb, cwb + "-" + (transcwbs.size() + 1), emscwb, bingTime);// 订单运单表运单不能为空，一票多件扫订单号生成运单号，规则为订单号+"-i"
			} else {
				saveTransAndCreFlow(user, cwbOrder, cwb, scancwb, emscwb, bingTime);//一票多件 扫描运单号
			}
		} else { 
			saveTransAndCreFlow(user, cwbOrder, cwb, scancwb, emscwb, bingTime);//一票一件
		}
	}
	
	/**
	 * 绑定邮政小包运单号和创建操作流程 add by vic.liang@pjbest.com 2016-08-20
	 * @param user
	 * @param cwbOrder
	 * @param cwb
	 * @param scancwb
	 * @param emscwb
	 * @param bingTime
	 */
	private void saveTransAndCreFlow (User user, CwbOrder cwbOrder, String cwb, String scancwb, String emscwb, String bingTime) {
		this.eMSDAO.saveEMSEmailnoAndDMPTranscwb(cwb, scancwb, emscwb, bingTime);
		long count = this.orderFlowDAO.getFlowCount(cwb, FlowOrderTypeEnum.BingEmsTrans.getValue(), user.getBranchid());
		if (count == 0) {
			this.cwbOrderService.createFloworder(user, user.getBranchid(), cwbOrder, FlowOrderTypeEnum.BingEmsTrans, "", System.currentTimeMillis());
		}
	}
	
	/**
	 * 更新订单/运单号绑定邮政运单号   add by vic.liang@pjbest.com 2016-07-25
	 * @param cwb
	 * @param transcwb
	 * @param emscwb
	 */
	public void rebingCwb(String transcwb, String scanems) {
		String bingTime = DateTimeUtil.getNowTime();
		this.eMSDAO.updateEmscwb(transcwb, scanems, bingTime);
	}
	
	/**
	 * 查询已经绑定邮政运单关系的订单   add by vic.liang@pjbest.com 2016-07-26
	 * @param cwb
	 * @return
	 */
	public DataGridReturn getEMSViewListByTransCwb(int page,int pageSize,String cwbType,String cwb, String starttime,String endtime,String status) {
		List<EmsSmallPackageViewVo> rows = this.eMSDAO.getEMSViewListByTransCwb(page,pageSize,cwbType,cwb,starttime,endtime,status);
		int total = this.eMSDAO.getCountEMSViewListByTransCwb(cwbType, cwb, starttime, endtime, status);
				
	    DataGridReturn dg = new DataGridReturn();
		dg.setRows(rows);
		dg.setTotal(total);
		
		return dg;
	}
	
	/**
	 * 查询已经绑定邮政运单关系的订单   add by vic.liang@pjbest.com 2016-07-26
	 * @param cwb
	 * @return
	 */
	public List<EmsSmallPackageViewVo> getEMSViewListByTransCwb(String cwbType,String cwb, String starttime,String endtime,String status) {
		List<EmsSmallPackageViewVo> rows = this.eMSDAO.getEMSViewListByTransCwb(cwbType,cwb,starttime,endtime,status);
		return rows;
	}
	
	/**
	 * 检验所输入的订单/运单是否符合绑定邮政小包 add by vic.liang@pjbest.com 2016-07-26
	 */
	public CwbOrder validateCwb (String scancwb) {
		CwbOrder cwbOrder = this.getCwbByCwb(scancwb);
		if (cwbOrder == null) {
			cwbOrder = this.getCwbByCwb(this.translateCwb(scancwb));
		}
		if (cwbOrder == null) {
			throw new CwbException(scancwb, ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		/**
		 * a. 配送站点是邮政站点 b. 订单类型是正常配送订单 c. 代收货款、运费为0 d. 订单已全部出库
		 */
		if (cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Peisong
				.getValue()) {// 订单是否为配送单
			throw new CwbException(scancwb,
					ExceptionCwbErrorTypeEnum.FEI_PEI_SONG_BANG_DING_EMS_TRANS);
		}
		if (cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) == 1) { // 代收货款是否大于0
			throw new CwbException(scancwb,
					ExceptionCwbErrorTypeEnum.DAI_SHOU_HUO_KUAN_DA_LING);
		}
		if (cwbOrder.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) { // 集单模式不发送给邮政
			throw new CwbException(scancwb,
					ExceptionCwbErrorTypeEnum.JI_DAN_BU_FA_SONG_YOU_ZHENG);
		}
		if (cwbOrder.getFlowordertype() != FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) { //订单当前操作状态校验
			throw new CwbException(scancwb,
					ExceptionCwbErrorTypeEnum.DING_DAN_ZHUANG_TAI_BU_SHI_CHU_KU);
		}
		
		Customer customer = customerDao.getCustomerById(cwbOrder.getCustomerid());
		if (cwbOrder.getSendcarnum() > 1 
				&& customer.getIsypdjusetranscwb() == 1 
				&& customer.getIsUsetranscwb() == 0) {// 一票多件  运单当前操作状态校验
			int transCount = transcwbOrderFlowDao
					.getTransScanTimeByCwbFlowordertype(cwbOrder.getCwb(),
							FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
			if (cwbOrder.getSendcarnum() != transCount) {
				throw new CwbException(scancwb,
						ExceptionCwbErrorTypeEnum.DING_DAN_ZHUANG_TAI_BU_SHI_CHU_KU);
			}
		} 
		
		int deliveryBranchid = -1;
		SystemInstall systemInstall = this.systemInstallDAO.getSystemInstall("emsbranchid");
		if (systemInstall != null) {
			try {
				deliveryBranchid = Integer.valueOf(systemInstall.getValue());
			} catch (Exception e) {
				deliveryBranchid = -1;
			}
		}
		if (cwbOrder.getDeliverybranchid() != deliveryBranchid) {//配送站点校验
			throw new CwbException(scancwb,
					ExceptionCwbErrorTypeEnum.BU_SHI_GAI_PEI_SONG_ZHAN_EMS_CWB);
		}
		if (cwbOrder.getSendcarnum() > 1) { //一票多件扫描运单号检验
			if (!StringUtil.isEmpty(cwbOrder.getTranscwb())) {
				boolean isScanTransCwb = false;
				String[] split = cwbOrder.getTranscwb().split(",");
				for (String string : split) {
					if (string.equals(scancwb)) {
						isScanTransCwb = true;
						break;
					}
				}
				if (!isScanTransCwb)
				throw new CwbException(scancwb,
						ExceptionCwbErrorTypeEnum.Qing_SAO_MIAO_YUN_DAN_HAO);
			} 
		}
		return cwbOrder;
	}
}
