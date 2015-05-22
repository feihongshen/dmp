<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.AccountCwbFareDetail"%>
<%@page import="cn.explink.domain.Customer"%>
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
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
Date now = new Date();
  String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
  Page page_obj = (Page)request.getAttribute("page_obj");
  List customeridList =(List) request.getAttribute("customeridStr");
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
	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择供货商' });
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
<form id="createForm" action="<%=request.getContextPath()%>/accountcwbfare/payfare" method="post">
	<!-- 弹出框开始 -->
	<div id="alert_box" style="display:none">
	  <div id="box_bg" ></div>
	  <div id="box_contant" >
	    <div id="box_top_bg"></div>
	    <div id="box_in_bg">
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
	    </div>
	  </div>
	  <div id="box_yy"></div>
	</div>
	<!-- 弹出框结束 -->
</form>	
   <div class="menucontant">
		<form id="searchForm" action ="<%=request.getContextPath()%>/accountcwbfaredetail/accountfarelist/1" method = "post">
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1" border="1">
			  <tr>
					<td width="100%">
						供货商:
						<select name ="customerid" id ="customerid" multiple="multiple" style="width: 300px;">
				          <%for(Customer c : customerlist){ %>
				           <option value ="<%=c.getCustomerid() %>" 
				            <%if(!customeridList.isEmpty()) 
					            {for(int i=0;i<customeridList.size();i++){
					            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
					            		%>selected="selected"<%
					            	 break;
					            	}
					            }
						     }%>><%=c.getCustomername() %></option>
				          <%} %>
				        </select>
				        [<a href="javascript:multiSelectAll('customerid',1,'请选择');">全选</a>]
						[<a href="javascript:multiSelectAll('customerid',0,'请选择');">取消全选</a>]
						交款状态:
						<select id="ispay" name="ispay" onchange="changeTime();">
							<option value="0" <%=request.getParameter("ispay")!=null&&request.getParameter("ispay").equals("0")?"selected":"" %>>未交款</option>
							<option value="1" <%=request.getParameter("ispay")!=null&&request.getParameter("ispay").equals("1")?"selected":"" %>>已交款</option>
						</select>
						
						<select id="isaudittime" name="isaudittime" disabled="disabled">
							<option value="1" <%=request.getParameter("isaudittime")!=null&&request.getParameter("isaudittime").equals("1")?"selected":"" %>>审核时间</option>
							<option value="2" <%=request.getParameter("isaudittime")!=null&&request.getParameter("isaudittime").equals("2")?"selected":"" %>>交款时间</option>
						</select>
							<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
						到
							<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
						<font color="red">（查询31天以内数据）</font>
					        <br/>
						配送站点：<select name ="deliverybranchid" id="deliverybranchid">
				               <option value="-1">全部</option>
				               <%if(branchList != null && branchList.size()>0){ %>
				                <%for( Branch b:branchList){ %>
				               <option value ="<%=b.getBranchid()%>" <%if(b.getBranchid() == new Long(request.getParameter("deliverybranchid")==null?"-1":request.getParameter("deliverybranchid"))) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
				               <%} }%>
			              </select>
						 配送结果：
						<select name ="deliverystate" id ="deliverystate" >
							<option value ="-1">请选择</option>
							<option value="<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue() %>" <%=request.getParameter("deliverystate")!=null&&request.getParameter("deliverystate").equals(DeliveryStateEnum.ShangMenTuiChengGong.getValue())?"selected":"" %>><%=DeliveryStateEnum.ShangMenTuiChengGong.getText() %></option>
							<option value="<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>" <%=request.getParameter("deliverystate")!=null&&request.getParameter("deliverystate").equals(DeliveryStateEnum.ShangMenJuTui.getValue())?"selected":"" %>><%=DeliveryStateEnum.ShangMenJuTui.getText() %></option>
							<option value="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>" <%=request.getParameter("deliverystate")!=null&&request.getParameter("deliverystate").equals(DeliveryStateEnum.FenZhanZhiLiu.getValue())?"selected":"" %>><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
							<option value="<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue() %>" <%=request.getParameter("deliverystate")!=null&&request.getParameter("deliverystate").equals(DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue())?"selected":"" %>><%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getText() %></option>
							<option value="<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>" <%=request.getParameter("deliverystate")!=null&&request.getParameter("deliverystate").equals(DeliveryStateEnum.HuoWuDiuShi.getValue())?"selected":"" %>><%=DeliveryStateEnum.HuoWuDiuShi.getText() %></option>
						 </select>
						 应收运费：
						<select name ="shoulefarefeesign" id ="shoulefarefeesign" >
							<option value="1" <%=request.getParameter("shoulefarefeesign")!=null&&request.getParameter("shoulefarefeesign").equals("1")?"selected":"" %>>大于0</option>
							<option value="0" <%=request.getParameter("shoulefarefeesign")!=null&&request.getParameter("shoulefarefeesign").equals("0")?"selected":"" %>>等于0</option>
						</select>	
						 每页<select name="pageNumber" id="pageNumber">
								<option value="100">100</option>
								<option value="300">300</option>
								<option value="500">500</option>
								<option value="800">800</option>
								<option value="1000">1000</option>
							</select>行
					<input type ="button" value ="查询" class="input_button2" onclick="sumitForm();"/>
					<input type="button" value="导出" class="input_button2"  onclick="exportfile()">
					</td>
				</tr>
				</table>
		 </form>
		<form action="<%=request.getContextPath()%>/accountcwbfaredetail/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="begindate" value="<%=starttime%>"/>
		<input type="hidden" name="enddate" value="<%=endtime%>"/>
		<input type="hidden" name="ispay" value="<%=request.getParameter("ispay")==null?"0":request.getParameter("ispay")%>"/>
		<input type="hidden" name="deliverybranchid" value="<%=request.getParameter("deliverybranchid")==null?"0":request.getParameter("deliverybranchid")%>"/>
		<input type="hidden" name="deliverystate" value="<%=request.getParameter("deliverystate")==null?"-1":request.getParameter("deliverystate")%>"/>
		<input type="hidden" name="shoulefarefeesign" value="<%=request.getParameter("shoulefarefeesign")==null?"1":request.getParameter("shoulefarefeesign")%>"/>
		<div style="display: none;">
			<select name ="customerid" id ="customerid" multiple="multiple" style="width: 300px;">
	          <%for(Customer c : customerlist){ %>
	           <option value ="<%=c.getCustomerid() %>" 
	            <%if(!customeridList.isEmpty()) 
		            {for(int i=0;i<customeridList.size();i++){
		            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
		            		%>selected="selected"<%
		            	 break;
		            	}
		            }
			     }%>><%=c.getCustomername() %></option>
	          <%} %>
	        </select>
		</div>
	</form>
 
	<div>	
		<h1 style="line-height:30px; font-size:18px; font-family:'微软雅黑', '黑体'; font-weight:bold; color:#369"><%="1".equals(request.getParameter("ispay"))?"已交款":"未交款" %>记录：</h1>	
		<div>	
				<!--滑动内容开始 -->
				<div >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
						<tbody>
							<tr class="font_1">
								<td width="100" align="center" valign="middle" ><a style="cursor: pointer;" onclick="isgetallcheck();">[全选/反选]</a></td>
								<td width="200" align="center" valign="middle" >订单号</td>
								<td align="center" valign="middle" >供货商</td>
								<td align="center" valign="middle" >订单类型</td>
								<td align="center" valign="middle" >配送站点</td>
								<td align="center" valign="middle" >审核时间</td>
								<td align="center" valign="middle" >配送结果</td>
								<td align="center" valign="middle" >应收运费[元]</td>
								<td align="center" valign="middle" >实收运费[元]</td>
								<td align="center" valign="middle" >交款状态</td>
								<td align="center" valign="middle" >交款时间</td>
							</tr>
							<%if(acfdList.size()>0){for(AccountCwbFareDetail acfd: acfdList){ %>
								<tr valign="middle">
									<td><input id="cwb" name="cwb" type="checkbox" value="<%=acfd.getCwb()%>" <%if(acfd.getFareid()>0){ %> disabled="disabled" <%}else{ %>checked="checked" <%} %> onClick="changeYj()" infactfare="<%=acfd.getInfactfare()%>"/></td>
									<td align="center" valign="middle" ><%=acfd.getCwb()%></td>
									<td align="center" valign="middle" ><%for(Customer c :customerlist){if(acfd.getCustomerid()==c.getCustomerid()){out.print(c.getCustomername());}} %></td>
									<td align="center" valign="middle" ><%for(CwbOrderTypeIdEnum ct : CwbOrderTypeIdEnum.values()){if(acfd.getCwbordertypeid()==ct.getValue()){out.print(ct.getText());}} %></td>
									<td align="center" valign="middle"  ><%for(Branch b :branchList){if(acfd.getDeliverybranchid()==b.getBranchid()){out.print(b.getBranchname());}} %></td>
									<td align="center" valign="middle" ><%=acfd.getAudittime()%></td>
									<td align="center" valign="middle" ><%for(DeliveryStateEnum ds : DeliveryStateEnum.values()){if(acfd.getDeliverystate()==ds.getValue()){out.print(ds.getText());}} %></td>
									<td align="center" valign="middle" ><%=acfd.getShouldfare()%></td>
									<td align="center" valign="middle" ><%=acfd.getInfactfare()%></td>
									<td align="center" valign="middle" ><%=acfd.getFareid()>0?"已交款":"未交款"%></td>
									<td align="center" valign="middle" ><%=acfd.getPayuptime()%></td>
								 </tr>
							 <%}} %>
							<tr valign="middle" >
								<td>合计</td>
								<td><strong><%=page_obj.getTotal() %></strong>单</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><strong><%=accountCwbFareDetailSum.getShouldfare()==null?BigDecimal.ZERO:accountCwbFareDetailSum.getShouldfare() %></strong></td>
								<td><strong><%=accountCwbFareDetailSum.getInfactfare()==null?BigDecimal.ZERO:accountCwbFareDetailSum.getInfactfare() %></strong></td>
								<td></td>
								<td></td>
							</tr>
							<%if(request.getParameter("ispay")==null||Long.parseLong(request.getParameter("ispay"))==0){%>
						    <tr  valign="middle">
						       <td colspan ="20" align="center" valign="middle" >
							      <form id="updateForm" action ="<%=request.getContextPath()%>/funds/update"  method = "post">
				                      
				                      <input type="button" id="updateF"  value="交款"/>
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
		<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
		<%if(page_obj.getMaxpage()>1){ %>
		<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
								<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>"><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
			</table>
		</div>
	<%} %>
	</div>
<div class="jg_10"></div>
<div class="clear"></div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);

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
   
