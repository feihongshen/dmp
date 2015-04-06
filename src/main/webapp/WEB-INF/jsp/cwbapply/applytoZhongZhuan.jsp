<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<CwbOrderView> cwbList = (List<CwbOrderView>)request.getAttribute("cwbList");
List<User> userList = (List<User>)request.getAttribute("User");
List<Branch> zhongzhuanbranchList = (List<Branch>)request.getAttribute("zhongzhuanbranchList");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	$("table#gd_table tr:odd").css("backgroundColor","#f9fcfd");
	$("table#gd_table tr:odd").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#f9fcfd");	
	});
	$("table#gd_table tr:even").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#fff");	
	});//表单颜色交替和鼠标滑过高亮
	
	$("table#gd_table2 tr").click(function(){
			$(this).css("backgroundColor","#FFA500");
			$(this).siblings().css("backgroundColor","#ffffff");
			$("tr[cwbstate='no']").css("backgroundColor","#aaaaaa");
		});
	
	$(".index_dh li").mouseover(function(){
		//$(this).show(0)
		var $child = $(this).children(0);
		$child.show();
	});
	$(".index_dh li").mouseout(function(){
 
		$(".menu_box").hide();
	});
	
	$("tr[cwbstate='no']").css("backgroundColor","#aaaaaa");
});

function sub(){
	var datavalue = "[";
	var flag="true";
	<%if(cwbList!=null&&cwbList.size()>0)for(CwbOrderView cwb :cwbList){ %>
	if($("#zhongzhuanremark_<%=cwb.getCwb() %>").val()==null||$("#zhongzhuanremark_<%=cwb.getCwb() %>").val().length==0){
		alert("订单<%=cwb.getCwb() %>未填写原因");
		flag="false";
		return;
	}
	if($("#zhongzhuanremark_<%=cwb.getCwb() %>").val().length>50){
		alert("订单<%=cwb.getCwb() %>备注过长（备注应少于50字）");
		flag="false";
		return;
	}
	
	if($("#branchid_<%=cwb.getCwb() %>").val()=="<%=cwb.getCwb() %>_s_0"){
		alert("订单<%=cwb.getCwb() %>未选择站点");
		flag="false";
		return;
	}
		datavalue = datavalue +"\""+ $("#branchid_<%=cwb.getCwb() %>").val()+"_s_"+ $("#zhongzhuanremark_<%=cwb.getCwb() %>").val()+"\",";
	<%}%>
	datavalue= datavalue + "]";
	if(flag=="true"){
		
	if(datavalue.length>2){
		$.ajax({
			type: "POST",
			url:$("#SubFrom").attr("action"),
			data:{branchidsAndRemarks:datavalue},
			dataType:"html",
			success : function(data) {
				alert("成功修改状态："+data.split("_s_")[0]+"单\n订单状态无变动："+data.split("_s_")[1]+"单");
				searchForm.submit();
			}
		});
	}
	}
}
</script>
</HEAD>
<BODY style="background:#f5f5f5"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath() %>/cwbapply/applytoZhongZhuan" class="light">中转订单申请</a></li>
				<li><a href="<%=request.getContextPath() %>/cwbapply/applytoZhongZhuanlist/1">历史申请中转订单</a></li>
			</ul>
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="<%=request.getContextPath() %>/cwbapply/applytoZhongZhuan" method="POST" id="searchForm">
								订单号：
								<textarea name="cwb" rows="3" class="kfsh_text" id="cwb" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
								<input type="submit" value="确定" class="input_button2">
							</form>
						</div>
						
					</div>
					<%if(cwbList!=null){%>
					<div style="height:100px"></div>
					<from action="<%=request.getContextPath() %>/cwbapply/auditApplyZhongZhuan" method="post" id="SubFrom" >
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="150" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">当前站点</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">配送结果</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">小件员</td>
									<td width="150" align="center" valign="middle" bgcolor="#E7F4E3">申请中转站</td>
									<td align="center" valign="middle" bgcolor="#E7F4E3">中转申请备注</td>
								</tr>
							<%if(cwbList!=null&&cwbList.size()>0)for(CwbOrderView cwb :cwbList){ %>
								<%if(cwb.getCurrentsitetype()!=BranchEnum.ZhanDian.getValue()
									||cwb.getApplyzhongzhuanbranchid()>0
									||(cwb.getCurrentsitetype()==BranchEnum.ZhanDian.getValue()&&
										cwb.getDeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue())){ %>
									<tr height="30" cwbFlowordertype="<%=cwb.getFlowordertype() %>"  cwbstate="no">
										<td width="150" align="center" valign="middle"><%=cwb.getCwb() %></td>
										<td width="100" align="center" valign="middle"><%=cwb.getCustomername()%></td>
										<td width="100" align="center" valign="middle"><%=cwb.getCwbordertypeid() %></td>
										<td width="100" align="center" valign="middle"><%=cwb.getCurrentbranchname() %></td>
										<td width="120" align="center" valign="middle"><%=cwb.getDeliverStateText() %></td>
										<td width="100" align="center" valign="middle"><%=cwb.getDelivername() %></td>
										<td width="150" align="center" valign="middle">
											<%=cwb.getApplyzhongzhuanbranchname() %>
											<input type="hidden" name="branchid_<%=cwb.getCwb() %>" value="<%=cwb.getCwb() %>_s_0"/>
										</td>
										<td align="center" valign="middle"><%=cwb.getApplyzhongzhuanremark() %></td>
									</tr>
								<%}else{ %>
									<tr height="30" cwbFlowordertype="<%=cwb.getFlowordertype() %>"  cwbstate="">
										<td width="150" align="center" valign="middle"><%=cwb.getCwb() %></td>
										<td width="100" align="center" valign="middle"><%=cwb.getCustomername()%></td>
										<td width="100" align="center" valign="middle"><%=cwb.getCwbordertypeid() %></td>
										<td width="100" align="center" valign="middle"><%=cwb.getCurrentbranchname() %></td>
										<td width="120" align="center" valign="middle"><%=cwb.getDeliverStateText() %></td>
										<td width="100" align="center" valign="middle"><%=cwb.getDelivername() %></td>
										<td width="150" align="center" valign="middle">
											<select name="branchid_<%=cwb.getCwb() %>" id="branchid_<%=cwb.getCwb() %>">
												<option value="<%=cwb.getCwb() %>_s_0">请选择</option>
												<%for(Branch b :zhongzhuanbranchList) {%>
													<option value="<%=cwb.getCwb() %>_s_<%=b.getBranchid() %>" <%if(cwb.getApplyzhongzhuanbranchid()==b.getBranchid()){ %>selected="selected" <%} %>><%=b.getBranchname() %></option>
												<%} %>
											</select>
										</td>
										<td align="center" valign="middle">
											<input type="text" name="zhongzhuanremark_<%=cwb.getCwb() %>" id="zhongzhuanremark_<%=cwb.getCwb() %>" value="<%=cwb.getApplyzhongzhuanremark() %>"/>
										</td>
									</tr>								
								<%} %>
							<%} %>
							</tbody>
						</table>
					</from>
				</div>
				<div style="height:40px"></div>
				<div class="iframe_bottom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
						<tbody>
							<tr height="38" >
								<td align="center" valign="middle" bgcolor="#FFFFFF"><input type="button" id="button" value="提交" onclick="sub()" class="input_button1"></td>
							</tr>
						</tbody>
					</table>
				</div><%} %>
		</div>
	</div>
</div>
</BODY>
</HTML>
