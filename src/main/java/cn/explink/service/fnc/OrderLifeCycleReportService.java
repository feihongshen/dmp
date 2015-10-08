package cn.explink.service.fnc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.fnc.OrderLifeCycleReportDao;
import cn.explink.domain.OrderLifeCycleReportVO;

@Service
public class OrderLifeCycleReportService {

	private Logger logger = LoggerFactory
			.getLogger(OrderLifeCycleReportService.class);

	@Autowired
	private CustomerDAO customerDao;
	@Autowired
	private OrderLifeCycleReportDao orderLifeCycleReportDao;

	

	/**
	 * 获取订单生命周期列表和汇总数据 
	 * 
	 * @param selectedCustomers
	 * @return 列表和汇总数据 
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public DataGridReturn getDataGridReturn(String selectedCustomers) {
		DataGridReturn dg = new DataGridReturn();

		List<OrderLifeCycleReportVO> rows = orderLifeCycleReportDao
				.getListByCustomers(selectedCustomers);

		dg.setRows(rows);
		dg.setFooter(getSummaryFooter(rows));
		
		return dg;
	}
	
	
	private List<LinkedHashMap<String, Object>> getSummaryFooter(
			List<OrderLifeCycleReportVO> rows) {

		List<LinkedHashMap<String, Object>> footer = new ArrayList<LinkedHashMap<String, Object>>();

		Map<String, Object> foot = new LinkedHashMap<String, Object>();
		
		BigDecimal amount1 = BigDecimal.ZERO;
		BigDecimal amount2 = BigDecimal.ZERO;
		BigDecimal amount3 = BigDecimal.ZERO;
		BigDecimal amount4 = BigDecimal.ZERO;
		BigDecimal amount5 = BigDecimal.ZERO;
		BigDecimal amount6 = BigDecimal.ZERO;
		BigDecimal amount7 = BigDecimal.ZERO;
		BigDecimal amount8 = BigDecimal.ZERO;
		BigDecimal amount9 = BigDecimal.ZERO;
		BigDecimal amount10 = BigDecimal.ZERO;
		BigDecimal amount11 = BigDecimal.ZERO;
		BigDecimal amount12 = BigDecimal.ZERO;
		BigDecimal amount13 = BigDecimal.ZERO;
		BigDecimal amount14 = BigDecimal.ZERO;

		Integer count1 = 0;
		Integer count2 = 0;
		Integer count3 = 0;
		Integer count4 = 0;
		Integer count5 = 0;
		Integer count6 = 0;
		Integer count7 = 0;
		Integer count8 = 0;
		Integer count9 = 0;
		Integer count10 = 0;
		Integer count11 = 0;
		Integer count12 = 0;
		Integer count13 = 0;
		Integer count14 = 0;

		for (OrderLifeCycleReportVO row : rows) {

			amount1 = amount1.add(row.getAmout1());
			amount2 = amount2.add(row.getAmout2());
			amount3 = amount3.add(row.getAmout3());
			amount4 = amount4.add(row.getAmout4());
			amount5 = amount5.add(row.getAmout5());
			amount6 = amount6.add(row.getAmout6());
			amount7 = amount7.add(row.getAmout7());
			amount8 = amount8.add(row.getAmout8());
			amount9 = amount9.add(row.getAmout9());
			amount10 = amount10.add(row.getAmout10());
			amount11 = amount11.add(row.getAmout11());
			amount12 = amount12.add(row.getAmout12());
			amount13 = amount13.add(row.getAmout13());
			amount14 = amount14.add(row.getAmout14());
			count1 = count1 + row.getCount1();
			count2 = count2 + row.getCount2();
			count3 = count3 + row.getCount3();
			count4 = count4 + row.getCount4();
			count5 = count5 + row.getCount5();
			count6 = count6 + row.getCount6();
			count7 = count7 + row.getCount7();
			count8 = count8 + row.getCount8();
			count9 = count9 + row.getCount9();
			count10 = count10 + row.getCount10();
			count11 = count11 + row.getCount11();
			count12 = count12 + row.getCount12();
			count13 = count13 + row.getCount13();
			count14 = count14 + row.getCount14();
		}
		
		foot.put("amount1",amount1);
		foot.put("amount2",amount2);
		foot.put("amount3",amount3);
		foot.put("amount4",amount4);
		foot.put("amount5",amount5);
		foot.put("amount6",amount6);
		foot.put("amount7",amount7);
		foot.put("amount8",amount8);
		foot.put("amount9",amount9);
		foot.put("amount10",amount10);
		foot.put("amount11",amount11);
		foot.put("amount12",amount12);
		foot.put("amount13",amount13);
		foot.put("amount14",amount14);
		
		foot.put("count1",count1);
		foot.put("count2",count2);
		foot.put("count3",count3);
		foot.put("count4",count4);
		foot.put("count5",count5);
		foot.put("count6",count6);
		foot.put("count7",count7);
		foot.put("count8",count8);
		foot.put("count9",count9);
		foot.put("count10",count10);
		foot.put("count11",count11);
		foot.put("count12",count12);
		foot.put("count13",count13);
		foot.put("count14",count14);


		return footer;
	}
}
