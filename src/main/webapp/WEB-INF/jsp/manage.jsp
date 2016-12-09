<%@page import="cn.explink.util.ResourceBundleUtil"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>易普配送信息管理平台DMP</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/js/easyui1.5/themes/default/easyui.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/js/easyui1.5/themes/icon.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/easyui1.5/jquery.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/manage.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/easyui1.5/jquery.easyui.min.js" type="text/javascript"></script>
<style>
    td{
       width:300px;
    }
    td div {
       width: 100%;
    }    
</style>
</head>
<body>
    <!--  
	<textarea id="cwbs" rows="25" cols="100"></textarea>
	-->
	<div style="margin-left:20px;height:100%; display:inline-block">
	   <input id="cwbs" class="easyui-textbox" data-options="multiline:true, label:'订单号', labelPosition:'top'"
	   style="width:250px;height:100%">
	</div>
	<div style="display:inline-block">
		<table>
			<tr>
				<td><div id="btnResendFlowJms" class="easyui-linkbutton">重发express_ops_order_flow所有</div></td>
				<td><div id="btnResendFlowJmsEnd" class="easyui-linkbutton">重发express_ops_order_flow最后状态</div></td>
			</tr>
			<tr>
				<td><div id="btnResendGotoClassJms" class="easyui-linkbutton">重发express_ops_goto_class_auditing消息(id)</div></td>
			</tr>
			<tr>
				<td><div id="btnResendPayup" class="easyui-linkbutton">重发express_ops_pay_up消息(id)</div></td>
			</tr>
			<tr>
			    <td><div id="btnTpoSendDoInfFail" class="easyui-linkbutton">重推tpo_send_do_inf失败</div></td>			    
			    <td><div id="btnTpoSendDoInfAll" class="easyui-linkbutton">重推tpo_send_do_inf所有</div></td>
			</tr>
			<tr>
                <td><div id="btnTpoOtherOrderTrackFail" class="easyui-linkbutton">重推tpo_other_order_track失败</div></td>
                <td><div id="btnTpoOtherOrderTrackAll" class="easyui-linkbutton">重推tpo_other_order_track全部</div></td>                
            </tr>
            <tr>
                <td><div id="btnOpsSendB2cDataFail" class="easyui-linkbutton">重推express_send_b2c_data失败</div></td>
                <td><div id="btnOpsSendB2cDataAll" class="easyui-linkbutton">重推express_send_b2c_data全部</div></td>                
            </tr>
		</table>
		<!-- 这个事件没有写
	   <button id="btnResendPdaDeliverJms" class="easyui-linkbutton">重发反馈消息</div>	
	   -->
		
	</div>
</body>
</html>