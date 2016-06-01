<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbDetailView> weirukuList = (List<CwbDetailView>)request.getAttribute("weirukulist");
List<CwbDetailView> yirukulist = (List<CwbDetailView>)request.getAttribute("yirukulist");
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
List<User> uList = (List<User>)request.getAttribute("userList");
Switch ck_switch = (Switch) request.getAttribute("ck_switch");
List<JSONObject> objList = request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
long customerid=request.getParameter("customerid")==null?0:Long.parseLong(request.getParameter("customerid"));
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>入库扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var promt=${promt};
	<%-- $(function(){
		getcwbsdataForCustomer('<%=request.getContextPath() %>','-1','');
		getcwbsquejiandataForCustomer('-1');
		
		$("#cwbs").focus();
		$("#updateswitch").click(function(){
			var switchstate = "rkbq_02";
			if($("#updateswitch").attr("checked")=="checked"){
				switchstate = "rkbq_01";
				$("#rk_switch").val("rkbq_01");
			}else{
				switchstate = "rkbq_02";
				$("#rk_switch").val("rkbq_02");
			}
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/switchcontroller/updateswitch?switchstate="+switchstate,
				dataType:"json",
				success : function(data) {
					if(data.errorCode==1){
						alert(data.error);
					}
				}
			});
		});
	}); --%>
	var data;
	var startIndex=0;
	var step=4;
	var preStep;
	function initEmailDateUI(emaildate){
		 $.ajax({
			 type: "POST",
				url:"<%=request.getContextPath()%>/emaildate/getEmailDateList",
				data:{customerids:$("#customerid").val(),state:"-1"},
				success:function(optionData){
					data=optionData;
					var optionstring="";
					var high ="";
					
					for(var j=0;j<data.length;j++){
						if(data[j].emaildateid==emaildate){
							preStep=j;
						}
					}
					
					moreOpt();
					
					//$("#emaildate").html(optionstring);
					if(emaildate){
						$("#emaildate").val(emaildate);
					}
				
				}
			});
	}
		function moreOpt(){
			step=startIndex+4;
			if(preStep>step){
				step=preStep;
			}
			for(var i=startIndex;i<data.length;i++){
				if(i>step){
					continue;
				}
				optionstring="<option value='"+data[i].emaildateid+"'>"+
				data[i].emaildatetime+(data[i].state==0?"（未到货）":"")+" "+
				data[i].customername+"_"+data[i].warehousename+"_"+data[i].areaname
				+"</option>";
				var opt=$(optionstring);
				$("#emaildate").append(opt);
				startIndex=i+1;
			}
		}
	var emaildate=0;
	$(function(){
		$("#more").click(moreOpt);
		var msg="";
		$.each(promt, function(i, item) {
           msg+=item.cwb+item.emaildatename+"\n";
        });
		if(msg){
			alert(msg);
		}
		$("#finish").click(function(){
			/* if($("#customerid").val()==-1)
				{
				alert("请选择供货商！");
				return false;
				} */
			$("#cwbintoform").submit();
		});
		emaildate=GetQueryString("emaildate");
		if(!emaildate){
			emaildate=0;
		}
		initEmailDateUI(emaildate);
		var $menuli = $(".saomiao_tab ul li");
		$menuli.click(function(){
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
	});

	$(function(){
		var $menuli1 = $("#bigTag li");
		$menuli1.click(function(){
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
		});
		
		var $menuli2 = $("#smallTag li");
		$menuli2.click(function(){
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli2.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
		
	})
	
	function focusCwb(){
		$("#cwbs").focus();
	}
	function tabView(tab){
		$("#"+tab).click();
	}
	
	function addAndRemoval(cwb,tab,isRemoval,customerid){
		var trObj = $("#ViewList tr[id='TR"+cwb+"']");
		if(isRemoval){
			$("#"+tab).append(trObj);
		}else{
			$("#ViewList #errorTable tr[id='TR"+cwb+"error']").remove();
			trObj.clone(true).appendTo("#"+tab);
			$("#ViewList #errorTable tr[id='TR"+cwb+"']").attr("id",trObj.attr("id")+"error");
		}
		//已入库明细只显示该供货商明细、异常单明细只显示该供货商明细
		if(customerid>0){
			$("#successTable tr").hide();
			$("#successTable tr[customerid='"+customerid+"']").show();
			
			$("#errorTable tr").hide();
			$("#errorTable tr[customerid='"+customerid+"']").show();
		}else{
			$("#successTable tr").show();
			$("#errorTable tr").show();
		}
	}

	//得到当前入库的供应商的库存量
	function getcwbsdataForCustomer(pname, customerid, cwb) {
		$.ajax({
			type : "POST",
			url : pname + "/PDA/getZhongZhuanZhanInSum",
			data : {
				"customerid" : customerid,
				"cwb" : cwb
			},
			dataType : "json",
			success : function(data) {
				$("#rukukucundanshu").html(data.weirukucount);
				$("#rukukucunjianshu").html(data.weirukusum);
				$("#successcwbnum").html(data.yirukunum);
			}
		});
		
		//未入库明细、已入库明细、异常单明细只显示该供货商明细
		/* if(customerid>0){
			$("#weirukuTable tr").hide();
			$("#weirukuTable tr[customerid='"+customerid+"']").show();
			
			$("#successTable tr").hide();
			$("#successTable tr[customerid='"+customerid+"']").show();
			
			$("#errorTable tr").hide();
			$("#errorTable tr[customerid='"+customerid+"']").show();
		}else{
			$("#weirukuTable tr").show();
			$("#successTable tr").show();
			$("#errorTable tr").show();
		} */
	}
	
	
	//得到入库缺货件数的统计
	function getcwbsquejiandataForCustomer(customerid) {
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getZhongZhuanZhanInQueSum",
			data : {
				"customerid" : customerid
			},
			dataType : "json",
			success : function(data) {
				$("#lesscwbnum").html(data.lesscwbnum);
			}
		});
	}
	
	//得到入库缺货件数的list列表
	function getrukucwbquejiandataList(customerid){
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getZhongZhuanZhanInQueList",
			data : {
				"customerid" : customerid
			},
			dataType : "html",
			success : function(data) {
				$("#lesscwbTable").html(data);
			}
		});
		
	}

	<%--if (rk_switch == "rkbq_01") {
		$("#printcwb",parent.document).attr("src",pname + "/printcwb?scancwb="+ scancwb + "&a="+ new Date());
	}--%>
	
function exportField(flag,customerid){
	var cwbs = "";
	$("#expemailid").val("0");
	if(flag==1){
		$("#type").val("weiruku");
		$("#searchForm3").submit();
	}else if(flag==2){
		$("#type").val("yiruku");
		$("#searchForm3").submit();
	}else if(flag==3){//修改导出问题
		/* if(customerid>0){
			$("#errorTable tr[customerid='"+customerid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{  */
			$("#errorTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		 /* }  */
		if(cwbs.length>0){
			cwbs = cwbs.substring(0, cwbs.length-1);
		}
		if(cwbs!=""){
			$("#exportmould2").val($("#exportmould").val());
			$("#exportcwbs").val(cwbs);
			$("#btnval").attr("disabled","disabled");
		 	$("#btnval").val("请稍后……");
		 	$("#searchForm2").submit();
		}else{
			alert("没有订单信息，不能导出！");
		};
	}else if(flag==4){
		$("#type").val("ypdj");
		$("#searchForm3").submit();
	}
	
}

var weipage=1;var yipage=1;
function weiruku(){
	weipage+=1;
	$.ajax({
		url:'<%=request.getContextPath()%>/PDA/getimportweirukulist',
		type:"post",
		data:{"page":weipage,"customerid":$("#customerid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%}else{ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%} %>
				}
				$("#weiruku").remove();
				$("#weirukuTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="weiruku();" id="weiruku">查看更多</td></tr>';
				$("#weirukuTable").append(more);
				}
			}
		}
	});
}

function yiruku(){
	yipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getimportyiruku",
		data:{"page":yipage,"customerid":$("#customerid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%}else{ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%} %>
				}
				$("#yiruku").remove();
				$("#successTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="yiruku();" id="yiruku">查看更多</td></tr>'
				$("#successTable").append(more);
				}
			}
		}
	});
	
}

function tohome(){
	window.location.href="<%=request.getContextPath() %>/PDA/cwbChangeintowarhouseBatch?customerid="+$("#customerid").val();
}

$(function(){
	$("#customerid").combobox();
})

</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box2">

	<div class="saomiao_tab2">
		<ul>
			<li><a href="<%=request.getContextPath()%>/PDA/changeintowarhouse" >逐单操作</a></li>		
			<li><a href="#" class="light" >批量操作</a></li>
		</ul>
	</div>

	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>未入库合计</dt>
			<dd style="cursor:pointer" onclick="tabView('table_weiruku')" id="rukukucundanshu">${count }</dd>
		</dl>
		<dl class="yellow">
			<dt>未入库件数合计</dt>
			<dd id="rukukucunjianshu">${sum }</dd>
		</dl>
		
		<dl class="green">
			<dt>已入库</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yiruku')" id="successcwbnum" name="successcwbnum">${thissuccess }</dd>
		</dl>
		
		
		<dl class="yellow">
			<dt>一票多件缺货件数</dt>
			<dd style="cursor:pointer" onclick="tabView('table_quejian');getrukucwbquejiandataList($('#customerid').val());"  id="lesscwbnum" name="lesscwbnum" >${lesscwbnum }</dd>
		</dl>
		<br clear="all">
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<form id="cwbintoform" action="<%=request.getContextPath() %>/PDA/cwbChangeintowarhouseBatch" method="post">
			<div class="saomiao_selet2">
				客户：
				<select id="customerid" name="customerid" onchange="tohome();" class="select1">
					<option value="-1" selected>全部供应商</option>
					<%for(Customer c : cList){ %>
						<option value="<%=c.getCustomerid() %>"  <%if(customerid==c.getCustomerid()){ %> selected=selected <%} %> ><%=c.getCustomername() %></option>
					<%} %>
				</select>
				<%-- 发货批次：
				<select id="emaildate" name="emaildate" onchange="tohome();" style="height: 20px;width: 280px">
					<option value='0' id="option2">请选择(供货商_供货商仓库_结算区域)</option> 
				</select>
				<a href="#" id="more" style="color:#222222">更多</a>
				驾驶员：
				<select id="driverid" name="driverid">
					<option value="-1" selected>请选择</option>
					<%for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
					<%} %>
				</select> --%>
			</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<%-- <p>
						打印标签：<input type="checkbox" id="updateswitch" name="updateswitch" <%if(ck_switch.getState().equals("rkbq_01")){ %>checked="checked"<%} %>/>
					</p> --%>
					<p><span>订单号：</span>
						<textarea cols="24" rows="4"  name ="cwbs" id="cwbs" ></textarea>
					</p>
					<span>&nbsp;</span><input type="button" name="finish" id="finish" value="确认批量入库" class="button" />
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" >${msg }</p>
				</div>
			        <input type="hidden" id="rk_switch" name="rk_switch" value="<%=ck_switch.getState() %>" />
			</div></form>
		</div>
	</div>



		<div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px"></span>
				<ul id="smallTag">
					<li><a id="table_weiruku" href="#" class="light">未入库明细</a></li>
					<li><a id="table_yiruku" href="#">已入库明细</a></li>
					<li><a id="table_quejian" href="#" onclick='getrukucwbquejiandataList($("#customerid").val());'>一票多件缺件</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li>
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(1,$("#customerid").val());'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="weirukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbDetailView co : weirukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" class="cwbids">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=co.getCustomername() %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
											<%if(weirukuList!=null&&weirukuList.size()==Page.DETAIL_PAGE_NUMBER){ %>
												<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="weiruku();" id="weiruku">查看更多</td></tr>
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(2,$("#customerid").val());'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%-- <%if(objList!=null)for(JSONObject obj : objList){if(obj.get("errorcode").equals("000000")){ %>
											<%JSONObject cwbOrder =  obj.get("cwbOrder")==null?null:JSONObject.fromObject(obj.get("cwbOrder"));%>
											<tr id="TR<%=obj.get("cwb") %>" cwb="<%=obj.get("cwb") %>" customerid="<%=cwbOrder==null?"":cwbOrder.getString("customerid") %>">
												<td width="120" align="center"><%=obj.get("cwb") %></td>
												<td width="100" align="center"><%=obj.getString("customername") %></td>
												<td width="140" align="center"><%=cwbOrder==null?"":cwbOrder.getString("emaildate") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getString("consigneename") %></td>
												<td width="100" align="center"><%=cwbOrder==null?0.00:cwbOrder.getDouble("receivablefee") %></td>
												<td align="left"><%=cwbOrder==null?"":cwbOrder.getString("consigneeaddress") %></td>
											</tr>
											<%}} %> --%>
											<%for(CwbDetailView co : yirukulist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=co.getCustomername() %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
											<%if(yirukulist!=null&&yirukulist.size()==Page.DETAIL_PAGE_NUMBER){ %>
											<tr  aglin="center"><td colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="yiruku();" id="yiruku">查看更多</td></tr>
										<%} %>	
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(4,$("#customerid").val());'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="120" align="center" bgcolor="#f1f1f1">运单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="lesscwbTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
											<%-- <%for(JSONObject obj : quejianList){ %>
											<%
												String transcwb = ""; 
												if(obj.getString("transcwb").indexOf("explink")>-1){
													
												}else if(obj.getString("transcwb").indexOf("havetranscwb")>-1){
													transcwb = obj.getString("transcwb").split("_")[1];
												}
											%>
											<tr id="TR<%=obj.getString("cwb") %>" cwb="<%=obj.getString("cwb") %>" customerid="<%=obj.getLong("customerid") %>">
											<td width="120" align="center"><%=obj.getString("cwb") %></td>
											<td width="120" align="center"><%=transcwb %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==obj.getLong("customerid")){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=obj.getString("emaildate") %></td>
											<td width="100"><%=obj.getString("consigneename") %></td>
											<td width="100"><%=obj.getDouble("receivablefee") %></td>
											<td align="left"><%=obj.getString("consigneeaddress") %></td>
											</tr>	
										<%} %> --%>
										
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(3,$("#customerid").val());'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td width="350" align="center" bgcolor="#f1f1f1">地址</td>
											<td align="center" bgcolor="#f1f1f1">异常原因</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
											<%if(objList!=null)for(JSONObject obj : objList){if(!obj.get("errorcode").equals("000000")){ %>
											<%JSONObject cwbOrder =  obj.get("cwbOrder")==null?null:JSONObject.fromObject(obj.get("cwbOrder"));%>
											<tr id="TR<%=obj.get("cwb") %>" cwb="<%=obj.get("cwb") %>" customerid="<%=cwbOrder==null?"":cwbOrder.getString("customerid") %>">
												<td width="120" align="center"><%=obj.get("cwb") %></td>
												<td width="100" align="center"><%=obj.getString("customername") %></td>
												<td width="140" align="center"><%=cwbOrder==null?"":cwbOrder.getString("emaildate") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getString("consigneename") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getDouble("receivablefee") %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=obj.get("showRemark") %></td>
												<%} %>
												
												<td width="350" align="left"><%=cwbOrder==null?"":cwbOrder.getString("consigneeaddress") %></td>
												<td align="center"><%=obj.get("errorinfo") %></td>
											</tr>
											<%}} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
			</div>
		</div>
		
	<form action="<%=request.getContextPath()%>/PDA/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="cwbs" id="exportcwbs" value=""/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
	</form>
	<form action="<%=request.getContextPath() %>/PDA/exportByCustomerid" method="post" id="searchForm3">
		<input  type="hidden"  name="customerid" value="<%=request.getParameter("customerid")==null?"0":request.getParameter("customerid")%>" id="expcustomerid" />
		<input type="hidden" name="type" value="" id="type"/>
		<input  type="hidden"  name="emaildate" value='0' id="expemailid" />
	</form>
	</div>
</body>
</html>
