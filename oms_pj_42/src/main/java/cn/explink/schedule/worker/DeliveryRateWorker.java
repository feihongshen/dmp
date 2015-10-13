package cn.explink.schedule.worker;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.DownloadManagerDAO;
import cn.explink.dao.DownloadResultDAO;
import cn.explink.domain.DownloadManager;
import cn.explink.domain.DownloadResult;
import cn.explink.domain.ScheduledTask;
import cn.explink.enumutil.DownloadState;
import cn.explink.exception.ExplinkRuntimeException;
import cn.explink.schedule.Constants;
import cn.explink.schedule.ScheduledWorker;
import cn.explink.service.DeliveryRateService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JsonUtil;
import cn.explink.vo.delivery.DeliveryRateRequest;

@Service("deliveryRateWorker")
public class DeliveryRateWorker extends ScheduledWorker {

	private static Logger logger = LoggerFactory.getLogger(DeliveryRateWorker.class);

	@Autowired
	private DownloadManagerDAO downloadManagerDao;

	@Autowired
	private DownloadResultDAO downloadResultDAO;

	@Autowired
	private DeliveryRateService deliveryRateService;

	@Override
	protected boolean doJob(ScheduledTask scheduledTask) throws Exception {
		if (!Constants.TASK_TYPE_DELIVERY_RATE.equals(scheduledTask.getTaskType())) {
			throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not deliveryRateTask.");
		}

		DownloadManager downloadManager = downloadManagerDao.getDownloadManagerById(Long.parseLong(scheduledTask.getReferenceId()));
		String dataJson = downloadManager.getDatajson();
		logger.info("process delivery rate request {}", dataJson);
		DeliveryRateRequest deliveryRateRequest = JsonUtil.readValue(dataJson, DeliveryRateRequest.class);
		String json = deliveryRateService.queryDeliveryRate(deliveryRateRequest);
		// save result
		DownloadResult result = new DownloadResult();
		result.setDownloadId(downloadManager.getId());
		result.setResult(json);
		downloadResultDAO.create(result);

		downloadManager.setEndtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		downloadManager.setState(DownloadState.ready.getValue());

		downloadManagerDao.updateStateById(DownloadState.ready.getValue(), downloadManager.getId(), DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return true;
	}

}
