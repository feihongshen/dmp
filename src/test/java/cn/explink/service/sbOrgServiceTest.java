package cn.explink.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pjbest.pjorganization.bizservice.service.SbOrgModel;
import com.pjbest.pjorganization.bizservice.service.SbOrgRequest;
import com.pjbest.pjorganization.bizservice.service.SbOrgResponse;
import com.pjbest.pjorganization.bizservice.service.SbOrgService;
import com.pjbest.pjorganization.bizservice.service.SbOrgServiceHelper;
import com.vip.osp.core.context.InvocationContext;

public class sbOrgServiceTest {
	
	@Before
	public void setUp(){
		InvocationContext.Factory.getInstance().setCalleeIP("10.199.199.245");
		InvocationContext.Factory.getInstance().setCalleePort(1803);
		InvocationContext.Factory.getInstance().setTimeout(3000);
	}
	
	@Test
	public void testAddSbOrg() throws Exception {
		SbOrgService sbOrgService = new SbOrgServiceHelper.SbOrgServiceClient();
		SbOrgModel model = getSbOrgModelFromBranch();
		SbOrgResponse result = sbOrgService.addSbOrg(model);
		Assert.assertEquals("1", result.getResultCode());
	}
	
	private SbOrgModel getSbOrgModelFromBranch() {
		SbOrgModel model = new SbOrgModel();
		model.setOrgSource("DMP");
		model.setOrgName("a站点01");
		model.setOrgType((byte) 4);
		model.setProvinceName("山西省");
		model.setTelephone("");
		model.setFax("");
		model.setContactor("b01");
		model.setAddress("");
		model.setEmail("");
		model.setIsCarriers((byte)1);
		model.setCarrierCode("30113");
		model.setIsActive((byte) 1);
		model.setLesseeCode("001");
		model.setCreatedByUser("b01");
		model.setCarrierSiteCode("102");
		model.setSuperSiteCode("30113");
		model.setPickingCode("zja01");
		
		model.setUpdatedByUser("b01");
		model.setUpdatedDtmLoc((new Date()).getTime());
		return model;

	}
	
	@Test
	public void testUpdateBranchSyncToOsp() throws Exception {
		SbOrgService sbOrgService = new SbOrgServiceHelper.SbOrgServiceClient();
		SbOrgModel model = getSbOrgModelFromBranch();
		SbOrgResponse result = sbOrgService.updateSbOrgByCarrierAndSiteCode(model);
		Assert.assertEquals("1", result.getResultCode());
	}
	
	@Test
	public void testUpdateBranchSetAactiveSyncToOsp() throws Exception {
		SbOrgService sbOrgService = new SbOrgServiceHelper.SbOrgServiceClient();
		SbOrgModel model = getSbOrgModelFromBranch();
		model.setIsActive((byte) 0);
		SbOrgResponse result = sbOrgService.updateSbOrgByCarrierAndSiteCode(model);
		Assert.assertEquals("1", result.getResultCode());
	}
	
	@Test
	public void testFindBranchSyncFromOsp() throws Exception {
		SbOrgService sbOrgService = new SbOrgServiceHelper.SbOrgServiceClient();
		SbOrgRequest model = new SbOrgRequest();
		model.setOrgSource("DMP");
		String carrierCode = "30113";
		String carrierSiteCode = "12341234";

		model.setUpdatedByUser("ww");
		model.setUpdatedDtmLoc((new Date()).getTime());

		SbOrgModel result = sbOrgService.findOrgByCarrierAndSiteCode(carrierCode, carrierSiteCode);
		Assert.assertNotNull(result);
		Assert.assertEquals("30113",result.getCarrierCode());
		Assert.assertEquals("12341234",result.getCarrierSiteCode());
	}
	
	@Test
	public void testDeleteBranchSyncToOsp() throws Exception {
		SbOrgService sbOrgService = new SbOrgServiceHelper.SbOrgServiceClient();
		SbOrgRequest model = new SbOrgRequest();
		model.setOrgSource("DMP");
		model.setCarrierCode("101");
		model.setCarrierSiteCode("30113");

		model.setUpdatedByUser("ww");
		model.setUpdatedDtmLoc((new Date()).getTime());

		SbOrgResponse result = sbOrgService.deleteSbOrgByCarrierAndSiteCode(model);
		Assert.assertEquals("1", result.getResultCode());
	}

}
