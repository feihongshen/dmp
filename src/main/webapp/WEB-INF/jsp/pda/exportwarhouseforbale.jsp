<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> bList = (List<Branch>)request.getAttribute("branchlist");
List<User> uList = (List<User>)request.getAttribute("userList");
List<Bale> balelist = (List<Bale>)request.getAttribute("balelist");
Switch ck_switch = (Switch) request.getAttribute("ck_switch");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>出库扫描（包）</h1>
		<div id="box_form2">
				<ul>
					<li>
	           			<!-- <div id="chukukucundanshu" style="color:red;font-weight:bold"></div> -->
					</li>
					 <li><span>目标库房：</span>
						<select id="targetcarwarehouseid" name="targetcarwarehouseid">
							<option value="-1" selected>请选择</option>
							<%for(Branch b : bList){ %>
								<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
							<%} %>
				        </select>
					</li> 
					<li><span>司&nbsp;&nbsp;机：</span>
						<select id="driverid" name="driverid">
							<option value="-1" selected>请选择</option>
							<%for(User u : uList){ %>
								<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
							<%} %>
				        </select>
					</li>
					 <li><span>包&nbsp;&nbsp;号：</span><input type="text" class="inputtext_2" name="baleno" id="baleno" maxlength="50"/>
						<%-- <select id="baleid" name="baleid">
							<option value="-1" selected>请选择</option>
							<%for(Bale b : balelist){ %>
								<option value="<%=b.getId() %>" ><%=b.getBaleno() %></option>
							<%} %>
				        </select> --%>
					</li> 
					<li><span>扫&nbsp;&nbsp;描：</span><input type="text" class="inputtext_2" id="scancwb" name="scancwb" value="" maxlength="50" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){exportWarehouseforbale("<%=request.getContextPath()%>",$(this).val(),$("#targetcarwarehouseid").val(),$("#driverid").val(),$("#responsebatchno").val(),$("#baleno").val(),$("#ck_switch").val());}'/></li>
					<table class="table_ruku">
						<tr>
							<td><h4 id="msg" name="msg" ></h4></td>
						</tr>
						<tr>
							<td style="display: none;"><h4 id="cwbbranchnamesorted" name="cwbbranchnamesorted" ></h4></td>
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
						<tr>
							<td style="display: none;"><span>本批订单票数：</span><h4 type="text" id="allcwbnum" name="allcwbnum" ></h4></td>
						</tr>
						<div style="display: none" id="EMBED">
						</div>
						<div style="display: none">
							<EMBED id='ypdj' name='ypdj' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
						</div>
						<div style="display: none">
							<!-- <EMBED id="error" name="error" SRC='/dmp/images/waverror/fail.wav' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED> -->
						</div>
						<div style="display: none" id="errorvedio">
						</div>
					</table>
	         </ul>
		</div>
		<div align="center">
        <input type="hidden" id="responsebatchno" name="responsebatchno" value="0" />
        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
        <input type="hidden" id="ck_switch" name="ck_switch" value="<%=ck_switch.getState() %>" />
        <input type="button" id="finish" name="finish" value="完成扫描" class="button" onclick='exportwarhousetocheck("<%=request.getContextPath()%>",$("#targetcarwarehouseid").val(),$("#driverid").val(),"-1",$("#responsebatchno").val());'/>
        </div>
	</div>
</div>

<div id="box_yy"></div>

</BODY>
</HTML>
