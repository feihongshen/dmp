<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.AccountCwbFareDetail"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.domain.PayUp"%>
<%@page import="cn.explink.domain.AccountCwbFare"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<User> userList = (List<User>)request.getAttribute("userList");
List<User> userListofbranch = (List<User>)request.getAttribute("userListofbranch");
Date now = new Date();
  String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
  String deliverystate=request.getAttribute("deliverystate")==null?"":request.getAttribute("deliverystate").toString();
  String userid=request.getAttribute("userid")==null?"":request.getAttribute("userid").toString();
  Page page_obj = (Page)request.getAttribute("page_obj");
  List customeridList =(List) request.getAttribute("customeridStr");
  List<AccountCwbFareDetail> acfdList=request.getAttribute("acfdList")==null?new ArrayList<AccountCwbFareDetail>():(List<AccountCwbFareDetail>)request.getAttribute("acfdList");
  AccountCwbFareDetail accountCwbFareDetailSum=request.getAttribute("accountCwbFareDetailSum")==null?new AccountCwbFareDetail():(AccountCwbFareDetail)request.getAttribute("accountCwbFareDetailSum");

  Map<Long, AccountCwbFare> accountFareMap=(Map<Long, AccountCwbFare>)request.getAttribute("accountFareMap");
  
  
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

function change(){
	var optionstring="";
	
	if($("#branchid").val()!=0){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/user/",
			data:{branchid:$("#branchid").val()},
			success:function(data){
				optionstring+="<option value='0'>请选择</option>";
				for(var i=0;i<data.length;i++){
					optionstring+="<option value='"+data[i].userid+"'>"+data[i].realname+"</option>";
				}
				//alert(optionstring);
				$("#userid").html(optionstring);
			}
		});
	}else{
		optionstring+="<option value='0'>请选择</option>";
		
		<%for(User u : userList){ %>
			optionstring += "<option value=<%=u.getUserid()%>><%=u.getRealname()%></option>";
        <%} %>
        $("#userid").html(optionstring);
	}
	
}
function Days(){     
	var day1 = $("#strtime").val();   
	var day2 = $("#endtime").val(); 
	var y1, y2, m1, m2, d1, d2;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	var date1 = new Date(y1, m1, d1);            
	var date2 = new Date(y2, m2, d2);   
	var minsec = Date.parse(date2) - Date.parse(date1);          
	var days = minsec / 1000 / 60 / 60 / 24;  
	if(days>31){
		return false;
	}        
	return true;
}
function check(){
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(!Days()||($("#strtime").val()=='' &&$("#endtime").val()!='')||($("#strtime").val()!='' &&$("#endtime").val()=='')){
		alert("时间跨度不能大于31天！");
		return false;
	}
	return true;
}
$("document").ready(function(){  

	$("#updateF").click(function(){
		
		var cwbstr="";//已妥投交款列表选中id
		
		$("input[name='cwb']:checkbox:checked").each(function() {
			cwbstr+=$(this).val()+",";
		});
		if(cwbstr.length>0){	
			confirmSubVerify(cwbstr);
		}else{
			alert("暂无审核信息");
			return false;
		}
	});
	
	
	
	
}); 


function confirmSubVerify(cwbstr){
	if(confirm("确定要审核吗？")){
 		$("#updateF").attr("disabled","disabled");
    	$("#updateF").val("请稍候..");
    	
    	$("#cwbs").val(cwbstr.length>0?cwbstr.substring(0,cwbstr.length-1):"");
    	
    	$("#subVerifyForm").submit();
    	
	}
}

function sumitForm(){
	if(check()){
		$("#searchForm").submit();
	}
}

$(function() {
	$("#strtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择供货商' });
});
function isgetallcheck(){
	if($('input[name="cwb"]:checked').size()>0){
		$('input[name="cwb"]').each(function(){
			if($(this).attr("disabled")==false||$(this).attr("disabled")==undefined){
				$(this).attr("checked",false);
			}
		});
	}else{
		$('input[name="cwb"]').each(function(){
			if($(this).attr("disabled")==false||$(this).attr("disabled")==undefined){
				$(this).attr("checked",true);
			}
		});
	}
}
function changeTime(){
	if($("#verifyflag").val()==0){
		$("#verifytime").val(1);
	}else{
		$("#verifytime").val(2);
		$("#updateF").hide();
	}
}


</script>   
</HEAD>
<body style="background:#fff" marginwidth="0" marginheight="0">
<form id="verifyForm" action="<%=request.getContextPath()%>/accountcwbfare/payfareVerify" method="post">
	<input type="hidden" name="begindate" value="<%=starttime%>"/>
</form>	
   <div class="menucontant">
		<form id="searchForm" action ="<%=request.getContextPath()%>/accountcwbfaredetailVerify/accountfarelist/1" method = "post">
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1" border="1">
			  <tr>
					<td width="100%">
						供货商:
						<select name ="customerid" id ="customerid" multiple="multiple" style="width: 300px;">
				          <%for(Customer c : customerlist){ %>
				           <option value ="<%=c.getCustomerid() %>" 
				            <%if(!customeridList.isEmpty()) 
					            {for(int i=0;i<customeridList.size();i++){
					            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
					            		%>selected="selected"<%
					            	 break;
					            	}
					            }
						     }%>><%=c.getCustomername() %></option>
				          <%} %>
				        </select>
				        [<a href="javascript:multiSelectAll('customerid',1,'请选择');">全选</a>]
						[<a href="javascript:multiSelectAll('customerid',0,'请选择');">取消全选</a>]
						审核状态:
						<select id="verifyflag" name="verifyflag" onchange="changeTime();">
							<option value="0" <%=request.getParameter("verifyflag")!=null&&request.getParameter("verifyflag").equals("0")?"selected":"" %>>未审核</option>
							<option value="1" <%=request.getParameter("verifyflag")!=null&&request.getParameter("verifyflag").equals("1")?"selected":"" %>>已审核</option>
						</select>
						
						<select id="verifytime" name="verifytime" disabled="disabled">
							<option value="1" <%=request.getParameter("verifytime")!=null&&request.getParameter("verifytime").equals("1")?"selected":"" %>>交款时间</option>
							<option value="2" <%=request.getParameter("verifytime")!=null&&request.getParameter("verifytime").equals("2")?"selected":"" %>>审核时间</option>
						</select>
							<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
						到
							<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
						<font color="red">（查询31天以内数据）</font>
					        <br/>
						配送站点：<select name ="deliverybranchid" id="branchid" onchange="change()">
				               <option value="-1">全部</option>
				               <%if(branchList != null && branchList.size()>0){ %>
				                <%for( Branch b:branchList){ %>
				               <option value ="<%=b.getBranchid()%>" <%if(b.getBranchid() == new Long(request.getParameter("deliverybranchid")==null?"-1":request.getParameter("deliverybranchid"))) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
				               <%} }%>
			              </select>
			            小件员：  <select name ="userid" id ="userid" >
			            <option value="0">请选择</option>
						<%for(User u:userListofbranch){ %>
						<option value="<%=u.getUserid()%>" <%if(userid.equals(u.getUserid()+"")) {%> selected="selected"<%} %>><%=u.getRealname()%></option>
						<%} %>
			            </select>
						 配送结果：
						<select name ="deliverystate" id ="deliverystate" >
							<option value ="-1">请选择</option>
							<option value="<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue() %>" <%if(deliverystate.equals(""+DeliveryStateEnum.ShangMenTuiChengGong.getValue())){ %>selected="selected"<%} %>><%=DeliveryStateEnum.ShangMenTuiChengGong.getText() %></option>
							<option value="<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>" <%if(deliverystate.equals(""+DeliveryStateEnum.ShangMenJuTui.getValue())){ %>selected="selected"<%} %>><%=DeliveryStateEnum.ShangMenJuTui.getText() %></option>
						</select>
						
						 应收运费：
						<select name ="shoulefarefeesign" id ="shoulefarefeesign" >
							<option value="1" <%=request.getParameter("shoulefarefeesign")!=null&&request.getParameter("shoulefarefeesign").equals("1")?"selected":"" %>>大于0</option>
							<option value="0" <%=request.getParameter("shoulefarefeesign")!=null&&request.getParameter("shoulefarefeesign").equals("0")?"selected":"" %>>等于0</option>
						</select>	
						 每页<select name="pageNumber" id="pageNumber">
								<option value="100">100</option>
								<option value="300">300</option>
								<option value="500">500</option>
								<option value="800">800</option>
								<option value="1000">1000</option>
							</select>行
					<input type ="button" value ="查询" class="input_button2" onclick="sumitForm();"/>
					<input type="button" value="导出" class="input_button2"  onclick="exportfile()">
					</td>
				</tr>
				</table>
		 </form>
		<form action="<%=request.getContextPath()%>/accountcwbfaredetailVerify/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="begindate" value="<%=starttime%>"/>
		<input type="hidden" name="enddate" value="<%=endtime%>"/>
		<input type="hidden" name="userid" value="<%=userid%>"/>
		<input type="hidden" name="verifyflag" value="<%=request.getParameter("verifyflag")==null?"0":request.getParameter("verifyflag")%>"/>
		<input type="hidden" name="deliverybranchid" value="<%=request.getParameter("deliverybranchid")==null?"0":request.getParameter("deliverybranchid")%>"/>
		<input type="hidden" name="deliverystate" value="<%=request.getParameter("deliverystate")==null?"-1":request.getParameter("deliverystate")%>"/>
		<input type="hidden" name="shoulefarefeesign" value="<%=request.getParameter("shoulefarefeesign")==null?"1":request.getParameter("shoulefarefeesign")%>"/>
		<div style="display: none;">
			<select name ="customerid" id ="customerid" multiple="multiple" style="width: 300px;">
	          <%for(Customer c : customerlist){ %>
	           <option value ="<%=c.getCustomerid() %>" 
	            <%if(!customeridList.isEmpty()) 
		            {for(int i=0;i<customeridList.size();i++){
		            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
		            		%>selected="selected"<%
		            	 break;
		            	}
		            }
			     }%>><%=c.getCustomername() %></option>
	          <%} %>
	        </select>
		</div>
	</form>
	
	<form action="<%=request.getContextPath()%>/accountcwbfaredetailVerify/verify" method="post" id="subVerifyForm">
		<input type="hidden" name="cwbs" id="cwbs" value=""/>
	</form>
 
	<div>	
		<h1 style="line-height:30px; font-size:18px; font-family:'微软雅黑', '黑体'; font-weight:bold; color:#369"><%="1".equals(request.getParameter("verifyflag"))?"已审核":"未审核" %>记录：</h1>	
		<div>	
				<!--滑动内容开始 -->
				<div >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
						<tbody>
							<tr class="font_1">
								<td width="80" align="center" valign="middle" ><a style="cursor: pointer;" onclick="isgetallcheck();">[全选/反选]</a></td>
								<td  align="center" valign="middle" >订单号</td>
								<td  align="center" valign="middle" >小件员</td>
								<td align="center" valign="middle" >供货商</td>
								<td align="center" valign="middle" >配送站点</td>
								<td align="center" valign="middle" >归班时间</td>
								<td align="center" valign="middle" >配送结果</td>
								<td align="center" valign="middle" >应收运费[元]</td>
								<td align="center" valign="middle" >实收运费[元]</td>
								<td align="center" valign="middle" >交款时间</td>
								<td align="center" valign="middle" >交款方式</td>
								<td align="center" valign="middle" >交款人</td>
								<td align="center" valign="middle" >卡号</td>
								<td align="center" valign="middle" >审核状态</td>
								<td align="center" valign="middle" >审核时间</td>
							</tr>
					 		<%if(acfdList.size()>0){for(AccountCwbFareDetail acfd: acfdList){
								String girouser=accountFareMap.get(acfd.getFareid()).getGirouser();
								String cashuser=accountFareMap.get(acfd.getFareid()).getCashuser();
								
								String jiaokuanren= "";
								if(girouser.length()>0)
								{
									jiaokuanren=girouser;
								}
								if(cashuser.length()>0)
								{
									jiaokuanren=cashuser;
								}
								if(cashuser.length()>0&&girouser.length()>0)
								{  
									jiaokuanren=cashuser+"--"+girouser;
								}
								BigDecimal girofee=accountFareMap.get(acfd.getFareid()).getGirofee();
								BigDecimal cashfee=accountFareMap.get(acfd.getFareid()).getCashfee();
								double girofee1=girofee==null?0:girofee.doubleValue();
								double cashfee1=cashfee==null?0:cashfee.doubleValue();
								String jiaokuantype= "";
								if(girofee1>0)
								{
									jiaokuantype="转账";
								}
								if(cashfee1>0)
								{
									jiaokuantype="现金";
								}
								if(cashfee1>0&&girofee1>0)
								{
									jiaokuantype="现金--转账";
								}
								 
								%> 
								<tr valign="middle">
									<td><input id="cwb" name="cwb" type="checkbox" value="<%=acfd.getCwb()%>" <%if(acfd.getVerifyflag()>0){ %> disabled="disabled" <%}else{ %>checked="checked" <%} %> onClick="changeYj()" infactfare="<%=acfd.getInfactfare()%>"/></td>
									<td align="center" valign="middle" ><%=acfd.getCwb()%></td>
									<td align="center" valign="middle" ><%for(User u :userList){if(acfd.getUserid()==u.getUserid()){out.print(u.getRealname());}} %></td>
									<td align="center" valign="middle" ><%for(Customer c :customerlist){if(acfd.getCustomerid()==c.getCustomerid()){out.print(c.getCustomername());}} %></td>
									<td align="center" valign="middle"  ><%for(Branch b :branchList){if(acfd.getDeliverybranchid()==b.getBranchid()){out.print(b.getBranchname());}} %></td>
									<td align="center" valign="middle" ><%=acfd.getAudittime()%></td>
									<td align="center" valign="middle" ><%for(DeliveryStateEnum ds : DeliveryStateEnum.values()){if(acfd.getDeliverystate()==ds.getValue()){out.print(ds.getText());}} %></td>
									<td align="center" valign="middle" ><%=acfd.getShouldfare()%></td>
									<td align="center" valign="middle" ><%=acfd.getInfactfare()%></td>
									<td align="center" valign="middle" ><%=acfd.getPayuptime()%></td>
									<td align="center" valign="middle" ><%=jiaokuantype%></td>
									<td align="center" valign="middle" ><%=jiaokuanren%></td>
									<td align="center" valign="middle" ><%=accountFareMap.get(acfd.getFareid()).getGirocardno()%></td>
									<td align="center" valign="middle" ><%=acfd.getVerifyflag()>0?"已审核":"未审核"%></td>
									<td align="center" valign="middle" ><%=acfd.getVerifytime()==null?"":acfd.getVerifytime()%></td>
								 </tr>
							 <%}} %>
							<tr valign="middle" >
								<td>合计</td>
								<td><strong><%=page_obj.getTotal() %></strong>单</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><strong><%=accountCwbFareDetailSum.getShouldfare()==null?BigDecimal.ZERO:accountCwbFareDetailSum.getShouldfare() %></strong></td>
								<td><strong><%=accountCwbFareDetailSum.getInfactfare()==null?BigDecimal.ZERO:accountCwbFareDetailSum.getInfactfare() %></strong></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<%if(request.getParameter("verifyflag")==null||Long.parseLong(request.getParameter("verifyflag"))==0){%>
						    <tr  valign="middle">
						       <td colspan ="20" align="center" valign="middle" >
							      <form id="updateForm" action ="<%=request.getContextPath()%>/funds/update"  method = "post">
				                      
				                      <input type="button" id="updateF"  value="审核"/>
				                  </form>
			                  </td>
				            </tr>
				            <%} %>	
						</tbody>
					</table>
				</div>
				<!--滑动内容结束 -->
		   </div>
		</div>
		<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
		<%if(page_obj.getMaxpage()>1){ %>
		<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
								<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>"><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
			</table>
		</div>
	<%} %>
	</div>
<div class="jg_10"></div>
<div class="clear"></div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);

function exportfile(){
	if(<%=acfdList.size()>0%> ){
		$("#searchForm2").submit();
	}else{
		alert("没有数据无法导出！");
	}
}
</script> 
</body>
</HTML>
   
