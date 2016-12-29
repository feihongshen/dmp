package cn.explink.b2c.mss;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;

/**
 * @author xxx
 * @version 2.0
 */
public class SignUtil {

	/**
	 * 公钥(可在o3系统开发者信息中生成)
	 */
	private static final String ACCESS_KEY = "xxxxx";

	/**
	 * 私钥(可在o3系统开发者信息中生成)
	 */
	private static final String SECRET_KEY = "xxxx";

	/**
	 * 加密规则:HmacMD5
	 */
	private static final String HMAC_MD5 = "HmacMD5";

	private Map<String, Object> request_map;

	public static void main(String[] args) {

		SignUtil st = new SignUtil();
		st.initRequest();
		st.startRequest();

	}

	/**
	 * 初始化Request参数
	 */
	public void initRequest() {

		long time = System.currentTimeMillis();

		this.request_map = new HashMap();
		// 公钥(可在o3系统开发者信息中生成)
		this.request_map.put("access_key", SignUtil.ACCESS_KEY);
		// 请求的服务
		this.request_map.put("cmd", "o3.biz.order.create");
		// 请求发起时的时间戳
		this.request_map.put("time", time / 1000);
		// API版本号 当前为v2.0
		this.request_map.put("version", "v2.0");
		// request data
		Map<String, Object> body_map = new HashMap<String, Object>();
		body_map.put("channel", "1000");
		body_map.put("city_code", "110000");

		Map<String, Object> con_map = new HashMap<String, Object>();
		con_map.put("address", "xxxxxx");
		con_map.put("tel", "xxxxx");
		con_map.put("name", "xxxxx");
		con_map.put("mobile", "xxxxxx");
		body_map.put("consignee", con_map);

		Map<String, Object> ext_map = new HashMap<String, Object>();

		Map<String, Object> del_map = new HashMap<String, Object>();

		Map<String, Object> pic_map = new HashMap<String, Object>();

		pic_map.put("shop_id", "xxxxxx");
		pic_map.put("address", "xxxxxx");
		pic_map.put("tel", "xxxx");
		pic_map.put("name", "xx");
		pic_map.put("mobile", "xxx");

		del_map.put("pick_up", pic_map);
		del_map.put("production_code", "xxxx");
		del_map.put("delivery_partner_id", "xxxxxxx");

		ext_map.put("delivery", del_map);
		body_map.put("extra_services", ext_map);

		Map<String, Object> ext_meta_map = new HashMap<String, Object>();
		ext_meta_map.put("need_invoice", 2);
		body_map.put("extra_metas", ext_meta_map);

		List<Object> ext_list = new ArrayList<Object>();
		ext_list.add("delivery");
		body_map.put("extra_services_type", ext_list);

		List<Object> goods_list = new ArrayList<Object>();
		Map<String, Object> goods_map = new HashMap<String, Object>();
		goods_map.put("price", 0);
		goods_map.put("name", "xxxxxx");
		goods_map.put("quantity", 1);
		goods_map.put("partner_goods_id", "xxxxxx");
		goods_map.put("specs", "箱");
		goods_list.add(goods_map);
		body_map.put("goods", goods_list);

		body_map.put("partner_id", "xxxxx");
		body_map.put("partner_order_id", "xxxxxx");
		body_map.put("shop_id", "xxxxxxx");
		body_map.put("partner_order_created_at", "xxxxxx");
		body_map.put("barcode", "xxxxxx");

		// 对request data排序 如果里面是List,Map也要进行排序
		body_map = SignUtil.sortMapByKey(body_map);
		this.request_map.put("body", body_map);

		// 请求唯一标识(UUID)
		this.request_map.put("ticket", this.getTicket());

		// 签名
		this.request_map.put("sign", SignUtil.getSign(this.request_map, SignUtil.SECRET_KEY));

		this.request_map = SignUtil.sortMapByKey(this.request_map);
	}

	/**
	 * 生成请求唯一标识
	 *
	 * @return 唯一标识
	 */
	public String getTicket() {

		UUID uuid = UUID.randomUUID();
		return uuid.toString().toUpperCase();
	}

	/**
	 * 获取sign
	 *
	 * @param request_map
	 *            用于签名的对象
	 * @return 签名字符串
	 */
	public static String getSign(Map<String, Object> request_map, String secret_key) {

		String json_str = SignUtil.toJson(SignUtil.sortMapByKey(request_map));
		json_str = SignUtil.chineseToUnicode(json_str);
		return SignUtil.encryptToString(json_str, secret_key);
	}

	/**
	 * 将unicode字符串转换成汉字
	 *
	 * @param str
	 *            unicode字符串
	 * @return 汉字
	 */
	public String encodingtoStr(String str) {

		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}

	/**
	 * 所有非ASSIC码字符均转换为对应的Unicode表示
	 *
	 * @param str
	 *            需要转换的字符串
	 * @return String
	 */
	public static String chineseToUnicode(String str) {

		StringBuilder builder = new StringBuilder("");
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			Character.UnicodeScript sc = Character.UnicodeScript.of(ch);
			if (SignUtil.isChinese(ch) || SignUtil.isChinesePunctuation(ch)) {
				builder.append(SignUtil.gbEncoding(ch));
				continue;
			}
			builder.append(ch);
		}
		return builder.toString();
	}

	/**
	 * 根据UnicodeScript方法判断中文字符
	 *
	 * @param c
	 *            中文字符
	 * @return true/false
	 */
	private static boolean isChinese(char c) {

		Character.UnicodeScript sc = Character.UnicodeScript.of(c);
		return sc == Character.UnicodeScript.HAN;
	}

	/**
	 * 根据UnicodeBlock方法判断中文标点符号
	 *
	 * @param c
	 *            中文字符标点
	 * @return true/false
	 */
	public static boolean isChinesePunctuation(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		return (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) || (ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) || (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
				|| (ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS) || (ub == Character.UnicodeBlock.VERTICAL_FORMS);
	}

	/**
	 * 汉字转Unicode
	 *
	 * @param ch
	 *            汉字
	 * @return unicode字符串
	 */
	public static String gbEncoding(char ch) {

		return "\\u" + Integer.toHexString(ch);
	}

	/**
	 * 为Map按key进行升序排序 排序规则:按照 ASSIC 编码升序
	 *
	 * @param map
	 *            需排序Map
	 * @return 排序后的Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> sortMapByKey(Map<String, Object> map) {

		if ((map == null) || map.isEmpty()) {
			return null;
		}

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof List) {
				entry.setValue(SignUtil.iteratorList(value));
			} else if (value instanceof Map) {
				entry.setValue(SignUtil.sortMapByKey((Map<String, Object>) value));
			}
		}

		Map<String, Object> sortMap = new TreeMap<String, Object>(new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {

				return lhs.compareTo(rhs);
			}
		});

		sortMap.putAll(map);

		return sortMap;
	}

	/**
	 * 对List中的递归排序
	 *
	 * @param list_obj
	 *            List
	 * @return Object
	 */
	public static Object iteratorList(Object list_obj) {

		List<Object> list = (List<Object>) list_obj;
		List<Object> new_list = new ArrayList<Object>(list.size());
		for (Object object : list) {
			if (object instanceof List) {
				new_list.add(SignUtil.iteratorList(object));
			} else if (object instanceof Map) {
				object = SignUtil.sortMapByKey((Map<String, Object>) object);
				new_list.add(object);
			} else {
				new_list.add(object);
			}
		}
		return new_list;
	}

	/***
	 * 将对象转换成json字符串
	 *
	 * @param map
	 *            需要转换的对象
	 * @return json字符串
	 */
	public static String toJson(Map<String, Object> map) {

		return JSONObject.fromObject(map).toString();
	}

	/**
	 * HmacMD5加密 返回字符串
	 *
	 * @param src
	 *            需要加密的字符串
	 * @param key
	 *            加密的key
	 * @return 加密后的数据
	 */
	public static String encryptToString(String src, String key) {

		byte[] encrypt_bytes = SignUtil.encrypt(src, key);
		return encrypt_bytes == null ? null : SignUtil.byte2HexString(encrypt_bytes);
	}

	/**
	 * HmacMD5加密 返回字节数组
	 *
	 * @param src
	 *            需要加密的字符串
	 * @param key
	 *            加密的key
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(String src, String key) {
		// 根据key来构建密钥
		SecretKeySpec sk = new SecretKeySpec(key.getBytes(), SignUtil.HMAC_MD5);
		try {
			// 生成一个MAC
			Mac mac = Mac.getInstance(sk.getAlgorithm());
			// 初始化MAC
			mac.init(sk);
			// 加密src并转化成十六进制字符串
			return mac.doFinal(src.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * byte 数组转换为十六进制的字符串
	 *
	 * @param b
	 *            输入需要转换的byte数组
	 * @return 返回十六进制 字符串
	 */
	public static String byte2HexString(byte[] b) {

		char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] newChar = new char[b.length * 2];
		for (int i = 0; i < b.length; i++) {
			newChar[2 * i] = hex[(b[i] & 0xf0) >> 4];
			newChar[(2 * i) + 1] = hex[b[i] & 0xf];

		}
		return new String(newChar);
	}

	public void startRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					URL postUrl = new URL("https://openapi.o3cloud.cn");

					// 打开连接
					HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

					// 打开读写属性，默认均为false
					connection.setDoOutput(true);
					connection.setDoInput(true);

					// 设置请求方式，默认为GET
					connection.setRequestMethod("POST");

					// Post 请求不能使用缓存
					connection.setUseCaches(false);

					connection.setRequestProperty("Content-Type", "application/json");

					// 连接，从postUrl.openConnection()至此的配置必须要在 connect之前完成，
					// 要注意的是connection.getOutputStream()会隐含的进行调用
					// connect()，所以这里可以省略
					// connection.connect();
					DataOutputStream out = new DataOutputStream(connection.getOutputStream());

					String json_str = SignUtil.toJson(SignUtil.this.request_map);
					out.write(json_str.getBytes(Charset.forName("utf-8")));
					out.flush();
					out.close(); // flush and close

					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}
}