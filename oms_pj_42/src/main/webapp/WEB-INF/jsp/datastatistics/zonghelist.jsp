<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
  List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
  String [] customeridArray =(String[]) request.getAttribute("customeridStr");
  String [] dispatchbranchidArray =(String[]) request.getAttribute("dispatchbranchidArray");
  String [] curdispatchbranchidArray =(String[]) request.getAttribute("curdispatchbranchidArray");
  String [] nextdispatchbranchidArray =(String[]) request.getAttribute("nextdispatchbranchidArray");
  String [] cwbordertypeidArray =(String[]) request.getAttribute("cwbordertypeidStr");
  List operationOrderResultTypeList =(List) request.getAttribute("operationOrderResultTypeStr");
  
  String starttime=request.getAttribute("begindate")==null?"":(String)request.getAttribute("begindate");
  String endtime=request.getAttribute("enddate")==null?"":(String)request.getAttribute("enddate");
  long count = (Long)request.getAttribute("count");
  Page page_obj = (Page)request.getAttribute("page_obj");
  List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
  List<CwbOrderTail> orderlist = (List<CwbOrderTail>)request.getAttribute("orderlist");
  Integer isaudit=Integer.parseInt((String)request.getAttribute("isaudit"));
  Map <Long,String> brachmap=( Map <Long,String> )request.getAttribute("brachmap");
  Map <Long,String> customermap=( Map <Long,String> )request.getAttribute("customermap");
  
  Integer financeAuditStatus =(Integer)request.getAttribute("financeAuditStatus");
  Integer goodsType =(Integer)request.getAttribute("goodsType");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>综合查询</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js"
	type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet"
	type="text/css" />
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	   //获取下拉框的值
	   $("#find").click(function(){
	         if(check()){
	        	 /*if($(":checked[name=dispatchbranchid]").length==0){
		        	 multiSelectAll('dispatchbranchid',1,'请选择');}
	        	  if($(":checked[name=curdispatchbranchid]").length==0){
		        	 multiSelectAll('curdispatchbranchid',1,'请选择');} */
	        	/*  if($(":checked[name=nextdispatchbranchid]").length==0){
		        	 multiSelectAll('nextdispatchbranchid',1,'请选择');} */
		    	$("#find").attr("disabled","disabled");
				$("#find").val("请稍等..");
		    	$("#searchForm").submit();
	         }
	   });
});

function check(){
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if($("#endtime").val()>"<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"){
		$("#endtime").val("<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>");
	}
	return true;
}

$(function() {
	$("#strtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
		timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择供货商' });
	$("#dispatchbranchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择配送站点' });
	$("#curdispatchbranchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择当前操作站点' });
	$("#nextdispatchbranchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择下一站点' });
	$("#cwbordertypeid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
	$("#operationOrderResultType").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
});
function searchFrom(){
	$('#searchForm').attr('action',1);return true;
}
function delSuccess(data){
	$("#searchForm").submit();
}

function clearSelect(){
	$("#strtime").val('');//开始时间
	$("#endtime").val('');//结束时间
	$("#startbranchid").val(-1);//站点名称
	$("#isaudit").val(-1);//审核状态
	$("#cwd_id").val('');//订单编号
	$("#paytype").val(-1);//支付类型
	$("#curpaytype").val(-1);//当前支付类型
	$("#cwbstate").val(-1);//订单状态
	$("#curquerytimetype").val('<%=""+FlowOrderTypeEnum.DaoRuShuJu.getValue()%>');//
	$("#branchname").val('输入站点名称自动匹配');//
	$("#curbranchname").val('输入站点名称自动匹配');//
	$("#nextbranchname").val('输入站点名称自动匹配');//
	$("#financeAuditStatus").val(-1);
	$("#goodsType").val(-1);
	multiSelectAll('customerid',0,'请选择供货商');
	multiSelectAll('cwbordertypeid',0,'请选择');
	multiSelectAll('dispatchbranchid',0,'请选择配送站点');
	multiSelectAll('curdispatchbranchid',0,'请选择当前操作站点');
	multiSelectAll('nextdispatchbranchid',0,'请选择下一站点');
	multiSelectAll('operationOrderResultType',0,'请选择');
	
}
</script>
</head>


<body style="background: #fff" marginwidth="0" marginheight="0">
	<div class="inputselect_box" style="top: 0px">
		<form action="<%=request.getContextPath()%>/datastatistics/zonghechaxun/1" method="post"
			id="searchForm">
			<input type="hidden" id="isshow" name="isshow" value="1" /> <input type="hidden" name="page"
				value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page")%>" />
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
				<tbody>
					<tr>
						<td align="left">供货商：<select name="customerid" id="customerid" multiple="multiple"
							style="width: 120px;">
								<%
									for(Customer c : customerlist){
								%>
								<option value="<%=c.getCustomerid()%>"
									<%if(customeridArray!=null&&customeridArray.length>0) 
			            {for(int i=0;i<customeridArray.length;i++){
			            	if(c.getCustomerid()== new Long(customeridArray[i])){%>
									selected="selected" <%break;
			            	}
			            }
				     }%>><%=c.getCustomername()%></option>
								<%
									}
								%>
						</select> [<a href="javascript:multiSelectAll('customerid',1,'请选择');">全选</a>] [<a
							href="javascript:multiSelectAll('customerid',0,'请选择');">取消全选</a>]&nbsp;&nbsp; 订单类型： <select
							name="cwbordertypeid" id="cwbordertypeid" multiple="multiple">
								<%
									for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){
								%>
								<option value="<%=c.getValue()%>"
									<%if(cwbordertypeidArray!=null&&cwbordertypeidArray.length>0) 
			            {for(int i=0;i<cwbordertypeidArray.length;i++){
			            	if(c.getValue()== new Long(cwbordertypeidArray[i])){%>
									selected="selected" <%break;
			            	}
			            }
				     }%>><%=c.getText()%></option>
								<%
									}
								%>
						</select> &nbsp;&nbsp;<select name="cwbstate" id="cwbstate">
								<option value="-1">请选择订单当前状态</option>
								<%
									for(FlowOrderTypeZongheEnum c : FlowOrderTypeZongheEnum.values()){
								%>
								<option value=<%=c.getValue()%>
									<%if(c.getValue()==Integer.parseInt(request.getParameter("cwbstate")==null?"-1":request.getParameter("cwbstate"))){%>
									selected="selected" <%}%>><%=c.getText()%></option>
								<%
									}
								%>
						</select></td>
						<td align="left">配送站点： <input name="branchname" id="branchname"
							onblur="if(this.value==''){this.value='输入站点名称自动匹配'}"
							onfocus="if(this.value=='输入站点名称自动匹配'){this.value=''}" class="input_text1" value="输入站点名称自动匹配"
							onkeyup="if($(this).val().length>0){multiSelectAll('dispatchbranchid',0,'请选择配送站点');moHuOrJingQueSlect($('#ismohu').val(),'<%=request.getContextPath()%>','dispatchbranchid',$(this).val());}" />
							<select name="ismohu" id="ismohu"
							onchange="if($('#branchname').val().length>0 && $('#branchname').val()!='输入站点名称自动匹配'){multiSelectAll('dispatchbranchid',0,'请选择配送站点');moHuOrJingQueSlect($('#ismohu').val(),'<%=request.getContextPath()%>','dispatchbranchid',$('#branchname').val());}">
								<option value="1"
									<%if(1==(request.getParameter("ismohu")==null?1:Long.parseLong(request.getParameter("ismohu")))){%>
									selected="selected" <%}%>>模糊匹配</option>
								<option value="2"
									<%if(2==(request.getParameter("ismohu")==null?1:Long.parseLong(request.getParameter("ismohu")))){%>
									selected="selected" <%}%>>精确匹配</option>
						</select><label> <select name="dispatchbranchid" id="dispatchbranchid" multiple="multiple"
								style="width: 120px;">
									<%
										for(Branch b : branchlist){
									%>
									<option value="<%=b.getBranchid()%>"
										<%if(dispatchbranchidArray.length>0) 
		               {for(int i=0;i<dispatchbranchidArray.length;i++){
		                if(b.getBranchid()== new Long(dispatchbranchidArray[i])){%>
										selected="selected" <%break;
		                }
		               }
		         	}%>><%=b.getBranchname()%></option>
									<%
										}
									%>
							</select>[<a href="javascript:multiSelectAll('dispatchbranchid',1,'请选择');">全选</a>][<a
								href="javascript:multiSelectAll('dispatchbranchid',0,'请选择');">取消全选</a>]
						</label></td>
					</tr>
					<tr>
						<td align="left">原支付方式： <select id="paytype" name="paytype">
								<option value="-1">请选择</option>
								<%
									for(PaytypeEnum c : PaytypeEnum.values()){
								%>
								<option value="<%=c.getValue()%>"
									<%if(c.getValue()==Integer.parseInt(request.getParameter("paytype")==null?"-1":request.getParameter("paytype"))){%>
									selected="selected" <%}%>><%=c.getText()%></option>
								<%
									}
								%>
						</select>现支付方式： <select id="curpaytype" name="curpaytype">
								<option value="-1">请选择</option>
								<%
									for(PaytypeEnum c : PaytypeEnum.values()){
								%>
								<option value="<%=c.getValue()%>"
									<%if(c.getValue()==Integer.parseInt(request.getAttribute("curpaytypeid")==null?"-1":(String)request.getAttribute("curpaytypeid"))){%>
									selected="selected" <%}%>><%=c.getText()%></option>
								<%
									}
								%>
						</select> 配送结果 <select name=operationOrderResultType id="operationOrderResultType" multiple="multiple"
							style="width: 120px;">
								<%
									for(DeliveryStateEnum c : DeliveryStateEnum.values()){
								%>
								<option value="<%=c.getValue()%>"
									<%if(operationOrderResultTypeList!=null&&operationOrderResultTypeList.size()>0) 
			            {for(int i=0;i<operationOrderResultTypeList.size();i++){
			            	if(c.getValue()== new Long(operationOrderResultTypeList.get(i).toString())){%>
									selected="selected" <%break;
			            	}
			            }
				     }%>><%=c.getText()%></option>
								<%
									}
								%>

						</select></td>
						<td align="left">当前站点： <input name="curbranchname" id="curbranchname"
							onblur="if(this.value==''){this.value='输入站点名称自动匹配'}"
							onfocus="if(this.value=='输入站点名称自动匹配'){this.value=''}" class="input_text1" value="输入站点名称自动匹配"
							onkeyup="if($(this).val().length>0){multiSelectAll('curdispatchbranchid',0,'请选择当前操作站点');moHuOrJingQueSlect($('#cur_ismohu').val(),'<%=request.getContextPath()%>','curdispatchbranchid',$(this).val());}" />
							<select name="cur_ismohu" id="cur_ismohu"
							onchange="if($('#curbranchname').val().length>0 && $('#curbranchname').val()!='输入站点名称自动匹配'){multiSelectAll('curdispatchbranchid',0,'请选择当前操作站点');moHuOrJingQueSlect($('#cur_ismohu').val(),'<%=request.getContextPath()%>','curdispatchbranchid',$('#curbranchname').val());}">
								<option value="1"
									<%if(1==(request.getParameter("cur_ismohu")==null?1:Long.parseLong(request.getParameter("cur_ismohu")))){%>
									selected="selected" <%}%>>模糊匹配</option>
								<option value="2"
									<%if(2==(request.getParameter("cur_ismohu")==null?1:Long.parseLong(request.getParameter("cur_ismohu")))){%>
									selected="selected" <%}%>>精确匹配</option>
						</select> <label> <select name="curdispatchbranchid" id="curdispatchbranchid"
								multiple="multiple" style="width: 120px;">
									<%
										for(Branch b : branchlist){
									%>
									<option value="<%=b.getBranchid()%>"
										<%if(curdispatchbranchidArray!=null&&curdispatchbranchidArray.length>0) 
		               {for(int i=0;i<curdispatchbranchidArray.length;i++){
		                if(b.getBranchid()== new Long(curdispatchbranchidArray[i])){%>
										selected="selected" <%break;
		                }
		               }
		         	}%>><%=b.getBranchname()%></option>
									<%
										}
									%>
							</select>[<a href="javascript:multiSelectAll('curdispatchbranchid',1,'请选择');">全选</a>][<a
								href="javascript:multiSelectAll('curdispatchbranchid',0,'请选择');">取消全选</a>]
						</label></td>
					</tr>
					<tr>
						<td align="left">归班审核状态：<select name="isaudit" id="isaudit">
								<option value="-1" <%if(isaudit==-1){%> selected="selected" <%}%>>全部</option>
								<option value="0" <%if(isaudit==0){%> selected="selected" <%}%>>未审核</option>
								<option value="1" <%if(isaudit==1){%> selected="selected" <%}%>>已审核</option>
						</select> 财务审核状态：<select name="financeAuditStatus" id="financeAuditStatus">
								<option value="-1" <%if(financeAuditStatus==-1){%> selected="selected" <%}%>>请选择</option>
								<option value="0" <%if(financeAuditStatus==0){%> selected="selected" <%}%>>站点交款已审核</option>
								<option value="1" <%if(financeAuditStatus==1){%> selected="selected" <%}%>>站点交款未审核</option>
								<option value="2" <%if(financeAuditStatus==2){%> selected="selected" <%}%>>小件员交款已审核</option>
								<option value="3" <%if(financeAuditStatus==3){%> selected="selected" <%}%>>小件员交款未审核</option>
						</select> 货物类型：<select name="goodsType" id="goodsType">
								<option value="-1" <%if(goodsType==-1){%> selected="selected" <%}%>>请选择</option>
								<option value="0" <%if(goodsType==0){%> selected="selected" <%}%>>普件</option>
								<option value="1" <%if(goodsType==1){%> selected="selected" <%}%>>大件</option>
								<option value="2" <%if(goodsType==2){%> selected="selected" <%}%>>贵品</option>
								<option value="3" <%if(goodsType==3){%> selected="selected" <%}%>>大件+贵品</option>
						</select>
						</td>
						<td align="left">下一站点： <input name="nextbranchname" id="nextbranchname"
							onblur="if(this.value==''){this.value='输入站点名称自动匹配'}"
							onfocus="if(this.value=='输入站点名称自动匹配'){this.value=''}" class="input_text1" value="输入站点名称自动匹配"
							onkeyup="if($(this).val().length>0){multiSelectAll('nextdispatchbranchid',0,'请选择下一站点');moHuOrJingQueSlect($('#next_ismohu').val(),'<%=request.getContextPath()%>','nextdispatchbranchid',$(this).val());}" />
							<select name="next_ismohu" id="next_ismohu"
							onchange="if($('#nextbranchname').val().length>0 && $('#nextbranchname').val()!='输入站点名称自动匹配'){multiSelectAll('nextdispatchbranchid',0,'请选择下一站点');moHuOrJingQueSlect($('#next_ismohu').val(),'<%=request.getContextPath()%>','nextdispatchbranchid',$('#nextbranchname').val());}">
								<option value="1"
									<%if(1==(request.getParameter("next_ismohu")==null?1:Long.parseLong(request.getParameter("next_ismohu")))){%>
									selected="selected" <%}%>>模糊匹配</option>
								<option value="2"
									<%if(2==(request.getParameter("next_ismohu")==null?1:Long.parseLong(request.getParameter("next_ismohu")))){%>
									selected="selected" <%}%>>精确匹配</option>
						</select><label> <select name="nextdispatchbranchid" id="nextdispatchbranchid"
								multiple="multiple" style="width: 120px;">
									<%
										for(Branch b : branchlist){
									%>
									<option value="<%=b.getBranchid()%>"
										<%if(nextdispatchbranchidArray!=null&&nextdispatchbranchidArray.length>0) 
		               {for(int i=0;i<nextdispatchbranchidArray.length;i++){
		                if(b.getBranchid()== new Long(nextdispatchbranchidArray[i])){%>
										selected="selected" <%break;
		                }
		               }
		         	}%>><%=b.getBranchname()%></option>
									<%
										}
									%>
							</select>[<a href="javascript:multiSelectAll('nextdispatchbranchid',1,'请选择');">全选</a>][<a
								href="javascript:multiSelectAll('nextdispatchbranchid',0,'请选择');">取消全选</a>]
						</label></td>
					</tr>

					<tr>
						<td>最后操作时间：<select id="curquerytimetype" name="curquerytimetype">
								<option value="<%=""+FlowOrderTypeEnum.DaoRuShuJu.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.DaoRuShuJu.getValue())?"selected":""%>>发货时间</option>
								<option value="<%=""+FlowOrderTypeEnum.TiHuo.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.TiHuo.getValue())?"selected":""%>>提货时间</option>
								<option value="<%=BranchEnum.KuFang.getValue()+"_"+FlowOrderTypeEnum.RuKu.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(BranchEnum.KuFang.getValue()+"_"+FlowOrderTypeEnum.RuKu.getValue())?"selected":""%>>入库时间</option>
								<option
									value="<%=BranchEnum.KuFang.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(BranchEnum.KuFang.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue())?"selected":""%>>出库时间</option>
								<option value="<%=""+FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())?"selected":""%>>到货时间</option>
								<option value="<%=""+FlowOrderTypeEnum.FenZhanLingHuo.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.FenZhanLingHuo.getValue())?"selected":""%>>领货时间</option>
								<option value="<%=""+FlowOrderTypeEnum.YiFanKui.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.YiFanKui.getValue())?"selected":""%>>反馈时间</option>
								<option value="<%=""+FlowOrderTypeEnum.YiShenHe.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.YiShenHe.getValue())?"selected":""%>>审核时间</option>
								<option value="<%=""+FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue())?"selected":""%>>库对库出库时间</option>
								<option
									value="<%="_"+BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals("_"+BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue())?"selected":""%>>站点中转出站时间</option>
								<option value="<%=BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.RuKu.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.RuKu.getValue())?"selected":""%>>中转站入库时间</option>
								<option
									value="<%=BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue())?"selected":""%>>中转站出库时间</option>
								<option value="<%=""+FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue())?"selected":""%>>站点退货出站时间</option>
								<option value="<%=""+FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())?"selected":""%>>退货站入库时间</option>
								<option
									value="<%="_"+BranchEnum.TuiHuo.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals("_"+BranchEnum.TuiHuo.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue())?"selected":""%>>退货站出库时间</option>
								<option value="<%=""+FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue())?"selected":""%>>退供货商出库时间</option>
								<option value="<%=""+FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue())?"selected":""%>>供货商拒收返库时间</option>
								<option value="<%=""+FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()%>"
									<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue())?"selected":""%>>退供货商成功时间</option>

						</select> <input type="text" name="begindate" id="strtime" value="<%=starttime%>" /> 到 <input
							type="text" name="enddate" id="endtime" value="<%=endtime%>" /></td>
					</tr>
					<tr>
						<td align="left" colspan="2">数据更新时间：${lastupdatetime }&nbsp;&nbsp;&nbsp;<input
							type="button" id="find" value="查询" class="input_button2" /> &nbsp;&nbsp;<input type="reset"
							value="清空" onclick="clearSelect();" class="input_button2" /> &nbsp;&nbsp;&nbsp;&nbsp;<%
 	if(orderlist != null && orderlist.size()>0){
 %> <select name="exportmould" id="exportmould">
								<option value="0">默认导出模板</option>
								<%
									for(Exportmould e:exportmouldlist){
								%>
								<option value="<%=e.getMouldfieldids()%>"><%=e.getMouldname()%></option>
								<%
									}
								%>
						</select> <%
 	}
 %> <%
 	if(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)==1){
 %> &nbsp;&nbsp;<input size="10px" type="button" id="btnval0" value="导出1-<%=count%>"
							class="input_button1" onclick="exportField('0','0');" /> <%
 	}else{for(int j=0;j<count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0);j++){
 %> <%
 	if(j==0){
 %> &nbsp;&nbsp;<input size="10px" type="button" id="btnval<%=j%>"
							value="导出1-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0%>万" class="input_button1"
							onclick="exportField('0','<%=j%>');" /> <%
 	}else if(j!=0&&j!=(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){
 %> &nbsp;&nbsp;<input size="10px" type="button" id="btnval<%=j%>"
							value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1%>-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0%>万"
							class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER%>','<%=j%>');" /> <%
 	}else if(j==(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){
 %> &nbsp;&nbsp;<input size="10px" type="button" id="btnval<%=j%>"
							value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1%>-<%=count%>" class="input_button1"
							onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER%>','<%=j%>');" /> <%
 	}
 %> <%
 	}}
 %>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<form action="<%=request.getContextPath()%>/datastatistics/exportExcle" method="post"
			id="searchForm2">
			<input type="hidden" name="count" id="count1" value="<%=page_obj.getTotal()%>" /> <input
				type="hidden" name="begin" id="begin" value="0" /> <input type="hidden" name="exportmould2"
				id="exportmould2" /> <input type="hidden" name="sign" id="sign"
				value="<%=ModelEnum.ZongHeChaXun.getValue()%>" />
			<div style="display: none;">
				<table width="100%" border="0" cellspacing="0" cellpadding="2">
					<tbody>
						<tr>
							<td align="left">供货商：<select name="customerid" multiple="multiple" style="width: 300px;">
									<%
										for(Customer c : customerlist){
									%>
									<option value="<%=c.getCustomerid()%>"
										<%if(customeridArray!=null&&customeridArray.length>0) 
			            {for(int i=0;i<customeridArray.length;i++){
			            	if(c.getCustomerid()== new Long(customeridArray[i])){%>
										selected="selected" <%break;
			            	}
			            }
				     }%>><%=c.getCustomername()%></option>
									<%
										}
									%>
							</select> 订单类型： <select name="cwbordertypeid" multiple="multiple">
									<%
										for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){
									%>
									<option value="<%=c.getValue()%>"
										<%if(cwbordertypeidArray!=null&&cwbordertypeidArray.length>0) 
			            {for(int i=0;i<cwbordertypeidArray.length;i++){
			            	if(c.getValue()== new Long(cwbordertypeidArray[i])){%>
										selected="selected" <%break;
			            	}
			            }
				     }%>><%=c.getText()%></option>
									<%
										}
									%>
							</select> &nbsp;
							</td>
							<td align="left">配送站点： <select name="dispatchbranchid" multiple="multiple"
								style="width: 200px;">
									<%
										for(Branch b : branchlist){
									%>
									<option value="<%=b.getBranchid()%>"
										<%if(dispatchbranchidArray.length>0) 
		               {for(int i=0;i<dispatchbranchidArray.length;i++){
		                if(b.getBranchid()== new Long(dispatchbranchidArray[i])){%>
										selected="selected" <%break;
		                }
		               }
		         	}%>><%=b.getBranchname()%></option>
									<%
										}
									%>
							</select></td>
						</tr>
						<tr>
							<td align="left">原支付方式： <select name="paytype">
									<option value="-1">请选择</option>
									<%
										for(PaytypeEnum c : PaytypeEnum.values()){
									%>
									<option value="<%=c.getValue()%>"
										<%if(c.getValue()==Integer.parseInt(request.getParameter("paytype")==null?"-1":request.getParameter("paytype"))){%>
										selected="selected" <%}%>><%=c.getText()%></option>
									<%
										}
									%>
							</select>现支付方式： <select name="curpaytype">
									<option value="-1">请选择</option>
									<%
										for(PaytypeEnum c : PaytypeEnum.values()){
									%>
									<option value="<%=c.getValue()%>"
										<%if(c.getValue()==Integer.parseInt(request.getAttribute("curpaytypeid")==null?"-1":(String)request.getAttribute("curpaytypeid"))){%>
										selected="selected" <%}%>><%=c.getText()%></option>
									<%
										}
									%>
							</select> <select name="cwbstate">
									<option value="-1">请选择订单当前状态</option>
									<%
										for(FlowOrderTypeZongheEnum c : FlowOrderTypeZongheEnum.values()){
									%>
									<option value=<%=c.getValue()%>
										<%if(c.getValue()==Integer.parseInt(request.getParameter("cwbstate")==null?"-1":request.getParameter("cwbstate"))){%>
										selected="selected" <%}%>><%=c.getText()%></option>
									<%
										}
									%>
							</select> 配送结果 <select name=operationOrderResultType multiple="multiple" style="width: 200px;">
									<%
										for(DeliveryStateEnum c : DeliveryStateEnum.values()){
									%>
									<option value="<%=c.getValue()%>"
										<%if(operationOrderResultTypeList!=null&&operationOrderResultTypeList.size()>0) 
			            {for(int i=0;i<operationOrderResultTypeList.size();i++){
			            	if(c.getValue()== new Long(operationOrderResultTypeList.get(i).toString())){%>
										selected="selected" <%break;
			            	}
			            }
				     }%>><%=c.getText()%></option>
									<%
										}
									%>

							</select></td>
							<td align="left">当前站点： <select name="curdispatchbranchid" multiple="multiple"
								style="width: 200px;">
									<%
										for(Branch b : branchlist){
									%>
									<option value="<%=b.getBranchid()%>"
										<%if(curdispatchbranchidArray!=null&&curdispatchbranchidArray.length>0) 
		               {for(int i=0;i<curdispatchbranchidArray.length;i++){
		                if(b.getBranchid()== new Long(curdispatchbranchidArray[i])){%>
										selected="selected" <%break;
		                }
		               }
		         	}%>><%=b.getBranchname()%></option>
									<%
										}
									%>
							</select></td>
						</tr>
						<tr>
							<td align="left">最后操作时间：<select name="curquerytimetype">
									<option value="<%=""+FlowOrderTypeEnum.DaoRuShuJu.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.DaoRuShuJu.getValue())?"selected":""%>>发货时间</option>
									<option value="<%=""+FlowOrderTypeEnum.TiHuo.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.TiHuo.getValue())?"selected":""%>>提货时间</option>
									<option value="<%=BranchEnum.KuFang.getValue()+"_"+FlowOrderTypeEnum.RuKu.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(BranchEnum.KuFang.getValue()+"_"+FlowOrderTypeEnum.RuKu.getValue())?"selected":""%>>入库时间</option>
									<option
										value="<%=BranchEnum.KuFang.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(BranchEnum.KuFang.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue())?"selected":""%>>出库时间</option>
									<option value="<%=""+FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())?"selected":""%>>到货时间</option>
									<option value="<%=""+FlowOrderTypeEnum.FenZhanLingHuo.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.FenZhanLingHuo.getValue())?"selected":""%>>领货时间</option>
									<option value="<%=""+FlowOrderTypeEnum.YiFanKui.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.YiFanKui.getValue())?"selected":""%>>反馈时间</option>
									<option value="<%=""+FlowOrderTypeEnum.YiShenHe.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.YiShenHe.getValue())?"selected":""%>>审核时间</option>
									<option value="<%=""+FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue())?"selected":""%>>库对库出库时间</option>
									<option
										value="<%="_"+BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals("_"+BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue())?"selected":""%>>站点中转出站时间</option>
									<option value="<%=BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.RuKu.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.RuKu.getValue())?"selected":""%>>中转站入库时间</option>
									<option
										value="<%=BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(BranchEnum.ZhongZhuan.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue())?"selected":""%>>中转站出库时间</option>
									<option value="<%=""+FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue())?"selected":""%>>站点退货出站时间</option>
									<option value="<%=""+FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())?"selected":""%>>退货站入库时间</option>
									<option
										value="<%="_"+BranchEnum.TuiHuo.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals("_"+BranchEnum.TuiHuo.getValue()+"_"+FlowOrderTypeEnum.ChuKuSaoMiao.getValue())?"selected":""%>>退货站出库时间</option>
									<option value="<%=""+FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue())?"selected":""%>>退供货商出库时间</option>
									<option value="<%=""+FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue())?"selected":""%>>供货商拒收返库时间</option>
									<option value="<%=""+FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()%>"
										<%=request.getAttribute("curquerytimetype")!=null&&request.getAttribute("curquerytimetype").equals(""+FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue())?"selected":""%>>退供货商成功时间</option>

							</select> <input type="text" name="begindate" value="<%=starttime%>" /> 到 <input type="text"
								name="enddate" id="endtime" value="<%=endtime%>" /> 归班审核状态： <select name="isaudit">
									<option value="-1" <%if(isaudit==-1){%> selected="selected" <%}%>>全部</option>
									<option value="0" <%if(isaudit==0){%> selected="selected" <%}%>>未审核</option>
									<option value="1" <%if(isaudit==1){%> selected="selected" <%}%>>已审核</option>
							</select></td>
							<td align="left">下一站点： <select name="nextdispatchbranchid" multiple="multiple"
								style="width: 200px;">
									<%
										for(Branch b : branchlist){
									%>
									<option value="<%=b.getBranchid()%>"
										<%if(nextdispatchbranchidArray!=null&&nextdispatchbranchidArray.length>0) 
		               {for(int i=0;i<nextdispatchbranchidArray.length;i++){
		                if(b.getBranchid()== new Long(nextdispatchbranchidArray[i])){%>
										selected="selected" <%break;
		                }
		               }
		         	}%>><%=b.getBranchname()%></option>
									<%
										}
									%>
							</select></td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
	</div>
	<div style="height: 200px"></div>
	<%
		if(orderlist != null && orderlist.size()>0){
	%>
	<div style="overflow-x: scroll; width: 100%" id="scroll">
		<table width="2800" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
			<tr class="font_1">
				<td align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">配送站</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">当前站</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">下一站</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">支付方式</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">现支付方式</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">应收金额</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">退款金额</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">当前状态</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">配送结果</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">归班状态</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>

				<td align="center" valign="middle" bgcolor="#eef6ff">提货时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">入库时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">库对库出库时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">出库时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">到站时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">领货时间</td>

				<td align="center" valign="middle" bgcolor="#eef6ff">反馈时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">审核时间</td>

				<td align="center" valign="middle" bgcolor="#eef6ff">站点中转出站时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">中转站入库时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">中转站出库时间</td>

				<td align="center" valign="middle" bgcolor="#eef6ff">站点退货出站时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">退货站入库时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">退货站出库时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">退供货商出库时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">供货商拒收返库时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">退供货商成功时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">财务审核状态</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">货物类型</td>

				<!-- <td  align="center" valign="middle" bgcolor="#eef6ff"  >审核状态</td> -->

			</tr>

			<%
				for(CwbOrderTail  c : orderlist){
			%>
			<tr bgcolor="#FF3300">
				<td align="center" valign="middle"><a target="_blank"
					href="<%=request.getAttribute("dmpUrl")%>/order/queckSelectOrder/<%=c.getCwb()%>"><%=c.getCwb()%></a></td>
				<td align="center" valign="middle"><%=brachmap.get(Long.parseLong(c.getDeliverybranchid()))==null?"":brachmap.get(Long.parseLong(c.getDeliverybranchid()))%></td>
				<td align="center" valign="middle"><%=brachmap.get(Long.parseLong(c.getBranchid()))%></td>
				<td align="center" valign="middle"><%=brachmap.get(Long.parseLong(c.getNextbranchid()))==null?"":brachmap.get(Long.parseLong(c.getNextbranchid()))%></td>
				<td align="center" valign="middle"><%=c.getPayType()%></td>
				<td align="center" valign="middle"><%=c.getCurPayType()%></td>

				<td align="center" valign="middle"><%=customermap.get(c.getCustomerid())==null?"":customermap.get(c.getCustomerid())%></td>
				<td align="center" valign="middle"><%=c.getCwbOrderTypeMethod()%></td>

				<td align="center" valign="middle"><%=c.getReceivablefee()%></td>
				<td align="center" valign="middle"><%=c.getPaybackfee()%></td>
				<td align="center" valign="middle"><%=c.getFlowordertypeMethod()%></td>
				<td align="center" valign="middle">
					<%
						if(c.getDeliverystate()!=-1){
					%> <%=DeliveryStateEnum.getByValue(c.getDeliverystate())==null?"":DeliveryStateEnum.getByValue(c.getDeliverystate()).getText()%>
					<%
						}
					%>
				</td>
				<td align="center" valign="middle"><%=c.getIsAudit()%></td>

				<td align="center" valign="middle"><%=c.getEmaildatetime()%></td>

				<td align="center" valign="middle"><%=c.getGetgoodstime()%></td>

				<td align="center" valign="middle"><%=c.getIntowarehoustime()%></td>
				<td align="center" valign="middle"><%=c.getHousetohousetime()%></td>
				<td align="center" valign="middle"><%=c.getOutwarehousetime()%></td>
				<td align="center" valign="middle"><%=c.getSubstationgoodstime()%></td>
				<td align="center" valign="middle"><%=c.getReceivegoodstime()%></td>
				<td align="center" valign="middle"><%=c.getCouplebacktime()%></td>
				<td align="center" valign="middle"><%=c.getChecktime()%></td>
				<td align="center" valign="middle"><%=c.getZhandianouttozhongzhuantime()%></td>
				<td align="center" valign="middle"><%=c.getChangeintowarehoustime()%></td>
				<td align="center" valign="middle"><%=c.getChangeouttowarehoustime()%></td>
				<td align="center" valign="middle"><%=c.getReturngoodsoutwarehousetime()%></td>
				<td align="center" valign="middle"><%=c.getTuihuointowarehoustime()%></td>
				<td align="center" valign="middle"><%=c.getTuihuoouttozhandiantime()%></td>
				<td align="center" valign="middle"><%=c.getCustomerbacktime()%></td>
				<td align="center" valign="middle"><%=c.getGonghuoshangjushoutime()%></td>
				<td align="center" valign="middle"><%=c.getGonghuoshangchenggongtime()%></td>
				<td align="center" valign="middle"><%=c.getStrFinanceAuditStatus()%></td>
				<td align="center" valign="middle"><%=c.getStrGoodsType()%></td>
				<%-- <td  align="center" valign="middle"><%=c.getAuditstateStr() %></td> --%>
			</tr>
			<%
				}
			%>
			<%
				if(request.getAttribute("count")!= null){
			%>
			<tr bgcolor="#FF3300">
				<td align="center" valign="middle" class="high">合计：<font color="red"><%=count%></font>&nbsp;单
				</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle" class="high"><font color="red"><%=request.getAttribute("sum")==null?"0.00":request.getAttribute("sum")%></font>&nbsp;元
				</td>
				<td align="center" valign="middle" class="high"><font color="red"><%=request.getAttribute("paybackfeesum")==null?"0.00":request.getAttribute("paybackfeesum")%></font>&nbsp;元
				</td>

				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>
				<td align="center" valign="middle">&nbsp;</td>

				<!-- <td  align="center" valign="middle">&nbsp;</td> -->
			</tr>
			<%
				}
			%>
		</table>


	</div>
	<%
		}
	%>

	<%
		if(page_obj.getMaxpage()>1){
	%>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a
					href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();">第一页</a> <a
					href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious()%>');$('#searchForm').submit();">上一页</a>
					<a
					href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext()%>');$('#searchForm').submit();">下一页</a>
					<a
					href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage()%>');$('#searchForm').submit();">最后一页</a>
					共<%=page_obj.getMaxpage()%>页 共<%=page_obj.getTotal()%>条记录 当前第<select id="selectPg"
					onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
						<%
							for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {
						%>
						<option value="<%=i%>"><%=i%></option>
						<%
							}
						%>
				</select>页</td>
			</tr>
		</table>
	</div>
	<%
		}
	%>

	<script type="text/javascript">
		$("#selectPg").val(<%=request.getAttribute("page")%>);
function exportField(page,j){
		if($("#isshow").val()=="1"&&<%=orderlist != null && orderlist.size()>0%>){
			$("#exportmould2").val($("#exportmould").val());
			$("#btnval"+j).attr("disabled","disabled");
			$("#btnval"+j).val("请稍后……");
			$("#begin").val(j);
			$.ajax({
				type: "POST",
				url:$("#searchForm2").attr("action"),
				data:$("#searchForm2").serialize(),
				dataType:"json",
				success : function(data) {
					if(data.errorCode==0){
						alert("已进入导出任务队列！");
						$("#downCount",parent.document).html(Number($("#downCount",parent.document).html())+1);
						$.ajax({
							type: "POST",
							url:"<%=request.getContextPath()%>/datastatistics/commitExportExcle",
								data : $("#searchForm2").serialize(),
								dataType : "json",
								success : function(data) {

								}
							});
						} else {
							alert(data.remark);
						}
					}
				});
			} else {
				alert("没有做查询操作，不能导出！");
			}
		}
	</script>
</body>
</html>

