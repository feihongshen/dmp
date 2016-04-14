package cn.explink.dao.interfac;

import cn.explink.domain.BranceReportAdjustPO;
public interface IBranceReportAdjustDao {
	
	/**
	 * 添加应付调整单明细记录
	 * @param adjustPO
	 */
	public void addBranceReportAdjust(BranceReportAdjustPO adjustPO) ;

}
