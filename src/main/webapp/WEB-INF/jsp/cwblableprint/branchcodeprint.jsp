<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
    List<Branch> branches = (List<Branch>)request.getAttribute("branches");
    Page page_obj = (Page)request.getAttribute("page_obj");
    int sitetype= (Integer)request.getAttribute("sitetype");
    int pagesize= (Integer)request.getAttribute("pagesize");
    List<Integer> pagesizeList=new ArrayList<Integer>();
    pagesizeList.add(10);
    pagesizeList.add(50);
    pagesizeList.add(100);
    pagesizeList.add(500);
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>条形码打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function isgetallcheck(){
	if($('input[name="isprint"]:checked').size()>0){
		$('input[name="isprint"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="isprint"]').attr("checked",true);
	}
}
function sub(){
	$("#formsubmit").submit();
		
}
function printAll(){
	
}
function submitCwbPrint(){
	if(<%=branches.size()>0%>){
	var branchid="";
		var isprint = "";
		$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
			isprint = $(this).val();
			if($.trim(isprint).length!=0){
			branchid+=""+isprint+",";
			}
			});
		if(isprint==""){
			alert("请选择要打印的订单！");
		}else{
		$("#btnval").attr("disabled","disabled");
	 	$("#btnval").val("请稍后……");
	 	$("#branchid").val(branchid.substring(0, branchid.length-1));
	 	$("#searchForm2").submit();
		}
	}else{
		alert("没有做查询操作，不能打印！");
	};
}
</script>

<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview(branchid) {	
	CreateOneFormPage(branchid);
	LODOP.PREVIEW();	
};
function prn1_print(branchid) {		
	CreateOneFormPage(branchid);
	LODOP.PRINT();	
};
function prn1_printA(branchid) {		
	CreateOneFormPage(branchid);
	LODOP.PRINTA(); 	
};	
function CreateOneFormPage(branchid){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	var strBodyStyle="<style>"+document.getElementById("style1").innerHTML+"</style>";
	
	LODOP.PRINT_INIT("标签打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	//LODOP.ADD_PRINT_HTM(1,0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
	if(List!=null&&List.size()>0){
	for (var i = 0; i < List.size(); i++) {
		LODOP.NewPage();
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("printTable"+i).innerHTML+"</body>";
		LODOP.ADD_PRINT_HTM(0,0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
		
		//LODOP.ADD_PRINT_RECT(0,0,360,515,0,1);
		var cwb = branchid.toString().split(",")[i];
		LODOP.ADD_PRINT_BARCODE(25,250,150,70,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
	}
	}
};

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
	
	}
}
function tip(val){
	if(val.value=='cwb')
		{
		$("#tip").html("多个订单号用回车隔开,数量小于或等于1000（订单号不可大于9位）");		
		}
	if(val.value=='baleno')
		{
		$("#tip").html("多个包号用回车隔开,数量小于或等于1000（包号不可大于21位）");		
		}
}
function load(val){
	if(val=='cwb')
		{
		$("#tip").html("多个订单号用回车隔开,数量小于或等于1000（订单号不可大于9位）");		
		}
	if(val=='baleno')
		{
		$("#tip").html("多个包号用回车隔开,数量小于或等于1000（包号不可大于21位）");		
		}
}
</script>
<body style="background:#eef9ff" marginwidth="0" marginheight="0" onload="load('')">

		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath()%>/cwbLablePrint/barcodeprint" >手工输入生成</a></li>
				<li><a href="<%=request.getContextPath()%>/cwbLablePrint/randomcodeprint">随机生成</a></li>
				<li><a href="#" class="light">机构条形码打印</a></li>
			</ul>
		</div>
	<div>
		<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
			机构名称：<input type ="text" name ="branchname"  class="input_text1" value = "<%=request.getParameter("branchname")==null?"":request.getParameter("branchname") %>"/>
			机构地址：<input type ="text" name ="branchaddress"  class="input_text1" value = "<%=request.getParameter("branchaddress")==null?"":request.getParameter("branchaddress") %>"/>
			机构类型：
			<select name="sitetype">
			<option value="0">请选择</option>
			<%for(BranchEnum branchEnum:BranchEnum.values()){ %>
			<option value="<%=branchEnum.getValue() %>" <%if(sitetype==branchEnum.getValue()){%>selected="selected" <%} %>><%=branchEnum.getText() %></option>
			<%} %>
			</select>
			每页显示条数：
			<select name="pagesize">
			<option value="0">请选择</option>
			<%for(Integer num:pagesizeList){ %>
			<option value="<%=num %>" <%if(pagesize==num){%>selected="selected" <%} %>><%=num%></option>
			<%} %>
			</select>
			
			<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="查询"  class="input_button2" />
			<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
			<input type="button" value="生成打印" onclick='submitCwbPrint();' class="input_button2">
			
		</form>
	</div>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				<tr class="font_1">
						<td width="60" align="center" valign="middle" bgcolor="#f3f3f3"><a style="cursor: pointer;" onclick="isgetallcheck();">全选</a></td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">机构名称</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">机构编号</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">地址</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">联系人</td>
						<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">电话</td>
						<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">手机</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">机构类型</td>
					</tr>
					  <% for(Branch b : branches){ %>
					<tr>
						<td align="center">
						<input id="isprint" type="checkbox" value="<%="@zd_"+b.getBranchid()+"#"+b.getBranchcode()+"&"+b.getBranchname() %>" name="isprint"/>
						</td>
					 	<td width="20%" align="center" valign="middle"><%=b.getBranchname() %></td>
					 	<td width="10%" align="center" valign="middle"><%=b.getBranchcode() %></td>
						<td width="10%" align="left" valign="middle"><%=b.getBranchaddress() %></td>
						<td width="10%" align="center" valign="middle"><%=b.getBranchcontactman() %></td>
						<td width="15%" align="center" valign="middle"><%=b.getBranchphone() %></td>
						<td width="15%" align="center" valign="middle"><%=b.getBranchmobile() %></td>
						<td width="10%" align="center" valign="middle"><%=b.getSitetypeName() %></td>
					</tr>
					<%} %>
				</table>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				
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
			
	<div class="jg_10"></div>
	<div class="clear"></div>
		<form action="<%=request.getContextPath()%>/cwbLablePrint/printBranchcode" method="post" id="searchForm2">
			<input type="hidden" name="branchids" id="branchid" value=""/>
		</form>
		
</body>

</html>