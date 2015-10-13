<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CommonEmaildate"%>
<%@page import="cn.explink.domain.Common"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Common> comList = (List<Common>)request.getAttribute("commList");
  List<CommonEmaildate> commonList = (List<CommonEmaildate>)request.getAttribute("commonEmaildateList");
  Map<String,String> comMap = (Map<String,String>)request.getAttribute("comMap");
  Page page_obj = (Page)request.getAttribute("page_obj");
  String startdate=request.getParameter("startdate")==null?"":request.getParameter("startdate");
  String enddate=request.getParameter("enddate")==null?"":request.getParameter("enddate");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出库给承运商查询</title>
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
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
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
.kfsh_tabbtn li a { display:block; line-height:31px; color:#666; font-size:12px;background:url(<%=request.getAttribute("dmpUrl")%>/images/uc_menubg3.png) no-repeat  }
.kfsh_tabbtn li a:hover { text-decoration:none; color:#C30; background-position:0px -31px }
.kfsh_tabbtn .light { background-position:0px -62px;background-color:#FFF; }
.kfsh_text{height:48px}
</style>
<script type="text/javascript">
$(document).ready(function() {
	   //获取下拉框的值
	   $("#find").click(function(){
	         if(check()){
	            $("#isshow").val(1);
		    	$("#searchForm").submit();
		    	$("#find").attr("disabled","disabled");
				$("#find").val("请稍等..");
	         }
	   });
	});
function check(){
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if($("#endtime").val()>"<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"){
		$("#endtime").val("<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>");
	}
	return true;
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
</head>

<body style="background:#eef9ff">
<div class="right_box " >
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath() %>/commonemail/list/1" class="light">按批次查询</a></li>
				<li><a href="<%=request.getContextPath() %>/commonemail/showdetailbycwbs/1">按订单查询</a></li>
			</ul>
		</div>
		<div class="jg_10"></div>
		<div style="position:relative; z-index:0; " >
				<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
								<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
									承运商：<select name ="commonnumber" id ="commonnumber" >
									          <option value ="">请选择</option>
									          <%if(comList != null && comList.size()>0){ %>
									          <%for(Common c : comList){ %>
									           <option value =<%=c.getCommonnumber() %> 
									           <%if(c.getCommonnumber().equals(request.getParameter("commonnumber")) ){ %>selected="selected" <%} %> ><%=c.getCommonname() %></option>
									          <%} }%>
									        </select>
									  产生批次时间<input type ="text" name ="startdate" id="strtime"  value="<%=startdate %>"/>
										到
											<input type ="text" name ="enddate" id="endtime"  value="<%=enddate %>"/>
											<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
											<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="搜索" class="input_button2" />
								</form>
						</div>
					
					</div>
					
					
<%-- 	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		承运商：<select name ="commonnumber" id ="commonnumber" >
		          <option value ="">请选择</option>
		          <%if(comList != null && comList.size()>0){ %>
		          <%for(Common c : comList){ %>
		           <option value =<%=c.getCommonnumber() %> 
		           <%if(c.getCommonnumber().equals(request.getParameter("commonnumber")) ){ %>selected="selected" <%} %> ><%=c.getCommonname() %></option>
		          <%} }%>
		        </select>
		  产生批次时间<input type ="text" name ="startdate" id="strtime"  value="<%=startdate %>"/>
			到
				<input type ="text" name ="enddate" id="endtime"  value="<%=enddate %>"/>
				<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
		<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="搜索" class="input_button2" />
	</form> --%>
	</div>
	<div class="right_title">
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">批次编号</td>
			<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">批次时间</td>
			<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">承运商</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单数量</td>
		</tr>
		<% for(CommonEmaildate c : commonList){ %>
		<tr>
			<td width="10%" align="center" valign="middle"><%=c.getEmaildateid()%></td>
			<td width="25%" align="center" valign="middle"><%=c.getEmaildate()%></td>
			<td width="10%" align="center" valign="middle"><%=comMap.get(c.getCommoncode().replace("'",""))%></td>
			<td width="30%" align="center" valign="middle"><a href="<%=request.getContextPath()%>/commonemail/show/<%=c.getEmaildateid() %>/1"><%=c.getCwbcount()%></a></td>
		</tr>
		<%} %>
	</table>
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
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>

</body>
</html>

