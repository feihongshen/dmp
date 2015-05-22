<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%List<Branch> branchList = (List<Branch>)request.getAttribute("branchList"); 
	List<Role> roleList = (List<Role>)request.getAttribute("roles") ;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<title>群发短信</title>
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
<script>
$(document).ready(function() {
	   // Options displayed in comma-separated list
	   $("#branchids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'站点' });
	   $("#roleids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'角色' });
	});
function onblurcwb(){
	if($("#mobilesID").val().length>0){
		if($("#mobilesID").val().split("\n").length >1){
			
		$("#cwbcount").html($("#mobilesID").val().split("\n").length);
		}else{
			$("#cwbcount").html(1);
		}
	}else{
		$("#cwbcount").html(0);
	}
}
function onblursms(){
	$("#smscount").html($("#smsRemackID").val().length);
}

$(document).ready(function() {
	$("#btnval").click(function(){
		if($("#mobilesID").val().length==0){
			alert("请输入单号！");
			return false;
			}
		if($("#smsRemackID").val().length==0){
			alert("请输入短信内容！");
			return false;
			}
		$("#batchForm").submit();
	});
});

</script>
<script language="javascript">
$(function(){
	var $menuli = $(".uc_midbg ul li");
	var $menulilink = $(".uc_midbg ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
})
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
	<div class="uc_midbg">
		<ul>
			<li><a href="#" class="light">公司内部</a></li>
			<li><a href="#">订单号</a></li>
			<li><a href="#">站点区域</a></li>
		</ul>
	</div>
	<div class="tabbox">
	  <li>
	  <div class="right_title">
	  	<form name="form1" method="post" action=""><table width="100%" border="0" cellspacing="0" cellpadding="5" class="table_2">
	  		<tr>
	  			<td width="60" align="left">岗位角色：</td>
	  			<td align="left">
	  			<select name ="roleids" id ="roleids" multiple="multiple" style="width: 180px;">
							<%for(Role r : roleList){ %>
							<option value="<%=r.getRoleid() %>"  ><%=r.getRolename() %></option>
							<%} %>
				        </select>
				        </td>
  			</tr>
	  		<tr>
	  			<td align="left" valign="top">短信内容：</td>
	  			<td align="left"><textarea name="textfield" cols="50" rows="5" id="textfield"></textarea></td>
  			</tr>
	  		<tr>
	  			<td align="left">&nbsp;</td>
	  			<td align="left"><input name="button" type="submit" class="input_button1" id="button" value="发送"></td>
  			</tr>
  		</table>
	  	</form>
	  	
	  </div>
	  </li>
		<li style="display:none"><div class="right_title">
	  	<form name="form1" method="post" action=""><table width="100%" border="0" cellspacing="0" cellpadding="5" class="table_2">
	  		<tr>
	  			<td width="60" align="left">订单号：</td>
	  			<td align="left"><textarea name="smsRemack" cols="40" rows="5" id="mobilesID" onkeyup="onblurcwb();" ></textarea></td>
  			</tr>
	  		<tr>
	  			<td align="left" valign="top">短信内容：</td>
	  			<td align="left"><textarea name="smsRemack" cols="50" rows="5" id="smsRemackID"  onkeyup="onblursms();" ><%if(request.getParameter("smsRemack")!=null){%><%=request.getParameter("smsRemack") %><%} %></textarea></td>
  			</tr>
	  		<tr>
	  			<td align="left">&nbsp;</td>
	  			<td align="left"><input name="button" type="submit" class="input_button1" id="button" value="发送"></td>
  			</tr>
  		</table>
	  	</form>
	  	
	  </div></li>
		<li style="display:none"><div class="right_title">
	  	<form name="form1" method="post" action=""><table width="100%" border="0" cellspacing="0" cellpadding="5" class="table_2">
	  		<tr>
	  			<td width="60" align="left">站点区域：</td>
	  			<td align="left"><select name ="branchids" id ="branchids" multiple="multiple" style="width: 180px;">
               	<%for(Branch b : branchList){ %>
		          <option value =<%=b.getBranchid() %> 
		                  ><%=b.getBranchname()%></option>
					<%} %>
		        </select></td>
  			</tr>
	  		<tr>
	  			<td align="left" valign="top">短信内容：</td>
	  			<td align="left"><textarea name="textfield" cols="50" rows="5" id="textfield"></textarea></td>
  			</tr>
	  		<tr>
	  			<td align="left">&nbsp;</td>
	  			<td align="left"><input name="button" type="submit" class="input_button1" id="button" value="发送"></td>
  			</tr>
  		</table>
	  	</form>
	  	
	  </div></li>
		
	</div>
</div>
</div>
</body>
</html>
