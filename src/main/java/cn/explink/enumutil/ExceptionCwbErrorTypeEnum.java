package cn.explink.enumutil;

public enum ExceptionCwbErrorTypeEnum {
	FenZhanYouHuoWuDanDaoHuo(1, "分站有货无单到货"), YI_CHANG_DAN_HAO(2, "异常单号，含有非法字符"), Y_H_W_D_WEI_XUAN_GONG_HUO_SHANG(3, "有货无单，未选择供货商不允许入库"), BU_YUN_XU(4, "有货无单，本站不允许有货无单入库"), YOU_HUO_WU_DAN(5, "有货无单"), GONG_YING_SHANG_XUAN_ZE_CUO_WU(
			6, "供货商选择错误,正确的供货商为：{0}"), CHONG_FU_RU_KU(7, "重复入库"), SYS_ERROR(8, "系统内部错误"), YOU_HUO_WU_DAN_RU_KU(9, "有货无单入库"), KU_FANG_CHU_KU_YI_CHANG_DING_DAN_BU_CUN_ZAI(10, "库房出库异常，订单不在可出库数据中"), CHONG_FU_CHU_KU(
			11, "重复出库"), CHONG_FU_DAO_HUO(12, "重复到货"), CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI(13, "无此单号/包号"), DIAN_XU_YAO_TUI_HUO_SHENG_QING(93, "电商需要退货申请"), TUI_GONG_YING_SHANG_CHU_KU_YI_CHANG_FEI_BEN_GONG_YING_SHANG_DING_DAN(
			14, "退供货商出库异常，非本供货商订单"), WEI_PI_PEI_ZHAN_DIAN(16, "未匹配站点"), DING_DAN_WEI_SHEN_HE(17, "订单未审核"), QING_ZUO_DAO_HUO_SAO_MIAO(18, "请做到货扫描"), KU_FANG_BU_YUN_XU(19, "有货无单，库房不允许有货无单入库"), YI_TUI_HUO(
			20, "已退货"), YI_PEI_SONG_CHENG_GONG(21, "已配送成功"), YI_ZHI_LIU(22, "已滞留"), YI_HUAN_HUO(23, "已换货"), YI_JU_SHUO(24, "已拒收"), MEI_YOU_FEN_HUO_ZHAN_DIAN(25, "没有分货站点"), CHONG_FU_FAN_KUI(26, "重复反馈"), YI_CHANG_DING_DAN_WEI_LING_HUO_FAN_KUI(
			27, "异常订单，未领货反馈"), FEI_PEI_SONG_DING_DAN(28, "非配送和快递订单"), FEI_SHANG_MEN_TUI_DING_DAN(29, "非上门退类型订单"), FEI_SHANG_MEN_HUAN_DING_DAN(30, "非上门换类型订单"), ZHONG_ZHUAN_HUO(31, "中转货"), FANG_CUO_HUO(
			32, "放错货"), WEI_ZAI_TOU(33, "未再投"), YI_CHANG_CAO_ZUO_ZHAO_BU_DAO_ZHONG_ZHUAN_ZHAN(34, "异常操作，找不到中转站"), YI_CHANG_CAO_ZUO_ZHAO_BU_DAO_TUI_HUO_ZHAN(35, "异常操作，找不到退货站"), FEN_ZHAN_TUI_HUO_YI_CHANG_DING_DAN_BU_ZAI_KE_TUI_HUO_SHU_JU_ZHONG(
			36, "分站退货异常，订单不在可退货数据中"), FEN_ZHAN_ZHONG_ZHUAN_YI_CHANG_DING_DAN_BU_ZAI_KE_ZHONG_ZHUAN_SHU_JU_ZHONG(37, "分站中转异常，订单不在可退货数据中"), MEI_YOU_FEN_HUO_KU_FANG(38, "没有分货库房"), FEI_BEN_KU_FAN_HUO_WU(
			39, "非本库房货物"), BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO(40, "不是这个小件员的货"), GUI_BAN_SHEN_HE_DING_DAN_BU_NENG_ZAI_FAN_KUI(41, "归班审核订单不能再次反馈"), STATE_CONTROL_ERROR(42, "状态为{0}的订单不允许进行{1}操作"), YI_FAN_KUI_BU_NENG_ZAI_LING(
			43, "已反馈的未归班订单不能再领"), YI_FAN_KUI_BU_NENG_PI_LIANG_ZAI_FAN_KUI(44, "已反馈的订单不能进行批量再次反馈"), WU_FA_ZI_DONG_PAN_DUAN_XIA_YI_ZHAN(45, "无法自动判断下一站"), YI_JIAO_FEI_SHEN_HE_BU_NENG_QU_XIAO(46,
			"已缴费审核不能取消"), SYS_SCAN_ERROR(47, "已欠费"), BU_SHI_ZHE_GE_SI_JI(48, "不是这个司机"), BU_SHI_ZHE_GE_MU_DI_DI(49, "不是这个目的地的货，目的站：{0}"), PEi_SONG_LEI_XING_WEI_ZHAO_DAO(50, "配送类型{0}没有找到"), MU_DI_ZHAN_WEI_ZHI(
			51, "目的站未知"), Invalid_Cwb_State(52, "无效的订单状态"), Field_IS_Mandatory(53, "{0}字段不能为空"), Field_IS_Cwb_State(54, "订单状态不对"), Operation_Repeat(55, "重复扫描"), Invalid_Operation(56, "不可识别的操作"), DingDanYiZhiFu(
			57, "订单已支付"), ZhiFuAmountExceiton(58, "支付金额有误"), ChongFuShenHe(59, "{0}重复审核"), BaoHaoBuZhengQue(60, "包号不正确"), BAO_HAO_BU_CUN_ZAI(61, "包号不可以为0"), XiaoJianYuanYouQianKuan(62,
			"此小件员上次交款有欠款，补齐欠款才能继续领货！"), YPDJSTATE_CONTROL_ERROR(63, "一票多件上一状态为{0}的订单未完成，不允许进行{1}操作"), Qing_SAO_MIAO_YUN_DAN_HAO(64, "请扫描运单号"), Qing_Xuan_Ze_Xiao_Jian_Yuan(555555, "请检查小件员是否选择"), Fen_Zhan_Dao_Huo_Ding_Dan_Bu_Yun_Xu_Shi_Xiao(
			70, "分站到货订单不允许失效"), Fei_Dai_Fan_Dan(71, "非待返单"), Chong_Fu_Sao_Miao(72, "重复扫描"), FEI_BEN_ZHAN_HUO(73, "非本站货"), Wei_Shua_Ka_Yuan_Yin(74, "请选择未刷卡原因"), Bei_Zhu_Tai_Chang(75, "备注太长"), Shen_Qing_Zhong_Zhuan_Wei_Shen_He_Cheng_Gong_Error(
			76, "订单未申请中转或者未审核成功，不允许进行中转出站操作"), Bao_Hao_Bu_Keyi_Duozhan_Shiyong(77, "当前包号出给【{0}】,不能再出给【{1}】"), Fei_Kuai_Di_Dan_Bu_Yun_Xu_Lan_Shou_Dao_Huo(78, "非快递单不允许进行揽收到货操作"), JieSuanQianKuan(79,
			"结算欠款金额验证不通过"), JieSuanJiaKuan(80, "结算加款金额验证不通过"), JieSuanJianKuan(81, "结算减款金额验证不通过"), JieSuanChongFuTiJiao(82, "结算重复提交!"), ZhanDianYuEBuZu(83, "站点余额不足"), ZhangDanWeiJieSuan(84,
			"您有未结算的账单,请与财务联系"), LingHuoWeiTuoYiChangDingDan(85, "委托领货异常，订单不在可领货委托派送的数据中"), XiaoJianYuanBuYunXuLingHuoWeiTuo(86, "该订单已是此小件员的货，不允许领货委托派送"), FeiBenZhanDianDingDanBuYunXuZhongZhuanChuZhan(
			87, "非本站点订单，不允许进行中转出站操作"), PiLiangFanKuiZhiFuFangShiXuYiZhi(88, "批量反馈前置支付方式需一致"), // 目前只适用于亚马逊
	KouKuanLiuChengJianCha(89, "请检查流程设置,到货扫描不允许做到错货"), KouKuanLiuChengJianChaQiangZhiChuKu(90, "您当前选择扣款模式,不允许强制出库"), KouKuanLiuChengJianCha1(91, "请检查流程设置,到错货不允许做到货扫描"), KouKuanLiuChengJianCha2(92,
			"请检查流程设置,到错货不允许做领货扫描"),

	// BU_SHI_ZHE_GE_MU_DI_DI(49,"不是这个目的地的货，目的站：{0}"),
	Bale_Error(93, "{0}包号{1}!"), Bale_Error1(94, "订单{0}已经在{1}包号中{2}!"), Bale_ChongXinFengBao(95, "订单{0}已经在{1}包号中，确认重新封包吗?"),
	// Bale_ChongFu_Error(96,"重复封包"),
	Bale_BU_SHI_ZHE_GE_MU_DI(96, "不是这个目的地的包，目的站：{0}"), Bale_Fei_Ben_Zhan(97, "非本站包"),

	Peisong_Bu_YunXu_ZhongZhuan(98, "配送单不允许做中转"), FeiDaorushujuandrukunotallowshixiao(99, "非导入数据或入库订单不允许失效"), FeiDaorushujunotallowshixiao(100, "非导入数据不允许失效"), GongDanZhuangTaiWeiZhaoDao(101,
			"工单状态未找到"), Fei_ZhongZhuan_Tuihuo(103, "非待中转、待退货不能做此操作"), GongHuoShang_Bufu(104, "请选择正确的供货商"), WuShuJuYouHuoWuDanError(105, "无数据，有货无单"), ShangWeiPiPeiZhanDian(102, "尚未匹配站点"), ShangWeiPiPeiZhanDianBuyunxuRuku(
			202, "尚未匹配站点,不允许入库"),

	FANKUI_KUAIDIDANHAO_YIGUANLIAN(150, "此快递单号已经和其它订单关联！"), Shen_Qing_Tui_Huo_Wei_Shen_He_Cheng_Gong_Error(106, "订单未进行审核或者未审核成功，不允许进行退货出站操作"), Shen_Qing_Tui_Huo_Zhi_Liu_Wu_Fa_Tui_Huo_Error(107,
			"退货单审核为站点滞留，不允许进行退货出站操作"), GongDanLeiXingWeiZhaoDao(108, "工单类型未找到"), Chong_Fu_Ling_Huo(109, "重复扫描，派送员已领货！"), FeiDiYiCiRuKuBuNengShiXiao(110, "该订单非第一次入库，不允许失效！"), Tui_huo_chu_zhan_dai_shen_he(
			111, "退货出站待审核状态,请先进行审核!"), Fenzhanzhiliustatenotzhongzhanchuzhan(112, "分站滞留的订单不允许中转出站！！"), DaizhongzhuanshenheCannotLinghuo(113, "待中转审核的订单或者审核通过的订单不允许领货"), ShenheweijushouCannotZhongzhuanchuzhan(
			114, "审核为拒收的订单不允许中转出站"), Shenhebutongguobuyunxuzhongzhuan(115, "审核不通过的订单不允许{0}！！"), Wei_Shen_he_huozhe_shen_he_butongguo(116, "未审核或者审核不通过的订单不允许合包出库"), Weishenhebuxuzhongzhuan(117,
			"未审核的订单不允许{0}！！"), Weishenhebuxutuihuoruku(118, "未审核的订单不允许退货入库！！"), Shenheweiquerentuihuo(119, "审核为确认退货的订单请先进性退货出站！！"), Shenheweizhandianpeisong(120, "审核为站点配送的订单不允许退货出站或退货入库或退货再投或退客户出库！！"), Shenheweiquerentuihuosuccess(
			121, "待退货出站审核的订单不可以进行领货操作！！"), Weishenhebuxuzhongzhuanchuku(122, "未审核的订单不允许中转出站！！"), Shenhebutongguobuyunxuzhongzhuanchuku(123, "审核不通过的订单不允许中转出站！！"), Weishenhebuxuhebaochuku(124,
			"未审核的订单不允许合包出站！！"), Shenhebutongguobuyunxuhebaochuku(125, "审核不通过的订单不允许合包出站！！"), Weishenhebuxuzhongzhuankuchuku(126, "未审核的订单不允许中转库出库！！"), Shenhebutongguobuyunxuzhongzhuankuchuku(127,
			"审核不通过的订单不允许中转库出库！！"), Weishenhebuxuzhongzhuankuhebaochuku(128, "未审核的订单不允许中转库合包出库！！"), Shenhebutongguobuyunxuzhongzhuankuhebaochuku(129, "审核不通过的订单不允许中转库合包出库！！"), Tui_huo_chu_zhan_shen_he_shenhe_zhandianpeisong(
			130, "审核为站点配送的订单不允许退货出站!"),

	DaizhongzhuanshenheCannotlinghuo(131, "待中转审核的订单不允许领货"), ShenhetongguoCannotlinghuo(132, "审核通过的订单不允许领货"), Tuihuoquerensuccess(133, "审核确认退货的订单不可以进行领货操作！！"), DaizhongzhuanCannotlinghuo(134,
			"反馈待中转的订单不允许领货"), OXO_JIT_DISALLOW_RECEIVEGOODS(135, "OXO_JIT类型的订单不允许领货"), OXO_JIT_DISALLOW_TRANSFER_STATION_OUTBOUND(136, "OXO_JIT类型的订单不允许中转出站或站点出站"), NOT_EXPRESS_CWB(137,
			"单号{0}不是快递单，不允许揽件出站"), NotIntoStation(147, "没做入站操作的快递单不允许进行揽件出站操作"), ExpressNotAllowTUIHUOCHUZHAN(148, "快递单不允许做退货出站操作"), ExpressInstationNotAllowZHONGZHUANCHUZHAN(149,
			"快递单揽件入站状态不允许做中转出站操作"), ExpressLuruNotAllowZHONGZHUANCHUZHAN(150, "快递单揽件录入状态不允许做中转出站操作"), BaleCannotUse(151, "该包已拆包"), BuNengKuaHuanJieRuKu(152, "快递单不能跨环节入库"), BuNengKuaHuanJieRuZhan(152,
			"快递单不能跨环节入站"), CURRENT_BRANCH_NOT_MATCH(138, "不是这个站点的运单/包号,下一站为{0}站"), REPEATED_OUT_STATION(139, "运单/包号重复出站,下一站为{0}"), NEXT_BRANCH_NOT_MATCH(139, "页面所选下一站{0}和运单/包的下一站{1}不一致"), YIPIAODUOJIAN_DAOHUOBUQUAN(
			140, "一票多件订单到货不全,不许领货操作!"), SHANGMENJUTUI_BUYUNXU_TUIHUOCHUZHAN(141, "审核为上门拒退的订单不允许做退货出站操作!"), TUIKEHU_DAIQUEREN(142, "退客户待确认的订单禁止退客户拒收返库!"), TUIKEHU_SUCCESS(143, "退客户成功的订单禁止退客户拒收返库!"), YIJINGSHANGXIAN(
			144, "此单发货件数已经达到上限了,不允许{0}操作"), YIJINGFENGBAO(145, "此单已经在{0}封包了,不允许{1}操作"), SMHorSMTBXYTHCZ(146, "{0}类型的订单,不允许反馈为{1}"), PeisongDan_vipshop_weiwanjie(147, "唯品会配送单状态未完结，不能操作当前揽退单:{0}"), Shangmentui_vipshop_shixiao(
			148, "当前操作订单{0}已失效，请关联订单{1}"), LingHuo_ZhiFuXinxiWeiQueRen(149, "订单修改未审核，请联系客服人员！"), Do_Not_Have_Addition_Record(150, "快递单未完成录入，不能入库！"), ZHANDIANHEBAOBUYUNXUBENZHANDAOHUO(151,
			"快递揽件合包后不允许本站直接按包到货扫描"), Second_Level_Station_Validation(152, "二级站的订单或包不允许直接入库！"),
			
			OUTWAREHOUSE_MPS_NOT_ALL_ARRIVED(160, "一票多件货物未到齐，需要库房集单完成之后才能够处理出库操作!"), DELIVERTAKEGOODS_MPS_NOT_ALL_ARRIVED(
					161, "一票多件货物未到齐，需要站点集单完成之后才能够分配小件员进行配送操作!"), TRANSORDER_LOST(162, "此{0}运单因{1}已导致丢失，已被客服人员操作拦截!"), TRANSORDER_BROKEN(163, "此{0}运单因{1}已导致破损，已被客服人员操作拦截，请将此件货物给到退货组！"), TRANSORDER_LOST_RETURN(
					164, "{0}订单中的{1}运单，因{2}已丢失，请将此件货物给到退货组!"), TRANSORDER_BROKEN_RETURN(165, "{0}订单中的{1}运单，因{2}已破损，请将此件货物给到退货组!"), TRANSCWB_STATE_CONTROL_ERROR(166, "状态为{0}的运单不允许进行{1}操作"), NEXT_STATION_NOT_UNIQUE_ERROR(
					167, "配置的逆向退货组不唯一"), NONE_NEXT_STATION_ERROR(168, "没有配置逆向的退货组"), TRANSORDER_RETURN(169, "{0}订单中的{1}运单已退货，请将此件货物给到退货组!"), TUIHUOZHONGZHUANRUKU_NONSUPPORT_MPS(170, "退货中转入库不支持一票多件订单!"), FENJIANZHONGZHUANCHUKU_NONSUPPORT_MPS(
					171, "分拣中转出库不支持一票多件订单!"),
	
			BU_SHI_ZHE_GE_MU_DI_DI_zong(200, "不是这个目的地的货"),
			Weishenhebuxutuihuozaitou(201,"未审核的订单不允许退货再投！！"),
			SHANG_MEN_TUI_CHENG_GONG_TUI_HUO_CHU_ZHAN(203, "上门退成功的订单，不能做 “{0}” 的操作"),
			TUI_HUO_CHU_ZHAN_SHEN_HE_ZHONG_LING_HUO(204, "退货出站审核中，不能做“{0}”操作"),
	        SHANG_MEN_TUI_PEI_SONG_JU_SHOU(205 ,"上门退订单关联的配送订单反馈拒收不能领货！ "),
			YUN_DAN_ZHAN_DIAN_YU_YONG_HU_ZHAN_DIAN_BU_YI_ZHI(210, "运单所在站点与当前用户所在站点不一致"),
			DING_DAN_ZHAN_DIAN_YU_YONG_HU_ZHAN_DIAN_BU_YI_ZHI(206, "订单所在站点与当前用户所在站点不一致"),
			BU_CUN_ZAI_TUI_HUO_CHU_ZHAN_SHEN_HE_JI_LU(207, "不存在退货出站审核记录"),
			BU_CUN_ZAI_TUI_HUO_CHU_ZHAN_SHEN_HE_WEI_ZHAN_DIAN_PEI_SONG(208, "不存在退货出站审核为站点配送的记录"),
			YUN_DAN_ZHUANG_TAI_BU_SHI_PEI_SONG(209, "运单{0}状态不是配送，不允许领货"),
			FEI_PEI_SONG_BANG_DING_EMS_TRANS(216,"非配送单不允许绑定邮政运单号！"),
			BU_SHI_GAI_PEI_SONG_ZHAN_EMS_CWB(211,"订单目的站点不是邮政站点！"),
			DAI_SHOU_HUO_KUAN_DA_LING(212,"订单代收货款大于0或者到付运费大0，不能绑定邮政运单号！"),
			DING_DAN_ZHUANG_TAI_BU_SHI_CHU_KU(213,"订单当前操作状态不是出库，不能绑定邮政运单号！"),
			JI_DAN_BU_FA_SONG_YOU_ZHENG(214,"订单为集单模式，不能绑定邮政运单号！"),
			DING_DAN_BANG_DING_SHU_CHAO_CHU(215,"一票多件绑定次数已经超出订单发货数量！"),
			PEI_SONG_YUAN_BU_PI_PEI(218, "此订单匹配的是{0}，{1}不能进行领货！"),
			PEI_SONG_YUAN_WEI_PI_PEI(217, "此订单未匹配配送员，不能进行领货！"),
			AUTO_ALLOCATING_BUT_DISCOUNTED_PLEASE_HANDLE(220, "连接自动分拨机失败，不能入库！请取消自动分拨功能，或重新连接。"),
			DIFF_TUI_HUO_KU_ZUO_BACK_TO_CUSTOMER(222, "不在同一个退货库做退供货商出库"),
			Ling_huo_EXPRESS_LIMIT(221,"非本站揽收/配送的快递单不能领货！"),
			PLEASE_RECEIVE_GOODS_FIRST(227, "请先做{0}");
	

	private int value;
	private String text;

	private ExceptionCwbErrorTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

}
