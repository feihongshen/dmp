package cn.explink.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.controller.OperateSelectView;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.pos.tools.JacksonMapper;

@Service
public class OperateSelectService {

	private static Logger logger = LoggerFactory.getLogger(OperateSelectService.class);
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private ObjectMapper objectMapper = JacksonMapper.getInstance();

	public List<OperateSelectView> getOperateSelectViewList(List<OrderFlow> orderFlowList, List<User> userList, List<Customer> customerList, List<Branch> branchList) {

		List<OperateSelectView> operateSelectViewList = new ArrayList<OperateSelectView>();
		if (orderFlowList.size() > 0) {
			for (OrderFlow of : orderFlowList) {
				try {
					CwbOrderWithDeliveryState cwbOrderWithDeliveryState = objectMapper.readValue(of.getFloworderdetail(), CwbOrderWithDeliveryState.class);
					CwbOrder cwbOrder = cwbOrderWithDeliveryState.getCwbOrder();

					OperateSelectView operateSelectView = new OperateSelectView();
					operateSelectView.setFloworderid(of.getFloworderid());
					operateSelectView.setCwb(of.getCwb());
					operateSelectView.setBranchname(this.getOperateBranchName(branchList, of.getBranchid()));
					operateSelectView.setCredate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
					operateSelectView.setUsername(this.getOperateUserName(userList, of.getUserid()));
					operateSelectView.setFlowordertype(of.getFlowordertypeText());
					operateSelectView.setComment(of.getComment());
					operateSelectView.setCustomername(this.getOperateCustomerName(customerList, cwbOrder.getCustomerid()));
					operateSelectView.setCwbordertype(this.getCwbOrderTypeText(cwbOrder.getCwbordertypeid()));

					operateSelectViewList.add(operateSelectView);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
		return operateSelectViewList;
	}

	private String getOperateCustomerName(List<Customer> customerList, long customerid) {
		String customername = "";
		for (Customer c : customerList) {
			if (c.getCustomerid() == customerid) {
				customername = c.getCustomername();
				break;
			}
		}
		return customername;
	}

	private String getOperateBranchName(List<Branch> branchList, long branchid) {
		String branchname = "";
		if (branchList.size() > 0) {
			for (Branch b : branchList) {
				if (b.getBranchid() == branchid) {
					branchname = b.getBranchname();
					break;
				}
			}
		}
		return branchname;
	}

	private String getOperateUserName(List<User> userList, long userid) {
		String username = "";
		for (User u : userList) {
			if (u.getUserid() == userid) {
				username = u.getRealname();
				break;
			}
		}
		return username;
	}

	private String getCwbOrderTypeText(long cwbOrderTypeId) {
		String cwbOrderTypeText = "";
		for (CwbOrderTypeIdEnum cot : CwbOrderTypeIdEnum.values()) {
			if (cot.getValue() == cwbOrderTypeId)
				return cot.getText();
		}
		return cwbOrderTypeText;

	}

	public boolean checkBranchRepeat(List<Branch> branchlist, Branch branch) {
		for (int i = 0; i < branchlist.size(); i++) {
			if (branch.getBranchname().equals(branchlist.get(i).getBranchname())) {
				return true;
			}
		}
		return false;
	}

	public SXSSFWorkbook download(List<OperateSelectView> operateSelectViewList) {
		SXSSFWorkbook wb = createWorkbook(operateSelectViewList);
		return wb;
	}

	public SXSSFWorkbook createWorkbook(List<OperateSelectView> operateSelectViewList) {

		SXSSFWorkbook wb = new SXSSFWorkbook();
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Sheet sheet = wb.createSheet();
		Row row = sheet.createRow(0);
		Cell cell = createCell(row, 0, style);
		cell.setCellValue("供货商");
		cell = createCell(row, 1, style);
		cell.setCellValue("订单号");
		cell = createCell(row, 2, style);
		cell.setCellValue("订单类型");
		cell = createCell(row, 3, style);
		cell.setCellValue("操作状态");
		cell = createCell(row, 4, style);
		cell.setCellValue("操作时间");
		cell = createCell(row, 5, style);
		cell.setCellValue("操作站点");
		cell = createCell(row, 6, style);
		cell.setCellValue("操作人");
		int rowNo = 1;
		for (OperateSelectView op : operateSelectViewList) {
			row = sheet.createRow(rowNo);
			cell = createCell(row, 0, style);
			cell.setCellValue(op.getCustomername());
			cell = createCell(row, 1, style);
			cell.setCellValue(op.getCwb());
			cell = createCell(row, 2, style);
			cell.setCellValue(op.getCwbordertype());
			cell = createCell(row, 3, style);
			cell.setCellValue(op.getFlowordertype());
			cell = createCell(row, 4, style);
			cell.setCellValue(op.getCredate());
			cell = createCell(row, 5, style);
			cell.setCellValue(op.getBranchname());
			cell = createCell(row, 6, style);
			cell.setCellValue(op.getUsername());
			rowNo = rowNo + 1;
		}

		return wb;
	}

	private Cell createCell(Row row, int column, CellStyle style) {
		Cell cell = row.createCell(column);
		cell.setCellStyle(style);
		return cell;
	}
}
