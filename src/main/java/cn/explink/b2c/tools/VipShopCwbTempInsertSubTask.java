package cn.explink.b2c.tools;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.b2c.vipshop.VipShop;
import cn.explink.b2c.vipshop.VipshopInsertCwbDetailTimmer;
import cn.explink.controller.CwbOrderDTO;

/**
 * 订单临时表转主表业务
 * @date 2016-11-23
 * @author jian.xie
 */
public class VipShopCwbTempInsertSubTask implements Runnable{
	
	private Logger logger = LoggerFactory.getLogger(VipShopCwbTempInsertSubTask.class);
	
	private VipshopInsertCwbDetailTimmer 			vipshopInsertCwbDetailTimmer; 
	
	private VipShop									vipshop;
	
	private String									customerids;
	
	private List<CwbOrderDTO>						listCwborderDto;
	
	private CountDownLatch 							downLatch;
	
	public VipShopCwbTempInsertSubTask(VipShop vipshop, String customerids, List<CwbOrderDTO> listCwbOrderDto, VipshopInsertCwbDetailTimmer vipshopInsertCwbDetailTimmer, CountDownLatch downLatch){
		this.vipshop = vipshop;
		this.customerids = customerids;
		this.listCwborderDto = listCwbOrderDto;
		this.vipshopInsertCwbDetailTimmer = vipshopInsertCwbDetailTimmer;
		this.downLatch = downLatch;
	}

	@Override
	public void run() {
		try{
			vipshopInsertCwbDetailTimmer.subDealWithOrders(vipshop, customerids, listCwborderDto);
		}finally{
			downLatch.countDown();
		}
	}

}
