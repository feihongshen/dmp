package cn.explink.b2c.tools;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.Wholeline.WholeLine;
import cn.explink.b2c.Wholeline.WholeLineService;
import cn.explink.b2c.amazon.AmazonService_CommitDeliverInfo;
import cn.explink.b2c.benlaishenghuo.BenlaishenghuoService;
import cn.explink.b2c.dangdang.DangDangService;
import cn.explink.b2c.dongfangcj.DongFangCJService_Cod;
import cn.explink.b2c.dongfangcj.DongFangCJService_Delivery;
import cn.explink.b2c.dpfoss.DpfossService;
import cn.explink.b2c.explink.code_down.EpaiApiService;
import cn.explink.b2c.explink.core.CoreService;
import cn.explink.b2c.explink.core_up.EpaiCoreService_Receiver;
import cn.explink.b2c.gome.GomeService_CommitDeliverInfo;
import cn.explink.b2c.gzabc.GuangZhouABCService;
import cn.explink.b2c.haoxgou.HaoXiangGouService;
import cn.explink.b2c.happygo.HappyGoService;
import cn.explink.b2c.homegou.HomegouService_Delivery;
import cn.explink.b2c.homegou.HomegouService_Message;
import cn.explink.b2c.huitongtx.HuitongtxService;
import cn.explink.b2c.hzabc.HangZhouABCService;
import cn.explink.b2c.jiuxian.JiuxianService;
import cn.explink.b2c.jumeiyoupin.JumeiYoupinService;
import cn.explink.b2c.maikolin.MaikolinService;
import cn.explink.b2c.maisike.MaisikeService_Send2LvBranch;
import cn.explink.b2c.rufengda.RufengdaService_CommitDeliverInfo;
import cn.explink.b2c.smile.SmileService;
import cn.explink.b2c.telecomsc.TelecomshopService;
import cn.explink.b2c.tmall.TmallService;
import cn.explink.b2c.tools.b2cmonitor.B2cSendMointorService;
import cn.explink.b2c.vipshop.VipShopCwbFeedBackService;
import cn.explink.b2c.yangguang.YangGuangService_upload;
import cn.explink.b2c.yemaijiu.YeMaiJiuService;
import cn.explink.b2c.yihaodian.YihaodianService;
import cn.explink.b2c.yixun.YiXunService;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.service.DownloadManagerService;
import cn.explink.service.ProxyService;
import cn.explink.util.DateTimeUtil;

@Service
public class JobUtilService {
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ProxyService proxyService;
	@Autowired
	JumeiYoupinService jumeiYoupinService;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	DangDangService dangdangService;
	@Autowired
	TmallService tmallService;
	@Autowired
	VipShopCwbFeedBackService vipshopService;
	@Autowired
	B2cSendMointorService b2cSendMointorService;
	@Autowired
	RufengdaService_CommitDeliverInfo rufengdaService_CommitDeliverInfo;
	@Autowired
	SmileService smileService;
	@Autowired
	YihaodianService yihaodianService;
	@Autowired
	GomeService_CommitDeliverInfo gomeService_CommitDeliverInfo;
	@Autowired
	YangGuangService_upload yangGuangService_upload;
	@Autowired
	GuangZhouABCService guangZhouABCService;
	@Autowired
	HangZhouABCService hangZhouABCService;
	@Autowired
	YeMaiJiuService yeMaiJiuService;
	@Autowired
	MaikolinService maikolinService;
	@Autowired
	DongFangCJService_Cod dongFangCJService_Cod;
	@Autowired
	DongFangCJService_Delivery dongFangCJService_Delivery;
	@Autowired
	HaoXiangGouService haoXiangGouService;
	@Autowired
	AmazonService_CommitDeliverInfo amazonService_CommitDeliverInfo;
	@Autowired
	HappyGoService goService;
	@Autowired
	DownloadManagerService downloadManagerService;
	@Autowired
	HomegouService_Delivery homegouService_Delivery;
	@Autowired
	HomegouService_Message homegouService_Message;
	@Autowired
	HuitongtxService huitongtxService;
	@Autowired
	DpfossService dpfossService;
	@Autowired
	BenlaishenghuoService benlaishenghuoService;
	@Autowired
	JiuxianService jiuxianService;
	@Autowired
	EpaiCoreService_Receiver epaiCoreService_Receiver;
	@Autowired
	EpaiApiService epaiApiService;
	@Autowired
	CoreService coreService;
	@Autowired
	TelecomshopService telecomshopService;
	@Autowired
	MaisikeService_Send2LvBranch maisikeService_Send2LvBranch;
	@Autowired
	WholeLineService wholeLineService;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	YiXunService yiXunService;

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	long time = 2 * 60 * 60 * 1000;

	private Logger logger = LoggerFactory.getLogger(JobUtilService.class);

	/**
	 * 执行当当的定时器
	 */
	public long feedBackToDangDang_Task() {

		long count = dangdangService.feedback_status();

		logger.info("执行了dangdang推送的定时器!");
		return count;
	}

	public long feedBackToTeleCom_Task() {

		return telecomshopService.feedback_status();

	}

	/**
	 * 执行聚美优品 状态推送的定时器
	 */
	public long feedBackToJumeiYoupin_Task() {
		long calcCount = 0;

		calcCount = jumeiYoupinService.feedback_status();

		logger.info("执行了[聚美优品]推送的定时器!");
		return calcCount;
	}

	// 删除某段时间之前推送B2C成功的数据
	public void DeleteSendB2cDataForSuccess() {
		int day = 60;
		b2cDataDAO.DeleteSendB2cDataForSuccess(day);
		logger.info("执行了删除{}天前的推送成功的数据！", day);
	}

	/**
	 * 执行tmall反馈的定时器
	 */
	public long feedBackToTmall_Task() {
		long calcCount = 0;
		int check = 0;
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("tmall")) {
				if (!b2ctools.isB2cOpen(enums.getKey())) {
					logger.info("未开tmall的对接!tmall_key={}", enums.getKey());
					continue;
				}
				check++;
				calcCount += tmallService.feedback_status(enums.getKey());
			}
		}
		if (check == 0) {
			return -1;
		}

		logger.info("执行了tmall推送的定时器!");
		return calcCount;
	}

	/**
	 * 执行vipshop反馈的定时器
	 */
	public long feedBackToVipShop_Task() {
		long calcCount = 0;
		int check = 0;
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("vipshop")) {
				if (!b2ctools.isB2cOpen(enums.getKey())) {
					logger.info("未开唯品会的对接!tmall_key={}", enums.getKey());
					continue;
				}
				check++;
				calcCount += vipshopService.feedback_status(enums.getKey());
			}

		}
		if (check == 0) {
			return -1;
		}
		logger.info("执行了vipshop推送的定时器!");

		return calcCount;
	}

	/**
	 * 执行yangguang反馈的定时器
	 */
	public long feedBackToYangGuang_Task() {
		long calcCount = 0;

		calcCount = yangGuangService_upload.createTxtFile_DeliveryFinishMethod();

		return calcCount;
	}

	/**
	 * 执行也买酒反馈的定时器
	 */
	public long feedBackToYemaijiu_Task() {
		long calcCount = 0;

		calcCount = yeMaiJiuService.feedback_status();

		return calcCount;
	}

	/*
	 * 执行麦考林定时器
	 */
	public long maikolin_Task() {
		long calcCount = 0;
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("maikaolin")) {
				calcCount += maikolinService.feedback_status(enums.getKey());
			}

		}

		logger.info("执行了[maikolin]推送的定时器!");
		return calcCount;
	}

	/*
	 * 执行本来网定时器
	 */
	public long Benlai_Task() {

		logger.info("执行了[本来网]推送的定时器!");
		return benlaishenghuoService.feedback_status(B2cEnum.Benlaishenghuo.getKey());
	}

	/*
	 * 执行快乐购定时器
	 */
	public long getHappyGo_Task() {
		logger.info("执行了[快乐购oms]推送的定时器!");
		return goService.feed_backstate();

	}

	/*
	 * 执行本来网定时器
	 */
	public long Jiuxian_Task() {
		long calcCount = 0;
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("Jiuxian")) {
				calcCount += jiuxianService.feedback_status(enums.getKey());
			}

		}
		logger.info("执行了[本来网]推送的定时器!");
		return calcCount;

	}

	/**
	 * 执行OMS发送给 DMP反馈的定时器
	 */
	/*
	 * public void OMSSendDMP_MonitorJMS_Task(){
	 * b2cSendMointorService.parseB2cMonitorData_timmer();
	 * logger.info("执行了OMS Send DMP 推送的定时器!"); }
	 */
	/**
	 * 执行如风达反馈的定时器
	 */
	public long feedBackToRufengda_Task() {
		long calcCount = 0;

		calcCount = rufengdaService_CommitDeliverInfo.CommitDeliverInfo_interface();

		logger.info("执行了如风达反馈的定时器!");

		return calcCount;
	}

	/**
	 * 思迈对接
	 */
	public long feedBackToSiMai_Task() {
		long calcCount = 0;

		calcCount = smileService.feedback_status(); // 思迈对接

		return calcCount;
	}

	/**
	 * 广州ABC
	 */
	public long feedBackToGuangZhou_Task() {
		long calcCount = 0;

		calcCount = guangZhouABCService.feedback_status(B2cEnum.GuangZhouABC.getKey());

		return calcCount;
	}

	/**
	 * 杭州ABC
	 */
	public long feedBackToHangZhou_Task() {
		long calcCount = 0;

		calcCount = hangZhouABCService.feedback_status(B2cEnum.HangZhouABC.getKey());

		return calcCount;
	}

	/**
	 * 好享购
	 */
	public long feedBackToHaoXgou_Task() {
		long calcCount = 0;

		calcCount = haoXiangGouService.feedback_status();

		return calcCount;
	}

	/**
	 * 易迅
	 */
	public long feedBackToYiXun_Task() {
		long calcCount = 0;

		calcCount = yiXunService.feedback_status();

		return calcCount;
	}

	/**
	 * 东方CJ
	 */
	public long feedBackToDongfangCJ_Task() {
		long calcCount = 0;
		long starttime = System.currentTimeMillis();
		String nowHours = DateTimeUtil.getNowTime("HH");
		if ("06".equals(nowHours) || "07".equals(nowHours)) {
			calcCount += dongFangCJService_Delivery.createDongFangCJTxtFile_DeliveryFinish();
			calcCount += dongFangCJService_Cod.createDongFangCJTxtFile_Cod();
			long endtime = System.currentTimeMillis();
			logger.info("执行了0东方CJ0状态反馈定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		}

		return calcCount;
	}

	/**
	 * 执行一号店重发机制 反馈的定时器 1小时反馈一次
	 */
	public long feedBackToYihaodian_Task() {
		long calcCount = 0;
		int check = 0;
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("yihaodian")) {
				if (!b2ctools.isB2cOpen(enums.getKey())) {
					logger.info("未开启[一号店]对接,yhd_key={}", enums.getKey());
					continue;
				}
				calcCount += yihaodianService.YiHaoDianInterfaceInvoke(enums.getKey());
				yihaodianService.DeliveryResultByYiHaoDianAgain(enums.getKey());
				check++;
			}
		}
		if (check == 0) {
			return -1;
		}

		logger.info("执行了一号店重发反馈的定时器!");
		return calcCount;
	}

	/**
	 * 国美定时器
	 */
	public long getGome_Task() {
		long calcCount = 0;
		try {
			calcCount = gomeService_CommitDeliverInfo.commitDeliverInfo_interface();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("执行了国美推送的定时器!");
		return calcCount;
	}

	/**
	 * 各个电商对接临时表插入主表说明 以后统一不加入定时器了
	 */
	public long getHomeGou_Task() {
		long calcCount = 0;
		long starttime = System.currentTimeMillis();

		String nowHours = DateTimeUtil.getNowTime("HH");

		if ("05".equals(nowHours) || "06".equals(nowHours)) {
			calcCount += homegouService_Delivery.feedback_state();
			calcCount += homegouService_Message.feedback_state();
			long endtime = System.currentTimeMillis();
			logger.info("执行了0家有购物0状态反馈定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		}

		return calcCount;

	}

	public long getHttx_Task() {

		return huitongtxService.feedback_status(B2cEnum.Huitongtx.getKey());
	}

	/**
	 * 亚马逊定时器
	 */
	public long amazon_Task() {
		long calcCount = 0;
		try {
			amazonService_CommitDeliverInfo.commitDeliverInfo_interface();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("执行了亚马逊推送的定时器!");
		return calcCount;
	}

	/**
	 * 亚马逊COD定时器
	 */
	public long amazon_Cod_Task() {
		long calcCount = 0;
		try {
			amazonService_CommitDeliverInfo.commitDeliver_Cod();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("执行了亚马逊COD推送的定时器!");
		return calcCount;
	}

	/**
	 * 离线下载任务
	 */
	public void down_Task() {
		downloadManagerService.down_task();
	}

	public void deleteFile_Task() {
		downloadManagerService.deleteFile_Task();
		logger.info("执行删除文件任务!");
	}

	/**
	 * 德邦物流定时 反馈任务
	 */
	public long dpfoss_Task() {

		return dpfossService.feedback_status();
	}

	/**
	 * 易派系统对接定时 反馈任务 上游OMS->DMP
	 */
	public long epaiFeedback_up_Task() {

		return coreService.selectOMStemp_feedback();

	}

	/**
	 * 易派系统对接定时 反馈任务下游反馈
	 */
	public long epaiFeedback_down_Task() {

		return epaiApiService.feedback_status();
	}

	/**
	 * 一级站出库迈思可 定时器 OMS
	 */
	public long getOuttoBranchMsk_Task() {
		long calcCount = 0;
		try {
			calcCount = maisikeService_Send2LvBranch.sendTwoLeavelBranch();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("执行了0一级站出库迈思可0推送的定时器!");
		return calcCount;
	}

}
