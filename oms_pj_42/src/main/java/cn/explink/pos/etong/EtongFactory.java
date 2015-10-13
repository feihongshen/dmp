package cn.explink.pos.etong;

import com.hyyt.elog.dmp.ObjectFactory;

public class EtongFactory {

	private static final ObjectFactory etFactory = new ObjectFactory();

	private EtongFactory() {
	}

	public static ObjectFactory getInstance() {
		return etFactory;
	}

}
