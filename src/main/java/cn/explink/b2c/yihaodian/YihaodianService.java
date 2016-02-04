package cn.explink.b2c.yihaodian;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.happyGo.FunctionForHappy;
import cn.explink.b2c.happyGo.HappyGo;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.util.DateTimeUtil;

@Service
public class YihaodianService {
	private Logger logger = LoggerFactory.getLogger(YihaodianService.class);
	@Autowired
	RestTemplateClient restTemplate;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	Yihaodian_Master yihaodian_Master;
	@Autowired
	YihaodianInsertCwbDetailTimmer yihaodianTimmer;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;

	public String getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Yihaodian getYihaodian(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		Yihaodian Yihaodian = (Yihaodian) JSONObject.toBean(jsonObj, Yihaodian.class);
		return Yihaodian;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Yihaodian yihaodian = new Yihaodian();
		yihaodian.setUserCode(request.getParameter("userCode"));
		yihaodian.setYwUserCode(request.getParameter("ywUserCode"));
		yihaodian.setExportCwb_pageSize(Integer.parseInt(request.getParameter("exportCwb_pageSize")));
		yihaodian.setExportCwb_URL(request.getParameter("exportCwb_URL"));
		yihaodian.setDeliveryResult_URL(request.getParameter("deliveryResult_URL"));
		yihaodian.setTrackLog_URL(request.getParameter("trackLog_URL"));
		yihaodian.setPrivate_key(request.getParameter("private_key"));
		yihaodian.setCustomerids(request.getParameter("customerids"));
		yihaodian.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		yihaodian.setUpdatePayResult_URL(request.getParameter("updatePayResult_URL"));
		yihaodian.setExportSuccess_URL(request.getParameter("exportSuccess_URL"));
		yihaodian.setCallBackCount(Long.parseLong(request.getParameter("callBackCount")));

		yihaodian.setIsopenDataDownload(Integer.parseInt(request.getParameter("isopenDataDownload")));
		yihaodian.setLoopcount(Integer.parseInt(request.getParameter("loopcount")));

		yihaodian.setIsopenywaddressflag(Integer.parseInt(request.getParameter("isopenywaddressflag")));
		yihaodian.setYwexportCwb_URL(request.getParameter("ywexportCwb_URL"));
		yihaodian.setYwdeliveryResult_URL(request.getParameter("ywdeliveryResult_URL"));
		yihaodian.setYwtrackLog_URL(request.getParameter("ywtrackLog_URL"));
		yihaodian.setYwexportSuccess_URL(request.getParameter("ywexportSuccess_URL"));

		String ywcustomerid = request.getParameter("ywcustomerid");
		yihaodian.setYwcustomerid(ywcustomerid);
		String oldYwCustomerids = ""; // yaowang customerid

		String customerids = request.getParameter("customerids");
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(yihaodian);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = this.getYihaodian(joint_num).getCustomerids();
				oldYwCustomerids = this.getYihaodian(joint_num).getYwcustomerid();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
		this.customerDAO.updateB2cEnumByJoint_num(ywcustomerid, oldYwCustomerids, joint_num);
	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	public int getStateForYihaodian(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = this.jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	/**
	 * 一号店定时器的总开关方法
	 */

	public long excute_getYihaodianTask() {
		String customerids = "";
		long count = 0;
		int check = 0;
		String remark = "";

		try {
			for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
				if (enums.getMethod().contains("yihaodian")) {
					if (this.getStateForYihaodian(enums.getKey()) == 0) {
						this.logger.info("未开启[一号店]对接key={}", enums.getKey());
						continue;
					}
					count += this.YiHaoDianInterfaceInvoke(enums.getKey());
					check++;
					Yihaodian yihaodian = this.getYihaodian(enums.getKey());
					customerids += yihaodian.getCustomerids() + ",";
				}
			}
			if (check == 0) {
				return -1;
			}
		} catch (Exception e) {
			remark = "下载遇未知异常" + e.getMessage();
			this.logger.error(remark, e);
		}
		this.b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(customerids.indexOf(",") > 0 ? customerids.substring(0, customerids.length() - 1) : customerids, DateTimeUtil.getNowTime(), count, remark);

		return count;
	}

	public long YiHaoDianInterfaceInvoke(int yhd_key) {
		long calcCount = 0;
		if (this.getStateForYihaodian(yhd_key) == 0) {
			this.logger.info("未开启[一号店]对接key={}", yhd_key);
			return -1;
		}
		Yihaodian yihaodian = this.getYihaodian(yhd_key);
		if (yihaodian.getIsopenDataDownload() == 0) {
			this.logger.info("未开启[一号店]对接订单下载接口,key={}", yhd_key);
			return -1;
		}
		int loopcount = yihaodian.getLoopcount() == 0 ? 5 : yihaodian.getLoopcount();
		calcCount = downloadByUrl(yihaodian,yhd_key, calcCount, loopcount,yihaodian.getExportCwb_URL(),yihaodian.getExportSuccess_URL(),0,yihaodian.getUserCode()); //老地址下载
		if(yihaodian.getIsopenywaddressflag()==1){
			calcCount = downloadByUrl(yihaodian,yhd_key, calcCount, loopcount,yihaodian.getYwexportCwb_URL(),yihaodian.getYwexportSuccess_URL(),1,yihaodian.getYwUserCode()); //新地址下载
		}
		this.yihaodianTimmer.selectTempAndInsertToCwbDetail(yhd_key); // 临时表数据插入到detail中

		return calcCount;
	}

	private long downloadByUrl(Yihaodian yihaodian ,int yhd_key, long calcCount, int loopcount,String url,String callUrl,int urlFlag,String userCode) {
		for (int i = 0; i < loopcount; i++) {
			calcCount += yihaodian_Master.getYihaodian_DownloadCwb().DownLoadCwbDetailByYiHaoDian(yhd_key, i + 1,url,urlFlag,userCode); // 订单数据下载接口
			
			String customerid = getMatchCustomerId(yihaodian, yhd_key, url,urlFlag, i);
			yihaodian_Master.getYihaodian_ExportCallBack().ExportCallBackByYiHaoDian(yhd_key, i + 1,callUrl,customerid,userCode); // 下载成功回传接口
		}
		return calcCount;
	}

	private String getMatchCustomerId(Yihaodian yihaodian, int yhd_key, String url, int urlFlag, int i) {
		String customerid = "-1";
		if (yihaodian.getIsopenywaddressflag() == 1) {
			if (urlFlag == 0) {
				customerid = yihaodian.getCustomerids();
			} else {
				customerid = yihaodian.getYwcustomerid();
			}
		} else {
			customerid = yihaodian.getCustomerids() + "," + yihaodian.getYwcustomerid();
		}
		return customerid;
	}
}
