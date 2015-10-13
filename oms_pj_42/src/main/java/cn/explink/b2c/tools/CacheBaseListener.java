package cn.explink.b2c.tools;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.User;

@Service
public interface CacheBaseListener {

	public void onChange(Map<String, String> parameters); // 变更通知

	public Customer getCustomer(long customerid);

	public Branch getBranch(long branchid);

	public User getUser(long userid);

	public void initAll();

}
