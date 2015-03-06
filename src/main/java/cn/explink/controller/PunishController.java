package cn.explink.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.AppearWindowDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.PunishDAO;
import cn.explink.dao.PunishTypeDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Punish;
import cn.explink.domain.PunishType;
import cn.explink.domain.User;
import cn.explink.enumutil.PunishlevelEnum;
import cn.explink.enumutil.PunishtimeEnum;
import cn.explink.service.Excel2003Extractor;
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.ExcelExtractor;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ResultCollectorManager;
import cn.explink.service.UserService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

@Controller
@RequestMapping("/punish")
public class PunishController {
	@Autowired
	Excel2007Extractor excel2007Extractor;

	@Autowired
	Excel2003Extractor excel2003Extractor;
	@Autowired
	ResultCollectorManager resultCollectorManager;

	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	PunishDAO punishDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	AppearWindowDao appearWindowDao;
	@Autowired
	PunishTypeDAO punishTypeDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	CustomerDAO customerDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		List<Branch> branchlist = this.branchDAO.getAllBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<PunishType> punishTypeList = this.punishTypeDAO.getAllPunishTypeByName();
		List<Customer> customers = this.customerDAO.getAllCustomers();
		model.addAttribute("customers", customers);
		model.addAttribute("branchlist", branchlist);
		model.addAttribute("userList", userList);
		model.addAttribute("punishTypeList", punishTypeList);
		return "/punish/add";
	}

	@RequestMapping("/importPage")
	public String importPage(Model model) throws Exception {
		return "/punish/importPage";
	}

	@RequestMapping("/importData")
	public String importData(Model model, final HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "Filedata", required = false) final MultipartFile file)
			throws Exception {
		final ExcelExtractor excelExtractor = this.getExcelExtractor(file);
		final InputStream inputStream = file.getInputStream();
		final SecurityContext scontext = this.securityContextHolderStrategy.getContext();
		int count = 0;
		if (excelExtractor != null) {
			/*
			 * ExecutorService newSingleThreadExecutor =
			 * Executors.newSingleThreadExecutor();
			 * newSingleThreadExecutor.execute(new Runnable() {
			 * 
			 * @Override public void run() {
			 */
			try {
				PunishController.this.securityContextHolderStrategy.setContext(scontext);
				count = PunishController.this.processFile(excelExtractor, inputStream);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
			/*
			 * } });
			 */
		} else {
		}
		model.addAttribute("showData", 1);
		model.addAttribute("state", -1);
		model.addAttribute("count", count);
		List<Branch> branchlist = this.branchDAO.getAllBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<PunishType> punishTypeList = this.punishTypeDAO.getAllPunishTypeByName();
		model.addAttribute("branchlist", branchlist);
		model.addAttribute("userList", userList);
		model.addAttribute("punishTypeList", punishTypeList);
		return "/punish/list";
	}

	protected int processFile(ExcelExtractor excelExtractor, InputStream inputStream) {
		return excelExtractor.extract(inputStream);

	}

	private ExcelExtractor getExcelExtractor(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename.endsWith("xlsx")) {
			return this.excel2007Extractor;
		} else if (originalFilename.endsWith(".xls")) {
			return this.excel2003Extractor;
		}
		return null;
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") int id) throws Exception {
		List<Branch> branchlist = this.branchDAO.getAllBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<PunishType> punishTypeList = this.punishTypeDAO.getAllPunishTypeByName();
		Punish punish = this.punishDAO.getPunishById(id);
		List<Customer> customers = this.customerDAO.getAllCustomers();
		model.addAttribute("customers", customers);
		model.addAttribute("branchlist", branchlist);
		model.addAttribute("userList", userList);
		model.addAttribute("punishTypeList", punishTypeList);
		model.addAttribute("punish", punish == null ? new Punish() : punish);
		return "/punish/edit";
	}

	@RequestMapping("/state")
	public String state(Model model, @RequestParam(value = "id", defaultValue = "0", required = false) int id) throws Exception {
		List<Branch> branchlist = this.branchDAO.getAllBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<PunishType> punishTypeList = this.punishTypeDAO.getAllPunishTypeByName();
		Punish punish = this.punishDAO.getPunishById(id);
		model.addAttribute("branchlist", branchlist);
		model.addAttribute("userList", userList);
		model.addAttribute("punishTypeList", punishTypeList);
		model.addAttribute("punish", punish == null ? new Punish() : punish);
		return "/punish/state";
	}

	@RequestMapping("/stateBactch")
	public @ResponseBody
	String stateBactch(Model model, @RequestParam(value = "ids", defaultValue = "", required = false) String ids, @RequestParam(value = "userids", defaultValue = "", required = false) String userids,
			@RequestParam(value = "state", defaultValue = "0", required = false) int state) throws Exception {

		this.punishDAO.updateStateBatchPunish(ids, state);
		/*
		 * String[] userid = userids.split(","); for (int i = 0; i <
		 * userid.length; i++) { int uid = Integer.parseInt(userid[i]); if
		 * (state == 1) { this.appearWindowDao.creWindowTime("扣罚登记", 7, uid, 1);
		 * } else { this.appearWindowDao.creWindowTime("扣罚登记取消", 8, uid, 1); }
		 * 
		 * }
		 */
		return "{\"errorCode\":0,\"error\":\" 操作成功\"}";
	}

	@RequestMapping("/create")
	public @ResponseBody
	String create(@RequestParam(value = "cwb", defaultValue = "", required = false) String cwb, @RequestParam(value = "punishid", defaultValue = "0", required = false) long punishid,
			@RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid, @RequestParam(value = "userid", defaultValue = "0", required = false) long userid,
			@RequestParam(value = "punishtime", defaultValue = "0", required = false) long punishtime, @RequestParam(value = "punishlevel", defaultValue = "0", required = false) long punishlevel,
			@RequestParam(value = "punishfee", defaultValue = "0", required = false) BigDecimal punishfee, @RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid,
			@RequestParam(value = "punishcontent", defaultValue = "0", required = false) String punishcontent, @RequestParam(value = "realfee", defaultValue = "0", required = false) BigDecimal realfee)
			throws Exception {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb.trim());
		/*
		 * Punish pu = this.punishDAO.getPunishByCwb(cwb); if (pu != null) {
		 * return "{\"errorCode\":1,\"error\":\" 扣罚信息登记失败！扣罚信息已经存在\"}"; }
		 */
		if (co == null) {
			return "{\"errorCode\":1,\"error\":\" 扣罚信息登记失败！订单号不存在\"}";
		}
		Punish punish = new Punish();
		punish.setCwb(cwb.trim());
		punish.setPunishid(punishid);
		punish.setCustomerid(customerid);
		punish.setBranchid(branchid);
		punish.setUserid(userid);
		punish.setPunishtime(punishtime);
		punish.setPunishlevel(punishlevel);
		punish.setPunishfee(punishfee);
		punish.setPunishcontent(punishcontent);
		punish.setRealfee(realfee);
		punish.setCreateuser(this.getSessionUser().getUserid());
		try {
			this.punishDAO.createPunish(punish);
			return "{\"errorCode\":0,\"error\":\"扣罚信息登记成功！\"}";

		} catch (Exception e) {
			this.logger.error("扣罚信息登记失败：" + e);
			return "{\"errorCode\":1,\"error\":\"扣罚信息登记失败！\"}";
		}
	}

	@RequestMapping("/update")
	public @ResponseBody
	String update(@RequestParam(value = "cwb", defaultValue = "", required = false) String cwb, @RequestParam(value = "id", defaultValue = "0", required = false) long id,
			@RequestParam(value = "punishid", defaultValue = "0", required = false) long punishid, @RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid,
			@RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid, @RequestParam(value = "userid", defaultValue = "0", required = false) long userid,
			@RequestParam(value = "punishtime", defaultValue = "0", required = false) long punishtime, @RequestParam(value = "punishlevel", defaultValue = "0", required = false) long punishlevel,
			@RequestParam(value = "punishfee", defaultValue = "0", required = false) BigDecimal punishfee,
			@RequestParam(value = "punishcontent", defaultValue = "0", required = false) String punishcontent, @RequestParam(value = "realfee", defaultValue = "0", required = false) BigDecimal realfee)
			throws Exception {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb.trim());
		if (co == null) {
			return "{\"errorCode\":1,\"error\":\" 扣罚信息更新失败！订单号不存在\"}";
		}
		Punish punish = new Punish();
		punish.setId(id);
		punish.setCwb(cwb.trim());
		punish.setPunishid(punishid);
		punish.setCustomerid(customerid);
		punish.setBranchid(branchid);
		punish.setUserid(userid);
		punish.setPunishtime(punishtime);
		punish.setPunishlevel(punishlevel);
		punish.setPunishfee(punishfee);
		punish.setPunishcontent(punishcontent);
		punish.setRealfee(realfee);
		punish.setCreateuser(this.getSessionUser().getUserid());
		try {
			this.punishDAO.updatePunish(punish);
			return "{\"errorCode\":0,\"error\":\"扣罚信息更新成功！\"}";

		} catch (Exception e) {
			this.logger.error("扣罚信息更新失败：" + e);
			return "{\"errorCode\":1,\"error\":\"扣罚信息更新失败！\"}";
		}
	}

	@RequestMapping("/updateState")
	public @ResponseBody
	String updateState(@RequestParam(value = "cwb", defaultValue = "", required = false) String cwb, @RequestParam(value = "id", defaultValue = "0", required = false) int id,
			@RequestParam(value = "state", defaultValue = "0", required = false) int state) throws Exception {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb.trim());
		if (co == null) {
			return "{\"errorCode\":1,\"error\":\" 审核失败！订单号不存在\"}";
		}

		try {
			state = state == 1 ? 0 : 1;
			this.punishDAO.updateStatePunish(id, state);
			return "{\"errorCode\":0,\"error\":\"审核成功！\"}";

		} catch (Exception e) {
			this.logger.error("审核失败：" + e);
			return "{\"errorCode\":1,\"error\":\"审核失败！\"}";
		}
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "punishid", defaultValue = "0", required = false) long punishid, @RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid,
			@RequestParam(value = "userid", defaultValue = "0", required = false) long userid, @RequestParam(value = "punishlevel", defaultValue = "0", required = false) long punishlevel,
			@RequestParam(value = "isnow", defaultValue = "0", required = false) long isnow, @RequestParam(value = "state", defaultValue = "-1", required = false) int state) {
		List<Branch> branchlist = this.branchDAO.getAllBranches();
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<User> userList = this.userDAO.getAllUser();
		List<PunishType> punishTypeList = this.punishTypeDAO.getAllPunishTypeByName();
		model.addAttribute("branchlist", branchlist);
		model.addAttribute("userList", userList);
		model.addAttribute("punishTypeList", punishTypeList);
		model.addAttribute("customerList", customerList);
		List<Punish> punishList = new ArrayList<Punish>();
		int count = 0;
		if (isnow > 0) {
			punishList = this.punishDAO.getPunishList(cwb, punishid, userid, branchid, punishlevel, state, page);
			count = this.punishDAO.getPunishCount(cwb, punishid, userid, branchid, punishlevel, state, page);
		}
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("punishList", punishList);
		model.addAttribute("userid", userid);
		model.addAttribute("branchid", branchid);
		model.addAttribute("punishid", punishid);
		model.addAttribute("cwb", cwb);
		model.addAttribute("state", state);
		model.addAttribute("punishlevel", punishlevel);
		return "/punish/list";
	}

	@RequestMapping("/deletePunish")
	public @ResponseBody
	String deletePunish(@RequestParam(value = "id", defaultValue = "0", required = false) long id) throws Exception {

		int count = this.punishDAO.deletePunish(id);
		if (count > 0) {
			return "{\"errorCode\":0,\"error\":\"删除成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"删除失败\"}";
		}
	}

	@RequestMapping("/selectUser")
	public @ResponseBody
	User selectUser(@RequestParam(value = "userid", defaultValue = "0", required = false) long userid) throws Exception {

		User user = this.userDAO.getUserByUserid(userid);
		return user;
	}

	@RequestMapping("/selectBranch")
	public @ResponseBody
	List<User> selectBranch(@RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid) throws Exception {

		List<User> users = new ArrayList<User>();
		if (branchid == 0) {
			users = this.userDAO.getAllUser();
		} else {
			users = this.userDAO.getUserByBranchid(branchid);
		}
		return users;
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(HttpServletResponse response, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "punishid", defaultValue = "0", required = false) long punishid, @RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid,
			@RequestParam(value = "userid", defaultValue = "0", required = false) long userid, @RequestParam(value = "punishlevel", defaultValue = "0", required = false) long punishlevel,
			@RequestParam(value = "state", defaultValue = "-1", required = false) int state) {
		String[] cloumnName1 = new String[12]; // 导出的列名
		String[] cloumnName2 = new String[12]; // 导出的英文列名

		this.setExcelstyle(cloumnName1, cloumnName2);
		final String[] cloumnName3 = cloumnName1;
		final String[] cloumnName4 = cloumnName2;
		String sheetName = "扣罚信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			final List<Punish> punishList = this.punishDAO.getPunishforExcel(cwb, punishid, userid, branchid, punishlevel, state);
			final List<Branch> branchs = this.branchDAO.getAllBranches();
			final List<Customer> customers = this.customerDAO.getAllCustomers();
			final List<User> users = this.userDAO.getAllUser();
			final List<PunishType> punishTypeList = this.punishTypeDAO.getAllPunishTypeByName();

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					style.setAlignment(CellStyle.ALIGN_CENTER);
					for (int k = 0; k < punishList.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName4.length; i++) {
							sheet.setColumnWidth(i, 15 * 256);
							if (cloumnName4[i].equals("Punishcontent")) {
								sheet.setColumnWidth(i, 35 * 256);
							}
							if (cloumnName4[i].equals("Createtime")) {
								sheet.setColumnWidth(i, 25 * 256);
							}
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = this.setExcelObject(cloumnName4, a, i, punishList.get(k));

							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}

				private Object setExcelObject(String[] cloumnName, Object a, int i, Punish pu) {
					try {
						if (cloumnName[i].equals("Branchid")) {
							for (Branch b : branchs) {
								if (b.getBranchid() == pu.getBranchid()) {
									a = b.getBranchname();
								}
							}
						} else if (cloumnName[i].equals("Userid")) {

							for (User u : users) {
								if (u.getUserid() == pu.getUserid()) {
									a = u.getRealname();
								}
							}
						} else if (cloumnName[i].equals("Customerid")) {

							for (Customer cu : customers) {
								if (cu.getCustomerid() == pu.getCustomerid()) {
									a = cu.getCustomername();
								}
							}
						} else if (cloumnName[i].equals("Createuser")) {

							for (User u : users) {
								if (u.getUserid() == pu.getCreateuser()) {
									a = u.getRealname();
								}
							}
						} else if (cloumnName[i].equals("State")) {

							a = pu.getState() == 1 ? "已审核" : "未审核";
						} else if (cloumnName[i].equals("Punishid")) {
							for (PunishType pty : punishTypeList) {
								if (pty.getId() == pu.getPunishid()) {
									a = pty.getName();
								}
							}
						} else if (cloumnName[i].equals("Punishtime")) {
							for (PunishtimeEnum pty : PunishtimeEnum.values()) {
								if (pty.getValue() == pu.getPunishtime()) {
									a = pty.getText();
								}
							}
						} else if (cloumnName[i].equals("Punishlevel")) {
							for (PunishlevelEnum pty : PunishlevelEnum.values()) {
								if (pty.getValue() == pu.getPunishlevel()) {
									a = pty.getText();
								}
							}
						} else {
							a = pu.getClass().getMethod("get" + cloumnName[i]).invoke(pu);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return a;
				}
			};
			excelUtil.excel(response, cloumnName3, sheetName, fileName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setExcelstyle(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "供货商";
		cloumnName2[1] = "Customerid";
		cloumnName1[2] = "扣罚类型";
		cloumnName2[2] = "Punishid";
		cloumnName1[3] = "扣罚站点";
		cloumnName2[3] = "Branchid";
		cloumnName1[4] = "扣罚人员";
		cloumnName2[4] = "Userid";
		cloumnName1[5] = "扣罚时效";
		cloumnName2[5] = "Punishtime";
		cloumnName1[6] = "优先级别";
		cloumnName2[6] = "Punishlevel";
		cloumnName1[7] = "扣罚金额";
		cloumnName2[7] = "Punishfee";
		cloumnName1[8] = "实扣金额";
		cloumnName2[8] = "Realfee";
		cloumnName1[9] = "扣罚内容";
		cloumnName2[9] = "Punishcontent";
		cloumnName1[10] = "创建人";
		cloumnName2[10] = "Createuser";
		cloumnName1[11] = "创建时间";
		cloumnName2[11] = "Createtime";
		cloumnName1[12] = "审核状态";
		cloumnName2[12] = "State";

	}

}