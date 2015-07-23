<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<script src="${ctx}/js/paybusinessbenefit.js" type="text/javascript"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
    <!--查询页面div  -->
	<div id="content" class="easyui-panel">
		<!--数据表格 -->
		 <table id="tt" class="easyui-datagrid"   
   			url="${pageContext.request.contextPath}/paybusinessbenefits/listfind" 
   			toolbar="#tb"  
    		title="工资业务补助设置" 
    		iconCls="icon-save"  
    		rownumbers="true" 
    		pagination="true" 
    		pageSize=10 
    		collapsible ="true"
    		> 
    		<thead >
			<tr>  
             	<th field="id"  checkbox="true" ></th>  
                <th field="customername" width="200"  formatter="go">客户名称</th>  
                <th field="paybusinessbenefits" width="500" >业务KPI补助</th>  
                <th field="othersubsidies" width="200" >其他补助</th>  
                <th field="customerid" width="200" hidden="true">其他补助</th>  
            </tr>
            </thead>
        
          </table>
           <!--数据表格结束 --> 
	 <!--查询页面div查询条件与操作  -->
	 <div id="tb" style="padding:5px;height:auto">  
	 	<label>客户名称:</label><input  class="easyui-textbox" name="customername" id="customername" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitform();}'/>
	 	 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="createpaybusinessbenefits('create','','','');">新建</a>  
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cut" onclick="deleteData();">删除</a>  
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="submitform();">查询</a>  
    </div> 
              <!--查询页面div查询条件与操作结束  -->
    </div>
              <!--查询页面div结束  -->
    
    <!--新建create弹出框div  -->
    <div id="dlgcreate" class="easyui-dialog" 
   		 title="新建工资业务补助设置"
    	 data-options="iconCls:'icon-save',closed:'true',resizable:true" 
    	 style="width:500px;height:400px;padding:10px">
		<table id="create" class="easyui-datagrid"   
   			 url=""  toolbar="#createToolbar"
    		 iconCls="icon-save"  
    		 rownumbers="true" > 
          </table>
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="insertNew('create');">插入</a>  
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" onclick="deleteRow('create');">移除</a>  
	<!--dialog查询条件与操作  -->
	 	<div id="createToolbar" style="padding:5px;height:auto">  
	  	 	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-back" onclick="closeDialog('dlgcreate');">返回</a>  
         	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="saveData('create');">保存</a> 
        <br> 
	 	客户名称:
	 		<select name="customerid" id="customeridcreate" class="easyui-combobox"  style="width:200px;">
	 			<!-- <option value="0">请选择客户名称</option> -->
	 			<%-- <c:forEach items="${customersList}" var="customer">
	 				<option value="${customer.customerid}">${customer.customername}</option>
	 			</c:forEach> --%>
	 		</select>
	 	其它补助：<input class="easyui-textbox" name="createotherbenefits" id="createotherbenefitscreate" value="0" onchange="checkCreateotherbenefits(this);"/>
	 </div>
	      <!--dialog查询条件与操作结束  -->
	</div>
		  <!--新建弹出框div结束  -->
		
	<!--编辑弹出框div  -->
    <div id="dlgedit" class="easyui-dialog" title="修改工资业务补助设置" 
    	data-options="iconCls:'icon-save',closed:'true',resizable:true" 
    	style="width:500px;height:400px;padding:10px">
			 <table id="edit" class="easyui-datagrid"   
   			 url=""  toolbar="#editToolbar"
    		iconCls="icon-save"  
    		rownumbers="true" > 
          </table>
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="insertNew('edit');">插入</a>  
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" onclick="deleteRow('edit');">移除</a>  
	 <!--dialog修改  -->
	 <div id="editToolbar" style="padding:5px;height:auto">  
	  	 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-back" onclick="closeDialog('dlgedit');">返回</a>  
         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="saveData('edit');">保存</a> 
        <br> 
	 	客户名称:
	 		<select name="customerid" id="customeridedit"
	 		 class="easyui-combobox" disabled="true"   
	 		 style="width:200px;"
	 		 >
	 			<%-- <option value="0">请选择客户名称</option>
	 			<c:forEach items="${customersList}" var="customer">
	 				<option value="${customer.customerid}">${customer.customername}</option>
	 			</c:forEach> --%>
	 		</select>
	 	其它补助：<input class="easyui-textbox" name="createotherbenefits" id="createotherbenefitsedit" value="0" onchange="checkCreateotherbenefits(this);"/>
	 </div>
	   <!--dialog修改 结束  -->
	</div>
	   <!--编辑弹出框结束  -->
</body>
</html>