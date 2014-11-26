package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.CommonModelDAO;
import cn.explink.domain.CommonModel;
import cn.explink.service.CommonModelService;
import cn.explink.util.ServiceUtil;

@RequestMapping("/commonmodel")
@Controller
public class CommonModelController {

	@Autowired
	CommonModelDAO commonmodelDAO;
	@Autowired
	CommonModelService commonmodelService;

	@RequestMapping("/getModel/{id}")
	public @ResponseBody JSONObject getModel(@PathVariable("id") Long id, HttpServletRequest request) {

		CommonModel c = commonmodelDAO.getCommonModelById(id);
		JSONObject reC = new JSONObject();
		if (c != null) {
			reC.put("imageurl", request.getContextPath() + ServiceUtil.imgPath + c.getImageurl());
			reC.put("coordinate", JSONObject.fromObject(c.getCoordinate()));
		}
		return reC;
	}

	@RequestMapping("/add")
	public String add(Model model) {
		return "/commonmodel/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, @RequestParam(value = "modelname", required = true) String modelname) {
		CommonModel listCommonmodel = commonmodelDAO.getCommonModelByModelname(modelname);
		if (listCommonmodel != null) {
			return "{\"errorCode\":1,\"error\":\"该模版已存在\"}";
		} else {
			commonmodelDAO.CreateCommonModel(modelname);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}
	}

	@RequestMapping("/tosetcoordinate")
	public String tosetcoordinate(Model model) {
		List<CommonModel> commonmodellist = commonmodelDAO.getAllCommonModel();
		model.addAttribute("commonmodellist", commonmodellist);

		return "/commonmodel/common_coordinateModel";
	}

	@RequestMapping("/setcoordinate/{id}")
	public @ResponseBody String setcoordinate(Model model, HttpServletRequest request, @PathVariable("id") long id, @RequestParam("coordinate") String coordinate) {
		// String coordinate = request.getParameter("coordinate");

		try {
			commonmodelDAO.saveCommonModelById(coordinate, id);
			return "{\"errorCode\":0,\"error\":\"设置成功\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"errorCode\":1,\"error\":\"设置失败\"}";
		}
	}

	@RequestMapping("/setimageurl/")
	public String setimageurl(Model model, HttpServletRequest request, @RequestParam("modelname") long modelname, @RequestParam(value = "imageurl", required = false) MultipartFile file) {

		CommonModel commonmodel = commonmodelService.loadFormForCommonModel(request, file);
		commonmodelDAO.saveCommonModelImageurlById(commonmodel.getImageurl(), modelname);

		List<CommonModel> commonmodellist = commonmodelDAO.getAllCommonModel();

		model.addAttribute("commonmodellist", commonmodellist);
		return "/commonmodel/common_coordinateModel";
	}

}
