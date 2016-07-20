package cn.explink.service;

import cn.explink.core.utils.StringUtils;
import cn.explink.dao.ApplyEditDeliverystateDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.DfAdjustmentRecordDAO;
import cn.explink.dao.DfFeeDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.CountyDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.ApplyEditDeliverystate;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.deliveryFee.DfAdjustmentRecord;
import cn.explink.domain.deliveryFee.DfBillFee;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DfFeeService {

    @Autowired
    private DeliveryStateDAO deliveryStateDAO;
    @Autowired
    private CwbDAO cwbDAO;
    @Autowired
    private DfFeeDAO dfFeeDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private OrderFlowDAO orderFlowDAO;
    @Autowired
    private ProvinceDAO provinceDAO;
    @Autowired
    private CityDAO cityDAO;
    @Autowired
    private CountyDAO countyDAO;
    @Autowired
    private BranchDAO branchDAO;
    @Autowired
    DfAdjustmentRecordDAO dfAdjustmentRecordDAO;
    @Autowired
    ApplyEditDeliverystateDAO applyEditDeliverystateDAO;

    public enum DeliveryFeeRuleChargeType {
        GET(0, "揽件费"),
        SEND(1, "派件费");

        private int value;

        private String text;

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }

        DeliveryFeeRuleChargeType(int value, String text) {
            this.value = value;
            this.text = text;
        }

    }

    public enum DeliveryFeeChargerType {
        ORG(0, "加盟站点"),
        STAFF(1, "小件员");

        private int value;

        private String text;

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }

        DeliveryFeeChargerType(int value, String text) {
            this.value = value;
            this.text = text;
        }

    }

    private final static Logger logger = LoggerFactory.getLogger(DfFeeService.class);

    public void saveFeeRelativeAfterOXOImport(String cwb, User currentUser) {
        logger.info("OXO/OXOJIT 导入后插入派费相关表操作开始");

        List<AdressVO> allProvince = provinceDAO.getAllProvince();
        List<AdressVO> allCity = cityDAO.getAllCityWithParentCode();
        List<AdressVO> allCounty = countyDAO.getAllCounty();

        saveFeeRelative(cwb, currentUser, allProvince, allCity, allCounty);

        logger.info("OXO/OXOJIT 导入后插入派费相关表操作结束");
    }

    public void saveFeeRelativeAfterAuditConfirmed(String cwbsStr, User currentUser) {
        logger.info("归班审核后插入派费相关表操作开始");

        List<AdressVO> allProvince = provinceDAO.getAllProvince();
        List<AdressVO> allCity = cityDAO.getAllCityWithParentCode();
        List<AdressVO> allCounty = countyDAO.getAllCounty();

        String[] cwbs = cwbsStr.split(",");
        for (String cwb : cwbs) {
            cwb = cwb.replaceAll("'", "");
            saveFeeRelative(cwb, currentUser, allProvince, allCity, allCounty);
        }

        logger.info("归班审核后插入派费相关表操作结束");
    }

    private void saveFeeRelative(String cwb, User currentUser, List<AdressVO> allProvince, List<AdressVO> allCity, List<AdressVO> allCounty) {
        logger.info("正处理订单号：" + cwb);

        DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
        CwbOrder order = cwbDAO.getCwbByCwb(cwb);
        List<OrderFlow> orderFlows = orderFlowDAO.getOrderFlowByCwb(cwb);

        //站点到站时间
        Date credate = null;
        //导入时间
        Date create_time = null;
        //领货时间
        Date pick_time = null;
        for (OrderFlow orderFlow : orderFlows) {
            if (credate == null && FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() == orderFlow.getFlowordertype()) {
                credate = orderFlow.getCredate();
            }
            if (create_time == null && FlowOrderTypeEnum.DaoRuShuJu.getValue() == orderFlow.getFlowordertype()) {
                create_time = orderFlow.getCredate();
            }
            if (pick_time == null && FlowOrderTypeEnum.FenZhanLingHuo.getValue() == orderFlow.getFlowordertype()) {
                pick_time = orderFlow.getCredate();
            }
            if (credate != null && create_time != null && pick_time != null) {
                break;
            }
        }

        int chargeType;
        String userName = "";

        BigDecimal realWeight = BigDecimal.ZERO;

        if (order.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue())
            realWeight = BigDecimal.valueOf(order.getRealweight());
        else
            realWeight = order.getCarrealweight();

        long branchId = 0;
        if (order.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO.getValue() || order.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO_JIT.getValue())
            branchId = order.getPickbranchid();
        else
            branchId = order.getInstationid();

        if (branchId > 0) {
            //如果揽件站点不为空，创建揽件费订单。
            chargeType = DeliveryFeeRuleChargeType.GET.getValue();

            List<User> deliver = userDAO.getUserByid(order.getInstationhandlerid());
            if (deliver.size() > 0) {
                userName = deliver.get(0).getRealname();
            }

            String address = "";
            String province = "";
            String city = "";
            String county = "";
            if (order.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO.getValue() || order.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO_JIT.getValue()) {
                address = order.getSenderaddress();
                province = order.getSenderprovince();
                city = order.getSendercity();
                county = order.getSendercounty();
            } else {
                address = StringUtils.remove(order.getRemark4(), "&");
            }
            if (StringUtils.isBlank(province)) {
                province = getEffectiveAddressId(address, allProvince, null);
            }
            if (StringUtils.isNotBlank(province) && StringUtils.isBlank(city)) {
                String parentCode = getAddressCode(province, allProvince);
                city = getEffectiveAddressId(address, allCity, parentCode);
            }
            if (StringUtils.isNotBlank(city) && StringUtils.isBlank(county)) {
                String parentCode = getAddressCode(city, allCity);
                county = getEffectiveAddressId(address, allCounty, parentCode);
            }

            DfBillFee fee = dfFeeDAO.findFeeByAdjustCondition(DeliveryFeeChargerType.STAFF.getValue(), cwb, chargeType, (long) order.getInstationhandlerid());
            if (null == fee) {
                logger.info("相同小件员ID{}，相同订单号{}，相同结算状态{}, 未能找到计费明细", order.getInstationhandlerid(), DeliveryFeeChargerType.STAFF.getText(), cwb, chargeType);
                saveDeliveryFee(DeliveryFeeChargerType.STAFF, cwb, order.getTranscwb(), order.getCwbordertypeid(), order.getCustomerid(), order.getSendcarnum(),
                        order.getBackcarnum(), order.getSenderaddress(), order.getConsigneeaddress(), realWeight, order.getCargovolume(),
                        chargeType, order.getInstationhandlerid(), userName, branchId, order.getCwbstate(),
                        order.getFlowordertype(), create_time, order.getOutstationdatetime(), order.getDeliverystate(), order.getEmaildate(), credate,//order_flow, flow ordertype = 7
                        pick_time, deliveryState.getMobilepodtime(), deliveryState.getAuditingtime(), 0, 0, province, city, county,
                        order.getPaybackfee(), order.getReceivablefee(), currentUser.getRealname(), order.getCartype());

            } else {
                logger.info("相同小件员ID{}，相同订单号{}，相同结算状态{}, 能找到计费明细", order.getInstationhandlerid(), DeliveryFeeChargerType.STAFF.getText(), cwb, chargeType);
                saveDeliveryAdjustmentFromFee(DeliveryFeeChargerType.STAFF, fee, currentUser.getRealname(), false);
            }

            if (isJoinBranch(branchId)) {
                fee = dfFeeDAO.findFeeByAdjustCondition(DeliveryFeeChargerType.ORG.getValue(), cwb, chargeType, branchId);
                if (null == fee) {
                    logger.info("相同站点ID{}，相同订单号{}，相同结算状态{}, 未能找到计费明细", branchId, DeliveryFeeChargerType.ORG.getText(), cwb, chargeType);
                    saveDeliveryFee(DeliveryFeeChargerType.ORG, cwb, order.getTranscwb(), order.getCwbordertypeid(), order.getCustomerid(), order.getSendcarnum(),
                            order.getBackcarnum(), order.getSenderaddress(), order.getConsigneeaddress(), realWeight, order.getCargovolume(),
                            chargeType, order.getInstationhandlerid(), userName, branchId, order.getCwbstate(),
                            order.getFlowordertype(), create_time, order.getOutstationdatetime(), order.getDeliverystate(), order.getEmaildate(), credate, //order_flow, flow ordertype = 7
                            pick_time, deliveryState.getMobilepodtime(), deliveryState.getAuditingtime(), 0, 0, province, city, county,
                            order.getPaybackfee(), order.getReceivablefee(), currentUser.getRealname(), order.getCartype());
                } else {
                    logger.info("相同站点ID{}，相同订单号{}，相同结算状态{}, 未能找到计费明细", branchId, DeliveryFeeChargerType.ORG.getText(), cwb, chargeType);
                    saveDeliveryAdjustmentFromFee(DeliveryFeeChargerType.ORG, fee, currentUser.getRealname(), false);
                }
            }
        }

        //如果派件站点不为空，创建派件费订单。
        if (order.getDeliverybranchid() > 0)

        {
            if ((deliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue())
                    || (deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue())
                    || (deliveryState.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue())
                    || (deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue())) {

                chargeType = DeliveryFeeRuleChargeType.SEND.getValue();

                branchId = order.getDeliverybranchid();

                List<User> deliver = userDAO.getUserByid(order.getDeliverid());
                if (deliver.size() > 0) {
                    userName = deliver.get(0).getRealname();
                }

                String address = order.getConsigneeaddress();
                String province = order.getCwbprovince();
                String city = order.getCwbcity();
                String county = order.getCwbcounty();

                if (StringUtils.isBlank(province)) {
                    province = getEffectiveAddressId(address, allProvince, null);
                }
                if (StringUtils.isNotBlank(province) && StringUtils.isBlank(city)) {
                    String parentCode = getAddressCode(province, allProvince);
                    city = getEffectiveAddressId(address, allCity, parentCode);
                }
                if (StringUtils.isNotBlank(city) && StringUtils.isBlank(county)) {
                    String parentCode = getAddressCode(city, allCity);
                    county = getEffectiveAddressId(address, allCounty, parentCode);
                }

                DfBillFee fee = dfFeeDAO.findFeeByAdjustCondition(DeliveryFeeChargerType.STAFF.getValue(), cwb, chargeType, order.getDeliverid());
                if (null == fee) {
                    logger.info("相同小件员ID{}，相同订单号{}，相同结算状态{}, 未能找到计费明细", order.getDeliverid(), DeliveryFeeChargerType.STAFF.getText(), cwb, chargeType);
                    saveDeliveryFee(DeliveryFeeChargerType.STAFF, cwb, order.getTranscwb(), order.getCwbordertypeid(), order.getCustomerid(), order.getSendcarnum(),
                            order.getBackcarnum(), order.getSenderaddress(), order.getConsigneeaddress(), realWeight, order.getCargovolume(),
                            chargeType, order.getDeliverid(), userName, branchId, order.getCwbstate(),
                            order.getFlowordertype(), create_time, order.getOutstationdatetime(), order.getDeliverystate(), order.getEmaildate(), credate,//order_flow, flow ordertype = 7
                            pick_time, deliveryState.getMobilepodtime(), deliveryState.getAuditingtime(), 0, 0, province, city, county,
                            order.getPaybackfee(), order.getReceivablefee(), currentUser.getRealname(), order.getCartype());
                } else {
                    logger.info("相同小件员ID{}，相同订单号{}，相同结算状态{}, 能找到计费明细", order.getDeliverid(), DeliveryFeeChargerType.STAFF.getText(), cwb, chargeType);
                    saveDeliveryAdjustmentFromFee(DeliveryFeeChargerType.STAFF, fee, currentUser.getRealname(), false);
                }
                if (isJoinBranch(branchId)) {
                    fee = dfFeeDAO.findFeeByAdjustCondition(DeliveryFeeChargerType.ORG.getValue(), cwb, chargeType, branchId);
                    logger.info("相同小件员ID{}，相同订单号{}，相同结算状态{}, 未能找到计费明细", branchId, DeliveryFeeChargerType.ORG.getText(), cwb, chargeType);
                    if (null == fee) {
                        saveDeliveryFee(DeliveryFeeChargerType.ORG, cwb, order.getTranscwb(), order.getCwbordertypeid(), order.getCustomerid(), order.getSendcarnum(),
                                order.getBackcarnum(), order.getSenderaddress(), order.getConsigneeaddress(), realWeight, order.getCargovolume(),
                                chargeType, order.getDeliverid(), userName, branchId, order.getCwbstate(),
                                order.getFlowordertype(), create_time, order.getOutstationdatetime(), order.getDeliverystate(), order.getEmaildate(), credate, //order_flow, flow ordertype = 7
                                pick_time, deliveryState.getMobilepodtime(), deliveryState.getAuditingtime(), 0, 0, province, city, county,
                                order.getPaybackfee(), order.getReceivablefee(), currentUser.getRealname(), order.getCartype());
                    } else {
                        logger.info("相同站点ID{}，相同订单号{}，相同结算状态{}, 未能找到计费明细", branchId, DeliveryFeeChargerType.ORG.getText(), cwb, chargeType);
                        saveDeliveryAdjustmentFromFee(DeliveryFeeChargerType.ORG, fee, currentUser.getRealname(), false);
                    }
                }
            }
        }

    }

    private String getAddressCode(String addressName, List<AdressVO> addresses) {
        if (StringUtils.isNotBlank(addressName)) {
            if (CollectionUtils.isNotEmpty(addresses)) {
                for (AdressVO adressVO : addresses) {
                    if (addressName.equals(adressVO.getName())) {
                        return adressVO.getCode();
                    }
                }
            }
        }
        return null;
    }

    public void saveFeeRelativeAfterOrderReset(String cwb, User currentUser) {
        CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
        saveFeeRelativeAfterOrderResetOrDisabled(cwbOrder, currentUser, true);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFeeRelativeAfterOrderResetOrDisabled(CwbOrder cwbOrder, User currentUser, boolean isFromReset) {
        logger.info("重置反馈后插入派费相关表操作开始");

        Integer chargeType;
        Long id;

        String cwb = cwbOrder.getCwb();

        for (DeliveryFeeChargerType chargerType : DeliveryFeeChargerType.values()) {
            if (cwbOrder.getInstationid() > 0) {
                chargeType = DeliveryFeeRuleChargeType.GET.getValue();
                if (chargerType.equals(DeliveryFeeChargerType.ORG)) {
                    id = cwbOrder.getInstationid();
                } else {
                    id = new Long(cwbOrder.getInstationhandlerid());
                }
                deleteFeeOrAddAdjust(chargerType, cwb, chargeType, id, currentUser, isFromReset);
            }

            if (cwbOrder.getDeliverybranchid() > 0) {
                chargeType = DeliveryFeeRuleChargeType.SEND.getValue();

                if (chargerType.equals(DeliveryFeeChargerType.ORG)) {
                    id = cwbOrder.getDeliverybranchid();
                } else {
                    id = cwbOrder.getDeliverid();
                }
                deleteFeeOrAddAdjust(chargerType, cwb, chargeType, id, currentUser, isFromReset);
            }
        }


        logger.info("重置反馈后插入派费相关表操作结束");
    }

    private void deleteFeeOrAddAdjust(DeliveryFeeChargerType chargerType, String cwb, Integer chargeType, Long branchOrUserId, User currentUser, boolean isFromReset) {
        boolean isQulified = false;
        if (chargerType.equals(DeliveryFeeChargerType.ORG)) {
            if (isJoinBranch(branchOrUserId))
                isQulified = true;
        } else
            isQulified = true;

        if (isQulified) {
            //费用类型，同一配送站点或小件员
            DfBillFee fee = dfFeeDAO.findFeeByAdjustCondition(chargerType.getValue(), cwb, chargeType, branchOrUserId);

            if (fee != null) {
                if (fee.getIsBilled() == 1) {
                    DfAdjustmentRecord adjustment = transformFeeToAdjustment(fee, isFromReset);
                    if (adjustment != null) {
                        if (isFromReset) {
                            //如果是重置反馈，需要记录反馈申请人和反馈通过时间。
                            ApplyEditDeliverystate applyEditDeliverystateWithPass = applyEditDeliverystateDAO.getApplyEditDeliverystateWithPass(cwb);
                            adjustment.setApplyuserid(applyEditDeliverystateWithPass.getApplyuserid());
                            adjustment.setEdittime(applyEditDeliverystateWithPass.getEdittime());
                        }
                        long id = dfAdjustmentRecordDAO.saveAdjustmentRecord(chargerType.getValue(), adjustment, currentUser.getRealname());

                        logger.debug("插入调整记录表成功，Id为" + id + "，结算对象为" + chargerType.getText());
                    }
                } else {
                    logger.info("删除计费明细ID为{}" + fee.getId());
                    dfFeeDAO.deleteFeeById(chargerType.getValue(), fee.getId());
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void saveDeliveryAdjustmentFromFee(DeliveryFeeChargerType chargerType, DfBillFee fee, String currentUser, boolean isFromReset) {
        DfAdjustmentRecord adjustment = transformFeeToAdjustment(fee, isFromReset);

        if (adjustment == null)
            return;

        long id = dfAdjustmentRecordDAO.saveAdjustmentRecord(chargerType.getValue(), adjustment, currentUser);

        logger.debug("插入调整记录表成功，Id为" + id + "，结算对象为" + chargerType.getText());

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void saveDeliveryFee(DeliveryFeeChargerType chargerType, String cwb, String transcwb, int cwbordertypeid, long customerid, long sendcarnum, long backcarnum,
                                 String senderaddress, String consigneeaddress, BigDecimal realweight, BigDecimal cargovolume, int chargeType,
                                 long handlerid, String userName, long branchId, long cwbstate, long flowordertype,
                                 Date create_time, String outstationdatetime, int deliverystate, String emaildate, Date credate, Date pickTime,
                                 Date mobilepodtime, String auditingtime, int isCal, int isBill, String province, String city, String county,
                                 BigDecimal paybackfee, BigDecimal receivablefee, String realname, String cartype) {

        long feeId = dfFeeDAO.saveDeliveryFee(chargerType.getValue(), cwb, transcwb, cwbordertypeid, customerid, sendcarnum, backcarnum,
                senderaddress, consigneeaddress, realweight, cargovolume, chargeType,
                handlerid, userName, branchId, cwbstate, flowordertype,
                create_time, outstationdatetime, deliverystate, emaildate, credate, pickTime,
                mobilepodtime, auditingtime, isCal, isBill, province, city, county,
                paybackfee, receivablefee, realname, cartype);

        logger.debug("插入派费明细表成功，Id为" + feeId + "，结算对象为" + chargerType.getText());

    }

    private boolean isJoinBranch(long branchId) {
        Branch branch = branchDAO.getBranchById(branchId);
        if (branch != null) {
            int contractFlag = Integer.parseInt(branch.getContractflag());
            if (BranchTypeEnum.JiaMeng.getValue() == contractFlag
                    || BranchTypeEnum.JiaMengErJi.getValue() == contractFlag
                    || BranchTypeEnum.JiaMengSanJi.getValue() == contractFlag) {
                return true;
            }
        }
        return false;
    }


    private String getEffectiveAddressId(String address, List<AdressVO> addresses, String parentCode) {
        if (CollectionUtils.isNotEmpty(addresses)) {
            for (AdressVO adressVO : addresses) {
                if (StringUtils.isNotBlank(parentCode)) {
                    if (!parentCode.equals(adressVO.getParentCode())) {
                        continue;
                    }
                }
                if (StringUtils.contains(address, adressVO.getName())) {
                    return adressVO.getName();
                }
            }
        }
        return null;
    }

    private DfAdjustmentRecord transformFeeToAdjustment(DfBillFee fee, boolean isFromReset) {

        DfAdjustmentRecord adjustmentRecord = new DfAdjustmentRecord();

        try {
            PropertyUtils.copyProperties(adjustmentRecord, fee);
        } catch (IllegalAccessException e) {
            logger.error("生成调整派费订单错误" + e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
            logger.error("生成调整派费订单错误" + e.getMessage());
            return null;
        } catch (NoSuchMethodException e) {
            logger.error("生成调整派费订单错误" + e.getMessage());
            return null;
        }
//        adjustmentRecord.setAdjustAmount(fee.getFeeAmount());
        if (fee.getFeeAmount() != null) {
            if (isFromReset)
                adjustmentRecord.setAdjustAmount(fee.getFeeAmount().multiply(new BigDecimal(-1)));
            else
                adjustmentRecord.setAdjustAmount(fee.getFeeAmount());
        }

        return adjustmentRecord;
    }

}
