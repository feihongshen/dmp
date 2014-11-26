package cn.explink.b2c.yangguang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.pos.tools.JacksonMapper;

@Service
public class YangGuangService {
	private Logger logger = LoggerFactory.getLogger(YangGuangService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	protected ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public YangGuang getYangGuang(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		YangGuang smile = (YangGuang) JSONObject.toBean(jsonObj, YangGuang.class);
		return smile;
	}

	/**
	 * 获取央广不同的配置的信息
	 * 
	 * @param key
	 * @return
	 */
	public List<YangGuangdiff> getYangGuangDiffs(int key) {

		List<YangGuangdiff> list = new ArrayList<YangGuangdiff>();
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		YangGuang yangguang = (YangGuang) JSONObject.toBean(jsonObj, YangGuang.class);
		String multidiffs = yangguang.getMultiInfos();
		JSONArray array = JSONArray.fromObject(multidiffs);
		for (int i = 0; i < array.size(); i++) {
			try {
				YangGuangdiff diff = jacksonmapper.readValue(array.get(i).toString(), YangGuangdiff.class);
				list.add(diff);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * 获取央广不同的配置的信息 过滤有用的并且开启的配置
	 * 
	 * @param key
	 * @return 查询出 已开启的配置
	 */
	public List<YangGuangdiff> filterYangGuangDiffs(List<YangGuangdiff> list) {
		List<YangGuangdiff> difflist = new ArrayList<YangGuangdiff>();
		for (YangGuangdiff diff : list) {
			if (diff.getIsopen() == 0) {
				continue;
			}
			difflist.add(diff);
		}

		return difflist;
	}

	public void edit(HttpServletRequest request, int joint_num) throws JsonGenerationException, JsonMappingException, IOException {
		YangGuang yangguang = new YangGuang();
		yangguang.setHost(request.getParameter("host"));
		yangguang.setPort(Integer.parseInt(request.getParameter("port")));
		yangguang.setCharencode(request.getParameter("charencode"));
		yangguang.setDownload_remotePath(request.getParameter("download_remotePath"));
		yangguang.setUpload_remotePath(request.getParameter("upload_remotePath"));
		yangguang.setDownloadPath(request.getParameter("downloadPath"));
		yangguang.setDownloadPath_bak(request.getParameter("downloadPath_bak"));
		yangguang.setUploadPath(request.getParameter("uploadPath"));
		yangguang.setUploadPath_bak(request.getParameter("uploadPath_bak"));
		yangguang.setKeepDays(Integer.parseInt(request.getParameter("keepDays")));
		yangguang.setServer_sys(request.getParameter("server_sys"));
		yangguang.setFeedbackHours(Integer.valueOf(request.getParameter("feedbackHours")));

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);

		List<YangGuangdiff> difflist = new ArrayList<YangGuangdiff>();

		String customerids = getNewCustomeridsAndBuildList(request, difflist); // 构建多个配置的对象
		String oldCustomerids = getOldCustomerids(joint_num, jointEntity);
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);

		yangguang.setMultiInfos(jacksonmapper.writeValueAsString(difflist));

		JSONObject jsonObj = JSONObject.fromObject(yangguang);

		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}

	}

	private String getNewCustomeridsAndBuildList(HttpServletRequest request, List<YangGuangdiff> difflist) {
		String customerids = "";

		for (int i = 1; i <= 4; i++) { // 这个是按照页面的名称来执行的
			YangGuangdiff diff = new YangGuangdiff();

			String customerid = request.getParameter("customerids" + i);
			diff.setCustomerids(customerid);
			diff.setUsername(request.getParameter("username" + i));
			diff.setPwd(request.getParameter("pwd" + i));
			diff.setWarehouseid(Long.valueOf(request.getParameter("warehouseid" + i)));
			diff.setExpress_id(request.getParameter("express_id" + i));
			diff.setIsopen(Integer.valueOf(request.getParameter("isopen" + i)));
			difflist.add(diff);

			customerids = customerids + customerid + ",";

		}
		return customerids.contains(",") ? customerids.substring(0, customerids.length() - 1) : "";
	}

	private String getOldCustomerids(int joint_num, JointEntity jointEntity) {
		String oldCustomerids = "";
		if (jointEntity != null) {
			List<YangGuangdiff> ygdiffs = getYangGuangDiffs(joint_num);
			for (YangGuangdiff ygdiff : ygdiffs) {
				oldCustomerids = oldCustomerids + ygdiff.getCustomerids() + ",";
			}
			oldCustomerids = oldCustomerids.substring(0, oldCustomerids.length() - 1);
		}
		return oldCustomerids;
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 获取文件所在的文件夹的路径
	 * 
	 * @return
	 */
	public String getPath(String path) {
		String classpath = this.getClass().getClassLoader().getResource("/").getPath(); // E:/eclipseM9/workspace/tree/WEB-INF/classes/
		String tempdir = classpath.substring(0, classpath.indexOf("WEB-INF")); // E:/eclipseM9/workspace/tree/
																				// 带有最后的/
		tempdir = tempdir.substring(1, tempdir.length()).replaceAll("%20", " ");
		String localPath = tempdir + path;
		return localPath;
	}

}
