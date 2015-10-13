package cn.explink.b2c.yangguang;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class YangGuangService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GetDmpDAO getDmpDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;

	protected ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public YangGuang getYangGuangSettingMethod(int key) {
		YangGuang yangguang = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			yangguang = (YangGuang) JSONObject.toBean(jsonObj, YangGuang.class);
		} else {
			yangguang = new YangGuang();
		}
		return yangguang;
	}

	/**
	 * 获取[央广]配置信息的接口
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
	 * 获取央广不同的配置的信息
	 * 
	 * @param key
	 * @return
	 */
	public List<YangGuangdiff> getYangGuangDiffs(int key) {

		List<YangGuangdiff> list = new ArrayList<YangGuangdiff>();
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		YangGuang yangguang = (YangGuang) JSONObject.toBean(jsonObj, YangGuang.class);
		String multidiffs = yangguang.getMultiInfos();
		JSONArray array = JSONArray.fromObject(multidiffs);
		for (int i = 0; i < array.size(); i++) {
			try {
				YangGuangdiff diff = jacksonmapper.readValue(array.get(i).toString(), YangGuangdiff.class);
				list.add(diff);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * 获取央广不同的配置的信息 过滤有用的并且开启的配置
	 * 
	 * @param key
	 * @return 查询出 已开启的配置
	 */
	public List<YangGuangdiff> filterYangGuangDiffs(List<YangGuangdiff> list) {
		List<YangGuangdiff> difflist = new ArrayList<YangGuangdiff>();
		for (YangGuangdiff diff : list) {
			if (diff.getIsopen() == 0) {
				continue;
			}
			difflist.add(diff);
		}

		return difflist;
	}

	/**
	 * 获取央广需要存储的流程状态
	 * 
	 * @param flowordertype
	 * @param deliverystate
	 * @return
	 */
	public YangGuangFlowEnum getYangGuangFlowEnum(long flowordertype, long deliverystate, String cwbordertypeid) {

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return YangGuangFlowEnum.FenZhanZhiLiu;
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui
						.getValue())) {
			return YangGuangFlowEnum.JuShou;
		}

		return null;

	}

}
