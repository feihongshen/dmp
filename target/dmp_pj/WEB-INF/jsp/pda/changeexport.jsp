<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

List<Branch> bList = (List<Branch>)request.getAttribute("branchlist");
List<User> uList = (List<User>)request.getAttribute("userList");
List<Truck> tList = (List<Truck>)request.getAttribute("truckList");
List<Bale> balelist = (List<Bale>)request.getAttribute("balelist");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>中转出库</h1>
		<div id="box_form2">
				<ul>
					<li>
	           			<div id="zzzchukukucundanshu" style="color:red;font-weight:bold"></div>
					</li>
					<li><span>目的站：</span>
	           			<select id="branchid" name="branchid" onchange="">
							<option value="-1" selected>请选择</option>
							<%for(Branch b : bList){ %>
								<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
							<%} %>
						</select>*
					</li>
					<li><span>司&nbsp;&nbsp;机：</span>
						<select id="driverid" name="driverid">
							<option value="-1" selected>请选择</option>
							<%for(User u : uList){ %>
								<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
							<%} %>
				        </select>
					</li>
					<li><span>车&nbsp;&nbsp;辆：</span>
						<select id="truckid" name="truckid">
							<option value="-1" selected>请选择</option>
							<%for(Truck t : tList){ %>
								<option value="<%=t.getTruckid() %>" ><%=t.getTruckno() %></option>
							<%} %>
				        </select>
					</li>
					<li style="display: none"><span>包&nbsp;&nbsp;号：</span><input type="text" class="inputtext_2" name="baleno" id="baleno" maxlength="50"/>
					</li>
					<li><span>扫&nbsp;&nbsp;描：</span><input type="text" class="inputtext_2" id="scancwb" name="scancwb" value="" maxlength="50" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){changeexport("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val(),$("#driverid").val(),$("#truckid").val(),$("#responsebatchno").val(),$("#baleno").val());}'/></li>
					<table class="table_ruku">
						<tr>
							<td style="display: none;"><h4 id="msg" name="msg" ></h4></td>
						</tr>
						<tr>
							<td style="display: none;"><h4 id="excelbranch" name="excelbranch" ></h4></td>
						</tr>
						<!-- <tr>
							<td style="display: none;">
								<h4 id="successcwbnum" name="successcwbnum"></h4>
							</td>
							<td style="display: none;">
								<h4 type="text" id="errorcwbnum" name="errorcwbnum" ></h4>
							</td>
						</tr> -->
						<div style="display: none" id="EMBED">
						</div>
						<tr>
							<td style="display: none;"><span>本批订单票数：</span><h4 type="text" id="allcwbnum" name="allcwbnum" ></h4></td>
						</tr>
						<div style="display: none" id="errorvedio">
						</div>
					</table>
	         </ul>
		</div>
		<div align="center">
        <input type="hidden" id="responsebatchno" name="responsebatchno" value="0" />
        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
        <input type="button" id="finish" name="finish" value="完成扫描" class="button" onclick='changefinishexport("<%=request.getContextPath()%>",$("#branchid").val(),$("#driverid").val(),$("#truckid").val(),$("#responsebatchno").val());'/>
        </div>
	</div>
</div>

<div id="box_yy"></div>

