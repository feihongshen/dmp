package cn.explink.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeleteCwbDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void deleteCwbOrderByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_cwb_detail where state=1 and cwb=? ", cwb);
	}

	public void deleteDeliveryStateByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_delivery_state where cwb=? ", cwb);
	}

	public void deleteTiHuoByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_cwb_tihuo where cwb=? ", cwb);
	}

	public void deleteDeliveryChukuByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_delivery_chuku where cwb=? ", cwb);
	}

	public void deleteDeliveryDaohuoByOpscwbid(String cwb) {
		jdbcTemplate.update("delete from ops_delivery_daohuo where cwb=? ", cwb);
	}

	public void deleteDeliveryJushouByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_delivery_jushou where cwb=? ", cwb);
	}

	public void deleteDeliverySuccessfulByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_delivery_successful where cwb=? ", cwb);
	}

	public void deleteDeliveryZhiliuByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_delivery_zhiliu where cwb=? ", cwb);
	}

	public void deleteZhongZhuanByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_zhongzhuan where cwb=? ", cwb);
	}

	public void deleteB2cdataLiantongByCwb(String cwb) {
		jdbcTemplate.update("delete from express_b2cdata_liantong where cwb=? ", cwb);
	}

	public void deleteB2CDataSearchByCwb(String cwb) {
		jdbcTemplate.update("delete from express_b2cdata_search where cwb=? ", cwb);
	}

	public void deleteB2CDataSearchHappygoByCwb(String cwb) {
		jdbcTemplate.update("delete from express_b2cdata_search_happygo where cwb=? ", cwb);
	}

	public void deleteB2CDataSearchSaohuobangByCwb(String cwb) {
		jdbcTemplate.update("delete from express_b2cdata_search_saohuobang where cwb=? ", cwb);
	}

	public void deleteSendB2CCodByCwb(String cwb) {
		jdbcTemplate.update("delete from express_send_b2c_cod where cwb=? ", cwb);
	}

	public void deleteSendB2CDataByCwb(String cwb) {
		jdbcTemplate.update("delete from express_send_b2c_data where cwb=? ", cwb);
	}

	public void deleteTuiHuoZhanRuKu(String cwb) {
		jdbcTemplate.update("delete from ops_tuihuozhanruku  where cwb=? ", cwb);
	}

	public void deleteKuFangZaiTu(String cwb) {
		jdbcTemplate.update("delete from ops_kufangzaitu  where cwb=? ", cwb);
	}

	public void deleteKuFangRuKu(String cwb) {
		jdbcTemplate.update("delete from ops_kufangruku  where cwb=? ", cwb);
	}

	public void deleteKuDuiKu(String cwb) {
		jdbcTemplate.update("delete from ops_delivery_kdkchuku  where cwb=? ", cwb);
	}

	public void deleteTuiHuoChuZhan(String cwb) {
		jdbcTemplate.update("delete from ops_delivery_tuihuochuzhan  where cwb=? ", cwb);
	}

	public void deleteZongheChaxun(String cwb) {
		jdbcTemplate.update("delete from commen_cwb_order_tail  where cwb=? ", cwb);
	}

}
