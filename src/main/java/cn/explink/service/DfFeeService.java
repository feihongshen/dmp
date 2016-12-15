package cn.explink.service;

import cn.explink.core.utils.StringUtils;
import cn.explink.dao.ApplyEditDeliverystateDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.DfAdjustmentRecordDAO;
import cn.explink.dao.DfFeeDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.CountyDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.ApplyEditDeliverystate;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.deliveryFee.DfAdjustmentRecord;
import cn.explink.domain.deliveryFee.DfBillFee;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.JsonUtil;
import cn.explink.util.ResourceBundleUtil;
import com.pjbest.pjorganization.bizservice.service.SbOrgModel;
import com.pjbest.pjorganization.bizservice.service.SbOrgService;
import com.pjbest.pjorganization.bizservice.service.SbOrgServiceHelper;
import com.vip.osp.core.exception.OspException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
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
    private DfAdjustmentRecordDAO dfAdjustmentRecordDAO;
    @Autowired
    private ApplyEditDeliverystateDAO applyEditDeliverystateDAO;
    @Autowired
    private SystemInstallDAO systemInstallDAO;

    private static final String SYSTEM_INSTALL_CARRIERCODE = "CARRIERCODE";
    private static final int FROM_RESET_ORDER = 0;
    private static final int FROM_DISABLE_ORDER = 1;
    private static final int FROM_AUDIT_CONFIRMED = 2;

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

        public static String getTextByValue(int value) {
            for (DeliveryFeeRuleChargeType chargeType : DeliveryFeeRuleChargeType.values()) {
                if (chargeType.getValue() == value)
                    return chargeType.getText();
            }
            return "";
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
            try {
                saveFeeRelative(cwb, currentUser, allProvince, allCity, allCounty);
            } catch (Exception e) {
                //一条错不会影响之后的操作。
                logger.error("插入计费表时出错：订单号{}, 错误信息：{}", cwb, e.getMessage());
                e.printStackTrace();
            }
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
        //揽件出站时间
        Date outstationdatetime = null;
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
            if (outstationdatetime == null && FlowOrderTypeEnum.LanJianChuZhan.getValue() == orderFlow.getFlowordertype()) {
                outstationdatetime = orderFlow.getCredate();
            }

            if (credate != null && create_time != null && pick_time != null && outstationdatetime != null) {
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

        String senderAddr;
        String receiverAddr = order.getConsigneeaddress();

        if (order.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO.getValue() || order.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO_JIT.getValue()) {
            senderAddr = StringUtils.remove(order.getRemark4(), "&");
        } else {
            senderAddr = order.getSenderaddress();
        }

        if (branchId > 0) {
            //如果揽件站点不为空，创建揽件费订单。
            chargeType = DeliveryFeeRuleChargeType.GET.getValue();

            List<User> deliver = userDAO.getUserByid(order.getCollectorid());
            if (deliver.size() > 0) {
                userName = deliver.get(0).getRealname();
            }

            String province = "";
            String city = "";
            String county = "";
            if (order.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO.getValue() || order.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO_JIT.getValue()) {
//                senderAddr = StringUtils.remove(order.getRemark4(), "&");
            } else {
//                senderAddr = order.getSenderaddress();
                //如果不是OXO/OXOJIT 的单省市区就会去取值。
//                province = order.getSenderprovince();
                city = order.getSendercity();
                county = order.getSendercounty();
            }


//            if(StringUtils.isBlank(province)){
            //根据站点到TPS查找相应省份你的信息。
            SbOrgModel orgModelFromTPS =null;
            //去tps查当前省的信息
           // SbOrgModel orgModelFromTPS=findOrgByCarrierAndSiteCode(branchId);
            if (orgModelFromTPS != null) {
                if (StringUtils.isNotBlank(orgModelFromTPS.getProvinceName())) {
                    province = orgModelFromTPS.getProvinceName();
                }
//                if (StringUtils.isNotBlank(orgModelFromTPS.getCityName())) {
//                    city = orgModelFromTPS.getCityName();
//                }
//                if (StringUtils.isNotBlank(orgModelFromTPS.getRegionName())) {
//                    county = orgModelFromTPS.getRegionName();
//                }
            }
//            }
            if (StringUtils.isBlank(province)) {
                province = order.getSenderprovince();
            }

            if (StringUtils.isBlank(province)) {
                province = getEffectiveAddressId(senderAddr, allProvince, null);
            }

            String provinceCode = getAddressCode(province, allProvince, null);

            String cityCode = "";

            if (StringUtils.isNotBlank(province) && StringUtils.isBlank(city)) {
                city = getEffectiveAddressId(senderAddr, allCity, provinceCode);
            }

//            if (StringUtils.isNotBlank(city) && StringUtils.isBlank(county)) {
//                cityCode = getAddressCode(city, allCity, provinceCode);
//                county = getEffectiveAddressId(senderAddr, allCounty, cityCode);
//            }

            //针对线上的增城区问题，修复，如果能匹配市就用之前的方法
            if (StringUtils.isNotBlank(city) && StringUtils.isBlank(county)) {
                cityCode = getAddressCode(city, allCity, provinceCode);
                county = getEffectiveAddressId(senderAddr, allCounty, cityCode);
            }

            //产品要求查找区的搜索范围是在本省里面找
            if (StringUtils.isBlank(county)) {
                county = getEffectiveCountyByProvince(senderAddr, allCounty, allCity, provinceCode);
            }

            //如果有揽件员才生成基础数据。
            if (order.getCollectorid() > 0) {
                DfBillFee fee = dfFeeDAO.findFeeByAdjustCondition(DeliveryFeeChargerType.STAFF.getValue(), cwb, chargeType, (long) order.getCollectorid());
                if (null == fee) {
                    logger.info("相同小件员ID:{}，相同订单号:{}，相同费用类型:{}, 未能找到计费明细", order.getCollectorid(), cwb, DeliveryFeeRuleChargeType.getTextByValue(chargeType));
                    saveDeliveryFee(DeliveryFeeChargerType.STAFF, cwb, order.getTranscwb(), order.getCwbordertypeid(), order.getCustomerid(), order.getSendcarnum(),
                            order.getBackcarnum(), senderAddr, receiverAddr, realWeight, order.getCargovolume(),
                            chargeType, order.getCollectorid(), userName, branchId, order.getCwbstate(),
                            order.getFlowordertype(), create_time, outstationdatetime, order.getDeliverystate(), order.getEmaildate(), credate,//order_flow, flow ordertype = 7
                            pick_time, deliveryState.getMobilepodtime(), deliveryState.getAuditingtime(), 0, 0, province, city, county,
                            order.getPaybackfee(), order.getReceivablefee(), currentUser.getRealname(), order.getCartype());

                } else {
                    logger.info("相同小件员ID:{}，相同订单号:{}，相同费用类型:{}, 能找到计费明细, 不生成揽件费用", order.getCollectorid(), cwb, DeliveryFeeRuleChargeType.getTextByValue(chargeType));
                    //commented by Steve PENG, 2016/08/03, 揽件费不需要调整记录。
                    //saveDeliveryAdjustmentFromFee(DeliveryFeeChargerType.STAFF, fee, currentUser.getRealname());
                    //commented by Steve PENG, 2016/08/03, end
                }
            }
            //如果站点是加盟站点
            if (isJoinBranch(branchId)) {
                DfBillFee fee = dfFeeDAO.findFeeByAdjustCondition(DeliveryFeeChargerType.ORG.getValue(), cwb, chargeType, branchId);
                if (null == fee) {
                    logger.info("相同站点ID:{}，相同订单号:{}，相同费用类型:{}, 未能找到计费明细", branchId, cwb, DeliveryFeeRuleChargeType.getTextByValue(chargeType));
                    saveDeliveryFee(DeliveryFeeChargerType.ORG, cwb, order.getTranscwb(), order.getCwbordertypeid(), order.getCustomerid(), order.getSendcarnum(),
                            order.getBackcarnum(), senderAddr, receiverAddr, realWeight, order.getCargovolume(),
                            chargeType, order.getCollectorid(), userName, branchId, order.getCwbstate(),
                            order.getFlowordertype(), create_time, outstationdatetime, order.getDeliverystate(), order.getEmaildate(), credate, //order_flow, flow ordertype = 7
                            pick_time, deliveryState.getMobilepodtime(), deliveryState.getAuditingtime(), 0, 0, province, city, county,
                            order.getPaybackfee(), order.getReceivablefee(), currentUser.getRealname(), order.getCartype());
                } else {
                    logger.info("相同站点ID:{}，相同订单号:{}，相同费用类型:{}, 能找到计费明细, 不生成揽件费用", branchId, cwb, DeliveryFeeRuleChargeType.getTextByValue(chargeType));
                    //commented by Steve PENG, 2016/08/03, 揽件费不需要调整记录。
                    //saveDeliveryAdjustmentFromFee(DeliveryFeeChargerType.ORG, fee, currentUser.getRealname());
                    //commented by Steve PENG, 2016/08/03, end
                }
            }
        }

        //如果派件站点不为空，创建派件费订单。
        if (order.getDeliverybranchid() > 0) {
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

//                String address = order.getConsigneeaddress();
                String province = "";
//                        = order.getCwbprovince();
                String city = order.getCwbcity();
                String county = order.getCwbcounty();

//                if (StringUtils.isBlank(province)) {
                //根据站点到TPS查找相应省份你的信息。
                SbOrgModel orgModelFromTPS = null;
                // SbOrgModel orgModelFromTPS =findOrgByCarrierAndSiteCode(branchId)
                if (orgModelFromTPS != null) {
                    if (StringUtils.isNotBlank(orgModelFromTPS.getProvinceName())) {
                        province = orgModelFromTPS.getProvinceName();
                    }
//                    if (StringUtils.isNotBlank(orgModelFromTPS.getCityName())) {
//                        city = orgModelFromTPS.getCityName();
//                    }
//                    if (StringUtils.isNotBlank(orgModelFromTPS.getRegionName())) {
//                        county = orgModelFromTPS.getRegionName();
//                    }
                }
//                }

                if (StringUtils.isBlank(province)) {
                    //如果没有匹配到省份，派件就拿本省的province code。
//                    province = getEffectiveAddressId(receiverAddr, allProvince, null);
                    AdressVO currentProvince = provinceDAO.getProvinceByCode(ResourceBundleUtil.provinceCode);
                    if (currentProvince != null) {
                        province = currentProvince.getName();
                    }
                }

                if (StringUtils.isBlank(province)) {
                    province = order.getCwbprovince();
                }

                if (StringUtils.isBlank(province)) {
                    province = getEffectiveAddressId(receiverAddr, allProvince, null);
                }

                String provinceCode = getAddressCode(province, allProvince, null);
                String cityCode = "";

                if (StringUtils.isNotBlank(province) && StringUtils.isBlank(city)) {
                    city = getEffectiveAddressId(receiverAddr, allCity, provinceCode);
                }
//                if (StringUtils.isNotBlank(city) && StringUtils.isBlank(county)) {
//                    cityCode = getAddressCode(city, allCity, provinceCode);
//                    county = getEffectiveAddressId(receiverAddr, allCounty, cityCode);
//                }

                //针对线上的增城区问题，修复，如果能匹配市就用之前的方法
                if (StringUtils.isNotBlank(city) && StringUtils.isBlank(county)) {
                    cityCode = getAddressCode(city, allCity, provinceCode);
                    county = getEffectiveAddressId(receiverAddr, allCounty, cityCode);
                }

                //产品要求查找区的搜索范围是在本省里面找
                if (StringUtils.isBlank(county)) {
                    county = getEffectiveCountyByProvince(receiverAddr, allCounty, allCity, provinceCode);
                }

                if (order.getDeliverid() > 0) {
                    DfBillFee fee = dfFeeDAO.findFeeByAdjustCondition(DeliveryFeeChargerType.STAFF.getValue(), cwb, chargeType, order.getDeliverid());
                    if (null == fee) {
                        logger.info("相同小件员ID:{}，相同订单号:{}，相同费用类型:{}, 未能找到计费明细", order.getDeliverid(), cwb, DeliveryFeeRuleChargeType.getTextByValue(chargeType));
                        saveDeliveryFee(DeliveryFeeChargerType.STAFF, cwb, order.getTranscwb(), order.getCwbordertypeid(), order.getCustomerid(), order.getSendcarnum(),
                                order.getBackcarnum(), senderAddr, receiverAddr, realWeight, order.getCargovolume(),
                                chargeType, order.getDeliverid(), userName, branchId, order.getCwbstate(),
                                order.getFlowordertype(), create_time, outstationdatetime, order.getDeliverystate(), order.getEmaildate(), credate,//order_flow, flow ordertype = 7
                                pick_time, deliveryState.getMobilepodtime(), deliveryState.getAuditingtime(), 0, 0, province, city, county,
                                order.getPaybackfee(), order.getReceivablefee(), currentUser.getRealname(), order.getCartype());
                    } else {
                        logger.info("相同小件员ID:{}，相同订单号:{}，相同费用类型:{}, 能找到计费明细", order.getDeliverid(), cwb, DeliveryFeeRuleChargeType.getTextByValue(chargeType));
                        saveDeliveryAdjustmentFromFee(DeliveryFeeChargerType.STAFF, fee, currentUser.getRealname());
                    }
                }

                if (isJoinBranch(branchId)) {
                    DfBillFee fee = dfFeeDAO.findFeeByAdjustCondition(DeliveryFeeChargerType.ORG.getValue(), cwb, chargeType, branchId);
                    logger.info("相同站点ID:{}，相同订单号:{}，相同费用类型:{}, 未能找到计费明细", branchId, cwb, DeliveryFeeRuleChargeType.getTextByValue(chargeType));
                    if (null == fee) {
                        saveDeliveryFee(DeliveryFeeChargerType.ORG, cwb, order.getTranscwb(), order.getCwbordertypeid(), order.getCustomerid(), order.getSendcarnum(),
                                order.getBackcarnum(), senderAddr, receiverAddr, realWeight, order.getCargovolume(),
                                chargeType, order.getDeliverid(), userName, branchId, order.getCwbstate(),
                                order.getFlowordertype(), create_time, outstationdatetime, order.getDeliverystate(), order.getEmaildate(), credate, //order_flow, flow ordertype = 7
                                pick_time, deliveryState.getMobilepodtime(), deliveryState.getAuditingtime(), 0, 0, province, city, county,
                                order.getPaybackfee(), order.getReceivablefee(), currentUser.getRealname(), order.getCartype());
                    } else {
                        logger.info("相同站点ID:{}，相同订单号:{}，相同费用类型:{}, 能找到计费明细", branchId, cwb, DeliveryFeeRuleChargeType.getTextByValue(chargeType));
                        saveDeliveryAdjustmentFromFee(DeliveryFeeChargerType.ORG, fee, currentUser.getRealname());
                    }
                }
            }
        }
    }

    private SbOrgModel findOrgByCarrierAndSiteCode(long branchId) {

        Branch branch = branchDAO.getBranchById(branchId);

        if (branch != null) {
            String carrierCode = "";

            SystemInstall carrierCodeSystemInstall = systemInstallDAO
                    .getSystemInstall(SYSTEM_INSTALL_CARRIERCODE);
            carrierCode = (carrierCodeSystemInstall == null ? ""
                    : carrierCodeSystemInstall.getValue());
            String carrierSiteCode = branch.getTpsbranchcode();

            SbOrgService sbOrgService = new SbOrgServiceHelper.SbOrgServiceClient();
            logger.info("查询机构服务 - 请求：carrierCode={}，carrierSiteCode={}",
                    new Object[]{carrierCode, carrierSiteCode});
            List<SbOrgModel> models = null;
            try {
                models = sbOrgService
                        .findSbOrgByCarrierAndSelfStation(carrierCode, carrierSiteCode);
            } catch (OspException e) {
                e.printStackTrace();
                logger.error("TPS 查询机构服务错误。 错误信息：" + e.getMessage());
            }
            try {
                logger.info("查询机构服务 - 返回：" + JsonUtil.translateToJson(models));
            } catch (IOException e) {
                logger.error("TPS 查询机构服务错误, 转换为json时发生错误。");
            }

            if (CollectionUtils.isNotEmpty(models)) {
                return models.get(0);
            }
        }

        return null;
    }

    private String getEffectiveCountyByProvince(String address, List<AdressVO> allCounty, List<AdressVO> allCity, String provinceCode) {
        for (AdressVO city : allCity) {
            if (city.getParentCode().equals(provinceCode)) {
                for (AdressVO county : allCounty) {
                    if (county.getParentCode().equals(city.getCode())) {
                        if (StringUtils.contains(address, county.getName())) {
                            return county.getName();
                        }
                    }
                }
            }
        }
        return null;
    }

    private String getAddressCode(String addressName, List<AdressVO> addresses, String parentCode) {
        if (StringUtils.isNotBlank(addressName)) {
            if (CollectionUtils.isNotEmpty(addresses)) {
                for (AdressVO adressVO : addresses) {
                    if (addressName.equals(adressVO.getName())) {
                        if (StringUtils.isNotBlank(parentCode)) {
                            if (parentCode.equals(adressVO.getParentCode())) {
                                return adressVO.getCode();
                            }
                        } else {
                            return adressVO.getCode();
                        }
                    }
                }
            }
        }
        return null;
    }

    public void saveFeeRelativeAfterOrderReset(String cwb, User currentUser) {
        CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
        saveFeeRelativeAfterOrderResetOrDisabled(cwbOrder, currentUser, FROM_RESET_ORDER);
    }

    public void saveFeeRelativeAfterOrderDisabled(String cwb) {
        User currentUser = userDAO.getUserByUsername("admin");
        CwbOrder cwbOrder = cwbDAO.getDisabledCwbByCwb(cwb);
        saveFeeRelativeAfterOrderResetOrDisabled(cwbOrder, currentUser, FROM_DISABLE_ORDER);
    }

    private void saveFeeRelativeAfterOrderResetOrDisabled(CwbOrder cwbOrder, User currentUser, int fromWhere) {
        logger.info("重置反馈或失效后插入派费相关表操作开始");

        Integer chargeType;
        Long id = null;
        if (cwbOrder != null) {
            String cwb = cwbOrder.getCwb();

            for (DeliveryFeeChargerType chargerType : DeliveryFeeChargerType.values()) {
                Long senderAddrId;

                if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO.getValue()
                        || cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO_JIT.getValue())
                    senderAddrId = cwbOrder.getPickbranchid();
                else
                    senderAddrId = cwbOrder.getInstationid();

                if (senderAddrId > 0) {
                    chargeType = DeliveryFeeRuleChargeType.GET.getValue();
                    if (chargerType.equals(DeliveryFeeChargerType.ORG)) {
                        id = senderAddrId;
                    } else {
                        id = new Long(cwbOrder.getCollectorid());
                    }
                    deleteFeeOrAddAdjust(chargerType, cwb, chargeType, id, currentUser, fromWhere);
                }

                if (cwbOrder.getDeliverybranchid() > 0) {
                    chargeType = DeliveryFeeRuleChargeType.SEND.getValue();

                    if (chargerType.equals(DeliveryFeeChargerType.ORG)) {
                        id = cwbOrder.getDeliverybranchid();
                    } else {
                        id = cwbOrder.getDeliverid();
                    }
                    deleteFeeOrAddAdjust(chargerType, cwb, chargeType, id, currentUser, fromWhere);
                }
            }
        }


        logger.info("重置反馈后插入派费相关表操作结束");
    }

    private void deleteFeeOrAddAdjust(DeliveryFeeChargerType chargerType, String cwb, Integer chargeType, Long branchOrUserId, User currentUser, int fromWhere) {
        //是否适合删除派费记录或增加调整记录。
        boolean isQualified = false;
        //amended by Steve PENG, 2016/08/03, 揽件费不需要调整
        if (DeliveryFeeRuleChargeType.GET.getValue() == chargeType) {
            //揽件费不需要
            isQualified = false;
        } else {
            if (branchOrUserId != null && branchOrUserId > 0) {
                if (chargerType.equals(DeliveryFeeChargerType.ORG)) {
                    if (isJoinBranch(branchOrUserId)) {
                        //加盟站点才需要
                        isQualified = true;
                    }
                } else
                    isQualified = true;
            }
        }
        //amended by Steve PENG, 2016/08/03, end

        if (isQualified) {
            //费用类型，同一配送站点或小件员
            DfBillFee fee = dfFeeDAO.findFeeByAdjustCondition(chargerType.getValue(), cwb, chargeType, branchOrUserId);

            if (fee != null) {
                if (fee.getIsBilled() == 1) {
                    //added by Steve PENG, 2016/07/29, 查找同一订单号，同一配送站点或小件员，同一费用类型的调整记录。
                    //调整记录表，失效订单时，会插入负数的记录，先判断现在相同条件的记录的总数是否已经小于零，若是，则不插入。
                    //现在不对归班审核和重置反馈加上如此校验，因为假设进入这个方法前（归班审核或重置反馈后），上游业务已经有不能重复插入的验证逻辑，这里不做对重复插入的校验。
                    if (fromWhere == FROM_DISABLE_ORDER) {
                        List<DfAdjustmentRecord> existingRecords = dfAdjustmentRecordDAO.findByAdjustCondition(chargerType.getValue(),
                                cwb, chargeType, branchOrUserId);

                        BigDecimal existingAdjustFee = BigDecimal.ZERO;
                        if (CollectionUtils.isNotEmpty(existingRecords)) {
                            for (DfAdjustmentRecord existingRecord : existingRecords) {
                                existingAdjustFee = existingAdjustFee.add(existingRecord.getAdjustAmount());
                            }
                        }
                        if (existingAdjustFee.compareTo(BigDecimal.ZERO) < 0) {
                            if (DfFeeService.DeliveryFeeChargerType.ORG.equals(chargerType)) {
                                logger.info("失效订单时，插入调整记录失败，订单号为{}，费用类型为{}，站点ID为{}的订单在调整记录表里的总金额小于零，所以不在插入调整记录。", cwb,
                                        DeliveryFeeRuleChargeType.getTextByValue(chargeType), branchOrUserId);
                            } else {
                                logger.info("失效订单时，插入调整记录失败，订单号为{}，费用类型为{}，小件员ID为{}的订单在调整记录表里的总金额小于零，所以不在插入调整记录。", cwb,
                                        DeliveryFeeRuleChargeType.getTextByValue(chargeType), branchOrUserId);
                            }
                            return;
                        }
                    }
                    //added by Steve PENG, 2016/07/29, end
                    DfAdjustmentRecord adjustment = transformFeeToAdjustment(fee, fromWhere);
                    if (adjustment != null) {
                        if (fromWhere == FROM_RESET_ORDER) {
                            //如果是重置反馈，需要记录反馈申请人和反馈通过时间。
                            ApplyEditDeliverystate applyEditDeliverystateWithPass = applyEditDeliverystateDAO.getApplyEditDeliverystateWithPass(cwb);
                            adjustment.setApplyuserid(applyEditDeliverystateWithPass.getApplyuserid());
                            adjustment.setEdittime(applyEditDeliverystateWithPass.getEdittime());
                        }
                        long id = dfAdjustmentRecordDAO.saveAdjustmentRecord(chargerType.getValue(), adjustment, currentUser.getRealname());

                        logger.info("插入调整记录表成功，Id为{}，结算对象为{}", id, chargerType.getText());
                    }
                } else {
                    logger.info("删除计费明细ID为{}", fee.getId());
                    dfFeeDAO.deleteFeeById(chargerType.getValue(), fee.getId());
                }
            }
        }
    }

    private void saveDeliveryAdjustmentFromFee(DeliveryFeeChargerType chargerType, DfBillFee fee, String currentUser) {
        DfAdjustmentRecord adjustment = transformFeeToAdjustment(fee, FROM_AUDIT_CONFIRMED);

        if (adjustment == null)
            return;

        long id = dfAdjustmentRecordDAO.saveAdjustmentRecord(chargerType.getValue(), adjustment, currentUser);

        logger.debug("插入调整记录表成功，Id为{}，结算对象为{}", id, chargerType.getText());

    }

    private void saveDeliveryFee(DeliveryFeeChargerType chargerType, String cwb, String transcwb, int cwbordertypeid, long customerid, long sendcarnum, long backcarnum,
                                 String senderaddress, String consigneeaddress, BigDecimal realweight, BigDecimal cargovolume, int chargeType,
                                 long handlerid, String userName, long branchId, long cwbstate, long flowordertype,
                                 Date create_time, Date outstationdatetime, int deliverystate, String emaildate, Date credate, Date pickTime,
                                 Date mobilepodtime, String auditingtime, int isCal, int isBill, String province, String city, String county,
                                 BigDecimal paybackfee, BigDecimal receivablefee, String realname, String cartype) {

        long feeId = dfFeeDAO.saveDeliveryFee(chargerType.getValue(), cwb, transcwb, cwbordertypeid, customerid, sendcarnum, backcarnum,
                senderaddress, consigneeaddress, realweight, cargovolume, chargeType,
                handlerid, userName, branchId, cwbstate, flowordertype,
                create_time, outstationdatetime, deliverystate, emaildate, credate, pickTime,
                mobilepodtime, auditingtime, isCal, isBill, province, city, county,
                paybackfee, receivablefee, realname, cartype);

        logger.debug("插入派费明细表成功，Id为{}，结算对象为{}", feeId, chargerType.getText());

    }

    private boolean isJoinBranch(long branchId) {
        Branch branch = branchDAO.getBranchById(branchId);
        if (branch != null) {
            if (NumberUtils.isNumber(branch.getContractflag())) {
                int contractFlag = Integer.parseInt(branch.getContractflag());
                if (BranchTypeEnum.JiaMeng.getValue() == contractFlag
                        || BranchTypeEnum.JiaMengErJi.getValue() == contractFlag
                        || BranchTypeEnum.JiaMengSanJi.getValue() == contractFlag) {
                    return true;
                }
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

    private DfAdjustmentRecord transformFeeToAdjustment(DfBillFee fee, int fromWhere) {

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
            if (fromWhere == FROM_DISABLE_ORDER || fromWhere == FROM_RESET_ORDER) {
                //如果是来自失效订单或重置反馈,插入负数数据
                adjustmentRecord.setAdjustAmount(fee.getFeeAmount().multiply(new BigDecimal(-1)));
            } else {
                adjustmentRecord.setAdjustAmount(fee.getFeeAmount());
            }
        }

        return adjustmentRecord;
    }

    /**
     * @param cwb         订单号
     * @param isCalculted 是否已计费，0未，1已计费
     * @param chargerType DeliveryFeeChargerType。ORG/DeliveryFeeChargerType.STAFF
     * @return
     * @author zhili01.liang on 20160816
     * 根据条件获取计费记录
     */
    public List<DfBillFee> findByCwbAndCalculted(String cwb, int isCalculted, int chargerType, int begin, int interval) {
        return dfFeeDAO.findByCwbAndCalculted(cwb, isCalculted, chargerType, begin, interval);
    }

}
