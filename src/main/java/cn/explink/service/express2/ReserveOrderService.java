package cn.explink.service.express2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pjbest.deliveryorder.bizservice.PjReserveOrderService;
import com.pjbest.deliveryorder.bizservice.PjReserveOrderServiceHelper;
import com.pjbest.deliveryorder.enumeration.OrderStatusEnum;
import com.pjbest.deliveryorder.service.OmReserveOrderModel;
import com.pjbest.deliveryorder.service.PjReserveOrderPageModel;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.CountyDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressInfoDetailVO;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.express2.VO.ReserveOrderLogVo;
import cn.explink.domain.express2.VO.ReserveOrderVo;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.express.ExpressCommonService;
import cn.explink.util.JsonUtil;

/**
 * 预约单Service
 * @date 2016年5月13日 下午6:11:56
 */
@Service
public class ReserveOrderService extends ExpressCommonService {
	
private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final int OSP_INVOKE_TIMEOUT = 60000;
	
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDao;
	@Autowired
    CountyDAO countyDAO;

    @Autowired
    CityDAO cityDAO;

    @Autowired
    ProvinceDAO provinceDAO;
	
	/**
	 * 获取预约单列表
	 * @date 2016年5月13日 下午6:13:22
	 * @return
	 */
	public List<ReserveOrderVo> getReserveOrderVoList() {
		OmReserveOrderModel omReserveOrderModel = null;//new OmReserveOrderModel();
		
		// 记录入库数据
		try {
			logger.info("omReserveOrderModel:{}", JsonUtil.translateToJson(omReserveOrderModel));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		InvocationContext.Factory.getInstance().setTimeout(OSP_INVOKE_TIMEOUT); 
		PjReserveOrderService pjReserveOrderService = new PjReserveOrderServiceHelper.PjReserveOrderServiceClient();
		PjReserveOrderPageModel pjReserveOrderPageModel = null;
		try {
			pjReserveOrderPageModel = pjReserveOrderService.getReserveOrders(omReserveOrderModel,1,10);
			// 返回数据记录
			logger.info("pjReserveOrderPageModel:{}", pjReserveOrderPageModel.getReserveOrders().size());
		} catch (OspException e) {
			logger.error(e.getMessage(), e);
		}
		List<OmReserveOrderModel> poList = pjReserveOrderPageModel.getReserveOrders();
		List<ReserveOrderVo> voList = new ArrayList<ReserveOrderVo>(poList.size());
		// po转vo
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(OmReserveOrderModel po : poList) {
			ReserveOrderVo vo = new ReserveOrderVo();
			vo.setOmReserveOrderId(po.getOmReserveOrderId());
			vo.setReserveOrderNo(po.getReserveOrderNo());
			Long appointTimeMs = po.getAppointTime();
			if(appointTimeMs != null) {
				Date appointTime = new Date(appointTimeMs);
				vo.setAppointTime(appointTime);
				vo.setAppointTimeStr(sdf.format(appointTime));
			}
			vo.setCnorName(po.getCnorName());
			vo.setCnorMobile(po.getCnorMobile());
			vo.setCnorTel(po.getCnorTel());
			vo.setCnorAddr(po.getCnorAddr());
			Long requireTimeMs = po.getRequireTime();
			if(requireTimeMs != null) {
				Date appointTime = new Date(requireTimeMs);
				vo.setRequireTime(appointTime);
				vo.setRequireTimeStr(sdf.format(appointTime));
			}
			Integer reservrOrderStatus = po.getReserveOrderStatus() == null ? null : po.getReserveOrderStatus().intValue();
			if(reservrOrderStatus != null) {
				vo.setReserveOrderStatus(po.getReserveOrderStatus());
				String reservrOrderStatusVal = null;
				for(OrderStatusEnum e : OrderStatusEnum.values()) {
					if(e.getIndex().intValue() == reservrOrderStatus.intValue()) {
						reservrOrderStatusVal = e.getName();
					}
				}
				vo.setReserveOrderStatusVal(reservrOrderStatusVal);
			}
			vo.setReason(po.getReason());
			vo.setTransportNo(po.getTransportNo());
			vo.setAcceptOrg(po.getAcceptOrg());
			vo.setAcceptOrgName(po.getAcceptOrgName());
			voList.add(vo);
		}
		return voList;
	}
	
	/**
	 * 通过城市获取区
	 * @param cityId
	 * @return
	 */
    public List<AdressVO> getRegionsByCity(Integer cityId) {
        return this.countyDAO.getCountyOfCity(cityId);
    }

    /**
     * 获取当前省的所有城市
     * @return
     */
    public List<AdressVO> getCities() {

        List<AdressVO> provincelist = this.provinceDAO.getProvince();
        Long provinceId = this.getProvinceId();
        if (provinceId == 0) {
            return this.cityDAO.getCityOfProvince(provincelist.size() > 0 ? provincelist.get(0).getCode() : null);
        } else {
            return this.cityDAO.getCityOfProvince(provinceId.intValue());
        }
    }
    
    /**
	 *
	 * @Title: getProvinceId
	 * @description 获取站点的省（先对比省的id，如果没对比上，在对比省的name，如果还没有则返回0）
	 * @author 刘武强
	 * @date  2015年8月17日下午8:13:35
	 * @param  @return
	 * @return  long
	 * @throws
	 */
	public long getProvinceId() {
		AdressInfoDetailVO adressInfoDetailVO = this.getAdressInfoByBranchid();
		List<AdressVO> Adresslist = this.provinceDAO.getAllProvince();
		//对比省的id
		Long provinceId = adressInfoDetailVO.getProvinceId();
		if (provinceId != null) {
			for (AdressVO temp : Adresslist) {
				if (provinceId == temp.getId()) {
					return provinceId;
				}
			}
		}
		//对比省的name
		String provinceName = adressInfoDetailVO.getProvinceName();
		for (AdressVO temp : Adresslist) {
			if ((provinceName != null) && provinceName.equals(temp.getName())) {
				return temp.getId();
			}
		}
		return 0;
	}

    /**
     * 获取所有站点
     * @return
     */
    public List<Branch> getBranches() {
        //数据库以省为单位，所以查找的是省的站点
       return branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
    }

    /**
     * 通过站点获取快递员
     * @param branchId
     * @return
     */
    public List<User>  getCourierByBranch(int branchId) {
        return userDao.getUserByRoleAndBranchid(2, branchId);
    }
	
	/**
	 * 查询预约单日志
	 * @param reserveOrderNo 预约单号
	 * @return 返回预约单日志list
	 */
	public List<ReserveOrderLogVo> queryReserveOrderLog(String reserveOrderNo) {
		
		InvocationContext.Factory.getInstance().setTimeout(OSP_INVOKE_TIMEOUT); 
		PjReserveOrderService pjReserveOrderService = new PjReserveOrderServiceHelper.PjReserveOrderServiceClient();
		
		
		return null;
	}
	
}
