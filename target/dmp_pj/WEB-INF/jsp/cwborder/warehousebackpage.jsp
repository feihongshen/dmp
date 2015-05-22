<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>库对库退货出库扫描</h1>
		<div id="box_form2">
				<ul>
					<li><span>扫&nbsp;&nbsp;描：</span><input type="text" class="inputtext_2" id="scancwb" name="scancwb" value="" maxlength="50" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){warehousebackexport("<%=request.getContextPath()%>",$(this).val(),$("#responsebatchno").val());}'/></li>
					<table class="table_ruku">
						<tr>
							<td><h4 id="msg" name="msg" ></h4></td>
						</tr>
						<tr>
							<td style="display: none;"><h4 id="excelbranch" name="excelbranch" ></h4></td>
						</tr>
						<div style="display: none" id="EMBED">
						</div>
						<div style="display: none" id="errorvedio">
						</div>
					</table>
	         </ul>
		</div>
		<div align="center">
        <input type="hidden" id="responsebatchno" name="responsebatchno" value="0" />
        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
        <input type="button" id="finish" name="finish" value="完成扫描" class="button" onclick='closeBox();'/>
        </div>
	</div>
</div>

<div id="box_yy"></div>

</BODY>
</HTML>
