<%@page import="cn.explink.enumutil.ExceptionCwbErrorTypeEnum,cn.explink.enumutil.ExceptionCwbIsHanlderEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum,cn.explink.enumutil.BranchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.Page"%>
<%
List<ExceptionCwb> eclist = (List<ExceptionCwb>)request.getAttribute("eclist");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<User> ulist = (List<User>)request.getAttribute("ulist");
Page page_obj =(Page)request.getAttribute("page_obj");
String cwb=request.getParameter("cwb")==null?"":request.getParameter("cwb");
String endemaildate=request.getParameter("endemaildate")==null?DateTimeUtil.getNowTime():request.getParameter("endemaildate");
String beginemaildate=request.getParameter("beginemaildate")==null?DateTimeUtil.getDateBefore(1):request.getParameter("beginemaildate");
List<Exportmould> exportmouldlist =(List<Exportmould>) request.getAttribute("exportmouldlist");														
Object cwbs =request.getAttribute("cwbs");		
Object ids =request.getAttribute("ids");	
String username=request.getParameter("username")==null?"":request.getParameter("username");		
String starttime=request.getAttribute("beginemaildate").toString();	
String endtime=request.getAttribute("endemaildate").toString();	
Long flowOrderTypeEnumid =(Long)request.getAttribute("flowOrderTypeEnumid");		
List<String> strArrList =request.getAttribute("strArrList")==null?null:(List<String>)request.getAttribute("strArrList");		
 /*String userid =(String)request.getAttribute("userid");	 */	
Long scopeValue =(Long)request.getAttribute("scopeValue");	
System.out.println(scopeValue);
%>
<%
String branchids = "0";
if(!strArrList.isEmpty()) 
      {for(int i=0;i<strArrList.size();i++){
      	branchids = branchids + "," + strArrList.get(i).toString();
      }
   }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script> 
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<title>扫描监控</title>
<script type="text/javascript">
//全选与取消全选多选下拉列表的方法 isSelect为0为取消全选 否则为全选
function multiSelectAll(name,isSelect,text){
	if(isSelect==0){
		$("input[name='"+name+"']").attr('checked', false).parent().removeClass('checked');
	}else{
		$("input[name='"+name+"']").attr('checked', true).parent().addClass('checked');
	}
	var txt = "";//用于存储显示批次文本框的文字
	$("input[name='"+name+"']").each(function(i){
	if($(this).parent()[0].style.display=="none"){
	$(this).parent().attr("class","");
	$(this).attr("checked",false);
	}
	if($(this).parent().attr("class")=="checked"){
	var pici = $(this).parent().html().substring($(this).parent().html().indexOf(">")+1);
	txt = txt+pici+", ";
	}
	});
	$("input[name='"+name+"']").parent().parent().prev().children(".multiSelect_txt").val(txt==""?text:txt);
}

function getSelectBranchForUsers(value){
	var optionstring="";
	if(value!=""){
		$.ajax({
			type: "POST",
			data:{branchid:value},
			url:"<%=request.getContextPath()%>/user/getUsersByBranchids",
			success:function(data){
				//$("#userid").empty();
				//optionstring+="<option value='0'>请选择</option>";
				var comboboxdata=[];
				for(var i=0;i<data.length;i++){
					//optionstring+="<option value='"+data[i].userid+"'>"+data[i].realname+"</option>";
					comboboxdata.push({label:data[i].realname,value:data[i].userid});
				}
				var id=$('#userid');
				$('#userid').combobox('loadData', comboboxdata);
				if($("input.combo-text.validatebox-text.validatebox-f.textbox").val()==0){
					$("input.combo-text.validatebox-text.validatebox-f.textbox").val("");
				}

				//$("#userid").html(optionstring);
			}
		});
	}else{
		$("#userid").empty();
		optionstring+="<option value='0'>请选择</option>";
		
		<%for(User u : ulist){ %>
			optionstring += "<option value=<%=u.getUserid()%>><%=u.getRealname()%></option>";
        <%} %>
        $("#userid").html(optionstring);
    	if($("input.combo-text.validatebox-text.validatebox-f.textbox").val()==0){
    		$("input.combo-text.validatebox-text.validatebox-f.textbox").val("");
    	}

	}
	
}

//输入站点名称，模糊查询到相关的站点并在多选下拉列表中选中
function selectallnexusbranch(path,tagname,branchname){
	if(branchname.length>0){
		$.ajax({
			url:path+"/cwborderPDA/getMoHuBranch",//后台处理程序
			type:"POST",//数据发送方式 
			data:"branchName="+branchname,//参数
			dataType:'json',//接受数据格式
			success:function(data){
				var strArr="";
				for(var j = 0 ; j < data.length ; j++){
					var txt = "";//用于存储显示批次文本框的文字
					$(".multiSelectOptions input[name='"+tagname+"']").each(function(){
						if($(this).val()==data[j].branchid){
							$(this).attr('checked', true).parent().addClass('checked');
							strArr+=data[j].branchid+",";
						}
						if($(this).parent()[0].style.display=="none"){
							$(this).parent().attr("class","");
							$(this).attr("checked",false);
						}
						if($(this).parent().attr("class")=="checked"){
							var pici = $(this).parent().html().substring($(this).parent().html().indexOf(">")+1);
							txt = txt+pici+", ";
						}
					});
					$("input[name='"+tagname+"']").parent().parent().prev().children(".multiSelect_txt").val(txt==""?"请选择":txt);
				}
				
				getSelectBranchForUsers(strArr);
			}
		});
	}
}

$(function(){
	$("#branchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择配送站点' });
	var strArr="";
	//获取已选中的机构
	$(".multiSelectOptions input[name='branchid']").each(function(){
		if($(this).parent().attr("class")=="checked"){
// 	         console.log($(this).val());		
	         strArr+=$(this).val()+",";
		}
	});
	
	strArr = strArr.substring(0,strArr.length-1);
	if(strArr.length!=0){
		var optionstring="";
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/user/getUsersByBranchids",
			data:{branchid:strArr},
			success:function(data){
				var comboboxdata=[];
// 				optionstring+="<option value='0'>请选择</option>";
				for(var i=0;i<data.length;i++){
// 					optionstring+="<option value='"+data[i].userid+"'>"+data[i].realname+"</option>";
					comboboxdata.push({label:data[i].realname,value:data[i].userid});
				}
// 				$("#userid").html(optionstring);
				$('#userid').combobox('loadData', comboboxdata);
				if($("input.combo-text.validatebox-text.validatebox-f.textbox").val()==0){
					$("input.combo-text.validatebox-text.validatebox-f.textbox").val("");
				}

			}
		});
	}
	
	
	$("#beginemaildate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endemaildate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
<%-- 	$("#branchid").change(function(){
		var optionstring="";
		if($(this).val()!=0){
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/user/",
				data:{branchid:$("#branchid").val()},
				success:function(data){
					optionstring+="<option value='0'>请选择</option>";
					for(var i=0;i<data.length;i++){
						optionstring+="<option value='"+data[i].userid+"'>"+data[i].realname+"</option>";
					}
					$("#userid").html(optionstring);
				}
			});
		}else{
			optionstring+="<option value='0'>请选择</option>";
			
			<%for(User u : ulist){ %>
				optionstring += "<option value=<%=u.getUserid()%>><%=u.getRealname()%></option>";
	        <%} %>
	        $("#userid").html(optionstring);
		}
		
	}); --%>
});
var arr="";
function getCheckValue(){
	for(var id=0;id<10000000;id++){
		if($("#ec_"+id).attr("checked")=="checked"){
			arr += $("#ec_"+id).val() + ",";
		}
	}
	if(arr!=""){
		arr = arr.substring(0,arr.length-1);
	}
	return arr;
}
function actionType(src)					
{					
	$("#searchFormFlash").attr("action",src);		
	$("#searchFormFlash").submit();
	}				

</script>
</head>
<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
	<form action="1" method="post" id="searchFormFlash" >
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:80px">
	    <tr>
    <td>
    订单号<input type="text" id="cwb" name="cwb" value="<%=cwb %>" class="input_text1"/>
    </td>
    <td>
    操作机构
    		<input name="branchname" id="branchname" style="height:20px;" class="input_text1" onkeyup="selectallnexusbranch('<%=request.getContextPath()%>','branchid',$(this).val());"/>
			<select name="branchid" id="branchid" class="select1" multiple="multiple">
		        <option value="0">请选择</option>
		        <%for(Branch b : branchlist){ %><%-- <% if(branchid==b.getBranchid()){ %>selected="selected" <% }%> --%>
		           <option  value="<%=b.getBranchid()%>" 
		           		<%if(!strArrList.isEmpty()) 
						            {for(int i=0;i<strArrList.size();i++){
						            	if(b.getBranchid()== new Long(strArrList.get(i).toString())){
						            		%>selected="selected"<%
						            	 break;
						            	}
						            }
							     }%>
		           
		           
		           ><%=b.getBranchname()%></option>
		        <%} %>
	        </select><a href="#" onclick="multiSelectAll('branchid','1','')">[全选]</a><a href="#" onclick="multiSelectAll('branchid','0','请选择配送站点')">[取消全选]</a>
    </td>
    <td>
      扫描类型
				<select name="flowOrderTypeEnumid" id="flowOrderTypeEnumid" class="select1">
			        <option value="0">请选择扫描类型</option>
			        <option value="<%=FlowOrderTypeEnum.TiHuo.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.TiHuo.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.TiHuo.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.RuKu.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.RuKu.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.RuKu.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>"<% if(flowOrderTypeEnumid==FlowOrderTypeEnum.ChuKuSaoMiao.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.ChuKuSaoMiao.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.FenZhanLingHuo.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.FenZhanLingHuo.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.TuiHuoChuZhan.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.TuiHuoChuZhan.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.TuiHuoChuZhan.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.YiFanKui.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.YiFanKui.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.YiFanKui.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.UpdateDeliveryBranch.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getText()%></option>
<%-- 		        	<option value="<%=FlowOrderTypeEnum.BeiZhu.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.BeiZhu.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.BeiZhu.getText()%></option>
 --%>		        	<option value="<%=FlowOrderTypeEnum.GaiDan.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.GaiDan.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.GaiDan.getText()%></option>
		        	
		        <!-- zhili01.liang  货物类型修改需求，添加操作类型过滤条件， =====START==== -->
		        	<option value="<%=FlowOrderTypeEnum.XiuGaiHuoWuLeiXingTongGuo.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.XiuGaiHuoWuLeiXingTongGuo.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.XiuGaiHuoWuLeiXingTongGuo.getText()%></option>
		        	<option value="<%=FlowOrderTypeEnum.XiuGaiHuoWuLeiXingBuTongGuo.getValue()%>" <% if(flowOrderTypeEnumid==FlowOrderTypeEnum.XiuGaiHuoWuLeiXingBuTongGuo.getValue()){ %>selected="selected" <% }%>><%=FlowOrderTypeEnum.XiuGaiHuoWuLeiXingBuTongGuo.getText()%></option>
		        <!-- zhili01.liang  货物类型修改需求，添加操作类型过滤条件， =====END====== -->
		        </select>

    </td>
    </tr>
    <tr>
    <td>
       操作人
			<select name="userid" id="userid" class="easyui-combobox" data-options="valueField:'value',textField:'label'">
		       <option value="0">请选择</option>
		         <%for(User u : ulist){ %><%-- <% if(userid==u.getUserid()){ %>selected="selected" <% }%> --%>
		           <option value="<%=u.getUserid()%>" ><%=u.getRealname()%></option>
		        <%} %> 
	        </select>
    </td>
    <td>
    操作状态
		        <select id="scopeValue" name="scopeValue" class="select1">
		        <option value="-1" <% if(-1==scopeValue){ %>selected="selected" <% }%>>请选择</option>
		        <option value="1"  <% if(1==scopeValue){ %>selected="selected" <% }%>>正常</option>
		        <option value="2"  <% if(2==scopeValue){ %>selected="selected" <% }%>>异常</option>
		        </select>
    </td>
    <td>
    	     操作时间<input type ="text" name ="beginemaildate"  class="input_text1" id ="beginemaildate" value ="<%=beginemaildate %>"/>
		到
		<input type ="text" name= "endemaildate"  class="input_text1" id ="endemaildate" value ="<%=endemaildate %>"/>
    </td>
    </tr>
    <tr>
    	<td>
    		工号查询
    		<input name="username" id="username" class="input_text1" value ="<%=username %>"/>
    	</td>
    </tr>
    <tr>
    <td>
    <input type="button" value="查询" onclick="actionType('1')" class="input_button2"/>
    <%if(eclist != null && eclist.size()>0){%>																	
    <input type="hidden" name="cwbs" id="cwbs" value="<%=cwbs%>" />																																	
    <input type="hidden" name="ids" id="ids" value="<%=ids%>" />																																	
    <input type="hidden" name="starttime" id="starttime" value="<%=starttime%>" />																																	
    <input type="hidden" name="endtime" id="endtime" value="<%=endtime%>" />																																	
    <input type="button" id="btnval0" onclick="actionType('<%=request.getContextPath()%>/cwborderPDA/exportExcle')" value="导出Excel" class="input_button2"/>																	
    <%}%>
    </td>
    </tr>
	</table>		
		<!-- <span><input name="" type="button" value="批量处理" class="input_button1"  onclick="allCheck("<%=request.getContextPath()%>");"/></span> -->
	</form>
	
	</div>
	<div style="height:90px;"></div>
	<div class="right_title">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr class="font_1">
				<!-- <td width="6%" align="center" valign="middle" bgcolor="#eef6ff">选择</td> -->
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">扫描号</td>
				<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">操作机构</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">扫描类型</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">错误类型</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作时间</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作人</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">备注</td>
				
			</tr>
		</table>
		<%if(eclist!=null){for(ExceptionCwb ec : eclist){ %>
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
			<tr>
				<td width="10%" align="center" valign="middle"><%=ec.getCwb() %></td>
				<td width="10%" align="center" valign="middle"><%=ec.getScancwb() %></td>
				<td width="8%" align="center" valign="middle">
					<%for(Branch b : branchlist){if(ec.getBranchid()==b.getBranchid()){ %>
			        <%=b.getBranchname() %>
			        <%}} %>
				</td>
				<td width="10%" align="center" valign="middle">
				<%for(FlowOrderTypeEnum f:FlowOrderTypeEnum.values()){
					if(ec.getScantype()==f.getValue()){%>
						<%=f.getText() %>
					<%}	
				}%>
				</td>
				<td width="10%" align="center" valign="middle">
				<%if(ec.getErrortype().length()>0&&ec.getErrortype().matches("^[0-9]*$")){ %>
					<%for(ExceptionCwbErrorTypeEnum er:ExceptionCwbErrorTypeEnum.values()){
						if(Long.parseLong(ec.getErrortype())==er.getValue()){%>
							<%=er.getText() %>
						<%}
					}%>
				<%}else{ %>
				<%=ec.getErrortype()%>
				<%} %>
				</td>
				<td width="10%" align="center" valign="middle"><%=ec.getCreatetime() %></td>
				<td width="10%" align="center" valign="middle">
				<%for(User u : ulist){
					if(ec.getUserid()==u.getUserid()){%>
					<%=u.getRealname() %>
				<% }
				}%>
				</td>
				<td width="10%" align="center" valign="middle"><%=ec.getRemark() %></td>
				
			</tr>
		</table>
		<%}}%>
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>
	</div>
	<%if(page_obj.getMaxpage()>1){ %>
			<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:$('#searchFormFlash').attr('action','1');$('#searchFormFlash').submit();" >第一页</a>　
					<a href="javascript:$('#searchFormFlash').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchFormFlash').submit();">上一页</a>　
					<a href="javascript:$('#searchFormFlash').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchFormFlash').submit();" >下一页</a>　
					<a href="javascript:$('#searchFormFlash').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchFormFlash').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchFormFlash').attr('action',$(this).val());$('#searchFormFlash').submit()">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>"><%=i %></option>
							<% } %>
						</select>页
				</td>
			</tr> 
			</table>
			</div>
		<%} %>
</div>			
	<div class="jg_10"></div>
	<div class="clear"></div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#flowOrderTypeEnumid").val(<%=request.getParameter("flowOrderTypeEnumid")%>);
<%-- $("#branchid").val(<%=request.getParameter("branchid")%>); --%>
$("#userid").val(<%=request.getParameter("userid")%>);
<%-- $("#username").val(<%=request.getParameter("username")%>); --%>
$(function(){
	$("div.panel.combo-p").css({"top":"50px"});
// 	$("#span.combo-arrow").css({"margin-right":"-18px"});
	$("span.combo-arrow").css({"margin-right":"-18","margin-top":"-20px"});
	$("div.combo-panel.panel-body.panel-body-noheader").css({"width":"152px"});
	if($("input.combo-text.validatebox-text.validatebox-f.textbox").val()==0){
		$("input.combo-text.validatebox-text.validatebox-f.textbox").val("");
	}
// 	margin-top: -20px;
})
</script>
</body>
</html>