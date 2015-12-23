<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.enumutil.EditCwbTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>修改订单</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<style type="text/css"> 
#search{ 
text-align: center; 
position:relative; 
} 
.autocomplete{ 
border: 1px solid #9ACCFB; 
background-color: white; 
text-align: left; 
} 
.autocomplete li{ 
list-style-type: none; 
} 
.clickable { 
cursor: default; 
} 
.highlight { 
background-color: #9ACCFB; 
} 
</style> 
<script type="text/javascript">
$(function(){ 
	$("#cwbs").keydown(function(){
		$("#num").html($.trim($(this).val()).split(/\r?\n/).length);
	});
	var $search = $('#search'); 
	var $searchInput = $search.find('#username'); 
	$searchInput.attr('autocomplete','off'); 
	var $autocomplete = $('<div class="autocomplete"></div>').hide().insertAfter('#username'); 
	var clear = function(){ 
		$autocomplete.empty().hide(); 
	}; 
	$searchInput.blur(function(){ 
		setTimeout(clear,500); 
	}); 
	var selectedItem = null; 
	var timeoutid = null; 
	var setSelectedItem = function(item){ 
		selectedItem = item ; 
		if(selectedItem < 0){ 
			selectedItem = $autocomplete.find('li').length - 1; 
		}else if(selectedItem > $autocomplete.find('li').length-1 ) { 
			selectedItem = 0; 
		} 
		$autocomplete.find('li').removeClass('highlight').eq(selectedItem).addClass('highlight'); 
	}; 
	var ajax_request = function(){ 
		$.ajax({ 
		'url':'<%=request.getContextPath() %>/editcwb/getUserForAutoComplete',
		'data':{'username':$searchInput.val()}, 
		'type':'POST', 
		'success':function(data){ 
		 	if(data.length>0) {
		 		if(data.length==1){
					$autocomplete.empty().hide();
					$searchInput.val(data[0].username); 
					$("#userid").val(data[0].userid);
				}else{
					$.each(data, function(index,term) {
						$autocomplete.append("<li class='clickable' id='"+term.userid+"'>"+term.username+"</li>");
						$('.clickable').hover(function(){
							$(this).siblings().removeClass('highlight'); 
							$(this).addClass('highlight'); 
							selectedItem = index; 
						},function(){ 
							$(this).removeClass('highlight'); 
							selectedItem = -1; 
						});
					
						$("#"+term.userid).click(function(){ 
							$searchInput.val(term.username); 
							$("#userid").val(term.userid);
							$autocomplete.empty().hide(); 
						});
					});
				
				//设置下拉列表的位置，然后显示下拉列表 
				var ypos = $searchInput.position().top; 
				var xpos = $searchInput.position().left; 
				$autocomplete.css('width',$searchInput.css('width')); 
				$autocomplete.css({'position':'absolute','left':xpos + "px",'top':ypos +"px"}); 
				setSelectedItem(0); 
				//显示下拉列表 
				$autocomplete.show(); 
				}
			} 
		} 
		}); 
	}; 
	//对输入框进行事件注册 
	$searchInput.keyup(function(event) { 
		//字母数字，退格，空格 
		if(event.keyCode > 40 || event.keyCode == 8 || event.keyCode ==32) { 
			//首先删除下拉列表中的信息 
			$autocomplete.empty().hide(); 
			clearTimeout(timeoutid); 
			timeoutid = setTimeout(ajax_request,100); 
		}else if(event.keyCode == 38){ 
			//上 
			//selectedItem = -1 代表鼠标离开 
			if(selectedItem == -1){ 
				setSelectedItem($autocomplete.find('li').length-1); 
			}else{ 
				//索引减1 
				setSelectedItem(selectedItem - 1); 
			} 
			event.preventDefault(); 
		}else if(event.keyCode == 40) { 
			//下 
			//selectedItem = -1 代表鼠标离开 
			if(selectedItem == -1){ 
				setSelectedItem(0); 
			}else { 
				//索引加1 
				setSelectedItem(selectedItem + 1); 
			} 
			event.preventDefault(); 
		} 
	}).keypress(function(event){
		//enter键 
		if(event.keyCode == 13) { 
			//列表为空或者鼠标离开导致当前没有索引值 
			if($autocomplete.find('li').length == 0 || selectedItem == -1) { 
				return; 
			} 
			$searchInput.val($autocomplete.find('li').eq(selectedItem).text()); 
			$autocomplete.empty().hide(); 
			event.preventDefault(); 
		} 
	}).keydown(function(event){//esc键 
		if(event.keyCode == 27 ) { 
			$autocomplete.empty().hide(); 
			event.preventDefault(); 
		} 
	}); 
	//注册窗口大小改变的事件，重新调整下拉列表的位置 
	$(window).resize(function() { 
		var ypos = $searchInput.position().top; 
		var xpos = $searchInput.position().left; 
		$autocomplete.css('width',$searchInput.css('width')); 
		$autocomplete.css({'position':'absolute','left':xpos + "px",'top':ypos +"px"}); 
	}); 
});
function checkuser(type){
	var username=$("#username").val().replace(/(^\s*)|(\s*$)/g,"");
	if($("#num").text().length>0){
		if(username.length>0){
			 if($("#userid").val()!='0'){ 
				$('#type').val(type);
				$('#searchForm').submit(); 
			}else{
				alert("该申请人不存在，请重新填写申请人");				
			} 
		}else{
			alert("请填写申请人");
		}
	}else{
		alert("订单号不为空");
	}
	
};

</script>
</HEAD>
<BODY style="background:#f5f5f5">

<div class="saomiao_box2"> 
<form id="searchForm" action ="<%=request.getContextPath()%>/editcwb/start" method = "post">
		<div class="kfsh_tabbtn">
			<%-- <ul>
				<li><a href="<%=request.getContextPath()%>/editcwb/start" class="light">支付信息修改申请</a></li>
			</ul> --%>
		</div>
<table>
	<tr>
		<td>订单号：<br/>[多个订单号用回车键隔开]<br/>
 			<textarea rows="25" cols="30" name ="cwbs" id ="cwbs"></textarea>
 			<br/>共<label id="num"></label>单
 		</td>
 		<td><br> 
 			<input type="hidden" id="type" name="type" value="0" />
 			申请人：
 			<div id="search">
			<input type="text" id="username" name="username" class="input_text1" style="height:20px;"/> 
			<input type="hidden" id="userid" name="requestUser" value="0" />
			</div> 
			</br></br></br></br>
 			<%-- <input type="button" class="input_button1" onclick="checkuser(<%=EditCwbTypeEnum.ChongZhiShenHeZhuangTai.getValue() %>)" value="重置审核状态" /> <br/><br/><br/> --%>
 			<input type="button" class="input_button1" onclick="checkuser(<%=EditCwbTypeEnum.XiuGaiJinE.getValue() %>)" value="修改订单金额申请" /> <br/><br/><br/>
 			<input type="button" class="input_button1" onclick="checkuser(<%=EditCwbTypeEnum.XiuGaiZhiFuFangShi.getValue() %>)" value="修改订单支付方式申请" /><br/><br/><br/>
 			<input type="button" class="input_button1" onclick="checkuser(<%=EditCwbTypeEnum.XiuGaiDingDanLeiXing.getValue() %>)" value="修改订单类型申请" /><br/><br/><br/>
 			<input type="button" class="input_button1" onclick="checkuser(<%=EditCwbTypeEnum.XiuGaiKaiDiYunFeiJinE.getValue() %>)" value="修改快递运费金额" />
 		</td>
 	</tr>
</table>
</form>
</div>

</BODY>
</HTML>
