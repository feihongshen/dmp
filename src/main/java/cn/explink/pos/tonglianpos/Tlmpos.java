package cn.explink.pos.tonglianpos;

public class Tlmpos {

	private String requester;
	private String targeter;
	private String privateKey;
	private String publicKey;
	private String deliver_dept_no;// 员工所属单位编码
	private String deliver_dept; // 员工所属单名称
	private String request_url; // POS机请求的url
	private int isValidateSign; // 是否开启签名验证
	private int isbackout; // 是否允许撤销 1允许 0不允许
	private int isshowPhone; // 是否显示电话 0不显示 1显示
	private int isshowPaytype; // 是否显示支付方式放在remark列 0不显示 1显示
	private String private_key;
	private String forwardUrl; //转发URl
	
	public String getForwardUrl() {
		return forwardUrl;
	}

	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public int getIsshowPhone() {
		return isshowPhone;
	}

	public void setIsshowPhone(int isshowPhone) {
		this.isshowPhone = isshowPhone;
	}

	public int getIsshowPaytype() {
		return isshowPaytype;
	}

	public void setIsshowPaytype(int isshowPaytype) {
		this.isshowPaytype = isshowPaytype;
	}

	public int getIsbackout() {
		return isbackout;
	}

	public void setIsbackout(int isbackout) {
		this.isbackout = isbackout;
	}

	public int getIsValidateSign() {
		return isValidateSign;
	}

	public void setIsValidateSign(int isValidateSign) {
		this.isValidateSign = isValidateSign;
	}

	public String getRequest_url() {
		return request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	private int isotherdeliverupdate = 0; // 他人刷卡是否更新派送员 1(更新) 0（不更新）
	private int isotheroperator = 0; // 是否限制他人刷卡

	public int getIsotheroperator() {
		return isotheroperator;
	}

	public void setIsotheroperator(int isotheroperator) {
		this.isotheroperator = isotheroperator;
	}

	public String getDeliver_dept_no() {
		return deliver_dept_no;
	}

	public int getIsotherdeliverupdate() {
		return isotherdeliverupdate;
	}

	public void setIsotherdeliverupdate(int isotherdeliverupdate) {
		this.isotherdeliverupdate = isotherdeliverupdate;
	}

	public void setDeliver_dept_no(String deliver_dept_no) {
		this.deliver_dept_no = deliver_dept_no;
	}

	public String getDeliver_dept() {
		return deliver_dept;
	}

	public void setDeliver_dept(String deliver_dept) {
		this.deliver_dept = deliver_dept;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getTargeter() {
		return targeter;
	}

	public void setTargeter(String targeter) {
		this.targeter = targeter;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	// public static void main(String[] args) throws Exception {
	// getPEM();
	// }
	// public static void getPEM() throws Exception{
	// BufferedReader br = new BufferedReader(new
	// FileReader("D://BJZZ_rsa_public_key.pem"));
	// String s = br.readLine();
	// String str = "";
	// s = br.readLine();
	// while (s.charAt(0) != '-'){
	// str += s + "\r";
	// s = br.readLine();
	// }
	//
	// //编码转换，进行BASE64解码
	// //BASE64Decoder base64decoder = new BASE64Decoder();
	// byte[] b =Base64Utils.decode(str);
	//
	// //byte[] b = base64decoder.decodeBuffer(str);
	//
	// //生成私匙
	// KeyFactory kf = KeyFactory.getInstance("RSA");
	// //X509EncodedKeySpec keySpec = new X509EncodedKeySpec(b);
	// PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(b);
	// //PublicKey privateKey = kf.generatePublic(keySpec);
	// PrivateKey privateKey = kf.generatePrivate(keySpec);
	// System.out.println(privateKey.getAlgorithm());
	//
	//
	//
	//
	//
	// }
	//

}
