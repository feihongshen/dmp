package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.domain.Exportmould;
import cn.explink.domain.Role;

@Service
public class ExportmouldService {

	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	RoleDAO roleDAO;

	public boolean checkRoleid(List<Exportmould> listmould, long roleid) {
		for (Exportmould e : listmould) {
			if (e.getRoleid() == roleid) {
				return true;
			}
		}
		return false;
	}

	public boolean checkMouldname(List<Exportmould> listmould, String mouldname) {
		for (Exportmould e : listmould) {
			if (e.getMouldname().equals(mouldname)) {
				return true;
			}
		}
		return false;
	}

	@Transactional
	public String createForMoreRole(String mouldname, long[] roleids, String mouldfieldids, List<Exportmould> listmould) {
		for (long roleid : roleids) {
			if (this.checkRoleid(listmould, roleid)) {
				return "{\"errorCode\":0,\"error\":\"该模版已存在\"}";
			} else {
				Role role = roleDAO.getRolesByRoleid(roleid);
				String rolename = role.getRolename();
				exportmouldDAO.creExportmould(roleid, rolename, mouldname, mouldfieldids);
			}

		}
		return "{\"errorCode\":0,\"error\":\"新建成功\"}";
	}

}
