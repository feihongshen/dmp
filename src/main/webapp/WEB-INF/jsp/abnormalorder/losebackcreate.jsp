<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.AbnormalType"%>
<%@page import="cn.explink.domain.AbnormalOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchList=(List<Branch>)request.getAttribute("branchList");
List<Customer> customerlist=(List<Customer>)request.getAttribute("customerList");
List<CwbOrder> cwbList=(List<CwbOrder>)request.getAttribute("cwbList");
String losebackdescribe=request.getAttribute("losebackdescribe")==null?"":request.getAttribute("losebackdescribe").toString();
long losebackbranchid=request.getAttribute("losebackbranchid")==null?0:Long.parseLong(request.getAttribute("losebackbranchid").toString());
String message=request.getAttribute("message")==null?"":request.getAttribute("message").toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ajaxfileupload.js"></script>
<script language="javascript">
$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	if(<%=!message.equals("")%>){
		alert("<%=message%>");
	}
	
});

function createabnormalData(){
	var flag=true;
	if($("#cwb").val()==""||$("#cwb").val()=="查询多个订单用回车隔开"){
		alert("订单号不能为空！！");
		flag=false;
	}
	console.info($("#abnormalinfo").val().length);
	if($("#abnormalinfo").val().length>100){
		alert("丢失件说明过长，请适当填写！！");
		flag=false;
	}
	return flag;
	

}
function ajaxFileUpload()  
{  
  /*   //设置加载图标的显示  
    $('#loading').show();  
    uploadProcessTimer = window.setInterval(getFileUploadProcess, 20);  
 */
 	var dataInit = {};
 	dataInit['cwb'] = $('#cwb').val();
 	dataInit['callbackbranchid'] = $("#callbackbranchid").val();
 	dataInit['abnormalinfo'] = $("#abnormalinfo").val();
    $.ajaxFileUpload  
    ({  
        url:$("#searchForm").attr("action"),  
        async: false, 
        secureuri:false,  
        fileElementId:['file'],  
        dataType: 'json',  
        data:dataInit,  
        success: function (data)
        {  
            if(data.errorCode==0){
            	alert(data.error);
            	
            }else{
            	alert(data.error);
              $("#abnormalinfo").val("最多输入100个字");
          	$("#callbackbranchid").val(0);
          	$("#cwb").val("查询多个订单用回车隔开");
            }
            
        },  
        error: function (data, status, e)  
        {  
            alert("上传发生异常,创建丢失件失败");  
        }  
    });  

    return false;  
}  


function sub(){
	var datavalue = "[";
	var noMsg = 0;
	
	<%
	if(cwbList!=null&&cwbList.size()>0)
		{
		for(CwbOrder cwb : cwbList){
%>	
		if($("#losebackbranchid<%=cwb.getOpscwbid()%>").val()==0){
		
			noMsg +=1;
		}else{
			var opscwbid="<%=cwb.getOpscwbid()%>";
			var describe=$("#describe<%=cwb.getOpscwbid()%>").val();
			var losebackbranchid=$("#losebackbranchid<%=cwb.getOpscwbid()%>").val();
			var cwb="<%=cwb.getCwb()%>";
			var filename=$("#file<%=cwb.getOpscwbid()%>").val();
			datavalue=  datavalue +  "\""+opscwbid+"_s_"+describe+"_s_"+losebackbranchid+"_s_"+cwb+"_s_"+filename+" \",";
		}
		
	<%}}%>
	if(noMsg>0){
		alert("您还有"+noMsg+"单没选择找回机构，请先选择！");
		return false;
	}
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
	}
	datavalue= datavalue + "]";
/* 	alert(datavalue);
 */	$("#cwbdetails").val(datavalue);
	$("#searchform2").submit();
}
function subChexiao(){
	$("#searchForm3").submit();
}
</script>
</head>
<body style="background:#f5f5f5;overflow: hidden;" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="tabbox">
				<div style="position:relative; z-index:0; " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<div class="menucontant">
							<form id="searchForm"  name="searchForm" action="<%=request.getContextPath()%>/abnormalOrder/toCreatMissPieceToCheck" method="post"  >
							<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1" align="left">
							<tr>
							<td>
							
								订    单    号*：
								<textarea id="cwb" class="kfsh_text" onblur="if(this.value==''){this.value='查询多个订单用回车隔开'}" onfocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" rows="3" name="cwb">查询多个订单用回车隔开</textarea>
								</td>
								<td>
								找回机构:
									<select name="callbackbranchid" id="callbackbranchid" class="select1">
										<option value="0">请选择责任机构</option>
										<%if(branchList!=null){for(Branch b: branchList){ %>
											<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
											<%}} %>
									
								</td>
								<td></td>
								</tr>
								<tr>
								<td >
								找 回说 明：<textarea id="abnormalinfo" name="abnormalinfo" onblur="if(this.value==''){this.value='最多输入100个字'}" onfocus="if(this.value=='最多输入100个字'){this.value=''}">最多输入100个字</textarea><br>
								</td>
								<!-- <td align="left">
								上传附件:<input type="file" name="file" id="file"/>
								</td> -->
								<td >
								<input type="submit" value="提交" id="createabnormal" name="createabnormal"  class="input_button2"> <input type="reset" value="重置" class="input_button2">
								</td>
								
								</tr>
								
								
								
								</table>
						<!-- 		<font id="showMessage" color="red" style="border-left: thin;"></font>
								<font id="showMessage1" color="blue" style="border-left: thin;"></font> -->
								</div>	
							</form>
						</div>
						
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="tijiaoshow">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="150" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">配送站点</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">当前状态</td>
									<td width="200" align="center" valign="middle" bgcolor="#E7F4E3">找回机构</td>
									<td width="200" align="center" valign="middle" bgcolor="#E7F4E3">找回说明</td>
									<td width="200" align="center" valign="middle" bgcolor="#E7F4E3">附件上传</td>
								</tr>
							</tbody>
						</table>
					</div>
					<br>
					<br>
					<br>
					<br>
					<div style="height:100px"></div>
					<div style="height:400px;overflow-y:scroll">
					<form id="searchform2"  action="<%=request.getContextPath()%>/abnormalOrder/submitCreateLoseback" method="post" enctype="multipart/form-data">
					<%if(cwbList!=null&&cwbList.size()>0)for(CwbOrder cwb : cwbList){ 
							%>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
						<tbody>
							<tr height="30" >
								<td width="200" align="center" valign="middle"><%=cwb.getCwb() %></td>
								<td width="120" align="center" valign="middle"><%for(Customer c : customerlist){if(cwb.getCustomerid()==c.getCustomerid()){ %><%=c.getCustomername() %><%}} %></td>
								<td width="120" align="center" valign="middle"><%=cwb.getEmaildate() %></td>
								<td width="100" align="center" valign="middle"><%for(Branch b : branchList){if(cwb.getDeliverybranchid()==b.getBranchid()){ %><%=b.getBranchname() %><%}} %></td>
								<td width="100" align="center" valign="middle"><%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){if(cwb.getFlowordertype()==ft.getValue()){ %><%=ft.getText() %><%}} %></td>
								<td width="200" align="center" valign="middle">
								<select name="losebackbranchid<%=cwb.getOpscwbid()%>" id="losebackbranchid<%=cwb.getOpscwbid()%>" style="width: 200;">
									<option value="0">请选择</option>
									<%if(branchList.size()>0)for(Branch at : branchList){ %>
											<%-- <option  title="<%=at.getName() %>" value="<%=at.getId()%>_<%= Long.parseLong(mapForAb.get(cwb.getOpscwbid()).getString("abnormalorderid"))%>"
											<%if(Long.parseLong(mapForAb.get(cwb.getOpscwbid()).getString("abnormalordertype"))==at.getId()){ %>selected<%} %>><%if(at.getName().length()>12){%><%=at.getName().substring(0,12)%><%}else{ %><%=at.getName() %><%} %>
											</option> --%>
											<option value="<%=at.getBranchid() %>" <%if(at.getBranchid()==losebackbranchid) {%> selected="selected"  <%} %>>
											<%=at.getBranchname() %>
											</option>
									<%} %>
								</select></td>
								<td width="200" align="center" valign="middle"><input type="text" name="describe<%=cwb.getOpscwbid()%>" id="describe<%=cwb.getOpscwbid()%>" style="width:95%" value="<%=losebackdescribe.equals("最多输入100个字")?"":losebackdescribe %>"/></td>
								<td width="200" align="center"  valign="middle">
								<input type="file" id="file<%=cwb.getOpscwbid() %>" name="file<%=cwb.getOpscwbid() %>" width="200"/>
								</td>
							</tr>
							<%} %>
							<input type="hidden" id="cwbdetails" name="cwbdetails" value=""/>
						</tbody>
					</table>
					</form>
					</div>
					

				</div>
				<%if(cwbList!=null&&cwbList.size()>0){ %>
				<div class="iframe_bottom" >
						<table width="100%" border="0" cellspacing="1"  class="table_2" id="gd_table2">
							<tbody>
								<tr height="30" >
									<td align="center"><input type="button" value="提交" class="button" onclick="sub();">
									<input type="button" value="取消" class="button" onclick="subChexiao();">
									
									</td>
									
								</tr>
							</tbody>
						</table>
				</div>
				<%} %>
		</div>
	</div>
</div>
<form id="searchForm3"  name="searchForm3" action="<%=request.getContextPath()%>/abnormalOrder/losebackcreate" method="post"  enctype="multipart/form-data">
</form>
</body>
</html>

