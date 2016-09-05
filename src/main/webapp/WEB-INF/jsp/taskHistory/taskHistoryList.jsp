<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>历史版本 Update List</title>

<script type="text/javascript">
$(function(){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/taskShow/getNearestViewRecord",
		dataType:"json",
		success : function(data) {
			createAccordionByRecords(data);
		}                 
	});	
});

function createAccordionByRecords(records){
	for(var i=0; i<records.length; i++){
		var item = records[i];
		
		var titleValue = item.versionNo + " " + item.name + " " + getDateStringByTimestamp(item.onlineTime);
		var contentValue = item.added + getAttachmentLinkHtml(item);
		var selectedValue = (i < 3);	//最近3次的内容展开，3次之外的内容折叠
		$('#recordsDiv').accordion('add', {
			title: titleValue,
			content: contentValue,
			selected: selectedValue
		});
	}
}

function getAttachmentLinkHtml(item) {
	var attachmentDiv = $('<div></div>');
	var attachmentModelList = item.attachmentModelList;
	if(attachmentModelList!=null && (attachmentModelList instanceof Array) && attachmentModelList.length>0) {
		attachmentDiv.append('<hr>')
		attachmentDiv.append('<p><label><b>附件：</b></label></p>')
		for (var i=0; i<attachmentModelList.length; i++){
			var attachmentModel =  attachmentModelList[i];
			var name = attachmentModel.name;
			var url = attachmentModel.url;
			var finalUrl = url + '?type=download&name="' + name + '"';
			attachmentDiv.append('<p><a href="' + finalUrl + '">' + name + '</a></p>');
		}
	}
	return attachmentDiv.html();
}

function getDateStringByTimestamp(timestamp){
	var newDate = new Date();
	newDate.setTime(timestamp);
	return newDate.toLocaleDateString();
}

function viewHandbook(){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/taskShow/getLatestHandbook",
		dataType:"json",
		success : function(data) {
			if(!!data && !!data.url){
				var handbookUrl = data.url + '?type=download&name="' + data.name + '"';
				window.open(handbookUrl);
			} else {
				alert('找不到文件！');
			}
		}                 
	});	
}

</script>
</head>

<body style="background:#f5f5f5">
	<div class="saomiao_box2">
		<table id="descTable" width="100%">
			<tr width="100%">
				<td width="50%" align="left">
					<img src="<%=request.getContextPath()%>/images/mail.png" alt="" style="height: 20px;" />
					<label><b>历史版本 Update List</b></label>
				</td>
				<td width="50%" align="right">
					<img src="<%=request.getContextPath()%>/images/doc.png" alt="" style="height: 20px;" />
					<a href="javascript:viewHandbook();">DMP操作手册</a>
					<!-- <input type="button" id="viewHandbook" onclick="viewHandbook()"  value="查看" /> -->
				</td>
			</tr>
		</table>
		<div id="recordsDiv" class="easyui-accordion" data-options="multiple:true" style="padding:10px">
		</div>		
	</div>
</body>
</html>