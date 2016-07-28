package cn.explink.schedule.worker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.ScheduledTask;
import cn.explink.domain.User;
import cn.explink.domain.addressvo.AddressSyncServiceResult;
import cn.explink.domain.addressvo.ApplicationVo;
import cn.explink.domain.addressvo.DelivererVo;
import cn.explink.domain.addressvo.DeliveryStationVo;
import cn.explink.domain.addressvo.VendorVo;
import cn.explink.exception.ExplinkRuntimeException;
import cn.explink.schedule.Constants;
import cn.explink.service.addressmatch.AddressSyncService;
import cn.explink.util.ResourceBundleUtil;

@Service
public class SynAddressTaskWrapper {

	@Autowired
	CustomerDAO customerDao;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	AddressSyncService addressService;

	public Object doSomething(Object param) throws Exception {
		ScheduledTask scheduledTask = (ScheduledTask) param;
		long synAddressId = Long.parseLong(scheduledTask.getReferenceId());
		String taskType = scheduledTask.getTaskType();
		String result = "success";
		long addresscustomerid = Long.parseLong(ResourceBundleUtil.addresscustomerid);
		//
		/*
		 * SystemInstall addrwsurl =
		 * systemInstallDAO.getSystemInstallByName("addressSynWSURL1"); String
		 * addrwpathsurl = ""; if(addrwsurl == null){ addrwpathsurl =
		 * "http://localhost:8080/address/service/AddressSyncService?wsdl";
		 * }else{ addrwpathsurl = addrwsurl.getValue(); }
		 */
		if (Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE.equals(taskType)) {
			List<User> userList = this.userDAO.getUserByid(synAddressId);
			User user = userList.get(0);
			AddressSyncServiceResult addressSyncServiceResult = this.addressService.createDeliverer(this.setApplicationVo(), this.setDelivererVo(user, addresscustomerid));
			// System.out.println("小件员同步地址库 create"+addressSyncServiceResult.toString());
			if (addressSyncServiceResult.getResultCode().getCode() != 0) {
				throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not a valid synaddress task 小件员同步地址库 create");
			}
		} else if (Constants.TASK_TYPE_SYN_ADDRESS_USER_MODIFY.equals(taskType)) {
			List<User> userList = this.userDAO.getUserByid(synAddressId);
			User user = userList.get(0);
			AddressSyncServiceResult addressSyncServiceResult = this.addressService.updateDeliverer(this.setApplicationVo(), this.setDelivererVo(user, addresscustomerid));
			// System.out.println("小件员同步地址库 modify"+addressSyncServiceResult.toString());
			if (addressSyncServiceResult.getResultCode().getCode() != 0) {
				throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not a valid synaddress task 小件员同步地址库 modify");
			}
		} else if (Constants.TASK_TYPE_SYN_ADDRESS_USER_DELETE.equals(taskType)) {
			// TODO 小件员同步地址库
			List<User> userList = this.userDAO.getUserByid(synAddressId);
			User user = userList.get(0);
			AddressSyncServiceResult addressSyncServiceResult = this.addressService.deleteDeliverer(this.setApplicationVo(), this.setDelivererVo(user, addresscustomerid));
			// System.out.println("小件员同步地址库 delete"+addressSyncServiceResult.toString());
			if (addressSyncServiceResult.getResultCode().getCode() != 0) {
				throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not a valid synaddress task 小件员同步地址库 delete");
			}
		} else if (Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_CREATE.equals(taskType)) {
			Branch branch = this.branchDAO.getBranchByBranchid(synAddressId);
			AddressSyncServiceResult addressSyncServiceResult = this.addressService.createDeliveryStation(this.setApplicationVo(), this.setDeliveryStationVo(branch, addresscustomerid));
			// System.out.println("站点同步地址库 create "+addressSyncServiceResult.toString());
			if (addressSyncServiceResult.getResultCode().getCode() != 0) {
				throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not a valid synaddress task 站点同步地址库 create " + addressSyncServiceResult.getMessage());
			}
		} else if (Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_MODIFY.equals(taskType)) {
			Branch branch = this.branchDAO.getBranchByBranchid(synAddressId);
			AddressSyncServiceResult addressSyncServiceResult = this.addressService.updateDeliveryStation(this.setApplicationVo(), this.setDeliveryStationVo(branch, addresscustomerid));
			// System.out.println("站点同步地址库 modify"+addressSyncServiceResult.toString());
			if (addressSyncServiceResult.getResultCode().getCode() != 0) {
				throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not a valid synaddress task 站点同步地址库 modify");
			}
		} else if (Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_DELETE.equals(taskType)) {
			// TODO 站点同步地址库
			Branch branch = this.branchDAO.getBranchByBranchid(synAddressId);
			AddressSyncServiceResult addressSyncServiceResult = this.addressService.deleteDeliveryStation(this.setApplicationVo(), this.setDeliveryStationVo(branch, addresscustomerid));
			// System.out.println("站点同步地址库 delete"+addressSyncServiceResult.toString());
			if (addressSyncServiceResult.getResultCode().getCode() != 0) {
				throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not a valid synaddress task 站点同步地址库 delete");
			}
		} else if (Constants.TASK_TYPE_SYN_ADDRESS_CUSTOMER_CREATE.equals(taskType)) {
			Customer customer = this.customerDao.getCustomerById(synAddressId);
			AddressSyncServiceResult addressSyncServiceResult = this.addressService.createVendor(this.setApplicationVo(), this.setVendorVo(customer, addresscustomerid));
			// System.out.println("供货商同步地址库 create"+addressSyncServiceResult.toString());
			if (addressSyncServiceResult.getResultCode().getCode() != 0) {
				throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not a valid synaddress task 供货商同步地址库 create");
			}
		} else if (Constants.TASK_TYPE_SYN_ADDRESS_CUSTOMER_MODIFY.equals(taskType)) {
			Customer customer = this.customerDao.getCustomerById(synAddressId);
			AddressSyncServiceResult addressSyncServiceResult = this.addressService.updateVendor(this.setApplicationVo(), this.setVendorVo(customer, addresscustomerid));
			if (addressSyncServiceResult.getResultCode().getCode() != 0) {
				throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not a valid synaddress task 供货商同步地址库 modify");
			}
			// System.out.println("供货商同步地址库 modify"+addressSyncServiceResult.toString());
		} else if (Constants.TASK_TYPE_SYN_ADDRESS_CUSTOMER_DELETE.equals(taskType)) {
			// TODO 供货商同步地址库
			Customer customer = this.customerDao.getCustomerById(synAddressId);
			/*
			 * Object []prams = {setApplicationVo(),
			 * setVendorVo(customer,addresscustomerid)};
			 * WebServiceHandler.invokeWs(addrwpathsurl, "deleteVendor", prams);
			 */
			AddressSyncServiceResult addressSyncServiceResult = this.addressService.deleteVendor(this.setApplicationVo(), this.setVendorVo(customer, addresscustomerid));
			// System.out.println("供货商同步地址库 delete"+addressSyncServiceResult.toString());
			if (addressSyncServiceResult.getResultCode().getCode() != 0) {
				throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not a valid synaddress task 供货商同步地址库 delete");
			}
		} else {
			throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not a valid synaddress task");
		}
		return result;
	}

	/**
	 * 生成applicationVo
	 */
	private ApplicationVo setApplicationVo() {
		ApplicationVo applicationVo = new ApplicationVo();
		applicationVo.setCustomerId(Long.parseLong(ResourceBundleUtil.addresscustomerid));
		applicationVo.setId(Long.parseLong(ResourceBundleUtil.addressid));
		applicationVo.setPassword(ResourceBundleUtil.addresspassword);
		return applicationVo;
	}

	/**
	 * 生成userVo
	 *
	 * @param user
	 * @return
	 */
	private DelivererVo setDelivererVo(User user, Long vendorid) {
		DelivererVo delivererVo = new DelivererVo();
		delivererVo.setCustomerId(vendorid);
		delivererVo.setExternalId(user.getUserid());
		delivererVo.setName(user.getRealname());
		delivererVo.setExternalStationId(user.getBranchid());
		delivererVo.setUserCode(user.getUsername());
		return delivererVo;
	}

	/**
	 * 生成branchVo
	 *
	 * @param branch
	 * @return
	 */
	private DeliveryStationVo setDeliveryStationVo(Branch branch, Long vendorid) {
		DeliveryStationVo deliveryStationVo = new DeliveryStationVo();
		deliveryStationVo.setCustomerId(vendorid);
		deliveryStationVo.setExternalId(branch.getBranchid());
		deliveryStationVo.setName(branch.getBranchname());
		return deliveryStationVo;
	}

	/**
	 * 生成customerVo
	 *
	 * @param customer
	 * @return
	 */
	private VendorVo setVendorVo(Customer customer, Long vendorid) {
		VendorVo vendorVo = new VendorVo();
		vendorVo.setCustomerId(vendorid);
		vendorVo.setExternalId(customer.getCustomerid());
		vendorVo.setName(customer.getCustomername());
		return vendorVo;
	}

	public static void main(String[] arg) throws Exception {
		// 创建WebService客户端代理工厂

		/*
		 * ApplicationVo applicationVo = new ApplicationVo();
		 * applicationVo.setCustomerId(Long.parseLong("0"));
		 * applicationVo.setId(Long.parseLong("1"));
		 * applicationVo.setPassword("123"); DeliveryStationVo deliveryStationVo
		 * = new DeliveryStationVo();
		 * deliveryStationVo.setCustomerId(Long.parseLong("1"));
		 * deliveryStationVo.setExternalId(Long.parseLong("123"));
		 * deliveryStationVo.setName("东城站"); Object []params = {applicationVo,
		 * deliveryStationVo}; JaxWsDynamicClientFactory clientFactory =
		 * JaxWsDynamicClientFactory.newInstance(); Client client =
		 * clientFactory.createClient(
		 * "http://127.0.0.1:8080/address/service/AddressSyncService?wsdl");
		 * Object []r = client.invoke("updateDeliveryStation", params);
		 * System.out.print(r);
		 *
		 * WebServiceHandler.invokeWs(
		 * "http://localhost:8080/address/service/AddressSyncService?wsdl",
		 * "createDeliveryStation", params); System.exit(0);
		 */

		/*
		 * JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		 * //注册WebService接口 factory.setServiceClass(GreetingService.class);
		 * //设置WebService地址
		 * factory.setAddress("http://gary-pc:8080/testWebService/GreetingService"
		 * ); GreetingService greetingService =
		 * (GreetingService)factory.create();
		 * System.out.println("invoke webservice...");
		 * System.out.println("message context is:"
		 * +greetingService.greeting("gary"));
		 */

	}
}