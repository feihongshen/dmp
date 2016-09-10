<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="cn.explink.controller.DeliveryStateView"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.dao.CwbOrderTypeDAO"%>
<%@page import=" cn.explink.domain.DeliveryState"%>
<%@page import=" cn.explink.domain.Reason"%>
<%@page import=" net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum" %>
<%@page import="java.math.BigDecimal" %>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
DeliveryStateView deliverystate =request.getAttribute("deliverystate")==null?new DeliveryStateView(): (DeliveryStateView)request.getAttribute("deliverystate");
List<Reason> backlist = (List<Reason>)request.getAttribute("backreasonlist");

List<Reason> leavedlist = (List<Reason>)request.getAttribute("leavedreasonlist");
Long deliveryStateType=(Long)session.getAttribute("deliveryStateType"); 
List<Reason> losereasonlist = (List<Reason>)request.getAttribute("losereasonlist");

List<Reason> podremarkreasonlist = (List<Reason>)request.getAttribute("podremarkreasonlist");

List<Reason> weishuakareasonlist = (List<Reason>)request.getAttribute("weishuakareasonlist");
List<Reason> changereasonlist = (List<Reason>)request.getAttribute("changereasonlist");

CwbOrder cwborder = (CwbOrder)request.getAttribute("cwborder");

long backreasonid = request.getAttribute("backreasonid")==null?0:((Long)request.getAttribute("backreasonid")).longValue(); 
long leavedreasonid = request.getAttribute("leavedreasonid")==null?0:((Long)request.getAttribute("leavedreasonid")).longValue();
long weishuakareasonid = request.getAttribute("weishuakareasonid")==null?0:((Long)request.getAttribute("weishuakareasonid")).longValue();
long losereasonid = request.getAttribute("losereasonid")==null?0:((Long)request.getAttribute("losereasonid")).longValue();

long changereasonid = request.getAttribute("changereasonid")==null?0:((Long)request.getAttribute("changereasonid")).longValue(); 

String showposandqita = request.getAttribute("showposandqita")==null?"no":(String)request.getAttribute("showposandqita");
String isShowZLZDLH = request.getAttribute("isShowZLZDLH")==null?"no":(String)request.getAttribute("isShowZLZDLH");
String isReasonRequired = request.getAttribute("isReasonRequired")==null?"no":(String)request.getAttribute("isReasonRequired");
//是否允许反馈为部分拒收
String partReject = request.getAttribute("partReject")==null?"yes":(String)request.getAttribute("partReject");

int isOpenFlag = request.getAttribute("isOpenFlag")==null?0:((Integer)request.getAttribute("isOpenFlag")).intValue();

%>
<script>
var showposandqita="<%=showposandqita%>";
function editInit(){
	for(var i =0 ; i < initEditArray.length ; i ++){
		var value = initEditArray[i].split(",")[0];
		var name = initEditArray[i].split(",")[1];
		$("#"+name, parent.document).val(value);
	}
	thisCheck();
	window.parent.click_podresultid(<%=deliverystate.getDeliverystate()%>,<%=DeliveryStateEnum.PeiSongChengGong.getValue()%>,<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>,
   			<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue()%>,<%=DeliveryStateEnum.JuShou.getValue()%>,
   			<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>,<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>,<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue() %>,
   			<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>,<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>,<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>,
   			$("#backreasonid", parent.document).val(),$("#leavedreasonid", parent.document).val(),$("#podremarkid", parent.document).val(),$("#newpaywayid", parent.document).val(),
   			$("#weishuakareasonid", parent.document).val(),$("#losereasonid", parent.document).val(),false);
	$("input[type='text']", parent.document).focus(function(){
		$(this).select();
	});
}

var initEditArray = new Array();
initEditArray[0]="<%=deliverystate.getDeliverystate()%>,podresultid";
initEditArray[1]="<%=backreasonid%>,backreasonid";
initEditArray[2]="<%=leavedreasonid%>,leavedreasonid";
initEditArray[3]="<%=deliverystate.getPodremarkid()%>,podremarkid";
initEditArray[4]="<%=deliverystate.getReturnedfee()%>,returnedfee";
initEditArray[5]="<%=deliverystate.getCash() %>,receivedfeecash";
if(showposandqita=="yes"){
	initEditArray[6]="<%=deliverystate.getPos()%>,receivedfeepos";
}else{
	initEditArray[6]="<%=BigDecimal.ZERO%>,receivedfeepos";
}

initEditArray[7]="<%=deliverystate.getPosremark()%>,posremark";
initEditArray[8]="<%=deliverystate.getCheckfee()%>,receivedfeecheque";
initEditArray[9]="<%=deliverystate.getOtherfee()%>,receivedfeeother";
initEditArray[10]="<%=deliverystate.getCheckremark().replaceAll(",", "，")%>,checkremark";
initEditArray[11]="<%=deliverystate.getDeliverstateremark().replaceAll(",", "，") %>,deliverstateremark";
initEditArray[12]="<%=deliverystate.getReceivedfee()%>,infactfee";
initEditArray[13]="<%=deliverystate.getInfactfare()%>,infactfare";
if(parseInt($("#isOpenFlag").val())!=0){
	initEditArray[14]="<%=weishuakareasonid%>,weishuakareasonid";
	initEditArray[15]="<%=losereasonid%>,losereasonid";
}
</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
	<h1><div id="close_box" onclick="closeBox()"></div>修改反馈
	<div id="box_form"  style="overflow:auto;width:400px">
	<form id="deliverystate_save_Form" name="deliverystate_save_Form" 
			 onSubmit="if(check_deliveystate(<%=DeliveryStateEnum.PeiSongChengGong.getValue()%>,<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>,
   			<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue()%>,<%=DeliveryStateEnum.JuShou.getValue()%>,
   			<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>,<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>,<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue() %>,
   			<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>,<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>,<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>,'<%=isReasonRequired %>','<%=cwborder.getTranscwb()%>')){$('#sub').attr('disabled','disabled');submitSaveFormAndCloseBox4Shangmentui(this);$('#sub').val('处理中...');}return false;" 
			 action="<%=request.getContextPath()%>/delivery/editDeliveryState/<%=deliverystate.getCwb()%>/<%=deliverystate.getDeliveryid()%>" method="post"  >
				<ul>
					<li><span>订单号：</span><%=deliverystate.getCwb() %></li>
					<li><span>订单类型：</span>
					<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(deliverystate.getCwbordertypeid()==ce.getValue()){ %>
						<%=ce.getText() %>
					<%}} 
					%>
					</li>
					<li>
						<span>小件员姓名：</span><%=deliverystate.getDeliverealname() %>
					</li>
					<li><span>配送结果：</span> 
					<input type="hidden" id="isReceivedfee" value="<%=(cwborder.getPaybackfee().compareTo(cwborder.getReceivablefee())==1?"no":"yes") %>"/>
					<input type="hidden" id="newpaywayid" value="<%=cwborder.getNewpaywayid()%>"/>
					<input type="hidden" id="paywayid" value="<%=cwborder.getPaywayid()%>"/>
					<input type="hidden" id="isOpenFlag" value="<%=isOpenFlag%>"/>
					<input type="hidden" id="shishou" value="<%=deliverystate.getInfactfare()%>"/>
				<select id ="podresultid" name ="podresultid" 
		   			onChange="click_podresultid(<%=deliverystate.getDeliverystate() %>,<%=DeliveryStateEnum.PeiSongChengGong.getValue()%>,<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>,
		   			<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue()%>,<%=DeliveryStateEnum.JuShou.getValue()%>,
		   			<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>,<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>,<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()%>,
		   			<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>,<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>,<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>,<%=backreasonid%>,<%=leavedreasonid%>,
		   			<%=deliverystate.getPodremarkid()%>,$('#newpaywayid', parent.document).val(),<%=weishuakareasonid%>,<%=losereasonid%>,'<%=showposandqita%>',true)">
                   		<option value ="-1">==请选择==</option>
                   <%if(deliverystate.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue() 
                	//OXO 订单反馈结果与 PeiSong一样 by jinghui.pan@pjbest.com on 20150729
                   || deliverystate.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO.getValue() ||deliverystate.getCwbordertypeid()==CwbOrderTypeIdEnum.Express.getValue()){%>
                   		<option value ="<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>"><%=DeliveryStateEnum.PeiSongChengGong.getText() %></option>
						<option value ="<%=DeliveryStateEnum.JuShou.getValue() %>"><%=DeliveryStateEnum.JuShou.getText() %></option>
						<%if(partReject.equals("yes")){ %>
							<option value ="<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>"><%=DeliveryStateEnum.BuFenTuiHuo.getText() %></option>
						<%}%>
						<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
						<option value ="<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>"><%=DeliveryStateEnum.DaiZhongZhuan.getText() %></option>
						<%if(isShowZLZDLH.equals("yes")){ %>
							<option value ="<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue() %>"><%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getText() %></option>
						<%} %>
						<option value ="<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>"><%=DeliveryStateEnum.HuoWuDiuShi.getText() %></option>
                   <%}else if(deliverystate.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()){ %>
                   		<option value ="<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue() %>"><%=DeliveryStateEnum.ShangMenHuanChengGong.getText() %></option>
                   		<option value ="<%=DeliveryStateEnum.JuShou.getValue() %>"><%=DeliveryStateEnum.JuShou.getText() %></option>
                   		<%if(partReject.equals("yes")){ %>
							<option value ="<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>"><%=DeliveryStateEnum.BuFenTuiHuo.getText() %></option>
						<%} %>
						<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
						<option value ="<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>"><%=DeliveryStateEnum.DaiZhongZhuan.getText() %></option>
						<%if(isShowZLZDLH.equals("yes")){ %>
							<option value ="<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue() %>"><%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getText() %></option>
						<%} %>
						<option value ="<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>"><%=DeliveryStateEnum.HuoWuDiuShi.getText() %></option>
                   <%}else if(deliverystate.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()){ %>
                   		<option value ="<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue() %>"><%=DeliveryStateEnum.ShangMenTuiChengGong.getText() %></option>
                   		<option value ="<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>"><%=DeliveryStateEnum.ShangMenJuTui.getText() %></option>
                   		<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
                   		<option value ="<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>"><%=DeliveryStateEnum.DaiZhongZhuan.getText() %></option>
                   		<%if(isShowZLZDLH.equals("yes")){ %>
                   			<option value ="<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue() %>"><%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getText() %></option>
                   		<%} %>
                   <%} %>
                  
                   
                   
                </select>*</li>
           		<li><span>快递单号：</span>
                     <input type="text" name="transcwb" id="transcwb" value="<%=cwborder.getTranscwb()%>" onKeyDown='if(event.keyCode==13){return false;}'/> (每次只能输入一个快递单号)
	           	</li>
           		<li><span>退货原因：</span>
	           		<select name="backreasonid" id="backreasonid">
	           			<option value ="0">==请选择==</option>
	           			<%for(Reason r : backlist){ %>
	           				<option value="<%=r.getReasonid()%>" title="<%=r.getReasoncontent() %>"><%if(r.getReasoncontent()!=null&&r.getReasoncontent().length()>10){ %><%=r.getReasoncontent().substring(0,10) %>...<%}else{ %><%=r.getReasoncontent()%><%} %></option>
	           			<%} %>
	           		</select>
	           	</li>
	           		
	           	<li><span>一级原因：</span>
			        <select name="firstlevelreasonid" id="firstlevelreasonid"  onchange="getSecondReasonByFirstreasonid('<%=request.getContextPath()%>/reason/getSecondreason','leavedreasonid',this.value)"  >
			        	<option value ="0">==请选择==</option>
			        	<%
			        	if(leavedlist!=null&&leavedlist.size()>0){
			        	for(Reason r :leavedlist){
			        		if(r.getWhichreason()!=1){
			        			continue;
			        		}
			        	%>
			        		<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			        	<%}}%>
			        </select>
		        </li>
		        
	           	<li><span>二级原因：</span>
			        <select name="leavedreasonid" id="leavedreasonid">
			        	<option value ="0">==请选择==</option>
			        	<%-- <%for(Reason r : leveltwolist){ %>
	           				<option value="<%=r.getParentid()%>" title="<%=r.getReasoncontent() %>"><%if(r.getReasoncontent()!=null&&r.getReasoncontent().length()>10){ %><%=r.getReasoncontent().substring(0,10) %>...<%}else{ %><%=r.getReasoncontent()%><%} %></option>
	           			<%} %> --%>
			        </select>
		        </li>
		        
	        	<li><span>一级原因：</span>
			        <select name="firstchangereasonid" id="firstchangereasonid" onchange="getSecondReasonByFirstreasonid('<%=request.getContextPath()%>/reason/getSecondreason','changereasonid',this.value)" >
			        	<option value ="0">==请选择==</option>
			        	<%
			        	if(changereasonlist!=null&&changereasonlist.size()>0){
			        	for(Reason r :changereasonlist){
			        		if(r.getWhichreason()!=1){
			        			continue;
			        		}
			        	%>
			        		<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			        	<%}}%>
			        </select>
	       		 </li>
		        
	           	<li><span>二级原因：</span>
			        <select name="changereasonid" id="changereasonid">
			        	<option value ="0">==请选择==</option>
			        </select>
		        </li>
		        
		        
		       
		        <li><span>配送结果备注：</span>
			        <select name="podremarkid" id="podremarkid">
			        	<option value ="-1">==请选择==</option>
			        	<%for(Reason r : podremarkreasonlist){ %>
	           				<option value="<%=r.getReasonid()%>" title="<%=r.getReasoncontent() %>"><%if(r.getReasoncontent()!=null&&r.getReasoncontent().length()>10){ %><%=r.getReasoncontent().substring(0,10) %>...<%}else{ %><%=r.getReasoncontent()%><%} %></option>
	           			<%} %>
			        </select>
		        </li>
		        <li><span>未刷卡原因：</span>
			        <select name="weishuakareasonid" id="weishuakareasonid">
			        	<option value ="0">==请选择==</option>
			        	<%for(Reason r : weishuakareasonlist){ %>
	           				<option value="<%=r.getReasonid()%>"><%if(r.getReasoncontent()!=null&&r.getReasoncontent().length()>10){ %><%=r.getReasoncontent().substring(0,10) %>...<%}else{ %><%=r.getReasoncontent()%><%} %></option>
	           			<%} %>
			        </select>*
		        </li>
		        <li><span>货物丢失原因：</span>
			        <select name="losereasonid" id="losereasonid">
			        	<option value ="0">==请选择==</option>
			        	<%for(Reason r : losereasonlist){ %>
	           				<option value="<%=r.getReasonid()%>"  title="<%=r.getReasoncontent() %>"><%if(r.getReasoncontent()!=null&&r.getReasoncontent().length()>10){ %><%=r.getReasoncontent().substring(0,10) %>...<%}else{ %><%=r.getReasoncontent()%><%} %></option>
	           			<%} %>
			        </select>*
		        </li>
		        <li><span>签收人：</span>
			        <select name="signmanid" id="signmanid" onchange="signmanchange();">
			        	<option value ="1">本人签收</option>
			        	<option value ="2">他人签收</option>
			        </select>*
		        </li>
		        <li><span>实际签收人：</span><input type="text" name="signman" id="signman" value ="<%=deliverystate.getSign_man()%>" maxlength="50"/></li>
		        <li><span>实际签收人手机：</span><input type="text" name="signmanphone" id="signmanphone" value ="" maxlength="50"/></li>
		        <li><span>应<%=(cwborder.getPaybackfee().compareTo(cwborder.getReceivablefee())==1?"退":"收") %>款：</span><%=deliverystate.getBusinessfee() %><input type="hidden" id="shouldfee" value="<%=deliverystate.getBusinessfee()%>"/></li>
		        <li><span>实收款：</span><input type="text" id="infactfee" value="<%=deliverystate.getReceivedfee()%>"/></li>
		        <li><span>退还现金：</span><input type="text" name="returnedfee" id="returnedfee" value ="<%=deliverystate.getReturnedfee()%>" maxlength="50"/></li>
			    <li><span>实收现金：</span><input type="text" name="receivedfeecash" id="receivedfeecash" value ="<%= deliverystate.getCash()%>" onkeyup="weishuakachange();" /></li>
			    <li><span>应收运费：</span><%=deliverystate.getShouldfare() %><input type="hidden" id="shouldfare" value="<%=deliverystate.getShouldfare()%>"/></li>
		        <li><span>实收运费：</span><input type="text" id="infactfare" name="infactfare" value="<%=deliverystate.getInfactfare()%>" maxlength="50"/></li>
			    <%if(showposandqita.equals("yes")){ %>
					<li><span>POS刷卡实收：</span><input type="text" name="receivedfeepos" id="receivedfeepos" value ="<%=deliverystate.getPos()%>" maxlength="50"/><input  id="isforchange" type="button" onclick="forchange();" value="换"/></li>
					<li><span>POS备注：</span><input type="text" name="posremark" id="posremark" value ="<%=deliverystate.getPosremark()%>" maxlength="50"/></li>
					<li><span>其他实收：</span><input type="text" name="receivedfeeother" id="receivedfeeother" value ="<%=deliverystate.getOtherfee()%>" maxlength="50" onkeyup="weishuakachange();" /></li>
				<%}else{ %>
					<p>
					<input type="hidden" name="receivedfeepos" id="receivedfeepos" value ="<%=BigDecimal.ZERO%>" maxlength="50"/>
					<input type="hidden" name="posremark" id="posremark" value="" maxlength="50"/>
					<input type="hidden" name="receivedfeeother" id="receivedfeeother" value ="<%=BigDecimal.ZERO%>" maxlength="50">
					</p>
				<%} %>
		        <li><span>支票实收：</span><input type="text" name="receivedfeecheque" id="receivedfeecheque" value="<%=deliverystate.getCheckfee() %>" maxlength="50" onkeyup="weishuakachange();" /></li>
				<li><span>支票号备注：</span><input type="text" name="checkremark" id="checkremark" value ="<%=deliverystate.getCheckremark()%>" maxlength="50"></li>
		        <li><span>反馈备注输入内容:</span><input type="text" name="deliverstateremark" id="deliverstateremark" value ="<%=deliverystate.getDeliverstateremark()%>" maxlength="50"></li>
		        <li><span>真实反馈时间:</span><input type="text" name="deliverytime" id="deliverytime" value ="<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) %>"  maxlength="19"></li>
		        <li><span>反馈时间格式:</span>2013-09-06 02:07:01</li>
	         </ul>
	         <div align="center">
	         <input type="submit" value="保存" class="button" id="sub" /><br/>
	         </div>
			</form>
		</div>
	</div>
</div>
