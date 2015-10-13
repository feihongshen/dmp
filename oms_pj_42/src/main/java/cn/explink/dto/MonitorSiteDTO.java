package cn.explink.dto;

import java.math.BigDecimal;

/**
 * 数据监控 实体
 * 
 * @author Administrator
 *
 */
public class MonitorSiteDTO {
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

	private long branchid; // 站点id
	private String branchname;// 站点名称
	private String customername; // 供货商名称
	private long customerid;// 供货商id
	private long weidaohuoCountsum;// 未到货
	private BigDecimal weidaohuoCaramountsum; // 未到货
	private long rukuweilingCountsum; // 入库未领
	private BigDecimal rukuweilingCaramountsum; // 入库未领
	private long youhuowudanCountsum; // 有货无单
	private BigDecimal youhuowudanCaramountsum; // 有货无单
	private long yichangdanCountsum; // 异常单
	private BigDecimal yichangdanCaramountsum; // 异常单
	private long youdanwuhuoCountsum; // 有单无货
	private BigDecimal youdanwuhuoCaramountsum; // 有单无货
	private long yilinghuoCountsum; // 已领货
	private BigDecimal yilinghuoCaramountsum; // 已领货
	private long yiliudanCountsum; // 遗留单
	private BigDecimal yiliudanCaramountsum; // 遗留单
	private long kucuntuihuoCountsum; // 退货
	private BigDecimal kucuntuihuoCaramountsum; // 退货
	private long zhiliuCountsum; // 滞留
	private BigDecimal zhiliuCaramountsum; // 滞留
	private long zhongzhuanCountsum; // 中转
	private BigDecimal zhongzhuanCaramountsum; // 中转
	private long zaitutuihuoCountsum; // 退货
	private BigDecimal zaituchaCaramountsum; // 退货
	private long weijiaokunCountsum; // 未交款
	private BigDecimal weijiaokuanCaramountsum; // 未交款
	private long qiankuanCountsum; // 欠款
	private BigDecimal qiankuanCaramountsum; // 欠款

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

	public long getWeidaohuoCountsum() {
		return weidaohuoCountsum;
	}

	public void setWeidaohuoCountsum(long weidaohuoCountsum) {
		this.weidaohuoCountsum = weidaohuoCountsum;
	}

	public BigDecimal getWeidaohuoCaramountsum() {
		return weidaohuoCaramountsum;
	}

	public void setWeidaohuoCaramountsum(BigDecimal weidaohuoCaramountsum) {
		this.weidaohuoCaramountsum = weidaohuoCaramountsum;
	}

	public long getRukuweilingCountsum() {
		return rukuweilingCountsum;
	}

	public void setRukuweilingCountsum(long rukuweilingCountsum) {
		this.rukuweilingCountsum = rukuweilingCountsum;
	}

	public BigDecimal getRukuweilingCaramountsum() {
		return rukuweilingCaramountsum;
	}

	public void setRukuweilingCaramountsum(BigDecimal rukuweilingCaramountsum) {
		this.rukuweilingCaramountsum = rukuweilingCaramountsum;
	}

	public long getYouhuowudanCountsum() {
		return youhuowudanCountsum;
	}

	public void setYouhuowudanCountsum(long youhuowudanCountsum) {
		this.youhuowudanCountsum = youhuowudanCountsum;
	}

	public BigDecimal getYouhuowudanCaramountsum() {
		return youhuowudanCaramountsum;
	}

	public void setYouhuowudanCaramountsum(BigDecimal youhuowudanCaramountsum) {
		this.youhuowudanCaramountsum = youhuowudanCaramountsum;
	}

	public long getYichangdanCountsum() {
		return yichangdanCountsum;
	}

	public void setYichangdanCountsum(long yichangdanCountsum) {
		this.yichangdanCountsum = yichangdanCountsum;
	}

	public BigDecimal getYichangdanCaramountsum() {
		return yichangdanCaramountsum;
	}

	public void setYichangdanCaramountsum(BigDecimal yichangdanCaramountsum) {
		this.yichangdanCaramountsum = yichangdanCaramountsum;
	}

	public long getYoudanwuhuoCountsum() {
		return youdanwuhuoCountsum;
	}

	public void setYoudanwuhuoCountsum(long youdanwuhuoCountsum) {
		this.youdanwuhuoCountsum = youdanwuhuoCountsum;
	}

	public BigDecimal getYoudanwuhuoCaramountsum() {
		return youdanwuhuoCaramountsum;
	}

	public void setYoudanwuhuoCaramountsum(BigDecimal youdanwuhuoCaramountsum) {
		this.youdanwuhuoCaramountsum = youdanwuhuoCaramountsum;
	}

	public long getYilinghuoCountsum() {
		return yilinghuoCountsum;
	}

	public void setYilinghuoCountsum(long yilinghuoCountsum) {
		this.yilinghuoCountsum = yilinghuoCountsum;
	}

	public BigDecimal getYilinghuoCaramountsum() {
		return yilinghuoCaramountsum;
	}

	public void setYilinghuoCaramountsum(BigDecimal yilinghuoCaramountsum) {
		this.yilinghuoCaramountsum = yilinghuoCaramountsum;
	}

	public long getYiliudanCountsum() {
		return yiliudanCountsum;
	}

	public void setYiliudanCountsum(long yiliudanCountsum) {
		this.yiliudanCountsum = yiliudanCountsum;
	}

	public BigDecimal getYiliudanCaramountsum() {
		return yiliudanCaramountsum;
	}

	public void setYiliudanCaramountsum(BigDecimal yiliudanCaramountsum) {
		this.yiliudanCaramountsum = yiliudanCaramountsum;
	}

	public long getKucuntuihuoCountsum() {
		return kucuntuihuoCountsum;
	}

	public void setKucuntuihuoCountsum(long kucuntuihuoCountsum) {
		this.kucuntuihuoCountsum = kucuntuihuoCountsum;
	}

	public BigDecimal getKucuntuihuoCaramountsum() {
		return kucuntuihuoCaramountsum;
	}

	public void setKucuntuihuoCaramountsum(BigDecimal kucuntuihuoCaramountsum) {
		this.kucuntuihuoCaramountsum = kucuntuihuoCaramountsum;
	}

	public long getZhiliuCountsum() {
		return zhiliuCountsum;
	}

	public void setZhiliuCountsum(long zhiliuCountsum) {
		this.zhiliuCountsum = zhiliuCountsum;
	}

	public BigDecimal getZhiliuCaramountsum() {
		return zhiliuCaramountsum;
	}

	public void setZhiliuCaramountsum(BigDecimal zhiliuCaramountsum) {
		this.zhiliuCaramountsum = zhiliuCaramountsum;
	}

	public long getZhongzhuanCountsum() {
		return zhongzhuanCountsum;
	}

	public void setZhongzhuanCountsum(long zhongzhuanCountsum) {
		this.zhongzhuanCountsum = zhongzhuanCountsum;
	}

	public BigDecimal getZhongzhuanCaramountsum() {
		return zhongzhuanCaramountsum;
	}

	public void setZhongzhuanCaramountsum(BigDecimal zhongzhuanCaramountsum) {
		this.zhongzhuanCaramountsum = zhongzhuanCaramountsum;
	}

	public long getZaitutuihuoCountsum() {
		return zaitutuihuoCountsum;
	}

	public void setZaitutuihuoCountsum(long zaitutuihuoCountsum) {
		this.zaitutuihuoCountsum = zaitutuihuoCountsum;
	}

	public BigDecimal getZaituchaCaramountsum() {
		return zaituchaCaramountsum;
	}

	public void setZaituchaCaramountsum(BigDecimal zaituchaCaramountsum) {
		this.zaituchaCaramountsum = zaituchaCaramountsum;
	}

	public long getWeijiaokunCountsum() {
		return weijiaokunCountsum;
	}

	public void setWeijiaokunCountsum(long weijiaokunCountsum) {
		this.weijiaokunCountsum = weijiaokunCountsum;
	}

	public BigDecimal getWeijiaokuanCaramountsum() {
		return weijiaokuanCaramountsum;
	}

	public void setWeijiaokuanCaramountsum(BigDecimal weijiaokuanCaramountsum) {
		this.weijiaokuanCaramountsum = weijiaokuanCaramountsum;
	}

	public long getQiankuanCountsum() {
		return qiankuanCountsum;
	}

	public void setQiankuanCountsum(long qiankuanCountsum) {
		this.qiankuanCountsum = qiankuanCountsum;
	}

	public BigDecimal getQiankuanCaramountsum() {
		return qiankuanCaramountsum;
	}

	public void setQiankuanCaramountsum(BigDecimal qiankuanCaramountsum) {
		this.qiankuanCaramountsum = qiankuanCaramountsum;
	}

}
