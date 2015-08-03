<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1>
			<div id="close_box" onclick="closeBox()"></div>
			站点超区
		</h1>
		<form id="customerwarehouses_cre_Form" name="customerwarehouses_cre_Form"
			onSubmit="if(check_customerwarehouses()){submitCreateForm(this);}return false;"
			action="<%=request.getContextPath()%>/customerwarehouses/create" method="post">
			<div id="box_form">
				<table>
					<tr>
						<td>备注：</td>
						<td><textarea cols="3"></textarea></td>
					</tr>
				</table>
			</div>
			<input type="submit" value="确认" class="button" /> <input type="submit" value="关闭" class="button" />
		</form>
	</div>
</div>
<div id="box_yy"></div>
