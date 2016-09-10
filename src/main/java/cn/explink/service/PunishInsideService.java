package cn.explink.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.controller.AbnormalPunishView;
import cn.explink.controller.PenalizeInsideView;
import cn.explink.core.utils.StringUtils;
import cn.explink.dao.AbnormalOrderDAO;
import cn.explink.dao.AbnormalTypeDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.PunishInsideDao;
import cn.explink.dao.PunishInsideOperationinfoDao;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.WorkOrderDAO;
import cn.explink.domain.AbnormalOrder;
import cn.explink.domain.AbnormalType;
import cn.explink.domain.Branch;
import cn.explink.domain.CsComplaintAccept;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.PenalizeInside;
import cn.explink.domain.PenalizeInsideFilepath;
import cn.explink.domain.PenalizeInsideShenhe;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.PunishGongdanView;
import cn.explink.domain.PunishInsideOperationinfo;
import cn.explink.domain.PunishInsideReviseAndReply;
import cn.explink.domain.Reason;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.AbnormalOrderHandleEnum;
import cn.explink.enumutil.ComplaintResultEnum;
import cn.explink.enumutil.ComplaintStateEnum;
import cn.explink.enumutil.ComplaintTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PunishInsideStateEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StringUtil;


@Service
public class PunishInsideService {
	@Autowired
	WorkOrderDAO workOrderDAO;
	@Autowired
	AbnormalOrderDAO abnormalOrderDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	PenalizeTypeDAO penalizeTypeDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	BranchDAO branchDAO;
	@Autowired 
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	AbnormalTypeDAO abnormalTypeDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	PunishInsideDao punishInsideDao;
	@Autowired
	PunishInsideOperationinfoDao punishInsideOperationinfoDao;
	public static final String DUINEIKOUFASHIXIAO="punishinsideshixiao";
	private static final String PUNISHINSIDEZIDONGSHENHE="punishinsidezidongshenheshixiao";
	private static final String ZHAOHUI="找回";
	private static final String WEIZHAOHUI="未找回";
	private static final String WENTICHENGLI="问题成立";
	private static final String WENTIBUCHENGLI="问题不成立";
	private static final String DINGDAN="订单";
	private static final String GONGDAN="工单";
	private static final String WENTIJIAN="问题件";
	private static final String CHEXIAOKOUFA="撤销扣罚";
	private static final String JIANSHAOKOUFA="减少扣罚";
	private static final String KOUFACHENGLI="扣罚成立";
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
	//根据订单创建对内扣罚单组装要insert数据库中的数据
	public  PenalizeInside changePageValue(HttpServletRequest request){
		int punishinsidetype=PunishInsideStateEnum.daiqueren.getValue();
		String nowtime = this.getNowtime();
		//生成扣罚单号
		String punishNO="P"+System.currentTimeMillis()+"";
		PenalizeInside penalizeInside=new PenalizeInside();
		String cwb = StringUtil.nullConvertToEmptyString(request.getParameter("cwb")).trim();
		String cwbstate = StringUtil.nullConvertToEmptyString(request.getParameter("cwbstate")).trim();
		String cwbprice = StringUtil.nullConvertToEmptyString((request.getParameter("cwbprice")==""||request.getParameter("cwbprice")==null)?"0.00":request.getParameter("cwbprice")).trim();
		String cwpunishbigsort = StringUtil.nullConvertToEmptyString(request.getParameter("punishbigsort")).trim();
		String punishsmallsort = StringUtil.nullConvertToEmptyString(request.getParameter("punishsmallsort")).trim();
		String dutybranchid = StringUtil.nullConvertToEmptyString(request.getParameter("dutybranchid")).trim();
		String dutypersoname = StringUtil.nullConvertToEmptyString(request.getParameter("dutyname")).trim();
		String punishprice = StringUtil.nullConvertToEmptyString(request.getParameter("punishprice")==""?"0.00":request.getParameter("punishprice")).trim();
		String punishdescribe = StringUtil.nullConvertToEmptyString(request.getParameter("punishdescribe")).trim();
		String createBySource = StringUtil.nullConvertToEmptyString(request.getParameter("punishinsidetype")).trim();
		String cwbgoodprice = StringUtil.nullConvertToEmptyString(request.getParameter("cwbgoodprice")==""?"0.00":request.getParameter("cwbgoodprice")).trim();
		String cwbqitaprice = StringUtil.nullConvertToEmptyString(request.getParameter("cwbqitaprice")==""?"0.00":request.getParameter("cwbqitaprice")).trim();
		String goodpriceremark="";
		String qitapriceremark="";
		if(!isNum(cwbgoodprice)){
			goodpriceremark=cwbgoodprice;
			cwbgoodprice="0";
		}
		if(!isNum(cwbqitaprice)){
			qitapriceremark=cwbqitaprice;
			cwbqitaprice="0";
		}
		penalizeInside.setCreategoodpunishprice(new BigDecimal(cwbgoodprice));
		penalizeInside.setCreateqitapunishprice(new BigDecimal(cwbqitaprice));
		penalizeInside.setCreateBySource(Integer.valueOf(createBySource));//根据订单创建的来源标识为1
		long userid=this.getSessionUser().getUserid();
		penalizeInside.setCwb(cwb);
		penalizeInside.setCwbstate(Integer.valueOf(cwbstate));
		BigDecimal priceDecimal=new BigDecimal(cwbprice);
		penalizeInside.setCwbPrice(priceDecimal);
		penalizeInside.setPunishbigsort(Integer.valueOf(cwpunishbigsort));
		penalizeInside.setPunishsmallsort(Integer.valueOf("".equals(punishsmallsort)?"0":punishsmallsort));
		penalizeInside.setDutybranchid(Integer.valueOf(dutybranchid));
		penalizeInside.setDutypersonid(Integer.valueOf(dutypersoname));
		BigDecimal punishprice1=new BigDecimal(punishprice);
		penalizeInside.setPunishInsideprice(punishprice1);
		penalizeInside.setPunishdescribe(this.switchDescribe(punishdescribe));
		penalizeInside.setPunishNo(punishNO);
		penalizeInside.setCreateuserid(userid);
		penalizeInside.setCreDate(nowtime);
		penalizeInside.setPunishcwbstate(punishinsidetype);
		penalizeInside.setSourceNo(cwb);
		penalizeInside.setGoodpriceremark(goodpriceremark);
		penalizeInside.setQitapriceremark(qitapriceremark);
		return penalizeInside;
	}
	//根据问题件生成想要的insert对内扣罚单的数据
	public PenalizeInside switchTowantDataWithQuestion(HttpServletRequest request,String type1){
		String goodpriceremark="";
		String qitapriceremark="";
		//生成扣罚单号
		String punishNO="P"+System.currentTimeMillis()+"";
		int punishinsidetype=PunishInsideStateEnum.daiqueren.getValue();
		String punishbigsort1=StringUtil.nullConvertToEmptyString(request.getParameter("punishbigsort"+type1));
		String punishsmallsort1=StringUtil.nullConvertToEmptyString(request.getParameter("punishsmallsort"+type1));
		String dutybranchid1=StringUtil.nullConvertToEmptyString(request.getParameter("dutybranchid"+type1));
		String dutypersoname1=StringUtil.nullConvertToEmptyString(request.getParameter("dutypersoname"+type1)).trim();
		String punishprice1=StringUtil.nullConvertToEmptyString(request.getParameter("punishprice"+type1));
		String describe1=StringUtil.nullConvertToEmptyString(request.getParameter("describe"+type1));
		String availablecwb1=StringUtil.nullConvertToEmptyString(request.getParameter("availablecwb"+type1));
		String cwbhhh=StringUtil.nullConvertToEmptyString(request.getParameter("cwbhhh"+type1));
		String cwbgoodprice=StringUtil.nullConvertToEmptyString(request.getParameter("cwbgoodprice"+type1));
		String cwbqitaprice=StringUtil.nullConvertToEmptyString(request.getParameter("cwbqitaprice"+type1));
		if(!isNum(cwbgoodprice)){
			goodpriceremark=cwbgoodprice;
			cwbgoodprice="0";
		}
		if(!isNum(cwbqitaprice)){
			qitapriceremark=cwbqitaprice;
			cwbqitaprice="0";
		}
		
		
		long userid=this.getSessionUser().getUserid();
		PenalizeInside penalizeInside=new PenalizeInside();
		penalizeInside.setCreategoodpunishprice(new BigDecimal(cwbgoodprice==""?"0.00":cwbgoodprice));
		penalizeInside.setCreateqitapunishprice(new BigDecimal(cwbqitaprice==""?"0.00":cwbqitaprice));
		penalizeInside.setCreateBySource(Integer.parseInt(type1));
		penalizeInside.setCreateuserid(userid);
		penalizeInside.setCreDate(this.getNowtime());
		penalizeInside.setCwb(cwbhhh);
		penalizeInside.setCwbPrice(new BigDecimal(this.getCwbprice(cwbhhh)));
		penalizeInside.setCwbstate(this.cwbDAO.getCwbByCwb(cwbhhh).getFlowordertype());
		penalizeInside.setDutybranchid(Long.parseLong(dutybranchid1));
		penalizeInside.setDutypersonid(Long.parseLong(dutypersoname1==""?"0":dutypersoname1));
		penalizeInside.setPunishbigsort(Long.parseLong(punishbigsort1));
		penalizeInside.setPunishcwbstate(punishinsidetype);
		penalizeInside.setPunishdescribe(this.switchDescribe(describe1));
		penalizeInside.setPunishInsideprice(new BigDecimal(punishprice1==""?"0.00":punishprice1));
		penalizeInside.setPunishNo(punishNO);
		penalizeInside.setSourceNo(availablecwb1);
		penalizeInside.setPunishsmallsort(Integer.parseInt("".equals(punishsmallsort1)?"0":punishbigsort1));
		penalizeInside.setGoodpriceremark(goodpriceremark);
		penalizeInside.setQitapriceremark(qitapriceremark);
		return penalizeInside;
	}
		public String switchDescribe(String describe){
			String deString="";
			if(!describe.equals("最多100个字")){
				deString=describe;
			}
			return deString;
		}
		//转化为在创建为对内扣罚单的时候弹出框对问题件的查询
		public List<AbnormalPunishView> changeWantData(List<AbnormalOrder> abnormalOrders){
			List<AbnormalPunishView> abnormalPunishViews=new ArrayList<AbnormalPunishView>();
			if (abnormalOrders!=null&&abnormalOrders.size()>0) {
				for (AbnormalOrder abnormalOrder : abnormalOrders) {
					AbnormalPunishView abnormalPunishView=new AbnormalPunishView();
					abnormalPunishView.setAbnormaltypeid(abnormalOrder.getAbnormaltypeid());
					abnormalPunishView.setAbnormaltype(this.getAbnormalType(abnormalOrder.getAbnormaltypeid()));
					abnormalPunishView.setCretime(abnormalOrder.getCredatetime());
					abnormalPunishView.setCreuser(this.getCreUser(abnormalOrder.getCreuserid()));
					abnormalPunishView.setCustomername(this.getCustomename(abnormalOrder.getCustomerid()));
					abnormalPunishView.setCwb(abnormalOrder.getCwb());
					String cwbprice="0.00";
					CwbOrder cwbOrder=cwbDAO.getCwbByCwb(abnormalOrder.getCwb());
					if (cwbOrder!=null) {
						cwbprice=cwbOrder.getReceivablefee()+"";
					}
					abnormalPunishView.setCwbprice(cwbprice);
					abnormalPunishView.setCwbtype(this.getFlowOrdertype(abnormalOrder.getFlowordertype()));
					abnormalPunishView.setDealstate(this.getDealState(abnormalOrder.getIshandle()));
					abnormalPunishView.setDealstateid(abnormalOrder.getIshandle());
					abnormalPunishView.setQuestNo(abnormalOrder.getQuestionno());
					abnormalPunishView.setIsfind(this.getisfind(abnormalOrder.getIsfind()));
					abnormalPunishView.setDealresult(this.getdealresult(abnormalOrder.getDealresult()));
					abnormalPunishViews.add(abnormalPunishView);
				}
			}
			return abnormalPunishViews;
		}
		//弹出审核页面的时候需要用到的数据
		public PenalizeInsideView changedatatoshehe(PenalizeInside penalizeInside){
			PenalizeInsideView penalizeInsideView=new PenalizeInsideView();
			penalizeInsideView.setPunishno(penalizeInside.getPunishNo());
			penalizeInsideView.setSourceNo(penalizeInside.getSourceNo());
			penalizeInsideView.setCwb(penalizeInside.getCwb());
			penalizeInsideView.setCreatesourcename(this.getCreateSource(penalizeInside.getCreateBySource()));
			penalizeInsideView.setCreateFileposition(StringUtil.nullConvertToEmptyString(penalizeInside.getFileposition()));
			penalizeInsideView.setCreateusername(this.getCreUser(penalizeInside.getCreateuserid()));
			penalizeInsideView.setCreDate(penalizeInside.getCreDate());
			penalizeInsideView.setCwbprice(String.valueOf(penalizeInside.getCwbPrice()));
			penalizeInsideView.setDutybranchname(this.getBranchName(penalizeInside.getDutybranchid()));
			penalizeInsideView.setDutyperson(this.getCreUser(penalizeInside.getDutypersonid()));
			penalizeInsideView.setFlowordertypename(this.getFlowOrdertype(penalizeInside.getCwbstate()));
			penalizeInsideView.setPunishbigsort(this.getSortname(Integer.parseInt(penalizeInside.getPunishbigsort()+"")));//-----暂时没写????
			penalizeInsideView.setPunishcwbstate(this.getPunishState(penalizeInside.getPunishcwbstate()));//???扣罚操作状态
			penalizeInsideView.setPunishdescribe(penalizeInside.getPunishdescribe());
			penalizeInsideView.setPunishsmallsort(this.getSortname(Integer.parseInt(penalizeInside.getPunishsmallsort()+"")));
			penalizeInsideView.setShensudescribe(StringUtil.nullConvertToEmptyString(penalizeInside.getShensudescribe()));
			penalizeInsideView.setShensufileposition(StringUtil.nullConvertToEmptyString(penalizeInside.getShensufileposition()));
			penalizeInsideView.setShensutype(this.getShenSuType(penalizeInside.getShensutype()));
			penalizeInsideView.setShensuUsername(this.getCreUser(penalizeInside.getShensuuserid()));
			penalizeInsideView.setShenhedescribe(penalizeInside.getShenhedescribe());
			penalizeInsideView.setShenhefileposition(penalizeInside.getShenhefileposition());
			penalizeInsideView.setShenhepunishprice(this.checkkoufajine(penalizeInside.getPunishcwbstate(), String.valueOf(penalizeInside.getShenhepunishprice())));
			penalizeInsideView.setShenhetype(this.getShenheType(penalizeInside.getShenhetype()));
			penalizeInsideView.setShenheusername(this.getCreUser(penalizeInside.getShenheuserid()));
			penalizeInsideView.setShenhedate(penalizeInside.getShenhedate());
			penalizeInsideView.setShensudate(penalizeInside.getShensudate());
			penalizeInsideView.setLastqitapunishprice(String.valueOf(penalizeInside.getLastqitapunishprice()));
			penalizeInsideView.setLastgoodpunishprice(String.valueOf(penalizeInside.getLastgoodpunishprice()));
			// modify by bruce shangguan 20160903  当罚款金额不为0时，就显示相应的罚款金额，否则如果相应的罚款金额说明不为空，才显示该罚款说明
						BigDecimal createGoodPunishPrice = penalizeInside.getCreategoodpunishprice() ;
						BigDecimal createOtherPrice =  penalizeInside.getCreateqitapunishprice() ;
						BigDecimal punishInsidePrice = penalizeInside.getPunishInsideprice() ;
						String goodPriceRemark =  penalizeInside.getGoodpriceremark() ;
						String otherPriceRemark =  penalizeInside.getQitapriceremark() ;
						if(createGoodPunishPrice != null){
							penalizeInsideView.setCreategoodpunishprice(String.valueOf(createGoodPunishPrice));
						}else if(!StringUtils.isEmpty(goodPriceRemark) && !goodPriceRemark.trim().equalsIgnoreCase("null") ){
							penalizeInsideView.setCreategoodpunishprice(goodPriceRemark);
						}else{
							penalizeInsideView.setCreategoodpunishprice("");
						}
						if(createOtherPrice != null){
							penalizeInsideView.setCreateqitapunishprice(String.valueOf(createOtherPrice));
						}else if(!StringUtils.isEmpty(otherPriceRemark) && otherPriceRemark.trim().equalsIgnoreCase("null")){
							penalizeInsideView.setCreateqitapunishprice(otherPriceRemark);
						}else{
							penalizeInsideView.setCreateqitapunishprice("");
						}
						if(punishInsidePrice != null){
							penalizeInsideView.setPunishInsideprice(String.valueOf(punishInsidePrice));
						}else{
							penalizeInsideView.setPunishInsideprice("");
						}
						// end by bruce shangguan 20160903
			penalizeInsideView.setDutybrachid(penalizeInside.getDutybranchid());
			penalizeInsideView.setDutypersonid(penalizeInside.getDutypersonid());
			return penalizeInsideView;
		}
		//对内扣罚审核需要的数据
		public PenalizeInsideShenhe getpunInsideShenhe(HttpServletRequest request){
			PenalizeInsideShenhe penalizeInsideShenhe=new PenalizeInsideShenhe();
			//判断是扣罚成立的还是撤销扣罚的（1.扣罚成立。2.撤销扣罚）
			String shenheresult=StringUtil.nullConvertToEmptyString(request.getParameter("shenheresult"));
			String koufajine=StringUtil.nullConvertToEmptyString(request.getParameter("koufajine"));
			String describe=StringUtil.nullConvertToEmptyString(request.getParameter("describe"));
			String id=StringUtil.nullConvertToEmptyString(request.getParameter("id")).trim();
			String resultgoodprice=StringUtil.nullConvertToEmptyString(request.getParameter("resultgoodprice")).trim();
			String resultqitaprice=StringUtil.nullConvertToEmptyString(request.getParameter("resultqitaprice")).trim();
			penalizeInsideShenhe.setResultgoodprice(new BigDecimal(resultgoodprice==""?"0.00":resultgoodprice));
			penalizeInsideShenhe.setResultqitaprice(new BigDecimal(resultqitaprice==""?"0.00":resultqitaprice));
			if (shenheresult.equals("1")) {
				long koufachengli=PunishInsideStateEnum.koufachengli.getValue();
				penalizeInsideShenhe.setPunishcwbstate(koufachengli);
			}else if (shenheresult.equals("2")) {
				long koufachexiao=PunishInsideStateEnum.koufachexiao.getValue();
				penalizeInsideShenhe.setPunishcwbstate(koufachexiao);
			}
			penalizeInsideShenhe.setShenhedescribe(this.switchDescribe(describe));
			penalizeInsideShenhe.setShenhepunishprice(new BigDecimal(koufajine==""?"0.00":koufajine));
			penalizeInsideShenhe.setShenheresult(Long.parseLong(shenheresult));
			penalizeInsideShenhe.setId(Long.parseLong(id));
			return penalizeInsideShenhe;
		}
		//获得要导出的数据（对内扣罚）
		public List<PenalizeInsideView> setViews(List<PenalizeInside> penalizeInsides,String sumprice){
			try {
				List<PenalizeInsideView> penalizeInsideViews=new ArrayList<PenalizeInsideView>();
				for (PenalizeInside penalizeInside : penalizeInsides) {
					PenalizeInsideView penalizeInsideView=this.changedatatoshehe(penalizeInside);
					penalizeInsideViews.add(penalizeInsideView);
				}
				PenalizeInsideView penalizeInsideView=new PenalizeInsideView();
				penalizeInsideView.setCreatesourcename("扣罚总计");
				penalizeInsideView.setPunishInsideprice(sumprice);
				penalizeInsideViews.add(penalizeInsideView);
				return penalizeInsideViews;
			} catch (Exception e) {
				return new ArrayList<PenalizeInsideView>();
			}
			
		}
		//根据工单创建的弹出框的查询工单信息
		public List<PunishGongdanView> setViewsGongdan(List<CsComplaintAccept> csComplaintAccepts){
			try {
				List<PunishGongdanView> punishGongdanViews=new ArrayList<PunishGongdanView>();
				for (CsComplaintAccept csComplaintAccept : csComplaintAccepts) {
					PunishGongdanView punishGongdanView=new PunishGongdanView();
					punishGongdanView.setCuijianNum(String.valueOf(csComplaintAccept.getCuijianNum()));
					punishGongdanView.setCustomername(this.getCustomename(csComplaintAccept.getCustomerid()));
					punishGongdanView.setCwb(csComplaintAccept.getOrderNo());
					punishGongdanView.setGongdanNo(csComplaintAccept.getAcceptNo());
					punishGongdanView.setGongdanShouliName(this.getRealName(csComplaintAccept.getHandleUser()));
					punishGongdanView.setGongdanState(this.getGongdanState(csComplaintAccept.getComplaintState()));
					punishGongdanView.setGongdanType(this.getGongdanType(csComplaintAccept.getComplaintType()));
					punishGongdanView.setPhoneNumber(csComplaintAccept.getPhoneOne());
					punishGongdanView.setShoulitime(StringUtil.nullConvertToEmptyString(csComplaintAccept.getHandleTime()));
					punishGongdanView.setTousudealresult(this.getGongdanDealresult(csComplaintAccept.getComplaintResult()));
					punishGongdanView.setTousuonesort(this.getTousuOneSort(csComplaintAccept.getComplaintOneLevel()));
					punishGongdanView.setTousuOrganization(this.getBranchName(csComplaintAccept.getCodOrgId()));
					punishGongdanView.setToususecondsort(this.getTousuTwoSort(csComplaintAccept.getComplaintTwoLevel()));
					punishGongdanViews.add(punishGongdanView);
				}
				return punishGongdanViews;
			} catch (Exception e) {
				return new ArrayList<PunishGongdanView>();
			}
		}
		//在创建对内扣罚的时候判断该订单是否已经创建为问题件或者工单，是的话改变其扣罚字段
		public void changeFineField(PenalizeInside penalizeInside,int type){
			/*	if (type==1) {
					List<AbnormalOrder> abnormalOrders=abnormalOrderDAO.getAbnormalOrderByOCwb(penalizeInside.getCwb());
					 List<CsComplaintAccept> csComplaintAccepts=workOrderDAO.findGoOnacceptWOByCWB(penalizeInside.getCwb());
					if (abnormalOrders.size()>0) {
						this.changeWentijianIsfine(penalizeInside);
					}
					if (csComplaintAccepts!=null&&csComplaintAccepts.size()>0) {
					this.changeGongdanIsfine(penalizeInside);
					}
				}*/
			//工单创建
				if (type==2) {
					this.changeGongdanIsfine(penalizeInside);
				}
				//问题件创建
				if (type==3) {
					this.changeWentijianIsfine(penalizeInside);
				}
			
		}
		//改变问题件里面的是否扣罚关联的状态
		public void changeWentijianIsfine(PenalizeInside penalizeInside){
			abnormalOrderDAO.updateWentijianIsFine(penalizeInside.getSourceNo(),2);
		}
		//改变工单里面的是否扣罚关联的状态
		public void changeGongdanIsfine(PenalizeInside penalizeInside){
			this.workOrderDAO.updateIsfine(penalizeInside.getSourceNo(),2);
		}
		public String getRealName(String username){
			
			try {
				User user= userDAO.getUserByUsername(username);
				return user.getRealname();
			} catch (Exception e) {
				return "";
			}
			
		}
		public String getTousuTwoSort(long tousuonesort){
			String tousuonesorting="";
			List<Reason> lr=reasonDao.getAllTwoLevelReason();
			for (Reason reason : lr) {
				if (reason.getReasonid()==tousuonesort) {
					tousuonesorting=reason.getReasoncontent();
					break;
				}
			}
			return tousuonesorting;
		}
		public String getTousuOneSort(long tousuonesort){
			String tousuonesorting="";
			List<Reason> lr=reasonDao.add();
			for (Reason reason : lr) {
				if (reason.getReasonid()==tousuonesort) {
					tousuonesorting=reason.getReasoncontent();
					break;
				}
			}
			return tousuonesorting;
		}
		public String getGongdanDealresult(long tousudealresult){
			String tousudealString="";
			for (ComplaintResultEnum complaintResultEnum : ComplaintResultEnum.values()) {
				if (complaintResultEnum.getValue()==tousudealresult) {
					tousudealString=complaintResultEnum.getText();
					break;
					
				}
			}
			return tousudealString;
		}
		public String getGongdanType(long gongdantype){
			String typeString="";
			for (ComplaintTypeEnum complaintTypeEnum : ComplaintTypeEnum.values()) {
				if (complaintTypeEnum.getValue()==gongdantype) {
					typeString=complaintTypeEnum.getText();
				}
			}
			return typeString;
		}
		public String getGongdanState(long state){
			String stateString="";
			for (ComplaintStateEnum comEnum : ComplaintStateEnum.values()) {
				if (comEnum.getValue()==state) {
					stateString=comEnum.getText();
					break;
				}
			}
			return stateString;
		}
		public String getShenheType(long shenhetype){
			String shensutypeString="";
			if (shenhetype==1) {
				shensutypeString=PunishInsideService.KOUFACHENGLI;
			}else if(shenhetype==2){
				shensutypeString=PunishInsideService.CHEXIAOKOUFA;
			}
			return shensutypeString;
		}
		public String getShenSuType(long shensutype){
			String shensutypeString="";
			if (shensutype==1) {
				shensutypeString=PunishInsideService.CHEXIAOKOUFA;
			}else if(shensutype==2){
				shensutypeString=PunishInsideService.JIANSHAOKOUFA;
			}
			return shensutypeString;
		}
		public String getPunishState(long punishcwbstate){
			String state="";
			for (PunishInsideStateEnum punishinsidestate : PunishInsideStateEnum.values()) {
				if (punishinsidestate.getValue()==punishcwbstate) {
					state=punishinsidestate.getText();
					break;
				}
			}
			return state;
		}
		//对内扣罚单附件添加到指定路径下
		public  String loadexceptfile(MultipartFile file){
					String name="";
					try {
						if ((file != null) && !file.isEmpty()) {
							String filePath = ResourceBundleUtil.EXCEPTPATH;
							name=file.getOriginalFilename();
							if (name.indexOf(".")!=-1) {
								String suffix=name.substring(name.lastIndexOf("."));
								 name = DateTimeUtil.getNowTime().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "") + suffix;
							}else {
								 name = DateTimeUtil.getNowTime().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "")+"";
							}
							ServiceUtil.uploadWavFile(file, filePath, name);
						}
					} catch (Exception e) {
						this.logger.error("问题件添加到指定路径下出现错误");
					}
					return name;
				}
		//获得当前的创建的时间
		public String getNowtime(){
			//当前的创建的时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			return nowtime;
		}
		//获得问题件的类型
		public String getAbnormalType(long abnormaltypeid){
			String abnormaltypeString="";
			AbnormalType abnormalType=this.abnormalTypeDAO.getAbnormalTypeById(abnormaltypeid);
			if (abnormalType!=null) {
				abnormaltypeString=abnormalType.getName();
			}
			return abnormaltypeString;
		}
		public String getCreUser(long userid){
			String username="";
			User user=this.userDAO.getUserByidAdd(userid);
			if (user!=null) {
				username=user.getRealname();
			}
			return username;
		}
		public String getCustomename(long customerid){
			Customer customer=this.customerDAO.getCustomerById(customerid);
			return StringUtil.nullConvertToEmptyString(customer.getCustomername());
		}
		public String getFlowOrdertype(long flowordertype){
			String flowString="";
			for (FlowOrderTypeEnum flowOrderTypeEnum : FlowOrderTypeEnum.values()) {
				if (flowOrderTypeEnum.getValue()==flowordertype) {
					flowString=flowOrderTypeEnum.getText();
					break;
				}
			}
			return flowString;
		}
		public String getDealState(long ishandle){
			String state="";
			for (AbnormalOrderHandleEnum anorEnum : AbnormalOrderHandleEnum.values()) {
				if (anorEnum.getValue()==ishandle) {
					state=anorEnum.getText();
					break;
				}
			}
			return state;
		}
		public  String getisfind(long isfind){
			if (isfind==0) {
				return PunishInsideService.WEIZHAOHUI;
			}else {
				return PunishInsideService.ZHAOHUI;
			}
		}
		public String getdealresult(String dealresult){
			String dealresultString="";
			if (dealresult.equals("1")) {
				dealresultString=PunishInsideService.WENTICHENGLI;
			}else if (dealresult.equals("2")) {
				dealresultString=PunishInsideService.WENTIBUCHENGLI;
			}
			return dealresultString;
		}
		public String getCwbprice(String cwb){
			String cwbprice="0.00";
			CwbOrder cwbOrder=cwbDAO.getCwbByCwb(cwb);
			if (cwbOrder!=null) {
				cwbprice=cwbOrder.getReceivablefee()+"";
			}
			return cwbprice;
		}
		public String getCreateSource(long sourceid){
			if (sourceid==1) {
				return PunishInsideService.DINGDAN;
			}else if (sourceid==2) {
				return PunishInsideService.GONGDAN;
			}else {
				return PunishInsideService.WENTIJIAN;
			}
		}
		public String getBranchName(long branchid){
			String branchname="";
			Branch branch=this.branchDAO.getBranchByIdAdd(branchid);
			if (branch!=null) {
				branchname=branch.getBranchname();
			}
			return branchname;
		}
		public String getSortname(int id){
			String sortname="";
			try {
				PenalizeType penalizeType=penalizeTypeDAO.getPenalizeTypeById(id);
				if (penalizeType!=null) {
					sortname=penalizeType.getText();
				}
				return sortname;

			} catch (Exception e) {
				return "";
			}
		}
		public boolean switchhourTomill(long id){
			SystemInstall systeminstall=systemInstallDAO.getSystemInstallByName(PunishInsideService.DUINEIKOUFASHIXIAO);
			if (systeminstall!=null) {
				PenalizeInside penalizeInside=punishInsideDao.getInsidebyid(id);
				String credate=penalizeInside.getCreDate();
				long cretime=DateTimeUtil.StringToMill(credate);
				String shixiao=systeminstall.getValue();
				if (shixiao==null||shixiao.equals("")) {
					return true;
				}
				Double shixiaotime=Double.parseDouble(shixiao);
				if ((cretime+shixiaotime*1000*60*60)<System.currentTimeMillis()) {
					return false;
				}
			}
			return true;
		}
		public boolean checkisshenhe(long id){
			PenalizeInside penalizeInside=punishInsideDao.getInsidebyid(id);
			if (penalizeInside.getPunishcwbstate()==PunishInsideStateEnum.koufachengli.getValue()||penalizeInside.getPunishcwbstate()==PunishInsideStateEnum.koufachexiao.getValue()) {
				return true;
			}
		
			return false;
		}
		public boolean checkisshenheAdd(long id){
			PenalizeInside penalizeInside=punishInsideDao.getInsidebyid(id);
			if (penalizeInside.getPunishcwbstate()==PunishInsideStateEnum.koufachengli.getValue()||penalizeInside.getPunishcwbstate()==PunishInsideStateEnum.koufachexiao.getValue()) {
				return true;
			}
			
			return false;
		}
		public String checkkoufajine(long state,String price){
			String koufajine="";
			if (state==PunishInsideStateEnum.koufachengli.getValue()||state==PunishInsideStateEnum.koufachexiao.getValue()) {
				koufajine=price;
			}
			return koufajine;
		}
		/**
		 * 获得新的filepath
		 * @param filepathMaps
		 * @param id
		 * @param newFilepath
		 * @return
		 */
		public String getNewFilePath(Map<Long,String> filepathMaps,String id,String newFilepath){
			String lastfilePath="";
			String oldFilePath=filepathMaps.get(Long.parseLong(id));
			if(!StringUtils.isEmpty(oldFilePath)){
				lastfilePath=newFilepath+","+oldFilePath;
				return lastfilePath;
			}else{
				return newFilepath;
			}
		}
		/**
		 * 获得从ids查询到的filepath的Map
		 * @param ids
		 * @return
		 */
		public Map<Long, String> getFilePathMapByIds(String ids){
			List<PenalizeInsideFilepath> filePaths=punishInsideDao.findPenalizeById(ids);
			Map<Long, String> filepathMap=new HashMap<Long, String>();
			for (PenalizeInsideFilepath penalizeInsideFilepath : filePaths) {
				filepathMap.put(penalizeInsideFilepath.getId(), penalizeInsideFilepath.getFilepath());
			}
			return filepathMap;
		}
		/**
		 * 当前记录的是申诉操作的记录，后续可以添加其它操作
		 * @param id
		 * @param shensutype
		 * @param describe
		 * @param userid
		 * @param punishinsidestate
		 */
		public void inserIntoOpertionInfo(long id,int shensutype,String describe,long userid,long punishinsidestate,String shensuTime){
			PunishInsideOperationinfo punishInsideOperationinfo=new PunishInsideOperationinfo();
			punishInsideOperationinfo.setDetailid(id);
			punishInsideOperationinfo.setOperationdescribe(describe);
			punishInsideOperationinfo.setOperationtype(punishinsidestate);
			punishInsideOperationinfo.setOperationuserid(userid);
			punishInsideOperationinfo.setShensutype(shensutype);
			punishInsideOperationinfo.setShensudate(shensuTime);
			punishInsideOperationinfoDao.insertIntoOperationInfo(punishInsideOperationinfo);
		}
		/**
		 * 通过对内扣罚单的主键来查询申诉的操作
		 * @param id
		 * @return
		 */
		public List<PunishInsideOperationinfo> getOperationRecord(long id){
			List<PunishInsideOperationinfo> punishInsideOperationinfos=punishInsideOperationinfoDao.findByDetailId(id);
			Map<Integer, String> punishInsideStateEnumMap=new HashMap<Integer, String>();
			for (PunishInsideStateEnum punishInsideStateEnum: PunishInsideStateEnum.values()) {
				if(1==punishInsideStateEnum.getValue()){
					punishInsideStateEnumMap.put(punishInsideStateEnum.getValue(), "创建");
					continue;
				}else if(2==punishInsideStateEnum.getValue()){
					punishInsideStateEnumMap.put(punishInsideStateEnum.getValue(), "申诉");
					continue;
				}
				punishInsideStateEnumMap.put(punishInsideStateEnum.getValue(), punishInsideStateEnum.getText());

			}
			Map<Long, String> userMap=this.getUsersMap();
			for (PunishInsideOperationinfo punishInsideOperationinfo : punishInsideOperationinfos) {
				punishInsideOperationinfo.setOperationusername(userMap.get(punishInsideOperationinfo.getOperationuserid()));
				punishInsideOperationinfo.setOperationtypename(punishInsideStateEnumMap.get((int)punishInsideOperationinfo.getOperationtype()));
				punishInsideOperationinfo.setShensutypeName(this.getShenSuType(punishInsideOperationinfo.getShensutype()));
			}
			return punishInsideOperationinfos;
		}
		/**
		 * 获得user的Map集合
		 * @return
		 */
		public Map<Long,String> getUsersMap(){
			List<User> users=this.userDAO.getAllUser();
			Map<Long, String> userMap=new HashMap<Long, String>();
			for (User user : users) {
				userMap.put(user.getUserid(), user.getRealname());
			}
			return userMap;
		}
		/**
		 * 定时器修改满足期限条件的对内扣罚单进行自动审核为扣罚成立
		 */
		@Transactional
		public void automaticShenheChengli(){
			List<PenalizeInside> penalizeInsides=punishInsideDao.getWeiShenheData();
			if (penalizeInsides==null) {
				this.logger.info("数据库中不存在非审核订单，不需要进行对内扣罚自动审核");
			}else{
				List<PenalizeInside> insides=this.isOrnotAutoShenhe(penalizeInsides);
				if (insides.size()==0) {
					this.logger.error("对内扣罚暂时没有要自动修改为扣罚成立的订单");
				}else{
					punishInsideDao.autoShenheWeiKouFaChengLi(insides);
				}
				
			}
			
		}
		/**
		 * 获得自动审核的对内扣罚单进行自动扣罚成立
		 * @param penalizeInsides
		 * @return
		 */
		public List<PenalizeInside> isOrnotAutoShenhe(List<PenalizeInside> penalizeInsides){
			List<PenalizeInside> needChangePenalize=new ArrayList<PenalizeInside>();
			for (PenalizeInside penalizeInside : penalizeInsides) {
				boolean flag=this.cheackisOrnotAutoShenhe(penalizeInside.getCreDate());
				if(flag){
					needChangePenalize.add(penalizeInside);
				}
				
			}
			return needChangePenalize;
			
		}
		/**
		 * 判断对内扣罚订单是否超过设定的时限
		 * @param createDate
		 * @return
		 */
		public boolean  cheackisOrnotAutoShenhe(String createDate){
			SystemInstall systeminstall=systemInstallDAO.getSystemInstallByName(PunishInsideService.PUNISHINSIDEZIDONGSHENHE);
			long cretime=DateTimeUtil.StringToMill(createDate);
			String shixiao="72";
			if (systeminstall!=null&&!"".equals(systeminstall.getValue())) {
				shixiao=systeminstall.getValue();
			}
			Double shixiaotime=Double.parseDouble(shixiao);
			if ((cretime+shixiaotime*1000*60*60)<System.currentTimeMillis()) {
				return true;
			}
			return false;
		}
		/**
		 * 修改金额与站点或责任人与回复
		 * @param punishInsideReviseAndReply
		 */
		@Transactional
		public void reviseAndReply(PunishInsideReviseAndReply punishInsideReviseAndReply){
			User user=this.getSessionUser();
			if (!isNum(punishInsideReviseAndReply.getRevisegoodprice())) {
				punishInsideReviseAndReply.setRevisegoodpriceNew(BigDecimal.ZERO);
			}else {
				punishInsideReviseAndReply.setRevisegoodpriceNew(new BigDecimal("".equals(punishInsideReviseAndReply.getRevisegoodprice())?"0.00":punishInsideReviseAndReply.getRevisegoodprice()));
				punishInsideReviseAndReply.setRevisegoodprice("");
			}
			if (!isNum(punishInsideReviseAndReply.getReviseqitaprice())) {
				punishInsideReviseAndReply.setReviseqitapriceNew(BigDecimal.ZERO);
			}else{
				punishInsideReviseAndReply.setReviseqitapriceNew(new BigDecimal("".equals(punishInsideReviseAndReply.getReviseqitaprice())?"0.00":punishInsideReviseAndReply.getReviseqitaprice()));
				punishInsideReviseAndReply.setReviseqitaprice("");
			}
			if("".equals(punishInsideReviseAndReply.getKoufajine())){
				punishInsideReviseAndReply.setKoufajineNew(BigDecimal.ZERO);;
			}else{
				punishInsideReviseAndReply.setKoufajineNew(new BigDecimal(punishInsideReviseAndReply.getKoufajine()));
			}
			punishInsideDao.updatekoufaPriceAndDutyInfo(punishInsideReviseAndReply);
			punishInsideOperationinfoDao.insertIntoOPerationwithRevise(punishInsideReviseAndReply,user);
		}
		
		/**
		 * 判断是不是客服角色，客服角色在系统设置中的ServiceID设置了岗位角色哪一个可以作为客服的角色权限
		 * @param roleid
		 */
		public long  checkIsKeFu(long roleid){
			long realroleid=1;
			SystemInstall systemInstall=systemInstallDAO.getSystemInstallByName("ServiceID");
			if(systemInstall!=null&&(systemInstall.getValue()+",").indexOf(roleid+",")>=0){
				return realroleid;
			}else {
				return roleid;
			}
		}
		
		public static boolean isNum(String str){
			return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
		}
}
