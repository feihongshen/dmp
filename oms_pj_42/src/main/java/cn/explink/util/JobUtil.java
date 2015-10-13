package cn.explink.util;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.b2c.Wholeline.WholeLine;
import cn.explink.b2c.Wholeline.WholeLineService;
import cn.explink.b2c.Wholeline.WholeLineService_search;
import cn.explink.b2c.amazon.AmazonService_CommitDeliverInfo;
import cn.explink.b2c.benlaishenghuo.BenlaishenghuoService;
import cn.explink.b2c.chinamobile.ChinamobileService;
import cn.explink.b2c.dangdang.DangDangService;
import cn.explink.b2c.dongfangcj.DongFangCJService_Cod;
import cn.explink.b2c.dongfangcj.DongFangCJService_Delivery;
import cn.explink.b2c.dpfoss.DpfossService;
import cn.explink.b2c.explink.code_down.EpaiApiService;
import cn.explink.b2c.explink.core.CoreService;
import cn.explink.b2c.explink.core_up.EpaiCoreService_Receiver;
import cn.explink.b2c.gome.GomeService_CommitDeliverInfo;
import cn.explink.b2c.gzabc.GuangZhouABCService;
import cn.explink.b2c.gztl.GztlService;
import cn.explink.b2c.gztlfeedback.GztlServiceFeedback;
import cn.explink.b2c.haoxgou.HaoXiangGouService;
import cn.explink.b2c.happygo.HappyGoService;
import cn.explink.b2c.homegobj.HomegobjService;
import cn.explink.b2c.homegou.HomegouService_Delivery;
import cn.explink.b2c.homegou.HomegouService_Message;
import cn.explink.b2c.huitongtx.HuitongtxService;
import cn.explink.b2c.hxgdms.HxgdmsService;
import cn.explink.b2c.hzabc.HangZhouABCService;
import cn.explink.b2c.jiuxian.JiuxianService;
import cn.explink.b2c.jumeiyoupin.JumeiService;
import cn.explink.b2c.jumeiyoupin.JumeiYoupinService;
import cn.explink.b2c.lechong.LechongService;
import cn.explink.b2c.lefeng.LefengService;
import cn.explink.b2c.letv.LetvService;
import cn.explink.b2c.liantong.LiantongService;
import cn.explink.b2c.maikolin.MaikolinService;
import cn.explink.b2c.maisike.MaisikeService_Send2LvBranch;
import cn.explink.b2c.mmb.MmbService;
import cn.explink.b2c.rufengda.RufengdaService_CommitDeliverInfo;
import cn.explink.b2c.sfexpress.SfexpressService_searchOrderStatus;
import cn.explink.b2c.sfexpress.SfexpressService_sendOrder;
import cn.explink.b2c.sfxhm.SfxhmService;
import cn.explink.b2c.smile.SmileService;
import cn.explink.b2c.smiled.SmiledService_SendBranch;
import cn.explink.b2c.telecomsc.TelecomshopService;
import cn.explink.b2c.tmall.TmallService;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.b2cmonitor.B2cSendMointorService;
import cn.explink.b2c.vipshop.VipShopCwbFeedBackService;
import cn.explink.b2c.wangjiu.WangjiuService;
import cn.explink.b2c.wanxiang.WanxiangService;
import cn.explink.b2c.weisuda.WeisudaService;
import cn.explink.b2c.yangguang.YangGuangService_upload;
import cn.explink.b2c.yemaijiu.YeMaiJiuService;
import cn.explink.b2c.yihaodian.YihaodianService;
import cn.explink.b2c.yonghuics.YonghuiService;
import cn.explink.b2c.zhongliang.ZhongliangService;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExpressSysMonitorDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.SystemInstall;
import cn.explink.enumutil.ExpressSysMonitorEnum;
import cn.explink.service.DownloadManagerService;
import cn.explink.service.ProxyService;

@Component
public class JobUtil {
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ProxyService proxyService;
	@Autowired
	JumeiYoupinService jumeiYoupinService;
	@Autowired
	JumeiService jumeiService;
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
	WholeLineService_search wholeLineService_search;
	@Autowired
	LiantongService liantongService;
	@Autowired
	MmbService mmbService;
	@Autowired
	LetvService letvService;
	@Autowired
	WanxiangService wanxiangService;
	@Autowired
	ChinamobileService chinamobileService;
	@Autowired
	YonghuiService yonghuiService;
	@Autowired
	HxgdmsService hxgdmsService;

	@Autowired
	SfexpressService_sendOrder sfexpressService_sendOrder;
	@Autowired
	SfexpressService_searchOrderStatus sfexpressService_searchOrderStatus;
	@Autowired
	WangjiuService wangjiuService;

	@Autowired
	LechongService lechongService;
	@Autowired
	WeisudaService weisudaService;

	@Autowired
	HomegobjService homegobjService;
	@Autowired
	SfxhmService sfxhmService;
	@Autowired
	SmiledService_SendBranch smiledService_SendBranch;
	@Autowired
	ZhongliangService zhongliangService;
	@Autowired
	ExpressSysMonitorDAO expressSysMonitorDAO;
	@Autowired
	LefengService lefengService;
	@Autowired
	GztlService gztlService;
	@Autowired
	GztlServiceFeedback gztlServiceFeedback;

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	long time = 2 * 60 * 60 * 1000;

	private Logger logger = LoggerFactory.getLogger(JobUtil.class);

	/**
	 * 查询控制总开关。
	 *
	 * @return
	 */
	private String getOpenValue() {
		SystemInstall systemInstall = this.getDmpDAO.getSystemInstallByName("isOpenJobHand");
		return systemInstall == null ? "no" : systemInstall.getValue();
	}

	/**
	 * 执行当当的定时器
	 */
	public void feedBackToDangDang_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能dangdang");
			return;
		}

		this.dangdangService.feedback_status();
		this.telecomshopService.feedback_status();
		long endtime = System.currentTimeMillis();
		this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.DANGDANG, endtime);
		this.logger.info("执行了dangdang推送的定时器!");
	}

	/**
	 * 执行聚美优品 状态推送的定时器
	 */
	public void feedBackToJumeiYoupin_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能jumei");
			return;
		}
		// jumeiYoupinService.feedback_status();
		this.jumeiService.feedback_status();
		this.logger.info("执行了[聚美优品]推送的定时器!");
	}

	// 删除某段时间之前推送B2C成功的数据
	public void DeleteSendB2cDataForSuccess() {
		// if("yes".equals(getOpenValue())){
		// logger.warn("未开启本地调用定时器功能");
		// return;
		// }
		int day = 60;
		this.b2cDataDAO.DeleteSendB2cDataForSuccess(day);
		this.logger.info("执行了删除{}天前的推送成功的数据！", day);
	}

	/**
	 * 执行tmall反馈的定时器
	 */
	public void feedBackToTmall_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能tmall");
			return;
		}
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("tmall")) {
				this.tmallService.feedback_status(enums.getKey());
			}
		}
		long endtime = System.currentTimeMillis();
		this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.TMALL, endtime);
		this.logger.info("执行了tmall推送的定时器!");
	}

	/**
	 * 执行vipshop反馈的定时器
	 */
	public void feedBackToVipShop_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能vipshop");
			return;
		}
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("vipshop")) {
				this.vipshopService.feedback_status(enums.getKey());
			}

		}

		this.logger.info("执行了vipshop推送的定时器!");
	}

	/*
	 * 执行麦考林定时器
	 */
	public void maikolin_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能maikolin");
			return;
		}
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("maikaolin")) {
				this.maikolinService.feedback_status(enums.getKey());
			}

		}

		this.logger.info("执行了[maikolin]推送的定时器!");
	}

	/*
	 * 执行本来网定时器
	 */
	public void Benlai_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能benlai");
			return;
		}
		this.benlaishenghuoService.feedback_status(B2cEnum.Benlaishenghuo.getKey());
		this.logger.info("执行了[本来网]推送的定时器!");
	}

	/*
	 * 执行快乐购定时器
	 */
	public void getHappyGo_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能happygo");
			return;
		}
		this.goService.feed_backstate();
		this.logger.info("执行了[快乐购oms]推送的定时器!");
	}

	/*
	 * 执行本来网定时器
	 */
	public void Jiuxian_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能Jiuxian");
			return;
		}
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("Jiuxian")) {
				this.jiuxianService.feedback_status(enums.getKey());
			}

		}

		this.logger.info("执行了[本来网]推送的定时器!");
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
	public void feedBackToRufengda_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能yihaodian");
			return;
		}
		this.rufengdaService_CommitDeliverInfo.CommitDeliverInfo_interface();
		this.smileService.feedback_status(); // 思迈对接
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("yihaodian")) {
				this.yihaodianService.YiHaoDianInterfaceInvoke(enums.getKey());
			}
		}
		long endtime = System.currentTimeMillis();
		this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.FANKE, endtime);
		this.logger.info("执行了如风达反馈的定时器!");
	}

	/**
	 * 执行一号店重发机制 反馈的定时器 1小时反馈一次
	 */
	public void feedBackToYihaodianAgain_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能yihaodian");
			return;
		}
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("yihaodian")) {
				this.yihaodianService.DeliveryResultByYiHaoDianAgain(enums.getKey());
			}
		}
		this.logger.info("执行了一号店重发反馈的定时器!");
		long endtime = System.currentTimeMillis();
		this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.YIHAODIAN, endtime);
	}

	/**
	 * 国美定时器
	 */
	public void gome_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能gome");
			return;
		}
		try {
			this.gomeService_CommitDeliverInfo.commitDeliverInfo_interface();
			long endtime = System.currentTimeMillis();
			this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.GOME, endtime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.getDiffB2cTimmer_Task();

		this.logger.info("执行了国美推送的定时器!");
	}

	/**
	 * 各个电商对接临时表插入主表说明 以后统一不加入定时器了
	 */
	public void getDiffB2cTimmer_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能abc,yangguang,yemaijiu");
			return;
		}
		long starttime = System.currentTimeMillis();

		this.guangZhouABCService.feedback_status(B2cEnum.GuangZhouABC.getKey());
		this.hangZhouABCService.feedback_status(B2cEnum.HangZhouABC.getKey());

		this.yangGuangService_upload.createTxtFile_DeliveryFinishMethod();
		this.yeMaiJiuService.feedback_status();

		String nowHours = DateTimeUtil.getNowTime("HH");
		if ("06".equals(nowHours) || "07".equals(nowHours)) {
			this.dongFangCJService_Delivery.createDongFangCJTxtFile_DeliveryFinish();
			this.dongFangCJService_Cod.createDongFangCJTxtFile_Cod();
			long endtime = System.currentTimeMillis();
			this.logger.info("执行了0东方CJ0状态反馈定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		}

		this.haoXiangGouService.feedback_status();

	}

	/**
	 * 家有购物定时器
	 */
	public void getHomeGou_Task() {

		long starttime = System.currentTimeMillis();
		String nowHours = DateTimeUtil.getNowTime("HH");
		long nowMinutes = Long.valueOf(DateTimeUtil.getNowTime("mm"));

		if ("05".equals(nowHours) || "06".equals(nowHours)) {
			if (nowMinutes >= 30) {
				this.homegouService_Delivery.feedback_state();
				this.homegouService_Message.feedback_state();
				long endtime = System.currentTimeMillis();
				this.logger.info("执行了0家有购物0状态反馈定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
			}
		}

		this.huitongtxService.feedback_status(B2cEnum.Huitongtx.getKey());
		this.logger.info("执行了汇通天下反馈定时器");

	}

	/**
	 * 亚马逊定时器
	 */
	public void amazon_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能amazon");
			return;
		}
		try {
			this.amazonService_CommitDeliverInfo.commitDeliverInfo_interface();
			long endtime = System.currentTimeMillis();
			this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.AMAZON, endtime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.logger.info("执行了亚马逊推送的定时器!");
	}

	/**
	 * 亚马逊COD定时器
	 */
	public void amazon_Cod_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能amazon_cod");
			return;
		}
		try {
			this.amazonService_CommitDeliverInfo.commitDeliver_Cod();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.logger.info("执行了亚马逊COD推送的定时器!");
	}

	/**
	 * 离线下载任务
	 */
	public void down_Task() {
		this.downloadManagerService.down_task();
	}

	public void deleteFile_Task() {
		this.downloadManagerService.deleteFile_Task();
		this.logger.info("执行删除文件任务!");
	}

	/**
	 * 德邦物流定时 反馈任务
	 */
	public void dpfoss_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能dpfoss");
			return;
		}
		try {
			this.dpfossService.feedback_status();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.logger.info("执行了0德邦物流0推送的定时器!");
	}

	/**
	 * 易派系统对接定时 反馈任务 上游OMS->DMP
	 */
	public void epaiFeedback_up_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能hxdj");
			return;
		}
		try {
			this.coreService.selectOMStemp_feedback();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.logger.info("执行了0上游OMS->DMP0推送的定时器!");
	}

	/**
	 * 易派系统对接定时 反馈任务下游反馈
	 */
	public void epaiFeedback_down_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能hxdj");
			return;
		}
		try {
			this.epaiApiService.feedback_status();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.logger.info("执行了0下游反馈0推送的定时器!");
	}

	/**
	 * 一级站出库迈思可 定时器 OMS
	 */
	public void getOuttoBranchMsk_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能maisike");
			return;
		}
		try {
			this.maisikeService_Send2LvBranch.sendTwoLeavelBranch();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.logger.info("执行了0一级站出库迈思可0推送的定时器!");
	}

	/*
	 * 全线快递
	 */
	public void wholeLine_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能quanxian");
			return;
		}
		try {
			int key = B2cEnum.wholeLine.getKey();
			WholeLine whole = this.wholeLineService.getWholeline(B2cEnum.wholeLine.getKey());
			this.wholeLineService_search.searchQuanxianRoute(key, whole);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("执行了全线快递的定时器，报错!", e);
		}
		this.logger.info("执行了全线快递的定时器!");
	}

	public void Transmigration_wholeline() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能");
			return;
		}
		try {
			long starttime = System.currentTimeMillis();
			this.wholeLineService.getInfoCall();
			long endtime = System.currentTimeMillis();
			this.logger.info("执行了全线快递info的定时器!------------轮询order表完毕,用时={}", (endtime - starttime) / 1000);
		} catch (Exception e) {

			this.logger.error("执行了全线快递info的定时器，报错!", e);
		}
	}

	/**
	 * 联通商城接口
	 */
	public void getLiantong_Task() {

		try {
			this.liantongService.feedback_status();
		} catch (Exception e) {

			this.logger.error("执行了联通商城的定时器异常!", e);
		}
		this.logger.info("执行了联通商城定时器!");
	}

	/**
	 * 买卖宝
	 */
	public void getMmb_Task() {

		try {
			this.mmbService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行了买卖宝的定时器异常!", e);
		}
		this.logger.info("执行了买卖宝定时器!");
	}

	/**
	  *
	  */
	public void getChinamobile_Task() {
		try {
			this.chinamobileService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行了移动状态反馈的定时器异常!", e);
		}
		this.logger.info("执行了移动状态反馈的定时器!");
	}

	/**
	 * 乐视网推送接口定时器
	 */
	public void getLetv_Task() {

		try {
			this.letvService.feedback_status();
			this.wanxiangService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行了乐视网,万象的定时器异常!", e);
		}

		this.logger.info("执行了乐视网定时器!");
	}

	/**
	 * 永辉超市推送接口定时器
	 */
	public void getYonghui_Task() {

		try {
			this.yonghuiService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行永辉超市的定时器异常!", e);
		}

		this.logger.info("执行了永辉超市定时器!");
	}

	/**
	 * 好享购DMS推送接口定时器
	 */
	public void getHxgdms_Task() {

		try {
			this.hxgdmsService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行了好享购的定时器异常!", e);
		}

		this.logger.info("执行了好享购DMS定时器!");
	}

	/**
	 * 顺丰快递定时器
	 */
	public void getSfexpress_Task() {

		try {
			this.sfexpressService_sendOrder.sendCwbOrdersToSFexpress();

			this.sfexpressService_searchOrderStatus.getWSReturnJson();
		} catch (Exception e) {
			this.logger.error("执行了顺丰的定时器异常!", e);
		}

		this.logger.info("执行了推送顺丰快递定时器!");
	}

	/**
	 * 网酒网
	 */
	public void getWangjiu_Task() {
		try {

			this.wangjiuService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行了网酒网的定时器异常!", e);
		}

		this.logger.info("执行了推送网酒网定时器!");
	}

	/**
	 * 乐宠
	 */
	public void getLechong_Task() {
		try {
			this.lechongService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行了乐宠的定时器异常!", e);
		}

		this.logger.info("执行了推送乐宠网定时器!");
	}

	/**
	 * 中粮
	 */
	public void getZhongliang_Task() {
		try {
			this.zhongliangService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行了中粮的定时器异常!", e);
		}

		this.logger.info("执行了推送中粮我买网定时器!");
	}

	/**
	 * 家有购物BJ
	 */
	public void getHomegobj_Task() {
		try {

			this.homegobjService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行了家有购物北京的定时器异常!", e);
		}

		this.logger.info("执行了推送家有购物北京定时器!");
	}

	public void getWeisuda_Task() {
		try {

			this.weisudaService.selectWeisudaCwb();
			this.weisudaService.getUnVerifyOrders();
		} catch (Exception e) {
			this.logger.error("执行了唯速达定时器异常!", e);
		}

		this.logger.info("执行了推送唯速达定时器!");
	}

	/**
	 * 思迈下游
	 */
	public void getSmiled_Task() {
		try {

			this.smiledService_SendBranch.sendNextBranch();
		} catch (Exception e) {
			this.logger.error("执行了思迈下游的定时器异常!", e);
		}

		this.logger.info("执行了推送思迈下游定时器!");
	}

	/**
	 * 顺丰小红帽
	 */
	public void getSfxhm_Task() {
		try {

			this.sfxhmService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行了顺丰小红帽的定时器异常!", e);
		}

		this.logger.info("执行了推送顺丰小红帽定时器!");
	}

	/**
	 * 乐蜂网
	 */
	public void getLefeng_Task() {
		try {

			this.lefengService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行了乐蜂网的定时器异常!", e);
		}

		this.logger.info("执行了乐蜂网定时器!");
	}

	/**
	 * 广州通路
	 */
	public void getGztl_Task() {
		try {

			this.gztlService.feedback_status();
		} catch (Exception e) {
			this.logger.error("执行了广州通路的定时器异常!", e);
		}

		this.logger.info("执行了广州通路定时器!");
	}

	/**
	 * 一级站出库广州通路 外发 定时器 OMS
	 */
	public void getOuttoBranchGztl_Task() {
		if ("yes".equals(this.getOpenValue())) {
			this.logger.warn("未开启本地调用定时器功能外发广州通路");
			return;
		}
		try {
			this.gztlServiceFeedback.sendTwoLeavelBranch();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.logger.info("执行了0一级站出库外发广州通路订单0推送的定时器!");
	}

}
