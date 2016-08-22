<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String pageParamName = request.getParameter("pageParamName");
	String pageSizeParamName =request.getParameter("pageSizeParamName");
	String pageIndex = request.getParameter(pageParamName);
	String pageSize = request.getParameter(pageSizeParamName);
	if(pageIndex ==null || pageIndex.equals("")){
		pageIndex = "1";
	}
	if(pageSize ==null || pageSize.equals("")){
		pageSize = "10";
	}
	//String action = java.net.URLEncoder.encode(request.getParameter("action"),"UTF-8");
	String action = getEncodeAction(request.getParameter("action"));
	
%>
<%!
//将url里的参数值进行uriEncode
//例： /dmp/lis?page=1&remark=测试， 将会变为： /dmp/lis?page=1&remark=%asdasd%
public String getEncodeAction(String action){
	StringBuffer encodeAction = new StringBuffer();
	if(action!=null && !action.trim().equals("")){
		String[] arr = action.split("=");
		if(arr.length < 1){
			return action;
		}
		encodeAction.append(arr[0]);
		for(int i=1;i<arr.length;i++){
			encodeAction.append("=");
			String str = arr[i];
			int valueEnd = str.indexOf("&");
			if(valueEnd<0){
				encodeAction.append(java.net.URLEncoder.encode(str));
			}else{
				//例子：&param1=value1&param2=value, 截取
				String value = str.substring(0,valueEnd);
				String others = str.substring(valueEnd);
				encodeAction.append(java.net.URLEncoder.encode(value));
				encodeAction.append(others);
			}
			
		}
	}
	return encodeAction.toString();
}
%>
<c:set var="pageParamName" value="${param.pageParamName}" />
<c:set var="pageSizeParamName" value="${param.pageSizeParamName}" />
<c:set var="page" value="<%=pageIndex%>" />
<c:set var="pageSize" value="<%=pageSize%>" />
<c:set var="total" value="0" />
<c:set var="height" value="20" />
<c:set var="pageCount" value="1" />

<!-- 初始化参数 -->
<c:if test="${not empty param.height && param.height>0 }"><c:set var="height" value="${param.height}" /></c:if>
<c:if test="${not empty param.total}"><c:set var="total" value="${param.total}" /></c:if>
<c:set var="pageCount" value="${(total-total%pageSize)/pageSize}" />
<c:if test="${total%pageSize>0}"><c:set var="pageCount" value="${pageCount+1}" /></c:if>



<script type="text/javascript">
function goPage(page,pageSize){
	var action = "<%=action%>";
	if(action.indexOf("?")<0){
		action = action + "?"
	}else{
		action = action + "&"
	}
	action = action +"${pageParamName}="+page+"&${pageSizeParamName}="+pageSize;
	location = action;
}
</script>

	<c:if test='${pageCount>1}'>
	<div class="iframe_bottom">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
	<tr>
		<td height="${height}" align="center" valign="middle" bgcolor="#eef6ff">
			<a href="javascript:goPage(1, ${pageSize});" >第一页</a>
			<c:if test="${page>1}">
				<a href="javascript:goPage(${page-1}, ${pageSize});">上一页</a>　
			</c:if>
			<c:if test="${page<pageCount}">
				<a href="javascript:goPage(${page+1}, ${pageSize});" >下一页</a>　
			</c:if>
			<a href="javascript:goPage(${pageCount}, ${pageSize}) ;" >最后一页</a>
			
			共${pageCount}页　共${total}条记录 
			<c:if test="${empty param.showSelectPage || param.showSelectPage}">
			 &nbsp; &nbsp;
			　当前第<select
					id="selectPg"
					onchange="javascript:goPage($(this).val(),${pageSize})">
					<c:forEach var="i" begin="1" end="${pageCount}">
					<option value='${i}'  <c:if test="${page==i}">selected="selected" </c:if>>${i}</option>
					</c:forEach>
				</select>页
			</c:if>
			
			<c:if test="${empty param.showSelectPageSize || param.showSelectPageSize}">
			 &nbsp; &nbsp;
				每页显示 &nbsp;<select
					id="selectPg"
					onchange="javascript:goPage(1,$(this).val())">
					<option value='10'  <c:if test="${pageSize==10}">selected="selected" </c:if>>10</option>
					<option value='20'  <c:if test="${pageSize==20}">selected="selected" </c:if>>20</option>
					<option value='50'  <c:if test="${pageSize==50}">selected="selected" </c:if>>50</option>
	
				</select> &nbsp;&nbsp; 条记录
			</c:if>
		</td>
	</tr>
	</table>
	</div>
	</c:if>