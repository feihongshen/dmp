package cn.explink.b2c.tools;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OrderSelectDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmsType;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class ExplinkCwbSearchService {
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	OrderSelectDAO orderSelectDao;

	public String getLiebo(String cwbs, String companyname, String sign) {
		StringBuffer responesXML = new StringBuffer();
		responesXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		responesXML.append("<Result xmlns=\"http://www.fiorano.com/fesb/activity/DBQueryOnInput2/Out\">");

		String jsonCompanyname = "";
		String keyStr = "explinkAndliebo";
		int count = 50;
		// like '%companyname":"1%'
		JointEntity joint = getDmpDAO.getJointEntityByCompanyname(companyname);
		if (joint != null && joint.getJoint_property() != null && !joint.getJoint_property().equals("")) {
			JSONObject jsonO = JSONObject.fromObject(joint.getJoint_property());
			jsonCompanyname = jsonO.getString("companyname");
			keyStr = jsonO.getString("key");
			count = jsonO.getInt("count");
		} else {
			joint = null;
		}

		String sigstr = MD5Util.md5(jsonCompanyname + keyStr);
		if (joint == null) {
			responesXML.append("<msg>配置信息有误</msg>");
			responesXML.append("</Result>");
			return responesXML.toString();
		}
		if (companyname == null) {
			responesXML.append("<msg>缺少参数companyname</msg>");
			responesXML.append("</Result>");
			return responesXML.toString();
		}
		if (cwbs == null) {
			responesXML.append("<msg>缺少参数cwb</msg>");
			responesXML.append("</Result>");
			return responesXML.toString();
		}

		if (sign == null) {
			responesXML.append("<msg>缺少参数sign</msg>");
			responesXML.append("</Result>");
			return responesXML.toString();
		}
		if (cwbs.split(",").length > count) {
			responesXML.append("<msg>订单数不能超过" + count + "个</msg>");
			responesXML.append("</Result>");
			return responesXML.toString();
		}
		if (sign.equals(sigstr)) {// 验证sign
			StringBuffer cwbStr = new StringBuffer();
			if (cwbs.trim().length() > 0) {
				for (String cwb : cwbs.split(",")) {
					getEMS(cwb);// 调ems接口查询最新跟踪状态
					cwbStr.append("'" + cwb.trim());
					cwbStr.append("',");
				}
				String u = cwbStr.substring(0, cwbStr.length() - 1);
				cwbStr = new StringBuffer(u);
			}
			List<CwbOrder> list = cwbDAO.getCwbOrderByInterface(cwbStr.toString());
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Branch branch = getDmpDAO.getNowBranch(list.get(i).getStartbranchid());
					String branchNane = "";
					if (branch != null) {
						branchNane = branch.getBranchname();
					}
					List<EmsType> emslist = list.get(i).getEmsTypeList();
					if (emslist != null && emslist.size() > 0 && !emslist.isEmpty()) {
						for (int j = 0; j < emslist.size(); j++) {
							responesXML.append("<row resultcount=\"" + (j + 1) + "\">");
							responesXML.append("<cwb>" + list.get(i).getCwb() + "</cwb>");// <!--订单号
																							// -->
							responesXML.append("<trackdatetime>" + emslist.get(j).getTime() + "</trackdatetime>");// <!--操作时间
																													// -->
							responesXML.append("<branchname>" + branchNane + "</branchname>");// <!--站点
																								// -->
							responesXML.append("<trackevent>" + emslist.get(j).getContext() + "</trackevent>");// <!--跟踪信息
																												// -->
							if (j == emslist.size() - 1) {
								responesXML.append("<podresultname>" + (list.get(i).getFlowordertype() == 1 ? "配送中" : list.get(i).getFlowordertypeMethod()) + "</podresultname>");// <!--订单状态
																																													// -->
							} else {
								responesXML.append("<podresultname>配送中</podresultname>");// <!--订单状态
																							// -->
							}
							responesXML.append("</row>");
						}
					} else {
						String instoreroomtime = list.get(i).getInstoreroomtime();
						String outstoreroomtime = list.get(i).getOutstoreroomtime();
						String gobacktime = list.get(i).getGobacktime();
						String tracktime = DateTimeUtil.getNowTime();
						if (instoreroomtime != null && !"".equals(instoreroomtime) && (outstoreroomtime == null || "".equals(outstoreroomtime)) && (gobacktime == null || "".equals(gobacktime))) {
							tracktime = instoreroomtime;
						} else if (outstoreroomtime != null && !"".equals(outstoreroomtime)) {
							tracktime = outstoreroomtime;
						} else if (gobacktime != null && !"".equals(gobacktime)) {
							tracktime = gobacktime;
						}
						responesXML.append("<row resultcount=\"1\">");
						responesXML.append("<cwb>" + list.get(i).getCwb() + "</cwb>");// <!--订单号
																						// -->
						responesXML.append("<trackdatetime>" + tracktime + "</trackdatetime>");// <!--操作时间
																								// -->
						responesXML.append("<branchname>" + branchNane + "</branchname>");// <!--站点
																							// -->
						responesXML.append("<trackevent>" + getFloworderTrackInfo(list.get(i).getFlowordertype(), branchNane) + "</trackevent>");// <!--跟踪信息
																																					// -->
						responesXML.append("<podresultname>" + list.get(i).getFlowordertypeMethod() + "</podresultname>");// <!--订单状态
																															// -->
						responesXML.append("</row>");
					}

				}
				responesXML.append("</Result>");
				return responesXML.toString();
			} else {
				responesXML.append("<msg>查询没有数据</msg>");
				responesXML.append("</Result>");
				return responesXML.toString();
			}
		} else {
			responesXML.append("<msg>sign验证不通过</msg>");
			responesXML.append("</Result>");
			return responesXML.toString();
		}
	}

	public String getDangdang(HttpServletRequest request) {

		// S1 非法的XML格式
		// S2 非法的加密验证
		// S3 非法的配送公司
		// S4 服务器处理错误
		// S5 数据不完整
		/*
		 * String
		 * para_xml="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>"
		 * + "<inputObject>" + "<row>" + "<order_id>123</order_id>" + "</row>" +
		 * "</inputObject >";
		 */
		String MD5Data = request.getParameter("MD5Data");
		int key = B2cEnum.DangDang.getKey();
		JointEntity joint = getDmpDAO.getJointEntity(key);
		String keyStr = "explinkAnddangdang";
		int count = 50;
		if (joint != null) {
			if (joint.getJoint_property() != null && !joint.getJoint_property().equals("")) {
				JSONObject jsonO = JSONObject.fromObject(joint.getJoint_property());
				keyStr = jsonO.getString("key");
				count = jsonO.getInt("count");
			}
		}
		String para_xml = request.getParameter("para_xml");
		StringBuffer responesXML = new StringBuffer();
		responesXML.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>");
		responesXML.append("<inputObject>");
		if (para_xml == null) {
			responesXML.append("<statusCode>-1</statusCode>");
			responesXML.append("<errorCode>S1</errorCode");
			responesXML.append("</inputObject>");
			return responesXML.toString();
		}
		if (MD5Data == null) {
			responesXML.append("<statusCode>-1</statusCode>");
			responesXML.append("<errorCode>S2</errorCode");
			responesXML.append("</inputObject>");
			return responesXML.toString();
		}
		String sigstr = MD5Util.md5(para_xml + "&key=" + keyStr);
		if (MD5Data.equals(sigstr)) {// 验证sign
			InputStream iStream = new ByteArrayInputStream(para_xml.getBytes());
			SAXReader reader = new SAXReader();
			Document document = null;
			try {
				document = reader.read(iStream);
			} catch (DocumentException e) {

			}
			if (document == null) {
				responesXML.append("<statusCode>-1</statusCode>");
				responesXML.append("<errorCode>S1</errorCode");
				responesXML.append("</inputObject>");
				return responesXML.toString();
			}
			Element rootElm = document.getRootElement();
			List nodes = rootElm.elements("row");
			String orderIds = "";
			for (int i = 0; i < nodes.size(); i++) {
				Element elm = (Element) nodes.get(i);
				orderIds += "'" + elm.elementText("order_id") + "',";
				getEMS(elm.elementText("order_id"));// 调ems接口查询最新跟踪状态
			}

			if (orderIds.length() > 0) {
				orderIds = orderIds.substring(0, orderIds.length() - 1);
			} else {
				responesXML.append("<statusCode>-1</statusCode>");
				responesXML.append("<errorCode>S5</errorCode");
				responesXML.append("</inputObject>");
				return responesXML.toString();
			}
			if (orderIds.split(",").length > count) {
				responesXML.append("<statusCode>-1</statusCode>");
				responesXML.append("<errorCode>S5</errorCode");
				responesXML.append("</inputObject>");
				return responesXML.toString();
			}

			List<CwbOrder> list = cwbDAO.getCwbOrderByInterface(orderIds);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					List<EmsType> emslist = list.get(i).getEmsTypeList();
					if (emslist != null && emslist.size() > 0 && !emslist.isEmpty()) {
						responesXML.append("<row>");
						responesXML.append("<cwb>" + list.get(i).getCwb() + "</cwb>");// <!--订单号
																						// -->
						responesXML.append("<trans_log>" + list.get(i).getAllfollownotes() + "</trans_log>");// <!--日志内容
																												// -->
						responesXML.append("<log_time>" + emslist.get(0).getTime() + "</log_time>");// <!--日志时间-->
						responesXML.append("<express_id>" + list.get(i).getCommonname() + "</express_id>");// <!--快递公司id
																											// -->
						responesXML.append("<operator>" + list.get(i).getFlowordertypeMethod() + "</operator>");// <!--操作人姓名
																												// -->
						responesXML.append("</row>");
					} else {
						responesXML.append("<row>");
						responesXML.append("<cwb>" + list.get(i).getCwb() + "</cwb>");// <!--订单号
																						// -->
						responesXML.append("<statusCode>-1</statusCode>");
						responesXML.append("<errorCode>S5</errorCode");
						responesXML.append("</row>");
					}
				}
				responesXML.append("</inputObject>");
				return responesXML.toString();
			} else {
				responesXML.append("<statusCode>-1</statusCode>");
				responesXML.append("<errorCode>S5</errorCode");
				responesXML.append("</inputObject>");
				return responesXML.toString();
			}
		} else {
			responesXML.append("<statusCode>-1</statusCode>");
			responesXML.append("<errorCode>S2</errorCode");
			responesXML.append("</inputObject>");
			return responesXML.toString();
		}

	}

	private void getEMS(String cwb) {
		List<CwbOrder> list1 = orderSelectDao.getCwbOrderBycwbid(cwb);
		if (list1 != null && list1.size() > 0) {
			if (list1.get(0).getTranscwb() != null && !"".equals(list1.get(0).getTranscwb())) {
				String htmlStr = getDmpDAO.getEMSType(list1.get(0).getTranscwb());
				JSONObject jsonValue = JSONObject.fromObject(htmlStr.length() > 0 ? htmlStr : "{}");
				int status = htmlStr.length() > 0 ? jsonValue.getInt("status") : 0;
				int state = htmlStr.length() > 0 ? (jsonValue.get("state") == null ? 0 : jsonValue.getInt("state")) : 0;
				if (status == 200) {
					String allfollownotes = jsonValue.getString("data");
					JSONArray jsonArray = JSONArray.fromObject(allfollownotes);
					String jsonOneObject = jsonArray.get(0).toString();
					JSONObject jsonOne = JSONObject.fromObject(jsonOneObject);
					String newfollownotes = jsonOne.getString("context");
					String signintime = jsonOne.getString("time");
					if (jsonArray.size() > 10) {// 退货完成
						cwbDAO.updateByTranscwb(FlowOrderTypeEnum.JuShou.getValue(), signintime, list1.get(0).getCwb(), false);
						cwbDAO.saveCwbByTranscwb(allfollownotes, newfollownotes, signintime, list1.get(0).getTranscwb());
					} else if (state == 3) {// 做签收处理
						cwbDAO.updateByTranscwb(FlowOrderTypeEnum.PeiSongChengGong.getValue(), signintime, list1.get(0).getCwb(), true);
						cwbDAO.saveCwbByTranscwb(allfollownotes, newfollownotes, signintime, list1.get(0).getTranscwb());

					} else {
						cwbDAO.saveCwbByTranscwb(allfollownotes, newfollownotes, signintime, list1.get(0).getTranscwb());
					}

				}
			}
		}

	}

	public String getFloworderTrackInfo(long flowordertype, String startbranchname) {
		String returnStrs = "";

		if (flowordertype == FlowOrderTypeEnum.RuKu.getValue()) {
			returnStrs = FlowOrderTypeEnum.RuKu.getText() + ",当前库房" + startbranchname;
		} else if (flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			returnStrs = FlowOrderTypeEnum.ChuKuSaoMiao.getText() + ",货物从[" + startbranchname + "]出库";
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
			returnStrs = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText() + ",货物已送达分站[" + startbranchname + "]";
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			returnStrs = "货物由分站[" + startbranchname + "]的小件员开始配送";
		} else if (flowordertype == FlowOrderTypeEnum.PeiSongChengGong.getValue()) {
			returnStrs = "已配送成功";
		} else if (flowordertype == FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue()) {
			returnStrs = FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ZhongZhuanChuKuSaoMiao.getValue()) {
			returnStrs = FlowOrderTypeEnum.ZhongZhuanChuKuSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) {
			returnStrs = FlowOrderTypeEnum.CheXiaoFanKui.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
			returnStrs = FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ZhongZhuanZhanYouHuoWuDanRuKu.getValue()) {
			returnStrs = FlowOrderTypeEnum.ZhongZhuanZhanYouHuoWuDanRuKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.YouHuoWuDan.getValue()) {
			returnStrs = FlowOrderTypeEnum.YouHuoWuDan.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TuiHuoZhanZaiTouSaoMiao.getValue()) {
			returnStrs = FlowOrderTypeEnum.TuiHuoZhanZaiTouSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanRuKu.getValue()) {
			returnStrs = FlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanRuKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			returnStrs = FlowOrderTypeEnum.TuiHuoZhanRuKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue()) {
			returnStrs = FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
			returnStrs = FlowOrderTypeEnum.TuiGongYingShangChuKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()) {
			returnStrs = FlowOrderTypeEnum.TiHuoYouHuoWuDan.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TiHuo.getValue()) {
			returnStrs = FlowOrderTypeEnum.TiHuo.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ShangMenTuiChengGong.getValue()) {
			returnStrs = FlowOrderTypeEnum.ShangMenTuiChengGong.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ShangMenJuTui.getValue()) {
			returnStrs = FlowOrderTypeEnum.ShangMenJuTui.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ShangMenHuanChengGong.getValue()) {
			returnStrs = FlowOrderTypeEnum.ShangMenHuanChengGong.getText();
		} else if (flowordertype == FlowOrderTypeEnum.HuoWuDiuShi.getValue()) {
			returnStrs = FlowOrderTypeEnum.HuoWuDiuShi.getText();
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanZhiLiu.getValue()) {
			returnStrs = FlowOrderTypeEnum.FenZhanZhiLiu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.JuShou.getValue()) {
			returnStrs = FlowOrderTypeEnum.JuShou.getText();
		}

		return returnStrs;
	}
}
