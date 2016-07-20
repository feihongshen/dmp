package cn.explink.b2c.tps;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.TpsCwbFlowDao;
import cn.explink.dao.TpsCwbMpsDao;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TpsCwbFlowVo;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import net.sf.json.JSONObject;

@Service
public class TpsCwbFlowService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TpsCwbFlowDao tpsCwbFlowDao;
	@Autowired
	private JointService jointService;
	@Autowired
	private BranchDAO branchDAO;
	
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private TransCwbDetailDAO transCwbDetailDAO;
	@Autowired
	private TpsCwbMpsDao tpsCwbMpsDao;
	@Autowired
	private CwbOrderService cwborderService;
	
	private static final String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	
	@Transactional
	public void save(CwbOrder co,String scancwb, FlowOrderTypeEnum flowordertype,long currentbranchid,String operateTime,boolean fromPage,BigDecimal weight,BigDecimal volume) {
		try {
			if (flowordertype.getValue() == FlowOrderTypeEnum.RuKu.getValue()||
				flowordertype.getValue() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()||
				flowordertype.getValue() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {//add by huangzh 2016-6-23 分站到货扫描也需要添加上
				int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.TPS_Cwb_Flow.getKey());//
				if(isOpenFlag!=1){
					this.logger.info("订单体积重量反馈tps开关未开启.");
					return;
				}
				
				TpsCwbFlowCfg cfg= getTpsCwbFlowCfg();
				if(cfg==null||cfg.getOpenFlag()!=1){
					this.logger.info("订单体积重量反馈tps开关未开启.");
					return;
				}
				
				if(co==null||scancwb==null||CwbOrderTypeIdEnum.Peisong.getValue()!=co.getCwbordertypeid()){
					return;
				}
				
				Branch currentbranch = this.branchDAO.getBranchById(currentbranchid);
				//add by huangzh 2016-6-23  站点的操作也需要加入到临时表
				if(currentbranch==null||(currentbranch.getSitetype()!=BranchEnum.KuFang.getValue()&&currentbranch.getSitetype()!=BranchEnum.ZhanDian.getValue())){
					return;
				}
				
				String transportNo=cwbDAO.getTpsTransportNoByCwb(co.getCwb());
				if(transportNo==null||transportNo.length()<1){
					this.logger.info("tps运单号为空时不保存,cwb="+co.getCwb());
					return;
				}
				
				if (flowordertype.getValue() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
					boolean scancwbExist= this.tpsCwbFlowDao.checkScancwbExist(co.getCwb(), scancwb);
					if(scancwbExist){
						this.logger.info("入库时保存过,cwb="+co.getCwb());
						return;
					}
				}
				
				//add by huangzh 2016-6-23 分站扫描到货也需要校验临时表是否有数据
				if (flowordertype.getValue() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
					boolean scancwbExist= this.tpsCwbFlowDao.checkScancwbExist(co.getCwb(), scancwb);
					if(scancwbExist){
						this.logger.info("分站到货扫描时保存过,cwb="+co.getCwb());
						return;
					}
				}
				
				int sendemaildate=0;//1为需要发送emaildate,0不需要
				if(co.getIsmpsflag()==IsmpsflagEnum.no.getValue()){
					boolean cwbExist= this.tpsCwbFlowDao.checkCwbExist(co.getCwb());//非集单时的第一件
					this.logger.info("非集单第一件="+(!cwbExist)+",cwb="+co.getCwb()+",sacncwb="+scancwb);
					if(!cwbExist){
						//非集单时用第一件入库时间作为出仓时间
						updateCwbEmaildate(co.getCwb(),operateTime);
						sendemaildate=1;
					}
				}else{
					//集单时用最后一件入库时间作为出仓时间
					co=cwbDAO.getCwbByCwbLock(co.getCwb());//查看集单到齐标志
					String emaildate=updateTransCwbEmaildate(co.getCwb(),scancwb,operateTime);//集单时才更新运单表
					tpsCwbMpsDao.save(co.getCwb(), scancwb);//保存已到的运单号到集单表
					boolean lastBox=isLastBox(co);//检查运单号是否全都在集单表
					this.logger.info("集单最后一件="+lastBox+",cwb="+co.getCwb()+",sacncwb="+scancwb);
					if(lastBox){
						//避免重复更新出仓时间
						boolean sendemaildateExist= this.tpsCwbFlowDao.checkSendemildateExist(co.getCwb());//集单时是否推过出仓时间
						if(!sendemaildateExist){
							updateCwbEmaildate(co.getCwb(),emaildate);
							sendemaildate=1;
						}
					}
				}

				//暂时不考虑外单;外单tps运单号目前放在oms数据库
				//Set<Long> customerids= getVipshopId(cfg.getCustomerids());
				//if(customerids!=null&&customerids.contains(co.getCustomerid())){
						TpsCwbFlowVo vo = new TpsCwbFlowVo();
						vo.setCwb(co.getCwb());
						vo.setFlowordertype(flowordertype.getValue());
						vo.setScancwb(scancwb);
						vo.setState(0);
						vo.setSendemaildate(sendemaildate);
						vo.setSendweight(1);//总是要回传重量体积到tps
						vo.setWeight(weight==null?new BigDecimal("0.01"):weight);
						vo.setVolume(volume==null?new BigDecimal("0.01"):volume);
						this.tpsCwbFlowDao.save(vo);

				//}
			}
		} catch (Exception e) {
			String tmpcwb=(co==null)?"":co.getCwb();
			logger.error("保存重量及出仓时间到临时表时出错,scancwb="+scancwb+",cwb="+tmpcwb,e);
			throw new CwbException(tmpcwb,flowordertype.getValue(),"保存重量及出仓时间到临时表时出错,"+e.getMessage());
		}  	
	}

	//集单时查看是否最后一箱
	private boolean isLastBox(CwbOrder co){	
		boolean last=true;
		if(co.getMpsallarrivedflag()==MPSAllArrivedFlagEnum.NO.getValue()){
			last=false;
		}else{
			List<String> transcwbList=cwborderService.getTranscwbList(co.getTranscwb());
			if(transcwbList==null||transcwbList.size()<1){
				last=false;
			}else{
				List<String> arrivedTranscwbList=tpsCwbMpsDao.findTranscwbList(co.getCwb());
				if(arrivedTranscwbList==null||arrivedTranscwbList.size()<transcwbList.size()){
					last=false;
				}else{
					Set<String> arrivedSet=new HashSet<String>();
					arrivedSet.addAll(arrivedTranscwbList);
					for(String tc:transcwbList){
						if(!arrivedSet.contains(tc)){
							last=false;
							break;
						}
					}
				}
			}
		}
		
		return last;
	}
	
	//更新订单表出仓时间
	private String updateCwbEmaildate(String cwb,String operateTime){
		String emailDate=handleEmaildate(operateTime);
		cwbDAO.updateCwbEmaildate(cwb, emailDate);
		return emailDate;
	}
	
	//更新运单表出仓时间
	private String updateTransCwbEmaildate(String cwb,String transcwb,String operateTime){
		String emailDate=handleEmaildate(operateTime);
		transCwbDetailDAO.updateEmaildate(cwb, transcwb, emailDate);
		return emailDate;
	}
	
	//参数为空则用当前时间
	private String handleEmaildate(String operateTime){
		String emailDate=operateTime==null?null:operateTime.trim();
		if(emailDate==null||emailDate.length()<1){
			Date now=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT);
			emailDate=sdf.format(now);
		}
		return emailDate;
	}
	
	@Transactional
	public List<TpsCwbFlowVo> retrieveData(int size,int trytime){
		return this.tpsCwbFlowDao.list(size, trytime);
	}

	@Transactional
	public void comleteWithError(TpsCwbFlowVo vo,String errorInfo){
		vo.setErrinfo(errorInfo);
		vo.setState(2);//0等待处理，1成功处理，2错误
		this.tpsCwbFlowDao.update(vo);
	}
	
	@Transactional
	public void complete(TpsCwbFlowVo vo){
		
		this.tpsCwbFlowDao.delete(vo);
		vo.setErrinfo("");
		vo.setState(1);//0等待处理，1成功处理，2错误
		vo.setTrytime(vo.getTrytime()+1);
		this.tpsCwbFlowDao.saveSent(vo);
		if(vo.getSendemaildate()==1){
			CwbOrder co=cwbDAO.getCwbByCwb(vo.getCwb());
			if(co!=null&&co.getIsmpsflag()==IsmpsflagEnum.yes.getValue()){
				this.tpsCwbMpsDao.delete(vo.getCwb());
			}
		}
	}
	
	@Transactional
	public void housekeep(int day){
		this.tpsCwbFlowDao.delete(day);
		this.tpsCwbFlowDao.deleteSent(day);
	}
	
	private Set<Long> getVipshopId(String customerids){
		if(customerids==null){
			return null;
		}
	
		Set<Long> set=new HashSet<Long>();
		String arr[]=customerids.split(",");
		if(arr!=null&&arr.length>0){
			for(String idStr:arr){
				if(idStr!=null){
					idStr=idStr.trim();
				}
				if(idStr!=null&&idStr.length()>0){
					Long id=Long.parseLong(idStr);
					set.add(id);
				}
			}
		}
		
		return set;
	}
	
	public TpsCwbFlowCfg getTpsCwbFlowCfg() {
		JointEntity jointEntity=jointService.getObjectMethod(B2cEnum.TPS_Cwb_Flow.getKey());
		TpsCwbFlowCfg cfg = null;
		if(jointEntity!=null){
			String objectMethod = jointEntity.getJoint_property();
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			cfg = (TpsCwbFlowCfg)JSONObject.toBean(jsonObj, TpsCwbFlowCfg.class);
		}
				
		return cfg;
	}

}
