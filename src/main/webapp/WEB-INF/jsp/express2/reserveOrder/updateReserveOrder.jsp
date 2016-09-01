<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<div id="dialog1" title="修改预约单" style="display:none;">
    <div style="margin-top: 20px; margin-left:10px;margin-right:10px; width: 460px;">
        <table>
            <tr>
                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;width: 30%;">
                    预约单号：
                </td>
                <td style="border: 0px; vertical-align: middle;">
                    <input id="reserveOrderNo4edit" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;">寄件人</td>
                <td style="border: 0px; vertical-align: middle;">
                    <input id="cnorName4edit"/>
                </td>
            </tr>
            <tr>
                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;">地址：</td>
                <td style="border: 0px; vertical-align: middle;">
                    <select id="province4edit" disabled="disabled" name="province4edit" style="width: 90px;">
                       <option value=""><%=request.getAttribute("provinceName")%></option>
                        <%-- <%for (CustomWareHouse cw : wareHouseList) { %>
                         <option class="customerid2_<%=cw.getCustomerid() %>"
                                 value="<%=cw.getWarehouseid() %>"><%=cw.getCustomerwarehouse() %>
                         </option>
                         <%} %>--%>
                    </select>
                    <select id="city4edit" name="city4edit" style="width: 90px;">
                        <option value="" selected="selected">市</option>
                        <c:forEach items="${cityList}" var="list">
                            <option value="${list.id}" code="${list.code}">${list.name}</option>
                        </c:forEach>
                    </select>
                    <select id="county4edit" name="county4edit" style="width: 100px;">
                        <option value="">区/县</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td></td>
                <td><input id="cnorAddr4edit"/></td>
            </tr>
            <tr>
                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;">预约上门时间：</td>
                <td style="border: 0px; vertical-align: middle; ">
                    <input style="width: 94%; background-color: #fff; cursor: pointer" type="text" 
                    name="requireTimeStr4edit" readonly="readonly" class="Wdate" id="requireTimeStr4edit"
					<%--style="height:30px;"--%> value=""
					onFocus="WdatePicker({isShowClear:false,startDate: '%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
				</td>
            </tr>

        </table>
        <hr style="margin-top:20px;margin-bottom:20px; border-top:1px solid #cccccc;"/>
        <div class="pull-right">
            <div class="btn btn-default" style="margin-right:5px;" id="confirmEditReserveOrderBtn"><i
                    class="icon-ok"></i>确定
            </div>
            <div class="btn btn-default" id="closeEditReserveOrderPanel"><i class="icon-remove"></i>取消</div>
        </div>
    </div>
</div>