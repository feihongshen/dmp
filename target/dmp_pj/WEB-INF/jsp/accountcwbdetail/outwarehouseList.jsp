<%@page language="java" import="java.util.*,java.math.BigDecimal" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.AccountCwbDetail,cn.explink.domain.AccountFeeDetail"%>
<%@page import="cn.explink.enumutil.AccountTongjiEnum,cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%
	List<Branch> branchList=(List<Branch>)request.getAttribute("branchList");
	Branch selectBranch=(Branch)request.getAttribute("selectBranch");
	List<AccountCwbDetail> qkList=request.getAttribute("qkList")==null?null:(List<AccountCwbDetail>)request.getAttribute("qkList");
	List<AccountCwbDetail> yjList=request.getAttribute("yjList")==null?null:(List<AccountCwbDetail>)request.getAttribute("yjList");
	List<AccountFeeDetail> jiajianList=request.getAttribute("jiajianList")==null?null:(List<AccountFeeDetail>)request.getAttribute("jiajianList");
	List<AccountCwbDetail> yjejList=request.getAttribute("yjejList")==null?null:(List<AccountCwbDetail>)request.getAttribute("yjejList");
	
	Map typeMap=request.getAttribute("typeMap")==null?null:(Map)request.getAttribute("typeMap");
	String zhongZhuanFees="0";
	String tuiHuoFees="0";
	String yingJiao="0";
	String qianKuan="0";
	String jiaKuan="0";
	String jianKuan="0";
	String jiakuanIds="";//加款ids
	String jiankuanIds="";//减款ids
	String zhongZhuanIds="";//中转退款ids
	String tuiHuoIds="";//退货退款ids
	String qiankuanIds="";//欠款ids;
	String zhongZhuanNums="0";//中转退款订单数
	String tuiHuoNums="0"; //退货退款订单数
	String yingJiaoNums="0";//应交货款订单数
	String jiaKuanNums="0";
	String jianKuanNums="0";
	String qianKuanNums="0";
	String yingJiaoEjCash="0";//二级
	String yingJiaoEjNums="";
	
	String posIds="";//pos退款ids
	String posNums="0"; //pos退款订单数
	String posFees="0";
	if(typeMap != null && !typeMap.isEmpty()){
		zhongZhuanFees=typeMap.get(AccountTongjiEnum.ZhongZhuanFees.getValue()).toString();
		tuiHuoFees=typeMap.get(AccountTongjiEnum.TuiHuoFees.getValue()).toString();
		yingJiao=typeMap.get(AccountTongjiEnum.YingJiao.getValue()).toString();
		qianKuan=typeMap.get(AccountTongjiEnum.QianKuan.getValue()).toString();
		zhongZhuanIds=typeMap.get(AccountTongjiEnum.ZhongZhuanCwbs.getValue()).toString();
		tuiHuoIds=typeMap.get(AccountTongjiEnum.TuiHuoCwbs.getValue()).toString();
		jiaKuan=typeMap.get(AccountTongjiEnum.JiaKuan.getValue()).toString();
		jianKuan=typeMap.get(AccountTongjiEnum.JianKuan.getValue()).toString();
		zhongZhuanNums=typeMap.get(AccountTongjiEnum.ZhongZhuanNums.getValue()).toString();
		tuiHuoNums=typeMap.get(AccountTongjiEnum.TuiHuoNums.getValue()).toString();
		yingJiaoNums=typeMap.get(AccountTongjiEnum.YingJiaoNums.getValue()).toString();
		jiaKuanNums=typeMap.get(AccountTongjiEnum.JiaKuanNums.getValue()).toString();
		jianKuanNums=typeMap.get(AccountTongjiEnum.JianKuanNums.getValue()).toString();
		qiankuanIds=typeMap.get(AccountTongjiEnum.QianKuanCwbs.getValue()).toString();
		qianKuanNums=typeMap.get(AccountTongjiEnum.QianKuanNums.getValue()).toString();
		jiakuanIds=typeMap.get(AccountTongjiEnum.JiaKuanCwbs.getValue()).toString();
		jiankuanIds=typeMap.get(AccountTongjiEnum.JianKuanCwbs.getValue()).toString();
		yingJiaoEjCash=typeMap.get(AccountTongjiEnum.YingJiaoEjCash.getValue()).toString();
		yingJiaoEjNums=typeMap.get(AccountTongjiEnum.YingJiaoEjNums.getValue()).toString();
		
		posIds=typeMap.get(AccountTongjiEnum.PosCwbs.getValue()).toString();
		posNums=typeMap.get(AccountTongjiEnum.PosNums.getValue()).toString();
		posFees=typeMap.get(AccountTongjiEnum.PosFees.getValue()).toString();
	}
	BigDecimal yingJiaoHJ=BigDecimal.ZERO;//一级二级合计应交
	yingJiaoHJ=yingJiaoHJ.add(new BigDecimal(yingJiao));
	yingJiaoHJ=yingJiaoHJ.add(new BigDecimal(yingJiaoEjCash));
	
	long yingJiaoHJNums=Long.parseLong(yingJiaoNums)+Long.parseLong("".equals(yingJiaoEjNums)?"0":yingJiaoEjNums);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>买单结算信息</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript">
var zhongZhuanFees="<%=zhongZhuanFees%>"; //中转货款
var tuiHuoFees="<%=tuiHuoFees%>";//退货退款
var posFees="<%=posFees%>";//pos退款
var jiaKuan="<%=jiaKuan%>";
var jianKuan="<%=jianKuan%>";
var yingJiao="<%=yingJiao%>"; //应交
var qianKuan="<%=qianKuan%>"; //欠款
var branchid="<%=request.getAttribute("branchid") %>";
var chaoe="<%=selectBranch.getAccountexcessfee()==null?"":selectBranch.getAccountexcessfee()%>"; //超额
var chaoetype="<%=selectBranch.getAccountexcesstype()%>";//超额类型
var hj=0;
var wj="<%=yingJiaoHJ%>";//未勾选的款项
var yingJiaoEjCash="<%=yingJiaoEjCash%>";

$(function(){
	if($("#branchid").val()==0){//未选择站点时隐藏主列表
		$("#viewMain").hide();
	}
	
	$("#branchid").change(function(){ 
		location.href="<%=request.getContextPath()%>/accountcwbdetail/outwarehouseInfo/"+this.value;
	});
	
	if(branchid>0){
		totalHJ(yingJiao,qianKuan,jianKuan,jiaKuan,zhongZhuanFees,tuiHuoFees,yingJiaoEjCash,posFees);
	}
	
	//保存按钮
	$("#saveF").click(function(){
		$("#yjOnStr").val("");
		$("#yjOffStr").val("");
		var str2On="";//应交货款列表选中id
		var str2Off="";//应交货款列表未选中id
		
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
	    
    	$("#yjOnStr").val(str2On.substring(0,str2On.lastIndexOf(",")));
   		$("#yjOffStr").val(str2Off.substring(0,str2Off.lastIndexOf(",")));
    	
      	if(chaoetype==1){
      		//if(chaoe*100>0){
		    	if((wj*100)-((yingJiao*100+yingJiaoEjCash*100))>(chaoe*100)){
		    		alert("您的欠款超额！");
		    		return false;
		    	}
      		//}
		}
		if(chaoetype==2){
			//if(chaoe*100>0){
				if((wj*100)-((yingJiao*100+yingJiaoEjCash*100))>(hj*(chaoe/100)*100)){
					alert("您的欠款超额！");
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
		
		
		if(confirm("确定提交交款数据吗？")){
			$("#saveF").attr("disabled","disabled");
	    	$("#saveF").val("请稍候");
	    	$.ajax({
	    		type: "POST",
	    		url:'<%=request.getContextPath()%>/accountcwbdetail/createOutwarehouse/<%=selectBranch.getBranchid()%>',
	    		data:$('#createForm').serialize(),
	    		dataType : "json",
	    		success : function(data) {
	    			if(data.errorCode==0){
	    				alert(data.error);
	    				location.href="<%=request.getContextPath()%>/accountcwbdetail/getOutwarehouse/"+data.summaryid;
	    			}else{
	    				alert(data.error);
	    				location.href="<%=request.getContextPath()%>/accountcwbdetail/outwarehouseInfo/<%=selectBranch.getBranchid()%>";
	    			}
	    		}
	    	});
	    	
	    	
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
	//应交展开收起
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
function totalHJ(yJ,qK,jianK,jiaK,zZ,tH,yJEJ,pos){
	hj=((parseFloat(yJ).toFixed(2)*100+parseFloat(qK).toFixed(2)*100+parseFloat(yJEJ).toFixed(2)*100
			+parseFloat(jianK).toFixed(2)*100-parseFloat(zZ).toFixed(2)*100-parseFloat(tH).toFixed(2)*100-parseFloat(pos).toFixed(2)*100
			-parseFloat(jiaK).toFixed(2)*100)/100).toFixed(2);
	$("#hjShow").html(hj);
	$("#hjFee").val(hj);
	$("#yingJiaoEj").html(yJEJ);	
}

//计算勾选未勾选应交款
function changeYj(fee,obj){
	if(document.getElementById("btn2_"+obj).checked==true){//选中 相加
		yingJiao=((parseFloat(yingJiao).toFixed(2)*100+parseFloat(fee).toFixed(2)*100)/100).toFixed(2);
	}else{//相减
		yingJiao=((parseFloat(yingJiao).toFixed(2)*100-parseFloat(fee).toFixed(2)*100)/100).toFixed(2);
	}
	$("#yingJiao").html(yingJiao);
	totalHJ(yingJiao,qianKuan,jianKuan,jiaKuan,zhongZhuanFees,tuiHuoFees,yingJiaoEjCash,posFees);
}

//二级计算勾选未勾选已妥投货款合计、现金、POS、支票、其他
function changeYjEj(cash,obj){
	if(document.getElementById("btn3_"+obj).checked==true){//选中 相加
		yingJiaoEjCash=((parseFloat(yingJiaoEjCash).toFixed(2)*100+parseFloat(cash).toFixed(2)*100)/100).toFixed(2);
	}else{//相减
		yingJiaoEjCash=((parseFloat(yingJiaoEjCash).toFixed(2)*100-parseFloat(cash).toFixed(2)*100)/100).toFixed(2);
	}
	totalHJ(yingJiao,qianKuan,jianKuan,jiaKuan,zhongZhuanFees,tuiHuoFees,yingJiaoEjCash,posFees);
}
/* function exportField(){
	if(hj==0){
		alert("没有数据,不能导出！");
		return false;
	}
	$("#clickExport").attr("disabled","disabled");
	$("#clickExport").val("请稍后");
 	$("#exportForm").submit();
} */

//全选按钮
function btn2click(){
	$("[name='checkbox2']").attr("checked",'true');//全选  
	$("#yingJiao").html("<%=yingJiao%>");
	yingJiao="<%=yingJiao%>";
	totalHJ(yingJiao,qianKuan,jianKuan,jiaKuan,zhongZhuanFees,tuiHuoFees,yingJiaoEjCash,posFees);
}

//二级全选按钮
function btn3click(){
	$("[name='checkbox3']").attr("checked",'true');//全选  
	$("#yingJiaoEj").html("<%=yingJiaoEjCash%>");
	yingJiaoEjCash="<%=yingJiaoEjCash%>";
	totalHJ(yingJiao,qianKuan,jianKuan,jiaKuan,zhongZhuanFees,tuiHuoFees,yingJiaoEjCash,posFees);
}

//中转退货详情
function detailCwbs(ids,nums){
	if(nums!=0){
		$("#ids").val(ids);
		$("#detailCwbsForm").submit();
	}
}

//加减款详情
function detailJiaJian(feetype,ids,nums){
	if(nums!=0){
		$("#feetype").val(feetype);
		$("#feedetailid").val(ids);
		$("#detailJiaJianForm").submit();
	}
}
</script>
</head>
<body style="background:#fff" marginwidth="0" marginheight="0">
<div class="inputselect_box" style="top: 0px ">
	<span style="font-family:'微软雅黑', '黑体'; font-size:18px">超额金额(未交货款&lt=超额金额)：<%=selectBranch.getAccountexcessfee()==null?"":selectBranch.getAccountexcessfee()%>
	<%
	if(selectBranch.getAccountexcesstype()==1){
		out.print("元");
	}else if(selectBranch.getAccountexcesstype()==2){
		out.print("%");
	}else{
		out.print("");
	}
	%>
	</span> &nbsp;站点：
	<select id="branchid" name="branchid" style="width:150px;">	
		<option value="0">==请选择==</option>
		<%if(!branchList.isEmpty()){
			for(Branch b: branchList){%>
			<option value="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
		<%}}%>	
   </select>
	付费方式：<%if(selectBranch.getAccounttype()==1){
				out.print("买单结算");
			}else if(selectBranch.getAccounttype()==2){
					out.print("配送结果结算");
				}else{
					out.print("");
				}%>
	<!-- <input type="button" class="input_button1" id="clickExport"  onclick="exportField();" value="导出Excel" /> -->
</div>


<div id="viewMain">
<div style="height:35px"></div>
	<form id="createForm" action="" method="post">
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
	    	<td align="center">中转退款</td>
	    	<td align="center"><a href="#" onclick="detailCwbs('<%=zhongZhuanIds%>','<%=zhongZhuanNums%>')"><%=zhongZhuanNums%></a></td>
	    	<td align="right"><strong><%=zhongZhuanFees%></strong></td>
	    	<td align="center">减款</td>
	    	<td align="center"><a href="#" onclick="detailJiaJian(2,'<%=jiankuanIds%>','<%=jianKuanNums%>')"><%=jianKuanNums%></a></td>
	    	<td align="right"><strong><%=jianKuan%></strong></td>
      	</tr>
      	<tr>
	    	<td align="center">退货退款</td>
	    	<td align="center"><a href="#" onclick="detailCwbs('<%=tuiHuoIds%>','<%=tuiHuoNums%>')"><%=tuiHuoNums%></a></td>
	    	<td align="right"><strong><%=tuiHuoFees%></strong></td>
	    	<td align="center"></td>
	    	<td align="center"></td>
	    	<td align="right"></td>
      	</tr>
      	<tr>
	    	<td align="center">POS退款</td>
	    	<td align="center"><a href="#" onclick="detailCwbs('<%=posIds%>','<%=posNums%>')"><%=posNums%></a></td>
	    	<td align="right"><strong><%=posFees%></strong></td>
	    	<td align="center"></td>
	    	<td align="center"></td>
	    	<td align="right"></td>
      	</tr>
      	<tr>
	    	<td align="center">加款</td>
	    	<td align="center"><a href="#" onclick="detailJiaJian(1,'<%=jiakuanIds%>','<%=jiaKuanNums%>')"><%=jiaKuanNums%></a></td>
	    	<td align="right"><strong><%=jiaKuan%></strong></td>
	    	<td align="center"></td>
	    	<td align="center"></td>
	    	<td align="right"></td>
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
					<td>发货件数</td>
					<td>出库件数</td>
					<td>货物价值[元]</td>
					<td>代收货款[元]</td>
					<td>应退款[元]</td>
				</tr>
				<%if(qkList!=null&&!qkList.isEmpty()){
					for(int i=0;i<qkList.size();i++){
						AccountCwbDetail list=qkList.get(i);
				%>
				<tr>
					<td>
						</td>
					<td><%=list.getCwb()%></td>
					<td><%if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()){
							out.print("配送");
						}else if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue()){
							out.print("上门换");
						}else if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
							out.print("上门退");
						}else{
							out.print("未确定");
						}
						%></td>
					<td><%=list.getSendcarnum()%></td>
					<td><%=list.getScannum()%></td>
					<td align="right"><%=list.getCaramount()%></td>
					<td align="right"><%=list.getReceivablefee()%></td>
					<td align="right"><%=list.getPaybackfee()%></td>
				</tr>
				<%}}%>
			</table>
		</div>
	</div>
	<div class="shenzhan_box1">
		<div class="zhankaibox_title"><span>本次交款只显示最近3000条数据&nbsp;&nbsp;&nbsp;&nbsp;
		<a id="clickYJ"><%if(yjList!=null&&!yjList.isEmpty()){out.print("展开/收起");}%>
		</a></span><strong>应交货款[元]：</strong><strong id="yingJiao"><%=yingJiao%></strong></div>
		<div id="viewYJ"  style="display:none">
			<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
				<tr>
					<td width="40"><a href="#" onclick="btn2click();">全选</a></td>
					<td>订单号</td>
					<td>订单类型</td>
					<td>发货件数</td>
					<td>出库件数</td>
					<td>货物价值[元]</td>
					<td>代收货款[元]</td>
					<td>应退款[元]</td>
				</tr>
				<%if(yjList!=null&&!yjList.isEmpty()){
					for(int i=0;i<yjList.size();i++){
						AccountCwbDetail list=yjList.get(i);
				%>
				<tr>
					<td><input type="checkbox" checked="true" onClick="changeYj('<%=list.getReceivablefee()%>','<%=i+1 %>')" name="checkbox2" id="btn2_<%=i+1%>" value="<%=list.getAccountcwbid()%>"/>
						<label for="checkbox3"></label></td>
					<td><%=list.getCwb()%></td>
					<td><%if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()){
							out.print("配送");
						}else if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue()){
							out.print("上门换");
						}else if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
							out.print("上门退");
						}else{
							out.print("未确定");
						}
						%></td>
					<td><%=list.getSendcarnum()%></td>
					<td><%=list.getScannum()%></td>
					<td  align="right"><%=list.getCaramount()%></td>
					<td  align="right"><%=list.getReceivablefee()%></td>
					<td  align="right"><%=list.getPaybackfee()%></td>
				</tr>
				<%}}%>
			</table>
		</div>
	</div>
	
	<%if(yjejList!=null&&!yjejList.isEmpty()){%>
	<div class="shenzhan_box1">
		<div class="zhankaibox_title"><span>本次交款只显示最近3000条数据&nbsp;&nbsp;&nbsp;&nbsp;<a id="clickYJEJ">展开/收起</a>
		</span> <strong>二级站点应交货款[元]：</strong><strong id="yingJiaoEj"><%=yingJiaoEjCash%></strong></div>
		<div id="viewYJEJ"  style="display:none">
			<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
				<tr>
					<td width="40"><a href="#" onclick="btn3click();">全选</a></td>
					<td>订单号</td>
					<td>订单类型</td>
					<td>发货件数</td>
					<td>出库件数</td>
					<td>货物价值[元]</td>
					<td>代收货款[元]</td>
					<td>应退款[元]</td>
				</tr>
				<%for(int i=0;i<yjejList.size();i++){
						AccountCwbDetail list=yjejList.get(i);
				%>
				<tr>
					<td><input type="checkbox" checked="true" checkPos="<%=list.getPos()%>" name="checkbox3" id="btn3_<%=i+1%>" value="<%=list.getAccountcwbid()%>"
						onClick="changeYjEj('<%=list.getReceivablefee()%>','<%=i+1 %>')" />
					</td>
					<td><%=list.getCwb()%></td>
					<td><%if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()){
							out.print("配送");
						}else if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue()){
							out.print("上门换");
						}else if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
							out.print("上门退");
						}else{
							out.print("未确定");
						}
						%></td>
					<td><%=list.getSendcarnum()%></td>
					<td><%=list.getScannum()%></td>
					<td align="right"><%=list.getCaramount()%></td>
					<td align="right"><%=list.getReceivablefee()%></td>
					<td align="right"><%=list.getPaybackfee()%></td>
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
				<td align="center" bgcolor="#f4f4f4" style="font-family:'微软雅黑', '黑体'; font-size:18px"><font>合计(应交货款+欠款+对站点收款-对站点返款)：</font>
				<span id="hjShow" style="color:#F00"></span>元</td>
			</tr>
			<tr>
			  <td align="center" bgcolor="#f4f4f4">
			  	<input type="hidden" id="acounttype"  name="acounttype" value="<%=selectBranch.getAccounttype()%>"/>
			  	<input type="hidden" id="jiakuanStr" name="jiakuanStr" value="<%=jiakuanIds%>"/>
				<input type="hidden" id="jiankuanStr" name="jiankuanStr" value="<%=jiankuanIds%>"/>
				<input type="hidden" id="zzStr" name="zzStr" value="<%=zhongZhuanIds%>"/>
				<input type="hidden" id="thStr" name="thStr" value="<%=tuiHuoIds%>"/>
				<input type="hidden" id="qkStr" name="qkStr" value="<%=qiankuanIds%>"/>
				<input type="hidden" id="yjOnStr" name="yjOnStr" value=""/>
				<input type="hidden" id="yjOffStr" name="yjOffStr" value=""/>
				<input type="hidden" id="otheraddfee" name="otheraddfee" value="<%=jiaKuan%>"/>
				<input type="hidden" id="othersubtractfee" name="othersubtractfee" value="<%=jianKuan%>"/>
				<input type="hidden" id="zzcash" name="zzcash" value="<%=zhongZhuanFees%>"/>
				<input type="hidden" id="thcash" name="thcash" value="<%=tuiHuoFees%>"/>
				<input type="hidden" id="zznums" name="zznums" value="<%=zhongZhuanNums%>"/>
				<input type="hidden" id="thnums" name="thnums" value="<%=tuiHuoNums%>"/>
				<input type="hidden" id="tocash" name="tocash" value="<%=yingJiaoHJ%>"/>
				<input type="hidden" id="tonums" name="tonums" value="<%=yingJiaoHJNums%>"/>
				<input type="hidden" id="accounttype" name="accounttype" value="<%=selectBranch.getAccounttype()%>"/>
				<input type="hidden" id="hjFee" name="hjFee" value=""/>
				<input type="hidden" id="otheraddnums" name="otheraddnums" value="<%=jiaKuanNums%>"/>
				<input type="hidden" id="othersubnums" name="othersubnums" value="<%=jianKuanNums%>"/>
				<input type="hidden" id="qknums" name="qknums" value="<%=qianKuanNums%>"/>
				<input type="hidden" id="qkfee" name="qkfee" value="<%=qianKuan%>"/>
				<input type="hidden" id="posStr" name="posStr" value="<%=posIds%>"/>
				<input type="hidden" id="posnums" name="posnums" value="<%=posNums%>"/>
				<input type="hidden" id="poscash" name="poscash" value="<%=posFees%>"/>
				<input type="button" class="input_button1" id="saveF"  value="提 交" />
			  </td>
		  </tr>
		</table>
	</div>
	<!--底部 -->
</form>
<form action="<%=request.getContextPath() %>/accountcwbdetail/outwarehouseDetail/1" target="_blank" method="post" id="detailCwbsForm">
	<input type="hidden" id="ids" name="ids" value=""/>
</form>

<form action="<%=request.getContextPath() %>/accountfeedetail/detailList" target="_blank" method="post" id="detailJiaJianForm">
	<input type="hidden" id="feetype" name="feetype" value=""/>
	<input type="hidden" id="feedetailid" name="feedetailid" value=""/>
</form>
</div>

<script type="text/javascript">
//站点下拉框赋值
$("#branchid").val(branchid);
$("#exportBranchid").val(branchid);
</script>
</body>
</html>