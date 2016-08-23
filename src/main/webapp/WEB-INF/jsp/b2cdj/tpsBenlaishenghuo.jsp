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
        <h1><div id="close_box" onclick="closeBox()"></div>TPS-本来生活接口设置</h1>
        <form id="vipshop_save_Form" name="vipshop_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/vipshop/saveVipShop/${joint_num}" method="post">
    
        <input type ="hidden" id="forward_hours" name ="forward_hours" value="0"  maxlength="2">
<div id="box_form">
                <ul>
                    <%if(vipshop != null){ %>
                        <li><span>承运商编码：</span>
                            <input type ="text" id="shipper_no" name ="shipper_no" value="<%=vipshop.getShipper_no() %>"  maxlength="300">
                        </li>
                        <li><span>系统中客户id：</span>
                            <input type ="text" id="customerids" name ="customerids" onblur="validate('customerids')" value="<%=vipshop.getCustomerids() %>"  maxlength="300">
                        </li>
                        <li><span>托运模式：</span>
                            <input type ="radio" id="isTuoYunDanFlag1" name ="isTuoYunDanFlag" value="1" >开启
                            <input type ="radio" id="isTuoYunDanFlag2" name ="isTuoYunDanFlag" value="0"  checked="checked" >关闭
                        </li>
                        <li><span>订单接收类型：</span>
                            <input type="checkbox" name ="isGetPeisongFlag" value="1" <%if(vipshop.getIsGetPeisongFlag()==1){%>checked<%}%>  >配送
                            <input type="checkbox" name ="isGetShangmentuiFlag" value="1" <%if(vipshop.getIsGetShangmentuiFlag()==1){%>checked<%}%>  >上门退
                            <input type="checkbox" name ="isGetShangmenhuanFlag" value="1" <%if(vipshop.getIsGetShangmenhuanFlag()==1){%>checked<%}%>  >上门换
                            <input type="checkbox" name ="isGetOXOFlag" value="1" <%if(vipshop.getIsGetOXOFlag()==1){%>checked<%}%>  >OXO/OXO_JIT
                        </li>
                        <li><span>是否订单下载：</span>
							<input type ="radio" id="isopendownload1" name ="isopendownload" value="1" <%if(vipshop.getIsopendownload()==1){%>checked<%}%>  >开启
							<input type ="radio" id="isopendownload2" name ="isopendownload" value="0"   <%if(vipshop.getIsopendownload()==0){%>checked<%}%>  >关闭
						</li>
                        <li><span>是否自动化接口：</span>
                            <input type ="radio" id="isAutoInterface1" name ="isAutoInterface" value="0" <%if(vipshop.getIsAutoInterface()==0){%>checked<%}%>  >否
                            <input type ="radio" id="isAutoInterface2" name ="isAutoInterface" value="1" <%if(vipshop.getIsAutoInterface()==1){%>checked<%}%>  >是
                        </li>
                        <li><span>取消或拦截：</span>
                            <input type ="radio" id="cancelOrIntercept1" name ="cancelOrIntercept" value="0" <%if(vipshop.getCancelOrIntercept()==0){%>checked<%}%>  >取消开启
                            <input type ="radio" id="cancelOrIntercept2" name ="cancelOrIntercept" value="1" <%if(vipshop.getCancelOrIntercept()==1){%>checked<%}%>  >拦截开启
                        </li>
                        <li><span>拒收原因回传：</span>
                            <input type ="radio" id="resuseReasonFlag1" name ="resuseReasonFlag" value="0" <%if(vipshop.getResuseReasonFlag()==0){%>checked<%}%>  >回传
                            <input type ="radio" id="resuseReasonFlag2" name ="resuseReasonFlag" value="1" <%if(vipshop.getResuseReasonFlag()==1){%>checked<%}%>  >不回传
                        </li>
                        <li><span>生成批次标识：</span>
                            <input type ="radio" id="isCreateTimeToEmaildateFlag1" name ="isCreateTimeToEmaildateFlag" value="0" <%if(vipshop.getIsCreateTimeToEmaildateFlag()==0){%>checked<%}%>  >关闭
                            <input type ="radio" id="isCreateTimeToEmaildateFlag2" name ="isCreateTimeToEmaildateFlag" value="1" <%if(vipshop.getIsCreateTimeToEmaildateFlag()==1){%>checked<%}%>  >开启（订单出仓时间作为标识,开启必须关闭托运模式）
                        </li>
                        <li style="display: none;"><span>是否订单下发接口：</span>
                            <input type ="text" id="isTpsSendFlag" name ="isTpsSendFlag" value="1"  maxlength="300">
                        </li>
                        <li><span>订单导入库房：</span>
                            <select name="warehouseid">
                                <option value="0">请选择库房</option>
                                <%for(Branch b:warehouselist){
                                %>
                                    <option value="<%=b.getBranchid()%>" <%if(vipshop.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
                                <%}%>
                            </select>
                        </li>
                        <li><span>密码：</span>
                            <input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
                        </li>
                    <%}else{ %>
                        <li><span>承运商编码：</span>
                            <input type ="text" id="shipper_no" name ="shipper_no"  maxlength="300">
                        </li>
                        <li><span>系统中客户id：</span>
                            <input type ="text" id="customerids" name ="customerids" onblur="validate('customerids')"  maxlength="300">
                        </li>
                        <li><span>托运模式：</span>
                            <input type ="radio" id="isTuoYunDanFlag1" name ="isTuoYunDanFlag" value="1" >开启
                            <input type ="radio" id="isTuoYunDanFlag2" name ="isTuoYunDanFlag" value="0" checked>关闭
                        </li>
                        <li><span>订单接收类型：</span>
                            <input type="checkbox" name ="isGetPeisongFlag" value="1"  >配送
                            <input type="checkbox" name ="isGetShangmentuiFlag" value="1" >上门退
                            <input type="checkbox" name ="isGetShangmenhuanFlag" value="1"  >上门换
                            <input type="checkbox" name ="isGetOXOFlag" value="1" >OXO/OXO_JIT
                        </li>
                        <li><span>是否订单下载：</span>
							<input type ="radio" id="isopendownload1" name ="isopendownload" value="1" checked >开启
							<input type ="radio" id="isopendownload2" name ="isopendownload" value="0" >关闭
						</li>
                         <li><span>是否自动化接口：</span>
                            <input type ="radio" id="isAutoInterface1" name ="isAutoInterface" value="0" checked>否
                            <input type ="radio" id="isAutoInterface2" name ="isAutoInterface" value="1" >是
                        </li>
                        <li><span>取消或拦截：</span>
                            <input type ="radio" id="cancelOrIntercept1" name ="cancelOrIntercept" value="0" checked  >取消开启
                            <input type ="radio" id="cancelOrIntercept2" name ="cancelOrIntercept" value="1"  >拦截开启
                        </li>
                        <li><span>拒收原因回传：</span>
                            <input type ="radio" id="resuseReasonFlag1" name ="resuseReasonFlag" value="0" checked>回传
                            <input type ="radio" id="resuseReasonFlag2" name ="resuseReasonFlag" value="1" >不回传
                        </li>
                        <li><span>生成批次标识：</span>
                            <input type ="radio" id="isCreateTimeToEmaildateFlag1" name ="isCreateTimeToEmaildateFlag" value="0" checked >关闭
                            <input type ="radio" id="isCreateTimeToEmaildateFlag2" name ="isCreateTimeToEmaildateFlag" value="1"  >开启（订单生成时间作为标识）
                        </li>
                        <li style="display: none;"><span>是否订单下发接口：</span>
                            <input type ="text" id="isTpsSendFlag" name ="isTpsSendFlag" value="1"  maxlength="300">
                        </li>
                        <li><span>订单导入库房：</span>
                            <select name="warehouseid">
                                <option value="0">请选择库房</option>
                                <%for(Branch b:warehouselist){
                                %>
                                    <option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
                                <%}%>
                            
                            </select>
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

