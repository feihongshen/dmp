///*
// * Copyright 2010-2011 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package cn.explink.b2c.lefeng.ws;
//
//import java.util.Calendar;
//import java.util.List;
//
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.xfire.fault.XFireFault;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import cn.explink.b2c.explink.ExplinkService;
//import cn.explink.b2c.lefeng.LefengService;
//import cn.explink.b2c.lefeng.LefengT;
//import cn.explink.b2c.tools.B2cEnum;
//import cn.explink.dao.OrderFlowDAO;
//import cn.explink.dao.UserDAO;
//import cn.explink.domain.DeliveryState;
//import cn.explink.domain.User;
//import cn.explink.domain.orderflow.OrderFlow;
//import cn.explink.enumutil.DeliveridEnum;
//import cn.explink.enumutil.DeliveryStateEnum;
//import cn.explink.enumutil.FlowOrderTypeEnum;
//import cn.explink.pos.tools.JacksonMapper;
//import cn.explink.service.CwbOrderWithDeliveryState;
//
//import com.lefeng.ordertracer.ws.vo.LefengDocument;
//import com.lefeng.ordertracer.ws.vo.LefengDocument.Lefeng;
//import com.lefeng.ordertracer.ws.vo.LefengDocument.Lefeng.Agent;
//import com.lefeng.ordertracer.ws.vo.LefengDocument.Lefeng.Orders;
//import com.lefeng.ordertracer.ws.vo.LefengDocument.Lefeng.Orders.Order;
//import com.lefeng.ordertracer.ws.vo.LefengDocument.Lefeng.Orders.Order.Routes;
//import com.lefeng.ordertracer.ws.vo.RouteType;
//import com.lefeng.ordertracer.ws.vo.StatusCodeType;
//
//
//
//
///**
// * @version 0.1
// * 
// * @author Hefei Li
// * 
// * @since May 27, 2013
// */
//
//public class TracerService implements ITracerService {
//	
//
//	//OrderFlowDAO orderFlowDAO;
//	
//	//UserDAO userDAO;
//	
//	//ExplinkService explinkService;
//	
//	
//	
//	
//	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance(); 
//	private static Logger logger = LoggerFactory.getLogger(TracerService.class);
//
//	
//	/** 
//	 * 同步订单消息
//	 * @see com.lefeng.ordertracer.ws.service.ITracerService#syscOrder(com.lefeng.ordertracer.ws.vo.LefengDocument)
//	 */
//	@Override
//	public LefengDocument syscOrder(LefengDocument document) throws XFireFault {
//		LefengDocument doc = LefengDocument.Factory.newInstance();
//		
//		logger.info("*************************\r\n" +document.toString());
//		
//		LefengT lefengt=new LefengService().getLefengT(B2cEnum.LefengWang.getKey());
//		
//		Lefeng lefeng = Lefeng.Factory.newInstance();
//		
//		Calendar cal = java.util.GregorianCalendar.getInstance();
//
//		Lefeng lf = document.getLefeng();
//		
//		lf.setAgent(buildAgent(lefengt, lf)); //agent
//		
//		
//		Orders preOrders = lf.getOrders();
//		Order[] orderArr=new Order[preOrders.sizeOfOrderArray()];
//		try {
//			int o=0;
//		for (Order order : preOrders.getOrderArray()) {  //eval all Order node items
//			
//			order.setSponsorId(order.getSponsorId());
//			order.setOrderNum(order.getOrderNum());
//			
//			Routes rt=order.addNewRoutes();
//		
//			List<OrderFlow> tracklist=new OrderFlowDAO().getOrderFlowByCwb(order.getSponsorId());
//			
//			RouteType[] routes=new RouteType[tracklist.size()];
//			
//			int i=0;
//			for(OrderFlow orderFlow:tracklist){
//					String lf_code=new LefengService().getLefengStatus(orderFlow.getFlowordertype(),orderFlow);
//					if(lf_code==null){
//						continue;
//					}
//					CwbOrderWithDeliveryState cwbOrderWithDeliveryState=jacksonmapper.readValue(orderFlow.getFloworderdetail(), CwbOrderWithDeliveryState.class);
//					long deliveryState=cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate();
//					User users=new UserDAO().getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid());
//					
//					RouteType route = rt.addNewRoute();
//					route.setCourier(users.getRealname()+"-"+users.getUsermobile());
//					route.setTime(cal);
//					getRouteCode(orderFlow, deliveryState, route);
//					route.setState(new ExplinkService().getDetail(orderFlow));
//					routes[i]=route;
//					i++;
//			}
//			rt.setRouteArray(routes);
//			order.setRoutes(rt);
//			o++;
//			orderArr[o]=order;
//			
//		}
//		
//		preOrders.setOrderArray(orderArr);
//		lefeng.setOrders(preOrders);
//
//		doc.setLefeng(lefeng);
//
//		} catch (Exception e) {
//			logger.error("0乐蜂网0请求查询接口发生未知异常",e);
//		}
//		
//		return doc;
//	}
//
//
//	private void getRouteCode(OrderFlow orderFlow, long deliveryState,
//			RouteType route) {
//		if(orderFlow.getFloworderid()==FlowOrderTypeEnum.FenZhanLingHuo.getValue()){
//			 route.setCode(StatusCodeType.N_10);
//		}else if(deliveryState==DeliveryStateEnum.PeiSongChengGong.getValue()||deliveryState==DeliveryStateEnum.ShangMenTuiChengGong.getValue()||
//			 deliveryState==DeliveryStateEnum.ShangMenHuanChengGong.getValue()){
//			 route.setCode(StatusCodeType.S_00);
//		}else if(deliveryState==DeliveryStateEnum.JuShou.getValue()||deliveryState==DeliveryStateEnum.BuFenTuiHuo.getValue()){
//			route.setCode(StatusCodeType.E_30);
//		}else if(deliveryState==DeliveryStateEnum.FenZhanZhiLiu.getValue()){
//			route.setCode(StatusCodeType.E_31);
//		}
//	}
//
//
//	private Agent buildAgent(LefengT lefengt, Lefeng lf) {
//		Agent agent=lf.getAgent();
//		agent.setId(lefengt.getCode());
//		agent.setName(lefengt.getCompanyname());
//		agent.setWebsite(lefengt.getWebsite());
//		return agent;
//	}
//
//	/** 
//	 * 推�?订单消息</p>
//	 * 
//	 * @see com.lefeng.ordertracer.ws.service.ITracerService#pushOrderRoutes(com.lefeng.ordertracer.ws.vo.LefengDocument)
//	 */
//	/*@Override
//	public String pushOrderRoutes(LefengDocument document) throws XFireFault {
//		String info = document.toString();
//		System.out.println(info);
//		return null;
//	}*/
//
// }
