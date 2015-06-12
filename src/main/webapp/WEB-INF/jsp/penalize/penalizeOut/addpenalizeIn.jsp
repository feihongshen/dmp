<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>对外赔付</h1> <!-- onSubmit="return check();" -->
  		<fieldset style="margin-left: 5%;margin-right:5%;margin-top: 0%;margin-bottom:2%;background-color: white;">
            <legend>
                	生成对内扣罚单
            </legend>
            <form action="" method="post">
            <table style="width: 100%;">
            <tr>
            <td align="right" nowrap="nowrap">订单号：</td>
            <td ><input id="cwb" name="cwb" type="text" style="width: 100%;border-style:none"/></td>
            <td align="right" nowrap="nowrap">赔付大类：</td>
            <td ><input id="penalizeOutbig" name="penalizeOutbig" style="width: 100%;border-style:none"/></td>
            <td align="right" nowrap="nowrap">赔付小类：</td>
            <td ><input id="penalizeOutsmall" name="penalizeOutsamll" style="width: 100%;border-style:none"/></td>
            </tr>
            <tr>
            <td align="right" nowrap="nowrap">订单状态：</td>
            <td ><input id="flowordertype" name="flowordertype" type="text" style="width: 100%;border-style:none"/></td>
            <td align="right" nowrap="nowrap">订单金额：</td>
            <td ><input id="receivablefee" name="receivablefee" type="text" style="width: 100%;border-style:none"/></td>
        	<td align="right" nowrap="nowrap">赔付金额：</td>
            <td ><input id="penalizeOutfee" name="penalizeOutfee" type="text" style="width: 24.5%;border-style:none"/></td>
            </tr>
            <tr>
            <td align="right" nowrap="nowrap">责任机构<span style="color: red">*</span>：</td>
            <td >
            <select id="branchid" name="branchid" style="width: 100%;">
            <option value="0">请选择</option>
            <c:forEach items="${branchList}" var="branch">
            <option value="${branch.branchid }">${branch.branchname }</option>
            </c:forEach>
            </select>
            </td>
            <td align="right" nowrap="nowrap">责任人：</td>
            <td > <input type="text" style="width: 100%;"/></td>
           	</tr>
          	<tr>
           	<td align="right" nowrap="nowrap">扣罚金额<span style="color: red">*</span>：</td>
            <td ><input  type="text" style="width: 100%;"/></td>
          	</tr>
            <tr>
            <td align="right" nowrap="nowrap">扣罚说明：</td>
            <td colspan="5"><textarea  style="width: 100%;resize: none;" >最多100字</textarea></td>
           </tr>
           <tr>
           <td colspan="6" align="right">
         <input type="submit" class="input_button2" value="提交"/> <input class="input_button2" type="button" value="取消" onclick="closeBox()"/>
           </td>
           </tr>
            </table>
            </form>
        </fieldset>
	
	</div>
</div>
<div id="box_yy"></div>
<input type="hidden" id="dmpurl" value="${pageContext.request.contextPath}/penalizeOut/add" />
