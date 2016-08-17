package cn.explink.controller.express;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.b2c.pjwl.ExpressCwbOrderDataImportDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressInfoDetailVO;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.VO.express.EmbracedImportOrderVO;
import cn.explink.domain.VO.express.EmbracedImportResult;
import cn.explink.domain.VO.express.EmbracedOrderVO;
import cn.explink.domain.express.ExpressPreOrder;
import cn.explink.domain.express.ExpressWeigh;
import cn.explink.domain.express.NewAreaForm;
import cn.explink.domain.express2.VO.ReserveOrderPageVo;
import cn.explink.domain.express2.VO.ReserveOrderVo;
import cn.explink.service.Excel2003Extractor;
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.ExcelExtractor;
import cn.explink.service.ResultCollectorManager;
import cn.explink.service.express.EmbracedOrderInputService;
import cn.explink.service.express2.ReserveOrderService;
import cn.explink.util.ExportUtil4Express;
import cn.explink.util.Page;

import com.pjbest.deliveryorder.service.OmReserveOrderModel;

/**
 *
 * @description 揽件录入和揽件补录的controller
 * @author  刘武强
 * @data   2015年8月6日
 * 待确认： 如果运单号不是该站点的小件员揽收的，如何处理
 */
@Controller
@RequestMapping("/embracedOrderInputController")
public class EmbracedOrderInputController extends ExpressCommonController {

	@Autowired
	private EmbracedOrderInputService embracedOrderInputService;

	@Autowired
	Excel2007Extractor excel2007Extractor;

	@Autowired
	Excel2003Extractor excel2003Extractor;

	@Autowired
	ResultCollectorManager resultCollectorManager;
	@Autowired
	ExpressCwbOrderDataImportDAO expressCwbOrderDataImportDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ReserveOrderService reserveOrderService;

	private ExcelExtractor excelExtractor = null;
	private static final String CONTENT_TYPE = "text/html; charset=GBK";

	/**
	 *
	 * @Title: embracedOrderInputInit
	 * @description 揽件录入初始化方法
	 * @author 刘武强
	 * @date  2015年8月5日上午8:56:08
	 * @param  @param model
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/embracedOrderInputInit")
	public String embracedOrderInputInit(Model model) {
		//根据站点位置，自动匹配寄件人地址的省市（先看省市id，如果没有则匹配省市名称）
		model.addAttribute("deliveryMansList", this.embracedOrderInputService.getDeliveryManBybranchid());
		//获取省市的级联下拉列表
		Long provinceid = this.embracedOrderInputService.getProvinceId();
		model.addAttribute("BranchprovinceId", provinceid);
		model.addAttribute("BranchcityId", this.embracedOrderInputService.getCityId());
		//根据站点获取小件员
		List<AdressVO> provincelist = this.embracedOrderInputService.getProvince();
		model.addAttribute("provincelist", provincelist);
		if (provinceid == 0) {
			model.addAttribute("citylist", this.embracedOrderInputService.getCityOfProvince(provincelist.size() > 0 ? provincelist.get(0).getCode() : null));
		} else {
			model.addAttribute("citylist", this.embracedOrderInputService.getCityOfProvince(provinceid.intValue()));
		}
		return "express/stationOperation/embracedOrderInput";
	}

	/**
	 *
	 * @Title: embracedOrderExtraInputInit
	 * @description 揽收运单补录初始化方法
	 * @author 刘武强
	 * @date  2015年8月6日上午10:46:57
	 * @param  @param model
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/embracedOrderExtraInputInit")
	public String embracedOrderExtraInputInit(Model model, String orderNo) {
		this.logger.info("进入揽收运单补录页面时间为：" + System.currentTimeMillis());
		long startTime = System.currentTimeMillis();
		AdressInfoDetailVO adressInfoDetailVO = this.embracedOrderInputService.getAdressInfoByBranchid();
		
		//获取尚未补录的订单
		model.addAttribute("notExtraInputNumber", this.embracedOrderInputService.getNonExtraInputOrder(1, Page.ONE_PAGE_NUMBER).get("count"));
		//站点的省
		Long provinceid = this.embracedOrderInputService.getProvinceId();
		model.addAttribute("BranchprovinceId", provinceid);
		//站点的市
		Long cityid = this.embracedOrderInputService.getCityId();
		model.addAttribute("BranchcityId", cityid);
		//站点的区
		Long countyid = this.embracedOrderInputService.getCountyId();
		model.addAttribute("BranchcountyId", countyid);
		model.addAttribute("orderNo", orderNo);
		model.addAttribute("customers", this.embracedOrderInputService.getCustomers(1));
		//uat后新加
		model.addAttribute("branchname", this.embracedOrderInputService.getBracnch().getBranchname());
		model.addAttribute("deliveryMansList", this.embracedOrderInputService.getDeliveryManBybranchid());
		
		//获取根据所在机构的地址
		model.addAttribute("adressInfoDetailVO", adressInfoDetailVO);
		
		this.logger.info("请求揽收运单补录页面时间为：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return "express/stationOperation/embracedOrderExtraStandard";
	}

	/**
	 *
	 * @Title: getDeliveryManByOrderNo
	 * @description	根据录入的运单号，查询关联的小件员
	 * @author 刘武强
	 * @date  2015年8月6日上午10:58:49
	 * @param  @param oj
	 * @param  @return
	 * @return  JSONObject
	 * @throws
	 */
	@RequestMapping("/getDeliveryManByOrderNo")
	@ResponseBody
	public JSONObject getDeliveryManByOrderNo(String orderNo) {
		JSONObject obj = new JSONObject();
		ExpressPreOrder expressPreOrder = this.embracedOrderInputService.getDeliveryManByOrderNO(orderNo);
		obj.put("delivermanId", expressPreOrder != null ? expressPreOrder.getDelivermanId() : "");
		return obj;
	}

	/**
	 *
	 * @Title: getProvince
	 * @description 获取境内省份
	 * @author 刘武强
	 * @date  2015年9月28日上午10:28:03
	 * @param  InnerOrOut:"Inner":境内，”Out“:境外
	 * @param  @return
	 * @return  JSONObject
	 * @throws
	 */
	@RequestMapping("/getProvince")
	@ResponseBody
	public JSONObject getProvinceInner(String InnerOrOut) {
		JSONObject obj = new JSONObject();
		List<NewAreaForm> provincelist = this.embracedOrderInputService.getNewProvince(InnerOrOut);
		obj.put("provincelist", provincelist);
		return obj;
	}

	/**
	 *
	 * @Title: getNextAddress
	 * @description 根据上级地址，获取下级地址信息
	 * @author 刘武强
	 * @date  2015年9月28日下午1:01:19
	 * @param  @param parentCode :上级地址的addressCode
	 * @param  @return
	 * @return  List<NewAreaForm>
	 * @throws
	 */
	@RequestMapping("/getNextAddress")
	@ResponseBody
	public JSONObject getNextAddress(String parentCode) {
		JSONObject obj = new JSONObject();
		List<NewAreaForm> addresslist = this.embracedOrderInputService.getNextAddress(parentCode);
		obj.put("addresslist", addresslist);
		return obj;
	}

	/**
	 *
	 * @Title: getMunicipalityByProvince
	 * @description 根据选择的省份，查询该省的市
	 * @author 刘武强
	 * @date  2015年8月6日上午11:07:22
	 * @param  @param oj
	 * @param  @return
	 * @return  JSONObject
	 * @throws
	 */
	@RequestMapping("/getCityByProvince")
	@ResponseBody
	public JSONObject getCityByProvince(String provinceCode) {
		JSONObject obj = new JSONObject();
		List<AdressVO> citylist = this.embracedOrderInputService.getCityOfProvince(provinceCode);
		obj.put("citylist", citylist);
		List<AdressVO> countylist = this.embracedOrderInputService.getCountyOfCity(citylist.size() > 0 ? citylist.get(0).getCode() : null);
		obj.put("countylist", countylist);
		obj.put("townlist", this.embracedOrderInputService.getTownOfCounty(countylist.size() > 0 ? countylist.get(0).getCode() : null));
		return obj;
	}

	/**
	 *
	 * @Title: getCountyByCity
	 * @description 根据选择的市，查询该市的区/县
	 * @author 刘武强
	 * @date  2015年8月10日下午2:55:32
	 * @param  @param cityCode
	 * @param  @return
	 * @return  JSONObject
	 * @throws
	 */
	@RequestMapping("/getCountyByCity")
	@ResponseBody
	public JSONObject getCountyByCity(String cityCode) {
		JSONObject obj = new JSONObject();
		List<AdressVO> countylist = this.embracedOrderInputService.getCountyOfCity(cityCode);
		obj.put("countylist", countylist);
		obj.put("townlist", this.embracedOrderInputService.getTownOfCounty(countylist.size() > 0 ? countylist.get(0).getCode() : null));
		return obj;
	}

	/**
	 *
	 * @Title: getTownOfCounty
	 * @description 根据选择的区/县，查询该区/县的街道
	 * @author 刘武强
	 * @date  2015年8月10日下午2:56:06
	 * @param  @param cityCode
	 * @param  @return
	 * @return  JSONObject
	 * @throws
	 */
	@RequestMapping("/getTownOfCounty")
	@ResponseBody
	public JSONObject getTownOfCounty(String countyCode) {
		JSONObject obj = new JSONObject();
		obj.put("townlist", this.embracedOrderInputService.getTownOfCounty(countyCode));
		return obj;
	}

	/**
	 *
	 * @Title: judgeCwbOrderSaved
	 * @description 揽件录入判断运单号是否已经录入
	 * @author 刘武强
	 * @date  2015年8月10日下午3:21:28
	 * @param  @param orderNo
	 * @param  @return
	 * @return  JSONObject
	 * @throws
	 */
	@RequestMapping("/judgeCwbOrderSaved")
	@ResponseBody
	public JSONObject judgeCwbOrderSaved(String orderNo) {
		JSONObject obj = new JSONObject();
		//如果该orderNo在数据库找到了记录，则返回true
		obj.put("flag", this.embracedOrderInputService.judgeCwbOrderByCwb(orderNo) == null ? false : true);
		return obj;
	}

	/**
	 *
	 * @Title: getCwbOrderEmbraced
	 * @description 揽件补录，通过运单号，获取揽件录入数据
	 * @author 刘武强
	 * @date  2015年8月11日上午3:04:32
	 * @param  @param orderNo
	 * @param  @return
	 * @return  JSONObject
	 * @throws
	 */
	@RequestMapping("/getCwbOrderEmbraced")
	@ResponseBody
	public JSONObject getCwbOrderEmbraced(String orderNo) {
		JSONObject obj = new JSONObject();
		EmbracedOrderVO embracedOrderVO = this.embracedOrderInputService.judgeCwbOrderByCwb(orderNo);
		ExpressWeigh expressWeigh = this.embracedOrderInputService.getWeighByCwb(orderNo, this.getSessionUser().getBranchid());
		//查询快递单号的图片路径
		String expressImage = expressCwbOrderDataImportDAO.getExpressImageById(orderNo);
		
		//根据运单号获取预约单号
		OmReserveOrderModel omReserveOrderModel = new OmReserveOrderModel();
		String tpstransportNo = cwbDAO.getTpsTransportNoByCwb(orderNo);
         
		ReserveOrderVo  reserveOrder = null;
		if(tpstransportNo!=null&&!tpstransportNo.isEmpty()){
			omReserveOrderModel.setTransportNo(tpstransportNo);
			ReserveOrderPageVo reserveOrderVO = this.reserveOrderService.getReserveOrderPage(omReserveOrderModel,1,1);
			if(reserveOrderVO.getReserveOrderVoList().size()!=0){
				reserveOrder = reserveOrderVO.getReserveOrderVoList().get(0);
			}
		}
		boolean isRepeat = this.embracedOrderInputService.checkTranscwb(orderNo);//校验录入运单号是否与系统订单号/运单号重复 add by vic.liang@pjbest.com 2016-08-05
		obj.put("embracedOrderVO", embracedOrderVO);
		obj.put("expressWeigh", expressWeigh);
		obj.put("branchid", this.getSessionUser().getBranchid());
		obj.put("expressImage", expressImage);
		obj.put("reserveOrder", reserveOrder);
		obj.put("isRepeatTranscwb", isRepeat);
		return obj;
	}

	/**
	 *
	 * @Title: inputSave
	 * @description 将前台返回的揽件信息保存到订单表
	 * @author 刘武强
	 * @date  2015年8月6日上午10:40:49
	 * @param  @param model
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/inputSave")
	public String inputSave(Model model, EmbracedOrderVO embracedOrderVO) {
		long startTime = System.currentTimeMillis();
		//保存订单数据
		String flag = this.embracedOrderInputService.savaEmbracedOrderVO(embracedOrderVO, 0);
		//根据站点获取小件员
		model.addAttribute("deliveryMansList", this.embracedOrderInputService.getDeliveryManBybranchid());
		//获取省市的级联下拉列表
		Long provinceid = this.embracedOrderInputService.getProvinceId();
		model.addAttribute("BranchprovinceId", provinceid);
		Long countyid = this.embracedOrderInputService.getCountyId();
		model.addAttribute("BranchcountyId", countyid);
		List<AdressVO> provincelist = this.embracedOrderInputService.getProvince();
		model.addAttribute("provincelist", provincelist);
		if (provinceid == 0) {
			model.addAttribute("citylist", this.embracedOrderInputService.getCityOfProvince(provincelist.size() > 0 ? provincelist.get(0).getCode() : null));
		} else {
			model.addAttribute("citylist", this.embracedOrderInputService.getCityOfProvince(provinceid.intValue()));
		}
		model.addAttribute("flag", flag);
		//目前录入不需要给dmp回写信息
		/*if ("true".equals(flag)) {
			this.embracedOrderInputService.dmpWriteBack(embracedOrderVO, "input");
		}*/
		this.logger.info("揽收运单补录保存操作的时间为：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return "express/stationOperation/embracedOrderInput";
	}

	/**
	 *
	 * @Title: extraInputSave
	 * @description 揽收运单补录的保存方法
	 * @author 刘武强
	 * @date  2015年8月6日上午11:27:56
	 * @param  @param model
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/extraInputSave")
	public String extraInputSave(Model model, EmbracedOrderVO embracedOrderVO, int isRead) {
		// save数据（放在service里面，添加事务）
		String flag = this.embracedOrderInputService.savaEmbracedOrderVO(embracedOrderVO, "0".equals(embracedOrderVO.getIsadditionflag()) ? 2 : 1);
		//刘武强 11.17  对页面读取的实际重量进行保存
		this.embracedOrderInputService.savaexpressWeigh(embracedOrderVO, isRead);
		// 获取回显数据
		model.addAttribute("notExtraInputNumber", this.embracedOrderInputService.getNonExtraInputOrder(1, Page.ONE_PAGE_NUMBER).get("count"));
		//获取省市的级联下拉列表
		Long provinceid = this.embracedOrderInputService.getProvinceId();
		model.addAttribute("BranchprovinceId", provinceid);
		model.addAttribute("BranchcityId", this.embracedOrderInputService.getCityId());
		model.addAttribute("BranchcountyId", this.embracedOrderInputService.getCountyId());
		//List<AdressVO> provincelist = this.embracedOrderInputService.getProvince();
		//model.addAttribute("provincelist", provincelist);
		//List<AdressVO> citylist;
		//if (provinceid == 0) {
		//	citylist = this.embracedOrderInputService.getCityOfProvince(provincelist.size() > 0 ? provincelist.get(0).getCode() : null);
		//} else {
		//	citylist = this.embracedOrderInputService.getCityOfProvince(provinceid.intValue());
		//}
		//model.addAttribute("citylist", citylist);
		//List<AdressVO> countylist = this.embracedOrderInputService.getCountyOfCity(citylist.size() > 0 ? citylist.get(0).getCode() : null);
		//model.addAttribute("countylist", countylist);
		//model.addAttribute("townlist", this.embracedOrderInputService.getTownOfCounty(countylist.size() > 0 ? countylist.get(0).getCode() : null));
		model.addAttribute("flag", flag);
		model.addAttribute("customers", this.embracedOrderInputService.getCustomers(1));
		model.addAttribute("branchname", this.embracedOrderInputService.getBracnch().getBranchname());
		model.addAttribute("deliveryMansList", this.embracedOrderInputService.getDeliveryManBybranchid());
		return "express/stationOperation/embracedOrderExtraStandard";
	}

	/**
	 *
	 * @Title: nonExtraInput
	 * @description 揽收运单补录-未补录查询页面
	 * @author 刘武强
	 * @date  2015年8月10日下午11:10:46
	 * @param  @param model
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/nonExtraInput/{page}")
	@ResponseBody
	public JSONObject nonExtraInput(Model model, @PathVariable(value = "page") long page) {
		JSONObject obj = new JSONObject();
		Map<String, Object> map = this.embracedOrderInputService.getNonExtraInputOrder(page, Page.ONE_PAGE_NUMBER);
		obj.put("infoMap", map.get("list"));
		obj.put("page_obj", new Page((Integer) map.get("count"), page, Page.ONE_PAGE_NUMBER));
		obj.put("page", page);
		return obj;
	}

	/**
	 *
	 * @Title: exportAllExtraOrder
	 * @description 揽收运单补录-导出所有未补录运单
	 * @author 王志宇
	 * @date  2015年10月29日下午14:23
	 * @param  @param model
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/exportAllNotExtraOrder")
	@ResponseBody
	public void exportAllnotExtraOrder(Model model, HttpServletRequest request, HttpServletResponse response, Long page) {
		List<EmbracedImportOrderVO> list = this.embracedOrderInputService.getAllNotExtraOrder();
		ExportUtil4Express.exportXls(request, response, list, EmbracedImportOrderVO.class, "未补录运单信息");
	}

	/**
	 *
	 * @Title: importExcel
	 * @description 补录界面导入方法
	 * @author 刘武强
	 * @date  2015年10月10日下午2:43:46
	 * @param  @param model
	 * @param  @param file
	 * @param  @param request
	 * @param  @param response
	 * @param  @return
	 * @param  @throws Exception
	 * @return  EmbracedImportResult
	 * @throws
	 */
	@RequestMapping("/import")
	public EmbracedImportResult importExcel(Model model, @RequestParam("Filedata") final MultipartFile file, final HttpServletRequest request, HttpServletResponse response) throws Exception {
		final EmbracedImportResult resultCollector = this.resultCollectorManager.createNewEmbracedImportResultCollector();//存放导入结果
		this.excelExtractor = this.getExcelExtractor(file);//解析器
		final InputStream inputStream = file.getInputStream();//需要导入的文件流
		final User user = this.getSessionUser();
		response.getWriter().write(resultCollector.getId());
		response.getWriter().flush();
		response.flushBuffer();
		response.getWriter().close();
		if (this.excelExtractor != null) {
			ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
			newSingleThreadExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						//导入文件
						EmbracedOrderInputController.this.excelExtractor.extractExbraced(inputStream, resultCollector, user);
					} catch (Exception e) {
						resultCollector.setResultErrMsg("处理出错");
						e.printStackTrace();
					} finally {
						resultCollector.setFinished(true);
					}
				}
			});
		} else {
			resultCollector.setResultErrMsg("不可识别的文件");
		}

		return null;
	}

	/**
	 *
	 * @Title: getExcelExtractor
	 * @description 获取解析器
	 * @author 刘武强
	 * @date  2015年10月10日下午2:46:26
	 * @param  @param file
	 * @param  @return
	 * @return  ExcelExtractor
	 * @throws
	 */

	public ExcelExtractor getExcelExtractor(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename.endsWith("xlsx")) {
			return this.excel2007Extractor;
		} else if (originalFilename.endsWith(".xls")) {
			return this.excel2003Extractor;
		}
		return null;
	}

	/**
	 *
	 * @Title: saveImportInfo
	 * @description 确认导入后，将信息保存（保存前做校验）
	 * @author 刘武强
	 * @date  2015年10月13日上午9:06:48
	 * @param  @param id ： resultCollector的标示id
	 * @param  @return
	 * @return  JSONObject
	 * @throws
	 */
	@RequestMapping("/saveImportInfo")
	@ResponseBody
	public JSONObject saveImportInfo(String id) {
		long startTime = System.currentTimeMillis();
		JSONObject obj = new JSONObject();
		final User user = this.getSessionUser();
		try {
			this.excelExtractor.saveExbracedImport(this.resultCollectorManager.getEmbracedResultCollector(id).getAnalysisList(), user);
			obj.put("flag", true);
		} catch (Exception e) {
			obj.put("flag", false);
			e.printStackTrace();
		}
		this.logger.info("揽收运单补录导入的订单保存操作的时间为：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return obj;
	}

	@RequestMapping("/downloadExcel")
	public void downloadExcel(String filePath, HttpServletResponse response, HttpServletRequest request) {
		response.setContentType(EmbracedOrderInputController.CONTENT_TYPE);
		//得到下载文件的名字
		//String filename=request.getParameter("filename");

		//获取文件的根目录
		String path1 = request.getSession().getServletContext().getRealPath("/");

		String filename = new String(path1 + "/uppda/excel.xlsx");

		//创建file对象
		File file = new File(filename);

		//设置response的编码方式
		response.setContentType("application/x-msdownload");

		//写明要下载的文件的大小
		response.setContentLength((int) file.length());

		//设置附加文件名
		// response.setHeader("Content-Disposition","attachment;filename="+filename);

		//解决中文乱码
		response.setHeader("Content-Disposition", "attachment;fileName=" + "excel.xlsx");
		try {
			//读出文件到i/o流
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream buff = new BufferedInputStream(fis);

			byte[] b = new byte[1024];//相当于我们的缓存

			long k = 0;//该值用于计算当前实际下载了多少字节

			//从response对象中得到输出流,准备下载

			OutputStream myout = response.getOutputStream();

			//开始循环下载

			while (k < file.length()) {

				int j = buff.read(b, 0, 1024);
				k += j;

				//将b中的数据写到客户端的内存
				myout.write(b, 0, j);

			}

			//将写入到客户端的内存的数据,刷新到磁盘
			myout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/changeSenderAddr")
	public void changeSenderAddr() {
		this.embracedOrderInputService.changeSenderAddr();
	}
}
