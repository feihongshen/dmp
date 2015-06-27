<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<script src="${ctx}/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx}/js/commonUtil.js" type="text/javascript"></script>
<script src="${ctx}/js/workorder/csPushSmsList.js" type="text/javascript"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>催件短信统计</title>
</head>

<body>
	<div id="tb" style="padding:10px;height:auto">
		 
	 	<!-- 查询条件区域  -->
       	<form id="searchFrom" action="">
   	        <input type="text" name="filter_GTD_sendTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '短信起始时间'"/> - 
   	        <input type="text" name="filter_LTD_sendTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '短信终止时间'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
  	        <input type="text" name="filter_EQS_cwbOrderNo" class="easyui-validatebox" data-options="width:150,prompt: '订/运单号'"></input>
	        <span class="toolbar-item dialog-tool-separator"></span>
   	        <input type="text" name="filter_EQS_workOrderNo" class="easyui-validatebox" data-options="width:150,prompt: '工单号'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
   	       <!--  <input type="text" name="filter_EQS_complianUserName" class="easyui-validatebox" 
					initDataType="TABLE" 
					initDataKey="User" 
					viewField="realname" 
      	        	saveField="username"
					data-options="width:150,prompt: '责任人'"
			/>
	        <span class="toolbar-item dialog-tool-separator"></span>
   	        <input type="text" name="filter_EQL_complianBranchId" class="easyui-validatebox" 
					initDataType="TABLE" 
					initDataKey="Branch" 
					viewField="branchid,branchname" 
      	        	saveField="branchid"
					data-options="width:150,prompt: '责任机构'"
			/>
	        <span class="toolbar-item dialog-tool-separator"></span> -->
   	        <input type="text" name="filter_EQL_complaintState" class="easyui-validatebox" 
      	        	initDataType="ENUM" 
      	        	initDataKey="cn.explink.enumutil.ComplaintStateEnum"
      	        	viewField="text" 
      	        	saveField="value"
	      	        data-options="width:150,prompt: '工单状态'"
   	        />
			
		<!-- dataGrid工具条区域  -->
   		
        <a href="javascript(0)" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="cx()">查询</a>
  		<span class="toolbar-item dialog-tool-separator"></span>
  		
  		<!-- 
  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add();">添加</a>
  		<span class="toolbar-item dialog-tool-separator"></span>
      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="upd()">修改</a>  
        -->
            
	</div>
	
	<!-- dataGrid展示区域  -->
	<table id="dg"></table> 
	
	<!-- 弹出框展示区域 -->
	<div id="dlg">
	</div>  
	
</body>
</html>

