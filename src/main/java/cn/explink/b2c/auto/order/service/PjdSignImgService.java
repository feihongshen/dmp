package cn.explink.b2c.auto.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.DeliveryStateDAO;

@Service
public class PjdSignImgService {
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	
	/**
	 * 根据订单号pjd签收人签名图片地址
	 *
	 * @param cwb,imgUrl
	 * @return
	 */
	@Transactional
	public void saveSignImg(String cwb,String imgUrl){
		this.deliveryStateDAO.saveSignImgByCwb(cwb, imgUrl);
	}
}
