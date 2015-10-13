package cn.explink.b2c.vipshop;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.core_up.EpaiCoreService_Receiver;
import cn.explink.b2c.tmall.TmallService;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.CacheBaseListener;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidVipShopB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidVipShopB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	TmallService tmallService;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	VipShopCwbFeedBackService vipshopService;
	@Autowired
	WarehouseCommenDAO warehouseCommenDAO;
	@Autowired
	EpaiCoreService_Receiver epaiCoreService_Receiver;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	CacheBaseListener cacheBaseListener;

	public String BuildVipShopMethod(String b2cenum, DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, long delivery_state, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {
		
		String sign_man = "";
		String sign_man_phone = "";
		boolean allographSignFlag = false;//默认本人签收
		
		String VipShopReceiveStatus = vipshopService.getVipShopFlowEnum(flowOrdertype, delivery_state, Long.valueOf(cwbOrder.getCwbordertypeid()));
		
		if (VipShopReceiveStatus == null) {
			logger.info("订单号：{} 不属于 唯品会所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		}
		if (cwbOrder.getCwbstate() == 2 && !"-2".equals(VipShopReceiveStatus) && !"5".equals(VipShopReceiveStatus)
				&& Long.valueOf(cwbOrder.getCwbordertypeid()) == CwbOrderTypeIdEnum.Peisong.getValue()) {
			logger.info("订单号：{} 隔离退货之后的流程{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		}

		else {

			logger.info("订单号：{}封装成唯品会所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);

			VipShopXMLNote vipshopXMLNote = new VipShopXMLNote();
			vipshopXMLNote.setOrder_sn(cwbOrder.getCwb());
			vipshopXMLNote.setOrder_status(VipShopReceiveStatus);
			vipshopXMLNote.setCwbordertypeid(Long.valueOf(cwbOrder.getCwbordertypeid()));

			if (Long.valueOf(cwbOrder.getCwbordertypeid()) == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				if (VipShopReceiveStatus.equals(String.valueOf(VipShopFlowEnum.ShangMenTuiChengGong_t.getVipshop_state()))
						|| VipShopReceiveStatus.equals(String.valueOf(VipShopFlowEnum.ShengMenJuTui_t.getVipshop_state()))) {
					String details = filterGoodDetails(cwbOrder, delivery_state);
					vipshopXMLNote.setDetails(details);
					
					 if(VipShopReceiveStatus.equals(String.valueOf(VipShopFlowEnum.ShengMenJuTui_t.getVipshop_state()))){
						 vipshopXMLNote.setGoods_reason(cwbOrder.getBackreason());
					 }
					
				}
				vipshopXMLNote.setShangmenlanshoutime(deliveryState != null ? deliveryState.getShangmenlanshoutime() : "");

				if (VipShopReceiveStatus.equals(String.valueOf(VipShopFlowEnum.FenZhanLingHuo_t.getVipshop_state()))) {
					// User
					// deliverUser=getDmpdao.getUserById(cwbOrder.getDeliverid());
					User deliverUser = cacheBaseListener.getUser(cwbOrder.getDeliverid());

					String delivername = deliverUser.getRealname();
					String usermobile = deliverUser.getUsermobile();
					vipshopXMLNote.setDeliver_name(delivername);
					vipshopXMLNote.setDeliver_mobile(usermobile);
				}

			}
			
			/*
			 * 必然反馈
			 */
			if (deliveryState != null && deliveryState.getDeliverystate() != 0) {
				String currenttime = deliveryState.getDeliverytime() != null && !deliveryState.getDeliverytime().isEmpty() ? deliveryState.getDeliverytime() : DateTimeUtil.getNowTime();
				String shouldtime = "";
				try {// 以反馈时间作为推送唯品会的时间
					shouldtime = deliveryState.getSign_time() != null && !deliveryState.getSign_time().isEmpty() ? deliveryState.getSign_time() : currenttime;

				} catch (Exception e) {
					logger.error("唯品会提前时间未知异常", e);
					shouldtime = currenttime;
				}

				vipshopXMLNote.setOrder_status_time(shouldtime);
			} else {
				vipshopXMLNote.setOrder_status_time(DateTimeUtil.formatDate(orderFlow.getCredate()));
			}
			vipshopXMLNote.setVersion("1.0");

			
			/*
			 * vip 全部退货、部分退货
			 */
			if ((delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue()) || (delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
				ExptReason exptReason = this.b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), String.valueOf(cwbOrder.getCustomerid()), delivery_state);
				String expt_msg = ((exptReason.getExpt_msg() == null) || exptReason.getExpt_msg().equals("")) ? "其他原因" : exptReason.getExpt_msg();
				
				String is_unpacked ="0";
				VipShop vipshop = this.vipshopService.getVipShopSettingMethod(Integer.valueOf(b2cenum)); // 获取配置信息
				if(vipshop.getResuseReasonFlag()==1){ //回传拒收状态为空
					vipshopXMLNote.setOrder_status_info("");
				}else{
					try {
						 is_unpacked =expt_msg.contains("_")?expt_msg.substring(0,1):"0";
						 vipshopXMLNote.setOrder_status_info(expt_msg.substring(expt_msg.indexOf("_")+1));
					} catch (Exception e) {
						logger.error("vipshop拒收单维护错误cwb="+orderFlow.getCwb()+",expt_msg="+expt_msg,e);
						vipshopXMLNote.setOrder_status_info(expt_msg);
					}
				}
				
				vipshopXMLNote.setIs_unpacked(is_unpacked);  //是否开箱验货(0未开箱、1已开箱) 格式： 0_拒收不要   1_不喜欢
			}  
			/*
			 * vip 分站滞留
			 */
			else if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) { 
				/**
				 * 20131105
				 * 唯品会新增
				 * 滞留状态一定要存储编码，拒收不需要
				 */
				ExptReason exptReason = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), 0, String.valueOf(cwbOrder.getCustomerid()), delivery_state);
				String expt_msg = exptReason.getExpt_msg();
				String expt_code = exptReason.getExpt_code();
				if (expt_code == null || expt_code.isEmpty()) { // 如果不满足匹配条件
					return null;
				}

				vipshopXMLNote.setOrder_status_info(expt_msg);
				vipshopXMLNote.setOrder_status(expt_code);

			}
			/*
			 * vip 配送成功
			 */
			else if(delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue()){
				
				if( cwbOrder.getConsigneename()!= null && deliveryState.getSign_man() != null ){
					allographSignFlag = !cwbOrder.getConsigneename().equals(deliveryState.getSign_man());
					sign_man =deliveryState.getSign_man();
				}else{
					sign_man = "本人";
				}
				
				sign_man_phone = "";
				
			}
			/*
			 * vip 公共情况
			 */
			else {
				vipshopXMLNote.setOrder_status_info(orderFlowDetail.getDetail(orderFlow) == null ? "成功" : orderFlowDetail.getDetail(orderFlow));
			}
			String currentCityName = cacheBaseListener.getBranch(orderFlow.getBranchid()).getBranchname() == null ? "分站" : cacheBaseListener.getBranch(orderFlow.getBranchid()).getBranchname();

			vipshopXMLNote.setCurrent_city_name(currentCityName);
			vipshopXMLNote.setSign_man("5".equals(VipShopReceiveStatus) ? sign_man : "");
			vipshopXMLNote.setSign_man_phone("5".equals(VipShopReceiveStatus) ? sign_man_phone : "");

			// 33状态

			if ("33".equals(VipShopReceiveStatus)) {
				// User
				// deliverUser=getDmpdao.getUserById(cwbOrder.getDeliverid());
				User deliverUser = cacheBaseListener.getUser(cwbOrder.getDeliverid());
				String delivername = deliverUser.getRealname();
				String usermobile = deliverUser.getUsermobile();
				try {
					if (delivername != null && delivername.length() > 4) {
						delivername = delivername.substring(0, 4);
					}
					if (usermobile != null && usermobile.length() > 12) {
						usermobile = usermobile.substring(0, 12);
					}
				} catch (Exception e) {
				}

				String message = "您的订单:" + cwbOrder.getCwb() + "已到配送站点。详情请联系配送员：" + delivername + "，电话：" + usermobile + "。请留意收件";
				vipshopXMLNote.setOrder_status_info(message);
				vipshopXMLNote.setDeliverUser(deliverUser.getRealname());
				vipshopXMLNote.setDeliverMobile(deliverUser.getUsermobile());
				vipshopXMLNote.setDeliverBranch(getBranchById(orderFlow.getBranchid()).getBranchname());

			}
			
			if (flowOrdertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
				List<Common> comList = epaiCoreService_Receiver.getCommonList();
				if (comList != null && comList.size() > 0) {
					for (Common common : comList) {
						if (common.getBranchid() > 0 && cwbOrder.getNextbranchid() == common.getBranchid()) {
							vipshopXMLNote.setOrder_status_info("因超区,转" + common.getCommonname() + ",唯品会订单号：" + cwbOrder.getCwb() + ",承运商查单电话：" + common.getPhone());
							break;
						}

					}
				}
			}

			if (vipshopXMLNote.getOrder_status_info() == null) {
				vipshopXMLNote.setOrder_status_info("处理完成");
			}
			
			if( "5".equals(VipShopReceiveStatus)){
				if(sign_man_phone.isEmpty()&&allographSignFlag){ //如果他人签收并且电话是空的，则直接默认派送员电话
					sign_man_phone=cacheBaseListener.getUser(cwbOrder.getDeliverid()).getUsermobile();
				}
				
				vipshopXMLNote.setSign_man(deliveryState.getSign_man());
				vipshopXMLNote.setSign_man_phone(sign_man_phone);
				vipshopXMLNote.setIs_allograph_sign(allographSignFlag ? 1:0);
			}

			logger.info("订单号：{}封装成唯品会所需要的json----结束,状态：{}", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(vipshopXMLNote);
		}
	}

	private String filterGoodDetails(DmpCwbOrder cwbOrder, long delivery_state) throws IOException, JsonParseException, JsonMappingException, JsonGenerationException {
		String goodDetails = getDmpdao.getOrderGoods(cwbOrder.getCwb());

		List<OrderGoods> orderGoodslist = JacksonMapper.getInstance().readValue(goodDetails, new TypeReference<List<OrderGoods>>() {
		});
		if (orderGoodslist != null && orderGoodslist.size() > 0) {
			for (OrderGoods good : orderGoodslist) {
				if (delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
					if (good.getShituicount() == 0 && good.getWeituicount() == 0) {
						good.setShituicount(Integer.valueOf(good.getGoods_num()));
					}
				}

			}
		}
		String details = JacksonMapper.getInstance().writeValueAsString(orderGoodslist);
		return details;
	}

	private Branch getBranchById(long branchid) {
		return cacheBaseListener.getBranch(branchid);
	}

}
