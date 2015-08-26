<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<script src="${ctx}/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<%
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<CwbOrderView> applyeditlist = (List<CwbOrderView>)request.getAttribute("applyeditlist");
Page page_obj = (Page)request.getAttribute("page_obj");
String cwbs = request.getParameter("cwb")==null?"":request.getParameter("cwb");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
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


function check(){
	var len=$.trim($("#cwb").val()).length;
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

function btnClick(){
	if($("[name='checkbox']").attr("checked")=="checked"||$("[name='checkbox']").attr("checked")=="true"){
		$("[name='checkbox']").removeAttr("checked");
		$("#selectbtn").text("全选");
	}else{
		$("[name='checkbox']").attr("checked","checked");
		$("#selectbtn").text("取消");
	}
}


//重置反馈通过
function resetfeedbackPass(){
	var cwbdata = "";
	if($('input[name="checkbox"]:checked').length>0){
		$('input[name="checkbox"]:checked').each(function(index){
			//$(this).attr("checked",false);
			cwbdata = cwbdata+$(this).val()+",";
		});
	}
	if(cwbdata.length==0){
		alert("请勾选订单");
		return false;
	}
	if(cwbdata.length>0){
		cwbdata= cwbdata.substring(0, cwbdata.length-1);
		$.ajax({
			type:"post",
			url:"<%=request.getContextPath()%>/applyeditdeliverystate/getCheckboxDealPass",
			data:{"cwbdata":cwbdata},
			dataType:"json",
			success:function(data){
				if(data.errorCode==0){
					//$("#searchForm").attr("action","1");
					alert(data.error);
					$("#searchForm").submit();
					//location.href="<%=request.getContextPath()%>/applyeditdeliverystate/getCheckboxDealPass";
				}else{
					alert(data.error);
					//$("#searchForm").attr("action","1");
					$("#searchForm").submit();
    				//location.href="<%=request.getContextPath()%>/applyeditdeliverystate/getCheckboxDealPass";
				}
			}
		});
	}
}

//重置反馈不通过
function resetfeedbackNoPass(){
	var cwbdata = "";
	if($('input[name="checkbox"]:checked').length>0){
		$('input[name="checkbox"]:checked').each(function(index){
			$(this).attr("checked",false);
			cwbdata = cwbdata+$(this).val()+",";
		})
	}
	if(cwbdata.length>0){
		cwbdata= cwbdata.substring(0, cwbdata.length-1);
		$.ajax({
			type:"post",
			url:"<%=request.getContextPath()%>/applyeditdeliverystate/getCheckboxDealNoPass",
			data:{cwbdata:cwbdata},
			dataType:"json",
			success:function(data){
				if(data.errorCode==0){
					alert(data.error);
					$("#searchForm").submit();
					//location.href="<%=request.getContextPath()%>/applyeditdeliverystate/getCheckboxDealNoPass";
				}else if(data.errorCode==1){
					alert(data.error);
					$("#searchForm").submit();
    				//location.href="<%=request.getContextPath()%>/applyeditdeliverystate/getCheckboxDealNoPass";
				}else if(data.errorCode==2){
					alert(data.error);
					$("#searchForm").submit();
				}
			}
		});
	}
}
function exportExcel(){
	if(<%=applyeditlist!=null&&!applyeditlist.isEmpty()%>){
		$("#btnval").attr("disable","disable");
		$("#searchForm").attr("action","<%=request.getContextPath()%>/applyeditdeliverystate/rfbExportExcel");
		$("#searchForm").submit();
		$("#searchForm").attr("action","1");
	}else{
		alert("未查询结果,无法导出！");
	}
}
function resetData(){
	$("#cwb").val("");
	$("#cwbstate").val(0);
	$("#cwbresultid").val(0);
	$("#isdo").val(0);
	$("#feedbackbranchid").setValue(0);
	$("#cwbtypeid").val(0);
	$("#feedbackbranchid").val(0);
	$("input[typr='hidden' name='begindate']").val('');
	$("input[name='enddate']").val('');
}
</script>
</HEAD>
<BODY style="background:#f5f5f5;overflow: hidden;"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="1" method="post" id="searchForm">
								<table style="font-size: 12px;">
									<tr>
										<td rowspan="2">
											订单号：
										</td>
										<td rowspan="2">
											<textarea style="width: 140px;resize:none;" name="cwb" rows="3" cols="20" class="kfsh_text" id="cwb" ><%=cwbs %></textarea>
										</td>
										<td >
											&nbsp;&nbsp;
											订单状态:
										</td>
										<td >	
											<select style="width: 140px;" name ="cwbstate" id ="cwbstate">
												<option value ="0">全部</option>
												<option value ="<%=CwbStateEnum.PeiShong.getValue()%>"><%=CwbStateEnum.PeiShong.getText() %></option>
												<option value ="<%=CwbStateEnum.TuiHuo.getValue()%>"><%=CwbStateEnum.TuiHuo.getText() %></option>
												<option value ="<%=CwbStateEnum.DiuShi.getValue()%>"><%=CwbStateEnum.DiuShi.getText() %></option>
												<option value ="<%=CwbStateEnum.WuXiaoShuJu.getValue()%>"><%=CwbStateEnum.WuXiaoShuJu.getText() %></option>
												<option value ="<%=CwbStateEnum.TuiGongYingShang.getValue()%>"><%=CwbStateEnum.TuiGongYingShang.getText() %></option>
												<option value ="<%=CwbStateEnum.ZhongZhuan.getValue()%>"><%=CwbStateEnum.ZhongZhuan.getText() %></option>
											</select>
										</td>
										<td>
											&nbsp;&nbsp;
											配送结果:
										</td>	
										<td>
											<select style="width: 140px;" name ="cwbresultid" id ="cwbresultid">
												<option  value ="0">全部</option>
												<option value ="<%=DeliveryStateEnum.PeiSongChengGong.getValue()%>"><%=DeliveryStateEnum.PeiSongChengGong.getText()%></option>
												<option value ="<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>"><%=DeliveryStateEnum.ShangMenTuiChengGong.getText()%></option>
												<option value ="<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue()%>"><%=DeliveryStateEnum.ShangMenHuanChengGong.getText()%></option>
												<option value ="<%=DeliveryStateEnum.JuShou.getValue()%>"><%=DeliveryStateEnum.JuShou.getText()%></option>
												<option value ="<%=DeliveryStateEnum.BuFenTuiHuo.getValue()%>"><%=DeliveryStateEnum.BuFenTuiHuo.getText()%></option>
												<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue()%>"><%=DeliveryStateEnum.FenZhanZhiLiu.getText()%></option>
												<option value ="<%=DeliveryStateEnum.ShangMenJuTui.getValue()%>"><%=DeliveryStateEnum.ShangMenJuTui.getText()%></option>
												<option value ="<%=DeliveryStateEnum.HuoWuDiuShi.getValue()%>"><%=DeliveryStateEnum.HuoWuDiuShi.getText()%></option>
												<option value ="<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()%>"><%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getText()%></option>
												<option value ="<%=DeliveryStateEnum.DaiZhongZhuan.getValue()%>"><%=DeliveryStateEnum.DaiZhongZhuan.getText()%></option>
											</select>
										</td>
										<td>
											&nbsp;&nbsp;
											审核状态:
										</td>
										<td>	
											<select style="width: 140px;" name ="isdo" id ="isdo">
												<option  value ="0">全部</option>
												<option value ="<%=ShenHeStateEnum.daishenhe.getValue() %>"><%=ShenHeStateEnum.daishenhe.getText() %></option>
												<option value ="<%=ShenHeStateEnum.shenhebutongguo.getValue() %>"><%=ShenHeStateEnum.shenhebutongguo.getText() %></option>
												<option value ="<%=ShenHeStateEnum.shenhetongguo.getValue()%>"><%=ShenHeStateEnum.shenhetongguo.getText() %></option>
											</select>
										</td>
									</tr>
									<tr>
										<td>
											&nbsp;&nbsp;
											订单类型:
										</td>
										<td>	
											<select style="width: 140px;" name ="cwbtypeid" id ="cwbtypeid">
												<option  value ="0">全部</option>
												<option value ="<%=CwbOrderTypeIdEnum.Peisong.getValue() %>"><%=CwbOrderTypeIdEnum.Peisong.getText() %></option>
												<option value ="<%=CwbOrderTypeIdEnum.Shangmentui.getValue() %>"><%=CwbOrderTypeIdEnum.Shangmentui.getText() %></option>
												<option value ="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue() %>"><%=CwbOrderTypeIdEnum.Shangmenhuan.getText() %></option>
											</select>
										</td>
										<td>
											&nbsp;&nbsp;
											反馈站点:
										</td>
										<td>	
											<input type="text" id="feedbackbranchid" name="feedbackbranchid" class="easyui-validatebox" style="width: 130px;"initDataType="TABLE"
												initDataKey="Branch" 
												filterField="sitetype" 
												filterVal="2"
												viewField="branchname" saveField="branchid" />	
											<%-- <select style="width: 140px;" name ="feedbackbranchid" id ="feedbackbranchid">
												<option  value ="0">全部</option>
												<%for(Branch br:branchList){ %>
													<option value ="<%=br.getBranchid() %>"><%=br.getBranchname() %></option>
												<%} %>
											</select> --%>
										</td>
										<td>
											&nbsp;&nbsp;
											操作时间:
										</td>
										<td>	
											<input  type ="text" name="enddate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:140,prompt: '结束时间'"/>
											<input  type ="text" name="begindate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:140,prompt: '起始时间'"/>
										</td>
									</tr>
								</table>
								<table>
									<tr>
										<td width="20%" align="left">
											<input type="button" onclick="check();"  value="查询" class="input_button2"  />&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="button" onclick="resetData();" value="重置" class="input_button2" />&nbsp;&nbsp;&nbsp;&nbsp;
										</td>
										<td width="40%" align="center">
											<input type="button" onclick="resetfeedbackPass()" value="审核通过" class="input_button2">&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="button" onclick="resetfeedbackNoPass()" value="审核不通过" class="input_button2">&nbsp;&nbsp;&nbsp;&nbsp;
										</td>
										<td  width="40" align="right">
											<input type="button" id="btnval" onclick="exportExcel();" value="导出" class="input_button2">
										</td>
									</tr>
								</table>
							</form>
						</div>
						<div style="height: 390px;overflow: scroll;" >
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="2%" align="center" valign="middle" bgcolor="#E7F4E3"><a href="#" onclick="btnClick();" id="selectbtn">取消</a></td>
									<td width="6%" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="6%" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="6%" align="center" valign="middle" bgcolor="#E7F4E3">配送结果</td>
									<td width="6%" align="center" valign="middle" bgcolor="#E7F4E3">订单状态</td>
									<td width="6%" align="center" valign="middle" bgcolor="#E7F4E3">结算状态</td>
									<td width="6%" align="center" valign="middle" bgcolor="#E7F4E3">反馈站点</td>
									<td width="6%" align="center" valign="middle" bgcolor="#E7F4E3">反馈人</td>
									<td width="8%" align="center" valign="middle" bgcolor="#E7F4E3">反馈时间</td>
									<td width="6%" align="center" valign="middle" bgcolor="#E7F4E3">操作人</td>
									<td width="8%" align="center" valign="middle" bgcolor="#E7F4E3">操作时间</td>
									<td width="6%" align="center" valign="middle" bgcolor="#E7F4E3">审核状态</td>
									<td width="8%" align="center" valign="middle" bgcolor="#E7F4E3">修改配送结果</td>
									<td width="8%" align="center" valign="middle" bgcolor="#E7F4E3">原因备注</td>
									<td align="6%" valign="middle" bgcolor="#E7F4E3">备注</td>
								</tr>
							</tbody>
							<tbody>
							<%if(applyeditlist!=null){
								for(CwbOrderView aed :applyeditlist){ %>
									<tr height="30">
									<td  width="2%" align="center" valign="middle">
										<input type="checkbox"  name="checkbox" id="checkbox" checked="checked" value="<%=aed.getCwb()%>" />
									</td>
									<td width="2%" align="center" valign="middle" ><%=aed.getCwb() %></td>
									<td width="6%" align="center" valign="middle" ><%=aed.getCwbordertypename() %></td>
									<td width="6%" align="center" valign="middle" ><%=aed.getDeliveryname() %></td>
									<td width="6%" align="center" valign="middle" ><%=aed.getCwbstatename() %></td>
									<!--TODO 结算状态 -->
									<td width="6%" align="center" valign="middle" ></td>
									<td width="6%" align="center" valign="middle" ><%=aed.getBranchname() %></td>
									<td width="6%" align="center" valign="middle" ><%=aed.getResetfeedusername() %></td>
									<td width="8%" align="center" valign="middle" ><%=aed.getResetfeedtime() %></td>
									<td width="6%" align="center" valign="middle" ><%=aed.getDonepeople() %></td>
									<td width="8%" align="center" valign="middle" ><%=aed.getDonetime() %></td>
									<td width="6%" align="center" valign="middle" ><%=aed.getNowState() %></td>
									<td width="8%" align="center" valign="middle" ><%=aed.getRemark2() %></td>
									<td width="8%" align="center" valign="middle" ><%=aed.getRemark3() %></td>
									<td align="6%" valign="middle" ><%=aed.getRemark4() %></td>
									
								</tr>
								<%} }%>
						</tbody>
						</table>
						</div>
					</div>
					<div style="height:130px"></div>
					<br>
					<!-- <from action="./auditTuiGongHuoShangSuccess" method="post" id="SubFrom" >
					</from> -->
				</div>
				<div style="height:40px">
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
	$("#cwbstate").val(<%=request.getParameter("cwbstate")==null?0:Long.parseLong(request.getParameter("cwbstate"))%>);
	$("#cwbresultid").val(<%=request.getParameter("cwbresultid")==null?0:Long.parseLong(request.getParameter("cwbresultid"))%>);
	$("#isdo").val(<%=request.getParameter("isdo")==null?0:Long.parseLong(request.getParameter("isdo"))%>);
	$("#cwbtypeid").val(<%=request.getParameter("cwbtypeid")==null?0:Long.parseLong(request.getParameter("cwbtypeid"))%>);
	$("#feedbackbranchid").val(<%=request.getParameter("feedbackbranchid")==null?0:Long.parseLong(request.getParameter("feedbackbranchid"))%>);
	$("input[name='begindate']").val("<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>");
	$("input[name='enddate']").val("<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>");
</script>
</BODY>
</HTML>
