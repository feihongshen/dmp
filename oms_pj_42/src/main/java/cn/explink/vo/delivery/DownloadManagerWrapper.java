package cn.explink.vo.delivery;

import java.util.List;

import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.DownloadManager;
import cn.explink.enumutil.DownloadState;
import cn.explink.exception.ExplinkRuntimeException;
import cn.explink.util.JsonUtil;
import cn.explink.vo.delivery.util.DeliveryRateDisplayUtil;

public class DownloadManagerWrapper {

	private DownloadManager download;

	private DeliveryRateRequest deliveryRateRequest;

	private String query;

	private boolean branchTab;

	private boolean custemerTab;

	private boolean branchcustemerTab;

	public DownloadManagerWrapper(DownloadManager download, List<Branch> branchList, List<Customer> customerList) {
		this.download = download;
		try {
			deliveryRateRequest = JsonUtil.readValue(download.getDatajson(), DeliveryRateRequest.class);
			if (deliveryRateRequest.getBranchIds() != null && deliveryRateRequest.getCustomerIds() == null) {
				branchTab = true;
				custemerTab = false;
			}

			if (deliveryRateRequest.getBranchIds() == null && deliveryRateRequest.getCustomerIds() != null) {
				custemerTab = true;
				branchTab = false;
			}

			if (deliveryRateRequest.getBranchIds() != null && deliveryRateRequest.getCustomerIds() != null) {
				custemerTab = true;
				branchTab = true;
			}

		} catch (Exception e) {
			throw new ExplinkRuntimeException(e);
		}
		query = DeliveryRateDisplayUtil.buildQuery(deliveryRateRequest, branchList, customerList);
	}

	public String getStateDesc() {
		DownloadState[] allDownloadStates = DownloadState.values();
		for (DownloadState state : allDownloadStates) {
			if (state.getValue() == download.getState()) {
				return state.getDescription();
			}
		}
		return "";
	}

	public String getQuery() {
		return query;
	}

	public long getId() {
		return download.getId();
	}

	public long getUserid() {
		return download.getUserid();
	}

	public String getDatajson() {
		return download.getDatajson();
	}

	public int getModelid() {
		return download.getModelid();
	}

	public String getCreatetime() {
		return download.getCreatetime();
	}

	public String getFilename() {
		return download.getFilename();
	}

	public String getFileurl() {
		return download.getFileurl();
	}

	public long getTimeout() {
		return download.getTimeout();
	}

	public String getEndtime() {
		return download.getEndtime();
	}

	public String getCnfilename() {
		return download.getCnfilename();
	}

	public int getState() {
		return download.getState();
	}

	public boolean isBranchTab() {
		return branchTab;
	}

	public void setBranchTab(boolean branchTab) {
		this.branchTab = branchTab;
	}

	public boolean isCustemerTab() {
		return custemerTab;
	}

	public void setCustemerTab(boolean custemerTab) {
		this.custemerTab = custemerTab;
	}

	public boolean isBranchcustemerTab() {
		return branchcustemerTab;
	}

	public void setBranchcustemerTab(boolean branchcustemerTab) {
		this.branchcustemerTab = branchcustemerTab;
	}

}
