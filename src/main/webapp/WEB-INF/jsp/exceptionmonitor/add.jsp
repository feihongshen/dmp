<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<% 
	List<Branch> branchlist = request.getAttribute("branchList")==null?null:(List<Branch>)request.getAttribute("branchList");
	long sitetype=Long.parseLong(request.getAttribute("sitetype")==null?"0":request.getAttribute("sitetype").toString());
	String modelname=StringUtil.nullConvertToEmptyString(request.getAttribute("modelname"));
	long A1  = request.getAttribute("A1") == null ?0L : (Long)request.getAttribute("A1");
	long A2  = request.getAttribute("A2") == null ?0L : (Long)request.getAttribute("A2");
	long A3  = request.getAttribute("A3") == null ?0L : (Long)request.getAttribute("A3");
	long A4  = request.getAttribute("A4") == null ?0L : (Long)request.getAttribute("A4");
	long A5  = request.getAttribute("A5") == null ?0L : (Long)request.getAttribute("A5");
	long A6  = request.getAttribute("A6") == null ?0L : (Long)request.getAttribute("A6");
	long A7  = request.getAttribute("A7") == null ?0L : (Long)request.getAttribute("A7");
	long A8  = request.getAttribute("A8") == null ?0L : (Long)request.getAttribute("A8");
	long A9  = request.getAttribute("A9") == null ?0L : (Long)request.getAttribute("A9");
	long A10 = request.getAttribute("A10") == null ?0L : (Long)request.getAttribute("A10");
	long A11 = request.getAttribute("A11") == null ?0L : (Long)request.getAttribute("A11");
	String oBranchids=StringUtil.nullConvertToEmptyString(request.getAttribute("oBranchids"));
	long id=Long.parseLong(request.getParameter("id")==null?"0":request.getParameter("id").toString());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>超期异常时效设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script>
var sitetype="<%=sitetype%>";

$(function(){
	$("#branchids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择机构类型' });
	if(sitetype>0){
		$("#viewAll").attr("style","display:''"); 
		if(sitetype=="<%=BranchEnum.KuFang.getValue()%>"){
			$("#viewA1").attr("style","display:''"); 
			$("#viewA6").attr("style","display:''"); 
		}else if(sitetype=="<%=BranchEnum.ZhanDian.getValue()%>"){
			$("#viewA6").attr("style","display:''"); 
			$("#viewA3").attr("style","display:''"); 
			$("#viewA4").attr("style","display:''"); 
			$("#viewA9").attr("style","display:''"); 
			$("#viewA10").attr("style","display:''"); 
		}else if(sitetype=="<%=BranchEnum.ZhongZhuan.getValue()%>"){
			$("#viewA7").attr("style","display:''"); 
			$("#viewA2").attr("style","display:''"); 
			$("#viewA11").attr("style","display:''"); 
		}else if(sitetype=="<%=BranchEnum.TuiHuo.getValue()%>"){
			$("#viewA8").attr("style","display:''"); 
			$("#viewA5").attr("style","display:''"); 
		}
	}
	
	$("#subBtn").click(function(){
		if($("#sitetype").val()==0){
			alert("请选择机构类型");
			return;
		}
		
		var branchids=$(".multiSelectOptions input[name='branchids']:checked ").size();
		if(!branchids>0){
			alert("请选择机构名称");
			return;
		}
		
		if($("#modelname").val()==""){
			alert("请设置超期异常名称");
			return;
		}
		
		 $.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/ExceptionMonitor/systemsave",
				data:$("#subForm").serialize(),
				dataType:"json",
				success : function(data) {  
					alert(data.error);
					window.location.href="<%=request.getContextPath()%>/ExceptionMonitor/system";
				}
			}); 
		
		<%-- $("#subForm").attr('action','<%=request.getContextPath()%>/ExceptionMonitor/systemsave');
		$("#subForm").submit(); --%>
	});
});

//改变机构类型
function changeSitetype(){
	window.location.href="<%=request.getContextPath()%>/ExceptionMonitor/add?sitetype="+$("#sitetype").val();
}



</script>

<div style="background:#eef9ff">
<form id="subForm" action="" method="post">
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="location='<%=request.getContextPath()%>/ExceptionMonitor/system'"></div>设置监控时长</h1>
			<div id="box_form">
				<ul>
	           		<li><span>机构类型：</span>
	           			<%if(id>0){
	           				for(BranchEnum be:BranchEnum.values()){if(sitetype==be.getValue()){ out.print(be.getText());}}
	           			%>
	           				<input type="hidden" name="sitetype" id="sitetype" value="<%=sitetype%>"/>
	           			<%}else{%>
	           			<select name="sitetype" id="sitetype" onchange="changeSitetype()">
				            <option value="0">请选择</option>
				            <option value="<%=BranchEnum.KuFang.getValue()%>" <%=BranchEnum.KuFang.getValue()==sitetype?"selected":"" %>><%=BranchEnum.KuFang.getText()%></option>
				            <option value="<%=BranchEnum.ZhanDian.getValue()%>" <%=BranchEnum.ZhanDian.getValue()==sitetype?"selected":"" %>><%=BranchEnum.ZhanDian.getText()%></option>
				            <option value="<%=BranchEnum.ZhongZhuan.getValue()%>" <%=BranchEnum.ZhongZhuan.getValue()==sitetype?"selected":"" %>><%=BranchEnum.ZhongZhuan.getText()%></option>
				            <option value="<%=BranchEnum.TuiHuo.getValue()%>" <%=BranchEnum.TuiHuo.getValue()==sitetype?"selected":"" %>><%=BranchEnum.TuiHuo.getText()%></option>
			           	</select>*
			           	<%}%>
					</li>
					<li><span>机构名称：</span>
	           			<select name ="branchids" id ="branchids"  multiple="multiple" style="width:320px;">
				         <%if(branchlist!=null&&!branchlist.isEmpty()){
				         	for(Branch b : branchlist){ %>
				          	<option value ="<%=b.getBranchid()%>" 
				          	<%if(!"".equals(oBranchids)){
				          		for(int i=0;i<oBranchids.split(",").length;i++){
				          			if(b.getBranchid()==Long.parseLong(oBranchids.split(",")[i])){
				          				out.print("selected=selected'");
				          			}
				          		}
				          	}%>><%=b.getBranchname()%></option>
				          <%}}%>
						 </select>*
						[<a href="javascript:multiSelectAll('branchids',1,'请选择');">全选</a>]
						[<a href="javascript:multiSelectAll('branchids',0,'请选择');">取消全选</a>]
					</li>
					<li><span>超期异常名称：</span>
						<input type="text" id="modelname" name="modelname" value="<%=modelname%>"/>*
					</li>
					<li id="viewA1" style="display:none"><span>超期未出库：</span>
	           			<select name="A1">
							<option value="4" <%=A1== 4?"selected":"" %>>4小时</option>
							<option value="6" <%=A1== 6?"selected":"" %>>6小时</option>
							<option value="12" <%=A1 == 12?"selected":"" %>>12小时</option>
							<option value="24" <%=A1== 24?"selected":"" %>>1天</option>
							<option value="48" <%=A1 == 48?"selected":"" %>>2天</option>
							<option value="72" <%=A1 == 72?"selected":"" %>>3天</option>
							<option value="96" <%=A1 == 96?"selected":"" %>>4天</option>
							<option value="120" <%=A1 == 120?"selected":"" %>>5天</option>
							<option value="144" <%=A1 == 144?"selected":"" %>>6天</option>
							<option value="168" <%=A1 == 168?"selected":"" %>>7天</option>
						</select>
					</li>
					<li id="viewA6" style="display:none"><span>超期未到货：</span>
	           			<select name="A6">
							<option value="4" <%=A6== 4?"selected":"" %>>4小时</option>
							<option value="6" <%=A6 ==6?"selected":"" %>>6小时</option>
							<option value="12" <%=A6 ==12?"selected":"" %>>12小时</option>
							<option value="24" <%=A6 == 24?"selected":"" %>>1天</option>
							<option value="48" <%=A6 ==48?"selected":"" %>>2天</option>
							<option value="72" <%=A6 ==72?"selected":"" %>>3天</option>
							<option value="96" <%=A6 ==96?"selected":"" %>>4天</option>
							<option value="120" <%=A6 ==120?"selected":"" %>>5天</option>
							<option value="144" <%=A6 ==144?"selected":"" %>>6天</option>
							<option value="168" <%=A6 ==168?"selected":"" %>>7天</option>
						</select>
					</li>
					<li id="viewA3" style="display:none"><span>超期未领货：</span>
		      			<select name="A3">
							<option value="4" <%=A3== 4?"selected":"" %>>4小时</option>
							<option value="6" <%=A3 ==6?"selected":"" %>>6小时</option>
							<option value="12" <%=A3 ==12?"selected":"" %>>12小时</option>
							<option value="24" <%=A3 == 24?"selected":"" %>>1天</option>
							<option value="48" <%=A3 ==48?"selected":"" %>>2天</option>
							<option value="72" <%=A3 ==72?"selected":"" %>>3天</option>
							<option value="96" <%=A3 ==96?"selected":"" %>>4天</option>
							<option value="120" <%=A3 ==120?"selected":"" %>>5天</option>
							<option value="144" <%=A3 ==144?"selected":"" %>>6天</option>
							<option value="168" <%=A3 ==168?"selected":"" %>>7天</option>
						</select>
		    		</li>
					<li id="viewA4" style="display:none"><span>超期未归班：</span>
			      		<select name="A4">
							<option value="4" <%=A4== 4?"selected":"" %>>4小时</option>
							<option value="6" <%=A4 == 6?"selected":"" %>>6小时</option>
							<option value="12" <%=A4 == 12?"selected":"" %>>12小时</option>
							<option value="24" <%=A4 == 24?"selected":"" %>>1天</option>
							<option value="48" <%=A4 == 48?"selected":"" %>>2天</option>
							<option value="72" <%=A4 == 72?"selected":"" %>>3天</option>
							<option value="96" <%=A4 == 96?"selected":"" %>>4天</option>
							<option value="120" <%=A4 == 120?"selected":"" %>>5天</option>
							<option value="144" <%=A4 == 144?"selected":"" %>>6天</option>
							<option value="168" <%=A4 == 168?"selected":"" %>>7天</option>
						</select>
		    		</li>
					<li id="viewA9" style="display:none"><span>超期滞留：</span>
			      		<select name="A9">
							<option value="4" <%=A9== 4?"selected":"" %>>4小时</option>
							<option value="6" <%=A9 == 6?"selected":"" %>>6小时</option>
							<option value="12" <%=A9 == 12?"selected":"" %>>12小时</option>
							<option value="24" <%=A9 == 24?"selected":"" %>>1天</option>
							<option value="48" <%=A9 == 48?"selected":"" %>>2天</option>
							<option value="72" <%=A9 == 72?"selected":"" %>>3天</option>
							<option value="96" <%=A9 == 96?"selected":"" %>>4天</option>
							<option value="120" <%=A9 == 120?"selected":"" %>>5天</option>
							<option value="144" <%=A9 == 144?"selected":"" %>>6天</option>
							<option value="168" <%=A9 == 168?"selected":"" %>>7天</option>
						</select>
		    		</li>
					<li id="viewA10" style="display:none"><span>超期未退货：</span>
			      		<select name="A10">
							<option value="4" <%=A10== 4?"selected":"" %>>4小时</option>
							<option value="6" <%=A10 == 6?"selected":"" %>>6小时</option>
							<option value="12" <%=A10 == 12?"selected":"" %>>12小时</option>
							<option value="24" <%=A10 == 24?"selected":"" %>>1天</option>
							<option value="48" <%=A10 == 48?"selected":"" %>>2天</option>
							<option value="72" <%=A10 == 72?"selected":"" %>>3天</option>
							<option value="96" <%=A10 == 96?"selected":"" %>>4天</option>
							<option value="120" <%=A10 == 120?"selected":"" %>>5天</option>
							<option value="144" <%=A10 == 144?"selected":"" %>>6天</option>
							<option value="168" <%=A10 == 168?"selected":"" %>>7天</option>
						</select>
		    		</li>
					<li id="viewA7" style="display:none"><span>超期未到中转：</span>
			      		<select name="A7">
							<option value="4" <%=A7== 4?"selected":"" %>>4小时</option>
							<option value="6" <%=A7 == 6?"selected":"" %>>6小时</option>
							<option value="12" <%=A7 == 12?"selected":"" %>>12小时</option>
							<option value="24" <%=A7 == 24?"selected":"" %>>1天</option>
							<option value="48" <%=A7 == 48?"selected":"" %>>2天</option>
							<option value="72" <%=A7 == 72?"selected":"" %>>3天</option>
							<option value="96" <%=A7 == 96?"selected":"" %>>4天</option>
							<option value="120" <%=A7 == 120?"selected":"" %>>5天</option>
							<option value="144" <%=A7 == 144?"selected":"" %>>6天</option>
							<option value="168" <%=A7 == 168?"selected":"" %>>7天</option>
						</select>
		    		</li>
					<li id="viewA2" style="display:none"><span>超期未中转：</span>
			      		<select name="A2">
							<option value="4" <%=A2== 4?"selected":"" %>>4小时</option>
							<option value="6" <%=A2 == 6?"selected":"" %>>6小时</option>
							<option value="12" <%=A2 == 12?"selected":"" %>>12小时</option>
							<option value="24" <%=A2 == 24?"selected":"" %>>1天</option>
							<option value="48" <%=A2 == 48?"selected":"" %>>2天</option>
							<option value="72" <%=A2 == 72?"selected":"" %>>3天</option>
							<option value="96" <%=A2 == 96?"selected":"" %>>4天</option>
							<option value="120" <%=A2 == 120?"selected":"" %>>5天</option>
							<option value="144" <%=A2 == 144?"selected":"" %>>6天</option>
							<option value="168" <%=A2 == 168?"selected":"" %>>7天</option>
						</select>
	    			</li>
					<li id="viewA8" style="display:none"><span>超期未到退货：</span>
			      		<select name="A8">
							<option value="4" <%=A8== 4?"selected":"" %>>4小时</option>
							<option value="6" <%=A8 == 6?"selected":"" %>>6小时</option>
							<option value="12" <%=A8 == 12?"selected":"" %>>12小时</option>
							<option value="24" <%=A8 == 24?"selected":"" %>>1天</option>
							<option value="48" <%=A8 == 48?"selected":"" %>>2天</option>
							<option value="72" <%=A8 == 72?"selected":"" %>>3天</option>
							<option value="96" <%=A8 == 96?"selected":"" %>>4天</option>
							<option value="120" <%=A8 == 120?"selected":"" %>>5天</option>
							<option value="144" <%=A8 == 144?"selected":"" %>>6天</option>
							<option value="168" <%=A8 == 168?"selected":"" %>>7天</option>
						</select>
		    		</li>
					<li id="viewA5" style="display:none"><span>超期未退供货商：</span>
			      		<select name="A5">
							<option value="4" <%=A5== 4?"selected":"" %>>4小时</option>
							<option value="6" <%=A5 == 6?"selected":"" %>>6小时</option>
							<option value="12" <%=A5 == 12?"selected":"" %>>12小时</option>
							<option value="24" <%=A5 == 24?"selected":"" %>>1天</option>
							<option value="48" <%=A5 == 48?"selected":"" %>>2天</option>
							<option value="72" <%=A5 == 72?"selected":"" %>>3天</option>
							<option value="96" <%=A5 == 96?"selected":"" %>>4天</option>
							<option value="120" <%=A5 == 120?"selected":"" %>>5天</option>
							<option value="144" <%=A5 == 144?"selected":"" %>>6天</option>
							<option value="168" <%=A5 == 168?"selected":"" %>>7天</option>
						</select>
		    		</li>
					<li id="viewA11" style="display:none"><span>超期中转未到站：</span>
			      		<select name="A11">
							<option value="4" <%=A11== 4?"selected":"" %>>4小时</option>
							<option value="6" <%=A11 ==6?"selected":"" %>>6小时</option>
							<option value="12" <%=A11 ==12?"selected":"" %>>12小时</option>
							<option value="24" <%=A11 == 24?"selected":"" %>>1天</option>
							<option value="48" <%=A11 ==48?"selected":"" %>>2天</option>
							<option value="72" <%=A11 ==72?"selected":"" %>>3天</option>
							<option value="96" <%=A11 ==96?"selected":"" %>>4天</option>
							<option value="120" <%=A11 ==120?"selected":"" %>>5天</option>
							<option value="144" <%=A11 ==144?"selected":"" %>>6天</option>
							<option value="168" <%=A11 ==168?"selected":"" %>>7天</option>
						</select>
		    		</li>
	         	</ul>
		</div>
		<div align="center">
		<input type="hidden" value="<%=id%>" id="id" name="id"/>
        <input type="button" value="确认" class="button" id="subBtn" />
        <input type="button" value="返回" class="button" id="cancel" onclick="location='<%=request.getContextPath()%>/ExceptionMonitor/system'" /></div>
	</div>
	</form>
</div>
</head>
</html>
