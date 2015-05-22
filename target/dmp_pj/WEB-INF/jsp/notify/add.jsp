<%@ page language="java" contentType="text/html; charset=utf-8"    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>发布信息</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
  

    <style type="text/css">
        .clear {
            clear: both;
        }
    </style>
    <script type="text/javascript">
    function forsub(){
		
		  if($("#title").val().length==0){
			  alert("标题不为空");
			  return false;
		  }
		  if(!$("input[name='type']:checked").length==1){ 
				alert("请选择类型");
				return false;
		  }
		  if($("#editor").val().length==0){
			  alert("内容不为空");
			  return false;
		  }
		  
		  $.ajax({
			type:"post",
			url:$("#form1").attr("action"),
			dataType:"json",
			data:$("#form1").serialize(),
			success:function(data){
				$("#info").html(data.error);
				$("#info").show();
				$("#info").fadeOut(1000);
				//setTimeout("$(\"#info\").hide(1000)", 2000);
				$("#cancel").click();
			}
			/* complete:function(){
				
			} */
		  });
		  
	}


	  
</script>
</head>
<body>

	<div  >
		<div id="info" style="margin-top:200px;margin-left:300px;display:none;"></div>
		<div id="box_top_bg"></div>
		<div id="box_in_bg">
			<h1>新增</h1>
	<form action="<%=request.getContextPath() %>/notify/cre"  method="post"  enctype="multipart/form-data"  id="form1">
			<table width="100%" border="0" cellspacing="1" cellpadding="5" class="table_2">
				<tr class="font_1">
					<td align="left"><label for="textfield"><strong>标&nbsp;&nbsp;题</strong></label>
					<input type="text" name="title" id="title" style="height:30px;width:400px"/></td>
				</tr>
				<tr>
					<td align="left">
					<input type="radio" name="type"   value="1"  />
						公告
					<label for="radio">
						<input type="radio"  name="type"  value="2"/>
						通知
					</label>
					</td>
				</tr>
				<tr>
					<td align="left"><label for="textfield2"></label>
					<textarea id="editor" type="text/plain" name="content" style="width:100%;height:350px;"></textarea>
					</td>
				</tr>
			</table>
		<div align="center">
			<input type="button" value="确认" onclick="forsub();" class="button">
			&nbsp;
			<input type="button" value="取消"  id="cancel" onclick="location.href='<%=request.getContextPath() %>/notify/managelist/1'" class="button">
		</div>
	</form>
		</div>
	</div>
	<div id="box_yy"></div>
	<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/ueditor/ueditor.all.min.js"> </script>
	<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/ueditor/lang/zh-cn/zh-cn.js"></script>
	<script type="text/javascript">
	var option={
            toolbars:[['fullscreen',   'undo', 'redo', '|','bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
                       'rowspacingtop', 'rowspacingbottom', 'lineheight', '|','customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
                       'indent', '|','justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
                        'link', 'unlink', 'anchor', '|', /* 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|', */
                       /*  'insertimage',   'attachment',*/   'pagebreak',   'horizontal', 'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 
                        'print', 'preview', 'searchreplace', ]],
                        wordCount:false,
        	  			elementPathEnabled:false,
  };
	
	var editor = new baidu.editor.ui.Editor(option);
    editor.render("editor");
	</script>



</body>
</html>