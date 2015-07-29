package cn.explink.domain;

import java.math.BigDecimal;

public class SalaryGather {
	/**主键id**/
	private long id;
	/**批次编号**/
	private String batchid;
	/**站点id**/
	private long branchid;
	/**姓名id**/
	private long userid;
	/**姓名**/
	private String	realname;
	/**身份证号码**/
	private String	idcard;
	/**结算单量**/
	private long accountSingle;
	/**基本工资**/
	private BigDecimal	salarybasic;
	/**岗位工资**/
	private BigDecimal	salaryjob;
	/**绩效奖金**/
	private BigDecimal	pushcash;
	/**工龄**/
	private BigDecimal	agejob;
	/**固定补贴**/
	private BigDecimal	bonusfixed;
	/**话费补贴**/
	private BigDecimal	bonusphone;
	/**高温寒冷补贴**/
	private BigDecimal	bonusweather;
	/**扣款撤销**/
	private BigDecimal	penalizecancel;
	/**其它补贴**/
	private BigDecimal	bonusother1;
	/**其它补贴2**/
	private BigDecimal	bonusother2;
	/**其它补贴3**/
	private BigDecimal	bonusother3;
	/**其它补贴4**/
	private BigDecimal	bonusother4;
	/**其它补贴5**/
	private BigDecimal	bonusother5;
	/**其它补贴6**/
	private BigDecimal	bonusother6;
	/**加班**/
	private BigDecimal	overtimework;
	/**考勤扣款**/
	private BigDecimal	attendance;
	/**个人社保扣款**/
	private BigDecimal	security;
	/**个人公积金扣款**/
	private BigDecimal	gongjijin;
	/**违纪违规扣罚**/
	private BigDecimal	foul;
	/**货物赔偿**/
	private BigDecimal	goods;
	/**宿舍费用**/
	private BigDecimal	dorm;
	/**其它扣罚**/
	private BigDecimal	penalizeother1;
	/**其它扣罚2**/
	private BigDecimal	penalizeother2;
	/**其它扣罚3**/
	private BigDecimal	penalizeother3;
	/**其它扣罚4**/
	private BigDecimal	penalizeother4;
	/**其它扣罚5**/
	private BigDecimal	penalizeother5;
	/**其它扣罚6**/
	private BigDecimal	penalizeother6;
	/**货物预付款**/
	private BigDecimal	imprestgoods;
	/**其它预付款**/
	private BigDecimal	imprestother1;
	/**其它预付款2**/
	private BigDecimal	imprestother2;
	/**其它预付款3**/
	private BigDecimal	imprestother3;
	/**其它预付款4**/
	private BigDecimal	imprestother4;
	/**其它预付款5**/
	private BigDecimal	imprestother5;
	/**其它预付款6**/
	private BigDecimal	imprestother6;
	/**应发工资**/
	private BigDecimal	salaryaccrual;
	/**个税**/
	private BigDecimal	tax;
	/**实发工资**/
	private BigDecimal	salary;
	/**导入人员**/
	private long	creuserid;
	/**导入标识**/
	private long	importflag;
	/**岗位津贴**/
	private BigDecimal	jobpush;
	/**住房补贴**/
	private BigDecimal	bonusroom;
	/**全勤补贴**/
	private BigDecimal	bonusallday;
	/**餐费补贴**/
	private BigDecimal	bonusfood;
	/**交通补贴**/
	private BigDecimal	bonustraffic;
	/**车子租用**/
	private BigDecimal	carrent;
	/**车子维修**/
	private BigDecimal	carmaintain;
	/**油/电费用**/
	private BigDecimal	carfuel;
	/**扣款撤销(导入)**/
	private BigDecimal	penalizecancel_import;
	/**违纪扣款扣罚(导入)**/
	private BigDecimal	foul_import;
	/**提成**/
	private BigDecimal	salarypush;
	
	/**站点名=====用于显示**/
	private String branchname;
	
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return batchid
	 */
	public String getBatchid() {
		return batchid;
	}
	/**
	 * @param batchid
	 */
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	/**
	 * @return the realname
	 */
	public String getRealname() {
		return this.realname;
	}
	/**
	 * @param realname the realname to set
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}
	/**
	 * @return the idcard
	 */
	public String getIdcard() {
		return this.idcard;
	}
	/**
	 * @param idcard the idcard to set
	 */
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public long getAccountSingle() {
		return accountSingle;
	}
	public void setAccountSingle(long accountSingle) {
		this.accountSingle = accountSingle;
	}
	/**
	 * @return the salarybasic
	 */
	public BigDecimal getSalarybasic() {
		return this.salarybasic;
	}
	/**
	 * @param salarybasic the salarybasic to set
	 */
	public void setSalarybasic(BigDecimal salarybasic) {
		this.salarybasic = salarybasic;
	}
	/**
	 * @return the salaryjob
	 */
	public BigDecimal getSalaryjob() {
		return this.salaryjob;
	}
	/**
	 * @param salaryjob the salaryjob to set
	 */
	public void setSalaryjob(BigDecimal salaryjob) {
		this.salaryjob = salaryjob;
	}
	
	public BigDecimal getPushcash() {
		return pushcash;
	}
	public void setPushcash(BigDecimal pushcash) {
		this.pushcash = pushcash;
	}
	/**
	 * @return the agejob
	 */
	public BigDecimal getAgejob() {
		return this.agejob;
	}
	/**
	 * @param agejob the agejob to set
	 */
	public void setAgejob(BigDecimal agejob) {
		this.agejob = agejob;
	}
	/**
	 * @return the bonusfixed
	 */
	public BigDecimal getBonusfixed() {
		return this.bonusfixed;
	}
	/**
	 * @param bonusfixed the bonusfixed to set
	 */
	public void setBonusfixed(BigDecimal bonusfixed) {
		this.bonusfixed = bonusfixed;
	}

	/**
	 * @return the bonusphone
	 */
	public BigDecimal getBonusphone() {
		return this.bonusphone;
	}
	/**
	 * @param bonusphone the bonusphone to set
	 */
	public void setBonusphone(BigDecimal bonusphone) {
		this.bonusphone = bonusphone;
	}
	/**
	 * @return the bonusweather
	 */
	public BigDecimal getBonusweather() {
		return this.bonusweather;
	}
	/**
	 * @param bonusweather the bonusweather to set
	 */
	public void setBonusweather(BigDecimal bonusweather) {
		this.bonusweather = bonusweather;
	}
	/**
	 * @return the penalizecancel
	 */
	public BigDecimal getPenalizecancel() {
		return this.penalizecancel;
	}
	/**
	 * @param penalizecancel the penalizecancel to set
	 */
	public void setPenalizecancel(BigDecimal penalizecancel) {
		this.penalizecancel = penalizecancel;
	}
	/**
	 * @return the bonusother1
	 */
	public BigDecimal getBonusother1() {
		return this.bonusother1;
	}
	/**
	 * @param bonusother1 the bonusother1 to set
	 */
	public void setBonusother1(BigDecimal bonusother1) {
		this.bonusother1 = bonusother1;
	}
	/**
	 * @return the bonusother2
	 */
	public BigDecimal getBonusother2() {
		return this.bonusother2;
	}
	/**
	 * @param bonusother2 the bonusother2 to set
	 */
	public void setBonusother2(BigDecimal bonusother2) {
		this.bonusother2 = bonusother2;
	}
	/**
	 * @return the bonusother3
	 */
	public BigDecimal getBonusother3() {
		return this.bonusother3;
	}
	/**
	 * @param bonusother3 the bonusother3 to set
	 */
	public void setBonusother3(BigDecimal bonusother3) {
		this.bonusother3 = bonusother3;
	}
	/**
	 * @return the bonusother4
	 */
	public BigDecimal getBonusother4() {
		return this.bonusother4;
	}
	/**
	 * @param bonusother4 the bonusother4 to set
	 */
	public void setBonusother4(BigDecimal bonusother4) {
		this.bonusother4 = bonusother4;
	}
	/**
	 * @return the bonusother5
	 */
	public BigDecimal getBonusother5() {
		return this.bonusother5;
	}
	/**
	 * @param bonusother5 the bonusother5 to set
	 */
	public void setBonusother5(BigDecimal bonusother5) {
		this.bonusother5 = bonusother5;
	}
	/**
	 * @return the bonusother6
	 */
	public BigDecimal getBonusother6() {
		return this.bonusother6;
	}
	/**
	 * @param bonusother6 the bonusother6 to set
	 */
	public void setBonusother6(BigDecimal bonusother6) {
		this.bonusother6 = bonusother6;
	}
	/**
	 * @return the overtimework
	 */
	public BigDecimal getOvertimework() {
		return this.overtimework;
	}
	/**
	 * @param overtimework the overtimework to set
	 */
	public void setOvertimework(BigDecimal overtimework) {
		this.overtimework = overtimework;
	}
	/**
	 * @return the attendance
	 */
	public BigDecimal getAttendance() {
		return this.attendance;
	}
	/**
	 * @param attendance the attendance to set
	 */
	public void setAttendance(BigDecimal attendance) {
		this.attendance = attendance;
	}
	/**
	 * @return the security
	 */
	public BigDecimal getSecurity() {
		return this.security;
	}
	/**
	 * @param security the security to set
	 */
	public void setSecurity(BigDecimal security) {
		this.security = security;
	}
	/**
	 * @return the gongjijin
	 */
	public BigDecimal getGongjijin() {
		return this.gongjijin;
	}
	/**
	 * @param gongjijin the gongjijin to set
	 */
	public void setGongjijin(BigDecimal gongjijin) {
		this.gongjijin = gongjijin;
	}
	/**
	 * @return the foul
	 */
	public BigDecimal getFoul() {
		return this.foul;
	}
	/**
	 * @param foul the foul to set
	 */
	public void setFoul(BigDecimal foul) {
		this.foul = foul;
	}
	/**
	 * @return the goods
	 */
	public BigDecimal getGoods() {
		return this.goods;
	}
	/**
	 * @param goods the goods to set
	 */
	public void setGoods(BigDecimal goods) {
		this.goods = goods;
	}
	/**
	 * @return the dorm
	 */
	public BigDecimal getDorm() {
		return this.dorm;
	}
	/**
	 * @param dorm the dorm to set
	 */
	public void setDorm(BigDecimal dorm) {
		this.dorm = dorm;
	}
	/**
	 * @return the penalizeother1
	 */
	public BigDecimal getPenalizeother1() {
		return this.penalizeother1;
	}
	/**
	 * @param penalizeother1 the penalizeother1 to set
	 */
	public void setPenalizeother1(BigDecimal penalizeother1) {
		this.penalizeother1 = penalizeother1;
	}
	/**
	 * @return the penalizeother2
	 */
	public BigDecimal getPenalizeother2() {
		return this.penalizeother2;
	}
	/**
	 * @param penalizeother2 the penalizeother2 to set
	 */
	public void setPenalizeother2(BigDecimal penalizeother2) {
		this.penalizeother2 = penalizeother2;
	}
	/**
	 * @return the penalizeother3
	 */
	public BigDecimal getPenalizeother3() {
		return this.penalizeother3;
	}
	/**
	 * @param penalizeother3 the penalizeother3 to set
	 */
	public void setPenalizeother3(BigDecimal penalizeother3) {
		this.penalizeother3 = penalizeother3;
	}
	/**
	 * @return the penalizeother4
	 */
	public BigDecimal getPenalizeother4() {
		return this.penalizeother4;
	}
	/**
	 * @param penalizeother4 the penalizeother4 to set
	 */
	public void setPenalizeother4(BigDecimal penalizeother4) {
		this.penalizeother4 = penalizeother4;
	}
	/**
	 * @return the penalizeother5
	 */
	public BigDecimal getPenalizeother5() {
		return this.penalizeother5;
	}
	/**
	 * @param penalizeother5 the penalizeother5 to set
	 */
	public void setPenalizeother5(BigDecimal penalizeother5) {
		this.penalizeother5 = penalizeother5;
	}
	/**
	 * @return the penalizeother6
	 */
	public BigDecimal getPenalizeother6() {
		return this.penalizeother6;
	}
	/**
	 * @param penalizeother6 the penalizeother6 to set
	 */
	public void setPenalizeother6(BigDecimal penalizeother6) {
		this.penalizeother6 = penalizeother6;
	}
	/**
	 * @return the imprestgoods
	 */
	public BigDecimal getImprestgoods() {
		return this.imprestgoods;
	}
	/**
	 * @param imprestgoods the imprestgoods to set
	 */
	public void setImprestgoods(BigDecimal imprestgoods) {
		this.imprestgoods = imprestgoods;
	}
	/**
	 * @return the imprestother1
	 */
	public BigDecimal getImprestother1() {
		return this.imprestother1;
	}
	/**
	 * @param imprestother1 the imprestother1 to set
	 */
	public void setImprestother1(BigDecimal imprestother1) {
		this.imprestother1 = imprestother1;
	}
	/**
	 * @return the imprestother2
	 */
	public BigDecimal getImprestother2() {
		return this.imprestother2;
	}
	/**
	 * @param imprestother2 the imprestother2 to set
	 */
	public void setImprestother2(BigDecimal imprestother2) {
		this.imprestother2 = imprestother2;
	}
	/**
	 * @return the imprestother3
	 */
	public BigDecimal getImprestother3() {
		return this.imprestother3;
	}
	/**
	 * @param imprestother3 the imprestother3 to set
	 */
	public void setImprestother3(BigDecimal imprestother3) {
		this.imprestother3 = imprestother3;
	}
	/**
	 * @return the imprestother4
	 */
	public BigDecimal getImprestother4() {
		return this.imprestother4;
	}
	/**
	 * @param imprestother4 the imprestother4 to set
	 */
	public void setImprestother4(BigDecimal imprestother4) {
		this.imprestother4 = imprestother4;
	}
	/**
	 * @return the imprestother5
	 */
	public BigDecimal getImprestother5() {
		return this.imprestother5;
	}
	/**
	 * @param imprestother5 the imprestother5 to set
	 */
	public void setImprestother5(BigDecimal imprestother5) {
		this.imprestother5 = imprestother5;
	}
	/**
	 * @return the imprestother6
	 */
	public BigDecimal getImprestother6() {
		return this.imprestother6;
	}
	/**
	 * @param imprestother6 the imprestother6 to set
	 */
	public void setImprestother6(BigDecimal imprestother6) {
		this.imprestother6 = imprestother6;
	}
	/**
	 * @return the salaryaccrual
	 */
	public BigDecimal getSalaryaccrual() {
		return this.salaryaccrual;
	}
	/**
	 * @param salaryaccrual the salaryaccrual to set
	 */
	public void setSalaryaccrual(BigDecimal salaryaccrual) {
		this.salaryaccrual = salaryaccrual;
	}
	/**
	 * @return the tax
	 */
	public BigDecimal getTax() {
		return this.tax;
	}
	/**
	 * @param tax the tax to set
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	/**
	 * @return the salary
	 */
	public BigDecimal getSalary() {
		return this.salary;
	}
	/**
	 * @param salary the salary to set
	 */
	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}
	/**
	 * @return the creuserid
	 */
	public long getCreuserid() {
		return this.creuserid;
	}
	/**
	 * @param creuserid the creuserid to set
	 */
	public void setCreuserid(long creuserid) {
		this.creuserid = creuserid;
	}
	/**
	 * @return the importflag
	 */
	public long getImportflag() {
		return this.importflag;
	}
	/**
	 * @param importflag the importflag to set
	 */
	public void setImportflag(long importflag) {
		this.importflag = importflag;
	}
	/**
	 * @return the jobpush
	 */
	public BigDecimal getJobpush() {
		return this.jobpush;
	}
	/**
	 * @param jobpush the jobpush to set
	 */
	public void setJobpush(BigDecimal jobpush) {
		this.jobpush = jobpush;
	}
	/**
	 * @return the bonusroom
	 */
	public BigDecimal getBonusroom() {
		return this.bonusroom;
	}
	/**
	 * @param bonusroom the bonusroom to set
	 */
	public void setBonusroom(BigDecimal bonusroom) {
		this.bonusroom = bonusroom;
	}
	/**
	 * @return the bonusallday
	 */
	public BigDecimal getBonusallday() {
		return this.bonusallday;
	}
	/**
	 * @param bonusallday the bonusallday to set
	 */
	public void setBonusallday(BigDecimal bonusallday) {
		this.bonusallday = bonusallday;
	}
	
	public BigDecimal getSalarypush() {
		return salarypush;
	}
	public void setSalarypush(BigDecimal salarypush) {
		this.salarypush = salarypush;
	}
	/**
	 * @return the bonusfood
	 */
	public BigDecimal getBonusfood() {
		return this.bonusfood;
	}
	/**
	 * @param bonusfood the bonusfood to set
	 */
	public void setBonusfood(BigDecimal bonusfood) {
		this.bonusfood = bonusfood;
	}
	/**
	 * @return the bonustraffic
	 */
	public BigDecimal getBonustraffic() {
		return this.bonustraffic;
	}
	/**
	 * @param bonustraffic the bonustraffic to set
	 */
	public void setBonustraffic(BigDecimal bonustraffic) {
		this.bonustraffic = bonustraffic;
	}
	/**
	 * @return the carrent
	 */
	public BigDecimal getCarrent() {
		return this.carrent;
	}
	/**
	 * @param carrent the carrent to set
	 */
	public void setCarrent(BigDecimal carrent) {
		this.carrent = carrent;
	}
	/**
	 * @return the carmaintain
	 */
	public BigDecimal getCarmaintain() {
		return this.carmaintain;
	}
	/**
	 * @param carmaintain the carmaintain to set
	 */
	public void setCarmaintain(BigDecimal carmaintain) {
		this.carmaintain = carmaintain;
	}
	/**
	 * @return the carfuel
	 */
	public BigDecimal getCarfuel() {
		return this.carfuel;
	}
	/**
	 * @param carfuel the carfuel to set
	 */
	public void setCarfuel(BigDecimal carfuel) {
		this.carfuel = carfuel;
	}
	/**
	 * @return the penalizecancel_import
	 */
	public BigDecimal getPenalizecancel_import() {
		return this.penalizecancel_import;
	}
	/**
	 * @param penalizecancel_import the penalizecancel_import to set
	 */
	public void setPenalizecancel_import(BigDecimal penalizecancel_import) {
		this.penalizecancel_import = penalizecancel_import;
	}
	/**
	 * @return the foul_import
	 */
	public BigDecimal getFoul_import() {
		return this.foul_import;
	}
	/**
	 * @param foul_import the foul_import to set
	 */
	public void setFoul_import(BigDecimal foul_import) {
		this.foul_import = foul_import;
	}
	
	public String getBranchname() {
		return branchname;
	}
	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}
	
	public long getBranchid() {
		return branchid;
	}
	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}
}
