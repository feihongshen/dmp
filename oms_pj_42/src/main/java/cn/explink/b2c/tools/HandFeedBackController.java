package cn.explink.b2c.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.Wholeline.WholeLine;
import cn.explink.b2c.Wholeline.WholeLineService;
import cn.explink.b2c.Wholeline.WholeLineService_search;
import cn.explink.b2c.chinamobile.ChinamobileService;
import cn.explink.b2c.dangdang.DangDangService;
import cn.explink.b2c.dongfangcj.DongFangCJService_Cod;
import cn.explink.b2c.dongfangcj.DongFangCJService_Delivery;
import cn.explink.b2c.dongfangcj.DongFangCJService_goback;
import cn.explink.b2c.dpfoss.DpfossService;
import cn.explink.b2c.explink.code_down.EpaiApiService;
import cn.explink.b2c.explink.core.CoreService;
import cn.explink.b2c.explink.core.threadpool.CoreExcutorService;
import cn.explink.b2c.gzabc.GuangZhouABCService;
import cn.explink.b2c.gztl.GztlService;
import cn.explink.b2c.haoxgou.HaoXiangGouService;
import cn.explink.b2c.homegobj.HomegobjService;
import cn.explink.b2c.homegou.HomegouService_Delivery;
import cn.explink.b2c.homegou.HomegouService_Message;
import cn.explink.b2c.huitongtx.HuitongtxService;
import cn.explink.b2c.hxgdms.HxgdmsService;
import cn.explink.b2c.hzabc.HangZhouABCService;
import cn.explink.b2c.jumeiyoupin.JumeiService;
import cn.explink.b2c.jumeiyoupin.JumeiYoupinService;
import cn.explink.b2c.lechong.LechongService;
import cn.explink.b2c.lefeng.LefengService;
import cn.explink.b2c.letv.LetvService;
import cn.explink.b2c.liantong.LiantongService;
import cn.explink.b2c.maikolin.MaikolinService;
import cn.explink.b2c.mmb.MmbService;
import cn.explink.b2c.rufengda.RufengdaService_CommitDeliverInfo;
import cn.explink.b2c.sfxhm.SfxhmService;
import cn.explink.b2c.smile.SmileService;
import cn.explink.b2c.telecomsc.TelecomshopService;
import cn.explink.b2c.tmall.TmallService;
import cn.explink.b2c.tools.b2cmonitor.B2cSendMointorService;
import cn.explink.b2c.vipshop.VipShopCwbFeedBackService;
import cn.explink.b2c.wanxiang.WanxiangService;
import cn.explink.b2c.yangguang.YangGuangService_upload;
import cn.explink.b2c.yemaijiu.YeMaiJiuService;
import cn.explink.b2c.yihaodian.YihaodianService;
import cn.explink.b2c.yixun.YiXunService;
import cn.explink.b2c.yonghuics.YonghuiService;
import cn.explink.dao.GetDmpDAO;

/**
 * 提供一个手动反馈给各个已对接的供货商订单状态，相当于定时器调用。 如果不想等待定时器自动反馈，可以使用此功能
 *
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/handfeedback")
public class HandFeedBackController {
	@Autowired
	DangDangService dangdangService;
	@Autowired
	TmallService tmallService;
	@Autowired
	VipShopCwbFeedBackService vipshopService;
	@Autowired
	JumeiYoupinService jumeiYoupinService;
	@Autowired
	JumeiService jumeiService;
	@Autowired
	B2cSendMointorService b2cSendMointorService;
	@Autowired
	RufengdaService_CommitDeliverInfo rufengdaService_CommitDeliverInfo;
	@Autowired
	SmileService smileService;
	@Autowired
	YihaodianService yihaodianService;
	@Autowired
	YangGuangService_upload yangGuangService_upload;

	@Autowired
	YeMaiJiuService yeMaiJiuService;
	@Autowired
	GuangZhouABCService guangZhouABCService;
	@Autowired
	HangZhouABCService hangZhouABCService;
	@Autowired
	DongFangCJService_Cod dongFangCJService_Cod;
	@Autowired
	DongFangCJService_Delivery dongFangCJService_Delivery;
	@Autowired
	DongFangCJService_goback dongFangCJService_goback;
	@Autowired
	HaoXiangGouService haoXiangGouService;
	@Autowired
	YiXunService yiXunService;
	@Autowired
	HomegouService_Delivery homegouService_Delivery;
	@Autowired
	HomegouService_Message homegouService_Message;
	@Autowired
	DpfossService dpfossService;
	@Autowired
	MaikolinService maikolinService;
	@Autowired
	HuitongtxService huitongtxService;
	@Autowired
	EpaiApiService epaiApiService;

	@Autowired
	B2cJsonService b2cJsonService;
	@Autowired
	CoreService coreService;
	@Autowired
	TelecomshopService telecomshopService;
	@Autowired
	LiantongService liantongService;
	@Autowired
	MmbService mmbService;
	@Autowired
	LetvService letvService;
	@Autowired
	ChinamobileService chinamobileService;
	@Autowired
	WanxiangService wanxiangService;
	@Autowired
	YonghuiService yonghuiService;
	@Autowired
	HxgdmsService hxgdmsService;

	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	WholeLineService wholeLineService;
	@Autowired
	WholeLineService_search wholeLineService_search;

	@Autowired
	LechongService lechongService;

	@Autowired
	HomegobjService homegobjService;
	@Autowired
	SfxhmService sfxhmService;
	@Autowired
	CacheBaseListener cacheBaseListener;
	@Autowired
	LefengService lefengService;
	@Autowired
	GztlService gztlService;
	@Autowired
	CoreExcutorService coreExcutorService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 初始化 customer
	 *
	 * @return
	 */
	@RequestMapping("/init_customerlist")
	public @ResponseBody String init_customer() {

		// b2cJsonService.initCustomerList();
		this.cacheBaseListener.initAll();
		this.logger.info("dmp供货商设置表发生改变，重新加载成功");

		return "SUCCESS";

	}

	@RequestMapping("/dangdang_test")
	public @ResponseBody String dangdangtest(HttpServletResponse response, HttpServletRequest request) {

		this.dangdangService.feedback_status();
		return "";

	}

	@RequestMapping("/tmall_test")
	public @ResponseBody String tmalltest(HttpServletResponse response, HttpServletRequest request) {
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("tmall")) {
				this.tmallService.feedback_status(enums.getKey());
			}
		}
		return "success";

	}

	@RequestMapping("/vipshop_test")
	public @ResponseBody String vipshoptest(HttpServletResponse response, HttpServletRequest request) {
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("vipshop")) {
				this.vipshopService.feedback_status(enums.getKey());
			}
		}
		return "";

	}

	@RequestMapping("/jumei_test")
	public @ResponseBody String jumietest(HttpServletResponse response, HttpServletRequest request) {
		this.jumeiYoupinService.feedback_status();
		return "success";

	}

	@RequestMapping("/jumei_test1")
	public @ResponseBody String jumietest1(HttpServletResponse response, HttpServletRequest request) {
		this.jumeiService.feedback_status();
		return "success";

	}

	@RequestMapping("/dmp_test")
	public void DMP_test(HttpServletResponse response, HttpServletRequest request) {
		this.b2cSendMointorService.parseB2cMonitorData_timmer();

	}

	@RequestMapping("/rufengda_test")
	public void CommitDeliverInfo_interface(HttpServletResponse response, HttpServletRequest request) {
		this.rufengdaService_CommitDeliverInfo.CommitDeliverInfo_interface();

	}

	@RequestMapping("/smile_test")
	public void feedbackto_smile(HttpServletResponse response, HttpServletRequest request) {
		this.smileService.feedback_status();

	}

	@RequestMapping("/yihaodian_test")
	public void feedbackto_yihaodian(HttpServletResponse response, HttpServletRequest request) {

		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("yihaodian")) {
				this.yihaodianService.YiHaoDianInterfaceInvoke(enums.getKey());
				this.yihaodianService.DeliveryResultByYiHaoDianAgain(enums.getKey());
			}
		}
	}

	@RequestMapping("/yangguang_test")
	public void feedbackto_yangguang(HttpServletResponse response, HttpServletRequest request) {
		this.yangGuangService_upload.createTxtFile_DeliveryFinishMethod();

	}

	@RequestMapping("/yemaijiu_test")
	public void feedbackto_yemaijiu(HttpServletResponse response, HttpServletRequest request) {
		this.yeMaiJiuService.feedback_status();

	}

	@RequestMapping("/gzabc_test")
	public void feedbackto_gzabc(HttpServletResponse response, HttpServletRequest request) {
		this.guangZhouABCService.feedback_status(B2cEnum.GuangZhouABC.getKey());
	}

	@RequestMapping("/hzabc_test")
	public void feedbackto_hzabc(HttpServletResponse response, HttpServletRequest request) {
		this.hangZhouABCService.feedback_status(B2cEnum.HangZhouABC.getKey());
	}

	@RequestMapping("/dongfangcj_test")
	public void dongfangcj_test(HttpServletResponse response, HttpServletRequest request) {
		this.dongFangCJService_Delivery.createDongFangCJTxtFile_DeliveryFinish();
		this.dongFangCJService_Cod.createDongFangCJTxtFile_Cod();
		this.dongFangCJService_goback.createDongFangCJTxtFile_goback(); // /回收单推送标识
	}

	@RequestMapping("/hxg_test")
	public void feedbackto_hxg(HttpServletResponse response, HttpServletRequest request) {
		this.haoXiangGouService.feedback_status();
	}

	@RequestMapping("/tmall_test_insert")
	public @ResponseBody String tmall_test_insert(HttpServletResponse response, HttpServletRequest request) {

		this.tmallService.test_insert();
		return "success";
	}

	@RequestMapping("/yixun_test")
	public void feedbackto_yixun(HttpServletResponse response, HttpServletRequest request) {
		this.yiXunService.feedback_status();
	}

	// 配送结果
	@RequestMapping("/homegou_test")
	public void feedbackto_homegou1(HttpServletResponse response, HttpServletRequest request) {
		this.homegouService_Delivery.feedback_state();
	}

	// 发送短信通知
	@RequestMapping("/homegou_test1")
	public void feedbackto_homegou2(HttpServletResponse response, HttpServletRequest request) {
		this.homegouService_Message.feedback_state();
	}

	// 配送结果
	@RequestMapping("/dpfoss_test")
	public void dpfoss_test(HttpServletResponse response, HttpServletRequest request) {
		this.dpfossService.feedback_status();
	}

	// 配送结果
	@RequestMapping("/httx_test")
	public void httx_test(HttpServletResponse response, HttpServletRequest request) {
		this.huitongtxService.feedback_status(B2cEnum.Huitongtx.getKey());
	}

	// 配送结果
	@RequestMapping("/epai_test")
	public void epai_test(HttpServletResponse response, HttpServletRequest request) {
		this.epaiApiService.feedback_status();
	}

	/**
	 * 环形oms请求dmp
	 *
	 * @return
	 */
	@RequestMapping("/hx_hander")
	public @ResponseBody String hx_hander(HttpServletRequest request) {

		this.coreService.selectOMStemp_feedback();
		return "OMS环形对接手动反馈成功";
	}
	
	/**
	 * 环形oms请求dmp 
	 *多线程
	 * @return
	 */
	@RequestMapping("/hx_thread_hander")
	public @ResponseBody String hx_thread_hander(HttpServletRequest request) {

		this.coreExcutorService.selectOMStemp_feedback();
		return "OMS环形对接手动反馈成功-多线程";
	}

	@RequestMapping("/liantong")
	public @ResponseBody String liantong_hander(HttpServletRequest request) {

		this.liantongService.feedback_status();
		return "SUCCESS";
	}

	// 配送结果
	@RequestMapping("/telecom_test")
	public void telecom_test(HttpServletResponse response, HttpServletRequest request) {
		this.telecomshopService.feedback_status();
	}

	// maimaibao
	@RequestMapping("/mmb_test")
	public void mmb_test(HttpServletResponse response, HttpServletRequest request) {
		this.mmbService.feedback_status();
	}

	// 配送结果
	@RequestMapping("/wx_test")
	public void wx_test(HttpServletResponse response, HttpServletRequest request) {
		this.wanxiangService.feedback_status();
	}

	// 配送结果
	@RequestMapping("/letv_test")
	public void letv_test(HttpServletResponse response, HttpServletRequest request) {
		this.letvService.feedback_status();
	}

	// 配送结果
	@RequestMapping("/yonghui_test")
	public void yonghui_test(HttpServletResponse response, HttpServletRequest request) {
		this.yonghuiService.feedback_status();
	}

	// 配送结果
	@RequestMapping("/hxgdms_test")
	public void hxgdms_test(HttpServletResponse response, HttpServletRequest request) {
		this.hxgdmsService.feedback_status();
	}

	/**
	 * 轮偱order表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/wl_insert")
	public @ResponseBody String getInfoWholeline(HttpServletRequest request) {
		this.wholeLineService.getInfoCall();
		return "查询已出库数据插入到轮询表完成";
	}

	/**
	 * webservice请求
	 *
	 * @param request
	 *            方法名：queryWaybillRoute， 参数：comeCode[XA0300],waybillNos[订单号]
	 * @return
	 */
	@RequestMapping("/wl_search")
	public @ResponseBody String getOrderForWholeline(HttpServletRequest request) {
		int key = B2cEnum.wholeLine.getKey();
		WholeLine whole = this.wholeLineService.getWholeline(key);
		this.wholeLineService_search.searchQuanxianRoute(key, whole);
		return "手动执行请求全线快递完成";
	}

	/**
	 * 乐宠test
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/lechong_test")
	public @ResponseBody String lechong_test(HttpServletRequest request) {
		this.lechongService.feedback_status();
		return "SUCCESS";
	}

	/**
	 * 家有购物test
	 *
	 * @return
	 */
	@RequestMapping("/homegobj_test")
	public @ResponseBody String homegobj_test(HttpServletRequest request) {
		this.homegobjService.feedback_status();
		return "手动执行反馈家有购物北京完成";
	}

	@RequestMapping("/sfxhm_test")
	public @ResponseBody String sfxhmtest(HttpServletRequest request) {
		// SfxhmNote note = new SfxhmNote();
		// note.setMailno("test123456798");
		// note.setNote("货物入库，测试");
		// note.setOperator("张测试");
		// note.setScan_time("2014-08-21 09:30:00");
		// note.setScan_type("31");
		// note.setScan_zone("测试站");
		// note.setShipper("张派送");
		// note.setUpload_time("2014-08-21 09:50:00");

		this.sfxhmService.feedback_status();
		return "SUCCESS";
	}

	@RequestMapping("/lefeng_test")
	public @ResponseBody String lefeng_test(HttpServletRequest request) {
		this.lefengService.feedback_status();
		return "手动执行反馈乐峰网成功";
	}

	@RequestMapping("/gztl_test")
	public @ResponseBody String gztl_test(HttpServletRequest request) {
		this.gztlService.feedback_status();
		return "手动执行反馈广州通路成功";
	}
	
	

}