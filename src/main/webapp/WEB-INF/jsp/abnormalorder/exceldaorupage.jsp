
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

%>
<div id="box_bg"></div>
	<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="overflow: scroll;width: 800px;height: 230px;">
		<h1><div id="close_box" onclick="closeBox()"></div>excel问题件导入窗口</h1>
		<form method="post" id="form1"  action="<%=request.getContextPath()%>/abnormalOrder/submitAbnormalCreateByExcel;jsessionid=<%=session.getId()%>" onsubmit="submitPunishCreateByExcel(this);return false;" enctype="multipart/form-data">
			<table width="900" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox">
				<tr>
					<td width="600" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" style="height:130px">
						
						<tr class="font_1">
							<td  align="left" valign="top">
						请选择excel：<iframe id="update" name="update" src="penalize/penalizeIn/update1?fromAction=form1&a=<%=Math.random() %>" width="400px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>    
						<input type="submit"  id="importdaorubutton"   value="导入" class="input_button2"/>
						</td>
						</tr>
						<tr class="font_1">
				<td  align="left" valign="top">
				 <div class="right_title"  >
				 	<a id="Sto1" href="javascript:punishExcelImportSuccess('emaildate','<%=request.getContextPath() %>','appendhtmlid4','pathurl');">
						成功(
				<label id="successCount111" style="font-size:18;font-weight: bold;color:red;">0</label>
							)</a> 
					<a id="Sto" href="javascript:punishExcelImport('emaildate','<%=request.getContextPath() %>','appendhtmlid4','pathurl2');">
						<font color="red">失败</font>(
				<label id="failCount111" style="font-size:18;font-weight: bold;color:red;">0</label>
				<input type="hidden" id="emaildate" value="" >
							)</a> 
       	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table4" >
        		<tbody >
        			<tr class="font_1" height="30">
        				<td align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">错误原因</td>
        				</tr>
                        </tbody>
                        <tbody  id="appendhtmlid4" style="width: 100px;height:100px; ">
                        
                        </tbody>
                        
                    </table>
            </div>
				</td>
						</tr>
						<!-- <tr class="font_1">
						<td align="left" valign="top">
					</td>
						</tr> -->
			</table>
			</td>
			</tr>
			</table>
			<input type="hidden" id="pathurl" name="pathurl" value="/abnormalOrder/importFlagSuccess/"/>
			<input type="hidden" id="pathurl2" name="pathurl2" value="/abnormalOrder/importFlagError/"/>
			<div align="center">
				<input type="button" onclick="closeBox();"  value="返回" class="button">
			</div>
			</form>
	</div>
</div>