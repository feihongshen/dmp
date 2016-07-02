package cn.explink.b2c.auto.order.util;

import cn.explink.enumutil.PaytypeEnum;

/**
 * mq订单业务 辅助类
 * @author jian.xie
 *
 */
public class MqOrderBusinessUtil {

	/**
	 * 根据tps的支付方式，返回dmp的支付方式
	 * @param payment
	 * @return
	 */
	public static int getPayTypeValue(int payment){
		int pay = 0;
		switch (payment) {
		case -1:
		case 0:
			pay = PaytypeEnum.Xianjin.getValue();
			break;
		case 1:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 2:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 3:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 4:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 5:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 6:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 7:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 8:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 9:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 10:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 11:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 12:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 13:
			pay = PaytypeEnum.Zhipiao.getValue();
			break;
		case 14:
			pay = PaytypeEnum.Qita.getValue();
			break;
		}		
		return pay;
	}
	
	}
