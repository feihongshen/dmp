package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装了express_ops_order_flow 表中的flowordertype 对应到 commen_cwb_order_tail 表中的相应字段
 * 
 * @author xiaobao
 *
 */
public class FlowOrderColumnMap {

	public static Map<String, String> ORDER_FLOW_TAIL_MAP = new HashMap<String, String>();

	static {
		// express_ops_order_flow表中的create(时间)在表commen_cwb_order_tail中代表intowarehoustime(入库时间)
		ORDER_FLOW_TAIL_MAP.put(BranchEnum.KuFang.getValue() + "_" + FlowOrderTypeEnum.RuKu.getValue(), "credate_intowarehoustime");// 入库
		ORDER_FLOW_TAIL_MAP.put(BranchEnum.KuFang.getValue() + "_" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "credate_outwarehousetime");// 出库
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "credate_substationgoodstime");// 到货扫描时间
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), "credate_substationgoodstime");// 到货扫描时间
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "credate_receivegoodstime");// 分站领货时间
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue(), "credate_returngoodsoutwarehousetime");// 退货出站扫描时间
		ORDER_FLOW_TAIL_MAP.put(BranchEnum.ZhongZhuan.getValue() + "_" + FlowOrderTypeEnum.RuKu.getValue(), "credate_changeintowarehoustime");// 中转站入库时间
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.TiHuo.getValue(), "credate_getgoodstime");// 提货时间#
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), "credate_housetohousetime");// 库对库出库时间
		ORDER_FLOW_TAIL_MAP.put(BranchEnum.ZhongZhuan.getValue() + "_" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "credate_changeouttowarehoustime");// 中转站出库时间
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), "credate_tuihuointowarehoustime");// 退货站入库时间
		ORDER_FLOW_TAIL_MAP.put(BranchEnum.TuiHuo.getValue() + "_" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "credate_tuihuooutwarehoustime");// 退货站出库时间
																																					// 新加
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), "credate_customerbacktime");// 供应商退货时间
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.YiShenHe.getValue(), "credate_checktime");// 审核时间
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.YiFanKui.getValue(), "credate_couplebacktime");// 反馈时间
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.DaoRuShuJu.getValue(), "credate_emaildatetime");// 发货时间

		ORDER_FLOW_TAIL_MAP.put("_" + BranchEnum.ZhongZhuan.getValue() + "_" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "credate_zhandianouttozhongzhuantime");// 站点中转出站时间
		ORDER_FLOW_TAIL_MAP.put("_" + BranchEnum.TuiHuo.getValue() + "_" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "credate_tuihuoouttozhandiantime");// 退货站退货出库出给站点时间
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), "credate_gonghuoshangchenggongtime");// 退供货商成功时间
		ORDER_FLOW_TAIL_MAP.put("" + FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), "credate_gonghuoshangjushoutime");// 供货商拒收返库时间
	}

}
