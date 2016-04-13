package cn.explink.pos.tools;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.util.DateTimeUtil;

@Component
public class PosPayDAO {
	private Logger logger = LoggerFactory.getLogger(PosPayDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CwbDAO cwbDAO;

	/**
	 * 记录POS的交易详情
	 */
	public boolean record_TradeForPosDetail(PosTradeDetail pd, int typeid) {
		boolean flag = false;
		String sql = "";
		int updateFlag = 0;
		double payAmount = Double.valueOf(pd.getPayAmount() + "");
		try {
			if ((typeid == 1 || payAmount == 0) && typeid != 4) {
				sql = "insert into express_ops_pos_paydetail (pos_code,cwb,tradeTime,tradeDeliverId,tradeTypeId," + "payTypeId,payAmount, payDetail,payRemark,signName,"
						+ " signTime,signRemark,signtypeid,isSuccessFlag,acq_type," + "customerid,isonlineFlag,emaildate,terminal_no) " + "VALUES(?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?) ";
				updateFlag = jdbcTemplate.update(sql, pd.getPos_code(), pd.getCwb(), pd.getTradeTime(), pd.getTradeDeliverId(), pd.getTradeTypeId(), pd.getPayTypeId(), pd.getPayAmount(),
						pd.getPayDetail(), pd.getPayRemark(), pd.getSignName(), pd.getSignTime(), pd.getSignRemark(), pd.getSigntypeid(), pd.getIsSuccessFlag(),
						pd.getAcq_type() != null && !"".equals(pd.getAcq_type()) ? pd.getAcq_type() : "single", pd.getCustomerid(), pd.getIsonlineFlag(), pd.getEmaildate(), pd.getTerminal_no());
			} else if (typeid == 2) {
				sql = "update express_ops_pos_paydetail set signName=?,signTime=?,signTypeId=?,signRemark=? where cwb=? ";
				updateFlag = jdbcTemplate.update(sql, pd.getSignName(), pd.getSignTime(), pd.getSigntypeid(), pd.getSignRemark(), pd.getCwb());
			} else if (typeid == 3) {
				sql = "update express_ops_pos_paydetail set acq_type='split',acq_type_flag=1,payRemark=CONCAT(IF(payRemark IS NULL,'',payRemark),'" + pd.getPayRemark() + "')"
						+ " where isSuccessFlag=? and  cwb=? ";
				updateFlag = jdbcTemplate.update(sql, pd.getIsSuccessFlag(), pd.getCwb());
			} else if (typeid == 4) { //签收
				sql = "delete from express_ops_pos_paydetail  where cwb=? ";
				jdbcTemplate.update(sql, pd.getCwb());
				sql = "insert into express_ops_pos_paydetail (pos_code,cwb,tradeTime,tradeDeliverId,tradeTypeId," + "payTypeId,payAmount, payDetail,payRemark,signName,"
						+ " signTime,signRemark,signtypeid,isSuccessFlag,acq_type," + "customerid,isonlineFlag,emaildate,terminal_no) " + "VALUES(?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?) ";
				updateFlag = jdbcTemplate.update(sql, pd.getPos_code(), pd.getCwb(), pd.getTradeTime(), pd.getTradeDeliverId(), pd.getTradeTypeId(), pd.getPayTypeId(), pd.getPayAmount(),
						pd.getPayDetail(), pd.getPayRemark(), "", "", pd.getSignRemark(), pd.getSigntypeid(), pd.getIsSuccessFlag(),
						pd.getAcq_type() != null && !"".equals(pd.getAcq_type()) ? pd.getAcq_type() : "single", pd.getCustomerid(), pd.getIsonlineFlag(), pd.getEmaildate(), pd.getTerminal_no());
			}
		} catch (DataAccessException e) {
			logger.error("", e);
		}
		if (updateFlag > 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 
	 * @param OrderNo
	 *            订单号
	 * @param podremark
	 *            pos备注
	 * @param receivedfee
	 *            交易金额
	 * @param deliverid
	 *            小件员
	 * @param payTypeId
	 *            支付类型
	 * @param trackinfo
	 *            pos详细信息
	 * @param signName
	 *            签收人
	 * @param signTypeId
	 *            签收类型
	 * @param signRemark
	 *            签收备注
	 * @param typeid
	 *            支付还是签收 1，支付 2，签收,4，撤销
	 * @param isSuccessFlag
	 *            交易还是撤销
	 * @param acq_type
	 *            收单模式
	 * @param posMethod
	 *            支付方
	 * @param isonlineFlag
	 *            pos刷卡还是电脑手工反馈 0，线上，1 手工反馈
	 * @param terminal_no
	 *            终端号
	 * @return
	 */
	public boolean save_PosTradeDetailRecord(String OrderNo, String podremark, double receivedfee, long deliverid, int payTypeId, // 支付类型
			String trackinfo, String signName, int signTypeId, String signRemark, int typeid, // 1支付，2签收
			int isSuccessFlag, // 标识1，交易、2撤销
			String acq_type, // 收单模式
			String posMethod, // 支付方
			int isonlineFlag, String terminal_no) // 0 线上；1手工反馈
	{
		try {

			PosTradeDetail pd = new PosTradeDetail();
			pd.setPos_code(posMethod);
			pd.setCwb(OrderNo);
			pd.setTradeTime(DateTimeUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
			pd.setTradeDeliverId(deliverid);
			pd.setTradeTypeId(isSuccessFlag == 2 ? 2 : 1);
			pd.setPayTypeId(payTypeId);
			pd.setPayAmount(BigDecimal.valueOf(receivedfee));
			pd.setPayDetail(trackinfo);
			pd.setPayRemark(podremark);
			pd.setSignName(signName);
			pd.setSignTime(DateTimeUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
			pd.setSigntypeid(signTypeId);
			pd.setSignRemark(signRemark);
			pd.setIsSuccessFlag(isSuccessFlag);
			pd.setAcq_type(acq_type);
			pd.setIsonlineFlag(isonlineFlag);
			pd.setTerminal_no(terminal_no);
			try {
				CwbOrder order = cwbDAO.getCwbByCwb(OrderNo);
				pd.setEmaildate(order.getEmaildate());
				pd.setCustomerid(order.getCustomerid());
			} catch (Exception e) {
				logger.error("POS支付记录异常! 获取订单供货商有误,订单:" + OrderNo + "", e);
			}
			return this.record_TradeForPosDetail(pd, typeid);
		} catch (Exception e) {
			logger.error("POS支付记录异常!", e);
			return false;
		}
	}

}
