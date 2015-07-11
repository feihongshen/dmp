package cn.explink.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.controller.AbnormalPunishView;
import cn.explink.controller.PenalizeInsideView;
import cn.explink.dao.AbnormalOrderDAO;
import cn.explink.dao.AbnormalTypeDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.PunishInsideDao;
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
import cn.explink.domain.PenalizeInsideShenhe;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.PunishGongdanView;
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
	public static final String DUINEIKOUFASHIXIAO="punishinsideshixiao";
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
		penalizeInside.setCreategoodpunishprice(new BigDecimal(cwbgoodprice));
		penalizeInside.setCreateqitapunishprice(new BigDecimal(cwbqitaprice));
		penalizeInside.setCreateBySource(Integer.valueOf(createBySource));//根据订单创建的来源标识为1
		long userid=this.getSessionUser().getUserid();
		penalizeInside.setCwb(cwb);
		penalizeInside.setCwbstate(Integer.valueOf(cwbstate));
		BigDecimal priceDecimal=new BigDecimal(cwbprice);
		penalizeInside.setCwbPrice(priceDecimal);
		penalizeInside.setPunishbigsort(Integer.valueOf(cwpunishbigsort));
		penalizeInside.setPunishsmallsort(Integer.valueOf(punishsmallsort));
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
		return penalizeInside;
	}
	//根据问题件生成想要的insert对内扣罚单的数据
	public PenalizeInside switchTowantDataWithQuestion(HttpServletRequest request,String type1){
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
		penalizeInside.setPunishsmallsort(Integer.parseInt(punishsmallsort1));
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
			penalizeInsideView.setPunishInsideprice(String.valueOf(penalizeInside.getPunishInsideprice()));
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
								 name = System.currentTimeMillis() + suffix;
							}else {
								 name = System.currentTimeMillis()+"";
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
			if (penalizeInside.getPunishcwbstate()==PunishInsideStateEnum.koufachengli.getValue()||penalizeInside.getPunishcwbstate()==PunishInsideStateEnum.koufachexiao.getValue()||penalizeInside.getPunishcwbstate()==PunishInsideStateEnum.daishenhe.getValue()) {
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
}
