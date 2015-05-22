<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.domain.Reason,cn.explink.domain.User,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Reason> returnlist = (List<Reason>)request.getAttribute("returnlist");
List<Reason> staylist = (List<Reason>)request.getAttribute("staylist");
List<User> userlist = (List<User>)request.getAttribute("userlist");
Switch pl_switch = (Switch) request.getAttribute("pl_switch");
%>
<script type="text/javascript">

</script>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>批量反馈</h1>
		<div id="box_form2">
				<ul>
					<li>
						<span>小件员：</span>
						<select id="deliverid" name="deliverid">
							<option value="-1">请选择</option>
							<%for(User u :userlist){ %>
								<option value="<%=u.getUserid()%>"><%=u.getRealname()%></option>
							<%} %>
				        </select>*
					</li>
					<li>
						<input type="radio" name="radio" id="radio" value="" onclick="checkpodresultid('paywayid',<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>);"/>配送成功
						<input type="radio" name="radio" id="radio" value="" onclick="checkpodresultid('backreasonid',<%=DeliveryStateEnum.JuShou.getValue()%>);"/>拒收
						<input type="radio" name="radio" id="radio" value="" onclick="checkpodresultid('leavedreasonid',<%=DeliveryStateEnum.FenZhanZhiLiu.getValue()%>);"/>分站滞留
						<input type="hidden" name="podresultid_p" id="podresultid_p" value="0" />
					</li>
					<li style="display: none;">
						<select id="paywayid_p" name="paywayid_p">
							<option value="1">现金</option>
							<%if(pl_switch.getState()!=null&&pl_switch.getState().equals(SwitchEnum.PiLiangFanKuiPOS.getInfo())){ %>
								<option value="2">POS</option>
							<%} %>
							<option value="3">支票</option>
							<option value="4">其他</option>
				        </select>
					</li>
					<li style="display: none;">
						<select id="backreasonid_p" name="backreasonid_p">
							<option value="0" selected>退货原因</option>
							<%for(Reason r1 : returnlist){ %>
								<option value="<%=r1.getReasonid() %>" ><%=r1.getReasoncontent() %></option>
							<%} %>
				        </select>
					</li>
					<li style="display: none;">
						<select id="leavedreasonid_p" name="leavedreasonid_p">
							<option value="0" selected>滞留原因</option>
							<%for(Reason r : staylist){ %>
								<option value="<%=r.getReasonid() %>" ><%=r.getReasoncontent() %></option>
							<%} %>
				        </select>
					</li>
					<li>
						<span>批量扫描单号：</span>
						
					</li>
					<li>
						<textarea rows="2" cols="20" id="scancwb_p" name="scancwb_p" style="width:250px; line-height:15px"></textarea>
					</li>
					<table class="table_ruku">
						<tr>
							<td><h4 id="msg" name="msg" ></h4></td>
						</tr>
						<div style="display: none" id="errorvedio">
						</div>
					</table>
	         </ul>
		</div>
		<div align="center">
        <input onclick='if($("#scancwb_p").val().length>0){deliverpod("<%=request.getContextPath()%>",$("#deliverid").val(),$("#scancwb_p").val(),$("#podresultid_p").val(),$("#paywayid_p").val(),$("#backreasonid_p").val(),$("#leavedreasonid_p").val(),"","Y");}' type="button" value="确定">
        <input type="button" value="清空" onclick='$("#scancwb").val("");'>
        </div>
	</div>
</div>

<div id="box_yy"></div>

