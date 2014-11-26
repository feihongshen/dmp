package cn.explink.b2c.homegou;

/**
 * 存储 txt文件位置的枚举
 * 
 * @author Administrator 按字符串索引截取 type 配送类型
 */
public enum ROLineEnum {

	callbackcode("回收申请编号", 1, 14), express_id("配送公司代码", 15, 16), Shiptime("回收申请日", 17, 24), DingdanLeiXing("回收区分", 25, 27), DingGouBianHao("订购编号", 28, 50), ShangPingDaiMa("商品代码", 51, 60),

	BackCarnum("商品数量", 61, 62), BackCarname("商品名称", 63, 162), YunDanNum("运单数量", 163, 164), Consignee_no("顾客代码", 165, 176), Consigneename("顾客姓名", 177, 216),

	ConsigneepostCode("邮政代码", 217, 222), Consigneeaddress("邮政地址", 223, 422),

	ConsigneeMobile("手机", 423, 442), Consigneephone("电话", 443, 462),

	Cargoremark("商品配套", 463, 2462),

	Cwb("运单号", 2463, 2474),

	;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置

	private ROLineEnum(String lineName, int beginIndex, int endIndex) {
		this.lineName = lineName;
		this.bIx = beginIndex;
		this.eIx = endIndex;
	}

	public String getLineName() {
		return lineName;
	}

	// java取索引从 0开始需 -1
	public int getbIx() {
		return bIx - 1;
	}

	public int geteIx() {
		return eIx;
	}

}
