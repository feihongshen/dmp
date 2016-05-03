package cn.explink.b2c.tps;

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
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TpsCwbFlowVo;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
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
	
	@Transactional
	public void save(CwbOrder co,String scancwb, FlowOrderTypeEnum flowordertype,long currentbranchid) {
		if (flowordertype.getValue() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
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
			if(currentbranch==null||currentbranch.getSitetype()!=BranchEnum.KuFang.getValue()){
				return;
			}
			
			String transportNo=cwbDAO.getTpsTransportNoByCwb(co.getCwb());
			if(transportNo==null||transportNo.length()<1){
				this.logger.info("tps运单号为空时不保存,cwb="+co.getCwb());
				return;
			}

			//暂时不考虑外单;外单tps运单号目前放在oms数据库
			//Set<Long> customerids= getVipshopId(cfg.getCustomerids());
			//if(customerids!=null&&customerids.contains(co.getCustomerid())){
				TpsCwbFlowVo vo = new TpsCwbFlowVo();
				vo.setCwb(co.getCwb());
				vo.setFlowordertype(flowordertype.getValue());
				vo.setScancwb(scancwb);
				vo.setState(0);
				this.tpsCwbFlowDao.save(vo);
			//}
		}  	
	}

	@Transactional
	public List<TpsCwbFlowVo> retrieveData(int size,int trytime,int flowordertype){
		return this.tpsCwbFlowDao.list(size, trytime, flowordertype);
	}

	@Transactional
	public void update(TpsCwbFlowVo vo){
		this.tpsCwbFlowDao.update(vo);
	}
	
	@Transactional
	public void complete(TpsCwbFlowVo vo,List<String> transcwbList,int state){
		this.tpsCwbFlowDao.delete(vo);
		for(String transcwb:transcwbList){
			TpsCwbFlowVo flowVo=new TpsCwbFlowVo();
			flowVo.setCwb(vo.getCwb());
			flowVo.setScancwb(transcwb);
			flowVo.setFlowordertype(vo.getFlowordertype());
			flowVo.setState(state);
			this.tpsCwbFlowDao.save(flowVo);
		}
		
	}
	
	@Transactional
	public void housekeep(int day){
		this.tpsCwbFlowDao.delete(day);
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
