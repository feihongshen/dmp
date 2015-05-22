${config_iframe}
<#-- 添加扩展js引用标签-->
<script type="text/javascript">
	var requestUrl = "cgAutoListController.do?datagrid&configId=${config_id}&field=${fileds}${initquery}";
</script>
<table url="cgAutoListController.do?datagrid&configId=${config_id}&field=${fileds}${initquery}"
	 class="eap-container" id="div2" pkCode="id" title="查询列表" toolbar="#eap-container">
</table>
<div class="eap-container" id="eap-container" type="button" style="margin:5px 5px 3px 5px; border:0px;">
	<div class="accordion-inner eap-container" id="div1" style="border:0px;"></div>
	<div class="eap-container" id="btn1" type="button" style="margin:5px 5px 3px 5px; border:0px;"></div>
</div>