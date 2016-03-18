package cn.explink.b2c.auto.order.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.enumutil.AutoCommonStatusEnum;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.enumutil.AutoInterfaceEnum;


@Service
public class AutoExceptionService {
	@Autowired
	private ExceptionCwbDAO exceptionCwbDAO;
	
	@Autowired
	private AutoOrderStatusService autoOrderStatusService;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public long createAutoExceptionMsg(String msg,int interfacetype) {
		long msgid=this.exceptionCwbDAO.createAutoExceptionMsg(msg, interfacetype);
		return msgid;
	}
	
	//兼容没传operattype的情况
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public long createAutoExceptionDetail(String cwb, String transportno, String errinfo, int status,long msgid,long refid) {
		return this.exceptionCwbDAO.createAutoExceptionDetail(cwb, transportno, errinfo, status,msgid,refid);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public long createAutoExceptionDetail(String cwb, String transportno, String errinfo, int status,long msgid,long refid,String operatetype) {
		return this.exceptionCwbDAO.createAutoExceptionDetail(cwb, transportno, errinfo, status,msgid,refid,operatetype);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public long updateAutoExceptionDetail(long id,int status,String errorInfo,long msgid,String msg) {
		 return this.exceptionCwbDAO.updateAutoExceptionDetail(id, status, errorInfo,msgid,msg);
	}
	
	public List<Map<String,Object>> queryAutoExceptionDetail(String cwb,String transportno,String operateType){
		return this.exceptionCwbDAO.queryAutoExceptionDetail(cwb, transportno, operateType);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void fixException(String cwb,String transportno){
		this.exceptionCwbDAO.updateExceptionStatus(cwb, transportno, AutoExceptionStatusEnum.yixiufu.getValue());
	}
	
	@Transactional
	public long createAutoDispatchTimeoutException(AutoOrderStatusTmpVo vo,String errinfo){
		long msgid=this.exceptionCwbDAO.createAutoExceptionMsg(vo.getMsg(), AutoInterfaceEnum.fenjianzhuangtai.getValue());
		long detailid=this.exceptionCwbDAO.createAutoExceptionDetail(vo.getCwb(), "", errinfo, AutoExceptionStatusEnum.xinjian.getValue(),msgid,0,vo.getOperatetype());
		autoOrderStatusService.completedOrderStatusMsg(AutoCommonStatusEnum.fail.getValue(),vo.getCwb(),vo.getOperatetype(),vo.getTransportno());
		return detailid;
	}
}
