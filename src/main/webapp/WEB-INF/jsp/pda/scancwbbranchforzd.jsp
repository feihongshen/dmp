<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>理货</h1>
		<div id="box_form2">
				<ul>
					<!-- <li>
	           			当前库存量：<div id="nowkucundanshu" style="color:red;font-weight:bold"></div>
					</li> -->
					<li><span>扫描：</span><input type="text" class="inputtext_2" id="scancwb" name="scancwb" value="" maxlength="50" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){cwbscancwbbranch("<%=request.getContextPath()%>",$(this).val(),"zhandian");}'/></li>
					<table class="table_ruku">
						<tr>
							<td><h4 id="msg" name="msg" ></h4></td>
						</tr>
						<tr>
							<td><span>站点名称：</span><h4 id="cwbbranchname" name="cwbbranchname" ></h4></td>
						</tr>
						<tr>
							<td><span>代收金额：</span><h4 type="text" id="cwbreceivablefee" name="cwbreceivablefee" ></h4>
							</td>
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
        </div>
	</div>
</div>

<div id="box_yy"></div>

