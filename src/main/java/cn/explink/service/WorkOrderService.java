package cn.explink.service;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.WorkOrderDAO;
import cn.explink.domain.CsConsigneeInfo;
import cn.explink.domain.CwbOrder;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;

@Service
public class WorkOrderService {
	@Autowired
	private WorkOrderDAO workorderdao;
	@Autowired
	private CwbDAO cwbdao;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void addcsconsigneeInfo(CsConsigneeInfo cci) {
		if(cci!=null)
				workorderdao.save(cci);
		
	}

	public CsConsigneeInfo querycciByPhoneNum(String phoneonOne){
		
		return workorderdao.queryByPhoneNum(phoneonOne);
	}

	public List<CwbOrder> SelectCwbdetalForm(String phone) {
		List<CwbOrder> lc=cwbdao.SelectDetalForm(phone)==null?null:cwbdao.SelectDetalForm(phone);
		return lc;		
	}
	
	public  String loadexceptfile(MultipartFile file){
		String name="";
		try {
			if ((file != null) && !file.isEmpty()) {
				String filePath = ResourceBundleUtil.EXCEPTPATH;
				name=file.getOriginalFilename();
				if (name.indexOf(".")!=-1) {
					String suffix=name.substring(name.indexOf("."));
					 name = System.currentTimeMillis() + suffix;
				}else {
					 name = System.currentTimeMillis()+"";
				}
				ServiceUtil.uploadWavFile(file, filePath, name);
			}
		} catch (Exception e) {
			this.logger.error("问题件添加到指定路径下出现错误");
		}
		return name;
	}
	public String getExceptname(HttpServletRequest request){
		String name="";
		 //创建一个通用的多部分解析器  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
        if(multipartResolver.isMultipart(request)){  
        	 //转换成多部分request    
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
            //取得request中的所有文件名  
            Iterator<String> iter = multiRequest.getFileNames(); 
            while(iter.hasNext()){  
            	 //取得上传文件  
                MultipartFile file = multiRequest.getFile(iter.next());  
                name=this.loadexceptfile(file)+",";
            }
        }
        if (name!="") {
			name=name.substring(0, name.length()-1);
		}
        return name;
	}

}
