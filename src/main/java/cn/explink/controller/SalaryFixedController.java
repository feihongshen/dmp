/**
 *
 */
package cn.explink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/salaryFixed")
public class SalaryFixedController {

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, @RequestParam(value = "realname", required = false, defaultValue = "") String realname,
			 @RequestParam(value = "idcard", required = false, defaultValue = "") String idcard){

		return "salary/salaryFixed/list";
	}
}
