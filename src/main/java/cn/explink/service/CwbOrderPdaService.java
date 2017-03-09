package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.CountyDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.express.NewAreaForm;
import cn.explink.service.express.EmbracedOrderInputService;
import groovy.transform.AutoClone;
import net.sf.json.JSONObject;

@Service
public class CwbOrderPdaService {

	@Autowired
	private ProvinceDAO provinceDAO;
	@Autowired
	private CityDAO cityDAO;
	@Autowired
	private CountyDAO countyDAO;

	public String getAddress() {
		JSONObject jsonObject = new JSONObject();
		JSONObject provinceJson = new JSONObject();
		JSONObject provinceByCity=new JSONObject();
		JSONObject cityByCounty=new JSONObject();
		JSONObject cityJson=null;
		JSONObject countyJson=null;
		List<AdressVO> province = this.provinceDAO.getProvince();
		for (AdressVO adressVO : province) {
			provinceJson.element(adressVO.getCode(), adressVO.getName());
			List<AdressVO> cityOfProvince = this.cityDAO.getCityOfProvince(adressVO.getCode());
			cityJson = new JSONObject();
			for (AdressVO cityAdressVo : cityOfProvince) {
				cityJson.element(cityAdressVo.getCode(), cityAdressVo.getName());
				List<AdressVO> countyOfCity = this.countyDAO.getCountyOfCity(cityAdressVo.getCode());
				countyJson = new JSONObject();
				for (AdressVO countyAdressVo : countyOfCity) {
					countyJson.element(countyAdressVo.getCode(), countyAdressVo.getName());
				}
				cityByCounty.element(cityAdressVo.getCode(), countyJson);
			}
			provinceByCity.element(adressVO.getCode(), cityJson);
		}
		jsonObject.element("province", provinceJson);
		jsonObject.element("city", provinceByCity);
		jsonObject.element("country", cityByCounty);
		return jsonObject.toString();

	}

}
