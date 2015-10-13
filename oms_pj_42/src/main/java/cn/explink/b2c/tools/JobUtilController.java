package cn.explink.b2c.tools;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.b2cmonitor.B2cSendMointorService;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.SystemInstall;
import cn.explink.util.JSONReslutUtil;

/**
 * 提供一个手动反馈给各个已对接的供货商订单状态，相当于定时器调用。 如果不想等待定时器自动反馈，可以使用此功能
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/jobHand")
public class JobUtilController {

	@Autowired
	B2cSendMointorService b2cSendMointorService;
	@Autowired
	B2cJsonService b2cJsonService;

	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	JobUtilService jobUtilService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 初始化 customer
	 * 
	 * @return
	 */
	@RequestMapping("/init_customerlist")
	public @ResponseBody String init_customer() {

		logger.info("dmp供货商设置表发生改变，重新加载成功");

		return "SUCCESS";
	}

	/**
	 * 统一供应回写类
	 * 
	 * @param id
	 * @param response
	 */
	private void responseWriteHandler(long id, HttpServletResponse response) {
		try {
			response.getWriter().write("0");
			response.getWriter().flush();
			response.flushBuffer();
			// response.getWriter().close();
		} catch (IOException e1) {
			logger.error("执行回写失败id=" + id, e1);
		}
	}

	// 系统设置 设置开启对外操作
	private String getSysOpenValue(String key) {

		SystemInstall systemInstall = getDmpDAO.getSystemInstallByName(key);
		String sysValue = systemInstall.getValue();
		return sysValue;
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

		logger.info("执行了推送" + b2cName + "的定时器,本次耗时:{}秒", (excutetime / 1000));

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
			jobDTO.setMsg("未开启远程调用oms-" + b2cenum + "对接定时器");
			return false;
		}

		return true;

	}

	/**
	 * 统一返回未知的异常
	 * 
	 * @param id
	 * @param jobDTO
	 * @param starttime
	 * @param e
	 */
	private JobDTO buildUnknowExceptionJSON(long id, JobDTO jobDTO, long starttime, Exception e) {
		long endtime = System.currentTimeMillis();
		jobDTO.setId(id);
		jobDTO.setCode(JobCodeEnum.Error.getCode());
		jobDTO.setCount(0);
		jobDTO.setTime(endtime - starttime);
		jobDTO.setMsg("未知异常" + e.getMessage());
		logger.error("远程执行定时器异常,id=" + id, e);
		return jobDTO;
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
			String str = JSONReslutUtil.getResultMessage(callbackUrl, "json=" + JSONObject.fromObject(jobDTO).toString(), "POST").toString();
			logger.info("远程执行定时器回传结果：id={},结果={}", id, str);
		} catch (IOException e) {
			logger.error("异步回传url={" + callbackUrl + "}异常", e);
		}
	}

	/**
	 * 当当
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/dangdang/{id}")
	public void getDangDangFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "dangdang"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToDangDang_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "当当"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 天猫
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/tmall/{id}")
	public void getTmallFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "tmall"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToTmall_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "天猫"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 唯品会
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/vipshop/{id}")
	public void getVipshopFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "vipshop"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToVipShop_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "唯品会"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 聚美
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/jumei/{id}")
	public void getJumeiFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "jumei"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToJumeiYoupin_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "聚美"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 如风达
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/rufengda/{id}")
	public void getRufengdaFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "rufengda"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToRufengda_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "如风达"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	@RequestMapping("/dmp_test")
	public void DMP_test(HttpServletResponse response, HttpServletRequest request) {
		b2cSendMointorService.parseB2cMonitorData_timmer();

	}

	/**
	 * 广州思迈速递
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/smile/{id}")
	public void getSimaiFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "smile"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToSiMai_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "思迈速递"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 一号店
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/yihaodian/{id}")
	public void getYihaodianFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "yihaodian"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToYihaodian_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "一号店"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 央广购物
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/yangguang/{id}")
	public void getYangGuangFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "yangguang"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToYangGuang_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "央广购物"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 也买酒
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/yemaijiu/{id}")
	public void getYemaijiuFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "yemaijiu"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToYemaijiu_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "也买酒"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 广州ABC
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/gzabc/{id}")
	public void getGzabcFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "gzabc"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToGuangZhou_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "广州ABC"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 杭州ABC
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/hzabc/{id}")
	public void getHzabcFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "hzabc"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToHangZhou_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "杭州ABC"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 东方CJ
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/dongfangcj/{id}")
	public void getDongFangCJFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "dongfangcj"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToDongfangCJ_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "东方CJ"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 好享购
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/haoxgou/{id}")
	public void getHaoXGFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "haoxianggou"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToHaoXgou_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "好享购"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 易迅
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/yixun/{id}")
	public void getYiXunFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "yixun"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToYiXun_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "易迅网"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 家有购物
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/homegou/{id}")
	public void getHomegouFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "homegou"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.getHomeGou_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "家有购物"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 德邦物流
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/dpfoss/{id}")
	public void getDpfossFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "dpfoss"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.dpfoss_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "德邦物流"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 汇通天下
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/httx/{id}")
	public void getHttxFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "httx"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.getHttx_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "汇通天下"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 易派对接 回传
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/epaiFeedback/{id}")
	public void getEpaiFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "httx"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.epaiFeedback_down_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "环形对接状态回传-下游"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 易派对接 环形上游OMS请求dmp
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/hxtoDmp/{id}")
	public void getHxRequestDMPFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "hxtoDmp"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.epaiFeedback_up_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "环形oms请求DMP-上游"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 电信商城
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/telecom/{id}")
	public void getTeleComFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "telecom"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.feedBackToTeleCom_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "电信商城"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 国美
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/gome/{id}")
	public void getGomeFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "gome"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.getGome_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "国美在线"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 亚马逊
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/amazon/{id}")
	public void getAmazonFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "amazon"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.amazon_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "亚马逊"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 亚马逊 COD
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/amazon_cod/{id}")
	public void getAmazonCODFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "amazon_cod"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.amazon_Cod_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "亚马逊-cod"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 迈思可
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/maisike_send/{id}")
	public void getMaisikeSendFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "maisike_send"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.getOuttoBranchMsk_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "迈思可-下发数据"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 酒仙网
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/jiuxiangwang/{id}")
	public void getjiuxiangwangFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "jiuxianwang"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.Jiuxian_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "酒仙网"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 本来
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/benlaishenghuo/{id}")
	public void getBenlaishenghuoFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "benlaishenghuo"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.Benlai_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "本来生活"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * maikolin
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/maikolin/{id}")
	public void getMaikolinFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "maikolin"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.maikolin_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "麦考林"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

	/**
	 * 快乐购
	 * 
	 * @param id
	 * @param url
	 * @param request
	 * @param response
	 */
	@RequestMapping("/happygo/{id}")
	public void getHappyGoFeedback_Task(@PathVariable("id") long id, @RequestParam(value = "url", required = false, defaultValue = "no") final String url, final HttpServletRequest request,
			HttpServletResponse response) {

		JobDTO jobDTO = new JobDTO();
		long starttime = System.currentTimeMillis();
		try {

			responseWriteHandler(id, response); // 第一次 回写

			boolean isPassflag = validatorIsExcuteHandler(id, jobDTO, starttime, "happygo"); // 验证是否满足条件

			if (!isPassflag) {
				return;
			}

			long count = jobUtilService.getHappyGo_Task();

			long endtime = System.currentTimeMillis();
			long excutetime = endtime - starttime;

			jobDTO = buildReponseJSON(id, jobDTO, count, excutetime, "快乐购"); // 构建天猫的返回结果

		} catch (Exception e) {
			buildUnknowExceptionJSON(id, jobDTO, starttime, e);
		} finally {

			asynchronousCallBack(id, jobDTO, url + "/" + id);
		}

	}

}