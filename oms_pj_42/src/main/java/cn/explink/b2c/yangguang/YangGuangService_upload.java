package cn.explink.b2c.yangguang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONObject;

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
 * 配送结果反馈生成txt文件上传
 * 
 * @author Administrator 目前只处理 拒收和滞留，妥投不上传，通过人工方式上传excel
 */
@Service
public class YangGuangService_upload extends YangGuangService {
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	private B2cTools b2ctools;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public long createTxtFile_DeliveryFinishMethod() {
		long calcCount = 0;
		try {

			if (!b2ctools.isB2cOpen(B2cEnum.YangGuang.getKey())) {
				logger.info("未开启央广状态反馈对接!key={}", B2cEnum.YangGuang.getKey());
				return -1;
			}
			YangGuang yg = super.getYangGuangSettingMethod(B2cEnum.YangGuang.getKey());

			List<YangGuangdiff> yangGuangdiffList = filterYangGuangDiffs(getYangGuangDiffs(B2cEnum.YangGuang.getKey()));
			if (yangGuangdiffList == null || yangGuangdiffList.size() == 0) {
				logger.warn("央广购物未进行任何配置");
				return 0;
			}
			for (YangGuangdiff diff : yangGuangdiffList) {
				if (diff.getIsopen() == 0) {
					continue;
				}
				calcCount += createYangGuangTxtFile_DeliveryFinish(yg, diff.getExpress_id(), diff.getCustomerids(), diff.getUsername(), diff.getPwd());
			}

		} catch (Exception e) {
			logger.error("执行[配送结束]推送0央广购物0生成txt文件上传的方法异常", e);
			e.printStackTrace();
		}
		return calcCount;
	}

	public long createYangGuangTxtFile_DeliveryFinish(YangGuang yg, String express_id, String customerid, String username, String password) throws Exception {

		long nowHours = DateTimeUtil.getNowHours(); // 获取当前的小时数
		if (yg.getFeedbackHours() != nowHours) {
			logger.warn("当前时间{}不属于执行央广FTP上传的时间", nowHours);
			return 0;
		}
		String filetime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");

		String uploadPath = yg.getUploadPath();
		FileUtils.ifInExistsFileDirCreate(uploadPath); // 不存在则创建
		FileUtils.ifInExistsFileDirCreate(yg.getUploadPath_bak()); // 不存在则创建

		String partener = "RE" + express_id + "1" + "101"; // 文件标识，可配置
		String headpart = "RE" + express_id + "1"; // 表头标识
		String filename_finish = partener + filetime + ".txt";

		File file = new File(uploadPath + filename_finish); // 创建文件txt
		if (!file.exists()) {
			file.createNewFile();
		}

		List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(FlowOrderTypeEnum.YiShenHe.getValue(), customerid, 2000);
		if (datalist == null || datalist.size() == 0) {
			logger.warn("当前没有推送给0央广购物0的数据express_id={},customerid={}", express_id, customerid);
			return 0;
		}

		boolean isCreateFlag = BulildYangGuangTxtFileToDeliverySuccessFlag(filetime, headpart, file, datalist, yg, filename_finish, username, password);
		if (isCreateFlag) {
			b2cDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(getB2cIdBySendB2cList(datalist));
		}

		return datalist.size();

	}

	private boolean BulildYangGuangTxtFileToDeliverySuccessFlag(String filetime, String headpart, File file, List<B2CData> datalist, YangGuang yg, String filename_finish, String username,
			String password) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		OutputStreamWriter pw_dc = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		// 表头H
		String title = "H" + filetime.substring(0, 8) + headpart;
		pw_dc.write(title);
		pw_dc.write("\n");
		int i = 0;
		for (B2CData data : datalist) {
			YangGuangXMLNote xmlnote = getYangGuangXMLNoteMethod(data.getJsoncontent());
			StringBuffer line = new StringBuffer("");
			line.append("D");
			line.append(getNeedZeroStr((++i) + "", YgDeliveryEnum.PaiXuBianHao.getLen(), null)); // 排序编号
			line.append(getNeedNullSpaceStr(xmlnote.getOrderNo(), YgDeliveryEnum.OrderNo.getLen(), null)); // 订购编号
			line.append(getNeedNullSpaceStr(xmlnote.getShippNo(), YgDeliveryEnum.ShippNo.getLen(), null)); // 运单编号
			line.append(getNeedNullSpaceStr(xmlnote.getDeliveryDate(), YgDeliveryEnum.DeliveryDate.getLen(), null)); // 送货日期
			line.append(getNeedNullSpaceStr(xmlnote.getDeliveryResult(), YgDeliveryEnum.CompletionFlag.getLen(), null)); // 送货结果
			line.append(getNeedNullSpaceStr(xmlnote.getExptReason(), YgDeliveryEnum.ExptReason.getLen(), null)); // 异常原因
			line.append(getNeedNullSpaceStr(xmlnote.getExpress_id(), YgDeliveryEnum.Express_ID.getLen(), null)); // 客户标识
			line.append(getNeedNullSpaceStr(xmlnote.getWb_I_No(), YgDeliveryEnum.Wb_I_No.getLen(), null)); // 运单识别编号

			pw_dc.write(line.toString());
			pw_dc.write("\n");
		}
		if (i > 0) {

			String bottom = "T" + getNeedZeroStr(i + "", YgDeliveryEnum.EndLen.getLen() - 1, null);
			pw_dc.write(bottom);
			pw_dc.flush();
			pw_dc.close();

			// 加载配置
			YangGuangFTPUtils ftputils = new YangGuangFTPUtils(yg.getHost(), yg.getPort(), username, password, yg.getCharencode(), yg.getTimeout(), yg.isIsdelDirFlag(), yg.getServer_sys());
			try {
				ftputils.uploadFileToFTPServer(yg.getUploadPath(), yg.getUploadPath_bak(), filename_finish, yg.getUpload_remotePath());
			} catch (Exception e) {
				logger.error("上传央广购物服务器发生未知异常,filename=" + filename_finish, e);
				e.printStackTrace();
			}

			return true;
		}
		return false;
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

	// 不足位数前面补 空格
	private String getNeedNullSpaceStr(String str, int lennum, String feeflag) {
		if (str == null || str.isEmpty()) {
			return "";
		}
		String zerostr = "";

		str = str + "           ";
		zerostr = str.substring(0, str.length() + (lennum - str.length()));
		return zerostr;
	}

	// 获取vipshop XML Note
	public YangGuangXMLNote getYangGuangXMLNoteMethod(String jsoncontent) {
		try {
			JSONObject jsonObj = JSONObject.fromObject(jsoncontent);
			return (YangGuangXMLNote) JSONObject.toBean(jsonObj, YangGuangXMLNote.class);
		} catch (Exception e) {
			logger.error("获取YangGuangXMLNote异常！jsoncontent:" + jsoncontent + e);
			return null;
		}
	}

	// 根据查询出来要推送给当当的数据 查询出所有的主键id
	private String getB2cIdBySendB2cList(List<B2CData> datalist) {
		String b2cids = "";
		if (datalist != null && datalist.size() > 0) {
			for (B2CData bd : datalist) {
				b2cids += bd.getB2cid() + ",";
			}
			b2cids = b2cids.substring(0, b2cids.length() - 1);
		}
		return b2cids;
	}

}
