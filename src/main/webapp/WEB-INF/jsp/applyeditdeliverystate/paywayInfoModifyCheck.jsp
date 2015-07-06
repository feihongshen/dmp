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
Page page_obj = (Page)request.getAttribute("page_obj");
String cwbs = request.getParameter("cwb")==null?"":request.getParameter("cwb");
List<CwbOrderView> zhifulist = (List<CwbOrderView>)request.getAttribute("zhifulist");

long applyresult = request.getParameter("applyresult")==null?0:Long.parseLong(request.getParameter("applyresult"));

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
	
	/* $("table#gd_table2 tr").click(function(){
			$(this).css("backgroundColor","#FFA500");
			$(this).siblings().css("backgroundColor","#ffffff");
			$("tr[cwbstate='no']").css("backgroundColor","#aaaaaa");
		}); */
	
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
			datavalue = datavalue+$(this).val()+",";
		});
	}
	if(datavalue.length==0){
		alert("请选择当前要处理订单！");
	}
	if(datavalue.length>1){
		if(confirm("确定要审核通过吗?")){
			datavalue= datavalue.substring(0, datavalue.length-1);
			$.ajax({
				type: 'POST',
				url: "<%=request.getContextPath()%>/applyeditdeliverystate/editPaywayInfoModifyCheckpass",
				data:{applyids:datavalue},
				dataType:"json",
				success:function(data) {    
			        if(data.code == 0 ){  
			        	alert(data.msg);
			           $("#searchForm").submit();    
			        }else{    
			           alert(data.msg);   
		       		}    
		    	 }
			 });
			
		}
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
		if(confirm("确定要审核不通过吗？")){
			datavalue= datavalue.substring(0, datavalue.length-1);
			$.ajax({
				type: 'POST',
				url: "<%=request.getContextPath()%>/applyeditdeliverystate/editPaywayInfoModifyChecknopass",
				data:{applyids:datavalue},
				dataType:"json",
				success:function(data) {    
			        if(data.code == 0 ){    
			          alert(data.msg); 
			          window.location.reload();   
			        }else if(data.code == 1){
			           alert(data.msg);   
			        }
		     	}
			 });
		}
	}
}

function exportExcel(){
	if(<%=zhifulist!=null&&!zhifulist.isEmpty()%>){
		$("#btnval").attr("disable","disable");
		$("#searchForm").attr("action","<%=request.getContextPath()%>/applyeditdeliverystate/checkExportExcel");
		$("#searchForm").submit();
		$("#searchForm").attr("action","1");
	}else{
		alert("无查询结果,无法导出!");
	}
}
function reserData(){
	$("#cwb").val("");
	$("#cwbtypeid").val(0);
	$("#applypeople").val(0);
	$("#applytype").val(0);
	$("#applystate").val(0);
	$("#applyresult").val(0);
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
											审核状态:
											<select name ="applystate" id ="applystate">
												<option value ="<%=ApplyStateEnum.daishenhe.getValue() %>"><%=ApplyStateEnum.daishenhe.getText() %></option>
												<option value ="<%=ApplyStateEnum.yishenhe.getValue()%>"><%=ApplyStateEnum.yishenhe.getText() %></option>
											</select>
										</td>
										<td>
											审核结果:
											<select name ="applyresult" id ="applyresult">
												<option  value ="0" >全部</option>
												<option value ="<%=ShenHeResultEnum.shenhebutongguo.getValue() %>" ><%=ShenHeResultEnum.shenhebutongguo.getText() %></option>
												<option value ="<%=ShenHeResultEnum.shenhetongguo.getValue() %>"  ><%=ShenHeResultEnum.shenhetongguo.getText() %></option>
											</select>
										</td>
									</tr>
								</table>
								<table>
									<tr>
										<td width="20%">
											<input type="button" value="查询" onclick="check();" class="input_button2">&nbsp;&nbsp;
											<input type="button"  onclick="reserData();" value="重置" class="input_button2">&nbsp;&nbsp;
										</td>
										<td width="20%">
											<input type="button" onclick="applypass()" id="pass" value="审核通过" class="input_button2">&nbsp;&nbsp;
											<input type="button" onclick="applynopass()" id="nopass" value="审核不通过" class="input_button2">&nbsp;&nbsp;
											<input type="button" id="btnval" onclick="exportExcel();" value="导出" class="input_button2">
										</td>
									</tr>
								</table>
							</form>
						</div>
						<div style="overflow: scroll;height: 480px;">
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
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">申请时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">审核人</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">审核时间</td>
								</tr>
								
								<%if(zhifulist!=null){
								for(CwbOrderView zav :zhifulist){ 
									%>
									<tr height="30" >
										<td  width="40" align="center" valign="middle">
											<input type="checkbox"  name="checkbox" id="checkbox" checked="checked" value="<%=zav.getOpscwbid()%>"/>
										</td>
										<td width="100" align="center" valign="middle" ><%=zav.getCwb() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getCustomername()%></td>
										<td width="100" align="center" valign="middle" ><%=zav.getApplytype() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getOldnewCwbordertypename() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getOldnewReceivablefee() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getOldnewPaytype() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getNowState() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getBranchname()%></td>
										<td width="100" align="center" valign="middle" ><%=zav.getApplyuser() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getApplytime() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getAuditor() %></td>
										<td width="100" align="center" valign="middle" ><%=zav.getAudittime() %></td>
									</tr>
								<%} }%>
								
							</tbody>
						</table>
						</div>
					<from action="" method="post" id="SubFrom" >
					</from>
				</div>
			</div>
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
<script type="text/javascript">
	$("#selectPg").val(<%=request.getAttribute("page")%>);
	$("#cwbtypeid").val(<%=request.getParameter("cwbtypeid")==null?0:Integer.parseInt(request.getParameter("cwbtypeid"))%>);
	$("#applypeople").val(<%=request.getParameter("applypeople")==null?0:Long.parseLong(request.getParameter("applypeople"))%>);
	$("#applytype").val(<%=request.getParameter("applytype")==null?0:Long.parseLong(request.getParameter("applytype"))%>);
	$("#applystate").val(<%=request.getParameter("applystate")==null?0:Long.parseLong(request.getParameter("applystate"))%>);
	 $("#applyresult").val(<%=request.getParameter("applyresult")==null?0:Long.parseLong(request.getParameter("applyresult"))%>);
</script>
</BODY>
</HTML>
