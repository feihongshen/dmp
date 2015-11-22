package cn.explink.b2c.bjUnion;

import java.util.Map;

import org.springframework.stereotype.Service;
@Service
public interface BJUnionInterface {
	
	public String doLoginReq(Map<String, String> map,Object obj) throws Exception;
	public String doQueryReq(Map<String, String> map,Object obj) throws Exception;
	public String doSignReq(Map<String, String> map,Object obj) throws Exception;
	public String doRevokeReq(Map<String, String> map,Object obj) throws Exception;
}
