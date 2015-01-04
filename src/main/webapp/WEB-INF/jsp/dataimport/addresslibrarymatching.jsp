<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.domain.EmailDate"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="java.util.List,java.util.ArrayList"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@ page contentType="text/html; charset=UTF-8"%>


<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css"
	type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js"
	type="text/javascript"></script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function getOs()  
{  
    var OsObject = "";  
   if(navigator.userAgent.indexOf("MSIE")>0) {  
        return "MSIE";  
   }  
   if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){  
        return "Firefox";  
   }  
   if(isSafari=navigator.userAgent.indexOf("Safari")>0) {  
        return "Safari";  
   }   
   if(isCamino=navigator.userAgent.indexOf("Camino")>0){  
        return "Camino";  
   }  
   if(isMozilla=navigator.userAgent.indexOf("Gecko/")>0){  
        return "Gecko";  
   }  
    
}  
// alert("您的浏览器类型为:"+getOs()); 

 	function checked(form) {
		if ($("#cwbs").val() == "") {
			alert("订单号不能为空");
			return false;
		}
		if ($("#branches").val() == "") {
			alert("站点不能为空");
			return false;
		}
		var branchArray = $("#branches").val().split("\n");
		var cwbArray = $("#cwbs").val().split("\n");
		
		if (branchArray.length != cwbArray.length) {
			alert("订单号和站点数量不一致!请检查！");
			return false;
		}
		/* $(".tishi_box",parent.document).hide(); */
		if(branchArray.length = cwbArray.length){
			$.ajax({
				type:"post",
				url:$(form).attr("action"),
				data:$(form).serialize(),
				dataType:"json",
				success:function(data){
					/* alert('chegnggg');
					$(".tishi_box",parent.document).html(data.error);
					$(".tishi_box",parent.document).show();
					setTimeout( "$(\".tishi_box \",parent.document).hide(1000)", 2000); */
					if(data.errorCode==0){
						//alert('更新成功');
						//alert('更新成功!单号,站点:'+data.cwbbranches);
						$("#msg").html('更新成功!单号,站点:'+data.cwbbranches);
					}else{
						alert('更新失败,单号:'+data.cwb+',原因:'+data.error);
					}
				}
				
			});
		
		
		}
		
 	}
	
	
	
	
</script>
</head>
<body style="background: #eef9ff">

	<div class="right_box">
		<div class="menucontant">
			<div class="uc_midbg">
				<ul>
					<li><a href="excelimportPage">导入数据</a></li>
					<li><a href="reexcelimportPage">导入查询</a></li>
					<!-- <li><a href="batchedit" >批量修改</a></li> -->
					<li><a href="datalose">数据失效</a></li>
					<li><a href="editBranch">修改匹配站</a></li>
					<li><a href="editBranchonBranch">匹配站按站</a></li>
					<li><a href="editBatchBranch">批量匹配站</a></li>
					<li><a href="reproducttranscwb">运单号生成</a></li>
					<li><a href="addresslibrarymatching" class="light">手动匹配</a></li>
					<li><font color="red">地址库已开启</font></li>
				</ul>
			</div>
			<form onsubmit="checked(this);return false;" name="addresslibrarymatchingForm" id="addresslibrarymatchingForm" method="POST" action="<%=request.getContextPath()%>/dataimport/addresslibrarymatchingpage">
				<input type="hidden" id="isshow" name="isshow" value="1" />
				<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
					<tr id="customertr" class=VwCtr style="display:">
						<td width="100%" colspan="2">
						订单号：<textarea cols="20" rows="20" name="cwbs" id="cwbs"></textarea> 
						需匹配站点：<textarea cols="20" rows="20" name="branches" id="branches"></textarea> 
						<input type="submit" class="" value="匹配更新" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="saomiao_right2" style="overflow: auto;" height="20%" >
			<p id="msg" name="msg" ></p>
					
		</div>



		
	</div>
</body>
</html>
