package cn.explink.dao.express;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cn.explink.domain.VO.express.ReSendExpressOrderVO;
import cn.explink.domain.express.ExpressTpsInterfaceExcepRecord;
import cn.explink.enumutil.YesOrNoStateEnum;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.enumutil.express.SendTpsExpressOrderResultEnum;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;

@Repository
public class ExpressTpsInterfaceExcepRecordDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class TpsInterfaceRowMap implements RowMapper<ReSendExpressOrderVO> {

		@Override
		public ReSendExpressOrderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReSendExpressOrderVO vo = new ReSendExpressOrderVO();
			vo.setId(rs.getLong("id"));
			vo.setTransNo(rs.getString("trans_no"));
			vo.setOpeFlag(rs.getInt("ope_flag"));
			vo.setErrMsg(rs.getString("err_msg"));
			vo.setCreateTime(rs.getString("create_time") != null ? rs.getString("create_time").substring(0, 19) : "");
			vo.setMethodParams(rs.getString("method_params"));
			return vo;
		}
	}

	/**
	 * 创建异常记录
	 * @param entity
	 * @return
	 */
	public Long createTpsInterfaceExcepRecord(final ExpressTpsInterfaceExcepRecord entity) {
		final StringBuffer sql = new StringBuffer();
		sql.append(" insert into express_ops_tps_interface_excep ");
		sql.append(" (pre_order_no,trans_no,package_no,");
		sql.append(" err_msg,operation_type,method_params,execute_count,");
		sql.append(" create_time,update_time,ope_flag,flow_order_type,remark");
		sql.append(" )");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?)");
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				int i = 0;

				ps.setString(++i, StringUtil.nullConvertToEmptyString(entity.getPreOrderNo()));//预订单号
				ps.setString(++i, StringUtil.nullConvertToEmptyString(entity.getTransNo()));//运单号
				ps.setString(++i, StringUtil.nullConvertToEmptyString(entity.getPackageNo()));//包号

				ps.setString(++i, StringUtil.nullConvertToEmptyString(entity.getErrMsg()));
				ps.setInt(++i, entity.getOperationType());
				ps.setString(++i, StringUtil.nullConvertToEmptyString(entity.getMethodParams()));
				ps.setInt(++i, entity.getExecuteCount());
				ps.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));
				ps.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));
				ps.setInt(++i, entity.getOpeFlag());

				ps.setInt(++i, entity.getFlowOrderType().intValue());
				ps.setString(++i, StringUtil.nullConvertToEmptyString(entity.getRemark()));
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 记录调用Tps接口异常信息
	 * @param recordId
	 * @param exceptionMsg
	 * @return
	 */
	public Long updateTpsInterfaceExcepRecord(Long recordId, String exceptionMsg) {
		int effectRecord = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_tps_interface_excep ");
		sql.append(" set err_msg=?,update_time=?,ope_flag=? ");
		sql.append(" where id=? ");
		effectRecord = ExpressTpsInterfaceExcepRecordDAO.this.jdbcTemplate.update(sql.toString(), exceptionMsg, new Timestamp(System.currentTimeMillis()), YesOrNoStateEnum.No.getValue(), recordId);
		return Long.valueOf(effectRecord);
	}

	/**
	 *
	 * @Title: updateTpsInterfaceExcepRecordSuccess
	 * @description 批量更新接口表中的记录
	 * @author 刘武强
	 * @date  2015年11月11日下午4:49:57
	 * @param  @param recordId
	 * @param  @return
	 * @return  Long
	 * @throws
	 */
	public void updateTpsInterfaceExcepRecordBatch(List<ReSendExpressOrderVO> list) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_tps_interface_excep set err_msg=?,update_time=?,ope_flag=? where id=? ");

		final String sqlUpdate = sql.toString();
		int circleTimes = (list.size() / Tools.DB_OPERATION_MAX) + 1;

		for (int i = 0; i < circleTimes; i++) {
			int startIndex = Tools.DB_OPERATION_MAX * i;
			int endIndex = Tools.DB_OPERATION_MAX * (i + 1);
			if (endIndex > list.size()) {
				endIndex = list.size();
			}
			if (endIndex <= 0) {
				break;
			}
			final List<ReSendExpressOrderVO> tempList = list.subList(startIndex, endIndex);
			if ((null != tempList) && !tempList.isEmpty()) {
				this.jdbcTemplate.batchUpdate(sqlUpdate, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						int j = 1;
						ReSendExpressOrderVO vo = tempList.get(i);
						ps.setString(j++, vo.getErrMsg());
						ps.setTimestamp(j++, new Timestamp(System.currentTimeMillis()));
						ps.setInt(j++, vo.getOpeFlag());
						ps.setLong(j++, vo.getId());
					}

					@Override
					public int getBatchSize() {
						return tempList.size();
					}
				});
			}
		}
	}

	/**
	 *
	 * @Title: getTpsInterfaceInfo
	 * @description TODO
	 * @author 刘武强
	 * @date  2015年11月10日下午5:50:33
	 * @param  @param beginTime
	 * @param  @param endTime
	 * @param  @param transNo
	 * @param  @param opeFlag
	 * @param  @return
	 * @return  List<ReSendExpressOrderVO>
	 * @throws
	 */
	public Map<String, Object> getTpsInterfaceInfo(String beginTime, String endTime, String transNo, String opeFlag, long page, int pageNumber) {
		List<ReSendExpressOrderVO> list = new ArrayList<ReSendExpressOrderVO>();
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		StringBuffer countsql = new StringBuffer();
		StringBuffer where = new StringBuffer();
		final List<Object> params = new ArrayList<Object>();
		int count = 0;

		sql.append("select * from express_ops_tps_interface_excep");
		countsql.append("select count(id) t from express_ops_tps_interface_excep");
		where.append(" ").append("where operation_type =" + ExpressOperationEnum.CreateTransNO.getValue());
		if (StringUtils.isNotBlank(beginTime)) {
			where.append(" ").append("and create_time>=? ");
			params.add(beginTime + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			where.append(" ").append("and create_time<=?");
			params.add(endTime + " 23:59:59");
		}
		if (StringUtils.isNotBlank(transNo)) {
			where.append(" ").append("and trans_no=?");
			params.add(transNo);
		}
		if (StringUtils.isNotBlank(opeFlag)) {
			where.append(" ").append("and ope_flag=?");
			params.add(opeFlag);
		} else {
			where.append(" ").append("and ope_flag=?");
			params.add(SendTpsExpressOrderResultEnum.Success.getValue());
		}
		where.append(" order by create_time");
		countsql.append(where);
		if (page > 0) {
			where.append(" limit " + ((page - 1) * pageNumber) + " ," + pageNumber);
		}
		sql.append(where);

		Object[] args = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			args[i] = params.get(i);
		}
		//查询页面数据
		list = this.jdbcTemplate.query(sql.toString(), args, new TpsInterfaceRowMap());
		//查询数据总量--前面参数已经绑定，所以不需要再次绑定
		count = this.jdbcTemplate.queryForInt(countsql.toString(), args);
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 *
	 * @Title: getTpsInterfaceInfoByids
	 * @description 根据id的集合，获取接口表中的数据
	 * @author 刘武强
	 * @date  2015年11月11日下午4:07:52
	 * @param  @param ids
	 * @param  @return
	 * @return  List<ReSendExpressOrderVO>
	 * @throws
	 */
	public List<ReSendExpressOrderVO> getTpsInterfaceInfoByids(String ids) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_tps_interface_excep where id in(" + ids + ")");
		//查询页面数据
		return this.jdbcTemplate.query(sql.toString(), new TpsInterfaceRowMap());
	}
}
