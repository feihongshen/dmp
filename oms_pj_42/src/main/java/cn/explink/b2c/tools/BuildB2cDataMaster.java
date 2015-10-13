package cn.explink.b2c.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.amazon.BulidAmazonB2cData;
import cn.explink.b2c.chinamobile.BuildChinamobileB2cData;
import cn.explink.b2c.dangdang.BulidDangDangB2cData;
import cn.explink.b2c.dongfangcj.BuildDongFangCJB2cData;
import cn.explink.b2c.dpfoss.BuildDpfossB2cData;
import cn.explink.b2c.gome.BulidGomeB2cData;
import cn.explink.b2c.gzabc.BuildGuangZhouABCB2cData;
import cn.explink.b2c.gztl.BuildGztlB2cData;
import cn.explink.b2c.haoxgou.BuildHaoXiangGouB2cData;
import cn.explink.b2c.happygo.BuildHappyGoB2cData;
import cn.explink.b2c.happygo.BuildHappyGoMethodForYishenhe;
import cn.explink.b2c.homegobj.BuildHomegobjB2cData;
import cn.explink.b2c.homegou.BuildHomegouB2cData;
import cn.explink.b2c.huitongtx.BulidHuitongtxB2cData;
import cn.explink.b2c.hxgdms.BulidHxgdmsB2cData;
import cn.explink.b2c.hzabc.BuildHangZhouABCB2cData;
import cn.explink.b2c.jumeiyoupin.BulidJuMeiB2cData;
import cn.explink.b2c.lechong.BuildLeChongB2cData;
import cn.explink.b2c.lefeng.BuildLefengB2cData;
import cn.explink.b2c.letv.BuildLetvB2cData;
import cn.explink.b2c.liantong.BulidLiantongB2cData;
import cn.explink.b2c.mmb.BuildmmbB2cData;
import cn.explink.b2c.rufengda.BulidRufengdaB2cData;
import cn.explink.b2c.sfxhm.BuildSfxhmB2cData;
import cn.explink.b2c.smile.BulidSmileB2cData;
import cn.explink.b2c.telecomsc.BuildTelecomshopB2cData;
import cn.explink.b2c.tmall.BulidTmallB2cData;
import cn.explink.b2c.vipshop.BulidVipShopB2cData;
import cn.explink.b2c.wangjiu.BuildWangjiuB2cData;
import cn.explink.b2c.wanxiang.BuildWanxiangB2cData;
import cn.explink.b2c.yangguang.BulidYangGuangB2cData;
import cn.explink.b2c.yemaijiu.BuildYeMaiJiuB2cData;
import cn.explink.b2c.yihaodian.BulidYihaodianB2cData;
import cn.explink.b2c.yixun.BulidYiXunB2cData;
import cn.explink.b2c.yonghuics.BulidYonghuiB2cData;
import cn.explink.b2c.zhongliang.BuildZhongliangB2cData;

@Service
public class BuildB2cDataMaster {

	@Autowired
	private BulidJuMeiB2cData bulidJuMeiB2cData;
	@Autowired
	private BulidTmallB2cData bulidTmallB2cData;
	@Autowired
	private BulidDangDangB2cData bulidDangDangB2cData;
	@Autowired
	private BulidVipShopB2cData bulidVipShopB2cData;
	@Autowired
	private BulidYihaodianB2cData bulidYihaodianB2cData;
	@Autowired
	private BulidYiXunB2cData bulidYiXunB2cData;
	@Autowired
	private BulidRufengdaB2cData bulidRufengdaB2cData;
	@Autowired
	private BulidSmileB2cData bulidSmileB2cData;
	@Autowired
	private BulidGomeB2cData bulidGomeB2cData;
	@Autowired
	private BulidYangGuangB2cData bulidYangGuangB2cData;
	@Autowired
	private BuildYeMaiJiuB2cData buildYeMaiJiuB2cData;
	@Autowired
	private BuildGuangZhouABCB2cData buildGuangZhouABCB2cData;
	@Autowired
	private BuildHangZhouABCB2cData buildHangZhouABCB2cData;
	@Autowired
	private BuildDongFangCJB2cData buildDongFangCJB2cData;
	@Autowired
	private BuildHaoXiangGouB2cData buildHaoXiangGouB2cData;
	@Autowired
	private BulidAmazonB2cData bulidAmazonB2cData;
	@Autowired
	private BuildDpfossB2cData buildDpfossB2cData;
	@Autowired
	private BuildHomegouB2cData buildHomegouB2cData;
	@Autowired
	private BulidMaikolinB2cData bulidMaikolinB2cData;
	@Autowired
	private BulidHuitongtxB2cData bulidHuitongtxB2cData;
	@Autowired
	private BulidLiantongB2cData bulidLiantongB2cData;
	@Autowired
	private BuildWanxiangB2cData buildWanxiangB2cData;
	@Autowired
	private BuildSaohuobangB2cData buildSaohuobangB2cData;
	@Autowired
	BuildBenlaiB2cData benlaiB2cData;
	@Autowired
	BuildJiuxianWangB2cData buildJiuxianWangB2cData;
	@Autowired
	BuildHappyGoB2cData buildHappyGoB2cData;
	@Autowired
	BuildHappyGoMethodForYishenhe buildHappyGoMethodForYishenhe;
	@Autowired
	BuildTelecomshopB2cData buildTelecomshopB2cData;
	@Autowired
	BuildmmbB2cData buildmmbB2cData;

	@Autowired
	private BuildLetvB2cData buildLetvB2cData;
	@Autowired
	private BuildChinamobileB2cData buildChinamobileB2cData;
	@Autowired
	private BulidYonghuiB2cData bulidYonghuiB2cData;
	@Autowired
	BulidHxgdmsB2cData bulidHxgdmsB2cData;

	@Autowired
	BuildWangjiuB2cData buildWangjiuB2cData;

	@Autowired
	BuildHomegobjB2cData buildHomegobjB2cData;
	
	public BuildSfxhmB2cData getBuildSfxhmB2cData() {
		return this.buildSfxhmB2cData;
	}

	@Autowired
	BuildLeChongB2cData buildLeChongB2cData;
	@Autowired
	BuildZhongliangB2cData buildZhongliangB2cData;
	@Autowired
	BuildLefengB2cData buildLefengB2cData;

	public BuildLefengB2cData getBuildLefengB2cData() {
		return this.buildLefengB2cData;
	}

	public void setBuildLefengB2cData(BuildLefengB2cData buildLefengB2cData) {
		this.buildLefengB2cData = buildLefengB2cData;
	}

	public void setBuildSfxhmB2cData(BuildSfxhmB2cData buildSfxhmB2cData) {
		this.buildSfxhmB2cData = buildSfxhmB2cData;
	}

	@Autowired
	BuildSfxhmB2cData buildSfxhmB2cData;
	@Autowired
	BuildGztlB2cData buildGztlB2cData;

	public BuildGztlB2cData getBuildGztlB2cData() {
		return this.buildGztlB2cData;
	}

	public void setBuildGztlB2cData(BuildGztlB2cData buildGztlB2cData) {
		this.buildGztlB2cData = buildGztlB2cData;
	}

	public BuildHomegobjB2cData getBuildHomegobjB2cData() {
		return this.buildHomegobjB2cData;
	}

	public void setBuildHomegobjB2cData(BuildHomegobjB2cData buildHomegobjB2cData) {
		this.buildHomegobjB2cData = buildHomegobjB2cData;
	}

	public BuildWangjiuB2cData getBuildWangjiuB2cData() {
		return this.buildWangjiuB2cData;
	}

	public void setBuildWangjiuB2cData(BuildWangjiuB2cData buildWangjiuB2cData) {
		this.buildWangjiuB2cData = buildWangjiuB2cData;
	}

	public BulidHxgdmsB2cData getBulidHxgdmsB2cData() {
		return this.bulidHxgdmsB2cData;
	}

	public void setBulidHxgdmsB2cData(BulidHxgdmsB2cData bulidHxgdmsB2cData) {
		this.bulidHxgdmsB2cData = bulidHxgdmsB2cData;
	}

	public BulidYonghuiB2cData getBulidYonghuiB2cData() {
		return this.bulidYonghuiB2cData;
	}

	public void setBulidYonghuiB2cData(BulidYonghuiB2cData bulidYonghuiB2cData) {
		this.bulidYonghuiB2cData = bulidYonghuiB2cData;
	}

	public BuildLetvB2cData getBuildLetvB2cData() {
		return this.buildLetvB2cData;
	}

	public void setBuildLetvB2cData(BuildLetvB2cData buildLetvB2cData) {
		this.buildLetvB2cData = buildLetvB2cData;
	}

	public BuildmmbB2cData getBuildmmbB2cData() {
		return this.buildmmbB2cData;
	}

	public void setBuildmmbB2cData(BuildmmbB2cData buildmmbB2cData) {
		this.buildmmbB2cData = buildmmbB2cData;
	}

	public BuildChinamobileB2cData getBuildChinamobileB2cData() {
		return this.buildChinamobileB2cData;
	}

	public void setBuildChinamobileB2cData(BuildChinamobileB2cData buildChinamobileB2cData) {
		this.buildChinamobileB2cData = buildChinamobileB2cData;
	}

	public BuildHappyGoMethodForYishenhe getBuildHappyGoMethodForYishenhe() {
		return this.buildHappyGoMethodForYishenhe;
	}

	public void setBuildHappyGoMethodForYishenhe(BuildHappyGoMethodForYishenhe buildHappyGoMethodForYishenhe) {
		this.buildHappyGoMethodForYishenhe = buildHappyGoMethodForYishenhe;
	}

	public BuildHappyGoB2cData getBuildHappyGoB2cData() {
		return this.buildHappyGoB2cData;
	}

	public void setBuildHappyGoB2cData(BuildHappyGoB2cData buildHappyGoB2cData) {
		this.buildHappyGoB2cData = buildHappyGoB2cData;
	}

	public BuildSaohuobangB2cData getBuildSaohuobangB2cData() {
		return this.buildSaohuobangB2cData;
	}

	public BuildTelecomshopB2cData getBuildTelecomshopB2cData() {
		return this.buildTelecomshopB2cData;
	}

	public void setBuildTelecomshopB2cData(BuildTelecomshopB2cData buildTelecomshopB2cData) {
		this.buildTelecomshopB2cData = buildTelecomshopB2cData;
	}

	public void setBuildSaohuobangB2cData(BuildSaohuobangB2cData buildSaohuobangB2cData) {
		this.buildSaohuobangB2cData = buildSaohuobangB2cData;
	}

	public BuildWanxiangB2cData getBuildWanxiangB2cData() {
		return this.buildWanxiangB2cData;
	}

	public void setBuildWanxiangB2cData(BuildWanxiangB2cData buildWanxiangB2cData) {
		this.buildWanxiangB2cData = buildWanxiangB2cData;
	}

	public BulidLiantongB2cData getBulidLiantongB2cData() {
		return this.bulidLiantongB2cData;
	}

	public void setBulidLiantongB2cData(BulidLiantongB2cData bulidLiantongB2cData) {
		this.bulidLiantongB2cData = bulidLiantongB2cData;
	}

	public BulidAmazonB2cData getBulidAmazonB2cData() {
		return this.bulidAmazonB2cData;
	}

	public BuildZhongliangB2cData getBuildZhongliangB2cData() {
		return this.buildZhongliangB2cData;
	}

	public void setBuildZhongliangB2cData(BuildZhongliangB2cData buildZhongliangB2cData) {
		this.buildZhongliangB2cData = buildZhongliangB2cData;
	}

	public void setBulidAmazonB2cData(BulidAmazonB2cData bulidAmazonB2cData) {
		this.bulidAmazonB2cData = bulidAmazonB2cData;
	}

	public BulidMaikolinB2cData getBulidMaikolinB2cData() {
		return this.bulidMaikolinB2cData;
	}

	public void setBulidMaikolinB2cData(BulidMaikolinB2cData bulidMaikolinB2cData) {
		this.bulidMaikolinB2cData = bulidMaikolinB2cData;
	}

	public BulidHuitongtxB2cData getBulidHuitongtxB2cData() {
		return this.bulidHuitongtxB2cData;
	}

	public void setBulidHuitongtxB2cData(BulidHuitongtxB2cData bulidHuitongtxB2cData) {
		this.bulidHuitongtxB2cData = bulidHuitongtxB2cData;
	}

	public BuildHomegouB2cData getBuildHomegouB2cData() {
		return this.buildHomegouB2cData;
	}

	public void setBuildHomegouB2cData(BuildHomegouB2cData buildHomegouB2cData) {
		this.buildHomegouB2cData = buildHomegouB2cData;
	}

	public BuildDpfossB2cData getBuildDpfossB2cData() {
		return this.buildDpfossB2cData;
	}

	public void setBuildDpfossB2cData(BuildDpfossB2cData buildDpfossB2cData) {
		this.buildDpfossB2cData = buildDpfossB2cData;
	}

	public BuildHaoXiangGouB2cData getBuildHaoXiangGouB2cData() {
		return this.buildHaoXiangGouB2cData;
	}

	public void setBuildHaoXiangGouB2cData(BuildHaoXiangGouB2cData buildHaoXiangGouB2cData) {
		this.buildHaoXiangGouB2cData = buildHaoXiangGouB2cData;
	}

	public BuildDongFangCJB2cData getBuildDongFangCJB2cData() {
		return this.buildDongFangCJB2cData;
	}

	public void setBuildDongFangCJB2cData(BuildDongFangCJB2cData buildDongFangCJB2cData) {
		this.buildDongFangCJB2cData = buildDongFangCJB2cData;
	}

	public BuildHangZhouABCB2cData getBuildHangZhouABCB2cData() {
		return this.buildHangZhouABCB2cData;
	}

	public void setBuildHangZhouABCB2cData(BuildHangZhouABCB2cData buildHangZhouABCB2cData) {
		this.buildHangZhouABCB2cData = buildHangZhouABCB2cData;
	}

	public BuildGuangZhouABCB2cData getBuildGuangZhouABCB2cData() {
		return this.buildGuangZhouABCB2cData;
	}

	public void setBuildGuangZhouABCB2cData(BuildGuangZhouABCB2cData buildGuangZhouABCB2cData) {
		this.buildGuangZhouABCB2cData = buildGuangZhouABCB2cData;
	}

	public BuildYeMaiJiuB2cData getBuildYeMaiJiuB2cData() {
		return this.buildYeMaiJiuB2cData;
	}

	public void setBuildYeMaiJiuB2cData(BuildYeMaiJiuB2cData buildYeMaiJiuB2cData) {
		this.buildYeMaiJiuB2cData = buildYeMaiJiuB2cData;
	}

	public BulidYangGuangB2cData getBulidYangGuangB2cData() {
		return this.bulidYangGuangB2cData;
	}

	public void setBulidYangGuangB2cData(BulidYangGuangB2cData bulidYangGuangB2cData) {
		this.bulidYangGuangB2cData = bulidYangGuangB2cData;
	}

	public BulidSmileB2cData getBulidSmileB2cData() {
		return this.bulidSmileB2cData;
	}

	public void setBulidSmileB2cData(BulidSmileB2cData bulidSmileB2cData) {
		this.bulidSmileB2cData = bulidSmileB2cData;
	}

	public BulidRufengdaB2cData getBulidRufengdaB2cData() {
		return this.bulidRufengdaB2cData;
	}

	public void setBulidRufengdaB2cData(BulidRufengdaB2cData bulidRufengdaB2cData) {
		this.bulidRufengdaB2cData = bulidRufengdaB2cData;
	}

	public BulidYihaodianB2cData getBulidYihaodianB2cData() {
		return this.bulidYihaodianB2cData;
	}

	public void setBulidYihaodianB2cData(BulidYihaodianB2cData bulidYihaodianB2cData) {
		this.bulidYihaodianB2cData = bulidYihaodianB2cData;
	}

	public BulidVipShopB2cData getBulidVipShopB2cData() {
		return this.bulidVipShopB2cData;
	}

	public void setBulidVipShopB2cData(BulidVipShopB2cData bulidVipShopB2cData) {
		this.bulidVipShopB2cData = bulidVipShopB2cData;
	}

	public BulidDangDangB2cData getBulidDangDangB2cData() {
		return this.bulidDangDangB2cData;
	}

	public void setBulidDangDangB2cData(BulidDangDangB2cData bulidDangDangB2cData) {
		this.bulidDangDangB2cData = bulidDangDangB2cData;
	}

	public BulidTmallB2cData getBulidTmallB2cData() {
		return this.bulidTmallB2cData;
	}

	public void setBulidTmallB2cData(BulidTmallB2cData bulidTmallB2cData) {
		this.bulidTmallB2cData = bulidTmallB2cData;
	}

	public BulidJuMeiB2cData getBulidJuMeiB2cData() {
		return this.bulidJuMeiB2cData;
	}

	public void setBulidJuMeiB2cData(BulidJuMeiB2cData bulidJuMeiB2cData) {
		this.bulidJuMeiB2cData = bulidJuMeiB2cData;
	}

	public BulidYiXunB2cData getBulidYiXunB2cData() {
		return this.bulidYiXunB2cData;
	}

	public void setBulidYiXunB2cData(BulidYiXunB2cData bulidYiXunB2cData) {
		this.bulidYiXunB2cData = bulidYiXunB2cData;
	}

	public BulidGomeB2cData getBulidGomeB2cData() {
		return this.bulidGomeB2cData;
	}

	public void setBulidGomeB2cData(BulidGomeB2cData bulidGomeB2cData) {
		this.bulidGomeB2cData = bulidGomeB2cData;
	}

	public BulidAmazonB2cData getBuildAmazonB2cData() {
		return this.bulidAmazonB2cData;
	}

	public void setBuildAmazonB2cData(BulidAmazonB2cData bulidAmazonB2cData) {
		this.bulidAmazonB2cData = bulidAmazonB2cData;
	}

	public BuildBenlaiB2cData getBenlaiB2cData() {
		return this.benlaiB2cData;
	}

	public void setBenlaiB2cData(BuildBenlaiB2cData benlaiB2cData) {
		this.benlaiB2cData = benlaiB2cData;
	}

	public BuildJiuxianWangB2cData getBuildJiuxianWangB2cData() {
		return this.buildJiuxianWangB2cData;
	}

	public void setBuildJiuxianWangB2cData(BuildJiuxianWangB2cData buildJiuxianWangB2cData) {
		this.buildJiuxianWangB2cData = buildJiuxianWangB2cData;
	}

	public BuildLeChongB2cData getBuildLeChongB2cData() {
		return this.buildLeChongB2cData;
	}

	public void setBuildLeChongB2cData(BuildLeChongB2cData buildLeChongB2cData) {
		this.buildLeChongB2cData = buildLeChongB2cData;
	}

}
