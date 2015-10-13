<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.ExcelColumnSet"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    List<ExcelColumnSet> columnList = (List<ExcelColumnSet>)request.getAttribute("columns");
	List<Customer> customerList = (List<Customer>)request.getAttribute("customers");
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入订单类型管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var CustomerKeyAndValue = new Array();
<%for(Customer c : customerList) {%> CustomerKeyAndValue[<%=c.getCustomerid() %>]='<%=c.getCustomername() %>';<%} %>
function getCustomerValue(key,areaid){
	$("#customerid_"+key+"_"+areaid).html(CustomerKeyAndValue[key]);
}

//弹出页面中的select组件中的对应值
var columnKeyAndValue = new Array([53]);
columnKeyAndValue[0]='未设置';
columnKeyAndValue[1]='A';
columnKeyAndValue[2]='B';
columnKeyAndValue[3]='C';
columnKeyAndValue[4]='D';
columnKeyAndValue[5]='E';
columnKeyAndValue[6]='F';
columnKeyAndValue[7]='G';
columnKeyAndValue[8]='H';
columnKeyAndValue[9]='I';
columnKeyAndValue[10]='J';
columnKeyAndValue[11]='K';
columnKeyAndValue[12]='L';
columnKeyAndValue[13]='M';
columnKeyAndValue[14]='N';
columnKeyAndValue[15]='O';
columnKeyAndValue[16]='P';
columnKeyAndValue[17]='Q';
columnKeyAndValue[18]='R';
columnKeyAndValue[19]='S';
columnKeyAndValue[20]='T';
columnKeyAndValue[21]='U';
columnKeyAndValue[22]='V';
columnKeyAndValue[23]='W';
columnKeyAndValue[24]='X';
columnKeyAndValue[25]='Y';
columnKeyAndValue[26]='Z';
for (var y=1 ; y<27 ;y++){
	columnKeyAndValue[26+y] = 'A' + columnKeyAndValue[y];
}
//初始将所有select中的值进行初始化
function intiSelect(){
	for(var i = 1 ;i<53 ; i++){	
		$("select[id!='customerid']", parent.document).append("<option value='"+i+"'>"+columnKeyAndValue[i]+"</option>");
	}
}
//注入监控选择对应ABC后将其样式改变为深蓝
function initSelectCss(){
	$("select[id!='customerid']", parent.document).blur(function(){
		if($(this).val()>0){
			$("select[value!="+$(this).val()+"][id!='customerid'] option[value='"+$(this).val()+"']", parent.document).css({ color: "#ff0011", background: "blue" });
		}
	})
	$("select[id!='customerid']", parent.document).focus(function(){
		if($(this).val()!=0){
			$("select[id!="+$(this).attr("id")+"][id!='customerid'] option[value='"+$(this).val()+"']" , parent.document).css({ color: "#000000", background: "#ffffff" });
		}
	})
}

//创建功能的弹出框初始化函数
function addInit(){
	//去除已经做过配置的供货商
	<%for (ExcelColumnSet e : columnList){%>
	$("#customerid option[value='<%=e.getCustomerid()%>']", parent.document).remove(); 
	<%}%>
	intiSelect();
	initSelectCss();
}
//创建功能的弹出框初始化函数 END
//创建成功后，处理函数END
function addSuccess(){
	var customerid = $("#customerid", parent.document).val();
	$("#customerid option[value='"+customerid+"']", parent.document).remove(); 
	//$("#customerid", parent.document).val(-1); 
	//$("select[id!='customerid']", parent.document).val(0);
	location.reload();
}
//创建成功后，处理函数 END
//修改的弹出框初始化函数
function editInit(){
	intiSelect();
	initSelectCss();
	//初始化修改弹框中的select初始样式
	var selectArray = new Array();
	selectArray = $("select[value!='0'][id!='customerid']", parent.document);
	for(var i=0 ; i < selectArray.length;i++){
		$("select[value!='"+selectArray[i].value+"'][id!='customerid'] option[value='"+selectArray[i].value+"']", parent.document).css({ color: "#ff0011", background: "blue" });
	}
	//初始化以选择的select的默认值和样式
	for(var i =0 ; i < initEditArray.length ; i ++){
		var value = initEditArray[i].split(",")[0];
		var name = initEditArray[i].split(",")[1];
		if(value>0){
			$("#"+name, parent.document).val(value);
			$("select[value!="+value+"][id!='customerid'] option[value='"+value+"']", parent.document).css({ color: "#ff0011", background: "blue" });
		}
	}
}
//修改的弹出框初始化函数 END
//修改成功后，处理函数 
function editSuccess(data){
	location.reload();
}
//修改成功后，处理函数 END
//删除成功后，处理函数 
function delSuccess(data){
	location.reload();
}
//删除成功后，处理函数END
</script>
</head>
<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
	<span><input name="" type="button" value="创建excel对应记录"  class="input_button1" id="add_button"  />
	</span>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">编号</td>
			<td width="40%" align="center" valign="middle" bgcolor="#eef6ff">供货商名称</td>
			<td width="50%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		<% for(ExcelColumnSet e : columnList){ %>
		<tr>
	         <td width="10%" align="center" valign="middle"><%=e.getColumnid() %></td>
			<td width="40%" align="center" valign="middle" id="customerid_<%=e.getCustomerid() %>_<%=e.getColumnid() %>">
			<script>getCustomerValue(<%=e.getCustomerid()%>,<%=e.getColumnid()%>) </script>
			</td>
			<td width="50%" align="center" valign="middle" >
			[<a href ="javascript:getViewBox(<%=e.getCustomerid()%>);">对应字段详情查看</a><%=((((new Date()).getTime()-sdf.parse(e.getUpdatetime()).getTime())/1000)/3600)>72?"":e.getUpdatetime()+"修改" %>]
			[<a href="javascript:if(confirm('确定要删除?')){del(<%=e.getColumnid()%>);}">删除</a>]
			[<a href="javascript:edit_button(<%=e.getColumnid()%>);">修改</a>]
			
			</td>
		</tr>
		<%} %>
	</table>
	
	</div>
	
</div>
			
	<div class="jg_10"></div>
	<div class="clear"></div>

<!-- 创建导入设置的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/excelcolumn/add" />
<!-- 修改导入设置的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/excelcolumn/edit/" />
<!-- 删除导入设置的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/excelcolumn/del/" />
<!-- 查看导入设置的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/excelcolumn/view/" />
</body>
</html>
