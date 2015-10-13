<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.CwbOrderAll"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum" %>
<%@page import="cn.explink.enumutil.PaytypeEnum" %>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum" %>
<%@page import="cn.explink.enumutil.DeliveryStateEnum" %>
<%@page import="cn.explink.domain.Exportmould"%>    
<%
List<CwbOrderAll> cwborderList = (List<CwbOrderAll>)request.getAttribute("cwborderList");
Page page_obj = (Page)request.getAttribute("page_obj"); 
Map<String,String> comMap = (Map<String,String>)request.getAttribute("comMap");
Map<String,String> stateTimeMap = (Map<String,String>)request.getAttribute("stateTimeMap");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
String cwbs=request.getParameter("cwbs")==null?"":request.getParameter("cwbs");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>按单查询</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<style type="text/css">
/*通用*/
html, body, div, span, applet, object, iframe, caption, tbody, tfoot, thead, th, del, dfn, em, font, img, ins, kbd, q, s, samp, small, strike, strong, sub, sup, tt, var,     
h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, big, cite, code,     
dl, dt, dd, ol, ul, li, fieldset, form, label, legend {    
 vertical-align: baseline;    
 font-family: inherit;    
 font-weight: inherit;    
 font-style: inherit;    
 outline: 0;    
 padding: 0;    
 margin: 0;    
 border: 0;
 word-break:break-all;
 word-wrap:break-word;
 }    
ol, ul,li {    
 list-style: none;    
 }

.clear{content: "."; display: block; height: 0px; line-height:0px; visibility:hidden; clear:both}
 /*CSS-REST*/
 
 
/*LIST*/
.kfsh_search{padding:10px; border-left:1px solid #0CF; border-right:1px solid #0cf; background:#FFF; overflow:hidden}
	.kfsh_search strong{font-size:16px; font-weight:bold; font-family:"微软雅黑", "黑体"; color:#F30}
	.kfsh_search *{vertical-align:middle}
	.kfsh_search span{float:right}
.kfsh_tabbtn { position:relative; background:#AFD6EB; height:37px; z-index:9 }
.kfsh_tabbtn ul { position:absolute; z-index:99; height:31px; left:0; bottom:0px; padding-left:6px; }
.kfsh_tabbtn li { float:left; display:block; width:140px; height:31px; line-height:31px; text-align:center; margin-right:8px }
.kfsh_tabbtn li a { display:block; line-height:31px; color:#666; font-size:12px; background:url(<%=request.getAttribute("dmpUrl")%>/images/uc_menubg3.png) no-repeat }
.kfsh_tabbtn li a:hover { text-decoration:none; color:#C30; background-position:0px -31px }
.kfsh_tabbtn .light { background-position:0px -62px;background-color:#FFF; }
.kfsh_text{height:48px}
</style>
<script type="text/javascript">
    
	function searchforcwb(){
		var cwbs=$("#cwbs").val();
		var num=cwbs.split(/\r?\n/).length;
		if(num>10000){
			alert("一次查询不能超过一万单");
			return false;
		}
		$("#showdetail").submit();
		
	}
	function check(){
		if(<%=page_obj.getTotal() > 0 %>){
			$('#searchForm2').submit();
		}else{
			alert("没有数据，不能导出！");
		}
	}
	
	
</script>

</head>
<body>
<div class="right_box">
	<div class="kfsh_tabbtn">
		<ul>
			<li><a href="<%=request.getContextPath() %>/commonemail/list/1" >按批次查询</a></li>
			<li><a href="<%=request.getContextPath() %>/commonemail/showdetailbycwbs/1" class="light">按订单查询</a></li>
		</ul>
	</div>
	<div class="jg_10"></div>
	<table>
		<tr>
			<td>
			<form id="showdetail" action="<%=request.getContextPath() %>/commonemail/showdetailbycwbs/1"  method="post">
			订单号：
			<textarea name="cwbs"   rows="3" class="kfsh_text" id="cwbs" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" ><%if((request.getParameter("cwbs")==null?"":request.getParameter("cwbs")).length()>0){%><%=request.getParameter("cwbs") %><%}else{%>查询多个订单用回车隔开<%} %></textarea>
			<input type="hidden" name="isshow" value="1"/>
			</form>	
			</td>
			<td>
			<input type="button" value="查询" onclick="searchforcwb()"  class="input_button2">
			</td>
			<td>
			<input type ="button" id="back" value="返回" class="input_button2" onclick="$('#searchForm1').submit()"/>
			</td>
			<%if(page_obj.getTotal() > 0 ){ %>
			<td>
			<input type ="button" id="btnval" value="导出excel" class="input_button2"  onclick="check();"  />
			</td>
			<%} %>
			<td>
			<form action="<%=request.getContextPath()%>/commonemail/exportForcwbs" method="post" id="searchForm2">
				&nbsp;<select name ="exportmould2" id ="exportmould">
				          <option value ="0">默认导出模板</option>
				          <%if(exportmouldlist!=null&&exportmouldlist.size()>0){ for(Exportmould e:exportmouldlist){%>
				           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
				          <%}}%>
					</select>
					&nbsp;&nbsp;
			<input type="hidden" name="cwbs" value="<%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs")%>" >
			</form>
			</td>
		</tr>
	</table>
	<form id="searchForm1" action ="<%=request.getContextPath()%>/commonemail/showdetailbycwbs/1" method = "post">
	</form>
	<div class="right_title">
	<div style="height:10px"></div>
	
	<%if(cwborderList != null&&cwborderList.size()>0){ %>
	<div style="overflow-x:scroll; width:100% "  >
	<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">承运商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >出库时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >接货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >当前状态</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货仓库</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >入库仓库</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >下一站</td>
		</tr>
		
		<% for(CwbOrderAll c : cwborderList){
			String statetime="";
			if(stateTimeMap.containsKey(c.getCwb())){
				if(stateTimeMap.get(c.getCwb()).length()>2){
					statetime=stateTimeMap.get(c.getCwb());
				}
			}
			%>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getAttribute("dmpUrl")%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
					<td  align="center" valign="middle"><%=c.getCustomername() %></td>
					<td  align="center" valign="middle"><%=comMap.get(c.getCommonnumber()) %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=c.getOutstoreroomtime() %></td>
					<td  align="center" valign="middle"><%=statetime %></td>
					<td  align="center" valign="middle"><%=c.getOrderType() %></td>
					<td  align="center" valign="middle"><%=FlowOrderTypeEnum.getText((int)c.getFlowordertype())==null?"":FlowOrderTypeEnum.getText((int)c.getFlowordertype()).getText() %></td>
					<td  align="center" valign="middle"><%=c.getCustomerwarehousename() %></td>
					<td  align="center" valign="middle"><%=c.getStartbranchname()%></td>
					<td  align="center" valign="middle"><%=c.getNextbranchname() %></td>
				 </tr>
		 <%} %>
	</table>
	</div>
	<%} %>
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
							onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit();">
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
	<form  id="searchForm" action="<%=request.getContextPath()%>/commonemail/showdetailbycwbs/1">
	<input type="hidden" id="search2"  name="cwbs" value="<%=cwbs%>" />
	<input type="hidden" name="isshow" value="1" />
	</form>		
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>

</body>
</html>