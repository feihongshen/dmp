<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.wanxiang.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	Wanxiang lt = (Wanxiang)request.getAttribute("wanxiangObject");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>万象接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/wanxiang/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(lt != null){ %>
						
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  size="15"  maxlength="300"  value="<%=lt.getCustomerid()%>"   > 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300" size="15" value="<%=lt.getPrivate_key()%>"  > 
						</li>
						<li><span>用户名：</span>
	 						<input type ="text" id="user_name" name ="user_name"  maxlength="300" size="15" value="<%=lt.getUser_name()%>"  > 
						</li>
						<li><span>密码：</span>
	 						<input type ="text" id="pass_word" name ="pass_word"  maxlength="300" size="15" value="<%=lt.getPass_word()%>"  > 
						</li>
						<li><span>操作机构：</span>
	 						<input type ="text" id="branchname" name ="branchname"  maxlength="300" size="15" value="<%=lt.getBranchname()%>"  > 
						</li>
					
						<li><span>URL：</span>
	 						<input type ="text" id="url" name ="url"  maxlength="300"  value="<%=lt.getUrl()%>"  size="40"> 
						</li>
						<li><span>每次查询大小：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=lt.getMaxCount()%>"  size="15"> 
						</li>
						<li><span>查询版本：</span>
	 						<input id="version1" name ="version" type="radio" value="0" <%if(lt.getVersion()==0){%>checked<%} %>  size="15">旧版
	 						<input  id="version2" name ="version" type="radio" value="1" <%if(lt.getVersion()==1){%>checked<%} %>  size="15">新版
						</li>
						<li><span>拒收是否推送：</span>
	 						<input id="jushousendflag1" name ="jushousendflag" type="radio" value="0" <%if(lt.getJushousendflag()==0){%>checked<%} %>  size="15">推送
	 						<input  id="jushousendflag2" name ="jushousendflag" type="radio" value="1" <%if(lt.getJushousendflag()==1){%>checked<%} %>  size="15">不推送
						</li>
						<li><span>上门退是否推送：</span>
	 						<input id="shangmentuiSupport1" name ="shangmentuiSupport" type="radio" value="0" <%if(lt.getShangmentuiSupport()==0){%>checked<%} %>  size="15">推送
	 						<input  id="shangmentuiSupport2" name ="shangmentuiSupport" type="radio" value="1" <%if(lt.getShangmentuiSupport()==1){%>checked<%} %>  size="15">不推送
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  size="15"  maxlength="300"  value=""   > 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>用户名：</span>
	 						<input type ="text" id="user_name" name ="user_name"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>密码：</span>
	 						<input type ="text" id="pass_word" name ="pass_word"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>操作机构：</span>
	 						<input type ="text" id="branchname" name ="branchname"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>URL：</span>
	 						<input type ="text" id="url" name ="url"  maxlength="300"  value=""  size="40"> 
						</li>
						<li><span>每次查询大小：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>查询版本：</span>
	 						<input  id="version1" name ="version" type="radio" value="0" checked  size="15">旧版
	 						<input  id="version2" name ="version" type="radio" value="1"   size="15">新版
						</li>
						<li><span>拒收是否推送：</span>
	 						<input id="jushousendflag1" name ="jushousendflag" type="radio" value="0"   size="15">推送
	 						<input  id="jushousendflag2" name ="jushousendflag" type="radio" value="1"  size="15">不推送
						</li>
						<li><span>上门退是否推送：</span>
	 						<input id="shangmentuiSupport1" name ="shangmentuiSupport" type="radio" value="0"   size="15">推送
	 						<input  id="shangmentuiSupport2" name ="shangmentuiSupport" type="radio" value="1"  size="15">不推送
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

