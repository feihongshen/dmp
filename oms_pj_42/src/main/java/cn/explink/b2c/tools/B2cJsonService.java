package cn.explink.b2c.tools;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.cwbsearch.B2cDatasearchService;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Customer;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;

@Service
public class B2cJsonService {
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	BuildB2cDataMaster buildB2cDataMaster;
	@Autowired
	B2cDatasearchService b2cDatasearchService;
	@Autowired
	CacheBaseListener cacheBaseListener;

	private Logger logger = LoggerFactory.getLogger(B2cJsonService.class);
	private ObjectMapper objectMapper = JacksonMapper.getInstance();

	// public static Map<Long,Long> chackMap=new HashMap<Long, Long>();
	// private static List<Customer> custlist=null;
	// public Customer getOwnCustomer(long customerid){
	// try {
	// for(Customer cust:getCustomerList()){
	// if(cust.getCustomerid()==customerid){
	// return cust;
	// }
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// public void initCustomerList(){
	// custlist= getDmpdao.getAllCustomers();
	// }
	// public List<Customer> getCustomerList(){
	// if(chackMap.get(1l)==null){
	// chackMap.put(1l, 1l);
	// custlist=getDmpdao.getAllCustomers() ;
	// }
	//
	// return custlist;
	// }

	/**
	 * 封装为Json格式的字符串，用于B2C对接。
	 *
	 * @param cwbOrder
	 * @param flowOrdertype
	 * @return
	 */
	public String orderToJson(CwbOrderWithDeliveryState cwbOrderWothDeliverystate, DmpOrderFlow orderFlow, long flowOrdertype) throws Exception {

		this.logger.info("orderToJson执行了B2C对接状态的封装，cwb={},flowordertype={}", orderFlow.getCwb(), flowOrdertype);

		long delivery_state = cwbOrderWothDeliverystate.getDeliveryState() == null ? 0 : cwbOrderWothDeliverystate.getDeliveryState().getDeliverystate(); // 反馈状态
		// ObjectMapper objectMapper=new ObjectMapper();
		long customerid = cwbOrderWothDeliverystate.getCwbOrder().getCustomerid();

		Customer customer = this.cacheBaseListener.getCustomer(customerid);
		if (customer == null) {
			this.logger.info("Customer对象在缓存中没有获取到，请求dmp..cwb={}", orderFlow.getCwb());
			customer = this.getDmpdao.getCustomer(customerid);
		}

		if (customer == null) {
			this.logger.error("获取customer对象为空，return，当前订单号=" + orderFlow.getCwb() + ",customerid=" + cwbOrderWothDeliverystate.getCwbOrder().getCustomerid() + ",flowOrdertype=" + flowOrdertype);
			return null;
		}
		if ((customer != null) && (customer.getB2cEnum() == null)) {
			this.logger.warn("未设置对接，customername=" + customer.getCustomername() + ",cwb=" + orderFlow.getCwb());
			return null;
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.JuMeiYouPin.getKey()))) {
			return this.buildB2cDataMaster.getBulidJuMeiB2cData().BuildJuMeiDataMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, this.objectMapper);
		}

		if (customer.getB2cEnum().equals(this.getB2cEnumKeys(customer, "tmall"))) {
			return this.buildB2cDataMaster.getBulidTmallB2cData().BuildTmallMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, customer.getB2cEnum(),
					this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.DangDang.getKey()))) {
			return this.buildB2cDataMaster.getBulidDangDangB2cData().BuildDangDangMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(),
					cwbOrderWothDeliverystate.getDeliveryState(), this.objectMapper);
		}
		if (customer.getB2cEnum().equals(this.getB2cEnumKeys(customer, "vipshop"))) {

			return this.buildB2cDataMaster.getBulidVipShopB2cData().BuildVipShopMethod(customer.getB2cEnum(), orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(),
					cwbOrderWothDeliverystate.getDeliveryState(), delivery_state, this.objectMapper);

		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Yihaodian.getKey())) || customer.getB2cEnum().equals(String.valueOf(B2cEnum.Yihaodian_beijing.getKey()))) {
			return this.buildB2cDataMaster.getBulidYihaodianB2cData().BuildYihaodianMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate, delivery_state, customer.getB2cEnum(),
					this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Smile.getKey()))) {
			return this.buildB2cDataMaster.getBulidSmileB2cData().BuildSmailMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.YiXun.getKey())) || (customer.getCustomername().indexOf("易迅") > -1)) {

			return this.buildB2cDataMaster.getBulidYiXunB2cData().BuildYiXunMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate, delivery_state, this.objectMapper);

		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Amazon.getKey()))) {

			return this.buildB2cDataMaster.getBuildAmazonB2cData().BuildAmazonMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), cwbOrderWothDeliverystate.getDeliveryState(),
					delivery_state, this.objectMapper);

		}

		if (customer.getB2cEnum().equals(this.getB2cEnumKeys(customer, "rufengda")))// 如风达
		{
			return this.buildB2cDataMaster.getBulidRufengdaB2cData().BuildRufengdaMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(),
					cwbOrderWothDeliverystate.getDeliveryState(), delivery_state, this.objectMapper);
		}

		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Gome.getKey())) || (customer.getCustomername().indexOf("国美") > -1)) {// 国美

			return this.buildB2cDataMaster.getBulidGomeB2cData().BuildGomeMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), cwbOrderWothDeliverystate.getDeliveryState(),
					delivery_state, this.objectMapper);

		}

		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.YangGuang.getKey()))) {
			return this.buildB2cDataMaster.getBulidYangGuangB2cData().BuildYangGuangMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(),
					cwbOrderWothDeliverystate.getDeliveryState(), delivery_state, this.objectMapper);
		}

		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.YeMaiJiu.getKey()))) {
			return this.buildB2cDataMaster.getBuildYeMaiJiuB2cData().BuildYeMaiJiuMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, this.objectMapper);
		}

		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.GuangZhouABC.getKey()))) {
			return this.buildB2cDataMaster.getBuildGuangZhouABCB2cData().BuildGuangZhouMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state,
					cwbOrderWothDeliverystate, this.objectMapper);
		}

		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.HangZhouABC.getKey()))) {
			return this.buildB2cDataMaster.getBuildHangZhouABCB2cData().BuildHangZhouMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state,
					cwbOrderWothDeliverystate, this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.DongFangCJ.getKey()))) {
			return this.buildB2cDataMaster.getBuildDongFangCJB2cData().BuildDongFangCJMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state,
					cwbOrderWothDeliverystate, this.objectMapper);
		}

		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.HaoXiangGou.getKey()))) {
			return this.buildB2cDataMaster.getBuildHaoXiangGouB2cData().BuildHaoXiangGouMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(),
					cwbOrderWothDeliverystate.getDeliveryState(), delivery_state, this.objectMapper);
		}

		if (customer.getB2cEnum().equals(this.getB2cEnumKeys(customer, "DPFoss"))) {
			return this.buildB2cDataMaster.getBuildDpfossB2cData().BuildDpfossMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), cwbOrderWothDeliverystate.getDeliveryState(),
					delivery_state, Integer.valueOf(customer.getB2cEnum()), this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.HomeGou.getKey()))) {
			return this.buildB2cDataMaster.getBuildHomegouB2cData().BuildHomegouMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, cwbOrderWothDeliverystate,
					this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Maikaolin.getKey())))// maikaolin
		{
			return this.buildB2cDataMaster.getBulidMaikolinB2cData().BuildMaikolinMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, cwbOrderWothDeliverystate,
					this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.happyGo.getKey())))// 快乐购
		{
			this.buildB2cDataMaster.buildHappyGoMethodForYishenhe.BuildHappyGoMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, cwbOrderWothDeliverystate,
					this.objectMapper);
			return this.buildB2cDataMaster.getBuildHappyGoB2cData().BuildHappyGoMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, cwbOrderWothDeliverystate,
					this.objectMapper);

		}
		if (customer.getB2cEnum().equals(this.getB2cEnumKeys(customer, "saohuobang")))// 扫货帮
		{
			this.buildB2cDataMaster.getBuildSaohuobangB2cData().BuildSaohuobangMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, cwbOrderWothDeliverystate,
					customer.getB2cEnum(), this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Benlaishenghuo.getKey()))) {// 本来

			return this.buildB2cDataMaster.getBenlaiB2cData().BuildBenlaiMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, cwbOrderWothDeliverystate,
					this.objectMapper);

		}
		// 保存B2C查询的内容，用于查询订单的最终状态 (目前只适用于梦芭莎)
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Moonbasa.getKey()))) {

			this.b2cDatasearchService.buildB2cData(cwbOrderWothDeliverystate, orderFlow, flowOrdertype, delivery_state, customer);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Liantong.getKey()))) {

			return this.buildB2cDataMaster.getBulidLiantongB2cData().buildB2cData(cwbOrderWothDeliverystate, orderFlow, cwbOrderWothDeliverystate.getCwbOrder(), flowOrdertype, delivery_state,
					customer);
		}
		// if(customer.getB2cEnum().equals(String.valueOf(B2cEnum.Wanxiang.getKey()))){
		// buildB2cDataMaster.getBuildWanxiangB2cData().BuildWanXiangMethod(orderFlow,
		// flowOrdertype,cwbOrderWothDeliverystate.getCwbOrder(),cwbOrderWothDeliverystate.getDeliveryState(),delivery_state,objectMapper);
		// }

		// if(customer.getB2cEnum().equals(String.valueOf(B2cEnum.Telecomshop.getKey()
		// ))){
		// buildB2cDataMaster.getBuildTelecomshopB2cData().BuildWanXiangMethod(orderFlow,
		// flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(),
		// cwbOrderWothDeliverystate.getDeliveryState(), delivery_state,
		// objectMapper);
		// }
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Huitongtx.getKey()))) {

			return this.buildB2cDataMaster.getBulidHuitongtxB2cData().BuildHuitongtxlMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, customer.getB2cEnum(),
					this.objectMapper);

		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Jiuxian.getKey()))) {

			return this.buildB2cDataMaster.getBuildJiuxianWangB2cData().BuildBenlaiMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, cwbOrderWothDeliverystate,
					this.objectMapper);

		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.MaiMaiBao.getKey()))) {

			return this.buildB2cDataMaster.getBuildmmbB2cData().BuildmmbMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state, cwbOrderWothDeliverystate,
					this.objectMapper);

		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Letv.getKey()))) {
			return this.buildB2cDataMaster.getBuildLetvB2cData().BuildLetvMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), cwbOrderWothDeliverystate.getDeliveryState(),
					delivery_state, this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.ChinaMobile.getKey()))) {
			return this.buildB2cDataMaster.getBuildChinamobileB2cData().BuildChinaMobileMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(),
					cwbOrderWothDeliverystate.getDeliveryState(), delivery_state, this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.YongHuics.getKey()))) {
			return this.buildB2cDataMaster.getBulidYonghuiB2cData().buildYonghuiMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate, delivery_state, customer.getB2cEnum(), this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Hxgdms.getKey()))) {
			return this.buildB2cDataMaster.getBulidHxgdmsB2cData().buildHxgdmsMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state,
					cwbOrderWothDeliverystate.getDeliveryState(), this.objectMapper);
		}

		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Wangjiu.getKey()))) {
			return this.buildB2cDataMaster.getBuildWangjiuB2cData().buildWangjiuMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state,
					cwbOrderWothDeliverystate.getDeliveryState(), this.objectMapper);
		}

		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.HomegoBJ.getKey()))) {
			return this.buildB2cDataMaster.getBuildHomegobjB2cData().buildHomegobjMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state,
					cwbOrderWothDeliverystate.getDeliveryState(), this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.LeChong.getKey()))) {
			return this.buildB2cDataMaster.getBuildLeChongB2cData().buildLeChongMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state,
					cwbOrderWothDeliverystate.getDeliveryState(), this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Zhongliang.getKey()))) {
			return this.buildB2cDataMaster.getBuildZhongliangB2cData().buildZhongliangMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state,
					cwbOrderWothDeliverystate.getDeliveryState(), this.objectMapper);
		}

		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.SFexpressXHM.getKey()))) {
			return this.buildB2cDataMaster.getBuildSfxhmB2cData().buildHomegobjMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state,
					cwbOrderWothDeliverystate.getDeliveryState(), this.objectMapper);
		}

		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Lefeng.getKey()))) {
			return this.buildB2cDataMaster.getBuildLefengB2cData().buildLefengMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state,
					cwbOrderWothDeliverystate.getDeliveryState(), this.objectMapper);
		}
		if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Guangzhoutonglu.getKey()))) {
			return this.buildB2cDataMaster.getBuildGztlB2cData().buildGztlMethod(orderFlow, flowOrdertype, cwbOrderWothDeliverystate.getCwbOrder(), delivery_state,
					cwbOrderWothDeliverystate.getDeliveryState(), customer,this.objectMapper);
		}
		return null;

	}

	/**
	 * 获取对接电商 的配置 支持同一类客户不同枚举的设置 如：唯品会，天猫，一号店等。
	 *
	 * @param customer
	 * @return
	 */
	public String getB2cEnumKeys(Customer customer, String constainsStr) {
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains(constainsStr)) {
				if (customer.getB2cEnum().equals(String.valueOf(enums.getKey()))) {
					return String.valueOf(enums.getKey());
				}
			}
		}
		return null;
	}

}
