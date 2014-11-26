package cn.explink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.NotifyDao;

@Service
public class NotifyService {
	@Autowired
	NotifyDao notifyDao;

	public void delByIds(String ids) {
		notifyDao.delByIds(ids);

	}

	@Transactional
	public void updateToTop(long id) {
		notifyDao.clearIstop();
		notifyDao.updateToTop(id);
	}

}
