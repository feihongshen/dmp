<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div style="margin:20px 0;">
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg').dialog('open')">Open</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg').dialog('close')">Close</a>
	</div>
	<div id="dlg" class="easyui-dialog" title="Basic Dialog" data-options="iconCls:'icon-save'" style="width:400px;height:200px;padding:10px">
	
<div id="box_bg"></div>
<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="width: 650px;">
		<h1><div id="close_box" onclick="closeBox()"></div></h1> <!-- onSubmit="return check();" -->
  		<fieldset  style="height: 150px;overflow: auto;margin-left: 5%;margin-right:5%;margin-top: 0%;margin-bottom:2%;background-color: white;">
            <legend>
                	查询条件
            </legend>
         	<table width="100%" border="0" cellspacing="1" cellpadding="0" >
         	<tr>
         		<td align="right" nowrap="nowrap" style="width: 15%;">批次编号：</td>
         		<td nowrap="nowrap" style="width: 30%;"><input type="text" style="width: 100%;"/> </td>
         		<td nowrap="nowrap" align="right" style="width: 15%;">批次状态：</td>
         		<td nowrap="nowrap" style="width: 30%;">
	         		<select style="width: 100%;">
	         			<option>全部</option>
	         		</select>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" >站点：</td>
         		<td nowrap="nowrap"><input type="text" style="width: 100%;"/> </td>
         		<td nowrap="nowrap" align="right">期间：</td>
         		<td nowrap="nowrap">
	         		<input type="text" style="width: 40%;"/><span style="width: 15%;vertical-align: middle;">到</span><input type="text" style="width: 40%;"/>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right">核销人：</td>
         		<td nowrap="nowrap"><input type="text" style="width: 100%;"/> </td>
         		<td nowrap="nowrap" align="right">核销日期：</td>
         		<td nowrap="nowrap"><input type="text" style="width:100%;"/> </td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right">排序：</td>
         		<td nowrap="nowrap">
			    	<select style="width:70%;">
			    	
			    	</select>
			    	<select style="width:30%;">
			    	<option value="asc">升序</option>
			    	<option value="desc">降序</option>
			    	</select>
		        </td>
         	</tr>
         	<tr>
         	<td colspan="4" >
         	&nbsp;
         	</td>
         	</tr>
         	<tr>
         	<td colspan="4" rowspan="2" align="center" valign="bottom">
         	<input type="submit" class="input_button2" value="查询"/>
         	<input type="button" class="input_button2" value="关闭"/>
         	</td>
         	</tr>
         	</table>
         	
        </fieldset>
	
	</div>
</div>
<div id="box_yy"></div>
	</div>
