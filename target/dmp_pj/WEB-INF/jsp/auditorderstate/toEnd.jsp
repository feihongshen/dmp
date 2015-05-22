<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.orderflow.OrderFlow"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.dao.OrderFlowDAO"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.dao.UserDAO"%>
<%@page import="cn.explink.dao.BranchDAO"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<User> userallList = (List<User>)request.getAttribute("userallList");

List<CwbOrderView> cwbList = (List<CwbOrderView>)request.getAttribute("cwbList");
List<Reason> reasonList = (List<Reason>)request.getAttribute("reasonList");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
List<User> kufangList = new ArrayList<User>();
List<User> zhandianList = new ArrayList<User>();
List<User> tuihuoList = new ArrayList<User>();
List<User> zhongzhuanList = new ArrayList<User>();
List<User> driverList = new ArrayList<User>();
ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()); 
UserDAO userDAO=ctx.getBean(UserDAO.class);
BranchDAO branchDAO=ctx.getBean(BranchDAO.class);
OrderFlowDAO orderflowDAO=ctx.getBean(OrderFlowDAO.class);

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



$(function(){
	<%if(cwbList!=null&&cwbList.size()!=0){
		for(CwbOrderView cwb : cwbList){%>
	$("#handleresult<%=cwb.getCwb()%>").change(function(){
		var datavalue = $(this).val().split("_");
		if(datavalue[0]==-1){
			return ;
		}
		
			if("<%=cwb.getCwb()%>"==datavalue[1]){
				<%
					kufangList = userDAO.getUserByBranchid(Long.parseLong(cwb.getCarwarehouse()==null?"0":(cwb.getCarwarehouse().length()==0?"0":cwb.getCarwarehouse())));
					zhandianList = userDAO.getUserByBranchid(cwb.getCurrentbranchid());
					tuihuoList = userDAO.getUserByBranchid(branchDAO.getBranchByMyBranchIsTuihuo(cwb.getCurrentbranchid())==null?0:
						(branchDAO.getBranchByMyBranchIsTuihuo(cwb.getCurrentbranchid()).size()==0?0:branchDAO.getBranchByMyBranchIsTuihuo(cwb.getCurrentbranchid()).get(0).getBranchid()));
					zhongzhuanList = userDAO.getUserByBranchid(branchDAO.getBranchByMyBranchIsZhongzhuan(cwb.getCurrentbranchid())==null?0:
						(branchDAO.getBranchByMyBranchIsZhongzhuan(cwb.getCurrentbranchid()).size()==0?0:branchDAO.getBranchByMyBranchIsZhongzhuan(cwb.getCurrentbranchid()).get(0).getBranchid()));
					driverList = userDAO.getUserByRoleAndBranchid(3, cwb.getCurrentbranchid());
				%>
				
				if(datavalue[0]==1||datavalue[0]==7){//甲方未发货、无法确认处理结果
					var optionstring="";
					optionstring+="<option value='-1'>请选择</option>";
					optionstring+="<option value='0'>没有责任人</option>";
					$("#personliable<%=cwb.getCwb()%>").html(optionstring);
				}else if(datavalue[0]==2){//库房丢失
					var optionstring="";
					optionstring+="<option value='-1'>请选择</option>";
					<%if(kufangList!=null&&kufangList.size()!=0)for(User u : kufangList){ %>
						optionstring+="<option value='<%=u.getUserid()%>'><%=u.getRealname() %></option>";
					<%}%>
					$("#personliable<%=cwb.getCwb()%>").html(optionstring);
				}else if(datavalue[0]==4){//站点丢失
					var optionstring="";
					optionstring+="<option value='-1'>请选择</option>";
					<%if(zhandianList!=null&&zhandianList.size()!=0)for(User u : zhandianList){ %>
						optionstring+="<option value='<%=u.getUserid()%>'><%=u.getRealname() %></option>";
					<%}%>
					$("#personliable<%=cwb.getCwb()%>").html(optionstring);
				}else if(datavalue[0]==5){//退货丢失
					var optionstring="";
					optionstring+="<option value='-1'>请选择</option>";
					<%if(tuihuoList!=null&&tuihuoList.size()!=0)for(User u : tuihuoList){ %>
						optionstring+="<option value='<%=u.getUserid()%>'><%=u.getRealname() %></option>";
					<%}%>
					$("#personliable<%=cwb.getCwb()%>").html(optionstring);
				}else if(datavalue[0]==6){//中转丢失
					var optionstring="";
					optionstring+="<option value='-1'>请选择</option>";
					<%if(zhongzhuanList!=null&&zhongzhuanList.size()!=0)for(User u : zhongzhuanList){ %>
						optionstring+="<option value='<%=u.getUserid()%>'><%=u.getRealname() %></option>";
					<%}%>
					$("#personliable<%=cwb.getCwb()%>").html(optionstring);
				}else if(datavalue[0]==3){//运输丢失
					var optionstring="";
					optionstring+="<option value='-1'>请选择</option>";
					<%if(driverList!=null&&driverList.size()!=0)for(User u : driverList){ %>
						optionstring+="<option value='<%=u.getUserid()%>'><%=u.getRealname() %></option>";
					<%}%>
					
					$("#personliable<%=cwb.getCwb()%>").html(optionstring);
				}
			}
	});
	<%}}%>
})

function sub(){
	var datavalue = "[";
	var flag="true";
	<%if(cwbList!=null&&cwbList.size()!=0)for(CwbOrderView cwb : cwbList){%>
		if($("#handleresult<%=cwb.getCwb()%>").val()==-1){
			alert("请填写订单<%=cwb.getCwb()%>的处理结果");
			flag="false";
			return;
		}
		if($("#personliable<%=cwb.getCwb()%>").val()==-1){
			alert("订单<%=cwb.getCwb()%>请选择责任人");
			flag="false";
			return;
		}
		if($("#handlereason<%=cwb.getCwb()%>").val()==null||$("#handlereason<%=cwb.getCwb()%>").val().length==0){
			alert("订单<%=cwb.getCwb()%>未填写备注");
			flag="false";
			return;
		}
		if($("#handlereason<%=cwb.getCwb()%>").val().length>200){
			alert("订单<%=cwb.getCwb()%>备注原因过长（应少于200）");
			flag="false";
			return;
		}
		if($("#handleresult<%=cwb.getCwb()%>").val()!=-1){
			datavalue = datavalue + "\"<%=cwb.getScancwb()%>_s_"+$("#handleresult<%=cwb.getCwb()%>").val().split("_")[0]+"_s_"+$("#personliable<%=cwb.getCwb()%>").val()+"_s_"+$("#handlereason<%=cwb.getCwb()%>").val()+"@\",";
		}else{
			datavalue=  datavalue +  "\"<%=cwb.getScancwb()%>_s_"+$("#handleresult<%=cwb.getCwb()%>").val()+"_s_"+$("#personliable<%=cwb.getCwb()%>").val()+"_s_"+$("#handlereason<%=cwb.getCwb()%>").val()+"@\",";
		}
		
	<%}%>
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
	}
	datavalue= datavalue + "]";
	if(flag=="true"){
		$.ajax({
			type: "POST",
			url:$("#SubFrom").attr("action"),
			data:{cwbdetails:datavalue},
			dataType:"html",
			success : function(data) {
				alert("成功修改状态："+data.split("_s_")[0]+"单\n订单状态无变动："+data.split("_s_")[1]+"单");
				searchForm.submit();
			}
		});
	}

	
}

function exportField(){
	if(<%=cwbList!=null&&cwbList.size()!=0%>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval").attr("disabled","disabled"); 
	 	$("#btnval").val("请稍后……");
		$("#searchForm2").submit();	
	}else{
		alert("没有做查询操作，不能导出！");
	}
}

</script>
</HEAD>
<BODY style="background:#f5f5f5"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<!-- <div class="kfsh_tabbtn">
			<ul>
				<li><a href="./toEnd" class="light">异常订单处理</a></li>
			</ul>
		</div> -->
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="./toEnd" method="post" id="searchForm">
								订单号：
								<textarea name="cwb" rows="3" class="kfsh_text" id="cwb" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
								<input type="submit" value="确定" class="input_button2">
							</form>
						</div>
						<%if(cwbList!=null) {%>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="150" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">代收金额</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">当前状态</td>
									<td width="150" align="center" valign="middle" bgcolor="#E7F4E3">处理结果</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">责任人</td>
									<td width="200" align="center" valign="middle" bgcolor="#E7F4E3">处理原因</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">备注</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:100px"></div>
					<from action="./auditEnd" method="post" id="SubFrom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2" >
						<tbody>
							<%
							for(CwbOrderView cwb :cwbList){ 
							%>
								<tr height="30" cwbFlowordertype="<%=cwb.getFlowordertype() %>"  cwbstate="<%
								if(cwb.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue()&&cwb.getDeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue()&&
										!((cwb.getSendcarnum()>0||cwb.getBackcarnum()>0)&&cwb.getTranscwb().length()>0&&!cwb.getCwb().equals(cwb.getTranscwb())&&cwb.getFlowordertype()==FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue())
											){
									out.print("no");
								} %>"	>
								<td width="150" align="center" valign="middle"><%=cwb.getCwb() %></td>
								<td width="120" align="center" valign="middle"><%=cwb.getCustomername() %></td>
								<td width="120" align="center" valign="middle"><%=cwb.getEmaildate() %></td>
								<td width="120" align="right" valign="middle"><%=cwb.getReceivablefee() %></td>
								<td width="120" align="center" valign="middle"><%=FlowOrderTypeEnum.getText(cwb.getFlowordertype()).getText() %></td>
								<td width="150" align="center" valign="middle">
								<%if(cwb.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue()
										&&cwb.getDeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue()
										){%>
											<%for(HandleResultEnum hr : HandleResultEnum.values()){if(cwb.getHandleresult()==hr.getValue()){ %>
												<%=hr.getText() %>
											<%}} %>
									<input type="hidden" name="handleresult<%=cwb.getCwb()%>" id="handleresult<%=cwb.getCwb()%>" value="-1"/>
								<%}else{ %>
								<select id="handleresult<%=cwb.getCwb()%>" name="handleresult<%=cwb.getCwb()%>">
									<option value="-1">请选择</option>
									<option value="<%=HandleResultEnum.JiaFangWeiFaHuo.getValue() %>_<%=cwb.getCwb()%>"><%=HandleResultEnum.JiaFangWeiFaHuo.getText() %></option>
									<option value="<%=HandleResultEnum.KuFangDiuShi.getValue() %>_<%=cwb.getCwb()%>"><%=HandleResultEnum.KuFangDiuShi.getText() %></option>
									<option value="<%=HandleResultEnum.YunShuDiuShi.getValue() %>_<%=cwb.getCwb()%>"><%=HandleResultEnum.YunShuDiuShi.getText() %></option>
									<option value="<%=HandleResultEnum.ZhanDianDiuShi.getValue() %>_<%=cwb.getCwb()%>"><%=HandleResultEnum.ZhanDianDiuShi.getText() %></option>
									<option value="<%=HandleResultEnum.TuiHuoDiuShi.getValue() %>_<%=cwb.getCwb()%>"><%=HandleResultEnum.TuiHuoDiuShi.getText() %></option>
									<option value="<%=HandleResultEnum.ZhongZhuanDiuShi.getValue() %>_<%=cwb.getCwb()%>"><%=HandleResultEnum.ZhongZhuanDiuShi.getText() %></option>
									<option value="<%=HandleResultEnum.WuFaQueRenChuLiJieGuo.getValue() %>_<%=cwb.getCwb()%>"><%=HandleResultEnum.WuFaQueRenChuLiJieGuo.getText() %></option>
								</select>
								<%} %>
								</td>
								<td width="120" align="center" valign="middle">
								<%if(cwb.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue()
										&&cwb.getDeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue()
										){%>
											<%for(User u : userallList){if(cwb.getHandleperson()==u.getUserid()){ %>
											<%=u.getRealname() %>
											<%}} %>
									<input type="hidden" name="personliable<%=cwb.getCwb()%>" id="personliable<%=cwb.getCwb()%>" value="-1"/>
								<%}else{ %>
								<select id="personliable<%=cwb.getCwb()%>" name="personliable<%=cwb.getCwb()%>">
									<option value="-1">请选择</option>
								</select>
								<%} %>
								</td>
								<td width="200" align="center" valign="middle">
								<%if(cwb.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue()
										&&cwb.getDeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue()
										){%>
											<%=cwb.getHandlereason() %>
									<input type="hidden" name="handlereason<%=cwb.getCwb()%>" id="handlereason<%=cwb.getCwb()%>" value=""/>
								<%}else{ %>
									<input id="handlereason<%=cwb.getCwb()%>" name="handlereason<%=cwb.getCwb()%>" value="" maxlength="225"/>
								<%} %>
								</td>
								<td  width="120" align="center" valign="middle"><%=cwb.getCwbremark() %></td>
							</tr>
							<%} %>
						</tbody>
					</table>
					</from><%} %>
				</div>
				<%if(cwbList!=null) {%>
				<div style="height:40px"></div>
				<div class="iframe_bottom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" id="gd_table2">
						<tbody>
							<tr height="30" >
								<td align="center" valign="middle" bgcolor="#f3f3f3"><input type="button" name="button" id="button" value="提交" class="input_button1" onclick="sub();"></td>
							</tr>
						</tbody>
					</table>
				</div><%} %>
		</div>
	</div>
</div>
</BODY>
</HTML>
