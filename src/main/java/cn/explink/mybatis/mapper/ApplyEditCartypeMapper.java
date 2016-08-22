package cn.explink.mybatis.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.explink.mybatis.core.BaseMapper;
import cn.explink.mybatis.domain.ApplyEditCartypeVO;

public interface ApplyEditCartypeMapper extends BaseMapper<ApplyEditCartypeVO> {
	
	/**
	 * 按条件查询
	 * @param cwb 单个订单号  
	 * @param cwbs  多个订单号，以逗号分隔 
	 * @param applyUserIds  多个提交者ID，以逗号分隔 
	 * @param branchId 机构ID
	 * @param reviewStatus  审核状态 
	 * @param isReview  是否已审核 
	 * @param startApplyTime  申请时间（起） 
	 * @param endApplyTime  申请时间（止） 
	 * @return
	 */
	public List<ApplyEditCartypeVO> queryApplyEditCartype(@Param("cwb") String cwb,
														  @Param("cwbs") String cwbs,
														  @Param("applyUserId") String applyUserId,
														  @Param("branchId") String branchId,
														  @Param("reviewStatus") Integer reviewStatus,
														  @Param("isReview") Boolean isReview,
														  @Param("startApplyTime") String startApplyTime,
														  @Param("endApplyTime") String endApplyTime
														  );
	
	/*
	public List<ApplyEditCartypeVO> queryApplyEditCartypeByPage(String cwbs,Long branchid,String userids,
			Boolean isReview,Integer reviewStatus,
			String startApplyTime,String endApplyTimel);*/

}
