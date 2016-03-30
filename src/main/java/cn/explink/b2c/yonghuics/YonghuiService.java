package cn.explink.b2c.yonghuics;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.service.CustomerService;

@Service
public class YonghuiService {
	private Logger logger = LoggerFactory.getLogger(YonghuiService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;

	@Autowired
	YonghuiInsertCwbDetailTimmer yihaodianTimmer;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
	@Autowired
	Yonghui_Master yonghui_Master;
	@Autowired
	CustomerService customerService;


	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Yonghui getYonghui(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Yonghui yh = (Yonghui) JSONObject.toBean(jsonObj, Yonghui.class);
		return yh;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Yonghui yh = new Yonghui();
		yh.setUserCode(request.getParameter("userCode"));
		yh.setExportCwb_pageSize(Integer.parseInt(request.getParameter("exportCwb_pageSize")));

		yh.setPrivate_key(request.getParameter("private_key"));
		yh.setCustomerids(request.getParameter("customerids"));
		yh.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		yh.setCallBackCount(Long.parseLong(request.getParameter("callBackCount")));
		yh.setIsopenDataDownload(Integer.parseInt(request.getParameter("isopenDataDownload")));
		yh.setLoopcount(Integer.parseInt(request.getParameter("loopcount")));
		yh.setDownload_URL(request.getParameter("download_URL"));
		yh.setCallback_URL(request.getParameter("callback_URL"));
		yh.setTrackLog_URL(request.getParameter("trackLog_URL"));

		String customerids = request.getParameter("customerids");
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(yh);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getYonghui(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public int getStateForYihaodian(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	/**
	 * 永辉超市定时器的总开关方法
	 */

	public void yonghuiInterfaceInvoke(int yhd_key) {
		if (getStateForYihaodian(yhd_key) == 0) {
			logger.info("未开启[永辉超市]对接key={}", yhd_key);
			return;
		}
		Yonghui yh = getYonghui(yhd_key);
		if (yh.getIsopenDataDownload() == 0) {
			logger.info("未开启[永辉超市]对接订单下载接口,key={}", yhd_key);
			return;
		}
		int loopcount = yh.getLoopcount() == 0 ? 5 : yh.getLoopcount();
		for (int i = 0; i < loopcount; i++) {
			yonghui_Master.getYonghuiService_download().DownLoadCwbDetailByYiHaoDian(yhd_key, i + 1); // 订单数据下载接口
			yonghui_Master.getYonghuiService_callBack().ExportCallBackByYiHaoDian(yhd_key, i + 1); // 下载成功回传接口

		}

		yihaodianTimmer.selectTempAndInsertToCwbDetail(yhd_key); // 临时表数据插入到detail中

	}

}
