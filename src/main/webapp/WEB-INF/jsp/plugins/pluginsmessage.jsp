<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>插件检查</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css?_v=1.0" type="text/css"></link>
</head>
<body>

<div class="right_box">
	 
	<div class="right_title">
	<div><b>电子秤插件状态检查</b></div>
	<table width="40%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	  <tr class="font_1">
		<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">电子秤插件加载</td>
		<td width="80%" align="center" valign="middle" bgcolor="#eef6ff"><div id="firefoxpluginStatus"></div></td>
	  </tr>
	  <tr class="font_1">
		<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">读数记录</td>
		<td width="80%" align="center" valign="middle" bgcolor="#eef6ff"><div id="message"></div></td>
	  </tr>
	  <tr class="font_1">
		<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">当前读数</td>
		<td width="80%" align="center" valign="middle" bgcolor="#eef6ff">原字符串<input type="text" id="weightNumber_Old_String" size="15"><br>转为数字<input type="text" id="weightNumber" size="15"></td>
	  </tr>
	  <tr class="font_1">
		<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">插件下载</td>
		<td width="80%" align="center" valign="middle" bgcolor="#eef6ff"><a href="<%=request.getContextPath()%>/plugins/pluginsdownload.do"><font style="color:blue;">下载</font></a></td>
	  </tr>
	</table>
	<br/><br/>
	<hr>
	<br/><br/>
	<table width="80%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	  <tr class="font_1">
		<td width="" align="left" valign="middle" bgcolor="#eef6ff"><b>安装说明</font></b></td>
	  </tr>
	  <tr class="font_1">
		<td width="" align="left" valign="middle" bgcolor="#eef6ff">
		先下载插件，如下图提示安装即可：<br>
		<img src="<%=request.getContextPath()%>/firefoxplugins/images/firefoxs1.png"></img><br/><br/>
		<img src="<%=request.getContextPath()%>/firefoxplugins/images/firefoxs2.png"></img><br/><br/>
		<img src="<%=request.getContextPath()%>/firefoxplugins/images/firefoxs3.png"></img><br/><br/>
		<img src="<%=request.getContextPath()%>/firefoxplugins/images/firefoxs4.png"></img><br/><br/>
		<br/>
		执行完以上步骤后，按F5刷新落地配系统，会有如图提示：点击允许--长期允许
		<br/>
		<br/>
		<img src="<%=request.getContextPath()%>/firefoxplugins/images/firefoxalert.png"></img><br/><br/>
		<br/>
		<br/>
		然后，打开右上角的控制面板--电子秤插件，查看详细信息。
		<br/>
		<br/>
		</td>
	  </tr>
	</table>
	<br/><br/>
	<hr/><br/><br/>
	<table width="80%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	  <tr class="font_1">
		<td width="" align="left" valign="middle" bgcolor="#eef6ff"><b>如果安装不成功，尝试<font color="blue">手动安装</font></b></td>
	  </tr>
	  <tr class="font_1">
		<td width="" align="left" valign="middle" bgcolor="#eef6ff">
		1.下载插件  <a href="<%=request.getContextPath()%>/plugins/pluginsdownload.do?fileType=zip"><font style="color:blue;">下载</font></a><br>
		2.将下载文件解压缩，得到如图的文件：<br>
		<img src="<%=request.getContextPath()%>/firefoxplugins/images/firefox1.png"></img><br/><br/>
		<img src="<%=request.getContextPath()%>/firefoxplugins/images/firefox2.png"></img><br/><br/>
		<img src="<%=request.getContextPath()%>/firefoxplugins/images/firefox3.png"></img><br/><br/>
		<img src="<%=request.getContextPath()%>/firefoxplugins/images/firefox4.png"></img><br/><br/>
		3.执行完以上步骤后，按F5刷新落地配系统，打开右上角的控制面板--电子秤插件，查看详细信息。
		<br/><br/>
		<br/><br/>
		</td>
	  </tr>
	</table>
	
	</div>
</div>

</body>
<script type="text/javascript">

window.setInterval("getMessage()", 100);

document.getElementById('firefoxpluginStatus').innerText = window.parent.document.getElementById("firefoxpluginStatus").innerHTML;

function getMessage()
{
	document.getElementById("message").innerHTML = window.parent.document.getElementById("plugin_getWeight_message").innerHTML;
	document.getElementById("weightNumber_Old_String").value = window.parent.document.getElementById("weightNumber_Old_String").value;
	document.getElementById("weightNumber").value = window.parent.document.getElementById("weightNumber").value;
}

</script>

