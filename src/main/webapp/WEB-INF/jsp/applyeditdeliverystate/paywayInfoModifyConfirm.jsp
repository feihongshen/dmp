<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> uslist = (List<User>)request.getAttribute("uslist");
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
String cwbs = request.getParameter("cwb")==null?"":request.getParameter("cwb");
List<CwbOrderView> zhifulist = (List<CwbOrderView>)request.getAttribute("zhifulist");
Page page_obj = (Page)request.getAttribute("page_obj");
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


function check(){
	$("#searchForm").submit();
	return true;

}

function btnClick(){
	if($("[name='checkbox']").attr("checked")=="checked"||$("[name='checkbox']").attr("checked")=="true"){
		$("[name='checkbox']").removeAttr("checked");
		$("#selectbtn").text("全选");
	}else{
		$("[name='checkbox']").attr("checked","checked");
		$("#selectbtn").text("反选");
	}
}

function applypass(){
	var datavalue = "";
	
	if($('input[name="checkbox"]:checked').size()>0){
		$('input[name="checkbox"]:checked').each(function(index){
			$(this).attr("checked",false);
			datavalue = datavalue +$(this).val()+",";
		});
	}
	if(datavalue.length==0){
		alert("请选择当前要处理订单！");
	}
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/applyeditdeliverystate/editPaywayInfoModifyConfirmpass",
			data:{applyids:datavalue},
			dataType:"json",
			success:function(data) {    
		       /*  if(data.msg =="true1" ){    
		           alert("修改金额确认通过！");    
		           window.location.reload();    
		        }else if(data.msg =="true2" ){
		        	alert("修改支付方式确认通过！");    
		        	window.location.reload();    
		        }else if(data.msg =="true3" ){
		        	alert("修改订单类型确认通过！");    
		        	window.location.reload();    
		        }else{    
		           alert("未完成确认出现异常！");   
	        }     */
	        if(data.msg =="true5"){
	        	alert("订单已做过确认！");
	        }else{
	        	alert("修改金额确认通过"+data.msg.split("_")[1]+"单"+",修改支付方式确认通过"+data.msg.split("_")[3]+"单"+",修改订单类型确认通过"+data.msg.split("_")[5]+"单");
	        	$("#searchForm").submit();
	        }/* else{
		           alert("未完成确认出现异常！");   
	        } */
	     }
	 });
	}
}

function applynopass(){
	var datavalue = "";
	
	if($('input[name="checkbox"]:checked').size()>0){
		$('input[name="checkbox"]:checked').each(function(index){
			$(this).attr("checked",false);
			datavalue = datavalue+$(this).val()+",";
		});
	}
	if(datavalue.length==0){
		alert("请选择当前要处理订单！");
	}
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/applyeditdeliverystate/editPaywayInfoModifyConfirmnopass",
			data:{applyids:datavalue},
			dataType:"json",
			success:function(data) {    
		        if(data.msg =="true1" ){    
		           alert("修改金额未确认通过！");    
		           window.location.reload();    
		        }else if(data.msg =="true2" ){
		        	alert("修改支付方式未确认通过！");    
		        	window.location.reload();    
		        }else if(data.msg =="true3" ){
		        	alert("修改订单类型未确认通过！");    
		        	window.location.reload();    
		        }else if(data.msg == "true4"){
		        	alert("订单已做过确认!");
		        }else{    
		           alert("未完成确认出现异常！");   
	        	}    
	     }
	 });
	}
}
function exportExcel(){
	$("#searchForm").attr("action","<%=request.getContextPath()%>/applyeditdeliverystate/confirmExportExcel");
	$("#searchForm").submit();
	$("#searchForm").attr("action","1");
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
							<form action="1" method="post" id="searchForm">
								<table>
									<tr>
										<td rowspan="2">
											订单号：
											<textarea name="cwb"  rows="3" class="kfsh_text" id="cwb" ><%=cwbs %></textarea>
											<input type="hidden" name="isnow" value="1"/>
										</td>
										<td>
											订单类型:
											<select name ="cwbtypeid" id ="cwbtypeid">
												<option  value ="0">全部</option>
												<option value ="<%=CwbOrderTypeIdEnum.Peisong.getValue() %>"><%=CwbOrderTypeIdEnum.Peisong.getText() %></option>
												<option value ="<%=CwbOrderTypeIdEnum.Shangmentui.getValue() %>"><%=CwbOrderTypeIdEnum.Shangmentui.getText() %></option>
												<option value ="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue() %>"><%=CwbOrderTypeIdEnum.Shangmenhuan.getText() %></option>
											</select>
										</td>
										<td>
											申请人:
											<select name ="applypeople" id ="applypeople">
												<option  value ="0">全部</option>
													<%if(uslist.size()>0){
														for(User u:uslist){ %>
															<option value ="<%=u.getUserid() %>"><%=u.getRealname() %></option>
													<%} }%>
											</select>
										</td>
										<td>
											申请类型:
											<select name ="applytype" id ="applytype">
												<option  value ="0">全部</option>
												<option value ="<%=ApplyEnum.dingdanjinE.getValue()%>"><%=ApplyEnum.dingdanjinE.getText() %></option>
												<option value ="<%=ApplyEnum.zhifufangshi.getValue()%>"><%=ApplyEnum.zhifufangshi.getText() %></option>
												<option value ="<%=ApplyEnum.dingdanleixing.getValue()%>"><%=ApplyEnum.dingdanleixing.getText() %></option>
											</select>
										</td>
									</tr>	
									<tr>
										<td>
											确认状态:
											<select name ="confirmstate" id ="confirmstate">
												<option  value ="0">全部</option>
												<option value ="<%=ConfirmStateEnum.daiqueren.getValue() %>"><%=ConfirmStateEnum.daiqueren.getText() %></option>
												<option value ="<%=ConfirmStateEnum.yiqueren.getValue() %>"><%=ConfirmStateEnum.yiqueren.getText() %></option>
											</select>
										</td>
										<td>
											确认结果:
											<select name ="confirmresult" id ="confirmresult">
												<option  value ="0">全部</option>
												<option value ="<%=ConfirmResultEnum.querenbutongguo.getValue() %>"><%=ConfirmResultEnum.querenbutongguo.getText() %></option>
												<option value ="<%=ConfirmResultEnum.querentongguo.getValue() %>"><%=ConfirmResultEnum.querentongguo.getText() %></option>
											</select>
										</td>
									</tr>
								</table>
								<table>
									<tr>
										<td width="20%">
											<input type="button" value="查询" onclick="check();" class="input_button2">&nbsp;&nbsp;
											<input type="reset"  value="重置" class="input_button2">&nbsp;&nbsp;
										</td>
										<td width="20%">
											<input type="button" onclick="applypass();" id="pass" value="确认通过" class="input_button2">&nbsp;&nbsp;
											<input type="button" onclick="applynopass();" id="nopass" value="确认不通过" class="input_button2">&nbsp;&nbsp;
											<input type="button" onclick="exportExcel();" value="导出" class="input_button2">
										</td>
									</tr>
								</table>
							</form>
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="40" align="center" valign="middle" bgcolor="#E7F4E3"><a href="#" onclick="btnClick();" id="selectbtn">反选</a></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">申请类型</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单金额</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单支付方式</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单当前状态</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单当前机构</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">申请人</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">审核人</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">审核时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">确认人</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">确认时间</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:135px"></div>
					<from action="" method="post" id="SubFrom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2" >
						<tbody>
							<%
							if(zhifulist!=null){
								for(CwbOrderView zav :zhifulist){ 
									%>
									<tr height="30" >
										<td  width="40" align="center" valign="middle">
											<input type="checkbox"  name="checkbox" id="checkbox" checked="checked" value="<%=zav.getOpscwbid()%>"/>
										</td>
										<td width="100" align="center" valign="middle" ><%=zav.getCwb() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getCustomername()%></td>
										<td width="100" align="center" valign="middle" ><%=zav.getApplytype() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getOldnewCwbordertypename()%></td>
										<td width="100" align="center" valign="middle" ><%=zav.getOldnewReceivablefee() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getOldnewPaytype() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getNowState() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getBranchname()%></td>
										<td width="100" align="center" valign="middle" ><%=zav.getApplyuser() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getAuditor() %></td>
										<td width="120" align="center" valign="middle" ><%=zav.getAudittime() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getConfirmname() %></td>
										<td width="120" align="center" valign="middle" ><%=zav.getConfirmtime() %></td>
									</tr>
								<%} }%>
						</tbody>
					</table>
					</from>
					<%if(page_obj!=null&&page_obj.getMaxpage()>1){ %>
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
		</div>
	</div>
</div>
<script type="text/javascript">
	$("#selectPg").val(<%=request.getAttribute("page")%>);
	$("#cwbtypeid").val(<%=request.getParameter("cwbtypeid")==null?0:Integer.parseInt(request.getParameter("cwbtypeid"))%>);
	$("#applypeople").val(<%=request.getParameter("applypeople")==null?0:Long.parseLong(request.getParameter("applypeople"))%>);
	$("#applytype").val(<%=request.getParameter("applytype")==null?0:Long.parseLong(request.getParameter("applytype"))%>);
	$("#confirmstate").val(<%=request.getParameter("confirmstate")==null?0:Long.parseLong(request.getParameter("confirmstate"))%>);
	$("#confirmresult").val(<%=request.getParameter("confirmresult")==null?0:Long.parseLong(request.getParameter("confirmresult"))%>);
</script>
</BODY>
</HTML>
