
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
            <form onSubmit="submitCreateForm(this);return false;" action="${pageContext.request.contextPath}/penalizeOut/cancelpenalizeOutData"  method="post">
            <table style="width: 100%;">
            <tr>
            <td nowrap="nowrap" align="right">订单号<span style="color: red">*</span>：</td>
            <td ><input id="cwb" name="cwb" value="${penalizeOut.cwb}" readonly="readonly" type="text" style="width: 100%;border-style:none"/></td>
            <td nowrap="nowrap" align="right">订单状态：</td>
            <td ><input id="flowordertype" value="${flowordertypeText}" readonly="readonly" name="flowordertype" type="text" style="width: 100%;border-style:none"/></td>
            <td nowrap="nowrap" align="right">客户名称：</td>
            <td ><input id="customerid" name="customerid" value="${customername}" readonly="readonly" type="text" style="width: 100%;border-style:none"/></td>
            </tr>
            <tr>
            <td nowrap="nowrap" align="right">订单金额：</td>
            <td ><input id="caramount" name="caramount" value="${penalizeOut.caramount}" readonly="readonly" type="text" style="width: 100%;border-style:none"/></td>
            <td nowrap="nowrap" align="right">赔付大类<span style="color: red">*</span>：</td>
            <td ><input id="penalizeOutbig"  name="penalizeOutbig" value="${penalizeOutbigStr}" readonly="readonly" style="width: 100%;border-style:none"/></td>
            <td nowrap="nowrap" align="right">赔付小类：</td>
            <td ><input id="penalizeOutsmall" name="penalizeOutsamll" value="${penalizeOutsmallStr}" readonly="readonly" style="width: 100%;border-style:none"/></td>
            </tr>
            <tr>
            <td nowrap="nowrap" align="right">货物赔付金额<span style="color: red">*</span>：</td>
            <td ><input id="penalizeOutfee" name="penalizeOutfee"  value="${penalizeOut.penalizeOutGoodsfee}" readonly="readonly" type="text" style="width: 100%;border-style:none"/></td>
            <td nowrap="nowrap" align="right">其它赔付金额<span style="color: red">*</span>：</td>
            <td ><input id="penalizeOutfee" name="penalizeOutfee"  value="${penalizeOut.penalizeOutOtherfee}" readonly="readonly" type="text" style="width: 100%;border-style:none"/></td>
            <td nowrap="nowrap" align="right">总赔付金额<span style="color: red">*</span>：</td>
            <td ><input id="penalizeOutfee" name="penalizeOutfee"  value="${penalizeOut.penalizeOutfee}" readonly="readonly" type="text" style="width: 100%;border-style:none"/></td>
            </tr>
            <tr>
            <td nowrap="nowrap" align="right">撤销说明<span style="color: red">*</span>：</td>
            <td colspan="5"><textarea  onfocus="if(this.value=='最多100字'){ this.value=''}" onblur="if(this.value==''){ this.value='最多100字'}"  id="cancelContent" name="cancelContent" style="width: 100%;resize: none;" >${penalizeOut.cancelContent==''?'最多100字':penalizeOut.cancelContent }</textarea></td>
           </tr>
           <tr>
           <td colspan="6" align="right">
         <input type="submit" class="input_button2" value="提交"/> <input class="input_button2" type="button" value="取消" onclick="closeBox()"/>
           </td>
           </tr>
            </table>
            <input type="hidden" name="penalizeOutId" value="${penalizeOut.penalizeOutId }"/>
            </form>
        </fieldset>
	
	</div>
</div>
<div id="box_yy"></div>
<input type="hidden" id="dmpurl" value="${pageContext.request.contextPath}/penalizeOut/add" />

