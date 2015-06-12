
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>对外赔付</h1> <!-- onSubmit="return check();" -->
  		<fieldset style="margin-left: 5%;margin-right:5%;margin-top: 0%;margin-bottom:2%;background-color: white;">
            <legend>
                	撤销
            </legend>
            <form action="" method="post">
            <table style="width: 100%;">
            <tr>
            <td nowrap="nowrap" align="right">订单号<span style="color: red">*</span>：</td>
            <td ><input id="cwb" name="cwb" type="text" style="width: 100%;border-style:none"/></td>
            <td nowrap="nowrap" align="right">订单状态：</td>
            <td ><input id="flowordertype" name="flowordertype" type="text" style="width: 100%;border-style:none"/></td>
            <td nowrap="nowrap" align="right">客户名称：</td>
            <td ><input id="customerid" name="customerid" type="text" style="width: 100%;border-style:none"/></td>
            </tr>
            <tr>
            <td nowrap="nowrap" align="right">订单金额：</td>
            <td ><input id="receivablefee" name="receivablefee" type="text" style="width: 100%;border-style:none"/></td>
            <td nowrap="nowrap" align="right">赔付大类<span style="color: red">*</span>：</td>
            <td ><input id="penalizeOutbig" name="penalizeOutbig" style="width: 100%;border-style:none"/></td>
            <td nowrap="nowrap" align="right">赔付小类：</td>
            <td ><input id="penalizeOutsmall" name="penalizeOutsamll" style="width: 100%;border-style:none"/></td>
            </tr>
            <tr>
            <td nowrap="nowrap" align="right">赔付金额<span style="color: red">*</span>：</td>
            <td colspan="5"><input id="penalizeOutfee" name="penalizeOutfee" type="text" style="width: 24.5%;border-style:none"/></td>
            </tr>
            <tr>
            <td nowrap="nowrap" align="right">赔付说明<span style="color: red">*</span>：</td>
            <td colspan="5"><textarea id="penalizeOutContent" name="penalizeOutContent" style="width: 100%;resize: none;" >最多100字</textarea></td>
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

