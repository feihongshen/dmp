package cn.explink.b2c.tools;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.explink.b2c.amazon.AmazonService;
import cn.explink.b2c.auto.order.service.AutoDispatchStatusService;
import cn.explink.b2c.auto.order.service.ExpressOrderService;
import cn.explink.b2c.auto.order.service.TPSInsertCwbDetailTimmer;
import cn.explink.b2c.chinamobile.ChinamobileService;
import cn.explink.b2c.dangdang_dataimport.DangDangSynInsertCwbDetailTimmer;
import cn.explink.b2c.dongfangcj.DongFangCJInsertCwbDetailTimmer;
import cn.explink.b2c.dongfangcj.DongFangCJService_getOrder;
import cn.explink.b2c.dpfoss.DpfossInsertCwbDetailTimmer;
import cn.explink.b2c.efast.EfastInsertCwbDetailTimmer;
import cn.explink.b2c.efast.EfastService_getOrderDetailList;
import cn.explink.b2c.efast.EfastService_getOrderList;
import cn.explink.b2c.ems.EMSService;
import cn.explink.b2c.ems.EMSTimmer;
import cn.explink.b2c.explink.core_down.AcquisitionOrderService;
import cn.explink.b2c.explink.core_down.EpaiApiService_Download;
import cn.explink.b2c.explink.core_down.EpaiApiService_ExportCallBack;
import cn.explink.b2c.explink.core_down.EpaiInsertCwbDetailTimmer;
import cn.explink.b2c.feiniuwang.FNWInsertCwbDetailTimmer;
import cn.explink.b2c.gome.GomeService;
import cn.explink.b2c.gxdx.GxDxInsertCwbDetailTimmer;
import cn.explink.b2c.gzabc.GuangZhouABCInsertCwbDetailTimmer;
import cn.explink.b2c.gztl.GuangZhouTongLuInsertCwbDetailTimmer;
import cn.explink.b2c.haoxgou.HXGInsertCwbDetailTimmer;
import cn.explink.b2c.haoxgou.HaoXiangGouService;
import cn.explink.b2c.haoxgou.HaoXiangGouService_PeiSong;
import cn.explink.b2c.haoxgou.HaoXiangGouService_TuiHuo;
import cn.explink.b2c.happyGo.FunctionForHappy;
import cn.explink.b2c.happyGo.HappyGo;
import cn.explink.b2c.happyGo.HappyGoService;
import cn.explink.b2c.homegobj.HomegobjInsertCwbDetailTimmer;
import cn.explink.b2c.homegobj.HomegobjService_getOrderDetailList;
import cn.explink.b2c.homegou.HomegouInsertCwbDetailTimmer;
import cn.explink.b2c.homegou.HomegouService_getOrder;
import cn.explink.b2c.huanqiugou.HuanqiugouInsertCwbDetailTimmer;
import cn.explink.b2c.huitongtx.HuitongtxInsertCwbDetailTimmer;
import cn.explink.b2c.hxgdms.HxgdmsInsertCwbDetailTimmer;
import cn.explink.b2c.hzabc.HangZhouABCInsertCwbDetailTimmer;
import cn.explink.b2c.jiuye.JiuYeInsertCwbDetailTimmer;
import cn.explink.b2c.lechong.LechongInsertCwbDetailTimmer;
import cn.explink.b2c.liantong.LiantongInsertCwbDetailTimmer;
import cn.explink.b2c.maikaolin.MaikaolinInsertCwbDetailTimmer;
import cn.explink.b2c.maikaolin.MaikolinService;
import cn.explink.b2c.maisike.MaisikeService_branchSyn;
import cn.explink.b2c.meilinkai.MLKService;
import cn.explink.b2c.pinhaohuo.PinhaohuoInsertCwbDetailTimmer;
import cn.explink.b2c.pjwl.PjwlExpressService;
import cn.explink.b2c.rufengda.RufengdaService;
import cn.explink.b2c.saohuobang.SaohuobangInsertCwbDetailTimmer;
import cn.explink.b2c.sfxhm.SfxhmInsertCwbDetailTimmer;
import cn.explink.b2c.sfxhm.SfxhmService_getOrder;
import cn.explink.b2c.smile.SmileInsertCwbDetailTimmer;
import cn.explink.b2c.suning.SuNingInsertCwbDetailTimmer;
import cn.explink.b2c.telecomsc.TelecomInsertCwbDetailTimmer;
import cn.explink.b2c.tmall.TmallInsertCwbDetailTimmer;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.b2c.tps.TPSCarrierOrderStatusTimmer;
import cn.explink.b2c.tps.TpsCwbFlowPushService;
import cn.explink.b2c.vipshop.VipShopGetCwbDataService;
import cn.explink.b2c.vipshop.VipShopService;
import cn.explink.b2c.vipshop.VipshopInsertCwbDetailTimmer;
import cn.explink.b2c.vipshop.oxo.VipShopOXOGetPickStateService;
import cn.explink.b2c.vipshop.oxo.VipShopOXOInsertCwbDetailTimmer;
import cn.explink.b2c.vipshop.oxo.VipShopOXOJITFeedbackService;
import cn.explink.b2c.wangjiu.WangjiuInsertCwbDetailTimmer;
import cn.explink.b2c.wenxuan.WenxuanInsertCwbDetailTimmer;
import cn.explink.b2c.wenxuan.WenxuanService_getOrder;
import cn.explink.b2c.yangguang.YangGuangInsertCwbDetailTimmer;
import cn.explink.b2c.yangguang.YangGuangService_download;
import cn.explink.b2c.yihaodian.YihaodianService;
import cn.explink.b2c.yixun.YiXunInsertCwbDetailTimmer;
import cn.explink.b2c.yonghui.YongHuiInsertCwbDetailTimmer;
import cn.explink.b2c.yonghuics.YonghuiService;
import cn.explink.b2c.zhemeng.ZhemengInsertCwbDetailTimmer;
import cn.explink.b2c.zhongliang.ZhongliangBackOrderServices;
import cn.explink.b2c.zhongliang.ZhongliangInsertCwbDetailTimmer;
import cn.explink.b2c.zhongliang.ZhongliangService;
import cn.explink.dao.ExpressSysMonitorDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;
import cn.explink.enumutil.ExpressSysMonitorEnum;
import cn.explink.service.AccountDeductRecordService;
import cn.explink.service.AppearWindowService;
import cn.explink.service.BackSummaryService;
import cn.explink.service.BranchInfService;
import cn.explink.service.FlowExpService;
import cn.explink.service.FloworderLogService;
import cn.explink.service.LogToDayByTuihuoService;
import cn.explink.service.LogToDayByWarehouseService;
import cn.explink.service.LogToDayService;
import cn.explink.service.PunishInsideService;
import cn.explink.service.UserInfService;
import cn.explink.service.express.ReSendExpressOrderService;
import cn.explink.service.fnc.OrderLifeCycleReportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.RedisMap;
import cn.explink.util.impl.RedisMapImpl;

@Component
public class JobUtil {
	private Logger logger = LoggerFactory.getLogger(JobUtil.class);
	@Autowired
	VipShopGetCwbDataService vipShopGetCwbDataService;
	@Autowired
	VipShopService vipShopService;
	@Autowired
	MaikolinService maikolinService;

	@Autowired
	TmallInsertCwbDetailTimmer tmallService;
	@Autowired
	YihaodianService yihaodianService;
	@Autowired
	YiXunInsertCwbDetailTimmer yiXunInsertCwbDetailTimmer;
	@Autowired
	RufengdaService rufengdaService;
	@Autowired
	GomeService gomeService;
	@Autowired
	HappyGoService happyGoService;
	@Autowired
	DangDangSynInsertCwbDetailTimmer dangDangSynInsertCwbDetailTimmer;
	@Autowired
	YangGuangService_download yangGuangService_download;
	@Autowired
	YangGuangInsertCwbDetailTimmer yangGuangInsertCwbDetailTimmer;
	@Autowired
	GuangZhouABCInsertCwbDetailTimmer guangZhouABCInsertCwbDetailTimmer;
	@Autowired
	HangZhouABCInsertCwbDetailTimmer hangZhouABCInsertCwbDetailTimmer;
	@Autowired
	DongFangCJService_getOrder dongFangCJService_getOrder;
	@Autowired
	DongFangCJInsertCwbDetailTimmer dongFangCJInsertCwbDetailTimmer;
	@Autowired
	HaoXiangGouService_PeiSong haoXiangGouService_PeiSong;
	@Autowired
	HaoXiangGouService_TuiHuo haoXiangGouService_TuiHuo;
	@Autowired
	HaoXiangGouService haoXiangGouService;
	@Autowired
	HXGInsertCwbDetailTimmer hXGInsertCwbDetailTimmer;
	@Autowired
	AmazonService amazonService;
	@Autowired
	HomegouService_getOrder homegouService_getOrder;
	@Autowired
	HomegouInsertCwbDetailTimmer homegouInsertCwbDetailTimmer;
	@Autowired
	DpfossInsertCwbDetailTimmer dpfossInsertCwbDetailTimmer;
	@Autowired
	HuitongtxInsertCwbDetailTimmer huitongtxInsertCwbDetailTimmer;
	@Autowired
	MaikaolinInsertCwbDetailTimmer maikaolinInsertCwbDetailTimmer;
	@Autowired
	SaohuobangInsertCwbDetailTimmer saohuobangInsertCwbDetailTimmer;

	@Autowired
	TelecomInsertCwbDetailTimmer telecomInsertCwbDetailTimmer;
	@Autowired
	EpaiApiService_Download epaiApiService_Download;
	@Autowired
	EpaiApiService_ExportCallBack epaiApiService_ExportCallBack;
	@Autowired
	EpaiInsertCwbDetailTimmer epaiInsertCwbDetailTimmer;
	@Autowired
	EfastService_getOrderList efastService_getOrderList;
	@Autowired
	EfastService_getOrderDetailList efastService_getOrderDetailList;
	@Autowired
	EfastInsertCwbDetailTimmer efastInsertCwbDetailTimmer;
	@Autowired
	MaisikeService_branchSyn maisikeService_branchSyn;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
	@Autowired
	SmileInsertCwbDetailTimmer smileInsertCwbDetailTimmer;

	@Autowired
	LiantongInsertCwbDetailTimmer liantongInsertCwbDetailTimmer;

	@Autowired
	AppearWindowService appearWindowService;
	@Autowired
	ChinamobileService chinamobileService;

	@Autowired
	AccountDeductRecordService accountDeductRecordService;

	@Autowired
	YonghuiService yonghuiService;
	@Autowired
	HxgdmsInsertCwbDetailTimmer hxgdmsInsertCwbDetailTimmer;

	@Autowired
	WangjiuInsertCwbDetailTimmer wangjiuInsertCwbDetailTimmer;
	@Autowired
	LechongInsertCwbDetailTimmer lechongInsertCwbDetailTimmer;

	@Autowired
	HomegobjInsertCwbDetailTimmer homegobjInsertCwbDetailTimmer;
	@Autowired
	HomegobjService_getOrderDetailList homegobjService_getOrderDetailList;
	@Autowired
	SfxhmService_getOrder sfxhmService_getOrder;
	@Autowired
	SfxhmInsertCwbDetailTimmer sfxhmInsertCwbDetailTimmer;
	@Autowired
	ZhongliangInsertCwbDetailTimmer zhongliangInsertCwbDetailTimmer;
	@Autowired
	ZhongliangService zhongliangService;
	@Autowired
	ZhongliangBackOrderServices zhongliangBackOrderServices;
	@Autowired
	WenxuanInsertCwbDetailTimmer wenxuanInsertCwbDetailTimmer;
	@Autowired
	WenxuanService_getOrder wenxuanService_getOrder;
	@Autowired
	ExpressSysMonitorDAO expressSysMonitorDAO;
	@Autowired
	GuangZhouTongLuInsertCwbDetailTimmer guangZhouTongLuInsertCwbDetailTimmer;
	@Autowired
	FloworderLogService floworderLogService;
	@Autowired
	VipShopOXOGetPickStateService vipShopOXOGetPickStateService;
	@Autowired
	PjwlExpressService pjwlExpressService;
	@Autowired
	VipShopOXOJITFeedbackService vipShopOXOJITFeedbackService;
	@Autowired
	PunishInsideService punishInsideService;
	@Autowired
	JiuYeInsertCwbDetailTimmer jiuYeInsertCwbDetailTimmer;
	@Autowired
	OrderLifeCycleReportService orderLifeCycleReportService;
	@Autowired
	ZhemengInsertCwbDetailTimmer zhemengInsertCwbDetailTimmer;
	@Autowired
	VipshopInsertCwbDetailTimmer vipshopInsertCwbDetailTimmer;
	@Autowired
	GxDxInsertCwbDetailTimmer gxDxInsertCwbDetailTimmer;
	@Autowired
	FNWInsertCwbDetailTimmer fnwInsertCwbDetailTimmer;
	@Autowired
	AcquisitionOrderService acquisitionOrderService;
	@Autowired
	TPSInsertCwbDetailTimmer tPSInsertCwbDetailTimmer;
	@Autowired
	TPSCarrierOrderStatusTimmer tPSCarrierOrderStatusTimmer;
	@Autowired
	VipShopOXOInsertCwbDetailTimmer vipShopOXOInsertCwbDetailTimmer;
	@Autowired
	BackSummaryService backSummaryService;
	@Autowired
	LogToDayByTuihuoService logToDayByTuihuoService;
	@Autowired
	LogToDayByWarehouseService logToDayByWarehouseService;
	@Autowired
	LogToDayService logToDayService;
	@Autowired
	AutoDispatchStatusService autoDispatchStatusService;
	@Autowired
	HuanqiugouInsertCwbDetailTimmer huanqiugouInsertCwbDetailTimmer;
	
	@Autowired
	SuNingInsertCwbDetailTimmer suNingInsertCwbDetailTimmer;

	@Autowired
	MLKService mlkService;
	
	@Autowired
	YongHuiInsertCwbDetailTimmer yongHuiInsertCwbDetailTimmer;
	@Autowired
	PinhaohuoInsertCwbDetailTimmer pinhaohuoInsertCwbDetailTimmer;
	@Autowired
	FlowExpService flowExpService;
	@Autowired
	TpsCwbFlowPushService tpsCwbFlowPushService;
	@Autowired
	private ExpressOrderService expressOrderService;
	
	
	// public static Map<String, Integer> threadMap;
	public static RedisMap<String, Integer> threadMap;	
	@Autowired
	BranchInfService branchInfService;
	
	@Autowired
	UserInfService userInfService;
	@Autowired
	EMSService eMSService;
	@Autowired
	EMSTimmer eMSTimmer;
	
	@Autowired
	ReSendExpressOrderService reSendExpressOrderService;
	
	 @Autowired
     com.pjbest.util.dlock.IDistributedLock  distributedLock;
	
	static { // 静态初始化 以下变量,用于判断线程是否在执行
		JobUtil.threadMap = new RedisMapImpl<String, Integer>("JobUtil");
		JobUtil.threadMap.put("tmall", 0);
		JobUtil.threadMap.put("fanke", 0);
		JobUtil.threadMap.put("yihaodian", 0);
		JobUtil.threadMap.put("dangdang", 0);
		JobUtil.threadMap.put("yangguang", 0);
		JobUtil.threadMap.put("gzabc", 0);
		JobUtil.threadMap.put("hzabc", 0);
		JobUtil.threadMap.put("yixun", 0);
		JobUtil.threadMap.put("guomei", 0);
		JobUtil.threadMap.put("haoxianggou", 0);
		JobUtil.threadMap.put("maikolin", 0);
		JobUtil.threadMap.put("vipshop", 0);
		JobUtil.threadMap.put("vipshoptimmer", 0);
		JobUtil.threadMap.put("epaiDownload", 0); // 系统对接标识
		JobUtil.threadMap.put("zhongxingerp", 0); // 系统对接标识
		JobUtil.threadMap.put("xiaoxi", 0);// 消息列表
		JobUtil.threadMap.put("koukuan", 0);// 扣款结算
		JobUtil.threadMap.put("yonghuics", 0);
		JobUtil.threadMap.put("hxgdms", 0);
		JobUtil.threadMap.put("homegobj", 0);
		JobUtil.threadMap.put("zhongliang", 0);
		JobUtil.threadMap.put("guangzhoutonglu", 0);
		JobUtil.threadMap.put("vipshop_OXO", 0);
		JobUtil.threadMap.put("vipshop_OXO_pickstate", 0);
		// 快递
		JobUtil.threadMap.put("express_preOrder", 0);
		JobUtil.threadMap.put("express_transNo", 0);
		JobUtil.threadMap.put("vipshop_OXOJIT_feedback", 0);
		JobUtil.threadMap.put("punishinside_autoshenhe", 0);
		JobUtil.threadMap.put("order_lifecycle_report", 0);
		
		JobUtil.threadMap.put("getCarrierOrderStatusTimmer", 0);
		JobUtil.threadMap.put("vipshop_OXO_orderTempInsert", 0);
		JobUtil.threadMap.put("autoDispatchStatus", 0);
		
		JobUtil.threadMap.put("syncBranchInf", 0);
		JobUtil.threadMap.put("syncUserInf", 0);
		
		JobUtil.threadMap.put("tpsCwbFlow", 0);
		JobUtil.threadMap.put("tps_OXO_pickstate", 0);
		JobUtil.threadMap.put("expressOrderTransfer", 0);
		
		//EMS定时器
		JobUtil.threadMap.put("sendOrderToEMS", 0);
		JobUtil.threadMap.put("getEmsEmailNo", 0);
		JobUtil.threadMap.put("imitateEMSTraceToDmpOpt", 0);
	
		//快递单操作推TPS
		JobUtil.threadMap.put("resendExpressToTps", 0);
		
		//add by zhouhuan 订单数据从临时表转主表 2016-08-05
		JobUtil.threadMap.put("cwbInsertToOrderDetail", 0);
	}

	/**
	 * 手动初始化
	 */
	public void updateBatcnitialThreadMap() {
		JobUtil.threadMap.put("tmall", 0);
		JobUtil.threadMap.put("fanke", 0);
		JobUtil.threadMap.put("yihaodian", 0);
		JobUtil.threadMap.put("dangdang", 0);
		JobUtil.threadMap.put("yangguang", 0);
		JobUtil.threadMap.put("gzabc", 0);
		JobUtil.threadMap.put("hzabc", 0);
		JobUtil.threadMap.put("yixun", 0);
		JobUtil.threadMap.put("guomei", 0);
		JobUtil.threadMap.put("haoxianggou", 0);
		JobUtil.threadMap.put("maikolin", 0);
		JobUtil.threadMap.put("vipshop", 0);
		JobUtil.threadMap.put("vipshoptimmer", 0);
		JobUtil.threadMap.put("epaiDownload", 0);
		JobUtil.threadMap.put("zhongxingerp", 0); // 系统对接标识
		JobUtil.threadMap.put("yonghuics", 0);
		JobUtil.threadMap.put("hxgdms", 0);
		JobUtil.threadMap.put("homegobj", 0);
		JobUtil.threadMap.put("zhongliang", 0);
		JobUtil.threadMap.put("guangzhoutonglu", 0);
		JobUtil.threadMap.put("vipshop_OXO", 0);
		JobUtil.threadMap.put("vipshop_OXO_pickstate", 0);
		// 快递
		JobUtil.threadMap.put("express_preOrder", 0);
		JobUtil.threadMap.put("express_transNo", 0);
		JobUtil.threadMap.put("vipshop_OXOJIT_feedback", 0);
		JobUtil.threadMap.put("punishinside_autoshenhe", 0);
		JobUtil.threadMap.put("order_lifecycle_report", 0);
		JobUtil.threadMap.put("getCarrierOrderStatusTimmer", 0);
		JobUtil.threadMap.put("vipshop_OXO_orderTempInsert", 0);
		JobUtil.threadMap.put("autoDispatchStatus", 0);
		JobUtil.threadMap.put("syncBranchInf", 0);
		JobUtil.threadMap.put("syncUserInf", 0);
		JobUtil.threadMap.put("tpsCwbFlow", 0);
		JobUtil.threadMap.put("tps_OXO_pickstate", 0);
		JobUtil.threadMap.put("expressOrderTransfer", 0);
		this.logger.info("系统自动初始化定时器完成");
		
		//EMS定时器 
		JobUtil.threadMap.put("sendOrderToEMS", 0);
		JobUtil.threadMap.put("getEmsEmailNo", 0);
		JobUtil.threadMap.put("imitateEMSTraceToDmpOpt", 0);
		
		//快递单操作推TPS
		JobUtil.threadMap.put("resendExpressToTps", 0);
		
		//add by zhouhuan 订单数据从临时表转主表 2016-08-05
		JobUtil.threadMap.put("cwbInsertToOrderDetail", 0);
	}

	/**
	 * 执行重临时表获取tmall订单的定时器
	 */
	public void getTmallCwbDetailByTemp_Task() {

		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		synchronized (JobUtil.threadMap) {
			if (JobUtil.threadMap.get("tmall") == 1) {
				this.logger.warn("本地定时器没有执行完毕，跳出循环tmall");
				return;
			}
			JobUtil.threadMap.put("tmall", 1);
		}
		try {
			long starttime = System.currentTimeMillis();
			long endtime = System.currentTimeMillis();
			// tmallService.selectTempAndInsertToCwbDetail();
			this.tmallService.selectTempAndInsertToCwbDetailCount();

			this.logger.info("执行了临时表-获取tmall订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));

			this.dpfossInsertCwbDetailTimmer.selectTempAndInsertToCwbDetailTask();
			this.huitongtxInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			this.huitongtxInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.TMALL, endtime);

		} catch (Exception e) {
			this.logger.error("执行tmall定时器异常", e);
		} finally {
			JobUtil.threadMap.put("tmall", 0);
		}

	}

	// 系统设置 设置开启对外操作
	private String getSysOpenValue() {

		SystemInstall systemInstall = this.systemInstallDAO.getSystemInstall("isOpenJobHand");
		String sysValue = systemInstall.getValue();
		return sysValue;
	}

	/**
	 * 执行获取vipshop订单的定时器
	 */
	public void getVipShopCwbDetail_Task() {
		System.out.println("-----启动执行");
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}
		
		long starttime = 0;
		long endtime = 0;
		
		String taskName=this.getClass().getName() + "getVipShopCwbDetail_Task";
		try {
			
			boolean isAcquired=	distributedLock.tryLock(taskName, 1, 60*10, TimeUnit.SECONDS);	
			if (!isAcquired) {
				this.logger.warn("本地定时器没有执行完毕，跳出循环vipshop");
				return;
			}else{
				starttime = System.currentTimeMillis();
				this.vipShopService.excuteVipshopDownLoadTask();
				endtime = System.currentTimeMillis();
			}
		} catch (Exception e) {
			this.logger.error("执行vipshop定时器异常", e);
		} finally {
			 try {
		                 distributedLock.unlock(taskName);
		       } catch (Exception e) {
		       }
		}

		this.logger.info("执行了获取vipshop订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/**
	 * 执行获取maikaolin订单的定时器
	 */
	public void maikolin_Task() {
		long starttime = System.currentTimeMillis();
		try {
			String sysValue = this.getSysOpenValue();
			if ("yes".equals(sysValue)) {
				this.logger.warn("已开启远程定时调用,本地定时任务不生效");
				return;
			}

			if (JobUtil.threadMap.get("maikolin") == 1) {
				this.logger.warn("本地定时器没有执行完毕，跳出循环tmall");
				return;
			}
			JobUtil.threadMap.put("maikolin", 1);

			this.maikolinService.excute_getMaikolinTask(); // 下载

		} catch (Exception e) {
			this.logger.error("maikolin执行定时器异常", e);
		} finally {
			JobUtil.threadMap.put("maikolin", 0);

		}

		long endtime = System.currentTimeMillis();

		this.logger.info("执行了获取maikolin订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/**
	 * 执行获取广州通路从临时表查出数据插入到订单表中guangZhouTongLuInsertCwbDetailTimmer
	 */
	public void getGztlCwbDetail_Task() {

		if (JobUtil.threadMap.get("guangzhoutonglu") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环guangzhoutonglu");
			return;
		}
		JobUtil.threadMap.put("guangzhoutonglu", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			this.guangZhouTongLuInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.Guangzhoutonglu.getKey());
			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行vipshop定时器异常", e);
		} finally {
			JobUtil.threadMap.put("guangzhoutonglu", 0);
		}

		this.logger.info("执行了广州通路订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/*
	 * 扫货帮
	 */
	public void S_huobang_Task() {

		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("saohuobang")) {
				this.saohuobangInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(enums.getKey());
			}
		}

	}

	/*
	 * 快乐购
	 */
	public void HappyGo_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}
		try {
			JobUtil.threadMap.put("happigo", 1);
			HappyGo happy = this.happyGoService.getHappyGo(B2cEnum.happyGo.getKey());
			this.happyGoService.judgmentLanded(happy);
			this.happyGoService.callbackLanded(happy);
			this.happyGoService.insertDelivery(happy, FunctionForHappy.ChuKu.getText());// detail进入主表定时器别忘记加
			this.happyGoService.insertCallback(happy, FunctionForHappy.HuiShou.getText());
			this.happyGoService.happyGoForDetail();
			this.happyGoService.timerHappy();
		} catch (Exception e) {
			this.logger.error("执行快乐购异常！！", e);
		} finally {
			JobUtil.threadMap.put("happigo", 0);
		}

		this.happyGoService.excute_getHappyGoTask();
		this.logger.info("执行了快乐购定时器");
	}

	/**
	 * 执行获取一号店订单的定时器 20130606修改 兼容多个配置
	 */
	public void getYihaodianCwbDetail_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (JobUtil.threadMap.get("yihaodian") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环yihaodian");
			return;
		}
		JobUtil.threadMap.put("yihaodian", 1);

		try {
			long starttime = System.currentTimeMillis();

			this.yihaodianService.excute_getYihaodianTask();

			long endtime = System.currentTimeMillis();
			this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.YIHAODIAN, endtime);
			this.logger.info("执行了[一号店]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
			// getDiffB2cTimmer_Task();
		} catch (Exception e) {
			this.logger.error("0一号店0定时器调用发生未知异常", e);
		} finally {
			JobUtil.threadMap.put("yihaodian", 0);
		}

	}

	/**
	 * 各个电商对接临时表插入主表说明 以后统一不加入定时器了
	 */
	public void getDiffB2cTimmer_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		try {
			long starttime = System.currentTimeMillis();
			// dangDangSynInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.DangDang_daoru.getKey());
			this.yangGuangInsertCwbDetailTimmer.selectTempAndInsertToCwbDetailByMultipDiff(B2cEnum.YangGuang.getKey());
			this.guangZhouABCInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.GuangZhouABC.getKey());
			this.hangZhouABCInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.HangZhouABC.getKey());

			long endtime = System.currentTimeMillis();

			this.logger.info("执行了0央广0订单导入定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		} catch (Exception e) {
			this.logger.error("0当当0定时器调用发生未知异常", e);
		}

	}

	/**
	 * 执行重临时表获取易迅订单的定时器
	 */
	public void getYiXunCwbDetailByTemp_Task() {

		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (JobUtil.threadMap.get("yixun") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环yixun");
			return;
		}
		JobUtil.threadMap.put("yixun", 1);

		try {
			long starttime = System.currentTimeMillis();
			long endtime = System.currentTimeMillis();
			this.yiXunInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			this.logger.info("执行了临时表-获取[易迅网]订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		} catch (Exception e) {
			this.logger.error("0易迅网0定时器调用发生未知异常", e);
		} finally {
			JobUtil.threadMap.put("yixun", 0);
		}

		try {
			long starttime = System.currentTimeMillis();
			long endtime = System.currentTimeMillis();
			this.telecomInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.Telecomshop.getKey());
			this.logger.info("执行了临时表-获取[电信商城]订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		} catch (Exception e) {
			this.logger.error("0电信商城0定时器调用发生未知异常", e);
		}

	}

	/**
	 * 执行如风达{获取订单}{获取成功确认}{临时表插入主表}接口 调用频率是 每隔5分钟一次
	 */
	public void RufengdaInterfaceGetDataInvoke_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (JobUtil.threadMap.get("fanke") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环fanke");
			return;
		}
		JobUtil.threadMap.put("fanke", 1);

		try {
			long starttime = System.currentTimeMillis();
			this.rufengdaService.RufengdaInterfaceInvoke();

			long endtime = System.currentTimeMillis();
			this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.FANKE, endtime);
			this.logger.info("执行了临时表-获取[如风达]订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		} catch (Exception e) {
			this.logger.error("0如风达0定时器调用发生未知异常", e);
		} finally {
			JobUtil.threadMap.put("fanke", 0);
		}

	}

	/**
	 * 执行如风达{配送员信息同步}接口 调用频率是 每隔3天一次
	 */
	public void RufengdaInterfaceSynUserInvoke_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		long starttime = System.currentTimeMillis();
		this.rufengdaService.RufengdaInterfaceSynUserInfoInvoke();
		long endtime = System.currentTimeMillis();
		this.logger.info("执行了[如风达-配送员信息同步]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/**
	 * 执行获取国美订单数据
	 *
	 */
	public void getGome_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (JobUtil.threadMap.get("guomei") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环guomei");
			return;
		}
		JobUtil.threadMap.put("guomei", 1);

		try {
			long starttime = System.currentTimeMillis();
			this.gomeService.gomeInterfaceInvoke();
			long endtime = System.currentTimeMillis();
			this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.GOME, endtime);
			this.logger.info("执行了[执行获取国美订单数据]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		} catch (Exception e) {
			this.logger.error("0国美0定时器调用发生未知异常", e);
		} finally {
			JobUtil.threadMap.put("guomei", 0);
		}

	}

	/**
	 * 执行获取央广订单数据
	 *
	 */
	public void getYangGuang_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (JobUtil.threadMap.get("yangguang") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环yangguang");
			return;
		}
		JobUtil.threadMap.put("yangguang", 1);

		try {
			long starttime = System.currentTimeMillis();

			this.yangGuangService_download.getOrderDetailToYangGuangFTPServer(); // FTP下载订单
			this.yangGuangService_download.AnalyzTxtFileSaveB2cTemp(); // 解析txt插入临时表
			this.yangGuangService_download.MoveTxtFileToBakFile(); // 移动文件 到bak

			long endtime = System.currentTimeMillis();
			this.logger.info("执行了[从FTP获取央广订单数据]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));

			this.dongFangCJService_getOrder.downloadOrdersToDongFangCJFTPServer();
			this.dongFangCJService_getOrder.AnalyzTxtFileAndSaveB2cTemp();
			this.dongFangCJInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.DongFangCJ.getKey());
			this.dongFangCJService_getOrder.MoveTxtToDownload_BakFile();

			long endtime1 = System.currentTimeMillis();
			this.logger.info("执行了0从FTP获取东方CJ订单数据0定时器,本次耗时:{}秒", ((endtime1 - endtime) / 1000));

			this.homegouService_getOrder.downloadOrdersToHomeGouFTPServer();
			this.homegouService_getOrder.AnalyzTxtFileAndSaveB2cTemp();
			this.homegouInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.HomeGou.getKey());
			this.homegouService_getOrder.MoveTxtToDownload_BakFile();

			long endtime2 = System.currentTimeMillis();
			this.logger.info("执行了0从FTP获取家有购物订单数据0定时器,本次耗时:{}秒", ((endtime2 - endtime) / 1000));
		} catch (Exception e) {
			this.logger.error("0FTP获取家有购物0定时器调用发生未知异常", e);
		} finally {
			JobUtil.threadMap.put("yangguang", 0);
		}

	}

	/**
	 * 执行下载好享购订单数据的接口
	 *
	 */
	public void getHaoXiangGou_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (JobUtil.threadMap.get("haoxianggou") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环haoxianggou");
			return;
		}
		JobUtil.threadMap.put("haoxianggou", 1);

		long count = 0;
		try {
			// HaoXiangGou
			// hxg=haoXiangGouService.getHaoXiangGou(B2cEnum.HaoXiangGou.getKey());
			long starttime = System.currentTimeMillis();
			count += this.haoXiangGouService_PeiSong.GetOrdWayBillInfoForD2D();
			count += this.haoXiangGouService_TuiHuo.GetRtnWayBillInfoForD2D();
			this.hXGInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			// b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(hxg.getCustomerids(),DateTimeUtil.getNowTime(),count,"下载成功");

			long endtime = System.currentTimeMillis();
			this.logger.info("执行了[执行获取好享购订单数据]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));

			// efastInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		} catch (Exception e) {
			this.logger.error("0FTP获取家有购物0定时器调用发生未知异常", e);
		} finally {
			JobUtil.threadMap.put("haoxianggou", 0);
		}

	}

	public void getAmazon_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		long starttime = System.currentTimeMillis();
		this.amazonService.amazonGetOrderInvoke();
		long endtime = System.currentTimeMillis();
		this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.AMAZON, endtime);
		this.logger.info("执行了[执行获取亚马逊订单数据]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/*
	 * 系统之间的对接 订单下载
	 */
	public void getEpaiAPI_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (JobUtil.threadMap.get("epaiDownload") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环epaiapi");
			return;
		}
		JobUtil.threadMap.put("epaiDownload", 1);

		try {
			this.acquisitionOrderService.passiveReceptionOrActiveAcquisition();
			this.epaiApiService_ExportCallBack.exportCallBack_controllers();
			this.epaiInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		} catch (Exception e) {
			this.logger.error("下游订单下载定时器发生未知异常", e);
		} finally {
			this.logger.info("执行了易派系统对接订单下载接口");
			JobUtil.threadMap.put("epaiDownload", 0);
		}

	}

	public void getZhongXingYunGou_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		long starttime = System.currentTimeMillis();
		if (JobUtil.threadMap.get("zhongxingerp") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环zhongxingerp");
			return;
		}
		JobUtil.threadMap.put("zhongxingerp", 1);

		try {

			this.efastService_getOrderList.getOrderList();
			this.efastService_getOrderDetailList.getOrderDetailList();
			this.efastInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

		} catch (Exception e) {
			this.logger.error("中兴云购ERP定时器异常", e);
		} finally{
			JobUtil.threadMap.put("zhongxingerp", 0);
		}
		long endtime = System.currentTimeMillis();
		this.logger.info("执行了[获取中兴云购ERP订单]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		//JobUtil.threadMap.put("zhongxingerp", 0);
	}

	/**
	 * 执行如风达{迈思可站点信息同步}接口 调用频率是 每隔3天一次
	 */
	public void getMskBranchSyn_Task() {
		String sysValue = this.getSysOpenValue();
		if ("yes".equals(sysValue)) {
			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		long starttime = System.currentTimeMillis();
		this.maisikeService_branchSyn.syn_maisikeBranchs();
		long endtime = System.currentTimeMillis();
		this.logger.info("执行了【迈思可站点信息同步】定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/**
	 * 广州思迈速递
	 */
	public void getSmile_Task() {
		long starttime = System.currentTimeMillis();
		this.smileInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		long endtime = System.currentTimeMillis();
		this.logger.info("执行了【广州思迈】定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	public void gethappygogo() {
		this.happyGoService.gethappygoInfoTimmer();
		this.logger.info("happygo执行合并定时器完毕");
	}

	public void getLiantongTask() {
		this.liantongInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		this.logger.info("执行了联通定时器");
	}

	public void getChinamobile_Task() {

		this.chinamobileService.downloadOrdersToFTPServer();
		this.chinamobileService.AnalyzCSVFileAndSaveB2cTemp();
		this.chinamobileService.MoveTxtToDownload_BakFile();
		this.logger.info("执行了移动对接定时器");
	}

	/**
	 * 当当订单导入接口定时器
	 */
	public void getDangDang_Task() {
		try {
			if (JobUtil.threadMap.get("dangdang") == 1) {
				this.logger.info("当当订单导入定时器未执行完毕！....等待......");
				return;
			}
			JobUtil.threadMap.put("dangdang", 1);
			this.dangDangSynInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.DangDang_daoru.getKey());
			long endtime = System.currentTimeMillis();
			this.expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.DANGDANG, endtime);
			this.logger.info("执行了当当订单导入定时器！");
		} catch (Exception e) {
			this.logger.error("执行当当订单导入定时器异常", e);
		} finally {
			JobUtil.threadMap.put("dangdang", 0);
		}

	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}

	/**
	 * 消息列表的定时器
	 */
	public void getXiaoxiInfo_Task() {
		if (JobUtil.threadMap.get("xiaoxi") == 1) {
			this.logger.info("消息列表未执行完毕！....等待......");
			return;
		}
		JobUtil.threadMap.put("xiaoxi", 1);
		// appearWindowService.getAppearInfoShow();
		JobUtil.threadMap.put("xiaoxi", 0);
		this.logger.info("消息列表定时器执行完毕1");
	}

	/**
	 * 扣款结算订单自动到货定时器
	 */
	public void getKouKuanInfo_Task() {
		if (JobUtil.threadMap.get("koukuan") == 1) {
			this.logger.info("扣款结算自动到货未执行完毕！....等待......");
			return;
		}
		JobUtil.threadMap.put("koukuan", 1);
		try{
			this.accountDeductRecordService.updateJobKouKuan();
		}catch(Exception e){
			this.logger.error("执行扣款结算自动到货定时器异常", e);
		}finally{
			JobUtil.threadMap.put("koukuan", 0);
		}
		this.logger.info("扣款结算自动到货执行完毕！");
	}

	/**
	 * 永辉超市
	 */
	public void getYonghuics_Task() {
		try {
			if (JobUtil.threadMap.get("yonghuics") == 1) {
				this.logger.info("永辉超市订单导入定时器未执行完毕！....等待......");
				return;
			}
			JobUtil.threadMap.put("yonghuics", 1);
			this.yonghuiService.yonghuiInterfaceInvoke(B2cEnum.YongHuics.getKey());

			this.logger.info("执行了永辉超市订单导入定时器！");
		} catch (Exception e) {
			this.logger.error("执行永辉超市订单导入定时器异常", e);
		} finally {
			JobUtil.threadMap.put("yonghuics", 0);
		}

	}

	private Object hxgLock = new Object();

	/**
	 * 好享购DMS
	 */
	public void getHxgdms_Task() {
		try {
			synchronized (JobUtil.threadMap) {
				if (JobUtil.threadMap.get("hxgdms") == 1) {
					this.logger.info("好享购DMS订单导入定时器未执行完毕！....等待......");
					return;
				}
				JobUtil.threadMap.put("hxgdms", 1);
			}

			this.logger.info("I'm working.");
			Thread.sleep(10000L);
			this.hxgdmsInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			this.logger.info("执行了好享购DMS订单导入定时器！");
		} catch (Exception e) {
			this.logger.error("执行好享购DMS订单导入定时器异常", e);
		} finally {
			JobUtil.threadMap.put("hxgdms", 0);
		}
	}

	/**
	 * 网酒网
	 */
	public void getWangjiu_Task() {
		try {

			this.wangjiuInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			this.logger.info("执行了网酒网订单导入定时器！");
		} catch (Exception e) {
			this.logger.error("执行网酒网订单导入定时器异常", e);
		}

	}

	/**
	 * 乐宠
	 */
	public void getLeChong_Task() {
		try {

			this.lechongInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			this.logger.info("执行了乐宠订单导入定时器！");
		} catch (Exception e) {
			this.logger.error("执行乐宠订单导入定时器异常", e);
		}

	}

	/**
	 * 中粮
	 */
	public void getZhongliang_Task() {
		try {

			this.zhongliangService.waitOrdercounts();
			this.zhongliangService.CancelOrders();
			this.zhongliangBackOrderServices.BackOrdercounts();
			this.zhongliangBackOrderServices.CancelOrders();
			this.zhongliangInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			this.zhongliangBackOrderServices.BackOrdercounts();

			this.logger.info("执行了中粮订单导入定时器！");
		} catch (Exception e) {
			this.logger.error("执行中粮订单导入定时器异常", e);
		}

	}

	/**
	 * 家有购物bj
	 */
	public void getHomegobj_Task() {
		try {

			this.homegobjInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			this.logger.info("执行了家有购物北京导入临时表定时器！");

		} catch (Exception e) {
			this.logger.error("执行家有购物北京订单导入临时表定时器异常", e);
		}

	}

	/**
	 * 家有购物bj
	 */
	public void getHomegobj1_Task() {
		try {

			this.homegobjService_getOrderDetailList.excutorGetOrders();

			this.logger.info("执行了家有购物北京订单导入定时器！");

		} catch (Exception e) {
			this.logger.error("执行家有购物北京订单导入定时器异常", e);
		}

	}

	public void getSfxhm_Task() {
		try {

			this.sfxhmService_getOrder.downloadOrdersforRemoteProc();
			this.sfxhmInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			this.logger.info("执行了顺丰小红帽订单导入定时器！");

		} catch (Exception e) {
			this.logger.error("执行顺丰小红帽订单导入定时器异常", e);
		}

	}

	public void getWenxuan_Task() {
		try {

			this.wenxuanService_getOrder.excutorGetOrdersByTimes();
			this.wenxuanInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			this.logger.info("执行了文轩网订单导入定时器！");

		} catch (Exception e) {
			this.logger.error("执行文轩网订单导入定时器异常", e);
		}

	}

	/**
	 * 定时删除flowLog表
	 */
	public void deleteflowlog_Task() {
		try {

			this.floworderLogService.deteFlow();
		} catch (Exception e) {
			this.logger.error("执行了删除flowlog表定时器!", e);
		}

		this.logger.info("执行了删除flowlog表定时器!");
	}

	/**
	 * 定时抓取 唯品会OXO模式销售订单
	 */
	public void getVipShopOXOTask() {
		System.out.println("-----getVipShopOXOTask启动执行");
		// String sysValue = this.getSysOpenValue();
		// if ("yes".equals(sysValue)) {
		// this.logger.warn("已开启远程定时调用,本地定时任务不生效");
		// return;
		// }

		if (JobUtil.threadMap.get("vipshop_OXO") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环vipshop_OXO");
			return;
		}
		JobUtil.threadMap.put("vipshop_OXO", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			this.vipShopService.excuteVipshopOxoDownLoadTask();
			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行vipshop_OXO定时器异常", e);
		} finally {
			JobUtil.threadMap.put("vipshop_OXO", 0);
		}

		this.logger.info("执行了获取vipshop_OXO订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/**
	 * 抓取唯品会OXO订单揽件状态定时任务
	 */
	public void getVipShopOXOPickStateTask() {
		System.out.println("-----getVipShopOXOPickStateTask启动执行");
		// String sysValue = this.getSysOpenValue();
		// if ("yes".equals(sysValue)) {
		// this.logger.warn("已开启远程定时调用,本地定时任务不生效");
		// return;
		// }

		if (JobUtil.threadMap.get("vipshop_OXO_pickstate") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环vipshop_OXO_pickstate");
			return;
		}
		JobUtil.threadMap.put("vipshop_OXO_pickstate", 1);
		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			this.vipShopOXOGetPickStateService.getVipShopOXOPickState(B2cEnum.VipShop_OXO.getKey());
			endtime = System.currentTimeMillis();

		} catch (Exception e) {
			this.logger.error("执行vipshop_OXO_pickstate定时器异常", e);
		} finally {
			JobUtil.threadMap.put("vipshop_OXO_pickstate", 0);
		}
		this.logger.info("执行了获取vipshop_OXO_pickstate订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));

	}
	
	/**
	 * 抓取TPS,OXO订单揽件状态定时任务
	 */
	public void getTpsOXOPickStateTask() {
		System.out.println("-----getTpsOXOPickStateTask启动执行");
		// String sysValue = this.getSysOpenValue();
		// if ("yes".equals(sysValue)) {
		// this.logger.warn("已开启远程定时调用,本地定时任务不生效");
		// return;
		// }

		if (JobUtil.threadMap.get("tps_OXO_pickstate") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环tps_OXO_pickstate");
			return;
		}
		JobUtil.threadMap.put("tps_OXO_pickstate", 1);
		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			this.vipShopOXOGetPickStateService.getVipShopOXOPickState(B2cEnum.VipShop_TPSAutomate.getKey());
			endtime = System.currentTimeMillis();

		} catch (Exception e) {
			this.logger.error("执行tps_OXO_pickstate定时器异常", e);
		} finally {
			JobUtil.threadMap.put("tps_OXO_pickstate", 0);
		}
		this.logger.info("执行了获取tps_OXO_pickstate订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));

	}

	/**
	 * 定时抓取 快递预订单数据
	 */
	public void getPjwlExpressPreOrderTask() {
		System.out.println("-----getPjwlExpressPreOrderTask启动执行");

		if (JobUtil.threadMap.get("express_preOrder") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环express_preOrder");
			return;
		}
		JobUtil.threadMap.put("express_preOrder", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();

			this.pjwlExpressService.excutePjwlExpressDownLoadTask();

			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行express_preOrder定时器异常", e);
		} finally {
			JobUtil.threadMap.put("express_preOrder", 0);
		}

		this.logger.info("执行了获取express_preOrder订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/**
	 * 定时抓取 快递运单数据
	 * 不使用快递一期的定时器
	 * delete by jian_xie 2016-09-05
	 */
	@Deprecated
	public void getPjwlExpressTransNoTask() {
//		System.out.println("-----getPjwlExpressTransNoTask启动执行");
//
//		if (JobUtil.threadMap.get("express_transNo") == 1) {
//			this.logger.warn("本地定时器没有执行完毕，跳出循环express_transNo");
//			return;
//		}
//		JobUtil.threadMap.put("express_transNo", 1);
//
//		long starttime = 0;
//		long endtime = 0;
//		try {
//			starttime = System.currentTimeMillis();
//
//			this.pjwlExpressService.excutePjwlExpressTransNoSinfferTask();
//
//			endtime = System.currentTimeMillis();
//		} catch (Exception e) {
//			this.logger.error("执行express_transNo定时器异常", e);
//		} finally {
//			JobUtil.threadMap.put("express_transNo", 0);
//		}
//
//		this.logger.info("执行了获取express_transNo订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	public void sendVipShopOXOJITFeedbackTask() {
		System.out.println("-----sendVipShopOXOJITFeedbackTask启动执行");
		// String sysValue = this.getSysOpenValue();
		// if ("yes".equals(sysValue)) {
		// this.logger.warn("已开启远程定时调用,本地定时任务不生效");
		// return;
		// }

		if (JobUtil.threadMap.get("vipshop_OXOJIT_feedback") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环vipshop_OXOJIT_feedback");
			return;
		}
		JobUtil.threadMap.put("vipshop_OXOJIT_feedback", 1);
		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			this.vipShopOXOJITFeedbackService.sendVipShopOXOJITFeedback(B2cEnum.VipShop_OXO.getKey());
			endtime = System.currentTimeMillis();

		} catch (Exception e) {
			this.logger.error("执行vipshop_OXOJIT_feedback定时器异常", e);
		} finally {
			JobUtil.threadMap.put("vipshop_OXOJIT_feedback", 0);
		}
		this.logger.info("执行了获取vipshop_OXOJIT_feedback订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	public void sendPunishInsideAutoShenheTask() {
		System.out.println("-----sendPunishInsideAutoShenheTask启动执行");
		// String sysValue = this.getSysOpenValue();
		// if ("yes".equals(sysValue)) {
		// this.logger.warn("已开启远程定时调用,本地定时任务不生效");
		// return;
		// }

		if (JobUtil.threadMap.get("punishinside_autoshenhe") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环punishinside_autoshenhe");
			return;
		}
		JobUtil.threadMap.put("punishinside_autoshenhe", 1);
		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			this.punishInsideService.automaticShenheChengli();
			endtime = System.currentTimeMillis();

		} catch (Exception e) {
			this.logger.error("执行punishinside_autoshenhe定时器异常", e);
		} finally {
			JobUtil.threadMap.put("punishinside_autoshenhe", 0);
		}
		this.logger.info("执行了获取punishinside_autoshenhe订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/**
	 * 九曳
	 */
	public void getJiuYe_Task() {
		try {
			long starttime = System.currentTimeMillis();

			this.jiuYeInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			long endtime = System.currentTimeMillis();
			this.logger.info("执行了九曵订单导入定时器！本次耗时:{}秒", ((endtime - starttime) / 1000));

		} catch (Exception e) {
			this.logger.error("执行九曵订单导入定时器异常", e);
		}

	}

	// 飞牛网(http)
	public void getFeiNiuWang_Task() {
		try {
			long starttime = System.currentTimeMillis();
			this.fnwInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			long endtime = System.currentTimeMillis();
			this.logger.info("执行了飞牛网(http)订单导入定时器！本次耗时:{}秒", ((endtime - starttime) / 1000));

		} catch (Exception e) {
			this.logger.error("执行飞牛网(http)订单导入定时器异常", e);
		}
	}
	
	
	/**
	 * 广信电信
	 */
	public void getGuangXinDianXin() {
		try {
			long starttime = System.currentTimeMillis();
			this.gxDxInsertCwbDetailTimmer.execute(B2cEnum.GuangXinDianXin.getKey());
			long endtime = System.currentTimeMillis();
			this.logger.info("执行了广信电信订单导入定时器！本次耗时:{}秒", ((endtime - starttime) / 1000));

		} catch (Exception e) {
			this.logger.error("执行广信电信订单导入定时器异常", e);
		}

	}

	/**
	 * 生成前一天的生命周期报表
	 *
	 */
	public void generateOrderLifeCycleReport() {
		System.out.println("-----generateOrderLifeCycleReport启动执行");
		final String threadKey = "order_lifecycle_report";

		if (JobUtil.threadMap.get(threadKey) == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环generateOrderLifeCycleReport");
			return;
		}
		JobUtil.threadMap.put(threadKey, 1);
		long starttime = 0;
		long endtime = 0;

		try {
			starttime = System.currentTimeMillis();
			//
			String reportDate = DateTimeUtil.getDateBeforeDay(1);//in 'yyyy-MM-dd' format
			int batchSize = getbatchSizeOfOrderLifeCycleReport();

			// 生成前一天的订单详情列表
			this.orderLifeCycleReportService.genLifeCycleOrderDetail(batchSize, reportDate);

			// 生成前一天的生命周期报表
			this.orderLifeCycleReportService.genLifeCycleReport(reportDate);

			endtime = System.currentTimeMillis();

		} catch (Exception e) {
			this.logger.error("执行generateOrderLifeCycleReport定时器异常", e);
		} finally {
			JobUtil.threadMap.put(threadKey, 0);
		}
		this.logger.info("执行了获取generateOrderLifeCycleReport订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	public void getZheMeng_Task() {
		try {

			this.zhemengInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.ZheMeng.getKey());

			this.logger.info("执行了哲盟-安达信订单导入定时器！");

		} catch (Exception e) {
			this.logger.error("执行哲盟-安达信订单导入定时器异常", e);
		}

	}

	/**
	 * 环球购物
	 */
	public void getHuanQiuGouWu_Task() {
		try {
			long starttime = System.currentTimeMillis();
			this.huanqiugouInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			long endtime = System.currentTimeMillis();
			this.logger.info("执行了-环球购物-订单导入定时器！本次耗时:{}秒", ((endtime - starttime) / 1000));

		} catch (Exception e) {
			this.logger.error("执行了-环球购物-订单导入定时器异常", e);
		}
	}
	
	
	/**
	 * 订单生命周期报表每批次处理的行数,如果找不到，默认是1000
	 *
	 * @return
	 */
	private int getbatchSizeOfOrderLifeCycleReport() {
		// 系统设置 设置开启对外操作
		int defBatchSize = 1000;
		int ret = defBatchSize;
		try {
			SystemInstall systemInstall = this.systemInstallDAO.getSystemInstall("isOpenJobHand");
			String sysValue = systemInstall.getValue();
			if (systemInstall != null) {
				ret = Integer.parseInt(sysValue);

			}
		} catch (Exception e) {
			System.err.println("[getbatchSizeOfOrderLifeCycleReport error:]" + e.getMessage());
		}

		return ret;

	}

	/**
	 * 执行获取vipshop临时表插入主表log
	 */
	public void getVipShopCwbTempInsert_Task() {
		
		if (JobUtil.threadMap.get("vipshoptimmer") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环vipshop");
			return;
		}
		JobUtil.threadMap.put("vipshoptimmer", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			this.vipshopInsertCwbDetailTimmer.selectTempAndInsertToCwbDetails();
			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行vipshoptimmer定时器异常", e);
		} finally {
			JobUtil.threadMap.put("vipshoptimmer", 0);
		}

		this.logger.info("执行了获取vipshoptimmer订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}
	
	/**
	 * 永辉超市
	 */
	public void getYongHui_Task() {
		try {
			long starttime = System.currentTimeMillis();
			this.yongHuiInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			long endtime = System.currentTimeMillis();
			this.logger.info("执行了-永辉-订单导入定时器！本次耗时:{}秒", ((endtime - starttime) / 1000));

		} catch (Exception e) {
			this.logger.error("执行了-永辉-订单导入定时器异常", e);
		}
	}
	
	/**
	 * 【苏宁易购】定时器任务调用
	 */
	public void getSuNing_Task() {
		try {
			this.suNingInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			this.logger.info("执行了【苏宁易购】订单导入detail表定时器！");
		} catch (Exception e) {
			this.logger.error("执行【苏宁易购】订单导入detail表异常", e);
		}

	}
	//拼好货
		public void getPinhaohuo_Task() {
				try {
					long starttime = System.currentTimeMillis();
					this.pinhaohuoInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
					long endtime = System.currentTimeMillis();
					this.logger.info("执行了拼好货订单导入定时器！本次耗时:{}秒", ((endtime - starttime) / 1000));

				} catch (Exception e) {
					this.logger.error("执行拼好货订单导入定时器异常", e);
				}

			}
			
			public void setFlowExp() {
				long starttime = 0;
				long endtime = 0;
				try {
					starttime = System.currentTimeMillis();
					this.flowExpService.sendFlowExp();
					endtime = System.currentTimeMillis();
				} catch (Exception e) {
					this.logger.error("执行sendFlow定时器异常", e);
				} 
				this.logger.info("执行sendFlow定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
				}
	
	/**
	 * 执行获取物流运单状态接口
	 */
	public void getCarrierOrderStatus_Task() {
		System.out.println("-----getCarrierOrderStatus_Task启动执行");
		
		if (JobUtil.threadMap.get("getCarrierOrderStatusTimmer") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环");
			return;
		}
		JobUtil.threadMap.put("getCarrierOrderStatusTimmer", 1);
		
		try {
			long starttime = System.currentTimeMillis();
			
			this.tPSCarrierOrderStatusTimmer.getCarrierOrderStatus();
			
			long endtime = System.currentTimeMillis();
			this.logger.info("执行了物流状态查询定时器！本次耗时:{}秒", ((endtime - starttime) / 1000));
		} catch (Exception e) {
			this.logger.error("执行获取物流状态查询定时器异常", e);
		} finally {
			JobUtil.threadMap.put("getCarrierOrderStatusTimmer", 0);
		}
	}
	
	/*
	 * OXO数据从临时表转订单主表
	 */
	public void getOXOCwbTempInsert_Task() {

		if (JobUtil.threadMap.get("vipshop_OXO_orderTempInsert") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环tps");
			return;
		}
		JobUtil.threadMap.put("vipshop_OXO_orderTempInsert", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			vipShopOXOInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.VipShop_TPSAutomate.getKey());
			//this.tPSInsertCwbDetailTimmer.selectTempAndInsertToCwbDetails();
			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行TPS订单临时表数据插入主表定时器异常", e);
		} finally {
			JobUtil.threadMap.put("vipshop_OXO_orderTempInsert", 0);
		}

		this.logger.info("执行了获取TPS订单临时表数据插入主表的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}
			
	/**
	 * 退货中心出入库跟踪表日志
	 */
	public void createBackTimeLog(){
		try {
			SystemInstall backTimeLog = systemInstallDAO.getSystemInstallByName("backTimeLog");
			if (backTimeLog == null || !StringUtils.hasLength(backTimeLog.getValue())) {
				logger.info("退货中心出入库跟踪表日志未启用，没有找到参数{}", backTimeLog);
				return;
			}
			backSummaryService.createBackTimeLog();
		}catch(Exception e){
			logger.error("退货中心出入库跟踪表日志生成异常："+e.getMessage());
		}
		
	}
	
	/**
	 * 退货日志
	 */
	public void dailyDayLogByTuihuoGenerate(){
		try {
			SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("tuiHuoDayLogTime");
			if (siteDayLogTime == null || !StringUtils.hasLength(siteDayLogTime.getValue())) {
				logger.info("退货日志未启用，没有找到参数{}", siteDayLogTime);
				return;
			}
			logToDayByTuihuoService.dailyDayLogByTuihuoGenerate();
		}catch(Exception e){
			logger.error("退货日志生成异常："+e.getMessage());
		}
	}
	
	/**
	 * 库房日志
	 */
	public void dailyDayLogByWareHouseGenerate(){
		try {
			SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("wareHouseDayLogTime");
			if (siteDayLogTime == null || !StringUtils.hasLength(siteDayLogTime.getValue())) {
				logger.info("库房日志未启用，没有找到参数{}", siteDayLogTime);
				return;
			}
			logToDayByWarehouseService.dailyDayLogByWareHouseGenerate();
		}catch(Exception e){
			logger.error("库房日志生成异常："+e.getMessage());
		}
	}
	
	/**
	 * 站点日志
	 */
	public void dailyDayLogGenerate(){
		try{
			final SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("siteDayLogTime");
			if (siteDayLogTime == null || !StringUtils.hasLength(siteDayLogTime.getValue())) {
				logger.warn("站点日志未启用，没有找到参数{}", siteDayLogTime);
				return;
			}
			logToDayService.dailyDayLogGenerate();
		}catch(Exception e){
			logger.error("站点日志生成异常："+e.getMessage());
		}
	}
	
	/*
	 * 自动化分拣状态数据从临时表转业务表
	 */
	public void getAutoDispatchStatus_Task() {

		if (JobUtil.threadMap.get("autoDispatchStatus") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环autoDispatch");
			return;
		}
		JobUtil.threadMap.put("autoDispatchStatus", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			autoDispatchStatusService.process();
			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行自动化分拣状态数据从临时表转业务表时器异常", e);
		} finally {
			JobUtil.threadMap.put("autoDispatchStatus", 0);
		}

		this.logger.info("执行了自动化分拣状态数据从临时表转业务表的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}
	
	/**
	 * 同步站点机构
	 */
	public void syncBranchInf_Task() {
		if (JobUtil.threadMap.get("syncBranchInf") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出syncBranchInf");
			return;
		}
		JobUtil.threadMap.put("syncBranchInf", 1);
		long startTime = 0;
		long endTime = 0;
		try {
			startTime = System.currentTimeMillis();
			branchInfService.processSync();
			endTime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行了同步站点机构的定时器异常：", e);
		} finally {
			JobUtil.threadMap.put("syncBranchInf", 0);
		}
		this.logger.info("执行了同步站点机构的定时器,本次耗时:{}秒", ((endTime - startTime) / 1000));
	}
	
	/**
	 * 同步小件员
	 */
	public void syncUserInf_Task(){
		if (JobUtil.threadMap.get("syncUserInf") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出syncUserInf");
			return;
		}
		JobUtil.threadMap.put("syncUserInf", 1);
		long startTime = 0;
		long endTime = 0;
		try {
			startTime = System.currentTimeMillis();
			userInfService.processSync();
			endTime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行了同步小件员的定时器异常：", e);
		} finally {
			JobUtil.threadMap.put("syncUserInf", 0);
		}
		this.logger.info("执行了同步小件员的定时器,本次耗时:{}秒", ((endTime - startTime) / 1000));
	}
	
	/**
	 * 订单重量体积反馈给TPS
	 */
	public void tpsCwbFlow_Task(){
		if (JobUtil.threadMap.get("tpsCwbFlow") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出订单重量体积反馈TPS的任务");
			return;
		}
		JobUtil.threadMap.put("tpsCwbFlow", 1);
		long startTime = 0;
		long endTime = 0;
		try {
			startTime = System.currentTimeMillis();
			tpsCwbFlowPushService.process();
			endTime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行了订单重量体积反馈TPS的定时器异常：", e);
		} finally {
			JobUtil.threadMap.put("tpsCwbFlow", 0);
		}
		this.logger.info("执行了订单重量体积反馈TPS的定时器,本次耗时:{}秒", ((endTime - startTime) / 1000));
	}
	
	/**
	 * 定时器，处理快递临时表转业务
	 */
	public void expressOrderTransfer_Task(){
		System.out.println("-----expressOrderTransfer启动执行");

		if (JobUtil.threadMap.get("expressOrderTransfer") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环expressOrderTransfer");
			return;
		}
		JobUtil.threadMap.put("expressOrderTransfer", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			expressOrderService.expressOrderTransfer();
			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行expressOrderTransfer定时器异常", e);
		} finally {
			JobUtil.threadMap.put("expressOrderTransfer", 0);
		}

		this.logger.info("执行了获取expressOrderTransfer订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		
	}
	/**
	 * 执行获取EMS运单号的定时器
	 */
	public void getEmsEmailNo_task() {
		long starttime = System.currentTimeMillis();
		try {
			String sysValue = this.getSysOpenValue();
			if ("yes".equals(sysValue)) {
				this.logger.warn("已开启远程定时调用,本地定时任务不生效");
				return;
			}

			if (JobUtil.threadMap.get("getEmsEmailNo") == 1) {
				this.logger.warn("本地定时器没有执行完毕，跳出循环EMS运单号获取");
				return;
			}
			JobUtil.threadMap.put("getEmsEmailNo", 1);

			this.eMSTimmer.getEmsMailNoTask(); // 下载

		} catch (Exception e) {
			this.logger.error("getEmsEmailNo执行定时器异常", e);
		} finally {
			JobUtil.threadMap.put("getEmsEmailNo", 0);

		}

		long endtime = System.currentTimeMillis();

		this.logger.info("执行了获取getEmsEmailNo订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	//执行ems轨迹模拟dmp操作定时器  EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());
	public void imitateEMSTraceToDmpOpt_task() {
		if (JobUtil.threadMap.get("imitateEMSTraceToDmpOpt") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环EMS轨迹模拟dmp操作");
			return;
		}
		JobUtil.threadMap.put("imitateEMSTraceToDmpOpt", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			this.eMSTimmer.saveFlowInfo();
			this.eMSTimmer.imitateDmpOpt();
			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行imitateEMSTraceToDmpOpt定时器异常", e);
		} finally {
			JobUtil.threadMap.put("imitateEMSTraceToDmpOpt", 0);
		}

		this.logger.info("执行了获取imitateEMSTraceToDmpOpt订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}
	
	//执行推送订单信息给ems定时器 ;
	public void sendOrderToEMS_task() {
		if (JobUtil.threadMap.get("sendOrderToEMS") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环EMS订单推送操作");
			return;
		}
		JobUtil.threadMap.put("sendOrderToEMS", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			this.eMSTimmer.sendOrderToEMS();
			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行sendOrderToEMS定时器异常", e);
		} finally {
			JobUtil.threadMap.put("sendOrderToEMS", 0);
		}

		this.logger.info("执行了获取sendOrderToEMS订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}
	
	/**
	 * 重推快递单各种操作给TPS
	 * @author leo01.liao
	 */
	public void resendExpressToTps(){
		System.out.println("-----resendExpressToTps启动执行");
		
		JobUtil.threadMap.put("resendExpressToTps", 0);

		if (JobUtil.threadMap.get("resendExpressToTps") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环resendExpressToTps");
			return;
		}
		JobUtil.threadMap.put("resendExpressToTps", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			this.reSendExpressOrderService.resendToTps();
			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行resendExpressToTps定时器异常", e);
		} finally {
			JobUtil.threadMap.put("resendExpressToTps", 0);
		}

		this.logger.info("执行了获取resendExpressToTps订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		
	}
	
	
	//add by zhouhuan tps对接新增非唯品会订单转业务定时器，查询临时表，插入数据到detail表中 2016-08-05
	public void getCwbTempInsert_Task() {
		if (JobUtil.threadMap.get("cwbInsertToOrderDetail") == 1) {
			this.logger.warn("本地定时器没有执行完毕，跳出循环cwbInsertToOrderDetail");
			return;
		}
		JobUtil.threadMap.put("cwbInsertToOrderDetail", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			this.vipshopInsertCwbDetailTimmer.selectAllTempAndInsertToCwbDetails();
			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			this.logger.error("执行cwbInsertToOrderDetail定时器异常", e);
		} finally {
			JobUtil.threadMap.put("cwbInsertToOrderDetail", 0);
		}

		this.logger.info("执行了获取cwbInsertToOrderDetail订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}
}
