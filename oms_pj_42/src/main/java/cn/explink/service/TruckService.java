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
		truck.setTruckoil(Float.parseFloat(request.getParameter("truckoil") == null || "".equalsIgnoreCase(request.getParameter("truckoil")) ? "0" : request.getParameter("truckoil")));
		truck.setTruckway(StringUtil.nullConvertToEmptyString(request.getParameter("truckway")));
		truck.setTruckkm(Float.parseFloat(request.getParameter("truckkm") == null || "".equalsIgnoreCase(request.getParameter("truckkm")) ? "0" : request.getParameter("truckkm")));
		truck.setTruckstartkm(Float.parseFloat(request.getParameter("truckstartkm") == null || "".equalsIgnoreCase(request.getParameter("truckstartkm")) ? "0" : request.getParameter("truckstartkm")));
		truck.setTruckdriver(Integer.parseInt(request.getParameter("truckdriver") == null || "".equalsIgnoreCase(request.getParameter("truckdriver")) ? "0" : request.getParameter("truckdriver")));
		truck.setTruckflag(Integer.parseInt(request.getParameter("truckflag") == null || "".equalsIgnoreCase(request.getParameter("truckflag")) ? "1" : request.getParameter("truckflag")));
		return truck;
	}

}
