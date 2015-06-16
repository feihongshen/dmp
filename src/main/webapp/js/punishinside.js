
	function revisefindWithByCreateGongdan(form,appendhtmlid){
		var showhtml="";
		$.ajax({
			type : "POST",
			url:$(form).attr("action"),
			data : $(form).serialize(),
			dataType : "json",
			success : function(data) {
				$.each(data,function(i,a){
					showhtml+="<tr height=\"30\" onclick=\"clickonenum(this);\" bgcolor=\"#cccccc\">"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.gongdanNo+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.cwb+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.gongdanType+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.gongdanState+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.phoneNumber+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.tousuOrganization+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.gongdanShouliName+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.shoulitime+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.tousuonesort+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.toususecondsort+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.tousudealresult+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.customername+"</strong></td>"+
								"<td align=\"center\" valign=\"middle\"><strong>"+a.cuijianNum+"</strong></td>"
								+"</tr>";
				});
				$("#"+appendhtmlid).html("");
				$("#"+appendhtmlid).html(showhtml);
			}
		});
	}
	
function createinpunishbyQuestNo(form,appendhtmlid){
	var showhtml="";
	$.ajax({
		type : "POST",
		url:$(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$.each(data,function(i,a){
				showhtml+="<tr height=\"30\" onclick=\"clickonenum(this);\" bgcolor=\"#cccccc\">"+
							"<td align=\"center\" valign=\"middle\"><strong>"+a.questNo+"</strong></td>"+
							"<td align=\"center\" valign=\"middle\"><strong>"+a.cwb+"</strong></td>"+
							"<td align=\"center\" valign=\"middle\"><strong>"+a.cwbprice+"</strong></td>"+
							"<td align=\"center\" valign=\"middle\"><strong>"+a.customername+"</strong></td>"+
							"<td align=\"center\" valign=\"middle\"><strong>"+a.cwbtype+"</strong></td>"+
							"<td align=\"center\" valign=\"middle\"><strong>"+a.creuser+"</strong></td>"+
							"<td align=\"center\" valign=\"middle\"><strong>"+a.cretime+"</strong></td>"+
							"<td align=\"center\" valign=\"middle\"><strong>"+a.abnormaltype+"</strong></td>"+
							"<td align=\"center\" valign=\"middle\"><strong>"+a.dealstate+"</strong></td>"+
							"<td align=\"center\" valign=\"middle\"><strong>"+a.dealresult+"</strong></td>"+
							"<td align=\"center\" valign=\"middle\"><strong>"+a.isfind+"</strong></td>"+
							+"</tr>";
			});
			$("#"+appendhtmlid).html("");
			$("#"+appendhtmlid).html(showhtml);
			
		}
	});
	
}
