package cn.explink.controller;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class CwbOrderDTOTest {

	@Test
	public void testDashShouldNotRemove() {
		CwbOrderDTO cwbOrderDTO=new CwbOrderDTO();
		cwbOrderDTO.setConsigneeaddress("*# 重庆市龙湖南苑5-6-503");
		Assert.assertEquals("*# 重庆市龙湖南苑5-6-503", cwbOrderDTO.getConsigneeaddress());
	}
	


}
