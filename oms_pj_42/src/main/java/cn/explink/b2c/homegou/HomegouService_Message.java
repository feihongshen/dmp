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
import cn.explink.util.DateTimeUtil;

/**
 * 派送员领货 发送短信通知 v4.7
 * 
 * @author Administrator
 *
 */
@Service
public class HomegouService_Message extends HomegouService {

	@Autowired
	B2CDataDAO b2CDataDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public long feedback_state() {

		return createHomegouTxtFileAndUpload(FlowOrderTypeEnum.FenZhanLingHuo.getValue()); // 分站派送中发送短信通知
	}

	// 分站派送中。
	public long createHomegouTxtFileAndUpload(int flowordertype) {

		Homegou hg = super.getHomegouSettingMethod(B2cEnum.HomeGou.getKey());

		List<B2CData> datalist = b2CDataDAO.getDataListByFlowStatus(flowordertype, hg.getCustomerids() + "," + hg.getCustomerid_tuihuo(), Integer.valueOf(hg.getMaxcount()));

		if (datalist == null || datalist.size() == 0) {
			logger.info("当前没有反馈给0家有购物0的分站领货短信通知信息~");
			return 0;
		}

		String b2cids = getB2cIds(datalist);

		// 文件上传
		HomegouFTPUtils ftp = new HomegouFTPUtils(hg.getFtp_host(), hg.getFtp_username(), hg.getFtp_password(), hg.getFtp_port(), hg.getCharencode(), false);

		try {

			createFileUpload_message(hg, datalist, ftp); // 配送中短信通知 生成文件

		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}

		// 修改反馈结果为成功
		b2CDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(b2cids.substring(0, b2cids.length() - 1));
		return datalist.size();
	}

	private String getB2cIds(List<B2CData> datalist) {
		String b2cids = "";
		for (B2CData b2cdata : datalist) {
			b2cids += b2cdata.getB2cid() + ",";
		}
		return b2cids;
	}

	private void createFileUpload_message(Homegou hg, List<B2CData> datalist, HomegouFTPUtils ftp) throws IOException, UnsupportedEncodingException, FileNotFoundException, JsonParseException,
			JsonMappingException, Exception {
		String filetime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");

		String filename_dc = "SO" + hg.getPartener() + filetime + ".txt";

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

			pw_dc.write(getDeliveringMessageStrings(txtnum_dc, b2cData).toString());

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

	// 派送中 发送短信通知
	private StringBuffer getDeliveringMessageStrings(int txtnum_dc, B2CData b2cData) throws JsonParseException, JsonMappingException, IOException {
		String cwb = b2cData.getCwb();
		HomegouXMLNote xmlnote = getXMLNoteObjectsMethod(b2cData.getJsoncontent());

		StringBuffer line = new StringBuffer("");

		line.append(getNeedBlankString_UTF8(xmlnote.getConsigneeno(), MesageLineEnum.consigneeCode.getLen())); // 顾客代码
		line.append(getNeedBlankString_UTF8(xmlnote.getConsigneename(), MesageLineEnum.consigneeName.getLen())); // 顾客姓名
		line.append(getNeedBlankString_UTF8(xmlnote.getMobilephone(), MesageLineEnum.mobilePhone.getLen())); // 手机号
		line.append(getNeedBlankString_UTF8(xmlnote.getMessage_type(), MesageLineEnum.messageType.getLen())); // 短信类型
		line.append(getNeedBlankString_UTF8(xmlnote.getMessage_content(), MesageLineEnum.messageContent.getLen())); // 短信内容

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

	// 不足位数后面补空格 UTF-8截取格式
	private String getNeedBlankString_UTF8(String str, int lennum) {
		String tempblankstr = "                                                                                                    "
				+ "                                                                                                    "
				+ "                                                                                                    "
				+ "                                                                                                    "
				+ "                                                                                                    ";

		int len = UTF8length(str); // utf-8的长度
		String blankstr = "";
		if (len < lennum) {
			blankstr = tempblankstr.substring(0, lennum - len);
		}

		return str + blankstr;
	}

	public static int UTF8length(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* utf-8中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	// 不足位数后面补 空格
	private static String getNeedBlankStr(String str, int lennum) {
		if (str == null || str.isEmpty()) {
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
