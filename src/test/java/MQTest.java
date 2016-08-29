import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.explink.b2c.vipshop.VipshopInsertCwbDetailTimmer;
import daoTest.BaseTest;


public class MQTest extends BaseTest{
	@Autowired
	VipshopInsertCwbDetailTimmer vipshopInsertCwbDetailTimmer;
	//调用订单临时表转主表定时器
	@Test
	public void testVipshopTimmerJob(){
		vipshopInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(22501);
	}
}
