<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.AccountCwbFareDetail"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>   
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.PayUp"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<User> userList = (List<User> )request.getAttribute("userList");
User user = (User)request.getAttribute("user");
String cwbordertypeid = request.getAttribute("cwbordertypeid")==null?"0":request.getAttribute("cwbordertypeid").toString();
String faretypeid = request.getAttribute("faretypeid")==null?"0":request.getAttribute("faretypeid").toString();
//String userid = request.getAttribute("userids")==null?"0":request.getAttribute("userid").toString();
List useridList =request.getAttribute("useridList")==null?null:(List) request.getAttribute("useridList");
String userids=request.getAttribute("userids")==null?null:request.getAttribute("userids").toString();
String shouldfare = request.getAttribute("shouldfare")==null?"0":request.getAttribute("shouldfare").toString();
String infactfare = request.getAttribute("infactfare")==null?"0":request.getAttribute("infactfare").toString();
Branch branch   = (Branch)request.getAttribute("branch");
List<Branch> branchList   = (List<Branch>)request.getAttribute("branchList");
Date now = new Date();
  String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");

  String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
  List<AccountCwbFareDetail> acfdList=request.getAttribute("acfdList")==null?new ArrayList<AccountCwbFareDetail>():(List<AccountCwbFareDetail>)request.getAttribute("acfdList");
  AccountCwbFareDetail accountCwbFareDetailSum=request.getAttribute("accountCwbFareDetailSum")==null?new AccountCwbFareDetail():(AccountCwbFareDetail)request.getAttribute("accountCwbFareDetailSum");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function Days(){     
	var day1 = $("#strtime").val();   
	var day2 = $("#endtime").val(); 
	var y1, y2, m1, m2, d1, d2;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	var date1 = new Date(y1, m1, d1);            
	var date2 = new Date(y2, m2, d2);   
	var minsec = Date.parse(date2) - Date.parse(date1);          
	var days = minsec / 1000 / 60 / 60 / 24;  
	if(days>31){
		return false;
	}        
	return true;
}

function addInit(){
	
	
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val(0);
	$("#searchForm").submit();
}
function editInit(){

}
function check(){
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(!Days()||($("#strtime").val()=='' &&$("#endtime").val()!='')||($("#strtime").val()!='' &&$("#endtime").val()=='')){
		alert("时间跨度不能大于31天！");
		return false;
	}
	return true;
}
$("document").ready(function(){  
	/* $("#btn1").click(function(){  
		if($("#btn1").attr("checked")){
			$("[name='checkbox']").attr("checked",'true');//全选  
		}else{
		   $("[name='checkbox']").removeAttr("checked");//取消全选  
		}	
	
	});
	$("#btnval").click(function(){
		if(check()){
	    $("#searchForm").submit();
		}
	}); */
	$("#updateF").click(function(){
		/* $("#yjOnStr").val("");
		$("#yjOffStr").val(""); */
		var cwbstr="";//已妥投交款列表选中id
		
		$("input[name='cwb']:checkbox:checked").each(function() {
			cwbstr+=$(this).val()+",";
		});
		if(cwbstr.length>0){	
			$("#alert_box").show();
			centerBox();
		}else{
			alert("暂无交款信息");
			return false;
		}
	});
	/* $("#updateF").click(function(){//
		$("#controlStr").val("");
		$("#mackStr").val("");
		var str=""; 
		var mackStr = "";
		$("input[name='checkbox']:checkbox:checked").each(function(){  
			var id = $(this).val();
			if(id!=-1){
				str+=$(this).val()+";";
				mackStr+=$("#aremark"+id).val()+"P:P"; 
			}
			
		}); 
		$("#controlStr").val(str);
		$("#mackStr").val(mackStr);
		if($("#mackStr").val()==""){
			alert("没有勾选任何项,不能做此操作!");
			return false;
		}
		
	   $("#updateForm").submit();
	}); */
	changeYj();
}); 

$(function() {
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
	$("#userid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择小件员' });
	
});
function isgetallcheck(){
	if($('input[name="cwb"]:checked').size()>0){
		$('input[name="cwb"]').each(function(){
			if($(this).attr("disabled")==false||$(this).attr("disabled")==undefined){
				$(this).attr("checked",false);
			}
		});
	}else{
		$('input[name="cwb"]').each(function(){
			if($(this).attr("disabled")==false||$(this).attr("disabled")==undefined){
				$(this).attr("checked",true);
			}
		});
	}
}
function changeTime(){
	if($("#ispay").val()==0){
		$("#isaudittime").val(1);
	}else{
		$("#isaudittime").val(2);
	}
}
function sumitForm(){
	if(check()){
		$("#searchForm").submit();
	}
}

var zfhjFee=0;
var sxhjFee=0;
//光标离开金额框时统计合计
function hjFeeCheck(){
	var feetransfer=$("#feetransfer").val()==""?0:$("#feetransfer").val();
	var feecash=$("#feecash").val()==""?0:$("#feecash").val();
	
 	zfhjFee=((parseFloat(feetransfer).toFixed(2)*100+parseFloat(feecash).toFixed(2)*100)/100).toFixed(2); 
	
	sxhjFee=((parseFloat(infactyingjiao).toFixed(2)*100-parseFloat(zfhjFee).toFixed(2)*100)/100).toFixed(2);
	$("#sxhjShow").html(sxhjFee);
}
function checkFClick(){
	if($("#feetransfer").val()!=""){
		if(!isFloat($("#feetransfer").val())){
			alert("转账金额应为数字");
			return false;
		}
	}
	if($("#feecash").val()!=""){
		if(!isFloat($("#feecash").val())){
			alert("现金金额应为数字");
			return false;
		}
	}
	
	if((zfhjFee*100)>(infactyingjiao*100)){
		alert("超额支付！");
		return false;
	}
	
	if((zfhjFee*100)<(infactyingjiao*100)){
		alert("您有未交款,请检查交款金额！");
		return false;
	}
	
	if(confirm("确定交款吗？")){
 		$("#checkF").attr("disabled","disabled");
    	$("#checkF").val("请稍候");
    	$.ajax({
    		type: "POST",
    		url:'<%=request.getContextPath()%>/accountcwbfare/payfare',
    		data:$('#createForm').serialize(),
    		dataType : "json",
    		success : function(data) {
    			if(data.errorCode==0){
    				alert(data.error);
    				sumitForm();
    			}else{
    				alert(data.error);
    			}
    		}
    	});
	}
}
var infactyingjiao=0;
//计算勾选未勾选已妥投货款合计、现金、POS、支票、其他
function changeYj(){
	var yingJiao=0;
	var cwbStr = "";
	$('input[type="checkbox"][name="cwb"]').each(function() {
       	if($(this).attr("checked")=="checked"){//选中 相加
       		yingJiao=((parseFloat(yingJiao).toFixed(2)*100+parseFloat($(this).attr("infactfare")).toFixed(2)*100)/100).toFixed(2);
       		cwbStr+="'"+$(this).val()+"',";
       	}
	});
	infactyingjiao=yingJiao;
	$("#cwbs").val(cwbStr.length>0?cwbStr.substring(0,cwbStr.length-1):"");
	$("#hjOpen").html(yingJiao);
	$("#sxhjShow").html(yingJiao);
}
</script>   
</HEAD>
<body style="background:#fff" marginwidth="0" marginheight="0">

	<!-- 弹出框开始 -->
	<!-- 
	<div id="alert_box" style="display:none; " align="center">
	  <div id="box_bg" ></div>
	  <div id="box_contant" >
	    <div id="box_top_bg"></div>
	    <div id="box_in_bg">
	    <form id="createForm" action="<%=request.getContextPath()%>/accountcwbfare/payfare" method="post">
	      <h1><div id="close_box" onclick="closeBox()"></div>
	        	交款信息</h1>
	        <div class="right_title" style="padding:10px">
	        <input type="hidden" name="cwbs" id="cwbs" value="">
	         <h1>您需支付金额：<font id="hjOpen" style="font-family:'微软雅黑', '黑体'; font-size:25px"></font>
				元，&nbsp;&nbsp;还有<font id="sxhjShow" style="font-family:'微软雅黑', '黑体'; font-size:25px"></font>元未支付。</lable></h1>
	          <p>&nbsp;</p>
	          <table width="800" border="0" cellspacing="1" cellpadding="2" class="table_2" >
	            <tr>
					<td bgcolor="#F4F4F4">付款方式</td>
					<td bgcolor="#F4F4F4">金额</td>
					<td bgcolor="#F4F4F4">付款人</td>
					<td bgcolor="#F4F4F4">卡号</td>
				</tr>
				<tr>
					<td>转账</td>
					<td><input onblur="hjFeeCheck()" name="girofee" type="text" id="feetransfer" size="10" maxlength="10" value="" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
					<td><input name="girouser" type="text" id="usertransfer" size="10" maxlength="20" value=""/></td>
					<td><input name="girocardno" type="text" id="cardtransfer" size="25" maxlength="20" value=""/></td>
				</tr>
				<tr>
					<td>现金</td>
					<td><input onblur="hjFeeCheck()" name="cashfee" type="text" id="feecash" size="10" maxlength="10" value="" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
					<td><input name="cashuser" type="text" id="usercash" size="10" maxlength="20" value=""/></td>
					<td></td>
				</tr>
	            <tr>
	              <td colspan="4" bgcolor="#F4F4F4">
	              	<div class="jg_10"></div>
	                  <input type="button" class="input_button1" id="checkF" onclick="checkFClick()" value="确 认" />
	                  <div class="jg_10"></div>
	                </td>
	            </tr>
	          </table>
	        </div>
	        </form>
	    </div>
	  </div>
	  <div id="box_yy"></div>
	</div>
	 -->
	<!-- 弹出框结束 -->
	

   <div class="menucontant">
		<form id="searchForm" action ="<%=request.getContextPath()%>/accountcwbfaresubmit/accountfaresubmitlist/2" method = "post">
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1" border="1">
			  <tr>
					<td width="100%">
				
				当前站点:<%=branch.getBranchname() %><input type="hidden" value="<%=branch.getBranchid() %>"/>
						
						审核时间：
							<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
						到
							<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
						<font color="red">（查询31天以内数据）</font>
						订单类型：<select id="cwbordertypeid" name="cwbordertypeid">
						<option <%if(cwbordertypeid.equals(CwbOrderTypeIdEnum.Shangmentui.getValue()+"")){%>selected="selected"<%}%> value="<%=CwbOrderTypeIdEnum.Shangmentui.getValue() %>"><%=CwbOrderTypeIdEnum.Shangmentui.getText()%></option>
						<option <%if(cwbordertypeid.equals(CwbOrderTypeIdEnum.Shangmenhuan.getValue()+"")){%>selected="selected"<%}%> value="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()%>"><%=CwbOrderTypeIdEnum.Shangmenhuan.getText()%></option>
						<option <%if(cwbordertypeid.equals(CwbOrderTypeIdEnum.Peisong.getValue()+"")){%>selected="selected"<%}%> value="<%=CwbOrderTypeIdEnum.Peisong.getValue()%>"><%=CwbOrderTypeIdEnum.Peisong.getText()%></option>
						</select>
				</td>
				</tr>
				 <tr>
					<td width="100%">
					交款状态：<select id="faretypeid" name="faretypeid">
						<option value="1" <%if(faretypeid.equals("1")){%>selected="selected"<%}%>>未交款</option>
						<option value="2" <%if(faretypeid.equals("2")){%>selected="selected"<%}%>>已交款未审核</option>
						<option value="3" <%if(faretypeid.equals("3")){%>selected="selected"<%}%>>已审核</option>
						</select>
						
						小件员：
						<select id="userid" name="userid" multiple="multiple" style="width: 200px;" >
						<%if(userList!=null&&userList.size()>0)for(User u:userList){ %>
						<option value="<%=u.getUserid() %>"  <%if(useridList!=null&&!useridList.isEmpty()) 
			            {for(int i=0;i<useridList.size();i++){
			            	if(u.getUserid()== new Long(useridList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%> ><%=u.getRealname() %></option>
						<%} %>
						</select>
				
						[<a href="javascript:multiSelectAll('userid',1,'请选择');">全选</a>]
						[<a href="javascript:multiSelectAll('userid',0,'请选择');">取消全选</a>]
					<input type ="button" value ="查询" class="input_button2" onclick="sumitForm();"/>
					<input type="button" value="导出" class="input_button2"  onclick="exportfile()">
					
					</td>
				</tr>
				</table>
		 </form>
		<form action="<%=request.getContextPath()%>/accountcwbfaresubmit/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="begindate" value="<%=starttime%>"/>
		<input type="hidden" name="enddate" value="<%=endtime%>"/>
		<input type="hidden" name="cwbordertypeid" value="<%=cwbordertypeid%>"/>
		<input type="hidden" name="faretypeid" value="<%=faretypeid%>"/>
		<input type="hidden" id="userids" name="userids" value="<%=userids%>"/>
	</form>
 	<br/>
 	<br/>
 	<br/>
	<div>	
		<h1 style="line-height:30px; font-size:18px; font-family:'微软雅黑', '黑体'; font-weight:bold; color:#369"><%="1".equals(request.getParameter("ispay"))?"已交款":"未交款" %>记录：</h1>	
		<div>	
				<!--滑动内容开始 -->
				<div >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
						<tbody>
							<tr class="font_1">
								<td width="100" align="center" valign="middle" ><a style="cursor: pointer;" onclick="isgetallcheck();">[全选/反选]</a></td>
								<td  align="center" valign="middle" >订单号</td>
								<td  align="center" valign="middle" >小件员</td>
								<td align="center" valign="middle" >供货商</td>
								<td align="center" valign="middle" >订单类型</td>
								<td align="center" valign="middle" >配送站点</td>
								<td align="center" valign="middle" >归班审核时间</td>
								<td align="center" valign="middle" >配送结果</td>
								<td align="center" valign="middle" >应收运费[元]</td>
								<td align="center" valign="middle" >实收运费[元]</td>
								<td align="center" valign="middle" >交款状态</td>
								<td align="center" valign="middle" >交款时间</td>
								<td align="center" valign="middle" >审核时间</td>
							</tr>
							<%if(acfdList.size()>0){for(AccountCwbFareDetail acfd: acfdList){ %>
								<tr valign="middle">
									 	<td><input id="cwb" name="cwb" type="checkbox" value="<%=acfd.getCwb()%>" <%if(acfd.getFareid()>0){ %> disabled="disabled" <%}else{ %>checked="checked" <%} %> onClick="changeYj()" infactfare="<%=acfd.getInfactfare()%>"/></td>
									<td align="center" valign="middle" ><%=acfd.getCwb()%></td>
									<td align="center" valign="middle" ><%for(User u :userList){if(u.getUserid()==acfd.getUserid()){out.print(u.getRealname());}} %></td>
									<td align="center" valign="middle" ><%for(Customer c :customerlist){if(acfd.getCustomerid()==c.getCustomerid()){out.print(c.getCustomername());}} %></td>
								<td align="center" valign="middle" ><%for(CwbOrderTypeIdEnum ct : CwbOrderTypeIdEnum.values()){if(acfd.getCwbordertypeid()==ct.getValue()){out.print(ct.getText());}} %></td>
									<td align="center" valign="middle"  ><%for(Branch b :branchList){if(acfd.getDeliverybranchid()==b.getBranchid()){out.print(b.getBranchname());}} %></td>
									<td align="center" valign="middle" ><%=acfd.getAudittime()%></td>
									 <td align="center" valign="middle" ><%for(DeliveryStateEnum ds : DeliveryStateEnum.values()){if(acfd.getDeliverystate()==ds.getValue()){out.print(ds.getText());}} %></td>
									<td align="center" valign="middle" ><%=acfd.getShouldfare()%></td>
									<td align="center" valign="middle" ><%=acfd.getInfactfare()%></td>
									<td align="center" valign="middle" ><%=acfd.getVerifyflag()>0?"已审核":acfd.getFareid()>0?"已交款":"未交款"%></td>
									<td align="center" valign="middle" ><%=acfd.getPayuptime()%></td>
									<td align="center" valign="middle" ><%=acfd.getVerifytime()==null?"":acfd.getVerifytime()%></td>
								 </tr>
							 <%}} %>
							<tr valign="middle" >
								<td>合计</td>
								<td><strong><%=acfdList.size() %></strong>单</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><strong><%=shouldfare%></strong></td>
								<td><strong><%=infactfare%></strong></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<%
							if(faretypeid.equals("1")){
								 
							%>
						    <tr  valign="middle">
						       <td colspan ="20" align="center" valign="middle" >
							      <form id="updateForm" action ="<%=request.getContextPath()%>/funds/update"  method = "post">
				                      
				                      <input type="button"   id="add_button"   value="交款"/>
				                      <!-- 创建的ajax地址 -->
									  <input type="hidden" id="add" value="<%=request.getContextPath()%>/accountcwbfare/add" />
				                  </form>
			                  </td>
				            </tr>
				            <%} %>	
						</tbody>
					</table>
				</div>
				<!--滑动内容结束 -->
		   </div>
		</div>
		
<div class="jg_10"></div>
<div class="clear"></div>
<script type="text/javascript">

function exportfile(){
	if(<%=acfdList.size()>0%> ){
		$("#searchForm2").submit();
	}else{
		alert("没有数据无法导出！");
	}
}
</script> 
</body>
</HTML>

   
