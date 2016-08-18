<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.b2c.tps.TpsAutoCfg"%>
<%@page import="cn.explink.domain.Branch" %>

<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<%
TpsAutoCfg tpsAutoCfg = (TpsAutoCfg)request.getAttribute("tpsAutoCfg");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
%>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>TPS自动化分拣对接设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/tpsAutoFlowCfg/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(tpsAutoCfg != null){ %>
						<li><span>开启自动化分拣：</span>
							<input type ="radio" id="autoOpenFlag1" name ="autoOpenFlag" value="1"  <%if(tpsAutoCfg.getAutoOpenFlag()==1){%>checked<%}%> >开启
							<input type ="radio" id="autoOpenFlag2" name ="autoOpenFlag" value="0"  <%if(tpsAutoCfg.getAutoOpenFlag()==0){%>checked<%}%> >关闭
						</li>
						<li><span>订单导入库房：</span>
                            <select name="warehouseId">
                                <option value="0">请选择库房</option>
                                <%for(Branch b:warehouselist){
                                %>
                                    <option value="<%=b.getBranchid()%>" <%if(tpsAutoCfg.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
                                <%}%>
                            </select>
                        </li>
						<li><span>开启交接单号：</span>
							<input type ="radio" id="batchnoOpenFlag1" name ="batchnoOpenFlag" value="1"  <%if(tpsAutoCfg.getBatchnoOpenFlag()==1){%>checked<%}%> >开启
							<input type ="radio" id="batchnoOpenFlag2" name ="batchnoOpenFlag" value="0"  <%if(tpsAutoCfg.getBatchnoOpenFlag()==0){%>checked<%}%> >关闭
						</li>
						<li><span>开启合包出库：</span>
							<input type ="radio" id="hebaoFlag1" name ="hebaoFlag" value="0"  <%if(tpsAutoCfg.getHebaoFlag()==0){%>checked<%}%> >开启
							<input type ="radio" id="hebaoFlag2" name ="hebaoFlag" value="1"  <%if(tpsAutoCfg.getHebaoFlag()==1){%>checked<%}%> >关闭
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" >* 
						</li>
					
					<%}else{ %>
					
						<li><span>开启自动化分拣：</span>
							<input type ="radio" id="autoOpenFlag1" name ="autoOpenFlag" value="1" >开启
							<input type ="radio" id="autoOpenFlag2" name ="autoOpenFlag" value="0" checked>关闭
						</li>
						<li><span>订单导入库房：</span>
                            <select name="warehouseId">
                                <option value="0">请选择库房</option>
                                <%for(Branch b:warehouselist){
                                %>
                                    <option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
                                <%}%>
                            </select>
                        </li>
						<li><span>开启交接单号：</span>
							<input type ="radio" id="batchnoOpenFlag1" name ="batchnoOpenFlag" value="1" >开启
							<input type ="radio" id="batchnoOpenFlag2" name ="batchnoOpenFlag" value="0" checked>关闭
						</li>
						<li><span>开启合包出库：</span>
							<input type ="radio" id="hebaoFlag1" name ="hebaoFlag" value="0" checked>开启
							<input type ="radio" id="hebaoFlag2" name ="hebaoFlag" value="1" >关闭
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" >* 
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

