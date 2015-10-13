package cn.explink.b2c.Wholeline;

import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CommonSendDataDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.Common;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.util.DateTimeUtil;

/**
 * 查询承运商出库确认表插入到轮询表中
 * 
 * @author Administrator
 *
 */
@org.springframework.stereotype.Service
public class WholeLineService {
	@Autowired
	WholeLineDao wholeLineDao;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	GetDmpDAO getdmpDAO;
	@Autowired
	CommonSendDataDAO commonSendDataDAO;
	@Autowired
	WarehouseCommenDAO warehouseCommenDAO;
	private Logger logger = LoggerFactory.getLogger(WholeLineController.class);

	/**
	 * 轮偱order表到新表
	 * 
	 * @param key
	 *            b2c中的key
	 * @param whole
	 *            wholeLine对象
	 */
	public void getInfoCall() {

		if (!isWholeLineOpen(B2cEnum.wholeLine.getKey())) {
			logger.info("未开启全线快递对接-检索oms承运商确认出库表");
			return;
		}
		WholeLine whole = getWholeline(B2cEnum.wholeLine.getKey());

		Common common = getdmpDAO.getCommonByCommonnumber(whole.getUsercode());
		if (common == null) {
			logger.info("全线快递轮循时得到common是空，usercode={}", whole.getUsercode());
			return;
		}

		long count1 = warehouseCommenDAO.getCommenCwbListByCommencodeCount(whole.getUsercode());
		if (count1 == 0) {
			logger.info("全线查询-oms承运商确认出库表没有数据");
			return;
		}

		long page = common.getPageSize();
		if (page == 0) {
			page = 100;
		}
		int i = (int) (count1 / page);
		int m = (int) (count1 % page);
		if (m > 0 || i == 0) {
			i = i + 1;
		}

		long success = 0;
		for (int j = 0; j < i; j++) {
			long countAll = (j * page);
			countAll = countAll - success;
			List<WarehouseToCommen> list = warehouseCommenDAO.getCommenCwbListByCommencodes(countAll, "'" + whole.getUsercode() + "'", page);
			logger.info("全线快递轮循时得到order表中全线快递的list,大小={}", list.size());
			for (WarehouseToCommen comm : list) {
				try {

					WholelineSearch xmlnote = new WholelineSearch();
					xmlnote.setCwb(comm.getCwb());
					xmlnote.setCustomerid(String.valueOf(comm.getCustomerid()));
					xmlnote.setCretime(comm.getCredate());
					xmlnote.setRemark(comm.getRemark());
					long count = wholeLineDao.getCountWholeline(comm.getCwb());
					if (count > 0) {
						logger.info("全线轮询表已存在该订单{}", comm.getCwb());
						warehouseCommenDAO.updateCommenCwbListBycwbs("'" + comm.getCwb() + "'", DateTimeUtil.getNowTime());
						success++;
						continue;
					}
					wholeLineDao.saveWholeline(xmlnote);
					warehouseCommenDAO.updateCommenCwbListBycwbs("'" + comm.getCwb() + "'", DateTimeUtil.getNowTime());
					success++;
					logger.info("查询承运商出库确认表存储至轮询表成功，订单号={}", comm.getCwb());

				} catch (Exception e) {
					logger.error("轮循时存入全线快递新表异常cwb=" + comm.getCwb(), e);
				}
			}
		}

	}

	private boolean isWholeLineOpen(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getState() != 0;
		} catch (Exception e) {
			logger.warn("error while getting dangdang status with key {}, will defualt false", key);
			e.printStackTrace();
		}
		return false;
	}

	private void getJsonToObjectTwo(WholeLine whole, JsonContext context, OrderFlowDto dto) {
		dto.setCustid("");
		dto.setCwb(context.getWaybillNo());
		dto.setFloworderdetail(context.getContentValue());// 操作描述
		dto.setOperatortime(context.getDisOperTm());// 操作时间
		dto.setRequestTime(DateTimeUtil.getNowTime());// 请求时间
		dto.setUserCode(whole.getUsercode());
	}

	/**
	 * 循环得出是否为需要的状态
	 * 
	 * @param value
	 *            操作码
	 * @return boolean
	 */
	public boolean getByValue(int value) {
		for (EXPTmsgEnum Enum : EXPTmsgEnum.values()) {
			if (value == Enum.getValue()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 查询拒收异常码
	 * 
	 * @param a
	 *            页面设置的拒收数组
	 * @param code
	 *            得到的异常码
	 * @return boolean
	 */
	public boolean getBooleanByCode(String[] a, String code) {
		for (String b : a) {
			if (b.equals(code)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 得到dmp的whole实体
	 * 
	 * @param key
	 *            b2c设置的key
	 * @return WholeLine对象
	 */
	public WholeLine getWholeline(int key) {
		WholeLine wholeline = new WholeLine();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			wholeline = (WholeLine) JSONObject.toBean(jsonObj, WholeLine.class);
		} else {
			wholeline = new WholeLine();
		}
		return wholeline == null ? new WholeLine() : wholeline;
	}

	private String getObjectMethod(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
