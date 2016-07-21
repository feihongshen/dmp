package cn.explink.b2c.tps;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.auto.order.service.AutoExceptionService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.core.utils.SpringContextUtils;
import cn.explink.dao.CwbDAO;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.enumutil.AutoInterfaceEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TranscwbView;
import cn.explink.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPServiceHelper;
import com.pjbest.deliveryorder.dmp.service.CarrierOrderStatusRequest;
import com.pjbest.deliveryorder.dmp.service.CarrierOrderStatusResponse;

/**
 * DMP从TPS获取运单状态定时器
 * @author yurong.liang 2015/12/19
 */
@Service("tPSCarrierOrderStatusTimmer")
public class TPSCarrierOrderStatusTimmer {
	private Logger logger = LoggerFactory.getLogger(TPSCarrierOrderStatusTimmer.class);
	
	@Autowired
	SpringContextUtils springContextUtils;
	@Autowired
	private JiontDAO jiontDAO;
	@Autowired
	private JointService jointService;
	@Autowired
	private TransCwbDao transCwbDao;
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private AutoExceptionService autoExceptionService;
	

	/**
	 * 获取物流运单状态方法
	 */
	@SuppressWarnings("static-access")
	@Transactional
	public void getCarrierOrderStatus() {
		JointEntity jointEntity = jointService.getObjectMethod(B2cEnum.TPS_CarrierOrderStatus.getKey());
		if (jointEntity.getState() == 0) {// 未开启唯品会_TPS_运单状态接口对接
			logger.info("未开启" + B2cEnum.TPS_CarrierOrderStatus.getText() + "["
					+ B2cEnum.TPS_CarrierOrderStatus.getKey() + "]对接！");
			return;
		}

		ApplicationContext context = springContextUtils.getContext();
		TPSCarrierOrderStatusTimmer tPSCarrierOrderStatusTimmer=(TPSCarrierOrderStatusTimmer)context.getBean("tPSCarrierOrderStatusTimmer");
		// 获取配置信息
		TPSCarrierOrderStatus property= this.getCarrierOrderStatusProperty(jointEntity);
		if(property.getGetMaxCount()==0){
			logger.info(B2cEnum.TPS_CarrierOrderStatus.getText()+"接口对接设置每次获取订单数为0,不从TPS获取运单状态数据！");
			return;
		}
		List<CarrierOrderStatusResponse> carrierOrderStatusList=null;
		CarrierOrderStatusRequest request=null;
		try {
			PjDeliveryOrder4DMPService pdomService = new PjDeliveryOrder4DMPServiceHelper.PjDeliveryOrder4DMPServiceClient();
			request = new CarrierOrderStatusRequest();
			request.setCarrierCode(property.getShipper_no());
			request.setCount(property.getGetMaxCount());
			request.setSeq(property.getSeq());
			
			this.logger.info("从TPS获取物流状态数据请求参数：CarrierCode={},Count={},Seq={}", property.getShipper_no(), property.getGetMaxCount(), property.getSeq());
			
			// 调用接口方法获得运单状态数据
			carrierOrderStatusList = pdomService.getCarrierOrderStatus(request);
			
		} catch (Exception e) {
			logger.error("从TPS获取物流状态数据异常",e);
			return;
		}
		
		if (carrierOrderStatusList == null|| carrierOrderStatusList.size() == 0) {
			logger.info("已从tps获取完最新的运单状态数据");
			return;
		}
		
		for (int i = 0; i < carrierOrderStatusList.size(); i++) {
			CarrierOrderStatusResponse response = carrierOrderStatusList.get(i);
			logger.info("tps返回运单状态数据："+JSON.toJSON(response).toString());
			String cwb = response.getCustOrderNo();
			String transcwb = response.getTransportNo();
			if(StringUtil.isEmpty(transcwb)||StringUtil.isEmpty(cwb)){
				//保存tps返回的异常数据
				cwb=cwb==null?"":cwb;
				transcwb=transcwb==null?"":transcwb;
				long msgid= this.autoExceptionService.createAutoExceptionMsg(JSON.toJSON(response).toString(), AutoInterfaceEnum.insertTransportNo.getValue());
				this.autoExceptionService.createAutoExceptionDetail(cwb, transcwb, "无订单号或者不存在运单号", AutoExceptionStatusEnum.xinjian.getValue(), msgid, 0);
				logger.error("tps返回运单状态数据中无订单号或者不存在运单号,返回数据："+JSON.toJSON(response).toString());
			}else{
				try {
					// 录入运单号
					tPSCarrierOrderStatusTimmer.boundTranscwb(response);
				} catch (Exception e) {
					//保存录入运单号时出现异常的运单状态数据
					long msgid= this.autoExceptionService.createAutoExceptionMsg(JSON.toJSON(response).toString(), AutoInterfaceEnum.insertTransportNo.getValue());
					this.autoExceptionService.createAutoExceptionDetail(cwb, transcwb,e.getMessage(), AutoExceptionStatusEnum.xinjian.getValue(), msgid, 0);
					logger.error("录入运单号异常:"+e.getMessage()+" 返回运单状态数据:"+JSON.toJSON(response).toString());
				}
			}
			
			//更新seq
			property.setSeq(Integer.parseInt(response.getId()+""));
			String joint_property = JSONObject.fromObject(property).toString();
			jointEntity.setJoint_property(joint_property);
			jiontDAO.Update(jointEntity);
		}
	}

	/**
	 * 上门退成功，录入快递单号 兼容多个快递单号，多个快递单号用","隔开
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void boundTranscwb(CarrierOrderStatusResponse response) {
		if (response.getStatus() == 1 && response.getType() == 1) {//揽收类型并且成功状态数据 
			String transcwb=response.getTransportNo();
			String cwb=response.getCustOrderNo();
			String[] transcwbArr = transcwb.split(",");
			List<String> transcwbList = new ArrayList<String>();
			for (String transcwbTmp : transcwbArr) {
				if (transcwbTmp.length() > 0) {
					transcwbList.add(transcwbTmp);
				}
			}
	
			if (transcwbList.size() > 0) {// 有运单号
				this.transCwbDao.deleteTranscwb(cwb);// 删除与该订单关联的所有运单号
				for (String transcwbTmp : transcwbList) {
					// 查询该运单号是否已被绑定
					List<TranscwbView> transcwbViewList = this.transCwbDao.getcwbBytranscwb(transcwbTmp);
					if ((transcwbViewList != null) && (transcwbViewList.size() > 0)) {
						if(transcwbTmp.equals(cwbDAO.getOneCwbOrderByCwb(cwb).getTranscwb())) {//亚马逊及类似的情况，运单号是对方的主键，因此可能再次出现在退货时
							continue;
						}
						throw new CwbException(cwb,FlowOrderTypeEnum.YiFanKui.getValue(),ExceptionCwbErrorTypeEnum.FANKUI_KUAIDIDANHAO_YIGUANLIAN);
					}
					// 保存运单与订单关联关系
					this.transCwbDao.saveTranscwb(transcwbTmp, cwb);
				}
				this.transCwbDao.saveTranscwb(cwb, cwb);
				/** mod begin by yurong.liang 2016-7-21 **/
				//this.cwbDAO.saveTranscwbByCwb(transcwb, cwb);//更新运单号
				this.cwbDAO.saveTranscwbAndTpsTranscwbByCwb(transcwb, cwb);
				/************** mod end *****************/
				this.cwbDAO.saveBackcarnum(transcwbList.size(),cwb);//更新取货数量
				this.cwbDAO.saveSendcarnum(transcwbList.size(),cwb);//更新发货数量
			}
		}
	}

	/**
	 * 获取配置信息
	 */
	public TPSCarrierOrderStatus getCarrierOrderStatusProperty(JointEntity jointEntity) {
		String objectJson = jointEntity.getJoint_property();
		TPSCarrierOrderStatus carrierOrderStatus = null;
		if (objectJson != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectJson);
			carrierOrderStatus = (TPSCarrierOrderStatus) JSONObject.toBean(jsonObj, TPSCarrierOrderStatus.class);
		} else {
			carrierOrderStatus = new TPSCarrierOrderStatus();
		}
		return carrierOrderStatus;
	}
}
