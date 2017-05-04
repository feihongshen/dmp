//唐富忠 电子秤优化
//所有数据和状态信息都只是保存在plugin_getWeight_object相关的几个页面元素中
//在首页登录后，右上角的控制面板--电子秤插件，打开查看，对应显示文件pluginsmessage.jsp

window.setInterval("pluginLoaded()", 1000);
	
	    var comIndex = 0; //轮询所有串口的计数器
		var current_com = null;//正确读到数据的串口
		var arrayPorts = null;//能打开的所有串口
	    var lastReadTime = new Date();//最近的读数时间
		var plugin_getWeight_ok = false;//成功读取到数据
		var portSerial = null;
		
        function plugin0()
        {
            return document.getElementById('plugin_getWeight_object');
        }
        plugin = plugin0;
        
        function recv(bytes, size)
        {
		    var tostring=bin2String(bytes);
			if (bytes.length != 14 )
			{
				//如果之前读取到正确数据，则不关闭。因为有时候会返回长度28的数据，可能是dll的bug.
				if (!plugin_getWeight_ok)
				{					
					portSerial.close();
				}
			}
			
			plugin_getWeight_ok = true;
			lastReadTime = new Date();
			document.getElementById('weightNumber_Old_String').value=tostring;
			var weightNumber = tostring;
			weightNumber = weightNumber.replace("wn", "").replace("kg", "");//原字符串wn00000.81kg
			weightNumber = parseWeight_(weightNumber);
			document.getElementById('weightNumber').value=weightNumber;
        }
		
		
		function bin2String(array) {
           return String.fromCharCode.apply(String, array);
		}
		
		function parseWeight_(number_) {

			try {
				if (isNaN(number_)) {
					return -1;
				}		
			} catch (err) {
				return -1;
			}

			try {
				number_ = parseFloat(number_);
			} catch (err) {
				return -1;
			} finally {
				if (isNaN(number_)) {
					return -1;
				}
			}
			return number_;
		}

		
function initPorts()
{
    if (portSerial == null)
	{
	  portSerial = plugin().Serial;// 只初始化一次
	}
	arrayPorts = portSerial.getports();//获得已经打开的端口
}
         
function pluginLoaded() 
{

  initPorts();
  
  check_firefoxpluginStatus();
  
  var message = getMessage();
   
  if (comIndex >= arrayPorts.length || plugin_getWeight_ok)
  {
    document.getElementById('plugin_getWeight_message').innerHTML=message;
    return ;
  }
   
  if (portSerial.is_open())
  {
	portSerial.close();
  }
   
    current_com = arrayPorts[comIndex];
	message = message+' current_com='+current_com;
	comIndex++;
	
	portSerial.open(current_com);// Open a port
	portSerial.set_option(9600,0,8,0,0);// Set port options 
	portSerial.recv_callback(recv); // Callback function for recieve data
  
    document.getElementById('plugin_getWeight_message').innerHTML=message;
}

function getMessage()
{
  var message = ''+'最近读数时间='+lastReadTime.getHours()+':'+lastReadTime.getMinutes()+':'+lastReadTime.getSeconds()+'<br>';
  message = message + ' 正确读取数据='+plugin_getWeight_ok+'<br>';
  message = message + ' 本机所有打开的串口个数='+arrayPorts.length+'<br>';
  message = message + ' 当前串口='+current_com+'<br>';
  for(var i=0;i<arrayPorts.length;i++)
  {
    message = message + ' port'+i+'='+arrayPorts[i];
  }
  return message;
}
 
//检查电子秤插件是否加载成功
function check_firefoxpluginStatus()
{
	try
	{
		if(plugin().valid){
		    document.getElementById('firefoxpluginStatus').innerText='电子秤插件加载:成功';
		} else {
			document.getElementById('firefoxpluginStatus').innerText='电子秤插件加载:失败';
		}
	}
	catch(err)
	{
		document.getElementById('firefoxpluginStatus').innerText='电子秤插件加载:失败';
	}
}
        
        