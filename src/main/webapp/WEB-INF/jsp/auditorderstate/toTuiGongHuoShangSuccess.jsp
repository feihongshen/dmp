<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<CwbOrderView> cwbList = (List<CwbOrderView>)request.getAttribute("cwbList");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");

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
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

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

function check(){
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	else{
		return true;
	}
}
//退客户成功
function sub(){
	var datavalue = "[";
	
	if($('input[name="ischeck"]:checked').size()>0){
		$('input[name="ischeck"]:checked').each(function(index){
			$(this).attr("checked",false);
			datavalue = datavalue +"\""+$(this).val()+"\",";
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
		data:{cwbs:datavalue},
		dataType:"html",
		success : function(data) {
			alert("成功修改状态："+data.split("_s_")[0]+"单\n订单状态无变动："+data.split("_s_")[1]+"单");
			searchForm.submit();
		}
	});
	}
	
}

//退客户成功
function sub2(){
	var datavalue = "[";
	
	if($('input[name="ischeck"]:checked').size()>0){
		$('input[name="ischeck"]:checked').each(function(index){
			$(this).attr("checked",false);
			datavalue = datavalue +"\""+$(this).val()+"\",";
		});
	}
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
	}
	datavalue= datavalue + "]";
	if(datavalue.length>2){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/cwborder/auditTuiGongHuoShangFailure",
			data:{cwbs:datavalue},
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

</script>
</HEAD>
<BODY style="background:#f5f5f5"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div>
		<div class="kfsh_tabbtn">
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="./toTuiGongHuoShangSuccess" method="post" id="searchForm">
								<%if(cwbList!=null){ %><span>
									<select name ="exportmould" id ="exportmould">
											<option  value ="0">导出模板</option>
											<%for(Exportmould e:exportmouldlist){%>
												<option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
											<%} %>
									</select></span>
								<%} %> 
								<table>
									<tr>
										<td rowspan="2">
											订单号：
											<textarea name="cwb" rows="3" class="kfsh_text" id="cwb" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
										</td>
										<td>
											&nbsp;&nbsp;
											订单类型:
											<select name ="cwbtypeid" id ="cwbtypeid">
												<option  value ="0">全部</option>
													<option value ="<%=CwbOrderTypeIdEnum.Peisong.getValue()%>"><%=CwbOrderTypeIdEnum.Peisong.getText()%></option>
													<option value ="<%=CwbOrderTypeIdEnum.Shangmentui.getValue()%>"><%=CwbOrderTypeIdEnum.Shangmentui.getText()%></option>
													<option value ="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()%>"><%=CwbOrderTypeIdEnum.Shangmenhuan.getText()%></option>
											</select>
										</td>
										<td>
											&nbsp;&nbsp;
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
									</tr>
									<tr>
										<td>
											&nbsp;&nbsp;
											确认状态:
											<select name ="resultstate" id ="resultstate">
												<option  value ="0">全部</option>
													<option value = "<%=FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()%>">退客户成功</option>
													<option value ="<%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() %>">拒收退货</option>
											</select>
										</td>
										<td>
											&nbsp;&nbsp;
											退客户出库时间:
											<input type ="text" name ="begindate" id="strtime"  value=""/>
											到
											<input type ="text" name ="enddate" id="endtime"  value=""/>
										</td>
										<td>
										</td>
									</tr>
								</table>
								<table>
									<tr>
										<td width="20%">
											<input type="submit"  value="查询" class="input_button2">&nbsp;&nbsp;
											<input type="button" onclick="" value="重置" class="input_button2">&nbsp;&nbsp;
										</td>
										<td width="20%">
											<input type="button" onclick="sub()" value="退客户成功" class="input_button2">&nbsp;&nbsp;
											<input type="button" onclick="sub2()" value="拒收退货" class="input_button2">&nbsp;&nbsp;
											<%if(cwbList!=null&&!cwbList.isEmpty()){ %>
												<input name="" type="button" id="btnval" value="导出" class="input_button2" onclick="exportField();"/>
											<%} %>
										</td>
									</tr>
									
								</table>
							</form>
							<form action="<%=request.getContextPath()%>/cwborder/exportExcle" method="post" id="searchForm2">
								<input type="hidden" name="exportmould2" id="exportmould2" />
							</form>
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="40" align="center" valign="middle" bgcolor="#E7F4E3"><a href="#" onclick="btnClick();">全选</a></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单金额</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">退客户出库时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">确认状态</td>
								</tr>
							</tbody>
						</table> 
					</div>
					<div style="height:100px"></div>
					<from action="./auditTuiGongHuoShangSuccess" method="post" id="SubFrom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2" >
						<tbody>
						<%if(cwbList!=null){ %>
							<%for(CwbOrderView cwb :cwbList){ %>
								<tr height="30" cwbFlowordertype="<%=cwb.getFlowordertype() %>"  cwbstate="<%
								if(cwb.getFlowordertype()!=FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()&&cwb.getCwbstate()!=CwbStateEnum.TuiHuo.getValue()&&
										!((cwb.getSendcarnum()>0||cwb.getBackcarnum()>0)&&cwb.getTranscwb().length()>0&&!cwb.getCwb().equals(cwb.getTranscwb())&&cwb.getFlowordertype()==FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue())){
									out.print("no");
								} %>"	>
								<td width="60" align="center" valign="middle" bgcolor="#f3f3f3">
									<%if(cwb.getFlowordertype()!=FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()&&cwb.getCwbstate()!=CwbStateEnum.TuiHuo.getValue()&&
											!((cwb.getSendcarnum()>0||cwb.getBackcarnum()>0)&&cwb.getTranscwb().length()>0&&!cwb.getCwb().equals(cwb.getTranscwb())&&cwb.getFlowordertype()==FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue())){%>
										<input id="ischeck" name="ischeck" type="checkbox" value="<%=cwb.getScancwb() %>_s_0">
									<%}else{ %>
										<input id="ischeck" name="ischeck" type="checkbox" value="<%=cwb.getScancwb() %>_s_1" checked="checked">
									<%} %>
								</td>
								<td width="100" align="center" valign="middle"><%=cwb.getCwb() %></td>
								<td width="100" align="center" valign="middle"><%=cwb.getCwbordertypename() %></td>
								<td width="100" align="center" valign="middle"><%=cwb.getCustomername()%></td>
								<td width="100" align="right" valign="middle"><strong><%=cwb.getReceivablefee() %></strong></td>
								<td width="100" align="center" valign="middle"><%=cwb.getEmaildate() %></td>
								<td width="100" align="center" valign="middle"><%=cwb.getTuigonghuoshangchukutime() %></td>
								<td width="100" align="center" valign="middle">
								<%if(cwb.getFlowordertype()==FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()){%>
									<input id="flowordertype" name="flowordertype" value="退客户成功" />
								<%}else if(cwb.getFlowordertype()==FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()){%>
									<input id="flowordertype" name="flowordertype" value="拒收退货" />
								<%}else{ %>	
									<input id="flowordertype" name="flowordertype" value="" />
								<%} %>									
								</td>
							</tr>
						<%} }%>
						</tbody>
					</table>
					</from>
				</div>
				
				<div style="height:40px"></div><%if(cwbList!=null){ %>
				<div class="iframe_bottom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" id="gd_table2">
						<tbody>
							<tr height="30" >
								<td align="center" valign="middle" bgcolor="#f3f3f3"><input type="submit" name="button" id="button" value="提交" class="input_button1" onclick="sub();"></td>
							</tr>
						</tbody>
					</table>
				</div><%} %>
		</div>
	</div>
</div>
</BODY>
</HTML>
