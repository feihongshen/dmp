<%@page import="cn.explink.domain.Menu,cn.explink.domain.Role"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Menu> menuList = (List<Menu>)request.getAttribute("menus");
    List<Menu> menuPDAList = (List<Menu>)request.getAttribute("PDAmenu");
	List<Long> role_menuList = (List<Long>)request.getAttribute("role_menu");
%>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var initMenuList = new Array();
<%int i=0 ; for(Long role_menu:role_menuList){%>initMenuList[<%=i++ %>]="<%=role_menu %>";<%}%>
</script>
<div id="box_bg"></div>
<div id="box_contant" style="width:680px">
		<div id="box_in_bg">
			<h1>
				<div id="close_box" onclick="closeBox()"></div>
				<b>${role.rolename}-</b>权限设置:</h1>
			<form id="role_editroleandmenu_Form" name="role_editroleandmenu_Form" onsubmit="submitEditRoleAndMenuForm(this);return false;" method="POST" action="<%=request.getContextPath()%>/role/saveRoleAndMenu/${role.roleid}" >
			<div class="right_setin">
				<div class="right_set1" style="height:500px; overflow-y:auto">
					
						<div id="accordion" >
						  <% for(Menu menu: menuList){
							if(menu.getParentid()==0){
						  %>
							<div class="set_two">
								<h2><label><input type="checkbox" id="cb_<%=menu.getId() %>" name="menu" onclick="checkMenu($(this).val())"  value="<%=menu.getId() %>" ><b><%=menu.getName() %></b></label><a href="javascript:;" onclick="checkAll(<%=menu.getId() %>)">[全选]</a> </h2>
								
								<div style ="display:none ;" id="menu_<%=menu.getId() %>" class="set_three">
								<% for(Menu menu_1: menuList){ 
									if(menu_1.getParentid()==menu.getId()){
								%>
									<ul>
										<li>
											<h3>
												<label>
													<input type="checkbox" id="cb_<%=menu_1.getId() %>" name="menu" onclick="checkMenu($(this).val())" value="<%=menu_1.getId() %>" >
													<%=menu_1.getName() %></label>
												<a href="javascript:;" onclick="checkAll(<%=menu_1.getId() %>)">[全选]</a> </h3>
											<div id="menu_<%=menu_1.getId() %>" style ="display:none ;" >
											<ul>
											<% for(Menu menu_2: menuList){ 
												if(menu_2.getParentid()==menu_1.getId()){
											%>
													<li>
														<label>
															<input type="checkbox" id="cb_<%=menu_2.getId() %>" name="menu" onclick="checkMenu($(this).val())" value="<%=menu_2.getId() %>" >
															<%=menu_2.getName() %></label>
													</li>
												<%}} %>
												</ul>
											</div>
										</li>
									</ul>
									<%}} %>
								</div>
							</div>
							<%}} %>
							
						</div>
						
				</div>
			</div>
			<div align="center"><input type="submit" id="save" value="保存" class="button">
			<input type="button" onclick="closeBox()" value="取消" class="button"></div>
		</form>
		</div>
	</div>

