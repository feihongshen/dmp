<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<SmsConfigModel>  smsList =(List<SmsConfigModel>) request.getAttribute("smsList");
Map<Long , List<String>> customerMap = (Map) request.getAttribute("customerMap");
Map<Long , List<String>> branchMap = (Map) request.getAttribute("branchMap");
String ifUpdate =  request.getAttribute("ifUpdate")==null?"" : request.getAttribute("ifUpdate").toString();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>短信设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>

<script>
$(document).ready(function() {
	   // Options displayed in comma-separated list
	   if(<%=smsList!=null && smsList.size()>0%>){
	   <%for(SmsConfigModel sms : smsList){ %>
	   $("#branchids<%=sms.getFlowordertype() %>").multiSelect({ oneOrMoreSelected: '*',noneSelected:'站点' });
	   $("#customerids<%=sms.getFlowordertype() %>").multiSelect({ oneOrMoreSelected: '*',noneSelected:'供应商' });
	   <%}%>
	   }
	   if(<%="1".equals(ifUpdate)%>){
		   alert("修改成功！");
	   }else if(<%="0".equals(ifUpdate)%>){
		   alert("修改失败！");
	   }
	});
	
function updateConfig(id){
	var params=$('#updateBranchForm'+id).serialize();
	if(confirm("确定要修改？")){
	$.ajax({
		url:"<%=request.getContextPath()%>/smsconfigmodel/update1",//后台处理程序
		type:"POST",//数据发送方式 
		data:params,//参数
		dataType:'json',//接受数据格式
		success:function(json){
			
			}
		   
	});
	var param2=$('#updateCustomerForm'+id).serialize();
	$.ajax({
		url:"<%=request.getContextPath()%>/smsconfigmodel/update2",//后台处理程序
		type:"POST",//数据发送方式 
		data:param2,//参数
		dataType:'json',//接受数据格式
		success:function(json){
			
			}
		   
	});
	$.ajax({
		url:"<%=request.getContextPath()%>/smsconfigmodel/update",//后台处理程序
		type:"POST",//数据发送方式 
		data:{
			flowordertype:id,
			money:$("#money"+id).val(),
			templatecontent:$("#templatecontent"+id).val()
		},
		dataType:'json',//接受数据格式
		success:function(json){
			alert("修改成功！");
			}
		   
	});
	}
}	
function delConfig(id){
	if(confirm("确定要删除该条设置？")){
	$.ajax({
		url:"<%=request.getContextPath()%>/smsconfigmodel/del",//后台处理程序
		type:"POST",//数据发送方式 
		data:{
			flowordertype:id
		},
		dataType:'json',//接受数据格式
		success:function(json){
			alert("删除成功！");
			 $("#tableAdd").children().find("#tr"+id).remove();
			}
		   
	});
	}
}	

function showAll(){
	$("#showAllForm").submit();
}
</script>

</head>


<body style="background:#f5f5f5">
<div class="right_box">
	<div class="right_title">
		
		<table width="100%" border="0" cellspacing="1" cellpadding="6" class="table_5" id="tableAdd">
			<tr class="font_1">
			<td width="10%" align="center" valign="middle">操作节点</td>
			<td width="20%" align="center" valign="middle">站点</td>
			<td width="20%" align="center" valign="middle">供应商</td>
			<td width="10%" align="center" valign="middle">金额</td>
			<td width="10%" align="center" valign="middle">模板</td>
			<td width="10%" align="center" valign="middle">操作人</td>
			<td width="10%" align="center" valign="middle">最后更新时间</td>
			<td width="10%" align="center" valign="middle">操作</td>
			</tr>
		
			
			<%if(smsList!=null && smsList.size()>0){%>
			<%for(SmsConfigModel sms : smsList){ %>
			
			<tr id="tr<%=sms.getFlowordertype()%>">
			
				<td width="10%" align="right" valign="middle">
				<%if(sms.getFlowordertype()<100){%><%=FlowOrderTypeEnum.getText(sms.getFlowordertype()).getText() %><%}
				else if(sms.getFlowordertype()==100){
					%>
					站点返款结算（支付平台充值）
					<%
				}
				else{ %>
				<%=DeliveryStateEnum.getByValue((int)sms.getFlowordertype()-100).getText() %>
				<%} %>
				
				</td>
				<td width="20%" align="left" valign="middle">
				
				<%if(sms.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()||
		        sms.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()||sms.getFlowordertype()>100 ){ %>
		        <form action="" id="updateBranchForm<%=sms.getFlowordertype() %>">
		        <input type="hidden" name="flowordertype" value="<%=sms.getFlowordertype() %>"/>
							<select name ="branchids" id ="branchids<%=sms.getFlowordertype() %>" multiple="multiple" style="width: 180px;">
               	<%for(Branch b : branchList){ %>
		          <option value =<%=b.getBranchid() %> 
		                   <%if(!branchMap.isEmpty()) 
						            {for(int i=0;i< branchMap.get(sms.getFlowordertype()).size();i++){
						            	if(b.getBranchid()== new Long(branchMap.get(sms.getFlowordertype())==null?"0":
						            	    (branchMap.get(sms.getFlowordertype()).get(i)==null?"0":
						            		(branchMap.get(sms.getFlowordertype()).get(i).toString().equals("")?"0":branchMap.get(sms.getFlowordertype()).get(i).toString())
						            			))){
						            		%>selected="selected"<%
						            	 break;
						            	}
						            }
					     }%>><%=b.getBranchname()%></option>
					<%} %>
		        </select>
		        </form>
		         <%} %>
		         
		        </td>
		        <td width="20%" align="right" valign="middle">
		        <form action="" id="updateCustomerForm<%=sms.getFlowordertype() %>">
		        <input type="hidden" name="flowordertype" value="<%=sms.getFlowordertype() %>"/>
		        	<select name ="customerids" id ="customerids<%=sms.getFlowordertype() %>" multiple="multiple" style="width: 180px;">
               	<%for(Customer c : customerlist){ %>
		          <option value =<%=c.getCustomerid() %> 
		                   <%if(!customerMap.isEmpty()) 
						            {for(int i=0;i< customerMap.get(sms.getFlowordertype()).size();i++){
						            	if(c.getCustomerid()== new Long(customerMap.get(sms.getFlowordertype())==null?"0":
						            	(customerMap.get(sms.getFlowordertype()).get(i)==null?"0":
						            	    (customerMap.get(sms.getFlowordertype()).get(i).toString().equals("")?"0":customerMap.get(sms.getFlowordertype()).get(i).toString())
						            	    ))){
						            		%>selected="selected"<%
						            	 break;
						            	}
						            }
					     }%>><%=c.getCustomername()%></option>
					<%} %>
		        </select>
		        </form>
		        </td>
		        <td width="10%" align="right" valign="middle">
						<select name="money" id="money<%=sms.getFlowordertype() %>">
							<option value='-1'>金额</option>
							<option value='100' <%if(sms.getMoney().longValue()==100){ %>selected="selected"<%} %> >100元</option>
							<option value='200' <%if(sms.getMoney().longValue()==200){ %>selected="selected"<%} %>>200元</option>
							<option value='500' <%if(sms.getMoney().longValue()==500){ %>selected="selected"<%} %>>500元</option>
							<option value='1000' <%if(sms.getMoney().longValue()==1000){ %>selected="selected"<%} %>>1000元</option>
							<option value='5000' <%if(sms.getMoney().longValue()==5000){ %>selected="selected"<%} %>>5000元</option>
					    </select></td>
		        <td width="10%" align="left" valign="middle">
		        <textarea name="templatecontent" cols="70" rows="2" id="templatecontent<%=sms.getFlowordertype() %>"><%=sms.getTemplatecontent() %></textarea>
		        </td>
		        <td width="10%" align="center" valign="middle"><%=sms.getUsername() %></td>
		        <td width="10%" align="center" valign="middle"><%=sms.getCreateTime() %></td>
		        <td width="10%" align="center" valign="middle">
		        <input type ="submit"  value="修改" class="input_button2"  onclick="updateConfig('<%=sms.getFlowordertype() %>');"/>
		        <input type ="button"  value="删除" class="input_button2"  onclick="delConfig('<%=sms.getFlowordertype() %>');"/>
		        
		        </td>
				
				</tr>
					
				
			<%} 
			}%>
	   
			<tr class="font_1">
			<td  colspan="8" align="center" valign="middle">
			<form action="<%=request.getContextPath()%>/smsconfigmodel/setsmsview" method="post">
			<input type ="submit" id="btnval" value="返回" class="input_button2"/>
			</form>
			</td>
			</tr>
		</table>
		
	</div>
<form action="<%=request.getContextPath()%>/smsconfigmodel/showAll" method="post" id="showAllForm">
</form>

</div>
<!-- 删除的ajax地址 -->
</body>
</html>

