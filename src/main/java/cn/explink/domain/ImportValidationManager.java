package cn.explink.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.service.CwbOrderValidator;
import cn.explink.service.validator.BackCargoAmountShouldAboveZero;
import cn.explink.service.validator.BackCargoNumShouldAboveZero;
import cn.explink.service.validator.CargoAmountShouldAboveZeroAndBelowBillon;
import cn.explink.service.validator.CargoRealWeigghtShouldAboveZero;
import cn.explink.service.validator.CommonnumberValidator;
import cn.explink.service.validator.CwbDeliverTypeValidator;
import cn.explink.service.validator.CwbOrderTypeValidator;
import cn.explink.service.validator.CwbValidator;
import cn.explink.service.validator.EmaildateValidator;
import cn.explink.service.validator.ExcelDeliverShouldExists;
import cn.explink.service.validator.ExcelNextBranchShouldExists;
import cn.explink.service.validator.ModelNameValidator;
import cn.explink.service.validator.PayWayValidator;
import cn.explink.service.validator.PaybackFeeValidator;
import cn.explink.service.validator.ReceivableFeeValidator;
import cn.explink.service.validator.Remark1_5Validator;
import cn.explink.service.validator.SendCargoNumShouldAboveZero;
import cn.explink.service.validator.ShipCwbValidator;
import cn.explink.service.validator.TransCwbSendcarNumValidator;
import cn.explink.service.validator.TransCwbValidator;

@Component
public class ImportValidationManager {

	@Autowired
	private CwbValidator cwbValidator;

	@Autowired
	private ShipCwbValidator shipCwbValidator;

	@Autowired
	private TransCwbValidator transCwbValidator;

	@Autowired
	private ReceivableFeeValidator receivableFeeValidator;

	@Autowired
	private PaybackFeeValidator paybackFeeValidator;

	@Autowired
	private CargoRealWeigghtShouldAboveZero cargoRealWeigghtShouldAboveZero;

	@Autowired
	private CargoAmountShouldAboveZeroAndBelowBillon cargoAmountShouldAboveZero;

	@Autowired
	private BackCargoAmountShouldAboveZero backCargoAmountShouldAboveZero;

	@Autowired
	private SendCargoNumShouldAboveZero sendCargoNumShouldAboveZero;

	@Autowired
	private BackCargoNumShouldAboveZero backCargoNumShouldAboveZero;

	@Autowired
	private CwbOrderTypeValidator cwbOrderTypeValidator;

	@Autowired
	private CwbDeliverTypeValidator cwbDeliverTypeVilidator;

	@Autowired
	private EmaildateValidator emaildateValidator;

	@Autowired
	private ExcelDeliverShouldExists excelDeliverShouldExists;

	@Autowired
	private ExcelNextBranchShouldExists excelNextBranchShouldExists;

	@Autowired
	private CommonnumberValidator commonnumberValidator;

	@Autowired
	private ModelNameValidator modelNameValidator;
	
	@Autowired
	private TransCwbSendcarNumValidator transCwbSendcarNumValidator; //发货数量和运单数量验证

	/*
	 * @Autowired private ConsigneemobileValidator consigneemobileValidator;
	 */
	@Autowired
	private Remark1_5Validator remark1_5Validator;
	@Autowired
	private PayWayValidator payWayValidator;

	public List<CwbOrderValidator> getValidators(long customerid) {
		ArrayList<CwbOrderValidator> arrayList = new ArrayList<CwbOrderValidator>();
		return arrayList;
	}

	public List<CwbOrderValidator> getCommonValidators() {
		ArrayList<CwbOrderValidator> arrayList = new ArrayList<CwbOrderValidator>();
		arrayList.add(cwbValidator);
		/*
		 * arrayList.add(receivableFeeShouldBeZeroForShangementui);
		 * arrayList.add(paybackFeeShouldBeZeroForPeisong);
		 */
		return arrayList;
	}

	public List<CwbOrderValidator> getVailidators(ExcelColumnSet excelColumnSet) {

		ArrayList<CwbOrderValidator> arrayList = new ArrayList<CwbOrderValidator>();

		// 订单号
		arrayList.add(cwbValidator);

		// TODO 根据需求ordercwb不需要了
		// if (excelColumnSet.getOrdercwbindex() != 0) {
		// arrayList.add(orderCwbValidator);
		// }
		// 订单类型
		if (excelColumnSet.getCwbordertypeindex() != 0) {
			arrayList.add(cwbOrderTypeValidator);
		}
		// 配送类型
		if (excelColumnSet.getCwbdelivertypeindex() != 0) {
			arrayList.add(cwbDeliverTypeVilidator);
		}
		// 发货单号
		if (excelColumnSet.getTranscwbindex() != 0) {
			arrayList.add(transCwbValidator);
		}
		// 应收款
		if (excelColumnSet.getReceivablefeeindex() != 0) {
			arrayList.add(receivableFeeValidator);
		}
		// 应退款
		if (excelColumnSet.getPaybackfeeindex() != 0) {
			arrayList.add(paybackFeeValidator);
		}
		// 货物重量
		if (excelColumnSet.getCargorealweightindex() != 0) {
			arrayList.add(cargoRealWeigghtShouldAboveZero);
		}
		// 货物金额
		if (excelColumnSet.getCargoamountindex() != 0) {
			arrayList.add(cargoAmountShouldAboveZero);
		}
		// 取回商品的金额
		if (excelColumnSet.getBackcargoamountindex() != 0) {
			arrayList.add(backCargoAmountShouldAboveZero);
		}
		// 发货数量
		if (excelColumnSet.getSendcargonumindex() != 0) {
			arrayList.add(sendCargoNumShouldAboveZero);
		}
		// 取回商品数量
		if (excelColumnSet.getBackcargonumindex() != 0) {
			arrayList.add(backCargoNumShouldAboveZero);
		}
		// 发货时间
		if (excelColumnSet.getEmaildateindex() != 0) {
			arrayList.add(emaildateValidator);
		}
		// 小件员
		if (excelColumnSet.getExceldeliverindex() != 0) {
			arrayList.add(excelDeliverShouldExists);
		}
		// 派送站点
		if (excelColumnSet.getExcelbranchindex() != 0) {
			arrayList.add(excelNextBranchShouldExists);
		}
		// 指定承运商
		if (excelColumnSet.getCommonnumberindex() != 0) {
			arrayList.add(commonnumberValidator);
		}
		// 供货商运单号
		if (excelColumnSet.getShipcwbindex() != 0) {
			arrayList.add(shipCwbValidator);
		}
		// 模版名称
		if (excelColumnSet.getModelnameindex() != 0) {
			arrayList.add(modelNameValidator);
		}
		// 手机号码
		// if(excelColumnSet.getConsigneemobileindex()!=0){
		// arrayList.add(consigneemobileValidator);
		// }
		// 自定义字段的验证
		if (excelColumnSet.getRemark1index() != 0 || excelColumnSet.getRemark2index() != 0 || excelColumnSet.getRemark3index() != 0 || excelColumnSet.getRemark4index() != 0
				|| excelColumnSet.getRemark5index() != 0) {
			arrayList.add(remark1_5Validator);
		}

		// 支付方式的验证
		if (excelColumnSet.getPaywayindex() != 0) {
			arrayList.add(payWayValidator);
		}

		return arrayList;
	}
	
	/**
	 * 手工导入订单验证方法 add by vic.liang@pjbest.com 2016-08-23
	 * @param excelColumnSet
	 * @return
	 */
	public List<CwbOrderValidator> getExcelImportVailidators(ExcelColumnSet excelColumnSet) {
		List<CwbOrderValidator> list = getVailidators(excelColumnSet);
		//手工导入增加验证发货数量和运单数量 add by vic.liang@pjbest.com 2016-08-23
		if (excelColumnSet.getTranscwbindex() != 0 && excelColumnSet.getSendcargonumindex() != 0) {
			list.add(transCwbSendcarNumValidator);
		}
		return list;
	}
}
