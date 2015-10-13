package cn.explink.b2c.rufengda;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class RufengdaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GetDmpDAO getDmpDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;

	public Rufengda getRufengdaSettingMethod(int key) {
		Rufengda rufengda = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			rufengda = (Rufengda) JSONObject.toBean(jsonObj, Rufengda.class);
		} else {
			rufengda = new Rufengda();
		}
		return rufengda;
	}

	/**
	 * 获取[如风达]配置信息的接口
	 * 
	 * @param key
	 * @return
	 */
	public String getObjectMethod(int key) {
		try {
			JointEntity obj = getDmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取如风达需要存储的流程状态
	 * 
	 * @param flowordertype
	 * @param deliverystate
	 * @return
	 */
	public String getRufengdaFlowEnum(long flowordertype, long deliverystate, String cwbordertypeid) {
		if (flowordertype == FlowOrderTypeEnum.RuKu.getValue() && CwbOrderTypeIdEnum.Shangmentui.getValue() != Integer.valueOf(cwbordertypeid)) {
			return RufengdaFlowEnum.RuKu.getRfd_code() + "";
		}

		if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() && CwbOrderTypeIdEnum.Shangmentui.getValue() == Integer.valueOf(cwbordertypeid)) {
			return RufengdaFlowEnum.RuZhanFenZhan.getRfd_code() + "";
		}

		if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			return RufengdaFlowEnum.FenPeiXiaoJianYuan.getRfd_code() + "";
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return RufengdaFlowEnum.TuoTou.getRfd_code() + "";
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return RufengdaFlowEnum.TuoTou.getRfd_code() + "";
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			return RufengdaFlowEnum.TuoTou.getRfd_code() + "";
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return RufengdaFlowEnum.FenZhanZhiLiu.getRfd_code() + "";
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui
						.getValue())) {
			return RufengdaFlowEnum.JuShou.getRfd_code() + "";
		}

		return null;

	}

	/**
	 * 根据自己的订单类型转化为如风达的订单类型
	 * 
	 * @return 默认返回普通类型
	 */
	public int getRufengdaOrderTypeByOnwer(int ordertypeid) {
		for (RufengdaOrderTypeEnum en : RufengdaOrderTypeEnum.values()) {
			if (en.getOnwer_value() == ordertypeid) {
				return en.getValue();
			}
		}
		return 0;
	}

}
