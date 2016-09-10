package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.tonglianpos.delivery.DeliveryService;
import cn.explink.service.mps.release.DeliverTakeGoodsMPSReleaseService;
import cn.explink.util.B2cUtil;

public class DeliveryStateDTO {
	private Logger logger = LoggerFactory.getLogger(DeliveryStateDTO.class);
	// ----------- 已反馈订单 ------------
	private long fankui_peisong_chenggong = 0; // 配送成功
	private long fankui_peisong_chenggong_zanbuchuli = 0; // 配送成功 暂不处理
	private List<DeliveryStateView> fankui_peisong_chenggongList = new ArrayList<DeliveryStateView>();

	private long fankui_shangmentui_chenggong = 0; // 上门退成功
	private long fankui_shangmentui_chenggong_zanbuchuli = 0; // 上门退成功 暂不处理
	private List<DeliveryStateView> fankui_shangmentui_chenggongList = new ArrayList<DeliveryStateView>();

	private long fankui_shangmentui_jutui = 0; // 上门退拒退
	private long fankui_shangmentui_jutui_zanbuchuli = 0; // 上门退拒退 暂不处理
	private List<DeliveryStateView> fankui_shangmentui_jutuiList = new ArrayList<DeliveryStateView>();

	private long fankui_shangmenhuan_chenggong = 0; // 上门换成功
	private long fankui_shangmenhuan_chenggong_zanbuchuli = 0; // 上门换成功 暂不处理
	private List<DeliveryStateView> fankui_shangmenhuan_chenggongList = new ArrayList<DeliveryStateView>();

	private long fankui_tuihuo = 0;// 拒收
	private long fankui_tuihuo_zanbuchuli = 0;// 拒收暂不处理
	private List<DeliveryStateView> fankui_tuihuoList = new ArrayList<DeliveryStateView>();

	private long fankui_bufentuihuo = 0;// 部分退货
	private long fankui_bufentuihuo_zanbuchuli = 0;// 部分退货 暂不处理
	private List<DeliveryStateView> fankui_bufentuihuoList = new ArrayList<DeliveryStateView>();

	private long fankui_zhiliu = 0;// 分站滞留
	private long fankui_zhiliu_zanbuchuli = 0;// 分站滞留暂不处理
	private List<DeliveryStateView> fankui_zhiliuList = new ArrayList<DeliveryStateView>();

	private long fankui_diushi = 0;// 丢失
	private long fankui_diushi_zanbuchuli = 0;// 丢失 暂不处理
	private List<DeliveryStateView> fankui_diushiList = new ArrayList<DeliveryStateView>();
	
	private long fankui_zhongzhuan = 0;// 待中转
	private long fankui_zhongzhuan_zanbuchuli = 0;// 待中转暂不处理
	private List<DeliveryStateView> fankui_zhongzhuanList = new ArrayList<DeliveryStateView>();
	

	// ----------- 已反馈订单 end ------------
	// ----------- 未反馈订单 ------------
	private long weifankui = 0;
	private List<DeliveryStateView> weifankuiList = new ArrayList<DeliveryStateView>();
	private long yiliu = 0; // 遗留
	private List<DeliveryStateView> yiliuList = new ArrayList<DeliveryStateView>();
	// ----------- 未反馈订单 end ------------
	// ----------- 历史未归班审核订单 ------------
	private long lishi_weishenhe = 0; // 跟已反馈放到一起
	private List<DeliveryStateView> lishi_weishenheList = new ArrayList<DeliveryStateView>();
	// ----------- 历史未归班审核订单 end ------------
	// ----------- 暂不处理的订单 ------------
	private long zanbuchuli = 0;
	// ----------- 暂不处理的订单 end ------------

	private BigDecimal total = new BigDecimal(0);// 当前小件员所有货物的总金额
	private BigDecimal cash_amount = new BigDecimal(0);// 所有货物中现金所占的总金额
	private BigDecimal pos_amount = new BigDecimal(0);// 所有货物中pos刷卡所占的总金额
	private BigDecimal checkfee_amount = new BigDecimal(0);// 所有货物中支票所占的总金额
	private BigDecimal otherfee_amount = new BigDecimal(0);// 所有货物中其他支付方式所占的总金额
	// 新增
	private BigDecimal codpos_amount = new BigDecimal(0);// 所有货物中codpos刷卡所占的总金额
	private BigDecimal smtcgfare_amount = new BigDecimal(0);// 上门退成功所有货物中实收运费所占的总金额
	private BigDecimal smtjtfare_amount = new BigDecimal(0);// 上门退拒退所有货物中实收运费所占的总金额

	/**
	 * @return 全部订单记录 不包括 暂不处理 和 历史未归班
	 */
	public long getAllNumber() {
		return getNowNumber() + yiliu + lishi_weishenhe + zanbuchuli;
	}

	/**
	 * @return 今日领货
	 */
	public long getNowNumber() {
		return fankui_peisong_chenggong + fankui_shangmentui_chenggong + fankui_shangmentui_jutui + fankui_shangmenhuan_chenggong + fankui_tuihuo + fankui_bufentuihuo + fankui_zhiliu + fankui_diushi
				+fankui_zhongzhuan
				+ weifankui - lishi_weishenhe - zanbuchuli;
	}

	/**
	 * @return 已反馈货物数
	 */
	public long getYifankuiNumber() {
		return fankui_peisong_chenggong + fankui_shangmentui_chenggong + fankui_shangmentui_jutui + fankui_shangmenhuan_chenggong + fankui_tuihuo + fankui_bufentuihuo + fankui_zhiliu + fankui_diushi
				+fankui_zhongzhuan;
	}

	/**
	 * @return 审核的单数
	 */
	public long getSubNumber() {
		return getYifankuiNumber() - zanbuchuli;
	}

	/**
	 * @return 未反馈货物数
	 */
	public long getWeifankuiNumber() {
		return weifankui + yiliu;
	}

	/**
	 * 返回对应list里面的货物 总金额汇总
	 * 
	 * @param list
	 *            List<DeliveryState>
	 * @return
	 */
	public double getAmount(List<DeliveryStateView> list) {
		BigDecimal amount = new BigDecimal(0);
		for (DeliveryStateView ds : list) {
			amount = amount.add(ds.getReceivedfee()).subtract(ds.getReturnedfee());
		}
		return amount.doubleValue();
	}

	/**
	 * 返回对应list里面的货物 总金额汇总
	 * 
	 * @param list
	 *            List<DeliveryState>
	 * @return
	 */
	public double getWeiFanKuiAmount(List<DeliveryStateView> list) {
		BigDecimal amount = new BigDecimal(0);
		for (DeliveryStateView ds : list) {
			if (ds.getIsout() == 1) {
				amount = amount.subtract(ds.getBusinessfee());
			} else {
				amount = amount.add(ds.getBusinessfee());
			}

		}
		return amount.doubleValue();
	}

	/**
	 * 返回对应list里面的货物 非暂不处理类型的总金额汇总 不含pos
	 * 
	 * @param list
	 *            List<DeliveryState>
	 * @return
	 */
	public double getAmountNotZanbuchuli(List<DeliveryStateView> list) {
		BigDecimal amount = new BigDecimal(0);
		for (DeliveryStateView ds : list) {
			if (ds.getGcaid() != -1)
				amount = amount.add(ds.getReceivedfee()).subtract(ds.getReturnedfee()).subtract(ds.getPos()).subtract(ds.getCodpos());
		}
		return amount.doubleValue();
	}

	/**
	 * 返回对应list里面的货物 非暂不处理类型的总金额汇总 pos
	 * 
	 * @param list
	 *            List<DeliveryState>
	 * @return
	 */
	public double getPosAmountNotZanbuchuli(List<DeliveryStateView> list) {
		BigDecimal amount = new BigDecimal(0);
		for (DeliveryStateView ds : list) {
			if (ds.getGcaid() != -1)
				amount = amount.add(ds.getPos());
		}
		return amount.doubleValue();
	}

	/**
	 * 返回对应list里面的货物 非暂不处理类型的总金额汇总 codpos
	 * 
	 * @param list
	 *            List<DeliveryState>
	 * @return
	 */
	public double getCodPosAmountNotZanbuchuli(List<DeliveryStateView> list) {
		BigDecimal amount = new BigDecimal(0);
		for (DeliveryStateView ds : list) {
			if (ds.getGcaid() != -1)
				amount = amount.add(ds.getCodpos());
		}
		return amount.doubleValue();
	}

	/**
	 * 返回对应list里面的货物 非暂不处理类型的总金额汇总 smtcgfare
	 * 
	 * @param list
	 *            List<DeliveryState>
	 * @return
	 */
	public double getSmtcgFareAmountNotZanbuchuli(List<DeliveryStateView> list) {
		BigDecimal amount = new BigDecimal(0);
		for (DeliveryStateView ds : list) {
			if (ds.getGcaid() != -1)
				amount = amount.add(ds.getInfactfare());
		}
		return amount.doubleValue();
	}

	/**
	 * 返回对应list里面的货物 非暂不处理类型的总金额汇总 smtjtfare
	 * 
	 * @param list
	 *            List<DeliveryState>
	 * @return
	 */
	public double getSmtjtFareAmountNotZanbuchuli(List<DeliveryStateView> list) {
		BigDecimal amount = new BigDecimal(0);
		for (DeliveryStateView ds : list) {
			if (ds.getGcaid() != -1)
				amount = amount.add(ds.getInfactfare());
		}
		return amount.doubleValue();
	}

	/**
	 * 当前这批货物的退货总数
	 * 
	 * @return
	 */
	public long getTuihuoAllNumber() {

		return getFankui_tuihuo() - getFankui_tuihuo_zanbuchuli() + getFankui_bufentuihuo() - getFankui_bufentuihuo_zanbuchuli() + getFankui_shangmenhuan_chenggong()
				- getFankui_shangmenhuan_chenggong_zanbuchuli() + getFankui_shangmentui_chenggong() - getFankui_shangmentui_chenggong_zanbuchuli();
	}

	/**
	 * 当前需要上交款(现金、支票、其他类型)的总数
	 * 
	 * @return
	 */
	public double getUpPayAmount() {

		return getCash_amount().add(getCheckfee_amount()).add(getOtherfee_amount()).doubleValue();
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 处理当前属于小件员的所有货物，进行分类
	 * 
	 * @param dsList
	 *            小件员的非已审核订单列表
	 */
	public void analysisDeliveryStateList(List<DeliveryStateView> dsList, B2cUtil bcUtil, CustomerDAO customerDAO ) {

		for (DeliveryStateView ds : dsList) {
			DeliveryStateView DeliveryStateView = new DeliveryStateView();

			if (ds.getDeliverystate() == DeliveryStateEnum.WeiFanKui.getValue()) {

				if (ds.getCreatetime().substring(0, 10).equals(sdf.format(new Date()))) {// 如果是当天领的货
					weifankui++;
				} else {// 否则为历史未反馈订单 也就是遗留订单
					yiliu++;
					yiliuList.add(ds);
				}
				weifankuiList.add(ds);
				continue;
			}

			if (ds.getGcaid() == -1) {
				zanbuchuli++;
			} else if (!ds.getCreatetime().substring(0, 10).equals(sdf.format(new Date()))) {// 如果不是当天领的货
																								// 但是已经反馈了的了
																								// 称之为历史未审核
				lishi_weishenhe++;
				lishi_weishenheList.add(ds);
			}

			if (ds.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) {
				fankui_peisong_chenggong++;
				fankui_peisong_chenggongList.add(ds);
				if (ds.getGcaid() == -1) {
					fankui_peisong_chenggong_zanbuchuli++;
				}

			} else if (ds.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
				ds.setEditFlag(!this.jugeVIPShangmentuiChenggongOrdere(ds, bcUtil, customerDAO));
				fankui_shangmentui_chenggong++;
				fankui_shangmentui_chenggongList.add(ds);
				if (ds.getGcaid() == -1) {
					fankui_shangmentui_chenggong_zanbuchuli++;
				}

			} else if (ds.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
				fankui_shangmenhuan_chenggong++;
				fankui_shangmenhuan_chenggongList.add(ds);
				if (ds.getGcaid() == -1) {
					fankui_shangmenhuan_chenggong_zanbuchuli++;
				}

			} else if (ds.getDeliverystate() == DeliveryStateEnum.JuShou.getValue()) {
				fankui_tuihuo++;
				fankui_tuihuoList.add(ds);
				if (ds.getGcaid() == -1) {
					fankui_tuihuo_zanbuchuli++;
				}

			} else if (ds.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
				fankui_bufentuihuo++;
				fankui_bufentuihuoList.add(ds);
				if (ds.getGcaid() == -1) {
					fankui_bufentuihuo_zanbuchuli++;
				}

			} else if (ds.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()) {
				fankui_shangmentui_jutui++;
				fankui_shangmentui_jutuiList.add(ds);
				if (ds.getGcaid() == -1) {
					fankui_shangmentui_jutui_zanbuchuli++;
				}

			} else if (ds.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				fankui_zhiliu++;
				fankui_zhiliuList.add(ds);
				if (ds.getGcaid() == -1) {
					fankui_zhiliu_zanbuchuli++;
				}
			} else if (ds.getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
				fankui_diushi++;
				fankui_diushiList.add(ds);
				if (ds.getGcaid() == -1) {
					fankui_diushi_zanbuchuli++;
				}
			}else if (ds.getDeliverystate() == DeliveryStateEnum.DaiZhongZhuan.getValue()) {
				fankui_zhongzhuan++;
				fankui_zhongzhuanList.add(ds);
				if (ds.getGcaid() == -1) {
					fankui_zhongzhuan_zanbuchuli++;
				}
			}  
			
			
			else {
				logger.error("归班审核时为统计到的订单编号:{}", ds.getCwb());
				continue;
			}
			if (ds.getGcaid() != -1) {
				total = total.add(ds.getReceivedfee()).subtract(ds.getReturnedfee()).add(ds.getInfactfare());
				pos_amount = pos_amount.add(ds.getPos());
				codpos_amount = codpos_amount.add(ds.getCodpos());
				cash_amount = cash_amount.add(ds.getCash()).subtract(ds.getReturnedfee()).add(ds.getInfactfare());
				checkfee_amount = checkfee_amount.add(ds.getCheckfee());
				otherfee_amount = otherfee_amount.add(ds.getOtherfee());
				if (ds.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
					smtcgfare_amount = smtcgfare_amount.add(ds.getInfactfare());
				} else if (ds.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()) {
					smtjtfare_amount = smtjtfare_amount.add(ds.getInfactfare());
				}
			}

		}
	}

	public BigDecimal getCash_amount() {
		return cash_amount;
	}

	public void setCash_amount(BigDecimal cash_amount) {
		this.cash_amount = cash_amount;
	}

	public BigDecimal getCheckfee_amount() {
		return checkfee_amount;
	}

	public void setCheckfee_amount(BigDecimal checkfee_amount) {
		this.checkfee_amount = checkfee_amount;
	}

	public BigDecimal getOtherfee_amount() {
		return otherfee_amount;
	}

	public void setOtherfee_amount(BigDecimal otherfee_amount) {
		this.otherfee_amount = otherfee_amount;
	}

	public long getFankui_peisong_chenggong_zanbuchuli() {
		return fankui_peisong_chenggong_zanbuchuli;
	}

	public void setFankui_peisong_chenggong_zanbuchuli(long fankui_peisong_chenggong_zanbuchuli) {
		this.fankui_peisong_chenggong_zanbuchuli = fankui_peisong_chenggong_zanbuchuli;
	}

	public long getFankui_shangmentui_chenggong_zanbuchuli() {
		return fankui_shangmentui_chenggong_zanbuchuli;
	}

	public void setFankui_shangmentui_chenggong_zanbuchuli(long fankui_shangmentui_chenggong_zanbuchuli) {
		this.fankui_shangmentui_chenggong_zanbuchuli = fankui_shangmentui_chenggong_zanbuchuli;
	}

	public long getFankui_shangmentui_jutui_zanbuchuli() {
		return fankui_shangmentui_jutui_zanbuchuli;
	}

	public void setFankui_shangmentui_jutui_zanbuchuli(long fankui_shangmentui_jutui_zanbuchuli) {
		this.fankui_shangmentui_jutui_zanbuchuli = fankui_shangmentui_jutui_zanbuchuli;
	}

	public long getFankui_shangmenhuan_chenggong_zanbuchuli() {
		return fankui_shangmenhuan_chenggong_zanbuchuli;
	}

	public void setFankui_shangmenhuan_chenggong_zanbuchuli(long fankui_shangmenhuan_chenggong_zanbuchuli) {
		this.fankui_shangmenhuan_chenggong_zanbuchuli = fankui_shangmenhuan_chenggong_zanbuchuli;
	}

	public long getFankui_bufentuihuo_zanbuchuli() {
		return fankui_bufentuihuo_zanbuchuli;
	}

	public void setFankui_bufentuihuo_zanbuchuli(long fankui_bufentuihuo_zanbuchuli) {
		this.fankui_bufentuihuo_zanbuchuli = fankui_bufentuihuo_zanbuchuli;
	}

	public long getFankui_diushi_zanbuchuli() {
		return fankui_diushi_zanbuchuli;
	}

	public void setFankui_diushi_zanbuchuli(long fankui_diushi_zanbuchuli) {
		this.fankui_diushi_zanbuchuli = fankui_diushi_zanbuchuli;
	}

	public long getFankui_tuihuo_zanbuchuli() {
		return fankui_tuihuo_zanbuchuli;
	}

	public void setFankui_tuihuo_zanbuchuli(long fankui_tuihuo_zanbuchuli) {
		this.fankui_tuihuo_zanbuchuli = fankui_tuihuo_zanbuchuli;
	}

	public long getFankui_zhiliu_zanbuchuli() {
		return fankui_zhiliu_zanbuchuli;
	}

	public void setFankui_zhiliu_zanbuchuli(long fankui_zhiliu_zanbuchuli) {
		this.fankui_zhiliu_zanbuchuli = fankui_zhiliu_zanbuchuli;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getPos_amount() {
		return pos_amount;
	}

	public void setPos_amount(BigDecimal pos_amount) {
		this.pos_amount = pos_amount;
	}

	public long getFankui_peisong_chenggong() {
		return fankui_peisong_chenggong;
	}

	public void setFankui_peisong_chenggong(long fankui_peisong_chenggong) {
		this.fankui_peisong_chenggong = fankui_peisong_chenggong;
	}

	public List<DeliveryStateView> getFankui_peisong_chenggongList() {
		return fankui_peisong_chenggongList;
	}

	public void setFankui_peisong_chenggongList(List<DeliveryStateView> fankui_peisong_chenggongList) {
		this.fankui_peisong_chenggongList = fankui_peisong_chenggongList;
	}

	public long getFankui_shangmentui_chenggong() {
		return fankui_shangmentui_chenggong;
	}

	public void setFankui_shangmentui_chenggong(long fankui_shangmentui_chenggong) {
		this.fankui_shangmentui_chenggong = fankui_shangmentui_chenggong;
	}

	public List<DeliveryStateView> getFankui_shangmentui_chenggongList() {
		return fankui_shangmentui_chenggongList;
	}

	public void setFankui_shangmentui_chenggongList(List<DeliveryStateView> fankui_shangmentui_chenggongList) {
		this.fankui_shangmentui_chenggongList = fankui_shangmentui_chenggongList;
	}

	public long getFankui_shangmentui_jutui() {
		return fankui_shangmentui_jutui;
	}

	public void setFankui_shangmentui_jutui(long fankui_shangmentui_jutui) {
		this.fankui_shangmentui_jutui = fankui_shangmentui_jutui;
	}

	public List<DeliveryStateView> getFankui_shangmentui_jutuiList() {
		return fankui_shangmentui_jutuiList;
	}

	public void setFankui_shangmentui_jutuiList(List<DeliveryStateView> fankui_shangmentui_jutuiList) {
		this.fankui_shangmentui_jutuiList = fankui_shangmentui_jutuiList;
	}

	public long getFankui_shangmenhuan_chenggong() {
		return fankui_shangmenhuan_chenggong;
	}

	public void setFankui_shangmenhuan_chenggong(long fankui_shangmenhuan_chenggong) {
		this.fankui_shangmenhuan_chenggong = fankui_shangmenhuan_chenggong;
	}

	public List<DeliveryStateView> getFankui_shangmenhuan_chenggongList() {
		return fankui_shangmenhuan_chenggongList;
	}

	public void setFankui_shangmenhuan_chenggongList(List<DeliveryStateView> fankui_shangmenhuan_chenggongList) {
		this.fankui_shangmenhuan_chenggongList = fankui_shangmenhuan_chenggongList;
	}

	public long getFankui_tuihuo() {
		return fankui_tuihuo;
	}

	public void setFankui_tuihuo(long fankui_tuihuo) {
		this.fankui_tuihuo = fankui_tuihuo;
	}

	public List<DeliveryStateView> getFankui_tuihuoList() {
		return fankui_tuihuoList;
	}

	public void setFankui_tuihuoList(List<DeliveryStateView> fankui_tuihuoList) {
		this.fankui_tuihuoList = fankui_tuihuoList;
	}

	public long getFankui_bufentuihuo() {
		return fankui_bufentuihuo;
	}

	public void setFankui_bufentuihuo(long fankui_bufentuihuo) {
		this.fankui_bufentuihuo = fankui_bufentuihuo;
	}

	public List<DeliveryStateView> getFankui_bufentuihuoList() {
		return fankui_bufentuihuoList;
	}

	public void setFankui_bufentuihuoList(List<DeliveryStateView> fankui_bufentuihuoList) {
		this.fankui_bufentuihuoList = fankui_bufentuihuoList;
	}

	public long getFankui_zhiliu() {
		return fankui_zhiliu;
	}

	public void setFankui_zhiliu(long fankui_zhiliu) {
		this.fankui_zhiliu = fankui_zhiliu;
	}

	public List<DeliveryStateView> getFankui_zhiliuList() {
		return fankui_zhiliuList;
	}

	public void setFankui_zhiliuList(List<DeliveryStateView> fankui_zhiliuList) {
		this.fankui_zhiliuList = fankui_zhiliuList;
	}

	public long getFankui_diushi() {
		return fankui_diushi;
	}

	public void setFankui_diushi(long fankui_diushi) {
		this.fankui_diushi = fankui_diushi;
	}

	public List<DeliveryStateView> getFankui_diushiList() {
		return fankui_diushiList;
	}

	public void setFankui_diushiList(List<DeliveryStateView> fankui_diushiList) {
		this.fankui_diushiList = fankui_diushiList;
	}

	public long getWeifankui() {
		return weifankui;
	}

	public void setWeifankui(long weifankui) {
		this.weifankui = weifankui;
	}

	public List<DeliveryStateView> getWeifankuiList() {
		return weifankuiList;
	}

	public void setWeifankuiList(List<DeliveryStateView> weifankuiList) {
		this.weifankuiList = weifankuiList;
	}

	public long getYiliu() {
		return yiliu;
	}

	public void setYiliu(long yiliu) {
		this.yiliu = yiliu;
	}

	public long getLishi_weishenhe() {
		return lishi_weishenhe;
	}

	public void setLishi_weishenhe(long lishi_weishenhe) {
		this.lishi_weishenhe = lishi_weishenhe;
	}

	public long getZanbuchuli() {
		return zanbuchuli;
	}

	public void setZanbuchuli(long zanbuchuli) {
		this.zanbuchuli = zanbuchuli;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public List<DeliveryStateView> getYiliuList() {
		return yiliuList;
	}

	public void setYiliuList(List<DeliveryStateView> yiliuList) {
		this.yiliuList = yiliuList;
	}

	public List<DeliveryStateView> getLishi_weishenheList() {
		return lishi_weishenheList;
	}

	public void setLishi_weishenheList(List<DeliveryStateView> lishi_weishenheList) {
		this.lishi_weishenheList = lishi_weishenheList;
	}

	public BigDecimal getCodpos_amount() {
		return codpos_amount;
	}

	public void setCodpos_amount(BigDecimal codpos_amount) {
		this.codpos_amount = codpos_amount;
	}

	public BigDecimal getSmtcgfare_amount() {
		return smtcgfare_amount;
	}

	public void setSmtcgfare_amount(BigDecimal smtcgfare_amount) {
		this.smtcgfare_amount = smtcgfare_amount;
	}

	public BigDecimal getSmtjtfare_amount() {
		return smtjtfare_amount;
	}

	public void setSmtjtfare_amount(BigDecimal smtjtfare_amount) {
		this.smtjtfare_amount = smtjtfare_amount;
	}
	
	public long getFankui_zhongzhuan() {
		return fankui_zhongzhuan;
	}

	public void setFankui_zhongzhuan(long fankui_zhongzhuan) {
		this.fankui_zhongzhuan = fankui_zhongzhuan;
	}

	public long getFankui_zhongzhuan_zanbuchuli() {
		return fankui_zhongzhuan_zanbuchuli;
	}

	public void setFankui_zhongzhuan_zanbuchuli(long fankui_zhongzhuan_zanbuchuli) {
		this.fankui_zhongzhuan_zanbuchuli = fankui_zhongzhuan_zanbuchuli;
	}

	public List<DeliveryStateView> getFankui_zhongzhuanList() {
		return fankui_zhongzhuanList;
	}

	public void setFankui_zhongzhuanList(
			List<DeliveryStateView> fankui_zhongzhuanList) {
		this.fankui_zhongzhuanList = fankui_zhongzhuanList;
	}
	/**
	 * 判断订单是否是唯品会的上门退订单，并且反馈结果为上门退成功
	 * 刘武强
	 * 20160831
	 * @param cwbOrder
	 * @return
	 */
	public boolean jugeVIPShangmentuiChenggongOrdere( DeliveryStateView ds, B2cUtil bcUtil, CustomerDAO customerDAO){
		//判断是否是唯品会订单
		Customer customer = customerDAO.getCustomerById(ds.getCustomerid());
		if ((customer.getB2cEnum() != null) && !customer.getB2cEnum().equals(bcUtil.getB2cEnumKeys(customer, "vipshop"))) {
			return false;
		}
		//判断是否为上门退订单并且反馈为上门退成功
		if(ds.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue() && ds.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()){
			return true;
		}
		return false;
	}
	

	
}
