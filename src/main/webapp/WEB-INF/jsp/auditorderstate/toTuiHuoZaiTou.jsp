<%@page import="cn.explink.controller.pda.BranchListBodyPdaResponse"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
/* String starttime=request.getParameter("begindate")==null?DateTimeUtil.getNowDate()+" 00:00:00":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowDate()+" 23:59:59":request.getParameter("enddate"); */
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<CwbOrderView> cwbList = (List<CwbOrderView>)request.getAttribute("cwbList");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
List<Reason> reasonList = (List<Reason>)request.getAttribute("reasonList");
Map<Long,String> mapbranch = (Map<Long,String>)request.getAttribute("mapbranch");
Map<Long,String> mapcustomer = (Map<Long,String>)request.getAttribute("mapcustomer");
Map<Long,String> mapcwbordertype = (Map<Long,String>)request.getAttribute("mapcwbordertype");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script type="text/javascript">
$(function(){
	
	$("table#gd_table tr:odd").css("backgroundColor","#f9fcfd");
	$("table#gd_table tr:odd").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#f9fcfd");	
	});
	$("table#gd_table tr:even").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#fff");	
	});//表单颜色交替和鼠标滑过高亮
	
	$("table#gd_table2 tr").click(function(){
			$(this).css("backgroundColor","#FFA500");
			$(this).siblings().css("backgroundColor","#ffffff");
			$("tr[cwbstate='no']").css("backgroundColor","#aaaaaa");
		});
	
	$(".index_dh li").mouseover(function(){
		//$(this).show(0)
		var $child = $(this).children(0);
		$child.show();
	});
	$(".index_dh li").mouseout(function(){
 
		$(".menu_box").hide();
	});
	
	$("tr[cwbstate='no']").css("backgroundColor","#aaaaaa");
});

function sub(){
	var datavalue = "[";
	
	if($('input[name="ischeck"]:checked').size()>0){
		$('input[name="ischeck"]:checked').each(function(index){
			$(this).attr("checked",false);
			var id=$(this).val()+"_cwbremark";
			//alert(id+"==="+$('#'+id).val());
			/* for(var i = 0 ; i < $("select[name^='reason']").length ; i++){
				datavalue[datavalue.length]=$("select[name^='reason']")[i].value;
			}
			
			for(var i = 0 ; i < $("input[name^='reason']").length ; i++){
				datavalue[datavalue.length]=$("input[name^='reason']")[i].value;
			} */
			datavalue = datavalue +"\""+$('#'+id).val()+"\",";
		});
	}
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
	}
	datavalue= datavalue + "]";
	if(datavalue.length>2){
		$.ajax({
			type: "POST",
			url:$("#SubFrom").attr("action"),
			data:{cwbremarks:datavalue},
			dataType:"html",
			success : function(data) {
				alert("成功修改状态："+data.split("_s_")[0]+"单\n订单状态无变动："+data.split("_s_")[1]+"单");
				searchForm.submit();
			}
		});
	}

	
}

function exportField(){
	if(<%=cwbList!=null&&cwbList.size()!=0%>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval").attr("disabled","disabled"); 
	 	$("#btnval").val("请稍后……");
		$("#searchForm2").submit();	
	}else{
		alert("没有做查询操作，不能导出！");
	}
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
	
});

</script>
</HEAD>
<BODY style="background:#f5f5f5"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div>
		<div class="kfsh_tabbtn">
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 ">
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="./toTuiHuoZaiTou" method="POST" id="searchForm">
								<span>
								<select name ="exportmould" id ="exportmould">
									<option  value ="0">导出模板</option>
									<%for(Exportmould e:exportmouldlist){%>
										<option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
									<%} %>
								</select>
								</span>  
								<table>
									<tr>
										<td rowspan="2">
											订单号：
											<textarea name="cwb" rows="3" class="kfsh_text" id="cwb" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
										</td>
										<td>
											订单类型:
											<select name ="cwbtypeid" id ="cwbtypeid">
												<option  value ="0">全部</option>
													<option value ="<%=CwbOrderTypeIdEnum.Peisong.getValue()%>"><%=CwbOrderTypeIdEnum.Peisong.getText()%></option>
													<option value ="<%=CwbOrderTypeIdEnum.Shangmentui.getValue()%>"><%=CwbOrderTypeIdEnum.Shangmentui.getText()%></option>
													<option value ="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()%>"><%=CwbOrderTypeIdEnum.Shangmenhuan.getText()%></option>
											</select>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											客户名称:
											<select name ="customerid" id ="customerid">
												<option  value ="0">全部</option>
												<%if(customerList!=null){ %>
													<%for(Customer cus:customerList){ %>
													<option value ="<%=cus.getCustomerid()%>"><%=cus.getCustomername()%></option>
													<%} %>
												<%} %>
											</select>
										</td>
										<td>
											配送站点:
											<select name ="branchid" id ="branchid">
												<option  value ="0">全部</option>
												 <%if(branchList!=null && branchList.size()>0) {%>
													<%for(Branch branch:branchList){ %>
													<option value ="<%=branch.getBranchid()%>"><%=branch.getBranchname()%></option>
													<%} }%>
											</select>
										</td>
									</tr>
									<tr>
										<td>
											退货入库时间:
												<input type ="text" name ="begindate" id="strtime"  value="" class="input_text1" style="height:20px;"/>
											到
												<input type ="text" name ="enddate" id="endtime"  value=""class="input_text1" style="height:20px;"/>
										</td>
									</tr>
								</table>
								<table>
									<tr>
										<td width="20%">
											<input type="submit" value="查询" class="input_button2">&nbsp;&nbsp;
											<input type="submit" value="重置" class="input_button2">&nbsp;&nbsp;
										</td>
										<td width="20%">
											<input type="submit" name="button" id="tuizai" value="退货再投" class="input_button2" onclick="sub()">
											<%if(cwbList!=null&&!cwbList.isEmpty()){%><span>
												<input name="" type="button" id="btnval" value="导出" class="input_button2" onclick="exportField();"/>
											</span> <%} %>
										</td>
									</tr>
								</table>
								
							</form>
						<form action="<%=request.getContextPath()%>/cwborder/exportExcle" method="post" id="searchForm2">
							<input type="hidden" name="exportmould2" id="exportmould2"  class="input_button2" />
						</form>
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="40" align="center" valign="middle" bgcolor="#E7F4E3"><a href="#" onclick="btnClick();">全选</a></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">收件人</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">收件人地址</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">退货库入库时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">审核为退货再投</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">配送站点</td>
									<td width="200" align="center" valign="middle" bgcolor="#E7F4E3">备注</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:135px"></div>
					<from action="./auditTuiHuoZaiTou" method="post" id="SubFrom" >
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2" >
							<tbody>
								<%if(cwbList!=null){ 
									for(CwbOrderView cwb :cwbList){ %>
									<tr height="30">
										<td width="40" align="center" valign="middle" bgcolor="#E7F4E3"><input id="ischeck" name="ischeck" type="checkbox" value="<%=cwb.getScancwb() %>" <%if(cwb.getCwbstate()==CwbStateEnum.TuiHuo.getValue()||cwb.getCwbstate()==CwbStateEnum.TuiGongYingShang.getValue()){ %><%} %>></td>
										<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=cwb.getCwb()%></td>
										<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=mapcwbordertype.get(cwb.getCwbordertypeid()) %></td>
										<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=mapcustomer.get(cwb.getCustomerid()) %></td>
										<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=cwb.getConsigneename() %></td>
										<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=cwb.getConsigneeaddress() %></td>
										<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=cwb.getTuihuozhaninstoreroomtime() %></td>
										<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%="是" %></td>
										<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=mapbranch.get(cwb.getTuihuoid()) %></td>
										<td width="200" align="center" valign="middle">
										<%if(cwb.getCwbstate()!=CwbStateEnum.TuiHuo.getValue()&&cwb.getCwbstate()!=CwbStateEnum.TuiGongYingShang.getValue()){ %>
												<%=cwb.getCwbremark() %>
										<input type="hidden" id="<%=cwb.getCwb() %>_cwbremark" name="<%=cwb.getCwb() %>_cwbremark" value="<%=cwb.getCwb() %>_s_0"/>
										<%}else{ %>
										<select name="<%=cwb.getCwb() %>_cwbremark" id="<%=cwb.getCwb() %>_cwbremark">
										<option value="">请选择退货再投原因</option>
										<%for(Reason r :reasonList) {%><option value="<%=cwb.getCwb() %>_s_<%=r.getReasonid() %>"><%=r.getReasoncontent() %></option><%} %>
											</select>
										<%} %>
										</td>
									</tr>
								<%} }%>
						</table>
					</from>
				</div>
				<%-- <div style="height:40px"></div>
				<%if(cwbList!=null){ %>
				<div class="iframe_bottom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" id="gd_table2">
						<tbody>
							<tr height="30" >
								<td align="center" valign="middle" bgcolor="#f3f3f3"><input type="submit" name="button" id="button" value="退货再投" class="input_button1" onclick="sub()"></td>
							</tr>
						</tbody>
					</table>
				</div><%} %>  --%>
		</div>
	</div>
</div>
</BODY>
</HTML>
