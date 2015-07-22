$(function (){
	//设置分页控件  
    var p = $('#tt').datagrid('getPager');  
    $(p).pagination({  
        pageSize: 10,//每页显示的记录条数，默认为10  
        pageList: [10,20],//可以设置每页记录条数的列表  
        beforePageText: '第',//页数文本框前显示的汉字 
        afterPageText: '页    共 {pages} 页', 
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });  
	
});
/*根据客户名称查询信息 */
  function submitform(){
	  $('#tt').datagrid('load', {    
		  customername:$("#customername").val()
		});
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
                      ids.push($(rows[i].id)[0]);
                  }
                  //将选择到的行存入数组并用,分隔转换成字符串，
                  //本例只是前台操作没有与数据库进行交互所以此处只是弹出要传入后台的id
                     $.ajax({
                	  type:'post',
                	  url: App.ctx+"/paybusinessbenefits/cutData/"+ids.join(','),
                	  dataType:'json',
                	  success:function (data){
                		  $.messager.alert(data.errormsg);
                		  $('#tt').datagrid('load');
                	  }
                	  
                  });
                 
              }
          });
      }
      else {
          $.messager.alert("提示", "请选择要删除的行", "error");
      }
  }
  /*初始化dialog中的table  */
  var datagrid; //定义全局变量datagrid
  function createpaybusinessbenefits(){
	initDialog();
	$("#dlg").dialog('open');
	datagrid=$('#create').datagrid({
		autoEditing:true,
		singleEditing:true,
		collapsible:true,//是否可折叠的  
		columns:[[
          {field:'checkboxdata',title:'', width:100, align:'right', checkbox:'true'},
          {field:'tuotoudownrate',title:'妥投率下限', width:100, align:'right', editor: { type: 'numberbox',options: { required: true } }},
          {field:'tuotouprate',title:'妥投率上限', width:100, align:'right', editor: { type: 'numberbox',options: { required: true } }},
          {field:'kpimoney',title:'KPI奖励金额', width:100, align:'right', editor: { type: 'numberbox', options: { required: true } }}
    	]],
    	onAfterEdit:function(rowIndex, rowData, changes){
    		 if(rowData.kpimoney<0){
                  $('#create').datagrid('updateRow', { index: rowIndex, row: {kpimoney:'0'} });
    	          $.messager.alert("提示", "KPI奖励金额不能为负数！！请重新输入！！", "error");
				
 		}
    		 if(rowData.tuotoudownrate<0){
                  $('#create').datagrid('updateRow', { index: rowIndex, row: {tuotoudownrate:'0'} });
    	          $.messager.alert("提示", "妥投率下限不能为负数！！请重新输入！！", "error");
				
 		}
    		 if(rowData.tuotouprate<0){
                  $('#create').datagrid('updateRow', { index: rowIndex, row: {tuotouprate:'0'} });
    	          $.messager.alert("提示", "妥投率上限不能为负数！！请重新输入！！", "error");
				
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
  /*插入新的行  */
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
  /*删除选择的行  */
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
  /*保存客户设置  */
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
			  url: App.ctx +"/paybusinessbenefits/savedata",
			  dataType:'json',
			  success:function (data){
				  if(data.errorCode==0){
			          initDialog();
			          submitform();
				  }else if(data.errorCode==2){
					  initDialogCustomer();
				  }
		          $.messager.alert("提示", data.error, "error");
			  }
		  });
	  }else{
          $.messager.alert("提示", "没有需要保存的数据！！", "error");
	  }
  }
  function checkCreateotherbenefits(obj){
	  if(isNaN($(obj).val())){
          $.messager.alert("提示", "其它补助只能为数字类型！！", "error");
          $(obj).val("0");
          return;
	  }
	  if($(obj).val()<0){
		  $(obj).val('0');
          $.messager.alert("提示", "其它补助不能为负数！！", "error");
	  }
  }
  /*初始化dialog  */
  function initDialog(){
	  $("#customerid").combobox('setValue','0');
      $("#createotherbenefits").val('0');
      $("#create").datagrid('loadData', { total: 0, rows: [] });
  }
  function initDialogCustomer(){
	  $("#customerid").combobox('setValue','0');
  }
  function closeDialog(){
	  $("#dlg").dialog('close');
  }