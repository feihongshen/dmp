package cn.explink.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.dao.TpsTranscwbPrintDao;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.vo.TPStranscwb;

import com.pjbest.idallocation.bizservice.IdAllocationModel;
import com.pjbest.idallocation.bizservice.IdAllocationService;
import com.pjbest.idallocation.bizservice.IdAllocationServiceHelper;
import com.vip.osp.core.context.InvocationContext;

/**
 * TPS 运单打印
 * @author yurong.liang 2016-06-17
 */
@Service("tpsTranscwbPrintService")
public class TpsTranscwbPrintService {
	
	private final static Logger logger = LoggerFactory.getLogger(TpsTranscwbPrintService.class);
	@Autowired
	private TpsTranscwbPrintDao tpsTranscwbPrintDao;
	@Autowired
	private SecurityContextHolderStrategy securityContextHolderStrategy;
	
	/**
	 * 获得查询符合条件的列表
	 */
	public DataGridReturn getListToDataGridReturn(Integer printStatus,String tpstranscwb, int page, int rows){
		List<TPStranscwb> list = this.tpsTranscwbPrintDao.getList(printStatus,tpstranscwb,page,rows);
		int count= this.tpsTranscwbPrintDao.getCount(printStatus,tpstranscwb);
		DataGridReturn dg = new DataGridReturn();
		dg.setRows(list);
		dg.setTotal(count);
		return dg;
	}
	
	/**
	 * 获得查询符合条件的列表
	 */
	public List<TPStranscwb> getList(Integer printStatus,String tpstranscwb, int page, int rows){
		return this.tpsTranscwbPrintDao.getList(printStatus,tpstranscwb,page,rows);
	}
	
	/**
	 * 获取符合条件的记录数
	 */
	public int getCount(Integer printStatus,String tpstranscwb){
		return this.tpsTranscwbPrintDao.getCount(printStatus,tpstranscwb);
	}
	
	/**
	 * 获取tps的运单号保存到本地
	 */
	@Transactional
	public void getTranscwbFromTPS(int num){
		try {
			InvocationContext.Factory.getInstance().setTimeout(60000);
			IdAllocationService service = new IdAllocationServiceHelper.IdAllocationServiceClient();
			IdAllocationModel model = service.getDeliveryId("DMP", (byte) 9, ResourceBundleUtil.provinceCode, num);
			
			long startId=model.getStartId()==null?0:Long.parseLong(model.getStartId());
			long endId=model.getEndId()==null?0:Long.parseLong(model.getEndId());
			String createTime = DateTimeUtil.getNowTime();
			
			if(startId!=0 && endId!=0){
				for (long i = startId; i <= endId; i++) {
					this.tpsTranscwbPrintDao.createTPStranscwb(i+"",createTime);
				}
			}
		} catch (Exception e) {
			logger.error("获取tps的运单号保存到本地发生未知异常： "+e);
		}
	}
	
	/**
	 * 更新运单号打印状态
	 */
	@Transactional
	public void updatePrintStatus(List<String> tpstranscwbList){
		try {
				ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
				String printUser= userDetail.getUser().getRealname();
				String printTime = DateTimeUtil.getNowTime();
				for (String tpstranscwb : tpstranscwbList) {
					this.tpsTranscwbPrintDao.updatePrintStatus(tpstranscwb, printUser, printTime, 1);
				}
		} catch (Exception e) {
			logger.error("更新运单号打印状态发生未知异常： "+e);
		}
	}
}
