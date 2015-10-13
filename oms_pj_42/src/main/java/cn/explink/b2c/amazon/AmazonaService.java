package cn.explink.b2c.amazon;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.amazon.domain.Amazon;
import cn.explink.b2c.gome.Gome;
import cn.explink.b2c.gome.GomeFlowEnum;
import cn.explink.b2c.gome.GomeOrderTypeEnum;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class AmazonaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GetDmpDAO getDmpDAO;

	public Amazon getAmazonSettingMethod(int key) {
		Amazon amazon = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			amazon = (Amazon) JSONObject.toBean(jsonObj, Amazon.class);
		} else {
			amazon = new Amazon();
		}
		return amazon;
	}

	/**
	 * 获取[亚马逊]配置信息的接口
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
	 * 获取亚马逊需要存储的流程状态
	 * 
	 * @param flowordertype
	 * @param deliverystate
	 * @param cwbordertypeid
	 * @return
	 */
	public String getAmazonFlowEnum(long flowordertype, long deliverystate, String cwbordertypeid) {
		if (flowordertype == FlowOrderTypeEnum.BaoGuoweiDao.getValue()) {
			return AmazonFlowEnum.BaoGuoweiDao.getAmazon_code();
		}
		if (flowordertype == FlowOrderTypeEnum.ZhongZhuanyanwu.getValue()) {
			return "SD";
		}
		if (flowordertype == FlowOrderTypeEnum.ShouGongdiushi.getValue()) {
			return "CA";
		}
		if (cwbordertypeid.equals(CwbOrderTypeIdEnum.Shangmentui.getValue() + "")
				&& (flowordertype == FlowOrderTypeEnum.RuKu.getValue() || flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue())) {
			return AmazonFlowEnum.ShanmenTuiDaoru.getAmazon_code();
		}
		if (cwbordertypeid.equals(CwbOrderTypeIdEnum.Peisong.getValue() + "") && flowordertype == FlowOrderTypeEnum.RuKu.getValue()) {
			return AmazonFlowEnum.RuKu.getAmazon_code();
		}
		if (cwbordertypeid.equals(CwbOrderTypeIdEnum.Peisong.getValue() + "") && flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
			return AmazonFlowEnum.FenzhanDaohuoSaomiao.getAmazon_code();
		}
		if (cwbordertypeid.equals(CwbOrderTypeIdEnum.Peisong.getValue() + "") && flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			return AmazonFlowEnum.FenPeiXiaoJianYuan.getAmazon_code();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return AmazonFlowEnum.TuoTou.getAmazon_code();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
			return AmazonFlowEnum.ShanmenTuichenggong.getAmazon_code();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue() || (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() && (CwbOrderTypeIdEnum.Shangmenhuan.getValue() + "")
						.equals(cwbordertypeid)))) {
			return AmazonFlowEnum.Quhuoshibai.getAmazon_code();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
			return AmazonFlowEnum.Jushou.getAmazon_code();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			return AmazonFlowEnum.Diushi.getAmazon_code();
		}
		if (cwbordertypeid.equals(CwbOrderTypeIdEnum.Shangmentui.getValue() + "") && flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return AmazonFlowEnum.ShanmenTuizhiliu.getAmazon_code();
		}
		if (cwbordertypeid.equals(CwbOrderTypeIdEnum.Peisong.getValue() + "") && flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return AmazonFlowEnum.Fenzhanzhiliu2.getAmazon_code();
		}

		if (flowordertype == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			return AmazonFlowEnum.TuiHuozhanRuku.getAmazon_code();
		}
		if (flowordertype == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
			return AmazonFlowEnum.TuiGongYingShangChuKu.getAmazon_code();
		}

		return null;

	}

	public String getAmazonZitiFlowEnum(long flowordertype, long deliverystate) {
		if (flowordertype == FlowOrderTypeEnum.BaoGuoweiDao.getValue()) {
			return "SD_AY";
		}
		if (flowordertype == FlowOrderTypeEnum.RuKu.getValue()) {
			return "AF_NS";
		}
		if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
			return "AV_NS";
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return "D1_NS";
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
			return "RJ";
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			return "CA";
		}

		if (flowordertype == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			return "AR_N4";
		}
		if (flowordertype == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
			return "DR_N5";
		}
		if (flowordertype == FlowOrderTypeEnum.ZiTiYanWu.getValue()) {
			return "A3_AQ";
		}
		return null;

	}

}
