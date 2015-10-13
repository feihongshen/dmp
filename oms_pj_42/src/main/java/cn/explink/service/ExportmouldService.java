package cn.explink.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.explink.domain.Exportmould;

@Service
public class ExportmouldService {

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

}
