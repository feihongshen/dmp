<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<script src="${ctx}/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
$(function (){
	 //全选
    $("#detailcheckbox").click(function () {
        if ($(this).attr('checked') == 'checked') {
            $("input[name='PD']").attr("checked", 'checked');
        } else {
            $("input[name='PD']").removeAttr("checked");
        }
	});
	//设置分页控件  
    var p = $('#tt').datagrid('getPager');  
    $(p).pagination({  
        pageSize: 10,//每页显示的记录条数，默认为10  
        pageList: [10],//可以设置每页记录条数的列表  
        beforePageText: '第',//页数文本框前显示的汉字 
        afterPageText: '页    共 {pages} 页', 
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
        /*onBeforeRefresh:function(){ 
            $(this).pagination('loading'); 
            alert('before refresh'); 
            $(this).pagination('loaded'); 
        }*/ 
    });  
	
});
  function submitform(){
	  $("#querydata").submit();
  }
  /*删除操作  */
  function deleteData(){
	   //删除时先获取选择行
      var rows = $("#tt").datagrid("getSelections");
      //选择要删除的行
      if (rows.length > 0) {
          $.messager.confirm("提示", "你确定要删除吗?", function (r) {
              if (r) {
                  var ids = [];
                  for (var i = 0; i < rows.length; i++) {
                      ids.push($(rows[i].ck).val());
                  }
                  //将选择到的行存入数组并用,分隔转换成字符串，
                  //本例只是前台操作没有与数据库进行交互所以此处只是弹出要传入后台的id
/*                   alert(ids.join(','));
 */                  $.ajax({
                	  type:'post',
                	  url:"<%=request.getContextPath()%>/paybusinessbenefits/cutData/"+ids.join(','),
                	  dataType:'json',
                	  success:function (data){
                		  $.messager.alert(data.errormsg);
                		  $("#querydata").submit();
                	  }
                	  
                  });
                 
              }
          });
      }
      else {
          $.messager.alert("提示", "请选择要删除的行", "error");
      }
  }
  var datagrid; //定义全局变量datagrid
  function createpaybusinessbenefits(){
	$("#dlg").dialog('open');
	datagrid=$('#create').datagrid({
		autoEditing:true,
		singleEditing:true,
		collapsible:true,//是否可折叠的  
		columns:[[
          {field:'checkboxdata',title:'妥投率上限', width:100, align:'right', checkbox:'true'},
          {field:'tuotoudownrate',title:'妥投率上限', width:100, align:'right', editor: { type: 'text' }},
          {field:'tuotouprate',title:'妥投率下限', width:100, align:'right', editor: { type: 'text' }},
          {field:'kpimoney',title:'KPI奖励金额', width:100, align:'right', editor: { type: 'numberbox', options: { required: true } }}
    	]],
    	onAfterEdit:function(rowIndex, rowData, changes){
    		 if(rowData.kpimoney.indexOf("-")>=0){
    			/*  setTimeout(function () { */
                     $('#create').datagrid('updateRow', { index: rowIndex, row: {kpimoney:'0'} });
                 /* }, 100); */
/*     			  $(this).datagrid('beginEdit', rowIndex);
 */    	          $.messager.alert("提示", "KPI奖励金额不能为负数！！请重新输入！！", "error");
				return;
 		}
    	}
		,
		onDblClickCell: function(index,field,value){
			$(this).datagrid('beginEdit', index);
			var ed = $(this).datagrid('getEditor', {index:index,field:field});
			$(ed.target).focus();
		}

		
	});
  }
  function insertNew(){
	  var $rows=$('#create').datagrid('getRows');
	  $('#create').datagrid('insertRow',{
		  index:$rows.length,
		  row:{
			  checkboxdata:"",
			  tuotoudownrate:"0",
			  tuotouprate:"0",
			  kpimoney:"0"
		  }
	  });
  }
  function deleteRow(){
      var rows = datagrid.datagrid("getSelections");
      if (rows.length > 0) {
          $.messager.confirm("提示", "你确定要移除吗?", function (r) {
              if (r) {
                  for (var i = 0; i < rows.length; i++) {
                      var rowIndex = $('#create').datagrid('getRowIndex', rows[i]);
                      $('#create').datagrid('deleteRow', rowIndex);
                  }
              }
          });
      }
      else {
          $.messager.alert("提示", "请选择要移除的行", "error");
      }
  }
  function closeDialog(){
	  $("#dlg").dialog('close');
  }
  function saveData(){
	  if($("#customerid").combobox('getValue')==0){
          $.messager.alert("提示", "请选择客户！！", "error");
          return;
	  }
	  var savedata="";
	  var rows = $("#create").datagrid("getRows"); 
	  for(var i=0;i<rows.length;i++)
	  {
		  savedata+=rows[i].tuotoudownrate+"~"+rows[i].tuotouprate+"="+rows[i].kpimoney+"|";
	  }
	  if(savedata.length>0){
		  $.ajax({
			  type:"post",
			  data:
			  {
				  paybusinessbenefits:savedata.substring(0, savedata.length-1),
				  customerid:$("#customerid").combobox('getValue'),
				  othersubsidies:$("#createotherbenefits").val()
			  },
			  url:"<%=request.getContextPath()%>/paybusinessbenefits/savedata",
			  dataType:'json',
			  success:function (data){
				  if(data.errorCode==0){
			          $.messager.alert("提示", data.error, "error");
			          $("#customerid").combobox('setValue','0');
                      $("#createotherbenefits").val('0');
			          $("#create").datagrid('loadData', { total: 0, rows: [] });
				  }else{
			          $.messager.alert("提示", data.error, "error");
				  }
			  }
		  });
	  }else{
          $.messager.alert("提示", "没有需要保存的数据！！", "error");
	  }
  }
  function checkCreateotherbenefits(obj){
	  if($(obj).val()<0){
		  $(obj).val('0');
          $.messager.alert("提示", "其它补助不能为负数！！", "error");
	  }
  }
</script>
</head>
<body>
    <!--最外层div  -->
	<div id="content" class="easyui-panel" >
		<!--数据表格 -->
		 <table id="tt" class="easyui-datagrid"   
   			 url="" toolbar="#tb"  
    		title="工资业务补助设置" iconCls="icon-save"  
    		rownumbers="true" pagination="true" pageSize=10 style="width:auto;  height: 450px; padding: 5px"
    		> 
    		<thead >
			<tr>  
             <th field="ck" id="detailcheckbox" checkbox="true"></th>  
                <th field="customername" width="200"  >客户名称</th>  
                <th field="productid" width="500" >业务KPI补助</th>  
                <th field="otherbenefits" width="200" >其他补助</th>  
            </tr>
            </thead>
            <c:forEach items="${paybusinessbenefits}" var="paybusinessbenefit">
            	<tr>
            		<td><input name="PD" type="checkbox" value="${paybusinessbenefit.id}"/></td>
            		<td>${paybusinessbenefit.customername}</td>
            		<td>${paybusinessbenefit.paybusinessbenefits}</td>
            		<td>${paybusinessbenefit.othersubsidies == "0.00"?"" : paybusinessbenefit.othersubsidies}</td>
            	</tr>
            </c:forEach>
          </table>
            
	 <!--查询条件与操作  -->
	 <div id="tb" style="padding:5px;height:auto">  
	 	<form action="${pageContext.request.contextPath}/paybusinessbenefits/list/1" id="querydata" method="post">
	 	<label>客户名称:</label><input  class="easyui-textbox" name="customername" />
	 	 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="createpaybusinessbenefits();">新建</a>  
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cut" onclick="deleteData();">删除</a>  
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="submitform();">查询</a>  
	 	</form>
    </div> 
    </div>
    <!--新建弹出框  -->
    <div id="dlg" class="easyui-dialog" title="新建工资业务补助设置" data-options="iconCls:'icon-save',closed:'true',resizable:true" style="width:500px;height:400px;padding:10px">
    	
			 <table id="create" class="easyui-datagrid"   
   			 url=""  toolbar="#createToolbar"
    		iconCls="icon-save"  
    		rownumbers="true" > 
          </table>
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="insertNew();">插入</a>  
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" onclick="deleteRow();">移除</a>  
	</div>
	 <!--查询条件与操作  -->
	 <div id="createToolbar" style="padding:5px;height:auto">  
	  	 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-back" onclick="closeDialog();">返回</a>  
         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="saveData();">保存</a> 
        <br> 
	 	客户名称:
	 		<select name="customerid" id="customerid" class="easyui-combobox"  style="width:200px;">
	 			<option value="0">请选择客户名称</option>
	 			<c:forEach items="${customersList}" var="customer">
	 				<option value="${customer.customerid}">${customer.customername}</option>
	 			</c:forEach>
	 		</select>
	 	其它补助：<input class="easyui-textbox" name="createotherbenefits" id="createotherbenefits" value="0" onchange="checkCreateotherbenefits(this);"/>
	 </div>
</body>
</html>