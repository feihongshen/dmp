<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.domain.CwbALLStateControl,cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbALLStateControl> csList = (List<CwbALLStateControl>)request.getAttribute("csList");
int cwbstate = (Integer)request.getAttribute("cwbstate");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
	$("#toflowtype").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择可操作的环节' });
})
function buttonSave(form){
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			location.href='<%=request.getContextPath()%>/cwbAllStateControl/list';
		}
	});
}
</script>
<div style="background:#f5f5f5">
	<div id="box_in_bg">
		<h1>修改订单状态设置</h1>
		<form onSubmit="buttonSave(this);return false;" action="<%=request.getContextPath()%>/cwbAllStateControl/save/<%=cwbstate%>" method="post"  >
			<div id="box_form">
				<ul>
					<li><span>当前状态：</span>
							<%for(CwbStateEnum f : CwbStateEnum.values()){ %>
								<%if(cwbstate==f.getValue()){ %>
								<%=f.getText() %>
							<%}} %>
					</li>
	           		<li><span>可操作的环节：</span>
	           			<select id="toflowtype"  name="toflowtype" multiple="multiple"  style="height 30px;width: 500px">
							<option value="<%=FlowOrderTypeEnum.DaoRuShuJu.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.DaoRuShuJu.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.DaoRuShuJu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TiHuo.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.TiHuo.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.TiHuo.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.RuKu.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.RuKu.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.RuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.ChuKuSaoMiao.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.ChuKuSaoMiao.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() %>"  <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.FenZhanLingHuo.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.FenZhanLingHuo.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.DaoCuoHuoChuLi.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getText() %></option>
						    <option value="<%=FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TuiHuoChuZhan.getValue() %>"  <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.TuiHuoChuZhan.getValue()){ %>selected<%break;}}%> ><%=FlowOrderTypeEnum.TuiHuoChuZhan.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.YiFanKui.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.YiFanKui.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.YiFanKui.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.YiShenHe.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.YiShenHe.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.YiShenHe.getText() %></option>
							<option value="<%=FlowOrderTypeEnum.PosZhiFu.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.PosZhiFu.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.PosZhiFu.getText() %></option>
						    <option value="<%=FlowOrderTypeEnum.CheXiaoFanKui.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.CheXiaoFanKui.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.CheXiaoFanKui.getText() %></option>
						    <option value="<%=FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue() %>" <%for(CwbALLStateControl cs : csList){if(cs.getToflowtype()==FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()){ %>selected<%break;}}%>><%=FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getText() %></option>
						</select>*
					</li>
	         </ul>
		</div>
		<div align="center">
        <input type="submit" value="确认" class="button" id="sub" />　
        <input type="button" value="返回" class="button" id="cancel" onclick="location='<%=request.getContextPath()%>/cwbAllStateControl/list'" /></div>
	</form>
	</div>
</div>


