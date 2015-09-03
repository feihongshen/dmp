package cn.explink.controller.weixin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.controller.UserView;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.LogTodayLogDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.weixin.DeliverystasticsDto;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.pos.tools.JacksonMapper;

/**
 * 提供给OMS系统请求的接口
 * 
 * @author Administrator
 *
 */
@RequestMapping("/EPMInterface")
@Controller
public class EPMInterfaceController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	LogTodayLogDAO logTodayDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	/**
	 * 查询单量统计接口
	 * 
	 * @param customerid
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@RequestMapping("/searchCwbStastics/")
	public @ResponseBody String searchCwbStastics(HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException {
		String searchtime = request.getParameter("searchtime");

		List<Map<String, Object>> datalist = cwbDAO.getCwbStasticsByTime(searchtime);
		String resonseJson = JacksonMapper.getInstance().writeValueAsString(datalist);

		return resonseJson;
	}

	/**
	 * 根据username查询用户信息
	 * 
	 * @param userName
	 * @return
	 */
	@RequestMapping("/getUserByLoginUname/{userName}")
	public @ResponseBody String getUserByLoginUname(@PathVariable("userName") String userName) {
		User u = null;
		try {
			u = userDAO.getUserByUsername(userName);

			return JacksonMapper.getInstance().writeValueAsString(u);

		} catch (Exception e) {
			return "";

		}

	}

	/**
	 * 根据username查询用户信息
	 * 
	 * @param userName
	 * @return
	 */
	@RequestMapping("/getUserByRealname/")
	public @ResponseBody String getUserByRealname(HttpServletRequest request) {
		String realname = request.getParameter("realname");
		User u = null;
		UserView userView = new UserView();
		try {
			u = userDAO.getUserByRealname(realname);
			userView.setRealname(u.getRealname());
			userView.setUsername(u.getUsername());
			userView.setBranchname(branchDAO.getBranchByBranchid(u.getBranchid()).getBranchname());
			userView.setRolename(roleDAO.getRolesByRoleid(u.getRoleid()).getRolename());
			userView.setUsermobile(u.getUsermobile());
			userView.setUseremail(u.getUseremail() == null ? "无" : u.getUseremail());
			userView.setEmployeestatus(u.getEmployeestatus());

			return JacksonMapper.getInstance().writeValueAsString(userView);

		} catch (Exception e) {
			logger.error("根据姓名查询员工信息异常", e);
			return "";
		}

	}

	/**
	 * 查询发货统计 按日统计
	 * 
	 * @param customerid
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@RequestMapping("/fahuostastics/")
	public @ResponseBody String fahuoStastics(HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException {
		String searchdate = request.getParameter("searchdate");

		String starttime = searchdate + " 00:00:00";
		String endtime = searchdate + " 23:59:59";

		List<Map<String, Object>> datalist = cwbDAO.getFahuoStastics(starttime, endtime);
		String resonseJson = JacksonMapper.getInstance().writeValueAsString(datalist);

		return resonseJson;
	}

	/**
	 * 查询 派件量
	 * 
	 * @param type
	 *            1 站点 2,派件员
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@RequestMapping("/deliverystastics/")
	public @ResponseBody String deliverystastics(HttpServletRequest request, @RequestParam(value = "type", required = false, defaultValue = "1") int type) throws JsonGenerationException,
			JsonMappingException, IOException {

		String searchdate = request.getParameter("searchdate");

		String starttime = searchdate + " 00:00:00";
		String endtime = searchdate + " 23:59:59";
		List<Branch> branchlist = branchDAO.getAllBranchBySiteType(BranchEnum.ZhanDian.getValue());
		// 查询站点
		if (type == 1) {

			List<DeliverystasticsDto> todaydeliverylist = new ArrayList<DeliverystasticsDto>();

			for (Branch branch : branchlist) {
				// 今日领货
				DeliverystasticsDto todayDeliveryDTO = new DeliverystasticsDto();
				todayDeliveryDTO.setType(1);
				todayDeliveryDTO.setBranchname(branch.getBranchname());

				todayDeliveryDTO.setToday_linghuo(logTodayDAO.getTodaybyBranchid(branch.getBranchid(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), starttime, endtime));

				// 配送成功单数、应收金额、应退金额
				List<JSONObject> peisongchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(),
						DeliveryStateEnum.PeiSongChengGong.getValue(), starttime, endtime);

				// 配送成功单数、应收金额、应退金额
				if (peisongchenggongList != null) {
					// 配送成功单数
					todayDeliveryDTO.setToday_chenggong(peisongchenggongList.get(0).getLong("num"));
				}
				// 拒收
				todayDeliveryDTO.setToday_jushou(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateAndOrderType(branch.getBranchid(), DeliveryStateEnum.JuShou.getValue(),
						CwbOrderTypeIdEnum.Peisong.getValue(), starttime, endtime));
				// 部分拒收
				todayDeliveryDTO.setToday_bufenjushou(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branch.getBranchid(), DeliveryStateEnum.BuFenTuiHuo.getValue(), starttime,
						endtime));
				// 今日滞留
				todayDeliveryDTO.setToday_zhiliu(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateByJinri(FlowOrderTypeEnum.YiShenHe.getValue(), branch.getBranchid(),
						DeliveryStateEnum.FenZhanZhiLiu.getValue(), starttime, endtime));

				// 中转
				todayDeliveryDTO.setToday_zhongzhuan(logTodayDAO.getTodaybyBranchid(branch.getBranchid(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), starttime, endtime));

				todayDeliveryDTO.setSearchdate(searchdate);

				todaydeliverylist.add(todayDeliveryDTO);
			}

			return JacksonMapper.getInstance().writeValueAsString(todaydeliverylist);

		} else {

			List<List<DeliverystasticsDto>> datatoallist = new ArrayList<List<DeliverystasticsDto>>();
			for (Branch branch : branchlist) {
				List<JSONObject> userdeliverylist = deliveryStateDAO.getDeliveryStasticsByUserId(branch.getBranchid(), starttime, endtime);

				List<DeliverystasticsDto> todaydeliverylist = new ArrayList<DeliverystasticsDto>();

				for (JSONObject data : userdeliverylist) {
					DeliverystasticsDto todayDeliveryDTO = new DeliverystasticsDto();
					todayDeliveryDTO.setBranchid(branch.getBranchid());
					todayDeliveryDTO.setBranchname(branch.getBranchname());
					todayDeliveryDTO.setRealname(data.getString("realname"));
					todayDeliveryDTO.setToday_linghuo(data.getLong("num"));
					todayDeliveryDTO.setToday_chenggong(data.getLong("success"));
					todayDeliveryDTO.setToday_jushou(data.getLong("jushou"));
					todayDeliveryDTO.setToday_bufenjushou(data.getLong("bufenjushou"));
					todayDeliveryDTO.setToday_zhiliu(data.getLong("zhiliu"));
					todayDeliveryDTO.setSearchdate(searchdate);

					todaydeliverylist.add(todayDeliveryDTO);

				}
				datatoallist.add(todaydeliverylist);

			}

			return JacksonMapper.getInstance().writeValueAsString(datatoallist);

		}
	}

}
