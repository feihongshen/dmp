package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import cn.explink.domain.Truck;
import cn.explink.util.StringUtil;

@Service
public class TruckService {

	public Truck loadFormForTruck(HttpServletRequest request, long truckid) {
		Truck truck = loadFormForTruck(request);
		truck.setTruckid(truckid);
		return truck;
	}

	public Truck loadFormForTruck(HttpServletRequest request) {
		Truck truck = new Truck();
		truck.setTruckno(StringUtil.nullConvertToEmptyString(request.getParameter("truckno")));
		truck.setTrucktype(StringUtil.nullConvertToEmptyString(request.getParameter("trucktype")));
		truck.setTruckdriver(Integer.parseInt(request.getParameter("truckdriver") == null || "".equalsIgnoreCase(request.getParameter("truckdriver")) ? "0" : request.getParameter("truckdriver")));
		truck.setTruckflag(Integer.parseInt(request.getParameter("truckflag") == null || "".equalsIgnoreCase(request.getParameter("truckflag")) ? "1" : request.getParameter("truckflag")));
		return truck;
	}

}
