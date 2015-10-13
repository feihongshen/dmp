package cn.explink.b2c.gome;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class GuomeiService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GetDmpDAO getDmpDAO;

	public Gome getGomeSettingMethod(int key) {
		Gome gome = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			gome = (Gome) JSONObject.toBean(jsonObj, Gome.class);
		} else {
			gome = new Gome();
		}
		return gome;
	}

	/**
	 * 获取[国美]配置信息的接口
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
	 * 获取国美需要存储的流程状态
	 * 
	 * @param flowordertype
	 * @param deliverystate
	 * @param cwbordertypeid
	 * @return
	 */
	public String getRufengdaFlowEnum(long flowordertype, long deliverystate, String cwbordertypeid) {
		if ((CwbOrderTypeIdEnum.Peisong.getValue() + "").equals(cwbordertypeid)) {// 正向物流订单
			if (flowordertype == FlowOrderTypeEnum.RuKu.getValue()) {
				return GomeFlowEnum.RuKu.getGome_code();
			}
			if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				return GomeFlowEnum.FenzhanDaohuoSaomiao.getGome_code();
			}
			if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				return GomeFlowEnum.FenPeiXiaoJianYuan.getGome_code();
			}
			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) {
				return GomeFlowEnum.TuoTou.getGome_code();
			}
			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && (deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue())) {
				return GomeFlowEnum.JuShou.getGome_code();
			}
			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				return GomeFlowEnum.FenZhanZhiLiu.getGome_code();
			}
			if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue())
					|| flowordertype == FlowOrderTypeEnum.YiChangDingDanChuLi.getValue()) {
				return GomeFlowEnum.Diushi.getGome_code();
			}

			if (flowordertype == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
				return GomeFlowEnum.ShangMentuiChengGong.getGome_code();
			}
			if (flowordertype == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
				return GomeFlowEnum.TuiHuoChuZhan.getGome_code();
			}

		} else {// 逆向物流订单
			if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue() || flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				return GomeFlowEnum.ShangmenTuilinghuo.getGome_code();
			}
			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
				return GomeFlowEnum.ShangMentuiChengGong.getGome_code();
			}
			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
				return GomeFlowEnum.ShangMentuiChengGong.getGome_code();
			}
			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && (deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue())) {
				return GomeFlowEnum.ShangMentuiJutui.getGome_code();
			}
			if (flowordertype == FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue()) {
				return GomeFlowEnum.TuiHuoChuZhan.getGome_code();
			}
			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue()) {
				return GomeFlowEnum.ShangMentuiJutui.getGome_code();
			}
			if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue())
					|| flowordertype == FlowOrderTypeEnum.YiChangDingDanChuLi.getValue()) {
				return GomeFlowEnum.Diushi.getGome_code();
			}
		}

		return null;

	}

	/**
	 * 根据自己的订单类型转化为国美的订单类型
	 * 
	 * @return 默认返回普通类型
	 */
	public int getRufengdaOrderTypeByOnwer(int ordertypeid) {
		for (GomeOrderTypeEnum en : GomeOrderTypeEnum.values()) {
			if (en.getOnwer_value() == ordertypeid) {
				return en.getValue();
			}
		}
		return 0;
	}

}
