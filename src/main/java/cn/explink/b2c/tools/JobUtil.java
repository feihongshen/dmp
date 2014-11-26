package cn.explink.b2c.tools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.b2c.amazon.AmazonService;
import cn.explink.b2c.chinamobile.ChinamobileService;
import cn.explink.b2c.dangdang_dataimport.DangDangSynInsertCwbDetailTimmer;
import cn.explink.b2c.dongfangcj.DongFangCJInsertCwbDetailTimmer;
import cn.explink.b2c.dongfangcj.DongFangCJService_getOrder;
import cn.explink.b2c.dpfoss.DpfossInsertCwbDetailTimmer;
import cn.explink.b2c.efast.EfastInsertCwbDetailTimmer;
import cn.explink.b2c.efast.EfastService_getOrderDetailList;
import cn.explink.b2c.efast.EfastService_getOrderList;
import cn.explink.b2c.explink.core_down.EpaiApiService_Download;
import cn.explink.b2c.explink.core_down.EpaiApiService_ExportCallBack;
import cn.explink.b2c.explink.core_down.EpaiInsertCwbDetailTimmer;
import cn.explink.b2c.gome.GomeService;
import cn.explink.b2c.gzabc.GuangZhouABCInsertCwbDetailTimmer;
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
import cn.explink.b2c.huitongtx.HuitongtxInsertCwbDetailTimmer;
import cn.explink.b2c.hxgdms.HxgdmsInsertCwbDetailTimmer;
import cn.explink.b2c.hzabc.HangZhouABCInsertCwbDetailTimmer;
import cn.explink.b2c.lechong.LechongInsertCwbDetailTimmer;
import cn.explink.b2c.liantong.LiantongInsertCwbDetailTimmer;
import cn.explink.b2c.maikaolin.MaikaolinInsertCwbDetailTimmer;
import cn.explink.b2c.maikaolin.MaikolinService;
import cn.explink.b2c.maisike.MaisikeService_branchSyn;
import cn.explink.b2c.rufengda.RufengdaService;
import cn.explink.b2c.saohuobang.SaohuobangInsertCwbDetailTimmer;
import cn.explink.b2c.sfxhm.SfxhmInsertCwbDetailTimmer;
import cn.explink.b2c.sfxhm.SfxhmService_getOrder;
import cn.explink.b2c.smile.SmileInsertCwbDetailTimmer;
import cn.explink.b2c.telecomsc.TelecomInsertCwbDetailTimmer;
import cn.explink.b2c.tmall.TmallInsertCwbDetailTimmer;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.b2c.vipshop.VipShopGetCwbDataService;
import cn.explink.b2c.vipshop.VipShopService;
import cn.explink.b2c.vipshop.VipshopInsertCwbDetailTimmer;
import cn.explink.b2c.wangjiu.WangjiuInsertCwbDetailTimmer;
import cn.explink.b2c.wenxuan.WenxuanInsertCwbDetailTimmer;
import cn.explink.b2c.wenxuan.WenxuanService_getOrder;
import cn.explink.b2c.yangguang.YangGuangInsertCwbDetailTimmer;
import cn.explink.b2c.yangguang.YangGuangService_download;
import cn.explink.b2c.yihaodian.YihaodianService;
import cn.explink.b2c.yixun.YiXunInsertCwbDetailTimmer;
import cn.explink.b2c.yonghuics.YonghuiService;
import cn.explink.b2c.zhongliang.ZhongliangInsertCwbDetailTimmer;
import cn.explink.b2c.zhongliang.ZhongliangService;
import cn.explink.dao.ExpressSysMonitorDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;
import cn.explink.enumutil.ExpressSysMonitorEnum;
import cn.explink.service.AccountDeductRecordService;
import cn.explink.service.AppearWindowService;

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
	VipshopInsertCwbDetailTimmer vipshopInsertCwbDetailTimmer;
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
	WenxuanInsertCwbDetailTimmer wenxuanInsertCwbDetailTimmer;
	@Autowired
	WenxuanService_getOrder wenxuanService_getOrder;
	@Autowired
	ExpressSysMonitorDAO expressSysMonitorDAO;

	public static Map<String, Integer> threadMap;
	static { // 静态初始化 以下变量,用于判断线程是否在执行

		threadMap = new HashMap<String, Integer>();
		threadMap.put("tmall", 0);
		threadMap.put("fanke", 0);
		threadMap.put("yihaodian", 0);
		threadMap.put("dangdang", 0);
		threadMap.put("yangguang", 0);
		threadMap.put("gzabc", 0);
		threadMap.put("hzabc", 0);
		threadMap.put("yixun", 0);
		threadMap.put("guomei", 0);
		threadMap.put("haoxianggou", 0);
		threadMap.put("maikolin", 0);
		threadMap.put("vipshop", 0);
		threadMap.put("epaiDownload", 0); // 系统对接标识
		threadMap.put("zhongxingerp", 0); // 系统对接标识
		threadMap.put("xiaoxi", 0);// 消息列表
		threadMap.put("koukuan", 0);// 扣款结算
		threadMap.put("yonghuics", 0);
		threadMap.put("hxgdms", 0);
		threadMap.put("homegobj", 0);
		threadMap.put("zhongliang", 0);

	}

	/**
	 * 手动初始化
	 */
	public void updateBatcnitialThreadMap() {
		threadMap.put("tmall", 0);
		threadMap.put("fanke", 0);
		threadMap.put("yihaodian", 0);
		threadMap.put("dangdang", 0);
		threadMap.put("yangguang", 0);
		threadMap.put("gzabc", 0);
		threadMap.put("hzabc", 0);
		threadMap.put("yixun", 0);
		threadMap.put("guomei", 0);
		threadMap.put("haoxianggou", 0);
		threadMap.put("maikolin", 0);
		threadMap.put("vipshop", 0);
		threadMap.put("epaiDownload", 0);
		threadMap.put("zhongxingerp", 0); // 系统对接标识
		threadMap.put("yonghuics", 0);
		threadMap.put("hxgdms", 0);
		threadMap.put("homegobj", 0);
		threadMap.put("zhongliang", 0);
		logger.info("系统自动初始化定时器完成");
	}

	public void getVipShopCwbDetail_Task3() {

		System.out.println("哇哈哈哈哈哈哈哈哈");
	}

	/**
	 * 执行重临时表获取tmall订单的定时器
	 */
	public void getTmallCwbDetailByTemp_Task() {

		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		synchronized (threadMap) {
			if (threadMap.get("tmall") == 1) {
				logger.warn("本地定时器没有执行完毕，跳出循环tmall");
				return;
			}
			threadMap.put("tmall", 1);
		}
		try {
			long starttime = System.currentTimeMillis();
			long endtime = System.currentTimeMillis();
			// tmallService.selectTempAndInsertToCwbDetail();
			tmallService.selectTempAndInsertToCwbDetailCount();

			logger.info("执行了临时表-获取tmall订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));

			dpfossInsertCwbDetailTimmer.selectTempAndInsertToCwbDetailTask();
			huitongtxInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			huitongtxInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.TMALL, endtime);

		} catch (Exception e) {
			logger.error("执行tmall定时器异常", e);
		} finally {
			threadMap.put("tmall", 0);
		}

	}

	// 系统设置 设置开启对外操作
	private String getSysOpenValue() {

		SystemInstall systemInstall = systemInstallDAO.getSystemInstall("isOpenJobHand");
		String sysValue = systemInstall.getValue();
		return sysValue;
	}

	/**
	 * 执行获取vipshop订单的定时器
	 */
	public void getVipShopCwbDetail_Task() {
		System.out.println("-----启动执行");
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (threadMap.get("vipshop") == 1) {
			logger.warn("本地定时器没有执行完毕，跳出循环vipshop");
			return;
		}
		threadMap.put("vipshop", 1);

		long starttime = 0;
		long endtime = 0;
		try {
			starttime = System.currentTimeMillis();
			vipShopService.excuteVipshopDownLoadTask();
			endtime = System.currentTimeMillis();
		} catch (Exception e) {
			logger.error("执行vipshop定时器异常", e);
		} finally {
			threadMap.put("vipshop", 0);
		}

		logger.info("执行了获取vipshop订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/**
	 * 执行获取maikaolin订单的定时器
	 */
	public void maikolin_Task() {
		long starttime = System.currentTimeMillis();
		try {
			String sysValue = getSysOpenValue();
			if ("yes".equals(sysValue)) {
				logger.warn("已开启远程定时调用,本地定时任务不生效");
				return;
			}

			if (threadMap.get("maikolin") == 1) {
				logger.warn("本地定时器没有执行完毕，跳出循环tmall");
				return;
			}
			threadMap.put("maikolin", 1);

			maikolinService.excute_getMaikolinTask(); // 下载

		} catch (Exception e) {
			logger.error("maikolin执行定时器异常", e);
		} finally {
			threadMap.put("maikolin", 0);

		}

		long endtime = System.currentTimeMillis();

		logger.info("执行了获取maikolin订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/*
	 * 扫货帮
	 */
	public void S_huobang_Task() {

		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("saohuobang")) {
				saohuobangInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(enums.getKey());
			}
		}

	}

	/*
	 * 快乐购
	 */
	public void HappyGo_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}
		try {
			threadMap.put("happigo", 1);
			HappyGo happy = happyGoService.getHappyGo(B2cEnum.happyGo.getKey());
			happyGoService.judgmentLanded(happy);
			happyGoService.callbackLanded(happy);
			happyGoService.insertDelivery(happy, FunctionForHappy.ChuKu.getText());// detail进入主表定时器别忘记加
			happyGoService.insertCallback(happy, FunctionForHappy.HuiShou.getText());
			happyGoService.happyGoForDetail();
			happyGoService.timerHappy();
		} catch (Exception e) {
			logger.error("执行快乐购异常！！", e);
		} finally {
			threadMap.put("happigo", 0);
		}

		happyGoService.excute_getHappyGoTask();
		logger.info("执行了快乐购定时器");
	}

	/**
	 * 执行获取一号店订单的定时器 20130606修改 兼容多个配置
	 */
	public void getYihaodianCwbDetail_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (threadMap.get("yihaodian") == 1) {
			logger.warn("本地定时器没有执行完毕，跳出循环yihaodian");
			return;
		}
		threadMap.put("yihaodian", 1);

		try {
			long starttime = System.currentTimeMillis();

			yihaodianService.excute_getYihaodianTask();

			long endtime = System.currentTimeMillis();
			expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.YIHAODIAN, endtime);
			logger.info("执行了[一号店]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
			// getDiffB2cTimmer_Task();
		} catch (Exception e) {
			logger.error("0一号店0定时器调用发生未知异常", e);
		} finally {
			threadMap.put("yihaodian", 0);
		}

	}

	/**
	 * 各个电商对接临时表插入主表说明 以后统一不加入定时器了
	 */
	public void getDiffB2cTimmer_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		try {
			long starttime = System.currentTimeMillis();
			// dangDangSynInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.DangDang_daoru.getKey());
			yangGuangInsertCwbDetailTimmer.selectTempAndInsertToCwbDetailByMultipDiff(B2cEnum.YangGuang.getKey());
			guangZhouABCInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.GuangZhouABC.getKey());
			hangZhouABCInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.HangZhouABC.getKey());

			long endtime = System.currentTimeMillis();

			logger.info("执行了0央广0订单导入定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		} catch (Exception e) {
			logger.error("0当当0定时器调用发生未知异常", e);
		}

	}

	/**
	 * 执行重临时表获取易迅订单的定时器
	 */
	public void getYiXunCwbDetailByTemp_Task() {

		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (threadMap.get("yixun") == 1) {
			logger.warn("本地定时器没有执行完毕，跳出循环yixun");
			return;
		}
		threadMap.put("yixun", 1);

		try {
			long starttime = System.currentTimeMillis();
			long endtime = System.currentTimeMillis();
			yiXunInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			logger.info("执行了临时表-获取[易迅网]订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		} catch (Exception e) {
			logger.error("0易迅网0定时器调用发生未知异常", e);
		} finally {
			threadMap.put("yixun", 0);
		}

		try {
			long starttime = System.currentTimeMillis();
			long endtime = System.currentTimeMillis();
			telecomInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.Telecomshop.getKey());
			logger.info("执行了临时表-获取[电信商城]订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		} catch (Exception e) {
			logger.error("0电信商城0定时器调用发生未知异常", e);
		}

	}

	/**
	 * 执行如风达{获取订单}{获取成功确认}{临时表插入主表}接口 调用频率是 每隔5分钟一次
	 */
	public void RufengdaInterfaceGetDataInvoke_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (threadMap.get("fanke") == 1) {
			logger.warn("本地定时器没有执行完毕，跳出循环fanke");
			return;
		}
		threadMap.put("fanke", 1);

		try {
			long starttime = System.currentTimeMillis();
			rufengdaService.RufengdaInterfaceInvoke();

			long endtime = System.currentTimeMillis();
			expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.FANKE, endtime);
			logger.info("执行了临时表-获取[如风达]订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		} catch (Exception e) {
			logger.error("0如风达0定时器调用发生未知异常", e);
		} finally {
			threadMap.put("fanke", 0);
		}

	}

	/**
	 * 执行如风达{配送员信息同步}接口 调用频率是 每隔3天一次
	 */
	public void RufengdaInterfaceSynUserInvoke_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		long starttime = System.currentTimeMillis();
		rufengdaService.RufengdaInterfaceSynUserInfoInvoke();
		long endtime = System.currentTimeMillis();
		logger.info("执行了[如风达-配送员信息同步]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/**
	 * 执行获取国美订单数据
	 * 
	 */
	public void getGome_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (threadMap.get("guomei") == 1) {
			logger.warn("本地定时器没有执行完毕，跳出循环guomei");
			return;
		}
		threadMap.put("guomei", 1);

		try {
			long starttime = System.currentTimeMillis();
			gomeService.gomeInterfaceInvoke();
			long endtime = System.currentTimeMillis();
			expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.GOME, endtime);
			logger.info("执行了[执行获取国美订单数据]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		} catch (Exception e) {
			logger.error("0国美0定时器调用发生未知异常", e);
		} finally {
			threadMap.put("guomei", 0);
		}

	}

	/**
	 * 执行获取央广订单数据
	 * 
	 */
	public void getYangGuang_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (threadMap.get("yangguang") == 1) {
			logger.warn("本地定时器没有执行完毕，跳出循环yangguang");
			return;
		}
		threadMap.put("yangguang", 1);

		try {
			long starttime = System.currentTimeMillis();

			yangGuangService_download.getOrderDetailToYangGuangFTPServer(); // FTP下载订单
			yangGuangService_download.AnalyzTxtFileSaveB2cTemp(); // 解析txt插入临时表
			yangGuangService_download.MoveTxtFileToBakFile(); // 移动文件 到bak

			long endtime = System.currentTimeMillis();
			logger.info("执行了[从FTP获取央广订单数据]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));

			dongFangCJService_getOrder.downloadOrdersToDongFangCJFTPServer();
			dongFangCJService_getOrder.AnalyzTxtFileAndSaveB2cTemp();
			dongFangCJInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.DongFangCJ.getKey());
			dongFangCJService_getOrder.MoveTxtToDownload_BakFile();

			long endtime1 = System.currentTimeMillis();
			logger.info("执行了0从FTP获取东方CJ订单数据0定时器,本次耗时:{}秒", ((endtime1 - endtime) / 1000));

			homegouService_getOrder.downloadOrdersToHomeGouFTPServer();
			homegouService_getOrder.AnalyzTxtFileAndSaveB2cTemp();
			homegouInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.HomeGou.getKey());
			homegouService_getOrder.MoveTxtToDownload_BakFile();

			long endtime2 = System.currentTimeMillis();
			logger.info("执行了0从FTP获取家有购物订单数据0定时器,本次耗时:{}秒", ((endtime2 - endtime) / 1000));
		} catch (Exception e) {
			logger.error("0FTP获取家有购物0定时器调用发生未知异常", e);
		} finally {
			threadMap.put("yangguang", 0);
		}

	}

	/**
	 * 执行下载好享购订单数据的接口
	 * 
	 */
	public void getHaoXiangGou_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (threadMap.get("haoxianggou") == 1) {
			logger.warn("本地定时器没有执行完毕，跳出循环haoxianggou");
			return;
		}
		threadMap.put("haoxianggou", 1);

		long count = 0;
		try {
			// HaoXiangGou
			// hxg=haoXiangGouService.getHaoXiangGou(B2cEnum.HaoXiangGou.getKey());
			long starttime = System.currentTimeMillis();
			count += haoXiangGouService_PeiSong.GetOrdWayBillInfoForD2D();
			count += haoXiangGouService_TuiHuo.GetRtnWayBillInfoForD2D();
			hXGInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			// b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(hxg.getCustomerids(),DateTimeUtil.getNowTime(),count,"下载成功");

			long endtime = System.currentTimeMillis();
			logger.info("执行了[执行获取好享购订单数据]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));

			// efastInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		} catch (Exception e) {
			logger.error("0FTP获取家有购物0定时器调用发生未知异常", e);
		} finally {
			threadMap.put("haoxianggou", 0);
		}

	}

	public void getAmazon_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		long starttime = System.currentTimeMillis();
		amazonService.amazonGetOrderInvoke();
		long endtime = System.currentTimeMillis();
		expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.AMAZON, endtime);
		logger.info("执行了[执行获取亚马逊订单数据]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/*
	 * 系统之间的对接 订单下载
	 */
	public void getEpaiAPI_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		if (threadMap.get("epaiDownload") == 1) {
			logger.warn("本地定时器没有执行完毕，跳出循环epaiapi");
			return;
		}
		threadMap.put("epaiDownload", 1);

		try {
			epaiApiService_Download.downLoadOrders_controllers();
			epaiApiService_ExportCallBack.exportCallBack_controllers();
			epaiInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		} catch (Exception e) {
			logger.error("下游订单下载定时器发生未知异常", e);
		} finally {
			logger.info("执行了易派系统对接订单下载接口");
			threadMap.put("epaiDownload", 0);
		}

	}

	public void getZhongXingYunGou_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		long starttime = System.currentTimeMillis();
		if (threadMap.get("zhongxingerp") == 1) {
			logger.warn("本地定时器没有执行完毕，跳出循环zhongxingerp");
			return;
		}
		threadMap.put("zhongxingerp", 1);

		try {

			efastService_getOrderList.getOrderList();
			efastService_getOrderDetailList.getOrderDetailList();
			efastInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

		} catch (Exception e) {
			logger.error("中兴云购ERP定时器异常", e);
		}
		long endtime = System.currentTimeMillis();
		logger.info("执行了[获取中兴云购ERP订单]定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		threadMap.put("zhongxingerp", 0);
	}

	/**
	 * 执行如风达{迈思可站点信息同步}接口 调用频率是 每隔3天一次
	 */
	public void getMskBranchSyn_Task() {
		String sysValue = getSysOpenValue();
		if ("yes".equals(sysValue)) {
			logger.warn("已开启远程定时调用,本地定时任务不生效");
			return;
		}

		long starttime = System.currentTimeMillis();
		maisikeService_branchSyn.syn_maisikeBranchs();
		long endtime = System.currentTimeMillis();
		logger.info("执行了【迈思可站点信息同步】定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	/**
	 * 广州思迈速递
	 */
	public void getSmile_Task() {
		long starttime = System.currentTimeMillis();
		smileInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		long endtime = System.currentTimeMillis();
		logger.info("执行了【广州思迈】定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
	}

	public void gethappygogo() {
		happyGoService.gethappygoInfoTimmer();
		logger.info("happygo执行合并定时器完毕");
	}

	public void getLiantongTask() {
		liantongInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		logger.info("执行了联通定时器");
	}

	public void getChinamobile_Task() {

		chinamobileService.downloadOrdersToFTPServer();
		chinamobileService.AnalyzCSVFileAndSaveB2cTemp();
		chinamobileService.MoveTxtToDownload_BakFile();
		logger.info("执行了移动对接定时器");
	}

	/**
	 * 当当订单导入接口定时器
	 */
	public void getDangDang_Task() {
		try {
			if (threadMap.get("dangdang") == 1) {
				logger.info("当当订单导入定时器未执行完毕！....等待......");
				return;
			}
			threadMap.put("dangdang", 1);
			dangDangSynInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.DangDang_daoru.getKey());
			long endtime = System.currentTimeMillis();
			expressSysMonitorDAO.chooise(ExpressSysMonitorEnum.DANGDANG, endtime);
			logger.info("执行了当当订单导入定时器！");
		} catch (Exception e) {
			logger.error("执行当当订单导入定时器异常", e);
		} finally {
			threadMap.put("dangdang", 0);
		}

	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}

	/**
	 * 消息列表的定时器
	 */
	public void getXiaoxiInfo_Task() {
		if (threadMap.get("xiaoxi") == 1) {
			logger.info("消息列表未执行完毕！....等待......");
			return;
		}
		threadMap.put("xiaoxi", 1);
		appearWindowService.getAppearInfoShow();
		threadMap.put("xiaoxi", 0);
		logger.info("消息列表定时器执行完毕1");
	}

	/**
	 * 扣款结算订单自动到货定时器
	 */
	public void getKouKuanInfo_Task() {
		if (threadMap.get("koukuan") == 1) {
			logger.info("扣款结算自动到货未执行完毕！....等待......");
			return;
		}
		threadMap.put("koukuan", 1);
		accountDeductRecordService.updateJobKouKuan();
		threadMap.put("koukuan", 0);
		logger.info("扣款结算自动到货执行完毕！");
	}

	/**
	 * 永辉超市
	 */
	public void getYonghuics_Task() {
		try {
			if (threadMap.get("yonghuics") == 1) {
				logger.info("永辉超市订单导入定时器未执行完毕！....等待......");
				return;
			}
			threadMap.put("yonghuics", 1);
			yonghuiService.yonghuiInterfaceInvoke(B2cEnum.YongHuics.getKey());

			logger.info("执行了永辉超市订单导入定时器！");
		} catch (Exception e) {
			logger.error("执行永辉超市订单导入定时器异常", e);
		} finally {
			threadMap.put("yonghuics", 0);
		}

	}

	private Object hxgLock = new Object();

	/**
	 * 好享购DMS
	 */
	public void getHxgdms_Task() {
		try {
			synchronized (threadMap) {
				if (threadMap.get("hxgdms") == 1) {
					logger.info("好享购DMS订单导入定时器未执行完毕！....等待......");
					return;
				}
				threadMap.put("hxgdms", 1);
			}

			logger.info("I'm working.");
			Thread.sleep(10000L);
			hxgdmsInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			logger.info("执行了好享购DMS订单导入定时器！");
		} catch (Exception e) {
			logger.error("执行好享购DMS订单导入定时器异常", e);
		} finally {
			threadMap.put("hxgdms", 0);
		}
	}

	/**
	 * 网酒网
	 */
	public void getWangjiu_Task() {
		try {

			wangjiuInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			logger.info("执行了网酒网订单导入定时器！");
		} catch (Exception e) {
			logger.error("执行网酒网订单导入定时器异常", e);
		}

	}

	/**
	 * 乐宠
	 */
	public void getLeChong_Task() {
		try {

			lechongInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			logger.info("执行了乐宠订单导入定时器！");
		} catch (Exception e) {
			logger.error("执行乐宠订单导入定时器异常", e);
		}

	}

	/**
	 * 中粮
	 */
	public void getZhongliang_Task() {
		try {

			zhongliangService.waitOrder();
			zhongliangInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			zhongliangService.CancelOrders();

			logger.info("执行了中粮订单导入定时器！");
		} catch (Exception e) {
			logger.error("执行中粮订单导入定时器异常", e);
		}

	}

	/**
	 * 家有购物bj
	 */
	public void getHomegobj_Task() {
		try {

			homegobjInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			logger.info("执行了家有购物北京导入临时表定时器！");

		} catch (Exception e) {
			logger.error("执行家有购物北京订单导入临时表定时器异常", e);
		}

	}

	/**
	 * 家有购物bj
	 */
	public void getHomegobj1_Task() {
		try {

			homegobjService_getOrderDetailList.excutorGetOrders();

			logger.info("执行了家有购物北京订单导入定时器！");

		} catch (Exception e) {
			logger.error("执行家有购物北京订单导入定时器异常", e);
		}

	}

	public void getSfxhm_Task() {
		try {

			sfxhmService_getOrder.downloadOrdersforRemoteProc();
			sfxhmInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			logger.info("执行了顺丰小红帽订单导入定时器！");

		} catch (Exception e) {
			logger.error("执行顺丰小红帽订单导入定时器异常", e);
		}

	}

	public void getWenxuan_Task() {
		try {

			wenxuanService_getOrder.excutorGetOrdersByTimes();
			wenxuanInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			logger.info("执行了文轩网订单导入定时器！");

		} catch (Exception e) {
			logger.error("执行文轩网订单导入定时器异常", e);
		}

	}

}
