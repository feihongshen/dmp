$().ready(function(){
	 $("select[id*=branch]").each(function(){
		 LoadInputer($(this)[0].id);	 
	 });

}
);



    
    function LoadInputer(idkey) {
        //数据支持json数据格式，可以自己扩展
    	//var tempData = "{text:'<font color=blue>请选择</font>',value:'0'},";
    	var tempData = "";
    	var tempText="";
    	var tempVal="";
    	 $("#"+idkey+" option").each(function(){
         	if($(this)[0].selected&&$(this).val()!=0||$(this).val()==$("#"+idkey).val()){
         	//	tempData = "{text:'"+$(this).text()+"',value:'"+$(this).val()+"'},";
         		tempText=$(this).text();
         		tempVal=$(this).val();
         	}
         	tempData += "{text:'"+$(this).text()+"',value:'"+$(this).val()+"'},";
         });
        
        tempData = eval("[" + tempData + "]");
        //数据可以用ajax到服务器去取就可以了
        $("#"+idkey).before("<input type='text' id='"+idkey+"'/>");
        $("input[id="+idkey+"]").attr('onchange',$("select[id="+idkey+"]").attr("onchange"));
        $("select[id="+idkey+"]").remove();
        var obj = $("input[id="+idkey+"]");
//        var obj = $("input[viewId="+idkey+"]");
        obj.inputer(idkey, tempData, 0, null);
        $("input[id="+idkey+"]").val(tempText);
 		$("input[name="+idkey+"]").val(tempVal);
       

    }



