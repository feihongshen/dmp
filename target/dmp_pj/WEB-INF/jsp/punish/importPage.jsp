<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>导入扣罚信息</h1> <!-- onSubmit="return check();" -->
		<form id="punish_cre_Form" name="punish_import_Form"  onsubmit="importData()" action="<%=request.getContextPath()%>/punish/importData" method="post" enctype="multipart/form-data" >
		<div id="box_form">
		</div>
		<input type="file" name="Filedata">
		 <div align="center">
		 <input type="button" value="确认" class="button" />
		 <input type="button" value="取消" class="button" onclick="closeBox()" />
		 </div>
	</form>
	
	</div>
</div>
<div id="box_yy"></div>
<input type="hidden" id="dmpurl" value="<%=request.getContextPath()%>" />
