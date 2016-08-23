package cn.explink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.SmtCwbDAO;
import cn.explink.domain.SmtCwb;

/**
 * 退货信息-service
 * @author chunlei05.li
 * @date 2016年8月22日 下午3:55:59
 */
@Service
public class SmtCwbService {
	
	@Autowired
	private SmtCwbDAO SmtCwbDAO;
	
	/**
	 * 保存
	 * @author chunlei05.li
	 * @date 2016年8月22日 下午4:14:37
	 * @param SmtCwb
	 * @return
	 */
	public int saveSmtCwb(SmtCwb SmtCwb) {
		return this.SmtCwbDAO.saveSmtCwb(SmtCwb);
	}
	
	/**
	 * 根据订单号查询
	 * @author chunlei05.li
	 * @date 2016年8月22日 下午4:18:37
	 * @param cwb
	 * @return
	 */
	public SmtCwb getSmtCwbByCwb(String cwb) {
		return this.SmtCwbDAO.getSmtCwbByCwb(cwb);
	}
}
