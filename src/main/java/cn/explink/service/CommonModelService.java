package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.domain.CommonModel;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StringUtil;

@Service
public class CommonModelService {

	public CommonModel loadFormForCommonModel(HttpServletRequest request, MultipartFile file) {
		CommonModel commonmodel = loadFormForCommonModel(request);
		if (file != null && !file.isEmpty()) {
			String filePath = ResourceBundleUtil.UPLOADIMGPATH;
			String name = System.currentTimeMillis() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
			ServiceUtil.uploadimgFile(file, filePath, name);
			commonmodel.setImageurl(name);
		}
		return commonmodel;
	}

	public CommonModel loadFormForCommonModel(HttpServletRequest request) {
		CommonModel commonmodel = new CommonModel();
		commonmodel.setId(Integer.parseInt(request.getParameter("id") == null ? "0" : request.getParameter("id")));
		commonmodel.setModelname(StringUtil.nullConvertToEmptyString(request.getParameter("modelname")));
		commonmodel.setCoordinate(StringUtil.nullConvertToEmptyString(request.getParameter("coordinate")));
		commonmodel.setImageurl(StringUtil.nullConvertToEmptyString(request.getParameter("imageurl")));
		return commonmodel;
	}

}
