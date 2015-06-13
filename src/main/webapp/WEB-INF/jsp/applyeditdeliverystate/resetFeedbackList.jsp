<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<CwbOrderView> cwbList = (List<CwbOrderView>)request.getAttribute("cwbList");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");

List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
Map<Long,String> branchMap = (Map<Long,String>)request.getAttribute("branchMap");
List<ApplyEditDeliverystate> applyeditlist = (List<ApplyEditDeliverystate>)request.getAttribute("applyeditlist");
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

$("#serchcwb").click(function(){
	alert("sssss");
	$.ajax({
		type:"post",		
		url:"<%=request.getContextPath()%>/applyeditdeliverystate/resetFeedbackList",
		data:{cwb:$("#cwb").val(),
			cwbtypeid:$("#cwbtypeid").val(),
			cwbresultid:$("#cwbresultid").val(),
			isdo:$("#isdo").val(),
			cwbstate:$("#cwbstate").val(),
			feedbackbranchid:$("#feedbackbranchid").val(),
			begindate:$("#begindate").val(),
			enddate:$("#enddate").val()
		},
		datatype:"html",
		success:function(data){
			alert("查询完成！");
			searchForm.submit();
		}
	});
} );

//重置反馈通过
function resetfeedbackPass(){
	var cwbdata = "";
	if($('input[name="checkbox"]:checked').size>0){
		$('input[name="checkbox"]:checked').each(function(index){
		})
	}
	if(cwbdata.length>0){
		$.ajax({
			type:"post",
			url:"<%=request.getContextPath()%>/applyeditdeliverystate/getCheckboxDealPass",
			data:{cwbdata:cwbdata},
			datatype:"html",
			success:function(data){
				alert("对选中列表审核为通过！");
			}
		});
	}
}

//重置反馈不通过
function resetfeedbackNoPass(){
	var cwbdata = "";
	if($('input[name="checkbox"]:checked').size>0){
		$('input[name="checkbox"]:checked').each(function(index){
		})
	}
	if(cwbdata.length>0){
		$.ajax({
			type:"post",
			url:"<%=request.getContextPath()%>/applyeditdeliverystate/getCheckboxDealNoPass",
			data:{cwbdata:cwbdata},
			datatype:"html",
			success:function(data){
				alert("对选中列表进行审核为不通过！");
			}
		});
	}
}

</script>
</HEAD>
<BODY style="background:#f5f5f5;overflow: hidden;"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="<%=request.getContextPath()%>/applyeditdeliverystate/resetFeedbackList" method="post" id="searchForm">
								<%if(cwbList!=null){ %><span>
								<select name ="exportmould" id ="exportmould">
									<option  value ="0">导出模板</option>
									<%for(Exportmould e:exportmouldlist){%>
									<option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
									<%} %>
								</select>
									<input name="" type="button" id="btnval" value="导出excel" class="input_button2" onclick="exportField();"/>
								</span><%} %> 
								<table >
									<tr>
										<td rowspan="3">
											订单号：
											<textarea name="cwb" rows="3" cols="20" class="kfsh_text" id="cwb" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
										</td>
										<td >
											&nbsp;&nbsp;
											订单状态:
											<select name ="cwbstate" id ="cwbstate">
												<option value ="0">全部</option>
												<option value ="<%=CwbStateEnum.PeiShong.getValue()%>"><%=CwbStateEnum.PeiShong.getText() %></option>
												<option value ="<%=CwbStateEnum.TuiHuo.getValue()%>"><%=CwbStateEnum.TuiHuo.getText() %></option>
												<option value ="<%=CwbStateEnum.DiuShi.getValue()%>"><%=CwbStateEnum.DiuShi.getText() %></option>
												<option value ="<%=CwbStateEnum.WuXiaoShuJu.getValue()%>"><%=CwbStateEnum.WuXiaoShuJu.getText() %></option>
												<option value ="<%=CwbStateEnum.TuiGongYingShang.getValue()%>"><%=CwbStateEnum.TuiGongYingShang.getText() %></option>
												<option value ="<%=CwbStateEnum.ZhongZhuan.getValue()%>"><%=CwbStateEnum.ZhongZhuan.getText() %></option>
											</select>
										</td>
										<td>
											&nbsp;&nbsp;
											配送结果:
											<select name ="cwbresultid" id ="cwbresultid">
												<option  value ="0">全部</option>
												<option value ="<%=DeliveryStateEnum.PeiSongChengGong.getValue()%>"><%=DeliveryStateEnum.PeiSongChengGong.getText()%></option>
												<option value ="<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>"><%=DeliveryStateEnum.ShangMenTuiChengGong.getText()%></option>
												<option value ="<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue()%>"><%=DeliveryStateEnum.ShangMenHuanChengGong.getText()%></option>
												<option value ="<%=DeliveryStateEnum.JuShou.getValue()%>"><%=DeliveryStateEnum.JuShou.getText()%></option>
												<option value ="<%=DeliveryStateEnum.BuFenTuiHuo.getValue()%>"><%=DeliveryStateEnum.BuFenTuiHuo.getText()%></option>
												<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue()%>"><%=DeliveryStateEnum.FenZhanZhiLiu.getText()%></option>
												<option value ="<%=DeliveryStateEnum.ShangMenJuTui.getValue()%>"><%=DeliveryStateEnum.ShangMenJuTui.getText()%></option>
												<option value ="<%=DeliveryStateEnum.HuoWuDiuShi.getValue()%>"><%=DeliveryStateEnum.HuoWuDiuShi.getText()%></option>
												<option value ="<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()%>"><%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getText()%></option>
												<option value ="<%=DeliveryStateEnum.DaiZhongZhuan.getValue()%>"><%=DeliveryStateEnum.DaiZhongZhuan.getText()%></option>
											</select>
										</td>
										<td>
											&nbsp;&nbsp;
											审核状态:
											<select name ="isdo" id ="isdo">
												<option  value ="0">全部</option>
												<option value ="<%=ShenHeStateEnum.daishenhe.getValue() %>"><%=ShenHeStateEnum.daishenhe.getText() %></option>
												<option value ="<%=ShenHeStateEnum.shenhebutongguo.getValue() %>"><%=ShenHeStateEnum.shenhebutongguo.getText() %></option>
												<option value ="<%=ShenHeStateEnum.shenhetongguo.getValue()%>"><%=ShenHeStateEnum.shenhetongguo.getText() %></option>
											</select>
										</td>
									</tr>
									<tr></tr>
									<tr>
										<td>
											&nbsp;&nbsp;
											订单类型:
											<select name ="cwbtypeid" id ="cwbtypeid">
												<option  value ="0">全部</option>
												<option value ="<%=CwbOrderTypeIdEnum.Peisong.getValue() %>"><%=CwbOrderTypeIdEnum.Peisong.getText() %></option>
												<option value ="<%=CwbOrderTypeIdEnum.Shangmentui.getValue() %>"><%=CwbOrderTypeIdEnum.Shangmentui.getText() %></option>
												<option value ="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue() %>"><%=CwbOrderTypeIdEnum.Shangmenhuan.getText() %></option>
											</select>
										</td>
										<td>
											&nbsp;&nbsp;
											反馈站点:
											<select name ="feedbackbranchid" id =""feedbackbranchid"">
												<option  value ="0">全部</option>
												<%for(Branch br:branchList){ %>
													<option value ="<%=br.getBranchid() %>"><%=br.getBranchname() %></option>
												<%} %>
											</select>
										</td>
										<td>
											&nbsp;&nbsp;
											操作时间:
											<input type ="text" name ="begindate" id="strtime"  value=""/>到<input type ="text" name ="enddate" id="endtime"  value=""/>
											<input type="hidden" value="<%=request.getParameter("searchType")==null?"":request.getParameter("searchType")%>" id="searchType" name="searchType">
										</td>
									</tr>
								</table>
								<table>
									<tr>
										<td width="20%" align="left">
											<input type="button"  value="查询" class="input_button2" id="serchcwb" />&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="button" onclick="submitAll()" value="重置" class="input_button2" />&nbsp;&nbsp;&nbsp;&nbsp;
										</td>
										<td width="40%" align="center">
											<input type="button" onclick="resetfeedbackPass()" value="审核通过" class="input_button2">&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="button" onclick="resetfeedbackNoPass()" value="审核不通过" class="input_button2">&nbsp;&nbsp;&nbsp;&nbsp;
										</td>
										<td  width="40" align="right">
											<input type="button" onclick="submitAll()" value="导出" class="input_button2">
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
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">配送结果</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单状态</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">结算状态</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">反馈站点</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">反馈人</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">反馈时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">操作人</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">操作时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">审核状态</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:100px"></div>
					<from action="./auditTuiGongHuoShangSuccess" method="post" id="SubFrom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2" >
						<tbody>
							<%
							if(applyeditlist!=null){
								for(ApplyEditDeliverystate aed :applyeditlist){ %>
									<tr height="30">
									<td  width="40" align="center" valign="middle">
											<input type="checkbox" checked="checked" name="checkbox" id="checkbox" value="<%=aed.getOpscwbid()%>" checked="checked"/>
										</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=aed.getCwb() %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=aed.getCwbordertypeid() %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=aed.getEditnowdeliverystate() %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=aed.getCwbstate() %></td>
									<!--TODO 结算状态 -->
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=aed.getApplybranchid() %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=aed.getApplyuserid() %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=aed.getApplytime() %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=aed.getEdituserid() %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=aed.getEdittime() %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=ApplyStateEnum.getTextByValue(aed.getShenhestate()) %></td>
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
