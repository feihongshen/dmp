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
            <form onSubmit="if(checke_fee()){submitCreateForm(this);}return false;" action="${pageContext.request.contextPath}/penalizeOut/addpenalizeInData"  method="post">
            <table style="width: 100%;">
            <tr>
            <td align="right" nowrap="nowrap">订单号：</td>
            <td ><input id="cwb" type="text" value="${penalizeOut.cwb }" readonly="readonly" style="width: 100%;border-style:none"/></td>
            <td align="right" nowrap="nowrap">赔付大类：</td>
            <td >
            <input id="penalizeOutbigstr"  type="text"  readonly="readonly"  value="${penalizeOutbigStr}" style="width: 100%;border-style:none"/>
            </td>
            <td align="right" nowrap="nowrap">赔付小类：</td>
            <td >
            <input id="penalizeOutsmallstr" type="text" readonly="readonly" value="${penalizeOutsmallStr}"" style="width: 100%;border-style:none"/>
            </td>
            </tr>
            <tr>
            <td align="right" nowrap="nowrap">订单状态：</td>
            <td >
            <input id="flowordertype" type="text" readonly="readonly" value="${flowordertypeText}" type="text" style="width: 100%;border-style:none"/>
            </td>
            <td align="right" nowrap="nowrap">订单金额：</td>
            <td ><input id="caramount" readonly="readonly"  value="${penalizeOut.caramount}" type="text" style="width: 100%;border-style:none"/></td>
        	<td align="right" nowrap="nowrap">赔付金额：</td>
            <td ><input id="penalizeOutfee" readonly="readonly"  value="${penalizeOut.penalizeOutfee}" type="text" style="width: 100%;border-style:none"/></td>
            </tr>
            <tr>
            <td align="right" nowrap="nowrap">责任机构<span style="color: red">*</span>：</td>
            <td >
            <select id="dutybranchid" name="dutybranchid" style="width: 100%;">
            <option value="0">请选择</option>
            <c:forEach items="${branchList}" var="branch">
            <option value="${branch.branchid }">${branch.branchname }</option>
            </c:forEach>
            </select>
            </td>
            <td align="right" nowrap="nowrap">责任人：</td>
            <td > <input type="text" style="width: 100%;" name="dutypersonname"/></td>
           	</tr>
          	<tr>
          	<td align="right" nowrap="nowrap">货物扣罚金额<span style="color: red">*</span>：</td>
            <td ><input id="fee1" onblur="javascript:$('[name=punishInsideprice]').val(($('[name=createqitapunishprice]').val()-0)+(this.value-0))"  onkeyup="if(this.value<'0'){ this.value=''}" value="${penalizeOut.caramount}" name="creategoodpunishprice" type="text" style="width:100%;"/></td>
            <td align="right" nowrap="nowrap">其它扣罚金额<span style="color: red">*</span>：</td>
            <td ><input id="fee2" onblur="javascript:$('[name=punishInsideprice]').val(($('[name=creategoodpunishprice]').val()-0)+(this.value-0))" onkeyup="if(this.value<'0'){ this.value=''}" name="createqitapunishprice" type="text" style="width: 100%;"/></td>
           	<td align="right" nowrap="nowrap">总扣罚金额<span style="color: red">*</span>：</td>
            <td ><input  type="text" id="fee3" name="punishInsideprice" style="width: 100%;border-style:none" readonly="readonly" /></td>
          	</tr>
            <tr>
            <td align="right" nowrap="nowrap">扣罚说明<span style="color: red">*</span>：</td>
            <td colspan="5"><textarea name="punishdescribe" style="width: 100%;resize: none;" onfocus="if(this.value=='最多100字'){ this.value=''}" onblur="if(this.value==''){ this.value='最多100字'}" >最多100字</textarea></td>
           </tr>
           <tr>
           <td colspan="6" align="right">
            <input type="hidden" name="penalizeOutId" value="${penalizeOut.penalizeOutId }"/>
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
