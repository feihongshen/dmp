package cn.explink.service;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.controller.MonitorLogDTO;
import cn.explink.dao.MonitorDAO;

@Service
public class MonitorLogService {

	private static Logger logger = LoggerFactory.getLogger(MonitorService.class);

	@Autowired
	private MonitorDAO monitorDAO;

	public  List<MonitorLogDTO> getMonitorLogByBranchid(String branchids,String customerids) {
		
		return monitorDAO.getMonitorLogByBranchid(branchids,customerids);
	}

}
