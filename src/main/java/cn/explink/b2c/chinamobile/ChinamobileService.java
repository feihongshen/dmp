package cn.explink.b2c.chinamobile;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.CSVReaderUtil;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CustomerService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JMath;

@Service
public class ChinamobileService {
	private Logger logger = LoggerFactory.getLogger(ChinamobileService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerService customerService;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Chinamobile getChinaMobile(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Chinamobile dangdang = (Chinamobile) JSONObject.toBean(jsonObj, Chinamobile.class);
		return dangdang;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Chinamobile cm = new Chinamobile();
		String customerid = request.getParameter("customerid");
		String expressid = request.getParameter("expressid");
		long maxCount = Long.valueOf(request.getParameter("maxCount"));
		String feedback_url = request.getParameter("feedback_url");
		String ftp_host = request.getParameter("ftp_host");
		String ftp_port = request.getParameter("ftp_port");
		String ftp_username = request.getParameter("ftp_username");
		String ftp_password = request.getParameter("ftp_password");
		String remotePath = request.getParameter("remotePath");
		String downloadPath = request.getParameter("downloadPath");
		String downloadPath_bak = request.getParameter("downloadPath_bak");
		String charencode = request.getParameter("charencode");

		cm.setCustomerid(customerid);
		cm.setExpressid(expressid);
		cm.setMaxCount(maxCount);
		cm.setFeedback_url(feedback_url);
		cm.setFtp_host(ftp_host);
		cm.setFtp_port(ftp_port);
		cm.setFtp_username(ftp_username);
		cm.setFtp_password(ftp_password);
		cm.setRemotePath(remotePath);
		cm.setDownloadPath(downloadPath);
		cm.setDownloadPath_bak(downloadPath_bak);
		cm.setCharencode(charencode);

		JSONObject jsonObj = JSONObject.fromObject(cm);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getChinaMobile(joint_num).getCustomerid();

			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
		this.customerService.initCustomerList();

	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 从移动对接下载数据
	 */
	@Transactional
	public void downloadOrdersToFTPServer() {
		// 这里需要一个基础设置
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.ChinaMobile.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启移动对接下载审核未通过信息接口");
			return;
		}
		Chinamobile cm = this.getChinaMobile(B2cEnum.ChinaMobile.getKey());

		ChinamobileFTPUtils ftp = new ChinamobileFTPUtils(cm.getFtp_host(), cm.getFtp_username(), cm.getFtp_password(), Integer.valueOf(cm.getFtp_port()), cm.getCharencode(), true);

		try {

			ftp.downloadFileforFTP(cm.getRemotePath(), cm.getDownloadPath());
		} catch (Exception e) {
			logger.error("从FTP下载文件发生未知异常,return", e);
			return;
		}

	}

	/**
	 * 解析下载数据，存入b2c_temp表中
	 */
	@Transactional
	public void AnalyzCSVFileAndSaveB2cTemp() {
		String downloadPath = "";
		try {

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.ChinaMobile.getKey());
			if (isOpenFlag == 0) {
				logger.warn("未开启移动对接解析CSV插入临时表方法");
				return;
			}
			Chinamobile cm = this.getChinaMobile(B2cEnum.ChinaMobile.getKey());

			downloadPath = cm.getDownloadPath();
			String downloadPath_bak = cm.getDownloadPath_bak();

			String[] filelist = isExistFileDir(downloadPath, downloadPath_bak).list();
			if (filelist == null || filelist.length == 0) {
				logger.info("当前没有下载到CSV文件！download={}", downloadPath);
				return;
			}

			for (String fi : filelist) {
				String csv_url = downloadPath + "/" + fi;

				File file = new File(csv_url);
				if (!file.exists()) {
					logger.warn("不存在此文件无法解析");
					continue;
				}

				String updateDate = JMath.getFileLastUpdateTime(csv_url).substring(0, 10);
				if (!updateDate.equals(DateTimeUtil.getNowDate())) {
					logger.warn("该文件" + downloadPath + "/" + fi + "的文件不是今天要解析的文件,credate=" + updateDate);
					continue;
				}

				CSVReaderUtil csvReaderUtil = new CSVReaderUtil(csv_url, cm.getCharencode()); // 创建对象
				List<List<String>> csvList = csvReaderUtil.readCSVFile(); // 解析CSV格式文件，然后处理
				for (List<String> rowlist : csvList) {

					String transcwb = rowlist.get(0);
					String cwb = rowlist.get(1);
					String backtime = rowlist.get(2);
					String backreason = rowlist.get(3);
					String csremark = backtime + "收到移动对接审核未通过信息，需处理，原因：" + backreason;
					cwbDAO.updateCwbRemark(cwb, csremark);

					logger.info("当前处理移动对接CSV文件，内容：{},订单号={},运单号=" + transcwb, cwb);
				}

			}

		} catch (Exception e) {
			logger.error("移动对接对接读取服务器路径=" + downloadPath + "解析发生未知异常", e);
		}

	}

	/**
	 * 不存在就创建
	 * 
	 * @param downloadPath
	 * @param downloadPath_bak
	 * @return
	 */
	private File isExistFileDir(String downloadPath, String downloadPath_bak) {
		File MyDir = new File(downloadPath);
		if (!MyDir.exists()) { // 如果不存在,创建一个子目录
			MyDir.mkdirs();
		}
		File MyDirBak = new File(downloadPath_bak);
		if (!MyDirBak.exists()) { // 如果不存在,创建一个子目录
			MyDirBak.mkdirs();
		}
		return MyDir;
	}

	/**
	 * 移动对接路径到bak里面，删除原路径文件
	 */
	public void MoveTxtToDownload_BakFile() {
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.ChinaMobile.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启移动对接解析txt插入临时表方法");
			return;
		}
		Chinamobile cm = this.getChinaMobile(B2cEnum.ChinaMobile.getKey());

		String downloadPath = cm.getDownloadPath();
		String downloadPath_bak = cm.getDownloadPath_bak();
		try {

			for (int i = 0; i < 50; i++) {
				File MyDir1 = new File(downloadPath);
				String[] filelist = MyDir1.list();
				if (filelist == null || filelist.length == 0) {
					logger.info("downloadPath没有可移动对接的文件！");
					return;
				}

				if (MyDir1.list() != null && MyDir1.list().length > 0) {
					for (String fi : filelist) {
						JMath.moveFile(downloadPath + "/" + fi, downloadPath_bak + "/" + fi); // 移动对接文件到download_bak里面
						logger.info("移动对接移动对接文件从{}移动对接到{}", downloadPath + "/" + fi, downloadPath_bak + "/" + fi + ",移动对接次数=" + (i));
					}
				}
				Thread.sleep(10000);// 停留10秒钟再执行.

			}
		} catch (Exception e) {
			logger.error("移动对接移动对接文件发生未知异常", e);
		}

	}

}
