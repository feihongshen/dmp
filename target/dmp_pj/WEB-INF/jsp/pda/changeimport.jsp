<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> uList = (List<User>)request.getAttribute("userList");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>中转入库</h1>
		<div id="box_form2">
				<ul>
					<li>
	           			<div id="zzzrukukucundanshu" style="color:red;font-weight:bold"></div>
					</li>
					<li><span>选择司机：</span>
						<select id="driverid" name="driverid">
							<option value="-1" selected>请选择</option>
							<%for(User u : uList){ %>
								<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
							<%} %>
				        </select>
					</li>
					<li><span>扫&nbsp;&nbsp;&nbsp;&nbsp;描：</span><input type="text" class="inputtext_2" id="scancwb" name="scancwb" value="" maxlength="50" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){changeimport("<%=request.getContextPath()%>",$(this).val(),$("#driverid").val(),$("#responsebatchno").val(),$("#baleno").val(),$("#sysintowarhouse").val());}'/></li>
					<table class="table_ruku">
						<tr>
							<td style="display: none;"><h4 id="msg" name="msg" ></h4></td>
						</tr>
						<tr>
							<td style="display: none;"><h4 id="excelbranch" name="excelbranch" ></h4></td>
						</tr>
						<tr>
							<td style="display: none;"><span>正常到货：</span><h4 id="successcwbnum" name="successcwbnum"></h4></td>
						</tr>
						<tr>
							<td style="display: none;"><span>有货无单：</span><h4 type="text" id="morecwbnum" name="morecwbnum" ></h4></td>
						</tr>
						<tr>
							<td style="display: none;"><span>有单无货：</span><h4 type="text" id="lesscwbnum" name="lesscwbnum" ></h4></td>
						</tr>
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
		<input type="hidden" id="baleno" name="baleno" value=""/>
		<input type="hidden" id="sysintowarhouse" name="sysintowarhouse" value="0"/>
        <input type="hidden" id="responsebatchno" name="responsebatchno" value="0" />
        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
        <input type="button" id="finish" name="finish" value="完成扫描" class="button" onclick='changefinishimport("<%=request.getContextPath()%>",$("#driverid").val(),$("#responsebatchno").val());'/>
        </div>
	</div>
</div>

<div id="box_yy"></div>

