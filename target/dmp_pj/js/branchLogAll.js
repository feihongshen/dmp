
//站点日志ajax动态加载========================
//到货日报
function getTodayArrivalDTO(pname,path,branchid,htmlid,startTime){
	$.ajax({
		type: "POST",
		url:pname+"/logtoday/"+path+"",
		data:{
			"branchid":branchid,
			"startTime":startTime
		},
		dataType:"json",
		success : function(data) {
			var showhtml="";
			if(data.length!=0){
					showhtml="<td align=\"center\" valign=\"middle\"><strong><a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/daohuo_weidaohuo/1?flag=1\">"+data.weidaohuo+"</a></strong></td>" +
					"<td align=\"center\" valign=\"middle\"><strong><a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/daohuo_yidaohuo/1?flag=1\">"+data.yidaohuo+"</a></strong></td>" +
					"<td align=\"center\" valign=\"middle\"><strong><a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/daohuo_lousaoyidaohuo/1?flag=1\">"+data.jinri_lousaodaozhan+"</a></strong></td>" +
					"<td align=\"center\" valign=\"middle\"><strong><a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/daohuo_tazhandaohuo/1?flag=1\">"+data.tazhandaohuo+"</a></strong></td>" +
					"<td align=\"center\" valign=\"middle\"><strong><a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/daohuo_shaohuo/1?flag=1\">"+data.shaohuo+"</a></strong></td>" +
					"<td align=\"center\" valign=\"middle\"><strong><a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/daohuo_daocuohuo/1?flag=1\">"+data.daocuohuo+"</a></strong></td>";
			}
			if($("#"+htmlid,parent.document).length>0){
				$("#"+htmlid,parent.document).html("");
				$("#"+htmlid,parent.document).html(showhtml);
			}else{
				$("#"+htmlid).html("");
				$("#"+htmlid).html(showhtml);
			}
			$("#daohuo_a").html("今日到货日报");
		}
	});
}
//投递日报
function getTodayDeliveryDTO(pname,path,branchid,htmlid){
	$.ajax({
		type: "POST",
		url:pname+"/logtoday/"+path+"",
		data:{
			"branchid":branchid
		},
		dataType:"json",
		success : function(data) {
			var showhtml="";
			if(data.length!=0){
				showhtml="" +
				"<td align=\"center\" valign=\"middle\"><strong>" +
				"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_daohuo/1?flag=2\">"+data.today_fankui_daohuo+"</a></strong></td>" +
				"<td align=\"center\" valign=\"middle\"><strong>"+data.zuori_zhiliu_kuicun+"</strong></td>" + /*【todayStock.getZuori_zhiliu_kuicun() 】*/
				"<td align=\"center\" valign=\"middle\"><strong>"+data.zuori_toudi_daozhanweiling+"</strong></td>" +
				"<td align=\"center\" valign=\"middle\"><strong>" +
				""+data.zuori_weiguiban_kuicun+"</strong></td>" +  /*【todayStock.getZuori_weiguiban_kuicun() 】*/
				"<td align=\"center\" valign=\"middle\"><strong>" +
				"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_daozhanweiling/1?flag=2\">"+data.toudi_daozhanweiling+"</a></strong></td>" +
				"<td align=\"center\" valign=\"middle\"><strong>" +
				"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/zuori_fankui_leijizhiliu/1?flag=2\">"+data.zuori_fankui_leijizhiliu+"</a></strong></td>" +
				"<td align=\"center\" valign=\"middle\"><strong>" +
				"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_linghuo/1?flag=2\">"+data.today_fankui_linghuo+"</a></strong></td>" +
				"<td align=\"center\" valign=\"middle\"><strong>" +
				""+data.zuori_weiguiban_kuicun+"</strong></td>" + /*【todayStock.getZuori_weiguiban_kuicun() 】*/
				"<td align=\"center\" valign=\"middle\"><strong>" +
				"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_peisongcheng/1?flag=2\">"+data.today_fankui_peisongchenggong_count+"</a></strong></td>" +
				 "<td align=\"right\" valign=\"middle\"><strong>" +
				 "<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_peisongcheng/1?flag=2\">"+data.today_fankui_peisongchenggong_money.toFixed(2)+"</a></strong></td>" +
				 "<td colspan=\"4\" align=\"center\" valign=\"middle\"><strong>" +
				 "<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_jushou/1\">"+data.today_fankui_jushou+"</a></strong></td>" +
				 "<td align=\"center\" valign=\"middle\"><strong>" +
				 "<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_jinrizhiliu/1?flag=2\">"+data.today_fankui_leijizhiliu+"</a></strong></td>" +
				 "<td align=\"center\" valign=\"middle\"><strong>" +
				 "<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_bufenjushou/1?flag=2\">"+data.today_fankui_bufenjushou+"</a></strong></td>" +
				 "<td align=\"center\" valign=\"middle\"><strong>" +
				 "<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_zhongzhuan/1?flag=2\">"+data.today_fankui_zhobgzhuan+"</a></strong></td>" +
				 "<td align=\"center\" valign=\"middle\"><strong>" +
				 "<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_shangmentuichenggong/1?flag=2\">"+data.today_fankui_shangmentuichenggong_count+"</a></strong></td>" +
				 "<td align=\"right\" valign=\"middle\"><strong>" +
				 ""+data.today_fankui_shangmentuichenggong_money.toFixed(2)+"</strong></td>" +
				 "<td align=\"center\" valign=\"middle\"><strong>" +
				 "<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_shangmentuijutui/1?flag=2\">"+data.today_fankui_shangmentuijutui+"</a></strong></td>" +
				"<td align=\"center\" valign=\"middle\"><strong>" +
				"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_shangmenhuanchenggong/1?flag=2\">"+data.today_fankui_shangmenhuanchenggong_count+"</a></strong></td>" +
				"<td align=\"right\" valign=\"middle\"><strong>" +
				""+data.today_fankui_shangmenhuanchenggong_yingshou_money.toFixed(2)+"</strong></td>" +
				"<td align=\"right\" valign=\"middle\"><strong>" +
				""+data.today_fankui_shangmenhuanchenggong_yingtui_money.toFixed(2)+"</strong></td>" +
				"<td align=\"center\" valign=\"middle\"><strong>" +
				"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_shangmenhuanjuhuan/1?flag=2\">"+data.today_fankui_shangmenhuanjuhuan+"</a></strong></td>" +
				"<td align=\"center\" valign=\"middle\"><strong>" +
				"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_diushi/1?flag=2\">"+data.today_fankui_diushi+"</a></strong></td>" +
				"<td align=\"center\" valign=\"middle\"><strong>" +
				"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_fankuiheji/1?flag=2\">"+data.today_fankui_heji_count+"</a></strong></td>" +
				"<td align=\"right\" valign=\"middle\"><strong>" +
				""+data.today_fankui_heji_yingshou_money.toFixed(2)+"</strong></td>" +
				"<td align=\"right\" valign=\"middle\"><strong>" +
				""+data.today_fankui_heji_yingtui_money.toFixed(2)+"</strong></td>" +
				"<td align=\"center\" valign=\"middle\"><strong>" +
				"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_weifankuiheji/1?flag=2\">"+data.today_weifankui_heji_count+"</a></strong></td>" +
				"<td align=\"right\" valign=\"middle\"><strong>" +
				" "+data.today_weifankui_heji_yingshou_money.toFixed(2)+"</strong></td>" +
				" <td align=\"right\" valign=\"middle\"><strong>" +
				" "+data.today_weifankui_heji_yingtui_money.toFixed(2)+"</strong></td>" +
				" <td align=\"center\" valign=\"middle\"><strong>" +
				" <a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_fankuiweiguiban_heji/1?flag=2\">"+data.today_fankuiweiguiban_heji_count+"</a></strong></td>" +
				" <td align=\"right\" valign=\"middle\"><strong>" +
				" "+data.today_fankuiweiguiban_heji_yingshou_money.toFixed(2)+"</strong></td>" +
				" <td align=\"right\" valign=\"middle\"><strong>" +
				" "+data.today_fankuiweiguiban_heji_yingtui_money.toFixed(2)+"</strong></td>" +
				" <td colspan=\"2\" align=\"center\" valign=\"middle\"><strong>" +
				""+data.tuotoulv+"%</strong></td>";
			}
			if($("#"+htmlid,parent.document).length>0){
				$("#"+htmlid,parent.document).html("");
				$("#"+htmlid,parent.document).html(showhtml);
			}else{
				$("#"+htmlid).html("");
				$("#"+htmlid).html(showhtml);
			}
			$("#toudi_a").html("今日投递日报");
		}
	});
}
//款项日报
function getTodayFundsDTO(pname,path,branchid,htmlid,startTime){
	$.ajax({
		type: "POST",
		url:pname+"/logtoday/"+path+"",
		data:{
			"branchid":branchid,
			"startTime":startTime
		},
		dataType:"json",
		success : function(data) {
			var showhtml="";
			if(data.length!=0){
				showhtml="<td align=\"center\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/kuanxiang_peisongchenggong/1?flag=3\">"+data.peisongchenggong+"</a></strong></td>" +
		  					"<td align=\"right\" valign=\"middle\"><strong>"+data.peisongchenggong_cash_amount.toFixed(2)+"</strong></td>" +
		  					"<td align=\"right\" valign=\"middle\"><strong>"+data.peisongchenggong_pos_amount.toFixed(2)+"</strong></td>" +
		  					"<td align=\"right\" valign=\"middle\"><strong>"+data.peisongchenggong_checkfee_amount.toFixed(2)+"</strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/kuanxiang_shangmentui/1?flag=3\">"+data.shangmentuichenggong+"</a></strong></td>" +
		  					"<td align=\"right\" valign=\"middle\"><strong>"+data.shangmentuichenggong_cash_amount.toFixed(2)+"</strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/kuanxiang_shangmenhuan/1?flag=3\">"+data.shangmenhuanchenggong+"</a></strong></td>" +
		  					"<td align=\"right\" valign=\"middle\"><strong>"+data.shangmenhuanchenggong_yingshou_amount.toFixed(2)+"</strong></td>" +
		  					"<td align=\"right\" valign=\"middle\"><strong>"+data.shangmenhuanchenggong_yingtui_amount.toFixed(2)+"</strong></td>" +
		  					"<td align=\"right\" valign=\"middle\"><strong>"+data.shishou_sum_amount.toFixed(2)+"</strong></td>" +
		  					"<td align=\"right\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/kuanxiang_shijiaokuan/1?flag=3\">"+data.shijiaokuan_cash_amount.toFixed(2)+"</a></strong></td>" +
		  					"<td align=\"right\" valign=\"middle\"><strong><a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/kuanxiang_shijiaokuan/1?flag=3\">"+data.shijiaokuan_pos_amount.toFixed(2)+"</a></strong></td>" +
		  					"<td align=\"right\" valign=\"middle\"><strong><a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/kuanxiang_shijiaokuan/1?flag=3\">"+data.shijiaokuan_checkfee_amount.toFixed(2)+"</a></strong></td>";
			}
			if($("#"+htmlid,parent.document).length>0){
				$("#"+htmlid,parent.document).html("");
				$("#"+htmlid,parent.document).html(showhtml);
			}else{
				$("#"+htmlid).html("");
				$("#"+htmlid).html(showhtml);
			}
			$("#kuanxiang_a").html("今日款项日报");
		}
	});
}
//库存日报
function getTodayStockDTO(pname,path,branchid,htmlid){
	$.ajax({
		type: "POST",
		url:pname+"/logtoday/"+path+"",
		data:{
			"branchid":branchid
		},
		dataType:"json",
		success : function(data) {
			var showhtml="";
			if(data.length!=0){
				showhtml="<td align=\"center\" valign=\"middle\"><strong>" +
		  					""+data.zuori_toudi_daozhanweiling+"</strong></td>" +/*<%=todayDelivery.getZuori_toudi_daozhanweiling() %>*/
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					""+data.zuori_jushou_kuicun+"</strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					""+data.zuori_zhiliu_kuicun+"</strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					""+data.zuori_weiguiban_kuicun+"</strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/kucun_daohuo/1?flag=4\">"+data.jinridaohuo+"</a></strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/kucun_tuotou/1?flag=4\">"+data.jinrituodou+"</a></strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/kucun_tuihuochuku/1?flag=4\">"+data.jinrituihuochuku+"</a></strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/kucun_zhongzhuanchuku/1?flag=4\">"+data.jinrizhongzhuanchuku+"</a></strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_daozhanweiling/1?flag=4\">"+data.toudi_daozhanweiling+"</a></strong></td>" +/*<%=todayDelivery.getToudi_daozhanweiling() %>*/
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/jushou_kuicun/1?flag=4\">"+data.jushou_kuicun+"</a></strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/zhiliu_kuicun/1?flag=4\">"+data.zhiliu_kuicun+"</a></strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
		  					"<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/weiguiban_kuicun/1?flag=4\">"+data.weiguiban_kuicun+"</a></strong></td>" +
		  					"<td align=\"center\" valign=\"middle\"><strong>" +
                            "<a href=\""+pname+"/logtoday/showAllAjax/"+branchid+"/toudi_diushi/1?flag=4\">"+data.today_fankui_diushi+"</a></strong></td>";/*<%=todayDelivery.getToday_fankui_diushi() %>*/
			}
			if($("#"+htmlid,parent.document).length>0){
				$("#"+htmlid,parent.document).html("");
				$("#"+htmlid,parent.document).html(showhtml);
			}else{
				$("#"+htmlid).html("");
				$("#"+htmlid).html(showhtml);
			}
			$("#kucun_a").html("今日库存日报");
		}
		
	});
}
//站点日志ajax动态加载========================