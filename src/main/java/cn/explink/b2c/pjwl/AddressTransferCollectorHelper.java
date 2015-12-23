package cn.explink.b2c.pjwl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.CountyDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.dao.express.TownDAO;
import cn.explink.domain.VO.express.AdressVO;



@Service
public class AddressTransferCollectorHelper {
	@Autowired
	private CityDAO cityDAO;
	
	@Autowired
	private ProvinceDAO provinceDAO;
	
	@Autowired
	private CountyDAO countyDAO;
	
	@Autowired
	private TownDAO townDAO;
	/**
	 * 获取地址信息的缓存信息
	 * @return
	 */
	public Map<String, Map<String, String>> getAddressCollectorMap(){
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		List<AdressVO> provinces = provinceDAO.getAllProvince();
		List<AdressVO> cities = cityDAO.getAllCity();
		List<AdressVO> counties = countyDAO.getAllCounty();
		List<AdressVO> towns = townDAO.getAllTown();
		
		Map<String, String> provinceMap = this.transferinfo2Map(provinces);
		Map<String, String> cityMap = this.transferinfo2Map(cities);
		Map<String, String> countyMap = this.transferinfo2Map(counties);
		Map<String, String> townMap = this.transferinfo2Map(towns);
		
		map.put("province", provinceMap);
		map.put("city", cityMap);
		map.put("county", countyMap);
		map.put("town", townMap);
		return map;
	}
	/**
	 * 将list信息存放到map中
	 * @param address
	 * @return
	 */
	private Map<String, String> transferinfo2Map(List<AdressVO> address){
		Map<String, String> map = new HashMap<String, String>();
		if (address!=null&&address.size()>0) {
			for (AdressVO adressVO : address) {
				map.put(adressVO.getCode(), adressVO.getName());
			}
		}
		return map;
	}
	
	
}
