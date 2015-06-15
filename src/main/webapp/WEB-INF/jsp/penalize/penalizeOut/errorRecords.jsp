<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="height: 400px;width: 500px;">
		<h1><div id="close_box" onclick="closeBox()"></div>对外赔付</h1> <!-- onSubmit="return check();" -->
  		<fieldset  style="height: 300px;overflow: auto;margin-left: 5%;margin-right:5%;margin-top: 0%;margin-bottom:2%;background-color: white;">
            <legend>
                	导入错误明细
            </legend>
         	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				<tr>
					<td width="30%" align="center" valign="middle"style="font-weight: bold;"> 订单号</td>
					<td width="70%" align="center" valign="middle"style="font-weight: bold;"> 错误详情</td>
         		</tr>
         		<c:forEach items="${errorRecords}" var="error">
         		<tr>
         		<td align="center" valign="middle">${error.cwb}</td>
         		<td align="center" valign="middle">${error.error}</td>
         		</tr>
         		</c:forEach>
         	</table>
         	
        </fieldset>
	
	</div>
</div>
<div id="box_yy"></div>

