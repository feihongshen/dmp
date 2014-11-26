<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>库存盘点</h1>
		<div id="box_form2">
				<ul>
					<li><span>站点：</span>
						<select id="branchid" name="branchid">
							<option value="-1" selected>请选择</option>
							<%for(Branch b : branchlist){ %>
								<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
							<%} %>
				        </select>*
					</li>
					<li>
						<input type="button" id="beginscanstock" name="beginscanstock" value="开始盘点" onclick='beginscanstock("<%=request.getContextPath()%>",$("#branchid").val());'/>
					</li>
					<li style="display: none;"><span>扫描：</span><input type="text" class="inputtext_2" id="scancwb" name="scancwb" value="" maxlength="50" onKeyDown='if(event.keyCode==13&&$(this).val().length>0&&$(this).val()!=$("#scansuccesscwb").val()){scanstockcwbsubmit("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val());}'/></li>
					<table class="table_ruku">
						<tr>
							<td><h4 id="stockcwbnum" name="stockcwbnum" ></h4></td>
						</tr>
						<tr>
							<td style="display: none;"><h4 id="msg" name="msg" ></h4></td>
						</tr>
						<tr>
							<td style="display: none;"><h4 id="errorinfo" name="errorinfo" ></h4></td>
						</tr>
					</table>
					<li style="display: none;">
						<input type="button" id="cancelscancwb" name="cancelscancwb" value="取消盘点" onclick='scanstockcancel("<%=request.getContextPath()%>",$("#branchid").val());'/>
						<input type="button" id="scanfinish" name="scanfinish" value="盘点完成" onclick='scanstockfinish("<%=request.getContextPath()%>",$("#branchid").val());'/>
					</li>
					
					<table class="table_ruku">
						<tr>
							<td style="display: none;"><span>盘平：</span><h4 id="successcwbnum" name="successcwbnum"></h4></td>
						</tr>
						<tr>
							<td style="display: none;"><span>盘盈：</span><h4 id="morecwbnum" name="morecwbnum"></h4></td>
						</tr>
						<tr>
							<td style="display: none;"><span>盘亏：</span><h4 id="lesscwbnum" name="lesscwbnum"></h4></td>
						</tr>
						<div style="display: none" id="errorvedio">
						</div>
					</table>
	         </ul>
		</div>
		<div align="center">
        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
        </div>
	</div>
</div>

<div id="box_yy"></div>
