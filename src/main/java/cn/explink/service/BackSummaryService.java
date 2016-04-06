package cn.explink.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BackDetailDAO;
import cn.explink.dao.BackMiddleDAO;
import cn.explink.dao.BackSummaryDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.BackDetail;
import cn.explink.domain.BackMiddle;
import cn.explink.domain.BackSummary;
import cn.explink.enumutil.BackTypeEnum;

/**
 * 退货中心出入库报表
 * 
 * @author zs
 *
 */
@Service
public class BackSummaryService implements SystemConfigChangeListner {
	private Logger logger = LoggerFactory.getLogger(BackSummaryService.class);
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	CamelContext camelContext;
	@Autowired
	BackSummaryDAO backSummaryDAO;
	@Autowired
	BackDetailDAO backDetailDAO;
	@Autowired
	BackMiddleDAO backMiddleDAO;

	String time = "";
	long timeLong = 0;
	/*
	 * 改为quartz实现
	@PostConstruct
	public void init() throws Exception {
		final SystemInstall backTimeLog = systemInstallDAO.getSystemInstallByName("backTimeLog");
		if (backTimeLog == null || !StringUtils.hasLength(backTimeLog.getValue())) {
			camelContext.stopRoute("退货中心出入库跟踪表定时任务生成");
			logger.warn("自动退货中心出入库跟踪表日志未启用，没有找到参数{}", backTimeLog);
			return;
		}
		camelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// time= new SimpleDateFormat("yyyy-MM-dd").format(new
				// Date())+" " +"14:57:00";
				time = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " " + backTimeLog.getValue();
				Date nextExecuteTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
				timeLong = nextExecuteTime.getTime();
				// 系统定时任务是否生成
				long period = 86400000;
				long delay = timeLong - new Date().getTime();
				if (delay < 0) {
					delay = period + delay % period;
				}
				from("timer://foo?fixedRate=true&period=" + period + "&delay=" + delay).to("bean:backSummaryService?method=createBackTimeLog").routeId("退货中心出入库跟踪表任务生成");
			}
		});
	}
	*/
	@Override
	public void onChange(Map<String, String> parameters) {
		/*
		 * 改为quartz实现
		if (parameters.keySet().contains("backTimeLog")) {
			try {
				this.init();
			} catch (Exception e) {
				logger.error("error while reloading backTimeLog camle routes", e);
			}
		}
		*/
	}

	/**
	 * 生成退货中心出入库报表
	 */
	@Transactional
	public void createBackTimeLog() {
		// 取开始时间：如果有汇总数据则取最后一条创建的时间为开始时间 否则取当前时间为结束时间
		String starttime = "";
		BackSummary backSummary = backSummaryDAO.getBackSummary();
		if (backSummary != null) {
			starttime = backSummary.getCreatetime().substring(0, 19);
		}

		StringBuffer cwbSB = new StringBuffer(""); // 订单号存储格式:cwb|type,cwb|type,cwb|type
		long nums24 = 0, nums72 = 0, numsout = 0, numsinto = 0;

		// 根据开始结束时间获得 退供应商出库、库房入库的订单
		List<BackDetail> listInOrOut = backDetailDAO.getBackDetailListByInOrOut(starttime, time);
		if (listInOrOut != null && !listInOrOut.isEmpty()) {
			for (int i = 0; i < listInOrOut.size(); i++) {
				cwbSB.append(listInOrOut.get(i).getCwb()).append("|").append(listInOrOut.get(i).getType()).append(",");
				if (listInOrOut.get(i).getType() == BackTypeEnum.TuiGongHuoShangChuKu.getValue()) {
					numsout++;
				} else {
					numsinto++;
				}
			}
		}

		// 站点做了退货出站之后超24H/72H，退货中心仍未做入库的订单
		List<BackDetail> listBack = backDetailDAO.getBackDetailListByBack(timeLong);
		if (listBack != null && !listBack.isEmpty()) {
			for (int i = 0; i < listBack.size(); i++) {
				if (listBack.get(i).getTime24() > 0) {
					cwbSB.append(listBack.get(i).getCwb()).append("|").append(BackTypeEnum.TuiHuoChuZhan24.getValue()).append(",");
					nums24++;
				} else {
					cwbSB.append(listBack.get(i).getCwb()).append("|").append(BackTypeEnum.TuiHuoChuZhan72.getValue()).append(",");
					nums72++;
				}
			}
		}

		// 插入退货出站统计_汇总表
		BackSummary o = this.formForBackSummary(nums24, nums72, numsout, numsinto, time);
		long summaryid = backSummaryDAO.createBackSummary(o);
		logger.info("===退货中心出入库跟踪表(汇总表)===id:{},time:{}", new Object[] { summaryid, time });

		// 插入退货出站统计_中间表(汇总及订单关系表)
		if (!"".equals(cwbSB.toString())) {
			String cwbs[] = cwbSB.toString().split(",");
			for (int i = 0; i < cwbs.length; i++) {
				BackMiddle backMiddle = new BackMiddle();
				backMiddle.setSummaryid(summaryid);// 汇总id
				backMiddle.setCwb(cwbs[i].split("\\|")[0]);
				backMiddle.setType(Integer.parseInt(cwbs[i].split("\\|")[1]));
				backMiddleDAO.createBackMiddle(backMiddle);
			}
			logger.info("===退货中心出入库跟踪表(中间表)===汇总id:{},time:{},cwb:{}", new Object[] { summaryid, time, cwbSB.toString() });
		}
	}

	public BackSummary formForBackSummary(long nums24, long nums72, long numsout, long numsinto, String createtime) {
		BackSummary o = new BackSummary();
		o.setNums24(nums24);
		o.setNums72(nums72);
		o.setNumsout(numsout);
		o.setNumsinto(numsinto);
		o.setCreatetime(createtime);
		return o;
	}

	public List<BackSummary> getBackSummaryByExcelView(List<BackSummary> backList) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		List<BackSummary> list = new ArrayList<BackSummary>();
		if (backList != null && !backList.isEmpty()) {
			for (BackSummary old : backList) {
				BackSummary backSummary = new BackSummary();
				BeanUtils.copyProperties(old, backSummary);
				try {
					backSummary.setCreatetime(sd.format(sd.parse(old.getCreatetime())));
				} catch (ParseException e) {
					logger.error("", e);
				}
				if (old.getNumsinto() == 0) {
					backSummary.setPercent("--");
				} else if (old.getNumsout() == 0) {
					backSummary.setPercent("0.00%");
				} else {
					DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
					double z = (double) old.getNumsout() / old.getNumsinto();
					backSummary.setPercent(df.format(z * 100) + "%");
				}

				list.add(backSummary);
			}
		}
		return list;
	}

}
