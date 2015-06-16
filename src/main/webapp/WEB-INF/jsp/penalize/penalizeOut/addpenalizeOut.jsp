<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>对外赔付</h1> <!-- onSubmit="return check();" -->
  		<fieldset style="margin-left: 5%;margin-right:5%;margin-top: 0%;margin-bottom:2%;background-color: white;">
            <legend>
                	对外赔付单创建
            </legend>
            <form onSubmit="if(checke_fee()){submitCreateForm(this);}return false;" action="${pageContext.request.contextPath}/penalizeOut/addpenalizeOutData" method="post">
            <table style="width: 100%;">
            <tr>
            <td align="right" nowrap="nowrap" >订单号<span style="color: red">*</span>：</td>
            <td ><input id="cwb" name="cwb" type="text" style="width: 100%;" onkeyup="init($(this).val())"/></td>
            <td align="right" nowrap="nowrap">订单状态：</td>
            <td >
            <input id="flowordertypeText" name="flowordertypeText" readonly="readonly" type="text" style="width: 100%;border-style:none"/>
            <input id="flowordertype" name="flowordertype" type="hidden" style="width: 100%;border-style:none"/>
            </td>
            <td align="right" nowrap="nowrap">客户名称：</td>
            <td >
            <input id="customername" name="customername" readonly="readonly" type="text" style="width: 100%;border-style:none"/>
            <input id="customerid" name="customerid" type="hidden" style="width: 100%;border-style:none"/>
            </td>
            </tr>
            <tr>
            <td align="right" nowrap="nowrap">订单金额：</td>
            <td ><input id="receivablefee" readonly="readonly" name="receivablefee" type="text" style="width: 100%;border-style:none"/></td>
            <td align="right" nowrap="nowrap">赔付大类<span style="color: red">*</span>：</td>
            <td >
			<select style="width: 100%" id="penalizebig" name="penalizeOutbig" onchange="findsmall($(this).val())">
			<option value ="0">请选择</option>
			<c:forEach items="${penalizebigList}" var="big" >
			<option value="${big.id}">${big.text}</option>
			</c:forEach>
			</select>
            </td>
            <td align="right" nowrap="nowrap">赔付小类：</td>
            <td >
		     <select style="width: 100%" id="penalizesmall" name="penalizeOutsmall" onchange="findbig()">
			<option value ="0">请选择</option>
			<c:forEach items="${penalizesmallList}" var="small">
			<option value="${small.id}"  id="${small.parent }">${small.text}</option>
			</c:forEach>
			</select>
            </td>
            </tr>
            <tr>
            <td align="right" nowrap="nowrap">赔付金额<span style="color: red">*</span>：</td>
            <td colspan="5"><input id="fee" onkeyup="if(this.value<'0'){ this.value=''}" name="penalizeOutfee" type="text" style="width: 24.5%;"/></td>
            </tr>
            <tr>
            <td align="right" nowrap="nowrap">赔付说明<span style="color: red">*</span>：</td>
            <td colspan="5"><textarea  onfocus="if(this.value=='最多100字'){ this.value=''}" onblur="if(this.value==''){ this.value='最多100字'}"  id="penalizeOutContent" name="penalizeOutContent" style="width: 100%;resize: none;" >最多100字</textarea></td>
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
<input type="hidden" id="dmpurl" value="${pageContext.request.contextPath}" />
