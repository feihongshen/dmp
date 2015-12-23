package cn.explink.service.express.tps;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;

import com.pjbest.deliveryorder.bizservice.PjDeliverOrder4DMPRequest;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPServiceHelper;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderServiceHelper;
import com.pjbest.deliveryorder.bizservice.PjDeliveryTrackInfo;
import com.pjbest.deliveryorder.dcpackage.service.PjDcPackageDtModel;
import com.pjbest.deliveryorder.dcpackage.service.PjDcPackageModel;
import com.pjbest.deliveryorder.dcpackage.service.PjUnPackRequest;
import com.pjbest.deliveryorder.reserve.service.InfReserveOrderSModel;
import com.pjbest.deliveryorder.reserve.service.PjSaleOrderQueryRequest;
import com.pjbest.deliveryorder.service.PjSaleOrderFeedbackRequest;
import com.pjbest.deliveryorder.service.PjTransportFeedbackRequest;
import com.pjbest.pjorganization.bizservice.service.SbOrgModel;
import com.pjbest.pjorganization.bizservice.service.SbOrgService;
import com.pjbest.pjorganization.bizservice.service.SbOrgServiceHelper;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;
import com.vip.top.mainid.bizservice.MainIdService;
import com.vip.top.mainid.bizservice.MainIdServiceHelper;

public class OSPTest {

	@Test
	public void test() { //3.1DMP调用DO的接口获取上门取件的预约单。(根据预约单类型获取相应的预约单)
		PjDeliveryOrderService pjDeliveryOrderService = this.getPjDeliveryOrderService();
		InvocationContext.Factory.getInstance().setTimeout(10000);
		try {
			PjSaleOrderQueryRequest psqr = new PjSaleOrderQueryRequest();
			psqr.setCarrierCode("PJWL");
			psqr.setReserveOrderStatus(0);
			List<InfReserveOrderSModel> list = pjDeliveryOrderService.findSaleOrderList(psqr);
			System.out.println(list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test1() throws OspException { //3.2落地配系将预约单的状态反馈给TPS的DO服务
		PjDeliveryOrderService pjDeliveryOrderService = this.getPjDeliveryOrderService();
		InvocationContext.Factory.getInstance().setTimeout(20000);
		PjSaleOrderFeedbackRequest request = new PjSaleOrderFeedbackRequest();
		request.setOperateType(26);
		request.setReserveOrderNo("A1508190000096");
		request.setOperateOrg("gdfy");
		request.setOperater("eee");
		try {
			boolean feedbackSaleOrder = pjDeliveryOrderService.feedbackSaleOrder(request);
			System.out.println(feedbackSaleOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test3() { // 3.7 DMP上传打包信息到DO服务
		PjDeliveryOrderService pjDeliveryOrderService = this.getPjDeliveryOrderService();
		InvocationContext.Factory.getInstance().setTimeout(10000);

		PjDcPackageModel pjPackage = new PjDcPackageModel();
		pjPackage.setPackageNo("1111545451");
		pjPackage.setPackRdc("061001");
		pjPackage.setPackTime(new Date().getTime());
		pjPackage.setPackMan("d");
		pjPackage.setTotalOrder(1);
		pjPackage.setTotalWeight(1.0);
		pjPackage.setTotalVolume(1.0);
		//pjPackage.setDestiCity("d");

		List<PjDcPackageDtModel> pjPackageDtList = new ArrayList<PjDcPackageDtModel>();
		PjDcPackageDtModel packageDt = new PjDcPackageDtModel();
		packageDt.setBoxNo("1");
		packageDt.setTransportNo("u34232323");
		pjPackageDtList.add(packageDt);

		pjPackage.setDtList(pjPackageDtList);
		try {
			boolean upLoadPackageInfo = pjDeliveryOrderService.upLoadPackageInfo(pjPackage);
			System.out.println(upLoadPackageInfo);
		} catch (OspException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test4() { // 3.9 DMP检测运单是否属于包
		PjDeliveryOrderService pjDeliveryOrderService = this.getPjDeliveryOrderService();
		InvocationContext.Factory.getInstance().setTimeout(10000);
		try {
			boolean checkPackageInfo = pjDeliveryOrderService.checkPackageInfo("u34232323", "1111545451");
			System.out.println(checkPackageInfo);
		} catch (OspException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test5() {
		ExpressOpeAjaxResult res = new ExpressOpeAjaxResult();
		Map<String, Object> map = new HashMap<String, Object>();
		PjDeliveryOrderService service = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		try {
			InvocationContext.Factory.getInstance().setTimeout(100000);
			boolean result = service.checkPackageExist("P151022000008");
			res.setStatus(true);
			map.put("checkResult", result);
			System.out.println("结果：" + result);
		} catch (OspException e) {
			res.setStatus(false);
			res.setMsg("校验包号唯一性接口异常,请联系管理员");
			System.out.println(e.getMessage());
		}
		res.setAttributes(map);
	}

	private PjDeliveryOrderService getPjDeliveryOrderService() {
		return new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
	}

	@Test
	public void test6() { //3.5 状态反馈接口
		PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
		//Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		transNoFeedBack.setTransportNo("lwq133223");
		transNoFeedBack.setOperateOrg("gdfy");
		transNoFeedBack.setOperater("lwq");
		transNoFeedBack.setOperateTime(System.currentTimeMillis());
		transNoFeedBack.setOperateType(FeedbackOperateTypeEnum.InboundScan.getValue());
		transNoFeedBack.setReason("");

		/*//拼接描述
		JoinMessageVO contextVar = new JoinMessageVO();
		contextVar.setOperationType(TpsOperationEnum.ArrivalScan.getValue());//揽件入站对应入站扫描
		contextVar.setStation(branch.getBranchname());//站点名称
		contextVar.setOperator(user.getRealname());
		contextVar.connectMessage();
		transNoFeedBack.setTransportDetail(contextVar.getTrackMessage());*/
		//发送JMS消息
		Boolean resultFlag = false;
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		try {
			resultFlag = pjDeliveryOrderService.feedbackTransport(transNoFeedBack);
		} catch (OspException e) {
			e.printStackTrace();
			System.out.println("调用失败了");
		}
	}

	@Test
	public void test7() { //3.5 状态反馈接口
		InvocationContext.Factory.getInstance().setTimeout(10000);
		PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
		//Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		transNoFeedBack.setTransportNo("t3223133223");
		transNoFeedBack.setOperateOrg("gdfy");
		transNoFeedBack.setOperater("lwq");
		transNoFeedBack.setOperateTime(System.currentTimeMillis());
		transNoFeedBack.setOperateType(FeedbackOperateTypeEnum.InboundScan.getValue());
		transNoFeedBack.setReason("");
		transNoFeedBack.setActualFee(0d);
		transNoFeedBack.setActualPayType(PaytypeEnum.CodPos.getValue() + "");
		Boolean resultFlag = false;
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		try {
			resultFlag = pjDeliveryOrderService.feedbackTransport(transNoFeedBack);
			System.out.println("成功！");
		} catch (OspException e) {
			e.printStackTrace();
			System.out.println("调用失败了");
		}
	}

	@Test
	public void test10() {
		InvocationContext.Factory.getInstance().setTimeout(10000);
		PjDeliveryOrder4DMPService pjDeliveryOrder4DMPService = new PjDeliveryOrder4DMPServiceHelper.PjDeliveryOrder4DMPServiceClient();
		List<String> list = new ArrayList<String>();
		list.add("12342");
		list.add("we2132323");
		try {
			Map<String, PjDeliverOrder4DMPRequest> resultList = pjDeliveryOrder4DMPService.getDeliveryOrderByTransportNos(list);
			System.out.println("成功！");
		} catch (OspException e) {
			e.printStackTrace();
			System.out.println("失败！");
		}
	}

	@Test
	public void test11() { //3.3 DMP调用DO服务获取预约单轨迹
		InvocationContext.Factory.getInstance().setTimeout(20000);
		List<PjDeliveryTrackInfo> pjDeliveryTrackInfo = new ArrayList<PjDeliveryTrackInfo>();
		try {
			PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
			pjDeliveryTrackInfo = pjDeliveryOrderService.findReserveTracks("A1508190000096");
		} catch (OspException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test12() { //3.6 DMP调用DO服务获取运单轨迹
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		List<PjDeliveryTrackInfo> result = new ArrayList<PjDeliveryTrackInfo>();
		try {
			result = pjDeliveryOrderService.getDeliveryOrderTracking("lwq133223");
		} catch (OspException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test13() { //3.8 DMP上传拆包打包信息到TPS
		Boolean resultFlag = false;
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		PjUnPackRequest pjUnPackRequest = new PjUnPackRequest();
		pjUnPackRequest.setTransportNo("4444");
		pjUnPackRequest.setOperateOrg("gdfy");
		pjUnPackRequest.setOperateTime(new Date().getTime());
		pjUnPackRequest.setOperator("lwq");
		pjUnPackRequest.setPackageNo("111154545");
		try {
			if (pjUnPackRequest != null) {
				resultFlag = pjDeliveryOrderService.unPack(pjUnPackRequest);
			}
			if (resultFlag == true) {
				System.out.println("请求TPS上传拆包信息成功");
			} else {
				System.out.println("请求TPS上传拆包信息失败");
			}
		} catch (OspException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test14() { //3.10 DMP调用DO服务获取运单信息
		PjDeliveryOrder4DMPService pjDeliveryOrder4DMPService = new PjDeliveryOrder4DMPServiceHelper.PjDeliveryOrder4DMPServiceClient();
		List<PjDeliverOrder4DMPRequest> result = new ArrayList<PjDeliverOrder4DMPRequest>();
		try {
			result = pjDeliveryOrder4DMPService.getDeliveryOrder("30102000000");
		} catch (OspException e) {
			e.getMessage();
		}
	}

	@Test
	public void test15() { //3.11 DMP调用DO服务反馈抓取成功的预约单
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		List<String> list = new ArrayList<String>();
		list.add("lwq12342");
		list.add("lwq2132323");
		try {
			pjDeliveryOrderService.makeSaleOrderSuccessList(list);
		} catch (OspException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test16() { //3.12 DMP调用DO服务反馈抓取成功的运单
		PjDeliveryOrder4DMPService pjDeliveryOrder4DMPService = new PjDeliveryOrder4DMPServiceHelper.PjDeliveryOrder4DMPServiceClient();
		List<String> list = new ArrayList<String>();
		list.add("lwq12342");
		list.add("lwq2132323");
		try {
			pjDeliveryOrder4DMPService.makeDeliveryOrderSuccessList(list);
		} catch (OspException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test2() { //3.13  DMP调用主单号服务获取运单号和包号
		MainIdService mainIdService = new MainIdServiceHelper.MainIdServiceClient();
		InvocationContext.Factory.getInstance().setTimeout(20000);
		String seqRuleNo = "PACKAGE_NO";
		Map<String, String> contextVars = new HashMap<String, String>();
		try {
			String seqNo = mainIdService.getNextSeq(seqRuleNo, contextVars);
			System.out.println(seqNo);
		} catch (OspException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test8() { //3.14 DMP调用组织机构服务获取自建组织机构明细
		SbOrgService sbOrgService = new SbOrgServiceHelper.SbOrgServiceClient();
		List<SbOrgModel> resultList = new ArrayList<SbOrgModel>();
		try {
			resultList = sbOrgService.findSbOrgByCarrierAndSelfStation(null, "gdfy");
			System.out.println("成功！");
		} catch (OspException e) {
			e.printStackTrace();
			System.out.println("失败！");
		}
	}

	@Test
	public void test9() { //3.15 DMP调用DO服务查询运单信息
		PjDeliveryOrder4DMPService pjDeliveryOrder4DMPService = new PjDeliveryOrder4DMPServiceHelper.PjDeliveryOrder4DMPServiceClient();
		PjDeliverOrder4DMPRequest resultList = new PjDeliverOrder4DMPRequest();
		try {
			resultList = pjDeliveryOrder4DMPService.getDeliveryOrderByTransportNo("122324234");
			System.out.println("成功！");
		} catch (OspException e) {
			e.printStackTrace();
			System.out.println("失败！");
		}
	}

}
