<%@page import="cn.explink.domain.CwbStateControl,cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbStateControl> cscList = (List<CwbStateControl>)request.getAttribute("cscList");
int fromstate = (Integer)request.getAttribute("fromstate");

%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改订单状态设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script>
$(function(){
	$("#tostate").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择操作下一环节' });
})

function check_cwbstatecontrol(form){
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {                                                                                                                                                                                                                                      
			location.href='<%=request.getContextPath()%>/cwbStateControl/list';
		}
	});
}
</script>
<div style="background:#f5f5f5">
	<div id="box_in_bg">
		<h2>修改订单流程</h2>
		<form id="cwbstatecontrol_cre_Form" name="cwbstatecontrol_cre_Form"
			 onSubmit="check_cwbstatecontrol(this);return false;" 
			 action="<%=request.getContextPath()%>/cwbStateControl/save/<%=fromstate%>" method="post"  >
			<div id="box_form">
				<ul>
					<li><span>当前环节：</span>
						<%for(FlowOrderTypeEnum fte : FlowOrderTypeEnum.values()){if(fromstate==fte.getValue()){ %>
						<%=fte.getText() %>
						<%}} %>
						<%-- <select id="fromstate" name="fromstate">
							<option value="0" selected>----请选择----</option>
							<option value="<%=FlowOrderTypeEnum.DaoRuShuJu.getValue() %>"  <%if(fromstate==FlowOrderTypeEnum.DaoRuShuJu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.DaoRuShuJu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TiHuo.getValue() %>"  <%if(fromstate==FlowOrderTypeEnum.TiHuo.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.TiHuo.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.RuKu.getValue() %>" <%if(fromstate==FlowOrderTypeEnum.RuKu.getValue()){ %>selected<%}%> ><%=FlowOrderTypeEnum.RuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() %>" <%if(fromstate==FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()){ %>selected<%}%> ><%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() %>"  <%if(fromstate==FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()){ %>selected<%}%> ><%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue() %>" <%if(fromstate==FlowOrderTypeEnum.FenZhanLingHuo.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.FenZhanLingHuo.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() %>" <%if(fromstate==FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue() %>" <%if(fromstate==FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>"  <%if(fromstate==FlowOrderTypeEnum.ChuKuSaoMiao.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.ChuKuSaoMiao.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TuiHuoChuZhan.getValue() %>"  <%if(fromstate==FlowOrderTypeEnum.TuiHuoChuZhan.getValue()){ %>selected<%}%> ><%=FlowOrderTypeEnum.TuiHuoChuZhan.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() %>"  <%if(fromstate==FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() %>"  <%if(fromstate==FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.YiShenHe.getValue() %>"  <%if(fromstate==FlowOrderTypeEnum.YiShenHe.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.YiShenHe.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.YiFanKui.getValue() %>"  <%if(fromstate==FlowOrderTypeEnum.YiFanKui.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.YiFanKui.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.PosZhiFu.getValue() %>"  <%if(fromstate==FlowOrderTypeEnum.PosZhiFu.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.PosZhiFu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.CheXiaoFanKui.getValue() %>"  <%if(fromstate==FlowOrderTypeEnum.CheXiaoFanKui.getValue()){ %>selected<%}%>><%=FlowOrderTypeEnum.CheXiaoFanKui.getText() %></option>
						</select>* --%>
					</li>
	           		<li><span>下一环节：</span>
	           			<select id="tostate"  name="tostate" multiple="multiple"  style="height 30px;width: 500px">
							<option value="<%=FlowOrderTypeEnum.DaoRuShuJu.getValue() %>" <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.DaoRuShuJu.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.DaoRuShuJu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TiHuo.getValue() %>" <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.TiHuo.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.TiHuo.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.RuKu.getValue() %>" <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.RuKu.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.RuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() %>" <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.LanShouDaoHuo.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.LanShouDaoHuo.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.LanShouDaoHuo.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue() %>" <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.FenZhanLingHuo.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.FenZhanLingHuo.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() %>" <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue() %>" <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>" <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.ChuKuSaoMiao.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.ChuKuSaoMiao.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TuiHuoChuZhan.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.TuiHuoChuZhan.getValue()){ %>selected<% break;}}%> ><%=FlowOrderTypeEnum.TuiHuoChuZhan.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() %>" <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() %>" <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.YiShenHe.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.YiShenHe.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.YiShenHe.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.YiFanKui.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.YiFanKui.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.YiFanKui.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.PosZhiFu.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.PosZhiFu.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.PosZhiFu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.CheXiaoFanKui.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.CheXiaoFanKui.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.CheXiaoFanKui.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.UpdateDeliveryBranch.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.UpdateDeliveryBranch.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.UpdatePickBranch.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.UpdateDeliveryBranch.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue() %>"  <%for(CwbStateControl cs : cscList){if(cs.getTostate()==FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()){ %>selected<% break;}}%>><%=FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getText() %></option>
							
						</select>*
					</li>
	         </ul>
		</div>
		<div align="center">
        <input type="submit" value="确认" class="button" id="sub" />
        <input type="button" value="返回" class="button" id="cancel" onclick="location='<%=request.getContextPath()%>/cwbStateControl/list'" /></div>
	</form>
	</div>
</div>
