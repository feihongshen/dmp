<%@page import="cn.explink.domain.User,cn.explink.domain.addressvo.DelivererRuleVo"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> deliverList = (List<User>)request.getAttribute("delivererList");
List<DelivererRuleVo> ruleList = (List<DelivererRuleVo>)request.getAttribute("delivererRuleVoList");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配送站点关联维护</title>
<script type="text/javascript">
var cxt='<%=request.getContextPath()%>';
</script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/easyui/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/easyui/themes/icon.css" />
<link rel="stylesheet" type="text/css "href="${pageContext.request.contextPath}/css/index.css" type="text/css"  />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/zTree/zTreeStyle/zTreeStyle.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/zTree/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/zTree/js/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/address/getZAddress.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/js.js"></script> 

<script type="text/javascript">
var backNode;
$(document).ready(function(){
	 $("#saveRule").click(function(){				
		 if(!addressId){
			 alert("请选择地址");
			 return;
		 }
		 saveRule();
		 		
	});
	getAll();
	$("#collapseAllBtn").bind("click", {type:"collapseAll"}, expandNode);
    $("#refreshAllBtn").click(function(){
    	//alert(addressId);
   	    refreshtree();
    });
    $("#add").click(function(){
   	    backNode=$("#deliveryDeilvererRule").clone(true);
      	$("#optionRule").before(backNode);
    });
   
    $(".delOneRow").click(function() {  
	   	if ($(".deliveryDeilvererRule").length < 2) { 
	   		$(".addresstishi_box").html("至少保留一行!");
			$(".addresstishi_box").show();
			setTimeout("$(\".addresstishi_box\").hide(1000)", 2000);
	   	} else { 
	  		$(this).parent().parent().remove(); 
	   	} 
    }); 
    $("#rulelist").on('click', ".delOneRowList", function () {
  		if (confirm("确认删除?")) { 
  		    $.ajax({
			   type: "POST",
			url:cxt+"/addressdelivertostation/deletedelivererrule",
			data:{"deilvererRuleId":$(this).prev().val()},
			dataType:"json",
			success:function(data){	
				$(".addresstishi_box").html(data.error);
				$(".addresstishi_box").show();
				setTimeout("$(\".addresstishi_box\").hide(1000)", 2000);
			}
			});
			$(this).parent().parent().remove(); 
		 } 
   	
   }); 	       
});
		 
		 
		 
		
</script>

</head>
<body>
<div class="easyui-layout" style="height:600px;">
  <div data-options="region:'west',split:true" title="条件搜索" style="width:330px;">
      <table width="100%" border="0" cellspacing="0" cellpadding="10">
        <tr>
          <td><input style="width:150px" id="searchA" onkeydown="searchVal('searchA','tree')"/>
            </td>
        </tr>
        <tr>
          <td><a href="javascript:void(0)" id="collapseAllBtn" class="easyui-linkbutton">全部折叠</a>&nbsp;
          <a href="javascript:void(0)" id="refreshAllBtn" class="easyui-linkbutton">刷新节点</a>&nbsp;
         <!--  <a href="javascript:void(0)" id="unbindAllBtn" class="easyui-linkbutton">未绑定</a></td> -->
        </tr>
        <tr>
          <td><ul id="tree" class="ztree " style="width:auto;height:auto; overflow:auto;"></ul></td>
        </tr>
      </table>
  </div>
  
  <div class="addresstishi_box"></div>
  
  <div data-options="region:'center',title:'配送员关联维护'">
    <form action="" method="get" >
      <table width="100%" border="0" cellspacing="0" cellpadding="10" id="stationList">
        </table>
    <div style="padding:10px">
    <table width="100%" border="0" cellpadding="8" cellspacing="1" bgcolor="#CCCCCC" id="rulelist" class="table_2">
    <thead>
      <tr class="font_1">
        <th align="center" bgcolor="#f1f1f1">配送员</th>
        <th align="center" bgcolor="#f1f1f1">规则</th>
        <th align="center" bgcolor="#f1f1f1">操作</th>
      </tr>
      
      <tr id="deliveryDeilvererRule" class="deliveryDeilvererRule">
        <td align="center" bgcolor="#FFFFFF"><select class="deliveryDeilvererId" id="deliveryDeilvererId" name="deliveryDeilvererId" style="width:200px;">
         <option value="" >请选择</option>
         <%for(User u : deliverList){ %>
			 <option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
		 <%} %>
        </select>&nbsp&nbsp*</td>
        <td align="center" bgcolor="#FFFFFF">
          <input type="text" name="rule" class="rule" id="rule" />&nbsp&nbsp*
        </td>
        <td align="center" bgcolor="#FFFFFF">
          <input type="button" class="delOneRow" value="删除" />
        </td>
      </tr>
      
      <tr id="optionRule">
        <td colspan="3" bgcolor="#FFFFFF"><a href="javascript:void(0)" id="saveRule" class="easyui-linkbutton">保存</a>
        &nbsp;&nbsp;<a href="javascript:void(0)" id="add" class="easyui-linkbutton">新增</a></td>
        </tr>
      </thead>
  </table>
          </div>      
</form>
  </div>
</div>
</body>
</html>
