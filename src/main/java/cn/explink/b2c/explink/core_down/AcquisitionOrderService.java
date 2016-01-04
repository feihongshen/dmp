package cn.explink.b2c.explink.core_down;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.xmldto.OrderEntity;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.controller.CwbOrderDTO;

/**
 * 
 * @ClassName: PassiveReceptionService
 * @Description: 将获取到的订单导入到数据库中
 * @author 王强
 * @date 2015年11月24日 下午4:32:44
 *
 */
@Service
public class AcquisitionOrderService {
	@Autowired
	EpaiApiDAO epaiApiDAO;
	@Autowired
	EpaiApiService_Download epaiApiService_Download;
	@Autowired
	EpaiApiService_ExportCallBack epaiApiService_ExportCallBack;
	@Autowired
	EpaiInsertCwbDetailTimmer epaiInsertCwbDetailTimmer;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	
	
	private Logger logger = LoggerFactory.getLogger(AcquisitionOrderService.class);

	/**
	 * @Title: passiveReceptionOrActiveAcquisition
	 * @Description: 判断是主动获取还是被动接收订单信息
	 * @param @return 
	 * @return String 
	 * @throws
	 */
	public long passiveReceptionOrActiveAcquisition(){
		long calcCount = 0;
		long passiveCount = 0;
		int check = 0;
		List<EpaiApi> epailist = epaiApiDAO.getEpaiApiList();
		if (epailist == null || epailist.size() == 0) {
			logger.warn("订单获取部分-当前没有配置系统对接设置！");
			return -1;
		}
		for (EpaiApi epai : epailist) {
			if (epai.getIsopenflag() == 1) {
				if(epai.getIsPassiveReception()==1){
					calcCount += this.epaiApiService_Download.downLoadingOrders(epai);
					check++;
				}
			} else {
				logger.warn("订单获取部分-当前m没有开启对接！" + epai.getUserCode());
			}

		}
		if (check == 0 && passiveCount ==0) {
			return -1;
		}
		return calcCount;
		
	}
	
	public String orderDetailExportInterface(OrderEntity order, EpaiApi epaiApi) {
		String cwb = "";
		try {
			List<Map<String, String>> xmllist = getOrderDetailParms(order, epaiApi);
			cwb = xmllist.get(0).get("cwb").toString();
			CwbOrderDTO cwborder = dataImportDAO_B2c.getCwbByCwbB2ctemp(cwb);
			if(cwborder != null){
				logger.info(epaiApi.getUserCode()+"订单信息本地已存在,cwb={}", cwb);
				return sendXmlSuccess(cwb,"99","订单重复下发！");
			}

			List<CwbOrderDTO> cwbdto = dataImportService_B2c.Analizy_DataDealByB2c(epaiApi.getCustomerid(), "epai", xmllist, epaiApi.getWarehouseid(), true);
			
			if(cwbdto == null){
				return sendXml("04","业务异常");
			}
			logger.info(epaiApi.getUserCode()+"处理导入后的订单信息成功,cwb={}", cwb);
			return sendXmlSuccess(cwb,"00", "成功");

		} catch (Exception e) {
			logger.error(epaiApi.getUserCode()+"请求接口遇到未知异常" + e.getMessage(), e);
			return sendXml("99", "请求接口遇到未知异常");
		}
	}
	
	private List<Map<String, String>> getOrderDetailParms(OrderEntity order,EpaiApi epaiApi) {
		List<Map<String, String>> xmllist = new ArrayList<Map<String, String>>();
		Map<String, String> cwbMap = new HashMap<String, String>();

		cwbMap.put("cwb", order.getCwb());
		cwbMap.put("transcwb", order.getTranscwb() == null || order.getTranscwb().isEmpty() ? order.getCwb() : order.getTranscwb());
		cwbMap.put("consigneename", order.getConsigneename() == null ? "" : order.getConsigneename());
		cwbMap.put("cwbprovince", order.getCwbprovince() == null ? "" : order.getCwbprovince());
		cwbMap.put("cwbcity", order.getCwbcity() == null ? "" : order.getCwbcity());
		cwbMap.put("cwbcounty", order.getCwbcounty() == null ? "": order.getCwbcounty());
		cwbMap.put("consigneeaddress", order.getConsigneeaddress() == null ? "" : order.getConsigneeaddress());
		cwbMap.put("consigneepostcode", order.getConsigneepostcode() == null ? "" : order.getConsigneepostcode());
		cwbMap.put("consigneephone", order.getConsigneephone() == null ? "" : order.getConsigneephone());
		cwbMap.put("consigneemobile", order.getConsigneemobile() == null ? "" : order.getConsigneemobile());
		cwbMap.put("sendcarname", order.getSendcargoname() == null ? "" : order.getSendcargoname());
		cwbMap.put("backcargoname", order.getBackcargoname() == null ? "" : order.getBackcargoname());
		cwbMap.put("receivablefee", String.valueOf(order.getReceivablefee() == null ? "0" : order.getReceivablefee()));
		cwbMap.put("paybackfee", String.valueOf(order.getPaybackfee() == null ? "0" : order.getPaybackfee()));
		cwbMap.put("cargorealweight", String.valueOf(order.getCargorealweight() == null ? "0" : order.getCargorealweight()));

		cwbMap.put("caramount", String.valueOf(order.getCargoamount() == null ? "0" : order.getCargoamount()));
		cwbMap.put("cargotype", order.getCargotype() == null ? "" : order.getCargotype());
		cwbMap.put("cargosize", order.getCargosize() == null ? "" : order.getCargosize());
		cwbMap.put("sendcarnum", String.valueOf(order.getSendcargonum()));
		cwbMap.put("backcargonum", String.valueOf(order.getBackcargonum()));
		cwbMap.put("cwbordertypeid", String.valueOf(order.getCwbordertypeid()));
		cwbMap.put("cwbdelivertypeid", String.valueOf(order.getCwbdelivertypeid()));
		cwbMap.put("emaildate", order.getSendtime());
		cwbMap.put("customercommand", order.getCustomercommand());
		cwbMap.put("cwbremark", order.getOuttobranch() == null ? "" : order.getOuttobranch());
		cwbMap.put("paywayid", String.valueOf(order.getPaywayid()));
		cwbMap.put("customerwarehouseid", String.valueOf(epaiApiService_Download.getOrCreateCustomerWarehouse(epaiApi, order.getWarehousename())));
		cwbMap.put("remark1", order.getRemark1());
		cwbMap.put("remark2", order.getRemark2());
		cwbMap.put("remark3", order.getRemark3());
		cwbMap.put("remark4", order.getRemark4());
		cwbMap.put("remark5", order.getRemark5());
		
		
		xmllist.add(cwbMap);
		return xmllist;
	}
	
	/**
	 * 返回成功或失败
	 */
	public String sendXml(String errCode,String errMsg){
		return "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<IFReturn>"
				+ "<errCode>"+errCode+"</errCode>"
				+ "<errMsg>"+errMsg+"</errMsg>"
			+ "</IFReturn>";
	}
	public String sendXmlSuccess(String cwb ,String errCode,String errMsg){
		return "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<IFReturn>"
				+ "<cwb>"+cwb+"</cwb>"
				+ "<errCode>"+errCode+"</errCode>"
				+ "<errMsg>"+errMsg+"</errMsg>"
				+ "</IFReturn>";
	}
}
