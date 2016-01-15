package cn.explink.b2c.auto.order.service;

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
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public long createAutoExceptionDetail(String cwb, String transportno, String errinfo, int status,long msgid,long refid) {
		return this.exceptionCwbDAO.createAutoExceptionDetail(cwb, transportno, errinfo, status,msgid,refid);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void fixException(String cwb,String transportno){
		this.exceptionCwbDAO.updateExceptionStatus(cwb, transportno, AutoExceptionStatusEnum.yixiufu.getValue());
	}
	
	@Transactional
	public long createAutoDispatchTimeoutException(AutoOrderStatusTmpVo vo,String errinfo){
		long msgid=this.exceptionCwbDAO.createAutoExceptionMsg(vo.getMsg(), AutoInterfaceEnum.fenjianzhuangtai.getValue());
		long detailid=this.exceptionCwbDAO.createAutoExceptionDetail(vo.getCwb(), "", errinfo, AutoExceptionStatusEnum.xinjian.getValue(),msgid,0);
		autoOrderStatusService.completedOrderStatusMsg(AutoCommonStatusEnum.fail.getValue(),vo.getCwb(),vo.getOperatetype());
		return detailid;
	}
}
