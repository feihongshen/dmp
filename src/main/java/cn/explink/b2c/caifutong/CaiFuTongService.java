package cn.explink.b2c.caifutong;

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
public class CaiFuTongService {
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
	public CaiFuTong getCaiFuTong(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		CaiFuTong tonglian = (CaiFuTong) JSONObject.toBean(jsonObj, new CaiFuTong().getClass());
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
	public void edit(HttpServletRequest request, int joint_num, MultipartFile caifutongca, MultipartFile caifutongcer) {
		CaiFuTong cft = new CaiFuTong();
		String name = "";
		String filePath = ResourceBundleUtil.FileTongLianPath;
		CaiFuTong oldCft = this.getCaiFuTong(joint_num);
		if ((caifutongca != null) && !caifutongca.isEmpty()) {
			name = caifutongca.getOriginalFilename();
			cft.setCaInfo(filePath + name);
			ServiceUtil.uploadWavFile(caifutongca, filePath, name);
		} else {
			cft.setCaInfo(oldCft.getCaInfo());
		}
		if ((caifutongcer != null) && !caifutongcer.isEmpty()) {
			name = caifutongcer.getOriginalFilename();
			cft.setCertInfo(filePath + name);
			ServiceUtil.uploadWavFile(caifutongcer, filePath, name);
		} else {
			cft.setCertInfo(oldCft.getCertInfo());
		}
		cft.setPartner(request.getParameter("partner") == null ? "" : (request.getParameter("partner")));
		cft.setKey(request.getParameter("key") == null ? "" : (request.getParameter("key")));
		cft.setOpUser(request.getParameter("opUser") == null ? "" : (request.getParameter("opUser")));
		cft.setOpPasswd(request.getParameter("opPasswd") == null ? "" : (request.getParameter("opPasswd")));
		cft.setCertInfoPasswd(request.getParameter("certInfoPasswd") == null ? "" : (request.getParameter("certInfoPasswd")));

		JSONObject jsonObj = JSONObject.fromObject(cft);
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
