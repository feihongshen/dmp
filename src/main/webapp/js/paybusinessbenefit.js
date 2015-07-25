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
		  customername:$("#customernamedata").val()
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
          $.messager.alert("提示", "请选择要删除的行", "warning");
      }
  }
  var eachData;
  /*初始化dialog中的table(新建与修改)  */
  function createpaybusinessbenefits(obj,id,customerid,othersubsidies){
	  initDialog(obj);
	  var urlString;
	  initCustomers(obj);
	  if(obj=='create'){
		  urlString="";
	  }else{
		  $("#customeridedit").combobox('setValue',customerid);
		  $("#createotherbenefitsedit").val(othersubsidies);
		  urlString=App.ctx+"/paybusinessbenefits/findeditinstall/"+id;
	  }
	$("#dlg"+obj).dialog('open');
	$('#'+obj).datagrid({
		autoEditing:true,
		singleEditing:true,
		collapsible:true,//是否可折叠的  
		url:urlString,
		columns:[[
          {field:'checkboxdata',title:'', width:100, align:'right', checkbox:'true'},
          {field:'tuotoudownrate',title:'妥投率下限', width:100, align:'right', editor: { type: 'validatebox',options: { required: true } }},
          {field:'tuotouprate',title:'妥投率上限', width:100, align:'right', editor: { type: 'validatebox',options: { required: true } }},
          {field:'kpimoney',title:'KPI奖励金额', width:100, align:'right', editor: { type: 'validatebox', options: { required: true } }}
    	]],
    	onAfterEdit:function(rowIndex, rowData, changes){
    		 if(rowData.kpimoney<0||isNaN(rowData.kpimoney)){
                  $('#'+obj).datagrid('updateRow', { index: rowIndex, row: {kpimoney:'0'} });
    	          $.messager.alert("提示", "KPI奖励金额不能为负数或其它非数值类型的值！！请重新输入！！", "warning");
				
 		}
    		 if(rowData.tuotoudownrate<0||isNaN(rowData.tuotoudownrate)||rowData.tuotoudownrate>100){
                  $('#'+obj).datagrid('updateRow', { index: rowIndex, row: {tuotoudownrate:'0'} });
    	          $.messager.alert("提示", "妥投率下限不能为负数或非数值类型的值并且不能大于100！！请重新输入！！", "warning");
				
 		}
    		 if(rowData.tuotouprate<0||isNaN(rowData.tuotouprate)||rowData.tuotouprate>100){
                  $('#'+obj).datagrid('updateRow', { index: rowIndex, row: {tuotouprate:'0'} });
    	          $.messager.alert("提示", "妥投率上限不能为负数或非数值类型的值并且不能大于100！！请重新输入！！", "warning");
				
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
  function insertNew(obj){
	  var $rows=$('#'+obj).datagrid('getRows');
	  $('#'+obj).datagrid('insertRow',{
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
  function deleteRow(obj){
      var rows = $("#"+obj).datagrid("getSelections");
      if (rows.length > 0) {
          $.messager.confirm("提示", "你确定要移除吗?", function (r) {
              if (r) {
                  for (var i = 0; i < rows.length; i++) {
                      var rowIndex = $('#'+obj).datagrid('getRowIndex', rows[i]);
                      $('#'+obj).datagrid('deleteRow', rowIndex);
                  }
              }
          });
      }
      else {
          $.messager.alert("提示", "请选择要移除的行", "warning");
      }
  }
  /*保存客户设置  */
  function saveData(obj){
	  var initflag;
	  if(obj=='create'){
		  initflag=0;
	  }else{
		  initflag=1;
	  }
		  
	  if($("#customerid"+obj).combobox('getValue')==""){
          $.messager.alert("提示", "请选择客户！！", "warning");
          return;
	  }
	  var savedata="";
	  var rows = $("#"+obj).datagrid("getRows"); 
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
				  customerid:$("#customerid"+obj).combobox('getValue'),
				  othersubsidies:$("#createotherbenefits"+obj).val(),
				  remark:initflag
			  },
			  url: App.ctx +"/paybusinessbenefits/savedata",
			  dataType:'json',
			  success:function (data){
				  if(data.errorCode==0){
			          initDialog(obj);
			          submitform();
			          $.messager.alert("提示", data.error,"info");
				  }else if(data.errorCode==2){
					  initDialogCustomer(obj);
			          $.messager.alert("提示", data.error,"error");
				  }else{
			          $.messager.alert("提示", data.error,"error");
				  }
		          if(obj=="edit"){
		        	  $("#dlg"+obj).dialog('close');
		          }
			  }
		  });
	  }else{
          $.messager.alert("提示", "没有需要保存的数据！！", "warning");
	  }
  }
  function checkCreateotherbenefits(obj){
	  if(isNaN($(obj).val())){
          $.messager.alert("提示", "其它补助只能为数字类型！！", "warning");
          $(obj).val("0");
          return;
	  }
	  if($(obj).val()<0){
		  $(obj).val('0');
          $.messager.alert("提示", "其它补助不能为负数！！", "warning");
	  }
  }
  /*初始化dialog  */
  function initDialog(obj){
	  $("#customerid"+obj).combobox('setValue','');
      $("#createotherbenefits"+obj).val('0');
      $("#"+obj).datagrid('loadData', { total: 0, rows: [] });
  }
  function initDialogCustomer(obj){
	  $("#customerid"+obj).combobox('setValue','');
  }
  function closeDialog(obj){
	  $("#"+obj).dialog('close');
  }
  function go(val,row){
	  return '<a href="#" onclick="constructionManager(\'' + row.id+ '\',\'' + row.customerid+ '\',\'' + row.othersubsidies+ '\')">'+val+'</a> ';
  }
  function constructionManager(id,customerid,othersubsidies){
	  createpaybusinessbenefits('edit',id,customerid,othersubsidies);
  }
  /*初始化customer*/
  function initCustomers(obj){
	  if(isNull(eachData)){
		  setTimeout(getCustomerDatas(),1000);
	  }
	  $('#customerid'+obj).combobox({
		  data:eachData,
		  valueField:'customerid',
		  textField:'customername'
		  });
  }
  function getCustomerDatas(){
	  $.ajax({
		  type:'post',
		  url:App.ctx+"/paybusinessbenefits/getCustomers",
		  async : false,
		  dataType:'json',
		  success:function (data){
			  eachData=data;
		  }
	  });
  }
  /**
   * 对象判空
   * @param obj
   * @returns {Boolean}
   */
  function isNull(obj){
  	if(typeof(obj) == "undefined" || "" == obj || "undefined" == obj){
  		return true;
  	}else{
  		return false;
  	}
  }
 
