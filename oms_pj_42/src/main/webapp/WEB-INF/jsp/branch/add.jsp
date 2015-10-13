<%@page import="cn.explink.domain.AccountArea"%>
<%@ page import="cn.explink.domain.Branch,cn.explink.domain.Function"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<AccountArea> accountAreaList = (List<AccountArea>)request.getAttribute("accontareaList");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css"
	type="text/css" media="all" />
<style type="text/css">
li {
	float: left;
	padding: 0px 5px;
	list-style: none;
	width: 200px;
	border: 1px;
	border-color: blue;
	border-style: dashed;
}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript">
function isFloat(name){
	return /^[0-9\.]+$/.test(name);
}
function testEmail(str){
	  return  /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
}
$(function() {
	$("#branchname").blur(function(event){
		if($("#branchname").val().length>0){
		$.ajax({
			url:"<%=request.getContextPath()%>/branch/branchnamecheck",
				data : {
					branchname : $("#branchname").val()},success : 

					function(data) {
						if (data == false) {
							alert("机构名称已存在");
						} else {
							alert("机构名称可用");
						}
				}
			});
		}
	});
});


$(function(){
	$("#branchtypeflag").change(function(){//监控机构类型变化 对显示字段做相应处理
		var branchtypeflag =  $("#branchtypeflag").val();
		init();
		//库房 中转站 退货站 其他
		if(branchtypeflag==<%=BranchEnum.ZhanDian.getValue()%>){//站点
			zhandianObj();
		}else if(branchtypeflag==<%=BranchEnum.YunYing.getValue()%>){//运营
			yunyingObj();
		}else if(branchtypeflag==<%=BranchEnum.KeFu.getValue()%>){//客服
			kefuObj();
		}else if(branchtypeflag==<%=BranchEnum.CaiWu.getValue()%>){//财务
			caiwuObj();
		}else{
			qitaObj();
		}
	});
	
	$("#remandtype").change(function(){//对库房 中转站 退货站 和气态类型的机构中分拣线提示功能进行监控，确定是否需要上传声音文件
		if($("#remandtype").val()==<%=BranchEnum.YuYinTiXing.getValue() %>){
			$("#wav").parent().show();
		}else{
			$("#wav").parent().hide();
		}
	});
});

function init(){
	$("#branchname").parent().hide();
	$("#branchphone").parent().hide();
	$("#accountarea").parent().hide();
	$("#branchprovince").parent().hide();
	$("#branchaddress").parent().hide();
	$("#branchmobile").parent().hide();
	$("#branchemail").parent().hide();
	$("#remandtype").parent().hide();
	$("#branchmatter").parent().hide();
	$(".zhandian").parent().hide();
	$(".kefu").parent().hide();
	$(".yunying").parent().hide();
	$("#insert").parent().hide();
}
function gonggongObj(){
	$("#branchname").parent().show();
	$("#branchphone").parent().show();
	$("#insert").parent().show();
}
function zhandianObj(){
	gonggongObj();
	$("#accountarea").parent().show();
	$("#branchaddress").parent().show();
	$("#branchmobile").parent().show();
	$("#branchemail").parent().show();
	$(".zhandian").parent().show();
}
function yunyingObj(){
	gonggongObj();
	$("#branchmatter").parent().show();
	$(".yunying").parent().show();
}
function kefuObj(){
	gonggongObj();
	$("#branchmatter").parent().show();
	$(".kefu").parent().show();
}
function caiwuObj(){
	gonggongObj();
	$("#branchmatter").parent().show();
}
function qitaObj(){
	gonggongObj();
	$("#branchprovince").parent().show();
	$("#branchaddress").parent().show();
	$("#remandtype").parent().show();
}
function checkGongGong(){
	if($("#branchname").val().length==0){
		alert("机构名称不能为空");
		return false;
	}if($("#branchname").val().length>50){
		alert("机构名称超长");
		return false;
	}if($("#branchphone").val().length>50){
		alert("联系电话超长");
		return false;
	}
	return true;
}

function checkQiTa(){
	if(!checkGongGong()){return false;}
	else if($("#branchprovince").val().length>30){
		alert("省份名称过长");
		return false;
	}else if($("#branchaddress").val().length>100){
		alert("地址的名称过长");
		return false;
	}
	if($("#remandtype").val()==<%=BranchEnum.YuYinTiXing.getValue()%>&&!checkWav()){return false;}
	return true;
}

function checkZhanDian(){
	if(!checkGongGong()){return false;}
	if($("#branchaddress").val().length>100){
		alert("地址的名称过长");
		return false;
	}if($("#branchmobile").val().length>100){
		alert("联系人电话过长");
		return false;
	}if($("#branchemail").val().length>100){
		alert("Email超长");
		return;
	}if(!testEmail($("#branchemail").val())){
		alert("Email格式不正确");
		return;
	}
	return true;
}

function checkKeTuiCai(){
	if(!checkGongGong()){return false;}
	if($("#branchemail").val().length>100){
		alert("Email超长");
		return;
	}if(!testEmail($("#branchemail").val())){
		alert("Email格式不正确");
		return;
	}
	return true;
}

function checkWav(){
	if($("#wav").val().length>4 && $("#wav").val().substring($("#wav").val().length-4)!=".wav" && $("#wav").val().substring($("#wav").val().length-4)!=".WAV"){
		alert("声音文件只能选择wav格式，如xxx.wav");
		return false;
	}
	if($("#wav").val().length==0){
		alert("请选择上传文件");
		return false;
	}
	return true;
}

$(function(){
	$("#form1").submit(function(){
		var branchtypeflag =  $("#branchtypeflag").val();
		if(branchtypeflag==-1){
			return false;
		}		
		if(branchtypeflag==<%=BranchEnum.ZhanDian.getValue()%>){//站点
			return checkZhanDian();
		}else if(branchtypeflag==<%=BranchEnum.YunYing.getValue()%>){//运营
			return checkKeTuiCai();
		}else if(branchtypeflag==<%=BranchEnum.KeFu.getValue()%>){//客服
			return checkKeTuiCai();
		}else if(branchtypeflag==<%=BranchEnum.CaiWu.getValue()%>){//财务
			return checkKeTuiCai();
		}else{//库房 中转站 退货站 其他
			return checkQiTa();
		}
		return true;		
	});
});

</script>
</HEAD>
<BODY>
${errorState}
${addOk}
	<form id="form1" name="form1" action="create" method="post" enctype="multipart/form-data">
	   请选择站点类型：<select id ="branchtypeflag" name ="branchtypeflag">
                   <option value ="-1">==请选择==</option>
                   <option value ="<%=BranchEnum.KuFang.getValue()%>"><%=BranchEnum.KuFang.getText() %></option>
                   <option value ="<%=BranchEnum.ZhanDian.getValue() %>"><%=BranchEnum.ZhanDian.getText() %></option>
                   <option value ="<%=BranchEnum.TuiHuo.getValue() %>"><%=BranchEnum.TuiHuo.getText() %></option>
                   <option value ="<%=BranchEnum.ZhongZhuan.getValue() %>"><%=BranchEnum.ZhongZhuan.getText() %></option>
                   <option value ="<%=BranchEnum.YunYing.getValue() %>"><%=BranchEnum.YunYing.getText() %></option>
                   <option value ="<%=BranchEnum.KeFu.getValue() %>"><%=BranchEnum.KeFu.getText() %></option>
                   <option value ="<%=BranchEnum.CaiWu.getValue() %>"><%=BranchEnum.CaiWu.getText() %></option>
                   <option value ="<%=BranchEnum.QiTa.getValue() %>"><%=BranchEnum.QiTa.getText() %></option>
                </select><br>
                
	    <div>
	              机构名称:<input type="text" name="branchname" id="branchname"/><br/>
        </div>
        <div>
	              固定电话:<input type="text" name="branchphone" id="branchphone" /><br/>
	    </div>
	    <div>
	               地址:<input type="text" name="branchaddress" id="branchaddress"/>
	    </div>
	    <div>
		  所属区域：
		  <select name="accountarea" id="accountarea">
		    <option value ="0">请选择</option>
		    <%for(AccountArea accountarea:accountAreaList){ %>
		     <option value ="<%=accountarea.getAreaid()%>"><%=accountarea.getAreaname() %></option>
		    <%} %>
		  </select><br/>
		 </div>
		 <div>
		  站点手机：<input type="text" name="branchmobile" id="branchmobile" /><br/>
		 </div>
		 <div>
		   邮箱：<input type="text" name="branchemail" id="branchemail" /><br/>
		 </div>
		 <div>
		  预付款后缴款设置<input type="hidden" name="" class ="zhandian" /><br/>
		 </div>
		 <div>
                       账户设置<input type="hidden" name="" class ="zhandian" /><br/>
         </div>
         <div>
                       额度设置<input type="hidden" name="" class ="zhandian" /><br/>
         </div>
         <div>
                       货物流向设置<input type="hidden" name="" class ="zhandian" /><br/>
		 </div>
		 <div>
		  所在地省:<input type="text" name="branchprovince" id="branchprovince"><br/>
		 </div>
		 <div>
	   	  拣线提示方式:<select id ="remandtype" name ="remandtype">
             <option value ="0">==请选择==</option> 
             <option value ="<%=BranchEnum.BuQiYong.getValue()%>"><%=BranchEnum.BuQiYong.getText() %></option>
             <option value ="<%=BranchEnum.TiaoMaDaYin.getValue()%>"><%=BranchEnum.TiaoMaDaYin.getText() %></option>
             <option value ="<%=BranchEnum.YuYinTiXing.getValue()%>"><%=BranchEnum.YuYinTiXing.getText() %></option>
           </select><br/>
         <div style="display: none">
                                 上传声音文件：<input id="wav" name="wav" type="file" size="40" maxlength="600" ><br/>
         </div>     
		 </div>
		 <div>
		  邮件：<input type="text" name="branchmatter" id="branchmatter"><br/>
		 </div>
		 <div>
                         导出信息设置<input type="hidden" name="" class ="kefu" /><br/>
         </div>
         <div>
                         查询统计内容设置<input type="hidden" name="" class ="yunying" /><br/>
         </div>
         <div>
	     <input type="submit" value="保存" id="insert"><br/>
	     <a href="list/1">返回</a>
	    </div>
	</form>
<script>init();</script>
</BODY>
</HTML>
