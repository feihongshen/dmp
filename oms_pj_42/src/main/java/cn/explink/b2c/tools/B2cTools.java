package cn.explink.b2c.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ReasonTypeEnum;

@Component
public class B2cTools {
	private Logger logger = LoggerFactory.getLogger(B2cTools.class);
	@Autowired
	GetDmpDAO getdmpDAO;

	/**
	 * 获取b2c 配置信息的接口
	 * 
	 * @param key
	 * @return
	 */
	public JointEntity getObjectMethod(int key) {
		try {
			return getdmpDAO.getJointEntity(key);
		} catch (Exception e) {
			logger.warn("error while getting b2c entity with key {}, will defualt false", key);
			e.printStackTrace();
			return null;
		}
	}

	// 对接设置的开关判断
	public boolean isB2cOpen(int key) {
		try {
			JointEntity obj = getObjectMethod(key);
			return obj.getState() != 0;
		} catch (Exception e) {
			logger.warn("error while getting dangdang status with key {}, will defualt false", key);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据B2CId查询对应的 异常信息
	 * 
	 * @param reasontype
	 * @param leavedreasonid
	 * @param backreasonid
	 * @param customerids
	 * @return
	 */
	public ExptReason getExptReasonByB2c(long leavedreasonid, long backreasonid, String customerids, long deliverstate) {
		ExptReason exptreason = null;
		if (deliverstate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverstate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				|| deliverstate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return exptreason == null ? new ExptReason() : exptreason;
		}
		if (leavedreasonid != 0 && deliverstate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) { // 滞留
			exptreason = getdmpDAO.getExptCodeJointByB2c(leavedreasonid, ReasonTypeEnum.BeHelpUp.getValue(), customerids);
		} else if (backreasonid != 0
				&& (deliverstate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverstate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverstate == DeliveryStateEnum.ShangMenJuTui.getValue())) {
			exptreason = getdmpDAO.getExptCodeJointByB2c(backreasonid, ReasonTypeEnum.ReturnGoods.getValue(), customerids);
		}

		return exptreason == null ? new ExptReason() : exptreason;
	}

	public ExptReason getDiushiReasonByB2c(long diushireasonid, String customerids) {
		ExptReason exptreason = null;
		exptreason = getdmpDAO.getExptCodeJointByB2c(diushireasonid, ReasonTypeEnum.DiuShi.getValue(), customerids);
		return exptreason == null ? new ExptReason() : exptreason;
	}

	public ExptReason getWeishuakaReasonByB2c(long weishuakareasonid, String customerids) {
		ExptReason exptreason = null;
		exptreason = getdmpDAO.getExptCodeJointByB2c(weishuakareasonid, ReasonTypeEnum.WeiShuaKa.getValue(), customerids);
		return exptreason == null ? new ExptReason() : exptreason;
	}
	/**
	 * 广州通路对接查询对应的异常码
	 */
	public ExptReason getGztlExptReason(long customerid,String expt_code){
		ExptReason exptReason=null;
		exptReason=getdmpDAO.getExptCodeJointByB2cGztl(customerid, expt_code);
		return exptReason == null ? new ExptReason() : exptReason;
	}
}
