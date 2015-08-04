package cn.explink.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchDeliveryFeeBillDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.ExpressSetBranchDeliveryFeeBill;
import cn.explink.domain.ExpressSetBranchDeliveryFeeBillDetail;
import cn.explink.domain.VO.ExpressSetBranchDeliveryFeeBillVO;
import cn.explink.enumutil.DeliveryFeeBillDateTypeEnum;
import cn.explink.enumutil.DeliveryFeeBillStateEnum;
import cn.explink.enumutil.PaiFeiBuZhuTypeEnum;
import cn.explink.enumutil.PaiFeiRuleTabEnum;
import cn.explink.enumutil.YesOrNoStateEnum;
import cn.explink.util.BeanUtilsSelfDef;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.HtmlToWord;
import cn.explink.util.StringUtil;

@Service
public class BranchDeliveryFeeBillService {

	@Autowired
	BranchDeliveryFeeBillDAO branchDeliveryFeeBillDAO;
	@Autowired
	PaiFeiRuleService paiFeiRuleService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;

	public ExpressSetBranchDeliveryFeeBillVO getBranchDeliveryFeeBillVO(int id) {
		// 返回值
		ExpressSetBranchDeliveryFeeBillVO rtnVO = null;
		List<ExpressSetBranchDeliveryFeeBillDetail> billDetailList = new ArrayList<ExpressSetBranchDeliveryFeeBillDetail>();
		ExpressSetBranchDeliveryFeeBill branchDeliveryFeeBill = this.branchDeliveryFeeBillDAO
				.getBranchDeliveryFeeBillListById(id);
		if (branchDeliveryFeeBill != null) {
			rtnVO = new ExpressSetBranchDeliveryFeeBillVO();
			BeanUtilsSelfDef.copyPropertiesIgnoreException(rtnVO,
					branchDeliveryFeeBill);
		}
		Branch branch = this.branchDAO
				.getBranchByBranchid(branchDeliveryFeeBill.getBranchId());
		rtnVO.setBranchName(branch.getBranchname());
		billDetailList = this.branchDeliveryFeeBillDAO.getBranchDeliveryFeeBillDetailList(id);
		rtnVO.setBillDetailList(billDetailList);
		return rtnVO;
	}

	@Transactional
	public void updateBranchDeliveryFeeBill(
			ExpressSetBranchDeliveryFeeBillVO billVO) {
		if (billVO != null) {
			String cwbsRetained = billVO.getCwbs();
			if(StringUtils.isNotBlank(cwbsRetained)){
				cwbsRetained = StringUtil.removalDuplicateString(cwbsRetained);
				billVO.setCwbs(cwbsRetained);
				cwbsRetained = StringUtil.getStringsByStringList(Arrays.asList(cwbsRetained.split(",")));
			}
			ExpressSetBranchDeliveryFeeBill bill = this.branchDeliveryFeeBillDAO.getBranchDeliveryFeeBillListById(billVO.getId());
			if(bill != null && StringUtils.isNotBlank(bill.getCwbs())){
				String cwbsRemoved = bill.getCwbs();
				if(StringUtils.isNotBlank(cwbsRetained)){
					cwbsRemoved = StringUtil.removalDuplicateString(bill.getCwbs(), cwbsRetained);
				}
				int branchfeebillexportflag = 0;
				this.branchDeliveryFeeBillDAO.updateCwbOrder(branchfeebillexportflag, cwbsRemoved);
			}
			this.branchDeliveryFeeBillDAO.deleteBranchDeliveryFeeBillDetail(billVO.getId(), cwbsRetained);
			bill = new ExpressSetBranchDeliveryFeeBill();
			BeanUtilsSelfDef.copyPropertiesIgnoreException(bill, billVO);
			this.branchDeliveryFeeBillDAO.updateBranchDeliveryFeeBill(bill);
		}
	}

	@Transactional
	public void deleteBranchDeliveryFeeBill(String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		ids = StringUtil.getStringsByStringList(list);
		
		List<ExpressSetBranchDeliveryFeeBillDetail> detailList = this.branchDeliveryFeeBillDAO.getBranchDeliveryFeeBillDetailListByBillIds(ids);
		if(detailList != null && !detailList.isEmpty()){
			String cwbs = "";
			for(int i = 0; i < detailList.size(); i++){
				if(detailList.get(i) != null && StringUtils.isNotBlank(detailList.get(i).getCwb())){
					cwbs = detailList.get(i).getCwb() + "," + cwbs;
				}
			}
			if(StringUtils.isNotBlank(cwbs)){
				// 更新订单表加盟商派费账单导出标志
				int branchfeebillexportflag = 0;
				this.branchDeliveryFeeBillDAO.updateCwbOrder(branchfeebillexportflag, StringUtil.getStringsByStringList(Arrays.asList(cwbs.split(","))));
			}
		}
		// 删除加盟商派费账单子表
		this.branchDeliveryFeeBillDAO.deleteBranchDeliveryFeeBillDetailByBillIds(ids);
		// 删除加盟商派费账单主表
		this.branchDeliveryFeeBillDAO.deleteBranchDeliveryFeeBill(ids);
	}

	@Transactional
	public int createBranchDeliveryFeeBill(
			ExpressSetBranchDeliveryFeeBill branchDeliveryFeeBill) {

		String leftJoinSql = "";
		String onSql = "";
		String dateColumn = "";
		int dateType = branchDeliveryFeeBill.getDateType();
		if (dateType == DeliveryFeeBillDateTypeEnum.ShenHeDate.getValue()) {
			dateColumn = "d.auditingtime";
		} else if (dateType == DeliveryFeeBillDateTypeEnum.FanKuiDate
				.getValue()) {
			dateColumn = "d.deliverytime";
		} else if (dateType == DeliveryFeeBillDateTypeEnum.FaHuoDate.getValue()) {
			dateColumn = "cwb.emaildate";
		} else if (dateType == DeliveryFeeBillDateTypeEnum.RuKuDate.getValue()) {
			leftJoinSql = " left join express_ops_order_intowarhouse i ";
			onSql = " on cwb.cwb = i.cwb ";
			dateColumn = "i.credate";
		}

		String cwbs = "";
		List<ExpressSetBranchDeliveryFeeBillDetail> billDetailList = this.branchDeliveryFeeBillDAO.getBranchDeliveryFeeBillDetailList();
		if(billDetailList != null && !billDetailList.isEmpty()){
			for(int i = 0; i < billDetailList.size(); i++){
				if(billDetailList.get(i) != null && StringUtils.isNotBlank(billDetailList.get(i).getCwb())){
					cwbs = billDetailList.get(i).getCwb() + "," + cwbs;
				}
			}
		}
		if(StringUtils.isNotBlank(cwbs)){
			cwbs = cwbs.substring(0, cwbs.length() - 1);
			List<String> list = Arrays.asList(cwbs.split(","));
			cwbs = StringUtil.getStringsByStringList(list);
		}
		List<CwbOrder> orderList = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBill(branchDeliveryFeeBill, leftJoinSql,
						onSql, dateColumn, cwbs);
		Branch branch = this.branchDAO.getBranchByBranchid(branchDeliveryFeeBill.getBranchId());
		cwbs = "";
		int cwbCount = 0;
		BigDecimal deliverySumFee = new BigDecimal(0.0);
		BigDecimal deliveryBasicFee = new BigDecimal(0.0);
		BigDecimal deliveryCollectionSubsidyFee = new BigDecimal(0.0);
		BigDecimal deliveryAreaSubsidyFee = new BigDecimal(0.0);
		BigDecimal deliveryExceedSubsidyFee = new BigDecimal(0.0);
		BigDecimal deliveryBusinessSubsidyFee = new BigDecimal(0.0);
		BigDecimal deliveryAttachSubsidyFee = new BigDecimal(0.0);
		BigDecimal pickupSumFee = new BigDecimal(0.0);
		BigDecimal pickupCollectionSubsidyFee = new BigDecimal(0.0);
		BigDecimal pickupAreaSubsidyFee = new BigDecimal(0.0);
		BigDecimal pickupExceedSubsidyFee = new BigDecimal(0.0);
		BigDecimal pickupAttachSubsidyFee = new BigDecimal(0.0);
		BigDecimal pickupBasicFee = new BigDecimal(0.0);
		BigDecimal pickupBusinessSubsidyFee = new BigDecimal(0.0);
		List<ExpressSetBranchDeliveryFeeBillDetail> detaiList = new ArrayList<ExpressSetBranchDeliveryFeeBillDetail>();
		ExpressSetBranchDeliveryFeeBillDetail billDetail = null;

		BigDecimal deliveryFee = new BigDecimal(0.0);
		if (orderList != null && orderList.size() > 0) {
			//配送费
			Map<String, BigDecimal> deliveryBasicFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Basic, orderList);
			Map<String, BigDecimal> deliveryCollectionSubsidyFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Collection, orderList);
			Map<String, BigDecimal> deliveryAreaSubsidyFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Area, orderList);
			Map<String, BigDecimal> deliveryExceedSubsidyFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Overarea, orderList);
			Map<String, BigDecimal> deliveryBusinessSubsidyFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Business, orderList);
			Map<String, BigDecimal> deliveryAttachSubsidyFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Insertion, orderList);
			//提货费
			Map<String, BigDecimal> pickupCollectionSubsidyFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, PaiFeiBuZhuTypeEnum.Collection, orderList);
			Map<String, BigDecimal> pickupAreaSubsidyFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, PaiFeiBuZhuTypeEnum.Area, orderList);
			Map<String, BigDecimal> pickupExceedSubsidyFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, PaiFeiBuZhuTypeEnum.Overarea, orderList);
			Map<String, BigDecimal> pickupAttachSubsidyFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, PaiFeiBuZhuTypeEnum.Insertion, orderList);
			Map<String, BigDecimal> pickupBasicFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, PaiFeiBuZhuTypeEnum.Basic, orderList);
			Map<String, BigDecimal> pickupBusinessSubsidyFeeMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(branch.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, PaiFeiBuZhuTypeEnum.Business, orderList);
			for (CwbOrder order : orderList) {
				if (StringUtils.isNotBlank(order.getCwb())) {
					billDetail = new ExpressSetBranchDeliveryFeeBillDetail();
					
					deliveryBasicFee = deliveryBasicFeeMap.get(order.getCwb()) != null ? deliveryBasicFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setDeliveryBasicFee(deliveryBasicFee);
					deliveryCollectionSubsidyFee = deliveryCollectionSubsidyFeeMap.get(order.getCwb()) != null ? deliveryCollectionSubsidyFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setDeliveryCollectionSubsidyFee(deliveryCollectionSubsidyFee);
					deliveryAreaSubsidyFee = deliveryAreaSubsidyFeeMap.get(order.getCwb()) != null ? deliveryAreaSubsidyFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setDeliveryAreaSubsidyFee(deliveryAreaSubsidyFee);
					deliveryExceedSubsidyFee = deliveryExceedSubsidyFeeMap.get(order.getCwb()) != null ? deliveryExceedSubsidyFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setDeliveryExceedSubsidyFee(deliveryExceedSubsidyFee);
					deliveryBusinessSubsidyFee = deliveryBusinessSubsidyFeeMap.get(order.getCwb()) != null ? deliveryBusinessSubsidyFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setDeliveryBusinessSubsidyFee(deliveryBusinessSubsidyFee);
					deliveryAttachSubsidyFee = deliveryAttachSubsidyFeeMap.get(order.getCwb()) != null ? deliveryAttachSubsidyFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setDeliveryAttachSubsidyFee(deliveryAttachSubsidyFee);
					deliverySumFee = deliveryBasicFee.add(deliveryCollectionSubsidyFee).add(deliveryAreaSubsidyFee)
							.add(deliveryExceedSubsidyFee).add(deliveryBusinessSubsidyFee).add(deliveryAttachSubsidyFee);
					billDetail.setDeliverySumFee(deliverySumFee);

					pickupCollectionSubsidyFee = pickupCollectionSubsidyFeeMap.get(order.getCwb()) != null ? pickupCollectionSubsidyFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setPickupCollectionSubsidyFee(pickupCollectionSubsidyFee);
					pickupAreaSubsidyFee = pickupAreaSubsidyFeeMap.get(order.getCwb()) != null ? pickupAreaSubsidyFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setPickupAreaSubsidyFee(pickupAreaSubsidyFee);
					pickupExceedSubsidyFee = pickupExceedSubsidyFeeMap.get(order.getCwb()) != null ? pickupExceedSubsidyFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setPickupExceedSubsidyFee(pickupExceedSubsidyFee);
					pickupAttachSubsidyFee = pickupAttachSubsidyFeeMap.get(order.getCwb()) != null ? pickupAttachSubsidyFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setPickupAttachSubsidyFee(pickupAttachSubsidyFee);
					pickupBasicFee = pickupBasicFeeMap.get(order.getCwb()) != null ? pickupBasicFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setPickupBasicFee(pickupBasicFee);
					pickupBusinessSubsidyFee = pickupBusinessSubsidyFeeMap.get(order.getCwb()) != null ? pickupBusinessSubsidyFeeMap.get(order.getCwb()):new BigDecimal(0.00);
					billDetail.setPickupBusinessSubsidyFee(pickupBusinessSubsidyFee);
					pickupSumFee = pickupCollectionSubsidyFee.add(pickupAreaSubsidyFee).add(pickupExceedSubsidyFee)
							.add(pickupAttachSubsidyFee).add(pickupBasicFee).add(pickupBusinessSubsidyFee);
					billDetail.setPickupSumFee(pickupSumFee);
					
					billDetail.setCwb(order.getCwb());
					billDetail.setFlowordertype(new Long(order.getFlowordertype()).intValue());
					billDetail.setCwbordertypeid(String.valueOf(order.getCwbordertypeid()));
					billDetail.setCustomerid(new Long(order.getCustomerid()).intValue());
					billDetail.setReceivablefee(order.getReceivablefee());
					if(order.getReceivablefee().equals(BigDecimal.ZERO)){
						billDetail.setIsReceived(YesOrNoStateEnum.No.getValue());
					} else {
						billDetail.setIsReceived(YesOrNoStateEnum.Yes.getValue());
					}
					billDetail.setEmaildate(order.getEmaildate());
					billDetail.setNewpaywayid(order.getNewpaywayid());
					billDetail.setPodtime(order.getPodtime());
					billDetail.setSumFee(deliverySumFee.add(pickupSumFee));
					billDetail.setBasicFee(deliveryBasicFee.add(pickupBasicFee));
					billDetail.setCollectionSubsidyFee(deliveryCollectionSubsidyFee.add(pickupCollectionSubsidyFee));
					billDetail.setAreaSubsidyFee(deliveryAreaSubsidyFee.add(pickupAreaSubsidyFee));
					billDetail.setExceedSubsidyFee(deliveryExceedSubsidyFee.add(pickupExceedSubsidyFee));
					billDetail.setBusinessSubsidyFee(deliveryBusinessSubsidyFee.add(pickupBusinessSubsidyFee));
					billDetail.setAttachSubsidyFee(deliveryAttachSubsidyFee.add(pickupAttachSubsidyFee));
					detaiList.add(billDetail);
					
					cwbs += order.getCwb() + ",";
					cwbCount++;
					deliveryFee = deliveryFee.add(deliverySumFee).add(pickupSumFee);
				}
			}
		}
		if (StringUtils.isNotBlank(cwbs)) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
//			cwbs = StringUtil.removalDuplicateString(cwbs);
			// 更新订单表加盟商派费账单导出标志
			int branchfeebillexportflag = 1;
			this.branchDeliveryFeeBillDAO.updateCwbOrder(branchfeebillexportflag, StringUtil.getStringsByStringList(Arrays.asList(cwbs.split(","))));
		}
		branchDeliveryFeeBill.setCwbs(cwbs);
		branchDeliveryFeeBill.setCwbCount(cwbCount);
		branchDeliveryFeeBill.setDeliveryFee(deliveryFee);
		branchDeliveryFeeBill.setCreateDate(DateTimeUtil.getNowDate());
		branchDeliveryFeeBill.setBillBatch(this.generateBillBatch());
		branchDeliveryFeeBill.setBillState(DeliveryFeeBillStateEnum.WeiShenHe
				.getValue());
		branchDeliveryFeeBill
				.setShenHeDate(StringUtil
						.nullConvertToEmptyString(branchDeliveryFeeBill
								.getShenHeDate()));
		branchDeliveryFeeBill
				.setHeXiaoDate(StringUtil
						.nullConvertToEmptyString(branchDeliveryFeeBill
								.getHeXiaoDate()));

		long id = this.branchDeliveryFeeBillDAO
				.createBranchDeliveryFeeBill(branchDeliveryFeeBill);

		if(detaiList != null){
			for(int i = 0; i < detaiList.size(); i++){
				billDetail = detaiList.get(i);
				if(billDetail != null){
					billDetail.setBillId(new Long(id).intValue());
					this.branchDeliveryFeeBillDAO.createBranchDeliveryFeeBillDetail(billDetail);
				}
			}
		}
		return new Long(id).intValue();
	}

	public String generateBillBatch() {
		String rule = "B";
		String nowTime = DateTimeUtil.getNowTime("yyyyMMdd");
		String billBatch = rule + nowTime;
		String orderStr = "00001";
		ExpressSetBranchDeliveryFeeBill bill = this.branchDeliveryFeeBillDAO.getMaxBillBatch(billBatch);
		if(bill != null){
			String maxBillBatch = bill.getBillBatch();
			if(StringUtils.isNotBlank(maxBillBatch)){
				String maxOrderStr = maxBillBatch.substring(9);
				int maxOrderInt = Integer.valueOf(maxOrderStr);
				maxOrderInt++;
				orderStr = String.valueOf(maxOrderInt);
				while(orderStr.length() < 5){
					orderStr = "0" + orderStr;
				}
			}
		}
		billBatch = billBatch + orderStr;
		return billBatch;
	}

	public void exportByCustomer(String content, String contextPath, String rootPath) {
		content = joinHtml(content, contextPath, rootPath);
		String fileName = "派费汇总表" + DateTimeUtil.getNowTimeNo() + ".doc";
		HtmlToWord.writeWordFile(content, fileName);
	}

	public String joinHtml(String content, String contextPath, String rootPath) {
		rootPath = rootPath.replaceAll("\\\\", "/");
		String path = rootPath;
		content = content.replaceAll(contextPath + "/images/image002.jpg", path + "/images/image001.png");
		content = "<html>"
				+ "<head>"
				+ "<meta http-equiv=Content-Type content=\'text/html; charset=gb2312\'>"
				+ "<meta name=ProgId content=Word.Document>"
				+ "<meta name=Generator content=\'Microsoft Word 12\'>"
				+ "<meta name=Originator content=\'Microsoft Word 12\'>"
				+ "<link rel=File-List href=\'"
				+ path
				+ "/filelist.xml\'>"
				+ "<link rel=Edit-Time-Data href=\'"
				+ path
				+ "/editdata.mso\'>"
				+ "<link rel=dataStoreItem href=\'"
				+ path
				+ "/item0001.xml\'"
				+ "target=\'"
				+ path
				+ "/props0002.xml\'>"
				+ "<link rel=themeData href=\'"
				+ path
				+ "/themedata.thmx\'>"
				+ "<link rel=colorSchemeMapping href=\'"
				+ path
				+ "/colorschememapping.xml\'>"
				+ "<!--[if gte mso 9]><xml>"
				+ " <w:WordDocument>"
				+ "  <w:TrackMoves>false</w:TrackMoves>"
				+ "  <w:TrackFormatting/>"
				+ "  <w:PunctuationKerning/>"
				+ "  <w:DrawingGridVerticalSpacing>7.8 磅</w:DrawingGridVerticalSpacing>"
				+ "  <w:DisplayHorizontalDrawingGridEvery>0</w:DisplayHorizontalDrawingGridEvery>"
				+ "  <w:DisplayVerticalDrawingGridEvery>2</w:DisplayVerticalDrawingGridEvery>"
				+ "  <w:ValidateAgainstSchemas/>"
				+ "  <w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid>"
				+ "  <w:IgnoreMixedContent>false</w:IgnoreMixedContent>"
				+ "  <w:AlwaysShowPlaceholderText>false</w:AlwaysShowPlaceholderText>"
				+ "  <w:DoNotPromoteQF/>"
				+ "  <w:LidThemeOther>EN-US</w:LidThemeOther>"
				+ "  <w:LidThemeAsian>ZH-CN</w:LidThemeAsian>"
				+ "  <w:LidThemeComplexScript>X-NONE</w:LidThemeComplexScript>"
				+ "  <w:Compatibility>"
				+ "   <w:SpaceForUL/>"
				+ "   <w:BalanceSingleByteDoubleByteWidth/>"
				+ "   <w:DoNotLeaveBackslashAlone/>"
				+ "   <w:ULTrailSpace/>"
				+ "   <w:DoNotExpandShiftReturn/>"
				+ "   <w:AdjustLineHeightInTable/>"
				+ "   <w:BreakWrappedTables/>"
				+ "   <w:SnapToGridInCell/>"
				+ "   <w:WrapTextWithPunct/>"
				+ "   <w:UseAsianBreakRules/>"
				+ "   <w:DontGrowAutofit/>"
				+ "   <w:SplitPgBreakAndParaMark/>"
				+ "   <w:DontVertAlignCellWithSp/>"
				+ "   <w:DontBreakConstrainedForcedTables/>"
				+ "   <w:DontVertAlignInTxbx/>"
				+ "   <w:Word11KerningPairs/>"
				+ "   <w:CachedColBalance/>"
				+ "   <w:UseFELayout/>"
				+ "  </w:Compatibility>"
				+ "  <m:mathPr>"
				+ "   <m:mathFont m:val=\'Cambria Math\'/>"
				+ "   <m:brkBin m:val=\'before\'/>"
				+ "   <m:brkBinSub m:val=\'--\'/>"
				+ "   <m:smallFrac m:val=\'off\'/>"
				+ "   <m:dispDef/>"
				+ "   <m:lMargin m:val=\'0\'/>"
				+ "   <m:rMargin m:val=\'0\'/>"
				+ "   <m:defJc m:val=\'centerGroup\'/>"
				+ "   <m:wrapIndent m:val=\'1440\'/>"
				+ "   <m:intLim m:val=\'subSup\'/>"
				+ "   <m:naryLim m:val=\'undOvr\'/>"
				+ "  </m:mathPr></w:WordDocument>"
				+ "</xml><![endif]--><!--[if gte mso 9]><xml>"
				+ " <w:LatentStyles DefLockedState=\'false\' DefUnhideWhenUsed=\'true\'"
				+ "  DefSemiHidden=\'true\' DefQFormat=\'false\' DefPriority=\'99\'"
				+ "  LatentStyleCount=\'267\'>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'0\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Normal\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'9\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'heading 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'9\' QFormat=\'true\' Name=\'heading 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'9\' QFormat=\'true\' Name=\'heading 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'9\' QFormat=\'true\' Name=\'heading 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'9\' QFormat=\'true\' Name=\'heading 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'9\' QFormat=\'true\' Name=\'heading 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'9\' QFormat=\'true\' Name=\'heading 7\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'9\' QFormat=\'true\' Name=\'heading 8\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'9\' QFormat=\'true\' Name=\'heading 9\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'39\' Name=\'toc 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'39\' Name=\'toc 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'39\' Name=\'toc 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'39\' Name=\'toc 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'39\' Name=\'toc 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'39\' Name=\'toc 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'39\' Name=\'toc 7\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'39\' Name=\'toc 8\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'39\' Name=\'toc 9\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'35\' QFormat=\'true\' Name=\'caption\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'10\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Title\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'1\' Name=\'Default Paragraph Font\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'11\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Subtitle\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'22\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Strong\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'20\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Emphasis\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'59\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Table Grid\'/>"
				+ "  <w:LsdException Locked=\'false\' UnhideWhenUsed=\'false\' Name=\'Placeholder Text\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'1\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'No Spacing\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'60\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Shading\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'61\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light List\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'62\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Grid\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'63\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'64\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'65\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'66\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'67\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'68\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'69\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'70\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Dark List\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'71\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Shading\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'72\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful List\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'73\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Grid\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'60\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Shading Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'61\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light List Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'62\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Grid Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'63\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 1 Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'64\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 2 Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'65\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 1 Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' UnhideWhenUsed=\'false\' Name=\'Revision\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'34\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'List Paragraph\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'29\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Quote\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'30\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Intense Quote\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'66\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 2 Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'67\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 1 Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'68\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 2 Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'69\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 3 Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'70\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Dark List Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'71\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Shading Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'72\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful List Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'73\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Grid Accent 1\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'60\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Shading Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'61\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light List Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'62\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Grid Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'63\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 1 Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'64\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 2 Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'65\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 1 Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'66\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 2 Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'67\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 1 Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'68\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 2 Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'69\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 3 Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'70\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Dark List Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'71\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Shading Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'72\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful List Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'73\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Grid Accent 2\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'60\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Shading Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'61\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light List Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'62\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Grid Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'63\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 1 Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'64\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 2 Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'65\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 1 Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'66\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 2 Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'67\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 1 Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'68\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 2 Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'69\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 3 Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'70\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Dark List Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'71\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Shading Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'72\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful List Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'73\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Grid Accent 3\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'60\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Shading Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'61\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light List Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'62\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Grid Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'63\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 1 Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'64\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 2 Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'65\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 1 Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'66\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 2 Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'67\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 1 Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'68\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 2 Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'69\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 3 Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'70\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Dark List Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'71\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Shading Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'72\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful List Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'73\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Grid Accent 4\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'60\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Shading Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'61\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light List Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'62\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Grid Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'63\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 1 Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'64\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 2 Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'65\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 1 Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'66\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 2 Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'67\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 1 Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'68\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 2 Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'69\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 3 Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'70\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Dark List Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'71\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Shading Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'72\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful List Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'73\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Grid Accent 5\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'60\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Shading Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'61\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light List Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'62\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Light Grid Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'63\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 1 Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'64\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Shading 2 Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'65\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 1 Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'66\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium List 2 Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'67\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 1 Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'68\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 2 Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'69\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Medium Grid 3 Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'70\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Dark List Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'71\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Shading Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'72\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful List Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'73\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' Name=\'Colorful Grid Accent 6\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'19\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Subtle Emphasis\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'21\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Intense Emphasis\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'31\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Subtle Reference\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'32\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Intense Reference\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'33\' SemiHidden=\'false\'"
				+ "   UnhideWhenUsed=\'false\' QFormat=\'true\' Name=\'Book Title\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'37\' Name=\'Bibliography\'/>"
				+ "  <w:LsdException Locked=\'false\' Priority=\'39\' QFormat=\'true\' Name=\'TOC Heading\'/>"
				+ " </w:LatentStyles>"
				+ "</xml><![endif]-->"
				+ "<style>"
				+ "<!--"
				+ " /* Font Definitions */"
				+ " @font-face"
				+ "	{font-family:宋体;"
				+ "	panose-1:2 1 6 0 3 1 1 1 1 1;"
				+ "	mso-font-alt:SimSun;"
				+ "	mso-font-charset:134;"
				+ "	mso-generic-font-family:auto;"
				+ "	mso-font-pitch:variable;"
				+ "	mso-font-signature:3 680460288 22 0 262145 0;}"
				+ "@font-face"
				+ "	{font-family:黑体;"
				+ "	panose-1:2 1 6 9 6 1 1 1 1 1;"
				+ "	mso-font-alt:SimHei;"
				+ "	mso-font-charset:134;"
				+ "	mso-generic-font-family:modern;"
				+ "	mso-font-pitch:fixed;"
				+ "	mso-font-signature:-2147482945 953122042 22 0 262145 0;}"
				+ "@font-face"
				+ "	{font-family:\'Cambria Math\';"
				+ "	panose-1:2 4 5 3 5 4 6 3 2 4;"
				+ "	mso-font-charset:0;"
				+ "	mso-generic-font-family:roman;"
				+ "	mso-font-pitch:variable;"
				+ "	mso-font-signature:-536870145 1107305727 0 0 415 0;}"
				+ "@font-face"
				+ "	{font-family:Calibri;"
				+ "	panose-1:2 15 5 2 2 2 4 3 2 4;"
				+ "	mso-font-charset:0;"
				+ "	mso-generic-font-family:swiss;"
				+ "	mso-font-pitch:variable;"
				+ "	mso-font-signature:-520092929 1073786111 9 0 415 0;}"
				+ "@font-face"
				+ "	{font-family:\'宋体\';"
				+ "	panose-1:2 1 6 0 3 1 1 1 1 1;"
				+ "	mso-font-charset:134;"
				+ "	mso-generic-font-family:auto;"
				+ "	mso-font-pitch:variable;"
				+ "	mso-font-signature:3 680460288 22 0 262145 0;}"
				+ "@font-face"
				+ "	{font-family:\'黑体\';"
				+ "	panose-1:2 1 6 9 6 1 1 1 1 1;"
				+ "	mso-font-charset:134;"
				+ "	mso-generic-font-family:modern;"
				+ "	mso-font-pitch:fixed;"
				+ "	mso-font-signature:-2147482945 953122042 22 0 262145 0;}"
				+ " /* Style Definitions */"
				+ " p.MsoNormal, li.MsoNormal, div.MsoNormal"
				+ "	{mso-style-unhide:no;"
				+ "	mso-style-qformat:yes;"
				+ "	mso-style-parent:\'\';"
				+ "	margin:0cm;"
				+ "	margin-bottom:.0001pt;"
				+ "	text-align:justify;"
				+ "	text-justify:inter-ideograph;"
				+ "	mso-pagination:none;"
				+ "	font-size:10.5pt;"
				+ "	mso-bidi-font-size:11.0pt;"
				+ "	font-family:\'Calibri\',\'sans-serif\';"
				+ "	mso-ascii-font-family:Calibri;"
				+ "	mso-ascii-theme-font:minor-latin;"
				+ "	mso-fareast-font-family:宋体;"
				+ "	mso-fareast-theme-font:minor-fareast;"
				+ "	mso-hansi-font-family:Calibri;"
				+ "	mso-hansi-theme-font:minor-latin;"
				+ "	mso-bidi-font-family:\'Times New Roman\';"
				+ "	mso-bidi-theme-font:minor-bidi;"
				+ "	mso-font-kerning:1.0pt;}"
				+ "p.MsoHeader, li.MsoHeader, div.MsoHeader"
				+ "	{mso-style-noshow:yes;"
				+ "	mso-style-priority:99;"
				+ "	mso-style-link:\'页眉 Char\';"
				+ "	margin:0cm;"
				+ "	margin-bottom:.0001pt;"
				+ "	text-align:center;"
				+ "	mso-pagination:none;"
				+ "	tab-stops:center 207.65pt right 415.3pt;"
				+ "	layout-grid-mode:char;"
				+ "	border:none;"
				+ "	mso-border-bottom-alt:solid windowtext .75pt;"
				+ "	padding:0cm;"
				+ "	mso-padding-alt:0cm 0cm 1.0pt 0cm;"
				+ "	font-size:9.0pt;"
				+ "	font-family:\'Calibri\',\'sans-serif\';"
				+ "	mso-ascii-font-family:Calibri;"
				+ "	mso-ascii-theme-font:minor-latin;"
				+ "	mso-fareast-font-family:宋体;"
				+ "	mso-fareast-theme-font:minor-fareast;"
				+ "	mso-hansi-font-family:Calibri;"
				+ "	mso-hansi-theme-font:minor-latin;"
				+ "	mso-bidi-font-family:\'Times New Roman\';"
				+ "	mso-bidi-theme-font:minor-bidi;"
				+ "	mso-font-kerning:1.0pt;}"
				+ "p.MsoFooter, li.MsoFooter, div.MsoFooter"
				+ "	{mso-style-noshow:yes;"
				+ "	mso-style-priority:99;"
				+ "	mso-style-link:\'页脚 Char\';"
				+ "	margin:0cm;"
				+ "	margin-bottom:.0001pt;"
				+ "	mso-pagination:none;"
				+ "	tab-stops:center 207.65pt right 415.3pt;"
				+ "	layout-grid-mode:char;"
				+ "	font-size:9.0pt;"
				+ "	font-family:\'Calibri\',\'sans-serif\';"
				+ "	mso-ascii-font-family:Calibri;"
				+ "	mso-ascii-theme-font:minor-latin;"
				+ "	mso-fareast-font-family:宋体;"
				+ "	mso-fareast-theme-font:minor-fareast;"
				+ "	mso-hansi-font-family:Calibri;"
				+ "	mso-hansi-theme-font:minor-latin;"
				+ "	mso-bidi-font-family:\'Times New Roman\';"
				+ "	mso-bidi-theme-font:minor-bidi;"
				+ "	mso-font-kerning:1.0pt;}"
				+ "p.MsoAcetate, li.MsoAcetate, div.MsoAcetate"
				+ "	{mso-style-noshow:yes;"
				+ "	mso-style-priority:99;"
				+ "	mso-style-link:\'批注框文本 Char\';"
				+ "	margin:0cm;"
				+ "	margin-bottom:.0001pt;"
				+ "	text-align:justify;"
				+ "	text-justify:inter-ideograph;"
				+ "	mso-pagination:none;"
				+ "	font-size:9.0pt;"
				+ "	font-family:\'Calibri\',\'sans-serif\';"
				+ "	mso-ascii-font-family:Calibri;"
				+ "	mso-ascii-theme-font:minor-latin;"
				+ "	mso-fareast-font-family:宋体;"
				+ "	mso-fareast-theme-font:minor-fareast;"
				+ "	mso-hansi-font-family:Calibri;"
				+ "	mso-hansi-theme-font:minor-latin;"
				+ "	mso-bidi-font-family:\'Times New Roman\';"
				+ "	mso-bidi-theme-font:minor-bidi;"
				+ "	mso-font-kerning:1.0pt;}"
				+ "p.MsoNoSpacing, li.MsoNoSpacing, div.MsoNoSpacing"
				+ "	{mso-style-priority:1;"
				+ "	mso-style-unhide:no;"
				+ "	mso-style-qformat:yes;"
				+ "	mso-style-parent:\'\';"
				+ "	margin:0cm;"
				+ "	margin-bottom:.0001pt;"
				+ "	text-align:justify;"
				+ "	text-justify:inter-ideograph;"
				+ "	mso-pagination:none;"
				+ "	font-size:10.5pt;"
				+ "	mso-bidi-font-size:11.0pt;"
				+ "	font-family:\'Calibri\',\'sans-serif\';"
				+ "	mso-ascii-font-family:Calibri;"
				+ "	mso-ascii-theme-font:minor-latin;"
				+ "	mso-fareast-font-family:宋体;"
				+ "	mso-fareast-theme-font:minor-fareast;"
				+ "	mso-hansi-font-family:Calibri;"
				+ "	mso-hansi-theme-font:minor-latin;"
				+ "	mso-bidi-font-family:\'Times New Roman\';"
				+ "	mso-bidi-theme-font:minor-bidi;"
				+ "	mso-font-kerning:1.0pt;}"
				+ "span.Char"
				+ "	{mso-style-name:\'页眉 Char\';"
				+ "	mso-style-noshow:yes;"
				+ "	mso-style-priority:99;"
				+ "	mso-style-unhide:no;"
				+ "	mso-style-locked:yes;"
				+ "	mso-style-link:页眉;"
				+ "	mso-ansi-font-size:9.0pt;"
				+ "	mso-bidi-font-size:9.0pt;}"
				+ "span.Char0"
				+ "	{mso-style-name:\'页脚 Char\';"
				+ "	mso-style-noshow:yes;"
				+ "	mso-style-priority:99;"
				+ "	mso-style-unhide:no;"
				+ "	mso-style-locked:yes;"
				+ "	mso-style-link:页脚;"
				+ "	mso-ansi-font-size:9.0pt;"
				+ "	mso-bidi-font-size:9.0pt;}"
				+ "span.Char1"
				+ "	{mso-style-name:\'批注框文本 Char\';"
				+ "	mso-style-noshow:yes;"
				+ "	mso-style-priority:99;"
				+ "	mso-style-unhide:no;"
				+ "	mso-style-locked:yes;"
				+ "	mso-style-link:批注框文本;"
				+ "	mso-ansi-font-size:9.0pt;"
				+ "	mso-bidi-font-size:9.0pt;}"
				+ ".MsoChpDefault"
				+ "	{mso-style-type:export-only;"
				+ "	mso-default-props:yes;"
				+ "	mso-bidi-font-family:\'Times New Roman\';"
				+ "	mso-bidi-theme-font:minor-bidi;}"
				+ " /* Page Definitions */"
				+ " @page"
				+ "	{mso-page-border-surround-header:no;"
				+ "	mso-page-border-surround-footer:no;"
				+ "	mso-footnote-separator:url(\'"
				+ path
				+ "/html/header.htm\') fs;"
				+ "	mso-footnote-continuation-separator:url(\'"
				+ path
				+ "/html/header.htm\') fcs;"
				+ "	mso-endnote-separator:url(\'"
				+ path
				+ "/html/header.htm\') es;"
				+ "	mso-endnote-continuation-separator:url(\'"
				+ path
				+ "/html/header.htm\') ecs;}"
				+ "@page Section1"
				+ "	{size:595.3pt 841.9pt;"
				+ "	margin:53.85pt 51.05pt 53.85pt 51.05pt;"
				+ "	mso-header-margin:22.7pt;"
				+ "	mso-footer-margin:22.7pt;"
				+ "	mso-paper-source:0;"
				+ "	layout-grid:15.6pt;}"
				+ "div.Section1"
				+ "	{page:Section1;}"
				+ "-->"
				+ "</style>"
				+ "<!--[if gte mso 10]>"
				+ "<style>"
				+ " /* Style Definitions */"
				+ " table.MsoNormalTable"
				+ "	{mso-style-name:普通表格;"
				+ "	mso-tstyle-rowband-size:0;"
				+ "	mso-tstyle-colband-size:0;"
				+ "	mso-style-noshow:yes;"
				+ "	mso-style-priority:99;"
				+ "	mso-style-qformat:yes;"
				+ "	mso-style-parent:\'\';"
				+ "	mso-padding-alt:0cm 5.4pt 0cm 5.4pt;"
				+ "	mso-para-margin:0cm;"
				+ "	mso-para-margin-bottom:.0001pt;"
				+ "	mso-pagination:widow-orphan;"
				+ "	font-size:10.5pt;"
				+ "	mso-bidi-font-size:11.0pt;"
				+ "	font-family:\'Calibri\',\'sans-serif\';"
				+ "	mso-ascii-font-family:Calibri;"
				+ "	mso-ascii-theme-font:minor-latin;"
				+ "	mso-hansi-font-family:Calibri;"
				+ "	mso-hansi-theme-font:minor-latin;"
				+ "	mso-bidi-font-family:\'Times New Roman\';"
				+ "	mso-bidi-theme-font:minor-bidi;"
				+ "	mso-font-kerning:1.0pt;}"
				+ "table.MsoTableGrid"
				+ "	{mso-style-name:网格型;"
				+ "	mso-tstyle-rowband-size:0;"
				+ "	mso-tstyle-colband-size:0;"
				+ "	mso-style-priority:59;"
				+ "	mso-style-unhide:no;"
				+ "	border:solid black 1.0pt;"
				+ "	mso-border-themecolor:text1;"
				+ "	mso-border-alt:solid black .5pt;"
				+ "	mso-border-themecolor:text1;"
				+ "	mso-padding-alt:0cm 5.4pt 0cm 5.4pt;"
				+ "	mso-border-insideh:.5pt solid black;"
				+ "	mso-border-insideh-themecolor:text1;"
				+ "	mso-border-insidev:.5pt solid black;"
				+ "	mso-border-insidev-themecolor:text1;"
				+ "	mso-para-margin:0cm;"
				+ "	mso-para-margin-bottom:.0001pt;"
				+ "	mso-pagination:widow-orphan;"
				+ "	font-size:10.5pt;"
				+ "	mso-bidi-font-size:11.0pt;"
				+ "	font-family:\'Calibri\',\'sans-serif\';"
				+ "	mso-ascii-font-family:Calibri;"
				+ "	mso-ascii-theme-font:minor-latin;"
				+ "	mso-hansi-font-family:Calibri;"
				+ "	mso-hansi-theme-font:minor-latin;"
				+ "	mso-bidi-font-family:\'Times New Roman\';"
				+ "	mso-bidi-theme-font:minor-bidi;"
				+ "	mso-font-kerning:1.0pt;}"
				+ "</style>"
				+ "<![endif]--><!--[if gte mso 9]><xml>"
				+ " <o:shapedefaults v:ext=\'edit\' spidmax=\'25602\'/>"
				+ "</xml><![endif]--><!--[if gte mso 9]><xml>"
				+ " <o:shapelayout v:ext=\'edit\'>"
				+ "  <o:idmap v:ext=\'edit\' data=\'2\'/>"
				+ " </o:shapelayout></xml><![endif]-->"
				+ "</head>"
				+ "<body lang=ZH-CN style=\'tab-interval:21.0pt;text-justify-trim:punctuation\'>"
				+ content + "</body>" + "</html>";
		return content;
	}
}
