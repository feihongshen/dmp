package cn.explink.util.baiduAPI;

/**
 * 地理编码\逆地理编码 IGeoCoder接口实现的管理
 *
 * @author Administrator
 *
 */
public class GeoCoder {

	private static GeoCoder _instance = null;
	private IGeoCoder _geoCoder = null;;

	/**
	 * 私有构造函数
	 */
	private GeoCoder() {
		this._geoCoder = new BaiduV2GeoCoder();
	}

	/**
	 * 获取地理编码对象对象
	 *
	 * @return
	 */
	public static GeoCoder getInstance() {
		if (GeoCoder._instance == null) {
			GeoCoder._instance = new GeoCoder();
		}
		return GeoCoder._instance;
	}

	/**
	 * 获取地理编码接口
	 *
	 * @return
	 */
	public IGeoCoder getGeoCoder() {
		return this._geoCoder;
	}


}
