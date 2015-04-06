<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.dao.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Map usermap = (Map) session.getAttribute("usermap");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>站点扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script>
function gePDAtBox(pageurl,type){
	$.ajax({
		type: "POST",
		url:pageurl,
		dataType:"html",
		success : function(data) {
			$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
			$("#scancwb",parent.document).focus();
			<%-- if(type=="lihuo"){
				scanstockCwb(<%=usermap.get("branchid")%>);
			} --%>
			
			//$("#baleno",parent.document).focus();
		}
	});
}

function showpdaMenu(){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/PDA/getpdaMenu",
		dataType:"html",
		success : function(data) {
			var arr = data.split(",");
			for(var i=0;i<arr.length;i++){
				if(arr[i]!="P07"){
					$("#"+arr[i]).show();
				}
				
				<%-- if(arr[i]=="P05"){
					$.ajax({
						type: "POST",
						url:"<%=request.getContextPath()%>/PDA/getswitchbyparam/fzdh",
						dataType:"json",
						success : function(data) {
							if(data.switchstate!=""&&data.switchstate=="<%=SwitchEnum.DaoHuoFengBao.getInfo()%>"){
								$("#P05_1").show();
							}
						}
					});
				} --%>
				if(arr[i]=="P07"){
					$.ajax({
						type: "POST",
						url:"<%=request.getContextPath()%>/PDA/getswitchbyparam/plfk",
						dataType:"json",
						success : function(data) {
							if(data.switchstate!=""&&data.switchstate=="<%=SwitchEnum.PiLiangFanKui.getInfo()%>"){
								$("#P07").show();
							}
						}
					});
				}
			}
		}
	});
}

</script>
</head>

<body style="background:#f5f5f5" onload="showpdaMenu();">

<div class="right_box">
	<div class="inputselect_box3">
		<span>
			<input style="display: none;" name="P05" id="P05" type="button" value="到货扫描" class="input_button1" onclick="window.location.href='<%=request.getContextPath()%>/PDA/branchimort';"/>
			<%-- <input style="display: none;" name="P05_1" id="P05_1" type="button" value="到货扫描（包）" class="input_button1" onclick="gePDAtBox('<%=request.getContextPath()%>/PDA/branchimportforbale','b_daohuo');"/> --%>
			<input style="display: none;" name="P06" id="P06" type="button" value="领货扫描" class="input_button1" onclick="window.location.href='<%=request.getContextPath()%>/PDA/branchdeliver';"/>
			<input style="display: none;" name="P07" id="P07" type="button" value="投递反馈（批量反馈）" class="input_button1" onclick="gePDAtBox('<%=request.getContextPath()%>/PDA/deliverpod','');"/>
			<%-- <input style="display: none;" name="P15" id="P15" type="button" value="库存盘点" class="input_button1" onclick="gePDAtBox('<%=request.getContextPath()%>/PDA/branchscanstock','');"/> --%>
			<input style="display: none;" name="P09" id="P09" type="button" value="退货出站" class="input_button1" onclick="window.location.href='<%=request.getContextPath()%>/PDA/branchbackexport';"/>
			<input style="display: none;" name="P10" id="P10" type="button" value="中转出站" class="input_button1" onclick="window.location.href='<%=request.getContextPath()%>/PDA/branchchangeexport';"/>
			<input style="display: none;" name="P04" id="P04" type="button" value="理货" class="input_button1" onclick="gePDAtBox('<%=request.getContextPath()%>/PDA/scancwbbranchforzd','lihuo');"/>
		</span>            
	</div>                 
</div>
</body>
</html>


