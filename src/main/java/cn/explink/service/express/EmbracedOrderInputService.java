package cn.explink.service.express;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.CountyDAO;
import cn.explink.dao.express.ExpressOrderDao;
import cn.explink.dao.express.ExpressWeighDAO;
import cn.explink.dao.express.GeneralDAO;
import cn.explink.dao.express.PreOrderDao;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.dao.express.TownDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.VO.express.EmbracedImportOrderVO;
import cn.explink.domain.VO.express.EmbracedOrderVO;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.domain.express.ExpressPreOrder;
import cn.explink.domain.express.ExpressWeigh;
import cn.explink.domain.express.NewAreaForm;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.express.AddressMatchEnum;
import cn.explink.enumutil.express.ExcuteStateEnum;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.enumutil.express.ExpressOrderStatusEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;
import cn.explink.service.express2.ReserveOrderService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderCargoRequest;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderRequest;
import com.pjbest.deliveryorder.service.PjTransportFeedbackRequest;

@Transactional
@Service
public class EmbracedOrderInputService extends ExpressCommonService {
	@Autowired
	private CwbDAO cwbDao;
	@Autowired
	private TransCwbDao transCwbDao;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private ProvinceDAO provinceDAO;
	@Autowired
	private CityDAO cityDAO;
	@Autowired
	private CountyDAO countyDAO;
	@Autowired
	private TownDAO townDAO;
	@Autowired
	private PreOrderDao preOrderDao;
	@Autowired
	private ExpressOrderDao expressOrderDao;
	@Autowired
	private GeneralDAO generalDAO;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private BranchDAO branchDAO;
	@Autowired
	TpsInterfaceExecutor tpsInterfaceExecutor;
	@Autowired
	ExpressWeighDAO expressWeighDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	ReserveOrderService reserveOrderService;

	@SuppressWarnings("serial")
	Map<String, Integer> checkmMap = new HashMap<String, Integer>() {
		{
			this.put("OrderNo", 1);
			this.put("Sender_name", 2);
			this.put("Sender_companyName", 3);
			this.put("Monthly_account_number", 4);
			this.put("Sender_provinceName", 5);
			this.put("Sender_cityName", 6);
			this.put("Sender_countyName", 7);
			this.put("Sender_townName", 8);
			this.put("Sender_adress", 9);
			this.put("Sender_cellphone", 10);
			this.put("Sender_telephone", 11);
			this.put("Goods_name", 12);
			this.put("Goods_number", 13);
			this.put("Charge_weight", 14);
			this.put("Goods_length", 15);
			this.put("Goods_width", 16);
			this.put("Goods_high", 17);
			this.put("Goods_other", 18);
			this.put("Actual_weight", 19);
			this.put("Consignee_name", 20);
			this.put("Consignee_provinceName", 21);
			this.put("Consignee_cityName", 22);
			this.put("Consignee_countyName", 23);
			this.put("Consignee_townName", 24);
			this.put("Consignee_adress", 25);
			this.put("Consignee_cellphone", 26);
			this.put("Consignee_telephone", 27);
			this.put("DelivermanName", 28);
			this.put("Xianfu", 29);
			this.put("Daofu", 30);
			this.put("Yuejie", 31);
			this.put("Collection_amount", 32);
			this.put("Insured_amount", 33);
			this.put("Insured_cost", 34);
			this.put("Packing_amount", 35);
		}
	};

	/**
	 *
	 * @Title: getDeliveryManBybranchid
	 * @description 根据站点获取小件员
	 * @author 刘武强(复制俊哥的，只是把这个方法提到了service中)
	 * @date 2015年8月7日上午11:06:52
	 * @param @return
	 * @return List<User>
	 * @throws
	 */
	public List<User> getDeliveryManBybranchid() {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDAO.getUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());
		return uList;
	}

	/**
	 *
	 * @Title: getDeliveryManByOrderNO
	 * @description 通过运单号获取小件员
	 * @author 刘武强
	 * @date 2015年8月10日下午3:23:24
	 * @param @param orderNo
	 * @param @return
	 * @return ExpressPreOrder
	 * @throws
	 */
	public ExpressPreOrder getDeliveryManByOrderNO(String orderNo) {
		return this.preOrderDao.getDeliveryManByOrderNo(orderNo, ExcuteStateEnum.Succeed.getValue(), ExpressOrderStatusEnum.Normal.getValue());
	}

	/**
	 *
	 * @Title: getProvince
	 * @description 获取所有的省
	 * @author 刘武强
	 * @date 2015年8月7日下午2:13:23
	 * @param @return
	 * @return List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getProvince() {
		return this.provinceDAO.getProvince();
	}

	/**
	 *
	 * @Title: getNewProvince
	 * @description 获取新格式的省数据
	 * @author 刘武强
	 * @date 2015年9月28日上午10:30:37
	 * @param InnerOrOut
	 *            :"Inner":境内，”Out“:境外
	 * @param @return
	 * @return List<NewAreaForm>
	 * @throws
	 */
	public List<NewAreaForm> getNewProvince(String InnerOrOut) {
		if ("Inner".equals(InnerOrOut)) {
			return this.provinceDAO.getNewProvinceInner();
		} else {
			return this.provinceDAO.getNewProvinceOut();
		}
	}

	/**
	 *
	 * @Title: getNextAddress
	 * @description 根据上级地址，获取下级地址信息
	 * @author 刘武强
	 * @date 2015年9月28日下午1:01:19
	 * @param @param parentCode:上级地址的addressCode
	 * @param @return
	 * @return List<NewAreaForm>
	 * @throws
	 */
	public List<NewAreaForm> getNextAddress(String parentCode) {
		return this.provinceDAO.getNextAddress(parentCode);
	}

	/**
	 *
	 * @Title: getCityOfProvince
	 * @description 根据省code，查询该省下的所以市
	 * @author 刘武强
	 * @date 2015年8月8日上午10:47:36
	 * @param @param parentCode
	 * @param @return
	 * @return List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getCityOfProvince(String parentCode) {
		return this.cityDAO.getCityOfProvince(parentCode);
	}

	/**
	 *
	 * @Title: getCityOfProvince
	 * @description 根据省id，查询该省下的所以市
	 * @author 刘武强
	 * @date 2015年8月18日上午11:14:13
	 * @param @param parentId
	 * @param @return
	 * @return List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getCityOfProvince(Integer parentId) {
		return this.cityDAO.getCityOfProvince(parentId);
	}

	/**
	 *
	 * @Title: getCountyOfCity
	 * @description 根据市的code，获取该市的所有区/县
	 * @author 刘武强
	 * @date 2015年8月10日下午1:55:23
	 * @param @param parentCode
	 * @param @return
	 * @return List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getCountyOfCity(String parentCode) {
		return this.countyDAO.getCountyOfCity(parentCode);
	}

	/**
	 *
	 * @Title: getTownOfCounty
	 * @description 根据区/县的code，获取该区/县的所有街道
	 * @author 刘武强
	 * @date 2015年8月10日下午1:55:28
	 * @param @param parentCode
	 * @param @return
	 * @return List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getTownOfCounty(String parentCode) {
		return this.townDAO.getTownOfCounty(parentCode);
	}

	/**
	 *
	 * @Title: savaEmbracedOrderVO
	 * @description 往订单表里面保存揽件录入/揽件补录的数据
	 * @author 刘武强
	 * @date 2015年8月10日上午10:28:58
	 * @param @param embracedOrderVO * @param @param flags 0:揽件录入 1：揽件补录
	 * @param @return
	 * @return String
	 * @throws
	 */
	public synchronized String savaEmbracedOrderVO(EmbracedOrderVO embracedOrderVO, int flags) {
		// 判断运单号是否已经被录入过
		EmbracedOrderVO savedVO = this.judgeCwbOrderByCwb(embracedOrderVO.getOrderNo());
		if ((flags == 0 || flags == 2) && (savedVO != null)) { //如果flags=0或者2，那么会执行insert，但是如果数据库已经有数据了，就放弃insert ---刘武强20160811
			// TODO 从tps上校验运单号是否重复
			return flags == 0 ? "hasSaved" : "tpsErr";
		} else if ((savedVO != null) && "1".equals(savedVO.getIsadditionflag())) {
			return "hasSaved";
		}
		User user = this.getSessionUser();
		Map<String, Object> params = new HashMap<String, Object>();
		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		branch = branch == null ? new Branch() : branch;
		Date date = new Date();

		if (flags == 0) {
			this.logger.info("订单录入  cwb:" + embracedOrderVO.getOrderNo() + ";当前机构：" + this.getSessionUser().getBranchid() + ";当前时间：" + date);
			params.put("cwb", embracedOrderVO.getOrderNo());
			params.put("flowordertype", FlowOrderTypeEnum.LanJianRuZhan.getValue());
			params.put("cwbstate", CwbStateEnum.PeiShong.getValue());
			params.put("collectorid", StringUtils.isNotBlank(embracedOrderVO.getDelivermanId()) ? embracedOrderVO.getDelivermanId() : null);
			params.put("collectorname", StringUtils.isNotBlank(embracedOrderVO.getDelivermanName()) ? embracedOrderVO.getDelivermanName() : null);
			params.put("currentbranchid", this.getSessionUser().getBranchid());// 当前机构
			params.put("inputdatetime", date);
			embracedOrderVO.setInputdatetime(date.toString());
			params.put("cwbordertypeid", CwbOrderTypeIdEnum.Express.getValue());
			params.put("paymethod", -1);
			params.put("customerid", 1000);// 默认1000，代表快递单
			// 订单录入的时候，运费存到运费（shouldfare）和运费合计（totalfee）字段
			params.put("shouldfare", StringUtils.isNotBlank(embracedOrderVO.getFreight_total()) ? embracedOrderVO.getFreight_total() : 0.00);
			params.put("totalfee", StringUtils.isNotBlank(embracedOrderVO.getFreight_total()) ? embracedOrderVO.getFreight_total() : 0.00);
		} else if (flags == 1) {
			this.logger.info("订单补录后在修改  cwb:" + embracedOrderVO.getOrderNo() + ";当前机构：" + this.getSessionUser().getBranchid() + ";当前时间：" + date);
			embracedOrderVO.setCompletedatetime(date.toString());
			params.put("paymethod", StringUtils.isNotBlank(embracedOrderVO.getPayment_method()) ? embracedOrderVO.getPayment_method() : -1);// modified																																			// b																																			// jiangyu
			params.put("shouldfare", StringUtils.isNotBlank(embracedOrderVO.getFreight()) ? embracedOrderVO.getFreight() : 0.00);
			params.put("totalfee", StringUtils.isNotBlank(embracedOrderVO.getFreight_total()) ? embracedOrderVO.getFreight_total() : 0.00);		
			params.put("express_product_type", embracedOrderVO.getExpress_product_type());// 快递二期增加支付方式			
		} else if (flags == 2) {
			this.logger.info("订单补录  cwb:" + embracedOrderVO.getOrderNo() + ";当前机构：" + this.getSessionUser().getBranchid() + ";当前时间：" + date);
			params.put("cwb", embracedOrderVO.getOrderNo());
			params.put("flowordertype", FlowOrderTypeEnum.LanJianRuZhan.getValue());
			params.put("cwbstate", CwbStateEnum.PeiShong.getValue());
			params.put("collectorid", StringUtils.isNotBlank(embracedOrderVO.getDelivermanId()) ? embracedOrderVO.getDelivermanId() : null);
			params.put("collectorname", StringUtils.isNotBlank(embracedOrderVO.getDelivermanName()) ? embracedOrderVO.getDelivermanName() : null);
			params.put("currentbranchid", this.getSessionUser().getBranchid());// 当前机构
			params.put("inputdatetime", date);
			embracedOrderVO.setInputdatetime(date.toString());
			params.put("cwbordertypeid", CwbOrderTypeIdEnum.Express.getValue());
			if(!"0".equals(embracedOrderVO.getPayment_method())){
				params.put("customerid", 1000);// 默认1000，代表快递单
			}
			embracedOrderVO.setCompletedatetime(date.toString());
			params.put("paymethod", StringUtils.isNotBlank(embracedOrderVO.getPayment_method()) ? embracedOrderVO.getPayment_method() : -1);// modified
																																			// by
																																			// jiangyu
			params.put("shouldfare", StringUtils.isNotBlank(embracedOrderVO.getFreight()) ? embracedOrderVO.getFreight() : 0.00);
			params.put("totalfee", StringUtils.isNotBlank(embracedOrderVO.getFreight_total()) ? embracedOrderVO.getFreight_total() : 0.00);
			params.put("express_product_type", embracedOrderVO.getExpress_product_type());// 快递二期增加支付方式
			if(embracedOrderVO.getPayment_method()!=null&&embracedOrderVO.getPayment_method().equals("1")){
				if (embracedOrderVO.getPaywayid() == null) {
					params.put("paywayid", "1");// 快递二期增加支付方式
					params.put("newpaywayid", "1");// 快递二期增加支付方式
				} else {
					params.put("paywayid", embracedOrderVO.getPaywayid());// 快递二期增加支付方式
					params.put("newpaywayid", embracedOrderVO.getPaywayid());// 快递二期增加支付方式
				}
			}else{
				params.put("paywayid", "0");// 快递二期增加支付方式
				params.put("newpaywayid", "0");// 快递二期增加支付方式
			}
		}
		params.put("transcwb", embracedOrderVO.getOrderNo());// 将订单号写入transcwb
		if(!"null".equals(embracedOrderVO.getSender_provinceid().trim()) ){
			params.put("senderprovinceid", StringUtils.isNotBlank(embracedOrderVO.getSender_provinceid()) ? embracedOrderVO.getSender_provinceid() : null);
		}
		params.put("senderprovince", StringUtils.isNotBlank(embracedOrderVO.getSender_provinceName()) ? embracedOrderVO.getSender_provinceName() : null);
		if(!"null".equals(embracedOrderVO.getSender_cityid().trim())){
			params.put("sendercityid", StringUtils.isNotBlank(embracedOrderVO.getSender_cityid()) ? embracedOrderVO.getSender_cityid() : null);
		}
		params.put("sendercity", StringUtils.isNotBlank(embracedOrderVO.getSender_cityName()) ? embracedOrderVO.getSender_cityName() : null);
		params.put("sendercellphone", StringUtils.isNotBlank(embracedOrderVO.getSender_cellphone()) ? embracedOrderVO.getSender_cellphone() : null);
		params.put("sendertelephone", StringUtils.isNotBlank(embracedOrderVO.getSender_telephone()) ? embracedOrderVO.getSender_telephone() : null);
		if(!"null".equals(embracedOrderVO.getConsignee_provinceid().trim())){
			params.put("recprovinceid", StringUtils.isNotBlank(embracedOrderVO.getConsignee_provinceid()) ? embracedOrderVO.getConsignee_provinceid() : null);
		}
		params.put("cwbprovince", StringUtils.isNotBlank(embracedOrderVO.getConsignee_provinceName()) ? embracedOrderVO.getConsignee_provinceName() : null);
		if(!"null".equals(embracedOrderVO.getConsignee_cityid().trim())){
			params.put("reccityid", StringUtils.isNotBlank(embracedOrderVO.getConsignee_cityid()) ? embracedOrderVO.getConsignee_cityid() : null);
		}
		params.put("cwbcity", StringUtils.isNotBlank(embracedOrderVO.getConsignee_cityName()) ? embracedOrderVO.getConsignee_cityName() : null);
		params.put("consigneemobile", StringUtils.isNotBlank(embracedOrderVO.getConsignee_cellphone()) ? embracedOrderVO.getConsignee_cellphone() : null);
		params.put("consigneephone", StringUtils.isNotBlank(embracedOrderVO.getConsignee_telephone()) ? embracedOrderVO.getConsignee_telephone() : null);
		params.put("sendcarnum", StringUtils.isNotBlank(embracedOrderVO.getNumber()) ? embracedOrderVO.getNumber() : null);

		params.put("inputhandlerid", user.getUserid());
		params.put("inputhandlername", user.getUsername());

		if ((flags != 0) && (embracedOrderVO.getSender_customerid() != null) && (embracedOrderVO.getSender_customerid() != -1)) {
			params.put("customerid", embracedOrderVO.getSender_customerid());
		}
		params.put("sendername", StringUtils.isNotBlank(embracedOrderVO.getSender_name()) ? embracedOrderVO.getSender_name() : null);
		if( !"null".equals(embracedOrderVO.getSender_countyid().trim())){
			params.put("sendercountyid", StringUtils.isNotBlank(embracedOrderVO.getSender_countyid()) ? embracedOrderVO.getSender_countyid() : null);
		}
		params.put("sendercounty", StringUtils.isNotBlank(embracedOrderVO.getSender_countyName()) ? embracedOrderVO.getSender_countyName() : null);
		if( !"null".equals(embracedOrderVO.getSender_townid().trim())){
			params.put("senderstreetid", StringUtils.isNotBlank(embracedOrderVO.getSender_townid()) ? embracedOrderVO.getSender_townid() : null);
		}
		params.put("senderstreet", StringUtils.isNotBlank(embracedOrderVO.getSender_townName()) ? embracedOrderVO.getSender_townName() : null);
		params.put("senderid", StringUtils.isNotBlank(embracedOrderVO.getSender_certificateNo()) ? embracedOrderVO.getSender_certificateNo() : null);
		
		if ((flags != 0) && (embracedOrderVO.getConsignee_customerid() != null) && (embracedOrderVO.getConsignee_customerid() != -1)) {
			params.put("reccustomerid", embracedOrderVO.getConsignee_customerid());
		}
		params.put("consigneename", StringUtils.isNotBlank(embracedOrderVO.getConsignee_name()) ? embracedOrderVO.getConsignee_name() : null);
		if(!"null".equals(embracedOrderVO.getConsignee_countyid().trim())){
			params.put("reccountyid", StringUtils.isNotBlank(embracedOrderVO.getConsignee_countyid()) ? embracedOrderVO.getConsignee_countyid() : null);
		}
		params.put("cwbcounty", StringUtils.isNotBlank(embracedOrderVO.getConsignee_countyName()) ? embracedOrderVO.getConsignee_countyName() : null);
		if(!"null".equals(embracedOrderVO.getConsignee_townid().trim())){
			params.put("recstreetid", StringUtils.isNotBlank(embracedOrderVO.getConsignee_townid()) ? embracedOrderVO.getConsignee_townid() : null);
		}
		params.put("recstreet", StringUtils.isNotBlank(embracedOrderVO.getConsignee_townName()) ? embracedOrderVO.getConsignee_townName() : null);
		params.put("recid", StringUtils.isNotBlank(embracedOrderVO.getConsignee_certificateNo()) ? embracedOrderVO.getConsignee_certificateNo() : null);
		params.put("entrustname", StringUtils.isNotBlank(embracedOrderVO.getGoods_name()) ? embracedOrderVO.getGoods_name() : null);
		params.put("sendnum", StringUtils.isNotBlank(embracedOrderVO.getGoods_number()) ? embracedOrderVO.getGoods_number() : null);
		params.put("carrealweight", StringUtils.isNotBlank(embracedOrderVO.getGoods_weight()) ? embracedOrderVO.getGoods_weight() : null);
		params.put("length", StringUtils.isNotBlank(embracedOrderVO.getGoods_longth()) ? embracedOrderVO.getGoods_longth() : null);
		params.put("width", StringUtils.isNotBlank(embracedOrderVO.getGoods_width()) ? embracedOrderVO.getGoods_width() : null);
		params.put("height", StringUtils.isNotBlank(embracedOrderVO.getGoods_height()) ? embracedOrderVO.getGoods_height() : null);
		params.put("kgs", StringUtils.isNotBlank(embracedOrderVO.getGoods_kgs()) ? embracedOrderVO.getGoods_kgs() : null);
		params.put("other", StringUtils.isNotBlank(embracedOrderVO.getGoods_other()) ? embracedOrderVO.getGoods_other() : null);
		params.put("hascod", StringUtils.isNotBlank(embracedOrderVO.getCollection()) ? embracedOrderVO.getCollection() : null);
		params.put("receivablefee", StringUtils.isNotBlank(embracedOrderVO.getCollection_amount()) ? embracedOrderVO.getCollection_amount() : 0.00);
		params.put("packagefee", StringUtils.isNotBlank(embracedOrderVO.getPacking_amount()) ? embracedOrderVO.getPacking_amount() : 0.00);
		params.put("hasinsurance", StringUtils.isNotBlank(embracedOrderVO.getInsured()) ? embracedOrderVO.getInsured() : null);
		params.put("announcedvalue", StringUtils.isNotBlank(embracedOrderVO.getInsured_amount()) ? embracedOrderVO.getInsured_amount() : 0.00);
		params.put("insuredfee", StringUtils.isNotBlank(embracedOrderVO.getInsured_cost()) ? embracedOrderVO.getInsured_cost() : 0.00);
		params.put("chargeweight", StringUtils.isNotBlank(embracedOrderVO.getCharge_weight()) ? embracedOrderVO.getCharge_weight() : null);
		params.put("realweight", StringUtils.isNotBlank(embracedOrderVO.getActual_weight()) ? embracedOrderVO.getActual_weight() : null);
		params.put("sendareacode", StringUtils.isNotBlank(embracedOrderVO.getOrigin_adress()) ? embracedOrderVO.getOrigin_adress() : null);
		params.put("recareacode", StringUtils.isNotBlank(embracedOrderVO.getDestination()) ? embracedOrderVO.getDestination() : null);
		params.put("monthsettleno", StringUtils.isNotBlank(embracedOrderVO.getMonthly_account_number()) ? embracedOrderVO.getMonthly_account_number() : null);
		params.put("cwbremark", StringUtils.isNotBlank(embracedOrderVO.getRemarks()) ? embracedOrderVO.getRemarks() : null);
		params.put("emaildateid", -1);// 俊哥说这个的弄成-1，刘武强-11.06
		params.put("carsize", StringUtil.nullConvertToEmptyString(embracedOrderVO.getGoods_longth()) + "CM *" + StringUtil.nullConvertToEmptyString(embracedOrderVO.getGoods_width()) + "CM *" + StringUtil
				.nullConvertToEmptyString(embracedOrderVO.getGoods_height()) + "CM");// 俊哥说这个的弄成-1，刘武强-11.06
		if (this.checkEmbracedVO(embracedOrderVO, this.checkmMap)) {
			params.put("isadditionflag", 1);
			params.put("completehandlerid", user.getUserid());
			params.put("completehandlername", user.getUsername());
			params.put("completedatetime", date);
			//如果详细地址里面已经含省+市+区，则不再加入省市区
			String cneeProv = StringUtil.nullConvertToEmptyString((String) (params.get("cwbprovince")));
			String cneeCity = StringUtil.nullConvertToEmptyString((String) (params.get("cwbcity")));
			String cneeRegion = StringUtil.nullConvertToEmptyString((String) (params.get("cwbcounty")));
			String cneeTown = StringUtil.nullConvertToEmptyString((String) params.get("recstreet"));
			String consigneeaddress = StringUtil.nullConvertToEmptyString(embracedOrderVO.getConsignee_adress());
			String temp = cneeProv + cneeCity + cneeRegion + cneeTown;

			// add by jian_xie 2016-07-19，如果省市区街道，整个没有包含在详细中，就在左边拼接
			if(consigneeaddress.indexOf(temp) == -1){
				consigneeaddress = temp + consigneeaddress;
			}
			
			logger.info("手动录入快递单,orderNumber:" + embracedOrderVO.getOrderNo() + ",consigneeaddress:" + consigneeaddress);
			params.put("consigneeaddress", consigneeaddress);
			params.put("senderaddress", embracedOrderVO.getConsignee_adress() == null ? "" : StringUtil.nullConvertToEmptyString((String) (params.get("senderprovince"))) + StringUtil
					.nullConvertToEmptyString((String) (params.get("sendercity"))) + StringUtil.nullConvertToEmptyString((String) (params.get("sendercounty"))) + StringUtil
					.nullConvertToEmptyString((String) params.get("senderstreet")) + StringUtil.nullConvertToEmptyString(embracedOrderVO.getSender_adress()));
			
			logger.info("手动录入快递单,orderNumber:" + embracedOrderVO.getOrderNo() + ",senderaddress:" + StringUtil.nullConvertToEmptyString((String) params.get("senderaddress")));
			// 订单补录成功的时候，jms异步方式，调用地址库，将目的站点id存到订单表中
			Boolean matchFlag = this.tpsInterfaceExecutor.autoMatch(embracedOrderVO.getOrderNo(), user.getUserid(), (String) params.get("consigneeaddress"), AddressMatchEnum.OrderEmbraceMatch
					.getValue());
			if (matchFlag) {
				this.logger.info("订单补录调用地址库jms消息发送成功");

			} else {
				this.logger.info("订单补录调用地址库jms消息发送失败");
			}
			// 在必填项都填完整的时候才调用创建运单接口
			this.tpsSender(embracedOrderVO, "inbrace");
		} else {
			params.put("isadditionflag", 0);
			params.put("senderaddress", StringUtils.isNotBlank(embracedOrderVO.getSender_adress()) ? embracedOrderVO.getSender_adress() : null);
			params.put("consigneeaddress", StringUtils.isNotBlank(embracedOrderVO.getConsignee_adress()) ? embracedOrderVO.getConsignee_adress() : null);
		}
		String flag;
		if ((flags == 0) || (flags == 2)) {
			params.put("instationhandlerid", user.getUserid());
			params.put("instationhandlername", user.getUsername());
			params.put("instationdatetime", date);
			params.put("instationid", branch.getBranchid());
			params.put("instationname", branch.getBranchname());

			params.put("credate", Timestamp.valueOf(DateTimeUtil.getNowTime()));
			
			//再次校验数据库是否已经有记录，否则不在insert ---刘武强20160811
			EmbracedOrderVO VO = this.judgeCwbOrderByCwb(embracedOrderVO.getOrderNo());
			if(VO != null){
				System.out.println("快递单：" + embracedOrderVO.getOrderNo() +"已经有数据，不能再isnert");
				return "hasSaved";
			}
			logger.info("newpaywayid:" + params.get("newpaywayid") + ", paywayid" + params.get("paywayid"));
			flag = this.generalDAO.insert(params, "express_ops_cwb_detail") == false ? "false" : "true";
			this.logger.info("补录：inset方法，补录标志位：" + embracedOrderVO.getOrderNo() +"   " +  embracedOrderVO.getIsadditionflag());
			// 如果是新建运单，那么他的状态为入站，调用tps状态反馈接口 11.19 如果状态有改变，且变为揽件入站，则需要保存流程信息
			this.executeTpsTransInterface(embracedOrderVO, user);
			CwbOrder order = this.cwbOrderService.getCwbByCwb(embracedOrderVO.getOrderNo());
			logger.info("newpaywayid:" + order.getNewpaywayid() + ", paywayid" + order.getPaywayid());
			this.cwbOrderService.createFloworder(user, branch.getBranchid(), order, FlowOrderTypeEnum.LanJianRuZhan, "", System.currentTimeMillis());
		} else {
			boolean flowflag = false;
			// 如果是更新运单，且运单的状态为揽件录入（通过一体机创建的订单），那么这个时候会将状态置为揽件入站，并且调用tps的状态反馈接口
			if ((savedVO.getFlowordertype() != null) && (savedVO.getFlowordertype()).equals(FlowOrderTypeEnum.YunDanLuRu.getValue() + "")) {
				params.put("flowordertype", FlowOrderTypeEnum.LanJianRuZhan.getValue());
				params.put("instationhandlerid", user.getUserid());
				params.put("instationhandlername", user.getUsername());
				params.put("instationdatetime", date);
				params.put("instationid", branch.getBranchid());
				params.put("instationname", branch.getBranchname());
				this.executeTpsTransInterface(embracedOrderVO, user);
				flowflag = true;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cwb", embracedOrderVO.getOrderNo());
			map.put("state", 1);
			flag = this.generalDAO.update(params, "express_ops_cwb_detail", map) == false ? "false" : "true";
			this.logger.info("补录：update方法，补录标志位："+ embracedOrderVO.getOrderNo() +"   " + embracedOrderVO.getIsadditionflag());
			if (flowflag) { // 如果状态转变为揽件入站，那么就保存
				CwbOrder order = this.cwbOrderService.getCwbByCwb(embracedOrderVO.getOrderNo());
				this.cwbOrderService.createFloworder(user, branch.getBranchid(), order, FlowOrderTypeEnum.LanJianRuZhan, "", System.currentTimeMillis());
			}
		}
		if ("true".equals(flag)) {
			if(!StringUtil.isEmpty(embracedOrderVO.getReserveOrderNo())){
				try{
					//快递二期新增，反馈预约单状态:揽收成功给tps
					this.reserveOrderService.returnReserveOrderStateToTps(embracedOrderVO,branch);
				}catch(Exception e){
					flag="false"; 
				}
				
			}
			this.logger.info("保存成功");
		} else {
			this.logger.info("保存失败");
		}
		return flag;
	}

	/**
	 *
	 * @Title: savaexpressWeigh
	 * @description 补录页面点击读数的时候，根据情况保存把重量保存到express_ops_weigh表中
	 * @author 刘武强
	 * @date 2015年11月17日下午1:07:29
	 * @param @param embracedOrderVO
	 * @param @param flag
	 * @return void
	 * @throws
	 */
	public synchronized void savaexpressWeigh(EmbracedOrderVO embracedOrderVO, int flag) {
		// 判断运单号是否已经承重过
		ExpressWeigh savedVO = this.getWeighByCwb(embracedOrderVO.getOrderNo(), this.getSessionUser().getBranchid());
		if ((flag == 0)) {
			return;
		}
		User user = this.getSessionUser();
		Map<String, Object> params = new HashMap<String, Object>();
		Date date = new Date();
		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		branch = branch != null ? branch : new Branch();
		params.put("cwb", embracedOrderVO.getOrderNo());
		params.put("weight", embracedOrderVO.getActual_weight());
		params.put("branchid", this.getSessionUser().getBranchid());
		params.put("branchname", branch.getBranchname());
		params.put("handlerid", user.getUserid());
		params.put("handlername", user.getUsername());
		params.put("handletime", date);

		if (savedVO == null) {// 如果称重过，那么就update
			this.generalDAO.insert(params, "express_ops_weigh");
		} else { // 如果没有承重过，那么就insert
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", savedVO.getId());
			this.generalDAO.update(params, "express_ops_weigh", map);
		}
	}

	/**
	 *
	 * @Title: judgeCwbOrderByCwb
	 * @description 判断运单号在数据库中有没有对应的订单信息
	 * @author 刘武强
	 * @date 2015年8月8日下午5:20:30
	 * @param @param cwb
	 * @param @return
	 * @return boolean false:代表没有对应；true：代表对应
	 * @throws
	 */
	public EmbracedOrderVO judgeCwbOrderByCwb(String cwb, Long branchid) {
		EmbracedOrderVO embracedOrderVO = this.expressOrderDao.getCwbOrderByCwb(cwb, branchid);
		this.checkEmbracedVO(embracedOrderVO, this.checkmMap);
		return embracedOrderVO;
	}

	public EmbracedOrderVO judgeCwbOrderByCwb(String cwb) {
		EmbracedOrderVO embracedOrderVO = this.expressOrderDao.getCwbOrderByCwb(cwb);
		this.checkEmbracedVO(embracedOrderVO, this.checkmMap);
		return embracedOrderVO;
	}

	/**
	 *
	 * @Title: getWeighByCwb
	 * @description 通过运单号和机构id，查询最新的货物重量
	 * @author 刘武强
	 * @date 2015年11月17日上午11:15:40
	 * @param @param cwb
	 * @param @param branchid
	 * @param @return
	 * @return ExpressWeigh
	 * @throws
	 */
	public ExpressWeigh getWeighByCwb(String cwb, Long branchid) {
		ExpressWeigh expressWeigh = this.expressWeighDAO.getExpressWeighByCwb(cwb, branchid);
		return expressWeigh;
	}

	/**
	 *
	 * @Title: getPreOrderInfo
	 * @description 获取揽收运单补录--未揽收的数据
	 * @author 刘武强
	 * @date 2015年8月10日下午11:47:28
	 * @param @param preOrderQueryVO分currentbranchid
	 * @param @param page
	 * @param @param pageNumber
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getNonExtraInputOrder(long page, int pageNumber) {
		return this.expressOrderDao.getNonExtraInputOrder(page, pageNumber, CwbOrderTypeIdEnum.Express.getValue(), this.getSessionUser().getBranchid());
	}

	/**
	 *
	 * @Title: getAllNotExtraOrder
	 * @description 获取揽收运单补录--未揽收的数据
	 * @author 王志宇
	 * @date 2015年8月10日下午11:47:28
	 * @param @param preOrderQueryVO分currentbranchid
	 * @param @param page
	 * @param @param pageNumber
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public List<EmbracedImportOrderVO> getAllNotExtraOrder() {
		// List<EmbracedImportOrderVO> getAllNotExtraInputOrder
		return this.expressOrderDao.getAllNotExtraInputOrder(CwbOrderTypeIdEnum.Express.getValue(), this.getSessionUser().getBranchid());
	}

	/**
	 *
	 * @Title: getCustomers
	 * @description 获取所有的客户信息
	 * @author 刘武强
	 * @date 2015年8月18日下午1:48:10
	 * @param @return
	 * @return List<Customer>
	 * @throws
	 */
	public List<Map<String, Object>> getCustomers(int haveAllOrNot) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Customer> infolist = this.customerDAO.getAllCustomers();
		Map<String, Object> mapall = new HashMap<String, Object>();
		if (haveAllOrNot == 1) {
			mapall.put("customerid", -1);
			mapall.put("customercode", "");
			mapall.put("companyname", "");
			list.add(mapall);
		}
		for (int i = 0; i < infolist.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			Customer customer = infolist.get(i);
			map.put("customerid", customer.getCustomerid());
			map.put("customercode", customer.getCustomercode());
			map.put("companyname", customer.getCompanyname());
			list.add(map);
		}
		return list;
	}

	public Branch getBracnch() {
		return this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
	}

	/**
	 *
	 * @Title: tpsSender
	 * @description 发送jms消息，通知jms保存运单数据，并调用创建运单接口
	 * @author 刘武强
	 * @date 2015年9月9日上午9:24:36
	 * @param @param embracedOrderVO
	 * @param @param inputOrEmbrace "input"代表是录入，"inbrace"代表补录
	 * @return void
	 * @throws
	 */
	public void tpsSender(EmbracedOrderVO embracedOrderVO, String inputOrEmbrace) {
		if (StringUtils.isBlank(inputOrEmbrace) || !("input".equals(inputOrEmbrace) || "inbrace".equals(inputOrEmbrace))) {
			this.logger.info("不能判断是录入还是补录，dmp信息回写失败！");
		} else {
			try {
				Branch branch = getBracnch();
				PjDeliveryOrderRequest doReq = new PjDeliveryOrderRequest();
				doReq.setTransportNo(embracedOrderVO.getOrderNo());
				// doReq.setCustOrderNo(embracedOrderVO.getOrderNo());//接口要求运单号和订单号不一样，但快递是一样的，并且非必填项，所以不set
				// doReq.setAcceptDept(this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid())
				// + "");
				doReq.setAcceptDept(this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getTpsbranchcode());// 揽件站点tps编码
				// doReq.setAcceptDept("gdfy");//用于测试，因为目前tps和快递的站点尚未统一，没法校验这个
				doReq.setAcceptOperator(embracedOrderVO.getDelivermanName());
				//快递二期增加必填字段：操作人
				doReq.setCreatedByUser(this.getSessionUser().getUsername());
				doReq.setCreatedOffice(branch.getTpsbranchcode());
				doReq.setCnorProv(embracedOrderVO.getSender_provinceName());
				doReq.setCnorCity(embracedOrderVO.getSender_cityName());
				if (!"input".equals(inputOrEmbrace)) {// 只有补录才有的数据回写
					doReq.setCnorRegion(embracedOrderVO.getSender_countyName());//
					doReq.setCustName(embracedOrderVO.getSender_companyName());
					doReq.setCneeCorpName(embracedOrderVO.getConsignee_companyName());
					doReq.setCnorTown(StringUtil.nullConvertToEmptyString(embracedOrderVO.getSender_townName()));//
					doReq.setCneeRegion(embracedOrderVO.getConsignee_countyName());//
					doReq.setCneeTown(StringUtil.nullConvertToEmptyString(embracedOrderVO.getConsignee_townName()));//
					doReq.setCnorName(embracedOrderVO.getSender_name());
					doReq.setCneeName(embracedOrderVO.getConsignee_name());
					doReq.setCneeCertificate(StringUtil.nullConvertToEmptyString(embracedOrderVO.getConsignee_certificateNo()));
					doReq.setCustCode(StringUtil.nullConvertToEmptyString(embracedOrderVO.getSender_No()));
					doReq.setCneeNo(StringUtil.nullConvertToEmptyString(embracedOrderVO.getConsignee_No()));
					doReq.setIsCod(Boolean.parseBoolean(embracedOrderVO.getCollection()));
					doReq.setCodAmount(Double.parseDouble(((embracedOrderVO.getCollection_amount() != null) && (!"".equals(embracedOrderVO.getCollection_amount()))) ? embracedOrderVO.getCollection_amount() : "0.00"));// 按照王海要求，默认改为0.00
					doReq.setCarriage(((embracedOrderVO.getFreight_total() != null) && (!"".equals(embracedOrderVO.getFreight_total()))) ? this.toFixed(Double.parseDouble(embracedOrderVO
							.getFreight_total()), 2) : 0);
					doReq.setTotalWeight(((embracedOrderVO.getActual_weight() != null) && (!"".equals(embracedOrderVO.getActual_weight()))) ? this.toFixed(Double.parseDouble(embracedOrderVO
							.getActual_weight()), 2) : 0);
					doReq.setCalculateWeight(((embracedOrderVO.getCharge_weight() != null) && (!"".equals(embracedOrderVO.getCharge_weight()))) ? this.toFixed(Double.parseDouble(embracedOrderVO
							.getCharge_weight()), 2) : 0);
					doReq.setTotalVolume(((embracedOrderVO.getGoods_kgs() != null) && (!"".equals(embracedOrderVO.getGoods_kgs()))) ? this.toFixed(Double.parseDouble(embracedOrderVO.getGoods_kgs()) / 1000, 2) : 0);//
					doReq.setTotalBox(Integer.parseInt(embracedOrderVO.getGoods_number().trim()));//
					doReq.setAssuranceValue(((embracedOrderVO.getInsured_amount() != null) && (!"".equals(embracedOrderVO.getInsured_amount()))) ? this.toFixed(Double.parseDouble(embracedOrderVO
							.getInsured_amount()), 2) : 0);
					doReq.setAssuranceFee(((embracedOrderVO.getInsured_cost() != null) && (!"".equals(embracedOrderVO.getInsured_cost()))) ? this.toFixed(Double.parseDouble(embracedOrderVO
							.getInsured_cost()), 2) : 0);
					doReq.setPackingFee(((embracedOrderVO.getPacking_amount() != null) && (!"".equals(embracedOrderVO.getPacking_amount()))) ? this.toFixed(Double.parseDouble(embracedOrderVO
							.getPacking_amount()), 2) : 0);
					doReq.setPayType(Integer.parseInt(embracedOrderVO.getPayment_method()));
					// doReq.setPayment("0".equals(embracedOrderVO.getPayment_method().trim())
					// ? "0" : "-1");
					doReq.setPayment(-1);// 11.13 马哥说运单里任何情况都传-1
					//月结方式，需要把月结账号，寄件人单位名称，寄件人单位编码传过tps----刘武强20160718
					if(StringUtils.isNotBlank(embracedOrderVO.getPayment_method()) && ("0".equals(embracedOrderVO.getPayment_method().trim()) || "3".equals(embracedOrderVO.getPayment_method().trim()))){
						doReq.setAccountId(embracedOrderVO.getMonthly_account_number());
						doReq.setAccountCustName(embracedOrderVO.getSender_companyName());
						doReq.setAccountCustCode(embracedOrderVO.getMonthly_account_number());
					}
					doReq.setCnorRemark(embracedOrderVO.getRemarks());
					//快递二期新增：运费
					doReq.setActualFee(((embracedOrderVO.getFreight() != null) && (!"".equals(embracedOrderVO.getFreight()))) ? this.toFixed(Double.parseDouble(embracedOrderVO
							.getFreight()), 2) : 0); //自动截取两位有效小数，防止小数过长，tps校验不通过，导致接口发送失败---刘武强20161018
					doReq.setProductType(embracedOrderVO.getExpress_product_type());
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						doReq.setPickerTime(formatter.parse(embracedOrderVO.getInputdatetime().substring(0, 19)));
					} catch (Exception e) {
						Date uDate = new Date();
						//String strDate = formatter.format(uDate.getTime());
						doReq.setPickerTime(uDate);
						this.logger.info("补录界面进行了新建运单操作！");
					}
					List<PjDeliveryOrderCargoRequest> goodslist = new ArrayList<PjDeliveryOrderCargoRequest>();
					PjDeliveryOrderCargoRequest goodsinfo = new PjDeliveryOrderCargoRequest();
					goodsinfo.setCargoName(embracedOrderVO.getGoods_name());
					goodsinfo.setCount(Integer.parseInt(embracedOrderVO.getGoods_number().trim()));
					goodsinfo.setWeight(((embracedOrderVO.getGoods_weight() != null) && (!"".equals(embracedOrderVO.getGoods_weight()))) ? this.toFixed(Double.parseDouble(embracedOrderVO
							.getGoods_weight()), 2) : 0);
					goodsinfo
							.setVolume(((embracedOrderVO.getGoods_kgs() != null) && (!"".equals(embracedOrderVO.getGoods_kgs()))) ? this.toFixed(Double.parseDouble(embracedOrderVO.getGoods_kgs()) / 1000, 2) : 0);

					goodsinfo.setCargoLength(((embracedOrderVO.getGoods_longth() != null) && (!"".equals(embracedOrderVO.getGoods_longth()))) ? this.toFixed(Double.parseDouble(embracedOrderVO
							.getGoods_longth()), 2) : 0);
					goodsinfo.setCargoWidth(((embracedOrderVO.getGoods_width() != null) && !"".equals(embracedOrderVO.getGoods_width())) ? this.toFixed(Double.parseDouble(embracedOrderVO
							.getGoods_width()), 2) : 0);
					goodsinfo.setCargoHeight(((embracedOrderVO.getGoods_height() != null) && !"".equals(embracedOrderVO.getGoods_height())) ? this.toFixed(Double.parseDouble(embracedOrderVO
							.getGoods_height()), 2) : 0);
					goodslist.add(goodsinfo);
					doReq.setPjDeliveryOrderCargoInfos(goodslist);
				}
				doReq.setCnorAddr(embracedOrderVO.getSender_adress());
				if (StringUtils.isNotBlank(embracedOrderVO.getSender_cellphone())) {
					doReq.setCnorMobile(embracedOrderVO.getSender_cellphone());
				}
				if (StringUtils.isNotBlank(embracedOrderVO.getSender_telephone())) {
					doReq.setCnorTel(embracedOrderVO.getSender_telephone());
				}

				doReq.setCneeProv(embracedOrderVO.getConsignee_provinceName());
				doReq.setCneeCity(embracedOrderVO.getConsignee_cityName());

				//如果详细地址里面已经含省+市+区，则不再加入省市区
				String consigneeProvinceName = embracedOrderVO.getConsignee_provinceName();
				String consigneeCityName = embracedOrderVO.getConsignee_cityName();
				String consigneeCountyName = embracedOrderVO.getConsignee_countyName();
				String consigneeTownName = embracedOrderVO.getConsignee_townName();
				String address = embracedOrderVO.getConsignee_adress();
//				if(null != address){
//					if(null != consigneeTownName && address.indexOf(consigneeTownName) < 0){//从地址小的开始处理
//						address = consigneeTownName + address;
//					}
//					if(null != consigneeCountyName && address.indexOf(consigneeCountyName) < 0){
//						address = consigneeCountyName + address;
//					}
//					if(null != consigneeCityName && address.indexOf(consigneeCityName) < 0){
//						address = consigneeCityName + address;
//					}
//					if(null != consigneeProvinceName && address.indexOf(consigneeProvinceName) < 0){
//						address = consigneeProvinceName + address;
//					}
//				}
				String temp = consigneeProvinceName + consigneeCityName + consigneeCountyName + consigneeTownName;
				// add by jian_xie 2016-07-19，如果省市区街道，整个没有包含在详细中，就在左边拼接
				if(address.indexOf(temp) == -1){
					address = temp + address;
				}
				doReq.setCneeAddr(address);
				if (StringUtils.isNotBlank(embracedOrderVO.getConsignee_cellphone())) {
					doReq.setCneeMobile(embracedOrderVO.getConsignee_cellphone());
				}
				if (StringUtils.isNotBlank(embracedOrderVO.getConsignee_telephone())) {
					doReq.setCneeTel(embracedOrderVO.getConsignee_telephone());
				}
				doReq.setCneePeriod(0);
				// doReq.setCneeRemark("");//忽略
				doReq.setTotalNum(Integer.parseInt(embracedOrderVO.getNumber()));
				List<PjDeliveryOrderRequest> requestlist = new ArrayList<PjDeliveryOrderRequest>();
				requestlist.add(doReq);
				ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.CreateTransNO);
				paramObj.setRequestlist(requestlist);
				this.tpsInterfaceExecutor.executTpsInterface(paramObj);
			} catch (Exception e) {
				this.logger.info("发送jms消息异常！");
				logger.error("发送时的异常为：" + e);
			}
		}
	}

	/**
	 *
	 * @Title: toFixed
	 * @description 输入一个double类型的数：num，输出保留x位小数的数
	 * @author 刘武强
	 * @date 2015年9月2日下午3:04:43
	 * @param @param num
	 * @param @param x
	 * @param @return
	 * @return double
	 * @throws
	 */
	private double toFixed(double num, int x) {
		java.math.BigDecimal b = new java.math.BigDecimal(num);
		double value = b.setScale(x, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
		return value;
	}

	/**
	 *
	 * @Title: checkEmbracedVO
	 * @description 校验运单是否把必填项填写完
	 * @author 刘武强
	 * @date 2015年10月30日下午5:14:39
	 * @param @param embracedOrderVO
	 * @param @param map
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean checkEmbracedVO(EmbracedOrderVO embracedOrderVO, Map<String, Integer> map) {
		Set<String> keys = map.keySet();
		// 如果为空，返回false
		if (embracedOrderVO == null) {
			this.logger.info("vo为空");
			return false;
		}
		// 首先校验付款方式  
		if (this.jugNull(embracedOrderVO.getPayment_method())) {
			this.logger.info("付款方式未输入");
			return false;
		}
		// 如果为月结，那么校验送检人客户id是否填写
		if ("0".equals(embracedOrderVO.getPayment_method()) && ((embracedOrderVO.getSender_customerid() == null) || embracedOrderVO.getSender_customerid() == -1)) {
			this.logger.info("月结运单客户未输入");
			return false;
		}
		// 如果为月结或第三方支付，那么校验送检人单位是否填写 ---刘武强20160721（如果tps传月结账号的话，要求送件人单位不为空或""）
		if (("0".equals(embracedOrderVO.getPayment_method()) || "3".equals(embracedOrderVO.getPayment_method())) && ((embracedOrderVO.getSender_companyName() == null) || "".equals(embracedOrderVO.getSender_companyName().trim()))) {
			this.logger.info("月结或第三方支付运单寄件人单位未输入");
			return false;
		}
		// 如果为月结，那么校验月结账号
		if ("0".equals(embracedOrderVO.getPayment_method()) && (this.jugNull(embracedOrderVO.getMonthly_account_number()))) {
			this.logger.info("月结运单账号未输入");
			return false;
		}
		if (this.jugNull(embracedOrderVO.getSender_cellphone()) && this.jugNull(embracedOrderVO.getSender_telephone())) {
			this.logger.info("寄件人手机和固话至少填写一个");
			return false;
		}
		if (this.jugNull(embracedOrderVO.getConsignee_cellphone()) && this.jugNull(embracedOrderVO.getConsignee_telephone())) {
			this.logger.info("收件人手机和固话至少填写一个");
			return false;
		}
		if (this.jugNull(embracedOrderVO.getFreight())) {
			this.logger.info("运费未输入");
			return false;
		}
		if ("1".equals(embracedOrderVO.getInsured()) && this.jugNull(embracedOrderVO.getInsured_amount())) {
			this.logger.info("保价运单未输入保价金额");
			return false;
		}
		if ("1".equals(embracedOrderVO.getCollection()) && this.jugNull(embracedOrderVO.getCollection_amount())) {
			this.logger.info("代收货款运单未输入代收货款金额");
			return false;
		}
		for (String key : keys) {
			if ("OrderNo".equals(key) || "Monthly_account_number".equals(key) || "Sender_companyName".equals(key) || "Sender_countyName".equals(key) || "Sender_townName".equals(key) ||"Sender_cellphone".equals(key) || "Sender_telephone"
					.equals(key) || "Goods_length".equals(key) || "Goods_width".equals(key) || "Goods_high".equals(key) || "Goods_other".equals(key) || "Consignee_cellphone"
					.equals(key) || "Consignee_telephone".equals(key) || "Consignee_townName".equals(key) || "Xianfu".equals(key) || "Daofu".equals(key) || "Yuejie".equals(key) || "Collection_amount".equals(key) || "Insured_amount"
					.equals(key) || "Insured_cost".equals(key) || "Packing_amount".equals(key)) {
				continue;
			}
			// 通过反射机制获取值
			String value = "";
			String methodName = "";
			Method m;
			methodName = "get" + key;
			try {
				// 获取方法
				m = embracedOrderVO.getClass().getDeclaredMethod(methodName, null);
				// 调用方法

				value = (String) m.invoke(embracedOrderVO, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 校验是否为空
			if (this.jugNull(value)) {
				this.logger.info(key + "没有填写");
				return false;
			}
		}
		System.out.println("运单必填项全部填完");
		return true;
	}

	/**
	 *
	 * @Title: jugNull
	 * @description 判断字符串是否为空，是否为""
	 * @author 刘武强
	 * @date 2015年10月30日下午4:13:54
	 * @param @param obj
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean jugNull(String obj) {
		if ((obj == null) || "".equals(obj.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 调用tps运单反馈接口 ----揽件入站
	 *
	 * @param orders
	 */
	private void executeTpsTransInterface(EmbracedOrderVO order, User user) {
		if (order != null) {
			ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
			PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
			Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
			transNoFeedBack.setTransportNo(order.getOrderNo());
			transNoFeedBack.setOperateOrg(branch.getTpsbranchcode());//tps机构编码
			transNoFeedBack.setOperater(user.getRealname());
			transNoFeedBack.setOperateTime(System.currentTimeMillis());
			transNoFeedBack.setOperateType(FeedbackOperateTypeEnum.InboundScan.getValue());
			transNoFeedBack.setReason("");

			/*
			 * //拼接描述 JoinMessageVO contextVar = new JoinMessageVO();
			 * contextVar.
			 * setOperationType(TpsOperationEnum.ArrivalScan.getValue(
			 * ));//揽件入站对应入站扫描
			 * contextVar.setStation(branch.getBranchname());//站点名称
			 * contextVar.setOperator(user.getRealname());
			 * contextVar.connectMessage();
			 * transNoFeedBack.setTransportDetail(contextVar.getTrackMessage());
			 */
			paramObj.setTransNoFeedBack(transNoFeedBack);
			// 发送JMS消息
			this.tpsInterfaceExecutor.executTpsInterface(paramObj);
			this.logger.info("快递单：" + order.getOrderNo() + ",发送揽件入站轨迹反馈给tps！");
		}
	}

	public void changeSenderAddr() {
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		list = this.expressOrderDao.changeAddr();
		for (CwbOrder temp : list) {
			try {
				this.expressOrderDao.updateSenderAddr(temp.getCwb(), temp.getSenderaddress());
			} catch (Exception e) {
				e.printStackTrace();
				this.logger.info(e.getMessage());
			}
		}
	}
	
	/**
	 * 校验录入运单号是否与系统订单号/运单号重复 add by vic.liang@pjbest.com 2016-08-05
	 * @param transcwb
	 * @return
	 */
	public boolean checkTranscwb(String transcwb) {
		int countCwb = this.cwbDao.getCountByCwb(transcwb);
		if (countCwb > 0) 
			return true;
		int countTransCwb = this.transCwbDao.getCountByTranscwb(transcwb);
		if (countTransCwb > 0) 
			return true;
		
		return false;
	}
	
	/**
	* @Title: getCollector 
	* @Description: 根据运单对象，找到揽件员的 信息，判断该揽件员是否是这个站点的，是的话则输出该揽件员对象，否则就输出null
	* @param @param embracedOrderVO
	* @param @return    设定文件 
	* @return User    返回类型 
	* @throws 
	* @date 2016年10月5日 上午11:20:19 
	* @author 刘武强
	 */
	public User getCollector(EmbracedOrderVO embracedOrderVO){
		User collector = null;
		Branch userBranch = this.getBracnch();
		try{
			if(embracedOrderVO != null ){
				collector = this.userDAO.getAllUserByid(StringUtils.isNotBlank(embracedOrderVO.getDelivermanId())? Long.parseLong(embracedOrderVO.getDelivermanId()) : 0);
			}
		}catch(Exception e){
			this.logger.info("根据输入的运单号，获取小件员异常 ：" + e.getMessage());
		}
		if(collector == null || collector.getBranchid() != userBranch.getBranchid()){//userBranch不可能为null了，所以不做空判断;如果揽件员不属于该站点，就置空
			collector = null;
		}
		return collector;
	} 
}
