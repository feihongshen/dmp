<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<CwbOrderView> cwbList = (List<CwbOrderView>)request.getAttribute("cwbList");
List<Reason> reasonList = (List<Reason>)request.getAttribute("reasonList");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
String scanCwbs = (String)request.getAttribute("scanCwbs");
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
	
	//原先的退货原因不用再传了，现在直传
	/* for(var i = 0 ; i < $("input[name^='reason']").length ; i++){
		datavalue = datavalue +"\""+$("input[name^='reason']")[i].value+"\",";
	}
	
	for(var i = 0 ; i < $("select[name^='reason']").length ; i++){
		datavalue = datavalue +"\""+$("select[name^='reason']")[i].value+"\",";
	} */
	
	for(var i = 0 ; i < $("input[name^='interceptReason']").length ; i++){
		datavalue = datavalue +"\""+$("input[name^='interceptReason']")[i].value+"\",";
	}
	
	for(var i = 0 ; i < $("select[name^='interceptReason']").length ; i++){
		datavalue = datavalue +"\""+$("select[name^='interceptReason']")[i].value+"\",";
	}
	
	
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
	}
	datavalue= datavalue + "]";
	if(datavalue.length>2){
	$.ajax({
		type: "POST",
		url:$("#SubFrom").attr("action"),
		data:{reasons:datavalue},
		dataType:"html",
		success : function(data) {
			alert("成功修改状态："+data.split("_s_")[0]+"单\n订单状态无变动："+data.split("_s_")[1]+"单\n"+data.split("_s_")[2]);
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
function cancelIntercept(transcwb){
	$.ajax({
		type: "POST",
		url:$("#cancelInterceptPath").val(),
		data:{"transCwb":transcwb},
		success : function(data) {
			if(data == 1){
				alert("撤销成功！");
			}else{
				alert("撤销成功！");
			}
			debugger
			$("#scanArea").val($("#scanCwbs").val()) ;
 			searchForm.submit();	

		}
	});	
}
</script>
</HEAD>
<BODY style="background:#f5f5f5"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath() %>/cwborder/toTuiHuo" class="light">订单拦截</a></li>
			</ul>
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="./toTuiHuo" method="POST" id="searchForm">
									<%
							if(cwbList!=null){%><span>
								<select name ="exportmould" id ="exportmould" class="select1">
									<option  value ="0">导出模板</option>
									<%for(Exportmould e:exportmouldlist){%>
										<option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
									<%} %>
								</select>
							
								<input name="" type="button" id="btnval" value="导出excel" class="input_button2" onclick="exportField();"/>
								</span> <%} %>订单号：
								<textarea name="cwb" id="scanArea" rows="3" class="kfsh_text" id="cwb" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
								
								<input type="submit" value="确定" class="input_button2">
							</form>
							<form action="<%=request.getContextPath()%>/cwborder/exportExcle" method="post" id="searchForm2">
								<input type="hidden" name="exportmould2" id="exportmould2" />
							</form>
						</div>
						<%
							if(cwbList!=null){%>
								
								<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="150" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
									<td width="150" align="center" valign="middle" bgcolor="#E7F4E3">运单号</td>
									<td width="70" align="center" valign="middle" bgcolor="#E7F4E3">客户</td>
									<td width="70" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="70" align="center" valign="middle" bgcolor="#E7F4E3">当前状态</td>
									<td width="150" align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">当前位置</td>
									<td width="70" align="center" valign="middle" bgcolor="#E7F4E3">收件人</td>
									<td align="center" valign="middle" bgcolor="#E7F4E3">收件地址</td>
									<td width="150" align="center" valign="middle" bgcolor="#E7F4E3" style="display:none;">退货原因</td>
									<td width="150" align="center" valign="middle" bgcolor="#E7F4E3" >拦截原因</td>
									<td width="70" align="center" valign="middle" bgcolor="#E7F4E3" >操作</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:100px"></div>
					<from action="./auditTuiHuo" method="post" id="SubFrom" >
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
							<%for(CwbOrderView cwb :cwbList){ %>
								<tr height="30" cwbFlowordertype="<%=cwb.getFlowordertype() %>"  cwbstate="<%
									if(cwb.getFlowordertype()==FlowOrderTypeEnum.FenZhanLingHuo.getValue()
										||cwb.getFlowordertype()==FlowOrderTypeEnum.YiFanKui.getValue()
										||(cwb.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue() && cwb.getIsmpsflag() == IsmpsflagEnum.no.getValue())
										||(cwb.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue() && cwb.getDeliverystate()!=DeliveryStateEnum.DaiZhongZhuan.getValue() 
										  && cwb.getDeliverystate()!=DeliveryStateEnum.JuShou.getValue() && cwb.getDeliverystate()!=DeliveryStateEnum.BuFenTuiHuo.getValue() && cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())
										||cwb.getFlowordertype()==FlowOrderTypeEnum.CheXiaoFanKui.getValue()
										||cwb.getFlowordertype()==FlowOrderTypeEnum.PosZhiFu.getValue()
										||(cwb.getFlowordertype()==FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()&& cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())
										||(cwb.getFlowordertype()==FlowOrderTypeEnum.GongYingShangJuShouTuiHuo.getValue()&& cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())
										||(cwb.getFlowordertype()==FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()&& cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())
										||(cwb.getFlowordertype()==FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()&& cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())
										||(cwb.getCwbstate()==CwbStateEnum.TuiHuo.getValue() && cwb.getIsmpsflag() == IsmpsflagEnum.no.getValue())
										||(cwb.getCwbstate()==CwbStateEnum.DiuShi.getValue()&& cwb.getIsmpsflag() == IsmpsflagEnum.no.getValue())){
									out.print("no");
								} %>"	>
									<td width="150" align="center" valign="middle"><%=cwb.getCwb() %></td>
									<td width="150" align="center" valign="middle"><%=cwb.getTranscwb()%></td>
									<td width="70" align="center" valign="middle"><%=StringUtil.nullConvertToEmptyString(cwb.getCustomername()) %></td>
									<td width="70" align="center" valign="middle">
									<%if(cwb.getIsmpsflag() == IsmpsflagEnum.no.getValue()) {%>
										<%=CwbStateEnum.getTextByValue((int)cwb.getCwbstate() ) %>
									<%}else{ %>
										<%=TransCwbStateEnum.getTextByValue( (int)cwb.getCwbstate() ) %>
									<%} %>
									</td>
									<td width="70" align="center" valign="middle"><%=FlowOrderTypeEnum.getText(cwb.getFlowordertype()).getText() %></td>
									<td width="150" align="center" valign="middle"><%=cwb.getEmaildate() %></td>
									<td width="100" align="center" valign="middle"><%for(Branch b:branchList){if(cwb.getCurrentbranchid()==b.getBranchid()){out.print(b.getBranchname());} } %></td>
									<td width="70" align="center" valign="middle"><%=cwb.getConsigneename() %></td>
									<td align="left" valign="middle"><%=cwb.getConsigneeaddress() %></td>
									<!-- 201601109 把退货原因改为拦截原因,原先的隐藏起来 -->
									<td width="150" align="center" valign="middle" style="display:none;">
									<%if((cwb.getFlowordertype()==FlowOrderTypeEnum.FenZhanLingHuo.getValue()
											||cwb.getFlowordertype()==FlowOrderTypeEnum.YiFanKui.getValue()
											||(cwb.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue() 
											||cwb.getFlowordertype()==FlowOrderTypeEnum.CheXiaoFanKui.getValue()
											||cwb.getFlowordertype()==FlowOrderTypeEnum.PosZhiFu.getValue()
											||cwb.getCwbstate()==CwbStateEnum.TuiHuo.getValue())&&
											!((cwb.getSendcarnum()>0||cwb.getBackcarnum()>0)&&cwb.getTranscwb().length()>0&&!cwb.getCwb().equals(cwb.getTranscwb())&&cwb.getFlowordertype()==FlowOrderTypeEnum.DingDanLanJie.getValue()))){ %>
											<%=cwb.getBackreason() %>
									<input type="hidden" name="reason_<%=cwb.getCwb() %>" value="<%=cwb.getScancwb() %>_s_0"/>
									<%}else{ %>
									<select name="reason_<%=cwb.getCwb() %>">
									<option value="">请选择退货原因</option>
									<%for(Reason r :reasonList) {%><option value="<%=cwb.getScancwb() %>_s_<%=r.getReasonid() %>"><%=r.getReasoncontent() %></option><%} %>
										</select>
									<%} %>
									</td>
									
									
									<td width="150" align="center" valign="middle">
									<%if(cwb.getFlowordertype()==FlowOrderTypeEnum.FenZhanLingHuo.getValue()
											||cwb.getFlowordertype()==FlowOrderTypeEnum.YiFanKui.getValue()
											||(cwb.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue() && cwb.getIsmpsflag() == IsmpsflagEnum.no.getValue())
											||(cwb.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue() && cwb.getDeliverystate()!=DeliveryStateEnum.DaiZhongZhuan.getValue() 
										  		&& cwb.getDeliverystate()!=DeliveryStateEnum.JuShou.getValue() && cwb.getDeliverystate()!=DeliveryStateEnum.BuFenTuiHuo.getValue() && cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())
											||cwb.getFlowordertype()==FlowOrderTypeEnum.CheXiaoFanKui.getValue()
											||cwb.getFlowordertype()==FlowOrderTypeEnum.PosZhiFu.getValue()
											||(cwb.getFlowordertype()==FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()&& cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())
											||(cwb.getFlowordertype()==FlowOrderTypeEnum.GongYingShangJuShouTuiHuo.getValue()&& cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())
											||(cwb.getFlowordertype()==FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()&& cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())
											||(cwb.getFlowordertype()==FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()&& cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())
											||(cwb.getCwbstate()==TransCwbStateEnum.DIUSHI.getValue() && cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())
											||(cwb.getCwbstate()==CwbStateEnum.TuiHuo.getValue() && cwb.getIsmpsflag() == IsmpsflagEnum.no.getValue())
											||(cwb.getCwbstate()==CwbStateEnum.DiuShi.getValue()&& cwb.getIsmpsflag() == IsmpsflagEnum.no.getValue())){ %>
											<%if((cwb.getCwbstate()==TransCwbStateEnum.DIUSHI.getValue() && cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue())){ %>
												【丢失】
											<%} %>
											<%=cwb.getBackreason() %>
									<input type="hidden" name="interceptReason_<%=cwb.getCwb() %>" value="<%=cwb.getScancwb() %>_intercept_0_intercept_0_intercept_<%=cwb.getCwb() %>"/>
									<%}else{ %>
										<select name="interceptReason_<%=cwb.getCwb() %>" style="width:140;">
										<option value="">请选择拦截原因</option>
										<%for(Reason r :reasonList) {
											if(cwb.getIsmpsflag() != IsmpsflagEnum.no.getValue()) {%>
												<%if(cwb.getReasonid() == r.getReasonid()){ %>
													<option value="<%=cwb.getScancwb() %>_intercept_<%=r.getReasonid() %>_intercept_<%=cwb.getIsmpsflag() %>_intercept_<%=cwb.getCwb() %>" selected = "selected" >
												<%}else{ %>
													<option value="<%=cwb.getScancwb() %>_intercept_<%=r.getReasonid() %>_intercept_<%=cwb.getIsmpsflag() %>_intercept_<%=cwb.getCwb() %>">
												<%} %>
													<%if(r.getInterceptType() == InterceptTypeEnum.diushi.getValue()){ %>
													【丢失】
													<%}else if(r.getInterceptType() == InterceptTypeEnum.posun.getValue()){ %>
													【破损】
													<%}else{ %>
													【退货】
													<%} %>
													<%=r.getReasoncontent() %>
												</option>
											<%}else{
												if(r.getInterceptType() == InterceptTypeEnum.tuihuo.getValue()){%>
													<option value="<%=cwb.getScancwb() %>_intercept_<%=r.getReasonid() %>_intercept_<%=cwb.getIsmpsflag() %>_intercept_<%=cwb.getCwb() %>">【退货】<%=r.getReasoncontent() %></option>																								
										<%}}} %>
										</select>
									<%} %>
									</td>
									<td width="70" align="center" valign="middle">
									<!-- 一票多件中拦截为丢失的运单可以撤销，但是退供应商之后的活接就不能再撤消了 -->
									<%if(cwb.getCwbstate() == InterceptTypeEnum.diushi.getValue() && cwb.getIsmpsflag() == IsmpsflagEnum.yes.getValue() && cwb.getFlowordertype() != FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() &&  cwb.getFlowordertype() != FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue() &&  cwb.getFlowordertype() != FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() ){ %>
										<a href="javascript:cancelIntercept('<%=cwb.getTranscwb() %>')" style="text-decoration:underline;color:red;" onclick=""> 撤销</a>
									<%} %>
									</td>
								</tr>
							<%} %>
							</tbody>
						</table>
					</from>
				</div>
				<div style="height:40px"></div>
				<div class="iframe_bottom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
						<tbody>
							<tr height="38" >
								<td align="center" valign="middle" bgcolor="#FFFFFF"><input type="button" id="button" value="提交" onclick="sub()" class="input_button1"></td>
							</tr>
						</tbody>
					</table>
				</div><%} %>
		</div>
	</div>
</div>
<input id="cancelInterceptPath" type="hidden" value="<%=request.getContextPath() %>/cwborder/cancelIntercept">
<input id="scanCwbs" type="hidden" value="<%=scanCwbs%>"/>
</BODY>
</HTML>
