<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import=" cn.explink.domain.Reason"%>
<%@page import=" net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.express.ExpressFeedBackTongjiEnum" %>
<%@page import="cn.explink.domain.express.ExpressPreOrder"%>
<%@page import="cn.explink.enumutil.express.ExcuteStateEnum"%>
<%@page import="cn.explink.domain.VO.express.ExpressFeedBackView"%>


<%
ExpressFeedBackView feedBackView = (ExpressFeedBackView)request.getAttribute("feedBackView")==null?new ExpressFeedBackView(): (ExpressFeedBackView)request.getAttribute("feedBackView");
String feedBackTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(feedBackView.getFeedbackTime()==null?new Date():feedBackView.getFeedbackTime());
String nextPickTime = feedBackView.getNextPickTime()==null?"": new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(feedBackView.getNextPickTime());
String feedBackRemark = feedBackView.getFeedbackRemark()==null?"":feedBackView.getFeedbackRemark();
String transNo = feedBackView.getOrderNo()==null?"":feedBackView.getOrderNo();
ExpressPreOrder preOrder = (ExpressPreOrder)request.getAttribute("preOrder");

Long executeStateType = (Long)session.getAttribute("executeState");

List<Reason> pickFailedReason = (List<Reason>)request.getAttribute("pickFailedReason");
List<Reason> areaWrongReason = (List<Reason>)request.getAttribute("areaWrongReason");
List<Reason> pickWrongReason = (List<Reason>)request.getAttribute("pickWrongReason");
List<Reason> pickDelayReason = (List<Reason>)request.getAttribute("pickDelayReason");
%>

<script>
var initFeedBoxEditArray = new Array();

initFeedBoxEditArray[0]="<%=feedBackView.getExcuteState()%>,pickResultId";

if(<%=feedBackView.getExcuteState()%>==<%=ExcuteStateEnum.DelayedEmbrace.getValue()%>){//揽件延迟
	
	initFeedBoxEditArray[1]="<%=feedBackView.getFeedbackFirstReasonId() %>,pickDelayFirstLevel";
	initFeedBoxEditArray[2]="<%=feedBackView.getFeedbackSecondReasonId() %>,pickDelaySecondLevel";
	initFeedBoxEditArray[3]="<%=nextPickTime %>,nextPickExpressTime";
	initFeedBoxEditArray[4]="<%=feedBackTimeStr %>,feedBackTime";
	initFeedBoxEditArray[5]="<%=feedBackRemark %>,feedBackRemark";
}else if(<%=feedBackView.getExcuteState()%>==<%=ExcuteStateEnum.fail.getValue()%>){//揽件失败
	
	initFeedBoxEditArray[1]="<%=feedBackView.getFeedbackFirstReasonId() %>,pickFailedFirstLevel";
	initFeedBoxEditArray[2]="<%=feedBackView.getFeedbackSecondReasonId() %>,pickFailedSecondLevel";
	initFeedBoxEditArray[3]="<%=feedBackTimeStr %>,feedBackTime";
	initFeedBoxEditArray[4]="<%=feedBackRemark %>,feedBackRemark";
}else if(<%=feedBackView.getExcuteState()%>==<%=ExcuteStateEnum.StationSuperzone.getValue()%>){//站点超区
	
	initFeedBoxEditArray[1]="<%=feedBackView.getFeedbackFirstReasonId() %>,areaWrongFirstLevel";
	initFeedBoxEditArray[2]="<%=feedBackView.getFeedbackSecondReasonId() %>,areaWrongSecondLevel";
	initFeedBoxEditArray[3]="<%=feedBackTimeStr %>,feedBackTime";
	initFeedBoxEditArray[4]="<%=feedBackRemark %>,feedBackRemark";
}else if(<%=feedBackView.getExcuteState()%>==<%=ExcuteStateEnum.EmbraceSuperzone.getValue()%>){//揽件超区
	initFeedBoxEditArray[1]="<%=feedBackView.getFeedbackFirstReasonId() %>,pickWrongFirstLevel";
	initFeedBoxEditArray[2]="<%=feedBackView.getFeedbackSecondReasonId() %>,pickWrongSecondLevel";
	initFeedBoxEditArray[3]="<%=feedBackTimeStr %>,feedBackTime";
	initFeedBoxEditArray[4]="<%=feedBackRemark %>,feedBackRemark";
}else{
	initFeedBoxEditArray[1]="<%=transNo %>,transNo";
	initFeedBoxEditArray[2]="<%=feedBackTimeStr %>,feedBackTime";
	initFeedBoxEditArray[3]="<%=feedBackRemark %>,feedBackRemark";
}


function editInit(){
	for(var i =0 ; i < initFeedBoxEditArray.length ; i ++){
		var value = initFeedBoxEditArray[i].split(",")[0];
		var name = initFeedBoxEditArray[i].split(",")[1];
		if(name.indexOf("SecondLevel")>0&&parseInt(value)>0){
			var firstName = name.replace('Second','First');
			var value_temp = $("#"+firstName,parent.document).val();
			var URL = "<%=request.getContextPath()%>/reason/getSecondreason";
			$.ajax({
				url : URL, // 后台处理程序
				type : "POST",// 数据发送方式
				async: false, //设为false就是同步请求
				data : {
					firstreasonid : value_temp
				},
				dataType : 'json',// 接受数据格式
				success : function(json) {
					$("#"+name,parent.document).empty();// 清空下拉框//$("#select").html('');
					var html = "<option value ='0'>==请选择==</option>";
					for (var j = 0; j < json.length; j++) {
						var valueId = (json[j].reasonid);//.replace(/(^\s*)|(\s*$)/g, "")
						html += "<option value='"+valueId+"'>" + json[j].reasoncontent + "</option>";
					}
					$("#"+name,parent.document).append(html);		
				}
			});
		}
		$("#"+name, parent.document).val(value);
	}

	initDialogBox();
	
	window.parent.click_pickResult(<%=feedBackView.getExcuteState()%>,
			<%=ExcuteStateEnum.DelayedEmbrace.getValue()%>,
			<%=ExcuteStateEnum.Succeed.getValue()%>,
   			<%=ExcuteStateEnum.fail.getValue()%>,
   			<%=ExcuteStateEnum.StationSuperzone.getValue()%>,
   			<%=ExcuteStateEnum.EmbraceSuperzone.getValue() %>,false);
	$("input[type='text']", parent.document).focus(function(){
		$(this).select();
	});
}
</script>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
	<h1><div id="close_box" onclick="closeBox()"></div>修改反馈结果</h1>
	<div id="box_form"  style="overflow:auto;width:400px;">
	
	<form id="feedBackState_Save_Form" name="feedBackState_Save_Form"  onSubmit="if(checkFeedBackStateContent(<%=ExcuteStateEnum.DelayedEmbrace.getValue()%>,
			<%=ExcuteStateEnum.Succeed.getValue()%>,
			<%=ExcuteStateEnum.fail.getValue()%>,
			<%=ExcuteStateEnum.StationSuperzone.getValue()%>,
			<%=ExcuteStateEnum.EmbraceSuperzone.getValue() %>)){
				$('#sub').attr('disabled','disabled');submitSaveFormAndCloseBox(this);$('#sub').val('处理中...');
			}return false;" 
			 action="<%=request.getContextPath()%>/expressFeedback/editFeedBackState?" method="post"  >
			 	<input type="hidden" id="preOrderNoEdit" name="preOrderNoEdit" value="<%=preOrder.getPreOrderNo() %> "/>
			 	<input type="hidden" id="deliveryEditId" name="deliveryEditId" value="<%=feedBackView.getDelivermanId() %> "/>
				<ul>
					<li><span>预订单编号：</span><%=preOrder.getPreOrderNo() %></li>
					<li><span>订单类型：</span>快递</li>
					<li><span>小件员姓名：</span><%=preOrder.getDelivermanName() %></li>
					<li><span>揽收结果：</span> 
						<!-- 揽收结果 -->
						<select id ="pickResultId" name ="pickResultId" onchange="click_pickResult(<%=feedBackView.getExcuteState()%>,
									<%=ExcuteStateEnum.DelayedEmbrace.getValue()%>,
									<%=ExcuteStateEnum.Succeed.getValue()%>,
						   			<%=ExcuteStateEnum.fail.getValue()%>,
						   			<%=ExcuteStateEnum.StationSuperzone.getValue()%>,
						   			<%=ExcuteStateEnum.EmbraceSuperzone.getValue() %>,true);">
		                   		<option value ="-1">==请选择==</option>
		                   		<option value ="<%=ExcuteStateEnum.DelayedEmbrace.getValue()%>"><%=ExcuteStateEnum.DelayedEmbrace.getText() %></option>
		                   		<option value ="<%=ExcuteStateEnum.fail.getValue()%>"><%=ExcuteStateEnum.fail.getText() %></option>
		                   		<%-- <option value ="<%=ExcuteStateEnum.StationSuperzone.getValue()%>"><%=ExcuteStateEnum.StationSuperzone.getText() %></option> --%>
		                   		<option value ="<%=ExcuteStateEnum.EmbraceSuperzone.getValue()%>"><%=ExcuteStateEnum.EmbraceSuperzone.getText() %></option>
		                   		<option value ="<%=ExcuteStateEnum.Succeed.getValue()%>"><%=ExcuteStateEnum.Succeed.getText() %></option>
		                </select>*
	                </li>
	                
                <li><span>运单号：</span><input type="text" name="transNo" id="transNo" value ="" maxlength="50"/>*</li>
                
		        <!-- 揽收失败 -->
	           	<li><span>一级原因：</span>
			        <select name="pickFailedFirstLevel" id="pickFailedFirstLevel"  onchange="getSecondReasonByFirstreasonid('<%=request.getContextPath()%>/reason/getSecondreason','pickFailedSecondLevel',this.value);"  >
			        	<option value ="0">==请选择==</option>
			        	<%
			        	if(pickFailedReason!=null&&pickFailedReason.size()>0){
			        	for(Reason r :pickFailedReason){
			        		if(r.getWhichreason()!=1){
			        			continue;
			        		}
			        	%>
			        		<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			        	<%}}%>
			        </select>
		        </li>
		        
	           	<li><span>二级原因：</span>
			        <select name="pickFailedSecondLevel" id="pickFailedSecondLevel">
			        	<option value ="0">==请选择==</option>
			        </select>
		        </li>
                
		        <!-- 站点超区 -->
		       <%--  
	           	<li><span>一级原因：</span>
			        <select name="areaWrongFirstLevel" id="areaWrongFirstLevel"  onchange="getSecondReasonByFirstreasonid('<%=request.getContextPath()%>/reason/getSecondreason','areaWrongSecondLevel',this.value);"  >
			        	<option value ="0">==请选择==</option>
			        	<%
			        	if(areaWrongReason!=null&&areaWrongReason.size()>0){
			        	for(Reason r :areaWrongReason){
			        		if(r.getWhichreason()!=1){
			        			continue;
			        		}
			        	%>
			        		<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			        	<%}}%>
			        </select>
		        </li>
		        
	           	<li><span>二级原因：</span>
			        <select name="areaWrongSecondLevel" id="areaWrongSecondLevel">
			        	<option value ="0">==请选择==</option>
			        </select>
		        </li>
                 --%>
                
		        <!-- 揽收超区 -->
		        
	           	<li><span>一级原因：</span>
			        <select name="pickWrongFirstLevel" id="pickWrongFirstLevel"  onchange="getSecondReasonByFirstreasonid('<%=request.getContextPath()%>/reason/getSecondreason','pickWrongSecondLevel',this.value);"  >
			        	<option value ="0">==请选择==</option>
			        	<%
			        	if(pickWrongReason!=null&&pickWrongReason.size()>0){
			        	for(Reason r :pickWrongReason){
			        		if(r.getWhichreason()!=1){
			        			continue;
			        		}
			        	%>
			        		<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			        	<%}}%>
			        </select>
		        </li>
		        
	           	<li><span>二级原因：</span>
			        <select name="pickWrongSecondLevel" id="pickWrongSecondLevel">
			        	<option value ="0">==请选择==</option>
			        </select>
		        </li>
		        
                <!-- 延迟揽件 -->
	           	<li><span>一级原因：</span>
			        <select name="pickDelayFirstLevel" id="pickDelayFirstLevel"  onchange="getSecondReasonByFirstreasonid('<%=request.getContextPath()%>/reason/getSecondreason','pickDelaySecondLevel',this.value);"  >
			        	<option value ="0">==请选择==</option>
			        	<%
			        	if(pickDelayReason!=null&&pickDelayReason.size()>0){
			        	for(Reason r :pickDelayReason){
			        		if(r.getWhichreason()!=1){
			        			continue;
			        		}
			        	%>
			        		<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			        	<%}}%>
			        </select>
		        </li>
		        
	           	<li><span>二级原因：</span>
			        <select name="pickDelaySecondLevel" id="pickDelaySecondLevel">
			        	<option value ="0">==请选择==</option>
			        </select>
		        </li>
                <li><span>预计下次揽件时间：</span><input type="text" name="nextPickExpressTime" id="nextPickExpressTime" onclick="WdatePicker({'readOnly':true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate">*</li>
		        <li><span>反馈备注输入内容：</span><input type="text" name="feedBackRemark" id="feedBackRemark" value ="" maxlength="50"></li>
		        <li><span>反馈时间：</span><label type="text" name="feedBackTime" id="feedBackTime"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) %></label></li>
	         </ul>
	         <div align="center">
	         	<input type="submit" value="保存" class="button" id="sub" /><br/>
	         </div>
			</form>
		</div>
	</div>
</div>

