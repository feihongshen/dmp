package cn.explink.b2c.tools;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;

public enum B2cEnum {

	/**
	 * 新增接口类型 api_type
	 * 0:B2C提供接口;1:explink提供查询接口;2:以explink文档为主的开发。（包括主动推送订单状态，接收导入数据等等。）
	 */

	LieBo(20001, "裂帛服饰", "liebo", 1), // 裂帛
	DangDang(20002, "当当网-状态反馈", "dangdang", 0), // 当当
	JUShang(20003, "聚尚网", "jushangwang", 1), // 聚尚
	MinSiDa(20004, "敏思达", "minsida", 2), // 敏思达
	Tmall(20005, "天猫-淘宝商城", "tmall", 0), JuMeiYouPin(20006, "聚美优品", "jumeiyoupin", 0), // 聚美优品
	KuaiDi100(20007, "快递100", "kuaidi100", 1), // 快递100
	Yihaodian(20009, "一号店", "yihaodian", 0),

	VipShop_shanghai(20008, "唯品会_上海仓", "vipshop_shanghai", 0), // 唯品会shanghai
	VipShop_beijing(20010, "唯品会_北京仓", "vipshop_beijing", 0), // 唯品会 beijing
	VipShop_nanhai(20011, "唯品会_南海仓", "vipshop_nanhai", 0), // 唯品会 nanhai //华南仓

	VipShop_huabei(20012, "唯品会_华北仓", "vipshop_huabei", 0), // 唯品会 VipShop_huabei
	VipShop_huanan(20013, "唯品会_华南仓", "vipshop_huabei", 0), // 唯品会 VipShop_huanan
	LianHuaYiGou(20014, "联华易购", "LianHuaYiGou", 1), // 联华易购
	Smile(20015, "广州思迈速递", "Smile", 0), LefengWang(20016, "乐蜂网", "lefengwang", 0), YiXun(20017, "易迅网", "yixun", 0), Rufengda(20018, "如风达-凡客", "rufengda", 0), VipShop_tuangou(20019, "唯品会_团购仓",
			"vipshop_tuangou", 0), VipShop_chengdu(20020, "唯品会_成都仓", "vipshop_chengdu", 0), // 西南仓
	JingDong(20021, "京东", "jingdong", 0), // 京东
	Gome(20022, "国美", "gome", 0), // 国美
	YouGou(20023, "优购", "优购", 1), // 优购，跟快递100接口一致

	Yihaodian_beijing(20024, "一号店_北京", "yihaodian_beijing", 0), // 一号店 北京

	Tmall_ESuBao(20025, "天猫-E速宝", "tmall_esubao", 0), // 直接跟E速报合作的物流公司需配置这个
	Tmall_WanXiangESuBao(20026, "上海万象(天猫-E速宝)", "tmall_wanxiangesubao", 0), // 通过万象跟易速宝合作。
	Tmall_HuiTongTianXia(20027, "汇通天下(使用天猫API)", "tmall_huitongtianxia", 0), // 使用tmall接口的配送方，如汇通天下（汇通则作为tmall，小红帽则作为配送商）
	DangDang_daoru(20028, "当当网-订单导入", "dangdang_datasyn", 0), // 当当订单数据出库接口
	Tmall_ESuBaoShengXian(20029, "E速宝-生鲜", "tmall_esubaoshengxian", 0), // 直接跟E速报合作的物流公司需配置这个

	VipShop_shanghaishandong(20030, "唯品会_上海仓(山东)", "vipshop_shanghaishandong", 0), // 唯品会shanghaishandong
	VipShop_nanhaishandong(20031, "唯品会_南海仓(山东)", "vipshop_nanhaishandong", 0), // 唯品会
																				// nanhaishandong
	YangGuang(20032, "央广购物", "yangguang", 0), // 央广购物
	Tmall_ESuBaobangbaoshi(20033, "E速宝(帮宝适)", "tmall_esubaobangbaoshi", 0), // 通过万象跟易速宝合作。
	YeMaiJiu(20034, "也买酒", "yemaijiu", 0), GuangZhouABC(20035, "广州爱彼西", "guangzhouABC", 0), HangZhouABC(20036, "杭州爱彼西", "hangzhouABC", 0), BaShaLiPing(20037, "芭莎礼品", "bashaliping", 0), YeMaiJiuSearch(
			20038, "也买酒_查询", "yemaijiusearch", 1), // 也买酒

	DongFangCJ(20039, "东方CJ", "dongfangcj", 0), // 东方CJ
	Amazon(20040, "亚马逊", "amazon", 0), // 亚马逊
	HaoXiangGou(20041, "好享购", "haoxianggou", 0), // 好享购J
	Rufengda_shandong(20042, "如风达-凡客(山东)", "rufengda_shandong", 0),

	Rufengda_gansu(20043, "如风达-凡客(甘肃)", "rufengda_gansu", 0), Rufengda_ningxia(20044, "如风达-凡客(宁夏)", "rufengda_ningxia", 0), Rufengda_shanxi(20045, "如风达-凡客(陕西)", "rufengda_shanxi", 0), Rufengda_qinghai(
			20046, "如风达-凡客(青海)", "rufengda_qinghai", 0),

	LeJieDi(20048, "乐捷递", "lejiedi", 1), // 乐捷递
	HomeGou(20049, "家有购物", "homegou", 0), // 家有购物
	Yihaodian_shandong(20050, "一号店_山东", "yihaodian_shandong", 0), // 一号店 山东
	Tmall_zhouqigou(20051, "天猫-周期购", "tmall_zhouqigou", 0), // 新的宅配服务

	Moonbasa(20052, "梦芭莎", "moonbasa", 0),

	DPFoss1(20047, "德邦物流1", "DPFoss1", 0), DPFoss2(20053, "德邦物流2", "DPFoss2", 0), DPFoss3(20054, "德邦物流3", "DPFoss3", 0), DPFoss4(20055, "德邦物流4", "DPFoss4", 0), Huitongtx(20056, "汇通天下", "huitongtx", 0), VipShop_cangku1(
			20057, "唯品会_仓库1", "vipshop_cangku1", 0), VipShop_cangku2(20058, "唯品会_仓库2", "vipshop_cangku2", 0), VipShop_cangku3(20059, "唯品会_仓库3", "vipshop_cangku3", 0), Liantong(20061, "联通",
			"liantong", 0), saohuobang(20065, "扫货帮", "saohuobang", 0), saohuobang_2(20070, "扫货帮美甲", "saohuobang_2", 0), Maikaolin(20060, "麦考林", "maikaolin", 0), // 麦考林
	Wanxiang(20062, "万象-查询", "wangxiang", 0), Telecomshop(20063, "电信商城", "telecomshop", 0),

	Tmall_bak1(20064, "天猫-备用1", "tmall_bak1", 0), // 新的宅配服务
	Tmall_bak2(20066, "天猫-备用2", "tmall_bak2", 0), // 新的宅配服务

	benlaishenghuo(20067, "本来生活", "benlaishop", 0), Jiuxian(20068, "酒仙", "jiuxian", 0), VipShopAddress(20069, "唯品会面单站点前置", "vipshopaddress", 0), happyGo(20071, "快乐购", "happyGo", 0), EfastERP(20072,
			"中兴云购ERP", "efastERP", 0), Maisike(20073, "迈思可", "maisike", 0), wholeLine(20074, "全线快递", "wholeline", 0), yhdAddmatch(20075, "一号店-站点匹配", "yhdAddmatch", 0), MaiMaiBao(20076, "买卖宝", "mmb",
			0), ChinaMobile(20077, "中国移动", "chinamobile", 0), Letv(20078, "乐视网", "letv", 0), YongHuics(20079, "永辉超市", "yonghuics", 0), Hxgdms(20080, "好享购DMS", "hxgdms", 0), SFexpress(20081, "顺丰快递",
			"sfexpress", 0), EfastERP_bak(20082, "中兴云购ERP", "efastERP_bak", 0), Wangjiu(20083, "网酒网", "wangjiu", 0), HomegoBJ(20084, "家有购物（北京）", "homegoubj", 0),

	LeChong(20085, "乐宠（科捷）", "lechong", 0), Smiled(20086, "思迈下游", "smiled", 0), SFexpressXHM(20087, "顺丰快递(小红帽)", "sfexpressxhm", 0), Zhongliang(20088, "中粮(我买网)", "zhongliang", 0), Wenxuan(20089,
			"文轩网", "wenxuan", 0), lefengwang(20093, "乐蜂网_对接", "lefengwang_statereturn", 0), Guangzhoutonglu(20095, "广州通路_对接", "guangzhoutonglu", 0), GuangzhoutongluWaifadan(20096, "广州通路对接_外发单",
			"guangzhoutongluwaifadanduijie", 0), VipShop_OXO(20090, "唯品会_oxo", "VIP_OXO", 0), // 唯品会OXO vipshop_oxo
	JiuYe1(20101, "九曵1", "jiuye_1", 0), JiuYe2(20102, "九曵2", "jiuye_2", 0), JiuYe3(20103, "九曵3", "jiuye_3", 0), JiuYe4(20104, "九曵4", "jiuye_4", 0), JiuYe5(20105, "九曵5", "jiuye_5", 0), Feiniuwang(
			20107, "飞牛网(http)", "feiniuwang", 0),
			Yonghui(20108, "永辉物流", "Yonghui", 0),
			JiuYeAddressMatch1(20109, "九曳_站点匹配1", "jiuyeaddressmatch_1", 0), JiuYeAddressMatch2(20110, "九曳_站点匹配2", "jiuyeaddressmatch_2", 0), JiuYeAddressMatch3(
			20111, "九曳_站点匹配3", "jiuyeaddressmatch_3", 0), JiuYeAddressMatch4(20112, "九曳_站点匹配4", "jiuyeaddressmatch_4", 0), JiuYeAddressMatch5(20113, "九曳_站点匹配5", "jiuyeaddressmatch_5", 0), ZheMeng(
			20117, "哲盟-安达信", "zhemeng", 0), FeiyuanAddress(20118, "ZJ飞远站点匹配", "feiyuanAddress", 0), HaoYiGou(20120, "好易购", "haoyigou", 0),
	
	GuangXinDianXin(20121,"广信电信","guangxindianxin",0),
	VipShop_cangku4(20122, "唯品会_仓库4", "vipshop_cangku4",0),
	VipShop_cangku5(20123, "唯品会_仓库5", "vipshop_cangku5",0),
	VipShop_cangku6(20124, "唯品会_仓库6", "vipshop_cangku6",0),
	VipShop_cangku7(20125, "唯品会_仓库7", "vipshop_cangku7",0),
	VipShop_cangku8(20126, "唯品会_仓库8", "vipshop_cangku8",0),
	VipShop_cangku9(29001, "唯品会_仓库9", "vipshop_cangku9",0),
	VipShop_cangku10(29002, "唯品会_仓库10", "vipshop_cangku10",0),
	VipShop_cangku11(29003, "唯品会_仓库11", "vipshop_cangku11",0),
	ThirdPartyOrder_2_DO(20129, "外单推DO", "thirdPartyOrder2DO", 0),
	VipShop_TPSAutomate(20227, "TPS自动化", "vipshop_tps_automate",0),	
	TPS_CarrierOrderStatus(20228, "唯品会_TPS_运单状态", "TPS_CarrierOrderStatus",0),
	TPS_Cwb_Flow(20229, "唯品会_TPS_反馈体积重量", "TPS_Cwb_Flow",0),
	TPS_AUTO(20230, "TPS_华东自动化分拣状态", "TPS_Auto",0),
	SuNing(20127, "苏宁易购", "suning", 0), YiGuoShengXian(20128, "易果生鲜", "yiguoshengxian", 0),
	Yihaodian_bakup1(20130, "一号店_备用1", "yihaodian_bakup1", 0), 
	Yihaodian_bakup2(20131, "一号店_备用2", "yihaodian_bakup2", 0), 
	Yihaodian_bakup3(20132, "一号店_备用3", "yihaodian_bakup3", 0), 
	meilinkai(20135,"玫琳凯","meilinkai",0),
	HuanQiuGou(20136, "环球购物", "huanqiugouwu", 0),
	PinHaoHuo(20140, "拼好货", "pinhaohuo", 0), // 拼好货
	Tonglian(20200,"自动核销-通联-公司信息", "tonglian", 0), 
	Caifutong(20201, "自动核销-财付通-公司信息", "caifutong", 0),
	Zhongliang_bak1(21001, "中粮(我买网)_备用1", "zhongliang_bak1", 0),
	Zhongliang_bak2(21002, "中粮(我买网)_备用2", "zhongliang_bak2", 0),
	JingDong_cwbTrack(22001, "京东_订单跟踪", "jingdong_cwbtrack", 0),
	Shenzhoushuma(20137,"神州数码","shenzhoushuma",0),
	ZheMeng_track(23001, "哲盟_轨迹", "zhemeng_track", 0),
	EMS(22010, "邮政速递", "EMS", 0), 
	TPS_MQ(22501, "tps订单下发接口", "tpsvipshop_mq", 0),
	TPS_MQExpress(22502, "tps快递单下发接口", "tpsvipshop_mqExpress", 0),
	TPS_kuajinggou(22503, "跨境购订单下发接口", "kuajinggouvipshop_mq", 0),
	TPS_TraceFeedback(22601, "订单轨迹回传给tps接口", "tps_traceFeedback", 0),
	/**********************edit by zhouhuan 2016-08-05********/
	TPS_qilekang(22504, "七乐康订单下发接口", "qilekangtpsother", 0),
	TPS_dajiafen(22505, "大家分订单下发接口", "dajiafentpsother", 0),
	TPS_Maikaolin(22506, "麦考林-TPS", "maikaolintpsother",0), // 麦考林
	TPS_Benlaishenghuo(22507, "本来生活-TPS", "bnlaishenghuotpsother",0), // 来生活
	TPS_Weitepai(22508, "微特派-TPS", "weitepaitpsother",0), // 微特派
	TPS_Yousu(22509, "优速-TPS", "yousutpsother",0) //优速
	/**********************edit end ********************/
	;

	 

	private int key;
	private String text;
	private String method;
	private int api_type; // 接口类型

	public int getApi_type() {
		return this.api_type;
	}

	public void setApi_type(int api_type) {
		this.api_type = api_type;
	}

	private B2cEnum(int key, String text, String method, int api_type) {
		this.key = key;
		this.text = text;
		this.method = method;
		this.api_type = api_type;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getKey() {
		return this.key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public static B2cEnum getEnumByKey(int key){
		B2cEnum[] enums = B2cEnum.values();
		for(B2cEnum entity : enums){
			if(entity.getKey() == key){
				return entity;
			}
		}
		return null;
	}
	
	public static B2cEnum[] valuesSortedByText(){
		B2cEnum[] enums = B2cEnum.values();
        Arrays.sort(enums, new Comparator<B2cEnum>(){
			@Override
			public int compare(B2cEnum o1, B2cEnum o2) {
				Collator instance = Collator.getInstance(java.util.Locale.CHINA);
				return instance.compare(o1.getText(), o2.getText());
			}
		});
		return enums;
	}
}
