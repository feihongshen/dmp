package cn.explink.service;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbKuaiDiDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbKuaiDi;
import cn.explink.domain.CwbKuaiDiView;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.util.DateTimeUtil;

@Service
public class CwbKuaiDiService {
	@Autowired
	CwbKuaiDiDAO cwbKuaiDiDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	AddressMatchService addressMatchService;

	public CwbKuaiDiView getKuaiDiView(CwbOrder order, CwbKuaiDi kd, List<User> users, List<Branch> branchs) {
		CwbKuaiDiView ckv = new CwbKuaiDiView();
		ckv.setAllfee(kd.getAllfee());
		ckv.setConsigneeaddress(order.getConsigneeaddress());
		ckv.setConsigneeareacode(kd.getConsigneeareacode());
		ckv.setConsigneemobilekf(order.getConsigneemobileOfkf());
		ckv.setConsigneemobile(order.getConsigneemobile());
		ckv.setConsigneenamekf(order.getConsigneenameOfkf());
		ckv.setConsigneename(order.getConsigneename());
		ckv.setConsigneeno(order.getConsigneeno());
		ckv.setConsigneephone(order.getConsigneephone());
		ckv.setConsigneephonekf(order.getConsigneephoneOfkf());
		// ckv.setConsigneephone(consigneephone, guessMobile);
		ckv.setConsigneepostcode(order.getConsigneepostcode());
		ckv.setCwb(order.getCwb());
		ckv.setEdittime(kd.getEdittime());
		ckv.setEdituserid(kd.getEdituserid());//
		ckv.setFlowordertype(order.getFlowordertype());
		ckv.setFuturefee(kd.getFuturefee());
		ckv.setInsuredrate(kd.getInsuredrate());
		ckv.setLanshoubranchid(kd.getLanshoubranchid());
		ckv.setLanshoubranchname(getBranchName(branchs, kd.getLanshoubranchid()));//
		ckv.setLanshoutime(kd.getLanshoutime());
		ckv.setLanshouuserid(kd.getLanshouuserid());
		ckv.setLanshouusername(getUserName(users, kd.getLanshouuserid()));//
		ckv.setNowfee(kd.getNowfee());
		ckv.setOtherfee(kd.getOtherfee());
		ckv.setPackagefee(kd.getPackagefee());
		ckv.setPackingfee(kd.getPackingfee());
		ckv.setPaisongbranchid(kd.getPaisongbranchid());
		ckv.setPaisongtime(kd.getPaisongtime());
		ckv.setPaisonguserid(kd.getPaisonguserid());
		ckv.setPaytype(kd.getPaytype());
		ckv.setRealweight(kd.getRealweight());
		ckv.setSafefee(kd.getSafefee());
		ckv.setSendconsigneeaddress(kd.getSendconsigneeaddress());
		ckv.setSendconsigneeareacode(kd.getSendconsigneeareacode());
		ckv.setSendconsigneecity(kd.getSendconsigneecity());
		ckv.setSendconsigneecompany(kd.getSendconsigneecompany());
		ckv.setSendconsigneemobile(kd.getSendconsigneemobile());
		ckv.setSendconsigneename(kd.getSendconsigneename());
		ckv.setSendconsigneephone(kd.getSendconsigneephone());
		ckv.setSendconsigneepostcode(kd.getSendconsigneepostcode());
		ckv.setSignman(kd.getSignman());
		ckv.setSignstate(kd.getSignstate());
		ckv.setSigntime(kd.getSigntime());
		ckv.setTransitfee(kd.getTransitfee());
		ckv.setWeightfee(kd.getWeightfee());
		ckv.setZhongzhuanbranchid(kd.getZhongzhuanbranchid());
		ckv.setZhongzhuanzhanbranchname(getBranchName(branchs, kd.getZhongzhuanbranchid()));
		ckv.setPaisongusername(getUserName(users, kd.getPaisonguserid()));
		ckv.setPaisongzhandianname(getBranchName(branchs, kd.getPaisongbranchid()));
		ckv.setCwbcity(order.getCwbcity());
		ckv.setSendcarname(order.getSendcarname());
		ckv.setSendcarnum(order.getSendcarnum());
		ckv.setShoujianrencompany(kd.getShoujianrencompany());
		ckv.setCarrealweight(order.getCarrealweight());
		ckv.setRemark(kd.getRemark());
		ckv.setReceivablefee(order.getReceivablefee());
		String[] size = order.getCarsize().split("_");
		if (size.length == 3) {
			ckv.setChang(Long.parseLong((size[0] == null || "".equals(size[0])) ? "0" : size[0]));
			ckv.setKuan(Long.parseLong((size[1] == null || "".equals(size[1])) ? "0" : size[1]));
			ckv.setGao(Long.parseLong((size[2] == null || "".equals(size[2])) ? "0" : size[2]));
		}
		return ckv;
	}

	private String getUserName(List<User> users, long lanshouuserid) {
		String name = "";
		for (User user : users) {
			if (user.getUserid() == lanshouuserid) {
				name = user.getRealname();
				break;
			}
		}
		return name;
	}

	private String getBranchName(List<Branch> branchs, long lanshoubranchid) {
		String name = "";
		for (Branch branch : branchs) {
			if (branch.getBranchid() == lanshoubranchid) {
				name = branch.getBranchname();
				break;
			}
		}
		return name;
	}

	/**
	 * 更新信息
	 * 
	 * @param request
	 * @param response
	 * @param user
	 */
	// @Transactional
	public void savebulu(HttpServletRequest request, HttpServletResponse response, User user) {
		CwbKuaiDi kd = new CwbKuaiDi();
		// 必选字段 付款方式、收件人、收件人地址、收件人手机
		kd.setPaytype(Long.parseLong(request.getParameter("paytype") == null ? "0" : request.getParameter("paytype")));
		String consigneename = request.getParameter("consigneename");
		String consigneeaddress = request.getParameter("consigneeaddress");
		String consigneemobile = request.getParameter("consigneemobile");
		String consigneephone = request.getParameter("consigneephone");
		String cwbcity = request.getParameter("cwbcity");
		String consigneepostcode = request.getParameter("consigneepostcode");
		String sendcarname = request.getParameter("sendcarname");
		long sendcarnum = Long.parseLong((request.getParameter("sendcarnum") == null || "".equals(request.getParameter("sendcarnum"))) ? "1" : request.getParameter("sendcarnum"));
		BigDecimal carrealweight = BigDecimal.valueOf(Double.parseDouble((request.getParameter("carrealweight") == null || "".equals(request.getParameter("carrealweight"))) ? "0.0" : request
				.getParameter("carrealweight")));

		// 收件人信息
		String cwb = request.getParameter("cwb");
		kd.setCwb(cwb);
		kd.setSendconsigneename(request.getParameter("sendconsigneename"));// 寄件人
		kd.setSendconsigneeaddress(request.getParameter("sendconsigneeaddress"));
		kd.setSendconsigneeareacode(request.getParameter("sendconsigneeareacode"));
		kd.setSendconsigneecity(request.getParameter("sendconsigneecity"));
		kd.setSendconsigneecompany(request.getParameter("sendconsigneecompany"));
		kd.setSendconsigneemobile(request.getParameter("sendconsigneemobile"));
		kd.setSendconsigneephone(request.getParameter("sendconsigneephone"));
		kd.setSendconsigneepostcode(request.getParameter("sendconsigneepostcode"));
		kd.setShoujianrencompany(request.getParameter("shoujianrencompany"));
		kd.setConsigneeareacode(request.getParameter("consigneeareacode"));

		// 费用
		String packingfee = (request.getParameter("packingfee") == null || "".equals(request.getParameter("packingfee"))) ? "0.0" : request.getParameter("packingfee");
		String safefee = (request.getParameter("safefee") == null || "".equals(request.getParameter("safefee"))) ? "0.0" : request.getParameter("safefee");

		//
		BigDecimal receivablefee = BigDecimal.valueOf(Double.parseDouble((request.getParameter("receivablefee") == null || "".equals(request.getParameter("receivablefee"))) ? "0.0" : request
				.getParameter("receivablefee")));
		String packagefee = (request.getParameter("packagefee") == null || "".equals(request.getParameter("packagefee"))) ? "0.0" : request.getParameter("packagefee");
		String transitfee = (request.getParameter("transitfee") == null || "".equals(request.getParameter("transitfee"))) ? "0.0" : request.getParameter("transitfee");
		String weightfee = (request.getParameter("weightfee") == null || "".equals(request.getParameter("weightfee"))) ? "0.0" : request.getParameter("weightfee");
		String realweight = (request.getParameter("realweight") == null || "".equals(request.getParameter("realweight"))) ? "0.0" : request.getParameter("realweight");
		String nowfee = (request.getParameter("nowfee") == null || "".equals(request.getParameter("nowfee"))) ? "0.0" : request.getParameter("nowfee");
		String otherfee = (request.getParameter("otherfee") == null || "".equals(request.getParameter("otherfee"))) ? "0.0" : request.getParameter("otherfee");
		String allfee = (request.getParameter("allfee") == null || "".equals(request.getParameter("allfee"))) ? "0.0" : request.getParameter("allfee");
		String insuredrate = (request.getParameter("insuredrate") == null || "".equals(request.getParameter("insuredrate"))) ? "0.0" : request.getParameter("insuredrate");

		kd.setPackagefee(BigDecimal.valueOf(Double.parseDouble(packagefee)));
		kd.setPackingfee(BigDecimal.valueOf(Double.parseDouble(packingfee)));
		kd.setSafefee(BigDecimal.valueOf(Double.parseDouble(safefee)));
		kd.setTransitfee(BigDecimal.valueOf(Double.parseDouble(transitfee)));
		kd.setWeightfee(BigDecimal.valueOf(Double.parseDouble(weightfee)));
		kd.setRealweight(BigDecimal.valueOf(Double.parseDouble(realweight)));
		kd.setNowfee(BigDecimal.valueOf(Double.parseDouble(nowfee)));
		kd.setOtherfee(BigDecimal.valueOf(Double.parseDouble(otherfee)));
		kd.setAllfee(BigDecimal.valueOf(Double.parseDouble(allfee)));
		kd.setInsuredrate(BigDecimal.valueOf(Double.parseDouble(insuredrate)));

		/*
		 * shoujianrencompany changkuangao
		 */

		// 签收信息
		kd.setSignstate(Long.parseLong(request.getParameter("signstate") == null ? "0" : request.getParameter("signstate")));
		kd.setSignman(request.getParameter("signman"));
		kd.setSigntime(request.getParameter("signtime"));
		kd.setRemark(request.getParameter("remark"));
		kd.setEdituserid(user.getUserid());
		kd.setEdittime(DateTimeUtil.getNowTime());

		String chang = request.getParameter("chang");
		String kuan = request.getParameter("kuan");
		String gao = request.getParameter("gao");

		String carsize = chang + "_" + kuan + "_" + gao;
		cwbKuaiDiDAO.updateKuDi(kd);
		cwbDAO.updateCwbByParams(cwb, consigneename, consigneeaddress, consigneemobile, cwbcity, consigneepostcode, sendcarname, sendcarnum, carrealweight, receivablefee, carsize, consigneephone);
		addressMatchService.matchAddress(user.getUserid(), cwb);// 地址库匹配

	}

}
