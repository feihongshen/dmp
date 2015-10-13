package cn.explink.b2c.homegou;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.util.DateTimeUtil;

/**
 * 配送完成通知DC/CO/MO v4.3 v4.4 v4.5
 * 
 * @author Administrator
 *
 */
@Service
public class HomegouService_Delivery extends HomegouService {

	@Autowired
	B2CDataDAO b2CDataDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public long feedback_state() {
		long calcCount = 0;
		calcCount += createHomegouTxtFileAndUpload(FlowOrderTypeEnum.YiShenHe.getValue()); // 已审核
																							// 包括
																							// DC、CO/MO

		calcCount += createHomegouTxtFileAndUploadBack(FlowOrderTypeEnum.YiShenHe.getValue()); // 上门退类型订单,商品回收
		return calcCount;
	}

	// 配送结束通知 配送完成 4.3 ,4.4 ,4.5
	public long createHomegouTxtFileAndUpload(int flowordertype) {
		long calcCount = 0;
		Homegou hg = super.getHomegouSettingMethod(B2cEnum.HomeGou.getKey());

		List<B2CData> datalist = b2CDataDAO.getDataListByFlowStatus(flowordertype, hg.getCustomerids(), Integer.valueOf(hg.getMaxcount()));

		if (datalist == null || datalist.size() == 0) {
			logger.info("当前没有反馈给0家有购物0的货物信息~配送订单");
			return 0;
		}

		String b2cids = getB2cIds(datalist);

		// 文件上传
		HomegouFTPUtils ftp = new HomegouFTPUtils(hg.getFtp_host(), hg.getFtp_username(), hg.getFtp_password(), hg.getFtp_port(), hg.getCharencode(), false);

		try {

			createFileUpload_DC(hg, datalist, ftp); // 配送结果 生成文件
			createFileUpload_CO(hg, datalist, ftp); // CO 生成文件
			createFileUpload_MO(hg, datalist, ftp); // MO 生成文件

			calcCount += datalist.size();

		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}

		// 修改反馈结果为成功
		b2CDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(b2cids.substring(0, b2cids.length() - 1));
		return calcCount;
	}

	// 配送结束 商品回收接口
	public long createHomegouTxtFileAndUploadBack(int flowordertype) {
		long calcCount = 0;
		Homegou hg = super.getHomegouSettingMethod(B2cEnum.HomeGou.getKey());

		List<B2CData> datalist = b2CDataDAO.getDataListByFlowStatus(flowordertype, hg.getCustomerid_tuihuo(), Integer.valueOf(hg.getMaxcount()));

		if (datalist == null || datalist.size() == 0) {
			logger.info("当前没有反馈给0家有购物0的货物信息~商品回收");
			return 0;
		}

		String b2cids = getB2cIds(datalist);

		// 文件上传
		HomegouFTPUtils ftp = new HomegouFTPUtils(hg.getFtp_host(), hg.getFtp_username(), hg.getFtp_password(), hg.getFtp_port(), hg.getCharencode(), false);

		try {

			createFileUpload_RE(hg, datalist, ftp); // 配送结果 生成文件
			calcCount += datalist.size();
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}

		// 修改反馈结果为成功
		b2CDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(b2cids.substring(0, b2cids.length() - 1));
		return calcCount;
	}

	private String getB2cIds(List<B2CData> datalist) {
		String b2cids = "";
		for (B2CData b2cdata : datalist) {
			b2cids += b2cdata.getB2cid() + ",";
		}
		return b2cids;
	}

	private void createFileUpload_DC(Homegou hg, List<B2CData> datalist, HomegouFTPUtils ftp) throws IOException, UnsupportedEncodingException, FileNotFoundException, JsonParseException,
			JsonMappingException, Exception {
		String filetime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");

		String filename_dc = "DC" + hg.getPartener() + filetime + ".txt";

		String uploadPath = hg.getUploadPath();

		ifInExistsFileDirCreate(uploadPath); // 不存在则创建
		File file_dc = new File(uploadPath + filename_dc); // 创建文件txt
		if (!file_dc.exists()) {
			file_dc.createNewFile();
		}

		int txtnum_dc = 0;

		OutputStreamWriter pw_dc = new OutputStreamWriter(new FileOutputStream(file_dc), "UTF-8");

		// 表头H
		String title = "S" + filetime.substring(0, 12) + hg.getPartener();
		pw_dc.write(title);
		pw_dc.write("\r\n");

		for (B2CData b2cData : datalist) {

			pw_dc.write(getDeliveryStrings_DC(txtnum_dc, b2cData, hg).toString());

			txtnum_dc++;
			pw_dc.write("\r\n");
		}

		// 尾部文件

		String bottom = "E" + getNeedBlankStr(txtnum_dc + "", MesageLineEnum.EndLen.getLen());
		pw_dc.write(bottom);
		pw_dc.write("\r\n");
		pw_dc.flush();
		pw_dc.close();

		String uploadPath_bak = hg.getUploadPath_bak();
		ifInExistsFileDirCreate(uploadPath_bak); // 不存在则创建 upload_bak

		logger.info("家有购物->配送完成 生成文件{}", filename_dc);

		ftp.uploadFileToFTPByDongFangCJ(uploadPath, uploadPath_bak, filename_dc, hg);
		file_dc.delete(); // 删除文件

	}

	private void createFileUpload_RE(Homegou hg, List<B2CData> datalist, HomegouFTPUtils ftp) throws IOException, UnsupportedEncodingException, FileNotFoundException, JsonParseException,
			JsonMappingException, Exception {
		String filetime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");

		String filename_dc = "RE" + hg.getPartener() + filetime + ".txt";

		String uploadPath = hg.getUploadPath();

		ifInExistsFileDirCreate(uploadPath); // 不存在则创建
		File file_dc = new File(uploadPath + filename_dc); // 创建文件txt
		if (!file_dc.exists()) {
			file_dc.createNewFile();
		}

		int txtnum_dc = 0;

		OutputStreamWriter pw_dc = new OutputStreamWriter(new FileOutputStream(file_dc), "UTF-8");

		// 表头H
		String title = "S" + filetime.substring(0, 12) + hg.getPartener();
		pw_dc.write(title);
		pw_dc.write("\r\n");

		for (B2CData b2cData : datalist) {
			txtnum_dc++;
			pw_dc.write(getDeliveryStrings_RE(txtnum_dc, b2cData, hg).toString());

			pw_dc.write("\r\n");
		}

		// 尾部文件

		String bottom = "E" + getNeedBlankStr(txtnum_dc + "", MesageLineEnum.EndLen.getLen());
		pw_dc.write(bottom);
		pw_dc.write("\r\n");
		pw_dc.flush();
		pw_dc.close();

		String uploadPath_bak = hg.getUploadPath_bak();
		ifInExistsFileDirCreate(uploadPath_bak); // 不存在则创建 upload_bak

		logger.info("家有购物->商品回收结果通知 生成文件{}", filename_dc);

		ftp.uploadFileToFTPByDongFangCJ(uploadPath, uploadPath_bak, filename_dc, hg);
		file_dc.delete(); // 删除文件

	}

	private void createFileUpload_CO(Homegou hg, List<B2CData> datalist, HomegouFTPUtils ftp) throws IOException, UnsupportedEncodingException, FileNotFoundException, JsonParseException,
			JsonMappingException, Exception {

		int levelstep = 0;
		for (B2CData b2cData : datalist) {
			HomegouXMLNote xmlnote = getXMLNoteObjectsMethod(b2cData.getJsoncontent());
			if (Double.valueOf(xmlnote.getPayAmount()) > 0) {
				levelstep++;
			}
		}

		if (levelstep == 0) {
			logger.info("当前没有待回传给家有购物的数据!COD付款");
			return;
		}

		String filetime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");

		String filename_co = "CO" + hg.getPartener() + filetime + ".txt";

		String uploadPath = hg.getUploadPath();

		ifInExistsFileDirCreate(uploadPath); // 不存在则创建
		File file_dc = new File(uploadPath + filename_co); // 创建文件txt
		if (!file_dc.exists()) {
			file_dc.createNewFile();
		}

		OutputStreamWriter pw_dc = new OutputStreamWriter(new FileOutputStream(file_dc), "UTF-8");

		// 表头H
		String title = "S" + filetime.substring(0, 12) + hg.getPartener();
		pw_dc.write(title);
		pw_dc.write("\r\n");

		int txtnum_dc = 0;
		for (B2CData b2cData : datalist) {

			HomegouXMLNote xmlnote = getXMLNoteObjectsMethod(b2cData.getJsoncontent());
			if (Double.valueOf(xmlnote.getPayAmount()) > 0) {
				txtnum_dc++;
				pw_dc.write(getDeliveryStrings_CO(txtnum_dc, b2cData, hg, xmlnote).toString());
				pw_dc.write("\r\n");
			}

		}

		// 尾部文件
		String bottom = "E" + getNeedBlankStr(txtnum_dc + "", MesageLineEnum.EndLen.getLen());
		pw_dc.write(bottom);
		pw_dc.write("\r\n");
		pw_dc.flush();
		pw_dc.close();

		String uploadPath_bak = hg.getUploadPath_bak();
		ifInExistsFileDirCreate(uploadPath_bak); // 不存在则创建 upload_bak

		logger.info("家有购物->COD现金付款生成文件{}", filename_co);

		ftp.uploadFileToFTPByDongFangCJ(uploadPath, uploadPath_bak, filename_co, hg);
		file_dc.delete(); // 删除文件

	}

	private void createFileUpload_MO(Homegou hg, List<B2CData> datalist, HomegouFTPUtils ftp) throws IOException, UnsupportedEncodingException, FileNotFoundException, JsonParseException,
			JsonMappingException, Exception {
		int levelstep = 0;
		for (B2CData b2cData : datalist) {
			HomegouXMLNote xmlnote = getXMLNoteObjectsMethod(b2cData.getJsoncontent());
			if (xmlnote.getPaytypeflag() == PaytypeEnum.Pos.getValue()) {
				levelstep++;
			}
		}

		if (levelstep == 0) {
			logger.info("当前没有待回传给家有购物的数据!MO-POS刷卡支付!");
			return;
		}

		String filetime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");

		String filename_MO = "MO" + hg.getPartener() + filetime + ".txt";

		String uploadPath = hg.getUploadPath();

		ifInExistsFileDirCreate(uploadPath); // 不存在则创建
		File file_dc = new File(uploadPath + filename_MO); // 创建文件txt
		if (!file_dc.exists()) {
			file_dc.createNewFile();
		}

		OutputStreamWriter pw_dc = new OutputStreamWriter(new FileOutputStream(file_dc), "UTF-8");

		// 表头H
		String title = "S" + filetime.substring(0, 12) + hg.getPartener();
		pw_dc.write(title);
		pw_dc.write("\r\n");

		int txtnum_dc = 0;
		for (B2CData b2cData : datalist) {
			HomegouXMLNote xmlnote = getXMLNoteObjectsMethod(b2cData.getJsoncontent());
			if (xmlnote.getPaytypeflag() == PaytypeEnum.Pos.getValue()) {
				txtnum_dc++;
				pw_dc.write(getDeliveryStrings_MO(txtnum_dc, b2cData, hg, xmlnote).toString());
				pw_dc.write("\r\n");
			}

		}

		// 尾部文件
		String bottom = "E" + getNeedBlankStr(txtnum_dc + "", MesageLineEnum.EndLen.getLen());
		pw_dc.write(bottom);
		pw_dc.write("\r\n");
		pw_dc.flush();
		pw_dc.close();

		String uploadPath_bak = hg.getUploadPath_bak();
		ifInExistsFileDirCreate(uploadPath_bak); // 不存在则创建 upload_bak

		logger.info("家有购物-MO-POS付款生成文件{}", filename_MO);

		ftp.uploadFileToFTPByDongFangCJ(uploadPath, uploadPath_bak, filename_MO, hg);
		file_dc.delete(); // 删除文件

	}

	private StringBuffer getDeliveryStrings_MO(int txtnum_dc, B2CData b2cData, Homegou hg, HomegouXMLNote xmlnote) throws JsonParseException, JsonMappingException, IOException {
		String cwb = b2cData.getCwb();

		StringBuffer line = new StringBuffer("");

		line.append(getNeedBlankStr("", MOEnum.Business_no.getLen())); // 公司代码
		line.append(getNeedBlankStr("", MOEnum.terminal_no.getLen())); // 终端编号
		line.append(getNeedBlankStr("", MOEnum.card_bank.getLen())); // 银行代码

		line.append(getNeedBlankStr("", MOEnum.card_no.getLen())); // 卡号
		line.append(getNeedBlankStr(xmlnote.getOk_date(), MOEnum.ok_date.getLen())); // 授权时间
		line.append(getNeedZeroStr(xmlnote.getInamt_amt(), MOEnum.inamt_amt.getLen(), "1")); // 授权金额
		line.append(getNeedBlankStr(xmlnote.getCwb(), MOEnum.wb_no.getLen())); // 运单号

		line.append(getNeedBlankStr("", MOEnum.batch_no.getLen())); // batch号
		line.append(getNeedBlankStr("", MOEnum.sys_no.getLen())); // 系统号
		line.append(getNeedBlankStr("", MOEnum.serial_no.getLen())); // serial号
		line.append(getNeedBlankStr(xmlnote.getInamt_date(), MOEnum.inamt_date.getLen())); // inamt_date
		line.append(getNeedBlankStr(xmlnote.getType(), MOEnum.type.getLen())); // 授权区分

		return line;
	}

	private StringBuffer getDeliveryStrings_CO(int txtnum_dc, B2CData b2cData, Homegou hg, HomegouXMLNote xmlnote) throws JsonParseException, JsonMappingException, IOException {
		String cwb = b2cData.getCwb();

		StringBuffer line = new StringBuffer("");
		line.append(getNeedZeroStr((txtnum_dc) + "", CODEnum.PaiXuBianHao.getLen(), null)); // 配送结束编号
		line.append(getNeedZeroStr(hg.getPartener(), CODEnum.company_code.getLen(), null)); // 配送公司代码
		line.append(getNeedBlankStr(xmlnote.getCwb(), CODEnum.Cwb.getLen())); // 运单号
		line.append(getNeedZeroStr(xmlnote.getPayAmount(), CODEnum.CodAmount.getLen(), "1")); // 支付金额

		return line;
	}

	private StringBuffer getDeliveryStrings_DC(int txtnum_dc, B2CData b2cData, Homegou hg) throws JsonParseException, JsonMappingException, IOException {
		String cwb = b2cData.getCwb();
		HomegouXMLNote xmlnote = getXMLNoteObjectsMethod(b2cData.getJsoncontent());

		StringBuffer line = new StringBuffer("");
		line.append(getNeedZeroStr((txtnum_dc + 1) + "", DCEnum.paixuCode.getLen(), null)); // 配送结束编号
		line.append(getNeedZeroStr(hg.getPartener(), DCEnum.company_code.getLen(), null)); // 配送公司代码
		line.append(getNeedBlankStr(xmlnote.getCwb(), DCEnum.Cwb.getLen())); // 运单号
		line.append(getNeedBlankStr("1", DCEnum.deliveryResult.getLen())); // 配送结束与否
		line.append(getNeedBlankStr(xmlnote.getDeliverytime(), DCEnum.deliverytime.getLen())); // 配送结束日
		line.append(getNeedBlankStr("00", DCEnum.deliveryReason.getLen())); // 配送原因代码
																			// 使用“00”
		line.append(getNeedBlankStr(xmlnote.getPeisongqufen(), DCEnum.CwbOrdertype.getLen())); // 配送区分
																								// 10:不用付款,
																								// 20:货到付款，40：交换送货
		line.append(getNeedZeroStr(xmlnote.getPayAmount(), DCEnum.deliveryAmount.getLen(), "1")); // 配送金额

		line.append(getNeedBlankStr(xmlnote.getPaytypeid1(), DCEnum.payType1.getLen())); // 付款方法1
		line.append(getNeedZeroStr(xmlnote.getPayamount1(), DCEnum.payAmount1.getLen(), "1")); // 支付金额1
		line.append(getNeedBlankStr(xmlnote.getPaytypeid2(), DCEnum.payType2.getLen())); // 付款方法2
		line.append(getNeedZeroStr(xmlnote.getPayamount2(), DCEnum.payAmount2.getLen(), "1")); // 支付金额2

		return line;
	}

	private StringBuffer getDeliveryStrings_RE(int txtnum_dc, B2CData b2cData, Homegou hg) throws JsonParseException, JsonMappingException, IOException {
		String cwb = b2cData.getCwb();
		HomegouXMLNote xmlnote = getXMLNoteObjectsMethod(b2cData.getJsoncontent());

		StringBuffer line = new StringBuffer("");
		line.append(getNeedZeroStr((txtnum_dc) + "", REEnum.paixuCode.getLen(), null)); // 配送结束编号
		line.append(getNeedZeroStr(hg.getPartener(), REEnum.company_code.getLen(), null)); // 配送公司代码
		line.append(getNeedBlankStr(cwb, REEnum.Wb_no.getLen())); // 运单号
		line.append(getNeedBlankStr(xmlnote.getTranscwb(), REEnum.DingGouBianHao.getLen())); // 订购编号

		return line;
	}

	/**
	 * 不存在就创建 文件夹
	 * 
	 * @param uploadPath_bak
	 */
	private void ifInExistsFileDirCreate(String uploadPath_bak) {
		File Fupload_bak = new File(uploadPath_bak);
		if (!Fupload_bak.exists()) {
			Fupload_bak.mkdirs();
		}
	}

	// 不足位数前面补0
	private String getNeedZeroStr(String str, int lennum, String feeflag) {
		String zerostr = "";
		if (feeflag != null && str.indexOf(".") > 0) {// 金额字段
			str = str.substring(0, str.indexOf("."));
		}
		str = "0000000000000000000000000000000000000000000000000000000000000000000000000" + str;
		zerostr = str.substring(str.length() - lennum, str.length());
		return zerostr;
	}

	// 不足位数后面补 空格
	private static String getNeedBlankStr(String str, int lennum) {
		if (str == null) {
			return "";
		}

		String space = " "; // 定义一个空格，表示一个空格单位
		String needSpace = "";
		for (int i = 0; i < lennum; i++) {
			needSpace = needSpace + space;
		}
		String retstr = str + needSpace;

		String zerostr = retstr.substring(0, lennum);
		return zerostr;
	}

}
