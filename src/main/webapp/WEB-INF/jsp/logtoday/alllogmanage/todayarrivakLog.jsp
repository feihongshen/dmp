<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>今日站点日志</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
</head>
<%
List<BranchTodayLog> todayLogList  = (List<BranchTodayLog>)request.getAttribute("todayLogList");
Map branchMap = (Map)request.getAttribute("branchMap");
%>
<script>
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}
</script>
<script type="text/javascript">
$(function(){
$("#right_hideboxbtn").click(function(){
			var right_hidebox = $("#right_hidebox").css("right")
			if(
				right_hidebox == -400+'px'
			){
				$("#right_hidebox").css("right","10px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn2.gif)");
				
			};
			
			if(right_hidebox == 10+'px'){
				$("#right_hidebox").css("right","-400px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn.gif)");
			};
	})
});
</script>
<script language="javascript">
$(function(){
	var $menuli = $(".uc_midbg ul li");
	var $menulilink = $(".uc_midbg ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
})
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
<div class="menucontant">
<div class="inputselect_box" style="top: 0px; ">
		<form action="1" method="post" id="searchForm">
		  &nbsp;&nbsp;当前日期：<%=new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>
		</form>
	</div>
	<div class="jg_35"></div>
	<div class="uc_midbg">
		<ul>
			<li><a href="#" class="light">今日到货日报</a></li>
			<li><a href="#">今日投递日报</a></li>
			<li><a href="#">今日款项日报</a></li>
			<li><a href="#">今日库存日报</a></li>
		</ul>
	</div>
    <div class="tabbox">
    
        <li>
            <div class="right_title">
                <table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
                    <tbody>
                        <tr class="font_1" height="30">
                            <td rowspan="2" align="center" valign="middle" bgcolor="#f3f3f3">站点</td>
                            <td colspan="4" align="center" valign="middle" bgcolor="#E1F0FF">昨日库存</td>
                            <td colspan="4" align="center" valign="middle" bgcolor="#E7F4E3">今日实到货</td>
                            <td rowspan="2" align="center" valign="middle" bgcolor="#FFEFDF">到错货（单）</td>
                            </tr>
                        <tr class="font_1" height="30">
                          <td align="center" valign="middle" bgcolor="#E1F0FF">昨日少货（单）</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">昨日未到货（单）</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">昨日他站到货（单）</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">今日已出库（单）</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">未到货（单）</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">已到货（单）</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">他站到货（单）</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">少货（单）</td>
                            </tr>
                         <%if(todayLogList!=null){ %>  
                         <%for(BranchTodayLog branchTodayLog : todayLogList) {%> 
                         <tr height="30">
                            <td align="center" valign="middle"><strong><%=branchMap.get(branchTodayLog.getBranchid()) %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getYesterday_stortage() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getYesterday_not_arrive() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getYesterday_other_site_arrive() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_out_storehouse() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_not_arrive() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_arrive() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_other_site_arrive() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_stortage() %></strong></td>
                            <td align="center" valign="middle"><strong><%=branchTodayLog.getToday_wrong_arrive() %></strong></td>
                            </tr>
                        <%} }%>
                        </tbody>
                    </table>
            </div>
       </li>
    </div>
			
</div>
<div class="jg_35"></div>
<div class="iframe_bottom" >
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tbody>
				<tr>
				<td height="38" align="center" valign="middle" bgcolor="#FFFFFF">
					<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();">第一页</a>　
					<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();">上一页</a>　
					<a href="javascript:$('#searchForm').attr('action','2');$('#searchForm').submit();">下一页</a>　
					<a href="javascript:$('#searchForm').attr('action','120');$('#searchForm').submit();">最后一页</a>
					　共120页　共1197条记录 　当前第<select id="selectPg" onChange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
							
							<option value="1">1</option>
							
							<option value="2">2</option>
							
							<option value="3">3</option>
							
							<option value="4">4</option>
							
							<option value="5">5</option>
							
							<option value="6">6</option>
							
							<option value="7">7</option>
							
							<option value="8">8</option>
							
							<option value="9">9</option>
							
							<option value="10">10</option>
							
							<option value="11">11</option>
							
							<option value="12">12</option>
							
							<option value="13">13</option>
							
							<option value="14">14</option>
							
							<option value="15">15</option>
							
							<option value="16">16</option>
							
							<option value="17">17</option>
							
							<option value="18">18</option>
							
							<option value="19">19</option>
							
							<option value="20">20</option>
							
							<option value="21">21</option>
							
							<option value="22">22</option>
							
							<option value="23">23</option>
							
							<option value="24">24</option>
							
							<option value="25">25</option>
							
							<option value="26">26</option>
							
							<option value="27">27</option>
							
							<option value="28">28</option>
							
							<option value="29">29</option>
							
							<option value="30">30</option>
							
							<option value="31">31</option>
							
							<option value="32">32</option>
							
							<option value="33">33</option>
							
							<option value="34">34</option>
							
							<option value="35">35</option>
							
							<option value="36">36</option>
							
							<option value="37">37</option>
							
							<option value="38">38</option>
							
							<option value="39">39</option>
							
							<option value="40">40</option>
							
							<option value="41">41</option>
							
							<option value="42">42</option>
							
							<option value="43">43</option>
							
							<option value="44">44</option>
							
							<option value="45">45</option>
							
							<option value="46">46</option>
							
							<option value="47">47</option>
							
							<option value="48">48</option>
							
							<option value="49">49</option>
							
							<option value="50">50</option>
							
							<option value="51">51</option>
							
							<option value="52">52</option>
							
							<option value="53">53</option>
							
							<option value="54">54</option>
							
							<option value="55">55</option>
							
							<option value="56">56</option>
							
							<option value="57">57</option>
							
							<option value="58">58</option>
							
							<option value="59">59</option>
							
							<option value="60">60</option>
							
							<option value="61">61</option>
							
							<option value="62">62</option>
							
							<option value="63">63</option>
							
							<option value="64">64</option>
							
							<option value="65">65</option>
							
							<option value="66">66</option>
							
							<option value="67">67</option>
							
							<option value="68">68</option>
							
							<option value="69">69</option>
							
							<option value="70">70</option>
							
							<option value="71">71</option>
							
							<option value="72">72</option>
							
							<option value="73">73</option>
							
							<option value="74">74</option>
							
							<option value="75">75</option>
							
							<option value="76">76</option>
							
							<option value="77">77</option>
							
							<option value="78">78</option>
							
							<option value="79">79</option>
							
							<option value="80">80</option>
							
							<option value="81">81</option>
							
							<option value="82">82</option>
							
							<option value="83">83</option>
							
							<option value="84">84</option>
							
							<option value="85">85</option>
							
							<option value="86">86</option>
							
							<option value="87">87</option>
							
							<option value="88">88</option>
							
							<option value="89">89</option>
							
							<option value="90">90</option>
							
							<option value="91">91</option>
							
							<option value="92">92</option>
							
							<option value="93">93</option>
							
							<option value="94">94</option>
							
							<option value="95">95</option>
							
							<option value="96">96</option>
							
							<option value="97">97</option>
							
							<option value="98">98</option>
							
							<option value="99">99</option>
							
							<option value="100">100</option>
							
							<option value="101">101</option>
							
							<option value="102">102</option>
							
							<option value="103">103</option>
							
							<option value="104">104</option>
							
							<option value="105">105</option>
							
							<option value="106">106</option>
							
							<option value="107">107</option>
							
							<option value="108">108</option>
							
							<option value="109">109</option>
							
							<option value="110">110</option>
							
							<option value="111">111</option>
							
							<option value="112">112</option>
							
							<option value="113">113</option>
							
							<option value="114">114</option>
							
							<option value="115">115</option>
							
							<option value="116">116</option>
							
							<option value="117">117</option>
							
							<option value="118">118</option>
							
							<option value="119">119</option>
							
							<option value="120">120</option>
							
						</select>页
						
				</td>
			</tr>
		</tbody></table>
	</div>
</div>
</body>
</html>