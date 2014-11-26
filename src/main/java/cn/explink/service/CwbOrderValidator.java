package cn.explink.service;

import org.springframework.stereotype.Service;

import cn.explink.controller.CwbOrderDTO;

@Service
public interface CwbOrderValidator {
	public void validate(CwbOrderDTO cwbOrder);
}
