package cn.explink.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.AbnormalOrderDAO;
import cn.explink.dao.AbnormalWriteBackDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.GroupDetailDao;
import cn.explink.dao.OperationTimeDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.OutWarehouseGroupDAO;
import cn.explink.dao.ShangMenTuiCwbDetailDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.GroupDetail;
import cn.explink.domain.ShangMenTuiCwbDetail;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.service.UserService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.StreamingStatementCreator;
import cn.explink.util.StringUtil;
import cn.explink.util.baiduAPI.GeoCoder;
import cn.explink.util.baiduAPI.GeoPoint;
import cn.explink.util.baiduAPI.ReGeoCoderResult;

@Controller
@RequestMapping("/changeLog")
public class ChangeLogContronller {

	private static Logger logger = LoggerFactory.getLogger(ChangeLogContronller.class);
	
	@Autowired
	UserService userService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	OutWarehouseGroupDAO outwarehousegroupDao;
	@Autowired
	GroupDetailDao groupDetailDao;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ShangMenTuiCwbDetailDAO shangMenTuiCwbDetailDAO;

	@Autowired
	OperationTimeDAO operationTimeDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;

	@Autowired
	AbnormalOrderDAO abnormalOrderDAO;
	@Autowired
	AbnormalWriteBackDAO abnormalWriteBackDAO;

	private ObjectMapper om = new ObjectMapper();

	public static boolean changeCheck = true;

	@RequestMapping("/stop")
	public String stop(Model model) {
		GeoPoint position = GeoCoder.getInstance().getGeoCoder().GetLocationDetails("海南海口市龙华区南沙路61号海南电视台住宅区5栋302");
		logger.info("纬度值 = " + position.getLat());
		logger.info("经度值 = " + position.getLng());
		ReGeoCoderResult reG = GeoCoder.getInstance().getGeoCoder().GetAddress(position.getLng(),position.getLat());
		logger.info(reG.getAddressComponent().getCity());
		logger.info(reG.getAddressComponent().getDistrict());
		model.addAttribute("msg", "迁移数据,已停止");
		return "/changeLog/tuotouChange";
	}

	@RequestMapping("/floworderDataChangestop")
	public String floworderDataChangestop(Model model) {
		changeCheck = false;
		model.addAttribute("msg", "迁移数据,已停止");
		return "/changeLog/floworderChange";
	}

	@RequestMapping("/accountNeedDataChangestop")
	public String accountNeedDataChangestop(Model model) {
		changeCheck = false;
		model.addAttribute("msg", "迁移数据,已停止");
		return "/changeLog/accountNeedChange";
	}
	@RequestMapping("/changeFlow")
	public String changeFlow(Model model) {
		logger.info("开始");
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
		   logger.info("完成");
		  } catch (Exception e) {
			  logger.error("",e);
		  }
		return "/changeLog/tuotouChange";
	}
	
	private String getEmaildate(long k){
		int i = (int)k/30;
		String emaildate = DateTimeUtil.getNeedDate("2014-12-01 00:00:00",i) +" 02:00:00";
		return emaildate;
		
	}
	
	public static void main(String[] args) {
		int i = (int)5400/30;
		String emaildate = DateTimeUtil.getNeedDate("2014-12-01 00:00:00",i) +" 00:00:00";
		logger.info("emaildate = " + emaildate);
		
	}
	@RequestMapping("/changeFlowChange")
	public String changeFlowChange(Model model,HttpServletRequest request) {
		long sid = Long.parseLong(request.getParameter("sid")) ;
		long eid = Long.parseLong(request.getParameter("eid")) ;
		
		String sql ="INSERT INTO `express_ops_order_flow_log`(floworderid,`cwb`, `branchid`, `credate`,  `userid`, `floworderdetail`, `flowordertype`, `isnow`, `outwarehouseid`,`comment`) " +
				"SELECT floworderid,`cwb`, `branchid`, `credate`,  `userid`,`floworderdetail`, `flowordertype`, `isnow`,`outwarehouseid`, `comment` FROM `express_ops_order_flow` where floworderid >="+sid+" and floworderid <= "+eid+" ";
		
		jdbcTemplate.update(sql);
		return "/changeLog/tuotouChange";
	}

	/**
	 * 迁移出库统计
	 * 
	 * @param model
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping("/outWareHouse")
    public String add(Model model,
	    @RequestParam(value="startTime",defaultValue="",required=false)String startTime,
	    @RequestParam(value="endTime",defaultValue="",required=false)String endTime) {
	String msg="";
	if(startTime.length()>0 && endTime.length()>0){
	    List<EmailDate> emaildateList =  emailDateDAO.getEmailDateByEmaildate(startTime, endTime);
	    logger.info("开始迁移数据,emaildate时间从：{}到{}",startTime,endTime);
	    if(emaildateList != null && emaildateList.size()>0){
		for(EmailDate e:emaildateList){
		    logger.info("迁移数据,时间从：{}到{},emaildateid个数："+emaildateList.size()+",开始第"+(emaildateList.indexOf(e)+1)+"个,emaildateid:"+e.getEmaildateid(),startTime,endTime);
		    String sql ="SELECT of.* FROM `express_ops_order_flow`  WHERE  isnow =1 ";
		    jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs) throws SQLException {
			    try {
			    	String flowDetail = rs.getString("floworderdetail");
			    	CwbOrderWithDeliveryState cwbOrderWithDeliveryState=om.readValue(flowDetail, CwbOrderWithDeliveryState.class);
					final CwbOrder cwbOrderDTO = cwbOrderWithDeliveryState.getCwbOrder();
					
					jdbcTemplate.update(
							"update `express_ops_cwb_detail` set ", new PreparedStatementSetter() {

								@Override
								public void setValues(PreparedStatement ps) throws SQLException {

									
								}

							});
			    	
			    	
			    
			       } catch (Exception e) {
			    	   logger.error("",e);
			       }
			}});
		    logger.info("迁移数据,时间从：{}到{},emaildateid个数："+emaildateList.size()+",完成第"+(emaildateList.indexOf(e)+1)+"个,emaildateid:"+e.getEmaildateid(),startTime,endTime);
		}
	    }
	    msg +="迁移完成了发货时间："+startTime+"到"+endTime+"的数据";
	}
	model.addAttribute("msg", msg);
	return "/changeLog/outWareHouse";
    }

	@RequestMapping("/tuotouChange")
	public String tuotouChange(Model model, @RequestParam(value = "startTimeid", defaultValue = "0", required = false) long startTimeid,
			@RequestParam(value = "endTimeid", defaultValue = "0", required = false) long endTimeid, @RequestParam(value = "url", defaultValue = "", required = false) String omsurl,
			@RequestParam(value = "functionid", defaultValue = "0", required = false) long functionidParam, @RequestParam(value = "issycTime", defaultValue = "0", required = false) long issycTime,
			HttpServletRequest request) {
		final String url = omsurl;
		final long functionid = functionidParam;
		final long issycTimesign = issycTime;

		String msg = "";
		if (startTimeid > 0 || endTimeid > 0) {
			changeCheck = true;
			if (startTimeid > endTimeid) {
				model.addAttribute("msg", "开始id不能大于结束id");
				return "/changeLog/tuotouChange";
			}
			try {
				String Str = JSONReslutUtil.getResultMessageChangeLog(url + "/OMSChange/pushBranchMap", "a=1", "POST").toString();
				logger.info("保存branchMap返回：{}", Str);
				if (Str == null || Str.equals("")) {
					model.addAttribute("msg", "请求站点异常");
					return "/changeLog/tuotouChange";
				} else if (Str.indexOf("01") > -1) {
					model.addAttribute("msg", "站点列表为空");
					return "/changeLog/tuotouChange";
				}
			} catch (IOException e1) {
				logger.error("",e1);
			}
			long emaildatemaxid = emailDateDAO.getEmailDateMaxid();

			logger.info("开始迁移数据,emaildateid从：{}到{},最大emaildateid是:" + emaildatemaxid, startTimeid, endTimeid);
			if (emaildatemaxid == 0) {
				model.addAttribute("msg", "emaildateid为空");
				return "/changeLog/tuotouChange";
			}
			if (emaildatemaxid < endTimeid) {
				endTimeid = emaildatemaxid;
			}

			List<EmailDate> emaildateidList = emailDateDAO.getEmailDateByEmaildateid(startTimeid, endTimeid);
			if (emaildateidList != null && emaildateidList.size() > 0) {
				for (EmailDate e : emaildateidList) {
					if (!changeCheck) {
						logger.info("迁移数据,已停止,要开始的emaildateid:{}", e.getEmaildateid());
						model.addAttribute("msg", "迁移数据,已停止,将要开始的emaildateid:" + e.getEmaildateid());
						return "/changeLog/tuotouChange";
					}
					logger.info("迁移数据,emaildateid从：{}到{},emaildateid个数：" + emaildateidList.size() + ",开始第" + (emaildateidList.indexOf(e) + 1) + "个,emaildateid:" + e.getEmaildateid(), startTimeid,
							endTimeid);
					String sql = "SELECT of.* FROM `express_ops_order_flow` AS of LEFT JOIN `express_ops_cwb_detail` AS de ON of.cwb=de.cwb  WHERE " + "de.emaildateid =" + e.getEmaildateid()
							+ " AND de.state=1 ";
					jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
						@Override
						public void processRow(ResultSet rs) throws SQLException {
							try {
								OrderFlow orderFlow = new OrderFlow();
								orderFlow.setFloworderid(rs.getLong("floworderid"));
								String cwb = rs.getString("cwb");
								orderFlow.setCwb(cwb);
								orderFlow.setBranchid(rs.getLong("branchid"));
								orderFlow.setCredate(rs.getTimestamp("credate"));
								orderFlow.setUserid(rs.getLong("userid"));
								String flowDetail = rs.getString("floworderdetail").replaceAll("&", "").replaceAll("=", "").replaceAll("#", "");
								orderFlow.setFloworderdetail(flowDetail);
								orderFlow.setFlowordertype(rs.getInt("flowordertype"));
								orderFlow.setIsnow(rs.getInt("isnow"));
								orderFlow.setComment(rs.getString("comment"));
								try {
									JSONReslutUtil.getResultMessageChangeLog(url + "/OMSChange/tuotou",
											"pram=" + om.writeValueAsString(orderFlow) + "&functionid=" + functionid + "&issycTime=" + issycTimesign, "POST").toString();
								} catch (Exception e) {
									logger.error("获取当前用户信息异常", e);
								}
							} catch (Exception e) {
								logger.error("",e);
							}
						}
					});
					logger.info("迁移数据,时间从：{}到{},emaildateid个数：" + emaildateidList.size() + ",完成第" + (emaildateidList.indexOf(e) + 1) + "个,emaildateid:" + e.getEmaildateid(), startTimeid, endTimeid);
				}
			}
			msg += "迁移完成了emaildateid：" + startTimeid + "到" + endTimeid + "的数据";
		}
		model.addAttribute("msg", msg);
		return "/changeLog/tuotouChange";
	}

	@RequestMapping("/tuotouChangeBycwbs")
	public String tuotouChangeBycwbs(Model model, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "url", defaultValue = "", required = false) String omsurl, @RequestParam(value = "functionid", defaultValue = "0", required = false) long functionidParam,
			@RequestParam(value = "issycTime", defaultValue = "0", required = false) long issycTime, HttpServletRequest request) {
		final String url = omsurl;
		final long functionid = functionidParam;
		final long issycTimesign = issycTime;
		String msg = "";
		if (cwbs.length() > 0) {
			changeCheck = true;
			try {
				String Str = JSONReslutUtil.getResultMessageChangeLog(url + "/OMSChange/pushBranchMap", "a=1", "POST").toString();
				logger.info("保存branchMap返回：{}", Str);
				if (Str == null || Str.equals("")) {
					model.addAttribute("msg", "请求站点异常");
					return "/changeLog/tuotouChange";
				} else if (Str.indexOf("01") > -1) {
					model.addAttribute("msg", "站点列表为空");
					return "/changeLog/tuotouChange";
				}
			} catch (IOException e1) {
				logger.error("",e1);
			}
			String[] cwbss = cwbs.trim().split("\r\n");
			String cwbStr = "";
			for (String str : cwbss) {
				cwbStr += "'" + str + "',";
			}
			cwbStr = cwbStr.substring(0, cwbStr.length() - 1);
			String sql = "SELECT of.* FROM `express_ops_order_flow` AS of LEFT JOIN `express_ops_cwb_detail` AS de ON of.cwb=de.cwb  WHERE " + "de.cwb in(" + cwbStr + ") AND de.state=1  ";
			jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					try {
						OrderFlow orderFlow = new OrderFlow();
						orderFlow.setFloworderid(rs.getLong("floworderid"));
						String cwb = rs.getString("cwb");
						orderFlow.setCwb(cwb);
						orderFlow.setBranchid(rs.getLong("branchid"));
						orderFlow.setCredate(rs.getTimestamp("credate"));
						orderFlow.setUserid(rs.getLong("userid"));
						String flowDetail = rs.getString("floworderdetail").replaceAll("&", "").replaceAll("=", "").replaceAll("#", "");
						orderFlow.setFloworderdetail(flowDetail);
						orderFlow.setFlowordertype(rs.getInt("flowordertype"));
						orderFlow.setIsnow(rs.getInt("isnow"));
						orderFlow.setComment(rs.getString("comment"));
						try {
							JSONReslutUtil.getResultMessageChangeLog(url + "/OMSChange/tuotou",
									"pram=" + om.writeValueAsString(orderFlow) + "&functionid=" + functionid + "&issycTime=" + issycTimesign, "POST").toString();
							// logger.info("{}返回：{}",cwb,userStr);
						} catch (Exception e) {
							logger.error("获取当前用户信息异常", e);
						}
					} catch (Exception e) {
						logger.error("",e);
					}
				}
			});
		}
		model.addAttribute("msg", msg);
		return "/changeLog/tuotouChange";
	}

	/**
	 * 迁移问题件
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/abnormal")
	public String abnormal(Model model, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		String msg = "";
		if (isshow != 0) {
			try {
				jdbcTemplate.query(new StreamingStatementCreator("SELECT * FROM express_ops_abnormal_order WHERE isnow=1 ORDER BY credatetime"), new RowCallbackHandler() {
					@Override
					public void processRow(ResultSet rs) throws SQLException {
						long abnormalorderid = rs.getLong("id");
						long abnormalordertypeid = rs.getLong("abnormaltypeid");
						long opscwbid = rs.getLong("opscwbid");
						abnormalWriteBackDAO.updateForQianyi(opscwbid, abnormalorderid, abnormalordertypeid);

					}
				});
				msg = "数据迁移完成";
				logger.info("开始迁移数据 >> 问题件");
			} catch (Exception e) {
				logger.error("问题件数据迁移出错");
			}
		}
		model.addAttribute("msg", msg);
		return "/changeLog/abnormal";
	}

	@RequestMapping("/outWareHouseBycwb")
	public String outWareHouseBycwb(Model model, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs) {
		String msg = "";
		if (cwbs.length() > 0) {
			String[] cwbss = cwbs.trim().split("\r\n");
			String cwbStr = "";
			for (String str : cwbss) {
				cwbStr += "'" + str + "',";

			}
			cwbStr = cwbStr.substring(0, cwbStr.length() - 1);
			String sql = "SELECT of.* FROM `express_ops_order_flow` AS of LEFT JOIN `express_ops_cwb_detail` AS de ON of.cwb=de.cwb  WHERE " + "de.cwb in(" + cwbStr
					+ ") AND de.state=1 AND of.flowordertype =" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + " ";
			jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					try {
						// {"cwbOrder":{"opscwbid":21810,"startbranchid":0,"currentbranchid":0,"nextbranchid":189,"deliverybranchid":192,"backtocustomer_awb":"","cwbflowflag":"1","carrealweight":25.000,"cartype":"家电","carwarehouse":"189","carsize":"11*10*10","backcaramount":0.00,"sendcarnum":1,"backcarnum":0,"caramount":11111.11,"backcarname":"取回商品T1","sendcarname":"发出商品T1","deliverid":0,"deliverystate":0,"emailfinishflag":0,"reacherrorflag":0,"orderflowid":0,"flowordertype":1,"cwbreachbranchid":0,"cwbreachdeliverbranchid":0,"podfeetoheadflag":"0","podfeetoheadtime":null,"podfeetoheadchecktime":null,"podfeetoheadcheckflag":"0","leavedreasonid":0,"deliversubscribeday":null,"customerwarehouseid":"4","emaildateid":258,"emaildate":"2013-07-09 09:50:54","serviceareaid":4,"customerid":126,"shipcwb":"LG123456789","consigneeno":"CustomerID1","consigneename":"王旸","consigneeaddress":"中国北京北京市朝阳区,建国门外大街甲12号新华保险大厦10层","consigneepostcode":"100022","consigneephone":"15901169883","cwbremark":"备注T1","customercommand":"客户要求T1","transway":"航空","cwbprovince":"北京","cwbcity":"北京市","cwbcounty":"朝阳区","receivablefee":11111.11,"paybackfee":0.00,"cwb":"7901","shipperid":0,"cwbordertypeid":1,"consigneemobile":"15901169883","transcwb":"LG123456789","destination":"北京","cwbdelivertypeid":"1","exceldeliver":"","excelbranch":"朝阳站","excelimportuserid":1001,"state":1,"printtime":"","commonid":5,"commoncwb":"","signtypeid":0,"podrealname":"","podtime":"","podsignremark":"","modelname":null,"scannum":0,"isaudit":0,"backreason":"","leavedreason":"","paywayid":1,"newpaywayid":"1","tuihuoid":189,"cwbstate":1,"remark1":"自定义1T1","remark2":"自定义2T1","remark3":"自定义3T1","remark4":"自定义4T1","remark5":"自定义5T1","backreasonid":0,"multi_shipcwb":null,"packagecode":"","backreturnreasonid":0,"backreturnreason":"","handleresult":0,"handleperson":0,"handlereason":"","addresscodeedittype":0},"deliveryState":null}
						String floworderdetail = rs.getString("floworderdetail");
						String nextbranchid = floworderdetail.substring(floworderdetail.indexOf("\"nextbranchid\":") + 15, floworderdetail.indexOf(",\"deliverybranchid\""));
						cwbOrderService.updateOrInsertWareHouseToBranch(rs.getLong("branchid"), rs.getString("cwb"), Long.parseLong(nextbranchid),
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getTimestamp("credate")));
					} catch (Exception e) {
						logger.error("",e);
					}
				}
			});
			msg += "迁移完成了";
		}
		model.addAttribute("msg", msg);
		return "/changeLog/outWareHouse";
	}

	/**
	 * 按照环节迁移数据
	 */
	@RequestMapping("/floworderDataChangeByEmaildate")
	public String floworderDataChangeByEmaildate(Model model, @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
			@RequestParam(value = "endTime", defaultValue = "", required = false) String endTime, @RequestParam(value = "url", defaultValue = "", required = false) String omsurl,
			@RequestParam(value = "flowordertype", defaultValue = "", required = false) String flowordertype, HttpServletRequest request) {
		final String url = omsurl;
		String msg = "";
		if (startTime.length() > 0 && endTime.length() > 0 && flowordertype.length() > 0 && omsurl.length() > 0) {
			changeCheck = true;
			try {
				String Str = JSONReslutUtil.getResultMessageChangeLog(url + "/OMSChange/pushBranchMap", "a=1", "POST").toString();
				logger.info("保存branchMap返回：{}", Str);
				if (Str == null || Str.equals("")) {
					model.addAttribute("msg", "请求站点异常");
					return "/changeLog/floworderChange";
				} else if (Str.indexOf("01") > -1) {
					model.addAttribute("msg", "站点列表为空");
					return "/changeLog/floworderChange";
				}
			} catch (IOException e1) {
				logger.error("",e1);
			}
			List<EmailDate> emaildateList = emailDateDAO.getEmailDateByEmaildate(startTime, endTime);
			logger.info("开始迁移数据,emaildate时间从：{}到{}", startTime, endTime);
			if (emaildateList != null && emaildateList.size() > 0) {
				for (EmailDate e : emaildateList) {
					if (!changeCheck) {
						logger.info("" + flowordertype + "迁移数据,已停止,要开始的emaildateid:{}", e.getEmaildateid());
						model.addAttribute("msg", "" + flowordertype + "迁移数据,已停止,将要开始的emaildateid:" + e.getEmaildateid());
						return "/changeLog/floworderChange";
					}
					logger.info("" + flowordertype + "迁移数据,时间从：{}到{},emaildateid个数：" + emaildateList.size() + ",开始第" + (emaildateList.indexOf(e) + 1) + "个,emaildateid:" + e.getEmaildateid(),
							startTime, endTime);
					String sql = "SELECT of.* FROM `express_ops_order_flow` AS of LEFT JOIN `express_ops_cwb_detail` AS de ON of.cwb=de.cwb  WHERE " + "de.emaildateid =" + e.getEmaildateid()
							+ " AND de.state=1 and of.flowordertype in(" + flowordertype + ")";
					jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
						@Override
						public void processRow(ResultSet rs) throws SQLException {
							try {
								OrderFlow orderFlow = new OrderFlow();
								orderFlow.setFloworderid(rs.getLong("floworderid"));
								String cwb = rs.getString("cwb");
								orderFlow.setCwb(cwb);
								orderFlow.setBranchid(rs.getLong("branchid"));
								orderFlow.setCredate(rs.getTimestamp("credate"));
								orderFlow.setUserid(rs.getLong("userid"));
								String flowDetail = rs.getString("floworderdetail").replaceAll("&", "").replaceAll("=", "").replaceAll("#", "");
								orderFlow.setFloworderdetail(flowDetail);
								orderFlow.setFlowordertype(rs.getInt("flowordertype"));
								orderFlow.setIsnow(rs.getInt("isnow"));
								orderFlow.setComment(rs.getString("comment"));
								try {
									JSONReslutUtil.getResultMessageChangeLog(url + "/OMSChange/tuotou", "pram=" + om.writeValueAsString(orderFlow), "POST").toString();
									// logger.info("{}返回：{}",cwb,userStr);
								} catch (Exception e) {
									logger.error("获取当前用户信息异常", e);
								}
							} catch (Exception e) {
								logger.error("",e);
							}
						}
					});
					logger.info("" + flowordertype + "迁移数据,时间从：{}到{},emaildateid个数：" + emaildateList.size() + ",完成第" + (emaildateList.indexOf(e) + 1) + "个,emaildateid:" + e.getEmaildateid(),
							startTime, endTime);
				}
			}
			msg += "迁移完成了发货时间：" + startTime + "到" + endTime + "的数据";
		}
		model.addAttribute("msg", msg);
		return "/changeLog/floworderChange";
	}

	/**
	 * 按照环节迁移数据
	 */
	@RequestMapping("/floworderDataChangeBycwbs")
	public String floworderDataChangeBycwbs(Model model, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "url", defaultValue = "", required = false) String omsurl, @RequestParam(value = "flowordertype", defaultValue = "", required = false) String flowordertype,
			HttpServletRequest request) {
		final String url = omsurl;
		String msg = "";
		if (cwbs.length() > 0 && flowordertype.length() > 0 && omsurl.length() > 0) {
			changeCheck = true;
			try {
				String Str = JSONReslutUtil.getResultMessageChangeLog(url + "/OMSChange/pushBranchMap", "a=1", "POST").toString();
				logger.info("保存branchMap返回：{}", Str);
				if (Str == null || Str.equals("")) {
					model.addAttribute("msg", "请求站点异常");
					return "/changeLog/floworderChange";
				} else if (Str.indexOf("01") > -1) {
					model.addAttribute("msg", "站点列表为空");
					return "/changeLog/floworderChange";
				}
			} catch (IOException e1) {
				logger.error("",e1);
			}
			String[] cwbss = cwbs.trim().split("\r\n");
			String cwbStr = "";
			for (String str : cwbss) {
				cwbStr += "'" + str + "',";
			}
			cwbStr = cwbStr.substring(0, cwbStr.length() - 1);
			String sql = "SELECT of.* FROM `express_ops_order_flow` AS of LEFT JOIN `express_ops_cwb_detail` AS de ON of.cwb=de.cwb  WHERE " + "de.cwb in(" + cwbStr
					+ ") AND de.state=1  and of.flowordertype in(" + flowordertype + ")";
			jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					try {
						OrderFlow orderFlow = new OrderFlow();
						orderFlow.setFloworderid(rs.getLong("floworderid"));
						String cwb = rs.getString("cwb");
						orderFlow.setCwb(cwb);
						orderFlow.setBranchid(rs.getLong("branchid"));
						orderFlow.setCredate(rs.getTimestamp("credate"));
						orderFlow.setUserid(rs.getLong("userid"));
						String flowDetail = rs.getString("floworderdetail").replaceAll("&", "").replaceAll("=", "").replaceAll("#", "");
						orderFlow.setFloworderdetail(flowDetail);
						orderFlow.setFlowordertype(rs.getInt("flowordertype"));
						orderFlow.setIsnow(rs.getInt("isnow"));
						orderFlow.setComment(rs.getString("comment"));
						try {
							JSONReslutUtil.getResultMessageChangeLog(url + "/OMSChange/tuotou", "pram=" + om.writeValueAsString(orderFlow), "POST").toString();
							// logger.info("{}返回：{}",cwb,userStr);
						} catch (Exception e) {
							logger.error("获取当前用户信息异常", e);
						}
					} catch (Exception e) {
						logger.error("",e);
					}
				}
			});
		}
		model.addAttribute("msg", msg);
		return "/changeLog/floworderChange";
	}

	/**
	 * 财务系统根据发货时间迁移数据
	 * 
	 * @param model
	 * @param startTime
	 * @param endTime
	 * @param omsurl
	 * @param flowordertype
	 * @param request
	 * @return
	 */
	@RequestMapping("/accountNeedDatamoveByEmaildate")
	public String accountNeedDatamoveByEmaildate(Model model, @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
			@RequestParam(value = "endTime", defaultValue = "", required = false) String endTime, @RequestParam(value = "url", defaultValue = "", required = false) String accounturl,
			@RequestParam(value = "flowordertype", defaultValue = "", required = false) String flowordertype, HttpServletRequest request) {
		final String url = accounturl;
		String msg = "";
		if (startTime.length() > 0 && endTime.length() > 0 && accounturl.length() > 0) {
			changeCheck = true;
			try {
				String dmpid = request.getSession().getId() == null ? "" : request.getSession().getId();
				String Str = JSONReslutUtil.getResultMessageChangeLog(url + "/jmsCenter/pushBranchMap", "dmpid=" + dmpid, "POST").toString();
				logger.info("保存branchMap返回：{}", Str);
				if (Str == null || Str.equals("")) {
					model.addAttribute("msg", "请求站点异常");
					return "/changeLog/accountNeedChange";
				} else if (Str.indexOf("01") > -1) {
					model.addAttribute("msg", "站点列表为空");
					return "/changeLog/accountNeedChange";
				}
			} catch (IOException e1) {
				logger.error("",e1);
			}
			List<EmailDate> emaildateList = emailDateDAO.getEmailDateByEmaildate(startTime, endTime);
			logger.info("开始迁移财务数据,emaildate时间从：{}到{}", startTime, endTime);
			if (emaildateList != null && emaildateList.size() > 0) {
				for (EmailDate e : emaildateList) {
					if (!changeCheck) {
						logger.info("迁移财务数据,已停止,要开始的emaildateid:{}", e.getEmaildateid());
						model.addAttribute("msg", "迁移财务数据,已停止,将要开始的emaildateid:" + e.getEmaildateid());
						return "/changeLog/accountNeedChange";
					}
					logger.info("迁移财务数据,时间从：{}到{},emaildateid个数：" + emaildateList.size() + ",开始第" + (emaildateList.indexOf(e) + 1) + "个,emaildateid:" + e.getEmaildateid(), startTime, endTime);
					String sql = "SELECT of.* FROM `express_ops_order_flow` AS of LEFT JOIN `express_ops_cwb_detail` AS de ON of.cwb=de.cwb  WHERE " + "de.emaildateid =" + e.getEmaildateid()
							+ " AND de.state=1 ";
					final String dmpid = request.getSession().getId() == null ? "" : request.getSession().getId();
					jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
						@Override
						public void processRow(ResultSet rs) throws SQLException {
							try {

								OrderFlow orderFlow = new OrderFlow();
								orderFlow.setFloworderid(rs.getLong("floworderid"));
								String cwb = rs.getString("cwb");
								orderFlow.setCwb(cwb);
								orderFlow.setBranchid(rs.getLong("branchid"));
								orderFlow.setCredate(rs.getTimestamp("credate"));
								orderFlow.setUserid(rs.getLong("userid"));
								String flowDetail = rs.getString("floworderdetail").replaceAll("&", "").replaceAll("=", "").replaceAll("#", "");
								orderFlow.setFloworderdetail(flowDetail);
								orderFlow.setFlowordertype(rs.getInt("flowordertype"));
								orderFlow.setIsnow(rs.getInt("isnow"));
								orderFlow.setComment(rs.getString("comment"));
								try {
									JSONReslutUtil.getResultMessageChangeLog(url + "/jmsCenter/movedata", "dmpid=" + dmpid + "&pram=" + om.writeValueAsString(orderFlow), "POST").toString();
								} catch (Exception e) {
									logger.error("获取当前用户信息异常", e);
								}
							} catch (Exception e) {
								logger.error("",e);
							}
						}
					});
					logger.info("迁移财务数据,时间从：{}到{},emaildateid个数：" + emaildateList.size() + ",完成第" + (emaildateList.indexOf(e) + 1) + "个,emaildateid:" + e.getEmaildateid(), startTime, endTime);
				}
			}
			msg += "迁移财务数据完成了发货时间：" + startTime + "到" + endTime + "的数据";
		}
		model.addAttribute("msg", msg);
		return "/changeLog/accountNeedChange";
	}

	/**
	 * 财务系统根据订单号迁移数据
	 */
	@RequestMapping("/accountNeedDataMoveBycwbs")
	public String accountNeedDataMoveBycwbs(Model model, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "url", defaultValue = "", required = false) String accounturl, @RequestParam(value = "flowordertype", defaultValue = "", required = false) String flowordertype,
			HttpServletRequest request) {
		final String url = accounturl;
		String msg = "";
		if (cwbs.length() > 0 && flowordertype.length() > 0 && accounturl.length() > 0) {
			changeCheck = true;
			try {
				final String dmpid = request.getSession().getId() == null ? "" : request.getSession().getId();
				String Str = JSONReslutUtil.getResultMessageChangeLog(url + "/jmsCenter/pushBranchMap", "dmpid=" + dmpid, "POST").toString();
				logger.info("保存branchMap返回：{}", Str);
				if (Str == null || Str.equals("")) {
					model.addAttribute("msg", "请求站点异常");
					return "/changeLog/accountNeedChange";
				} else if (Str.indexOf("01") > -1) {
					model.addAttribute("msg", "站点列表为空");
					return "/changeLog/accountNeedChange";
				}
			} catch (IOException e1) {
				logger.error("",e1);
			}
			String[] cwbss = cwbs.trim().split("\r\n");
			String cwbStr = "";
			for (String str : cwbss) {
				cwbStr += "'" + str + "',";
			}
			cwbStr = cwbStr.substring(0, cwbStr.length() - 1);
			String sql = "SELECT of.* FROM `express_ops_order_flow` AS of LEFT JOIN `express_ops_cwb_detail` AS de ON of.cwb=de.cwb  WHERE " + "de.cwb in(" + cwbStr + ") AND de.state=1 ";
			final String dmpid = request.getSession().getId() == null ? "" : request.getSession().getId();
			jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					try {
						OrderFlow orderFlow = new OrderFlow();
						orderFlow.setFloworderid(rs.getLong("floworderid"));
						String cwb = rs.getString("cwb");
						orderFlow.setCwb(cwb);
						orderFlow.setBranchid(rs.getLong("branchid"));
						orderFlow.setCredate(rs.getTimestamp("credate"));
						orderFlow.setUserid(rs.getLong("userid"));
						String flowDetail = rs.getString("floworderdetail").replaceAll("&", "").replaceAll("=", "").replaceAll("#", "");
						orderFlow.setFloworderdetail(flowDetail);
						orderFlow.setFlowordertype(rs.getInt("flowordertype"));
						orderFlow.setIsnow(rs.getInt("isnow"));
						orderFlow.setComment(rs.getString("comment"));
						try {
							JSONReslutUtil.getResultMessageChangeLog(url + "/jmsCenter/movedata", "dmpid=" + dmpid + "pram=" + om.writeValueAsString(orderFlow), "POST").toString();
						} catch (Exception e) {
							logger.error("获取当前用户信息异常", e);
						}
					} catch (Exception e) {
						logger.error("",e);
					}
				}
			});
			logger.info("迁移财务按订单数据：{}", cwbStr);
			msg += "迁移财务按订单数据完成了";
		}
		model.addAttribute("msg", msg);
		return "/changeLog/accountNeedChange";
	}

	/**
	 * 上门退订单根据批次号迁移数据
	 * 
	 * @param model
	 * @param startTimeid
	 * @param endTimeid
	 * @param request
	 * @return
	 */
	@RequestMapping("/shangmentuiDataMoveByEmaildateid")
	public String shangmentuiDataMoveByEmaildateid(Model model, @RequestParam(value = "startTimeid", defaultValue = "0", required = false) long startTimeid,
			@RequestParam(value = "endTimeid", defaultValue = "0", required = false) long endTimeid, HttpServletRequest request) {

		String msg = "";
		if (startTimeid > 0 || endTimeid > 0) {
			changeCheck = true;
			if (startTimeid > endTimeid) {
				model.addAttribute("msg", "开始id不能大于结束id");
				return "/changeLog/shangmentuiChange";
			}
			long emaildatemaxid = emailDateDAO.getEmailDateMaxid();

			logger.info("上门退订单开始迁移数据,emaildateid从：{}到{},最大emaildateid是:" + emaildatemaxid, startTimeid, endTimeid);
			if (emaildatemaxid == 0) {
				model.addAttribute("msg", "emaildateid为空");
				return "/changeLog/shangmentuiChange";
			}
			if (emaildatemaxid < endTimeid) {
				endTimeid = emaildatemaxid;
			}

			List<EmailDate> emaildateidList = emailDateDAO.getEmailDateByEmaildateid(startTimeid, endTimeid);
			if (emaildateidList != null && emaildateidList.size() > 0) {
				for (EmailDate e : emaildateidList) {
					if (!changeCheck) {
						logger.info("上门退订单迁移数据,已停止,要开始的emaildateid:{}", e.getEmaildateid());
						model.addAttribute("msg", "迁移数据,已停止,将要开始的emaildateid:" + e.getEmaildateid());
						return "/changeLog/shangmentuiChange";
					}
					logger.info("上门退订单迁移数据,emaildateid从：{}到{},emaildateid个数：" + emaildateidList.size() + ",开始第" + (emaildateidList.indexOf(e) + 1) + "个,emaildateid:" + e.getEmaildateid(),
							startTimeid, endTimeid);
					String sql = "SELECT * FROM `express_ops_cwb_detail` WHERE emaildateid =" + e.getEmaildateid() + " AND state=1 and cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue();
					jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
						@Override
						public void processRow(ResultSet rs) throws SQLException {
							try {
								ShangMenTuiCwbDetail shangMenTuiCwbDetail = new ShangMenTuiCwbDetail();
								shangMenTuiCwbDetail.setCwb(rs.getString("cwb"));
								shangMenTuiCwbDetail.setBackcarnum(rs.getLong("backcarnum"));
								shangMenTuiCwbDetail.setCarwarehouseid(Long.parseLong(rs.getString("carwarehouse") == null ? "0" : rs.getString("carwarehouse")));
								shangMenTuiCwbDetail.setConsigneeaddress(rs.getString("consigneeaddress"));
								shangMenTuiCwbDetail.setConsigneemobile(rs.getString("consigneemobile"));
								shangMenTuiCwbDetail.setConsigneename(rs.getString("consigneename"));
								shangMenTuiCwbDetail.setConsigneephone(rs.getString("consigneephone"));
								shangMenTuiCwbDetail.setCustomerid(rs.getLong("customerid"));
								shangMenTuiCwbDetail.setPaybackfee(rs.getBigDecimal("paybackfee"));
								shangMenTuiCwbDetail.setPrinttime(rs.getString("printtime"));
								shangMenTuiCwbDetail.setRemark3(rs.getString("remark3"));
								shangMenTuiCwbDetail.setRemark4(rs.getString("remark4"));
								shangMenTuiCwbDetail.setRemark5(rs.getString("remark5"));
								shangMenTuiCwbDetail.setBackcarname(rs.getString("backcarname"));
								shangMenTuiCwbDetail.setConsigneepostcode(rs.getString("consigneepostcode"));
								shangMenTuiCwbDetail.setEmaildate(rs.getString("emaildate"));
								shangMenTuiCwbDetail.setEmaildateid(rs.getLong("emaildateid"));
								shangMenTuiCwbDetail.setDeliverybranchid(rs.getLong("deliverybranchid"));

								try {
									if (shangMenTuiCwbDetailDAO.getShangMenTuiCwbDetailCountByCwb(rs.getString("cwb")) > 0) {
										shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailByCwb(shangMenTuiCwbDetail);
									} else {
										shangMenTuiCwbDetailDAO.insertShangMenTuiCwbDetail(shangMenTuiCwbDetail);
									}
								} catch (Exception e) {
									logger.error("上门退订单迁移", e);
								}
							} catch (Exception e) {
								logger.error("",e);
							}
						}
					});
					logger.info("迁移上门退订单数据,时间从：{}到{},emaildateid个数：" + emaildateidList.size() + ",完成第" + (emaildateidList.indexOf(e) + 1) + "个,emaildateid:" + e.getEmaildateid(), startTimeid,
							endTimeid);
				}
			}
			msg += "上门退订单迁移完成了emaildateid：" + startTimeid + "到" + endTimeid + "的数据";
		}
		model.addAttribute("msg", msg);
		return "/changeLog/shangmentuiChange";
	}

	@RequestMapping("/shangmentuiDataMoveByCwbs")
	public String shangmentuiDataMoveByCwbs(Model model, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs, HttpServletRequest request) {
		String msg = "";
		if (cwbs.length() > 0) {
			changeCheck = true;

			String[] cwbss = cwbs.trim().split("\r\n");
			String cwbStr = "";
			for (String str : cwbss) {
				cwbStr += "'" + str + "',";
			}
			cwbStr = cwbStr.substring(0, cwbStr.length() - 1);
			logger.info("上门退订单按照订单号开始迁移数据,emaildateid从：{}到{},最大emaildateid是:");

			String sql = "SELECT * FROM `express_ops_cwb_detail` WHERE cwb in(" + cwbStr + ") AND state=1 and cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue();
			jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					try {
						ShangMenTuiCwbDetail shangMenTuiCwbDetail = new ShangMenTuiCwbDetail();
						shangMenTuiCwbDetail.setCwb(rs.getString("cwb"));
						shangMenTuiCwbDetail.setBackcarnum(rs.getLong("backcarnum"));
						shangMenTuiCwbDetail.setCarwarehouseid(Long.parseLong(rs.getString("carwarehouse") == null ? "0" : rs.getString("carwarehouse")));
						shangMenTuiCwbDetail.setConsigneeaddress(rs.getString("consigneeaddress"));
						shangMenTuiCwbDetail.setConsigneemobile(rs.getString("consigneemobile"));
						shangMenTuiCwbDetail.setConsigneename(rs.getString("consigneename"));
						shangMenTuiCwbDetail.setConsigneephone(rs.getString("consigneephone"));
						shangMenTuiCwbDetail.setCustomerid(rs.getLong("customerid"));
						shangMenTuiCwbDetail.setPaybackfee(rs.getBigDecimal("paybackfee"));
						shangMenTuiCwbDetail.setPrinttime(rs.getString("printtime"));
						shangMenTuiCwbDetail.setRemark3(rs.getString("remark3"));
						shangMenTuiCwbDetail.setRemark4(rs.getString("remark4"));
						shangMenTuiCwbDetail.setRemark5(rs.getString("remark5"));
						shangMenTuiCwbDetail.setBackcarname(rs.getString("backcarname"));
						shangMenTuiCwbDetail.setConsigneepostcode(rs.getString("consigneepostcode"));
						shangMenTuiCwbDetail.setEmaildate(rs.getString("emaildate"));
						shangMenTuiCwbDetail.setEmaildateid(rs.getLong("emaildateid"));
						shangMenTuiCwbDetail.setDeliverybranchid(rs.getLong("deliverybranchid"));

						try {
							if (shangMenTuiCwbDetailDAO.getShangMenTuiCwbDetailCountByCwb(rs.getString("cwb")) > 0) {
								shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailByCwb(shangMenTuiCwbDetail);
							} else {
								shangMenTuiCwbDetailDAO.insertShangMenTuiCwbDetail(shangMenTuiCwbDetail);
							}
						} catch (Exception e) {
							logger.error("上门退订单按订单迁移", e);
						}
					} catch (Exception e) {
						logger.error("",e);
					}
				}
			});
			msg += "上门退订单按订单迁移完成了";
		}
		model.addAttribute("msg", msg);
		return "/changeLog/shangmentuiChange";
	}

	@RequestMapping("/jiaojiedandataMove15days")
	public String jiaojiedandataMove15days(Model model, HttpServletRequest request, @RequestParam(value = "endtime", defaultValue = "", required = false) String endtime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		endtime = endtime.length() == 0 ? df.format(date) : endtime;
		String msg = "";

		for (int i = 14; i > -1; i--) {
			String newendtime = DateDayUtil.getDateBeforedays(endtime, i);
			String begintime = DateDayUtil.getDateBeforedays(newendtime, 1);
			logger.info("迁移交接单数据--begintime：{}，endtime：{}", begintime, newendtime);
			List<Long> owgids = outwarehousegroupDao.getOutWarehouseGroupByCreatetime(begintime, newendtime);

			if (owgids.size() > 0) {
				List<GroupDetail> groupdetailList = groupDetailDao.getAllGroupDetailByGroupids(StringUtil.getStringsByLongList(owgids));
				if (groupdetailList.size() > 0) {
					Map<Long, String> cwbsAndGroupid = new HashMap<Long, String>();
					for (GroupDetail gd : groupdetailList) {
						if (cwbsAndGroupid.get(gd.getGroupid()) == null) {
							cwbsAndGroupid.put(gd.getGroupid(), "");
						}
						cwbsAndGroupid.put(gd.getGroupid(), cwbsAndGroupid.get(gd.getGroupid()) + ",'" + gd.getCwb() + "'");
					}

					for (Map.Entry<Long, String> entry : cwbsAndGroupid.entrySet()) {
						logger.info(entry.getKey() + ":" + entry.getValue() + "\t");
						outwarehousegroupDao.saveOutWarehouseGroupByid(entry.getValue().substring(1, entry.getValue().length()), entry.getKey());
					}
				}
				logger.info("迁移交接单express_ops_outwarehousegroup数据完成了--begintime：{}，endtime：{}", begintime, newendtime);
				msg += "迁移交接单express_ops_outwarehousegroup数据完成了,begintime:" + begintime + ",newendtime:" + newendtime;
			}

			List<String> groupdetailcwbs = groupDetailDao.getAllGroupDetailByCreatetime(begintime, newendtime);

			if (groupdetailcwbs.size() > 0) {
				List<CwbOrder> colist = cwbDAO.getCwbOrderNotDetailByCwbs(StringUtil.getStringsByStringList(groupdetailcwbs));
				if (colist.size() > 0) {
					for (CwbOrder co : colist) {
						long branchid = co.getCurrentbranchid() == 0 ? co.getStartbranchid() : co.getCurrentbranchid();
						groupDetailDao.saveGroupDetailByCwb(co.getCwb(), co.getFlowordertype(), branchid, co.getNextbranchid(), co.getDeliverid(), co.getCustomerid());
					}
				}
				logger.info("迁移交接单express_ops_groupdetail数据完成了--begintime：{}，endtime：{}", begintime, newendtime);
				msg += "迁移交接单express_ops_groupdetail数据完成了,begintime:" + begintime + ",newendtime:" + newendtime;
			}
		}

		model.addAttribute("msg", msg);
		return "/changeLog/jiaojiedancwbNeedChange";
	}

	@RequestMapping("/optionChange")
	public String optionChange(Model model) {

		return "/changeLog/optionChange";
	}

	@RequestMapping("/option")
	public String option(Model model) {
		String msg = "";
		for (int i = 0; i < 1000; i++) {
			logger.info("迁移超期异常监控，出库时间迁移完成,第" + i + "个1000条 开始");
			String sql = " SELECT of.cwb,of.credate FROM `express_ops_order_flow` AS of LEFT JOIN `express_ops_operation_time` AS de ON of.cwb=de.cwb  WHERE " + " of.flowordertype="
					+ FlowOrderTypeEnum.RuKu.getValue() + " and de.customerid=0 limit 0,1000 ";
			jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					try {
						if (rs.getString("cwb") == null) {
							return;
						}
						operationTimeDAO.updateOperationTimeBycwb(rs.getString("cwb"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getTimestamp("credate")));
					} catch (Exception e) {
					    logger.error("",e);
						return;
					}
				}
			});
			logger.info("迁移超期异常监控，入库时间迁移完成,第" + i + "个1000条 结束");
		}
		msg += "迁移超期异常监控完成了";
		logger.info("迁移超期异常监控，入库时间迁移完成。");
		model.addAttribute("msg", msg);
		return option2(model);
	}

	@RequestMapping("/option2")
	public String option2(Model model) {
		String msg = "";
		for (int i = 0; i < 1000; i++) {
			logger.info("迁移超期异常监控，供货商迁移完成,第" + i + "个1000条 开始");
			String sql = " SELECT of.cwb,of.emaildate,of.customerid FROM `express_ops_cwb_detail` AS of LEFT JOIN `express_ops_operation_time` AS de ON of.cwb=de.cwb  WHERE "
					+ " of.state=1 and de.customerid in(0,99999) limit 0,1000  ";
			jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					try {
						if (rs.getString("cwb") == null || rs.getString("emaildate") == null) {
							return;
						}
						operationTimeDAO.updateOperationTimeBycwb(rs.getString("cwb"), rs.getString("emaildate"), rs.getLong("customerid"));
					} catch (Exception e) {
						logger.error("",e);
						return;
					}
				}
			});
			logger.info("迁移超期异常监控，供货商迁移完成,第" + i + "个1000条 结束");
		}
		msg += "迁移超期异常监控完成了";
		logger.info("迁移超期异常监控，供货商迁移完成。");
		model.addAttribute("msg", msg);
		return "/changeLog/optionChange";
	}
	
	
	
}
