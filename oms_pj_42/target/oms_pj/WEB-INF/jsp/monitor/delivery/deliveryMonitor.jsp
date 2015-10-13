<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.StringUtil"%>
<%
List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");

Branch branchNow =(Branch) request.getAttribute("branchNow");

List deliveryList =(List) request.getAttribute("deliveryList");
Branch nowBranch = request.getAttribute("nowBranch")==null?null:(Branch)request.getAttribute("nowBranch");
Map typeNameMap =(Map) request.getAttribute("typeNameMapPram");

String typeList =(String) (request.getAttribute("typeList")==null?"":request.getAttribute("typeList"));
String[] typeListS = typeList.split(",");

String typenameList =(String) request.getAttribute("typenameList");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/omsTable.css" type="text/css"> --%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/omsTable.js"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/exporting.js"></script>
<script type="text/javascript">

        $(document).ready(function() {
            // Options displayed in comma-separated list
            $("#control_7").multiSelect({ oneOrMoreSelected: '*' });
   
   //获取下拉框的值
   $("#btnval").click(function(){
        var checkval="";
        $("label").each(function(index){
     //找到被选中的项
           if($(this).attr("class")=="checked"){
        checkval+=$(this).children().attr("value")+",";
     }
        });
        $("#controlStr").val(checkval);
        $("#searchForm").submit();
   });
        });
   
    </script>
    <script type="text/javascript">

$(function () {
    var chart;
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'container',
                type: 'column'
            },
            title: {
                text: '投递实效统计'
            },
            subtitle: {
                text: '按时间跨度统计（默认统计最近6小时内）'
            },
            xAxis: {
                categories: [
                   <%=typenameList.equals("")?"'全部'":typenameList%>
                ]
            },
            yAxis: {
                min: 0,
                title: {
                    text: '订单数量'
                }
            },
            legend: {
                layout: 'vertical',
                backgroundColor: '#FFFFFF',
                align: 'left',
                verticalAlign: 'top',
                x: 100,
                y: 7,
                floating: true,
                shadow: true
            },
            tooltip: {
                formatter: function() {
                    return ''+
                        this.x +': '+ this.y;
                }
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: [
                     
				<% if(typeListS != null && typeListS.length>0){%> 
				
				 {
						name: '<%=branchNow.getBranchname() %>',
						data: [
						<%for(int j=0;j<typeListS.length;j++){
							if(j!=typeListS.length-1){%>
							<%=((Map)deliveryList.get(j)).get(typeListS[j]) %>,
							<%}
							else{%>
								<%=((Map)deliveryList.get(j)).get(typeListS[j]) %>
							<%}
						}%>]
					}
				
				<%}%>
             ]
        });
    });
    
});
</script>
    <style type="text/css">
        HTML
        {
            font-family: Arial, Helvetica, sans-serif;
            font-size: 12px;
        }
        H2
        {
            font-size: 14px;
            font-weight: bold;
            margin: 1em 0em .25em 0em;
        }
        P
        {
         margin-top:20px;
        }
    </style>
</HEAD>
<form id="searchForm" action ="<%=request.getContextPath()%>/monitordelivery/date/1" method = "post">
		站点：<select name ="branchid" id="branid">
            <%String nowzhandian="全部"; %>
            <%if(nowBranch !=null){ %>
            <option value ="<%=nowBranch.getBranchid()%>"><%=nowBranch.getBranchname() %><%nowzhandian=nowBranch.getBranchname();%></option>
            <%}else{ %>
		            <option value ="-1">全部</option>
	              <%if(branchnameList != null && branchnameList.size()>0){ %>
	               <%for( Branch b:branchnameList){ %>
	               		<option value ="<%=b.getBranchid()%>" <%if(b.getBranchid() == new Long(request.getAttribute("branchidSession")==null?"-1":request.getAttribute("branchidSession").toString())) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
	               <%} }}%>
	              </select> 
           订单状态：<select id="control_7" name="control_7[]" multiple="multiple" style="width: 320px;">
	               <option <%if(typeList.indexOf(FlowOrderTypeEnum.ChuKuSaoMiao.getValue()+"")>-1){ %>selected="selected"<%} %> value ="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>">出库未到站</option>
	               <option <%if(typeList.indexOf(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+"")>-1){ %>selected="selected"<%} %> value ="<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()%>">到站未领货</option>
	               <option <%if(typeList.indexOf(FlowOrderTypeEnum.FenZhanLingHuo.getValue()+"")>-1){ %>selected="selected"<%} %> value ="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue()%>">领货未反馈</option>
             </select>
          超过时长：<select name ="timeToid" id="timeToid">
                <%for(int i=1;i<=24;i++){ %>
                   <option value ="<%=i%>" <%if(i== Integer.parseInt(request.getAttribute("timeNow").toString())  || i == new Long(request.getParameter("timeToid")==null?"-1":request.getParameter("timeToid"))) {%>selected="selected"<%} %>><%=i %>小时</option>
               <%} %>
         </select>       
		<input type ="button" id="btnval" value="统计" class="input_button2" />
		<input type="hidden" id="controlStr" name="controlStrType" value=""/>
		
	</form>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<span>
	</span>
	</div>
	<div class="right_title">
	<div class="jg_30"><font color="red">注：此功能统计所选站点、在所选超时时间的订单的当前状态分布，如：可以统计A站在从“数据导入”到现在超过6小时，还属于“到货扫描”状态的订单数量</font></div>
<%if(branchNow != null){ %>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">站点</td>
			<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">状态</td>
			<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">订单数量</td>
		</tr>
		<% if(typeListS!=null && typeListS.length>0){%> 
		<%for(int j=0;j<typeListS.length;j++){%>
		<tr>
			<td><%=branchNow ==null?"全部":("".equals(branchNow.getBranchname())?"全部":branchNow.getBranchname())%></td> 
			<td><%=typeNameMap.get(typeListS[j])%></td>
			<td>
			<%if(Integer.parseInt(((Map)deliveryList.get(j)).get(typeListS[j]).toString()) >0) {%>
			<a href="<%=request.getContextPath()%>/monitordelivery/dateshow/1?flowType=<%=typeListS[j]%>"><%=((Map)deliveryList.get(j)).get(typeListS[j])%></a>
			<%} else{ %>
			0
			<%} %>
			</td>             
		</tr>
		<%} %>
		<%} %>
	</table>
		<div id="container" style="min-width: 400px; height: 280px; margin: 0 auto"></div>
<%} %>		
	<div class="jg_10"></div><div class="jg_10"></div>
</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>
	</div>
</body>
</html>
