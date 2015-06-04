<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.ShiXiao"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> customerList = request.getAttribute("customerList")==null?null:(List<Customer>)request.getAttribute("customerList");
List<User> userList = request.getAttribute("userList")==null?null:(List<User>)request.getAttribute("userList");
List customeridlist=(List)request.getAttribute("customeridlist");
List<ShiXiao> shixiaoList = (List<ShiXiao>)request.getAttribute("shixiaoList");
String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
long flowordertype=(request.getParameter("flowordertype")==null?-1:Long.parseLong(request.getParameter("flowordertype")));
long cwbstate=(request.getParameter("cwbstate")==null?-1:Long.parseLong(request.getParameter("cwbstate")));
long userid=(request.getParameter("userid")==null?-1:Long.parseLong(request.getParameter("userid")));
Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
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
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$("#strtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择发货客户' });

});

$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox ul li").eq(index).show().siblings().hide();
	});
	
})


function exportField(){
	if(<%=shixiaoList!=null&&shixiaoList.size()!=0%>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval").attr("disabled","disabled"); 
	 	$("#btnval").val("请稍后……");
		$("#searchForm2").submit();	
	}else{
		alert("没有做查询操作，不能导出！");
	}
}


function checkParam(){
	if($("#strtime").val()==""&&$("#endtime").val()==""&&$("#cwbs").val()==""&&$("#cwbstate").val()==-1&&$("#flowordertype").val()==-1&&$("#userid").val()==-1&&$(".checked [name='customerid']").length==0){
	alert("请选择任一条件");
	}else{
		$("#isnow").val("1");
		$("#searchForm").submit();
	}
}

function clearMsg(){
	$("#cwbs").val("");
	$("#cwbstate").val(-1);
	$("#flowordertype").val(-1);
	$("#strtime").val("");
	$("#endtime").val("");
	$("#userid").val(-1);
	$("#customerid").val(0);
}

</script>
</HEAD>
<BODY style="background:#f5f5f5"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF"  class="inputselect_box">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath() %>/cwborder/losecwbBatch">数据失效</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/selectlosecwb/1" class="light">数据失效查询</a></li>
			</ul>
		</div>
		<div class="tabbox">
			<div style="position:relative; z-index:0 " >
				<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
					<div class="kfsh_search">
						<form action="<%=request.getContextPath() %>/cwborder/selectlosecwb/1" method="POST" id="searchForm">
							<table  width="100%" border="0" cellspacing="0" cellpadding="0" style="height:100px;font-size: 12px;">
							<tr>
							<tr>
								<td  rowspan="2">
							订单号：
							<textarea name="cwbs" rows="3" class="kfsh_text" id="cwbs"><%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs")%></textarea>
								</td>
								<td>
								订单状态:<select id="cwbstate" name="cwbstate" class="select1">
									<option value="-1">请选择操作状态</option>
									<option value="<%=CwbStateEnum.WUShuju.getValue() %>" <%if(cwbstate==CwbStateEnum.WUShuju.getValue()){ %>selected<%}%>><%=CwbStateEnum.WUShuju.getText() %></option>
									<option value="<%=CwbStateEnum.PeiShong.getValue() %>" <%if(cwbstate==CwbStateEnum.PeiShong.getValue()){ %>selected<%}%>><%=CwbStateEnum.PeiShong.getText() %></option>
									<option value="<%=CwbStateEnum.TuiHuo.getValue() %>" <%if(cwbstate==CwbStateEnum.TuiHuo.getValue()){ %>selected<%}%>><%=CwbStateEnum.TuiHuo.getText() %></option>
									<option value="<%=CwbStateEnum.DiuShi.getValue() %>" <%if(cwbstate==CwbStateEnum.DiuShi.getValue()){ %>selected<%}%>><%=CwbStateEnum.DiuShi.getText() %></option>
									<option value="<%=CwbStateEnum.WuXiaoShuJu.getValue() %>" <%if(cwbstate==CwbStateEnum.WuXiaoShuJu.getValue()){ %>selected<%}%>><%=CwbStateEnum.WuXiaoShuJu.getText() %></option>
									<option value="<%=CwbStateEnum.TuiGongYingShang.getValue() %>" <%if(cwbstate==CwbStateEnum.TuiGongYingShang.getValue()){ %>selected<%}%>><%=CwbStateEnum.TuiGongYingShang.getText() %></option>
									<option value="<%=CwbStateEnum.ZhongZhuan.getValue() %>" <%if(cwbstate==CwbStateEnum.ZhongZhuan.getValue()){ %>selected<%}%>><%=CwbStateEnum.ZhongZhuan.getText() %></option>
									
								</select>
								</td>
								<td>
				订单操作状态:
				<select name ="flowordertype" id ="flowordertype" class="select1">
				<option value ="-1">请选择订单状态</option>
				<option value="<%=FlowOrderTypeEnum.DaoRuShuJu.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.DaoRuShuJu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.DaoRuShuJu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.TiHuo.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.TiHuo.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.TiHuo.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.RuKu.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.RuKu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.RuKu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.FenZhanLingHuo.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.FenZhanLingHuo.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.ChuKuSaoMiao.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.ChuKuSaoMiao.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.TuiHuoChuZhan.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.TuiHuoChuZhan.getValue()){ %>selected<%}%> ><%=FlowOrderTypeEnum.TuiHuoChuZhan.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() %>" <%if(flowordertype==FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.YiShenHe.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.YiShenHe.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.YiShenHe.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.YiFanKui.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.YiFanKui.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.YiFanKui.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.PosZhiFu.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.PosZhiFu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.PosZhiFu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.CheXiaoFanKui.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.CheXiaoFanKui.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.CheXiaoFanKui.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.YiChangDingDanChuLi.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.YiChangDingDanChuLi.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.YiChangDingDanChuLi.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.DingDanLanJie.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.DingDanLanJie.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.DingDanLanJie.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue() %>"  <%if(flowordertype==FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.ShenHeWeiZaiTou.getText() %></option>
			</select>
				&nbsp;&nbsp;&nbsp;&nbsp;失效时间:
				<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>" class="input_text1"/>
				到
				<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>" class="input_text1"/>
							</td>
							</tr>
							<tr>
							<td>
								操            作          人:<select id="userid" name="userid" class="select1">
									<option value="-1">请选择操作人</option>
									<%if(userList!=null){ %>
									<%for(User user:userList){ %>
										<option value="<%=user.getUserid() %>" <%if(userid==user.getUserid()){%> selected="selected" <%} %>><%=user.getRealname() %></option>
									<%} %>
									<%} %>
								</select>
							</td>
							<td>
								发          货            客        户:
							 <select name ="customerid" id ="customerid" multiple="multiple" style="width: 150px;" class="select1">
		         			 <%for(Customer c : customerList){ %>
		         				  <option value ="<%=c.getCustomerid() %>" 
		          						  <%if(!customeridlist.isEmpty()) 
			          					  {for(int i=0;i<customeridlist.size();i++){
			            			if(c.getCustomerid()== new Long(customeridlist.get(i).toString())){
			            				%>selected="selected"<%
			          		  	 break;
			        		    	}
			            }
				     }%>><%=c.getCustomername() %></option>
		          <%} %>
		        </select>
								[<a href="javascript:multiSelectAll('customerid',1,'请选择发货客户');">全选</a>]
								[<a href="javascript:multiSelectAll('customerid',0,'请选择发货客户');">取消全选</a>] 
								</td>
							</tr>
							</tr>
							</table>
						
							<input type="hidden" id="isnow" name="isnow" value=""/>
							<input type="button" value="确定" class="input_button2" onclick="checkParam();">&nbsp;<input type="button" value="重置 "  onclick="clearMsg();" class="input_button2" />
						</form>
					</div>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
						<tbody>
							<tr class="font_1" height="30" >
								<td width="10%" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
								<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
								<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3">发货批次</td>
								<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3">订单状态</td>
								<td width="15%" align="center" valign="middle" bgcolor="#E7F4E3">订单操作状态</td>
								<td width="15%" align="center" valign="middle" bgcolor="#E7F4E3">操作时间</td>
								<td align="center" valign="middle" bgcolor="#E7F4E3">操作人</td>
							</tr>
							
						</tbody>
					</table>
				</div>
				<br>
				<br>
				<br>
				<br>
				<br>
				
				<div style="height:100px"></div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<tbody>
					<%if(shixiaoList!=null&&shixiaoList.size()!=0){for(ShiXiao shixiao:shixiaoList){%>
						
						<tr height="30" >
							<td width="10%" align="center" valign="middle"><%=shixiao.getCwb() %></td>
							<td width="10%" align="center" valign="middle"><%if(customerList!=null){for(Customer c : customerList){if(shixiao.getCustomerid()==c.getCustomerid()){%><%=c.getCustomername() %><%}}} %></td>
							<td width="10%" align="center" valign="middle"><%=shixiao.getEmaildate()==null?"":shixiao.getEmaildate()%></td>
							<td width="10%" align="center" valign="middle"><%for(CwbStateEnum ft : CwbStateEnum.values()){if(shixiao.getCwbstate()==ft.getValue()){%><%=ft.getText() %><%}} %></td>
							<td width="15%" align="center" valign="middle"><%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){if(shixiao.getFlowordertype()==ft.getValue()){%><%=ft.getText() %><%}} %></td>
							<td width="15%" align="center" valign="middle"><%=shixiao.getCretime() %></td>
							<td align="center" valign="middle"><%if(userList!=null){for(User u : userList){if(shixiao.getUserid()==u.getUserid()){ %><%=u.getRealname() %><%}}} %></td>
						</tr>
						<%}} %>
					</tbody>
				</table>
				<div style="height:40px"></div>
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
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</BODY>
</HTML>
