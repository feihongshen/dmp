package cn.explink.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.dao.PosPayDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.PosPayEntity;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.pos.PosEnum;
import cn.explink.util.JMath;
import cn.explink.util.Page;

/**
 * POS刷卡支付
 * 
 * @author Administrator
 *
 */
@Service
public class PosPayService {

	@Autowired
	PosPayDAO posPayDAO;

	/**
	 * 插入POS支付记录 数据.
	 */
	public boolean PosPayRecord_insert(String pos_code, String cwb, double pos_money, String pos_document, String pos_remark, String pos_payname, String pos_paydate, int pos_delivery,
			String pos_deliveryname, long branchid, String branchname, int upbranchid, long customerid, String customername, String shiptime) {
		PosPayEntity posPay = new PosPayEntity();
		posPay.setPos_code(pos_code);
		posPay.setCwb(cwb);
		posPay.setPos_money(pos_money);
		posPay.setPos_document(pos_document);
		posPay.setPos_payname(pos_payname);
		posPay.setPos_paydate(pos_paydate);
		posPay.setPos_remark(pos_remark);
		posPay.setPos_delivery(pos_delivery);
		posPay.setPos_deliveryname(pos_deliveryname);
		posPay.setBranchid(branchid);
		posPay.setBranchname(branchname);
		posPay.setUpbranchid(upbranchid);
		posPay.setCustomerid(customerid);
		posPay.setCustomername(customername);
		posPay.setShiptime(shiptime);
		boolean insertflag = posPayDAO.PosPayRecord_insert(posPay);
		return insertflag;
	}

	/**
	 * 修改POS支付记录数据信息;
	 */
	public boolean PosPayRecord_update(String cwb, String signname, String signtime, String signremark, int signtype) // 1已签收。2未签收
	{
		PosPayEntity posPay = new PosPayEntity();
		posPay.setCwb(cwb);
		posPay.setPos_signname(signname);
		posPay.setPos_signtime(signtime);
		posPay.setPos_signremark(signremark);
		posPay.setPos_signtype(signtype);
		boolean updateflag = posPayDAO.PosPayRecord_update(posPay);
		return updateflag;
	}

	/**
	 * 查询POS刷卡记录list
	 */
	public List<PosPayEntity> PosPayRecord_selectByList(String pos_code, long customerid, long deliverid, long branchid, String payname, String cwb, String starttime, String endtime, int signtype,
			long branchidSession, int branchtype, String starttime_sp, String endtime_sp, int pos_backoutflag, long page) // 1.本人接受，2他人签收
	{
		List<PosPayEntity> datalist = posPayDAO.PosPayRecord_selectByList(pos_code, customerid, deliverid, branchid, payname, cwb, starttime, endtime, signtype, branchidSession, branchtype,
				starttime_sp, endtime_sp, pos_backoutflag, page);

		return datalist;
	}

	/**
	 * 导出查询POS刷卡记录list
	 */
	public void PosPayRecord_selectSaveAsExcel(String pos_code, long customerid, long deliverid, long branchid, String payname, String cwb, String starttime, String endtime, int signtype,
			long branchidSession, int branchtype, String starttime_sp, String endtime_sp, int pos_backoutflag, HttpServletResponse response) // 1.本人接受，2他人签收
	{
		List<PosPayEntity> datalist = posPayDAO.PosPayRecord_selectByList(pos_code, customerid, deliverid, branchid, payname, cwb, starttime, endtime, signtype, branchidSession, branchtype,
				starttime_sp, endtime_sp, pos_backoutflag, -1);

		String excelbranch = "POS刷卡记录数据列表";
		JMath.CreateExcelHeader(response, excelbranch);
		// 创建excel 新的工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("POS刷卡记录数据列表"); // Sheet1没个新建的Excel都是这个默认值
		sheet.getPrintSetup().setLandscape(true); // true：横向 false：纵向
		int rows = 0; // 第一行
		int c = 0; // 第一列

		HSSFRow row = sheet.createRow((short) rows++);
		JMath.setCellValue(row, c++, "序号");
		JMath.setCellValue(row, c++, "供货商");
		JMath.setCellValue(row, c++, "发货时间");
		JMath.setCellValue(row, c++, "订单号");
		JMath.setCellValue(row, c++, "支付方");
		JMath.setCellValue(row, c++, "付款金额");
		JMath.setCellValue(row, c++, "付款凭证号");
		JMath.setCellValue(row, c++, "付款日期");
		JMath.setCellValue(row, c++, "付款人");
		JMath.setCellValue(row, c++, "POS备注");
		JMath.setCellValue(row, c++, "签收人");
		JMath.setCellValue(row, c++, "签收状态 ");
		JMath.setCellValue(row, c++, "签收时间");
		JMath.setCellValue(row, c++, "签收备注");
		JMath.setCellValue(row, c++, "撤销状态");
		JMath.setCellValue(row, c++, "POS小件员");
		JMath.setCellValue(row, c++, "所属站点");
		int k = 0;
		for (PosPayEntity pe : datalist) {
			k++;
			row = sheet.createRow((short) rows++);
			c = 0; // 第一列
			String pos_name = "";
			String pos_code1 = pe.getPos_code();
			for (PosEnum f : PosEnum.values()) {
				if (f.getMethod().equals(pos_code1)) {
					pos_name = f.getText();
					break;
				}
			}
			JMath.setCellValue(row, c++, "" + (k + 1));
			JMath.setCellValue(row, c++, pe.getCustomername());
			JMath.setCellValue(row, c++, pe.getShiptime());
			JMath.setCellValue(row, c++, pe.getCwb());
			JMath.setCellValue(row, c++, pos_name);
			JMath.setCellValue(row, c++, pe.getPos_money() + "");
			JMath.setCellValue(row, c++, pe.getPos_document());
			JMath.setCellValue(row, c++, pe.getPos_paydate());
			JMath.setCellValue(row, c++, pe.getPos_payname());
			JMath.setCellValue(row, c++, pe.getPos_remark());
			JMath.setCellValue(row, c++, pe.getPos_signname());
			JMath.setCellValue(row, c++, pe.getPos_signtype() == 0 ? "未签收" : "已签收");
			JMath.setCellValue(row, c++, pe.getPos_signtime());
			JMath.setCellValue(row, c++, pe.getPos_signremark());
			JMath.setCellValue(row, c++, pe.getPos_backoutflag() == 1 ? "已撤销" : "");
			JMath.setCellValue(row, c++, pe.getPos_deliveryname());
			JMath.setCellValue(row, c++, pe.getBranchname());

		}
		row = sheet.createRow((short) rows++);
		c = 0; // 第一列
		JMath.setCellValue(row, c++, "合计");
		JMath.setCellValue(row, c++, datalist.size() + "[条]");

		JMath.CreateOutPutExcel(response, workbook);

	}

	public boolean checkBranchRepeat(List<Branch> branchlist, Branch branch) {
		for (int i = 0; i < branchlist.size(); i++) {
			if (branch.getBranchname().equals(branchlist.get(i).getBranchname())) {
				return true;
			}
		}
		return false;
	}

}
