package cn.explink.b2c.dongfangcj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;

/**
 * 回收完成通知
 * 
 * @author Administrator
 *
 */
@Service
public class DongFangCJService_goback extends DongFangCJService {

	@Autowired
	B2CDataDAO b2CDataDAO;
	@Autowired
	private B2cTools b2ctools;
	private Logger logger = LoggerFactory.getLogger(DongFangCJService.class);

	// 回收完成通知
	public void createDongFangCJTxtFile_goback() {
		if (!b2ctools.isB2cOpen(B2cEnum.DongFangCJ.getKey())) {
			logger.info("未开启0东方CJ0反馈接口");
			return;
		}

		try {
			String filetime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");
			DongFangCJ cj = super.getDongFangCJSettingMethod(B2cEnum.DongFangCJ.getKey());
			String partener = cj.getPartener() + "R1"; // 裕美捷标识
			String filename_finish = partener + filetime + ".txt";
			String uploadPath = cj.getUploadPath();

			ifInExistsFileDirCreate(uploadPath); // 不存在则创建
			File file = new File(uploadPath + filename_finish); // 创建文件txt
			if (!file.exists()) {
				file.createNewFile();
			}

			int txtnum_dc = 0;
			List<B2CData> datalist = b2CDataDAO.getDataListByFlowStatus(Long.valueOf(FlowOrderTypeEnum.YiShenHe.getValue()), cj.getCustomerids(), Integer.valueOf(cj.getMaxcount()));

			if (datalist == null || datalist.size() == 0) {
				logger.info("当前没有反馈给0东方CJ-回收通知0的货物信息~");
				return;
			}
			OutputStreamWriter pw_dc = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

			// 表头H
			String title = "H" + filetime + partener;
			pw_dc.write(title);
			pw_dc.write("\n");

			String b2cids = "";
			for (B2CData b2cData : datalist) {
				DongFangCJXMLNote xmlnote = getXMLNoteObjectsMethod(b2cData.getJsoncontent());
				int interfaceType = xmlnote.getInterfaceType();
				if (interfaceType != 2) {
					logger.info("当前订单不属于回收及时推送单,cwb={}", b2cData.getCwb());
					continue;
				}
				pw_dc.write(getGoBackStrings(txtnum_dc, b2cData).toString());

				b2cids += b2cData.getB2cid() + ",";
				txtnum_dc++;
				pw_dc.write("\n");
			}

			// 尾部文件

			// 配送成功文件反馈

			if (txtnum_dc > 0) {
				String bottom = "T" + getNeedZeroStr(txtnum_dc + "", GoBackLineEnum.EndLen.getLen() - 1, null);
				pw_dc.write(bottom);
				pw_dc.flush();
				pw_dc.close();

				// 文件上传
				DongFangCJFTPUtils ftp = new DongFangCJFTPUtils(cj.getFtp_host(), cj.getFtp_username(), cj.getFtp_password(), cj.getFtp_port(), cj.getCharencode());

				String uploadPath_bak = cj.getUploadPath_bak();
				ifInExistsFileDirCreate(uploadPath_bak); // 不存在则创建 upload_bak

				logger.info("东方CJ->回收信息完成生成文件{}", filename_finish);

				ftp.uploadFileToFTPByDongFangCJ(uploadPath, uploadPath_bak, filename_finish, cj);

				// 修改反馈结果为成功
				b2CDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(b2cids.substring(0, b2cids.length() - 1));
				file.delete(); // 删除文件

			} else {
				logger.info("当前没有推送给东方CJ的数据-回收单完成");
				pw_dc.close();
				file.delete();
			}

		} catch (Exception e) {
			logger.error("上传东方CJ回收单数据未知异常", e);
		}
	}

	private StringBuffer getGoBackStrings(int txtnum_dc, B2CData b2cData) throws JsonParseException, JsonMappingException, IOException {
		String cwb = b2cData.getCwb();
		DongFangCJXMLNote xmlnote = getXMLNoteObjectsMethod(b2cData.getJsoncontent());

		StringBuffer line = new StringBuffer("");
		line.append("D");
		line.append(getNeedZeroStr((txtnum_dc + 1) + "", GoBackLineEnum.PaiXuBianHao.getLen(), null)); // 排序编号
		line.append(getNeedBlankStr(cwb, GoBackLineEnum.HuoYunDanHao.getLen())); // 货运单号
		line.append(getNeedBlankStr(xmlnote.getGobacktime(), GoBackLineEnum.HuiShouShiJian.getLen())); // 回收时间
		line.append(getNeedBlankStr(xmlnote.getGobackflag(), GoBackLineEnum.HuiShouYuFou.getLen())); // 回收与否
		line.append(getNeedBlankStr(xmlnote.getFaildReason(), GoBackLineEnum.ShibaiYuanYin.getLen())); // 失败原因

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
