package cn.explink.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.GoodsStatusNumDao;


@Controller
@RequestMapping("/GoodsStatusNum")
public class GoodsStatusNumController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private GoodsStatusNumDao goodsStatusNumDao;
	@RequestMapping("/findGoodsStatusNum/{flowordertype}/{branchid}")	
	public @ResponseBody int findGoodsStatusNum(
			@PathVariable(value="flowordertype") int flowordertype,
			@PathVariable(value="branchid") int branchid){
		int goodsStatus=goodsStatusNumDao.findGoodsStatusNum(flowordertype,branchid);
		logger.info("符合条件数量{}",goodsStatus);
		return goodsStatus;	
	}

}
