package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.TimeEffectiveDAO;
import cn.explink.domain.TimeEffectiveVO;

@Service
public class TimeEffectiveService {

	@Autowired
	private TimeEffectiveDAO timeEffectiveDAO = null;

	public List<TimeEffectiveVO> getAllTimeEffectiveVO() {
		return this.getTimeEffectiveDAO().getAllTimeEffectiveVO();
	}

	public List<TimeEffectiveVO> updateTimeEffective(List<TimeEffectiveVO> teVOList) {
		this.getTimeEffectiveDAO().update(teVOList);

		return this.getAllTimeEffectiveVO();
	}

	private TimeEffectiveDAO getTimeEffectiveDAO() {
		return this.timeEffectiveDAO;
	}

}
