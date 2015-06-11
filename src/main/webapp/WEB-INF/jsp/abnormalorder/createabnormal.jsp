<%@page import="cn.explink.domain.User"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.AbnormalType"%>
<%@page import="cn.explink.domain.AbnormalOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.explink.dao.AbnormalOrderDAO"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<AbnormalType> abnormalTypeList = (List<AbnormalType>)request.getAttribute("abnormalTypeList");
List<CwbOrder> cwbList = (List<CwbOrder>)request.getAttribute("cwbList");
  List<Customer> customerlist = (List<Customer>)request.getAttribute("customerList");
  List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
  Page page_obj = (Page)request.getAttribute("page_obj");
  Map<Long,JSONObject> mapForAb=(Map<Long,JSONObject>)request.getAttribute("mapForAbnormal");
  Boolean iszhandian=(Boolean)request.getAttribute("iszhandian");
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
	
});
function sub(){
	var datavalue = "[";
	var noMsg = 0;
	<%
	
	if(cwbList!=null&&cwbList.size()>0)
		{
		for(CwbOrder cwb : cwbList){
%>	
		if($("#abnormaltypeid<%=cwb.getOpscwbid()%>").val()==0){
		
			noMsg +=1;
		}else{
			var opscwbid="<%=cwb.getOpscwbid()%>";
			var describe=$("#describe<%=cwb.getOpscwbid()%>").val();
			var abnormaltypeid=$("#abnormaltypeid<%=cwb.getOpscwbid()%>").val();
			var cwb="<%=cwb.getCwb()%>";
			datavalue=  datavalue +  "\""+opscwbid+"_s_"+describe+"_s_"+abnormaltypeid+"_s_"+cwb+" \",";
		}
		
	<%}}%>
	if(noMsg>0){
		alert("您还有"+noMsg+"单没选择问题件类型，请先选择！");
		return false;
	}
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
	}
	datavalue= datavalue + "]";
	$.ajax({
		type: "POST",
		url:'<%=request.getContextPath()%>/abnormalOrder/submitCreateabnormal',
		data:{cwbdetails:datavalue},
		dataType:"html",
		success : function(data) {
			alert("问题件成功提交："+data+"单");
			searchForm.submit();
		}
	});
	
}
function createabnormalData(){
	var flag=true;
	console.info($("#abnormalinfo").val().length);
	if($("#abnormalinfo").val().length>100){
		alert("问题件说明过长，请适当填写！！");
		flag=false;
	}
	if($("#cwb").val()==""||$("#cwb").val()=="查询多个订单用回车隔开"){
		alert("订单号不能为空！！");
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
 	dataInit['abnormaltypeid'] = $("#abnormaltypeid").val();
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
        	alert(data.error);
         /*    if(data.errorCode==0){
            	$("#showMessage").html(data.error);
            }else{
              $("#showMessage1").html(data.error);
            } */
            $("#cwb").val("查询多个订单用回车隔开");
            $("#abnormaltypeid").val(0);
            $("#abnormalinfo").val("最多输入100个字");
            
        },  
        error: function (data, status, e)  
        {  
            alert("上传发生异常,创建丢失件失败");  
        }  
    });  

    return false;  
}  
</script>
</head>
<body style="background:#f5f5f5;overflow: hidden;" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="#" class="light">创建问题件</a></li>
<!-- 				<li><a href="./tofindabnormal/1" class="light">问题件查询</a></li>
 -->			</ul>
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0; " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form id="searchForm"  name="searchForm" action="<%=request.getContextPath()%>/abnormalOrder/toCreateabnormalAdd" method="post" onsubmit="if(createabnormalData())ajaxFileUpload();return false;" enctype="multipart/form-data">
							<div class="menucontant">
							<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1" align="left">
							<tr>
							<td>
							
								订    单    号*：
								<textarea id="cwb" name="cwb"  onblur="if(this.value==''){this.value='查询多个订单用回车隔开'}" onfocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" rows="3" class="kfsh_text">查询多个订单用回车隔开</textarea>
								</td>
								<td>
								&nbsp;&nbsp;问题类型：
									<label for="select2"></label>
									<select name="abnormaltypeid" id="abnormaltypeid" class="select1">
										<option value="0">请选择</option>
										<%if(abnormalTypeList.size()>0)for(AbnormalType at : abnormalTypeList){ %>
											<option title="<%=at.getName() %>" value="<%=at.getId()%>"><%if(at.getName().length()>20){%><%=at.getName().substring(0,19)%><%}else{ %><%=at.getName() %><%} %></option>
										<%} %>
									</select>
									
									</td>
									<td>
							<%-- 		<p>
								
								处理部门:<select name="handleBranch" id="handleBranch" class="select1" <%if(!iszhandian){ %> disabled="disabled" <%} %>>
								<option value="<%=BranchEnum.KeFu.getValue()%>">请选择(限站点操作)</option>
								<option value="<%=BranchEnum.KuFang.getValue()%>"><%=BranchEnum.KuFang.getText()%></option>
								<option value="<%=BranchEnum.KeFu.getValue()%>"><%=BranchEnum.KeFu.getText()%></option>
								</select>
								</p> --%>
								</td>
								
								</tr>
								<tr>
								<td >
								问题件说明:<textarea id="abnormalinfo" name="abnormalinfo" onblur="if(this.value==''){this.value='最多输入100个字'}" onfocus="if(this.value=='最多输入100个字'){this.value=''}">最多输入100个字</textarea><br>
								</td>
								<td align="left">
								上传附件:<input type="file" name="file" id="file"/>
								</td>
								<td>
								<input type="submit" value="创建" id="createabnormal" name="createabnormal"  class="input_button2"> <input type="reset" value="取消" class="input_button2">
								</td>
								
								</tr>
								
								</table>
								</div>	
							</form>
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" style="display: none;">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="150" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">配送站点</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">当前状态</td>
									<td width="200" align="center" valign="middle" bgcolor="#E7F4E3">问题件类型</td>
									<td align="center" valign="middle" bgcolor="#E7F4E3">问题说明</td>
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
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
						<tbody>
							<%if(cwbList!=null&&cwbList.size()>0)for(CwbOrder cwb : cwbList){ 
							%>
							<tr height="30" >
								<td width="150" align="center" valign="middle"><%=cwb.getCwb() %></td>
								<td width="120" align="center" valign="middle"><%for(Customer c : customerlist){if(cwb.getCustomerid()==c.getCustomerid()){ %><%=c.getCustomername() %><%}} %></td>
								<td width="120" align="center" valign="middle"><%=cwb.getEmaildate() %></td>
								<td width="100" align="center" valign="middle"><%for(Branch b : branchlist){if(cwb.getDeliverybranchid()==b.getBranchid()){ %><%=b.getBranchname() %><%}} %></td>
								<td width="100" align="center" valign="middle"><%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){if(cwb.getFlowordertype()==ft.getValue()){ %><%=ft.getText() %><%}} %></td>
								<td width="200" align="center" valign="middle">
								<select name="abnormaltypeid<%=cwb.getOpscwbid()%>" id="abnormaltypeid<%=cwb.getOpscwbid()%>">
									<option value="0">请选择</option>
									<%if(abnormalTypeList.size()>0)for(AbnormalType at : abnormalTypeList){ %>
											<option  title="<%=at.getName() %>" value="<%=at.getId()%>_<%= Long.parseLong(mapForAb.get(cwb.getOpscwbid()).getString("abnormalorderid"))%>"
											<%if(Long.parseLong(mapForAb.get(cwb.getOpscwbid()).getString("abnormalordertype"))==at.getId()){ %>selected<%} %>><%if(at.getName().length()>12){%><%=at.getName().substring(0,12)%><%}else{ %><%=at.getName() %><%} %>
											</option>
									<%} %>
								</select></td>
								<td valign="middle"><input type="text" name="describe<%=cwb.getOpscwbid()%>" id="describe<%=cwb.getOpscwbid()%>" style="width:95%" /></td>
								
							</tr>
							<%} %>
						</tbody>
					</table>
					</div>
					

				</div>
				<%if(cwbList!=null&&cwbList.size()>0){ %>
				<div class="iframe_bottom" >
						<table width="100%" border="0" cellspacing="1"  class="table_2" id="gd_table2">
							<tbody>
								<tr height="30" >
									<td align="center"><input type="button" value="提交" class="button" onclick="sub();"></td>
								</tr>
							</tbody>
						</table>
				</div>
				<%} %>
		</div>
	</div>
</div>
<script type="text/javascript">
$("#abnormaltypeid").val(<%=request.getParameter("abnormaltypeid")==null?0:Long.parseLong(request.getParameter("abnormaltypeid"))%>);
</script>
</body>
</html>

