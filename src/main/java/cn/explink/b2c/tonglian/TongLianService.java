package cn.explink.b2c.tonglian;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;

@Service
public class TongLianService {
	@Autowired
	JiontDAO jiontDAO;

	/**
	 *
	 * @Title: getTongLian
	 * @description 获取通联的配置信息
	 * @author 刘武强
	 * @date  2015年12月12日上午9:38:07
	 * @param  @param key
	 * @param  @return
	 * @return  TongLian
	 * @throws
	 */
	public TongLian getTongLian(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		TongLian tonglian = (TongLian) JSONObject.toBean(jsonObj, new TongLian().getClass());
		return tonglian;
	}

	/**
	 *
	 * @Title: getObjectMethod
	 * @description 根据key，查出配置参数的方法
	 * @author 刘武强
	 * @date  2015年12月12日上午9:39:20
	 * @param  @param key
	 * @param  @return
	 * @return  Object
	 * @throws
	 */
	public Object getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	/**
	 *
	 * @Title: edit
	 * @description 根据情况对通联的配置信息进行保存（插入或者更新）
	 * @author 刘武强
	 * @date  2015年12月12日上午9:38:29
	 * @param  @param request
	 * @param  @param joint_num
	 * @return  void
	 * @throws
	 */
	public void edit(HttpServletRequest request, int joint_num, MultipartFile tongliancer, MultipartFile tonglianpfx) {
		TongLian Tl = new TongLian();
		String name = "";
		String filePath = ResourceBundleUtil.FileTongLianPath;
		//String filePath = "E:/paysource/";
		TongLian oldTl = this.getTongLian(joint_num);
		if ((tonglianpfx != null) && !tonglianpfx.isEmpty()) {
			name = tonglianpfx.getOriginalFilename();
			Tl.setPfxPath(filePath + name);
			ServiceUtil.uploadWavFile(tonglianpfx, filePath, name);
		} else {
			Tl.setPfxPath(oldTl.getPfxPath());
		}
		if ((tongliancer != null) && !tongliancer.isEmpty()) {
			name = tongliancer.getOriginalFilename();
			Tl.setTltcerPath(filePath + name);
			ServiceUtil.uploadWavFile(tongliancer, filePath, name);
		} else {
			Tl.setTltcerPath(oldTl.getTltcerPath());
		}
		Tl.setUserName(request.getParameter("userName") == null ? "" : (request.getParameter("userName")));
		Tl.setUserPass(request.getParameter("userPass") == null ? "" : (request.getParameter("userPass")));
		Tl.setPfxPassword(request.getParameter("pfxPassword") == null ? "" : (request.getParameter("pfxPassword")));
		Tl.setMerchantId(request.getParameter("merchantId") == null ? "" : (request.getParameter("merchantId")));

		JSONObject jsonObj = JSONObject.fromObject(Tl);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

}
