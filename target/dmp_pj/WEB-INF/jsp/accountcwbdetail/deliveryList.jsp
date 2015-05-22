<%@page language="java" import="java.util.*,java.math.BigDecimal" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.AccountCwbDetail,cn.explink.domain.AccountFeeDetail"%>
<%@page import="cn.explink.enumutil.AccountTongjiEnum,cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%
	List<Branch> branchList=(List<Branch>)request.getAttribute("branchList")==null?null:(List<Branch>)request.getAttribute("branchList");

	Branch selectBranch=request.getAttribute("selectBranch")==null?null:(Branch)request.getAttribute("selectBranch");
	List<AccountCwbDetail> qkList=request.getAttribute("qkList")==null?null:(List<AccountCwbDetail>)request.getAttribute("qkList");
	List<AccountCwbDetail> yjList=request.getAttribute("yjList")==null?null:(List<AccountCwbDetail>)request.getAttribute("yjList");
	List<AccountFeeDetail> jiajianList=request.getAttribute("jiajianList")==null?null:(List<AccountFeeDetail>)request.getAttribute("jiajianList");
	List<AccountCwbDetail> yjejList=request.getAttribute("yjejList")==null?null:(List<AccountCwbDetail>)request.getAttribute("yjejList");
	String selectDS=request.getParameter("selectDS")==null?"0":request.getParameter("selectDS");//代收货款下拉框
	Map typeMap=request.getAttribute("typeMap")==null?null:(Map)request.getAttribute("typeMap");
	String jiakuanIds="";//加款ids
	String jiankuanIds="";//减款ids
	String jiaKuan="0";
	String jianKuan="0";
	String yingJiaoCash="0";//已妥投
	String yingJiaoPos="0";
	String yingJiaoCheck="0";
	String yingJiaoOther="0";
	String yingJiao="0";
	String yingJiaoNums="";
	String qianKuan="0";
	String qiankuanIds="";//欠款ids;
	String yingJiaoEjCash="0";//二级已妥投
	String yingJiaoEjPos="0";
	String yingJiaoEjCheck="0";
	String yingJiaoEjOther="0";
	String yingJiaoEjNums="";
	
	String jiaKuanNums="0";
	String jianKuanNums="0";
	String qianKuanNums="0";
	if(typeMap != null && !typeMap.isEmpty()){
		jiaKuan=typeMap.get(AccountTongjiEnum.JiaKuan.getValue()).toString();
		jianKuan=typeMap.get(AccountTongjiEnum.JianKuan.getValue()).toString();
		jiaKuanNums=typeMap.get(AccountTongjiEnum.JiaKuanNums.getValue()).toString();
		jianKuanNums=typeMap.get(AccountTongjiEnum.JianKuanNums.getValue()).toString();
		jiakuanIds=typeMap.get(AccountTongjiEnum.JiaKuanCwbs.getValue()).toString();
		jiankuanIds=typeMap.get(AccountTongjiEnum.JianKuanCwbs.getValue()).toString();
		qianKuan=typeMap.get(AccountTongjiEnum.QianKuan.getValue()).toString();
		yingJiaoCash=typeMap.get(AccountTongjiEnum.YingJiaoCash.getValue()).toString();
		yingJiaoPos=typeMap.get(AccountTongjiEnum.YingJiaoPos.getValue()).toString();
		yingJiaoCheck=typeMap.get(AccountTongjiEnum.YingJiaoCheck.getValue()).toString();
		yingJiaoOther=typeMap.get(AccountTongjiEnum.YingJiaoOther.getValue()).toString(); 
		yingJiaoNums=typeMap.get(AccountTongjiEnum.YingJiaoNums.getValue()).toString();
		yingJiaoEjCash=typeMap.get(AccountTongjiEnum.YingJiaoEjCash.getValue()).toString();
		yingJiaoEjPos=typeMap.get(AccountTongjiEnum.YingJiaoEjPos.getValue()).toString();
		yingJiaoEjCheck=typeMap.get(AccountTongjiEnum.YingJiaoEjCheck.getValue()).toString();
		yingJiaoEjOther=typeMap.get(AccountTongjiEnum.YingJiaoEjOther.getValue()).toString(); 
		yingJiaoEjNums=typeMap.get(AccountTongjiEnum.YingJiaoEjNums.getValue()).toString();
		
		qianKuanNums=typeMap.get(AccountTongjiEnum.QianKuanNums.getValue()).toString();
	}
	BigDecimal yingJiaoHJ=BigDecimal.ZERO;//已妥投加款合计
	yingJiaoHJ=yingJiaoHJ.add(new BigDecimal(yingJiaoCash));
	yingJiaoHJ=yingJiaoHJ.add(new BigDecimal(yingJiaoPos));
	yingJiaoHJ=yingJiaoHJ.add(new BigDecimal(yingJiaoCheck));
	yingJiaoHJ=yingJiaoHJ.add(new BigDecimal(yingJiaoOther));
	
	BigDecimal yingJiaoEjHJ=BigDecimal.ZERO;//二级已妥投加款合计
	yingJiaoEjHJ=yingJiaoEjHJ.add(new BigDecimal(yingJiaoEjCash));
	yingJiaoEjHJ=yingJiaoEjHJ.add(new BigDecimal(yingJiaoEjPos));
	yingJiaoEjHJ=yingJiaoEjHJ.add(new BigDecimal(yingJiaoEjCheck));
	yingJiaoEjHJ=yingJiaoEjHJ.add(new BigDecimal(yingJiaoEjOther));
	BigDecimal dsk=BigDecimal.ZERO;//已妥投加款合计
	

	long yingJiaoHJNums=Long.parseLong("".equals(yingJiaoNums)?"0":yingJiaoNums)+Long.parseLong("".equals(yingJiaoEjNums)?"0":yingJiaoEjNums);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配送结果结算信息</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript">
var branchid="<%=request.getAttribute("branchid") %>";
var jiaKuan="<%=jiaKuan%>";
var jianKuan="<%=jianKuan%>";
var yingJiaoCash="<%=yingJiaoCash%>";
var yingJiaoPos="<%=yingJiaoPos%>";
var yingJiaoCheck="<%=yingJiaoCheck%>";
var yingJiaoOther="<%=yingJiaoOther%>";
var yingJiao="<%=yingJiaoHJ%>"; 
var qianKuan="<%=qianKuan%>";
var chaoe="<%=selectBranch.getAccountexcessfee()==null?"":selectBranch.getAccountexcessfee()%>"; //超额
var chaoetype="<%=selectBranch.getAccountexcesstype()%>";//超额类型
var hj=0;
var wj="<%=yingJiaoHJ.add(yingJiaoEjHJ)%>";//未勾选的款项

var yingJiaoEjCash="<%=yingJiaoEjCash%>";
var yingJiaoEjPos="<%=yingJiaoEjPos%>";
var yingJiaoEjCheck="<%=yingJiaoEjCheck%>";
var yingJiaoEjOther="<%=yingJiaoEjOther%>";
var yingJiaoEj="<%=yingJiaoEjHJ%>";

var zfhjFee=0;
var sxhjFee=0;

$(function(){
	<%-- if("<%=selectBranch.getAccountbranch()%>"==0){//未选择站点时隐藏主列表
		alert("请设置站点结算对象！");
		$("#viewMain").hide();
		return;
	} --%>
<%-- 	if("<%=selectBranch.getAccounttype()%>"!=2){
		alert("当前站点不是配送结果结算！");
		$("#viewMain").hide();
		return;
	} --%>

	if($("#branchid").val()==0){//未选择站点时隐藏主列表
		$("#viewMain").hide();
	}
	
	$("#branchid").change(function(){ 
		location.href="<%=request.getContextPath()%>/accountcwbdetail/deliveryInfo?branchid="+this.value;
	});
	
	//判断已妥投货款checkPos>0时 禁止勾选 默认为选中
	$('input[type="checkbox"][name="checkbox2"]').each(
       	function() {
          	var checkPos=parseFloat($(this).attr("checkPos")).toFixed(2)*100;
          	if(checkPos>0){
          		$(this).attr("checked","checked");
          		$(this).attr("disabled","disabled");
          	}
        });
	
	//判断二级已妥投货款checkPos>0时 禁止勾选 默认为选中
	$('input[type="checkbox"][name="checkbox3"]').each(
       	function() {
          	var checkPos=parseFloat($(this).attr("checkPos")).toFixed(2)*100;
          	if(checkPos>0){
          		$(this).attr("checked","checked");
          		$(this).attr("disabled","disabled");
          	}
          }
       );
	
	yingJiaoTotal(yingJiao,yingJiaoCash,yingJiaoPos,yingJiaoCheck,yingJiaoOther,yingJiaoEj,yingJiaoEjCash,yingJiaoEjPos,yingJiaoEjCheck,yingJiaoEjOther);
	totalHJ(yingJiao,qianKuan,jianKuan,jiaKuan,yingJiaoEj,yingJiaoPos,yingJiaoEjPos);
	
	//保存按钮
	$("#saveF").click(function(){
		$("#yjOnStr").val("");
		$("#yjOffStr").val("");
		var str2On="";//已妥投交款列表选中id
		var str2Off="";//已妥投交款列表未选中id
				
		
		$('input[type="checkbox"][name="checkbox2"]').each(
		       	function() {
		          	if($(this).attr("checked")=="checked"){
		          		str2On+=$(this).val()+",";
		          	}else{
		          		str2Off+=$(this).val()+",";
		          	}
		          }
		       );

		$('input[type="checkbox"][name="checkbox3"]').each(
		       	function() {
		          	if($(this).attr("checked")=="checked"){
		          		str2On+=$(this).val()+",";
		          	}else{
		          		str2Off+=$(this).val()+",";
		          	}
		          }
		       );
	    
	    	/* $("#yjOnStr").val(str2On.substring(0,(str2On.length)-1)); */
	    	$("#yjOnStr").val(str2On.substring(0,str2On.lastIndexOf(",")));
    		/* $("#yjOffStr").val(str2Off.substring(0,(str2Off.length)-1)); */
    		$("#yjOffStr").val(str2Off.substring(0,str2Off.lastIndexOf(",")));
    	
      	if(chaoetype==1){
      		//if(chaoe*100>0){
		    	if((wj*100)-(yingJiao*100+yingJiaoEj*100)>(chaoe*100)){
		    		alert("您已超额,请重新选择！");
		    		return false;
		    	}
      		//}
		}
		if(chaoetype==2){
			//if(chaoe*100>0){
				if((wj*100)-((yingJiao*100+yingJiaoEj*100))>(hj*(chaoe/100)*100)){
					alert("您已超额,请重新选择！");
		    		return false;
				}
			//}
		}  
		
		if("<%=yingJiaoHJNums%>">0||"<%=qianKuanNums%>">0){	
			$("#alert_box").show();
			centerBox();
		}else{
			alert("暂无交款信息");
			return false;
		}
	});
	
	//欠款展开收起
	$("#clickQK").toggle( 
		function () { 
			$("#viewQK").show();
		}, 
		function () { 
			$("#viewQK").hide();
		} 
	);
	//已妥投交款展开收起
	$("#clickYJ").toggle( 
		function () { 
			$("#viewYJ").show();
		}, 
		function () { 
			$("#viewYJ").hide();
		} 
	);
	//二级已妥投交款展开收起
	$("#clickYJEJ").toggle( 
		function () { 
			$("#viewYJEJ").show();
		}, 
		function () { 
			$("#viewYJEJ").hide();
		} 
	);
});

//计算总合计
function totalHJ(yJ,qK,jianK,jiaK,hjEj,pos,posEj){
	hj=((parseFloat(yJ).toFixed(2)*100+parseFloat(hjEj).toFixed(2)*100+parseFloat(qK).toFixed(2)*100
			+parseFloat(jianK).toFixed(2)*100-parseFloat(jiaK).toFixed(2)*100-parseFloat(pos).toFixed(2)*100-parseFloat(posEj).toFixed(2)*100)/100).toFixed(2);
	$("#hjShow").html(hj);
	$("#hjFee").val(hj);
	$("#hjOpen").html(hj);
	$("#sxhjShow").html(hj);
	
}

function yingJiaoTotal(hj,cash,pos,check,other,hjEj,cashEj,posEj,checkEj,otherEj){
	$("#yingJiao").html(hj+"&nbsp;&nbsp;&nbsp;&nbsp;(现金："+cash+"，POS："+pos+"，支票："+check+"，其他："+other);	
	$("#yingJiaoEj").html(hjEj+"&nbsp;&nbsp;&nbsp;&nbsp;(现金："+cashEj+"，POS："+posEj+"，支票："+checkEj+"，其他："+otherEj);	
	totalHJ(hj,qianKuan,jianKuan,jiaKuan,hjEj,pos,posEj);
}

//计算勾选未勾选已妥投货款合计、现金、POS、支票、其他
function changeYj(cash,pos,check,other,obj){
	if(document.getElementById("btn2_"+obj).checked==true){//选中 相加
		yingJiao=((parseFloat(yingJiao).toFixed(2)*100+parseFloat(cash).toFixed(2)*100+
				parseFloat(pos).toFixed(2)*100+parseFloat(check).toFixed(2)*100+parseFloat(other).toFixed(2)*100)/100).toFixed(2);
		yingJiaoCash=((parseFloat(yingJiaoCash).toFixed(2)*100+parseFloat(cash).toFixed(2)*100)/100).toFixed(2);
		yingJiaoPos=((parseFloat(yingJiaoPos).toFixed(2)*100+parseFloat(pos).toFixed(2)*100)/100).toFixed(2);
		yingJiaoCheck=((parseFloat(yingJiaoCheck).toFixed(2)*100+parseFloat(check).toFixed(2)*100)/100).toFixed(2);
		yingJiaoOther=((parseFloat(yingJiaoOther).toFixed(2)*100+parseFloat(other).toFixed(2)*100)/100).toFixed(2);
	}else{//相减
		yingJiao=((parseFloat(yingJiao).toFixed(2)*100-parseFloat(cash).toFixed(2)*100-
				parseFloat(pos).toFixed(2)*100-parseFloat(check).toFixed(2)*100-parseFloat(other).toFixed(2)*100)/100).toFixed(2);
		yingJiaoCash=((parseFloat(yingJiaoCash).toFixed(2)*100-parseFloat(cash).toFixed(2)*100)/100).toFixed(2);
		yingJiaoPos=((parseFloat(yingJiaoPos).toFixed(2)*100-parseFloat(pos).toFixed(2)*100)/100).toFixed(2);
		yingJiaoCheck=((parseFloat(yingJiaoCheck).toFixed(2)*100-parseFloat(check).toFixed(2)*100)/100).toFixed(2);
		yingJiaoOther=((parseFloat(yingJiaoOther).toFixed(2)*100-parseFloat(other).toFixed(2)*100)/100).toFixed(2);
	}
	yingJiaoTotal(yingJiao,yingJiaoCash,yingJiaoPos,yingJiaoCheck,yingJiaoOther,yingJiaoEj,yingJiaoEjCash,yingJiaoEjPos,yingJiaoEjCheck,yingJiaoEjOther);
}

//二级计算勾选未勾选已妥投货款合计、现金、POS、支票、其他
function changeYjEj(cash,pos,check,other,obj){
	if(document.getElementById("btn3_"+obj).checked==true){//选中 相加
		yingJiaoEj=((parseFloat(yingJiaoEj).toFixed(2)*100+parseFloat(cash).toFixed(2)*100+
				parseFloat(pos).toFixed(2)*100+parseFloat(check).toFixed(2)*100+parseFloat(other).toFixed(2)*100)/100).toFixed(2);
		yingJiaoEjCash=((parseFloat(yingJiaoEjCash).toFixed(2)*100+parseFloat(cash).toFixed(2)*100)/100).toFixed(2);
		yingJiaoEjPos=((parseFloat(yingJiaoEjPos).toFixed(2)*100+parseFloat(pos).toFixed(2)*100)/100).toFixed(2);
		yingJiaoEjCheck=((parseFloat(yingJiaoEjCheck).toFixed(2)*100+parseFloat(check).toFixed(2)*100)/100).toFixed(2);
		yingJiaoEjOther=((parseFloat(yingJiaoEjOther).toFixed(2)*100+parseFloat(other).toFixed(2)*100)/100).toFixed(2);
	}else{//相减
		yingJiaoEj=((parseFloat(yingJiaoEj).toFixed(2)*100-parseFloat(cash).toFixed(2)*100-
				parseFloat(pos).toFixed(2)*100-parseFloat(check).toFixed(2)*100-parseFloat(other).toFixed(2)*100)/100).toFixed(2);
		yingJiaoEjCash=((parseFloat(yingJiaoEjCash).toFixed(2)*100-parseFloat(cash).toFixed(2)*100)/100).toFixed(2);
		yingJiaoEjPos=((parseFloat(yingJiaoEjPos).toFixed(2)*100-parseFloat(pos).toFixed(2)*100)/100).toFixed(2);
		yingJiaoEjCheck=((parseFloat(yingJiaoEjCheck).toFixed(2)*100-parseFloat(check).toFixed(2)*100)/100).toFixed(2);
		yingJiaoEjOther=((parseFloat(yingJiaoEjOther).toFixed(2)*100-parseFloat(other).toFixed(2)*100)/100).toFixed(2);
	}
	yingJiaoTotal(yingJiao,yingJiaoCash,yingJiaoPos,yingJiaoCheck,yingJiaoOther,yingJiaoEj,yingJiaoEjCash,yingJiaoEjPos,yingJiaoEjCheck,yingJiaoEjOther);
}

//已妥投货款全选按钮
function btn2click(){
	$("[name='checkbox2']").attr("checked",'true');//全选  
	$("#yingJiao").html("<%=yingJiao%>");
	yingJiao="<%=yingJiaoHJ%>";
	yingJiaoCash="<%=yingJiaoCash%>";
	yingJiaoPos="<%=yingJiaoPos%>";
	yingJiaoCheck="<%=yingJiaoCheck%>";
	yingJiaoOther="<%=yingJiaoOther%>";
	yingJiaoTotal(yingJiao,yingJiaoCash,yingJiaoPos,yingJiaoCheck,yingJiaoOther,yingJiaoEj,yingJiaoEjCash,yingJiaoEjPos,yingJiaoEjCheck,yingJiaoEjOther);
}
//二级已妥投交款全选按钮
function btn3click(){
	$("[name='checkbox3']").attr("checked",'true');//全选  
	$("#yingJiao").html("<%=yingJiao%>");
	yingJiaoEj="<%=yingJiaoEjHJ%>";
	yingJiaoEjCash="<%=yingJiaoEjCash%>";
	yingJiaoEjPos="<%=yingJiaoEjPos%>";
	yingJiaoEjCheck="<%=yingJiaoEjCheck%>";
	yingJiaoEjOther="<%=yingJiaoEjOther%>";
	yingJiaoTotal(yingJiao,yingJiaoCash,yingJiaoPos,yingJiaoCheck,yingJiaoOther,yingJiaoEj,yingJiaoEjCash,yingJiaoEjPos,yingJiaoEjCheck,yingJiaoEjOther);
}

//光标离开金额框时统计合计
function hjFeeCheck(){
	var feetransfer=$("#feetransfer").val()==""?0:$("#feetransfer").val();
	var feecash=$("#feecash").val()==""?0:$("#feecash").val();
	var feepos=$("#feepos").val()==""?0:$("#feepos").val();
	var feecheck=$("#feecheck").val()==""?0:$("#feecheck").val();
	
	
 	zfhjFee=((parseFloat(feetransfer).toFixed(2)*100+parseFloat(feecash).toFixed(2)*100
			+parseFloat(feepos).toFixed(2)*100+parseFloat(feecheck).toFixed(2)*100)/100).toFixed(2); 
	
	sxhjFee=((parseFloat(hj).toFixed(2)*100-parseFloat(zfhjFee).toFixed(2)*100)/100).toFixed(2);
	$("#sxhjShow").html(sxhjFee);
}

//已妥投货款下拉框
function changeDS(){
	$("#table_2 tr").each(function(){
		var checkDSK=parseFloat($(this).attr("checkDSK")).toFixed(2)*100;
		if($("#selectDS").val()==1){//代收款>0
			if(checkDSK==0){//代收款为0时 隐藏
				$(this).attr("style","display:none");
			}
		}else{
			$(this).attr("style","display:block");
		}
	});
}

//加减款详情
function detailJiaJian(feetype,ids,nums){
	if(nums!=0){
		$("#feetype").val(feetype);
		$("#feedetailid").val(ids);
		$("#detailJiaJianForm").submit();
	}
}


function checkFClick(){
	if($("#feetransfer").val()!=""){
		if(!isFloat($("#feetransfer").val())){
			alert("转账金额应为数字");
			return false;
		}
	}
	if($("#feecash").val()!=""){
		if(!isFloat($("#feecash").val())){
			alert("现金金额应为数字");
			return false;
		}
	}
	if($("#feepos").val()!=""){
		if(!isFloat($("#feepos").val())){
			alert("POS金额应为数字");
			return false;
		}
	}
	if($("#feecheck").val()!=""){
		if(!isFloat($("#feecheck").val())){
			alert("支票金额应为数字");
			return false;
		}
	}
	
	if((zfhjFee*100)>(hj*100)){
		alert("超额支付！");
		return false;
	}
	
	if((zfhjFee*100)<(hj*100)){
		alert("您有未交款,请检查交款金额！");
		return false;
	}
	
	if(confirm("确定交款吗？")){
 		$("#checkF").attr("disabled","disabled");
    	$("#checkF").val("请稍候");
    	$.ajax({
    		type: "POST",
    		url:'<%=request.getContextPath()%>/accountcwbdetail/createdelivery/<%=selectBranch.getBranchid()%>',
    		data:$('#createForm').serialize(),
    		dataType : "json",
    		success : function(data) {
    			if(data.errorCode==0){
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/accountcwbdetail/listDelivery/1?checkoutstate=0";
    			}else{
    				alert(data.error);
    			}
    		}
    	});
	}
}



//=======导出===========
function exportField(){
	if("<%=yingJiaoHJNums%>">0||"<%=qianKuanNums%>">0){	
	 	$("#ids").val("");
	 	var str2On="";//已妥投交款列表选中id
	 	$('input[type="checkbox"][name="checkbox2"]').each(
		       	function() {
		          	if($(this).attr("checked")=="checked"){
		          		str2On+=$(this).val()+",";
		          	}
		          }
		       );
		$('input[type="checkbox"][name="checkbox3"]').each(
		       	function() {
		          	if($(this).attr("checked")=="checked"){
		          		str2On+=$(this).val()+",";
		          	}
		          }
		       );
		
		if(str2On.length>0){
			if($("#qkStr").val()!=""){//欠款
				str2On+=$("#qkStr").val()+",";
			}
			$("#ids").val(str2On.substring(0,str2On.lastIndexOf(",")));
		 	$("#clickExport").attr("disabled","disabled");
			$("#clickExport").val("请稍后");
		 	$("#exportForm").submit(); 
		}
	}else{
		alert("暂无数据！");
		return false;
	}
}
</script>
</head>
<body style="background:#fff" marginwidth="0" marginheight="0">
<form id="createForm" action="<%=request.getContextPath()%>/accountcwbdetail/createdelivery/<%=selectBranch.getBranchid()%>" method="post">
<!-- 弹出框开始 -->
<div id="alert_box" style="display:none">
  <div id="box_bg" ></div>
  <div id="box_contant" >
    <div id="box_top_bg"></div>
    <div id="box_in_bg">
      <h1><div id="close_box" onclick="closeBox()"></div>
        	交款信息</h1>
        <div class="right_title" style="padding:10px">
         <h1>您需支付金额：<font id="hjOpen" style="font-family:'微软雅黑', '黑体'; font-size:25px"></font>
			元，&nbsp;&nbsp;还有<font id="sxhjShow" style="font-family:'微软雅黑', '黑体'; font-size:25px"></font>元未支付。</lable></h1>
          <p>&nbsp;</p>
          <table width="800" border="0" cellspacing="1" cellpadding="2" class="table_2" >
            <tr>
				<td bgcolor="#F4F4F4">付款方式</td>
				<td bgcolor="#F4F4F4">金额</td>
				<td bgcolor="#F4F4F4">付款人</td>
				<td bgcolor="#F4F4F4">卡号</td>
			</tr>
			<tr>
				<td>转账</td>
				<td><input onblur="hjFeeCheck()" name="feetransfer" type="text" id="feetransfer" size="10" maxlength="10" value="" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
				<td><input name="usertransfer" type="text" id="usertransfer" size="10" maxlength="20" value=""/></td>
				<td><input name="cardtransfer" type="text" id="cardtransfer" size="25" maxlength="20" value=""/></td>
			</tr>
			<tr>
				<td>现金</td>
				<td><input onblur="hjFeeCheck()" name="feecash" type="text" id="feecash" size="10" maxlength="10" value="" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
				<td><input name="usercash" type="text" id="usercash" size="10" maxlength="20" value=""/></td>
				<td></td>
			</tr>
			<tr>
				<td>POS</td>
				<td><input onblur="hjFeeCheck()" name="feepos" type="text" id="feepos" size="10" maxlength="10" value="" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
				<td><input name="userpos" type="text" id="userpos" size="10" maxlength="20" value=""/></td>
				<td></td>
			</tr>
			<tr>
				<td>支票</td>
				<td><input onblur="hjFeeCheck()" name="feecheck" type="text" id="feecheck" size="10" maxlength="10" value="" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
				<td><input name="usercheck" type="text" id="usercheck" size="10" maxlength="20" value=""/></td>
				<td></td>
			</tr>
            <tr>
              <td colspan="4" bgcolor="#F4F4F4">
              	<div class="jg_10"></div>
                  <input type="button" class="input_button1" id="checkF" onclick="checkFClick()" value="确 认" />
				  <!-- &nbsp;&nbsp;
				  <input type="button" class="input_button1" id="checkF" onclick="checkFClick()" value="审 核" /> -->
                  <div class="jg_10"></div>
                </td>
            </tr>
          </table>
        </div>
    </div>
  </div>
  <div id="box_yy"></div>
</div>
<!-- 弹出框结束 -->

<div class="inputselect_box" style="top: 0px ">
	<span style="font-family:'微软雅黑', '黑体'; font-size:16px">超额金额(未交已妥投货款&lt=超额金额)：<%=selectBranch.getAccountexcessfee()==null?"":selectBranch.getAccountexcessfee()%>
	<%
	if(selectBranch.getAccountexcesstype()==1){
		out.print("元");
	}else if(selectBranch.getAccountexcesstype()==2){
		out.print("%");
	}else{
		out.print("");
	}
	%>
	</span> &nbsp;
	站点：
	<%if(branchList!=null&&!branchList.isEmpty()){ %>
	<select id="branchid" name="branchid" style="width:150px;">	
		<option value="0">==请选择==</option>
		<%for(Branch b: branchList){%>
			<option value="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
		<%}%>	
   </select>
   <%}else{
		out.print(selectBranch.getBranchname()==null?"":selectBranch.getBranchname());
	}%>
	
	付费方式：<%if(selectBranch.getAccounttype()==1){
				out.print("买单结算");
			}else if(selectBranch.getAccounttype()==2){
				out.print("配送结果结算");
			}else{
				out.print("");
			}%>
</div>
<div id="viewMain">
<div style="height:35px"></div>
	
		<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
			<tr class="font_1">
				<td colspan="6" bgcolor="#F4F4F4">冲减款</td>
			</tr>
			<tr class="font_1">
				<td colspan="3" width="50%" align="center" bgcolor="#F4F4F4">对站点返款【元】</td>
				<td colspan="3" width="50%" bgcolor="#F4F4F4">对站点收款【元】</td>
			</tr>
			<tr>
				<td width="20%" align="center" bgcolor="#F4F4F4">款项名称</td>
				<td width="15%" align="center" bgcolor="#F4F4F4">单数</td>
				<td width="15%" align="center" bgcolor="#F4F4F4">金额</td>
				<td width="20%" align="center" bgcolor="#F4F4F4">款项名称</td>
				<td width="15%" align="center" bgcolor="#F4F4F4">单数</td>
				<td width="15%" align="center" bgcolor="#F4F4F4">金额</td>
			</tr>
			<tr>
		    	<td align="center">加款</td>
		    	<td align="center"><a href="#" onclick="detailJiaJian(1,'<%=jiakuanIds%>','<%=jiaKuanNums%>')"><%=jiaKuanNums%></a></td>
		    	<td align="right"><strong><%=jiaKuan%></strong></td>
		    	<td align="center">减款</td>
		    	<td align="center"><a href="#" onclick="detailJiaJian(2,'<%=jiankuanIds%>','<%=jianKuanNums%>')"><%=jianKuanNums%></a></td>
		    	<td align="right"><strong><%=jianKuan%></strong></td>
	      	</tr>
	     </table>
	<div class="shenzhan_box1">
		<div class="zhankaibox_title"><span><a id="clickQK"><%if(qkList!=null&&!qkList.isEmpty()){out.print("展开/收起");}%>
		</a></span> <strong>欠款[元]：</strong><strong id="qianKuan"><%=qianKuan%></strong></div>
		<div id="viewQK" style="display:none">
			<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
				<tr>
					<td width="40"></td>
					<td>订单号</td>
					<td>订单类型</td>
					<td>配送成功时间</td>
					<td>发货件数</td>
					<td>出库件数</td>
					<td>货物价值[元]</td>
					<td>现金[元]</td>
					<td>POS[元]</td>
					<td>支票[元]</td>
					<td>其他[元]</td>
				</tr>
				<%if(qkList!=null&&!qkList.isEmpty()){
					for(int i=0;i<qkList.size();i++){
						AccountCwbDetail list=qkList.get(i);
						if(i==qkList.size()-1){
							qiankuanIds+=list.getAccountcwbid();
						}else{
							qiankuanIds+=list.getAccountcwbid()+",";
						}
				%> 
				<tr>
					<td>
						</td>
					<td><%=list.getCwb()%></td>
					<td><%for(CwbOrderTypeIdEnum ft:CwbOrderTypeIdEnum.values()){if(list.getCwbordertypeid()==ft.getValue()){ %><%=ft.getText() %><%}}%></td>
					<td><%=list.getDeliverytime()%></td>
					<td><%=list.getSendcarnum()%></td>
					<td><%=list.getScannum()%></td>
					<td align="right"><%=list.getCaramount()%></td>
					<td align="right"><%=list.getCash()%></td>
					<td align="right"><%=list.getPos()%></td>
					<td align="right"><%=list.getCheckfee()%></td>
					<td align="right"><%=list.getOtherfee()%></td>
				</tr>
				<%}}%>
			</table>
		</div>
	</div>
	<div class="shenzhan_box1">
		<div class="zhankaibox_title"><span>本次交款只显示最近3000条数据&nbsp;&nbsp;&nbsp;&nbsp;
		<a id="clickYJ"><%if(yjList!=null&&!yjList.isEmpty()){out.print("展开/收起");}%>
		</a></span> <strong>已妥投交款[元]：</strong><strong id="yingJiao"></strong>
			<select name="selectDS" id="selectDS" onchange="changeDS();">
				<option value="0">全部</option>
				<option value="1">代收款>0</option>
			</select></div>
		<div id="viewYJ"  style="display:none">
			<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="table_2">
				<tr>
					<td width="40"><a href="#" onclick="btn2click();">全选</a></td>
					<td>订单号</td>
					<td>订单类型</td>
					<td>配送成功时间</td>
					<td>发货件数</td>
					<td>出库件数</td>
					<td>货物价值[元]</td>
					<td>现金[元]</td>
					<td>POS[元]</td>
					<td>支票[元]</td>
					<td>其他[元]</td>
				</tr>
				<%if(yjList!=null&&!yjList.isEmpty()){
					for(int i=0;i<yjList.size();i++){
						AccountCwbDetail list=yjList.get(i);
				%>
				<tr name="trDSK" checkDSK="<%=dsk.add(list.getCheckfee().add(list.getCash().add(list.getPos().add(list.getOtherfee())))) %>">
					<td><input type="checkbox" checkPos="<%=list.getPos()%>"  checked="true" name="checkbox2" id="btn2_<%=i+1%>" value="<%=list.getAccountcwbid()%>"
						onClick="changeYj('<%=list.getCash()%>','<%=list.getPos()%>','<%=list.getCheckfee()%>','<%=list.getOtherfee()%>','<%=i+1 %>')" />
					</td>
					<td><%=list.getCwb()%></td>
					<td><%for(CwbOrderTypeIdEnum ft:CwbOrderTypeIdEnum.values()){if(list.getCwbordertypeid()==ft.getValue()){ %><%=ft.getText() %><%}}%></td>
					<td><%=list.getDeliverytime()%></td>
					<td><%=list.getSendcarnum()%></td>
					<td><%=list.getScannum()%></td>
					<td align="right"><%=list.getCaramount()%></td>
					<td align="right"><%=list.getCash()%></td>
					<td align="right"><%=list.getPos()%></td>
					<td align="right"><%=list.getCheckfee()%></td>
					<td align="right"><%=list.getOtherfee()%></td>
				</tr>
				<%}}%>
			</table>
		</div>
	</div>
	

	<%if(yjejList!=null&&!yjejList.isEmpty()){ %>
	<div class="shenzhan_box1">
		<div class="zhankaibox_title"><span>本次交款只显示最近3000条数据&nbsp;&nbsp;&nbsp;&nbsp;<a id="clickYJEJ">展开/收起</a>
		</span> <strong>二级站点已妥投交款[元]：</strong><strong id="yingJiaoEj"></strong></div>
		<div id="viewYJEJ"  style="display:none">
			<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
				<tr>
					<td width="40"><a href="#" onclick="btn3click();">全选</a></td>
					<td>订单号</td>
					<td>订单类型</td>
					<td>配送成功时间</td>
					<td>发货件数</td>
					<td>出库件数</td>
					<td>货物价值[元]</td>
					<td>现金[元]</td>
					<td>POS[元]</td>
					<td>支票[元]</td>
					<td>其他[元]</td>
				</tr>
				<%for(int i=0;i<yjejList.size();i++){
						AccountCwbDetail list=yjejList.get(i);
				%>
				<tr>
					<td><input type="checkbox" checked="true" checkPos="<%=list.getPos()%>" name="checkbox3" id="btn3_<%=i+1%>" value="<%=list.getAccountcwbid()%>"
						onClick="changeYjEj('<%=list.getCash()%>','<%=list.getPos()%>','<%=list.getCheckfee()%>','<%=list.getOtherfee()%>','<%=i+1 %>')" />
					</td>
					<td><%=list.getCwb()%></td>
					<td><%for(CwbOrderTypeIdEnum ft:CwbOrderTypeIdEnum.values()){if(list.getCwbordertypeid()==ft.getValue()){ %><%=ft.getText() %><%}}%></td>
					<td><%=list.getDeliverytime()%></td>
					<td><%=list.getSendcarnum()%></td>
					<td><%=list.getScannum()%></td>
					<td align="right"><%=list.getCaramount()%></td>
					<td align="right"><%=list.getCash()%></td>
					<td align="right"><%=list.getPos()%></td>
					<td align="right"><%=list.getCheckfee()%></td>
					<td align="right"><%=list.getOtherfee()%></td>
				</tr>
				<%}%>
			</table>
		</div>
	</div>
	<%}%>
	
	<!--底部 -->
	<div style="height:70px"></div>
	<div class="iframe_bottom2" >
		<table width="100%" border="0" cellspacing="1" cellpadding="1" class="table_5">
			<tr>
				<td align="center" bgcolor="#f4f4f4" style="font-family:'微软雅黑', '黑体'; font-size:16px">合计(已妥投货款+欠款+对站点收款-对站点返款)：<span id="hjShow" style="color:#F00"></span></td>
			</tr>
			<tr>
			  <td align="center" bgcolor="#f4f4f4">
			  	<input type="hidden" id="jiakuanStr" name="jiakuanStr" value="<%=jiakuanIds%>"/>
				<input type="hidden" id="jiankuanStr" name="jiankuanStr" value="<%=jiankuanIds%>"/>
				<input type="hidden" id="otheraddfee" name="otheraddfee" value="<%=jiaKuan%>"/>
				<input type="hidden" id="othersubtractfee" name="othersubtractfee" value="<%=jianKuan%>"/>
				<input type="hidden" id="hjFee" name="hjFee" value=""/>
				<input type="hidden" id="yjOnStr" name="yjOnStr" value=""/>
				<input type="hidden" id="yjOffStr" name="yjOffStr" value=""/>
				
				<input type="hidden" id="qkStr" name="qkStr" value="<%=qiankuanIds%>"/>
				<input type="hidden" id="qknums" name="qknums" value="<%=qianKuanNums%>"/>
				<input type="hidden" id="qkfee" name="qkfee" value="<%=qianKuan%>"/>
				<input type="hidden" id="otheraddnums" name="otheraddnums" value="<%=jiaKuanNums%>"/>
				<input type="hidden" id="othersubnums" name="othersubnums" value="<%=jianKuanNums%>"/>
				<input type="hidden" id="acounttype"  name="acounttype" value="<%=selectBranch.getAccounttype()%>"/>
				<input type="hidden" id="tonums" name="tonums" value="<%=yingJiaoHJNums%>"/>
				<input type="hidden" id="tocash" name="tocash" value="<%=new BigDecimal(yingJiaoCash).add(new BigDecimal((yingJiaoEjCash)))%>"/>
				<input type="hidden" id="topos" name="topos" value="<%=new BigDecimal(yingJiaoPos).add(new BigDecimal((yingJiaoEjPos)))%>"/>
				<input type="hidden" id="tocheck" name="tocheck" value="<%=new BigDecimal(yingJiaoCheck).add(new BigDecimal((yingJiaoEjCheck)))%>"/>
				<input type="hidden" id="toother" name="toother" value="<%=new BigDecimal(yingJiaoOther).add(new BigDecimal((yingJiaoEjOther)))%>"/>
				<input type="hidden" id="checkoutstate" name="checkoutstate" value="0"/>
				<input type="hidden" id="accounttype" name="accounttype" value="<%=selectBranch.getAccounttype()%>"/>
				<input type="button" class="input_button1" id="saveF"  value="交 款" />
				<input type="button" class="input_button1" id="clickExport"  onclick="exportField();"  value="导 出" />
			  </td>
		  </tr>
		</table>
	</div>
	<!--底部 -->
</form>


<form action="<%=request.getContextPath() %>/accountcwbdetail/exportOutwarehouseDetail" method="post" id="exportForm">
	<input  type="hidden"  name="ids" value="" id="ids" />
</form>
<form action="<%=request.getContextPath() %>/accountfeedetail/detailList" target="_blank" method="post" id="detailJiaJianForm">
	<input type="hidden" id="feetype" name="feetype" value=""/>
	<input type="hidden" id="feedetailid" name="feedetailid" value=""/>
</form>
</div>
<script type="text/javascript">
$("#selectDS").val("<%=selectDS%>");
//站点下拉框赋值
$("#branchid").val("<%=selectBranch.getBranchid()%>");
</script>
</body>
</html>