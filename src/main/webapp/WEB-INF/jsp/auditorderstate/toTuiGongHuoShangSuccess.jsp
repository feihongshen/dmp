<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Exportmould"%>
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
			<ul>
				<li><a href="<%=request.getContextPath() %>/cwborder/toTuiHuoZaiTou">审核为退货再投</a></li>
				<li><a href="<%=request.getContextPath() %>/orderBackCheck/toTuiHuoCheck">退货出站审核</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toChangeZhongZhuan">中转出站审核</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toTuiGongHuoShangSuccess" class="light">客户收退货确认</a></li>
			</ul>
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
									</select>
									<input name="" type="button" id="btnval" value="导出excel" class="input_button2" onclick="exportField();"/>
								</span><%} %> 订单号：
								<textarea name="cwb" rows="3" class="kfsh_text" id="cwb" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
								<input type="submit" value="确定" class="input_button2">
							</form>
							<form action="<%=request.getContextPath()%>/cwborder/exportExcle" method="post" id="searchForm2">
								<input type="hidden" name="exportmould2" id="exportmould2" />
							</form>
						</div><%if(cwbList!=null){ %>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="60" align="center" valign="middle" bgcolor="#f3f3f3">选择</td>
									<td width="150" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">拒收时间</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">应收金额（元）</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">退供货商出站时间</td>
								</tr>
							</tbody>
						</table><%} %>
					</div>
					<div style="height:100px"></div>
					<from action="./auditTuiGongHuoShangSuccess" method="post" id="SubFrom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2" >
						<tbody>
							<%
							if(cwbList!=null)
							for(CwbOrderView cwb :cwbList){ %>
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
								<td width="150" align="center" valign="middle"><%=cwb.getCwb() %></td>
								<td width="120" align="center" valign="middle"><%=cwb.getCustomername()%></td>
								<td width="120" align="center" valign="middle"><%=cwb.getEmaildate() %></td>
								<td width="120" align="center" valign="middle"><%=cwb.getJushoutime() %></td>
								<td width="120" align="right" valign="middle"><strong><%=cwb.getReceivablefee() %></strong></td>
								<td width="120" align="center" valign="middle"><%=cwb.getTuigonghuoshangchukutime() %></td>
							</tr>
							<%} %>
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
