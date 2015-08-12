package cn.explink.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.explink.domain.TimeEffectiveVO;
import cn.explink.domain.TimeTypeEnum;
import cn.explink.service.TimeEffectiveService;

@Controller
@RequestMapping("time_effective")
public class TimeEffectiveController {

	@Autowired
	private TimeEffectiveService service = null;

	@RequestMapping("/")
	public ModelAndView list() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("teTypeList", this.getService().getAllTimeEffectiveVO());
		mv.addObject("teTimeTypeEnumMap", this.getTimeTypeEnumMap());
		mv.setViewName("time_effective/timeeffective");

		return mv;
	}

	@RequestMapping("/submit")
	public String submit(HttpServletRequest request, Model model) throws Exception {
		List<TimeEffectiveVO> teVOList = this.resolveData(request);
		List<TimeEffectiveVO> afterUpdateVOList = this.getService().updateTimeEffective(teVOList);
		model.addAttribute("teTypeList", afterUpdateVOList);
		model.addAttribute("teTimeTypeEnumMap", this.getTimeTypeEnumMap());

		return "time_effective/timeeffective";
	}

	private List<TimeEffectiveVO> resolveData(HttpServletRequest request) {
		String[] strTimeTypes = request.getParameterValues("te_timetype");
		String[] strTimeHours = request.getParameterValues("te_hour");
		String[] strTimeMinutes = request.getParameterValues("te_minute");
		String[] strIds = request.getParameterValues("te_id");

		return this.transfer(strIds, strTimeTypes, strTimeHours, strTimeMinutes);
	}

	private List<TimeEffectiveVO> transfer(String[] strIds, String[] strTimeTypes, String[] strTimeHours, String[] strTimeMinutes) {
		List<TimeEffectiveVO> teVOList = new ArrayList<TimeEffectiveVO>();
		if(null!=strIds){
		for (int i = 0; i < strIds.length; i++) {
			teVOList.add(this.transfer(strIds[i], strTimeTypes[i], strTimeHours[i], strTimeMinutes[i]));
		}}
		return teVOList;
	}

	private TimeEffectiveVO transfer(String strId, String strTimeType, String strTimeHour, String strTimeMinute) {
		return new TimeEffectiveVO(strId, strTimeType, strTimeHour, strTimeMinute);
	}

	private Map<Integer, String> getTimeTypeEnumMap() {
		Map<Integer, String> timeTypeEnumMap = new HashMap<Integer, String>();
		for (TimeTypeEnum type : TimeTypeEnum.values()) {
			timeTypeEnumMap.put(Integer.valueOf(type.ordinal()), type.getName());
		}
		return timeTypeEnumMap;
	}

	private TimeEffectiveService getService() {
		return this.service;
	}
}
