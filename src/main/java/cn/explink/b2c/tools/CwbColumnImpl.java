package cn.explink.b2c.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.amazon.CwbColumnSetAmazon;
import cn.explink.b2c.benlaishenghuo.CwbColumnSetBenlaishenghuo;
import cn.explink.b2c.dangdang_dataimport.CwbColumnSetDangDang;
import cn.explink.b2c.dongfangcj.CwbColumnSetDongFangCJ;
import cn.explink.b2c.dpfoss.CwbColumnSetDpfoss;
import cn.explink.b2c.efast.CwbColumnSetEfast;
import cn.explink.b2c.explink.core_down.CwbColumnSetEpaiAPI;
import cn.explink.b2c.feiniuwang.CwbColumnSetFNW;
import cn.explink.b2c.gome.CwbColumnSetGome;
import cn.explink.b2c.gxdx.CwbColumnSetGXDX;
import cn.explink.b2c.gzabc.CwbColumnSetGZABC;
import cn.explink.b2c.gztl.CwbColumnSetGztl;
import cn.explink.b2c.haoxgou.CwbColumnSetHXG;
import cn.explink.b2c.happyGo.CwbColumnSetHappy;
import cn.explink.b2c.homegobj.CwbColumnSetHomegobj;
import cn.explink.b2c.homegou.CwbColumnSetHomegou;
import cn.explink.b2c.huitongtx.CwbColumnSetHuitongtx;
import cn.explink.b2c.hxgdms.CwbColumnSetHxgdms;
import cn.explink.b2c.hzabc.CwbColumnSetHZABC;
import cn.explink.b2c.jiuye.CwbColumnSetJiuye;
import cn.explink.b2c.liantong.CwbColumnSetLiantong;
import cn.explink.b2c.maikaolin.CwbColumnSetMaikaolin;
import cn.explink.b2c.meilinkai.CwbColumnSetMLK;
import cn.explink.b2c.mss.CwbColumnSetMSS;
import cn.explink.b2c.pinhaohuo.CwbColumnSetPinhaohuo;
import cn.explink.b2c.rufengda.CwbColumnSetRufengda;
import cn.explink.b2c.saohuobang.CwbColumnSetSaohuobang;
import cn.explink.b2c.sfxhm.CwbColumnSetSfxhm;
import cn.explink.b2c.smile.CwbColumnSetSmile;
import cn.explink.b2c.suning.CwbColumnSetSuNing;
import cn.explink.b2c.telecomsc.CwbColumnSetTelecom;
import cn.explink.b2c.tmall.CwbColumnSet;
import cn.explink.b2c.tmall.CwbColumnSetTmall;
import cn.explink.b2c.tps.CwbColumnSetTPS;
import cn.explink.b2c.vipshop.CwbColumnSetVipShop;
import cn.explink.b2c.wangjiu.CwbColumnSetWangjiu;
import cn.explink.b2c.wenxuan.CwbColumnSetWenxuan;
import cn.explink.b2c.yangguang.CwbColumnSetYangGuang;
import cn.explink.b2c.yihaodian.CwbColumnSetYihaodian;
import cn.explink.b2c.yixun.CwbColumnSetYiXun;
import cn.explink.b2c.yonghui.CwbColumnSetYH;
import cn.explink.b2c.yonghuics.CwbColumnSetYonghui;
import cn.explink.b2c.zhongliang.CwbColumnSetZhongliang;
import cn.explink.domain.ExcelColumnSet;

@Service
public class CwbColumnImpl implements CwbColumnSet {
	@Autowired
	CwbColumnSetTmall cwbColumnSetTmall;
	@Autowired
	CwbColumnSetVipShop cwbColumnSetVipShop;
	@Autowired
	CwbColumnSetYihaodian cwbColumnSetYihaodian;
	@Autowired
	CwbColumnSetYiXun cwbColumnSetYiXun;
	@Autowired
	CwbColumnSetRufengda cwbColumnSetRufengda;
	@Autowired
	CwbColumnSetGome cwbColumnSetGome;
	@Autowired
	CwbColumnSetDangDang cwbColumnSetDangDang;
	@Autowired
	CwbColumnSetYangGuang cwbColumnSetYangGuang;
	@Autowired
	CwbColumnSetGZABC cwbColumnSetGZABC;
	@Autowired
	CwbColumnSetHZABC cwbColumnSetHZABC;
	@Autowired
	CwbColumnSetDongFangCJ cwbColumnSetDongFangCJ;
	@Autowired
	CwbColumnSetHXG cwbColumnSetHXG;
	@Autowired
	CwbColumnSetAmazon cwbColumnSetAmazon;
	@Autowired
	CwbColumnSetDpfoss cwbColumnSetDpfoss;
	@Autowired
	CwbColumnSetHomegou cwbColumnSetHomegou;
	@Autowired
	CwbColumnSetMaikaolin columnSetMaikaolin;
	@Autowired
	CwbColumnSetHappy columnSetHappy;
	@Autowired
	CwbColumnSetHuitongtx cwbColumnSetHuitongtx;

	@Autowired
	CwbColumnSetTelecom cwbColumnSetTelecom;
	@Autowired
	CwbColumnSetBenlaishenghuo columnSetBenlaishenghuo;

	@Autowired
	CwbColumnSetSaohuobang columnSetSaohuobang;
	@Autowired
	CwbColumnSetEpaiAPI cwbColumnSetEpaiAPI;
	@Autowired
	CwbColumnSetEfast cwbColumnSetEfast;
	@Autowired
	CwbColumnSetSmile cwbColumnSetSmile;
	@Autowired
	CwbColumnSetLiantong cwbColumnSetLiantong;
	@Autowired
	CwbColumnSetYonghui cwbColumnSetYonghui;
	@Autowired
	CwbColumnSetHxgdms cwbColumnSetHxgdms;
	@Autowired
	CwbColumnSetWangjiu cwbColumnSetWangjiu;
	@Autowired
	CwbColumnSetHomegobj cwbColumnSetHomegobj;
	@Autowired
	CwbColumnSetSfxhm cwbColumnSetSfxhm;
	@Autowired
	CwbColumnSetZhongliang cwbColumnSetZhongliang;
	@Autowired
	CwbColumnSetWenxuan cwbColumnSetWenxuan;

	@Autowired
	CwbColumnSetGztl cwbClolumSetGztl;
	@Autowired
	CwbColumnSetJiuye cwbColumnSetJiuye;
	@Autowired
	CwbColumnSetGXDX  cwbColumnsetGXDX;
	@Autowired
	CwbColumnSetSuNing cwbColumnSetSuNing;
	@Autowired
	CwbColumnSetMLK cwbColumnSetMLK;
	@Autowired
	CwbColumnSetFNW cwbColumnSetFNW;
	@Autowired
	CwbColumnSetYH cwbColumnSetYH;
	@Autowired
	CwbColumnSetPinhaohuo cwbColumnSetPinhaohuo;
	@Autowired
	CwbColumnSetTPS cwbColumnSetTPS;
	@Autowired
	CwbColumnSetMSS cwbColumnSetMSS;
	/**
	 * 根据不同的b2c标识来设置导入规则 验证参数是否合格
	 */
	@Override
	public ExcelColumnSet getEexcelColumnSetByB2cJoint(String b2cFlag) {
		// TODO Auto-generated method stub
		if (b2cFlag.equals(B2cEnum.Tmall.getMethod())) {
			return this.cwbColumnSetTmall.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.contains("vipshop")) {
			return this.cwbColumnSetVipShop.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.contains(B2cEnum.Yihaodian.getMethod())) { // 主体部分
																		// yihaodian
			return this.cwbColumnSetYihaodian.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.YiXun.getMethod())) {
			return this.cwbColumnSetYiXun.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.contains("rufengda")) {
			return this.cwbColumnSetRufengda.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.Gome.getMethod())) {
			return this.cwbColumnSetGome.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.DangDang_daoru.getMethod())) {
			return this.cwbColumnSetDangDang.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.YangGuang.getMethod())) {
			return this.cwbColumnSetYangGuang.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.GuangZhouABC.getMethod())) {
			return this.cwbColumnSetGZABC.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.HangZhouABC.getMethod())) {
			return this.cwbColumnSetHZABC.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.DongFangCJ.getMethod())) {
			return this.cwbColumnSetDongFangCJ.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.HaoXiangGou.getMethod())) {
			return this.cwbColumnSetHXG.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.Amazon.getMethod())) {
			return this.cwbColumnSetAmazon.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.DPFoss1.getMethod())) {
			return this.cwbColumnSetDpfoss.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.HomeGou.getMethod())) {
			return this.cwbColumnSetHomegou.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.Maikaolin.getMethod())) {
			return this.columnSetMaikaolin.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.Huitongtx.getMethod())) {
			return this.cwbColumnSetHuitongtx.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.happyGo.getMethod())) {
			return this.columnSetHappy.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.benlaishenghuo.getMethod())) {
			return this.columnSetBenlaishenghuo.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.saohuobang.getMethod())) {
			return this.columnSetSaohuobang.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.Telecomshop.getMethod())) {
			return this.cwbColumnSetTelecom.getEexcelColumnSetByB2c(b2cFlag);
		}
		// 易派系统对接入口
		else if (b2cFlag.equals("epai")) {
			return this.cwbColumnSetEpaiAPI.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.EfastERP.getMethod())) {
			return this.cwbColumnSetEfast.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.Smile.getMethod())) {
			return this.cwbColumnSetSmile.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.Liantong.getMethod())) {
			return this.cwbColumnSetLiantong.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.YongHuics.getMethod())) {
			return this.cwbColumnSetYonghui.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.Hxgdms.getMethod())) {
			return this.cwbColumnSetHxgdms.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.Wangjiu.getMethod())) {
			return this.cwbColumnSetWangjiu.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.HomegoBJ.getMethod())) {
			return this.cwbColumnSetHomegobj.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.SFexpressXHM.getMethod())) {
			return this.cwbColumnSetSfxhm.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.LeChong.getMethod())) {
			return this.cwbColumnSetHomegobj.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.SFexpressXHM.getMethod())) {
			return this.cwbColumnSetSfxhm.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.contains("zhongliang")) {
			return this.cwbColumnSetZhongliang.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.Wenxuan.getMethod())) {
			return this.cwbColumnSetWenxuan.getEexcelColumnSetByB2c(b2cFlag);
		} else if (b2cFlag.equals(B2cEnum.Guangzhoutonglu.getMethod())) {
			return this.cwbClolumSetGztl.getEexcelColumnSetByB2c(b2cFlag);
		}else if (
				(b2cFlag.equals(B2cEnum.JiuYe1.getMethod())
				||(b2cFlag.equals(B2cEnum.JiuYe2.getMethod()))
				||(b2cFlag.equals(B2cEnum.JiuYe3.getMethod()))
				||(b2cFlag.equals(B2cEnum.JiuYe4.getMethod()))
				||(b2cFlag.equals(B2cEnum.JiuYe5.getMethod())))) 
		{
			return this.cwbColumnSetJiuye.getEexcelColumnSetByB2c(b2cFlag);
		}else if (b2cFlag.equals(B2cEnum.Yonghui.getMethod())) {
			return this.cwbColumnSetYH.getEexcelColumnSetByB2c(b2cFlag);
		}else if(b2cFlag.equals(B2cEnum.GuangXinDianXin.getMethod())){
			return this.cwbColumnsetGXDX.getEexcelColumnSetByB2c(b2cFlag);
		}else if(b2cFlag.equals(B2cEnum.SuNing.getMethod())){
			return this.cwbColumnSetSuNing.getEexcelColumnSetByB2c(b2cFlag);
		}else if(b2cFlag.equals(B2cEnum.meilinkai.getMethod())){
			return this.cwbColumnSetMLK.getEexcelColumnSetByB2c();
		}else if(b2cFlag.equals(B2cEnum.Feiniuwang.getMethod())){
			return this.cwbColumnSetFNW.getEexcelColumnSetByB2c(b2cFlag);
		}else if(b2cFlag.equals(B2cEnum.PinHaoHuo.getMethod())){
			return this.cwbColumnSetPinhaohuo.getEexcelColumnSetByB2c(b2cFlag);
		}else if (b2cFlag.contains("tps")) {
			return this.cwbColumnSetTPS.getTPSOrderColumnSetByB2c(b2cFlag);
		}else if(b2cFlag.equals(B2cEnum.MSS.getMethod())){
			return this.cwbColumnSetMSS.getEexcelColumnSetByB2c(b2cFlag);
		}

		return null;
	}

	public static void main(String[] args) {

	}

}
