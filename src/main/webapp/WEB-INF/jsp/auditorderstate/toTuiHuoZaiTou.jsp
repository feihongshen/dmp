<%@page import="cn.explink.domain.OrderBackRuku"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<OrderBackRuku> obrsList = (List<OrderBackRuku>)request.getAttribute("obrsList");
List<Reason> reasonList = (List<Reason>)request.getAttribute("reasonList");
String cwbStr = request.getParameter("cwbs")==null?"":request.getParameter("cwbs");
Page page_obj = (Page)request.getAttribute("page_obj");
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

<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.inputer.js"></script>
<script src="${pageContext.request.contextPath}/js/inputer.js"></script>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
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
	
	/* $("table#gd_table2 tr").click(function(){
			$(this).css("backgroundColor","#FFA500");
			$(this).siblings().css("backgroundColor","#ffffff");
			$("tr[cwbstate='no']").css("backgroundColor","#aaaaaa");
		}); */
	
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

function audit(){
	var yichulinum=0;
	var beizhunum=0;
	var datavalue = "[";
	//var remarkValue = "";
	if($('input[name="ischeck"]:checked').size()>0){
		$('input[name="ischeck"]:checked').each(function(index){
			//$(this).attr("checked",false);
			if($("#ishandle"+$(this).val()).val()==1){
				yichulinum++;
			}
			var id=$(this).val()+"_cwbremark";
			if($("#"+id).val()==0){
				beizhunum++;
				return false;
			}
			//remarkValue = $("#"+id).val();
			//alert(id+"==="+$('#'+id).val());
			/* for(var i = 0 ; i < $("select[name^='reason']").length ; i++){
				datavalue[datavalue.length]=$("select[name^='reason']")[i].value;
			}
			
			for(var i = 0 ; i < $("input[name^='reason']").length ; i++){
				datavalue[datavalue.length]=$("input[name^='reason']")[i].value;
			} */
			datavalue = datavalue +"\""+$('#'+id).val()+"\",";
		});
	}
	if(yichulinum>0){
		alert("已审核的订单不能再次退货再投！！");
		return;
	}
	if(beizhunum>0){
		alert("当前已选择的订单有没有选择备注的，请选择后再执行操作！！");
		return;
	}
	if(datavalue.length==1){
		alert("请选择要退货再投的订单！！");
		return;
	}

	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
	}
	datavalue= datavalue + "]";
	if(datavalue.length>2){
		$.ajax({
			type: "POST",
			url:$("#SubFrom").attr("action"),
			data:{cwbremarks:datavalue},
			dataType:"html",
			success : function(data) {
				alert("成功修改状态："+data.split("_s_")[0]+"单\n订单状态无变动："+data.split("_s_")[1]+"单");
				searchForm.submit();
			}
		});
	}
}

function btnClick(){
	if($("[name='ischeck']").attr("checked")=="checked"||$("[name='ischeck']").attr("checked")=="true"){
		$("[name='ischeck']").removeAttr("checked");
		$("#selectbtn").text("全选");
	}else{
		$("[name='ischeck']").attr("checked","checked");
		$("#selectbtn").text("取消");
	}
}

function exportField(){
	if(<%=obrsList!=null&&obrsList.size()!=0%>){
		$("#btnval").attr("disabled","disabled"); 
	 	$("#btnval").val("请稍后……");
	 	$("#searchForm").attr("action",'<%=request.getContextPath()%>/cwborder/tuihuozaitouexport');
		$("#searchForm").submit();	
		$("#searchForm").attr("action","1");
 }else{
		alert("没有做查询操作，不能导出！");
	} 
}

function check(){
	var len=$.trim($("#cwbs").val()).length;
 	if(len>0)
		{
 		 $("#searchForm").submit();
		return true;
		} 

 	if($("input[name='begindate']").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("input[name='enddate']").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("input[name='begindate']").val()>$("input[name='enddate']").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(!Days()||($("input[name='begindate']").val()=='' &&$("input[name='enddate']").val()!='')||($("input[name='begindate']").val()!='' &&$("input[name='enddate']").val()=='')){
		alert("时间跨度不能大于30天！");
		return false;
	}
   $("#searchForm").submit();
	return true;
}
function Days(){     
	var day1 = $("input[name='begindate']").val();   
	var day2 = $("input[name='enddate']").val();  
	var y1, y2, m1, m2, d1, d2;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	var date1 = new Date(y1, m1, d1);            
	var date2 = new Date(y2, m2, d2);   
	var minsec = Date.parse(date2) - Date.parse(date1);          
	var days = minsec / 1000 / 60 / 60 / 24;  
	if(days>30){
		return false;
	}        
	return true;
}

function resetData(){
	$("#cwbs").val("");
	$("#cwbtypeid").val(0);
	$("#customerid").val(0);
	$("#branchid").val(0);
	$("#strtime").val("");
	$("#endtime").val("");
	$("#auditstate").val(0);
}
$(function(){
	$("#customerid").combobox();
	})
</script>
</HEAD>
<BODY style="background:#f5f5f5"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div>
		<div class="kfsh_tabbtn">
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 ">
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="1" method="POST" id="searchForm">
								<table>
									<tr>
										<td rowspan="2">
											订单号：
											<textarea name="cwbs" rows="3" class="kfsh_text" id="cwbs" ><%=cwbStr %></textarea>
											<input type="hidden" name="isnow" value="1">
										</td>
										<td>
											订单类型:
											<select name ="cwbtypeid" id ="cwbtypeid" class="select1">
												<option  value ="0">全部</option>
													<option value ="<%=CwbOrderTypeIdEnum.Peisong.getValue()%>"><%=CwbOrderTypeIdEnum.Peisong.getText()%></option>
													<%-- <option value ="<%=CwbOrderTypeIdEnum.Shangmentui.getValue()%>"><%=CwbOrderTypeIdEnum.Shangmentui.getText()%></option>  上门退不能退货再投--刘武强 20160920 --%>
													<option value ="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()%>"><%=CwbOrderTypeIdEnum.Shangmenhuan.getText()%></option>
											</select>
											
											客户名称:
											<select name ="customerid" id ="customerid" class="select1">
												<option  value ="0">全部</option>
												<%if(customerList!=null){ %>
													<%for(Customer cus:customerList){ %>
													<option value ="<%=cus.getCustomerid()%>"><%=cus.getCustomername()%></option>
													<%} %>
												<%} %>
											</select>
										</td>
										<td>
											配送站点:
											<input type="text" id="branchid" name="branchid" class="easyui-validatebox" style="width: 130px;"initDataType="TABLE"
												initDataKey="Branch" 
												filterField="sitetype" 
												filterVal="2"
												viewField="branchname" saveField="branchid"/>	
											<%-- <select name ="branchid" id ="branchid" class="select1">
												<option  value ="0">全部</option>
												 <%if(branchList!=null && branchList.size()>0) {%>
													<%for(Branch branch:branchList){ %>
													<option value ="<%=branch.getBranchid()%>"><%=branch.getBranchname()%></option>
													<%} }%>
											</select> --%>
										</td>
									</tr>
									<tr>
										<td align="right">
											退货入库时间:
											<input  type ="text" name="enddate"  class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:140,prompt: '结束时间'" value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>"/>
											<input  type ="text" name="begindate"  class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:140,prompt: '起始时间'" value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>"/>
										</td>
										<td>
											审核状态:
											<select id="auditstate" name="auditstate" >
												<option value="0">待审核</option>
												<option value="1">已审核</option>
											</select>
										</td>
									</tr>
								</table>
								<table>
									<tr>
										<td width="20%">
											<input type="button" onclick="check();"  value="查询" class="input_button2">&nbsp;&nbsp;
											<input type="button" onclick="resetData();" value="重置" class="input_button2">&nbsp;&nbsp;
										</td>
										<td width="20%">
											<input type="button" name="tuizai" id="tuizai" value="退货再投" class="input_button2" onclick="audit()">
											<input name="" type="button" id="btnval" value="导出" class="input_button2"  onclick="exportField();"/>
										</td>
									</tr>
								</table>
							</form>
						</div>
						<div style="height: 390px;overflow: scroll;">
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="40" align="center" valign="middle" bgcolor="#E7F4E3"><a href="#" onclick="btnClick();" id="selectbtn">取消</a></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="80" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="80" align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
									<td width="80" align="center" valign="middle" bgcolor="#E7F4E3">收件人</td>
									<td width="180" align="center" valign="middle" bgcolor="#E7F4E3">收件人地址</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">退货库入库时间</td>
									<td width="80" align="center" valign="middle" bgcolor="#E7F4E3">审核状态</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">配送站点</td>
									<td width="190" align="center" valign="middle" bgcolor="#E7F4E3">备注</td>
									<td width="80" align="center" valign="middle" bgcolor="#E7F4E3">审核人</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">审核时间</td>
								</tr>
								<%if(obrsList!=null){ 
									for(OrderBackRuku cwb :obrsList){ %>
									<tr height="30">
										<td width="40" align="center" valign="middle" >
											<input id="ischeck" name="ischeck" type="checkbox"  checked="checked" value="<%=cwb.getCwb() %>">
										</td>
										<td width="100" align="center" valign="middle" ><%=cwb.getCwb()%></td>
										<td width="80" align="center" valign="middle" ><%=cwb.getCwbordertypename()%></td>
										<td width="80" align="center" valign="middle" ><%=cwb.getCustomername() %></td>
										<td width="80" align="center" valign="middle" ><%=cwb.getConsigneename() %></td>
										<td width="180" align="center" valign="middle" ><%=cwb.getConsigneeaddress() %></td>
										<td width="100" align="center" valign="middle" ><%=cwb.getCreatetime()%></td>
										<td width="80" align="center" valign="middle" ><%=cwb.getAuditstate()==0?"待审核":"已审核" %></td>
										<td width="100" align="center" valign="middle" ><%=cwb.getBranchname()%></td>
										<td width="190" align="center" valign="middle">
										<%if(cwb.getCwbstate()!=CwbStateEnum.TuiHuo.getValue()/* ||cwb.getCwbstate()!=CwbStateEnum.TuiGongYingShang.getValue() */){ %>
												<%=cwb.getRemarkstr() %>
										<input type="hidden" id="<%=cwb.getCwb() %>_cwbremark" name="<%=cwb.getCwb() %>_cwbremark" value="<%=cwb.getCwb() %>_s_0"/>
										<%}else{ %>
											<%if(!"".equals(cwb.getRemarkstr())){ %>
												<input value="<%=cwb.getRemarkstr() %>" />
											<%}else{ %>
												<select name="<%=cwb.getCwb() %>_cwbremark" id="<%=cwb.getCwb() %>_cwbremark">
													<option value="0">请选择退货再投原因</option>
													<%for(Reason r :reasonList) {%>
														<option value="<%=cwb.getCwb() %>_s_<%=r.getReasonid() %>"><%=r.getReasoncontent() %></option>
													<%} %>
												</select>
											<%} }%>
										</td>
										<td width="80" align="center" valign="middle" ><%=cwb.getAuditname() %></td>
										<td width="100" align="center" valign="middle" ><%=cwb.getAudittime() %></td>
									</tr>
									<input type="hidden" id="ishandle<%=cwb.getCwb()%>" value="<%=cwb.getAuditstate()%>"/>
								<%} }%>
								
							</tbody>
						</table>
						</div>
					</div>
					<div style="height:135px;"></div>
					<from action="<%=request.getContextPath() %>/cwborder/auditTuiHuoZaiTou" method="post" id="SubFrom" >
					</from>
				</div>
				<%if(page_obj!=null&&page_obj.getMaxpage()>1){ %>
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
	$("#selectPg").val(<%=request.getAttribute("page")%>);
	$("#cwbtypeid").val(<%=request.getParameter("cwbtypeid")==null?0:Integer.parseInt(request.getParameter("cwbtypeid"))%>);
	$("#customerid").val(<%=request.getParameter("customerid")==null?0:Long.parseLong(request.getParameter("customerid"))%>);
	$("#branchid").val(<%=request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid"))%>);
	$("#auditstate").val(<%=request.getParameter("auditstate")==null?0:Long.parseLong(request.getParameter("auditstate"))%>);
</script>
</BODY>
</HTML>
