<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
BranchTodayLog branchTodayLog  = (BranchTodayLog)request.getAttribute("branchLog");
Branch nowbranch = (Branch)request.getAttribute("nowbranch");
%>
<script>
var $menuli = $(".uc_midbg ul li",parent.document);
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li",parent.document).eq(index).show().siblings().hide();
	});
</script>
<div id="box_bg" ></div>
<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>
		详细日志</h1>
		<div style="padding:10px; background:#FFF">
		
		<div style="padding:10px">当前站点：<font color="#FF6600" style="font-weight:bold"><%=nowbranch ==null?"":nowbranch.getBranchname()%></font></div>
		 
	<div class="uc_midbg" style="height:35px;">
		<ul style="height:20px;">
			<li><a href="javascript:void(0);" class="light">当日到货日报</a></li>
			<li><a href="javascript:void(0);">当日投递日报</a></li>
			<li><a href="javascript:void(0);">当日款项日报</a></li>
			<li><a href="javascript:void(0);">当日库存日报</a></li>
		</ul>
	</div>
    <div class="tabbox" style="width:1000px">
    
        <li>
        <div class="right_title">
        	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table4">
        		<tbody>
        			<tr class="font_1" height="30">
        				<!-- <td  align="center" valign="middle" bgcolor="#E1F0FF">今日应到货</td> -->
        				<td colspan="5" align="center" valign="middle" bgcolor="#E7F4E3">今日到货</td>
        				<td rowspan="2" align="center" valign="middle" bgcolor="#FFEFDF">到错货（单）</td>
        				</tr>
        			<tr class="font_1" height="30">
        				<!-- <td align="center" valign="middle" bgcolor="#E1F0FF">昨日少货（单）</td>
        				<td align="center" valign="middle" bgcolor="#E1F0FF">昨日未到货（单）</td>
        				<td align="center" valign="middle" bgcolor="#E1F0FF">昨日他站到货（单）</td> -->
        				<!-- <td align="center" valign="middle" bgcolor="#E1F0FF">今日已出库（单）</td> -->
        				<td align="center" valign="middle" bgcolor="#E7F4E3">未到货（单）</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">已到货（单）</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">漏扫已到货（单）</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">他站到货（单）</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">少货（单）</td>
        				</tr>
                         <%if(branchTodayLog !=null){ %>  
                         <tr height="30">
                            <%-- <td align="center" valign="middle"><strong><%=branchTodayLog.getYesterday_stortage() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getYesterday_not_arrive() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getYesterday_other_site_arrive() %></strong></td> --%>
                           <%--  <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_out_storehouse() %></strong></td> --%>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_not_arrive() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_arrive() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getJinri_lousaodaozhan() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_other_site_arrive() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_stortage() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_wrong_arrive() %></strong></td>
                            </tr>
                        <%} %>
                        </tbody>
                    </table>
            </div>
       </li>
         
          <li style="display:none">
		  <div class="right_title">
        	<div style="overflow-x: scroll; width:100%; padding-bottom:15px" id="scroll2">
            
                <table width="2400" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
                    <tbody>
                        <tr class="font_1" height="30">
                          <td colspan="4" rowspan="2" align="center" valign="middle" bgcolor="#E7F4E3">今日应投合计</td>
                          <td colspan="2"  rowspan="2" align="center" valign="middle" bgcolor="#E7F4E3">未领货</td>
                          <td  colspan="2" rowspan="2" align="center" valign="middle" bgcolor="#F8F3D3">今日投递</td>
                          <td colspan="26" align="left" valign="middle" bgcolor="#f1f1f1">今日结果</td>
                      </tr>
                        <tr class="font_1" height="30">
                          <td colspan="2" align="center" valign="middle" bgcolor="#E7F4E3">配送成功</td>
                            <td colspan="4" align="center" valign="middle" bgcolor="#FFE6E6">拒收</td>
                          <td align="center" valign="middle" bgcolor="#E1F0FF">滞留</td>
                            <td align="center" valign="middle" bgcolor="#FFEFDF">部分拒收</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">中转</td>
                            <td colspan="2" align="center" valign="middle" bgcolor="#E7F4E3">上门退成功</td>
                            <td align="center" valign="middle" bgcolor="#FFE6E6">上门退拒退</td>
                            <td colspan="3" align="center" valign="middle" bgcolor="#E7F4E3">上门换成功</td>
                            <td align="center" valign="middle" bgcolor="#FFE6E6">上门换拒换</td>
                            <td align="center" valign="middle" bgcolor="#FFEFDF">丢失破损</td>
                            <td colspan="3" align="center" valign="middle" bgcolor="#E1F0FF">归班合计</td>
                            <td colspan="3" align="center" valign="middle" bgcolor="#FFEFDF">小件员未反馈</td>
                            <td colspan="3" align="center" valign="middle" bgcolor="#FFEFDF">小件员反馈未归班</td>
                        </tr>
                        <tr class="font_1" height="30">
                         <td align="center" valign="middle" bgcolor="#E7F4E3">今日到货（单）</td>
                          <td align="center" valign="middle" bgcolor="#E7F4E3">昨日滞留</td>
                          <td align="center" valign="middle" bgcolor="#E7F4E3">昨日到站未领</td>
                          <td align="center" valign="middle" bgcolor="#E7F4E3">昨日未归班</td>
                          <td align="center" valign="middle" bgcolor="#E7F4E3">今日未领货（单）</td>
                          <td align="center" valign="middle" bgcolor="#E7F4E3">昨日滞留（单）</td>
                          <td align="center" valign="middle" bgcolor="#F8F3D3">今日领货</td>
                           <td align="center" valign="middle" bgcolor="#F8F3D3">昨日未归班</td> 
                            <td align="center" valign="middle" bgcolor="#E7F4E3">单数</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">应收金额</td>
                            <td colspan="4" align="center" valign="middle" bgcolor="#FFE6E6">单数</td>
                          <td align="center" valign="middle" bgcolor="#E1F0FF">单数</td>
                            <td align="center" valign="middle" bgcolor="#FFEFDF">单数</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">单数</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">单数</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">应退金额</td>
                            <td align="center" valign="middle" bgcolor="#FFE6E6">单数</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">单数</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">应收金额</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">应退金额</td>
                            <td align="center" valign="middle" bgcolor="#FFE6E6">单数</td>
                            <td align="center" valign="middle" bgcolor="#FFEFDF">单数</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">单数</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">应收金额</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">应退金额</td>
                            <td align="center" valign="middle" bgcolor="#FFEFDF">单数</td>
                            <td align="center" valign="middle" bgcolor="#FFEFDF">应收金额</td>
                            <td align="center" valign="middle" bgcolor="#FFEFDF">应退金额</td>
                            <td align="center" valign="middle" bgcolor="#FFEFDF">单数</td>
                            <td align="center" valign="middle" bgcolor="#FFEFDF">应收金额</td>
                            <td align="center" valign="middle" bgcolor="#FFEFDF">应退金额</td>
                        </tr>
                        <%if(branchTodayLog !=null){ %> 
                        <tr height="30">
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_daohuo() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getZuori_zhiliu_kuicun() %></strong></td> 
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getZuori_toudi_daozhanweiling() %></strong></td> 
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getZuori_weiguiban_kuicun() %></strong></td> 
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToudi_daozhanweiling() %></strong></td> 
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getZuori_zhiliu_kuicun() %></strong></td> 
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_linghuo() %></strong></td>
                             <td align="center" valign="middle"><strong><%=branchTodayLog.getWeiguiban_kuicun()%></strong></td> 
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_peisongchenggong_count() %></strong></td>
                            <td align="right" valign="middle"><strong><%=branchTodayLog.getToday_fankui_peisongchenggong_money() %></strong></td>
                            <td colspan="4" align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_jushou() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_leijizhiliu() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_bufenjushou() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_zhobgzhuan() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_shangmentuichenggong_count() %></strong></td>
                            <td align="right" valign="middle"><strong><%=branchTodayLog.getToday_fankui_shangmentuichenggong_money() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_shangmentuijutui() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_shangmenhuanchenggong_count() %></strong></td>
                            <td align="right" valign="middle"><strong><%=branchTodayLog.getToday_fankui_shangmenhuanchenggong_yingshou_money() %></strong></td>
                            <td align="right" valign="middle"><strong><%=branchTodayLog.getToday_fankui_shangmenhuanchenggong_yingtui_money() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_shangmenhuanjuhuan() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_diushi() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_heji_count() %></strong></td>
                            <td align="right" valign="middle"><strong><%=branchTodayLog.getToday_fankui_heji_yingshou_money() %></strong></td>
                            <td align="right" valign="middle"><strong><%=branchTodayLog.getToday_fankui_heji_yingtui_money() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_weifankui_heji_count() %></strong></td>
                            <td align="right" valign="middle"><strong><%=branchTodayLog.getToday_weifankui_heji_yingshou_money() %></strong></td>
                            <td align="right" valign="middle"><strong><%=branchTodayLog.getToday_weifankui_heji_yingtui_money() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankuiweiguiban_heji_count() %></strong></td>
                            <td align="right" valign="middle"><strong><%=branchTodayLog.getToday_fankuiweiguiban_heji_yingshou_money() %></strong></td>
                            <td align="right" valign="middle"><strong><%=branchTodayLog.getToday_fankuiweiguiban_heji_yingtui_money() %></strong></td>
                        </tr>
                        <%} %>
                    </tbody>
                </table>
            </div>
			</div>
         </li>
         
          <li style="display:none">
		  	<div class="right_title">
		  	<div style="overflow-x: scroll; width:100%; padding-bottom:15px" id="scroll2">
		  		<table width="1400" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
		  			<tbody>
		  				<tr class="font_1" height="30">
		  					<td colspan="10" align="center" valign="middle" bgcolor="#E1F0FF">实收款</td>
		  					<td colspan="5" align="center" valign="middle" bgcolor="#E7F4E3">实缴款</td>
	  					</tr>
		  				<tr class="font_1" height="30">
		  				  <td rowspan="2" align="center" valign="middle" bgcolor="#E7F4E3">配送成功（单）</td>
		  					<td colspan="3" align="center" valign="middle" bgcolor="#E7F4E3">实收金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFEFDF">上门退成功</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE6E6">实付金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFEFDF">上门换成功（单）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE6E6">实收金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE6E6">实退金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE900">实收合计（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#F8F3D3">现金（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#F8F3D3">POS（元）</td>
		  					<td colspan="3" rowspan="2" align="center" valign="middle" bgcolor="#F8F3D3">支票（元）</td>
	  					</tr>
		  				<tr height="30" class="font_1">
		  					<td height="36" align="center" valign="middle" bgcolor="#F8F3D3">现金（元）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">POS（元）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">支票（元）</td>
	  					</tr>
	  					<%if(branchTodayLog !=null){ %> 
		  				<tr height="30">
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getToday_funds_peisongchenggong_count() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getToday_funds_peisongchenggong_cash() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getToday_funds_peisongchenggong_pos() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getToday_funds_peisongchenggong_checkfee() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getToday_funds_shangmentuichenggong_count() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getToday_funds_shangmentuichenggong_money() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getShangmenhuanchenggong() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getShangmenhuanchenggong_yingshou_amount() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getShangmenhuanchenggong_yingtui_amount() %></strong></td>
		  					<td align="right" valign="middle"><strong>
		  					<%=branchTodayLog.getToday_fankui_peisongchenggong_money().
		  						subtract(branchTodayLog.getToday_funds_shangmentuichenggong_money()).
		  					add(branchTodayLog.getShangmenhuanchenggong_yingshou_amount()).
		  					subtract(branchTodayLog.getShangmenhuanchenggong_yingtui_amount()) %>
		  					</strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getShijiaokuan_cash_amount() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getShijiaokuan_pos_amount() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getShijiaokuan_checkfee_amount() %></strong></td>
	  					</tr>
	  					<%} %>
	  				</tbody>
	  			</table>
	  			</div>
	  	</div>
         </li>
         
          <li style="display:none">
		  	<div class="right_title">
		  		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table3">
		  			<tbody>
		  				<tr class="font_1" height="30">
		  					<td colspan="4" align="center" valign="middle" bgcolor="#E1F0FF">昨日库存（单）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#E7F4E3">今日到货（单）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFEFDF">今日妥投（单）</td>
		  					<td colspan="2" align="center" valign="middle" bgcolor="#F8F3D3">今日出库（单）</td>
		  					<td colspan="4" align="center" valign="middle" bgcolor="#FFE6E6">今日库存（单）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE6E6">货物丢失（单）</td>
	  					</tr>
		  				<tr class="font_1" height="30">
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">到站未领（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">退货未出站（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">滞留（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">小件员未归班（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">退货出库（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">中转出库（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">到站未领（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">退货未出站（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">滞留（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">小件员未归班（单）</td>
	  					</tr>
	  					<%if(branchTodayLog !=null){ %>
		  				<tr height="30">
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getZuori_toudi_daozhanweiling() %></strong></td> 
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getZuori_jushou_kuicun() %></strong></td> 
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getZuori_zhiliu_kuicun() %></strong></td> 
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getZuori_weiguiban_kuicun() %></strong></td> 
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getToday_kucun_arrive() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getToday_kucun_sucess() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getToday_kucun_tuihuochuku() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getToday_kucun_zhongzhuanchuku() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getToudi_daozhanweiling() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getJushou_kuicun() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getZhiliu_kuicun() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getWeiguiban_kuicun() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getToday_fankui_diushi() %></strong></td>
	  					</tr>
	  					<%} %>
	  				</tbody>
	  			</table>
	  	</div>
         </li>
	</div>
	
	<table width="100%" border="0" cellpadding="10" cellspacing="0" class="table_5">
				<tbody>
					<tr >
					<%if(branchTodayLog !=null){ %>
						<td align="center" valign="middle">站点提交的备注：<%=branchTodayLog.getJobRemark() %></td>
					<%} else{%>	
					    <td align="center" valign="middle">站点提交的备注：</td>
					 <%} %>
					</tr>
				</tbody>
			</table>
		</div>
		 <div align="center"><input type="button" value="关闭" class="button" onClick="closeBox()"></div>
	</div>
</div>
<div id="box_yy"></div>

