<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>武汉中转出站交接单打印</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 companyname="北京易普联科信息技术有限公司" license="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview() {	
	createOneFormPage();           
	LODOP.PREVIEW();	
};
function prn1_print() {		
	createOneFormPage();
	LODOP.PRINT();	
};
function createOneFormPage(){
	LODOP = getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));                      
	var strBodyStyle = document.getElementById("print_style").outerHTML;
	LODOP.PRINT_INIT("中转出站交接单打印");
	var strBodyHtml = document.getElementById("printTable_0").outerHTML;
	strBodyHtml = strBodyHtml.replace("preview_box", "");
	LODOP.NewPage();
	LODOP.ADD_PRINT_HTM("0mm","0mm","RightMargin:0mm","BottomMargin:0mm", '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">' + strBodyStyle + "<body>" + strBodyHtml + "</body></html>");
};
function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print();
	}
}
</script>
<style type="text/css" id="print_style">
	.preview_box {
		border: 1px solid #585656;
	}
	.out_box {
		width: 75mm;
	}
	.inner_box {
		margin: 5mm;
	}
	.title {
		font-size: 14px;
		width:100%;
		text-align:center;
	}
	.content_1 {
		font-size: 12px;
	}
	.content_2 {
		font-size: 14px;
		font-weight: bold;
		padding-left: 3mm;
		margin-top:1mm;
	}
	.content_3 {
		font-size: 10px;
		margin-left: 3mm;
		margin-top:1mm;
	}
	.dashed {
		border-top:1px dashed #585656;
		height: 1px;
		overflow:hidden;
		margin-top:1mm;
		margin-bottom:1mm;
	}
	.underline {
		border-bottom:1px solid #585656;
	}
	.square {
		width: 10px;
		height: 10px; 
		border:1px solid #585656;
		margin-left:1mm;
		margin-right:1mm;
	}
	.float_left {
		float:left;
	}
	.float_right {
		float:right;
	}
	.content_sign {
		font-size: 12px;
		padding-top : 1.5mm;
		padding-bottom : 1.5mm;
	}
	.AutoNewline{
	  font-size: 10px;
      word-break: break-all;/*必须*/
      width:30mm;
    }
    .index_num{
      font-size: 10px;
      width:5mm;
    }
</style>
</head>
<body>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview()">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
	    <div id="printTable_0">
		<div class="out_box preview_box">
			<div class="inner_box">
				<div class="title">（${print.userbranchname}）至（${print.nextbranchname}）交接清单（补打）</div>
				<div class="content_1">出站时间：${print.starttime}至${print.endtime}</div>
				<div class="content_1"> 
				    <span >序号</span>
				    <span style="word-spacing:8mm;">&nbsp;</span>
					<span >单号1</span>
					<span style="word-spacing:14mm;">&nbsp;</span>
					<span >单号2</span>
					<!-- <span style="word-spacing:8mm;">&nbsp;</span>
				    <span >单号3</span> -->
				</div>
				<div class="dashed"></div>
				
				<div class="content_1"> 
				    <table width="100%" border="0" cellspacing="1" cellpadding="0">
				     <c:forEach items="${print.cwbmap}" var="item">
                       <tr>
                       <td class="index_num">${item.key}</td>
                           <c:forEach items="${item.value}" var="cwb">
                               <td class="AutoNewline">${cwb}</td>
                           </c:forEach>
                       </tr>
                     </c:forEach>
				    </table>
				</div> 
				<div class="dashed"></div>
				<div class="content_1">
				     <span>合计：</span>
				     <span> 共${print.count}单${print.sum}件。</span>
				     <span style="word-spacing:2mm;">&nbsp;</span>
				     <span> 第1页/共1页</span>
				</div>
				
				<div class="content_1">
				     <span>打印：</span>
				     <span>${print.username} （${print.username}）</span>
				     <span style="word-spacing:1mm;">&nbsp;</span>
				     <span>${print.printtime}</span>
				</div>
				
				<div class="dashed"></div>
				
				<div class="content_2">配送站填写区：</div>
				  
				<div class="content_sign"> 
				<span>应退</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>单</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>件，</span>
				
				<span>实退</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>单</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>件</span>
				</div> 
				
				<div class="content_sign"> 
				<span>说明：</span>
				<span class="underline" style="word-spacing:50mm;">&nbsp;</span>
				</div>  
				
				<div class="content_sign"> 
				<span>交接人签字:</span>
				<span class="underline" style="word-spacing:20mm;">&nbsp;</span>
				</div>
				
				<div class="content_sign">
				<span>时间：</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>月</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>日</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>时</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>分</span>
				</div>
				
				<div class="dashed"></div>
				
				<div class="content_2">运输部填写区：</div>
				
				<div class="content_sign"> 
				<span>应退</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>单</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>件，</span>
				
				<span>实退</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>单</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>件</span>
				</div> 
				
				<div class="content_sign"> 
				<span>说明：</span>
				<span class="underline" style="word-spacing:50mm;">&nbsp;</span>
				</div>  
				
				<div class="content_sign"> 
				<span>交接人签字:</span>
				<span class="underline" style="word-spacing:20mm;">&nbsp;</span>
				</div>
				
				<div class="content_sign">
				<span>时间：</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>月</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>日</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>时</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>分</span>
				</div>
				
				<div class="dashed"></div>
				
				<div class="content_2">中转站填写区：</div>
				
				<div class="content_sign"> 
				<span>应退</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>单</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>件，</span>
				
				<span>实退</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>单</span>
				<span class="underline" style="word-spacing:4mm;">&nbsp;</span>
				<span>件</span>
				</div> 
				
				<div class="content_sign"> 
				<span>说明：</span>
				<span class="underline" style="word-spacing:50mm;">&nbsp;</span>
				</div>  
				
				<div class="content_sign"> 
				<span>交接人签字:</span>
				<span class="underline" style="word-spacing:20mm;">&nbsp;</span>
				</div>
				
				<div class="content_sign">
				<span>时间：</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>月</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>日</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>时</span>
				<span style="word-spacing:3mm;">&nbsp;</span>
				<span>分</span>
				</div>
			</div>
		</div>
	</div>
</body>
</html>