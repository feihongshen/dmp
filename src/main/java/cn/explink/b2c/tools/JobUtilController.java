package cn.explink.b2c.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.b2c.amazon.AmazonService;
import cn.explink.b2c.dangdang_dataimport.DangDangSynInsertCwbDetailTimmer;
import cn.explink.b2c.dongfangcj.DongFangCJInsertCwbDetailTimmer;
import cn.explink.b2c.dongfangcj.DongFangCJService_getOrder;
import cn.explink.b2c.dpfoss.DpfossInsertCwbDetailTimmer;
import cn.explink.b2c.efast.EfastInsertCwbDetailTimmer;
import cn.explink.b2c.efast.EfastService_getOrderDetailList;
import cn.explink.b2c.efast.EfastService_getOrderList;
import cn.explink.b2c.explink.core_down.AcquisitionOrderService;
import cn.explink.b2c.explink.core_down.EpaiApiService_Download;
import cn.explink.b2c.explink.core_down.EpaiApiService_ExportCallBack;
import cn.explink.b2c.explink.core_down.EpaiInsertCwbDetailTimmer;
import cn.explink.b2c.gome.GomeService;
import cn.explink.b2c.gzabc.GuangZhouABCInsertCwbDetailTimmer;
import cn.explink.b2c.haoxgou.HXGInsertCwbDetailTimmer;
import cn.explink.b2c.haoxgou.HaoXiangGou;
import cn.explink.b2c.haoxgou.HaoXiangGouService;
import cn.explink.b2c.haoxgou.HaoXiangGouService_PeiSong;
import cn.explink.b2c.haoxgou.HaoXiangGouService_TuiHuo;
import cn.explink.b2c.happyGo.HappyGoService;
import cn.explink.b2c.homegou.HomegouInsertCwbDetailTimmer;
import cn.explink.b2c.homegou.HomegouService_getOrder;
import cn.explink.b2c.huitongtx.HuitongtxInsertCwbDetailTimmer;
import cn.explink.b2c.hzabc.HangZhouABCInsertCwbDetailTimmer;
import cn.explink.b2c.maikaolin.MaikolinService;
import cn.explink.b2c.maisike.MaisikeService_branchSyn;
import cn.explink.b2c.rufengda.RufengdaService;
import cn.explink.b2c.saohuobang.SaohuobangInsertCwbDetailTimmer;
import cn.explink.b2c.telecomsc.TelecomInsertCwbDetailTimmer;
import cn.explink.b2c.tmall.TmallInsertCwbDetailTimmer;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.b2c.vipshop.VipShopService;
import cn.explink.b2c.vipshop.VipshopInsertCwbDetailTimmer;
import cn.explink.b2c.yangguang.YangGuangInsertCwbDetailTimmer;
import cn.explink.b2c.yangguang.YangGuangService_download;
import cn.explink.b2c.yihaodian.YihaodianService;
import cn.explink.b2c.yixun.YiXunInsertCwbDetailTimmer;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;
import cn.explink.enumutil.JobCodeEnum;
import cn.explink.service.FlowExpService;
import cn.explink.service.LogToDayByTuihuoService;
import cn.explink.service.LogToDayByWarehouseService;
import cn.explink.service.LogToDayService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.RedisMap;
import cn.explink.util.impl.RedisMapImpl;

@Controller
@RequestMapping("/jobHand")
public class JobUtilController {
	private Logger logger = LoggerFactory.getLogger(JobUtilController.class);
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
	LogToDayService logToDayService;
	@Autowired
	LogToDayByTuihuoService logToDayByTuihuoService;
	@Autowired
	LogToDayByWarehouseService logToDayByWarehouseService;
	@Autowired
	AcquisitionOrderService acquisitionOrderService;
	@Autowired
	FlowExpService flowExpService;
	@Autowired
	VipshopInsertCwbDetailTimmer vipshopInsertCwbDetailTimmer;
	
	// public static Map<String, Integer> threadMap;
	public static RedisMap<String, Integer> threadMap;	
	static { // 静态初始化 以下变量,用于判断线程是否在执行
		threadMap = new RedisMapImpl<String, Integer>("JobUtilController");
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
		threadMap.put("epaiapi", 0); // 系统对接标识
		threadMap.put("efast", 0); // 系统对接标识
		threadMap.put("saohuobang", 0); // 系统对接标识
		threadMap.put("happygo", 0); // 系统对接标识
		threadMap.put("telecom", 0);
		threadMap.put("dongfangcj", 0);

		threadMap.put("homegou", 0);
		threadMap.put("amazon", 0);
		threadMap.put("epaiDownload", 0);

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

		threadMap.put("efast", 0); // 系统对接标识
		threadMap.put("saohuobang", 0); // 系统对接标识
		threadMap.put("happygo", 0); // 系统对接标识
		threadMap.put("telecom", 0);
		threadMap.put("dongfangcj", 0);
		threadMap.put("homegou", 0);
		threadMap.put("amazon", 0);
		threadMap.put("epaiDownload", 0);
	}

	/**
	 * 异步回传URL公用方法
	 * 
	 * @param id
	 * @param jobDTO
	 * @param callbackUrl
	 */
	private void asynchronousCallBack(long id, JobDTO jobDTO, String callbackUrl) {
		try {
			String str = JSONReslutUtil.getResultMessageChangeLog(callbackUrl, "json=" + JSONObject.fromObject(jobDTO).toString(), "POST").toString();
			logger.info("远程执行定时器回传结果：id={},结果={}", id, str);
		} catch (IOException e) {
			logger.error("异步回传url={" + callbackUrl + "}异常", e);
		}
	}

	/**
	 * 统一供应回写类
	 * 
	 * @param id
	 * @param response
	 */
	private static void responseWriteHandler(long id, HttpServletResponse response) {
		try {
			response.getWriter().write("0");
			response.getWriter().flush();
			response.flushBuffer();
			// response.getWriter().close(); //这里限制线程同步
		} catch (IOException e1) {

		}
	}

	/**
	 * 统一返回JSON格式
	 * 
	 * @param id
	 * @param jobDTO
	 * @param count
	 * @param excutetime
	 */
	private JobDTO buildReponseJSON(long id, JobDTO jobDTO, long count, long excutetime, String b2cName) {
		if (count == -1) {
			jobDTO.setId(id);
			jobDTO.setCode(JobCodeEnum.Error.getCode());
			jobDTO.setCount(0);
			jobDTO.setTime(excutetime);
			jobDTO.setMsg("未开启" + b2cName + "对接");
			return jobDTO;
		}

		jobDTO.setId(id);
		jobDTO.setCode(JobCodeEnum.Success.getCode());
		jobDTO.setCount(count);
		jobDTO.setTime(excutetime);
		jobDTO.setMsg(JobCodeEnum.Success.getMsg());
		return jobDTO;
	}

	/**
	 * 统一返回未知的异常
	 * 
	 * @param id
	 * @param jobDTO
	 * @param starttime
	 * @param e
	 */
	private void buildUnknowExceptionJSON(long id, JobDTO jobDTO, long starttime, Exception e) {
		long endtime = System.currentTimeMillis();
		jobDTO.setId(id);
		jobDTO.setCode(JobCodeEnum.Error.getCode());
		jobDTO.setCount(0);
		jobDTO.setTime(endtime - starttime);
		jobDTO.setMsg("未知异常" + e.getMessage());
		logger.error("远程执行定时器异常,id=" + id, e);
	}

	/**
	 * 验证是否可以执行
	 * 
	 * @param id
	 * @param jobDTO
	 * @param starttime
	 */
	private boolean validatorIsExcuteHandler(long id, JobDTO jobDTO, long starttime, String b2cenum) {
		String sysValue = getSysOpenValue("isOpenJobHand");
		if (!"yes".equals(sysValue)) {
			jobDTO.setId(id);
			jobDTO.setCode(JobCodeEnum.Error.getCode());
			jobDTO.setCount(0);
			jobDTO.setTime(0);
			jobDTO.setMsg("未开启远程调用dmp对接定时器");
			return false;
		}

		if (threadMap.get(b2cenum) == 1) {
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("上一个请求没有执行完毕，跳出循环" + b2cenum + ",执行时间：" + excutetime + "毫秒");
			jobDTO.setId(id);
			jobDTO.setCode(JobCodeEnum.Error.getCode());
			jobDTO.setCount(0);
			jobDTO.setTime(excutetime);
			jobDTO.setMsg("上一个请求没有执行完毕，本次请求无效");
			return false;
		}
		return true;

	}

	// 系统设置 设置开启对外操作
	private String getSysOpenValue(String key) {

		SystemInstall systemInstall = systemInstallDAO.getSystemInstall(key);
		String sysValue = systemInstall.getValue();
		return sysValue;
	}

	// for(int i=1;i<=20;i++){
	// Thread.sleep(1000);
	// System.out.println("执行次数="+i);
	// if(threadMap.get("tmall")==0){
	// logger.info("跳出循环，当前i="+i);
	// return;
	// }
	// }
	/**
	 * 执行重临时表获取tmall订单的定时器
	 */
	@RequestMapping("/tmall/{id}")
	public void getTmallCwbDetailByTemp_Task1(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") long id,
			@RequestParam(value = "url", required = false, defaultValue = "no") String url) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "tmall"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("tmall", 1);

			long count = tmallService.selectTempAndInsertToCwbDetailCount();
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取天猫订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "天猫"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("tmall", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);

		}
	}

	/**
	 * 唯品会
	 */
	@RequestMapping("/vipshop/{id}")
	public void getVipShopCwbDetailByTemp_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "vipshop"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}
			threadMap.put("vipshop", 1);

			vipShopService.excuteVipshopDownLoadTask();
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取唯品会订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, 0, excutetime, "唯品会");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("vipshop", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 麦考林
	 */
	@RequestMapping("/maikolin/{id}")
	public void getMaikolin_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "maikolin"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("maikolin", 1);

			long count = maikolinService.excute_getMaikolinTask();
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取麦考林订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "麦考林");

		} catch (Exception e) {

			buildUnknowExceptionJSON(id, jobDTO, starttime, e);

		} finally {
			threadMap.put("maikolin", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 扫货帮
	 */
	@RequestMapping("/saohuobang/{id}")
	public void getSaohuobang_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "saohuobang"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("saohuobang", 1);

			long count = saohuobangInsertCwbDetailTimmer.excute_saohuobangTask();
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取扫货帮订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "扫货帮");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("saohuobang", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 快乐购
	 */
	@RequestMapping("/happygo/{id}")
	public void getHappyGo_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "happygo"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("happygo", 1);

			long count = happyGoService.excute_getHappyGoTask();
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取快乐购订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "快乐购");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("happygo", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 一号店
	 */
	@RequestMapping("/yihaodian/{id}")
	public void getYihaodian_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "yihaodian"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("yihaodian", 1);

			long count = yihaodianService.excute_getYihaodianTask();
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取一号店订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "一号店");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("yihaodian", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 当当
	 */
	@RequestMapping("/dangdang/{id}")
	public void getDangDang_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "dangdang"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("dangdang", 1);

			long count = dangDangSynInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.DangDang_daoru.getKey());

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取当当订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "当当");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("dangdang", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 央广购物
	 */
	@RequestMapping("/yangguang/{id}")
	public void getYangguang_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "yangguang"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("yangguang", 1);

			yangGuangService_download.getOrderDetailToYangGuangFTPServer(); // FTP下载订单
			long count = yangGuangService_download.AnalyzTxtFileSaveB2cTemp(); // 解析txt插入临时表
			yangGuangInsertCwbDetailTimmer.selectTempAndInsertToCwbDetailByMultipDiff(B2cEnum.YangGuang.getKey());
			yangGuangService_download.MoveTxtFileToBakFile(); // 移动文件 到bak

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取央广订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "央广订单");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("yangguang", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 广州ABC
	 */
	@RequestMapping("/gzabc/{id}")
	public void getGzabc_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "gzabc"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("gzabc", 1);

			long count = guangZhouABCInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.GuangZhouABC.getKey());

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取广州ABC订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "广州ABC");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("gzabc", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 杭州ABC
	 */
	@RequestMapping("/hzabc/{id}")
	public void getHzabc_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "hzabc"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("hzabc", 1);

			long count = hangZhouABCInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.HangZhouABC.getKey());

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取杭州ABC订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "杭州ABC");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("hzabc", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 易迅
	 */
	@RequestMapping("/yixun/{id}")
	public void getYixun_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "yixun"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("yixun", 1);

			long count = yiXunInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取易迅订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "易迅");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("yixun", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 电信商城
	 */
	@RequestMapping("/telecom/{id}")
	public void getTelecom_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "telecom"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("telecom", 1);

			long count = telecomInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.Telecomshop.getKey());

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取电信商城订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "电信商城");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("telecom", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 如风达
	 */
	@RequestMapping("/rufengda/{id}")
	public void getRufengda_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "fanke"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("fanke", 1);

			long count = rufengdaService.RufengdaInterfaceInvoke();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取如风达订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "如风达");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("fanke", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 东方CJ
	 */
	@RequestMapping("/dongfangcj/{id}")
	public void getDongfangCJ_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "dongfangcj"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("dongfangcj", 1);
			long count = 0;

			dongFangCJService_getOrder.downloadOrdersToDongFangCJFTPServer();
			count = dongFangCJService_getOrder.AnalyzTxtFileAndSaveB2cTemp();
			dongFangCJInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.DongFangCJ.getKey());
			dongFangCJService_getOrder.MoveTxtToDownload_BakFile();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取东方购物订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "东方购物");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("dongfangcj", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 家有购物
	 */
	@RequestMapping("/homegou/{id}")
	public void getHomegou_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "homegou"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("homegou", 1);
			long count = 0;

			homegouService_getOrder.downloadOrdersToHomeGouFTPServer();
			count = homegouService_getOrder.AnalyzTxtFileAndSaveB2cTemp();
			homegouInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.HomeGou.getKey());
			homegouService_getOrder.MoveTxtToDownload_BakFile();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取家有购物订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "家有购物");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("homegou", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 好享购
	 */
	@RequestMapping("/haoxianggou/{id}")
	public void getHaoxianggou_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "haoxianggou"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("haoxianggou", 1);

			long count = 0;

			HaoXiangGou hxg = haoXiangGouService.getHaoXiangGou(B2cEnum.HaoXiangGou.getKey());

			count += haoXiangGouService_PeiSong.GetOrdWayBillInfoForD2D();
			count += haoXiangGouService_TuiHuo.GetRtnWayBillInfoForD2D();
			hXGInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			if (count > -1) {
				b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(hxg.getCustomerids(), DateTimeUtil.getNowTime(), count, "下载成功");
			}

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取好享购订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "好享购");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("haoxianggou", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 亚马逊
	 */
	@RequestMapping("/amazon/{id}")
	public void getAmazon_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "amazon"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("amazon", 1);

			long count = amazonService.amazonGetOrderInvoke();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取亚马逊订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "亚马逊");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("amazon", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 环形对接订单下载
	 */
	@RequestMapping("/epaiDownload/{id}")
	public void getEpaiDownload_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "epaiDownload"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("epaiDownload", 1);

			long count = this.acquisitionOrderService.passiveReceptionOrActiveAcquisition();
			epaiApiService_ExportCallBack.exportCallBack_controllers();
			epaiInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取环形对接下载订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "环形对接下载订单");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("epaiDownload", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 中兴云购
	 */
	@RequestMapping("/efast/{id}")
	public void getEfast_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "efast"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("efast", 1);

			efastService_getOrderList.getOrderList();
			efastService_getOrderDetailList.getOrderDetailList();
			efastInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取中兴云购下载订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, 0, excutetime, "中兴云购");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("efast", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 迈思可站点信息同步
	 */
	@RequestMapping("/MskBranchSyn/{id}")
	public void getMskBranchSyn_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			// 系统设置 设置开启对外操作
			String sysValue = getSysOpenValue("isOpenJobHand");
			if (!"yes".equals(sysValue)) {
				jobDTO.setId(id);
				jobDTO.setCode(JobCodeEnum.Error.getCode());
				jobDTO.setCount(0);
				jobDTO.setTime(0);
				jobDTO.setMsg("未开启远程调用dmp对接定时器");
				return;
			}

			long count = maisikeService_branchSyn.syn_maisikeBranchs();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了迈思可站点信息同步定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "迈思可站点信息同步");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("efast", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 如风达配送员信息同步
	 */
	@RequestMapping("/RfdUserSyn/{id}")
	public void getRfdUserSyn_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			// 系统设置 设置开启对外操作
			String sysValue = getSysOpenValue("isOpenJobHand");
			if (!"yes".equals(sysValue)) {
				jobDTO.setId(id);
				jobDTO.setCode(JobCodeEnum.Error.getCode());
				jobDTO.setCount(0);
				jobDTO.setTime(0);
				jobDTO.setMsg("未开启远程调用dmp对接定时器");
				return;
			}

			long count = rufengdaService.RufengdaInterfaceSynUserInfoInvoke();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了如风达信息同步定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "如风达信息同步");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("efast", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 国美
	 */
	@RequestMapping("/gome/{id}")
	public void getGome_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "gome"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			threadMap.put("gome", 1);

			long count = gomeService.gomeInterfaceInvoke();
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-获取国美下载订单的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "国美下载订单");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			threadMap.put("gome", 0);
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 站点生成日志-远程调用
	 */
	@RequestMapping("/branchDayLog/{id}")
	public void getbranchDayLog_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			String sysValue = getSysOpenValue("isOpenJobHand");
			if (!"yes".equals(sysValue)) {
				jobDTO.setId(id);
				jobDTO.setCode(JobCodeEnum.Error.getCode());
				jobDTO.setCount(0);
				jobDTO.setTime(0);
				jobDTO.setMsg("未开启远程调用dmp对接定时器");
				return;
			}

			long count = logToDayService.dailyDayLogGenerate_RemoteInvoke();
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】-站点日志的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "站点日志");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	//
	/**
	 * 站点退货日志-远程调用
	 */
	@RequestMapping("/branchTuihuoDayLog/{id}")
	public void branchTuihuoDayLog_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			String sysValue = getSysOpenValue("isOpenJobHand");
			if (!"yes".equals(sysValue)) {
				jobDTO.setId(id);
				jobDTO.setCode(JobCodeEnum.Error.getCode());
				jobDTO.setCount(0);
				jobDTO.setTime(0);
				jobDTO.setMsg("未开启远程调用dmp对接定时器");
				return;
			}

			long count = logToDayByTuihuoService.dailyDayLogByTuihuoGenerate_RemoteInvoke();
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-站点退货日志的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "站点退货日志");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 库房日志-远程调用
	 */
	@RequestMapping("/warehouseDayLog/{id}")
	public void warehouseDayLog_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {
		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 回写

			String sysValue = getSysOpenValue("isOpenJobHand");
			if (!"yes".equals(sysValue)) {
				jobDTO.setId(id);
				jobDTO.setCode(JobCodeEnum.Error.getCode());
				jobDTO.setCount(0);
				jobDTO.setTime(0);
				jobDTO.setMsg("未开启远程调用dmp对接定时器");
				return;
			}

			long count = logToDayByWarehouseService.dailyDayLogByWareHouseGenerate_remoteInvoke();
			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;
			logger.info("执行了【远程调用】临时表-站点退货日志的定时器,本次耗时:{}秒,id={}", (excutetime / 1000), id);

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "库房日志");

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {
			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}
	@RequestMapping("/vipOXO")
	public void getVipShopOXOTask(){
		System.out.println("-----getVipShopOXOTask启动执行");
//		String sysValue = this.getSysOpenValue();
//		if ("yes".equals(sysValue)) {
//			this.logger.warn("已开启远程定时调用,本地定时任务不生效");
//			return;
//		}

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

	@RequestMapping("/sendflowexp")
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
	
	//add by zhouhuan 执行获取vipshop临时表插入主表(不区分客户) 2016-08-05
	@RequestMapping("/getCwbTempInsert")
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
