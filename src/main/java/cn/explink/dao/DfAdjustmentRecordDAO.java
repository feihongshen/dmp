package cn.explink.dao;

import cn.explink.domain.deliveryFee.DfAdjustmentRecord;
import cn.explink.service.DfFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

@Component
public class DfAdjustmentRecordDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String getTableName(int chargerType) {
        if (DfFeeService.DeliveryFeeChargerType.ORG.getValue() == chargerType) {
            return "fn_df_fee_adjustment_org";
        } else if (DfFeeService.DeliveryFeeChargerType.STAFF.getValue() == chargerType) {
            return "fn_df_fee_adjustment_staff";
        } else {
            throw new IllegalArgumentException("只接受参数为 DfChargerType.ORG 或者 DfChargerType.STAFF");
        }
    }

    private final class DfAdjustmentRecordRowMapper implements RowMapper<DfAdjustmentRecord> {
        @Override
        public DfAdjustmentRecord mapRow(ResultSet rs, int rowNum)
                throws SQLException {
            DfAdjustmentRecord record = new DfAdjustmentRecord();
            record.setId(rs.getLong("id"));
            record.setOrderNo(rs.getString("order_no"));
            record.setTranscwb(rs.getString("transcwb"));
            record.setCwbordertypeid(rs.getInt("cwbordertypeid"));
            record.setCustomerid(rs.getLong("customerid"));
            record.setSendcarnum(rs.getInt("sendcarnum"));
            record.setBackcarnum(rs.getLong("backcarnum"));
            record.setSenderaddress(rs.getString("senderaddress"));
            record.setConsigneeaddress(rs.getString("consigneeaddress"));
            record.setRealweight(rs.getBigDecimal("realweight"));
            record.setCargovolume(rs.getBigDecimal("cargovolume"));
            record.setChargeType(rs.getInt("charge_type"));
            record.setAdjustAmount(rs.getBigDecimal("adjust_amount"));
            record.setAverPrice(rs.getBigDecimal("aver_price"));
            record.setLevelAverprice(rs.getBigDecimal("level_averprice"));
            record.setRangeAverprice(rs.getBigDecimal("range_averprice"));
            record.setRangeAddprice(rs.getBigDecimal("range_addprice"));
            record.setAddprice(rs.getBigDecimal("addprice"));
            record.setOverareaSub(rs.getBigDecimal("overarea_sub"));
            record.setMultiorderSub(rs.getBigDecimal("multiorder_sub"));
            record.setHugeorderSub(rs.getBigDecimal("hugeorder_sub"));
            record.setCodSub(rs.getBigDecimal("cod_sub"));
            record.setOthersSub(rs.getBigDecimal("others_sub"));
            record.setDeliverId(rs.getLong("deliver_id"));
            record.setDeliverUsername(rs.getString("deliver_username"));
            record.setDeliverybranchid(rs.getInt("deliverybranchid"));
            record.setCwbstate(rs.getInt("cwbstate"));
            record.setFlowordertype(rs.getInt("flowordertype"));
            record.setCreateTime(rs.getTimestamp("create_time"));
            record.setOutstationDate(rs.getTimestamp("outstationdatetime"));
            record.setDeliverystate(rs.getInt("deliverystate"));
            record.setEmaildate(rs.getTimestamp("emaildate"));
            record.setCredate(rs.getTimestamp("credate"));
            record.setMobilepodtime(rs.getTimestamp("mobilepodtime"));
            record.setAuditingtime(rs.getTimestamp("auditingtime"));
            record.setIsBilled(rs.getInt("is_billed"));
            record.setBillNo(rs.getString("bill_no"));
            record.setAgtIds(rs.getString("agt_ids"));
            record.setRuleIds(rs.getString("rule_ids"));
            record.setPaybackfee(rs.getBigDecimal("paybackfee"));
            record.setReceivablefee(rs.getBigDecimal("receivablefee"));
            record.setAdjustmentCreateTime(rs.getTimestamp("adjustment_create_time"));
            record.setAdjustmentCreateUser(rs.getString("adjustment_create_user"));
            record.setAdjustmentUpdateTime(rs.getTimestamp("adjustment_update_time"));
            record.setAdjustmentUpdateUser(rs.getString("adjustment_update_user"));
            record.setPickTime(rs.getTimestamp("pick_time"));
            record.setApplyuserid(rs.getLong("apply_userid"));
            record.setEdittime(rs.getString("edit_time"));

            return record;
        }

    }

    public long saveAdjustmentRecord(int chargeType, final DfAdjustmentRecord record, final String createUserName) {

        final StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(getTableName(chargeType));
        sql.append(" (");
        sql.append("order_no, ");
        sql.append("transcwb, ");
        sql.append("cwbordertypeid, ");
        sql.append("customerid, ");
        sql.append("sendcarnum, ");
        sql.append("backcarnum, ");
        sql.append("senderaddress, ");
        sql.append("consigneeaddress, ");
        sql.append("realweight, ");
        sql.append("cargovolume, ");
        sql.append("charge_type, ");
        sql.append("adjust_amount, ");
        sql.append("aver_price, ");
        sql.append("level_averprice, ");
        sql.append("range_averprice, ");
        sql.append("range_addprice, ");
        sql.append("addprice, ");
        sql.append("overarea_sub, ");
        sql.append("multiorder_sub, ");
        sql.append("hugeorder_sub, ");
        sql.append("cod_sub, ");
        sql.append("others_sub, ");
        sql.append("deliver_id, ");
        sql.append("deliver_username, ");
        sql.append("deliverybranchid, ");
        sql.append("cwbstate, ");
        sql.append("flowordertype, ");
        sql.append("create_time, ");
        sql.append("outstationdatetime, ");
        sql.append("deliverystate, ");
        sql.append("emaildate, ");
        sql.append("credate, ");
        sql.append("mobilepodtime, ");
        sql.append("auditingtime, ");
        sql.append("is_billed, ");
        sql.append("agt_ids, ");
        sql.append("bill_no, ");
        sql.append("paybackfee, ");
        sql.append("receivablefee, ");
        sql.append("adjustment_create_time, ");
        sql.append("adjustment_create_user, ");
        sql.append("pick_time, ");
        sql.append("apply_userid, ");
        sql.append("edit_time");
        sql.append(") VALUES (" +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,now(),?," +
                "?,?,?" +
                ")");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {

                PreparedStatement ps = connection.prepareStatement(sql.toString(), new String[]{"id"});
                ps.setString(1, record.getOrderNo());
                ps.setString(2, record.getTranscwb());
                ps.setInt(3, record.getCwbordertypeid());
                ps.setLong(4, record.getCustomerid());
                ps.setInt(5, record.getSendcarnum());
                ps.setLong(6, record.getBackcarnum());
                ps.setString(7, record.getSenderaddress());
                ps.setString(8, record.getConsigneeaddress());
                ps.setBigDecimal(9, record.getRealweight());
                ps.setBigDecimal(10, record.getCargovolume());
                ps.setInt(11, record.getChargeType());
                ps.setBigDecimal(12, record.getAdjustAmount());
                ps.setBigDecimal(13, record.getAverPrice());
                ps.setBigDecimal(14, record.getLevelAverprice());
                ps.setBigDecimal(15, record.getRangeAverprice());
                ps.setBigDecimal(16, record.getRangeAddprice());
                ps.setBigDecimal(17, record.getAddprice());
                ps.setBigDecimal(18, record.getOverareaSub());
                ps.setBigDecimal(19, record.getMultiorderSub());
                ps.setBigDecimal(20, record.getHugeorderSub());
                ps.setBigDecimal(21, record.getCodSub());
                ps.setBigDecimal(22, record.getOthersSub());
                ps.setLong(23, record.getDeliverId());
                ps.setString(24, record.getDeliverUsername());
                ps.setLong(25, record.getDeliverybranchid());
                ps.setLong(26, record.getCwbstate());
                ps.setLong(27, record.getFlowordertype());

                if (record.getCreateTime() == null) {
                    ps.setNull(28, Types.DATE);
                } else {
                    ps.setTimestamp(28, new Timestamp(record.getCreateTime().getTime()));
                }

                if (record.getOutstationDate() == null) {
                    ps.setNull(29, Types.DATE);
                } else {
                    ps.setTimestamp(29, new Timestamp(record.getOutstationDate().getTime()));
                }

                ps.setInt(30, record.getDeliverystate());

                if (record.getEmaildate() == null) {
                    ps.setNull(31, Types.DATE);
                }else {
                    ps.setTimestamp(31, new Timestamp(record.getEmaildate().getTime()));
                }

                if (record.getCredate() == null) {
                    ps.setNull(32, Types.DATE);
                } else {
                    ps.setTimestamp(32, new Timestamp(record.getCredate().getTime()));
                }

                if (record.getMobilepodtime() == null) {
                    ps.setNull(33, Types.DATE);
                } else {
                    ps.setTimestamp(33, new Timestamp(record.getMobilepodtime().getTime()));
                }

                if (record.getAuditingtime() == null) {
                    ps.setNull(34, Types.DATE);
                } else {
                    ps.setTimestamp(34, new Timestamp(record.getAuditingtime().getTime()));
                }
                //is_billed
                ps.setInt(35, 0);
                ps.setString(36, record.getAgtIds());
                //bill_no
                ps.setNull(37, Types.VARCHAR);
                ps.setBigDecimal(38, record.getPaybackfee());
                ps.setBigDecimal(39, record.getReceivablefee());
                ps.setString(40, createUserName);
                if (record.getPickTime() == null) {
                    ps.setNull(41, Types.DATE);
                } else {
                    ps.setTimestamp(41, new Timestamp(record.getPickTime().getTime()));
                }
                ps.setLong(42, record.getApplyuserid());
                ps.setString(43, record.getEdittime());

                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();

    }

    public List<DfAdjustmentRecord> findByAdjustCondition(int chargerType, String cwb, Integer chargeType, Long branchOrUserId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(getTableName(chargerType));
        sql.append(" WHERE 1=1");
        sql.append(" AND order_no = ?");
        sql.append(" AND charge_type = ?");

        if (chargerType == DfFeeService.DeliveryFeeChargerType.ORG.getValue()) {
            sql.append(" AND deliverybranchid = ?");
        } else {
            sql.append(" AND deliver_id = ?");
        }

        Object[] params = new Object[]{cwb, chargeType, branchOrUserId};

        List<DfAdjustmentRecord> fees = jdbcTemplate.query(sql.toString(), params, new DfAdjustmentRecordRowMapper());

        return fees;
    }

}
