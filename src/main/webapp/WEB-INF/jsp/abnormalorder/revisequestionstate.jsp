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
List<AbnormalType> abnormalTypeList = (List<AbnormalType>)request.getAttribute("abnormalTypeList");

%>
<div id="box_bg" ></div>
<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改问题件</h1>
		<form method="post" id="form1" onSubmit="if(check_describe()){reviseAbnormalCreateData(this);}return false;" action="<%=request.getContextPath()%>/abnormalOrder/SubmitReviseQuestionAdd;jsessionid=<%=session.getId()%>" enctype="multipart/form-data" >
			<table width="600" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox" class="table_2">
						<tr class="font_1">
						<td align="left" valign="middle">
							问题件类型：
									<select name="abnormaltypeid" id="abnormaltypeid" class="select1">
										<option value="0">请选择问题件类型</option>
										<%if(abnormalTypeList!=null||abnormalTypeList.size()>0)for(AbnormalType at : abnormalTypeList){ %>
										<option title="<%=at.getName() %>" value="<%=at.getId()%>"><%if(at.getName().length()>25){%><%=at.getName().substring(0,25)%><%}else{%><%=at.getName() %><%} %></option>
										<%} %>
									</select>
						</td>
						<td></td>
						</tr>
						<tr class="font_1">
							<td  align="left" valign="middle">问题件说明*：
							<textarea name="describe" id="describe" cols="80%" rows="3" id="textfield"></textarea></td>
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
				<input type="submit" value="修改问题件" class="button">
				<input type="reset" value="取消" class="button">
				</div>
		</form>
	</div>
</div>
<div id="box_yy"></div>