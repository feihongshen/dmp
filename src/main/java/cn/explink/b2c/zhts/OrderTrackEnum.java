package cn.explink.b2c.zhts;

import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 *@author LX
 *@date 创建时间:2016年3月29日下午5:35:55
 *@doWhat
 */
public enum OrderTrackEnum {
	RuKu(FlowOrderTypeEnum.RuKu.getValue(),"4","入库"),
	ChuKu(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"6","库房出库"),
	DaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),"7","站点到货"),
	LingHuo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(),"9","派送中、领货"),
	QianShou(FlowOrderTypeEnum.YiShenHe.getValue(),"10","签收"),
	ZhiLiu(FlowOrderTypeEnum.YiShenHe.getValue(),"11","滞留"),
	JuShou(FlowOrderTypeEnum.YiShenHe.getValue(),"12","拒收"),
	TuiHuoChuZhan(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(),"14","退货出站"),
	TuiHuoRuKu(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(),"15","入退货库"),
	TuiGongYingShangChuKu(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(),"16","退供货商出库");
	
	private int flowordertype;
	public long getFlowordertype() {
		return flowordertype;
	}
	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}
	private String status;
	private String description;
	public String getStatus() {
		return status;
	}
	public void setMark(String mark) {
		this.status = mark;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	private OrderTrackEnum(int flowordertype,String status,String description){
		this.flowordertype=flowordertype;
		this.status = status;
		this.description = description;
	}
	public static String getOrderTrackStatusByFlowId(long flowordertype,long deliveryState){
		for(OrderTrackEnum em:OrderTrackEnum.values()){
			if(flowordertype!=FlowOrderTypeEnum.YiShenHe.getValue()&&em.getFlowordertype()==flowordertype){
				return em.getStatus();
			}
			if(flowordertype==FlowOrderTypeEnum.YiShenHe.getValue()){
				if(deliveryState==DeliveryStateEnum.PeiSongChengGong.getValue()
						||deliveryState==DeliveryStateEnum.ShangMenHuanChengGong.getValue()
						||deliveryState==DeliveryStateEnum.ShangMenTuiChengGong.getValue()){
					return OrderTrackEnum.QianShou.getStatus();
				}
				if(deliveryState==DeliveryStateEnum.FenZhanZhiLiu.getValue()){
					return OrderTrackEnum.ZhiLiu.getStatus();
					
				}
				if(deliveryState==DeliveryStateEnum.JuShou.getValue()||deliveryState==DeliveryStateEnum.ShangMenJuTui.getValue()){
					return OrderTrackEnum.JuShou.getStatus();
				}
			}
		}
		return null;
	}

	
	
}
