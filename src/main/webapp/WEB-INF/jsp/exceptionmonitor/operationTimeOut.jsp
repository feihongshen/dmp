<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.OperationSetTime"%>
<%@page import="cn.explink.util.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	long modelid = request.getParameter("modelid") == null ?0L :Long.parseLong(request.getParameter("modelid").toString());
	long customerid = request.getParameter("customerid") == null ?0L :Long.parseLong(request.getParameter("customerid").toString());
	List<OperationSetTime> modelList = request.getAttribute("modelList")==null?null:(List<OperationSetTime>)request.getAttribute("modelList");
	List<Branch> branchList = request.getAttribute("branchList")==null?null:(List<Branch>)request.getAttribute("branchList");
	String[] branchids=request.getParameterValues("branchids")==null?null:request.getParameterValues("branchids");
	long sitetype=Long.parseLong(request.getAttribute("sitetype")==null?"0":request.getAttribute("sitetype").toString());
	List<Customer> customerList = request.getAttribute("customerlist")==null?null:(List<Customer>)request.getAttribute("customerlist");
	String starttime=StringUtil.nullConvertToEmptyString(request.getParameter("begindate"));
	String endtime=StringUtil.nullConvertToEmptyString(request.getParameter("enddate"));
	/* String begindate=StringUtil.nullConvertToEmptyString(request.getParameter("begindate"));
	String enddate=StringUtil.nullConvertToEmptyString(request.getParameter("enddate")); */
	String begindate=request.getParameter("begindate")==null||"".equals(request.getParameter("begindate").toString())?"-":request.getParameter("begindate").toString();
	String enddate=request.getParameter("enddate")==null||"".equals(request.getParameter("enddate").toString())?"-":request.getParameter("enddate").toString();
	Map<Long, Long> chukuMap = (Map<Long, Long>)request.getAttribute("chuku_time_out");
	Map<Long, Long> rukuMap = (Map<Long, Long>)request.getAttribute("ruku_time_out");
	Map<Long, Long> daozhanMap = (Map<Long, Long>)request.getAttribute("daozhan_time_out");
	Map<Long, Long> linghuoMap = (Map<Long, Long>)request.getAttribute("linghuo_time_out");
	Map<Long, Long> zhiliuMap = (Map<Long, Long>)request.getAttribute("zhiliu_time_out");
	Map<Long, Long> jushouMap = (Map<Long, Long>)request.getAttribute("jushou_time_out");
	Map<Long, Long> zhongzhuanzhanchukuMap = (Map<Long, Long>)request.getAttribute("zhongzhuanzhan_chuku_time_out");
	Map<Long, Long> zhongzhuanzhanrukuMap = (Map<Long, Long>)request.getAttribute("zhongzhuanzhan_ruku_time_out");
	Map<Long, Long> tuihuozhanrukuMap = (Map<Long, Long>)request.getAttribute("tuihuozhanruku_time_out");
	Map<Long, Long> tuihuochuzhanMap = (Map<Long, Long>)request.getAttribute("tuihuo_chuzhan_time_out");
	List<Map<String, Object>> branchdata =request.getAttribute("branchdata")==null?null:(List<Map<String, Object>>)request.getAttribute("branchdata");
	long A1  = request.getAttribute("A1") == null ?0L : (Long)request.getAttribute("A1");
	long A2  = request.getAttribute("A2") == null ?0L : (Long)request.getAttribute("A2");
	long A3  = request.getAttribute("A3") == null ?0L : (Long)request.getAttribute("A3");
	long A4  = request.getAttribute("A4") == null ?0L : (Long)request.getAttribute("A4");
	long A5  = request.getAttribute("A5") == null ?0L : (Long)request.getAttribute("A5");
	long A6  = request.getAttribute("A6") == null ?0L : (Long)request.getAttribute("A6");
	long A7  = request.getAttribute("A7") == null ?0L : (Long)request.getAttribute("A7");
	long A8  = request.getAttribute("A8") == null ?0L : (Long)request.getAttribute("A8");
	long A9  = request.getAttribute("A9") == null ?0L : (Long)request.getAttribute("A9");
	long A10 = request.getAttribute("A10") == null ?0L : (Long)request.getAttribute("A10");
	long A11 = request.getAttribute("A11") == null ?0L : (Long)request.getAttribute("A11");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- saved from url=(0076)http://58.83.193.9/oms/order/select/1?dmpid=85C94DDF6073E6BE87A8C1577448EE08 -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
var sitetype="<%=sitetype%>";
$(function() {
	$("#branchids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'' });
	
	$("#strtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	<%if(branchdata!=null&&!branchdata.isEmpty()){
		for(int i=0;i<branchdata.size();i++){
		%>
			var count=0;
			$("tr[id='branch<%=branchdata.get(i).get("branchid")%>'] a").each(function(){
				if($(this).attr("id")!="allcount<%=branchdata.get(i).get("branchid")%>"){
					count+=parseInt($(this).html());
				}
			});
			$("#allcount<%=branchdata.get(i).get("branchid")%>").html(count);
	<%}}%>
	
	if(sitetype>0){
/* 		$("#A1_title").show();
		$("td[id^='A1_list']").show();
		$("#A2_title").show();
		$("td[id^='A2_list']").show();
		$("#A3_title").show();
		$("td[id^='A3_list']").show();
		$("#A4_title").show();
		$("td[id^='A4_list']").show();
		$("#A5_title").show();
		$("td[id^='A5_list']").show();
		$("#A6_title").show();
		$("td[id^='A6_list']").show();
		$("#A7_title").show();
		$("td[id^='A7_list']").show();
		$("#A8_title").show();
		$("td[id^='A8_list']").show();
		$("#A9_title").show();
		$("td[id^='A9_list']").show();
		$("#A10_title").show();
		$("td[id^='A10_list']").show();
		$("#A11_title").show();
		$("td[id^='A11_list']").show(); */
		if(sitetype=="<%=BranchEnum.KuFang.getValue()%>"){
			$("#A1_title").show();
			$("td[id^='A1_list']").show();
			$("#A6_title").show();
			$("td[id^='A6_list']").show();
		}else if(sitetype=="<%=BranchEnum.ZhanDian.getValue()%>"){
			$("#A6_title").show();
			$("td[id^='A6_list']").show();
			$("#A3_title").show();
			$("td[id^='A3_list']").show();
			$("#A4_title").show();
			$("td[id^='A4_list']").show();
			$("#A9_title").show();
			$("td[id^='A9_list']").show();
			$("#A10_title").show();
			$("td[id^='A10_list']").show();
		}else if(sitetype=="<%=BranchEnum.ZhongZhuan.getValue()%>"){
			$("#A7_title").show();
			$("td[id^='A7_list']").show();
			$("#A2_title").show();
			$("td[id^='A2_list']").show();
			$("#A11_title").show();
			$("td[id^='A11_list']").show();
		}else if(sitetype=="<%=BranchEnum.TuiHuo.getValue()%>"){
			$("#A8_title").show();
			$("td[id^='A8_list']").show();
			$("#A5_title").show();
			$("td[id^='A5_list']").show();
		}
	}
	
	
	$("#find").click(function(){
		if($("#modelid").val()==0){
			alert("请选择超期异常名称");
			return;
		}
		if($("#strtime").val()!=""&&$("#endtime").val()!=""){
			if($("#strtime").val()>$("#endtime").val()){
				alert("开始时间要小于结束时间");
			}
		}
		$("#searchForm").submit();
	});
});

function count(branchid){
	$("#subForm").attr('action','<%=request.getContextPath()%>/ExceptionMonitor/showTimeOutByBranchid/'+branchid+'/1');
	/* $("#custID").val($("#customerid").val());
	$("#beginID").val($("#strtime").val());
	$("#endID").val($("#endtime").val());
	$("#modID").val($("#modelid").val()); */
	$("#subForm").submit();
}


function changeSitetype(){
	$("#searchForm").submit();
}
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="tabbox">
				<div style="position:relative; z-index:0 ">
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
					<form action="" method="post" id="searchForm">
						<div class="kfsh_search">
							超期异常名称：
							<select name="modelid" id="modelid" onchange="changeSitetype()">
								<option value="0">请选择</option>
							  <%if(modelList!=null&&modelList.size()>0)for(OperationSetTime c : modelList){ %>
							  	<option value="<%=c.getId()%>"<%if(modelid==c.getId()){%>selected<%} %>><%=c.getName() %></option>
							  <%} %>
							</select>
							&nbsp;机构类型：
							<%for(BranchEnum be:BranchEnum.values()){if(sitetype==be.getValue()){ %><%=be.getText() %><%}} %>
				           	&nbsp;机构名称：
							<select name ="branchids" id ="branchids"  multiple="multiple" style="width:320px;">
					         <%if(branchList!=null&&!branchList.isEmpty()){
					         	for(Branch b : branchList){ %>
					          	<option value ="<%=b.getBranchid()%>" 
					          	<%if(branchids != null &&  branchids.length>0){
									for (String branchid : branchids) {
										if(b.getBranchid()==Long.parseLong(branchid)){
					          				out.print("selected=selected'");
					          			}
									}
								 }%>><%=b.getBranchname()%></option>
					          <%}}%>
							 </select>
							 [<a href="javascript:multiSelectAll('branchids',1,'请选择');">全选</a>]
							 [<a href="javascript:multiSelectAll('branchids',0,'请选择');">取消全选</a>]
							&nbsp;供货商：
							<select name="customerid" id="customerid">
								<option value="0">请选择</option>
							  <%if(customerList!=null&&customerList.size()>0)for(Customer c : customerList){ %>
							  	<option value="<%=c.getCustomerid()%>"<%if(Long.parseLong(request.getParameter("customerid")==null?"0":request.getParameter("customerid").toString())==c.getCustomerid()){%>selected<%} %>><%=c.getCustomername() %></option>
							  <%} %>
							</select>
							&nbsp;入库时间：
								<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
							&nbsp;到
								<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
								<input type="button" id="find" value="查询"  class="input_button2" />
							</div>
						</form>
						
						</div>
						
						<div style="height:60px"></div>
						<table id="viewTitle"   width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1" height="30" >
								<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">机构名称</td>
								<td id="A1_title" style="display:none" width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未出库<br/><%=A1>=24?A1/24+"天":A1+"小时"%></td>
								<td id="A6_title" style="display:none" width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未到货<br/><%=A6>=24?A6/24+"天":A6+"小时"%></td>
								<td id="A3_title" style="display:none" width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未领货<br/><%=A3>=24?A3/24+"天":A3+"小时"%></td>
								<td id="A4_title" style="display:none" width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未归班<br/><%=A4>=24?A4/24+"天":A4+"小时"%></td>
								<td id="A9_title" style="display:none" width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期滞留<br/><%=A9>=24?A9/24+"天":A9+"小时"%></td>
								<td id="A10_title" style="display:none" width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未退货<br/><%=A10>=24?A10/24+"天":A10+"小时"%></td>
								<td id="A7_title" style="display:none" width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未到中转<br/><%=A7>=24?A7/24+"天":A7+"小时"%></td>
								<td id="A2_title" style="display:none" width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未中转<br/><%=A2>=24?A2/24+"天":A2+"小时"%></td>
								<td id="A8_title" style="display:none" width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未到退货<br/><%=A8>=24?A8/24+"天":A8+"小时"%></td>
								<td id="A5_title" style="display:none" width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未退供货商<br/><%=A5>=24?A5/24+"天":A5+"小时"%></td>
								<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">合计<br/></td>
							</tr>
						<%if(branchdata!=null&&!branchdata.isEmpty()){
							for(int i=0;i<branchdata.size();i++){
								long bId=Long.parseLong(branchdata.get(i).get("branchid").toString());
								String bName=branchdata.get(i).get("branchname").toString();
						%>
						<tr height="30" name="data" height="30" id="branch<%=bId%>">
							<td width="100" align="center" valign="middle" bgcolor="#f3f3f3"><strong><%=bName%></strong></td>
							<td id="A1_list<%=i%>" style="display:none" width="100" align="center" valign="middle"><!--超期未出库 -->
								<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=modelid%>/<%=A1 %>/<%=bId%>/<%=FlowOrderTypeEnum.RuKu.getValue() %>/0/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%=sitetype==BranchEnum.KuFang.getValue()?String.valueOf(rukuMap.get(bId)==null?0:rukuMap.get(bId)):"0" %>
								</a>
							</td>
							<td id="A6_list<%=i%>" style="display:none" width="100" align="center" valign="middle"><!--超期未到货 -->
								<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=modelid%>/<%=A6 %>/0/<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>/0/<%=bId%>/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%=(sitetype==BranchEnum.ZhanDian.getValue()||sitetype==BranchEnum.KuFang.getValue())?String.valueOf(chukuMap.get(bId)==null?0:chukuMap.get(bId)):"0" %>
								</a>
							</td>
							<td id="A3_list" style="display:none" width="100" align="center" valign="middle"><!--超期未领货 -->
								<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=modelid%>/<%=A3 %>/<%=bId%>/<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() %>/0/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%=sitetype==BranchEnum.ZhanDian.getValue()?String.valueOf(daozhanMap.get(bId)==null?0:daozhanMap.get(bId)):"0" %>
								</a>
							</td>
							<td id="A4_list" style="display:none" width="100" align="center" valign="middle"><!--超期未归班 -->
								<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=modelid%>/<%=A4 %>/<%=bId%>/<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue() %>/0/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%=sitetype==BranchEnum.ZhanDian.getValue()?String.valueOf(linghuoMap.get(bId)==null?0:linghuoMap.get(bId)):"0" %>
								</a>
							</td>
							<td id="A9_list" style="display:none" width="100" align="center" valign="middle"><!--超期滞留 -->
								<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=modelid%>/<%=A9 %>/<%=bId%>/<%=FlowOrderTypeEnum.YiFanKui.getValue() %>,<%=FlowOrderTypeEnum.YiShenHe.getValue() %>/<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%=sitetype==BranchEnum.ZhanDian.getValue()?String.valueOf(zhiliuMap.get(bId)==null?0:zhiliuMap.get(bId)):"0" %>
								</a>
							</td>
							<td id="A10_list" style="display:none" width="100" align="center" valign="middle"><!--超期未退货 -->
								<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=modelid%>/<%=A10 %>/<%=bId%>/<%=FlowOrderTypeEnum.YiFanKui.getValue() %>,<%=FlowOrderTypeEnum.YiShenHe.getValue() %>/<%=DeliveryStateEnum.JuShou.getValue() %>/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%=sitetype==BranchEnum.ZhanDian.getValue()?String.valueOf(jushouMap.get(bId)==null?0:jushouMap.get(bId)):"0" %>
								</a>
							</td>
							<td id="A7_list" style="display:none" width="100" align="center" valign="middle"><!--超期未到中转 -->
								<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=modelid%>/<%=A7 %>/0/<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>/0/<%=bId%>/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%=sitetype==BranchEnum.ZhongZhuan.getValue()?String.valueOf(zhongzhuanzhanchukuMap.get(bId)==null?0:zhongzhuanzhanchukuMap.get(bId)):"0" %>
								</a>
							</td>
							<td id="A2_list" style="display:none" width="100" align="center" valign="middle"><!--超期未中转-->
								<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=modelid%>/<%=A2 %>/<%=bId%>/<%=FlowOrderTypeEnum.RuKu.getValue() %>,<%=FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue() %>/0/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%=sitetype==BranchEnum.ZhongZhuan.getValue()?String.valueOf(zhongzhuanzhanrukuMap.get(bId)==null?0:zhongzhuanzhanrukuMap.get(bId)):"0" %>
								</a>
							</td>
							<td id="A8_list" style="display:none" width="100" align="center" valign="middle"><!--超期未到退货-->
								<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=modelid%>/<%=A8 %>/0/<%=FlowOrderTypeEnum.TuiHuoChuZhan.getValue() %>/0/<%=bId%>/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%=sitetype==BranchEnum.TuiHuo.getValue()?String.valueOf(tuihuochuzhanMap.get(bId)==null?0:tuihuochuzhanMap.get(bId)):"0" %>
								</a>
							</td>
							<td id="A5_list" style="display:none" width="100" align="center" valign="middle"><!--超期未退供货商-->
								<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=modelid%>/<%=A5 %>/<%=bId%>/<%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() %>/0/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%=sitetype==BranchEnum.TuiHuo.getValue()?String.valueOf(tuihuozhanrukuMap.get(bId)==null?0:tuihuozhanrukuMap.get(bId)):"0" %>
								</a>
							</td>
							<td width="100" align="center" valign="middle">
								<a id="allcount<%=bId%>"  href="#" onclick="count(<%=bId%>)"  ></a>
							</td>
						</tr>
					<%}} %>
						</table>
					
		</div>
	</div>
</div>

<form id="subForm" action="" method="POST">
	<input type="hidden" id="modID" name="modelid" value="<%=modelid%>"/>
	<input type="hidden" id="custID" name="customerid" value="<%=customerid%>"/>
	<input type="hidden" id="beginID" name="begindate" value="<%=starttime %>"/>
	<input type="hidden" id="endID" name="enddate" value="<%=endtime %>"/>
</form>
</body>
</html>
   
