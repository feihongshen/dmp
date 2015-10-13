package cn.explink.dto;

import java.math.BigDecimal;

/**
 * 数据监控 实体
 * 
 * @author Administrator
 *
 */
public class MonitorNewDTO {
	// DaoRuShuJu(1,"导入数据","ImportCwb"),
	// TiHuo(2,"提货","GetGoods"),
	// TiHuoYouHuoWuDan(3,"提货有货无单","GetGoodsnoListIntoWarehous"),
	// RuKu(4,"入库","IntoWarehous"),
	// YouHuoWuDan(5,"有货无单入库","NoListIntoWarehous"),
	// ChuKuSaoMiao(6,"出库扫描","OutWarehouse"),
	// FenZhanDaoHuoSaoMiao(7,"分站到货扫描","SubstationGoods"),
	// FenZhanDaoHuoYouHuoWuDanSaoMiao(8,"分站到货有货无单扫描","SubstationGoodsNoList"),
	// FenZhanLingHuo(9,"分站领货","ReceiveGoods"),
	// ZhongZhuanChuKuSaoMiao(10,"中转出库扫描","ChangeGoodsOutwarehouse"),
	// TuiHuoChuKuSaoMiao(11,"退货出库扫描","ReturnGoodsOutwarehouse"),
	// ZhongZhuanZhanRuKu(12,"中转站入库","ChangeIntoWarehous"),
	// ZhongZhuanZhanYouHuoWuDanRuKu(13,"中转站有货无单入库","ChangeNoListIntoWarehous"),
	// ZhongZhuanZhanChuKuSaoMiao(14,"中转站出库扫描","TransBranchOutWarehouse"),
	// TuiHuoZhanRuKu(15,"退货站入库","BackIntoWarehous"),
	// TuiHuoZhanYouHuoWuDanRuKu(16,"退货站有货无单入库","BackNoListIntoWarehous"),
	// TuiHuoZhanZaiTouSaoMiao(17,"退货站再投扫描","BackReturnOutWarehous"),
	// PeiSongChengGong(18,"配送成功","Success"),
	// ShangMenTuiChengGong(19,"上门退成功","BackToDoorSuccess"),
	// ShangMenHuanChengGong(20,"上门换成功","ChangeToDoorSuccess"),
	// QuanBuTuiHuo(21,"全部退货","ReturnGoods"),
	// BuFenTuiHuo(22,"部分退货","SomeReturnGoods"),
	// FenZhanZhiLiu(23,"分站滞留","StayGoods"),
	// ShangMenJuTui(24,"上门拒退","BackToDoorFail"),
	// HuoWuDiuShi(25,"货物丢失","GoodsLose"),
	// TuiGongYingShangChuKu(27,"退供货商出库","ReturnCustomer"),
	// GongYingShangJuShouFanKu(28,"供货商拒收返库","CustomRefuse"),
	// CheXiaoFanKui(29,"撤销反馈","DeliverStatePodCancel");
	// 导入数据 1
	// 库房 2,3,4
	// 在途 6,
	// 站点
	// 小件员
	// 退货站
	// 中转站
	// 成功
	// 丢失
	// 异常
	// 差
	private long branchid; // 站点id
	private String branchname;// 站点名称
	private String customername; // 供货商名称
	private long customerid;// 供货商id
	private long daoruCountsum;// 导入订单数量
	private BigDecimal daoruCaramountsum;// 导入金额订单总量
	private long weirukuCountsum;// 未入库数量
	private BigDecimal weirukuCaramountsum;// 未入库金额总量
	private long kufangCountsum;// 库房数量
	private BigDecimal kufangCaramountsum;// 金额总量
	private long zaituCountsum;// 在途数量
	private BigDecimal zaituCaramountsum;// 在途金额总量
	private long zhandianCountsum;// 站点数量
	private BigDecimal zhandianCaramountsum;// 站点金额总量
	private long xiaojianyuanCountsum;// 小件员 数量
	private BigDecimal xiaojianyuanCaramountsum;// 小件员 金额总量
	private long tuihuozhanCountsum;// 退货站数量
	private BigDecimal tuihuozhanCaramountsum;// 退货站金额总量
	private long zhongzhuanzhanCountsum;// 中转站数量
	private BigDecimal zhongzhuanzhanCaramountsum;// 中转站金额总量
	private long chenggongCountsum;// 成功数量
	private BigDecimal chenggongCaramountsum;// 成功金额总量
	private long diushiCountsum;// 丢失数量
	private BigDecimal diushiCaramountsum;// 丢失金额总量
	private long yichangCountsum;// 异常数量
	private BigDecimal yichangCaramountsum;// 异常金额总量
	private long chaCountsum;// 异常数量
	private BigDecimal chaCaramountsum;// 异常金额总量

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getDaoruCountsum() {
		return daoruCountsum;
	}

	public void setDaoruCountsum(long daoruCountsum) {
		this.daoruCountsum = daoruCountsum;
	}

	public BigDecimal getDaoruCaramountsum() {
		return daoruCaramountsum;
	}

	public void setDaoruCaramountsum(BigDecimal daoruCaramountsum) {
		this.daoruCaramountsum = daoruCaramountsum;
	}

	public long getWeirukuCountsum() {
		return weirukuCountsum;
	}

	public void setWeirukuCountsum(long weirukuCountsum) {
		this.weirukuCountsum = weirukuCountsum;
	}

	public BigDecimal getWeirukuCaramountsum() {
		return weirukuCaramountsum;
	}

	public void setWeirukuCaramountsum(BigDecimal weirukuCaramountsum) {
		this.weirukuCaramountsum = weirukuCaramountsum;
	}

	public long getKufangCountsum() {
		return kufangCountsum;
	}

	public void setKufangCountsum(long kufangCountsum) {
		this.kufangCountsum = kufangCountsum;
	}

	public BigDecimal getKufangCaramountsum() {
		return kufangCaramountsum;
	}

	public void setKufangCaramountsum(BigDecimal kufangCaramountsum) {
		this.kufangCaramountsum = kufangCaramountsum;
	}

	public long getZaituCountsum() {
		return zaituCountsum;
	}

	public void setZaituCountsum(long zaituCountsum) {
		this.zaituCountsum = zaituCountsum;
	}

	public BigDecimal getZaituCaramountsum() {
		return zaituCaramountsum;
	}

	public void setZaituCaramountsum(BigDecimal zaituCaramountsum) {
		this.zaituCaramountsum = zaituCaramountsum;
	}

	public long getZhandianCountsum() {
		return zhandianCountsum;
	}

	public void setZhandianCountsum(long zhandianCountsum) {
		this.zhandianCountsum = zhandianCountsum;
	}

	public BigDecimal getZhandianCaramountsum() {
		return zhandianCaramountsum;
	}

	public void setZhandianCaramountsum(BigDecimal zhandianCaramountsum) {
		this.zhandianCaramountsum = zhandianCaramountsum;
	}

	public long getXiaojianyuanCountsum() {
		return xiaojianyuanCountsum;
	}

	public void setXiaojianyuanCountsum(long xiaojianyuanCountsum) {
		this.xiaojianyuanCountsum = xiaojianyuanCountsum;
	}

	public BigDecimal getXiaojianyuanCaramountsum() {
		return xiaojianyuanCaramountsum;
	}

	public void setXiaojianyuanCaramountsum(BigDecimal xiaojianyuanCaramountsum) {
		this.xiaojianyuanCaramountsum = xiaojianyuanCaramountsum;
	}

	public long getTuihuozhanCountsum() {
		return tuihuozhanCountsum;
	}

	public void setTuihuozhanCountsum(long tuihuozhanCountsum) {
		this.tuihuozhanCountsum = tuihuozhanCountsum;
	}

	public BigDecimal getTuihuozhanCaramountsum() {
		return tuihuozhanCaramountsum;
	}

	public void setTuihuozhanCaramountsum(BigDecimal tuihuozhanCaramountsum) {
		this.tuihuozhanCaramountsum = tuihuozhanCaramountsum;
	}

	public long getZhongzhuanzhanCountsum() {
		return zhongzhuanzhanCountsum;
	}

	public void setZhongzhuanzhanCountsum(long zhongzhuanzhanCountsum) {
		this.zhongzhuanzhanCountsum = zhongzhuanzhanCountsum;
	}

	public BigDecimal getZhongzhuanzhanCaramountsum() {
		return zhongzhuanzhanCaramountsum;
	}

	public void setZhongzhuanzhanCaramountsum(BigDecimal zhongzhuanzhanCaramountsum) {
		this.zhongzhuanzhanCaramountsum = zhongzhuanzhanCaramountsum;
	}

	public long getChenggongCountsum() {
		return chenggongCountsum;
	}

	public void setChenggongCountsum(long chenggongCountsum) {
		this.chenggongCountsum = chenggongCountsum;
	}

	public BigDecimal getChenggongCaramountsum() {
		return chenggongCaramountsum;
	}

	public void setChenggongCaramountsum(BigDecimal chenggongCaramountsum) {
		this.chenggongCaramountsum = chenggongCaramountsum;
	}

	public long getDiushiCountsum() {
		return diushiCountsum;
	}

	public void setDiushiCountsum(long diushiCountsum) {
		this.diushiCountsum = diushiCountsum;
	}

	public BigDecimal getDiushiCaramountsum() {
		return diushiCaramountsum;
	}

	public void setDiushiCaramountsum(BigDecimal diushiCaramountsum) {
		this.diushiCaramountsum = diushiCaramountsum;
	}

	public long getYichangCountsum() {
		return yichangCountsum;
	}

	public void setYichangCountsum(long yichangCountsum) {
		this.yichangCountsum = yichangCountsum;
	}

	public BigDecimal getYichangCaramountsum() {
		return yichangCaramountsum;
	}

	public void setYichangCaramountsum(BigDecimal yichangCaramountsum) {
		this.yichangCaramountsum = yichangCaramountsum;
	}

	public long getChaCountsum() {
		return chaCountsum;
	}

	public void setChaCountsum(long chaCountsum) {
		this.chaCountsum = chaCountsum;
	}

	public BigDecimal getChaCaramountsum() {
		return chaCaramountsum;
	}

	public void setChaCaramountsum(BigDecimal chaCaramountsum) {
		this.chaCaramountsum = chaCaramountsum;
	}

}
