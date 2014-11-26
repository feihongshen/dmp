package cn.explink.controller.pda;

import java.math.BigDecimal;

public class PDAXMLBody {
	private long delivercwbnum;
	private double delivercwbamount;
	private long successcwbnum;
	private double successcwbamount;
	private long backcwbnum;
	private double backcwbamount;
	private long leavedcwbnum;
	private double leavedcwbamount;
	private long nopodcwbnum;
	private double nopodcwbamount;
	private long podnoconfirmcwb;
	private double podnoconfirmcwbamount;
	private long leavednopodcwbnum;
	private double leavednopodcwbamount;
	private String cwb;
	private String cwbbranchname;
	private String cwbbranchnamewav;
	private String cwbdelivername;
	private String cwbdelivernamewav;
	private BigDecimal cwbreceivablefee;
	private String wavfilenames;

	public PDAXMLBody() {
	}

	public long getDelivercwbnum() {
		return delivercwbnum;
	}

	public void setDelivercwbnum(long delivercwbnum) {
		this.delivercwbnum = delivercwbnum;
	}

	public double getDelivercwbamount() {
		return delivercwbamount;
	}

	public void setDelivercwbamount(double delivercwbamount) {
		this.delivercwbamount = delivercwbamount;
	}

	public long getSuccesscwbnum() {
		return successcwbnum;
	}

	public void setSuccesscwbnum(long successcwbnum) {
		this.successcwbnum = successcwbnum;
	}

	public double getSuccesscwbamount() {
		return successcwbamount;
	}

	public void setSuccesscwbamount(double successcwbamount) {
		this.successcwbamount = successcwbamount;
	}

	public long getBackcwbnum() {
		return backcwbnum;
	}

	public void setBackcwbnum(long backcwbnum) {
		this.backcwbnum = backcwbnum;
	}

	public double getBackcwbamount() {
		return backcwbamount;
	}

	public void setBackcwbamount(double backcwbamount) {
		this.backcwbamount = backcwbamount;
	}

	public long getLeavedcwbnum() {
		return leavedcwbnum;
	}

	public void setLeavedcwbnum(long leavedcwbnum) {
		this.leavedcwbnum = leavedcwbnum;
	}

	public double getLeavedcwbamount() {
		return leavedcwbamount;
	}

	public void setLeavedcwbamount(double leavedcwbamount) {
		this.leavedcwbamount = leavedcwbamount;
	}

	public long getNopodcwbnum() {
		return nopodcwbnum;
	}

	public void setNopodcwbnum(long nopodcwbnum) {
		this.nopodcwbnum = nopodcwbnum;
	}

	public double getNopodcwbamount() {
		return nopodcwbamount;
	}

	public void setNopodcwbamount(double nopodcwbamount) {
		this.nopodcwbamount = nopodcwbamount;
	}

	public long getPodnoconfirmcwb() {
		return podnoconfirmcwb;
	}

	public void setPodnoconfirmcwb(long podnoconfirmcwb) {
		this.podnoconfirmcwb = podnoconfirmcwb;
	}

	public double getPodnoconfirmcwbamount() {
		return podnoconfirmcwbamount;
	}

	public void setPodnoconfirmcwbamount(double podnoconfirmcwbamount) {
		this.podnoconfirmcwbamount = podnoconfirmcwbamount;
	}

	public long getLeavednopodcwbnum() {
		return leavednopodcwbnum;
	}

	public void setLeavednopodcwbnum(long leavednopodcwbnum) {
		this.leavednopodcwbnum = leavednopodcwbnum;
	}

	public double getLeavednopodcwbamount() {
		return leavednopodcwbamount;
	}

	public void setLeavednopodcwbamount(double leavednopodcwbamount) {
		this.leavednopodcwbamount = leavednopodcwbamount;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getCwbbranchname() {
		return cwbbranchname;
	}

	public void setCwbbranchname(String cwbbranchname) {
		this.cwbbranchname = cwbbranchname;
	}

	public String getCwbbranchnamewav() {
		return cwbbranchnamewav;
	}

	public void setCwbbranchnamewav(String cwbbranchnamewav) {
		this.cwbbranchnamewav = cwbbranchnamewav;
	}

	public String getCwbdelivername() {
		return cwbdelivername;
	}

	public void setCwbdelivername(String cwbdelivername) {
		this.cwbdelivername = cwbdelivername;
	}

	public String getCwbdelivernamewav() {
		return cwbdelivernamewav;
	}

	public void setCwbdelivernamewav(String cwbdelivernamewav) {
		this.cwbdelivernamewav = cwbdelivernamewav;
	}

	public BigDecimal getCwbreceivablefee() {
		return cwbreceivablefee;
	}

	public void setCwbreceivablefee(BigDecimal cwbreceivablefee) {
		this.cwbreceivablefee = cwbreceivablefee;
	}

	public String getWavfilenames() {
		return wavfilenames;
	}

	public void setWavfilenames(String wavfilenames) {
		this.wavfilenames = wavfilenames;
	}

}
