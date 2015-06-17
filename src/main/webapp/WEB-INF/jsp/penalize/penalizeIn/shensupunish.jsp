<%@page import="cn.explink.domain.AbnormalWriteBack"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.AbnormalType"%>
<%@page import="cn.explink.domain.AbnormalOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

String ids = request.getAttribute("ids")==null?"":request.getAttribute("ids").toString();

%>
<div id="box_bg" ></div>
<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>申诉</h1>
		<form method="post" id="form1" onSubmit="if(check_punishshensucondition()){reviseShensuToSubmit(this);}return false;" action="<%=request.getContextPath()%>/inpunish/submitPunishShensufile;jsessionid=<%=session.getId()%>" enctype="multipart/form-data" >
			<table width="600" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox" class="table_2">
						<tr class="font_1">
						<td align="left" valign="middle">
							申诉类型类型<font color="red">*</font>：
									<select name="shensutype" id="shensutype" class="select1">
										<option value="0">请选择申诉类型</option>
										<option value="1">撤销扣罚</option>
										<option value="2">减少扣罚</option>
									</select>
						</td>
						<td></td>
						</tr>
						<tr class="font_1">
							<td  align="left" valign="middle">申诉理由<font color="red">*</font>：
							<textarea name="describe" id="describe" cols="80%" rows="3" id="textfield" onfocus="if(this.value == '最多100个字') this.value = ''" onblur="if(this.value == '') this.value = '最多100个字'">最多100个字</textarea></td>
						</tr>
						 <tr class="font_1">
							<td colspan="2" align="left" valign="top"> 
								<!-- 上传附件:<input type="file" name="file" id="file"/> -->
								上传附件：<iframe id="update" name="update" src="abnormalorder/update?fromAction=form1&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>    
							</td>
							<td></td>
						</tr>  
					</table>
			<div align="center">
			<input type="hidden" id="ids" name="ids" value="<%=ids%>"/>
				<input type="submit" value="提交" class="button">
				<input type="button" onclick="closeBox()" value="取消" class="button">
				</div>
		</form>
	</div>
</div>
<div id="box_yy"></div>