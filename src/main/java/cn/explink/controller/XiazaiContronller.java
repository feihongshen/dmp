package cn.explink.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.service.XiazaiService;
import cn.explink.util.DateTimeUtil;

@Controller
@RequestMapping("/xiazai")
public class XiazaiContronller {

	@Autowired
	XiazaiService xiazaiService;

	@RequestMapping("/test")
	public String add(Model model) {
		System.out.println("开始");
		  File f2 = new File("/home/apps/bakdata/detail.sql");
		  //int b=0;
		  try {
		   FileWriter writer = new FileWriter(f2);
		   BufferedWriter bw = new BufferedWriter(writer);
		   for(long i=1;i<= 5400 ;i++){
			   StringBuffer str = new StringBuffer();
			   String emaildate  = getEmaildate(i);
			   str.append("INSERT INTO `express_ops_cwb_detail` VALUES " );
				for(long j=1+((i-1)*2000); j<= 2000+((i-1)*2000) ;j++){
					if(j != i*2000){
						str.append("("+j+",'cheshi"+j+"',3,'"+emaildate+"','sjrid1','王莉莉','江西省东湖小区1号',0,0,'343700','0796-85896659','18615467898','2000-01-01 00:00:00','cheshi-"+j+"','备注test1',90,10000.00,0.00,1,NULL,'南昌站点一','预约派送_客户要求_test1','zff828-01',2,'北京','航空','江西省','南昌市','南湖区','1','1','1',193,0,'','1',0.240,'家电','187','11*10*10',0.00,1,0,10000.00,'取回商品test1','发出商品test1',3,0,0,0,36,NULL,NULL,'0','2000-01-01 00:00:00','2000-01-01 00:00:00','0',0,'2000-01-01 00:00:00',0,1,0,0,'2015-05-23 12:16:25',0,NULL,'',0,'','',NULL,NULL,NULL,1,'自定义1test1','自定义2test1','自定义3test1','自定义4test1','自定义5test1',0,'','',1,'1',1,NULL,'2015-05-23 04:16:25',193,0,1,0,'',0.0000,NULL,1,0,'',0,0,'',3,'',0,'',0,'',0.00,0.00,NULL,'南昌站点一',0,0,0,0,0.00,0,0,0,NULL,0,0,0,'',0),");
					}else{
						str.append("("+j+",'cheshi"+j+"',3,'"+emaildate+"','sjrid1','王莉莉','江西省东湖小区1号',0,0,'343700','0796-85896659','18615467898','2000-01-01 00:00:00','cheshi-"+j+"','备注test1',90,10000.00,0.00,1,NULL,'南昌站点一','预约派送_客户要求_test1','zff828-01',2,'北京','航空','江西省','南昌市','南湖区','1','1','1',193,0,'','1',0.240,'家电','187','11*10*10',0.00,1,0,10000.00,'取回商品test1','发出商品test1',3,0,0,0,36,NULL,NULL,'0','2000-01-01 00:00:00','2000-01-01 00:00:00','0',0,'2000-01-01 00:00:00',0,1,0,0,'2015-05-23 12:16:25',0,NULL,'',0,'','',NULL,NULL,NULL,1,'自定义1test1','自定义2test1','自定义3test1','自定义4test1','自定义5test1',0,'','',1,'1',1,NULL,'2015-05-23 04:16:25',193,0,1,0,'',0.0000,NULL,1,0,'',0,0,'',3,'',0,'',0,'',0.00,0.00,NULL,'南昌站点一',0,0,0,0,0.00,0,0,0,NULL,0,0,0,'',0);\n");
					}
					
				}
			     bw.write(str.toString());
			     bw.newLine();
				
			}
		   writer.close();
		   System.out.println("完成");
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		return "xiazai/test";
	}

	private String getEmaildate(long k){
		int i = (int)k/30;
		String emaildate = DateTimeUtil.getNeedDate("2014-12-01 00:00:00",i) +" 02:00:00";
		return emaildate;
		
	}
	@RequestMapping("/test1")
	public String add1(Model model) {
		return "xiazai/test1";
	}

	@RequestMapping("/xiazai1")
	public String xiazai(Model model) {
		xiazaiService.test();
		return "xiazai/test";
	}

	@RequestMapping("/tingzhi")
	public String tingzhi(Model model) {
		xiazaiService.zhongduanTest();
		return "xiazai/test1";
	}
}
