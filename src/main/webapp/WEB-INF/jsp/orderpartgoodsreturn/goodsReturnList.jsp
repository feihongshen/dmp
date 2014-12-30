<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.OrderPartGoodsRt"%>
<%@page import="cn.explink.domain.OrderGoods"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

//  List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
  List<User> userList = (List<User>)request.getAttribute("userList");
  Page page_obj = (Page)request.getAttribute("page_obj");
  List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
  Long userid = (Long)request.getAttribute("userid");
  Long customerid = (Long)request.getAttribute("customerid");
  List<OrderPartGoodsRt> orderPartGoodsRtList = (List<OrderPartGoodsRt>)request.getAttribute("orderPartGoodsRtList");
  String nowtime = (String)request.getAttribute("nowtime");
  List<Reason> weituiyuanyinList = (List<Reason>)request.getAttribute("weituiyuanyinList");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript">
$(function(){
	
	$("input[id^='collectiontime']").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
	$("img[id^='sub']").click(function(){
		 var hideTr = $(this).parent().parent().next();       //得到它的下个标签
		 var i = 0;
		 if($(hideTr).attr("id") == "detailgoods"){
	         if (hideTr.is(":hidden")) {        //如果下个标签是隐藏的 
	        	 $(this).attr("src","<%=request.getContextPath()%>/images/sub.png");
	             hideTr.show();                 
	         }else{
	        	 $(this).attr("src","<%=request.getContextPath()%>/images/sum.png");
	        	 hideTr.hide(); 
	         }
		 }else{
			 if(i == 0){
				 i = 1;
				 $(this).attr("src","<%=request.getContextPath()%>/images/sub.png");
			 }else{
				 i = 0;
				 $(this).attr("src","<%=request.getContextPath()%>/images/sum.png");
			 }
		 }
	});
	
	$("input[name='ordercheckbox']").click(function(){
		 var hideTr = $(this).parent().parent().next();       //得到它的下个标签
		 var i = 0;
		 if($(this).attr("checked")){
			 if($(hideTr).attr("id") == "detailgoods"){
		         hideTr.show();                 
			 }
		 }else{
			 if($(hideTr).attr("id") == "detailgoods"){
		         hideTr.hide(); 
			 }
		 }
	});
	
	if($("tr[id^='order']").length > 0){
		$("#rb").attr("disabled", false);
	}else{
		$("#rb").attr("disabled", "disabled");
	}
	
	
	
	$("input[name='tepituicount']").blur(function(){
		var tepituicount = $(this).val()*1;
		if($.trim($(this).val()) == ""){
			tepituicount = 0;
			$(this).val(0);
		}
		var goods_num = $(this).parent().parent().find("td[name='goods_num']").html()*1;
		var shituicount = $(this).parent().parent().find("td[name='shituicount']").children().val()*1;
		var Expression=/^[0-9]+(\d*$)/;  
		var objExp=new RegExp(Expression);		
		if(!objExp.test(tepituicount) ||  tepituicount < 0 || tepituicount > shituicount){
			$(".addresstishi_box").html("请输入大于0且小于等于实退数量的整数");
			$(".addresstishi_box").show();
			setTimeout("$(\".addresstishi_box\").hide(1000)", 2000);
			$(this).focus();
			return;
		}
	});
	
	$("input[name='weituicount']").blur(function(){
		var weituicount = $(this).val()*1;
		if($.trim($(this).val()) == ""){
			weituicount = 0;
			$(this).val(0);
		}
		var goods_num = $(this).parent().parent().find("td[name='goods_num']").html()*1;
		var Expression=/^[0-9]+(\d*$)/;  
		var objExp=new RegExp(Expression);	
		if(!objExp.test(weituicount) ||  weituicount < 0 || weituicount > goods_num){
			$(".addresstishi_box").html("请输入大于0且小于等于应退数量的整数");
			$(".addresstishi_box").show();
			setTimeout("$(\".addresstishi_box\").hide(1000)", 2000);
			$(this).focus();
			return;
		}
		if(weituicount > 0){
			$(this).parent().parent().find("td[name='weituireason']").children().attr("disabled",false);
		}else{
			$(this).parent().parent().find("td[name='weituireason']").children().attr("disabled",true);
		}
		$(this).parent().parent().find("td[name='shituicount']").children().val(goods_num-weituicount);
	});
	
	$("input[name='shituicount']").blur(function(){
		var shituicount = $(this).val()*1;
		if($.trim($(this).val()) == ""){
			shituicount = 0;
			$(this).val(0);
		}
		var goods_num = $(this).parent().parent().find("td[name='goods_num']").html()*1;
		var Expression=/^[0-9]+(\d*$)/;  
		var objExp=new RegExp(Expression);	
		if(!objExp.test(shituicount) ||  shituicount < 0 || shituicount > goods_num){
			$(".addresstishi_box").html("请输入大于0且小于等于应退数量的整数");
			$(".addresstishi_box").show();
			setTimeout("$(\".addresstishi_box\").hide(1000)", 2000);
			$(this).focus();
			return;
		}
		if(shituicount < goods_num){
			$(this).parent().parent().find("td[name='weituireason']").children().attr("disabled",false);
		}else{
			$(this).parent().parent().find("td[name='weituireason']").children().attr("disabled",true);
		}
		$(this).parent().parent().find("td[name='weituicount']").children().val(goods_num-shituicount);
	});
	
	
})

function  search(){
	$("#searchForm").submit();
}

function searchcwbs(){
	$("#searchFormcwbs").submit();
}

function getnowtime(){
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	var hour = date.getHours();
	var minute = date.getMinutes();
	var second = date.getSeconds();
	
	return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
}

function TableToJson(tableid) {
    var txt = "[";
    var row = $("tr[id^='order']"); 
    row.each(function(i,n) {
	    if($(n).find("td[name='checkbox']").find("input[name='ordercheckbox']").attr("checked")){
	        var objorder;
	    	var r = "{";
	        var txtsub = "[";
	        $(n).find("td").each(function(j,m){
	        	var key = $(m).attr("name"); 
	        	if(key == "checkbox"){
	        		return;
	        	}
	        	var value = $(m).html();
	        	if(key == "cwb"){
	        		value = $(m).children("span").text();        		
	        	}
	        	if(key == "collectiontime"){
	        		value = $(m).children().children().val();
	        	}
	        	r +=( "\"" + key + "\"\:\"" + value + "\","); 
	        });
	        var subgoodstr = $(n).next().children().children().find("tr[id^='goods']");
	        if(subgoodstr.length>0){
	        	r += "\""+"ordergoodsList"+"\""+":"+txtsub;
	        }
	        
	        subgoodstr.each(function(k,o){        	
	        	var subr = "{";
	        	$(o).find("td").each(function(l,p){
	            	var keysub = $(p).attr("name");        	
	            	var valuesub = $(p).html();
	            	if(keysub == "shituicount"){
	            		valuesub = $(p).children().val();
	            	}
	            	if(keysub == "weituicount"){
	            		valuesub = $(p).children().val();
	            	}
	            	if(keysub == "tepituicount"){
	            		valuesub = $(p).children().val();
	            	}
	            	if(keysub == "weituireason"){
	            		valuesub = $(p).children().val();
	            	}
	            	if(keysub == "remark1"){
	            		valuesub = $(p).children().val();
	            	}
	            	subr +=( "\"" + keysub + "\"\:\"" + valuesub + "\",");
	            });
	        	subr = subr.substring(0, subr.length - 1)
	            subr += "},";
	        	r += subr;        	
	        });
	        if(subgoodstr.length>0){
	        	r = r.substring(0, r.length - 1)
	        	r += "],";
	        }
	        r = r.substring(0, r.length - 1)
	        r += "},";
	        txt += r;
	    }
    });
    txt = txt.substring(0, txt.length - 1);
    txt += "]";
    return txt; 
}

function returnGoods(){
	var checkflag = 0;
	$("input[name='ordercheckbox']").each(function(i){
	    if($(this).attr("checked")){
	      checkflag = 1;
	   }
	});
	if(checkflag == 0){	
		$(".addresstishi_box").html("请选择退货订单");
		$(".addresstishi_box").show();
		setTimeout("$(\".addresstishi_box\").hide(1000)", 2000);
	    return;
	}
	var orderList = TableToJson("table_orders");
	var breakflag = 0;
	$("tr[id^='goods']").each(function(k,o){		
		var goods_num = $(this).find("td[name='goods_num']").html()*1;
		var shituicount = $(this).find("td[name='shituicount']").children().val()*1;
		var weituicount = $(this).find("td[name='weituicount']").children().val()*1;
		if(shituicount + weituicount != goods_num){
			$(o).find("td[name='shituicount']").children().focus();
			breakflag += 1;
			return false;
		}
	});
	if(breakflag != 0){
		$(".addresstishi_box").html("实退数量=商品数量-未退数量");
		$(".addresstishi_box").show();
		setTimeout("$(\".addresstishi_box\").hide(1000)", 2000);
		return;
	}
	$.ajax({
		type: "POST",
		url:'<%=request.getContextPath()%>/orderpartgoodsreturn/ordergoodsreturn',
		data:{orderjson:orderList},
		dataType:"json",
		success : function(data) {
			if(data.errorCode == 0){
				$("input[name='ordercheckbox']").each(function(){
					if($(this).attr('checked')){
						var hideTr = $(this).parent().parent().next(); 
						if($(hideTr).attr("id") == "detailgoods"){
					         hideTr.remove();                 
						}
						$(this).closest('tr').remove();
					}
				})
			}
			$(".addresstishi_box").html(data.error);
			$(".addresstishi_box").show();
			setTimeout("$(\".addresstishi_box\").hide(1000)", 2000);
		}
	});
}

</script>
</head>
<body style="background:#eef9ff;overflow: hidden;" marginwidth="0" marginheight="0">
<div class="addresstishi_box"></div>
<div class="right_box">
	<div style="background:#FFF;overflow:auto;min-height:550px">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="#" class="light">待反馈明细</a></li>
				<li><a href="../ordergoodsbycwbs">按订单号反馈</a></li>
			</ul>
		</div>
		
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search" id="returndetail" >
						<form action="<%=request.getContextPath() %>/orderpartgoodsreturn/ordergoodslist/1" method="post" id="searchForm">
						    小件员：
							 <select name="userid" id="userid" onchange="search()">
					        	<option value ="-1">全部</option>
					        	<%for(User r : userList){ %>
			           				<option value="<%=r.getUserid()%>" <%if(userid == r.getUserid()){%>selected<%}%>><%=r.getRealname() %></option>
			           			<%} %> 
					        </select>&nbsp&nbsp&nbsp&nbsp&nbsp
					        
					               供货商 ：
							 <select name="customerid" id="customerid" onchange="search()">
					        	<option value ="-1">全部</option>
					        	<%for(Customer cus : customerList){ %>
			           				<option value="<%=cus.getCustomerid()%>" <%if(customerid == cus.getCustomerid()){%>selected<%}%>><%=cus.getCustomername() %></option>
			           			<%} %>
					        </select>
					        &nbsp&nbsp&nbsp&nbsp&nbsp
					        
					        <input type="button" value="部分退货" class="input_button2" onclick="returnGoods()" id="rb">
							
							</form>
						</div>
						<%-- <div div class="kfsh_search" id="returncwb" style="display: none">
						   <form action="<%=request.getContextPath() %>/orderpartgoodsreturn/ordergoodslist/3" method="post" id="searchFormcwbs">
								订单号：
								<textarea id="cwb" class="kfsh_text" onblur="if(this.value==''){this.value='查询多个订单用回车隔开'}" onfocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" rows="3" name="cwb"><%=request.getParameter("cwb")==null?"查询多个订单用回车隔开":request.getParameter("cwb")%></textarea>
								
								<input type="button" value="查询" class="input_button2" onclick="searchcwbs()">
								<input type="button" value="部分退货" class="input_button2" onclick="returnGoods()">
							</form>
						</div> --%>
						<div  style="overflow: auto;height:500px">
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="table_orders" style="overflow:auto;">
							<tbody>
								<tr class="font_1" height="30" >
								    <td width="50" align="center" valign="middle" bgcolor="#E7F4E3"></td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">收件人</td>
									<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">收件地址</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">应收金额[元]</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">取件承运商</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">领货时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">上门揽收时间</td>									
									<td width="80" align="center" valign="middle" bgcolor="#E7F4E3">退货仓库地址</td>
								</tr>
								
								<%if(orderPartGoodsRtList!=null&&orderPartGoodsRtList.size()>0)
									for(OrderPartGoodsRt ort : orderPartGoodsRtList){ %>
								<tr id="order<%=ort.getCwb() %>">
								    <td width="50"  align="center" valign="middle" name="checkbox"><input type="checkbox" value="order<%=ort.getCwb() %>" name="ordercheckbox"></td>
									<td width="120" align="center" valign="middle" name="cwb"><span><%=ort.getCwb() %></span><%-- &nbsp&nbsp<img src="<%=request.getContextPath()%>/images/sum.png" id="sub<%=ort.getCwb() %>" > --%></td>
									<td width="100" align="center" valign="middle" name="consigneename"><%=ort.getConsigneename()%></td>
									<td width="120" align="center" valign="middle" name="consigneeaddress"><%=ort.getConsigneeaddress()%></td>
									<td width="100" align="center" valign="middle" name="receivablefee"><%=ort.getReceivablefee()%></td>
									<td width="100" align="center" valign="middle" name="customer"><%=ort.getCustomer()%></td>
									<td width="100" align="center" valign="middle" name="createtime"><%=ort.getCreatetime()%></td>
									<td width="100" align="center" valign="middle" name="collectiontime">
										<span style="height: 25">
										<input type="text" id="collectiontime<%=ort.getCwb() %>" name="collectiontime<%=ort.getCwb() %>" value="<%=ort.getCollectiontime()==null||ort.getCollectiontime().equals("")? nowtime:ort.getCollectiontime()%>">
										</span>
									
									<td width="80"  align="center" valign="middle" name="rtwarehouseaddress"><%=ort.getRtwarehouseaddress()%></td>
								</tr>
								<%if(ort.getOrdergoodsList() != null && ort.getOrdergoodsList().size()>0){ %>
								<tr id="detailgoods"  style="display: none">
								   <td colspan = 9>
									<table width="100%" border="0" cellspacing="1" cellpadding="0"  class="table_2" id="table_goods<%=ort.getCwb()%>">
									    <tr><td width="120" align="center" valign="middle">应退商品编码</td>
											<td width="100" align="center" valign="middle">应退商品名称</td>
											<td width="120" align="center" valign="middle">应退商品规格</td>
											<td width="100" align="center" valign="middle">应退商品数量</td>
											<td width="100" align="center" valign="middle">实退商品数量</td>
											<td width="100" align="center" valign="middle">未退商品数量</td>
											<td width="100" align="center" valign="middle">特批退货数量</td>
											<td width="170" align="center" valign="middle">未退商品原因</td>
											<td width="80" align="center" valign="middle">备注</td></tr>
										<%for(OrderGoods og : ort.getOrdergoodsList()){ %>
										<tr id="goods<%=og.getGoods_code()%>">
										    <td width="120" align="center" valign="middle" name="id" style="display: none"><%=og.getId()%></td>
										    <td width="120" align="center" valign="middle" name="goods_code"><%=og.getGoods_code()%></td>
											<td width="100" align="center" valign="middle" name="goods_name"><%=og.getGoods_name()%></td>
											<td width="120" align="center" valign="middle" name="goods_spec"><%=og.getGoods_spec()%></td>
											<td width="100" align="center" valign="middle" name="goods_num"><%=og.getGoods_num()%></td>
											<td width="100" align="center" valign="middle" name="shituicount"><input type="text" id="shituicount" name="shituicount" value="<%=og.getShituicount()==0?og.getGoods_num():og.getShituicount()%>"></td>
											<td width="100" align="center" valign="middle" name="weituicount"><input type="text" id="weituicount" name="weituicount" value="<%=og.getWeituicount()%>"></td>
											<td width="100" align="center" valign="middle" name="tepituicount"><input type="text" id="tepituicount" name="tepituicount" value="<%=og.getTepituicount()%>"></td>
											<td width="170" align="center" valign="middle" name="weituireason">
											 <select name="reason" id="reason" style="width:130px;" disabled="disabled">
									        	<option value ="-1">请选择</option>
									        	<%for(Reason r : weituiyuanyinList){ %>
							           				<option value="<%=r.getReasonid()%>" <%if(Long.parseLong(og.getWeituireason()==null ||og.getWeituireason().equals("")?"-1":og.getWeituireason()) == r.getReasonid()){%>selected<%}%>><%=r.getReasoncontent() %></option>
							           			<%} %> 
									        </select>
											</td>
											<td width="80" align="center" valign="middle" name="remark1"><input type="text" id="note" name="note" value="<%=og.getRemark1()==null?"":og.getRemark1()%>"></td></tr>
										<%} %>
									</table>
									</td>
								</tr>
								<%}} %>
							</tbody>
						</table>
						</div>
					</div>
				</div>
				<%-- <%if(page_obj.getMaxpage()>1){ %>
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
			<%} %> --%>
		</div>
	</div>
</div>
<script type="text/javascript">

</script>
</body>
</html>

