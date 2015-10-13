package cn.explink.vo.delivery.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.util.DateTimeUtil;
import cn.explink.vo.delivery.DeliveryRateQueryType;
import cn.explink.vo.delivery.DeliveryRateRequest;

public class DeliveryRateDisplayUtil {

	public static String buildQuery(DeliveryRateRequest deliveryRateRequest, List<Branch> branchList, List<Customer> customerList) {
		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : branchList) {
			branchMap.put(branch.getBranchid(), branch);
		}

		Map<Long, Customer> customerMap = new HashMap<Long, Customer>();
		for (Customer customer : customerList) {
			customerMap.put(customer.getCustomerid(), customer);
		}
		StringBuilder sb = new StringBuilder();
		DeliveryRateQueryType queryType = deliveryRateRequest.getQueryType();
		// int ww = deliveryRateRequest.getBranchIds().size();
		// int qq = deliveryRateRequest.getCustomerIds().size();
		if (DeliveryRateQueryType.byBranch == queryType || DeliveryRateQueryType.byUser == queryType) {
			for (int i = 0; i < deliveryRateRequest.getBranchIds().size(); i++) {
				Branch branch = branchMap.get(deliveryRateRequest.getBranchIds().get(i));
				if (branch == null) {
					continue;
				}
				if (i > 0) {
					sb.append("\\");
				}
				sb.append(branch.getBranchname());
			}
			if (deliveryRateRequest.getCustomerIds() != null) {
				sb.append(", ");
				for (int i = 0; i < deliveryRateRequest.getCustomerIds().size(); i++) {
					if (i > 0) {
						sb.append("\\");
					}
					sb.append(customerMap.get(deliveryRateRequest.getCustomerIds().get(i)).getCustomername());
				}
			}
		} else {
			for (int i = 0; i < deliveryRateRequest.getCustomerIds().size(); i++) {
				if (i > 0) {
					sb.append("\\");
				}
				sb.append(customerMap.get(deliveryRateRequest.getCustomerIds().get(i)).getCustomername());
			}
			if (deliveryRateRequest.getBranchIds() != null) {
				sb.append(", ");
				for (int i = 0; i < deliveryRateRequest.getBranchIds().size(); i++) {
					Branch branch = branchMap.get(deliveryRateRequest.getBranchIds().get(i));
					if (branch == null) {
						continue;
					}
					if (i > 0) {
						sb.append("\\");
					}
					sb.append(branch.getBranchname());
				}
			}
		}
		sb.append(", ").append(queryType.getDesc());
		if (DeliveryRateQueryType.byBranch == queryType || DeliveryRateQueryType.byUser == queryType) {

		}
		if (deliveryRateRequest.getStartDate() != null && deliveryRateRequest.getEndDate() != null) {
			sb.append(", ").append(DateTimeUtil.formatDate(deliveryRateRequest.getStartDate(), "MM月dd日"));
			sb.append("到").append(DateTimeUtil.formatDate(DateTimeUtil.previousDate(deliveryRateRequest.getEndDate()), "MM月dd日"));
		}
		sb.append(", ").append(deliveryRateRequest.getComputeType().getDesc());
		sb.append(", ");
		for (int i = 0; i < deliveryRateRequest.getTimeTypes().size(); i++) {
			if (i > 0) {
				sb.append("\\");
			}
			sb.append(deliveryRateRequest.getTimeTypes().get(i).getDesc());
		}
		return sb.toString();
	}
}
