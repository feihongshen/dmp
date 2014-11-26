<%@page language="java" import="java.util.*,java.math.BigDecimal" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.AccountDeducDetail,cn.explink.enumutil.AccountFlowOrderTypeEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%
	List<Branch> branchList=(List<Branch>)request.getAttribute("branchList");
	List<AccountDeducDetail> detailList=request.getAttribute("detailList")==null?null:(List<AccountDeducDetail>)request.getAttribute("detailList");
	String flowordertype=request.getAttribute("flowordertype")==null?"":request.getAttribute("flowordertype").toString();
	String strtime=StringUtil.nullConvertToEmptyString(request.getParameter("strtime"));
	String endtime=StringUtil.nullConvertToEmptyString(request.getParameter("endtime"));
	
	BigDecimal hj=new BigDecimal("0");//合计金额
	int nums=0;//合计单数
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预扣款冲正管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript">
$(function(){
	//保存按钮
	$("#saveF").click(function(){
		var ids="";//选中id
		$('input[type="checkbox"][name="checkbox"]').each(
		       	function() {
		          	if($(this).attr("checked")=="checked"){
		          		ids+=$(this).val()+",";
		          	}
		          }
		       );
		
		$("#ids").val(ids.substring(0,ids.lastIndexOf(",")));
		if(ids.length==0){
			alert("无需审核的数据");
			return false;
		}
		
		if($("#branchid").val()==0){
			alert("请选择站点");
			return false;
		}
		
		$("#sh_hjJe").html(hj);
		$("#sh_hjDs").html(nums);
		
		$("#alert_box").show();
		centerBox();
	});

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
	
	$("#branchid").val("<%=request.getParameter("branchid")==null?0:request.getParameter("branchid")%>");
	
	//中转 退货 POS 点击事件
	$("#zzTab").click(function(){
		clearSelect();
		$("#flowordertype").val("<%=AccountFlowOrderTypeEnum.ZhongZhuan.getValue()%>,<%=AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue()%>");
		$("#searchForm").submit();
	});
	$("#thTab").click(function(){
		clearSelect();
		$("#flowordertype").val("<%=AccountFlowOrderTypeEnum.TuiHuo.getValue()%>");
		$("#searchForm").submit();
	});
	$("#posTab").click(function(){
		clearSelect();
		$("#flowordertype").val("<%=AccountFlowOrderTypeEnum.Pos.getValue()%>");
		$("#searchForm").submit();
	});
	
	//控制TAB标签高亮显示
	if("<%=flowordertype%>"=="<%=AccountFlowOrderTypeEnum.ZhongZhuan.getValue()%>,<%=AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue()%>"){
		$("#zzTab").attr("class","light");
	}if("<%=flowordertype%>"=="<%=AccountFlowOrderTypeEnum.TuiHuo.getValue()%>"){
		$("#thTab").attr("class","light");
	}if("<%=flowordertype%>"=="<%=AccountFlowOrderTypeEnum.Pos.getValue()%>"){
		$("#posTab").attr("class","light");
	}
	
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
});

function search(){
	if($("#branchid").val()==0){
		alert("请选择站点");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间要小于结束时间");
		return false;
	}
	$("#searchForm").submit();
}

//清空查询条件
function clearSelect(){
	$("#branchid").val(0);
	$("#strtime").val("");
	$("#endtime").val("");
	$("#cwb").val("");
}

function submitF(){
	if($("#memo").val().length>200){
		alert("备注不能超过200个字！");
		return false;
	}
	var params=$('#createForm').serialize();
	params+="&memo="+$("#memo").val();
	params+="&flowordertype="+$("#flowordertype").val();
	params+="&branchid="+$("#branchid").val();
	if(confirm("确认审核吗？")){
		$("#submitF").attr("disabled","disabled");
    	$("#submitF").val("请稍候");
    	$.ajax({
    		type: "POST",
    		url:'<%=request.getContextPath()%>/accountdeductrecord/saveRecord',
    		data:params,
    		dataType : "json",
    		success : function(data) {
    			if(data.errorCode==0){
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/accountdeductrecord/detailList";
    			}else{
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/accountdeductrecord/detailList";
    			}
    		}
    	});
	}
}

function exportField(){	
	var ids="";//选中id
	$('input[type="checkbox"][name="checkbox"]').each(
	       	function() {
	          	if($(this).attr("checked")=="checked"){
	          		ids+=$(this).val()+",";
	          	}
	          }
	       );
	$("#idsStr").val(ids.substring(0,ids.lastIndexOf(",")));
	if(ids.length==0){
		alert("请勾选要导出的数据");
		return false;
	}
	
	$("#clickExport").attr("disabled","disabled");
	$("#clickExport").val("请稍后");
 	$("#exportForm").submit(); 
}
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<!--弹窗开始 -->
<div id="alert_box" style="display:none">
  <div id="box_bg" ></div>
  <div id="box_contant" >
    <div id="box_top_bg"></div>
    <div id="box_in_bg">
      <h1>
        <div id="close_box" onclick="closeBox()"></div>
                     审核信息</h1>
        <div class="right_title" style="padding:10px">
          <table width="500" border="0" cellspacing="1" cellpadding="2" class="table_2" >
          <tr>
              <td width="20%" align="right">单数：</td>
              <td align="center" id='sh_hjDs'></td>
            </tr>
            <tr>
              <td align="right">货款[元]：</td>
              <td align="center"><strong id='sh_hjJe'></strong></td>
            </tr>
            <tr>
            	<td align="right">备注：</td>
            <td align="left"><textarea name="memo" id="memo" cols="50" rows="4"></textarea></td>
           	</tr>
            <tr>
              <td colspan="2" align="center" bgcolor="#F4F4F4">
              	<div class="jg_10"></div>
              	<input type="button" class="input_button1" onclick="submitF()" value="确 认" />
                  <div class="jg_10"></div>
                </td>
            </tr>
          </table>
        </div>
    </div>
  </div>
  <div id="box_yy"></div>
</div>
<!--弹窗结束-->
<div class="saomiao_box2">
	<div>
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="#" id="zzTab">中转</a></li>
				<li><a href="#" id="thTab">退货</a></li>
				<li><a href="#" id="posTab">POS</a></li>
			</ul>
		</div>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
			<tr>
				<td><form action="<%=request.getContextPath()%>/accountdeductrecord/detailList" method="post" id="searchForm">
						选择站点：
						<select id="branchid" name="branchid" style="width:150px;">	
							<option value="0">---------请选择--------</option>
							<%if(!branchList.isEmpty()){
								for(Branch b: branchList){%>
								<option value="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
							<%}}%>	
					    </select>
						时间：
						<input type="text" name="strtime" id="strtime" value="<%=strtime%>"/>
						至
						<input type="text" name="endtime" id="endtime" value="<%=endtime%>"/>
						订单号：
						<textarea name="cwb" rows="3" id="cwb" style="vertical-align:middle"><%=request.getParameter("cwb")==null?"":request.getParameter("cwb")%></textarea>
						<input type="hidden" name="flowordertype" id="flowordertype" value="<%=flowordertype%>"/>
						<input name="button" type="button" class="input_button2" onclick="search()" value="查询" />
						<input name="button" type="button" class="input_button2" onclick="clearSelect()" value="清空" />
					</form></td>
				<td>
				<%if(detailList!=null&&!detailList.isEmpty()){%>
				<input type="button" class="input_button1" id="clickExport"  onclick="exportField();" value="导出Excel" />
				<%}%></td>
			</tr>
			<%if(detailList!=null){ %>
			<tr>
				<td colspan="2" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="gd_table" >
						<tr class="font_1">
							<td width="40" bgcolor="#e7f4e3"><a href="#" onclick="btnClick();">全选</a></td>
							<td align="center" bgcolor="#e7f4e3">站点</td>
							<td align="center" bgcolor="#e7f4e3">订单号</td>
							<td bgcolor="#e7f4e3">类型</td>
							<td bgcolor="#e7f4e3">代收货款[元]</td>
							<td bgcolor="#e7f4e3">备注</td>
							<td bgcolor="#e7f4e3">操作时间</td>
							<td bgcolor="#e7f4e3">操作人</td>
						</tr>
						<%if(!detailList.isEmpty()){
							for(int i=0;i<detailList.size();i++){
								AccountDeducDetail list=detailList.get(i);
								hj=hj.add(list.getFee());//合计金额
								nums++;//合计单数
						%>
						<tr>
							<td><input type="checkbox" checked="true" onClick="changeYj('<%=list.getFee()%>','<%=i+1 %>')" 
							name="checkbox" id="btn_<%=i+1%>" value="<%=list.getId()%>"/>
							</td>
							<td align="center"><%=list.getBranchname()%></td>
							<td align="center"><%=list.getCwb()%></td>
							<td><%for(AccountFlowOrderTypeEnum ft : AccountFlowOrderTypeEnum.values()){
									if(list.getFlowordertype()==ft.getValue()){ 
										if(list.getFlowordertype()==AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue()){
											out.print("中转");
										}else{
											out.print(ft.getText());
										}
								}} 
								%>
							</td>
							<td><%=list.getFee()%></td>
							<td><%=list.getMemo()%></td>
							<td><%=list.getCreatetime()%></td>
							<td><%=list.getUsername()%></td>
						</tr>
						<%}}}%>
					</table></td>
			</tr>
		</table>

		<!--底部 -->
		<div style="height:70px"></div>
		<div class="iframe_bottom2" >
			<form id="createForm" action="" method="post">
				<table width="100%" border="0" cellspacing="1" cellpadding="1" class="table_5">
					<tr>
						<td align="center" bgcolor="#f4f4f4" style="font-family:'微软雅黑', '黑体'; font-size:18px">合计：
						<font id="hjDsShow" style="color:#F00"><%=nums%></font>&nbsp;&nbsp;单，共
						<font id="hjJeShow" style="color:#F00"><%=hj%></font>&nbsp;&nbsp;元</td>
					</tr>
					<tr>
					  <td align="center" bgcolor="#f4f4f4">
					    <input type="hidden" name="hjJe" id="hjJe" value="<%=hj%>"/>
			  	        <input type="hidden" name="hjDs" id="hjDs" value="<%=nums%>"/>
			  	        <input type="hidden" name="ids" id="ids"/>
						<input type="button" class="input_button1" id="saveF"  value="审 核" />
					  </td>
				  </tr>
				</table>
			</form>
		</div>
	
</div>
</div>
<script language="javascript">
var hjVar="<%=hj%>";
var numsVar="<%=nums%>";
var hj="<%=hj%>";
var nums="<%=nums%>"
//计算勾选未勾选应交款
function changeYj(fee,obj){
	if(document.getElementById("btn_"+obj).checked==true){//选中 相加
		nums=nums+1;
		hj=((parseFloat(hj).toFixed(2)*100+parseFloat(fee).toFixed(2)*100)/100).toFixed(2);
	}else{//相减
		nums=nums-1;
		hj=((parseFloat(hj).toFixed(2)*100-parseFloat(fee).toFixed(2)*100)/100).toFixed(2);
	}
	$("#hjJeShow").html(hj);
	$("#hjJe").val(hj);
	$("#hjDsShow").html(nums);
	$("#hjDs").val(nums);
}

//全选按钮
function btnClick(){
	$("[name='checkbox']").attr("checked",'true');//全选  
	$("#hjJeShow").html(hjVar);
	$("#hjJe").val(hjVar);
	$("#hjDsShow").html(numsVar);
	$("#hjDs").val(numsVar);
	hj=hjVar;
	nums=numsVar;
}
</script>
<form action="<%=request.getContextPath() %>/accountdeductrecord/exportByRecordByCZ" method="post" id="exportForm">
	<input type="hidden" name="branchid" id="branchid" value="<%=request.getParameter("branchid")==null?0:request.getParameter("branchid")%>"/>
	<input type="hidden" name="type" id="type" value="<%=flowordertype%>"/>
	<input type="hidden" name="stime" id="stime" value="<%=strtime%>"/>
	<input type="hidden" name="edtime" id="edtime" value="<%=endtime%>"/>
	<input type="hidden" name="idsStr" id="idsStr" value=""/>
	<textarea style="display:none" name="cwbs" id="cwbs"><%=request.getParameter("cwb")==null?"":request.getParameter("cwb")%></textarea>
</form>
</body>
</html>