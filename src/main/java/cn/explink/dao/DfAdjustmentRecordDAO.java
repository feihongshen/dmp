package cn.explink.dao;

import cn.explink.domain.deliveryFee.DfAdjustmentRecord;
import cn.explink.service.DfFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

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

                ps.setTimestamp(31, new Timestamp(record.getEmaildate().getTime()));

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
}
