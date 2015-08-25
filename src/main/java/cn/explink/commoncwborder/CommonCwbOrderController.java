package cn.explink.commoncwborder;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CommonDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.WarehouseToCommenDAO;
import cn.explink.domain.Common;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Controller
@RequestMapping("/commencwborder")
public class CommonCwbOrderController {
	private Logger logger = LoggerFactory.getLogger(CommonCwbOrderController.class);
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	WarehouseToCommenDAO warehouseToCommenDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@RequestMapping("/")
	public String main(String cwb) {
		return "commencwborder";
	}
	@RequestMapping("/resendCwborder")
	public @ResponseBody String resendCwborder(@RequestParam("cwbs") String cwbs){
		long num=0;
		String[] split = cwbs.split("\n");
		for (String cwb : split) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			CwbOrder cwbOrder=cwbDao.getCwbByCwb(cwb);
			OrderFlow of=orderFlowDAO.getOrderCurrentFlowByCwb(cwb);
			if(cwbOrder!=null){
				try {
					

			/*		if (cwbOrder.getCwbstate() != CwbStateEnum.PeiShong.getValue()) {
						this.logger.warn("订单号{}订单当前状态为{}，不能参与发送至承运商", cwbOrder.getCwb(), cwbOrder.getCwbstate());
						continue;
					}
*/
					long count = this.warehouseToCommenDAO.getCommonCountByCwb(cwbOrder.getCwb());// 查询是否存在

					List<Common> commlist = this.commonDAO.getCommonByBranchid(cwbOrder.getNextbranchid());
					List<Common> commlist1 = this.commonDAO.getCommonByBranchid(cwbOrder.getCurrentbranchid());
					if(cwbOrder.getFlowordertype()==FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()){
						if ((commlist1 == null) || (commlist1.size() == 0)) {
							if (count > 0) {
								this.warehouseToCommenDAO.deleteCommonBycwb(cwbOrder.getCwb());
								this.logger.info("承运商出库表已存在该订单={},删除记录", cwbOrder.getCwb());
							}
							continue;
						}
					}else{
						if ((commlist == null) || (commlist.size() == 0)) {
							if (count > 0) {
								this.warehouseToCommenDAO.deleteCommonBycwb(cwbOrder.getCwb());
								this.logger.info("承运商出库表已存在该订单={},删除记录", cwbOrder.getCwb());
							}
							continue;
						}
					}
					this.logger.info("存入出库给承运商表  订单号:{}", of.getCwb());
					String commencode = commlist.get(0).getCommonnumber();

					if (count > 0) {
						this.warehouseToCommenDAO.updateWarehouseToCommen(cwbOrder.getCwb(), of.getBranchid(), cwbOrder.getNextbranchid(), commencode,
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
						this.logger.info("存入出库给承运商表 订单号:{},更新成功", cwb);
						num++;
					} else {
						this.warehouseToCommenDAO.creWarehouseToCommen(cwbOrder.getCwb(), cwbOrder.getCustomerid(), of.getBranchid(), cwbOrder.getNextbranchid(), commencode, new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").format(of.getCredate()), "", 0);
						this.logger.info("存入出库给承运商表 订单号:{},插入成功", cwb);
						num++;
					}
				
			} catch (Exception e) {
				this.logger.error("error while saveing commensendcwborder", e);
			}
			}
		}

		return "ok" + num;
	}
}
