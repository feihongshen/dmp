<%@page import="cn.explink.domain.Branch,cn.explink.domain.Function,cn.explink.enumutil.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Branch> branches = (List<Branch>)request.getAttribute("branches");
	List<Long> nextbranches = (List<Long>)request.getAttribute("nextbranches");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
var cwbtobranchids = "";
function clickNextStopBranch(id){
	var clickCwb = "";
	var isChoice = 0;//0为选用 1为取消
	//遍历全局变量 并判断此操作是取消还是选取
	for(var i = 0 ; i < cwbtobranchids.length ; i++){
		if(cwbtobranchids[i]!=id ){
			if(cwbtobranchids[i].length>0){clickCwb = clickCwb+","+cwbtobranchids[i];}
		}else{
			isChoice = 1;//如果触发的ID与cwbtobranchids中的数组有相同，则认为是取消操作
			/* $("#li_"+id).css('color','#000');
			$("#li_"+id).css('background','#fff'); */
			$("#li_"+id).removeClass("click");
		}
	}
	if(isChoice==0){
		clickCwb = clickCwb +","+ id; //如果不是取消，则将这个ID累加在数组中
		/* $("#li_"+id).css('color','#fff');
		$("#li_"+id).css('background','red'); */
		$("#li_"+id).addClass("click");
	}
	clickCwb = clickCwb.substring(1, clickCwb.length);
	$("#cwbtobranchid").val("");
	$("#cwbtobranchid").val(clickCwb);
	cwbtobranchids = clickCwb.split(",");
}

$(function() {
	$("#branchid").change(function(){
		cwbtobranchids = ""
		$("#cwbtobranchid").val("");
		$("#type").val(0);
		var branchid = $("#branchid").val();
		$("#BranchList li").show();
		/* $("#BranchList li").css('color','#000');
		$("#BranchList li").css('background','#fff'); */
		$("#BranchList li").removeClass("click");
		
		$("#li_"+branchid).hide();
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/branchRouteControl/gettoNextStopPage?branchid="+branchid,
			dataType:"json",
			success : function(data) {
				if(data.nextbranches.length!=0){
					$("#cwbtobranchid").val("");
					$("#cwbtobranchid").val(data.nextbranches);
					//将数组放入全局变量
					cwbtobranchids = $("#cwbtobranchid").val().split(",");
					for(var i = 0 ; i < cwbtobranchids.length ; i++){
						/* $("#li_"+cwbtobranchids[i]).css('color','#fff');
						$("#li_"+cwbtobranchids[i]).css('background','red'); */
						$("#li_"+cwbtobranchids[i]).addClass("click");
					}
					$("#type").val(<%=BranchRouteEnum.JinZhengXiang.getValue()%>);
				}else{
					$.ajax({
						type: "POST",
						url:"<%=request.getContextPath()%>/branchRouteControl/gettoNextbackStopPage?branchid="+branchid,
						dataType:"json",
						success : function(data) {
							if(data.nextbranches.length!=0){
								$("#cwbtobranchid").val("");
								$("#cwbtobranchid").val(data.nextbranches);
								//将数组放入全局变量
								cwbtobranchids = $("#cwbtobranchid").val().split(",");
								for(var i = 0 ; i < cwbtobranchids.length ; i++){
									$("#li_"+cwbtobranchids[i]).addClass("click");
								}
								$("#type").val(<%=BranchRouteEnum.JinDaoXiang.getValue()%>);
							}
							
						}
					});
				}
				
			}
		});
	});
	
	$("#branchename").keyup(function(event){
		if($(this).val().length>0){
			$("#BranchList li").hide();
			$("#BranchList li[title*='"+$(this).val()+"']").show();
		}else{
			$("#BranchList li").show();
		}
	});
	$("#clickAllByFilter").click(function(){
		if($("#branchename").val().length>0){
			$("#BranchList li[title*='"+$("#branchename").val()+"']").addClass("click");;
		}else{
			$("#BranchList li").addClass("click");
		}
		saveBranchidsToHideInput();
	});
	$("#notClickAllByFilter").click(function(){
		if($("#branchename").val().length>0){
			$("#BranchList li[title*='"+$("#branchename").val()+"']").removeClass("click");;
		}else{
			$("#BranchList li").removeClass("click");
		}
		saveBranchidsToHideInput();
	});
	
});

function saveBranchidsToHideInput(){
	var li_click = $("#BranchList li[class='click']");
	var li_ids="";
	for(var i=0 ; i < li_click.length ;i++ ){
		li_ids= li_ids+","+$(li_click[i]).attr("branchid");
	}
	li_ids = li_ids.substring(1, li_ids.length);
	$("#cwbtobranchid").val("");
	$("#cwbtobranchid").val(li_ids);
	cwbtobranchids = li_ids.split(",");
}
function clickSubmit(){
	if($("#branchid").val()==-1){
		alert("请选择您要操作的机构!");
		return false;
	}else if($("#type").val()==0){
		alert("流向方向不能为空");
		return false;
	}else{
		return true;
	}
}
</script>
</head>

<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box" >
		<form id="fromBranch"  action="saveNextStop" onsubmit="return clickSubmit();">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="40%">
						
						<select id="branchid" name="branchid">
						<option value="-1" >----请选择----</option>
						<% for(Branch b : branches){ %>
						<option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
						<%} %>
						</select>
						流向方向：
				        <select id="type"  name="type">
							<option value="0" selected>----请选择----</option>
							<%for(BranchRouteEnum br : BranchRouteEnum.values()){ %>
								<option value="<%=br.getValue() %>" ><%=br.getText() %></option>
							<%} %>
						</select>
						<input type="submit" value="保存修改" class="input_button2">
						<input type="hidden" id="cwbtobranchid" name="cwbtobranchid" value="">
						<%=(String)(request.getAttribute("errorState")==null?"　　　　":request.getAttribute("errorState")) %>
						　　　　　　　　　　<input type="text" id="branchename" value="" >
						<input type="button" value="过滤结果全选" id="clickAllByFilter" class="input_button1">
						<input type="button" value="过滤结果取消选择" id="notClickAllByFilter" class="input_button1">
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="jg_35"></div>
	<div class="right_title">
		<div class="station_box">
			<div id="BranchList">
				<ul>
					<% for(Branch b : branches){ %>
					<li id="li_<%=b.getBranchid() %>" branchid="<%=b.getBranchid() %>" onclick="clickNextStopBranch(<%=b.getBranchid() %>)" title="<%=b.getBranchname() %>"><%=b.getBranchname() %></li>
					<%} %>
				</ul>
			</div>
			<% for(Branch b : branches){ %>
			<input type="hidden"   id="<%=b.getBranchid()%>" value="<%=nextbranches.toString() %>" >
			<%} %>
		</div>
	</div>
	
</div>
</body>
</html>
