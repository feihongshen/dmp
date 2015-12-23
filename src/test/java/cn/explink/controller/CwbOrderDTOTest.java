package cn.explink.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.pjbest.deliveryorder.bizservice.PjAddressInfo;
import com.pjbest.deliveryorder.bizservice.PjAddressInfoHelper;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPServiceHelper;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderInfo;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderServiceHelper;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient;
import com.pjbest.deliveryorder.idl.PjDeliveryOrder4DMPServiceIDL;
import com.pjbest.deliveryorder.reserve.service.InfReserveOrderSModel;
import com.pjbest.deliveryorder.reserve.service.PjSaleOrderQueryRequest;
import com.pjbest.pjorganization.bizservice.service.SbOrgModel;
import com.pjbest.pjorganization.bizservice.service.SbOrgService;
import com.pjbest.pjorganization.bizservice.service.SbOrgServiceHelper;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;



public class CwbOrderDTOTest {

	/*@Test
	public void testDashShouldNotRemove() {
		CwbOrderDTO cwbOrderDTO=new CwbOrderDTO();
		cwbOrderDTO.setConsigneeaddress("*# 重庆市龙湖南苑5-6-503");
		Assert.assertEquals("*# 重庆市龙湖南苑5-6-503", cwbOrderDTO.getConsigneeaddress());
	}*/
	@Test
	public void tt(){
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		try {
			InvocationContext.Factory.getInstance().setTimeout(10000);
			PjSaleOrderQueryRequest psqr = new PjSaleOrderQueryRequest();
			psqr.setCarrierCode("30102");
			psqr.setReserveOrderStatus(10);
			List<InfReserveOrderSModel> list = pjDeliveryOrderService.findSaleOrderList(psqr);
			PjDeliveryOrder4DMPService cc=new PjDeliveryOrder4DMPServiceHelper.PjDeliveryOrder4DMPServiceClient();
			
			System.out.println(list.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void ttt(){
		List<String> list=new ArrayList<String>();
		list.add("222");
		list.add("333");
		list.add("111");
		list.add("222");
		list.add("333");
		for(String str:list){
			System.out.println(str);
		}
		
		long a=14;
		String s=a+"";
		Integer i=Integer.parseInt(s);
		int ii=i;
		int iii=(int)a;
		return;
	}
	
	@Test
	public void tps(){
		try{
			SbOrgService service=new SbOrgServiceHelper.SbOrgServiceClient();
			//SbOrgModel model1=service.findOrganizationByCode("0283001");
			List<SbOrgModel>  modellist=service.findSbOrgByCarrierAndSelfStation("30107",null);
			SbOrgModel model=modellist.get(0);
		}
		catch(OspException e){
			
		}
		
	}
	


}
