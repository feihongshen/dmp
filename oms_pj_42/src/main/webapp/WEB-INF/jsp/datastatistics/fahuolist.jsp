<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.springframework.ui.Model"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Common"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrder> orderlist = (List<CwbOrder>)request.getAttribute("orderlist");
  List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
 
  List<Branch> kufanglist = (List<Branch>)request.getAttribute("kufangList");
  List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
  List customeridList =(List) request.getAttribute("customeridStr");
  List cwbordertypeidList =(List) request.getAttribute("cwbordertypeidStr");
  List kufangidList =(List) request.getAttribute("kufangidStr");
  
  
  String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
  long count = (Long)request.getAttribute("count");
  Page page_obj = (Page)request.getAttribute("page_obj");
  long flowordertype=(request.getParameter("flowordertype")==null?-1:Long.parseLong(request.getParameter("flowordertype")));
  String servicetype = request.getParameter("servicetype")==null?"全部":request.getParameter("servicetype");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户发货统计</title>
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
<script type="text/javascript">
	$(document).ready(function() {
		if(<%=request.getAttribute("nouser") != null%>){
			alert("登录已失效，请重新登录！");
		}
	   //获取下拉框的值
	   $("#find").click(function(){
	         var checkval="";
	         $("label").each(function(index){
			     //找到被选中的项
			     if($(this).attr("class")=="checked"){
			        checkval+=$(this).children().attr("value")+",";
			     }
	         });
	         $("#controlStr").val(checkval);
	         if(check()){
	        	 if($(":checked[name=kufangid]").length==0){
		        	 multiSelectAll('kufangid',1,'请选择');}
	            $("#isshow").val(1);
		    	$("#searchForm").submit();
		    	$("#find").attr("disabled","disabled");
				$("#find").val("请稍等..");
	         }
	   });
	});

function check(){
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if($("#endtime").val()>"<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"){
		$("#endtime").val("<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>");
	}
	return true;
}
function checkBeishu(id,num){
	// 如用户倍数框留空，光标离开倍数输入框，则倍数输入框默认为1.
	
	if($('#'+id).val()==''||$('#'+id).val()==undefined 
			|| $('#'+id).val()==null||Number($('#'+id).val())<0){
		$('#'+id).val('');
		$('#'+id).focus();
		$('#'+id).select();
	}
	//判断倍数是否在num倍之间
	if(Number($('#'+id).val())> num){
		   $('#'+id).val(num);
		   $('#'+id).focus();
		   $('#'+id).select();
		} 
	//自动转换为半角，不支持标点、小数点以及英文字母等其他输入。
	 var pattern = /^-?\d+$/;
	if(isNaN($('#'+id).val()) || $('#'+id).val().search(pattern)!=0){
	    $('#'+id).val('');
		$('#'+id).focus();
		$('#'+id).select();
		return false;
	}
	return true;
}
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
	$("#cwbordertypeid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
	$("#kufangid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
	
});
function searchFrom(){
	$('#searchForm').attr('action',1);return true;
}
function delSuccess(data){
	$("#searchForm").submit();
}
function orderForm(ordername){
	if($("#orderByTypeId").val()=="ASC"){
    	$("#orderByNameId").val(ordername);
    	$("#orderByTypeId").val("DESC");
    }else {
    	$("#orderByNameId").val(ordername);
    	$("#orderByTypeId").val("ASC");
    }
	if(check()){
		$("#searchForm").submit();
	}
}
function clearSelect(){
	$("#customerid").val(0);//供货商
	$("#strtime").val('');//开始时间
	$("#endtime").val('');//结束时间
	$("#kufangid").val(-1);//发货库房
	$("#cwbordertypeid").val(-1);//订单类型
}
</script>
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form action="1" method="post" id="searchForm">
	
	<input type="hidden" id="orderByNameId" name="orderbyName" value="emaildate"/>
	<input type="hidden" id="orderByTypeId" name="orderbyType" value="<%=request.getParameter("orderbyType")==null?"DESC":request.getParameter("orderbyType") %>"/>
	<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
	<input type="hidden" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:40px">
	<tr>
		<td align="left">
			发货时间
				<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
			到
				<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
		    发货库房
			<select name ="kufangid" id ="kufangid" multiple="multiple" style="width: 320px;">
		          <%if(kufanglist!=null && kufanglist.size()>0) {%>
		          <%for(Branch b : kufanglist){ %>
			 		<option value ="<%=b.getBranchid() %>" 
		          <%if(!kufangidList.isEmpty()) 
			            {for(int i=0;i<kufangidList.size();i++){
			            	if(b.getBranchid()== new Long(kufangidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname()%></option>
		          <%} }%>
			</select>
			 供货商
			 <select name ="customerid" id ="customerid" multiple="multiple" style="width: 300px;">
		          <%for(Customer c : customerlist){ %>
		           <option value ="<%=c.getCustomerid() %>" 
		            <%if(customeridList!=null&&customeridList.size()>0) 
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
		        <br/>
		    订单类型
			<select name ="cwbordertypeid" id ="cwbordertypeid" multiple="multiple" >
		          <%for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){ %>
						<option value ="<%=c.getValue() %>" 
		           <%if(!cwbordertypeidList.isEmpty()) 
			            {for(int i=0;i<cwbordertypeidList.size();i++){
			            	if(c.getValue()== new Long(cwbordertypeidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=c.getText()%></option>
		          <%} %>
			</select>
		订单状态
			<select name ="flowordertype" id ="flowordertype">
				<option value ="-1">请选择订单状态</option>
				<option value="<%=FlowOrderTypeEnum.DaoRuShuJu.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.DaoRuShuJu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.DaoRuShuJu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.TiHuo.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.TiHuo.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.TiHuo.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.RuKu.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.RuKu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.RuKu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.FenZhanLingHuo.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.FenZhanLingHuo.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.ChuKuSaoMiao.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.ChuKuSaoMiao.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.TuiHuoChuZhan.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.TuiHuoChuZhan.getValue()){ %>selected<%}%> ><%=FlowOrderTypeEnum.TuiHuoChuZhan.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.YiShenHe.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.YiShenHe.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.YiShenHe.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.YiFanKui.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.YiFanKui.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.YiFanKui.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.PosZhiFu.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.PosZhiFu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.PosZhiFu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.CheXiaoFanKui.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.CheXiaoFanKui.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.CheXiaoFanKui.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.YiChangDingDanChuLi.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.YiChangDingDanChuLi.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.YiChangDingDanChuLi.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.DingDanLanJie.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.DingDanLanJie.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.DingDanLanJie.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.ShenHeWeiZaiTou.getText() %></option>
			</select>
			 服务类型
		    <select name="servicetype" id="servicetype">
		        <option value="全部" <%if("全部".equals(servicetype)){ %>selected<%}%>>全部</option>
		        <option value="B2C" <%if("B2C".equals(servicetype)){ %>selected<%}%>>B2C</option>
		        <option value="仓配服务" <%if("仓配服务".equals(servicetype)){ %>selected<%}%>>仓配服务</option>
		        <option value="配送服务" <%if("配送服务".equals(servicetype)){ %>selected<%}%>>配送服务</option>
		    </select>
			<input type="button" id="find" onclick="" value="查询" class="input_button2" />
			&nbsp;&nbsp;<input type="reset"  value="清空" onclick="clearSelect();" class="input_button2" />
			<%if(orderlist != null && orderlist.size()>0){  %>
			<select name ="exportmould" id ="exportmould">
	          <option value ="0">默认导出模板</option>
	          <%for(Exportmould e:exportmouldlist){%>
	           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
	          <%} %>
			</select>
			<input type="button" value="导出报表"  id="btnval10" class="input_button1" onclick="exportField('0','10',<%=ModelEnum.KeHuFaHuoHuiZong.getValue() %>);" />
			
			<%} %>
			<%if(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)==1){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval0" value="导出1-<%=count %>" class="input_button1" onclick="exportField('0','0',<%=ModelEnum.KeHuFaHuoTongJi.getValue() %>);"/>
			<%}else{for(int j=0;j<count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0);j++){ %>
				<%if(j==0){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出1-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('0','<%=j%>',<%=ModelEnum.KeHuFaHuoTongJi.getValue() %>);"/>
				<%}else if(j!=0&&j!=(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>',<%=ModelEnum.KeHuFaHuoTongJi.getValue() %>);"/>
				<%}else if(j==(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=count %>" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>',<%=ModelEnum.KeHuFaHuoTongJi.getValue() %>);"/>
				<%} %>
			<%}} %>
			
		</td>
	</tr>
</table>
	</form>
	<form action="<%=request.getContextPath() %>/datastatistics/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="begin" id="begin" value="0"/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
		<input type="hidden" name="sign" id="sign" value="<%=ModelEnum.KeHuFaHuoTongJi.getValue() %>"/>
		<input type="hidden" name="begindate1" id="begindate1" value="<%=starttime%>"/>
		<input type="hidden" name="enddate1" id="enddate1" value="<%=endtime%>"/>
		<input type="hidden" name="orderbyName1" id="orderbyName1" value="<%=request.getParameter("orderbyName")==null?"emaildate":request.getParameter("orderbyName")%>"/>
		<input type="hidden" name="orderbyType1" id="orderbyType1" value="<%=request.getParameter("orderbyType")==null?"DESC":request.getParameter("orderbyType") %>"/>
		<input type="hidden" name="servicetype1" id="servicetype1" value="<%=request.getParameter("servicetype")==null?"全部":request.getParameter("servicetype") %>"/>
		<div style="display: none;">
		<select name ="kufangid1" id ="kufangid1" multiple="multiple" style="width: 320px;">
		          <%if(kufanglist!=null && kufanglist.size()>0) {%>
		          <%for(Branch b : kufanglist){ %>
			 			<option value ="<%=b.getBranchid() %>" 
		          <%if(!kufangidList.isEmpty()) 
			            {for(int i=0;i<kufangidList.size();i++){
			            	if(b.getBranchid()== new Long(kufangidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname()%></option>
		          <%} }%>
			</select>
			<select name ="customerid1" id ="customerid1" multiple="multiple" >
		          <%for(Customer c : customerlist){ %>
		           <option value ="<%=c.getCustomerid() %>" 
		            <%if(customeridList!=null&&customeridList.size()>0) 
			            {for(int i=0;i<customeridList.size();i++){
			            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>
		           ><%=c.getCustomername() %></option>
		          <%} %>
	        </select>
			 <select name ="cwbordertypeid1" id ="cwbordertypeid1" multiple="multiple">
		          <option value ="-1">请选择</option>
		          <%for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){ %>
						<option value ="<%=c.getValue() %>"
						<%if(!cwbordertypeidList.isEmpty()) 
			            {for(int i=0;i<cwbordertypeidList.size();i++){
			            	if(c.getValue()== new Long(cwbordertypeidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=c.getText()%></option>
		          <%} %>
			</select>
			<select name ="flowordertype1" id ="flowordertype1">
		          <option value ="-1">请选择订单状态</option>
		          <%for(FlowOrderTypeEnum c : FlowOrderTypeEnum.values()){ %>
			<option value =<%=c.getValue() %> 
		           <%if(c.getValue()==flowordertype){ %>selected="selected" <%} %> ><%=c.getText()%></option>
		          <%} %>
			</select>
		</div>
	</form>
	</div>
	<div class="right_title">
	<div style="height:60px"></div><%if(orderlist != null && orderlist.size()>0){  %>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('customerid');" >供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('emaildate');" >发货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('receivablefee');" >应收金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwbordertypeid');" >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('deliverybranchid');" >配送站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >出库时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >支付方式</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >服务类型</td>
		</tr>
		
		<% for(CwbOrder  c : orderlist){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><%=c.getCwb() %></td>
					<td  align="center" valign="middle"><%=c.getCustomername()  %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=c.getReceivablefee() %></td>
					<td  align="center" valign="middle"><%=c.getOrderType() %></td>
					<td  align="center" valign="middle"><%=c.getDeliverybranch() %></td>
					<td  align="center" valign="middle"><%=c.getOutstoreroomtime() %></td>
					<td  align="center" valign="middle"><%=c.getPaytypeName()%></td>
					<td  align="center" valign="middle"><%=c.getCartype() %></td>
				 </tr>
		 <%} %>
		 <%if(request.getAttribute("count")!= null){ %>
		<tr bgcolor="#FF3300">
			<td  align="center" valign="middle" class="high">合计：<font color="red"><%=count %></font>&nbsp;单  </td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle" class="high"><font color="red"><%=request.getAttribute("sum")==null?"0.00":request.getAttribute("sum") %></font>&nbsp;元 </td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
		</tr>
		<%} %>
	</table>
		
	<%-- <tr>
	<td>代收金额总计：<font color="red"><%=request.getAttribute("sum")==null?"0.00":request.getAttribute("sum") %></font>&nbsp;元 </td>
	</tr>
	<tr>
	<td>代退金额总计：<font color="red"><%=request.getAttribute("paybackfeesum")==null?"0.00":request.getAttribute("paybackfeesum") %></font>&nbsp;元 </td>
	</tr> --%>
	</div><%} %>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
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
</script>

<script type="text/javascript">
function exportField(page,j,sign){
		if($("#isshow").val()=="1"&&<%=orderlist != null && orderlist.size()>0  %>){
			$("#exportmould2").val($("#exportmould").val());
			$("#btnval"+j).attr("disabled","disabled");
			$("#btnval"+j).val("请稍后……");
			$("#begin").val(page);
			$("#sign").val(sign);
		 	$.ajax({
				type: "POST",
				url:$("#searchForm2").attr("action"),
				data:$("#searchForm2").serialize(),
				dataType:"json",
				success : function(data) {
					if(data.errorCode==0){
						alert("已进入导出任务队列！");
						$("#downCount",parent.document).html(Number($("#downCount",parent.document).html())+1);
						$.ajax({
							type: "POST",
							url:"<%=request.getContextPath()%>/datastatistics/commitExportExcle",
							data:$("#searchForm2").serialize(),
							dataType:"json",
							success : function(data) {
								
							}
						});
					}else{
						alert(data.remark);
					}
				}
			});
		}else{
			alert("没有做查询操作，不能导出！");
		}
}

</script>


</body>
</html>

