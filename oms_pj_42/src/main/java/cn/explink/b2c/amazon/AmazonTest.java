package cn.explink.b2c.amazon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/amazon")
@Controller
public class AmazonTest {
	@Autowired
	AmazonService_CommitDeliverInfo amazonService_CommitDeliverInfo;

	@RequestMapping("/test")
	public void gome_Task() {
		amazonService_CommitDeliverInfo.commitDeliverInfo_interface();
		amazonService_CommitDeliverInfo.commitDeliver_Cod();
	}
}
