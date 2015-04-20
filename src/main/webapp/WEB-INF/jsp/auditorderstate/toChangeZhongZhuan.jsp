<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.OrderBackCheck,cn.explink.enumutil.FlowOrderTypeEnum,cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%
	List<CwbOrder> cwblist = request.getAttribute("cwbList")==null?null:(List<CwbOrder>)request.getAttribute("cwbList");
	Map<Long,Customer> mapcustomerlist = request.getAttribute("mapcustomerlist")==null?null:(Map<Long,Customer>)request.getAttribute("mapcustomerlist");
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
//提交审核按钮
function sub(){
	var ids="";//选中id
	$('input[type="checkbox"][name="checkbox"]').each(
	       	function() {
	          	if($(this).attr("checked")=="checked"){
	          		ids+=$(this).val()+",";
	          	}
	          }
	       );
	if(ids.length==0){
		alert("无提交数据");
		return false;
	}
	
	if(confirm("确认要审核为中转件吗？")){
		$("#submitF").attr("disabled","disabled");
    	$("#submitF").val("请稍候");
    	
    	$.ajax({
    		type: "POST",
    		url:'<%=request.getContextPath()%>/cwborder/saveChangeZhongZhuan',
    		data:"ids="+ids.substring(0,ids.lastIndexOf(",")),
    		dataType : "json",
    		success : function(data) {
    			if(data.errorCode==0){
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/cwborder/toChangeZhongZhuan?searchType="+$("#searchType").val();
    			}else{
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/cwborder/toChangeZhongZhuan?searchType="+$("#searchType").val();
    			}
    		}
    	});
	}
}

function exportField(){
	if(<%=cwblist!=null&&!cwblist.isEmpty()%>){
		$("#btnval").attr("disabled","disabled"); 
	 	$("#btnval").val("请稍后……");
		$("#exportForm").submit();	
	}else{
		alert("没有做查询操作，不能导出！");
	}
}

//全选按钮
function btnClick(){
	$("[name='checkbox']").attr("checked",'true');//全选  
}

//查询全部
function submitAll(){
	$("#searchType").val("1");
	$("#searchForm").submit();
}

//根据订单号查询
function submitCwb(){
	$("#searchType").val("0");
	$("#searchForm").submit();
}
</script>
</HEAD>
<BODY style="background:#eef9ff"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath() %>/cwborder/toTuiHuo" >订单拦截</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toTuiHuoZaiTou">审核为退货再投</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toTuiGongHuoShang">审核为退供货商</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toTuiGongHuoShangSuccess">审核为供货商确认退货</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toChongZhiStatus" >重置审核状态</a></li>
				<%if(request.getAttribute("amazonIsOpen") != null && "1".equals(request.getAttribute("amazonIsOpen").toString())){ %>
				<li><a href="<%=request.getContextPath() %>/cwborder/toBaoGuoWeiDao">亚马逊订单处理</a></li><%} %>
				<!-- <li><a href="./toZhongZhuan">审核为中转</a></li> -->
				 <%if(request.getAttribute("isUseAuditTuiHuo") != null && "yes".equals(request.getAttribute("isUseAuditTuiHuo").toString())){ %>
					<li><a href="<%=request.getContextPath() %>/cwbapply/kefuuserapplytoTuiHuolist/1">审核为退货</a></li>
				<%} %> 
				<%if(request.getAttribute("isUseAuditZhongZhuan") != null && "yes".equals(request.getAttribute("isUseAuditZhongZhuan").toString())){ %>
					<li><a href="<%=request.getContextPath() %>/cwbapply/kefuuserapplytoZhongZhuanlist/1">审核为中转</a></li>
				<%} %>
				<li><a href="<%=request.getContextPath() %>/orderBackCheck/toTuiHuoCheck">审核为允许退货出站</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toChangeZhongZhuan"  class="light">审核为中转件</a></li>
			</ul>
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="./toChangeZhongZhuan" method="POST" id="searchForm">
									<%
							if(cwblist!=null&&!cwblist.isEmpty()){%><span>
								</span> <%} %>订单号：
								<textarea name="cwbs" rows="3" class="kfsh_text" id="cwbs" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
								<input type="button" onclick="submitCwb()" value="查询" class="input_button2">&nbsp;&nbsp;
							</form>
						</div>
						<%
							if(cwblist!=null&&!cwblist.isEmpty()){%>
								<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="40" align="center" valign="middle" bgcolor="#E7F4E3"><a href="#" onclick="btnClick();">全选</a></td>
									<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
									<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3">当前状态</td>
									<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3">收件人</td>
									<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3">收件人手机</td>
									<td width="20%" valign="middle" bgcolor="#E7F4E3">收件地址</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:100px"></div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
							<%for(CwbOrder cwb :cwblist){
								
								%>
								<tr>
									<td  width="40" align="center" valign="middle">
										<input type="checkbox" checked="checked" name="checkbox" id="checkbox" value="<%=cwb.getOpscwbid()%>"/>
									</td>
									<td width="10%" align="center" valign="middle"><%=cwb.getCwb() %></td>
									<td width="10%" align="center" valign="middle"><%=mapcustomerlist.get(cwb.getCustomerid()).getCustomername() %></td>
									<td width="10%" align="center" valign="middle"><%=CwbOrderTypeIdEnum.getByValue(Integer.valueOf(cwb.getCwbordertypeid())).getText()%></td>
									<td width="10%" align="center" valign="middle"><%=CwbStateEnum.getByValue((int)cwb.getCwbstate()).getText()%></td>
									<td width="10%" align="center" valign="middle"><%=cwb.getConsigneename() %></td>
									<td width="10%" align="center" valign="middle"><%=cwb.getConsigneemobile() %></td>
									<td width="20%" valign="middle"><%=cwb.getConsigneeaddress() %></td>
								</tr>
							<%} %>
							</tbody>
						</table>
				</div>
				<div style="height:40px"></div>
				<div class="iframe_bottom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
						<tbody>
							<tr height="38" >
								<td align="center" valign="middle" bgcolor="#FFFFFF"><input type="button" id="submitF" value="提交审核" onclick="sub()" class="input_button1"></td>
							</tr>
						</tbody>
					</table>
				</div><%} %>
		</div>
	</div>
</div>
<form action="<%=request.getContextPath() %>/orderBackCheck/export" method="post" id="exportForm">
	<textarea style="display:none" name="cwb" id="cwb"><%=request.getParameter("cwb")==null?"":request.getParameter("cwb")%></textarea>
	<input type="hidden" value="<%=request.getParameter("searchType")==null?"":request.getParameter("searchType")%>" id="searchType" name="searchType"/>
</form>
</BODY>
</HTML>
