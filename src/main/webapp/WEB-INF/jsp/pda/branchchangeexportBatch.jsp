<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> bList = request.getAttribute("branchlist")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchlist");
List<User> uList = request.getAttribute("userList")==null?new ArrayList<User>():(List<User>)request.getAttribute("userList");
List<Truck> tList = request.getAttribute("truckList")==null?new ArrayList<Truck>():(List<Truck>)request.getAttribute("truckList");
List<Bale> balelist = request.getAttribute("balelist")==null?new ArrayList<Bale>():(List<Bale>)request.getAttribute("balelist");
List<Reason> reasonlist = request.getAttribute("reasonlist")==null?null:(List<Reason>)request.getAttribute("reasonlist");
Map usermap = (Map) session.getAttribute("usermap");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>中转出站批量扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
	$(function(){
		getOutSum('<%=request.getContextPath()%>',0);
		 $("#cwbs").focus();
	});
	//得到当前出库的站点的库存量
	function getOutSum(pname,branchid){
		$.ajax({
			type: "POST",
			url:pname+"/PDA/getOutSum?nextbranchid="+branchid,
			dataType:"json",
			success : function(data) {
				$("#chukukucundanshu").html(data.weichukucount);
			}
		});
	}
/**
 * 出库扫描
 */

var alloutnum = 0;

function exportWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag,reasonid){
	if(<%=bList.isEmpty()%>){
		alert("抱歉，系统未分配中转站点，请联络您公司管理员！");
		return;
	}
	if(scancwb.length>0){
		var successnum = 0,errorcwbnum = 0;
		$.ajax({
			type: "POST",
			url:pname+"/PDA/cwbexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno+"&reasonid="+reasonid,
			dataType:"json",
			success : function(data) {
				$("#scancwb").val("");
				if(data.statuscode=="000000"){
					<%-- if(data.body.cwbOrder.nextbranchid!=0&&data.body.cwbOrder.nextbranchid!=branchid&&branchid!=-1){
						errorcwbnum += 1;
						if(ck_switch=="ck_01"){//允许出库
							var con = confirm("出库站点与分拣站点不一致，是否强制出库？");
							if(con == false){
								errorcwbnum += 1;
								$("#excelbranch").html("");
								$("#showcwb").html("");
								$("#zhongzhuan").hide();
								
								$("#msg").html(scancwb+"         （异常扫描）出库站点与分拣站点不一致");
								$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/fail.wav&a="+Math.random());
							}else{
								$.ajax({
									type: "POST",
									url:pname+"/PDA/cwbexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag=1&requestbatchno="+requestbatchno+"&baleno="+baleno+"&reasonid="+reasonid,
									dataType:"json",
									success : function(data) {
										if(data.statuscode=="000000"){
											if(data.body.cwbOrder.scannum==1){
												successnum += 1;
											}
											
											$("#zhongzhuan").show();
											if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Peisong.getValue()%>){
												$("#cwbordertype").html("订单类型：配送订单");
												$("#fee").html("应收金额："+data.body.cwbOrder.receivablefee);
											}else if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()%>){
												$("#cwbordertype").html("订单类型：上门换订单");
												$("#fee").html("应收金额："+data.body.cwbOrder.receivablefee);
											}else if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Shangmentui.getValue()%>){
												$("#cwbordertype").html("订单类型：上门退订单");
												$("#fee").html("应退金额："+data.body.cwbOrder.paybackfee);
											}
											$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
											$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
											
											$("#branchid").val(data.body.cwbOrder.nextbranchid);
											$("#scansuccesscwb").val(scancwb);
											$("#showcwb").html("订 单 号："+scancwb);
											getOutSum(pname,data.body.cwbOrder.nextbranchid);
											if(data.body.cwbOrder.sendcarnum>1){
												document.ypdj.Play();
											}
											if(data.body.cwbbranchnamewav!=pname+"/wav/"){
												$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
											}else{
												$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
											}
										}else{
											errorcwbnum += 1;
											$("#excelbranch").html("");
											$("#showcwb").html("");
											$("#zhongzhuan").hide();
											$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
											errorvedioplay(pname,data);
										}
									}
								});
							}
						}else{
							errorcwbnum += 1;
							$("#excelbranch").html("");
							$("#showcwb").html("");
							$("#zhongzhuan").hide();
							$("#msg").html(scancwb+"         （异常扫描）出库站点与分拣站点不一致");
							$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/fail.wav&a="+Math.random());
						}
					}else{ --%>
						if(data.body.cwbOrder.scannum==1){
							successnum += 1;
						}
						
						$("#zhongzhuan").show();
						if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Peisong.getValue()%>){
							$("#cwbordertype").html("订单类型：配送订单");
							$("#fee").html("应收金额："+data.body.cwbOrder.receivablefee);
						}else if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()%>){
							$("#cwbordertype").html("订单类型：上门换订单");
							$("#fee").html("应收金额："+data.body.cwbOrder.receivablefee);
						}else if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Shangmentui.getValue()%>){
							$("#cwbordertype").html("订单类型：上门退订单");
							$("#fee").html("应退金额："+data.body.cwbOrder.paybackfee);
						}
						$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
						$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
						if(data.body.showRemark!=null){
						$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
						}
						$("#branchid").val(data.body.cwbOrder.nextbranchid);
						$("#scansuccesscwb").val(scancwb);
						$("#showcwb").html("订 单 号："+scancwb);
						getOutSum(pname,data.body.cwbOrder.nextbranchid);
						if(data.body.cwbOrder.sendcarnum>1){
							document.ypdj.Play();
						}
						if(data.body.cwbbranchnamewav!=pname+"/wav/"){
							$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
						}else{
							$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
						}
					//}
				}else{
					errorcwbnum += 1;
					
					$("#zhongzhuan").hide();
					$("#excelbranch").html("");
					$("#cwbDetailshow").html("");
					$("#showcwb").html("");
					$("#cwbordertype").html("");
					$("#fee").html("");
					$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
					errorvedioplay(pname,data);
				}
				$("#responsebatchno").val(data.responsebatchno);
				alloutnum += successnum;
				$("#alloutnum").html(alloutnum);
				getOutSum(pname,$("#branchid").val());
			}
		});
	}
}
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="saomiao_box">
 
	<div class="saomiao_topnum">
		<dl class="blue" style="display: none;">
			<dt>待中转出站</dt>
			<dd id="chukukucundanshu">0</dd>
		</dl>
		<dl class="green">
			<dt>已扫描</dt>
			<dd id="alloutnum">0</dd>
		</dl>
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info">
		<div class="saomiao_inbox">
			<div class="saomiao_lefttitle">扫描订单</div>
			<div class="saomiao_righttitle" id="pagemsg"></div>
			<!-- <form action="" method="get"> -->
			<div class="saomiao_selet">
				下一站：
				<select id="branchid" name="branchid" onchange="getOutSum('<%=request.getContextPath()%>',$(this).val())">
					<!-- <option value="-1" selected>请选择</option> -->
					<%for(Branch b : bList){ %>
						<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
					<%} %>
				</select>
				驾驶员：
				<select id="driverid" name="driverid">
					<option value="-1" selected>请选择</option>
					<%for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
					<%} %>
		        </select>
				车辆：
				<select id="truckid" name="truckid">
					<option value="-1" selected>请选择</option>
					<%for(Truck t : tList){ %>
						<option value="<%=t.getTruckid() %>" ><%=t.getTruckno() %></option>
					<%} %>
		        </select>
				中转原因：
					<select name="reasonid" id="reasonid">
			        	<option value ="0">==请选择==</option>
			        	<%if(reasonlist!=null&&reasonlist.size()>0)for(Reason r : reasonlist){ %>
	           				<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
	           			<%} %>
			        </select>
			</div>
			<div class="saomiao_inwrith">
				<div class="saomiao_left">
					<%if(Long.parseLong(usermap.get("isImposedOutWarehouse").toString())==1){ %>
					<p>
						强制出库:</span><input type="checkbox" id="confirmflag" name="confirmflag" value="1"/>
					</p>
					<%} %>
					<p><span>订单号：</span>
						<input type="text" class="saomiao_inputtxt" value="" id="scancwb" name="scancwb"  onKeyDown='if(event.keyCode==13&&$(this).val().length>0){exportWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val(),$("#driverid").val(),$("#truckid").val(),$("#requestbatchno").val(),$("#baleno").val(),$("#ck_switch").val(),$("#confirmflag").attr("checked")=="checked"?1:0,$("#reasonid").val());}'/>
						<textarea cols="24" rows="4"  name ="cwbs" id="cwbs" ></textarea>
					</p>
				</div>
				<div class="saomiao_right">
					<p id="msg" name="msg" ></p>
					<p id="zhongzhuan" style="display: none"><strong>中转单</strong></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="cwbordertype" name="cwbordertype"></p>
					<p id="fee" name="fee"></p>
					<p id="excelbranch" name="excelbranch" ></p>
					<p id="cwbDetailshow" name="cwbDetailshow" ></p>
					<div style="display: none" id="EMBED">
					</div>
					<div style="display: none">
						<EMBED id='ypdj' name='ypdj' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
					</div>
					<div style="display: none" id="errorvedio">
					</div>
				</div>
				<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" />
		        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
		        <input type="hidden" id="ck_switch" name="ck_switch" value="ck_02" />
			</div><!-- </form> -->
		</div>
	</div>
 
</div>
</body>
</html>
