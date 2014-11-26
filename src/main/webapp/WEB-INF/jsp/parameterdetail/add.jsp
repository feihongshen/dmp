<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.domain.CwbALLStateControl,cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>创建操作设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script>

function buttonSave(form){
	if($("#flowordertype").val()==-1){
		alert("当前环节不能为空");
		return false;
	}else if($("#filed").val()==0){
		alert("必选字段不能为空");
		return false;
	}else{
		$.ajax({
			type: "POST",
			url:$(form).attr("action"),
			data:$(form).serialize(),
			dataType:"json",
			success : function(data) {
				alert(data.error);
			}
		});
	}
}
</script>
<div style="background:#eef9ff">
	<div id="box_in_bg">
		<h1>创建操作设置</h1>
		<form id="parameterDetail_cre_Form" name="parameterDetail_cre_Form"
			 onSubmit="buttonSave(this);return false;" 
			 action="<%=request.getContextPath()%>/parameterDetail/create;jsessionid=<%=session.getId()%>" method="post"  >
			<div id="box_form">
				<ul>
					<li><span>当前环节：</span>
						<select id="flowordertype" name="flowordertype">
							<option value="-1" selected>----请选择----</option>
							<option value="<%=FlowOrderTypeEnum.TiHuo.getValue() %>" ><%=FlowOrderTypeEnum.TiHuo.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.RuKu.getValue() %>" ><%=FlowOrderTypeEnum.RuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>" ><%=FlowOrderTypeEnum.ChuKuSaoMiao.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue() %>" ><%=FlowOrderTypeEnum.FenZhanLingHuo.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() %>" ><%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() %>" ><%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getText() %></option>
						</select>*
					</li>
	           		<li><span>必选字段：</span>
	           			<select id="filed"  name="filed">
							<option value="0" selected>----请选择----</option>
							<option value="branchid" >下一站</option>
							<option value="customerid" >供货商</option>
							<option value="driverid" >司机</option>
							<option value="truckid" >车辆</option>
							<option value="baleid" >包号</option>
							<option value="comment" >备注</option>
						</select>*
					</li>
	         </ul>
		</div>
		<div align="center">
        <input type="submit" value="确认" class="button" id="sub" />　
        <input type="button" value="返回" class="button" id="cancel" onclick="location='list/1'" /></div>
	</form>
	</div>
</div>


