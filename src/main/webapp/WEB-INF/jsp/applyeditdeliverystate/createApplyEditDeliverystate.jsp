<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
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
List<Reason> reasonList = (List<Reason>)request.getAttribute("reasonList");	
  List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
  List<User> userList = (List<User>)request.getAttribute("userList");
  Page page_obj = (Page)request.getAttribute("page_obj");
  List<CwbOrderView> covList = (List<CwbOrderView>)request.getAttribute("covList");
  ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()); %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
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
//处理第一次修改时的状态
function sub(id){
	
	if($("#editnowdeliverystate"+id).val()==-1){
		alert("请选择要更改的配送结果！");
		return false;
	}
	if($("#reasonid"+id).val()==0){
		alert("请选择原因备注!");
		return false;
	}
	if($("#editreason"+id).val().length==0){
		alert("请填写备注！");
		return false;
	}
	
	$.ajax({
		type: "POST",
		url:'<%=request.getContextPath()%>/applyeditdeliverystate/submitCreateApplyEditDeliverystate/'+id,
		data:{editnowdeliverystate:$("#editnowdeliverystate"+id).val(),
			reasonid:$("#reasonid"+id).val(),
			editreason:$("#editreason"+id).val()},
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){
				alert("提交成功！");
				//parent.refreshState();
			}else{
				alert(data.error);
			}
			$("#cwb").val("");
			$("#searchForm").submit();
		}
	});
	
}
//处理之前已经修改过之后产生新状态
<%-- function sub2(cwbstr,id){
	
	$.ajax({
		type: "POST",
		url:'<%=request.getContextPath()%>/applyeditdeliverystate/toCreateApplyEditDeliverystateAgin',
		data:{cwbss:cwbstr,
			  editnowdeliverystate:$("#editnowdeliverystate"+id).val(),
			  editreason:$("#editreason"+id).val()
		     },
		dataType:"html",
		success : function(data) {
			if(data.errorCode==0){
				alert("问题件成功提交：1单");
			}else{
				alert("问题件成功提交：1单");
			}
			//searchForm.submit();
		}
	});
	
} --%>


function  search(){
	if($("#cwb").val()=='查询多个订单用回车隔开' || $("#cwb").val()=='' ){
		alert('请输入订单号');
		return false;
	}
	$("#searchForm").submit();
}

function changereasonremark(obj,opscwbid){
	$("#reasonid"+opscwbid).empty();
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/applyeditdeliverystate/finddeliveryreason",
		data:{"deliverytype":$(obj).val()},
		dataType:"json",
		success:function(data){
			var optstr="<option value='0'>==请选择==</option>";
			$.each(data,function(i,a){
				optstr+="<option value='"+a.reasonid+"' >"+a.reasoncontent+"</option>";
				});
				$("#reasonid"+opscwbid).append(optstr);
			}
		});
}
	
</script>
</head>
<body style="background:#f5f5f5;overflow: hidden;" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="#" class="light">反馈状态修改申请</a></li>
				<li><a href="<%=request.getContextPath()%>/applyeditdeliverystate/getApplyEditDeliverystateList/1">反馈状态修改列表</a></li>
			</ul>
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="1" method="post" id="searchForm">
								<table>
									<tr>
										<td>
											订单号：
										</td>
										<td>
											<textarea id="cwb" class="kfsh_text" onblur="if(this.value==''){this.value='查询多个订单用回车隔开'}" onfocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" rows="3" name="cwb"><%=request.getParameter("cwb")==null?"查询多个订单用回车隔开":request.getParameter("cwb")%></textarea>
											<input type="button" value="申请" class="input_button2" onclick="search();">
										</td>
									</tr>
								</table>
							</form>
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="120" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
									<td width="120" align="center" valign="middle" bgcolor="#eef6ff">当前站点</td>
									<td width="100" align="center" valign="middle" bgcolor="#eef6ff">配送结果</td>
									<td width="100" align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
									<td width="100" align="center" valign="middle" bgcolor="#eef6ff">处理状态</td>
									<td width="100" align="center" valign="middle" bgcolor="#eef6ff">处理人</td>
									<td width="120" align="center" valign="middle" bgcolor="#eef6ff">修改配送结果</td>
									<td width="120" align="center" valign="middle" bgcolor="#eef6ff">原因备注</td>
									<td width="100" align="center" valign="middle" bgcolor="#eef6ff">备注</td>
									<td width="80" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								</tr>
								<%if(covList!=null&&covList.size()>0){
								for(CwbOrderView cwb : covList){ %>
								<tr height="30" >
									<td width="120" align="center" valign="middle"><%=cwb.getCwb() %></td>
									<td width="100" align="center" valign="middle"><%=cwb.getCwbordertypename()%></td>
									<td width="120" align="center" valign="middle"><%=cwb.getBranchname()%></td>
									<td width="100" align="center" valign="middle"><%=cwb.getDeliveryname() %></td>
									<td width="100" align="center" valign="middle"><%=cwb.getDelivername() %></td>
									<td width="100" align="center" valign="middle"><%=cwb.getRemark1()%></td>
									<td width="100" align="center" valign="middle"><%=cwb.getRemark2()%></td>
									<td width="120" align="center" valign="middle">
										<select name="editnowdeliverystate<%=cwb.getOpscwbid() %>" id="editnowdeliverystate<%=cwb.getOpscwbid() %>" onchange="changereasonremark(this,'<%=cwb.getOpscwbid() %>')" style="width: 140px;">
											<option value ="-1">==请选择==</option>
						                   <%if(Integer.parseInt(cwb.getCwbordertypeid()) == CwbOrderTypeIdEnum.Peisong.getValue()){%>
						                   		<option value ="<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>" <%if(cwb.getDeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.PeiSongChengGong.getText() %></option>
												<option value ="<%=DeliveryStateEnum.JuShou.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.JuShou.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.JuShou.getText() %></option>
												<option value ="<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.BuFenTuiHuo.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.BuFenTuiHuo.getText() %></option>
												<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.FenZhanZhiLiu.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
												<option value ="<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.HuoWuDiuShi.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.HuoWuDiuShi.getText() %></option>
												<option value ="<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.DaiZhongZhuan.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.DaiZhongZhuan.getText() %></option>
												<option value ="<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getText() %></option>
						                    <% }else if(Integer.parseInt(cwb.getCwbordertypeid()) == CwbOrderTypeIdEnum.Express.getValue()){%>
						                   		<option value ="<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>" <%if(cwb.getDeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.PeiSongChengGong.getText() %></option>
												<option value ="<%=DeliveryStateEnum.JuShou.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.JuShou.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.JuShou.getText() %></option>
												<option value ="<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.BuFenTuiHuo.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.BuFenTuiHuo.getText() %></option>
												<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.FenZhanZhiLiu.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
												<option value ="<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.HuoWuDiuShi.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.HuoWuDiuShi.getText() %></option>
												<option value ="<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.DaiZhongZhuan.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.DaiZhongZhuan.getText() %></option>
												<option value ="<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getText() %></option>
						                   <%}else if(Integer.parseInt(cwb.getCwbordertypeid()) == CwbOrderTypeIdEnum.Shangmenhuan.getValue()){ %>
						                   		<option value ="<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.ShangMenHuanChengGong.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.ShangMenHuanChengGong.getText() %></option>
						                   		<option value ="<%=DeliveryStateEnum.JuShou.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.JuShou.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.JuShou.getText() %></option>
												<option value ="<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.BuFenTuiHuo.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.BuFenTuiHuo.getText() %></option>
												<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.FenZhanZhiLiu.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
												<option value ="<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.HuoWuDiuShi.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.HuoWuDiuShi.getText() %></option>
												<option value ="<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.DaiZhongZhuan.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.DaiZhongZhuan.getText() %></option>
												<option value ="<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getText() %></option>
						                   <%}else if(Integer.parseInt(cwb.getCwbordertypeid()) == CwbOrderTypeIdEnum.Shangmentui.getValue()){ %>
						                   		<option value ="<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.ShangMenTuiChengGong.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.ShangMenTuiChengGong.getText() %></option>
						                   		<option value ="<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.ShangMenJuTui.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.ShangMenJuTui.getText() %></option>
						                   		<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.FenZhanZhiLiu.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
						                 		<%-- 
						                 			hps_Concerto 注释  2016年5月25日12:06:43
						                 		<option value ="<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.HuoWuDiuShi.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.HuoWuDiuShi.getText() %></option> --%>
												<option value ="<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.DaiZhongZhuan.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.DaiZhongZhuan.getText() %></option>
												<option value ="<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue() %>"<%if(cwb.getDeliverystate()==DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()){ %>selected<%} %>><%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getText() %></option>
						                   <%} %>
										</select>
									</td>
									<td width="120" align="center" valign="middle">
										<select id="reasonid<%=cwb.getOpscwbid()%>" style="width: 140px"/>
											<option value="0">==请选择==</option>
											<%if(reasonList!=null){for(Reason reason:reasonList){ %>
											<option value="<%=reason.getReasonid()%>" <%if(cwb.getReasonid()==reason.getReasonid()){%>selected<%}%>><%=reason.getReasoncontent() %></option>
											<%} }%>
										</select>
									</td>
									<td width="100" align="center" valign="middle"><input name="editreason<%=cwb.getOpscwbid() %>" id="editreason<%=cwb.getOpscwbid() %>" type="text" value="<%=cwb.getRemark3()%>"></td>
									<td width="80" align="center" valign="middle">
									<%if(cwb.getState()==0){ %>
									<input name="提交" type="button" class="input_button2" onclick="sub(<%=cwb.getOpscwbid() %>);" value="提交">
									<%}else{ %>
									<input type="button"  value="已提交" <%-- onclick="sub2('<%=cwb.getCwb() %>',<%=cwb.getOpscwbid() %>); --%>">
									<%} %>
									</td>
								</tr>
								<%} }%>
							</tbody>
						</table>
					</div>
					<div style="height:105px"></div>
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

