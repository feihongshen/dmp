<%@page import="cn.explink.domain.ExpressSetBranchContract"%>
<%@page import="cn.explink.domain.VO.ExpressSetBranchContractVO"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.YesOrNoStateEnum"%>
<%@page import="cn.explink.enumutil.coutracManagementEnum.ContractStateEnum"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%-- <%
		List<ExpressSetBranchContract> branchContractList=request.getAttribute("branchContractList")==null?null:(List<ExpressSetBranchContract>)request.getAttribute("branchContractList");
		Page page_obj =request.getAttribute("page_obj")==null?null:(Page)request.getAttribute("page_obj");
%> --%>
<%
	List<ExpressSetBranchContract> branchContractList=request.getAttribute("branchContractList")==null?null:(List<ExpressSetBranchContract>)request.getAttribute("branchContractList");
	/* ExpressSetBranchContractVO branchContract=(ExpressSetBranchContractVO)request.getAttribute("bc")==null?null:(ExpressSetBranchContractVO)request.getAttribute("bc"); */
%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	/* $("#isDeposit").change(function(){
		if($('#isDeposit option:selected').text()=="是"){  
	        $("#firstDepositTr").css('display' ,'');  
	        $("#secondDepositTr").css('display' ,'');  
	    }else{  
            $("#depositCollectDate").val("");  
            $("#depositCollectAmount").val("");  
            $("#depositCollector").val("");  
            $("#depositPayor").val("");  
            $("#firstDepositTr").css('display', 'none');  
            $("#secondDepositTr").css('display' ,'none');
	   }
	}); */
	
	/* $("#").datepicker();
	$("#").datepicker();
	$("#").datepicker(); */
	
	$('#edit_btn').click(function(){
		
	});
	
	$("table#callertb tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});
	$('#delete_button').click(function(){
		/* var checked = $("#callertb input[type='checkbox'][name='count']");
		$(checked).each(function() {
			if ($(this).attr("checked") == true) // 注意：此处判断不能用$(this).attr("checked")==‘true'来判断
			{ */
				$.ajax({
					type:'POST',
					data:'id='+$("#branchId").val(),
					url:'<%=request.getContextPath()%>/branchContract/deleteBranchContract',
					dataType:'json',
					success:function(data){
						if(data && data.errorCode==0){
							alert(data.error);
							window.location.href='<%=request.getContextPath()%>/branchContract/branchContractList';
						}
					}
				});
		/* 	}
		});  */
	});
});

function setBranchId(id){
	$("#branchId").val(id);
}

function addInit(){
	
}
function afterInit(){

}

function editInit(){
	
}

function initSelect(){
	$("#contractState").append('<option value ="<%=ContractStateEnum.XinJian.getValue()%>"><%=ContractStateEnum.XinJian.getText()%></option>'); 
	$("#contractState").append('<option value ="<%=ContractStateEnum.ZhiXingZhong.getValue()%>"><%=ContractStateEnum.ZhiXingZhong.getText()%></option>'); 
	$("#contractState").append('<option value ="<%=ContractStateEnum.HeTongZhongZhi.getValue()%>"><%=ContractStateEnum.HeTongZhongZhi.getText()%></option>'); 
	$("#contractState").append('<option value ="<%=ContractStateEnum.HeTongJieShu.getValue()%>"><%=ContractStateEnum.HeTongJieShu.getText()%></option>'); 
	
	$("#isDeposit").append('<option value ="<%=YesOrNoStateEnum.No.getValue()%>"><%=YesOrNoStateEnum.No.getText()%></option>'); 
	$("#isDeposit").append('<option value ="<%=YesOrNoStateEnum.Yes.getValue()%>"><%=YesOrNoStateEnum.Yes.getText()%></option>'); 
}
function getPath(){
	var path = "<%=request.getContextPath()%>";
	return path;
}
</script>
</head>
<body>
	<!-- 弹出框开始 -->
	<div id="alert_box" style="display:none; " align="center"></div>
	<!-- 弹出框结束 -->
	
	<div>	
			<table>
				<tr>
					<td><button id="add_btn" onclick="addContract()" class="input_button2">新增</button></td>
					<td><button id="edit_btn" onclick="updateContract()" class="input_button2">查看/修改</button></td>
					<td><button id="delete_button" class="input_button2">删除</button></td>
					<td><button id="query_button" onclick="queryContract()" class="input_button2">查询</button></td>
				</tr>
			</table>
			<hr>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="callertb">
				<tr class="font_1">
					<!-- <th bgcolor="#eef6ff"><input type="checkbox" id="checkAll" value=""/></th> -->
					<th bgcolor="#eef6ff">编号</th>
					<th bgcolor="#eef6ff">合同状态</th>
					<th bgcolor="#eef6ff">加盟商名称</th>
					<th bgcolor="#eef6ff">区域经理</th>
					<th bgcolor="#eef6ff">合同日期范围</th>
					<th bgcolor="#eef6ff">站点负责人</th>
					<th bgcolor="#eef6ff">负责人身份证</th>
				</tr>
				<%if(branchContractList!=null){ %>
				<%for(ExpressSetBranchContract bc:branchContractList){ %>
				<tr onclick="setBranchId('<%=bc.getId()%>')">
					<td><%=bc.getContractNo()==null?"": bc.getContractNo()%></td>
					<td><%=bc.getContractState()%></td>
					<td><%=bc.getBranchName()==null?"":bc.getBranchName() %></td>				
					<td><%=bc.getAreaManager()==null?"":bc.getAreaManager() %></td>
					<td><%=bc.getContractBeginDate()==null?"": bc.getContractBeginDate()%>至<%=bc.getContractEndDate()==null?"": bc.getContractEndDate()%></td>
					<td><%=bc.getSiteChief()==null?"":bc.getSiteChief() %></td>
					<td><%=bc.getChiefIdentity()==null?"":bc.getChiefIdentity() %></td>
				</tr>
				<%}%>
				<%}%>
			</table>
		</div>
<%-- <input type="hidden" id="add" value="<%=request.getContextPath()%>/branchContract/addBranchContractPage"/> --%>
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/branchContract/updateBranchContractPage/"/>
<input type="hidden" id="branchId" value=""/>
	
	<%-- <table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="pageid">
		<tr>
			<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
				<a href="javascript:$('#PageFromW').attr('action','1');$('#PageFromW').submit();" >第一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#PageFromW').submit();">上一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#PageFromW').submit();" >下一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#PageFromW').submit();" >最后一页</a>
				　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第
					<select
						id="selectPg"
						onchange="$('#PageFromW').attr('action',$(this).val());$('#PageFromW').submit()">
						<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
						<option value="<%=i %>"><%=i %></option>
						<% } %>
					</select>页
			</td>
		</tr>
	</table>
	
	<script type="text/javascript">
		$("#selectPg").val(<%=request.getAttribute("page") %>);
	</script> --%>
	
	
</body>
</html>