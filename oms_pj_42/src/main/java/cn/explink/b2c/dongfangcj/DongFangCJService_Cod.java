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
 * Cod付款通知
 * 
 * @author Administrator
 *
 */
@Service
public class DongFangCJService_Cod extends DongFangCJService {

	@Autowired
	B2CDataDAO b2CDataDAO;
	@Autowired
	private B2cTools b2ctools;
	private Logger logger = LoggerFactory.getLogger(DongFangCJService.class);

	// Cod付款标示 针对现金
	public long createDongFangCJTxtFile_Cod() {
		long calcCount = 0;

		if (!b2ctools.isB2cOpen(B2cEnum.DongFangCJ.getKey())) {
			logger.info("未开启0东方CJ0反馈接口");
			return -1;
		}
		try {
			String filetime = DateTimeUtil.getNowTime("yyyyMMdd");
			DongFangCJ cj = super.getDongFangCJSettingMethod(B2cEnum.DongFangCJ.getKey());
			String partener = cj.getPartener() + "B"; // 裕美捷标识
			String filename_finish = partener + filetime + ".txt";
			String uploadPath = cj.getUploadPath();

			ifInExistsFileDirCreate(uploadPath); // 不存在则创建
			File file = new File(uploadPath + filename_finish); // 创建文件txt
			if (!file.exists()) {
				file.createNewFile();
			}

			int txtnum_dc = 0;
			List<B2CData> datalist = b2CDataDAO.getDataListByCodPayAmount(cj.getCustomerids(), Integer.valueOf(cj.getMaxcount()));

			if (datalist == null || datalist.size() == 0) {
				logger.info("当前没有反馈给0东方CJ-Cod付款0的货物信息~");
				return 0;
			}
			OutputStreamWriter pw_dc = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

			// 表头H
			String title = "H" + filetime.substring(0, 8) + partener;
			pw_dc.write(title);
			pw_dc.write("\n");

			String b2cids = "";
			for (B2CData b2cData : datalist) {
				DongFangCJXMLNote xmlnote = getXMLNoteObjectsMethod(b2cData.getJsoncontent());
				int interfaceType = xmlnote.getInterfaceType();
				if (interfaceType == 2) {
					logger.info("老接口不存储回单完成状态通知!");
					continue;
				}
				pw_dc.write(getCodPayAmountStrings(txtnum_dc, b2cData, xmlnote).toString());

				b2cids += b2cData.getB2cid() + ",";
				txtnum_dc++;
				pw_dc.write("\n");
			}

			// 尾部文件

			// 配送成功文件反馈

			if (txtnum_dc > 0) {
				String bottom = "T" + getNeedZeroStr(txtnum_dc + "", CODLineEnum.EndLen.getLen() - 1, null);
				pw_dc.write(bottom);
				pw_dc.flush();
				pw_dc.close();

				// 文件上传
				DongFangCJFTPUtils ftp = new DongFangCJFTPUtils(cj.getFtp_host(), cj.getFtp_username(), cj.getFtp_password(), cj.getFtp_port(), cj.getCharencode());

				String uploadPath_bak = cj.getUploadPath_bak();
				ifInExistsFileDirCreate(uploadPath_bak); // 不存在则创建 upload_bak

				logger.info("东方CJ->Cod付款 生成文件{}", filename_finish);

				ftp.uploadFileToFTPByDongFangCJ(uploadPath, uploadPath_bak, filename_finish, cj);

				// 修改反馈结果为成功
				b2CDataDAO.updatePayAmountFlagByMultiB2cId(b2cids.substring(0, b2cids.length() - 1), 1, "");
				file.delete(); // 删除文件

			} else {
				logger.info("当前没有推送给东方CJ的数据");
				pw_dc.close();
				file.delete();
			}

			calcCount += datalist.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calcCount;

	}

	private StringBuffer getCodPayAmountStrings(int txtnum_dc, B2CData b2cData, DongFangCJXMLNote xmlnote) throws JsonParseException, JsonMappingException, IOException {
		String cwb = b2cData.getCwb();

		StringBuffer line = new StringBuffer("");
		line.append("D");
		line.append(getNeedZeroStr((txtnum_dc + 1) + "", CODLineEnum.PaiXuBianHao.getLen(), null)); // 排序编号
		line.append(getNeedZeroStr(cwb, CODLineEnum.HuoYunDanHao.getLen(), null)); // 货运单号
		line.append(getNeedZeroStr(xmlnote.getPayamount(), CODLineEnum.FuKuanJine.getLen(), "1"));
		line.append(getNeedZeroStr("0", CODLineEnum.ShouXuFei.getLen(), null));
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

}
