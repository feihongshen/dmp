<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page isErrorPage="true" %>
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

  <link rel="stylesheet" href="css/NotesViewdefault.css" type="text/css">
<title>出错了</title>
</head>
<body>
<br>
<table width=100% align="center">
  <tr>
    <td class=Vwtable><TABLE CLASS="MainTable" WIDTH="100%" BORDER=0 CELLSPACING=0 CELLPADDING=0>
      <TR VALIGN=top>
        <TD WIDTH="100%"><TABLE WIDTH="100%" BORDER=0 CELLSPACING=0 CELLPADDING=0>
          <TR VALIGN=top>
            <TD WIDTH="100%" COLSPAN=4><TABLE WIDTH="100%" BORDER=0 CELLSPACING=0 CELLPADDING=0>
              <TR VALIGN=top>
                <TD CLASS="ViewTable" STYLE="height:18" WIDTH="20%"><FONT SIZE=2> </FONT>
                  <IMG SRC="img/s.gif" WIDTH=12 HEIGHT=8 valign=middle> <B><FONT SIZE=2>错误信息</FONT></B></TD>
                <TD STYLE="padding-right:8px" WIDTH="24%"><IMG SRC="img/q.gif" WIDTH=17 HEIGHT=18></TD>
              </TR>
            </TABLE></TD>
          </TR>
        </TABLE>
              <table width=100% align="center">
                <tr>
                  <td style="padding-left:0px;padding-right:0px"><table width=100%>
                    <tr>
                      <td class=VwCtd style="padding-top:0px"><table width=100% class=Vwtable cellspacing=1>
                        <tr>
                          <td class=VwStd><table width="100%" class=VwTable cellspacing="1" height="23">
                          <%if(exception!=null){%>
						  <tr class=VwCtr>
                                    <td  align="center"><font color="red">请联系管理员<a href="http://www.explink.cn" target="_blank">www.explink.cn</a></font></td>
                                </tr>
                            <tr class=VwCtr>
                                    <td  align="center">
									<img src="img/error.jpg">									</td>
                                </tr>
                                  <tr class=VwCtr style="display:none">
                                    <td  align="left"><%= exception.toString() %></td>
                                  </tr>
                                  <tr class=VwCtr>
                                    <td align="left"></td>
                                  </tr>
                                  <%}%>
                                </table>
                          </table>
                    </TABLE>
            </TABLE>
        </TABLE>
</table>
</body>
</html>
