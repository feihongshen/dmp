
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.CwbOrderTypeBean"%>
<%@page import="cn.explink.domain.CwbOrderType"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
div{ float:left; padding:0px 5px; list-style:none; width: 650px; }
li{ float:left; padding:0px 5px; list-style:none; width: 200px; border:1px;border-color:blue;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript">
</script>
<script type="text/javascript">
	$(function() {
		$("#save").click(function(event){
			if($("#importtypeid").val()<0){
				alert("请选择类型");
				return;
			}
			else if($("#importtype").val().length>50){
				alert("您输入的信息过长！请输入50个汉字以内的信息！");
				return;
			}
			else{
				$("#creForm").submit();
			}
		});
		$("#importtype").blur(function(event){
			importtype1();
		});
	});
	
	function importtype1(){
		
		if($("#importtype").val().length>0){
			$.ajax({
				url:"<%=request.getContextPath()%>/cwbordertype/importtypecheck",
					data : {importtype : $("#importtype").val()},
						success : function(data) {
							if (data == false) {
								alert("备注已存在");
							} else {
								alert("备注可用");
							}
					}
				});
			} 
	}
</script>
</HEAD>

<BODY>
<div>

<form id="creForm" name="createForm" action="create" method="post" enctype="multipart/form-data">
		${errorState}<br/>
		${create }<br>
		类型:<select id="importtypeid" name="importtypeid" >
        	<option value=-1>请选择类型</option>
        	<option value="1">配送</option>
        	<option value="2">上门退货</option>
        	<option value="3">上门换货</option>
        </select><br>	
		 备注:<input type="text" name="importtype" id="importtype" value="${importCwbOrderType.importtype }"><br>
	    <input type="button" id="save" value="保存"/>
	    <a href="list/1">返回</a>
	</form>
  

</div>	
</BODY>
<script type="text/javascript">
	$("#importtypeid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("importtypeid"))%>);
</script>
</HTML>
