/**
 *
 */
package cn.explink.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.SalaryDAO;
import cn.explink.domain.Salary;
import cn.explink.util.Page;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/salaryFixed")
public class SalaryFixedController {
	@Autowired
	SalaryDAO salaryDAO;

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page,
			@RequestParam(value = "realname", required = false, defaultValue = "") String realname,
			@RequestParam(value = "idcard",required = false,defaultValue = "") String idcard,
			@RequestParam(value = "isnow",required = false,defaultValue = "0") int isnow,
			Model model) {
		List<Salary> salaryList=this.salaryDAO.getSalaryByRealnameAndIdcard(page, realname, idcard);
		int count= this.salaryDAO.getSalaryByRealnameAndIdcardCounts(realname, idcard);
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("salaryList", salaryList);
		model.addAttribute("realname", realname);
		model.addAttribute("idcard", idcard);
		return "salary/salaryFixed/list";
	}
	@RequestMapping("/delete")
	public  @ResponseBody
	Map<String, Long> delete(@RequestParam(value = "ids",required = false,defaultValue = "") String ids) throws Exception {
		long counts=0;
		if((ids!=null)&&(ids.length()>0)){
		counts=this.salaryDAO.deleteSalaryByids(ids);
		}
		Map<String, Long> map=new HashMap<String, Long>();
		map.put("counts", counts);
		return map;
	}

	@Test
	public void test() {
		Class class1 = Salary.class;
		String sql="INSERT INTO `dmp_pj`.`express_ops_salary_detail` ( `branchid`, `realname`, `idcard`, `accountSingle`, `salarybasic`, `salaryjob`, `salarypush`, `agejob`, `bonusfuel`, `bonusfixed`, `bonusweather`, `penalizecancel`, `bonusother1`, `bonusother2`, `bonusother3`, `bonusother4`, `bonusother5`, `bonusother6`, `overtimework`, `attendance`, `security`, `gongjijin`, `foul`, `goods`, `dorm`, `penalizeother1`, `penalizeother2`, `penalizeother3`, `penalizeother4`, `penalizeother5`, `penalizeother6`, `imprestgoods`, `imprestother1`, `imprestother2`, `imprestother3`, `imprestother4`, `imprestother5`, `imprestother6`, `salaryaccrual`, `tax`, `salary`) VALUES ( 222, '王永贺', '622722199102152231', 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222, 222);";
for(int i=0;i<=10;i++){
	sql=sql.replace("0", i+"");
	sql=sql.replace("222", (222+i)+"");
	System.out.println(sql);
}
		System.out.println(class1.getDeclaredFields().length);
	}
}
