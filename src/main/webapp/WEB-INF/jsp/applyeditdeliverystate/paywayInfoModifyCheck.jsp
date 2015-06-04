<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
Map<Long,String> userMap = (Map<Long,String>)request.getAttribute("userMap");
Map<Long,String> customermap = (Map<Long,String>)request.getAttribute("customermap");
Map<Long,String> bramap = (Map<Long,String>)request.getAttribute("bramap");
List<ZhiFuApplyView> zhifulist = (List<ZhiFuApplyView>)request.getAttribute("zhifulist");
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
	
	if($('input[name="checkbox"]:checked').size()>0){
		$('input[name="checkbox"]:checked').each(function(index){
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


function applypass(){
	if(<%=zhifulist!=null&&zhifulist.size()!=0%>){
		$("#shenhesubmit").attr("action","<%=request.getContextPath()%>/applyeditdeliverystate/");
		$("#shenhesubmit").submit();	
	}else{
		alert("没有做查询操作，不能审核！");
	}
} 

function serchsubmit(){
	$("#shenhesubmit").attr("action","<%=request.getContextPath()%>/applyeditdeliverystate/paywayInfoModifyCheck");
	$("#shenhesubmit").submit();
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
							<form action="./paywayInfoModifyCheck" method="post" id="searchForm">
								<span>
								<select name ="exportmould" id ="exportmould">
										<option  value ="0">导出模板</option>
										<%for(Exportmould e:exportmouldlist){%>
											<option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
										<%} %>
									</select>
									<input name="" type="button" id="btnval" value="导出excel" class="input_button2" onclick="exportField();"/>
								</span>
								订单号：
								<textarea name="cwb" rows="3" class="kfsh_text" id="cwb" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
								订单类型:
								<select name ="cwbtypeid" id ="cwbtypeid">
									<option  value ="0">全部</option>
										<option value ="<%=CwbOrderTypeIdEnum.Peisong.getValue() %>"><%=CwbOrderTypeIdEnum.Peisong.getText() %></option>
										<option value ="<%=CwbOrderTypeIdEnum.Shangmentui.getValue() %>"><%=CwbOrderTypeIdEnum.Shangmentui.getText() %></option>
										<option value ="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue() %>"><%=CwbOrderTypeIdEnum.Shangmenhuan.getText() %></option>
								</select>
								申请人:
								<select name ="applypeople" id ="cwbtypeid">
									<option  value ="0">全部</option>
										<% Set<Long> s=userMap.keySet();%>
										<%for(Long u:s){ %>
										<option value ="<%=u %>"><%=userMap.get(u) %></option>
										<%} %>
								</select>
								申请类型:
								<select name ="applytype" id ="cwbtypeid">
									<option  value ="0">全部</option>
										<option value ="<%=ApplyEnum.dingdanjinE.getValue()%>"><%=ApplyEnum.dingdanjinE.getText() %></option>
										<option value ="<%=ApplyEnum.zhifufangshi.getValue()%>"><%=ApplyEnum.zhifufangshi.getText() %></option>
										<option value ="<%=ApplyEnum.dingdanleixing.getValue()%>"><%=ApplyEnum.dingdanleixing.getText() %></option>
								</select>
								审核状态:
								<select name ="shenhestate" id ="shenhestate">
									<option  value ="0">全部</option>
										<option value ="<%=ApplyStateEnum.daishenhe.getValue() %>"><%=ApplyStateEnum.daishenhe.getText() %></option>
										<option value ="<%=ApplyStateEnum.yishenhe.getValue() %>"><%=ApplyStateEnum.yishenhe.getText() %></option>
								</select>
								审核结果:
								<select name ="shenheresult" id ="customerid">
									<option  value ="0">全部</option>
									<option value ="<%=ShenHeResultEnum.shenhebutongguo.getValue() %>"><%=ShenHeResultEnum.shenhebutongguo.getText() %></option>
									<option value ="<%=ShenHeResultEnum.shenhetongguo.getValue() %>"><%=ShenHeResultEnum.shenhetongguo.getText() %></option>
								</select>
								<input type="button" value="查询" onclick="serchsubmit();" class="input_button2">&nbsp;&nbsp;
								<input type="button" onclick="" value="重置" class="input_button2">&nbsp;&nbsp;
							</form>
							<form action="" id="shenhesubmit" method="post">
								<input hidden="hidden" id="hiddencwbs" value="" />
							</form>
								<input type="button" onclick="applypass()" value="审核通过" class="input_button2">&nbsp;&nbsp;
								<input type="button" onclick="applynopass()" value="审核不通过" class="input_button2">&nbsp;&nbsp;
								<input type="button" onclick="" value="导出" class="input_button2">
							<form action="<%=request.getContextPath()%>/cwborder/exportExcle" method="post" id="searchForm2">
								<input type="hidden" name="exportmould2" id="exportmould2" />
							</form>
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="40" align="center" valign="middle" bgcolor="#E7F4E3"><a href="#" onclick="btnClick();">全选</a></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">申请类型</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单金额</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单支付方式</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单当前状态</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单当前机构</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:120px"></div>
					<from action="./paywayInfoModifyCheck" method="post" id="SubFrom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2" >
						<tbody>
							<%
							if(zhifulist!=null){
								
								for(ZhiFuApplyView zav :zhifulist){ 
								%>
									<tr height="30" <%--cwbFlowordertype="<%=cwb.getFlowordertype() %>"  cwbstate="<%
									if(cwb.getFlowordertype()!=FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()&&cwb.getCwbstate()!=CwbStateEnum.TuiHuo.getValue()&&
											!((cwb.getSendcarnum()>0||cwb.getBackcarnum()>0)&&cwb.getTranscwb().length()>0&&!cwb.getCwb().equals(cwb.getTranscwb())&&cwb.getFlowordertype()==FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue())){
										out.print("no");
									} %>"	 --%>>
									<td  width="40" align="center" valign="middle">
											<input type="checkbox" checked="checked" name="checkbox" id="checkbox" value="<%=zav.getApplyid()%>"/>
										</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=zav==null?"":zav.getCwb() %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=customermap==null?"":customermap.get(zav.getCustomerid()) %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=ApplyEnum.getTextByValue(zav.getApplyway()) %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=CwbOrderTypeIdEnum.getTextByValue(zav.getCwbordertypeid()) %>/<%=CwbOrderTypeIdEnum.getTextByValue(zav.getApplycwbordertypeid()) %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=zav==null?"":zav.getReceivablefee() %>/<%=zav==null?"":zav.getApplyreceivablefee() %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=PaytypeEnum.getTextByValue(zav.getPaywayid()) %>/<%=PaytypeEnum.getTextByValue(zav.getApplypaywayid()) %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=ApplyStateEnum.getTextByValue(zav.getApplystate()) %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3"><%=bramap==null?"":bramap.get(zav.getBranchid()) %></td>
									</tr>
							<%} }%>
						</tbody>
					</table>
					</from>
				</div>
				
				<!-- <div style="height:40px"></div> -->
				<%-- <%if(cwbList!=null){ %>
				<div class="iframe_bottom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" id="gd_table2">
						<tbody>
							<tr height="30" >
								<td align="center" valign="middle" bgcolor="#f3f3f3"><input type="submit" name="button" id="button" value="提交" class="input_button1" onclick="sub();"></td>
							</tr>
						</tbody>
					</table>
				</div><%} %> --%>
		</div>
	</div>
</div>
</BODY>
</HTML>
