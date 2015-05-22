package cn.explink.b2c.gztl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/gztl")
public class GztlController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	GztlService gztlService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	GuangZhouTongLuInsertCwbDetailTimmer guangZhouTongLuInsertCwbDetailTimmer;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("guangzhoutongluObject", this.gztlService.getGztl(key));
		model.addAttribute("warehouselist", this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/guangzhoutonglu";
	}

	@RequestMapping("/saveGuangzhoutonglu/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {
			this.gztlService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.gztlService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 订单导入接口
	 */
	@RequestMapping("/importgztl")
	public @ResponseBody String requestByGztl(HttpServletRequest request, HttpServletResponse response) {
		String xml = null;
		try {

			int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Guangzhoutonglu.getKey());
			if (isOpenFlag == 0) {
				return this.errorReturnData("F", "未开启0广州通路0推进接口");

			}
			Gztl gztl = this.gztlService.getGztl(B2cEnum.Guangzhoutonglu.getKey());

			xml = request.getParameter("XML");
			String MD5 = request.getParameter("MD5");

			this.logger.info("广州通路请求参数xml={},MD5={}", xml, MD5);

			String localSignString = MD5Util.md5(xml + gztl.getPrivate_key());
			System.out.println(localSignString);
			if (!MD5.equalsIgnoreCase(localSignString)) {
				this.logger.info("签名验证失败,xml={},MD5={}", xml, MD5);
				return this.errorReturnData("F", "签名验证失败");
			}

			return this.gztlService.orderDetailExportInterface(xml, gztl);
		} catch (Exception e) {
			this.logger.error("0广州通路处理业务逻辑异常！" + xml, e);
			return this.errorReturnData("F", "处理业务逻辑异常");
		}
	}

	@RequestMapping("/gztl_timmer")
	public @ResponseBody void ExcuteTimmerMethod_tmall(HttpServletRequest request, HttpServletResponse response) {
		this.guangZhouTongLuInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.Guangzhoutonglu.getKey());
		this.logger.info("执行了广州通路查询临时表的定时器!");
	}

	public String errorReturnData(String flag, String remark) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<MSD>");
		buffer.append("<Orders>");
		buffer.append("<Order>");
		buffer.append("<orderno></iorderno>");
		buffer.append("<result>" + flag + "</result>");
		buffer.append("<remark>" + remark + "</remark>");
		buffer.append("</Order>");
		buffer.append("</Orders>");
		buffer.append("</MSD>");
		return buffer.toString();
	}

	public String testhh() {
		String localSignString = MD5Util
				.md5(""
						+ "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><MSD><Orders><Order><typeid>3</typeid><orderid>QQLL1249103976</orderid><sclientcode>U2415984728041</sclientcode><shipperid>1号店</shipperid><consignorname></consignorname><consignoraddress>广东1号店</consignoraddress><consignormobile></consignormobile><consignorphone></consignorphone><customername>列晓蔓</customername><customeraddress>广东中山市沙溪镇博爱一路世纪新城三期别墅区D2-3</customeraddress><customermobile>186****8139</customermobile><customerphone>186****8139</customerphone><deliverygoods>立白天然皂液90g（含椰子油精华）;洁柔 面子系列三层抽取式面纸 百花香味 135抽*3包;威猛先生玻璃清洁剂双包装 500g+420g;洁柔蓝面子系列3层卷纸136g*12卷</deliverygoods><returngoods></returngoods><deliverygoodsprice></deliverygoodsprice><returngoodsprice></returngoodsprice><weight>12.52</weight><shouldreceive>0.0</shouldreceive><accuallyreceive></accuallyreceive><remark></remark><subnumber>1249103976010976,1249103976010975,1249103976010939</subnumber><arrivedate>2015-01-24 13:05:25</arrivedate><pushtime>2015-01-23 23:01:12</pushtime><goodsnum>3</goodsnum><deliverarea></deliverarea><extPayType>2415984728041</extPayType><orderBatchNo></orderBatchNo><otherservicefee></otherservicefee><orderDate>2015-01-23 23:02:57</orderDate></Order><Order><typeid>1</typeid><orderid>QQLL15012397735419</orderid><sclientcode>U15012397735419</sclientcode><shipperid>广州唯品会</shipperid><consignorname></consignorname><consignoraddress>VIP_NH</consignoraddress><consignormobile></consignormobile><consignorphone></consignorphone><customername>张利</customername><customeraddress>广东省中山市小榄镇沙口市场</customeraddress><customermobile>13318405703</customermobile><customerphone></customerphone><deliverygoods></deliverygoods><returngoods></returngoods><deliverygoodsprice></deliverygoodsprice><returngoodsprice></returngoodsprice><weight>0.0</weight><shouldreceive>27.0</shouldreceive><accuallyreceive></accuallyreceive><remark>送货时间不限</remark><subnumber></subnumber><arrivedate>2015-01-24 15:06:22</arrivedate><pushtime>2015-01-24 15:07:32</pushtime><goodsnum>1</goodsnum><deliverarea></deliverarea><extPayType>0</extPayType><orderBatchNo>BTH150124071397</orderBatchNo><otherservicefee>0</otherservicefee><orderDate>2015-01-24 15:08:44</orderDate></Order><Order><typeid>2</typeid><orderid>QQLL15011817123819</orderid><sclientcode>U15011817123819</sclientcode><shipperid>广州唯品会</shipperid><consignorname></consignorname><consignoraddress>VIP_NH</consignoraddress><consignormobile></consignormobile><consignorphone></consignorphone><customername>梁玲</customername><customeraddress>广东省.中山市.阜沙镇上南工业区富贵路2-2号‘发之界；收</customeraddress><customermobile>13435757161</customermobile><customerphone></customerphone><deliverygoods></deliverygoods><returngoods></returngoods><deliverygoodsprice></deliverygoodsprice><returngoodsprice></returngoodsprice><weight>11.23</weight><shouldreceive>-289.0</shouldreceive><accuallyreceive></accuallyreceive><remark>送货时间不限</remark><subnumber></subnumber><arrivedate>2015-01-20 15:49:21</arrivedate><pushtime>2015-01-20 15:49:21</pushtime><goodsnum>1</goodsnum><deliverarea></deliverarea><extPayType>0</extPayType><orderBatchNo>BTH150120066514</orderBatchNo><otherservicefee></otherservicefee><orderDate>2015-01-20 15:49:54</orderDate></Order></Orders></MSD>"
						+ "123456");
		return localSignString;
	}

	@RequestMapping("/test11111")
	public String test11111111111() {
		return "b2cdj/test11111111111111111";
	}

	public static void main(String[] args) {
		GztlController gztlController = new GztlController();
		String kkString = gztlController.testhh();
		System.out.println(kkString);
	}
}
