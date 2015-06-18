
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

%>
<div id="box_bg"></div>
<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="overflow: scroll;width: 800px;height: 600px;">
		<h1><div id="close_box" onclick="closeBox()"></div>根据时效创建对内扣罚单</h1>
		<form method="post" id="shixiaoform"  action="<%=request.getContextPath()%>/inpunish/findinpunishbyShixiao" onsubmit="if(check_CreatepunishByShixiao())createinpunishbyShixiao(this);return false;">
			<table width="900" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox">
				<tr>
					<td width="600" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" style="height:280px">
						<tr class="font_1">
							<td colspan="1" align="left" valign="top">
							策略名称：
							<select id="celuename" name="celuename" class="select1">
							<option value="0" selected="selected">请选择策略名称</option>
							</select>
							&nbsp;&nbsp;考核项目：
							<select id="kaoheproject" name="kaoheproject" class="select1">
									<option value="0">请选择考核项目</option>
							</select>		
							&nbsp;&nbsp;订单号：
							<input type="text" id="cwb" name="cwb" class="input_text1" style="height:15px;width: 120px;"/>
							<input type="hidden" id="isshow" name="isshow" value="1"/>
						</td>				
							</tr>
						<tr class="font_1">
						<td align="left" valign="top">
							<input type="submit"   value="查询" class="input_button2"/>
							<input type="reset"    value="重置" class="input_button2" >
					</td>
						</tr>
			</table>
			</td>
			</tr>
			</table>
			</form>
			<tr class="font_1">
				<td  align="left" valign="top">
				 <div class="right_title">
        	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table4">
        		<tbody >
        			<tr class="font_1" height="30">
        				<td align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">超期时长</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">客户</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">当前状态</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">入库库房</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">上一站</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">当前机构</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">下一站</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">配送站点</td>
        				</tr>
        				
        				<tr class="font_1" height="30" onclick="clickonenum(this);">
        				<td align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">超期时长</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">客户</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">当前状态</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">入库库房</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">上一站</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">当前机构</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">下一站</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">配送站点</td>
        				</tr>
                        </tbody>
                        
                    </table>
            </div>
				</td>
						</tr>
		<form method="post" id="form2" onSubmit="if(check_createbyshixiaokaohe()){submitPunishCreateByshixiaokoahe(this);}return false;" action="<%=request.getContextPath()%>/inpunish/createbyshixiaofile;jsessionid=<%=session.getId()%>" enctype="multipart/form-data">
			<table width="900" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox">
				<tr>
					<td width="600" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" style="height:280px">
						<tr class="font_1">
							<td align="left" valign="top">
							扣罚大类<font color="red">*</font>：
							<select id="punishbigsort4" name="punishbigsort4" class="select1" onchange="findbigAdd(this,'punishbigsort2');">
								<option value="0">==请选择扣罚大类==</option>
								<%if(penalizebigList!=null&&penalizebigList.size()>0){for(PenalizeType   pType :penalizebigList) {%>
								<option value="<%=pType.getId()%>"><%=pType.getText() %></option>
								<%}} %>
							</select>
							&nbsp;&nbsp;扣罚小类：
							<select id="punishsmallsort4" name="punishsmallsort4" class="select1" onchange="findbigAdd(this,'punishbigsort2');">
								<option value="0">==请选择扣罚小类==</option>
								<%if(penalizesmallList!=null&&penalizesmallList.size()>0){for(PenalizeType     penType:penalizesmallList) {%>
								<option value="<%=penType.getId()%>" id="<%=penType.getParent() %>"><%=penType.getText() %></option>
								<%}} %>
							</select>
							&nbsp;&nbsp;责任机构<font color="red">*</font>:<select  id="dutybranchid4" name="dutybranchid4" class="select1" onchange="selectbranchUsers('dutypersoname3','dutybranchid3');">
							<option value="0" selected="selected">请选择责任机构</option>
							<%if(branchList!=null){for(Branch branch:branchList){ %>
							<option value="<%=branch.getBranchid() %>"><%=branch.getBranchname() %></option>
							<% }}%>
						</select>
						&nbsp;&nbsp;责任人:
							<select id="dutypersoname4" name="dutypersoname4" class="select1" >
							<option value ='0' selected="selected">请选择机构责任人</option>
							</select>
						
							</td>
						
						</tr >
						<tr class="font_1">
						<td align="left" valign="top">
						货物扣罚金额<font color="red">*</font>:<input type="text" id="shixiaogoodprice4" name="shixiaoqitaprice4" class="input_text1" style="height:15px;width: 120px;" onkeyup="alculateSumprice(this,'shixiaoqitaprice4','punishprice4');"/>
						&nbsp;&nbsp;其它扣罚金额<font color="red">*</font>:<input type="text" id="shixiaoqitaprice4" name="shixiaoqitaprice4" class="input_text1" style="height:15px;width: 120px;" onfocus="javascript:if(this.value=='0.00') this.value=''" onblur="javascript:if(this.value=='') this.value='0.00'" value="0.00"/>
						&nbsp;&nbsp;扣罚金额<font color="red">*</font>:<input type="text" id="punishprice4" name="punishprice4" class="input_text1" style="height:15px;width: 120px;"/>
						&nbsp;&nbsp;上传附件：
						<label for="fileField"></label>
						<span id="swfupload-control"><input type="text" id="txtFileName" disabled="true" style="border: solid 1px; background-color: #FFFFFF;" /><input type="button" id="button" /></span>*
						</td>
						</tr>
						 <tr class="font_1">
							<td  align="left" valign="top"> 
							扣罚说明：<textarea name="describe4" id="describe4" cols="40" rows="4"  onfocus="if(this.value == '最多100个字') this.value = ''" onblur="if(this.value == '') this.value = '最多100个字'">最多100个字</textarea>
							</td>
						</tr>  
					</table>
					
					</td>
				</tr>
			</table>
			<input type="hidden" id="type4" name="type4" value="4"/>
			<input type="hidden" id="availablecwb4" name="availablecwb4" value=""/>
			<input type="hidden" id="cwbhhh4" name="cwbhhh4" value=""/>
			<div align="center">
				<input type="submit" value="提交" class="button">
				<input type="button" onclick="closeBox()"  value="取消" class="button">
			</div>
		</form>
	</div>
</div>