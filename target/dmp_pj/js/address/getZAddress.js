var setting = {
		async: {
			enable: true,
			url: getUrl
		},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeClick: beforeClick,
		onClick: onClick,
		beforeExpand: beforeExpand,
		onAsyncSuccess: onAsyncSuccess,
		onAsyncError: onAsyncError
	}
};

var addressId;
var log, className = "dark";
var zTree;
var lastValue = "", nodeList = [], fontCss = {};
var addressLevel;


function beforeClick(treeId, treeNode, clickFlag) {
	className = (className === "dark" ? "":"dark");
	return (treeNode.click != false);
}
function onClick(event, treeId, treeNode, clickFlag) {
	addressId=treeNode.id;
	rulerowclone = $("#deliveryDeilvererRule").clone(true);
	$.ajax({
		 type: "POST",
			url:cxt+"/addressdelivertostation/loaddelivererrule",
			data:{"addressId":addressId},
			dataType:"json",
			success:function(data){	
				$("tr[id=deliveryDeilvererRuleList]").remove();
				if(data != null){
					for(var i=0;i<data.length;i++){
						 var rule = data[i].rule;
						 if(rule == null){
							 rule = "";
						 }
						 var trHtml="<tr align='center' id='deliveryDeilvererRuleList'><td align='center' bgcolor='#FFFFFF'>"+data[i].delivererName+"</td><td align='center' bgcolor='#FFFFFF'>"+rule+"</td><td align='center' bgcolor='#FFFFFF'><input type=hidden value='"+data[i].ruleId+"'/><input type='button' class='delOneRowList' value='删除'/></td></tr>";
						 addTr("rulelist", 0, trHtml);
					}
				}
				$("tr[id=deliveryDeilvererRule]").remove();
				$("#optionRule").before(rulerowclone);
				$("#deliveryDeilvererRule").children().children("#rule").attr("value","");
			}
		});
	addressLevel=treeNode.level;
}	

function getUrl(treeId, treeNode) {
	var url=cxt+"/addressdelivertostation/loadsubtree?addressId="+treeNode.id;
	return url;
}

function beforeExpand(treeId, treeNode) {
	if (!treeNode.isAjaxing) {		
		treeNode.times = 1;
		ajaxGetNodes(treeNode, "refresh");
		return true;
	} else {
		alert("zTree 正在下载数据中，请稍后展开节点。。。");
		return false;
	}
}
function onAsyncSuccess(event, treeId, treeNode, msg) {
	if (!msg || msg.length == 0) {
		treeNode.icon = cxt+"/css/zTree/zTreeStyle/img/loading.gif";
		return;
	}
	//每次最多加载100个
	totalCount = 0;
	if (treeNode.children.length < totalCount) {
		setTimeout(function() {ajaxGetNodes(treeNode);}, perTime);
	} else {
		treeNode.icon = "";
		zTree.updateNode(treeNode);
		if(!treeNode.children[0]){
			treeNode.icon = cxt+"/css/zTree/zTreeStyle/img/loading.gif";
		}else{
			
			zTree.selectNode(treeNode.children[0]);
		}
	}
}
function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
	treeNode.icon = "";
	zTree.updateNode(treeNode);
}
function ajaxGetNodes(treeNode, reloadType) {	
	if (reloadType == "refresh") {
		treeNode.icon = "../../../css/zTreeStyle/img/loading.gif";
		zTree.updateNode(treeNode);
	}
	zTree.reAsyncChildNodes(treeNode, reloadType, true);
}


function expandNode(e) {
	type = e.data.type,
	nodes = zTree.getSelectedNodes();
	if (type.indexOf("All")<0 && nodes.length == 0) {
		alert("请先选择一个父节点");
	}

	if (type == "expandAll") {
		zTree.expandAll(true);
	} else if (type == "collapseAll") {
		zTree.expandAll(false);
	} else {
		var callbackFlag = $("#callbackTrigger").attr("checked");
		for (var i=0, l=nodes.length; i<l; i++) {
			zTree.setting.view.fontCss = {};
			if (type == "expand") {
				zTree.expandNode(nodes[i], true, null, null, callbackFlag);
			} else if (type == "collapse") {
				zTree.expandNode(nodes[i], false, null, null, callbackFlag);
			} else if (type == "toggle") {
				zTree.expandNode(nodes[i], null, null, null, callbackFlag);
			} else if (type == "expandSon") {
				zTree.expandNode(nodes[i], true, true, null, callbackFlag);
			} else if (type == "collapseSon") {
				zTree.expandNode(nodes[i], false, true, null, callbackFlag);
			}
		}
	}
}

function searchVal(valName,treeName){	
	if (event.keyCode!=13) return;  //回车键的键值为13
	event.stopPropagation();
	var target = $.fn.zTree.getZTreeObj(treeName);
	//经过transformToArray转换后是一个Array数组，数组里的每个元素都是object对象，这个对象里包含了node的21个属性。
    var nodes = target.transformToArray(target.getNodes()[0].children);
    var key=$("#"+valName).val();
    //空格回车符 不做查询 直接显示全部
    if(/^\s*$/.test(key)){
     //updateNodes(false); 
     target.showNodes(nodes);
     return;
    }
    //首先隐藏
    target.hideNodes(nodes);
    nodeList=target.getNodesByParamFuzzy("name", key); //模糊匹配
  
    var filterNodes=[];
    for(var i=0;i<nodeList.length;i++){
       filterNodes.push(nodeList[i]);
    }
    target.showNodes(filterNodes);
    for(var i=0;i<filterNodes.length;i++){
     toggle(target,filterNodes[i].getParentNode());
    }
}

function toggle(target,node){
	target.expandNode(node, true, false, false);
	target.showNode(node);
	var parentNode = node.getParentNode();
	if(parentNode){
		 toggle(target,parentNode);
	}
}

function searchTree(){
	treeObj= $.fn.zTree.getZTreeObj("tree");
	var filterString = $("#searchA").val();
	nodeList = treeObj.getNodesByParamFuzzy("name", filterString);
	updateNodes(true);
	var nodes = treeObj.getNodes();
	
	var cloneNodes = [];
	var removeNodes=[];
	$.each(nodes, function(index, node) {
		cloneNodes.push(node);
	});
	treeObj.showNodes(cloneNodes);
	pushRemoveNodes(cloneNodes,removeNodes,filterString);
	treeObj.hideNodes(removeNodes);
}
function updateNodes(highlight) {
	var nodes = zTree.getNodes();
	zTree.hideNodes(nodes);
	
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
	zTree.showNodes(nodeList);
}
function pushRemoveNodes(cloneNodes,removeNodes,filterString){
	
	$.each(cloneNodes, function(index, node) {
		var childrens=node.children;
		if(!childrens)return;
		treeObj.showNodes(childrens);
		if (node.name.indexOf(filterString) != -1) {
			return;
		}else{
			var count=0;
			$.each(childrens,function(ci,cn){
				if(!cn)return;
				//递归查询
				pushRemoveNodes(childrens,removeNodes,filterString);
				if(cn.name.indexOf(filterString)==-1){
					removeNodes.push(cn);
					count++;
				}
			});
			if(count==childrens.length){
				removeNodes.push(node);
			}
		}
		
		
	});
}

function getAll(){
$.ajax({
	 type: "POST",
		url:cxt+"/addressdelivertostation/loadsubtree",
		data:{isBind:true},
		dataType:"json",
		success:function(optionData){
			var opdate = optionData[0];
			opdate.addressLevel = null;
			opdate.addressTypeId = null;
			opdate.id = 1;
			opdate.isParent = true;
			opdate.name = "中国";
			opdate.open = true;
			opdate.pId = -1;
			opdate.parentId = -1;
			opdate.path = null;			
	        var t = $("#tree");
            zTree = $.fn.zTree.init(t, setting, opdate);
			
		}
	});
}


function refreshtree(){
	$.ajax({
		 type: "POST",
			url:cxt+"/addressdelivertostation/loadsubtree?addressId=1",
			data:{isBind:true},
			dataType:"json",
			success:function(optionData){
				var opdataArr = new Array();
				var opdate =[];
				opdate.addressLevel = null;
				opdate.addressTypeId = null;
				opdate.id = 1;
				opdate.isParent = true;
				opdate.name = "中国";
				opdate.open = true;
				opdate.pId = -1;
				opdate.parentId = -1;
				opdate.path = null;					
				opdataArr[0] = opdate;				
				for(var i=0;i<optionData.length;i++){
					opdataArr[i+1] = optionData[i];
				}
		        var t = $("#tree");
	            zTree = $.fn.zTree.init(t, setting, opdataArr);
				
			}
		});
	}

var rulerowclone;
function saveRule(){
	if(addressLevel<4){
		$(".addresstishi_box").html("请选择区县级以下进行绑定！");
		$(".addresstishi_box").show();
		setTimeout("$(\".addresstishi_box\").hide(1000)", 2000);
		return;
	}
	deliveryDeilvererRule="";
	rulerowclone = $("#deliveryDeilvererRule").clone(true);
   	var len=$(".deliveryDeilvererId").length;
   	//用#拼接参数字段
		$(".deliveryDeilvererId").each(function(j){
			var c='.rule:eq('+j+')';
			var rule=$(c).val();
			var val=$(this).val();			
			if(val == ""){
				alert("小件员不能为空！");
				return;
			}
			if(val){
				deliveryDeilvererRule+=val+"#"+rule+"#"+addressId;
			}
			if(len!=j){
				deliveryDeilvererRule+="&";
			}
		});
		if(deliveryDeilvererRule){
			$.ajax({
				 type: "POST",
					url:cxt+"/addressdelivertostation/savedelivererrule",
					data:{"deliveryDeilvererRule":deliveryDeilvererRule,"addressId":addressId},
					dataType:"json",
					success:function(data){	
						$(".addresstishi_box").html(data.error);
						$(".addresstishi_box").show();
						setTimeout("$(\".addresstishi_box\").hide(1000)", 2000);
						$.ajax({
							    type: "POST",
								url:cxt+"/addressdelivertostation/loaddelivererrule",
								data:{"addressId":addressId},
								dataType:"json",
								success:function(data){		
									$("tr[id=deliveryDeilvererRuleList]").remove();
									if(data != null){
										for(var i=0;i<data.length;i++){
											var rule = data[i].rule;
											 if(rule == null){
												 rule = "";
											 }
											 var trHtml="<tr align='center' id='deliveryDeilvererRuleList'><td align='center' bgcolor='#FFFFFF'>"+data[i].delivererName+"</td><td align='center' bgcolor='#FFFFFF'>"+rule+"</td><td align='center' bgcolor='#FFFFFF'><input type=hidden value='"+data[i].ruleId+"'/><input type='button' class='delOneRowList' value='删除'/></td></tr>";
											 addTr("rulelist", 0, trHtml);
										}
									}
									$("tr[id=deliveryDeilvererRule]").remove();
									$("#optionRule").before(rulerowclone);
									$("#deliveryDeilvererRule").children().children("#rule").attr("value","");
								}
						}); 
					}
				});
		}
}

/**
 * 为table指定行添加一行
 *
 * tab 表id
 * row 行数，如：0->第一行 1->第二行 -2->倒数第二行 -1->最后一行
 * trHtml 添加行的html代码
 *
 */
function addTr(tab, row, trHtml){
   //获取table最后一行 $("#tab tr:last")
   //获取table第一行 $("#tab tr").eq(0)
   //获取table倒数第二行 $("#tab tr").eq(-2)
   var $tr=$("#"+tab+" tr").eq(row);
   if($tr.size()==0){
      alert("指定的table id或行数不存在！");
      return;
   }
   $tr.after(trHtml);
}



var pureStation;
function initStations(data){
	$(".deliveryDeilvererRule").each(function(i){
		if(i==0){
			return;
		}
		$(this).remove();
	});
	$("#deliveryDeilvererId").empty();
	optionData=data.rows;
	for(var i=0;i<optionData.length;i++){
		var option=$("  <option value="+optionData[i]['id']+">"+optionData[i]['name']+"</option>");
		$("#deliveryDeilvererId").append(option);
		
	}
	backNode=$("#deliveryDeilvererRule").clone(true);
}

function initOption(){
	 $.ajax({
		 type: "POST",
			url:cxt+"/addressdelivertostation/loaddelivererrule",
			dataType:"json",
			success:function(optionData){
				for(var i=0;i<optionData.length;i++){
					var option=$("  <option value="+optionData[i]['id']+">"+optionData[i]['text']+"</option>");
					$("#deliveryDeilvererId").append(option);
					
				}
				
				backNode=$("#deliveryDeilvererRule").clone(true);
			}
		});
}


var detailRow={
		idField : 'id',
		title : '关联站点',
		url : cxt+'/addressdelivertostation/loaddelivererrule?addressId=0',
		fit : false,
		height : 300,
		loadMsg : '数据加载中...',
		pageSize : 10,
		pagination : true,
		pageList : [ 10, 20, 30 ],
		sortOrder : 'asc',
		rownumbers : true,
		singleSelect : true,
		fitColumns : true,
		showFooter : true,
		frozenColumns : [ [] ],
		columns : [ [
				{
					field : 'id',
					title : '编号',
					hidden : true,
					sortable : true
				},
				{
					field : 'opt',
					title : '操作',
					width : 10,
					formatter : function(value, rec, index) {
						if (!rec.id) {
							return '';
						}
						var href = '';
						href += "[<a href='#' onclick=delObj('deleteImportAddressResult?id="
								+ rec.id + "','address')>";
						href += "删除</a>]";
						return href;
					}
				}, 

				{
					field : 'name',
					title : '站点名称',
					width : 50,
					sortable : true
				}

		] ],
		onLoadSuccess : function(data) {
			initStations(data);
			$("#stationList").datagrid("clearSelections");
		},
		onClickRow : function(rowIndex, rowData) {
			rowid = rowData.id;
			gridname = 'stationList';
		}
	};

