package cn.explink.b2c.maisike;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.maisike.branchsyn_json.RespStore;
import cn.explink.b2c.maisike.branchsyn_json.Stores;
import cn.explink.b2c.maisike.stores.StoresDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 站点信息同步接口
 * 
 * @author Administrator
 *
 */
@Service
public class MaisikeService_branchSyn extends MaisikeService {

	@Autowired
	StoresDAO storesDAO;

	private Logger logger = LoggerFactory.getLogger(MaisikeService_branchSyn.class);

	/**
	 * 迈思可站点信息同步接口
	 */
	public long syn_maisikeBranchs() {
		try {

			int b2cenum = B2cEnum.Maisike.getKey();

			int isOpenFlag = jointService.getStateForJoint(b2cenum);
			if (isOpenFlag == 0) {
				logger.info("未配置迈思可对接");
				return -1;
			}

			Maisike maisike = super.getMaisike(b2cenum);
			if (maisike == null) {
				logger.info("未配置迈思可对接");
				return -1;
			}

			String fn = "store.list.get";
			String appname = maisike.getAppname();
			String apptime = DateTimeUtil.getNowTime();
			String appkey = MD5Util.md5(appname + maisike.getApp_key() + apptime); // 签名

			Map<String, String> params = buildRequestMap(fn, appname, apptime, appkey);

			String responseData = RestHttpServiceHanlder.sendHttptoServer(params, maisike.getSend_url()); // 请求并返回

			responseData = URLDecoder.decode(responseData, "UTF-8"); // 解码

			logger.info("迈思可站点信息同步返回信息={}", URLDecoder.decode(responseData, "UTF-8"));

			RespStore respStore = JacksonMapper.getInstance().readValue(responseData, RespStore.class); // 解析

			if (!respStore.getCode().equals(MaisikeExpEmum.Success.getErrCode())) {
				logger.info("请求迈思可站点同步功能发生异常,code={},msg={}", respStore.getCode(), respStore.getMsg());
				return 0;
			}

			for (Stores store : respStore.getStores()) {
				boolean isexistsflag = storesDAO.isExistsBranchs(store.getSid());
				if (isexistsflag) {
					storesDAO.updateInfoById(store.getSid(), store.getSname(), store.getSarea(), store.getSaddress(), store.getSphone());
					// 修改原来的部分
					continue;
				}
				storesDAO.insertMaisikeStores(store, b2cenum);
				logger.info("迈思可站点={},sid={}同步完成", store.getSname(), store.getSid());
			}

			return respStore.getStores().size();

		} catch (Exception e) {
			logger.error("请求迈思可站点同步发生未知异常", e);
			return 0;
		}

	}

	private Map<String, String> buildRequestMap(String fn, String appname, String apptime, String appkey) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fn", fn);
		params.put("appname", appname);
		params.put("apptime", apptime);
		params.put("appkey", appkey);
		return params;
	}

}
