<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Truck"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.OutWarehouseGroup"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.enumutil.*"%>


<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String cwb = (String) request.getAttribute("cwb");
	List<Customer> customerList = (List<Customer>) request.getAttribute("clist");
	List<Branch> branchList = (List<Branch>) request.getAttribute("blist");
	List<Truck> truckList = (List<Truck>) request.getAttribute("tlist");
	List<User> driverList = (List<User>) request.getAttribute("driverlist");
	List<User> deliverList = (List<User>) request.getAttribute("deliverlist");
	List<OutWarehouseGroup> owgList = (List<OutWarehouseGroup>) request.getAttribute("owglist");
	List<Reason> reasonList = (List<Reason>) request.getAttribute("reasonlist");
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
function onselect(parm){
	var Arr = parm.split(",");
	for(var i=0;i<Arr.size();i++){
		$("#"+Arr[i]+"").attr("display","");
	}
	$("form div").each(function(){
		if(Arr.indexOf(this)==-1){
			this.attr("display","none");
		}
	});
}

</script>
<title>PDA测试</title>
</head>
<body>
<form action="../../cwborderPDA/">
操作：
<select id="requestparam" name="requestparam">
	<option value="">请选择</option>
	<option value="selectcustomerlist">供货商列表查询</option>
	<option value="selectdriverlist">驾驶员列表查询</option>
	<option value="warehouse_catchcargo_submit">提货扫描提交</option>
	<option value="warehouse_finishcargo_submit">提货完成确认</option>
	<option value="warehouseimport_submit">库房入库扫描</option>
	<option value="warehouseimport_submit_forremark">入库备注提交</option>
	<option value="warehouse_finishimport_submit">入库完成确认</option>
	<option value="selectbranchlist_forexport">站点列表查询</option>
	<option value="selecttrucklist">车辆列表查询</option>
	<option value="warehouseexport_submit">出库扫描提交</option>
	<option value="warehouse_finishexport_submit">出库扫描批次封包确认</option>
	<option value="warehouse_printbar_submit">标签打印</option>
	<option value="warehouse_scancwbbranch_submit">理货扫描</option>
	<option value="branchimport_submit">分站到货扫描</option>
	<option value="branch_finishimport_submit">到货完成确认</option>
	<option value="selectdeliverlist_forbranchdeliver">查询本站点派送员列表</option>
	<option value="branchdeliver_submit">领货扫描提交</option>
	<option value="selecttransreasonlist_fortransexport">查询中转原因列表</option>
	<option value="branchtransexport_submit">分站中转出站扫描提交</option>
	<option value="branch_finishtransexport_submit">分站中转出站批次封包确认</option>
	<option value="branchbackexport_submit">退货出站扫描：</option>
	<option value="branch_finishbackexport_submit">退货出站扫描批次封包确认</option>
	<option value="transbranchimport_submit">中转站入库</option>
	<option value="transbranch_finishimport_submit">中转站入库到货完成确认</option>
	<option value="transbranchexport_submit">中转出库扫描</option>
	<option value="transbranch_finishexport_submit">中转出库批次封包确认</option>
	<option value="backbranchimport_submit">退货站入库</option>
	<option value="backbranch_finishimport_submit">退货站入库到货完成确认</option>
	<option value="backbranchexport_submit">退货站再投扫描</option>
	<option value="backbranch_finishexport_submit">退货站再投批次封包确认</option>
	<option value="stockscan_query">查询当前站点库存数量(开始库存盘点)</option>
	<option value="stockscan_submit">库存盘点</option>
	<option value="stockscan_cancel">盘点扫描取消</option>
	<option value="stockscan_finish_submit">盘点完成确认</option>
	<option value="deliverpod_forquery">订单扫描查询</option>
	<option value="selectpodresultlist_forpod">配送结果列表查询</option>
	<option value="selectbackreasonlist_forpod">退货原因列表查询</option>
	<option value="selectleavedreasonlist_forpod">滞留原因列表查询</option>
	<option value="selectpodremarklist_forpod">配送结果备注列表查询</option>
	<option value="deliverpod_statistics">派送员归班汇总</option>
	<option value="cwbtrack_submit">查询单号提交</option>
</select><br/>
订单号：<%=cwb %><br/>
<input id="cwb" name="cwb" value="<%=cwb %>"  type="hidden"/>
<input id="deviceid" name="deviceid" value="1" type="hidden"/>
<input id="authtoken" name="authtoken" value="123456789" type="hidden"/>
<input id="clientversion" name="clientversion" value="1.0" type="hidden"/>
<div id="one">
供货商id：
<select id="customerid" name="customerid">
	<option value="0">请选择</option>
	<%
		for (Customer c : customerList) {
	%>
	<option value="<%=c.getCustomerid()%>"><%=c.getCustomername()%></option>
	<%}%>
</select><br/>
</div>
<div id="two">
站点id：
<select id="branchid" name="branchid">
	<option value="0">请选择</option>
	<%
		for (Branch b : branchList) {
	%>
	<option value="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
	<%
		}
	%>
</select><br/>
</div>
<div id="three">
车辆id：
<select id="truckid" name="truckid">
	<option value="0">请选择</option>
	<%
		for (Truck t : truckList) {
	%>
	<option value="<%=t.getTruckid()%>"><%=t.getTruckno()%></option>
	<%
		}
	%>
</select><br/>
</div>
<div id="four">
驾驶员id：
<select id="driverid" name="driverid">
	<option value="0">请选择</option>
	<%
		for (User u : driverList) {
	%>
	<option value="<%=u.getUserid()%>"><%=u.getRealname()%></option>
	<%
		}
	%>
</select><br/>
</div>
<div id="five">
小件员id：
<select id="deliverid" name="deliverid">
	<option value="0">请选择</option>
	<%
		for (User u : deliverList) {
	%>
	<option value="<%=u.getUserid()%>"><%=u.getRealname()%></option>
	<%
		}
	%>
</select><br/>
</div>
<div id="six">
批次号：
<select id="requestbatchno" name="requestbatchno">
	<option value="0">请选择</option>
	<%
		for (OutWarehouseGroup o : owgList) {
	%>
	<option value="<%=o.getId()%>"><%=o.getId()%></option>
	<%
		}
	%>
</select><br/>
</div>
<div id="seven">
入库备注提交类型：
<select id="csremarkid" name="csremarkid">
	<option value="0">请选择</option>
</select><br/>
</div>
<div id="eight">
入库备注一票多件时，件数：
<select id="multicwbnum" name="multicwbnum">
	<option value="0">请选择</option>
	<%for(int i=1;i<1000;i++){ %>
	<option value="<%=i%>"><%=i%></option>
	<%} %>
</select><br/>
</div>
<div id="nine">
出库扫描时是否强制出库：（0为否，1为是）
<select id="confirmflag" name="confirmflag">
	<option value="0">否</option>
	<option value="1">是</option>
</select><br/>
</div>
<div id="ten">
中转原因：
<select id="reasonid" name="reasonid">
	<option value="0">请选择</option>
	<%
		for (Reason r : reasonList) {
	%>
	<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent()%></option>
	<%
		}
	%>
</select><br/>
</div>
<input type="submit" value="提交参数，开始测试">
</form>

<%-- <a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=selectcustomerlist&requestbatchno=" >供货商列表查询：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=selectdriverlist&requestbatchno=">驾驶员列表查询： </a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=warehouse_catchcargo_submit&cwb=<%=cwb%>&customerid=<%=request.getParameter("customerid")%>&driverid=<%=request.getParameter("driverid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">提货扫描提交：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=warehouse_finishcargo_submit&customerid=<%=request.getParameter("customerid")%>&driverid=<%=request.getParameter("driverid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">提货完成确认：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=warehouseimport_submit&cwb=<%=cwb%>&customerid=<%=request.getParameter("customerid")%>&driverid=<%=request.getParameter("driverid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">库房入库扫描：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=warehouseimport_submit_forremark&cwb=<%=cwb%>&csremarkid=<%=request.getParameter("csremarkid")%>&multicwbnum=<%=request.getParameter("multicwbnum")%>&requestbatchno=">入库备注提交：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=warehouse_finishimport_submit&customerid=<%=request.getParameter("customerid")%>&driverid=<%=request.getParameter("driverid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">入库完成确认：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=selectbranchlist_forexport&requestbatchno=">站点列表查询：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=selecttrucklist&requestbatchno=">车辆列表查询：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=warehouseexport_submit&cwb=<%=cwb%>&driverid=<%=request.getParameter("driverid")%>&branchid=<%=request.getParameter("branchid")%>&truckid=1&requestbatchno=<%=request.getParameter("requestbatchno")%>&confirmflag=<%=request.getParameter("confirmflag")%>">出库扫描提交：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=warehouse_finishexport_submit&driverid=<%=request.getParameter("diverid")%>&branchid=<%=request.getParameter("branchid")%>&truckid=<%=request.getParameter("truckid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">出库扫描批次封包确认：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=warehouse_printbar_submit&cwb=<%=cwb%>&requestbatchno=">标签打印:</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=warehouse_scancwbbranch_submit&cwb=<%=cwb%>&requestbatchno=">理货扫描:</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=branchimport_submit&cwb=<%=cwb%>&driverid=<%=request.getParameter("driverid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">分站到货扫描：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=branch_finishimport_submit&driverid=<%=request.getParameter("driverid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">到货完成确认：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=selectdeliverlist_forbranchdeliver&requestbatchno=">查询本站点派送员列表：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=branchdeliver_submit&cwb=<%=cwb%>&deliverid=<%=request.getParameter("deliverid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">领货扫描提交：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=selecttransreasonlist_fortransexport&requestbatchno=">查询中转原因列表:</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=branchtransexport_submit&cwb=<%=cwb%>&reasonid=<%=request.getParameter("reasonid")%>&requestbatchno=">分站中转出站扫描提交：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=branch_finishtransexport_submit&driverid=<%=request.getParameter("driverid")%>&truckid=<%=request.getParameter("truckid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">分站中转出站批次封包确认：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=branchbackexport_submit&cwb=<%=cwb%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">退货出站扫描：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=branch_finishbackexport_submit&driverid=<%=request.getParameter("driverid")%>&truckid=<%=request.getParameter("truckid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">退货出站扫描批次封包确认：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=transbranchimport_submit&cwb=<%=cwb%>&driverid=<%=request.getParameter("driverid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">中转站入库：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=transbranch_finishimport_submit&driverid=<%=request.getParameter("driverid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">中转站入库到货完成确认：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=transbranchexport_submit&cwb=<%=cwb%>&driverid=<%=request.getParameter("driverid")%>&branchid=<%=request.getParameter("branchid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">中转站出库扫描：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=transbranch_finishexport_submit&driverid=<%=request.getParameter("driverid")%>&branchid=<%=request.getParameter("branchid")%>&truckid=1&requestbatchno=<%=request.getParameter("requestbatchno")%>">中转站出库批次封包确认：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=backbranchimport_submit&cwb=<%=cwb%>&driverid=<%=request.getParameter("driverid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">退货站入库：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=backbranch_finishimport_submit&driverid=<%=request.getParameter("driverid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">退货站入库到货完成确认：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=backbranchexport_submit&cwb=<%=cwb%>&driverid=<%=request.getParameter("driverid")%>&truckid=<%=request.getParameter("truckid")%>&branchid=<%=request.getParameter("branchid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">退货站再投扫描：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=backbranch_finishexport_submit&cwb=<%=cwb%>&driverid=<%=request.getParameter("driverid")%>&truckid=<%=request.getParameter("truckid")%>&branchid=<%=request.getParameter("branchid")%>&requestbatchno=<%=request.getParameter("requestbatchno")%>">退货站再投批次封包确认：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=stockscan_query&branchid=<%=request.getParameter("branchid")%>&requestbatchno=">查询当前站点库存数量(开始库存盘点)：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=stockscan_submit&cwb=<%=cwb%>&branchid=<%=request.getParameter("branchid")%>&requestbatchno=">库存盘点：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=stockscan_cancel&branchid=<%=request.getParameter("branchid")%>&requestbatchno=">盘点扫描取消:</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=stockscan_finish_submit&branchid=<%=request.getParameter("branchid")%>&requestbatchno=">盘点完成确认:</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=deliverpod_forquery&cwb=<%=cwb%>&requestbatchno=">订单扫描查询:</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=selectpodresultlist_forpod&requestbatchno=">配送结果列表查询:</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=selectbackreasonlist_forpod&requestbatchno=">退货原因列表查询:</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=selectleavedreasonlist_forpod&requestbatchno=">滞留原因列表查询:</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=deliverpod_statistics&deliverid=<%=request.getParameter("deliverid")%>&requestbatchno=">配送结果备注列表查询:</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=selectcustomerlist&requestbatchno=">派送员归班汇总：</a><br/>

<a href="http://localhost:8080/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=cwbtrack_submit&cwb=<%=cwb%>&requestbatchno=">查询单号提交</a><br/>

 --%>







结果反馈提交:（podresultid的值只能是 (1,"配送成功"),(2,"上门退成功"),(3,"上门换成功"),(4,"拒收"),(5,"部分退货"),(6,"分站滞留"),(7,"上门拒退"),(8,"货物丢失"),(9,"其他")这几种的任意一个的编号）
http://talentslink.net/dmp/cwborderPDA/?deviceid=1&authtoken=123456789&clientversion=1.0&requestparam=deliverpod_submit&cwb=112051918334&deliverid=2&podresultid=1&backreasonid=&leavedreasonid=&podremarkid=6&receivedfeecash=&receivedfeepos=&receivedfeecheque=&receivedfeeother=&paybackedfee=&requestbatchno=


<script type="text/javascript">
$("#requestparam").val(<%=request.getParameter("requestparam")%>);
$("#customerid").val(<%=request.getParameter("customerid")%>);
$("#branchid").val(<%=request.getParameter("branchid")%>);
$("#truckid").val(<%=request.getParameter("truckid")%>);
$("#driverid").val(<%=request.getParameter("driverid")%>);
$("#deliverid").val(<%=request.getParameter("deliverid")%>);
$("#requestbatchno").val(<%=request.getParameter("requestbatchno")%>);
$("#csremarkid").val(<%=request.getParameter("csremarkid")%>);
$("#multicwbnum").val(<%=request.getParameter("multicwbnum")%>);
$("#confirmflag").val(<%=request.getParameter("confirmflag")%>);
$("#reasonid").val(<%=request.getParameter("reasonid")%>);

</script>


</body>
</html>