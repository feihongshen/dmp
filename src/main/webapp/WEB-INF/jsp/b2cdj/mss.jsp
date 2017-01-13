<%@page import="cn.explink.b2c.mss.Mss"%>
<%@page import="cn.explink.b2c.jiuye.JiuYe"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.jiuye.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%

Mss smile = (Mss)request.getAttribute("mss");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");

%>

<script type="text/javascript">   
</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>otms对接设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/mss/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(smile != null){ %>
						<li><span>接收URL：</span>
	 						<input type ="text" id="importUrl" name ="importUrl"  maxlength="300"  value="<%=smile.getImportUrl()%>"  > 
						</li>
						<li><span>反馈URL：</span>
	 						<input type ="text" id="feedbackUrl" name ="feedbackUrl"  maxlength="300"  value="<%=smile.getFeedbackUrl()%>"  > 
						</li>
						<li><span>上传图片URL：</span>
	 						<input type ="text" id="imgUrl" name ="imgUrl"  maxlength="300"  value="<%=smile.getImgUrl()%>"  > </li>
						<li><span>公钥：</span>
	 						<input type ="text" id="access_key" name ="access_key"  maxlength="300"  value="<%=smile.getAccessKey() %>"  > 
						</li>
						<li><span>每次反馈数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=smile.getMaxCount()%>"    > 
						</li>
						<li><span>命令：</span>
	 						<input type ="text" id="cmd" name ="cmd"  maxlength="300"  value="<%=smile.getCmd()%>"    > 
						</li>
						<li><span>请求唯一标识：</span>
	 						<input type ="text" id="ticket" name ="ticket"  maxlength="300"  value="<%=smile.getTicket()%>"    > 
						</li>
						<li><span>版本：</span>
	 						<input type ="text" id="version" name ="version"  maxlength="300"  value="<%=smile.getVersion()%>"    > 
						</li>
							<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=smile.getCustomerid()%>"  size="15" > 
						</li>
						<li><span>私钥：</span>
	 						<input type ="text" id="secretKey" name ="secretKey"  maxlength="300"  value="<%=smile.getSecretKey()%>"    > 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>接收URL：</span>
	 						<input type ="text" id="importUrl" name ="importUrl"  maxlength="300"  value=""  > 
						</li>
						<li><span>反馈URL：</span>
	 						<input type ="text" id="feedbackUrl" name ="feedbackUrl"  maxlength="300"  value=""  > 
						</li>
						<li><span>上传图片URL：</span>
	 						<input type ="text" id="imgUrl" name ="imgUrl"  maxlength="300"  value=""  > </li>
						<li><span>公钥：</span>
	 						<input type ="text" id="access_key" name ="access_key"  maxlength="300"  value=""  > 
						</li>
						<li><span>每次反馈数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""    > 
						</li>
						<li><span>命令：</span>
	 						<input type ="text" id="cmd" name ="cmd"  maxlength="300"  value=""    > 
						</li>
						<li><span>请求唯一标识：</span>
	 						<input type ="text" id="ticket" name ="ticket"  maxlength="300"  value=""    > 
						</li>
						<li><span>版本：</span>
	 						<input type ="text" id="version" name ="version"  maxlength="300"  value=""    > 
						</li>
							<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>私钥：</span>
	 						<input type ="text" id="secretKey" name ="secretKey"  maxlength="300"  value=""    > 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
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

