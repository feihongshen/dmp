package cn.explink.dao;

import cn.explink.domain.deliveryFee.DfBillFee;
import cn.explink.service.DfFeeService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.DateTimeUtil;
import com.vipshop.mercury.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class DfFeeDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String getTableName(int chargerType) {
        if (DfFeeService.DeliveryFeeChargerType.ORG.getValue() == chargerType) {
            return "fn_df_fee_org";
        } else if (DfFeeService.DeliveryFeeChargerType.STAFF.getValue() == chargerType) {
            return "fn_df_fee_staff";
        } else {
            throw new IllegalArgumentException("只接受参数为 DfChargerType.ORG 或者 DfChargerType.STAFF");
        }
    }


    private final class DfBillFeeRowMapper implements RowMapper<DfBillFee> {
        private int chargerType;

        @Override
        public DfBillFee mapRow(ResultSet rs, int rowNum) throws SQLException {
            DfBillFee dfBillFee = new DfBillFee();
            dfBillFee.setId(rs.getLong("id"));
            dfBillFee.setOrderNo(rs.getString("order_no"));
            dfBillFee.setTranscwb(rs.getString("transcwb"));
            dfBillFee.setCwbordertypeid(rs.getInt("cwbordertypeid"));
            dfBillFee.setCustomerid(rs.getLong("customerid"));
            dfBillFee.setSendcarnum(rs.getInt("sendcarnum"));
            dfBillFee.setSenderaddress(rs.getString("senderaddress"));
            dfBillFee.setConsigneeaddress(rs.getString("consigneeaddress"));
            dfBillFee.setRealweight(rs.getBigDecimal("realweight"));
            dfBillFee.setCargovolume(rs.getBigDecimal("cargovolume"));
            dfBillFee.setChargeType(rs.getInt("charge_type"));
            dfBillFee.setFeeAmount(rs.getBigDecimal("fee_amount"));
            dfBillFee.setAverPrice(rs.getBigDecimal("aver_price"));
            dfBillFee.setLevelAverprice(rs.getBigDecimal("level_averprice"));
            dfBillFee.setRangeAverprice(rs.getBigDecimal("range_averprice"));
            dfBillFee.setRangeAddprice(rs.getBigDecimal("range_addprice"));
            dfBillFee.setAddprice(rs.getBigDecimal("addprice"));
            dfBillFee.setOverareaSub(rs.getBigDecimal("overarea_sub"));
            dfBillFee.setMultiorderSub(rs.getBigDecimal("multiorder_sub"));
            dfBillFee.setHugeorderSub(rs.getBigDecimal("hugeorder_sub"));
            dfBillFee.setCodSub(rs.getBigDecimal("cod_sub"));
            dfBillFee.setOthersSub(rs.getBigDecimal("others_sub"));
            dfBillFee.setFinalSubsidy(rs.getBigDecimal("final_subsidy"));
            dfBillFee.setDeliverId(rs.getLong("deliver_id"));
            dfBillFee.setDeliverUsername(rs.getString("deliver_username"));
            dfBillFee.setDeliverybranchid(rs.getInt("deliverybranchid"));
            dfBillFee.setCwbstate(rs.getInt("cwbstate"));
            dfBillFee.setFlowordertype(rs.getInt("flowordertype"));
            dfBillFee.setCreateTime(rs.getTimestamp("create_time"));
            dfBillFee.setOutstationDate(rs.getTimestamp("outstationdatetime"));
            dfBillFee.setDeliverystate(rs.getInt("deliverystate"));
            dfBillFee.setEmaildate(rs.getTimestamp("emaildate"));
            dfBillFee.setCredate(rs.getTimestamp("credate"));
            dfBillFee.setPickTime(rs.getTimestamp("pick_time"));
            dfBillFee.setMobilepodtime(rs.getTimestamp("mobilepodtime"));
            dfBillFee.setAuditingtime(rs.getTimestamp("auditingtime"));
            dfBillFee.setIsCalculted(rs.getInt("is_calculted"));
            dfBillFee.setIsBilled(rs.getInt("is_billed"));
            dfBillFee.setAgtIds(rs.getString("agt_ids"));
            dfBillFee.setRuleIds(rs.getString("rule_ids"));
            dfBillFee.setBillNo(rs.getString("bill_no"));
            dfBillFee.setCwbprovince(rs.getString("cwbprovince"));
            dfBillFee.setCwbcity(rs.getString("cwbcity"));
            dfBillFee.setCwbcounty(rs.getString("cwbcounty"));
            dfBillFee.setPaybackfee(rs.getBigDecimal("paybackfee"));
            dfBillFee.setReceivablefee(rs.getBigDecimal("receivablefee"));
            dfBillFee.setCartype(rs.getString("cartype"));
            dfBillFee.setFeeCreateTime(rs.getTimestamp("fee_create_time"));
            dfBillFee.setFeeCreateUser(rs.getString("fee_create_user"));
            dfBillFee.setFeeUpdateTime(rs.getTimestamp("fee_update_time"));
            dfBillFee.setFeeUpdateUser(rs.getString("fee_update_user"));

            dfBillFee.setChargerType(chargerType);
            return dfBillFee;
        }
    }


    public long saveDeliveryFee(int chargerType, final String cwb, final String transcwb, final int cwbordertypeid, final long customerid, final long sendcarnum, final long backcarnum,
                                final String senderaddress, final String consigneeaddress, final BigDecimal realweight, final BigDecimal cargovolume, final int chargeType,
                                final long deliverId, final String userName, final long branchId, final long cwbstate, final long flowordertype, final Date create_time,
                                final Date outstationdatetime, final int deliverystate, final String emaildate, final Date credate, final Date pickTime, final Date mobilepodtime,
                                final String auditingtime, final int isCal, final int isBill, final String province, final String city, final String county, final BigDecimal paybackfee,
                                final BigDecimal receivablefee, final String createUserName, final String cartype) {

        final String sql = "INSERT INTO " + getTableName(chargerType) +
                "(order_no, transcwb, cwbordertypeid, customerid, sendcarnum, backcarnum, " +
                "senderaddress, consigneeaddress, realweight, cargovolume, charge_type, " +
                "deliver_id, deliver_username, deliverybranchid, cwbstate, flowordertype, " +
                "create_time, outstationdatetime, deliverystate, emaildate, credate, pick_time, mobilepodtime, " +
                "auditingtime, is_calculted, is_billed, cwbprovince, cwbcity, cwbcounty, paybackfee, " +
                "receivablefee, fee_create_time, fee_create_user, cartype)" +
                "VALUES (" +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,now(),?,?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date outstationdate = null;
                Date emaildateDate = null;
                Date auditingtimeDate = null;
//                try {
//                    outstationdate = sdf.parse(outstationdatetime);
//                } catch (ParseException e) {
//                }
                try {
                    emaildateDate = sdf.parse(emaildate);
                } catch (ParseException e) {
                }
                try {
                    auditingtimeDate = sdf.parse(auditingtime);
                } catch (ParseException e) {
                }

                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, cwb);
                ps.setString(2, transcwb);
                ps.setInt(3, cwbordertypeid);
                ps.setLong(4, customerid);
                ps.setLong(5, sendcarnum);
                ps.setLong(6, backcarnum);
                ps.setString(7, senderaddress);

                ps.setString(8, consigneeaddress);
                ps.setBigDecimal(9, realweight);
                ps.setBigDecimal(10, cargovolume);
                ps.setInt(11, chargeType);
                ps.setLong(12, deliverId);
                ps.setString(13, userName);
                ps.setLong(14, branchId);
                ps.setLong(15, cwbstate);
                ps.setLong(16, flowordertype);

                if (create_time == null) {
                    ps.setNull(17, Types.DATE);
                } else {
                    ps.setTimestamp(17, new Timestamp(create_time.getTime()));
                }

//                if (outstationdate == null) {
//                    ps.setNull(18, Types.DATE);
//                } else {
//                    ps.setTimestamp(18, new Timestamp(outstationdate.getTime()));
//                }

                if (outstationdatetime == null) {
                    ps.setNull(18, Types.DATE);
                } else {
                    ps.setTimestamp(18, new Timestamp(outstationdatetime.getTime()));                }

                ps.setInt(19, deliverystate);

                if (emaildateDate == null) {
                    ps.setNull(20, Types.DATE);
                } else {
                    ps.setTimestamp(20, new Timestamp(create_time.getTime()));
                }

                if (credate == null) {
                    ps.setNull(21, Types.DATE);
                } else {
                    ps.setTimestamp(21, new Timestamp(credate.getTime()));
                }

                if (pickTime == null) {
                    ps.setNull(22, Types.DATE);
                } else {
                    ps.setTimestamp(22, new Timestamp(pickTime.getTime()));
                }

                if (mobilepodtime == null) {
                    ps.setNull(23, Types.DATE);
                } else {
                    ps.setTimestamp(23, new Timestamp(mobilepodtime.getTime()));
                }

                if (auditingtimeDate == null) {
                    ps.setNull(24, Types.DATE);
                } else {
                    ps.setTimestamp(24, new Timestamp(auditingtimeDate.getTime()));
                }

                ps.setInt(25, isCal);
                ps.setInt(26, isBill);
                ps.setString(27, province);
                ps.setString(28, city);
                ps.setString(29, county);
                ps.setBigDecimal(30, paybackfee);
                ps.setBigDecimal(31, receivablefee);
                ps.setString(32, createUserName);
                ps.setString(33, cartype);

                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();

    }

    public DfBillFee findFeeByAdjustCondition(int chargerType, String cwb, Integer chargeType, Long branchOrUserId) {
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

        List<DfBillFee> fees = jdbcTemplate.query(sql.toString(), params, new DfBillFeeRowMapper());

        if (CollectionUtils.isNotEmpty(fees)) {
            return fees.get(0);
        } else
            return null;

    }

    public void deleteFeeById(int chargerType, long id) {
        StringBuilder sql = new StringBuilder("delete from ");
        sql.append(getTableName(chargerType));
        sql.append(" WHERE id=? ");
        jdbcTemplate.update(sql.toString(), id);
    }

}
