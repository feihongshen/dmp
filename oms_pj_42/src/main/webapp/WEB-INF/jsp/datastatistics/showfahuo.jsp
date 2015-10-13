<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.ModelEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrderType"%>
<%@page import="cn.explink.domain.DeliveryChuku"%>
<%@page import="cn.explink.domain.KuFangRuKuOrder"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
    Object type=request.getAttribute("type");
    Page page_obj = (Page)request.getAttribute("page_obj");
    List<CwbOrder> kufanglist =null;
    List<KuFangRuKuOrder> rukulist =null;
    List<DeliveryChuku> chukulist = null;
    List<Customer> customerList = request.getAttribute("customerList")==null?null:( List<Customer>)request.getAttribute("customerList");
    if(1==Integer.parseInt(type.toString())){
     	kufanglist = request.getAttribute("showlist")==null?null:(List<CwbOrder>)request.getAttribute("showlist");
    }
    if(2==Integer.parseInt(type.toString())){
    	  rukulist = request.getAttribute("showlist")==null?null:(List<KuFangRuKuOrder>)request.getAttribute("showlist");
    }if(3==Integer.parseInt(type.toString())){
    	chukulist=request.getAttribute("showlist")==null?null:(List<DeliveryChuku>)request.getAttribute("showlist");
    }
    List<Branch> list = request.getAttribute("kufanglist")==null?null:(List<Branch>)request.getAttribute("kufanglist");
    List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
    %>
  <html>
<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script language="javascript"> 
function exportField(){
	if(<%=(kufanglist != null && kufanglist.size()>0)||(rukulist != null && rukulist.size()>0) ||(chukulist != null && chukulist.size()>0)%>){
		$("#btnval").attr("disabled","disabled");
	 	$("#btnval").val("请稍后……");
	 	$.ajax({
			type: "POST",
			url:$("#searchForm2").attr("action"),
			data:$("#searchForm2").serialize(),
			dataType:"json",
			success : function(data) {
				if(data.errorCode==0){
					alert("已进入导出任务队列！");
					$("#downCount",parent.document).html(Number($("#downCount",parent.document).html())+1);
					$.ajax({
						type: "POST",
						url:"<%=request.getContextPath()%>/datastatistics/commitExportExcle",
						data:$("#searchForm2").serialize(),
						dataType:"json",
						success : function(data) {
						}
					});
				}else{
					alert(data.remark);
				}
			}
		});
	 	
	}else{
		alert("没有做查询操作，不能导出！");
	};
}
function backFClick(){
	window.location.href="<%=request.getContextPath()%>/datastatistics/dianshangdanliang";
}
function selectup(){
	$("input:[name=mouldfieldids]").val($("select:[name=mouldfieldids]").val());
}
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
	<div>
		<div class="inputselect_box" style="top: 0px ">
	<form action="1" method="post" id="searchForm">
		&nbsp;
		<input type="button" class="input_button1" id="backF" onclick="window.history.go(-1)" value="返回" />
		<select name="mouldfieldids" id="mouldfieldids" onchange="selectup()" >
			<option value="0" >默认导出模板</option>
			<%for(Exportmould e:exportmouldlist){%>
          	<option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
          	<%} %>
		</select>
		<input type ="button" id="btnval" value="导出" class="input_button1" onclick="exportField();"/>
</form>
</div>
<div style="height:35px"></div>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
			<tr>
				<td valign="top">
					<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="gd_table" >
						<tr class="font_1">
							<td width="100" align="center" bgcolor="#F4F4F4">订单号</td>
							<td align="center" bgcolor="#F4F4F4">供货商</td>
							 <% if(1==Integer.parseInt(type.toString())){%>
							<td bgcolor="#F4F4F4">发货时间</td>
							<%}else if(2==Integer.parseInt(type.toString())){ %>
							<td bgcolor="#F4F4F4">入库时间</td>
							<%} else {%>
							<td bgcolor="#F4F4F4">出库时间</td>
							<%} %>
							<td bgcolor="#F4F4F4">发货库房</td>
							<td bgcolor="#F4F4F4">订单类型</td>
							<% if(1==Integer.parseInt(type.toString())){%>
							<td bgcolor="#F4F4F4">订单当前状态</td>
							<%}%>
							
						</tr>
						 <% if(1==Integer.parseInt(type.toString())){%>
						 <%for(CwbOrder fh: kufanglist){ %>
						 <tr>
							<td align="center" bgcolor="#F4F4F4"><%=fh.getCwb()%></td><!-- 订单号 -->
							<td align="center"><div id="u123"><!-- 供货商 -->
								<%for(Customer c:customerList){
									if(c.getCustomerid()==fh.getCustomerid()){%>
									<p><%=c.getCustomername()%></p>
								<%	}
									
								} %>
							</div></td>
							<td><div id="u127">
								<p><%=fh.getEmaildate() %></p><!-- 时间 -->
							</div></td>
							<td>
					<%for(Branch b:list){
									if(b.getBranchid()==Integer.valueOf(fh.getCarwarehouse())){%><!-- 发货库房 -->
									<p><%=b.getBranchname()%></p>
								<%	}
									
								} %>
							</td>
							<td>
							<%=CwbOrderTypeIdEnum.getByValue(Integer.valueOf(fh.getCwbordertypeid())).getText()%>
							</td>
							<td><%=FlowOrderTypeEnum.getText((int)fh.getFlowordertype()).getText()%></td>
						</tr>
						 
							<%}}else if(2==Integer.parseInt(type.toString())){ %>
							<%for(KuFangRuKuOrder fh: rukulist){ %>
						 <tr>
							<td align="center" bgcolor="#F4F4F4"><%=fh.getCwb()%></td><!-- 订单号 -->
							<td align="center"><div id="u123">
								<%for(Customer c:customerList){
									if(c.getCustomerid()==fh.getCustomerid()){%>
									<p><%=c.getCustomername()%></p>
								<%	}
									
								} %>
							</div></td>
							<td><div id="u127">
								<p><%=fh.getIntowarehousetime() %></p><!-- 时间 -->
							</div></td>
							<td>
					<%for(Branch b:list){
									if(b.getBranchid()==fh.getIntobranchid()){%>
									<p><%=b.getBranchname()%></p>
								<%	}
									
								} %>
							</td>
							<td>
							<%=CwbOrderTypeIdEnum.getByValue((int)fh.getCwbordertypeid()).getText()%>
							</td>
						</tr>
							<%} }else {%>
							<%for(DeliveryChuku fh: chukulist){ %>
						 <tr>
							<td align="center" bgcolor="#F4F4F4"><%=fh.getCwb()%></td><!-- 订单号 -->
							<td align="center"><div id="u123">
								<%for(Customer c:customerList){
									if(c.getCustomerid()==fh.getCustomerid()){%>
									<p><%=c.getCustomername()%></p>
								<%	}
									
								} %>
							</div></td>
							<td><div id="u127">
								<p><%=fh.getOutstoreroomtime() %></p><!-- 时间 -->
							</div></td>
							<td>
					<%for(Branch b:list){
									if(b.getBranchid()==fh.getStartbranchid()){%>
									<p><%=b.getBranchname()%></p>
								<%	}
									
								} %>
							</td>
							<td>
							<%=CwbOrderTypeIdEnum.getByValue(Integer.valueOf(fh.getCwbordertypeid())).getText()%>
							</td>
						</tr>
							<%} }%>
						
						<!-- <tr>
							<td align="center" bgcolor="#F4F4F4">2014-03-31</td>
							<td align="center"><div id="u123">
								<p>凡客</p>
							</div></td>
							<td><div id="u127">
								<p>2014-04-01</p>
							</div></td>
							<td>朝阳区</td>
							<td>配送</td>
							<td>审核中</td>
						</tr>
						 -->
					</table></td>
			</tr>
		</table>
		<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:$('#searchForm1').attr('action','1');$('#searchForm1').submit();" >第一页</a>　
					<a href="javascript:$('#searchForm1').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm1').submit();">上一页</a>　
					<a href="javascript:$('#searchForm1').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm1').submit();" >下一页</a>　
					<a href="javascript:$('#searchForm1').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm1').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchForm1').attr('action',$(this).val());$('#searchForm1').submit()">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>"><%=i %></option>
							<% } %>
						</select>页
				</td>
			</tr>
		</table>
	</div><%} %>
		<form action="<%=request.getContextPath()%>/datastatistics/exportExcle" method="post" id="searchForm2">
	<input type="hidden" name="select" id="select" value="<%=type%>"/>
	<input type="hidden" name="sign" id="sign" value="<%=ModelEnum.DanliangChaxun.getValue() %>"/>
	<input type="hidden" name="timer" id="list" value="<%=request.getAttribute("timer")==null?null:(String)request.getAttribute("timer")%>">
	<input type="hidden" name="customerid" id="list" value="<%=request.getAttribute("customerid")==null?null:(Long)request.getAttribute("customerid")%>">
	<input type="hidden" name="kufangid" id="kufangid" value="<%=request.getAttribute("kufangid")==null?null:(Long)request.getAttribute("kufangid")%>">
	<input type="hidden" name="mouldfieldids" id="mouldfieldids" value="<%=request.getAttribute("mouldfieldids")==null?"0":(String)request.getAttribute("mouldfieldids")%>"/>
</form>
<form action="1" method="post" id="searchForm1">
	<input type="hidden" name="select" id="select" value="<%=type%>"/>
	<input type="hidden" name="searchtime" id="list" value="<%=request.getAttribute("timer")==null?null:(String)request.getAttribute("timer")%>">
	<input type="hidden" name="searchcustid" id="searchcustid" value="<%=request.getAttribute("customerid")==null?null:(Long)request.getAttribute("customerid")%>">
	<input type="hidden" name="searchbranchid" id="searchbranchid" value="<%=request.getAttribute("kufangid")==null?null:(Long)request.getAttribute("kufangid")%>">
	<input type="hidden" name="mouldfieldids" id="mouldfieldids" value="<%=request.getAttribute("mouldfieldids")==null?"0":(String)request.getAttribute("mouldfieldids")%>"/>
</form>
		<!--翻页 -->
		
									
							
	</div>
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>