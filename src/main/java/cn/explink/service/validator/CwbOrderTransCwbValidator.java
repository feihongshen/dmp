package cn.explink.service.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;
/**
* @ClassName: CwbOrderTransCwbValidator 
* @Description: 判断运单号是否相等 
* @author 刘武强
* @date 2016年10月18日 下午4:38:20 
*
 */
@Component
public class CwbOrderTransCwbValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		if(cwbOrder != null && cwbOrder.getTranscwb() != null && !"".equals(cwbOrder.getTranscwb())){
			List<String> transCwbList = new ArrayList<String>();
			//客户输入什么运单号就是什么运单号，不排除空格，与以前逻辑一致
			transCwbList = Arrays.asList(cwbOrder.getTranscwb().split(this.getSplitstring(cwbOrder.getTranscwb())));
			if(!hasSame(transCwbList)){
				throw new RuntimeException("不能包含相同运单号!");
			}
		}
	}
	
	/**
	* @Title: getSplitstring 
	* @Description: 获取运单之间的分隔符 
	* @param @param transcwb
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	* @date 2016年10月18日 下午4:36:42 
	* @author 刘武强
	 */
	private String getSplitstring(String transcwb) {
		if (transcwb.indexOf(':') != -1) {
			return ":";
		}
		return ",";
	}
	
	/**
	* @Title: hasSame 
	* @Description: 判断list中是否有重复元素
	* @param @param list
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws 
	* @date 2016年10月18日 下午4:48:18 
	* @author 刘武强
	 */
	private static boolean hasSame(List<? extends Object> list){  
        if(null == list){  
            return false;
        }
        return list.size() == new HashSet<Object>(list).size(); //HashSet中不会含有重复的元素
	}
}
