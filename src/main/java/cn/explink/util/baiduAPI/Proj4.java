
package cn.explink.util.baiduAPI;


/**
 * 坐标变换
 * @author liumurong
 *
 */
public class Proj4
{
	/**
	 * 椭球的偏心率
	 */
	public static final double ee=0.00669342162296594323;
	/**
	 * 卫星椭球坐标投影到平面地图坐标系的投影因子
	 */
	public static final double a=6378245.0;
	/**
	 * 圆周率
	 */
	public static final double pi=3.14159265358979324;
	/**
	 * 你懂的
	 */
	public static final double xpi=3.14159265358979324 * 3000.0 / 180.0;

	
	/**
	 * bd09>84
	 * @param oript
	 * @return
	 */
	public static GeoPoint bd09Towgs84(GeoPoint oript)
	{
		return gcj02Towgs84(bd09Togcj02(oript));
	};
	
	/**
	 * bd09>火星
	 * @param oript
	 * @return
	 */
	public static GeoPoint bd09Togcj02(GeoPoint oript)
	{
		double x = oript.getLng() - 0.0065;
		double y = oript.getLat() - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * xpi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * xpi);
		double glng = z * Math.cos(theta);
		double glat = z * Math.sin(theta);
		return new GeoPoint(glng,glat);
	};

	/**
	 * 火星>84
	 * @param oript
	 * @return
	 */
	public static GeoPoint gcj02Towgs84(GeoPoint oript)
	{
		double initDelta = 0.01;
		double threshold = 0.000000001;     // 
		double dLat = initDelta;
		double dLng = initDelta;
		double mLat = oript.getLat() - dLat, mLng = oript.getLng() - dLng;
		double pLat = oript.getLat() + dLat, pLng = oript.getLng() + dLng;
		double wgsLat, wgsLng, i = 0;
	    while (true)
	    {
	        wgsLat = (mLat + pLat) / 2;
	        wgsLng = (mLng + pLng) / 2;
	        GeoPoint tmp = wgs84Togcj02(new GeoPoint(wgsLng, wgsLat));
	        dLat = tmp.getLat() - oript.getLat();
	        dLng = tmp.getLng() - oript.getLng();
	        if ((Math.abs(dLat) < threshold) && (Math.abs(dLng) < threshold))
	        {
	            break;
	        }
	        if (dLat > 0)
	        {
	            pLat = wgsLat;
	        } else
	        {
	            mLat = wgsLat;
	        }
	        if (dLng > 0)
	        {
	            pLng = wgsLng;
	        } else
	        {
	            mLng = wgsLng;
	        }
	        if (++i > 10000)
	        {
	            break;
	        }
	    }
	    return new GeoPoint(wgsLng,wgsLat);
	};
	
	/**
	 * 84>火星
	 * @param oript
	 * @return
	 */
	public static GeoPoint wgs84Togcj02(GeoPoint oript)
	{
		if (outOfChina(oript))
	    {
	        return oript;
	    }
	    GeoPoint dwgs = transform(oript);
	    if (dwgs == null)
	    {
	        return oript;
	    }
	    double mgLng = oript.getLng() + dwgs.getLng();
	    double mgLat = oript.getLat() + dwgs.getLat();
	    return new GeoPoint(mgLng,mgLat);
	};
	
	/**
	 * 火星>09
	 * @param oript
	 * @return
	 */
	public static GeoPoint gcj02Tobd09(GeoPoint oript)
	{
		double x = oript.getLng();
		double y = oript.getLat();
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y *xpi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * xpi);
		double bd_lng = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		return new GeoPoint(bd_lng,bd_lat);
	};
	/**
	 * 84>09
	 * @param oript
	 * @return
	 */
	public static GeoPoint wgs84Tobd09(GeoPoint oript)
	{
		return wgs84Togcj02(gcj02Tobd09(oript));
	};
	
	/**
	 * 判断给定坐标是否在国外
	 * 国外坐标不用进行坐标变换
	 * @param oript
	 * @return
	 */
	public static Boolean outOfChina(GeoPoint oript)
	{
		if (oript.getLng() < 72.004 || oript.getLng() > 137.8347)
	    {
	        return true;
	    }
	    if (oript.getLat() < 0.8293 || oript.getLat() > 55.8271)
	    {
	        return true;
	    }
	    return false;
	};
	/**
	 * 计算坐标偏移量
	 * wgs84>gcj02
	 * @param oript
	 * @return
	 */
	public static GeoPoint transform(GeoPoint oript)
	{
		if (outOfChina(oript))
	    {
	        return null;
	    }
		double dLat = transformLat(oript.getLng() - 105.0, oript.getLat() - 35.0);
		double dLng = transformLng(oript.getLng() - 105.0, oript.getLat() - 35.0);
		double radLat = oript.getLat() / 180.0 * pi;
		double magic = Math.sin(radLat);
	    magic = 1 - ee * magic * magic;
	    double sqrtMagic = Math.sqrt(magic);
	    dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
	    dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
	    return new GeoPoint(dLng,dLat);
	};
	/**
	 * 计算纬度方向偏移量
	 * @param x
	 * @param y
	 * @return
	 */
	public static double transformLat(double x,double y)
	{
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
	    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
	    ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
	    ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
	    return ret;
	};
	/**
	 * 计算经度方向偏移量
	 * @param x
	 * @param y
	 * @return
	 */
	public static double transformLng(double x,double y)
	{
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
	    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
	    ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
	    ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
	    return ret;
	};
	
	
}


