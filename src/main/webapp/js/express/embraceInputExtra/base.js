var date_pattern = "yyyy-MM-dd HH:mm:ss";
/**
 * 基础JS库
 */
// 过滤xss攻击,返回xss安全字符串,并过滤<script>标签
function doFilterXSSWithStripIgnore(html) {
	if (!!html) {
		return filterXSS(html, {
			whiteList : [],
			stripIgnoreTag : true, // 过滤所有非白名单标签的HTML
			stripIgnoreTagBody : [ 'script' ]
		// script标签较特殊，需要过滤标签中间的内容
		});
	} else {
		return "";
	}
};

// 日期格式化
function formattime(val) {
	var year = parseInt(val.year) + 1900;
	var month = (parseInt(val.month) + 1);
	month = month > 9 ? month : ('0' + month);
	var date = parseInt(val.date);
	date = date > 9 ? date : ('0' + date);
	var hours = parseInt(val.hours);
	hours = hours > 9 ? hours : ('0' + hours);
	var minutes = parseInt(val.minutes);
	minutes = minutes > 9 ? minutes : ('0' + minutes);
	var seconds = parseInt(val.seconds);
	seconds = seconds > 9 ? seconds : ('0' + seconds);
	var time = year + '-' + month + '-' + date + ' ' + hours + ':' + minutes
			+ ':' + seconds;
	return time;
}

/**
 * 将1，0转换为是否
 * @param val
 * @returns {String}
 */
function formatbyte(val) {
	if (val == 1) {
		return "是";
	} else {
		return "否";
	}
}

function formatdatetime(value,data_pattern_org) {
	if (value == null) {
		return "";
	}
	if(!data_pattern_org){
		data_pattern_org = date_pattern;
	}
	var unixTimestamp = new Date(value);
	return dateToStr(unixTimestamp,data_pattern_org);
}

function dateToStr(value,data_pattern_org) {
	return baidu.date.format(value, data_pattern_org);
}

function isDateStr(str) {
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
	return reg.test(str);
}

function isEmpty(str) {
	return str == null || str == "" || str == undefined;
}

var showVendorPicker = function() {
	var url = contextPath + "/vendor/picker";
	art.dialog.open(url, {
		title : '选择供应商',
		width : 620,
		height : 400,
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				alert('iframe还没加载完毕呢');
				return false;
			}
			;
			var vendorNameHidden = iframe.document
					.getElementById('vendorNameHidden');
			var portalVendorIdHidden = iframe.document
					.getElementById('portalVendorIdHidden');
			var vendorCodeHidden = iframe.document
					.getElementById('vendorCodeHidden');
			var vendorName = $(vendorNameHidden).val();
			var portalVendorId = $(portalVendorIdHidden).val();
			var vendorCode = $(vendorCodeHidden).val();
			if (!!vendorCode && vendorCode != "") {
				$("#clientName").val(vendorName);
				$("#clientCode").val(vendorCode);
				$("#vendorName").val(vendorName);
				$("#vendorCode").val(vendorCode);
			}
		},
		cancel : true
	});
};

var showMessage=function(){
	var msg=doFilterXSSWithStripIgnore($("#message").text());
	if(!!msg&&msg!=""){
		art.dialog(msg, function(){});
	}
};

var showResult=function(){
	var msg=doFilterXSSWithStripIgnore($("#message").text());
	$("#message").attr("visibility","visible");
};

function sendAjax(url, params, successCallBack, errorCallBack) {
	$.ajax({
		url : url,
		type : "POST",
		dataType : "JSON",
		data : params,
		async : false,
		success : successCallBack,
		error : errorCallBack
	});
}

function padSpace(str, len, lOrR) {
	str = String(str);
	var padStr = "";
    if(str.length<len){  
        for(var i=0;i<len-str.length;i++){  
            padStr += " ";
        }  
    }
    if(lOrR == 'L' || lOrR == 'l'){
    	str = padStr + str;
    } else if(lOrR == 'R' || lOrR == 'r'){
    	str = str + padStr;
    }
    return str;
}

function padTable(table) {
	var text = "";
	text = padTableSpace(table, "th", text, false);
	text = padTableSpace(table, "td", text, true);
	return text;
}

function padTableSpace(table, row, text, isSep) {
	$("#" + table).find("tr").each(function(){
		$(this).find(row).each(function(){
			var label = "";
			var value = "";
			if ($(this).is(':has(label)') ) {
				label = padSpace($.trim($(this).text()), 8, "l");
			} else {
				value = padSpace($.trim($(this).text()), 16, "r");
			}
			text += (label + value + "	");
		});
		if (isSep) {
			text += "\n";
		}
	});
	return text;
}

jQuery.fn.serializeObject = function() {
	  var arrayData, objectData;
	  arrayData = this.serializeArray();
	  objectData = {};

	  $.each(arrayData, function() {
	    var value;

	    if (this.value != null) {
	      value = this.value;
	    } else {
	      value = '';
	    }

	    if (objectData[this.name] != null) {
	      if (!objectData[this.name].push) {
	        objectData[this.name] = [objectData[this.name]];
	      }

	      objectData[this.name].push(value);
	    } else {
	      objectData[this.name] = value;
	    }
	  });

	  return objectData;
};

String.prototype.startWith = function(str) {
	var reg = new RegExp("^" + str);
	return reg.test(this);
}

String.prototype.endWith = function(str) {
	var reg = new RegExp(str + "$");
	return reg.test(this);
}
Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i] === obj) {
            return true;
        }
    }
    return false;
}
Date.prototype.format = function (mask)
{
    var d = this;
    var zeroize = function (value, length)
    {
        if (!length) length = 2;
        value = String(value);
        for (var i = 0, zeros = ''; i < (length - value.length); i++)
        {
            zeros += '0';
        }
        return zeros + value;
    };
 
    return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0)
    {
        switch ($0)
        {
            case 'd': return d.getDate();
            case 'dd': return zeroize(d.getDate());
            case 'ddd': return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
            case 'dddd': return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
            case 'M': return d.getMonth() + 1;
            case 'MM': return zeroize(d.getMonth() + 1);
            case 'MMM': return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
            case 'MMMM': return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
            case 'yy': return String(d.getFullYear()).substr(2);
            case 'yyyy': return d.getFullYear();
            case 'h': return d.getHours() % 12 || 12;
            case 'hh': return zeroize(d.getHours() % 12 || 12);
            case 'H': return d.getHours();
            case 'HH': return zeroize(d.getHours());
            case 'm': return d.getMinutes();
            case 'mm': return zeroize(d.getMinutes());
            case 's': return d.getSeconds();
            case 'ss': return zeroize(d.getSeconds());
            case 'l': return zeroize(d.getMilliseconds(), 3);
            case 'L': var m = d.getMilliseconds();
                if (m > 99) m = Math.round(m / 10);
                return zeroize(m);
            case 'tt': return d.getHours() < 12 ? 'am' : 'pm';
            case 'TT': return d.getHours() < 12 ? 'AM' : 'PM';
            case 'Z': return d.toUTCString().match(/[A-Z]+$/);
            // Return quoted strings with the surrounding quotes removed
            default: return $0.substr(1, $0.length - 2);
        }
    });
};