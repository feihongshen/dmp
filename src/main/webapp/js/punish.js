
function selectUser(userid)
{ var dmpurl=$("#dmpurl").val();
	$.ajax({
		type:"post",
		url:dmpurl+"/punish/selectUser",
		data:{"userid":userid},
		dataType:"json",
		success:function(data){
			$("#branchid").val(data.branchid);
		}});
	}
function selectBranch(branchid)
{var dmpurl=$("#dmpurl").val();
$("#userid").empty();
	$.ajax({
		type:"post",
		url:dmpurl+"/punish/selectBranch",
		data:{"branchid":branchid},
		dataType:"json",
		success:function(data){
			if(data.length>0){
			var optstr="";
/*			var optstr="<option value='0'>请选择</option>";*/
			for(var i=0;i<data.length;i++)
				{
				optstr+="<option value='"+data[i].userid+"'>"+data[i].realname+"</option>";
				}
			
			$("#userid").append(optstr);
			}
		}});
	}
function findBranch(branchname)
{var dmpurl=$("#dmpurl").val();
$("#branchid").empty();
$.ajax({
	type:"post",
	url:dmpurl+"/punish/findBranch",
	data:{"branchname":branchname},
	dataType:"json",
	success:function(data){
		if(data.length>0){
			var optstr="";
/*			var optstr="<option value='0'>请选择</option>";*/
			for(var i=0;i<data.length;i++)
			{
				optstr+="<option value='"+data[i].branchid+"'>"+data[i].branchname+"</option>";
			}
			
			$("#branchid").append(optstr);
		}
	}});
}
function create()
{ var dmpurl=$("#dmpurl").val();
	check();
	$.ajax({
		type:"post",
		url:dmpurl+"/punish/create",
		data:$("form").serialize(),
		dataType:"json",
		success:function(data){
			/*alert(data.error);
			if(data.errorCode==0){
			closeBox();
			addSuccess(data);
			}*/
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				$("#WORK_AREA")[0].contentWindow.addSuccess(data);
			}
		}});
	}
function update()
{ var dmpurl=$("#dmpurl").val();
	check();
	$.ajax({
		type:"post",
		url:dmpurl+"/punish/update",
		data:$("form").serialize(),
		dataType:"json",
		success:function(data){
			/*alert(data.error);
			if(data.errorCode==0){
			closeBox();
			editSuccess(data);
			}*/
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				closeBox();
				$("#WORK_AREA")[0].contentWindow.addSuccess(data);
			}
		}});
	}
function updateState()
{ var dmpurl=$("#dmpurl").val();
check();
$.ajax({
	type:"post",
	url:dmpurl+"/punish/updateState",
	data:$("form").serialize(),
	dataType:"json",
	success:function(data){
		/*alert(data.error);
			if(data.errorCode==0){
			closeBox();
			editSuccess(data);
			}*/
		$(".tishi_box").html(data.error);
		$(".tishi_box").show();
		setTimeout("$(\".tishi_box\").hide(1000)", 2000);
		if (data.errorCode == 0) {
			closeBox();
			$("#WORK_AREA")[0].contentWindow.addSuccess(data);
		}
	}});
}
function importData()
{
	$("#update")[0].contentWindow.submitBranchLoad();
}
function submitUpLoad() {
	
	$('#swfupload-control').swfupload('startUpload');
}
function check(){
	var re =/^(-)?(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,2})?$/;
	if($("#punishfee").val()==''){$("#punishfee").val('0.00');}
	if($("#realfee").val()==''){$("#realfee").val('0.00');}
    if (!re.test($("#punishfee").val()))
   {
       alert("扣罚金额输入错误！");
 
       return false;
    } 
    else if (!re.test($("#realfee").val()))
    {
    	alert("实扣金额输入错误！");
    	
    	return false;
    } 
}

function getinfo(cwb)
{
	  var dmpurl=$("#dmpurl").val();
	  $.ajax({
	  	type:"post",
	  	url:dmpurl+"/punish/findCustomer",
	  	data:{"cwb":cwb},
	  	dataType:"json",
	  	success:function(data){
	  		if(data.length>0){
	  			var optstr="";
	  			for(var i=0;i<data.length;i++)
	  			{
	  				optstr+="<option value='"+data[i].customerid+"'>"+data[i].customername+"</option>";
	  			}
	  			$("#customerid").empty();
	  			$("#customerid").append(optstr);
	  		}
	  	}});
}
