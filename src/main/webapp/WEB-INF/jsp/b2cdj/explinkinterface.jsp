<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.explink.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Explink explink = (Explink)request.getAttribute("explinkObject");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>B2C对接设置(按自己公司文档)</h1>
		<form id="explink_save_Form" name="explink_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/explinkInterface/saveExplink/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(explink != null){ %>
						<li><span>请求url：</span>
							<input type ="text" id="url" name ="url"  value ="<%=explink.getUrl()%>" maxlength="300"  >
						</li>
						<li><span>B2C标识：</span>
	 						<input type ="text" id="companyname" name ="companyname" value ="<%=explink.getCompanyname()%>" maxlength="30"  > 
						</li>
						<li><span>加密密钥：</span>
	 						<input type ="text" id="key" name ="key" value ="<%=explink.getKey()%>" maxlength="30"  > 
						</li>
						<li><span>订单数量限额：</span>
	 						<input type ="text" id="count" name ="count" value ="<%=explink.getCount()%>" maxlength="30"  > 
						</li>
						<li><span>版本：</span>
	 						<input type ="radio"  name ="version" value ="0"  <%if("0".equals(explink.getVersion())){%>checked<%}%> > 旧版
	 						<input type ="radio"  name ="version" value ="1"   <%if("1".equals(explink.getVersion())){%>checked<%}%>  > 新版
						</li>
						<li><span>新签名方式：</span>
	 						<input type ="radio"  name ="newSignMethod" value ="0"  <%if(explink.getNewSignMethod()==0){%>checked<%}%> > 关闭
	 						<input type ="radio"  name ="newSignMethod" value ="1"   <%if(explink.getNewSignMethod()==1){%>checked<%}%>  > 开启
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"  > 
						</li>
					<%}else{ %>
						<li><span>请求url：</span>
							<input type ="text" id="url" name ="url"   maxlength="300"  >
						</li>
						<li><span>B2C标识：</span>
	 						<input type ="text" id="companyname" name ="companyname" maxlength="30"  > 
						</li>
						<li><span>加密密钥：</span>
	 						<input type ="text" id="key" name ="key" maxlength="30"  > 
						</li>
						<li><span>订单数量限额：</span>
	 						<input type ="text" id="count" name ="count"  maxlength="30"  > 
						</li>
						<li><span>版本：</span>
	 						<input type ="radio"  name ="version" value ="0"   > 旧版
	 						<input type ="radio"  name ="version" value ="1"   > 新版
						</li>
						<li><span>新签名方式：</span>
	 						<input type ="radio"  name ="newSignMethod" value ="0"  checked> 关闭
	 						<input type ="radio"  name ="newSignMethod" value ="1" > 开启
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"  > 
						</li>
					
					<%} %>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

