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
	
});

function createabnormalData(){
	var flag=true;
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
            	$("#showMessage").html(data.error);
            }else{
              $("#showMessage1").html(data.error);
            }
            
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
		<div class="tabbox">
				<div style="position:relative; z-index:0; " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<div class="menucontant">
							<form id="searchForm"  name="searchForm" action="<%=request.getContextPath()%>/abnormalOrder/toCreatMissPiece" method="post" onsubmit="if(createabnormalData())ajaxFileUpload();return false;" enctype="multipart/form-data">
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
								找 回说 明：<textarea id="abnormalinfo" name="abnormalinfo" ></textarea><br>
								</td>
								<td align="left">
								上传附件:<input type="file" name="file" id="file"/>
								</td>
								<td >
								<input type="submit" value="提交" id="createabnormal" name="createabnormal"  class="input_button2"> <input type="reset" value="取消" class="input_button2">
								</td>
								
								</tr>
								
								
								
								</table>
								<font id="showMessage" color="red" style="border-left: thin;"></font>
								<font id="showMessage1" color="blue" style="border-left: thin;"></font>
								</div>	
							</form>
						</div>
						</div>
						<br>
				</div>		
		</div>
	</div>
</div>

</body>
</html>

