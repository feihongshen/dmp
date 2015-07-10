<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<title>按泡货规格称重</title>
<script type="text/javascript">
function ispaohuo(){
	if($("#paohuo").attr("checked")=="checked"){
		$("#sizenum").show();
	}else{
		$("#sizenum").hide();
	}
}
function resetform(){
	$("#carrealweight").val("");
	$("#scancwb").val("");
}
function setscanways(value){
	$("#scanways").val(value);
}
function verifiForm(){
	if($("#carrealweight").val()==""){
		alert("请称重");
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
<body style="background:#f5f5f5">
<div class="menucontant">
	<form id="paohuoForm" name="paohuoForm" onSubmit="if(verifiForm()){submitChangeCwbSaveForm(this);}return false;" action="<%=request.getContextPath()%>/changecwb/changecwbforwegihttopaohuo" method="post" >
		<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
			
			<tr id="chengzhong_tishi" style="display: none">
				<td>
					<b>如果您的称重无法读取到电子称的数值，请确定您使用的是IE7或者IE8浏览器，并对IE浏览器做如下操作</b><br/>
					<br/>IE----工具---Internet选项----安全----Internet-----自定义级别,Intranet-----自定义级别
	  				<br/>ActiveX控件和插件的第二项和第三项：
	  				<br/>　　对标记为可安全执行脚本的  ActiveX控件执行脚本设置为“启用”
	 				<br/>　　对没有标记为安全的  ActiveX控件进行初始化和脚本运行设置为“启用”
	 				<br/><a href="<%=request.getContextPath()%>/images/dianzicheng.rar"><font color="red">下载电子称重程序</font></a>
 				</td>
			</tr>
			<tr>
				<td>实际重量：<input type="text" id="carrealweight" name="carrealweight" ><input type="checkbox" id="paohuo" onclick="ispaohuo();" checked="checked">泡货规格<br/></td>
			</tr>
			<tr id="sizenum">
				<td>
				长*宽*高：
					<select id="carsize" name="carsize">
						<option value="1,1,2">1*1*2</option>
						<option value="2,2,4">2*2*4</option>
						<option value="5,2,3">5*2*3</option>
					</select> cm<br/>
				</td>
			</tr>
			<tr>
				<td>
					扫描方式：<input type="radio" id="radio" name="radio" value="1" onclick="setscanways($(this).val());" checked="checked">按订单号扫描 <input type="radio" id="radio" name="radio" value="2" onclick="setscanways($(this).val());" checked="checked">按运单号扫描<br/>
					<input id="scanways" name="scanways" type="hidden" value="1">
				</td>
			</tr>
			<tr>
				<td>扫描单号：<input type="text" id="scancwb" name="scancwb" ><br/></td>
			</tr>
			<tr>
				<td><input type="submit" value="保存"><input type="reset" value="清空" onclick="resetform();"></td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>