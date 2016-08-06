package cn.explink.param;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.explink.util.Tools;
import cn.explink.util.MD5.MD5Util;

/**
 * 与武汉飞远中间件通信时使用的参数vo
 * 
 * @author mali 2016年3月24日
 *
 */
public class AutoAllocationParam {

	private String serialNo;//序列号
	private String act;//动作
	private String orderid;//订单号
	private String export;//出口
	private String direction;//方向0正向和1反向
	private String station;//站点中文
	
	private static final String GRANTDIRECTION="whfy";
	
	public static final String SERIALNO="serialno";
	public static final String ACT="act";
	public static final String ORDERID="orderid";
	public static final String EXPORT="export";
	public static final String DIRECTION="direction";
	public static final String STATION="station";
	
	public static final String ADDQUEUE="0";
	public static final String QUERYQUEUE="1";
	public static final String DELETEHEAD="-1";
	public static final String DELETETAIL="-2";
	public static final String CLEARQUEUE="-99";
	public static final String INITQUEUE="99";
	
	public AutoAllocationParam(){
		super();
		this.serialNo = String.valueOf((new Date().getTime()));
	}
	
	public AutoAllocationParam(String orderid,String export,String direction,String station){
		this.serialNo = String.valueOf((new Date().getTime()));
		this.orderid=orderid;
		this.export=export;
		this.direction=direction;
		this.station=station;
		
	}
	
	
	/**
	 * 把参数转换为map格式
	 * @return
	 */
	public Map<String,String> toMap(){
		Map<String,String> map=new HashMap<String,String>();
		map.put(SERIALNO,this.getSerialNo());
		map.put(ACT,this.getAct());
		map.put(ORDERID,this.getOrderid());
		map.put(EXPORT,this.getExport());
		map.put(DIRECTION,this.getDirection());
		map.put(STATION,this.getStation());
		return map;
		
	}
	
	/**
	 * 把参数转换为String格式
	 * <root><serialno>1234567890</serialno><act>0</act><orderid>123456</orderid><export>950</export><direction>0</direction><station>光谷</station></root>
	 * @return
	 */
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append("<root>");
		sb.append("<"+SERIALNO+">").append(this.getSerialNo()).append("</"+SERIALNO+">");
		sb.append("<"+ACT+">").append(this.getAct()).append("</"+ACT+">");
		sb.append("<"+ORDERID+">").append(this.getOrderid()).append("</"+ORDERID+">");
		sb.append("<"+EXPORT+">").append(this.getExport()).append("</"+EXPORT+">");
		sb.append("<"+DIRECTION+">").append(this.getDirection()).append("</"+DIRECTION+">");
		sb.append("<"+STATION+">").append(this.getStation()).append("</"+STATION+">");
		sb.append("</root>");
		
		return sb.toString();
		
	}
	
	public static AutoAllocationParam getInitParams(){
		AutoAllocationParam params=new AutoAllocationParam();
		params.setAct(INITQUEUE);
		String currentTime = Tools.getCurrentTime("yyyy-MM-dd HH:mm:ss.SSS");
		params.setOrderid(getInitOrderId(currentTime, GRANTDIRECTION));
		params.setExport(currentTime);
		params.setDirection(GRANTDIRECTION);
		params.setStation("");
		return params;
	}
	
	/**
	 * 返回动作名称
	 *  act为 0  时   添加队列
	 *  act为 1  时   队列查询
	 *	act为 -1 时   删除列首
	 *	act为 -2 时   删除列尾
	 *	act为 -99时   清空对列
	 *	act为 99 时     初始化授权
	 * @param act
	 * @return
	 */
	public static String getActName(String act){
		if(null==act){
			return "";
		}else if(ADDQUEUE.equals(act)){
			return "添加队列";			
		}else if(QUERYQUEUE.equals(act)){
			return "队列查询";			
		}else if(DELETEHEAD.equals(act)){
			return "删除列首";			
		}else if(DELETETAIL.equals(act)){
			return "删除列尾";			
		}else if(CLEARQUEUE.equals(act)){
			return "清空队列";			
		}else if(INITQUEUE.equals(act)){
			return "初始化授权";			
		}else{
			return "";
		}
	}
	/**
	 * @return the serialNo
	 */
	public String getSerialNo() {
		return serialNo;
	}
	/**
	 * @param act the serialNo to set
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	/**
	 * @return the act
	 */
	public String getAct() {
		return act;
	}
	/**
	 * @param act the act to set
	 */
	public void setAct(String act) {
		this.act = act;
	}
	/**
	 * @return the orderid
	 */
	public String getOrderid() {
		return orderid;
	}
	/**
	 * @param orderid the orderid to set
	 */
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	/**
	 * @return the export
	 */
	public String getExport() {
		return export;
	}
	/**
	 * @param export the export to set
	 */
	public void setExport(String export) {
		this.export = export;
	}
	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}
	/**
	 * @return the station
	 */
	public String getStation() {
		return station;
	}
	/**
	 * @param station the station to set
	 */
	public void setStation(String station) {
		this.station = station;
	}
	
	/**
	 * orderid为MD5(MD5(MD5(MD5(Export + Direction)+“x”)+“i”)+“e”)
	 * export为当前时间精确到毫秒 2010-10-10 10:10:10.999
	 * @return
	 */
	private static String getInitOrderId(String time, String grantdirection){
		String orderId=MD5Util.md5(MD5Util.md5(MD5Util.md5(MD5Util.md5(time+GRANTDIRECTION)+"x")+"i")+"e");
		return orderId;
	}
	
	
}
