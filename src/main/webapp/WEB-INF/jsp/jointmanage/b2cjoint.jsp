
<%@page import="cn.explink.b2c.tools.power.*"%>
<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="java.util.List"%>
<%@page import="cn.explink.b2c.tools.B2cEnum"%>
<%@ page contentType="text/html; charset=UTF-8"%>


<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>

<%
	List<JointEntity> b2cList = (List<JointEntity>)request.getAttribute("b2cList");
List<JointPower> powerlist = (List<JointPower>)request.getAttribute("b2cenumlist");

	//zhili01.liang on 20160902==添加新的EDI处理接口==Begin===
	StringBuffer ediSysIds = new StringBuffer();
	B2cEnum[] ediEnums = B2cEnum.TYPES_ALL_EDI;
	if(ediEnums!=null){
		for(int i=0;i<ediEnums.length;i++){
			ediSysIds.append(ediEnums[i].getKey());
			if(i<ediEnums.length-1){
				ediSysIds.append(",");
			}
		}
	}
	//zhili01.liang on 20160902==添加新的EDI处理接口==End===
%>

<script type="text/javascript">
function addInit(){
	//无处理
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val(0);
	$("#searchForm").submit();
}
function editInit(){

}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
</script>
</head>
<body style="background: #f5f5f5">
	<div class="menucontant">
		<div class="uc_midbg">
			<ul>
				<li><a href="<%=request.getContextPath()%>/jointpower/">对接权限设置</a></li>
				<li><a href="#" class="light">电商对接</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/jointpos">POS对接</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/poscodemapp/1">POS/商户映射</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/exptreason/1">异常码设置</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/exptcodejoint/1">异常码关联</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/epaiApi/1">系统环形对接</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/encodingsetting/1">供货商编码设置</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/address/1">地址库同步</a></li>
			</ul>
		</div>
		<form action="<%=request.getContextPath()%>/jointManage/jointb2c" method="post" id="searchForm"></form>
		<div class="right_box">
			<div class="right_title">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<tr class="font_1">
						<td width="60%" align="center" valign="middle" bgcolor="#eef6ff">机构名称</td>
						<td width="40%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>

					</tr>
					<%
						for(B2cEnum em:B2cEnum.valuesSortedByText()){ 
																		String text=em.getText();
																		int key=em.getKey();
																		for(JointPower power:powerlist){
																			if(power.getJoint_num()!=key){
																				continue;
																			}
					%>
					<tr>
						<td width="60%" align="center" valign="middle"><%=text%></td>
						<td width="40%" align="center" valign="middle">
							<%
								if(b2cList != null && b2cList.size()>0){
							%> <%
 	for(JointEntity jion : b2cList) {
 %> <%
 	if(jion.getJoint_num() == key){
 %> [<a
							href="javascript:changeUrl('<%=key%>',<%=(jion.getState()==1?0:1)%>);setDelKey('<%=key%>',<%=(jion.getState()==1?0:1)%>);del('<%=key%>');"><font
								id="<%=key%>"><%=(jion.getState()==1?"停用":"启用")%></font></a>] <%
 	} 
   				 } }
 %> [<a href="javascript:changeUrl('<%=key%>','');edit_button('<%=key%>');">设置</a>]
						</td>
					</tr>
					<%
						}
					%>

					<%
						}
					%>
				</table>
			</div>

		</div>

		<div class="jg_10"></div>
		<div class="clear"></div>


		<!-- 修改常用于设置的ajax地址 -->
		<input type="hidden" id="edit" value="<%=request.getContextPath()%>/explinkInterface/show/" /> <input
			type="hidden" id="del" value="<%=request.getContextPath()%>/explinkInterface/del/" />
	</div>
	<script type="text/javascript">
	function setEditKey(key){
		$("#edit").val("<%=request.getContextPath()%>/explinkInterface/show/"+key);
	}
	
	function setDelKey(key,state){
		$("#del").val("<%=request.getContextPath()%>/explinkInterface/del/"+state+"/");
		return true;
	}
</script>

	<!-- 修改常用于设置的ajax地址 -->
	<script>
	function changeUrl(obj,state){
		
		if(obj=='20001'){
			$("#edit").val('<%=request.getContextPath()%>/explinkInterface/show/');
			$("#del").val('<%=request.getContextPath()%>/explinkInterface/del/');
		}else if(obj=='20002'){
			$("#edit").val('<%=request.getContextPath()%>/dangdang/show/');
			$("#del").val('<%=request.getContextPath()%>/dangdang/del/');
		}
		else if(obj=='20005'){
			$("#edit").val('<%=request.getContextPath()%>/tmall/show/');
			$("#del").val('<%=request.getContextPath()%>/tmall/del/');
		}else if(obj=='20006'){
			$("#edit").val('<%=request.getContextPath()%>/jumeiyoupin/show/');
			$("#del").val('<%=request.getContextPath()%>/jumeiyoupin/del/');
		}else if(obj=='20008'||obj=='20057'||obj=='20058'||obj=='20059'){
			$("#edit").val('<%=request.getContextPath()%>/vipshop/show/');
			$("#del").val('<%=request.getContextPath()%>/vipshop/del/');
		}
		else if(obj=='20010'){
			$("#edit").val('<%=request.getContextPath()%>/vipshop/show/');
			$("#del").val('<%=request.getContextPath()%>/vipshop/del/');
		}
		else if(obj=='20011'){
			$("#edit").val('<%=request.getContextPath()%>/vipshop/show/');
			$("#del").val('<%=request.getContextPath()%>/vipshop/del/');
		}
		else if(obj=='20012'){
			$("#edit").val('<%=request.getContextPath()%>/vipshop/show/');
			$("#del").val('<%=request.getContextPath()%>/vipshop/del/');
		}
		else if(obj=='20013'){
			$("#edit").val('<%=request.getContextPath()%>/vipshop/show/');
			$("#del").val('<%=request.getContextPath()%>/vipshop/del/');
		}
		else if(obj=='20015'){
			$("#edit").val('<%=request.getContextPath()%>/smile/show/');
			$("#del").val('<%=request.getContextPath()%>/smile/del/');
		}
		else if(obj=='20009'||obj=='20050'||obj=='20130'||obj=='20131'||obj=='20132'){
			$("#edit").val('<%=request.getContextPath()%>/yihaodian/show/');
			$("#del").val('<%=request.getContextPath()%>/yihaodian/del/');
		}
		else if(obj=='20016'){
			$("#edit").val('<%=request.getContextPath()%>/lefeng/show/');
			$("#del").val('<%=request.getContextPath()%>/lefeng/del/');
		}
		else if(obj=='20017'){
			$("#edit").val('<%=request.getContextPath()%>/yixun/show/');
			$("#del").val('<%=request.getContextPath()%>/yixun/del/');
		}
		else if(obj=='20018'){
			$("#edit").val('<%=request.getContextPath()%>/rufengda/show/');
			$("#del").val('<%=request.getContextPath()%>/rufengda/del/');
		}
		else if(obj=='20019'){
			$("#edit").val('<%=request.getContextPath()%>/vipshop/show/');
			$("#del").val('<%=request.getContextPath()%>/vipshop/del/');
		}
		else if(obj=='20020'){
			$("#edit").val('<%=request.getContextPath()%>/vipshop/show/');
			$("#del").val('<%=request.getContextPath()%>/vipshop/del/');
		}
		else if(obj=='20021'){
			$("#edit").val('<%=request.getContextPath()%>/jingdong/show/');
			$("#del").val('<%=request.getContextPath()%>/jingdong/del/');
		}
		else if(obj=='20022'){
			$("#edit").val('<%=request.getContextPath()%>/gome/show/');
			$("#del").val('<%=request.getContextPath()%>/gome/del/');
		}
		else if(obj=='20024'){
			$("#edit").val('<%=request.getContextPath()%>/yihaodian/show/');
			$("#del").val('<%=request.getContextPath()%>/yihaodian/del/');
		}
		else if(obj=='20025'){
			$("#edit").val('<%=request.getContextPath()%>/tmall/show/');
			$("#del").val('<%=request.getContextPath()%>/tmall/del/');
		}
		else if(obj=='20026'||obj=='20064'|obj=='20066'){
			$("#edit").val('<%=request.getContextPath()%>/tmall/show/');
			$("#del").val('<%=request.getContextPath()%>/tmall/del/');
		}
		else if(obj=='20027'){
			$("#edit").val('<%=request.getContextPath()%>/tmall/show/');
			$("#del").val('<%=request.getContextPath()%>/tmall/del/');
		}
		else if(obj=='20029'){
			$("#edit").val('<%=request.getContextPath()%>/tmall/show/');
			$("#del").val('<%=request.getContextPath()%>/tmall/del/');
		}
		else if(obj=='20028'){
			$("#edit").val('<%=request.getContextPath()%>/dangdangdatasyn/show/');
			$("#del").val('<%=request.getContextPath()%>/dangdangdatasyn/del/');
		}
		else if(obj=='20031'||obj=='20030'){
			$("#edit").val('<%=request.getContextPath()%>/vipshop/show/');
			$("#del").val('<%=request.getContextPath()%>/vipshop/del/');
		}
		else if(obj=='20032'){
			$("#edit").val('<%=request.getContextPath()%>/yangguang/show/');
			$("#del").val('<%=request.getContextPath()%>/yangguang/del/');
		}
		else if(obj=='20033'){
			$("#edit").val('<%=request.getContextPath()%>/tmall/show/');
			$("#del").val('<%=request.getContextPath()%>/tmall/del/');
		}
		else if(obj=='20034'){
			$("#edit").val('<%=request.getContextPath()%>/yemaijiu/show/');
			$("#del").val('<%=request.getContextPath()%>/yemaijiu/del/');
		}
		else if(obj=='20035'){
			$("#edit").val('<%=request.getContextPath()%>/gzabc/show/');
			$("#del").val('<%=request.getContextPath()%>/gzabc/del/');
		}
		else if(obj=='20036'){
			$("#edit").val('<%=request.getContextPath()%>/hzabc/show/');
			$("#del").val('<%=request.getContextPath()%>/hzabc/del/');
		}
		else if(obj=='20039'){
			$("#edit").val('<%=request.getContextPath()%>/dongfangcj/show/');
			$("#del").val('<%=request.getContextPath()%>/dongfangcj/del/');
		}

		else if(obj=='20041'){
			$("#edit").val('<%=request.getContextPath()%>/hxg/show/');
			$("#del").val('<%=request.getContextPath()%>/hxg/del/');
		}

		else if(obj=='20040'){
			$("#edit").val('<%=request.getContextPath()%>/amazon/show/');
			$("#del").val('<%=request.getContextPath()%>/amazon/del/');
		}
		else if(obj=='20042'||obj=='20043'||obj=='20044'||obj=='20045'||obj=='20046'){
			$("#edit").val('<%=request.getContextPath()%>/rufengda/show/');
			$("#del").val('<%=request.getContextPath()%>/rufengda/del/');
		}
		else if(obj=='20047'||obj=='20053'||obj=='20054'||obj=='20055'){
			$("#edit").val('<%=request.getContextPath()%>/dpfoss/show/');
			$("#del").val('<%=request.getContextPath()%>/dpfoss/del/');
		}
		else if(obj=='20049'){
			$("#edit").val('<%=request.getContextPath()%>/homegou/show/');
			$("#del").val('<%=request.getContextPath()%>/homegou/del/');
		}
		else if(obj=='20051'){
			$("#edit").val('<%=request.getContextPath()%>/tmall/show/');
			$("#del").val('<%=request.getContextPath()%>/tmall/del/');
		}
		else if(obj=='20052'){
			$("#edit").val('<%=request.getContextPath()%>/moonbasa/show/');
			$("#del").val('<%=request.getContextPath()%>/moonbasa/del/');
		}else if(obj=='20060'){
			$("#edit").val('<%=request.getContextPath()%>/maikaolin/show/');
			$("#del").val('<%=request.getContextPath()%>/maikaolin/del/');
		}
		else if(obj=='20056'){
			$("#edit").val('<%=request.getContextPath()%>/httx/show/');
			$("#del").val('<%=request.getContextPath()%>/httx/del/');
		}
		else if(obj=='20061'){
			$("#edit").val('<%=request.getContextPath()%>/liantong/show/');
			$("#del").val('<%=request.getContextPath()%>/liantong/del/');
		}else if(obj=='20065'||obj=='20070'){
			$("#edit").val('<%=request.getContextPath()%>/S_huobang/show/');
			$("#del").val('<%=request.getContextPath()%>/S_huobang/del/');
		}
		else if(obj=='20062'){
			$("#edit").val('<%=request.getContextPath()%>/wanxiang/show/');
			$("#del").val('<%=request.getContextPath()%>/wanxiang/del/');
		}else if(obj=='20067'){
			$("#edit").val('<%=request.getContextPath()%>/benlaishenghuo/show/');
			$("#del").val('<%=request.getContextPath()%>/benlaishenghuo/del/');
		}
		else if(obj=='20068'){
			$("#edit").val('<%=request.getContextPath()%>/Jiuxian_wang/show/');
			$("#del").val('<%=request.getContextPath()%>/Jiuxian_wang/del/');
		}
		else if(obj=='20063'){
			$("#edit").val('<%=request.getContextPath()%>/telecom/show/');
			$("#del").val('<%=request.getContextPath()%>/telecom/del/');
		}
		else if(obj=='20069'){
			$("#edit").val('<%=request.getContextPath()%>/address/show/');
			$("#del").val('<%=request.getContextPath()%>/address/del/');
		}else if(obj=='20071'){
			$("#edit").val('<%=request.getContextPath()%>/happyGo/show/');
			$("#del").val('<%=request.getContextPath()%>/happyGo/del/');
		}
		
		else if(obj=='20072'||obj=='20082'){
			$("#edit").val('<%=request.getContextPath()%>/efast/show/');
			$("#del").val('<%=request.getContextPath()%>/efast/del/');
		}
		else if(obj=='20073'){
			$("#edit").val('<%=request.getContextPath()%>/maisike/show/');
			$("#del").val('<%=request.getContextPath()%>/maisike/del/');
		}
		else if(obj=='20074'){//全线快递
			$("#edit").val('<%=request.getContextPath()%>/wholeLine/show/');
			$("#del").val('<%=request.getContextPath()%>/wholeLine/del/');
		}
		else if(obj=='20075'){
			$("#edit").val('<%=request.getContextPath()%>/yhdAddressmatch/show/');
			$("#del").val('<%=request.getContextPath()%>/yhdAddressmatch/del/');
		}
		else if(obj=='20076'){
			$("#edit").val('<%=request.getContextPath()%>/mmb/show/');
			$("#del").val('<%=request.getContextPath()%>/mmb/del/');
		}
		else if(obj=='20077'){
			$("#edit").val('<%=request.getContextPath()%>/chinamobile/show/');
			$("#del").val('<%=request.getContextPath()%>/chinamobile/del/');
		}
		else if(obj=='20078'){
			$("#edit").val('<%=request.getContextPath()%>/letv/show/');
			$("#del").val('<%=request.getContextPath()%>/letv/del/');
		}
		else if(obj=='20079'){
			$("#edit").val('<%=request.getContextPath()%>/yonghuics/show/');
			$("#del").val('<%=request.getContextPath()%>/yonghuics/del/');
		}
		else if(obj=='20080'){
			$("#edit").val('<%=request.getContextPath()%>/hxgdms/show/');
			$("#del").val('<%=request.getContextPath()%>/hxgdms/del/');
		}
		else if(obj=='20081'){
			$("#edit").val('<%=request.getContextPath()%>/sfexpress/show/');
			$("#del").val('<%=request.getContextPath()%>/sfexpress/del/');
		}
		else if(obj=='20083'){
			$("#edit").val('<%=request.getContextPath()%>/wangjiu/show/');
			$("#del").val('<%=request.getContextPath()%>/wangjiu/del/');
		}
		else if(obj=='20084'){
			$("#edit").val('<%=request.getContextPath()%>/homegobj/show/');
			$("#del").val('<%=request.getContextPath()%>/homegobj/del/');
		}

		else if(obj=='20085'){
			$("#edit").val('<%=request.getContextPath()%>/lechong/show/');
			$("#del").val('<%=request.getContextPath()%>/lechong/del/');
		}

		else if(obj=='20087'){
			$("#edit").val('<%=request.getContextPath()%>/sfxhm/show/');
			$("#del").val('<%=request.getContextPath()%>/sfxhm/del/');
		}
		else if(obj=='20086'){
			$("#edit").val('<%=request.getContextPath()%>/smiled/show/');
			$("#del").val('<%=request.getContextPath()%>/smiled/del/');
		}

		

		else if(obj=='20088'||obj=='21001'||obj=='21002'){
			$("#edit").val('<%=request.getContextPath()%>/zhongliang/show/');
			$("#del").val('<%=request.getContextPath()%>/zhongliang/del/');
		}
		else if(obj=='20089'){
			$("#edit").val('<%=request.getContextPath()%>/wenxuan/show/');
			$("#del").val('<%=request.getContextPath()%>/wenxuan/del/');
			
		}else if(obj=='20093'){
			$("#edit").val('<%=request.getContextPath()%>/lefengdms/show/');
			$("#del").val('<%=request.getContextPath()%>/lefengdms/del/');
			
		}else if(obj=='20095'){
			$("#edit").val('<%=request.getContextPath()%>/gztl/show/');
			$("#del").val('<%=request.getContextPath()%>/gztl/del/');
		}else if(obj=='20096'){
			$("#edit").val('<%=request.getContextPath()%>/gztlfeedback/show/');
			$("#del").val('<%=request.getContextPath()%>/gztlfeedback/del/');
		}
		
		else if(obj=='20090'){
			$("#edit").val('<%=request.getContextPath()%>/vipshopOXO/show/');
			$("#del").val('<%=request.getContextPath()%>/vipshopOXO/del/');
		}
		else if(obj=='20101'||obj=='20102'||obj=='20103'||obj=='20104'||obj=='20105'){
			$("#edit").val('<%=request.getContextPath()%>/jiuye/show/');
			$("#del").val('<%=request.getContextPath()%>/jiuye/del/');
		}
		else if(obj=='20107'){
			$("#edit").val('<%=request.getContextPath()%>/feiniuwang/show/');
			$("#del").val('<%=request.getContextPath()%>/feiniuwang/del/');
		}
		else if(obj=='20108'){
			$("#edit").val('<%=request.getContextPath()%>/yonghui/show/');
			$("#del").val('<%=request.getContextPath()%>/yonghui/del/');
		}
		
		else if(obj=='20109'||obj=='20110'||obj=='20111'||obj=='20112'||obj=='20113'){
			$("#edit").val('<%=request.getContextPath()%>/jiuyeaddressmatch/show/');
			$("#del").val('<%=request.getContextPath()%>/jiuyeaddressmatch/del/');
		}
		else if(obj=='20117'){
			$("#edit").val('<%=request.getContextPath()%>/zhemeng/show/');
			$("#del").val('<%=request.getContextPath()%>/zhemeng/del/');
		}
		
		else if(obj=='20118'){
			$("#edit").val('<%=request.getContextPath()%>/fyAddress/show/');
			$("#del").val('<%=request.getContextPath()%>/fyAddress/del/');
		}
		
		else if(obj=='20120'){
			$("#edit").val('<%=request.getContextPath()%>/haoyigou/show/');
			$("#del").val('<%=request.getContextPath()%>/haoyigou/del/');
		}
		
		else if(obj=='20121'){
			$("#edit").val('<%=request.getContextPath()%>/gxdxAddress/show/');
			$("#del").val('<%=request.getContextPath()%>/gxdxAddress/del/');
		}
		
		else if(obj=='20122'||obj=='20123'||obj=='20124'||obj=='20125'||obj=='20126'||obj.substr(0,2)=='29'){
			$("#edit").val('<%=request.getContextPath()%>/vipshop/show/');
			$("#del").val('<%=request.getContextPath()%>/vipshop/del/');
		}

        else if(obj=='20129'){ //外单推DO
			$("#edit").val('<%=request.getContextPath()%>/thirdPartyOrder2DO/show/');
			$("#del").val('<%=request.getContextPath()%>/thirdPartyOrder2DO/del/');
		}

       else if(obj=='20127'){
			$("#edit").val('<%=request.getContextPath()%>/suning/show/');
			$("#del").val('<%=request.getContextPath()%>/suning/del/');
		}

		else if(obj=='20227'){
			$("#edit").val('<%=request.getContextPath()%>/tpsAutomate/show/');
			$("#del").val('<%=request.getContextPath()%>/vipshop/del/');
		}
		else if(obj=='20228'){//唯品会_TPS_运单状态
			$("#edit").val('<%=request.getContextPath()%>/tpsCarrierOrderStatus/show/');
			$("#del").val('<%=request.getContextPath()%>/tpsCarrierOrderStatus/del/');

		}else if(obj=='20200'){
			$("#edit").val('<%=request.getContextPath()%>/tonglian/show/');
			$("#del").val('<%=request.getContextPath()%>/tonglian/del/');
		}else if(obj=='20136'){
			$("#edit").val('<%=request.getContextPath()%>/huanqiugou/show/');
			$("#del").val('<%=request.getContextPath()%>/huanqiugou/del/');
		}else if(obj=='20201'){
			$("#edit").val('<%=request.getContextPath()%>/caifutong/show/');
			$("#del").val('<%=request.getContextPath()%>/caifutong/del/');
		}
		else if(obj=='20127'){
			$("#edit").val('<%=request.getContextPath()%>/suning/show/');
			$("#del").val('<%=request.getContextPath()%>/suning/del/');
		}
		
		else if(obj=='20135'){
			$("#edit").val('<%=request.getContextPath()%>/meilinkai/show/');
			$("#del").val('<%=request.getContextPath()%>/meilinkai/del/');
		}else if(obj=='20200'){
			$("#edit").val('<%=request.getContextPath()%>/tonglian/show/');
			$("#del").val('<%=request.getContextPath()%>/tonglian/del/');
		}else if(obj=='20136'){
			$("#edit").val('<%=request.getContextPath()%>/huanqiugou/show/');
			$("#del").val('<%=request.getContextPath()%>/huanqiugou/del/');
		}else if(obj=='20201'){
			$("#edit").val('<%=request.getContextPath()%>/caifutong/show/');
			$("#del").val('<%=request.getContextPath()%>/caifutong/del/');
		}

		else if(obj=='20140'){
			$("#edit").val('<%=request.getContextPath()%>/pinhaohuo/show/');
			$("#del").val('<%=request.getContextPath()%>/pinhaohuo/del/');
		}
		
		else if(obj=='22001'){//京东_订单跟踪接口
			$("#edit").val('<%=request.getContextPath()%>/jdCwbTrack/show/');
			$("#del").val('<%=request.getContextPath()%>/jdCwbTrack/del/');
		}
		else if(obj=='22010'){//EMS接口
			$("#edit").val('<%=request.getContextPath()%>/ems/show/');
			$("#del").val('<%=request.getContextPath()%>/ems/del/');
		}
		
		else if(obj=='20229'){
			$("#edit").val('<%=request.getContextPath()%>/tpsCwbFlow/show/');
			$("#del").val('<%=request.getContextPath()%>/tpsCwbFlow/del/');
		}
		
		else if(obj=='20137'){//神州数码
			$("#edit").val('<%=request.getContextPath()%>/shenzhoushuma/show/');
			$("#del").val('<%=request.getContextPath()%>/shenzhoushuma/del/');
		}
		else if(obj=='22501'){//TPS订单下发接口
            $("#edit").val('<%=request.getContextPath()%>/tPSMQ/show/');
            $("#del").val('<%=request.getContextPath()%>/vipshop/del/');
        }
		else if(obj=='22502'){//TPS快递单下发接口
            $("#edit").val('<%=request.getContextPath()%>/tPSMQExpress/show/');
            $("#del").val('<%=request.getContextPath()%>/vipshop/del/');
        }
		else if(obj=='22503'){//跨境购单下发接口
            $("#edit").val('<%=request.getContextPath()%>/kuaJingGou/show/');
            $("#del").val('<%=request.getContextPath()%>/vipshop/del/');
        }
		/******************************add start**********************************************/
		//add by zhouhuan 七乐康接口配置 2016-07-22
		else if(obj=='22504'){//七乐康
            $("#edit").val('<%=request.getContextPath()%>/qiLeKang/show/');
            $("#del").val('<%=request.getContextPath()%>/vipshop/del/');
        }
		else if(obj=='22505'){//大家分
            $("#edit").val('<%=request.getContextPath()%>/daJiaFen/show/');
            $("#del").val('<%=request.getContextPath()%>/vipshop/del/');
        }
		/********************************add end**********************************************/
		else if(obj=='23001'){//哲盟_轨迹
			$("#edit").val('<%=request.getContextPath()%>/zhemengTrack/show/');
			$("#del").val('<%=request.getContextPath()%>/zhemengTrack/del/');
		}
		
		else if(obj=='22601'){ //订单轨迹推送给tps
            $("#edit").val('<%=request.getContextPath()%>/orderTraceToTPS/show/');
            $("#del").val('<%=request.getContextPath()%>/thirdPartyOrder2DO/del/');
        }
		else if(obj=='20230'){
			$("#edit").val('<%=request.getContextPath()%>/tpsAutoFlowCfg/show/');
			$("#del").val('<%=request.getContextPath()%>/tpsAutoFlowCfg/del/');
		}
		else if(obj=='22506'){//麦考林-TPS
            $("#edit").val('<%=request.getContextPath()%>/tpsMaikaolin/show/');
            $("#del").val('<%=request.getContextPath()%>/vipshop/del/');
        }
		else if(obj=='22507'){//本来生活-TPS
            $("#edit").val('<%=request.getContextPath()%>/tpsBenlaishenghuo/show/');
            $("#del").val('<%=request.getContextPath()%>/vipshop/del/');
        }
		else if(obj=='22508'){//微特派-TPS
            $("#edit").val('<%=request.getContextPath()%>/tpsWeitepai/show/');
            $("#del").val('<%=request.getContextPath()%>/vipshop/del/');
        }
		else if(obj=='22509'){//天联-TPS
            $("#edit").val('<%=request.getContextPath()%>/tpsTianlian/show/');
            $("#del").val('<%=request.getContextPath()%>/vipshop/del/');
        }
		//zhili01.liang on 20160902==添加新的EDI处理接口==Begin===
		else if(isNewEDIId(obj)){
            $("#edit").val('<%=request.getContextPath()%>/tpsMQOrderSetting/show/');
            $("#del").val('<%=request.getContextPath()%>/vipshop/del/');
        }
		//zhili01.liang on 20160902==添加新的EDI处理接口==End===
		else if(obj=='22565'){//PJD电子签名图片
            $("#edit").val('<%=request.getContextPath()%>/pjdsignimgcfg/show/');
            $("#del").val('<%=request.getContextPath()%>/pjdsignimgcfg/del/');
        }else if(obj=="21005"){
        	$("#edit").val('<%=request.getContextPath()%>/mss/show/');
            $("#del").val('<%=request.getContextPath()%>/mss/del/');
        }else if(obj=='22566'){
			$("#edit").val('<%=request.getContextPath()%>/liantongOrderCenter/show/');
			$("#del").val('<%=request.getContextPath()%>/liantongOrderCenter/del/');
		}
		else{
			$("#edit").val('<%=request.getContextPath()%>/explinkInterface/show/');
			$("#del").val('<%=request.getContextPath()%>/explinkInterface/del/');
		}
	}
	

	//zhili01.liang on 20160902==添加新的EDI处理接口==Begin===
	function isNewEDIId(id){
		var ids = '<%= ediSysIds.toString() %>'
		var idsArray = ids.split(",");
		for(var i=0;i<idsArray.length;i++){
			if(id==idsArray[i]){
				return true;
			}
		}
		return false;
	}
	//zhili01.liang on 20160902==添加新的EDI处理接口==End===

	</script>
</body>
</html>
