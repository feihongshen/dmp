<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.ApplyEditDeliverystate"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> cwbList = (List<CwbOrder>)request.getAttribute("cwbList");
List<ApplyEditDeliverystate> applyEditDeliverystateList = (List<ApplyEditDeliverystate>)request.getAttribute("applyEditDeliverystateList");

  List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
  List<User> userList = (List<User>)request.getAttribute("userList");
  Page page_obj = (Page)request.getAttribute("page_obj");
  
  ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()); 
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript">
$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
})
function sub(id){
	
	if($("#editnowdeliverystate"+id).val()==-1){
		alert("请选择要更改的配送结果！");
		return false;
	}
	if($("#editreason"+id).val().length==0){
		alert("请填写原因备注！");
		return false;
	}
	
	$.ajax({
		type: "POST",
		url:'<%=request.getContextPath()%>/applyeditdeliverystate/submitCreateApplyEditDeliverystate/'+id,
		data:{editnowdeliverystate:$("#editnowdeliverystate"+id).val(),
			editreason:$("#editreason"+id).val()},
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){
				alert("问题件成功提交：1单");
				parent.refreshState();
			}else{
				alert(data.error);
			}
			searchForm.submit();
		}
	});
	
}

function  search(){
	if($("#cwb").val()=='查询多个订单用回车隔开' || $("#cwb").val()=='' ){
		alert('请输入订单号');
		return false;
	}
	$("#searchForm").submit();
}
</script>
</head>
<body style="background:#eef9ff;overflow: hidden;" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="#" class="light">申请</a></li>
				<li><a href="../getApplyEditDeliverystateList/1">历史申请</a></li>
			</ul>
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="1" method="post" id="searchForm">
								<span>
								<select name="">
									<option>导出模板</option>
								</select>
								<input name="" type="button" value="导出Excel" class="input_button2">
								</span> 订单号：
								<textarea id="cwb" class="kfsh_text" onblur="if(this.value==''){this.value='查询多个订单用回车隔开'}" onfocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" rows="3" name="cwb"><%=request.getParameter("cwb")==null?"查询多个订单用回车隔开":request.getParameter("cwb")%></textarea>
								
								<input type="button" value="申请" class="input_button2" onclick="search();">
							</form>
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="120" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">当前站点</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">配送结果</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">小件员</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">处理状态</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">处理人</td>
									<td align="center" valign="middle" bgcolor="#E7F4E3">修改配送结果</td>
									<td width="170" align="center" valign="middle" bgcolor="#E7F4E3">原因备注</td>
									<td width="80" align="center" valign="middle" bgcolor="#E7F4E3">操作</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:100px"></div>
					
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
						<tbody>
						<%if(cwbList!=null&&cwbList.size()>0)for(CwbOrder cwb : cwbList){ %>
						<%int index = cwbList.indexOf(cwb); 
							ApplyEditDeliverystate aeds = applyEditDeliverystateList.get(index);
						%>
							<tr height="30" >
								<td width="120" align="center" valign="middle"><%=cwb.getCwb() %></td>
									<td width="100" align="center" valign="middle"><%for(CwbOrderTypeIdEnum coti : CwbOrderTypeIdEnum.values()){if(cwb.getCwbordertypeid()==coti.getValue()){ %><%=coti.getText() %><%}} %></td>
									<td width="120" align="center" valign="middle"><%for(Branch b : branchlist){if(cwb.getStartbranchid()==b.getBranchid()){ %><%=b.getBranchname() %><%}} %></td>
									<td width="100" align="center" valign="middle"><%for(DeliveryStateEnum dse : DeliveryStateEnum.values()){if(cwb.getDeliverystate()==dse.getValue()){ %><%=dse.getText() %><%}} %></td>
									<td width="100" align="center" valign="middle"><%for(User u : userList){if(cwb.getDeliverid()==u.getUserid()){ %><%=u.getRealname() %><%}} %></td>
									<td width="100" align="center" valign="middle"><%if(aeds.getIshandle()==ApplyEditDeliverystateIshandleEnum.WeiChuLi.getValue()){ %>未处理<%}else{ %>已处理<%} %></td>
									<td width="100" align="center" valign="middle"><%for(User u : userList){if(aeds.getEdituserid()==u.getUserid()){ %><%=u.getRealname() %><%}} %></td>
									<td align="center" valign="middle">
										<select name="editnowdeliverystate<%=aeds.getId() %>" id="editnowdeliverystate<%=aeds.getId() %>">
											<option value ="-1">==请选择==</option>
						                   <%if(cwb.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()){%>
						                   		<option value ="<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>" <%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.PeiSongChengGong.getText() %></option>
												<option value ="<%=DeliveryStateEnum.JuShou.getValue() %>"<%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.JuShou.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.JuShou.getText() %></option>
												<option value ="<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>"<%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.BuFenTuiHuo.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.BuFenTuiHuo.getText() %></option>
												<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"<%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.FenZhanZhiLiu.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
												<option value ="<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>"<%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.HuoWuDiuShi.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.HuoWuDiuShi.getText() %></option>
						                   <%}else if(cwb.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()){ %>
						                   		<option value ="<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue() %>"<%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.ShangMenHuanChengGong.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.ShangMenHuanChengGong.getText() %></option>
						                   		<option value ="<%=DeliveryStateEnum.JuShou.getValue() %>"<%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.JuShou.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.JuShou.getText() %></option>
												<option value ="<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>"<%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.BuFenTuiHuo.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.BuFenTuiHuo.getText() %></option>
												<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"<%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.FenZhanZhiLiu.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
						                   <%}else if(cwb.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()){ %>
						                   		<option value ="<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue() %>"<%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.ShangMenTuiChengGong.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.ShangMenTuiChengGong.getText() %></option>
						                   		<option value ="<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>"<%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.ShangMenJuTui.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.ShangMenJuTui.getText() %></option>
						                   		<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"<%if(aeds.getEditnowdeliverystate()==DeliveryStateEnum.FenZhanZhiLiu.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
						                   <%} %>
										</select>
								</td>
								<td width="170" align="center" valign="middle"><input name="editreason<%=aeds.getId() %>" id="editreason<%=aeds.getId() %>" type="text" value="<%=aeds.getEditreason()%>"></td>
									<td width="80" align="center" valign="middle">
										<%if(aeds.getEditnowdeliverystate()==0){ %>
										<input name="提交" type="button" class="input_button2" onclick="sub(<%=aeds.getId() %>);" value="提交">
										<%}else{ %>
										<input type="button"  value="已提交">
										<%} %>
									</td>
							</tr>
							<%} %>
						
					</table>
				</div>
				<%if(page_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
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
	</div>
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
function show(){
	if(<%=request.getAttribute("errorCwbs")!=null && !"".equals(request.getAttribute("errorCwbs")) %>){
		alert('<%=request.getAttribute("errorCwbs")%>');
	}
}
show();
</script>
</body>
</html>

