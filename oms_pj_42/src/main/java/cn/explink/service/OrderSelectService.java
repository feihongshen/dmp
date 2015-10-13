package cn.explink.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.controller.CwbOrderImportDTO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OperatelogDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.SetExportField;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.ExcelUtils;

@Service
public class OrderSelectService {
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	OperatelogDAO operatelogDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	SetExportFieldDAO setexportfieldDAO;
	@Autowired
	CwbDAO cwborderDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;

	@Produce(uri = "jms:topic:updateOrderMoney")
	ProducerTemplate sendUpdateOrderMoney;
	@Produce(uri = "jms:topic:auditBackGoods")
	ProducerTemplate auditBackGoods;
	@Produce(uri = "jms:topic:auditBackGoodsEgan")
	ProducerTemplate auditBackGoodsEgan;
	@Produce(uri = "jms:topic:auditBackGoodsRemark")
	ProducerTemplate auditBackGoodsRemark;
	@Autowired
	ExportService exportService;

	private Logger logger = LoggerFactory.getLogger(OrderSelectService.class);

	public int updateOrderMoneySendJms(String cwb, String money) {
		logger.info("发送修改订单金额---sendUpdateOrderMoney");
		JSONObject sendObject = new JSONObject();
		sendObject.put("cwb", cwb);
		sendObject.put("money", money);
		try {
			sendUpdateOrderMoney.sendBodyAndHeader(null, "cwbAndMoney", sendObject.toString());
			return 1;
		} catch (Exception e) {
			logger.error("JMS : Send : jms:topic:updateOrderMoney : cwb : " + cwb);
			return 0;
		}
	}

	public int auditBackGoodsJms(String cwb, int state) {
		logger.info("发送退货审核消息---auditBackGoodsJms");
		JSONObject sendObject = new JSONObject();
		sendObject.put("cwb", cwb);
		sendObject.put("auditstate", state);
		try {
			auditBackGoods.sendBodyAndHeader(null, "cwbAndAuditstate", sendObject.toString());
			logger.error("JMS : Send success: jms:topic:auditBackGoods : cwb : " + cwb + " auditstate:" + state);
			return 1;
		} catch (Exception e) {
			logger.error("JMS : Send error: jms:topic:auditBackGoods : cwb : " + cwb + " auditstate:" + state);
			return 0;
		}
	}

	public int auditBackGoodsEganJms(String cwb, int state) {
		logger.info("发送退货再投审核消息---auditBackGoodsEganJms");
		JSONObject sendObject = new JSONObject();
		sendObject.put("cwb", cwb);
		sendObject.put("auditEganstate", state);
		try {
			auditBackGoodsEgan.sendBodyAndHeader(null, "cwbAndAuditEganstate", sendObject.toString());
			logger.error("JMS : Send success: jms:topic:auditBackGoodsEgan : cwb : " + cwb + " auditEganstate:" + state);
			return 1;
		} catch (Exception e) {
			logger.error("JMS : Send error: jms:topic:auditBackGoodsEgan : cwb : " + cwb + " auditEganstate:" + state);
			return 0;
		}
	}

	public int auditBackGoodsRemarkJms(String cwb, String remark) {
		logger.info("发送退货备注消息---auditBackGoodsRemarkJms");
		JSONObject sendObject = new JSONObject();
		sendObject.put("cwb", cwb);
		sendObject.put("remark", remark);
		try {
			auditBackGoodsRemark.sendBodyAndHeader(null, "cwbAndBackremark", sendObject.toString());
			logger.error("JMS : Send success: jms:topic:auditBackGoodsRemark : cwb : " + cwb + " remark:" + remark);
			return 1;
		} catch (Exception e) {
			logger.error("JMS : Send error: jms:topic:auditBackGoodsRemark : cwb : " + cwb + " remark:" + remark);
			return 0;
		}
	}

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	public void exportexportExcleWang(String mouldfieldids2, HttpServletResponse response, HttpServletRequest request) {

		/*
		 * List<SetExportField> listSetExportField =
		 * exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2); String[]
		 * cloumnName1 ={}; //导出的列名 String[] cloumnName2 ={}; //导出的英文列名
		 * if(listSetExportField !=null && listSetExportField.size()>0){
		 * cloumnName1 = new String[listSetExportField.size()+4]; cloumnName2 =
		 * new String[listSetExportField.size()+4]; cloumnName1[0]= "[订单号]";
		 * cloumnName2[0]= "Cwb"; cloumnName1[1]= "[签收人]"; cloumnName2[1]=
		 * "Signinman"; cloumnName1[2]= "[签收时间]"; cloumnName2[2]=
		 * "Editsignintime"; cloumnName1[3]= "[备注]"; cloumnName2[3]=
		 * "Cwbremark"; for (int k=4,j = 0; j < listSetExportField.size();
		 * j++,k++) { cloumnName1[k]= listSetExportField.get(j).getFieldname();
		 * cloumnName2[k]= listSetExportField.get(j).getFieldenglishname(); }
		 * }else{ cloumnName1 = new String[7]; cloumnName2 = new String[7]; for
		 * (int j = 0; j < 7; j++) { cloumnName1[0]= "[订单号]"; cloumnName2[0]=
		 * "Cwb"; cloumnName1[1]= "[运单号]"; cloumnName2[1]= "Transcwb";
		 * cloumnName1[2]= "[收件人]"; cloumnName2[2]= "Consigneename";
		 * cloumnName1[3]= "[代收款]"; cloumnName2[3]= "Receivablefee";
		 * cloumnName1[4]= "[签收人]"; cloumnName2[4]= "Signinman"; cloumnName1[5]=
		 * "[签收时间]"; cloumnName2[5]= "Editsignintime"; cloumnName1[6]= "[备注]";
		 * cloumnName2[6]= "Cwbremark";
		 * 
		 * }
		 * 
		 * } final String[] cloumnName = cloumnName1; final String[] cloumnName3
		 * = cloumnName2; String sheetName = "订单信息"; //sheet的名称 SimpleDateFormat
		 * df = new SimpleDateFormat("yyyy-MM-dd_HH-MM-ss"); String fileName =
		 * "WangGoods_"+df.format(new Date())+".xls"; //文件名 try { //查询出数据
		 * //查询出数据 long customerid =
		 * Long.parseLong(request.getSession().getAttribute
		 * ("customerid").toString()); String commonnumber =
		 * request.getSession().getAttribute("commonnumber").toString(); int
		 * surpassdate =
		 * Integer.parseInt(request.getSession().getAttribute("surpassdate"
		 * ).toString
		 * ())==0?2:Integer.parseInt(request.getSession().getAttribute(
		 * "surpassdate").toString()); int marksflag =
		 * Integer.parseInt(request.getSession
		 * ().getAttribute("marksflag").toString()); String orderName =
		 * request.getSession().getAttribute("orderbyName").toString(); final
		 * List<CwbOrder> list =
		 * cwborderDAO.getcwbOrderByPageDExport(customerid, commonnumber,
		 * surpassdate, marksflag,orderName); ExcelUtils excelUtil = new
		 * ExcelUtils(){ //生成工具类实例，并实现填充数据的抽象方法
		 * 
		 * @Override public void fillData(Sheet sheet, CellStyle style) { //
		 * List<CwbOrder> list = cwborderDAO.getAllCwbOrder(); for (int k = 0; k
		 * < list.size(); k++) { Row row = sheet.createRow(k+1);
		 * row.setHeightInPoints((float) 15); for (int i = 0; i <
		 * cloumnName.length; i++) { Cell cell = row.createCell((short)i);
		 * cell.setCellStyle(style); Object a = null; try {
		 * if("Cwbordertypeid".equals(cloumnName3[i])){ a
		 * =((CwbOrder)list.get(k)
		 * ).getClass().getMethod("get"+cloumnName3[i]).invoke
		 * ((CwbOrder)list.get(k));
		 * if(a.toString().equals(CwbOrderTypeIdEnum.Peisong.getValue()+"")){ a
		 * = CwbOrderTypeIdEnum.Peisong.getText(); }else
		 * if(a.toString().equals(CwbOrderTypeIdEnum
		 * .Shangmentui.getValue()+"")){ a =
		 * CwbOrderTypeIdEnum.Shangmentui.getText(); }else
		 * if(a.toString().equals
		 * (CwbOrderTypeIdEnum.Shangmenhuan.getValue()+"")){ a =
		 * CwbOrderTypeIdEnum.Shangmenhuan.getText(); } }else{ a =
		 * ((CwbOrder)list
		 * .get(k)).getClass().getMethod("get"+cloumnName3[i]).invoke
		 * ((CwbOrder)list.get(k)); } } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * cell.setCellValue(a==null?"":a.toString()); } } } };
		 * excelUtil.excel(response, cloumnName, sheetName, fileName);
		 * 
		 * } catch (Exception e) {
		 * 
		 * }
		 */
	}

	public String updateImportOrder(List<CwbOrderImportDTO> cwbOrderDTOs) {
		try {
			if (cwbOrderDTOs != null && cwbOrderDTOs.size() > 0) {
				for (CwbOrderImportDTO cwbOrderImportDTO : cwbOrderDTOs) {
					if (cwbOrderImportDTO.getSigninman() != null && !cwbOrderImportDTO.getSigninman().equals("") && cwbOrderImportDTO.getSignintime() != null
							&& !cwbOrderImportDTO.getSignintime().equals("")) {
						cwbDAO.saveImportOrder(cwbOrderImportDTO.getCwb(), cwbOrderImportDTO.getSigninman(), cwbOrderImportDTO.getSignintime(), getSessionUser().getUsername() + ":"
								+ cwbOrderImportDTO.getCwbremark(), DeliveryStateEnum.PeiSongChengGong.getValue());
						try {
							// 写日志
							operatelogDAO.creOperatelog(getSessionUser().getUsername(), cwbOrderImportDTO.getCwb(), cwbOrderImportDTO.getCwb(), "订单号为：" + cwbOrderImportDTO.getCwb() + "被"
									+ cwbOrderImportDTO.getSigninman() + "(" + cwbOrderImportDTO.getSignintime() + ")签收，备注：" + cwbOrderImportDTO.getCwbremark() + "");
						} catch (Exception e) {
							System.out.println("写日志报错！");
						}
					} else {
						cwbDAO.saveImportOrder(cwbOrderImportDTO.getCwb(), cwbOrderImportDTO.getSigninman(), cwbOrderImportDTO.getSignintime(), getSessionUser().getUsername() + ":"
								+ cwbOrderImportDTO.getCwbremark(), -1);
						try {
							// 写日志
							operatelogDAO.creOperatelog(getSessionUser().getUsername(), cwbOrderImportDTO.getCwb(), cwbOrderImportDTO.getCwb(), "订单号为：" + cwbOrderImportDTO.getCwb() + "被修改备注，备注："
									+ cwbOrderImportDTO.getCwbremark() + "");
						} catch (Exception e) {
							System.out.println("写日志报错！");
						}
					}
				}
			}
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

	}

	public void backGoodsexportExcle(String mouldfieldids2, HttpServletResponse response, HttpServletRequest request) {
		/*
		 * List<SetExportField> listSetExportField =
		 * exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2); String[]
		 * cloumnName1 ={}; //导出的列名 String[] cloumnName2 ={}; //导出的英文列名
		 * if(listSetExportField !=null && listSetExportField.size()>0){
		 * cloumnName1 = new String[listSetExportField.size()+4]; cloumnName2 =
		 * new String[listSetExportField.size()+4]; cloumnName1[0]= "[订单号]";
		 * cloumnName2[0]= "Cwb"; cloumnName1[1]= "[签收人]"; cloumnName2[1]=
		 * "Signinman"; cloumnName1[2]= "[签收时间]"; cloumnName2[2]=
		 * "Editsignintime"; cloumnName1[3]= "[备注]"; cloumnName2[3]=
		 * "Cwbremark"; for (int k=4,j = 0; j < listSetExportField.size();
		 * j++,k++) { cloumnName1[k]= listSetExportField.get(j).getFieldname();
		 * cloumnName2[k]= listSetExportField.get(j).getFieldenglishname(); }
		 * }else{ cloumnName1 = new String[7]; cloumnName2 = new String[7]; for
		 * (int j = 0; j < 7; j++) { cloumnName1[0]= "[订单号]"; cloumnName2[0]=
		 * "Cwb"; cloumnName1[1]= "[运单号]"; cloumnName2[1]= "Transcwb";
		 * cloumnName1[2]= "[收件人]"; cloumnName2[2]= "Consigneename";
		 * cloumnName1[3]= "[代收款]"; cloumnName2[3]= "Receivablefee";
		 * cloumnName1[4]= "[签收人]"; cloumnName2[4]= "Signinman"; cloumnName1[5]=
		 * "[签收时间]"; cloumnName2[5]= "Editsignintime"; cloumnName1[6]= "[备注]";
		 * cloumnName2[6]= "Cwbremark";
		 * 
		 * } } final String[] cloumnName = cloumnName1; final String[]
		 * cloumnName3 = cloumnName2; String sheetName = "订单信息"; //sheet的名称
		 * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-MM-ss");
		 * String fileName = "BackGoogs_"+df.format(new Date())+".xlsx"; //文件名
		 * try { //查询出数据 //查询出数据 auditstate,auditEganstate, String beginshiptime
		 * = request.getSession().getAttribute("beginshiptime").toString();
		 * String endshiptime =
		 * request.getSession().getAttribute("endshiptime").toString(); long
		 * customerid =
		 * Long.parseLong(request.getSession().getAttribute("customerid"
		 * ).toString()); long auditstate =
		 * Long.parseLong(request.getSession().getAttribute
		 * ("auditstate").toString()); long auditEganstate =
		 * Long.parseLong(request
		 * .getSession().getAttribute("auditEganstate").toString()); String
		 * commonnumber =
		 * request.getSession().getAttribute("commonnumber").toString(); String
		 * orderbyName =
		 * request.getSession().getAttribute("orderbyName").toString(); final
		 * List<CwbOrder> list =
		 * cwborderDAO.getcwbOrderByPageRexport(customerid, commonnumber,
		 * auditstate,auditEganstate,beginshiptime, endshiptime, orderbyName);
		 * ExcelUtils excelUtil = new ExcelUtils(){ //生成工具类实例，并实现填充数据的抽象方法
		 * 
		 * @Override public void fillData(Sheet sheet, CellStyle style) { for
		 * (int k = 0; k < list.size(); k++) { Row row = sheet.createRow(k+1);
		 * row.setHeightInPoints((float) 15); for (int i = 0; i <
		 * cloumnName.length; i++) { Cell cell = row.createCell((short)i);
		 * cell.setCellStyle(style); Object a = null; try {
		 * if("Cwbordertypeid".equals(cloumnName3[i])){ a
		 * =((CwbOrder)list.get(k)
		 * ).getClass().getMethod("get"+cloumnName3[i]).invoke
		 * ((CwbOrder)list.get(k));
		 * if(a.toString().equals(CwbOrderTypeIdEnum.Peisong.getValue()+"")){ a
		 * = CwbOrderTypeIdEnum.Peisong.getText(); }else
		 * if(a.toString().equals(CwbOrderTypeIdEnum
		 * .Shangmentui.getValue()+"")){ a =
		 * CwbOrderTypeIdEnum.Shangmentui.getText(); }else
		 * if(a.toString().equals
		 * (CwbOrderTypeIdEnum.Shangmenhuan.getValue()+"")){ a =
		 * CwbOrderTypeIdEnum.Shangmenhuan.getText(); } }else{ a =
		 * ((CwbOrder)list
		 * .get(k)).getClass().getMethod("get"+cloumnName3[i]).invoke
		 * ((CwbOrder)list.get(k)); } } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * cell.setCellValue(a==null?"":a.toString()); } } } };
		 * excelUtil.excel(response, cloumnName, sheetName, fileName);
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}

}
