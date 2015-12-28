package cn.explink.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.BankDao;

@Service
public class BankService {

	@Autowired
	private BankDao bankDao;

	/**
	 *
	 * @Title: getTlBankList
	 * @description 获取通联的银行列表
	 * @author 刘武强
	 * @date  2015年12月22日上午8:56:15
	 * @param  @return
	 * @return  List<JSONObject>
	 * @throws
	 */
	public List<JSONObject> getTlBankList() {
		List<JSONObject> bankList = new ArrayList<JSONObject>();
		bankList = this.bankDao.getTlBank();
		return bankList;
	}

	/**
	 * @Title: getCftBankList
	 * @description 获取财付通的银行列表
	 * @author 刘武强
	 * @date  2015年12月22日上午8:56:52
	 * @param  @return
	 * @return  List<JSONObject>
	 * @throws
	 */
	public List<JSONObject> getCftBankList() {
		List<JSONObject> bankList = new ArrayList<JSONObject>();
		bankList = this.bankDao.getCftBank();
		return bankList;
	}
}
