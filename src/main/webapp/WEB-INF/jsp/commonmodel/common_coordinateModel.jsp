<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CommonModel"%>
<%@page import="cn.explink.util.ResourceBundleUtil"%>
<%
List<CommonModel> commonmodellist = (List<CommonModel>)request.getAttribute("commonmodellist");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<style type="text/css">
/*margin and padding on body element
  can introduce errors in determining
  element position and are not recommended;
  we turn them off as a foundation for YUI
  CSS treatments. */
body {
	margin:0;
	padding:0;
}
</style>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/fonts-min.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/yahoo-dom-event.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dragdrop-min.js"></script>


<!--begin custom header content for this example-->

<style type="text/css">

#playground {
    position: relative;
    height: 200px;
}
.dd-demo1 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:20px;
    width:150px;
}

.dd-demo1 .dd-handle1 {
    background: #003366;
    cursor:move;
}

.dd-demo2 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:20px;
    width:200px;
}

.dd-demo2 .dd-handle2 {
    background: #003366;
    cursor:move;
}

.dd-demo3 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:40px;
    width:200px;
}

.dd-demo3 .dd-handle3 {
    background: #003366;
    cursor:move;
}

.dd-demo4 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:20px;
    width:200px;
}

.dd-demo4 .dd-handle4 {
    background: #003366;
    cursor:move;
}
.dd-demo5 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:20px;
    width:180px;
}

.dd-demo5 .dd-handle5 {
    background: #003366;
    cursor:move;
}
.dd-demo6 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:20px;
    width:130px;
}

.dd-demo6 .dd-handle6 {
    background: #003366;
    cursor:move;
}
.dd-demo7 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:20px;
    width:140px;
}

.dd-demo7 .dd-handle7 {
    background: #003366;
    cursor:move;
}
.dd-demo8 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:20px;
    width:200px;
}

.dd-demo8 .dd-handle8 {
    background: #003366;
    cursor:move;
}
.dd-demo9 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:30px;
    width:220px;
}

.dd-demo9 .dd-handle9 {
    background: #003366;
    cursor:move;
}
.dd-demo10 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:20px;
    width:100px;
}

.dd-demo10 .dd-handle10 {
    background: #003366;
    cursor:move;
}
.dd-demo11 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:18px;
    width:180px;
}

.dd-demo11 .dd-handle11 {
    background: #003366;
    cursor:move;
}
.dd-demo12 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:17px;
    width:130px;
}

.dd-demo12 .dd-handle12 {
    background: #003366;
    cursor:move;
}
.dd-demo13 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:20px;
    width:250px;
}

.dd-demo13 .dd-handle13 {
    background: #003366;
    cursor:move;
}
.dd-demo14 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:20px;
    width:250px;
}

.dd-demo14 .dd-handle14 {
    background: #003366;
    cursor:move;
}
.dd-demo15 {
    position:absolute;
    border:4px solid #666;
    text-align:center;
    color:#fff;
    height:20px;
    width:130px;
}

.dd-demo15 .dd-handle15 {
    background: #003366;
    cursor:move;
}


#dd-demo-getdate {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-workname { 
    background-color:#6D739A;
	top:0px; left:0px;
}

#dd-demo-goodsname { 
    background-color:#566F4E;
	top:0px; left:0px;
}

#dd-demo-cwb {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-salecompany {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-name {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-mobile {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-workname1 {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-address {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-weight {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-size {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-posecode {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-remark {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-bigwords {
    background-color:#7E5B60;
    top:0px; left:0px;
}
#dd-demo-smallwords {
    background-color:#7E5B60;
    top:0px; left:0px;
}

</style>
<title>承运商模版设置</title>
<script type="text/javascript">
function resetform(){
	$("#cwb").val("");
	$("#commoncwb").val("");
}
</script>
<script language="javascript" type="text/javascript">

(function() {

    var  dd1;
	var  dd2;
	var  dd3;
	var  dd4;
	var  dd5;
	var  dd6;
	var  dd7;
	var  dd8;
	var  dd9;
	var  dd10;
	var  dd11;
	var  dd12;
	var  dd13;
	var  dd14;
	var  dd15;
	
    YAHOO.util.Event.onDOMReady(function() {
		dd1 = new YAHOO.util.DD("dd-demo-getdate");
        dd1.setHandleElId("dd-handle-1");
	
        dd2 = new YAHOO.util.DD("dd-demo-workname");
        dd2.setHandleElId("dd-handle-2");
		
		dd3 = new YAHOO.util.DD("dd-demo-goodsname");
        dd3.setHandleElId("dd-handle-3");
		
		dd4 = new YAHOO.util.DD("dd-demo-cwb");
        dd4.setHandleElId("dd-handle-4");
		
		dd5 = new YAHOO.util.DD("dd-demo-salecompany");
        dd5.setHandleElId("dd-handle-5");
		
		dd6 = new YAHOO.util.DD("dd-demo-name");
        dd6.setHandleElId("dd-handle-6");
		
		dd7 = new YAHOO.util.DD("dd-demo-mobile");
        dd7.setHandleElId("dd-handle-7");
		
		dd8 = new YAHOO.util.DD("dd-demo-workname1");
        dd8.setHandleElId("dd-handle-8");
		
		dd9 = new YAHOO.util.DD("dd-demo-address");
        dd9.setHandleElId("dd-handle-9");
		
		dd10 = new YAHOO.util.DD("dd-demo-weight");
        dd10.setHandleElId("dd-handle-10");
		
		dd11 = new YAHOO.util.DD("dd-demo-size");
        dd11.setHandleElId("dd-handle-11");
		
		dd12 = new YAHOO.util.DD("dd-demo-posecode");
        dd12.setHandleElId("dd-handle-12");
		
		dd13 = new YAHOO.util.DD("dd-demo-remark");
        dd13.setHandleElId("dd-handle-13");
        
        dd14 = new YAHOO.util.DD("dd-demo-bigwords");
        dd14.setHandleElId("dd-handle-14");
        
        dd15 = new YAHOO.util.DD("dd-demo-smallwords");
        dd15.setHandleElId("dd-handle-15");

    });

})();

var str="";
function getXY(){
	str = "{";
	str += "getdate:\""+$("#dd-demo-getdate").css("top")+","+$("#dd-demo-getdate").css("left")+"\";";
	str += "workname:\""+$("#dd-demo-workname").css("top")+","+$("#dd-demo-workname").css("left")+"\";";
	str += "goodsname:\""+$("#dd-demo-goodsname").css("top")+","+$("#dd-demo-goodsname").css("left")+"\";";
	str += "cwb:\""+$("#dd-demo-cwb").css("top")+","+$("#dd-demo-cwb").css("left")+"\";";
	str += "salecompany:\""+$("#dd-demo-salecompany").css("top")+","+$("#dd-demo-salecompany").css("left")+"\";";
	str += "name:\""+$("#dd-demo-name").css("top")+","+$("#dd-demo-name").css("left")+"\";";
	str += "mobile:\""+$("#dd-demo-mobile").css("top")+","+$("#dd-demo-mobile").css("left")+"\";";
	str += "workname1:\""+$("#dd-demo-workname1").css("top")+","+$("#dd-demo-workname1").css("left")+"\";";
	str += "address:\""+$("#dd-demo-address").css("top")+","+$("#dd-demo-address").css("left")+"\";";
	str += "weight:\""+$("#dd-demo-weight").css("top")+","+$("#dd-demo-weight").css("left")+"\";";
	str += "size:\""+$("#dd-demo-size").css("top")+","+$("#dd-demo-size").css("left")+"\";";
	str += "posecode:\""+$("#dd-demo-posecode").css("top")+","+$("#dd-demo-posecode").css("left")+"\";";
	str += "remark:\""+$("#dd-demo-remark").css("top")+","+$("#dd-demo-remark").css("left")+"\";";
	str += "bigwords:\""+$("#dd-demo-bigwords").css("top")+","+$("#dd-demo-bigwords").css("left")+"\";";
	str += "smallwords:\""+$("#dd-demo-smallwords").css("top")+","+$("#dd-demo-smallwords").css("left")+"\";";
	str = str+"}";
	return str;
}
function showOrHideBox(n,top,left){
	if(!top){
		top="0px";
		left="0px";
	}else{
		$("#check"+n).attr("checked","checked");
	}
	if($("#check"+n).attr("checked")=="checked"){
		$("#dd-demo-"+n).show();
	}else{
		$("#dd-demo-"+n).hide();
	}
	$("#dd-demo-"+n).css("top",top);
	$("#dd-demo-"+n).css("left",left);
	getXY();
}
function check_setcommonmodel(){
	if($("#modelname").val()==0){
		alert("请选择模版");
		return false;
	}
	
	if($("#playground").attr("style").indexOf("background-image")==-1){
		alert("请上传面单图片");
		return false;
	}
	if(getCheck()==0){
		alert("请选择按钮");
		return false;
	}
	return true;
}
function getCheck(){
	var count = 0;
	if($("#checkgetdate").attr("checked")=="checked"){
		count +=1;
	}else if($("#checkworkname").attr("checked")=="checked"){
		count +=1;
	}else if($("#checkgoodsname").attr("checked")=="checked"){
		count +=1;
	}else if($("#checkcwb").attr("checked")=="checked"){
		count +=1;
	}else if($("#checksalecompany").attr("checked")=="checked"){
		count +=1;
	}else if($("#checkname").attr("checked")=="checked"){
		count +=1;
	}else if($("#checkmobile").attr("checked")=="checked"){
		count +=1;
	}else if($("#checkworkname1").attr("checked")=="checked"){
		count +=1;
	}else if($("#checkaddress").attr("checked")=="checked"){
		count +=1;
	}else if($("#checkweight").attr("checked")=="checked"){
		count +=1;
	}else if($("#checksize").attr("checked")=="checked"){
		count +=1;
	}else if($("#checkposecode").attr("checked")=="checked"){
		count +=1;
	}else if($("#checkremark").attr("checked")=="checked"){
		count +=1;
	}else if($("#checkbigwords").attr("checked")=="checked"){
		count +=1;
	}else if($("#checksmallwords").attr("checked")=="checked"){
		count +=1;
	}
	return count;
}
function changeBackground(id){
	if(id=="0"){
		location.href="<%=request.getContextPath()%>/commonmodel/tosetcoordinate";
	}else{
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/commonmodel/getModel/"+id,
			dataType:"json",
			success : function(data) {
				if(data.coordinate != undefined){
					if(data.imageurl!=""){
						$("#playground").css("background-image","url('"+data.imageurl+"')");
					}
					
					$("div[id^='dd-demo-']").hide();
					$("input[id^='check']").attr("checked",false);
					$("div[id^='dd-demo-']").css("top",'0px');
					$("div[id^='dd-demo-']").css("left",'0px');
					
					if(data.coordinate.getdate.split(",")[0]!='0px'&&data.coordinate.getdate.split(",")[1]!="0px")
						showOrHideBox('getdate',data.coordinate.getdate.split(",")[0],data.coordinate.getdate.split(",")[1]);
					
					if(data.coordinate.workname.split(",")[0]!='0px'&&data.coordinate.workname.split(",")[1]!="0px")
						showOrHideBox('workname',data.coordinate.workname.split(",")[0],data.coordinate.workname.split(",")[1]);
					
					if(data.coordinate.goodsname.split(",")[0]!='0px'&&data.coordinate.goodsname.split(",")[1]!="0px")
						showOrHideBox('goodsname',data.coordinate.goodsname.split(",")[0],data.coordinate.goodsname.split(",")[1]);
					
					if(data.coordinate.cwb.split(",")[0]!='0px'&&data.coordinate.cwb.split(",")[1]!="0px")
						showOrHideBox('cwb',data.coordinate.cwb.split(",")[0],data.coordinate.cwb.split(",")[1]);
					
					if(data.coordinate.salecompany.split(",")[0]!='0px'&&data.coordinate.salecompany.split(",")[1]!="0px")
						showOrHideBox('salecompany',data.coordinate.salecompany.split(",")[0],data.coordinate.salecompany.split(",")[1]);
					
					if(data.coordinate.name.split(",")[0]!='0px'&&data.coordinate.name.split(",")[1]!="0px")
						showOrHideBox('name',data.coordinate.name.split(",")[0],data.coordinate.name.split(",")[1]);
					
					if(data.coordinate.mobile.split(",")[0]!='0px'&&data.coordinate.mobile.split(",")[1]!="0px")
						showOrHideBox('mobile',data.coordinate.mobile.split(",")[0],data.coordinate.mobile.split(",")[1]);
					
					if(data.coordinate.workname1.split(",")[0]!='0px'&&data.coordinate.workname1.split(",")[1]!="0px")
						showOrHideBox('workname1',data.coordinate.workname1.split(",")[0],data.coordinate.workname1.split(",")[1]);
					
					if(data.coordinate.address.split(",")[0]!='0px'&&data.coordinate.address.split(",")[1]!="0px")
						showOrHideBox('address',data.coordinate.address.split(",")[0],data.coordinate.address.split(",")[1]);
					
					if(data.coordinate.weight.split(",")[0]!='0px'&&data.coordinate.weight.split(",")[1]!="0px")
						showOrHideBox('weight',data.coordinate.weight.split(",")[0],data.coordinate.weight.split(",")[1]);
					
					if(data.coordinate.size.split(",")[0]!='0px'&&data.coordinate.size.split(",")[1]!="0px")
						showOrHideBox('size',data.coordinate.size.split(",")[0],data.coordinate.size.split(",")[1]);
					
					if(data.coordinate.posecode.split(",")[0]!='0px'&&data.coordinate.posecode.split(",")[1]!="0px")
						showOrHideBox('posecode',data.coordinate.posecode.split(",")[0],data.coordinate.posecode.split(",")[1]);
					
					if(data.coordinate.remark.split(",")[0]!='0px'&&data.coordinate.remark.split(",")[1]!="0px")
						showOrHideBox('remark',data.coordinate.remark.split(",")[0],data.coordinate.remark.split(",")[1]);
					
					if(data.coordinate.bigwords.split(",")[0]!='0px'&&data.coordinate.bigwords.split(",")[1]!="0px")
						showOrHideBox('bigwords',data.coordinate.bigwords.split(",")[0],data.coordinate.bigwords.split(",")[1]);
					
					if(data.coordinate.smallwords.split(",")[0]!='0px'&&data.coordinate.smallwords.split(",")[1]!="0px")
						showOrHideBox('smallwords',data.coordinate.smallwords.split(",")[0],data.coordinate.smallwords.split(",")[1]);
				}
			}
				
		});
	}
}
function SetCommonModelImageurl(form,id,imageurl){
	$.ajax({
		type: "POST",
		url:$(form).attr("action")+id+"?imageurl="+imageurl,
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			alert(data.error);
		}
	});
}

function SetCommonModel(form,parm,coordinate){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/commonmodel/setcoordinate/"+parm+"?coordinate="+coordinate,
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			alert(data.error);
		}
	});
}
function addInit(){
	//无处理
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val(-1);
	$("#searchForm").submit();
}
</script>
</head>
<body style="background:#f5f5f5" class="yui-skin-sam">
<div class="right_box">
	<div class="inputselect_box">
	<form id="searchForm" name="searchForm" action="<%=request.getContextPath()%>/commonmodel/tosetcoordinate"></form>
	<form  action="<%=request.getContextPath()%>/commonmodel/setimageurl/" method="post"  enctype="multipart/form-data">
			模版：
				<select id="modelname" name="modelname" onchange="changeBackground($(this).val());">
					<option value="0">请选择模版名称</option>
					<%if(commonmodellist!=null){for(CommonModel c : commonmodellist){ %>
						<option value="<%=c.getId()%>"><%=c.getModelname() %></option>
					<%}} %>
				</select>
				
				上传面单图片：<input type ="file" name="imageurl"  id="imageurl"/>
				<input type="submit" value="上传">
				<input type="button" value="保存" onclick='if(check_setcommonmodel()){SetCommonModel("setcoordinateForm",$("#modelname").val(),getXY());}return false;'>
				<span><input name="" type="button" value="创建模版" class="input_button1"  id="add_button"  /></span>
				<br/>
	</form>
</div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="right_title">
		<form id="setcoordinateForm" name="setcoordinateForm" action="<%=request.getContextPath()%>/commonmodel/setcoordinate/" method="post" onSubmit="return false;">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">		
				<tr class="font_1">
					<td>	
						请选择按钮：
						<input type="checkbox" value="收寄日期" id="checkgetdate" onclick="showOrHideBox('getdate');">收寄日期 
						<input type="checkbox" value="单位名称" id="checkworkname" onclick="showOrHideBox('workname');">单位名称
						<input type="checkbox" value="内件品名" id="checkgoodsname" onclick="showOrHideBox('goodsname');">内件品名
						<input type="checkbox" value="订单号" id="checkcwb" onclick="showOrHideBox('cwb');">订单号 
						<input type="checkbox" value="销售公司" id="checksalecompany" onclick="showOrHideBox('salecompany');">销售公司
						<input type="checkbox" value="收件人姓名" id="checkname" onclick="showOrHideBox('name');">收件人姓名
						<input type="checkbox" value="收件人电话" id="checkmobile" onclick="showOrHideBox('mobile');">收件人电话 
						<input type="checkbox" value="单位名称" id="checkworkname1" onclick="showOrHideBox('workname1');">单位名称 
						<input type="checkbox" value="收件人地址" id="checkaddress" onclick="showOrHideBox('address');">收件人地址
						<input type="checkbox" value="重量" id="checkweight" onclick="showOrHideBox('weight');">重量 
						<input type="checkbox" value="体积" id="checksize" onclick="showOrHideBox('size');">体积 
						<input type="checkbox" value="邮政编码" id="checkposecode" onclick="showOrHideBox('posecode');">邮政编码
						<input type="checkbox" value="备注" id="checkremark" onclick="showOrHideBox('remark');">备注
						<input type="checkbox" value="应付款（大写）" id="checkbigwords" onclick="showOrHideBox('bigwords');">应付款（大写）
						<input type="checkbox" value="应付款（小写）" id="checksmallwords" onclick="showOrHideBox('smallwords');">应付款（小写）
					</td>
				</tr>
				<tr>
					<td>
						<div id="playground" style="width:900px;height: 480px">
							<div id="dd-demo-getdate" class="dd-demo1" style="display:none;">
							<div id="dd-handle-1" class="dd-handle1" onclick="getXY()">收寄日期</div>
							</div>
							<div id="dd-demo-workname" class="dd-demo2" style="display:none;">
							<div id="dd-handle-2" class="dd-handle2" onclick="getXY()">单位名称</div>
							</div>
							<div id="dd-demo-goodsname" class="dd-demo3" style="display:none;">
							<div id="dd-handle-3" class="dd-handle3" onclick="getXY()">内件品名</div>
							</div>
							<div id="dd-demo-cwb" class="dd-demo4" style="display:none;">
							<div id="dd-handle-4" class="dd-handle4" onclick="getXY()">订单号</div>
							</div>
							<div id="dd-demo-salecompany" class="dd-demo5" style="display:none;">
							<div id="dd-handle-5" class="dd-handle5" onclick="getXY()">销售公司</div>
							</div>
							<div id="dd-demo-name" class="dd-demo6" style="display:none;">
							<div id="dd-handle-6" class="dd-handle6" onclick="getXY()">收件人姓名</div>
							</div>
							<div id="dd-demo-mobile" class="dd-demo7" style="display:none;">
							<div id="dd-handle-7" class="dd-handle7" onclick="getXY()">收件人电话</div>
							</div>
							<div id="dd-demo-workname1" class="dd-demo8" style="display:none;">
							<div id="dd-handle-8" class="dd-handle8" onclick="getXY()">单位名称</div>
							</div>
							<div id="dd-demo-address" class="dd-demo9" style="display:none;">
							<div id="dd-handle-9" class="dd-handle9" onclick="getXY()">收件人地址</div>
							</div>
							<div id="dd-demo-weight" class="dd-demo10" style="display:none;">
							<div id="dd-handle-10" class="dd-handle10" onclick="getXY()">重量</div>
							</div>
							<div id="dd-demo-size" class="dd-demo11" style="display:none;">
							<div id="dd-handle-11" class="dd-handle11" onclick="getXY()">体积</div>
							</div>
							<div id="dd-demo-posecode" class="dd-demo12" style="display:none;">
							<div id="dd-handle-12" class="dd-handle12" onclick="getXY()">邮政编码</div>
							</div>
							<div id="dd-demo-remark" class="dd-demo13" style="display:none;">
							<div id="dd-handle-13" class="dd-handle13" onclick="getXY()">备注</div>
							</div>
							<div id="dd-demo-bigwords" class="dd-demo14" style="display:none;">
							<div id="dd-handle-14" class="dd-handle14" onclick="getXY()">应付款（大写）</div>
							</div>
							<div id="dd-demo-smallwords" class="dd-demo15" style="display:none;">
							<div id="dd-handle-15" class="dd-handle15" onclick="getXY()">应付款（小写）</div>
							</div>
						</div>
					</td>
				</tr>
				</table>
			</form>
			
		</div>
	</div>


<div class="clear"></div>
<input type="hidden" id="add" value="<%=request.getContextPath()%>/commonmodel/add" />
<script type="text/javascript">
$("#modelname").val(<%=request.getParameter("modelname") %>);
</script>
</body>
</html>