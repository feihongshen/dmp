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
import cn.explink.service.validator.CwbValidator;
import cn.explink.service.validator.OrderCwbValidator;
import cn.explink.service.validator.PaybackFeeShouldBeZeroForPeisong;
import cn.explink.service.validator.PaybackFeeValidator;
import cn.explink.service.validator.ReceivableFeeShouldBeZeroForShangementui;
import cn.explink.service.validator.ReceivableFeeValidator;
import cn.explink.service.validator.SendCargoNumShouldAboveZero;
import cn.explink.service.validator.ShipCwbValidator;
import cn.explink.service.validator.TransCwbValidator;

@Component
public class ImportValidationManager {

	@Autowired
	private CwbValidator cwbValidator;

	@Autowired
	private ShipCwbValidator shipCwbValidator;

	@Autowired
	private OrderCwbValidator orderCwbValidator;

	@Autowired
	private TransCwbValidator transCwbValidator;

	@Autowired
	private ReceivableFeeValidator receivableFeeValidator;

	@Autowired
	private ReceivableFeeShouldBeZeroForShangementui receivableFeeShouldBeZeroForShangementui;

	@Autowired
	private PaybackFeeValidator paybackFeeValidator;

	@Autowired
	private PaybackFeeShouldBeZeroForPeisong paybackFeeShouldBeZeroForPeisong;

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

	public List<CwbOrderValidator> getValidators(long customerid) {
		ArrayList<CwbOrderValidator> arrayList = new ArrayList<CwbOrderValidator>();
		return arrayList;
	}

	public List<CwbOrderValidator> getCommonValidators() {
		ArrayList<CwbOrderValidator> arrayList = new ArrayList<CwbOrderValidator>();
		arrayList.add(cwbValidator);
		arrayList.add(receivableFeeShouldBeZeroForShangementui);
		arrayList.add(paybackFeeShouldBeZeroForPeisong);
		return arrayList;
	}

	public List<CwbOrderValidator> getVailidators(ExcelColumnSet excelColumnSet) {

		ArrayList<CwbOrderValidator> arrayList = new ArrayList<CwbOrderValidator>();

		if (excelColumnSet.getShipcwbindex() != 0) {
			arrayList.add(shipCwbValidator);
		}
		// TODO 根据需求ordercwb不需要了
		// if (excelColumnSet.getOrdercwbindex() != 0) {
		// arrayList.add(orderCwbValidator);
		// }
		if (excelColumnSet.getTranscwbindex() != 0) {
			arrayList.add(transCwbValidator);
		}

		if (excelColumnSet.getReceivablefeeindex() != 0) {
			arrayList.add(receivableFeeValidator);
		}
		if (excelColumnSet.getPaybackfeeindex() != 0) {
			arrayList.add(paybackFeeValidator);
		}
		if (excelColumnSet.getCargorealweightindex() != 0) {
			arrayList.add(cargoRealWeigghtShouldAboveZero);
		}

		if (excelColumnSet.getCargoamountindex() != 0) {
			arrayList.add(cargoAmountShouldAboveZero);
		}
		if (excelColumnSet.getBackcargoamountindex() != 0) {
			arrayList.add(backCargoAmountShouldAboveZero);
		}
		if (excelColumnSet.getSendcargonumindex() != 0) {
			arrayList.add(sendCargoNumShouldAboveZero);
		}
		if (excelColumnSet.getBackcargonumindex() != 0) {
			arrayList.add(backCargoNumShouldAboveZero);
		}
		return arrayList;
	}
}
