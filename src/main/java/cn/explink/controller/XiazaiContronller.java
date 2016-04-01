package cn.explink.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.service.XiazaiService;
import cn.explink.util.DateTimeUtil;

@Controller
@RequestMapping("/xiazai")
public class XiazaiContronller {

	private static Logger logger = LoggerFactory.getLogger(XiazaiContronller.class);
	
	@Autowired
	XiazaiService xiazaiService;

	@RequestMapping("/test")
	public String add(Model model) {
		  logger.info("开始");
		  File f2 = new File("/home/apps/bakdata/ds.sql");
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
		   logger.info("完成");
		  } catch (Exception e) {
			  logger.error("", e);
		  }
		return "xiazai/test";
	}
	@RequestMapping("/test2")
	public String test(Model model) {
		logger.info("开始");
		File f2 = new File("F:/deliverystate.sql");
		//int b=0;
		//INSERT INTO `dmp_42`.`express_ops_delivery_state`(`id`,`cwb`,`deliveryid`,`receivedfee`,`returnedfee`,`businessfee`,`cwbordertypeid`,`deliverystate`,`cash`,`pos`,`posremark`,`mobilepodtime`,`checkfee`,`checkremark`,`receivedfeeuser`,`statisticstate`,`createtime`,`otherfee`,`podremarkid`,`deliverstateremark`,`gcaid`,`isout`,`pos_feedback_flag`,`userid`,`deliverybranchid`,`state`,`sign_typeid`,`sign_man`,`sign_time`,`deliverytime`,`auditingtime`,`customerid`,`payupid`,`issendcustomer`,`isautolinghuo`,`pushtime`,`pushstate`,`pushremarks`,`codpos`,`shouldfare`,`infactfare`,`shangmenlanshoutime`,`firstlevelid`)VALUES (NULL,'zff15001','3','10000.00','0.00','10000.00','1','1','10000.00','0.00','','2015-05-23 13:30:06','0.00','','3','1','2015-05-23 13:11:57','0.00',NULL,'','1','0','0','3','193','1','1','王莉莉','2015-05-23 13:29:56','2015-05-23 13:29:54','2015-05-23 13:30:00','1','0','0','0','','0','','0.00','0.00','0.00','','0'); 
		
		try {
			FileWriter writer = new FileWriter(f2);
			BufferedWriter bw = new BufferedWriter(writer);
			for(long i=1;i<= 1 ;i++){
				StringBuffer str = new StringBuffer();
				String emaildate  = getEmaildate(i);
				str.append("INSERT INTO `express_ops_delivery_state` VALUES " );
				for(long j=10798001; j<= 10799000 ;j++){
					if(j != 10799000 ){
						str.append("("+j+",'cheshi"+j+"','3','10000.00','0.00','10000.00','1','1','10000.00','0.00','','"+emaildate+" 03:30:06','0.00','','3','1','"+emaildate+" 10:30:06','0.00',NULL,'','1','0','0','3','193','1','1','王莉莉','"+emaildate+" 19:30:06','"+emaildate+" 19:30:06','"+emaildate+" 20:30:06','1','0','0','0','','0','','0.00','0.00','0.00','','0'),\n");
					}else{
						str.append("("+j+",'cheshi"+j+"','3','10000.00','0.00','10000.00','1','1','10000.00','0.00','','"+emaildate+" 03:30:06','0.00','','3','1','"+emaildate+" 10:30:06','0.00',NULL,'','1','0','0','3','193','1','1','王莉莉','"+emaildate+" 19:30:06','"+emaildate+" 19:30:06','"+emaildate+" 20:30:06','1','0','0','0','','0','','0.00','0.00','0.00','','0');\n");
					}
					
				}
				bw.write(str.toString());
				bw.newLine();
				
			}
			writer.close();
			logger.info("完成");
		} catch (Exception e) {
			logger.error("", e);
		}
		return "xiazai/test";
	}
	@RequestMapping("/test4")
	public String test4(Model model) {
		logger.info("开始");
		File f2 = new File("/home/apps/bakdata/cash.sql");
		//int b=0;
		// INSERT INTO `express_ops_deliver_cash`(`id`,`deliverybranchid`,`deliverid`,`linghuotime`,`fankuitime`,`guibantime`,`deliverystate`,`cwb`,`customerid`,`receivablePosfee`,`receivableNoPosfee`,`paybackfee`,`deliverystateid`,`gcaid`,`state`)VALUES(NULL,'193','3','2015-05-23 13:11:57','2015-05-23 13:29:54','2015-05-23 13:30:00','1','zff15001','1','0.00','10000.00','0.00','1','1','1');  
		
		try {
			FileWriter writer = new FileWriter(f2);
			BufferedWriter bw = new BufferedWriter(writer);
			 for(long i=1;i<= 5401 ;i++){
				   StringBuffer str = new StringBuffer();
				   String emaildate  = getEmaildate(i);
				   str.append("INSERT INTO `express_ops_deliver_cash` VALUES " );
					for(long j=1+((i-1)*2000); j<= 2000+((i-1)*2000) ;j++){
						if(j != i*2000){
						str.append("("+j+",'193','3','"+emaildate+" 03:30:06','"+emaildate+" 19:30:06','"+emaildate+" 20:30:06','1','cheshi"+j+"','1','0.00','10000.00','0.00','1','1','1'),");
					}else{
						str.append("("+j+",'193','3','"+emaildate+" 03:30:06','"+emaildate+" 19:30:06','"+emaildate+" 20:30:06','1','cheshi"+j+"','1','0.00','10000.00','0.00','1','1','1');\n");
					}
					
				}
				bw.write(str.toString());
				bw.newLine();
				
			}
			writer.close();
			logger.info("完成");
		} catch (Exception e) {
			logger.error("", e);
		}
		return "xiazai/test";
	}
	
	public static void main(String[] args) {
		StringBuffer str = new StringBuffer();
		String emaildate  = "2015-05-31";
		str.append("INSERT INTO `express_ops_delivery_state` VALUES " );
		for(long j=10799802; j<= 10800000 ;j++){
			if(j != 10800000 ){
				str.append("("+j+",'cheshi"+j+"','3','10000.00','0.00','10000.00','1','1','10000.00','0.00','','"+emaildate+" 03:30:06','0.00','','3','1','"+emaildate+" 10:30:06','0.00',NULL,'','1','0','0','3','193','1','1','王莉莉','"+emaildate+" 19:30:06','"+emaildate+" 19:30:06','"+emaildate+" 20:30:06','1','0','0','0','','0','','0.00','0.00','0.00','','0'),\n");
			}else{
				str.append("("+j+",'cheshi"+j+"','3','10000.00','0.00','10000.00','1','1','10000.00','0.00','','"+emaildate+" 03:30:06','0.00','','3','1','"+emaildate+" 10:30:06','0.00',NULL,'','1','0','0','3','193','1','1','王莉莉','"+emaildate+" 19:30:06','"+emaildate+" 19:30:06','"+emaildate+" 20:30:06','1','0','0','0','','0','','0.00','0.00','0.00','','0');\n");
			}
			
		}
		logger.info("str=" + str.toString());
	}
	@RequestMapping("/test3")
	public String test3(Model model) {
		logger.info("开始");
		File f2 = new File("/home/apps/bakdata/flow.sql");
		//int b=0;
		//INSERT INTO `express_ops_order_flow`(`floworderid`,`cwb`,`branchid`,`credate`,`userid`,`floworderdetail`,`flowordertype`,`isnow`,`outwarehouseid`,`comment`)VALUES(NULL,'zff15001','193','2015-05-23 13:30:06','3','{\"cwbOrder\":{\"opscwbid\":1,\"startbranchid\":193,\"currentbranchid\":0,\"nextbranchid\":0,\"deliverybranchid\":193,\"backtocustomer_awb\":\"\",\"cwbflowflag\":\"1\",\"carrealweight\":0.240,\"cartype\":\"家电\",\"carwarehouse\":\"187\",\"carsize\":\"11*10*10\",\"backcaramount\":0.00,\"sendcarnum\":1,\"backcarnum\":0,\"caramount\":10000.00,\"backcarname\":\"取回商品test1\",\"sendcarname\":\"发出商品test1\",\"deliverid\":3,\"deliverystate\":1,\"emailfinishflag\":0,\"reacherrorflag\":0,\"orderflowid\":0,\"flowordertype\":36,\"cwbreachbranchid\":0,\"cwbreachdeliverbranchid\":0,\"podfeetoheadflag\":\"0\",\"podfeetoheadtime\":null,\"podfeetoheadchecktime\":null,\"podfeetoheadcheckflag\":\"0\",\"leavedreasonid\":0,\"deliversubscribeday\":null,\"customerwarehouseid\":\"1\",\"emaildateid\":3,\"emaildate\":\"2015-05-23 09:09:08\",\"serviceareaid\":90,\"customerid\":1,\"shipcwb\":\"zff828-01\",\"consigneeno\":\"sjrid1\",\"consigneename\":\"王莉莉\",\"consigneeaddress\":\"江西省东湖小区1号\",\"consigneepostcode\":\"343700\",\"consigneephone\":\"0796-85896659\",\"cwbremark\":\"备注test1\",\"customercommand\":\"预约派送_客户要求_test1\",\"transway\":\"航空\",\"cwbprovince\":\"江西省\",\"cwbcity\":\"南昌市\",\"cwbcounty\":\"南湖区\",\"receivablefee\":10000.00,\"paybackfee\":0.00,\"cwb\":\"zff15001\",\"shipperid\":0,\"cwbordertypeid\":1,\"consigneemobile\":\"18615467898\",\"transcwb\":\"zff828-01\",\"destination\":\"北京\",\"cwbdelivertypeid\":\"1\",\"exceldeliver\":\"\",\"excelbranch\":\"南昌站点一\",\"excelimportuserid\":2,\"state\":1,\"printtime\":\"2015-05-23 12:16:25\",\"commonid\":0,\"commoncwb\":\"\",\"signtypeid\":0,\"podrealname\":\"\",\"podtime\":\"\",\"podsignremark\":\"\",\"modelname\":null,\"scannum\":1,\"isaudit\":0,\"backreason\":\"\",\"leavedreason\":\"\",\"paywayid\":1,\"newpaywayid\":\"1\",\"tuihuoid\":0,\"cwbstate\":1,\"remark1\":\"自定义1test1\",\"remark2\":\"自定义2test1\",\"remark3\":\"自定义3test1\",\"remark4\":\"自定义4test1\",\"remark5\":\"自定义5test1\",\"backreasonid\":0,\"multi_shipcwb\":null,\"packagecode\":\"\",\"backreturnreasonid\":0,\"backreturnreason\":\"\",\"handleresult\":0,\"handleperson\":0,\"handlereason\":\"\",\"addresscodeedittype\":3,\"resendtime\":\"\",\"weishuakareasonid\":0,\"weishuakareason\":\"\",\"losereasonid\":0,\"losereason\":\"\",\"fankuitime\":null,\"shenhetime\":null,\"chuzhantime\":null,\"shouldfare\":0.00,\"infactfare\":0.00,\"timelimited\":\"\",\"historybranchname\":\"南昌站点一\",\"goodsType\":0,\"outareaflag\":0,\"consigneenameOfkf\":\"王莉莉\",\"consigneemobileOfkf\":\"18615467898\",\"consigneephoneOfkf\":\"0796-85896659\",\"zhongzhuanreasonid\":0,\"zhongzhuanreason\":\"\",\"fnorgoffset\":0.00,\"fnorgoffsetflag\":0,\"firstlevelid\":0,\"changereason\":null,\"firstchangereasonid\":0,\"changereasonid\":0},\"deliveryState\":{\"id\":1,\"cwb\":\"zff15001\",\"deliveryid\":3,\"receivedfee\":10000.00,\"returnedfee\":0.00,\"businessfee\":10000.00,\"deliverystate\":1,\"cash\":10000.00,\"pos\":0.00,\"posremark\":\"\",\"mobilepodtime\":1432359006000,\"checkfee\":0.00,\"checkremark\":\"\",\"receivedfeeuser\":3,\"createtime\":\"2015-05-23 13:11:57\",\"otherfee\":0.00,\"podremarkid\":0,\"deliverstateremark\":\"\",\"isout\":0,\"pos_feedback_flag\":0,\"userid\":3,\"gcaid\":1,\"sign_typeid\":1,\"sign_man\":\"王莉莉\",\"sign_time\":\"2015-05-23 13:29:56\",\"backreason\":null,\"leavedreason\":null,\"deliverybranchid\":193,\"deliverystateStr\":null,\"deliverytime\":\"2015-05-23 13:29:54\",\"auditingtime\":\"2015-05-23 13:30:00\",\"pushtime\":\"\",\"pushstate\":0,\"pushremarks\":\"\",\"shouldfare\":0.00,\"infactfare\":0.00,\"customerid\":1,\"payupid\":0,\"issendcustomer\":0,\"isautolinghuo\":0,\"codpos\":0.00,\"shangmenlanshoutime\":\"\"},\"error\":null}','36','1','0',''); 
		try {
			FileWriter writer = new FileWriter(f2);
			BufferedWriter bw = new BufferedWriter(writer);
			for(long i=1;i<= 5401 ;i++){
				StringBuffer str = new StringBuffer();
				String emaildate  = getEmaildate(i);
				str.append("INSERT INTO `express_ops_order_flow` VALUES " );
				for(long j=1+((i-1)*2000); j<= 2000+((i-1)*2000) ;j++){
					if(j != i*2000){
						str.append("("+j+",'cheshi"+j+"','193','"+emaildate+" 20:30:00','3','{\"cwbOrder\":{\"opscwbid\":"+j+",\"startbranchid\":193,\"currentbranchid\":0,\"nextbranchid\":0,\"deliverybranchid\":193,\"backtocustomer_awb\":\"\",\"cwbflowflag\":\"1\",\"carrealweight\":0.240,\"cartype\":\"家电\",\"carwarehouse\":\"187\",\"carsize\":\"11*10*10\",\"backcaramount\":0.00,\"sendcarnum\":1,\"backcarnum\":0,\"caramount\":10000.00,\"backcarname\":\"取回商品test1\",\"sendcarname\":\"发出商品test1\",\"deliverid\":3,\"deliverystate\":1,\"emailfinishflag\":0,\"reacherrorflag\":0,\"orderflowid\":0,\"flowordertype\":36,\"cwbreachbranchid\":0,\"cwbreachdeliverbranchid\":0,\"podfeetoheadflag\":\"0\",\"podfeetoheadtime\":null,\"podfeetoheadchecktime\":null,\"podfeetoheadcheckflag\":\"0\",\"leavedreasonid\":0,\"deliversubscribeday\":null,\"customerwarehouseid\":\"1\",\"emaildateid\":3,\"emaildate\":\"2015-05-23 09:09:08\",\"serviceareaid\":90,\"customerid\":1,\"shipcwb\":\"cheshi-"+j+"\",\"consigneeno\":\"sjrid1\",\"consigneename\":\"王莉莉\",\"consigneeaddress\":\"江西省东湖小区1号\",\"consigneepostcode\":\"343700\",\"consigneephone\":\"0796-85896659\",\"cwbremark\":\"备注test1\",\"customercommand\":\"预约派送_客户要求_test1\",\"transway\":\"航空\",\"cwbprovince\":\"江西省\",\"cwbcity\":\"南昌市\",\"cwbcounty\":\"南湖区\",\"receivablefee\":10000.00,\"paybackfee\":0.00,\"cwb\":\"cheshi"+j+"\",\"shipperid\":0,\"cwbordertypeid\":1,\"consigneemobile\":\"18615467898\",\"transcwb\":\"cheshi-"+j+"\",\"destination\":\"北京\",\"cwbdelivertypeid\":\"1\",\"exceldeliver\":\"\",\"excelbranch\":\"南昌站点一\",\"excelimportuserid\":2,\"state\":1,\"printtime\":\"2015-05-23 12:16:25\",\"commonid\":0,\"commoncwb\":\"\",\"signtypeid\":0,\"podrealname\":\"\",\"podtime\":\"\",\"podsignremark\":\"\",\"modelname\":null,\"scannum\":1,\"isaudit\":0,\"backreason\":\"\",\"leavedreason\":\"\",\"paywayid\":1,\"newpaywayid\":\"1\",\"tuihuoid\":0,\"cwbstate\":1,\"remark1\":\"自定义1test1\",\"remark2\":\"自定义2test1\",\"remark3\":\"自定义3test1\",\"remark4\":\"自定义4test1\",\"remark5\":\"自定义5test1\",\"backreasonid\":0,\"multi_shipcwb\":null,\"packagecode\":\"\",\"backreturnreasonid\":0,\"backreturnreason\":\"\",\"handleresult\":0,\"handleperson\":0,\"handlereason\":\"\",\"addresscodeedittype\":3,\"resendtime\":\"\",\"weishuakareasonid\":0,\"weishuakareason\":\"\",\"losereasonid\":0,\"losereason\":\"\",\"fankuitime\":null,\"shenhetime\":null,\"chuzhantime\":null,\"shouldfare\":0.00,\"infactfare\":0.00,\"timelimited\":\"\",\"historybranchname\":\"南昌站点一\",\"goodsType\":0,\"outareaflag\":0,\"consigneenameOfkf\":\"王莉莉\",\"consigneemobileOfkf\":\"18615467898\",\"consigneephoneOfkf\":\"0796-85896659\",\"zhongzhuanreasonid\":0,\"zhongzhuanreason\":\"\",\"fnorgoffset\":0.00,\"fnorgoffsetflag\":0,\"firstlevelid\":0,\"changereason\":null,\"firstchangereasonid\":0,\"changereasonid\":0},\"deliveryState\":{\"id\":"+j+",\"cwb\":\"cheshi"+j+"\",\"deliveryid\":3,\"receivedfee\":10000.00,\"returnedfee\":0.00,\"businessfee\":10000.00,\"deliverystate\":1,\"cash\":10000.00,\"pos\":0.00,\"posremark\":\"\",\"mobilepodtime\":1432359006000,\"checkfee\":0.00,\"checkremark\":\"\",\"receivedfeeuser\":3,\"createtime\":\""+emaildate+" 03:30:06\",\"otherfee\":0.00,\"podremarkid\":0,\"deliverstateremark\":\"\",\"isout\":0,\"pos_feedback_flag\":0,\"userid\":3,\"gcaid\":1,\"sign_typeid\":1,\"sign_man\":\"王莉莉\",\"sign_time\":\""+emaildate+" 10:30:06\",\"backreason\":null,\"leavedreason\":null,\"deliverybranchid\":193,\"deliverystateStr\":null,\"deliverytime\":\""+emaildate+" 10:30:06\",\"auditingtime\":\""+emaildate+" 19:30:06\",\"pushtime\":\"\",\"pushstate\":0,\"pushremarks\":\"\",\"shouldfare\":0.00,\"infactfare\":0.00,\"customerid\":1,\"payupid\":0,\"issendcustomer\":0,\"isautolinghuo\":0,\"codpos\":0.00,\"shangmenlanshoutime\":\"\"},\"error\":null}','36','1','0',''),");
					}else{
						str.append("("+j+",'cheshi"+j+"','193','"+emaildate+" 20:30:00','3','{\"cwbOrder\":{\"opscwbid\":"+j+",\"startbranchid\":193,\"currentbranchid\":0,\"nextbranchid\":0,\"deliverybranchid\":193,\"backtocustomer_awb\":\"\",\"cwbflowflag\":\"1\",\"carrealweight\":0.240,\"cartype\":\"家电\",\"carwarehouse\":\"187\",\"carsize\":\"11*10*10\",\"backcaramount\":0.00,\"sendcarnum\":1,\"backcarnum\":0,\"caramount\":10000.00,\"backcarname\":\"取回商品test1\",\"sendcarname\":\"发出商品test1\",\"deliverid\":3,\"deliverystate\":1,\"emailfinishflag\":0,\"reacherrorflag\":0,\"orderflowid\":0,\"flowordertype\":36,\"cwbreachbranchid\":0,\"cwbreachdeliverbranchid\":0,\"podfeetoheadflag\":\"0\",\"podfeetoheadtime\":null,\"podfeetoheadchecktime\":null,\"podfeetoheadcheckflag\":\"0\",\"leavedreasonid\":0,\"deliversubscribeday\":null,\"customerwarehouseid\":\"1\",\"emaildateid\":3,\"emaildate\":\"2015-05-23 09:09:08\",\"serviceareaid\":90,\"customerid\":1,\"shipcwb\":\"cheshi-"+j+"\",\"consigneeno\":\"sjrid1\",\"consigneename\":\"王莉莉\",\"consigneeaddress\":\"江西省东湖小区1号\",\"consigneepostcode\":\"343700\",\"consigneephone\":\"0796-85896659\",\"cwbremark\":\"备注test1\",\"customercommand\":\"预约派送_客户要求_test1\",\"transway\":\"航空\",\"cwbprovince\":\"江西省\",\"cwbcity\":\"南昌市\",\"cwbcounty\":\"南湖区\",\"receivablefee\":10000.00,\"paybackfee\":0.00,\"cwb\":\"cheshi"+j+"\",\"shipperid\":0,\"cwbordertypeid\":1,\"consigneemobile\":\"18615467898\",\"transcwb\":\"cheshi-"+j+"\",\"destination\":\"北京\",\"cwbdelivertypeid\":\"1\",\"exceldeliver\":\"\",\"excelbranch\":\"南昌站点一\",\"excelimportuserid\":2,\"state\":1,\"printtime\":\"2015-05-23 12:16:25\",\"commonid\":0,\"commoncwb\":\"\",\"signtypeid\":0,\"podrealname\":\"\",\"podtime\":\"\",\"podsignremark\":\"\",\"modelname\":null,\"scannum\":1,\"isaudit\":0,\"backreason\":\"\",\"leavedreason\":\"\",\"paywayid\":1,\"newpaywayid\":\"1\",\"tuihuoid\":0,\"cwbstate\":1,\"remark1\":\"自定义1test1\",\"remark2\":\"自定义2test1\",\"remark3\":\"自定义3test1\",\"remark4\":\"自定义4test1\",\"remark5\":\"自定义5test1\",\"backreasonid\":0,\"multi_shipcwb\":null,\"packagecode\":\"\",\"backreturnreasonid\":0,\"backreturnreason\":\"\",\"handleresult\":0,\"handleperson\":0,\"handlereason\":\"\",\"addresscodeedittype\":3,\"resendtime\":\"\",\"weishuakareasonid\":0,\"weishuakareason\":\"\",\"losereasonid\":0,\"losereason\":\"\",\"fankuitime\":null,\"shenhetime\":null,\"chuzhantime\":null,\"shouldfare\":0.00,\"infactfare\":0.00,\"timelimited\":\"\",\"historybranchname\":\"南昌站点一\",\"goodsType\":0,\"outareaflag\":0,\"consigneenameOfkf\":\"王莉莉\",\"consigneemobileOfkf\":\"18615467898\",\"consigneephoneOfkf\":\"0796-85896659\",\"zhongzhuanreasonid\":0,\"zhongzhuanreason\":\"\",\"fnorgoffset\":0.00,\"fnorgoffsetflag\":0,\"firstlevelid\":0,\"changereason\":null,\"firstchangereasonid\":0,\"changereasonid\":0},\"deliveryState\":{\"id\":"+j+",\"cwb\":\"cheshi"+j+"\",\"deliveryid\":3,\"receivedfee\":10000.00,\"returnedfee\":0.00,\"businessfee\":10000.00,\"deliverystate\":1,\"cash\":10000.00,\"pos\":0.00,\"posremark\":\"\",\"mobilepodtime\":1432359006000,\"checkfee\":0.00,\"checkremark\":\"\",\"receivedfeeuser\":3,\"createtime\":\""+emaildate+" 03:30:06\",\"otherfee\":0.00,\"podremarkid\":0,\"deliverstateremark\":\"\",\"isout\":0,\"pos_feedback_flag\":0,\"userid\":3,\"gcaid\":1,\"sign_typeid\":1,\"sign_man\":\"王莉莉\",\"sign_time\":\""+emaildate+" 10:30:06\",\"backreason\":null,\"leavedreason\":null,\"deliverybranchid\":193,\"deliverystateStr\":null,\"deliverytime\":\""+emaildate+" 10:30:06\",\"auditingtime\":\""+emaildate+" 19:30:06\",\"pushtime\":\"\",\"pushstate\":0,\"pushremarks\":\"\",\"shouldfare\":0.00,\"infactfare\":0.00,\"customerid\":1,\"payupid\":0,\"issendcustomer\":0,\"isautolinghuo\":0,\"codpos\":0.00,\"shangmenlanshoutime\":\"\"},\"error\":null}','36','1','0','');\n");
					}
					
				}
				bw.write(str.toString());
				bw.newLine();
				
			}
			writer.close();
			logger.info("完成");
		} catch (Exception e) {
			logger.error("", e);
		}
		return "xiazai/test";
	}

	private String getEmaildate(long k){
		int i = (int)k/30;
		String emaildate = DateTimeUtil.getNeedDate("2014-12-01 00:00:00",i) +"";
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
