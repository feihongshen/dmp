<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>补录重量</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/dmp40/plug-in/easyui/themes/icon.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/dmp40/plug-in/datatables/css/bootstrap.css" type="text/css"></link>
<link href="/js/multiSelcet/multiple-select.css" rel="stylesheet" type="text/css" />
<link href="/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<style type="text/css">
.show-grid{
		margin-top: 2px;
		margin-bottom: 2px;
	}
form select,input[type="text"],input[type="number"]{
		font-size: 12px;
		height:30px ;
		width:120px
}
.datepicker{
		height:30px !important;
		width:170px !important;
	}
.validatebox-invalid {
	  border-color: #ffa8a8 !important;
	  background-color: #fff3f3 !important;
	  color: #000 !important;
}
</style>
</HEAD>
<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>
<body class="easyui-layout" leftmargin="0" topmargin="0">
  <div  data-options="region:'center'" style="overflow-x:auto;overflow-y:auto;width:100%;height:100%;">
      <table id = "weightAgainTab" class="table" style="border: 1px;align:center; margin-bottom:100px;width:100%;padding-left:10px;background-color:#f5f5f5;">
         <tr style = "height:40px;">
             <td style="border: 0px; text-align: right; vertical-align: middle; width:20%;">订单号:</td>
             <td style="border: 0px; vertical-align: middle;width:80%;padding:0px;">
                 <input type = "text" id = "orderNumber" name = "orderNumber" style = "padding-left:3px;width:180px;" />
             </td>
         </tr>
         <tr style = "height:40px;">
             <td style="border: 0px; text-align: right; vertical-align: middle; width:20%;">运单号:</td>
             <td style="border: 0px; vertical-align: middle;width:80%;padding:0px;">
                <input type = "text" id = "deliveryNumber" name = "deliveryNumber" style = "padding-left:3px;width:180px;" />
             </td>
         </tr>
         <tr style = "height:40px;">
             <td style="border: 0px; text-align: right; vertical-align: middle; width:20%;"> 重量(Kg):</td>
             <td style="border: 0px; vertical-align: middle;width:80%;padding:0px;">
                 <input type = "text" id = "weightAmount" name = "weightAmount" style = "padding-left:3px;width:180px;" />
             </td>                                
         </tr>
         <tr style = "height:40px;">
             <td style="border: 0px; vertical-align: middle;  width:100%;align:center" colspan = 2> 
                 <div class="btn btn-default" onclick="submitEdit();" style = "margin-left:150px;" ><i class="icon-ok-circle"></i>提交</div>
             </td>
         </tr>
      </table>
  </div>
</body>
</HTML>
