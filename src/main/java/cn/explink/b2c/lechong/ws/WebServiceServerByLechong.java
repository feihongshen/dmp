package cn.explink.b2c.lechong.ws;

import javax.jws.WebService;

@WebService
public interface WebServiceServerByLechong {
	public String BuildOrder(String xmlstr);

	public String CancelOrder(String xmlstr);
}
