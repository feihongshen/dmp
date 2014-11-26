<%@page language="java" import="java.util.*,java.math.BigDecimal" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.AccountDeductRecord,cn.explink.enumutil.AccountFlowOrderTypeEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
	Page page_obj = (Page)request.getAttribute("page_obj");
	String strtime=StringUtil.nullConvertToEmptyString(request.getAttribute("strtime"));
	String endtime=StringUtil.nullConvertToEmptyString(request.getAttribute("endtime")); 
	String sumOfamount=StringUtil.nullConvertToEmptyString(request.getAttribute("sumOfamount"));
	String starttimehz=StringUtil.nullConvertToEmptyString(request.getAttribute("starttimehz"));
	//String sumOfamount=StringUtil.nullConvertToEmptyString(request.getAttribute("sumOfamount"));
	String endtimehz=StringUtil.nullConvertToEmptyString(request.getAttribute("endtimehz")); 
	List<Branch> branchList=request.getAttribute("branchList")==null?null:(List<Branch>)request.getAttribute("branchList");
	Branch branch=request.getAttribute("branch")==null?new Branch():(Branch)request.getAttribute("branch");
	long branchid=Long.parseLong(request.getAttribute("branchid")==null?"0":request.getAttribute("branchid").toString());
	List<AccountDeductRecord> recordList=request.getAttribute("recordList")==null?null:(List<AccountDeductRecord>)request.getAttribute("recordList");
	long recordtype=Long.parseLong(request.getParameter("recordtype")==null?"0":request.getParameter("recordtype").toString());
	List<AccountDeductRecord> huizongList=(List<AccountDeductRecord>)request.getAttribute("huizongList");
	String flag=StringUtil.nullConvertToEmptyString(request.getAttribute("flag"));
	String message=StringUtil.nullConvertToEmptyString(request.getAttribute("message")); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账户基本信息</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript">
var branchid="<%=branchid%>"
var flag="<%=flag%>";

$(function(){
	//财务选择站点
	$("#selectBranchid").change(function(){ 
		location.href="<%=request.getContextPath()%>/accountdeductrecord/accountBasic/1?branchid="+this.value;
	});
	$("#selectBranchid").val(branchid);
	
	//控制列表显示隐藏
	if(branchid>0){
		$("#mainShow").show();
	}else{
		$("#mainShow").hide();
	}
	$("#strtime").datepicker();
	$("#endtime").datepicker();
	$("#starttimehz").datepicker();
	$("#endtimehz").datepicker();
	$("#recordtype").val(<%=recordtype%>);
	
	var $menuli = $(".kfsh_tabbtn2 ul li");
	var $menulilink = $(".kfsh_tabbtn2 ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
	//控制TAB标签高亮显示
	$("#content1").attr("style","display:none");
	$("#content2").attr("style","display:none");
	if(flag==1){
		$("#li1").attr("class","light");
		$("#content1").attr("style","display:block");
	}
	if(flag==2){
		$("#li2").attr("class","light");
		$("#content2").attr("style","display:block");
	}
});


//明细
function detailBtn(recordid){
	window.open("<%=request.getContextPath()%>/accountdeductrecord/detailByRecordList/1?recordid="+recordid);
}

<%-- //账务汇总跳转
function huizongBtn(){
	location.href="<%=request.getContextPath()%>/accountdeductrecord/huizong?flag=2&branchid="+branchid;
} --%>

function searchBtn(){
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间要小于结束时间");
		return false;
	}
	$('#searchForm').attr('action',1);
	$("#searchForm").submit();
}

function searchBtnHZ(){
	if($("#starttimehz").val()>$("#endtimehz").val()){
		alert("开始时间要小于结束时间");
		return false;
	}
	$('#searchFormHZ').attr('action',1);
	$("#searchFormHZ").submit();
}

function exportField(){	
	$("#clickExport").attr("disabled","disabled");
	$("#clickExport").val("请稍后");
 	$("#exportForm").submit(); 
}

function exportField2(){	
	$("#clickExport2").attr("disabled","disabled");
	$("#clickExport2").val("请稍后");
 	$("#exportForm2").submit(); 
}
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
	<div class="right_title">
		<div>
			<h1>帐户基本信息
				<%if(branchList!=null&&!branchList.isEmpty()){%>
					<select id="selectBranchid" name="selectBranchid" style="width:150px;">	
						<option value="0">====请选择====</option>
						<%for(Branch b: branchList){%>
							<option value="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
						<%}%>	
				    </select>
				 <%}%>
			</h1>
		<div id="mainShow">
			<div class="saomiao_topnum">
				<dl class="blue">
					<dt>站点</dt>
					<dd><%=StringUtil.nullConvertToEmptyString(branch.getBranchname())%></dd>
				</dl>
				
				<dl class="green">
					<dt>信用额度（元）</dt>
					<dd><%=StringUtil.nullConvertToBigDecimal(branch.getCredit())%></dd>
				</dl>
				
				<dl class="yellow">
					<dt>预扣帐户余额（元）</dt>
					<dd><%=StringUtil.nullConvertToBigDecimal(branch.getBalance())%></dd>
				</dl>
				<dl class="red">
					<dt>预扣欠款（元）</dt>
					<dd><%=StringUtil.nullConvertToBigDecimal(branch.getDebt())%></dd>
				</dl>
				<dl class="yellow">
					<dt>实扣帐户余额（元）</dt>
					<dd><%=StringUtil.nullConvertToBigDecimal(branch.getBalancevirt())%></dd>
				</dl>
				<dl class="red">
					<dt>实扣欠款（元）</dt>
					<dd><%=StringUtil.nullConvertToBigDecimal(branch.getDebtvirt())%></dd>
				</dl>
				<dl class="gay">
					<dt>已充值总额（元）</dt>
					<dd><%=sumOfamount%></dd>
				</dl> 
				<br clear="all">
			</div>
			<div class="jg_10"></div>
			<div class="kfsh_tabbtn2">
				<ul>
					<li><a href="#" id="li1">账务明细</a></li>
					<li><a href="#" id="li2">账务汇总</a></li>
				</ul>
			</div>
			<div class="jg_10"></div>
			
			<div class="tabbox">
      			<li  id="content1">
					<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
					&nbsp;&nbsp;
					类型：<select id="recordtype" name="recordtype" style="width:80px;">	
							<option value="0">全部</option>
							<option value="8">扣款</option>
							<option value="11">中转</option>
							<option value="12">退货</option>
							<option value="13">POS</option>
							<option value="10">充值</option>
							<option value="14">调账</option>
					    </select>
						<label for="select"></label>
						交易时间：
						<input type="text" name="strtime" id="strtime" value ="<%=strtime%>"/>
						至
						<input type="text" name="endtime" id="endtime" value ="<%=endtime%>"/>
						<input type="hidden" name="branchid" id="branchid" value="<%=branchid%>"/>
						<input type="hidden" name="flag" id="flag" value ="1"/>
						<input type="button" onclick="searchBtn()" id="find" value="查询" class="input_button2" />
						<input onclick="exportField()" id="clickExport" type="button" class="input_button2" value="导出Excel" />
					</form>
					<div class="jg_10"></div>
					<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="gd_table" >
						<tr class="font_1">
							<td bgcolor="#f4f4f4">类型</td>
							<td bgcolor="#f4f4f4">订单号</td>
							<td bgcolor="#f4f4f4">交易金额[元]</td>
							<td bgcolor="#f4f4f4">交易前余额[元]</td>
							<td bgcolor="#f4f4f4">交易后余额[元]</td>
							<td bgcolor="#f4f4f4">交易前欠款[元]</td>
							<td bgcolor="#f4f4f4">交易后欠款[元]</td>
							<td bgcolor="#f4f4f4">备注</td>
							<td bgcolor="#f4f4f4">操作人</td>
							<td bgcolor="#f4f4f4">交易时间</td>
							<!-- <td bgcolor="#f4f4f4">操作</td> -->
						</tr>
						<%if(recordList!=null&&!recordList.isEmpty()){
							for(AccountDeductRecord list:recordList){
						%>
						<tr>
							<td><%for(AccountFlowOrderTypeEnum ft : AccountFlowOrderTypeEnum.values()){if(list.getRecordtype()==ft.getValue()){ %><%=ft.getText() %><%}} %></td>
							<td align="center">
							<%if(list.getRecordtype()!=AccountFlowOrderTypeEnum.TiaoZhang.getValue()&&list.getRecordtype()!=AccountFlowOrderTypeEnum.ChongZhi.getValue()&&list.getRecordtype()!=AccountFlowOrderTypeEnum.KouKuan.getValue()){%>
								<a href="#" onclick="detailBtn('<%=list.getRecordid()%>')">明细<a>
							<%}else{
								out.print("".equals(list.getCwb())?"---":list.getCwb());
							}%>
							<%-- <%="".equals(list.getCwb())?"--":list.getCwb()%> --%></td>
							<td align="right"><strong><%=list.getFee()%></strong></td>
							<td align="right"><strong><%=list.getBeforefee()%></strong></td>
							<td align="right"><strong><%=list.getAfterfee()%></strong></td>
							<td align="right"><strong><%=list.getBeforedebt()%></strong></td>
							<td align="right"><strong><%=list.getAfterdebt()%></strong></td>
							<td align="center"><%=list.getMemo()%></td>
							<td align="center"><%=list.getUsername()%></td>
							<td align="center"><%=list.getCreatetime().substring(0,19)%></td>
							<%-- <td bgcolor="#f4f4f4">
								<%if(list.getRecordtype()!=AccountFlowOrderTypeEnum.TiaoZhang.getValue()&&list.getRecordtype()!=AccountFlowOrderTypeEnum.ChongZhi.getValue()&&list.getRecordtype()!=AccountFlowOrderTypeEnum.KouKuan.getValue()){%>
								<a href="#" onclick="detailBtn('<%=list.getRecordid()%>')">[明细]<a>
								<%}%>
							</td> --%>
						</tr>
						<%}}else{%>
						<tr>
							<td colspan="11">暂无数据</td>
						</tr>
						<%} %>
					</table>
					<!--底部翻页 -->
					<div class="jg_35"></div>
					<%if(page_obj.getMaxpage()>1){%>
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
				</li>
			
				<li id="content2">
				<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchFormHZ">
					&nbsp;&nbsp;
						<label for="select"></label>
						交易时间：
						<input type="text" name="starttimehz" id="starttimehz" value ="<%=starttimehz%>"/>
						至
						<input type="text" name="endtimehz" id="endtimehz" value ="<%=endtimehz%>"/>
						<input type="hidden" name="branchid" id="branchid" value="<%=branchid%>"/>
						<input type="hidden" name="flag" id="flag" value ="2"/>
						<input type="button" onclick="searchBtnHZ()" id="find" value="查询" class="input_button2" />
						<input onclick="exportField2()" id="clickExport2" type="button" class="input_button2" value="导出Excel" />
					</form>
					<div class="jg_10"></div>
					<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="gd_table" >
						<tr class="font_1">
							<td bgcolor="#f4f4f4">类型11</td>
							<td bgcolor="#f4f4f4">账户增加金额[元]</td>
							<td bgcolor="#f4f4f4">账户减少金额[元]</td>
							<td bgcolor="#f4f4f4">单数</td>
							<td bgcolor="#f4f4f4">交易时间</td>
						</tr>
						<%
							if(huizongList!=null&&!huizongList.isEmpty()){
								for(AccountDeductRecord list:huizongList){
						%>
						<tr>
							<td align="center"><%for(AccountFlowOrderTypeEnum ft : AccountFlowOrderTypeEnum.values()){if(list.getRecordtype()==ft.getValue()){ %><%=ft.getText() %><%}} %></td>
							<td><strong><%if(list.getRecordtype()!=AccountFlowOrderTypeEnum.KouKuan.getValue()){out.print(list.getFee());}else{out.print("---");}%></strong></td>
							<td><strong><%if(list.getRecordtype()==AccountFlowOrderTypeEnum.KouKuan.getValue()){out.print(list.getFee());}else{out.print("---");}%></strong></td>
							<td><%=list.getNums()%></td>
							<td><%=list.getCreatetime().substring(0,10)%></td>
						</tr>
						<%}%>
						<tr>
							<td colspan="5" align="left"><font  color="red" style="font-weight:bold;font-size:18px;"><%=message%></font></td>
						</tr>
						<%}else{%>
						<tr>
							<td colspan="5">暂无数据</td>
						</tr>
						<%}%>
					</table>
				</li>
			</div>
	</div>
</div>
<form action="<%=request.getContextPath() %>/accountdeductrecord/exportAccountList" method="post" id="exportForm">
	<input type="hidden" name="branchid" id="branchid" value="<%=branchid%>"/>
	<input type="hidden" name="recordtype" id="recordtype" value="<%=recordtype%>"/>
	<input type="hidden" name="strtime" id="strtime" value="<%=strtime%>"/>
	<input type="hidden" name="endtime" id="endtime" value="<%=endtime%>"/>
</form>


<form action="<%=request.getContextPath() %>/accountdeductrecord/exportSumList" method="post" id="exportForm2">
	<input type="hidden" name="branchid" id="branchid" value="<%=branchid%>"/>
	<input type="hidden" name="recordtype" id="recordtype" value="<%=recordtype%>"/>
	<input type="hidden" name="strtime" id="strtime" value="<%=starttimehz%>"/>
	<input type="hidden" name="endtime" id="endtime" value="<%=endtimehz%>"/>
</form>
<script type="text/javascript">
//站点下拉框赋值
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>