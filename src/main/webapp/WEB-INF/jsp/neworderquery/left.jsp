<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="cn.explink.domain.VO.BaleCwbClassifyVo"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Common"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrder> orderlist = (List<CwbOrder>)request.getAttribute("orderlist");
  List<Bale> baleList = (List<Bale>)request.getAttribute("baleList");
  List<Customer>  customerlist = (List<Customer>)request.getAttribute("customerlist");
  Map<Long,Customer> customerMap = request.getAttribute("customerMap")==null?new HashMap<Long,Customer>():(Map<Long,Customer>)request.getAttribute("customerMap");
  Map<String,String> cwbBaleMap = request.getAttribute("cwbBaleMap")==null?new HashMap<String,String>():(Map<String,String>)request.getAttribute("cwbBaleMap");
  Page page_obj = (Page)request.getAttribute("page_obj");
  String nowtime=request.getAttribute("currentime").toString();
  
  Map<Long,Branch> branchMap = request.getAttribute("branchMap")==null?new HashMap<Long,Branch>():(Map<Long,Branch>)request.getAttribute("branchMap");
  
  String begindate = request.getParameter("begindate")==null?"":request.getParameter("begindate");
  
  String chukubegindate = request.getParameter("chukubegindate")==null?"":request.getParameter("chukubegindate");
  String chukuenddate = request.getParameter("chukuenddate")==null?"":request.getParameter("chukuenddate");
  String cwbs=request.getAttribute("cwbs")==null?"":request.getAttribute("cwbs").toString();
  
  Map<String, List<BaleCwbClassifyVo>> baleCwbClassifyVoListMap = (Map<String, List<BaleCwbClassifyVo>>) request.getAttribute("baleCwbClassifyVoListMap");
  if(baleCwbClassifyVoListMap == null) {
	  baleCwbClassifyVoListMap = new HashMap<String, List<BaleCwbClassifyVo>>();
  }
  
  Map<String, Integer> baleCwbSizeMap = (Map<String, Integer>) request.getAttribute("baleCwbSizeMap");
  if(baleCwbSizeMap == null) {
	  baleCwbSizeMap = new HashMap<String, Integer>();
  }
  
  Map<String, BaleCwbClassifyVo> baleCwbClassifyVoMap = (Map<String, BaleCwbClassifyVo>) request.getAttribute("baleCwbClassifyVoMap");
  if(baleCwbClassifyVoMap == null) {
	  baleCwbClassifyVoMap = new HashMap<String, BaleCwbClassifyVo>();
  }
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单查询</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script>
$(function() {
	$("#strtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	 $("#chukustrtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	}); 
	 $("#chukuendtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	}); 
	 CopyAll();
});

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
	if(days>10){
		return false;
	}        
	return true;
}

function goForm(cwb){
	$("#WORK_AREA_RIGHT",parent.document)[0].contentWindow.gotoForm(cwb);
}
$(document).ready(function() {

	$("table#gd_table2 tr").click(function(){
			$(this).css("backgroundColor","#FFA500");
			$(this).siblings().css("backgroundColor","#ffffff");
		});
	$("#find1").click(function(){
		$("#isshow2").val(0);
		$("#isshow3").val(0);
		$("#isshow4").val(0);
		$("#isshow5").val(0);
	    $("#isshow").val(1);
		$("#showLetfOrRight1").val(1);
	    $("#showLetfOrRight2").val(1);
	    $("#showLetfOrRight3").val(1);
	    $("#showLetfOrRight4").val(1);
	    $("#showLetfOrRight5").val(1);
		$("#cwbs").val($("#cwbsText").val());
	   	$("#searchForm1").submit();
	    
	});
	$("#find2").click(function(){
		if($("#strtime").val()==""){
			alert("请选择开始时间");
			return false;
		}/* else if($("#endtime").val()==""){
			alert("请选择结束时间");
			return false;
		}else if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
			alert("开始时间不能大于结束时间");
			return false;
		}else if(!Days()||($("#strtime").val()=='' &&$("#endtime").val()!='')||($("#strtime").val()!='' &&$("#endtime").val()=='')){
			alert("时间跨度不能大于10天！");
			return false;
		} */else{
			$("#isshow").val(0);
			$("#isshow3").val(0);
			$("#isshow4").val(0);
			$("#isshow5").val(0);
			$("#isshow2").val(1);
			$("#showLetfOrRight1").val(2);
			$("#showLetfOrRight2").val(2);
			$("#showLetfOrRight3").val(2);
			$("#showLetfOrRight4").val(2);
			$("#showLetfOrRight5").val(2);
			$("#searchForm2").submit();
		}
	});
	$("#find3").click(function(){
		 $("#isshow").val(0);
	    $("#isshow2").val(0);
	    $("#isshow4").val(0);
	    $("#isshow5").val(0);
	    $("#isshow3").val(1);
		 $("#showLetfOrRight1").val(3);
		 $("#showLetfOrRight2").val(3);
	    $("#showLetfOrRight3").val(3);
	    $("#showLetfOrRight4").val(3);
	    $("#showLetfOrRight5").val(3);
	    $("#balenoId").val($("#balenoView").val());
	   	$("#searchForm3").submit();
	    
	});
	$("#find4").click(function(){
		 $("#isshow").val(0);
	    $("#isshow2").val(0);
	    $("#isshow3").val(0);
	    $("#isshow5").val(0);
	    $("#isshow4").val(1);
		 $("#showLetfOrRight1").val(4);
		 $("#showLetfOrRight2").val(4);
	    $("#showLetfOrRight3").val(4);
	    $("#showLetfOrRight4").val(4);
	    $("#showLetfOrRight5").val(4);
	    $("#transcwbId").val($("#transcwbView").val());
	   	$("#searchForm4").submit();
	    
	});
	$("#find5").click(function(){
		if($("#chukustrtime").val()==""){
			alert("请选择开始时间");
			return false;
		}
		if($("#chukuendtime").val()==""){
			alert("请选择结束时间");
			return false;
		}
		if($("#chukustrtime").val()>$("#chukuendtime").val() && $("#chukuendtime").val() !=''){
			alert("开始时间不能大于结束时间");
			return false;
		}
		 $("#isshow").val(0);
	    $("#isshow2").val(0);
	    $("#isshow3").val(0);
	    $("#isshow4").val(0);
	    $("#isshow5").val(1);
		 $("#showLetfOrRight1").val(5);
		 $("#showLetfOrRight2").val(5);
	    $("#showLetfOrRight3").val(5);
	    $("#showLetfOrRight4").val(5);
	    $("#showLetfOrRight5").val(5);
	   	$("#searchForm5").submit();
	    
	});
	
	$("#export").click(function(){
		   if(<%=page_obj.getTotal()>1000  %>){
			    if(confirm("温馨提示：您将要导出<%=page_obj.getTotal() %>条订单的订单过程信息，数据量比较大，导出速度会比较慢，您的操作将会被记录，确定继续做导出？")){
					$("#exportForm").submit();
				}
			}else if(<%=page_obj.getTotal()<=1000 %>){
				 if(confirm("这里导出的是订单过程数据，数据量比较大，您的操作将会被记录，确定继续做导出？")){
						$("#exportForm").submit();
				 }
			}
	});
	
	$("#export2").click(function(){
		$("#exportForm2").submit();
	});
	
});
	
function find3Post(){
	$("#isshow").val(0);
    $("#isshow2").val(0);
    $("#isshow4").val(0);
    $("#isshow5").val(0);
    $("#isshow3").val(1);
	$("#showLetfOrRight1").val(3);
	$("#showLetfOrRight2").val(3);
    $("#showLetfOrRight3").val(3);
    $("#showLetfOrRight4").val(3);
    $("#showLetfOrRight5").val(3);
    $("#balenoId").val($("#balenoView").val());
   	$("#searchForm3").submit();
}

function find4Post(){
	$("#isshow").val(0);
    $("#isshow2").val(0);
    $("#isshow3").val(0);
    $("#isshow5").val(0);
    $("#isshow4").val(1);
	$("#showLetfOrRight1").val(4);
	$("#showLetfOrRight2").val(4);
    $("#showLetfOrRight3").val(4);
    $("#showLetfOrRight4").val(4);
    $("#showLetfOrRight5").val(4);
    $("#transcwbId").val($("#transcwbView").val());
   	$("#searchForm4").submit();
}

</script>

<script language="javascript">
$(function(){
	var $menuli = $(".uc_midbg ul li");
	var $menulilink = $(".uc_midbg ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
});

	function copyToClipBoard(){
       /*  var clipBoardContent="";
        clipBoardContent+=$("#cwbsText").val();
        if(window.clipboardData){
                window.clipboardData.clearData();
                window.clipboardData.setData("Text", clipBoardContent);
        }else if(navigator.userAgent.indexOf("Opera") != -1){
                window.location = clipBoardContent;
        }else if (window.netscape){
                try{
                        netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
                }catch (e){
                        alert("您的当前浏览器设置已关闭此功能！请按以下步骤开启此功能！\n新开一个浏览器，在浏览器地址栏输入'about:config'并回车。\n然后找到'signed.applets.codebase_principal_support'项，双击后设置为'true'。\n声明：本功能不会危极您计算机或数据的安全！");
                }
                var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
                if (!clip) return;
                var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
                if (!trans) return;
                trans.addDataFlavor('text/unicode');
                var str = new Object();
                var len = new Object();
                var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
                var copytext = clipBoardContent;
                str.data = copytext;
                trans.setTransferData("text/unicode",str,copytext.length*2);
                var clipid = Components.interfaces.nsIClipboard;
                if (!clip) return false;
                clip.setData(trans,null,clipid.kGlobalClipboard);
        }
        alert("已成功复制！");
        return true; */
      /*   var c=$("#cwbsText").val();
        c.createRange().execCommand("Copy") ; */
        
        
}
	function CopyAll()
	{//onmouseover="CopyAll();"
		var c=$("#cwbsText");
				c.focus(); //得到文本框焦点
				c.select(); //全选文本框
	}


</script>
</head>

<body onLoad="$('#orderSearch').focus();" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="1" cellpadding="5" class="table_2">
	<tr>
		<td align="left" valign="top">
		<div class="uc_midbg">
				<ul>
					<li><a href="#" <%if(request.getAttribute("showLetfOrRight") == null || request.getAttribute("showLetfOrRight").toString().equals("1")){ %>class="light" <%} %>>按订单查询</a></li>
					<li><a href="#" <%if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("2")){ %>class="light" <%} %>>按条件查询</a></li>
					<li><a href="#" <%if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("3")){ %>class="light" <%} %>>按包查询</a></li>
					<li><a href="#" <%if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("5")){ %>class="light" <%} %>>按包时间查询</a></li>
					<%if(request.getAttribute("isAmazonOpen") != null && "1".equals(request.getAttribute("isAmazonOpen").toString()) ){ %>
					<li><a href="#" <%if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("4")){ %>class="light" <%} %>>按运单号查询</a></li>
					<%} %>
				</ul>
			</div>
			<div class="tabbox">
				<li <%if(request.getAttribute("showLetfOrRight") != null && (!request.getAttribute("showLetfOrRight").toString().equals("1"))){ %>style="display:none"<%} %>>
					<div style="padding:5px; border:1px solid #8EC5E6">
					<form action="<%=request.getContextPath()%>/order/left/1" method="post" id="searchForm1">
						<textarea  cols="20" rows="2" id="cwbsText" ><%=cwbs %></textarea>
						<input type="hidden" id="cwbs" name="cwbs" value="<%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs") %>" />
						<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
						<input type="hidden" id="showLetfOrRight1" name="showLetfOrRight" value="<%=request.getParameter("showLetfOrRight")==null?"1":request.getParameter("showLetfOrRight") %>" />
						<input type="button" name="button" id="find1" value="查询" class="input_button2"/>
						</form>
					<form action="<%=request.getContextPath()%>/order/exportExcle" method="post" id="exportForm">
						<input type="hidden"  name="cwbs" value="<%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs") %>" />
						<input type="hidden"  name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
						<input type="hidden"  name="showLetfOrRight" value="<%=request.getParameter("showLetfOrRight")==null?"1":request.getParameter("showLetfOrRight") %>" />
						<input type="hidden"  name="begindate" value="<%=begindate %>" />
						<input type="hidden"  name="customerid" value="<%=request.getParameter("customerid")==null?"0":request.getParameter("customerid") %>" />
						<input type="hidden"  name="consigneename" value="<%=request.getParameter("consigneename")==null?"":request.getParameter("consigneename") %>" />
						<input type="hidden"  name="consigneemobile" value="<%=request.getParameter("consigneemobile")==null?"":request.getParameter("consigneemobile") %>" />
						<input type="hidden"  name="consigneeaddress" value="<%=request.getParameter("consigneeaddress")==null?"":request.getParameter("consigneeaddress") %>" />
						<input type="hidden"  name="baleno" value="<%=request.getParameter("baleno")==null?"":request.getParameter("baleno") %>" />
						<input type="hidden"  name="transcwb" value="<%=request.getParameter("transcwb")==null?"":request.getParameter("transcwb") %>" />
					</form>
					<form action="<%=request.getContextPath()%>/bale/exportExcelBale" method="post" id="exportForm2">
						<input type="hidden"  name="chukubegindate" value="<%=chukubegindate %>" />
						<input type="hidden"  name="chukuenddate" value="<%=chukuenddate %>" />
					</form>
					
					</div>
				</li>
				<li <%if(!(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("2"))){ %>style="display:none" <%} %>>
					<div style="padding:5px; border:1px solid #8EC5E6">
					<form action="<%=request.getContextPath()%>/order/left/1" method="post" id="searchForm2">
						
						发货时间：
						<input type ="text" name ="begindate" id="strtime"  value="<%=nowtime %>"/>(默认查询该时间为起点的10天之内的数据) 
						<br/>
						客户：
						<select name ="customerid" id ="customerid">
				          <option value ="0">请选择客户</option>
				          <%for(Customer c : customerlist){ %>
				           <option value =<%=c.getCustomerid() %> 
				           <%if(c.getCustomerid() == Integer.parseInt(request.getParameter("customerid")==null?"0":request.getParameter("customerid"))  ){ %>selected="selected" <%} %> ><%=c.getCustomername() %></option>
				          <%} %>
				        </select>
						收件人：
						<input name="consigneename" type="text" size="10" value="<%=request.getParameter("consigneename")==null?"":request.getParameter("consigneename") %>"/>
						电话：
						<input name="consigneemobile" type="text" size="10" value="<%=request.getParameter("consigneemobile")==null?"":request.getParameter("consigneemobile") %>"/>
						<br/>
						地址：
						<input name="consigneeaddress" type="text" style="width:210px"  value="<%=request.getParameter("consigneeaddress")==null?"":request.getParameter("consigneeaddress") %>"/>
						<input type="hidden" id="isshow2" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
						<input type="hidden" id="showLetfOrRight2" name="showLetfOrRight" value="<%=request.getParameter("showLetfOrRight")==null?"1":request.getParameter("showLetfOrRight") %>" />
						<input type="hidden" id="ischangetime" name="ischangetime" value="1"/>
						<input type="button" name="button" id="find2" value="查询" class="input_button2"/>
						</form>
					</div>
				</li>
				<li <%if(!(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("3"))){ %>style="display:none"<%} %>>
					<div style="padding:5px; border:1px solid #8EC5E6">
					<form action="<%=request.getContextPath()%>/order/left/1" method="post" id="searchForm3">
						包号：<input id="balenoView"  type="text" size="20" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){find3Post();}'/>
						<input type="hidden" id="balenoId" name="baleno" value="<%=request.getParameter("baleno")==null?"":request.getParameter("baleno") %>" />
						<input type="hidden" id="isshow3" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
						<input type="hidden" id="showLetfOrRight3" name="showLetfOrRight" value="<%=request.getParameter("showLetfOrRight")==null?"1":request.getParameter("showLetfOrRight") %>" />
						<input type="button" name="button" id="find3" value="查询" class="input_button2"/>
						</form>
					</div>
				</li>
				<li <%if(!(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("5"))){ %>style="display:none"<%} %>>
					<div style="padding:5px; border:1px solid #8EC5E6">
					<form action="<%=request.getContextPath()%>/bale/left/1" method="post" id="searchForm5">
						出库时间：<input type ="text" name ="chukubegindate" id="chukustrtime"  value="<%=chukubegindate %>"/>
						到<input type ="text" name ="chukuenddate" id="chukuendtime"  value="<%=chukuenddate %>"/>
						<input type="hidden" id="isshow5" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
						<input type="hidden" id="showLetfOrRight5" name="showLetfOrRight" value="<%=request.getParameter("showLetfOrRight")==null?"1":request.getParameter("showLetfOrRight") %>" />
						<input type="button" name="button" id="find5" value="查询" class="input_button2"/>
						</form>
					</div>
				</li>
				<li <%if(!(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("4"))){ %>style="display:none"<%} %>>
					<div style="padding:5px; border:1px solid #8EC5E6">
					<form action="<%=request.getContextPath()%>/order/left/1" method="post" id="searchForm4">
						运单号：<input id="transcwbView"  type="text" size="20" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){find4Post();}'/>
						<input type="hidden" id="transcwbId" name="transcwb" value="<%=request.getParameter("transcwb")==null?"":request.getParameter("transcwb") %>" />
						<input type="hidden" id="isshow4" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
						<input type="hidden" id="showLetfOrRight4" name="showLetfOrRight" value="<%=request.getParameter("showLetfOrRight")==null?"1":request.getParameter("showLetfOrRight") %>" />
						<input type="button" name="button" id="find4" value="查询" class="input_button2"/>
						</form>
					</div>
				</li>
				
			</div>
			<div style="height:310px; overflow-y:scroll">
					<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" >
						<tbody>
						<%if("5".equals(request.getParameter("showLetfOrRight"))){ %>
							<tr>
								<td width="80" align="center" bgcolor="#F1F1F1">包号</td>
								<td width="140" align="center" bgcolor="#F1F1F1">出库时间</td>
								<td width="120" align="center" bgcolor="#F1F1F1">出库库房</td>
								<td width="120" align="center" bgcolor="#F1F1F1">下一站</td>
								<td width="60" align="center" bgcolor="#F1F1F1">单数</td>
								<td align="center" bgcolor="#F1F1F1">包状态</td>
							</tr>
							<%if(baleList != null && baleList.size()>0){ %>
							<%for(Bale bale : baleList){ %>
							<tr>
								<td width="80" align="center"><%=bale.getBaleno() %></td>
								<td width="140" align="center"><%=bale.getCretime()%></td>
								<td width="120" align="center"><%=branchMap.get(bale.getBranchid())==null?"":branchMap.get(bale.getBranchid()).getBranchname() %></td>
								<td width="120" align="center"><%=branchMap.get(bale.getNextbranchid())==null?"":branchMap.get(bale.getNextbranchid()).getBranchname() %></td>
								<td width="60" align="center"><%if(bale.getCwbcount()>0){%><a  target="_blank" href="<%=request.getContextPath()%>/bale/show/<%=bale.getId()%>/1"><%=bale.getCwbcount() %></a><%}else{ %>0<%} %></td>
								<td align="center"><%=BaleStateEnum.getValue(bale.getBalestate())==null?"" :BaleStateEnum.getValue(bale.getBalestate()).getText() %></td>
								</tr>
							<%}} %>
						<%}else{ %>
						
						<%if(request.getAttribute("isAmazonOpen") != null && "1".equals(request.getAttribute("isAmazonOpen").toString()) ){ %>
							  <tr>
								<td width="80" align="center" bgcolor="#F1F1F1">订&nbsp;单&nbsp;号</td>
								<td width="80" align="center" bgcolor="#F1F1F1">运单号</td>
								<td width="80" align="center" bgcolor="#F1F1F1">订单类型</td>
								<td width="120" align="center" bgcolor="#F1F1F1">发货时间</td>
								<td width="80" align="center" bgcolor="#F1F1F1">收件人</td>
								<td width="80" align="center" bgcolor="#F1F1F1">电话</td>
								<td width="80" align="center" bgcolor="#F1F1F1" title="订单当前状态">状态</td>
								</tr>
						<%} else if(request.getAttribute("showLetfOrRight") == null || request.getAttribute("showLetfOrRight").toString().equals("1")) {%>
							<tr>
								<td width="90" align="center" bgcolor="#F1F1F1">订单号</td>
								<td width="80" align="center" bgcolor="#F1F1F1">运单号</td>
								<td width="80" align="center" bgcolor="#F1F1F1">包号</td>
								<td width="80" align="center" bgcolor="#F1F1F1">客户</td>
								<td width="120" align="center" bgcolor="#F1F1F1">发货时间</td>
								<td width="80" align="center" bgcolor="#F1F1F1" title="订单当前状态">状态</td>
								<td width="80" align="center" bgcolor="#F1F1F1">关联单号</td>
							</tr>
						<%} else if(request.getAttribute("showLetfOrRight") == null || request.getAttribute("showLetfOrRight").toString().equals("3")) {%>
							<tr>
								<td width="80" align="center" bgcolor="#F1F1F1">包号</td>
								<td width="90" align="center" bgcolor="#F1F1F1">订单号</td>
								<td width="80" align="center" bgcolor="#F1F1F1">运单号</td>
								<td width="80" align="center" bgcolor="#F1F1F1">客户</td>
								<td width="120" align="center" bgcolor="#F1F1F1">发货时间</td>
								<td width="80" align="center" bgcolor="#F1F1F1" title="订单当前状态">状态</td>
							</tr>
						<%}else{ %>
						<tr>
								<td width="90" align="center" bgcolor="#F1F1F1">订单号</td>
								<td width="80" align="center" bgcolor="#F1F1F1">包号</td>
								<td width="80" align="center" bgcolor="#F1F1F1">客户</td>
								<td width="120" align="center" bgcolor="#F1F1F1">发货时间</td>
								<td width="80" align="center" bgcolor="#F1F1F1">收件人</td>
								<td width="80" align="center" bgcolor="#F1F1F1">电话</td>
								<td width="80" align="center" bgcolor="#F1F1F1" title="订单当前状态">状态</td>
								</tr>
						<%} %>
					<!-- 		</tbody>
						</table>
						<div style="height:310px; overflow-y:scroll">
						<table width="97%" border="0" cellspacing="0" cellpadding="2" class="table_5" id="gd_table2" >
						<tbody> -->
						<%if(orderlist != null && orderlist.size()>0){ %>
						<%for(CwbOrder order : orderlist){ %>
							<%if(request.getAttribute("isAmazonOpen") != null && "1".equals(request.getAttribute("isAmazonOpen").toString()) ){ %>
							<tr onclick="goForm('<%=order.getCwb() %>');" >
								<td width="90" align="center"><%=order.getCwb() %></td>
								<td width="80" align="center"><%=order.getTranscwb()%></td>
								<td width="80" align="center"><%=CwbOrderTypeIdEnum.getByValue(order.getCwbordertypeid()).getText() %></td>
								<td width="120" align="center"><%=order.getEmaildate() %></td>
								<td width="80" align="center"><%=order.getConsigneenameOfkf() %></td>
								<td width="80" align="center"><%=order.getConsigneemobileOfkf() %></td>
								<td align="center">
									<%if(CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText()=="已审核"){%>
										审核为：<%= DeliveryStateEnum.getByValue(order.getDeliverystate()).getText() %><%}
									else if(CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText()=="已反馈") {%>
										反馈为：<%= DeliveryStateEnum.getByValue(order.getDeliverystate()).getText() %><%}
									else{ %>
										<%=CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText() %>
									<%} %>
								</td>
								<td width="80" align="center"><%=order.getExchangecwb()==null?"":order.getExchangecwb() %></td>
							</tr>
							<%} else if(request.getAttribute("showLetfOrRight") == null || request.getAttribute("showLetfOrRight").toString().equals("1")) {
								int size = baleCwbSizeMap.get(order.getCwb());
								List<BaleCwbClassifyVo> voList = baleCwbClassifyVoListMap.get(order.getCwb());
								for(int i = 0; i < voList.size(); i++) {
									BaleCwbClassifyVo vo = voList.get(i);
									for(int j = 0; j < vo.getTranscwbList().size();j++) {
										String transcwb = vo.getTranscwbList().get(j);
										if(i == 0 && j == 0) {
									%>
											<tr onclick="goForm('<%=order.getCwb() %>');" >
												<td width="80" rowspan="<%=size %>" align="center" valign="middle"><%=order.getCwb() %></td>
												<td width="80" rowspan="1" align="center" valign="middle"><%=transcwb%></td>
												<td width="80" rowspan="<%=vo.getTranscwbList().size() %>" align="center" valign="middle"><%=vo.getBaleno()%></td>
												<td width="80" rowspan="<%=size %>" align="center" valign="middle"><%=customerMap.get(order.getCustomerid())==null?"":customerMap.get(order.getCustomerid()).getCustomername() %></td>
												<td width="120" rowspan="<%=size %>" align="center" valign="middle"><%=order.getEmaildate() %></td>
												<td rowspan="<%=size %>" align="center" valign="middle">
													<%if(CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText()=="已审核"){%>
														审核为：<%= DeliveryStateEnum.getByValue(order.getDeliverystate()).getText() %><%}
													else if(CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText()=="已反馈") {%>
														反馈为：<%= DeliveryStateEnum.getByValue(order.getDeliverystate()).getText() %><%}
													else{ %>
														<%=CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText() %>
													<%} %>
												</td>
												<td width="80" align="center"><%=order.getExchangecwb()==null?"":order.getExchangecwb() %></td>
												</tr>
										<%	} else if(j == 0) { %>
												<tr onclick="goForm('<%=order.getCwb() %>');" >
													<td width="80" rowspan="1" align="center" valign="middle"><%=transcwb%></td>
													<td width="80" rowspan="<%=vo.getTranscwbList().size() %>" align="center" valign="middle"><%=vo.getBaleno()%></td>
												</tr>
										<%	} else { %>
											<tr onclick="goForm('<%=order.getCwb() %>');" >
												<td width="80" rowspan="1" align="center" valign="middle"><%=transcwb%></td>
											</tr>
									<% } 
									} 
								}%>
							<%} else if(request.getAttribute("showLetfOrRight") == null || request.getAttribute("showLetfOrRight").toString().equals("3")) {
								BaleCwbClassifyVo vo = baleCwbClassifyVoMap.get(order.getCwb());
								for(int i = 0; i < vo.getTranscwbList().size(); i++) {
									int size = vo.getTranscwbList().size();
									if(i==0) {
							%>
									<tr onclick="goForm('<%=order.getCwb() %>');" >
										<td width="80" rowspan="<%=size %>" align="center" valign="middle"><%=vo.getBaleno()%></td>
										<td width="80" rowspan="<%=size %>" align="center" valign="middle"><%=order.getCwb() %></td>
										<td width="80" rowspan="1" align="center" valign="middle"><%=vo.getTranscwbList().get(i) %></td>	
										<td width="80" rowspan="<%=size %>" align="center" valign="middle"><%=customerMap.get(order.getCustomerid())==null?"":customerMap.get(order.getCustomerid()).getCustomername() %></td>
										<td width="120" rowspan="<%=size %>" align="center" valign="middle"><%=order.getEmaildate() %></td>
										<td rowspan="<%=size %>" align="center" valign="middle">
											<%if(CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText()=="已审核"){%>
												审核为：<%= DeliveryStateEnum.getByValue(order.getDeliverystate()).getText() %><%}
											else if(CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText()=="已反馈") {%>
												反馈为：<%= DeliveryStateEnum.getByValue(order.getDeliverystate()).getText() %><%}
											else{ %>
												<%=CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText() %>
											<%} %>
										</td>
										<td width="80" align="center"><%=order.getExchangecwb()==null?"":order.getExchangecwb() %></td>
									</tr>
							<% 	   } else { %>
									<tr onclick="goForm('<%=order.getCwb() %>');" >
										<td width="80" rowspan="1" align="center" valign="middle"><%=vo.getTranscwbList().get(i) %></td>	
									</tr>
							<%
								}
							   }%>
							<% }else{ %>
								<tr onclick="goForm('<%=order.getCwb() %>');" >
									<td width="80" align="center"><%=order.getCwb() %></td>
									<td width="80" align="center"><%=order.getPackagecode()%></td>
									<td width="80" align="center"><%=customerMap.get(order.getCustomerid())==null?"":customerMap.get(order.getCustomerid()).getCustomername() %></td>
									<td width="120" align="center"><%=order.getEmaildate() %></td>
									<td width="80" align="center"><%=order.getConsigneenameOfkf() %></td>
									<td width="80" align="center"><%=order.getConsigneemobileOfkf() %></td>
									<td align="center">
										<%if(CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText()=="已审核"){%>
											审核为：<%= DeliveryStateEnum.getByValue(order.getDeliverystate()).getText() %><%}
										else if(CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText()=="已反馈") {%>
											反馈为：<%= DeliveryStateEnum.getByValue(order.getDeliverystate()).getText() %><%}
										else{ %>
											<%=CwbFlowOrderTypeEnum.getText(order.getFlowordertype()).getText() %>
										<%} %>
									</td>
									<td width="80" align="center"><%=order.getExchangecwb()==null?"":order.getExchangecwb() %></td>
								</tr>
							<%} %>
						<%}} %>	
						<%} %>
							</tbody>
					</table></div>
						
					<div class="jg_10"></div>
					<table width="100%"  border="0" cellspacing="1" cellpadding="5" class="table_5" >
				<tr>
					<td align="center" >
					<%if("5".equals(request.getParameter("showLetfOrRight"))){ %>
						<%if(page_obj.getMaxpage()>1){ %>
							<a 
							href="javascript:$('#searchForm5').attr('action','<%=request.getContextPath()%>/bale/left/1');$('#searchForm5').submit();" 
							>第一页</a>　
							<a 
							href="javascript:$('#searchForm5').attr('action','<%=request.getContextPath()%>/bale/left/<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm5').submit();" 
							>上一页</a>　
							<a 
							href="javascript:$('#searchForm5').attr('action','<%=request.getContextPath()%>/bale/left/<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm5').submit();" 
							>下一页</a>　
							<a 
							href="javascript:$('#searchForm5').attr('action','<%=request.getContextPath()%>/bale/left/<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm5').submit();" 
							 >最后一页</a>
							　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
									id="selectPg"
									onchange="$('#searchForm5').attr('action','<%=request.getContextPath()%>/bale/left/'+$(this).val());$('#searchForm5').submit()" 
									>
									<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
									<option value="<%=i %>"><%=i %></option>
									<% } %>
								</select>页
					        <%} %>
					<%}else{ %>
						<%if(page_obj.getMaxpage()>1){ %>
						<a 
						<%if(request.getAttribute("showLetfOrRight") == null || request.getAttribute("showLetfOrRight").toString().equals("1")){ %>
						href="javascript:$('#searchForm1').attr('action','<%=request.getContextPath()%>/order/left/1');$('#searchForm1').submit();" 
						 <%} else if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("2")){%>
						href="javascript:$('#searchForm2').attr('action','<%=request.getContextPath()%>/order/left/1');$('#searchForm2').submit();" 
						<%}else if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("4")){%>
						href="javascript:$('#searchForm4').attr('action','<%=request.getContextPath()%>/order/left/1');$('#searchForm4').submit();" 
						<%} else{%>
						href="javascript:$('#searchForm3').attr('action','<%=request.getContextPath()%>/order/left/1');$('#searchForm3').submit();" 
						<%} %>
						>第一页</a>　
						<a 
						<%if(request.getAttribute("showLetfOrRight") == null || request.getAttribute("showLetfOrRight").toString().equals("1")){ %>
						href="javascript:$('#searchForm1').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm1').submit();" 
						 <%} else if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("2")) {%>
						href="javascript:$('#searchForm2').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm2').submit();"
						<%} else if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("4")) {%>
						href="javascript:$('#searchForm4').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm4').submit();"
						<%}else{%>	
						href="javascript:$('#searchForm3').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm3').submit();"
								<%} %>
						>上一页</a>　
						<a 
						<%if(request.getAttribute("showLetfOrRight") == null || request.getAttribute("showLetfOrRight").toString().equals("1")){ %>
						href="javascript:$('#searchForm1').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm1').submit();" 
						 <%} else if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("2")){%>
						href="javascript:$('#searchForm2').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm2').submit();" 
						<%} else if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("4")){%>
						href="javascript:$('#searchForm4').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm4').submit();" 
						<%}else{%>	
						href="javascript:$('#searchForm3').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm3').submit();" 
								<%} %>
						>下一页</a>　
						<a 
						<%if(request.getAttribute("showLetfOrRight") == null || request.getAttribute("showLetfOrRight").toString().equals("1")){ %>
						href="javascript:$('#searchForm1').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm1').submit();" 
						 <%} else if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("2")){%>
						href="javascript:$('#searchForm2').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm2').submit();" 
						<%} else if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("4")){%>
						href="javascript:$('#searchForm4').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm4').submit();" 
						<%} else{%>	
						href="javascript:$('#searchForm3').attr('action','<%=request.getContextPath()%>/order/left/<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm3').submit();" 
								<%} %>
						>最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								<%if(request.getAttribute("showLetfOrRight") == null || request.getAttribute("showLetfOrRight").toString().equals("1")){ %>
								onchange="$('#searchForm1').attr('action','<%=request.getContextPath()%>/order/left/'+$(this).val());$('#searchForm1').submit()" 
								 <%} else if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("2")){%>
								onchange="$('#searchForm2').attr('action','<%=request.getContextPath()%>/order/left/'+$(this).val());$('#searchForm2').submit()" 
								<%} else if(request.getAttribute("showLetfOrRight") != null && request.getAttribute("showLetfOrRight").toString().equals("4")){%>
								onchange="$('#searchForm4').attr('action','<%=request.getContextPath()%>/order/left/'+$(this).val());$('#searchForm4').submit()" 
								<%} else{%>	
								onchange="$('#searchForm3').attr('action','<%=request.getContextPath()%>/order/left/'+$(this).val());$('#searchForm3').submit()" 
								
								<%} %>
								>
								<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>"><%=i %></option>
								<% } %>
							</select>页
				        <%} %>
			        <%} %>
					</td>
					<%if("5".equals(request.getParameter("showLetfOrRight"))){ %>
					<td >
					<input type="button" name="button4" id="export2" value="导出" class="input_button2"></td>
					<%}else{ %>
					<td >
					<input type="button" name="button4" id="export" value="导出" class="input_button2"></td>
					<%} %>
				</tr>
		      </table></td>
			</tr>
			</table>
			<script language="javascript">
			$("#selectPg").val(<%=request.getAttribute("page") %>);
			</script>
</body></html>

