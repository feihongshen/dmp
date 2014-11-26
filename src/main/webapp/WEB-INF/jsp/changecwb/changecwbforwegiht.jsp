<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<title>订单称重</title>
<script type="text/javascript">
function ispaohuo(){
	if($("#paohuo").attr("checked")=="checked"){
		$("#sizenum").show();
		$("#length").focus();
	}else{
		$("#sizenum").hide();
		$("#scancwb").focus();
	}
}
function resetform(){
	$("#carrealweight").val("");
	$("#length").val("");
	$("#width").val("");
	$("#height").val("");
	$("#scancwb").val("");
	if($("#paohuo").attr("checked")=="checked"){
		$("#length").focus();
	}else{
		$("#scancwb").focus();
	}
}
function setscanways(value){
	$("#scanways").val(value);
	if($("#paohuo").attr("checked")=="checked"){
		$("#length").focus();
	}else{
		$("#scancwb").focus();
	}
	
}
function setsyncupdate(){
	if($("#issyncupdate").attr("checked")=="checked"){
		$("#syncupdate").val("1");
	}else{
		$("#syncupdate").val("0");
	}
	if($("#paohuo").attr("checked")=="checked"){
		$("#length").focus();
	}else{
		$("#scancwb").focus();
	}
	
}
function verifiForm(){
	if($("#carrealweight").val()==""){
		alert("请称重");
		return false;
	}
	if($("#paohuo").attr("checked")=="checked"&&($("#length").val()==""||$("#width").val()==""||$("#height").val()=="")){
		alert("包裹体积填写不完整，长宽高都要填");
		return false;
	}
	return true;
}

$(function(){
	setInterval(carrealweightPrint, "100"); 
	$("#scancwb").focus();
});

function carrealweightPrint(){
	try{
		 var fso = new ActiveXObject("Scripting.FileSystemObject");
		 if(fso.FileExists("C:\\test.txt")){
			 var ts = fso.OpenTextFile("C:\\test.txt",1); //   权限只读(只读=1，只写=2 ，追加=8 等权限)
			   try{
					var s=ts.ReadAll(); 
					if(s!=""){
						$('#carrealweight').val(s);
					}else{
						$('#carrealweight').val('0');
					}
					$('#chengzhong_tishi').hide();
				}catch(e){
					$('#chengzhong_tishi').show();
					$('#carrealweight').val('0');
				}
		       ts.Close();
		 }
	}catch(e){
		$('#chengzhong_tishi').show();
		$('#carrealweight').val('0');
	}

}
</script>
</head>
<body style="background:#eef9ff">
<div class="menucontant">
	<form id="wegihtForm" name="wegihtForm" onSubmit="if(verifiForm()){submitChangeCwbSaveForm(this);}return false;" action="<%=request.getContextPath()%>/changecwb/changecwbforwegiht" method="post" >
		<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
			<tr id="chengzhong_tishi" style="display: none">
				<td><b>如果您的称重无法读取到电子称的数值，请确定您使用的是IE7或者IE8浏览器，并对IE浏览器做如下操作</b><br/>
				<br/>IE----工具---Internet选项----安全----Internet-----自定义级别,Intranet-----自定义级别
  				<br/>ActiveX控件和插件的第二项和第三项：
  				<br/>　　对标记为可安全执行脚本的  ActiveX控件执行脚本设置为“启用”
 				<br/>　　对没有标记为安全的  ActiveX控件进行初始化和脚本运行设置为“启用”
 				<br/><a href="<%=request.getContextPath()%>/images/dianzicheng.rar"><font color="red">下载电子称重程序</font></a>
 				</td>
			</tr>
			<tr>
				<td>实际重量：<input type="text" id="carrealweight" name="carrealweight"><input type="checkbox" id="paohuo" onclick="ispaohuo();">泡货<br/></td>
			</tr>
			<tr id="sizenum" style="display: none;">
				<td>长*宽*高：<input type="text" id="length" name="length">  <input type="text" id="width" name="width" >  <input type="text" id="height" name="height" > cm<br/></td>
			</tr>
			<tr>
				<td>
					扫描方式：<input type="radio" id="radio" name="radio" value="1" onclick="setscanways($(this).val());" checked="checked">按订单号扫描 <input type="radio" id="radio" name="radio" value="2" onclick="setscanways($(this).val());">按运单号扫描<br/>
					<input id="scanways" name="scanways" type="hidden" value="1">
				</td>
			</tr>
			<tr>
				<td>
					扫描单号：<input type="text" id="scancwb" name="scancwb" >
					<input type="checkbox" id="issyncupdate" name="issyncupdate" onclick="setsyncupdate();">是否同步更新订单状态
					<input id="syncupdate" name="syncupdate" type="hidden" value="0">
					<br/>
				</td>
			</tr>
			<tr>
				<td><input type="submit" value="保存"><input type="button" value="清空" onclick="resetform();"></td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>