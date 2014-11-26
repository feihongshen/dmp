package cn.explink.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.WarehouseKeyDAO;
import cn.explink.service.CwbOrderValidator;

@Component
public class ConsigneeaddressValidator implements CwbOrderValidator {

	@Autowired
	WarehouseKeyDAO warehouseKeyDAO;

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 地址选择后验证是否有关联的目标库房
		/*
		 * long targetcarwarehouse = 0;
		 * if(cwbOrder.getCwbprovince()!=null&&cwbOrder
		 * .getCwbprovince().length()
		 * !=0&&cwbOrder.getCwbcity()!=null&&cwbOrder.getCwbcity().length()!=0
		 * &&warehouseKeyDAO.getWarehouseKeyByKeyname(cwbOrder.getCwbprovince()+
		 * cwbOrder.getCwbcity()).size()!=0
		 * &&cwbOrder.getTargetcarwarehouse()!=0
		 * &&warehouseKeyDAO.getWarehouseKeyByKeyname
		 * (cwbOrder.getCwbprovince()+cwbOrder
		 * .getCwbcity()).get(0).getTargetcarwarehouseid
		 * ()!=cwbOrder.getTargetcarwarehouse()){ throw new
		 * RuntimeException("指定目标库房不一致!"); }else
		 * if(cwbOrder.getCwbcity()!=null&&
		 * cwbOrder.getCwbcity().length()!=0&&warehouseKeyDAO
		 * .getWarehouseKeyByKeyname(cwbOrder.getCwbcity()).size()!=0
		 * &&cwbOrder.getTargetcarwarehouse()!=0
		 * &&warehouseKeyDAO.getWarehouseKeyByKeyname
		 * (cwbOrder.getCwbcity()).get(
		 * 0).getTargetcarwarehouseid()!=cwbOrder.getTargetcarwarehouse()){
		 * throw new RuntimeException("指定目标库房不一致!"); }else
		 * if(cwbOrder.getCwbprovince
		 * ()!=null&&cwbOrder.getCwbprovince().length()
		 * !=0&&warehouseKeyDAO.getWarehouseKeyByKeyname
		 * (cwbOrder.getCwbprovince()).size()!=0
		 * &&cwbOrder.getTargetcarwarehouse()!=0
		 * &&warehouseKeyDAO.getWarehouseKeyByKeyname
		 * (cwbOrder.getCwbprovince()).
		 * get(0).getTargetcarwarehouseid()!=cwbOrder.getTargetcarwarehouse()){
		 * throw new RuntimeException("指定目标库房不一致!"); }else
		 * if(((cwbOrder.getCwbprovince
		 * ()!=null&&cwbOrder.getCwbprovince().length
		 * ()!=0)||(cwbOrder.getCwbcity
		 * ()!=null&&cwbOrder.getCwbcity().length()!=0))
		 * &&warehouseKeyDAO.getWarehouseKeyByKeyname
		 * (cwbOrder.getCwbprovince()+cwbOrder.getCwbcity()).size()!=0
		 * &&cwbOrder.getTargetcarwarehouse()==0){ targetcarwarehouse =
		 * warehouseKeyDAO
		 * .getWarehouseKeyByKeyname(cwbOrder.getCwbprovince()+cwbOrder
		 * .getCwbcity()).get(0).getTargetcarwarehouseid();
		 * cwbOrder.setTargetcarwarehouse(targetcarwarehouse); }
		 */

	}
}
