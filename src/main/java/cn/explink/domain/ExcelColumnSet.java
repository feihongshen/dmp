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
	int isaudit;
	int commoncwb;
	int emaildateindex;/* email时间，表格中有字段的则按照表格字段为准，没有的话按照导入时设置的时间为准，否则为当前时间 */
	int excelbranchindex;/* 表格中的指定站点在导入时直接进行指定 */
	int exceldeliverindex;/* 表格中指定人员在导入时直接进行指定 */
	int getmobileflag;/* 判断是不是要从手机中获得手机号，0为不获取 1为获取 */
	int shipcwbindex;/*-供货商的运单号*/
	int accountareaindex;/* 结算区域 */
	int warehousenameindex;/* 发货仓库 */
	int commonnumberindex;/* 承运商编号 */
	String updatetime;
	long updateuserid;/* 最后一次更新这条记录的用户id */
	String updateusername;/* 最后一次更新这条记录的用户姓名 */
	int modelnameindex;// 模版名称

	int remark1index;
	int remark2index;
	int remark3index;
	int remark4index;
	int remark5index;
	int paywayindex;

	// ////2012-10-26 tmall对接新增 ////////

	int cargovolumeindex; // 货物体积
	int consignoraddressindex; // 取件地址
	int tmall_notifyidindex; // tmall发送数据唯一标示
	int multi_shipcwbindex; // 多个运单号逗号隔开
	
	/**订单导入模板新增退货地址，及商家退货号 add by chunlei05.li 2016/8/22*/
	private int returnnoindex;
	
	private int returnaddressindex;
	
	public int getMpsallarrivedflagindex() {
		return mpsallarrivedflagindex;
	}

	public void setMpsallarrivedflagindex(int mpsallarrivedflagindex) {
		this.mpsallarrivedflagindex = mpsallarrivedflagindex;
	}

	public int getIsmpsflagindex() {
		return ismpsflagindex;
	}

	public void setIsmpsflagindex(int ismpsflagindex) {
		this.ismpsflagindex = ismpsflagindex;
	}

	int mpsallarrivedflagindex; //'最后一箱标识:1表示最后一箱；0默认';
	int ismpsflagindex; //是否一票多件（集包模式）：0默认；1是一票多件'; 
	

	


	// /////唯品会会应收运费////////
	int cwbordertypeidindex;// 订单类型
	
	int vipclubindex; //团购

	public int getCwbordertypeidindex() {
		return cwbordertypeidindex;
	}

	public void setCwbordertypeidindex(int cwbordertypeidindex) {
		this.cwbordertypeidindex = cwbordertypeidindex;
	}

	int shouldfareindex;// 应收运费

	public int getShouldfareindex() {
		return shouldfareindex;
	}

	public void setShouldfareindex(int shouldfareindex) {
		this.shouldfareindex = shouldfareindex;
	}

	public int getCommoncwb() {
		return commoncwb;
	}

	public void setCommoncwb(int commoncwb) {
		this.commoncwb = commoncwb;
	}

	public int getIsaudit() {
		return isaudit;
	}

	public void setIsaudit(int isaudit) {
		this.isaudit = isaudit;
	}

	public int getMulti_shipcwbindex() {
		return multi_shipcwbindex;
	}

	public void setMulti_shipcwbindex(int multi_shipcwbindex) {
		this.multi_shipcwbindex = multi_shipcwbindex;
	}

	public int getTmall_notifyidindex() {
		return tmall_notifyidindex;
	}

	public void setTmall_notifyidindex(int tmall_notifyidindex) {
		this.tmall_notifyidindex = tmall_notifyidindex;
	}

	public int getConsignoraddressindex() {
		return consignoraddressindex;
	}

	public void setConsignoraddressindex(int consignoraddressindex) {
		this.consignoraddressindex = consignoraddressindex;
	}

	public int getCargovolumeindex() {
		return cargovolumeindex;
	}

	public void setCargovolumeindex(int cargovolumeindex) {
		this.cargovolumeindex = cargovolumeindex;
	}

	public String getUpdateusername() {
		return updateusername;
	}

	public void setUpdateusername(String updateusername) {
		this.updateusername = updateusername;
	}

	public long getUpdateuserid() {
		return updateuserid;
	}

	public void setUpdateuserid(long updateuserid) {
		this.updateuserid = updateuserid;
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

	public int getCommonnumberindex() {
		return commonnumberindex;
	}

	public void setCommonnumberindex(int commonnumberindex) {
		this.commonnumberindex = commonnumberindex;
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

	public int getModelnameindex() {
		return modelnameindex;
	}

	public void setModelnameindex(int modelnameindex) {
		this.modelnameindex = modelnameindex;
	}

	public int getRemark1index() {
		return remark1index;
	}

	public void setRemark1index(int remark1index) {
		this.remark1index = remark1index;
	}

	public int getRemark2index() {
		return remark2index;
	}

	public void setRemark2index(int remark2index) {
		this.remark2index = remark2index;
	}

	public int getRemark3index() {
		return remark3index;
	}

	public void setRemark3index(int remark3index) {
		this.remark3index = remark3index;
	}

	public int getRemark4index() {
		return remark4index;
	}

	public void setRemark4index(int remark4index) {
		this.remark4index = remark4index;
	}

	public int getRemark5index() {
		return remark5index;
	}

	public void setRemark5index(int remark5index) {
		this.remark5index = remark5index;
	}

	public int getPaywayindex() {
		return paywayindex;
	}

	public void setPaywayindex(int paywayindex) {
		this.paywayindex = paywayindex;
	}

	public int getVipclubindex() {
		return vipclubindex;
	}

	public void setVipclubindex(int vipclubindex) {
		this.vipclubindex = vipclubindex;
	}

	public int getReturnnoindex() {
		return returnnoindex;
	}

	public void setReturnnoindex(int returnnoindex) {
		this.returnnoindex = returnnoindex;
	}

	public int getReturnaddressindex() {
		return returnaddressindex;
	}

	public void setReturnaddressindex(int returnaddressindex) {
		this.returnaddressindex = returnaddressindex;
	}
}
