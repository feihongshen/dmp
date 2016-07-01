<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.vipshop.*"%>
<%@page import="cn.explink.domain.Branch" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<%
VipShop vipshop = (VipShop)request.getAttribute("vipshopObject");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");

%>



<script type="text/javascript">

</script>
<div id="box_bg"></div>
<div id="box_contant">
    <div id="box_top_bg"></div>
    <div id="box_top_bg"></div>
    <div id="box_top_bg"></div>
    <div id="box_in_bg" style="width: 500px;">
        <h1><div id="close_box" onclick="closeBox()"></div>TPS快递单下发对接设置</h1>
        <form id="vipshop_save_Form" name="vipshop_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/vipshop/saveVipShop/${joint_num}" method="post">
    
        <input type ="hidden" id="forward_hours" name ="forward_hours" value="0"  maxlength="2">
<div id="box_form">
                <ul>
                    <%if(vipshop != null){ %>
                        <li><span>承运商编码：</span>
                            <input type ="text" id="shipper_no" name ="shipper_no" value="<%=vipshop.getShipper_no() %>"  maxlength="300">
                        </li>
                        <li style="display: none;">><span>是否订单下载：</span>
                            <input type ="radio" id="isopendownload1" name ="isopendownload" value="1" <%if(vipshop.getIsopendownload()==1){%>checked<%}%>  >开启
                            <input type ="radio" id="isopendownload2" name ="isopendownload" value="0"   <%if(vipshop.getIsopendownload()==0){%>checked<%}%>  >关闭
                        </li>
                        <li><span>密码：</span>
                            <input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
                        </li>
                    <%}else{ %>
                        <li><span>承运商编码：</span>
                            <input type ="text" id="shipper_no" name ="shipper_no"  maxlength="300">
                        </li>
                        <li style="display: none;"><span>是否订单下载：</span>
                            <input type ="radio" id="isopendownload1" name ="isopendownload" value="1"  checked>开启
                            <input type ="radio" id="isopendownload2" name ="isopendownload" value="0"   >关闭
                        </li>
                        <li><span>密码：</span>
                            <input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
                        </li>
                        
                    <%} %>
                </ul>
        </div>
        <div align="center"><input type="submit" value="保存" class="button"/></div>
        <input type="hidden" name="joint_num" value="${joint_num}"/>
    </form>
    </div>
</div>
<div id="box_yy"></div>

