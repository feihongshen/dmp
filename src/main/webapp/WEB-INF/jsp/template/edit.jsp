<%@page import="cn.explink.print.template.*"%>
<%@page import="cn.explink.enumutil.PrintTemplateOpertatetypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
PrintTemplate pt = (PrintTemplate)request.getAttribute("printtemplate");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>交接单模版设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script type="text/javascript">
jQuery.extend({      
    /** 
    * @see 将javascript数据类型转换为json字符串   
    * @param 待转换对象,支持  object,array,string,function,number,boolean,regexp   
    * @return 返回json字符串 
    */     
    toJSON:function(object){        
        var type = typeof object;        
        if ('object' == type) {          
            if (Array == object.constructor) 
                type = 'array';          
            else if (RegExp == object.constructor)   
                type = 'regexp';          
            else
                type = 'object';        
        }        
        switch (type) {        
            case 'undefined':        
            case 'unknown':          
                return;           
            case 'function':        
            case 'boolean':        
            case 'regexp':          
                return object.toString();          
            case 'number':          
                return isFinite(object) ?   object.toString() : 'null';          
            case 'string':          
                return '"' + object.replace(/(|")/g, "$1").replace(/n|r|t/g, function(){            
                            var a = arguments[0];           
                            return (a == 'n') ? 'n': (a == 'r') ? 'r': (a == 't') ? 't': ""         
                        }) + '"';          
            case 'object':          
                if (object === null) 
                    return 'null';          
                var results = [];  
                for (var property in object) {            
                    var value = jQuery.toJSON(object[property]);            
                    if (value !== undefined) results.push(jQuery.toJSON(property) + ':' + value);          
                }          
                return '{' + results.join(',') + '}';          
            case 'array':          
                var results = [];          
                for (var i = 0; i < object.length; i++) {            
                    var value = jQuery.toJSON(object[i]);            
                    if (value !== undefined) results.push(value);         
                }          
                return '[' + results.join(',') + ']';         
        }      
    }    
});
function show(){
	$("#columnSetting input[field=index]").parent().parent().hide();
	$("#columnSetting input[field=index]").attr("checked",false);
	$("#columnSetting input[field=transcwb]").parent().parent().hide();
	$("#columnSetting input[field=transcwb]").attr("checked",false);
	$("#columnSetting input[field=startbranchid]").parent().parent().hide();
	$("#columnSetting input[field=startbranchid]").attr("checked",false);
	$("#columnSetting input[field=backintotime]").parent().parent().hide();
	$("#columnSetting input[field=backintotime]").attr("checked",false);
	$("#columnSetting input[field=backreason]").parent().parent().hide();
	$("#columnSetting input[field=backreason]").attr("checked",false);
	$("#columnSetting input[field=driver]").parent().parent().hide();
	$("#columnSetting input[field=driver]").attr("checked",false);
}
$(function(){
	show();
	if(<%=pt.getOpertatetype()%>%2==0){
		$('#num').hide();
	}else{
		$('#num').show();
	}
	if(<%=pt.getOpertatetype()%>==<%=PrintTemplateOpertatetypeEnum.TuiGongYingShangChuKuAnDan.getValue() %>){
		$("#opreator").show();
		$("#columnSetting input[field=opreator]").attr("checked",false);
	}else{
		$("#opreator").hide();
		$("#columnSetting input[field=opreator]").attr("checked",false);
	}
	if(<%=pt.getOpertatetype()%>==<%=PrintTemplateOpertatetypeEnum.ChuKuHuiZong.getValue() %>){
		$("#cwbcount").show();
		$("#columnSetting input[field=cwbcount]").attr("checked",false);
	}else{
		$("#cwbcount").hide();
		$("#columnSetting input[field=cwbcount]").attr("checked",false);
	}
	
	
	if(<%=pt.getOpertatetype()%>%2==0){
		$("#columnSetting input[field=cwb]").parent().parent().hide();
		$("#columnSetting input[field=cwb]").attr("checked",false);
		$("#columnSetting input[field=cwbordertypeid]").parent().parent().hide();
		$("#columnSetting input[field=cwbordertypeid]").attr("checked",false);
		$("#columnSetting input[field=consigneename]").parent().parent().hide();
		$("#columnSetting input[field=consigneename]").attr("checked",false);
		$("#columnSetting input[field=consigneeaddress]").parent().parent().hide();
		$("#columnSetting input[field=consigneeaddress]").attr("checked",false);
		$("#columnSetting input[field=consigneepostcode]").parent().parent().hide();
		$("#columnSetting input[field=consigneepostcode]").attr("checked",false);
		$("#columnSetting input[field=consigneemobile]").parent().parent().hide();
		$("#columnSetting input[field=consigneemobile]").attr("checked",false);
		$("#columnSetting input[field=consigneephone]").parent().parent().hide();
		$("#columnSetting input[field=consigneephone]").attr("checked",false);
		$("#columnSetting input[field=carrealweight]").parent().parent().hide();
		$("#columnSetting input[field=carrealweight]").attr("checked",false);
		$("#columnSetting input[field=carrealweight]").parent().parent().hide();
		$("#columnSetting input[field=carrealweight]").attr("checked",false);
		$("#columnSetting input[field=paywayid]").parent().parent().hide();
		$("#columnSetting input[field=paywayid]").attr("checked",false);
		$("#columnSetting input[field=cwbremark]").parent().parent().hide();
		$("#columnSetting input[field=cwbremark]").attr("checked",false);
		$("#columnSetting input[field=carsize]").parent().parent().hide();
		$("#columnSetting input[field=carsize]").attr("checked",false);
		
	}else{
		$("#columnSetting input[field=cwb]").parent().parent().show();
		$("#columnSetting input[field=cwb]").attr("checked",false);
		$("#columnSetting input[field=consigneename]").parent().parent().show();
		$("#columnSetting input[field=consigneename]").attr("checked",false);
		$("#columnSetting input[field=consigneeaddress]").parent().parent().show();
		$("#columnSetting input[field=consigneeaddress]").attr("checked",false);
		$("#columnSetting input[field=consigneepostcode]").parent().parent().show();
		$("#columnSetting input[field=consigneepostcode]").attr("checked",false);
		$("#columnSetting input[field=consigneemobile]").parent().parent().show();
		$("#columnSetting input[field=consigneemobile]").attr("checked",false);
		$("#columnSetting input[field=consigneephone]").parent().parent().show();
		$("#columnSetting input[field=consigneephone]").attr("checked",false);
		$("#columnSetting input[field=carrealweight]").parent().parent().show();
		$("#columnSetting input[field=carrealweight]").attr("checked",false);
		$("#columnSetting input[field=carrealweight]").parent().parent().show();
		$("#columnSetting input[field=carrealweight]").attr("checked",false);
		$("#columnSetting input[field=paywayid]").parent().parent().show();
		$("#columnSetting input[field=paywayid]").attr("checked",false);
		$("#columnSetting input[field=cwbremark]").parent().parent().show();
		$("#columnSetting input[field=cwbremark]").attr("checked",false);
		$("#columnSetting input[field=carsize]").parent().parent().show();
		$("#columnSetting input[field=carsize]").attr("checked",false);
	}
	if(<%=pt.getOpertatetype()%>==<%=PrintTemplateOpertatetypeEnum.Tuihuozhanrukumingxi.getValue() %>){
		$(":checkbox").parent().parent().hide();
		$("#num").hide();
		$("#columnSetting input[field=index]").parent().parent().show();
		$("#columnSetting input[field=index]").attr("checked",false);
		$("#columnSetting input[field=cwb]").parent().parent().show();
		$("#columnSetting input[field=cwb]").attr("checked",false);
		$("#columnSetting input[field=transcwb]").parent().parent().show();
		$("#columnSetting input[field=transcwb]").attr("checked",false);
		$("#columnSetting input[field=customername]").parent().parent().show();
		$("#columnSetting input[field=customername]").attr("checked",false);
		$("#columnSetting input[field=startbranchid]").parent().parent().show();
		$("#columnSetting input[field=startbranchid]").attr("checked",false);
		$("#columnSetting input[field=opreator]").parent().parent().show();
		$("#columnSetting input[field=opreator]").attr("checked",false);
		$("#columnSetting input[field=backintotime]").parent().parent().show();
		$("#columnSetting input[field=backintotime]").attr("checked",false);
		$("#columnSetting input[field=backreason]").parent().parent().show();
		$("#columnSetting input[field=backreason]").attr("checked",false);
		$("#columnSetting input[field=driver]").parent().parent().show();
		$("#columnSetting input[field=driver]").attr("checked",false);
	}
	
	$("#opertatetype").change(function(){
		show();
		if($(this).val()==<%=PrintTemplateOpertatetypeEnum.TuiGongYingShangChuKuAnDan.getValue() %>){
			$("#opreator").show();
			$("#columnSetting input[field=opreator]").attr("checked",false);
		}else{
			$("#opreator").hide();
			$("#columnSetting input[field=opreator]").attr("checked",false);
		}
		
		if($(this).val()==<%=PrintTemplateOpertatetypeEnum.ChuKuHuiZong.getValue() %>){
			$("#cwbcount").show();
			$("#columnSetting input[field=cwbcount]").attr("checked",false);
		}else{
			$("#cwbcount").hide();
			$("#columnSetting input[field=cwbcount]").attr("checked",false);
		}
		if($(this).val()%2==0){
			$("#columnSetting input[field=cwb]").parent().parent().hide();
			$("#columnSetting input[field=cwb]").attr("checked",false);
			$("#columnSetting input[field=cwbordertypeid]").parent().parent().show();
			$("#columnSetting input[field=cwbordertypeid]").attr("checked",false);
			$("#columnSetting input[field=cwbordertypeid]").parent().parent().hide();
			$("#columnSetting input[field=cwbordertypeid]").attr("checked",false);
			$("#columnSetting input[field=consigneename]").parent().parent().hide();
			$("#columnSetting input[field=consigneename]").attr("checked",false);
			$("#columnSetting input[field=consigneeaddress]").parent().parent().hide();
			$("#columnSetting input[field=consigneeaddress]").attr("checked",false);
			$("#columnSetting input[field=consigneepostcode]").parent().parent().hide();
			$("#columnSetting input[field=consigneepostcode]").attr("checked",false);
			$("#columnSetting input[field=consigneemobile]").parent().parent().hide();
			$("#columnSetting input[field=consigneemobile]").attr("checked",false);
			$("#columnSetting input[field=consigneephone]").parent().parent().hide();
			$("#columnSetting input[field=consigneephone]").attr("checked",false);
			$("#columnSetting input[field=carrealweight]").parent().parent().hide();
			$("#columnSetting input[field=carrealweight]").attr("checked",false);
			$("#columnSetting input[field=carrealweight]").parent().parent().hide();
			$("#columnSetting input[field=carrealweight]").attr("checked",false);
			$("#columnSetting input[field=paywayid]").parent().parent().hide();
			$("#columnSetting input[field=paywayid]").attr("checked",false);
			$("#columnSetting input[field=cwbremark]").parent().parent().hide();
			$("#columnSetting input[field=cwbremark]").attr("checked",false);
			$("#columnSetting input[field=carsize]").parent().parent().hide();
			$("#columnSetting input[field=carsize]").attr("checked",false);
		}else{
			$("#columnSetting input[field=cwb]").parent().parent().show();
			$("#columnSetting input[field=cwb]").attr("checked",false);
			$("#columnSetting input[field=cwbordertypeid]").parent().parent().show();
			$("#columnSetting input[field=cwbordertypeid]").attr("checked",false);
			$("#columnSetting input[field=consigneename]").parent().parent().show();
			$("#columnSetting input[field=consigneename]").attr("checked",false);
			$("#columnSetting input[field=consigneeaddress]").parent().parent().show();
			$("#columnSetting input[field=consigneeaddress]").attr("checked",false);
			$("#columnSetting input[field=consigneepostcode]").parent().parent().show();
			$("#columnSetting input[field=consigneepostcode]").attr("checked",false);
			$("#columnSetting input[field=consigneemobile]").parent().parent().show();
			$("#columnSetting input[field=consigneemobile]").attr("checked",false);
			$("#columnSetting input[field=consigneephone]").parent().parent().show();
			$("#columnSetting input[field=consigneephone]").attr("checked",false);
			$("#columnSetting input[field=carrealweight]").parent().parent().show();
			$("#columnSetting input[field=carrealweight]").attr("checked",false);
			$("#columnSetting input[field=carrealweight]").parent().parent().show();
			$("#columnSetting input[field=carrealweight]").attr("checked",false);
			$("#columnSetting input[field=paywayid]").parent().parent().show();
			$("#columnSetting input[field=paywayid]").attr("checked",false);
			$("#columnSetting input[field=cwbremark]").parent().parent().show();
			$("#columnSetting input[field=cwbremark]").attr("checked",false);
			$("#columnSetting input[field=carsize]").parent().parent().show();
			$("#columnSetting input[field=carsize]").attr("checked",false);
		}
		if($(this).val()==<%=PrintTemplateOpertatetypeEnum.Tuihuozhanrukumingxi.getValue() %>){
			$(":checkbox").parent().parent().hide();
			$("#num").hide();
			$("#columnSetting input[field=index]").parent().parent().show();
			$("#columnSetting input[field=index]").attr("checked",false);
			$("#columnSetting input[field=cwb]").parent().parent().show();
			$("#columnSetting input[field=cwb]").attr("checked",false);
			$("#columnSetting input[field=transcwb]").parent().parent().show();
			$("#columnSetting input[field=transcwb]").attr("checked",false);
			$("#columnSetting input[field=customername]").parent().parent().show();
			$("#columnSetting input[field=customername]").attr("checked",false);
			$("#columnSetting input[field=startbranchid]").parent().parent().show();
			$("#columnSetting input[field=startbranchid]").attr("checked",false);
			$("#columnSetting input[field=opreator]").parent().parent().show();
			$("#columnSetting input[field=opreator]").attr("checked",false);
			$("#columnSetting input[field=backintotime]").parent().parent().show();
			$("#columnSetting input[field=backintotime]").attr("checked",false);
			$("#columnSetting input[field=backreason]").parent().parent().show();
			$("#columnSetting input[field=backreason]").attr("checked",false);
			$("#columnSetting input[field=driver]").parent().parent().show();
			$("#columnSetting input[field=driver]").attr("checked",false);
		}
	});
	
	
	$.each(<%=pt.getDetail()%>,function(index,obj){
		$("#columnSetting input[type=checkbox]").each(function(i){
			if($("#columnSetting input[type=checkbox]").eq(i).attr("field")==obj.field){
				$("#columnSetting tr").eq(i+1).find("input[type=text]").val(obj.width);
				$("#columnSetting tr").eq(i+1).find("input[type=checkbox]").attr("checked","checked");
			}
		});
	});
	
	$("#save").click(function(){
		var columnSetting=[];
		i=0;
		$("#columnSetting input[type=checkbox]").each(function(){
			if($(this).attr("checked")){
				var tr=$(this).parents("tr");
				var columnName = $(this).attr("columnName");
				var field = $(this).attr("field");
				var width = tr.find("input[type=text]").val().replace(/(^\s*)|(\s*$)/g, "");
				if(!isFloat(width)){
					alert("宽度只能为数字");
					return false;
				}else if(width.length<=0){
					alert("宽度不为空");
					return false;
				}else if(width.length>8){
					alert("宽度不超过8位");
					return false;
				}else{
					columnSetting[i++]={width:width,columnName:columnName,field:field};
				}
			}
		});
		
		if($("#name").val().length==0){
			alert("模版名称不能为空");
			return false;
		}else if($("#opertatetype").val()==0){
			alert("交接单类型不能为空");
			return false;
		}else if(columnSetting.length==0){
			alert("模版内容不能为空");
			return false;
		} else if(columnSetting.length!=$("#columnSetting input[type=checkbox]:checked").length){
			alert("请认真填写宽度");
			return false;
		}else{
			$.ajax({
				url:"<%=request.getContextPath()%>/printtemplate/save/<%=pt.getId()%>",
				type:"POST",
				data:{
					detail:$.toJSON(columnSetting),
					name:$("#name").val(),
					customname:$("#customname").val(),
					shownum:$("#shownum").val(),
					opertatetype:$("#opertatetype").val()
				},
				dataType:"json",
				success:function(data){
					alert(data.error);
				}
			});
		}
	});
	
	$("#columnSetting button.upButton").click(function(){
		var tr=$(this).parents("tr");
		var prev=tr.prev();
		if(prev!=null){
			tr.insertBefore(prev);
		}
	});
	$("#columnSetting button.downButton").click(function(){
		var tr=$(this).parents("tr");
		var next=tr.next();
		if(next!=null){
			tr.insertAfter(next);
		}
	});
});
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="list_topbar">
		<div style="border:1px solid #6CF; border-bottom-width:0; background:#ebf1b1; height:30px">
			表头前缀：<input type="text" id="customname" name="customname" value="<%=pt.getCustomname()%>"/>
			模版名称：<input type="text" id="name" name="name" value="<%=pt.getName()%>"/>
			交接单类型：
				<select id="opertatetype" name="opertatetype" onchange="if($(this).val()%2==0){$('#num').hide();}else{$('#num').show();}">
					<option value="0">请选择</option>
					<%for(PrintTemplateOpertatetypeEnum ptot : PrintTemplateOpertatetypeEnum.values()){ %>
						<option value="<%=ptot.getValue()%>" <%if(pt.getOpertatetype()==ptot.getValue()){ %>selected<%} %>><%=ptot.getText() %></option>
					<%} %>
				</select>
			<font id="num"  style="display: none;">每行单数：
				<select id="shownum" name="shownum">
					<option value="1" <%if(pt.getShownum()==1){ %>selected<%} %>>1</option>
					<option value="2" <%if(pt.getShownum()==2){ %>selected<%} %>>2</option>
					<option value="3" <%if(pt.getShownum()==3){ %>selected<%} %>>3</option>
				</select>
			</font>
		</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="33%"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
						<td width="50" align="center" valign="middle" bgcolor="#f3f3f3">选择</td>
						<td align="center" valign="middle" bgcolor="#F3F3F3">名称</td>
						<td width="100" align="center" valign="middle" bgcolor="#F3F3F3">宽度</td>
					</tr>
				</table></td>
				<td width="33%"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
						<td width="50" align="center" valign="middle" bgcolor="#f3f3f3">选择</td>
						<td align="center" valign="middle" bgcolor="#F3F3F3">名称</td>
						<td width="100" align="center" valign="middle" bgcolor="#F3F3F3">宽度</td>
					</tr>
				</table></td>
				<td>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
						<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
							<td width="50" align="center" valign="middle" bgcolor="#f3f3f3">选择</td>
							<td align="center" valign="middle" bgcolor="#F3F3F3">名称</td>
							<td width="100" align="center" valign="middle" bgcolor="#F3F3F3">宽度</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</div>
		<div style="height:60px"></div>
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0"  id="columnSetting">
<tr>
				<td width="33%">
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
						<tbody>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="cwb" columnName="订单号"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>订单号</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100" size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
								<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="transcwb" columnName="运单号"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>运单号</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="customername" columnName="供货商"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>供货商</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100" size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="cwbordertypeid" columnName="订单类型"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>订单类型</strong></td>
								<td align="center" valign="middle"><input name="textfield2" type="text" id="textfield2" value="100" size="10"/>cm</td>
							</tr>
							<tr id="cwbcount" style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="cwbcount" columnName="订单数量"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>订单数量</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="sendcarnum" columnName="发货数量"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>发货数量</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="consigneename" columnName="收件人"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>收件人</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="consigneeaddress" columnName="地址"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>地址</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
						
						</tbody>
					</table>
				</td>
				<td width="33%">
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
						<tbody>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="consigneepostcode" columnName="邮编"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>邮编</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="consigneemobile" columnName="手机"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>手机</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="consigneephone" columnName="电话"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>电话</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="carrealweight" columnName="重量"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>重量</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							
							
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="backcarnum" columnName="取货数量"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>取货数量</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox" field="caramount" columnName="货物金额"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>货物金额</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							
								<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="startbranchid" columnName="上一站"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>上一站</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
						
						</tbody>
					</table>
				</td>
				<td>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
						<tbody>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="carsize" columnName="尺寸"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>尺寸</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox" field="receivablefee" columnName="应收金额"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>应收金额</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox" field="paybackfee" columnName="应退金额"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>应退金额</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="paywayid" columnName="支付方式"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>支付方式</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr id="opreator" style="background-color: rgb(249, 252, 253);display:none">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="opreator" columnName="操作人"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>操作人</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="cwbremark" columnName="备注"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>备注</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
												<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="backintotime" columnName="退货站入库时间"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>退货站入库时间</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
								<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="backreason" columnName="退货原因"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>退货原因</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td width="50" align="center" valign="middle" bgcolor="#f3f3f3"><input type="checkbox"  field="driver" columnName="司机"/></td>
								<td align="center" valign="middle" bgcolor="#EEF6FF"><strong>司机</strong></td>
								<td width="100" align="center" valign="middle"><input type="text" value="100"  size="10"/>cm</td>
								<!-- <td><button class="upButton">上移</button><button class="downButton">下移</button></td> -->
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</table>
	<table width="100%" border="0" cellpadding="10" cellspacing="0" class="table_5">
		<tbody>
			<tr >
				<td align="center" valign="middle">
					<input type="button" class="input_button1" id="save" value="保存"/>
					<input type="button"  onclick="location.href='<%=request.getContextPath()%>/printtemplate/testprinting_default/<%=pt.getId() %>'" value="直接预览" class="input_button1" />
					<input type="button"  onclick="location.href='<%=request.getContextPath()%>/printtemplate/list/1'" value="返回" class="input_button1" />
				</td>
			</tr>
		</tbody>
	</table>
</div>
</body>
</html>
