package cn.explink.util.baiduAPI;

import java.util.Random;

/**
 *
 * 百度api中必须用的key。此key可以在百度LBS开放平台申请 (http://lbsyun.baidu.com/apiconsole/key)
 * 每个key每天可以试用10万次，所以，这个Key池每天可以提供100万次的调用
 *
 * @author songkaojun 2015年5月13日
 */
public class BaiduApiKeyPool {
	private static final String[] apiKeys = new String[] { "DEl20Y464grmB8NX8ci7Xox7", "8SVNm6WSHuTHhHQcHC2Xvo9Y", "iGn6yWmgelj90pWGGqlvgiC3", "x381xCSw4MW2pOl84TgPuQHY", "m77jEXysEaE3IwKxffaakNdh",
			"OpButuNtd3fsi3cs1MU5cPTB", "XLotWMIB6uIYXCEoUwX7fA7X", "pSKhFlLrdXfqO8KpRwyG1Gck", "arAciBpzWtCjD96R9qPasPlU", "PoKvj1wkjxLxbM34XCL81Yui" };

	public static String getRandomKey() {
		Random random = new Random();
		int nextInt = random.nextInt(BaiduApiKeyPool.apiKeys.length);
		return BaiduApiKeyPool.apiKeys[nextInt];
	}
}
