package cn.explink.domain;

public class ExcelColumnSet {

	long columnid;
	long customerid;
	int cwbindex;
	int consigneenoindex;
	int consigneenameindex;
	int consigneeaddressindex;
	int consigneepostcodeindex;
	int consigneephoneindex;
	int cwbremarkindex;
	int cargorealweightindex;
	int customercommandindex;
	int cargotypeindex;
	int cargowarehouseindex;
	int cargosizeindex;
	int backcargoamountindex;
	int transwayindex;
	int sendcargonumindex;
	int backcargonumindex;
	int cwbprovinceindex;
	int cwbcityindex;
	int cwbcountyindex;
	int receivablefeeindex;
	int paybackfeeindex;
	int cargoamountindex;
	int cwbordertypeindex;
	int consigneemobileindex;
	int backcargonameindex;
	int transcwbindex;
	int sendcargonameindex;
	int destinationindex;
	int cwbdelivertypeindex;

	int emaildateindex;/* email时间，表格中有字段的则按照表格字段为准，没有的话按照导入时设置的时间为准，否则为当前时间 */
	int excelbranchindex;/* 表格中的指定站点在导入时直接进行指定 */
	int exceldeliverindex;/* 表格中指定人员在导入时直接进行指定 */
	int getmobileflag;/* 判断是不是要从手机中获得手机号，0为不获取 1为获取 */
	int shipcwbindex;/*-物流公司自己的订单编号-产生自自定义规则-可以为空*/
	int accountareaindex;/* 结算区域 */
	int warehousenameindex;/* 发货仓库 */
	int shippernameindex;/* 承运商 */
	String updatetime;
	long updateuserid;/* 最后一次更新这条记录的用户id */
	int commonnumberindex;

	public int getCommonnumberindex() {
		return commonnumberindex;
	}

	public void setCommonnumberindex(int commonnumberindex) {
		this.commonnumberindex = commonnumberindex;
	}

	public long getUpdateuserid() {
		return updateuserid;
	}

	public void setUpdateuserid(long updateuserid) {
		this.updateuserid = updateuserid;
	}

	public int getShippernameindex() {
		return shippernameindex;
	}

	public void setShippernameindex(int shippernameindex) {
		this.shippernameindex = shippernameindex;
	}

	public int getWarehousenameindex() {
		return warehousenameindex;
	}

	public void setWarehousenameindex(int warehousenameindex) {
		this.warehousenameindex = warehousenameindex;
	}

	public int getShipcwbindex() {
		return shipcwbindex;
	}

	public void setShipcwbindex(int shipcwbindex) {
		this.shipcwbindex = shipcwbindex;
	}

	public int getGetmobileflag() {
		return getmobileflag;
	}

	public void setGetmobileflag(int getmobileflag) {
		this.getmobileflag = getmobileflag;
	}

	public int getExceldeliverindex() {
		return exceldeliverindex;
	}

	public void setExceldeliverindex(int exceldeliverindex) {
		this.exceldeliverindex = exceldeliverindex;
	}

	public int getExcelbranchindex() {
		return excelbranchindex;
	}

	public void setExcelbranchindex(int excelbranchindex) {
		this.excelbranchindex = excelbranchindex;
	}

	public int getEmaildateindex() {
		return emaildateindex;
	}

	public void setEmaildateindex(int emaildateindex) {
		this.emaildateindex = emaildateindex;
	}

	public int getAccountareaindex() {
		return accountareaindex;
	}

	public void setAccountareaindex(int accountareaindex) {
		this.accountareaindex = accountareaindex;
	}

	public long getColumnid() {
		return columnid;
	}

	public void setColumnid(long columnid) {
		this.columnid = columnid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public int getCwbindex() {
		return cwbindex;
	}

	public void setCwbindex(int cwbindex) {
		this.cwbindex = cwbindex;
	}

	public int getConsigneenameindex() {
		return consigneenameindex;
	}

	public void setConsigneenameindex(int consigneenameindex) {
		this.consigneenameindex = consigneenameindex;
	}

	public int getConsigneeaddressindex() {
		return consigneeaddressindex;
	}

	public void setConsigneeaddressindex(int consigneeaddressindex) {
		this.consigneeaddressindex = consigneeaddressindex;
	}

	public int getConsigneepostcodeindex() {
		return consigneepostcodeindex;
	}

	public void setConsigneepostcodeindex(int consigneepostcodeindex) {
		this.consigneepostcodeindex = consigneepostcodeindex;
	}

	public int getConsigneephoneindex() {
		return consigneephoneindex;
	}

	public void setConsigneephoneindex(int consigneephoneindex) {
		this.consigneephoneindex = consigneephoneindex;
	}

	public int getConsigneemobileindex() {
		return consigneemobileindex;
	}

	public void setConsigneemobileindex(int consigneemobileindex) {
		this.consigneemobileindex = consigneemobileindex;
	}

	public int getCwbremarkindex() {
		return cwbremarkindex;
	}

	public void setCwbremarkindex(int cwbremarkindex) {
		this.cwbremarkindex = cwbremarkindex;
	}

	public int getSendcargonameindex() {
		return sendcargonameindex;
	}

	public void setSendcargonameindex(int sendcargonameindex) {
		this.sendcargonameindex = sendcargonameindex;
	}

	public int getBackcargonameindex() {
		return backcargonameindex;
	}

	public void setBackcargonameindex(int backcargonameindex) {
		this.backcargonameindex = backcargonameindex;
	}

	public int getCargorealweightindex() {
		return cargorealweightindex;
	}

	public void setCargorealweightindex(int cargorealweightindex) {
		this.cargorealweightindex = cargorealweightindex;
	}

	public int getReceivablefeeindex() {
		return receivablefeeindex;
	}

	public void setReceivablefeeindex(int receivablefeeindex) {
		this.receivablefeeindex = receivablefeeindex;
	}

	public int getPaybackfeeindex() {
		return paybackfeeindex;
	}

	public void setPaybackfeeindex(int paybackfeeindex) {
		this.paybackfeeindex = paybackfeeindex;
	}

	public int getConsigneenoindex() {
		return consigneenoindex;
	}

	public void setConsigneenoindex(int consigneenoindex) {
		this.consigneenoindex = consigneenoindex;
	}

	public int getCargoamountindex() {
		return cargoamountindex;
	}

	public void setCargoamountindex(int cargoamountindex) {
		this.cargoamountindex = cargoamountindex;
	}

	public int getCustomercommandindex() {
		return customercommandindex;
	}

	public void setCustomercommandindex(int customercommandindex) {
		this.customercommandindex = customercommandindex;
	}

	public int getCargotypeindex() {
		return cargotypeindex;
	}

	public void setCargotypeindex(int cargotypeindex) {
		this.cargotypeindex = cargotypeindex;
	}

	public int getCargowarehouseindex() {
		return cargowarehouseindex;
	}

	public void setCargowarehouseindex(int cargowarehouseindex) {
		this.cargowarehouseindex = cargowarehouseindex;
	}

	public int getCargosizeindex() {
		return cargosizeindex;
	}

	public void setCargosizeindex(int cargosizeindex) {
		this.cargosizeindex = cargosizeindex;
	}

	public int getBackcargoamountindex() {
		return backcargoamountindex;
	}

	public void setBackcargoamountindex(int backcargoamountindex) {
		this.backcargoamountindex = backcargoamountindex;
	}

	public int getDestinationindex() {
		return destinationindex;
	}

	public void setDestinationindex(int destinationindex) {
		this.destinationindex = destinationindex;
	}

	public int getTranswayindex() {
		return transwayindex;
	}

	public void setTranswayindex(int transwayindex) {
		this.transwayindex = transwayindex;
	}

	public int getSendcargonumindex() {
		return sendcargonumindex;
	}

	public void setSendcargonumindex(int sendcargonumindex) {
		this.sendcargonumindex = sendcargonumindex;
	}

	public int getBackcargonumindex() {
		return backcargonumindex;
	}

	public void setBackcargonumindex(int backcargonumindex) {
		this.backcargonumindex = backcargonumindex;
	}

	public int getCwbprovinceindex() {
		return cwbprovinceindex;
	}

	public void setCwbprovinceindex(int cwbprovinceindex) {
		this.cwbprovinceindex = cwbprovinceindex;
	}

	public int getCwbcityindex() {
		return cwbcityindex;
	}

	public void setCwbcityindex(int cwbcityindex) {
		this.cwbcityindex = cwbcityindex;
	}

	public int getCwbcountyindex() {
		return cwbcountyindex;
	}

	public void setCwbcountyindex(int cwbcountyindex) {
		this.cwbcountyindex = cwbcountyindex;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public int getCwbordertypeindex() {
		return cwbordertypeindex;
	}

	public void setCwbordertypeindex(int cwbordertypeindex) {
		this.cwbordertypeindex = cwbordertypeindex;
	}

	public int getCwbdelivertypeindex() {
		return cwbdelivertypeindex;
	}

	public void setCwbdelivertypeindex(int cwbdelivertypeindex) {
		this.cwbdelivertypeindex = cwbdelivertypeindex;
	}

	public int getTranscwbindex() {
		return transcwbindex;
	}

	public void setTranscwbindex(int transcwbindex) {
		this.transcwbindex = transcwbindex;
	}
}
