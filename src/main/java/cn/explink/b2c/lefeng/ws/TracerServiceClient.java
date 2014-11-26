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
//import java.io.FileNotFoundException;
//
//
//import java.io.PrintWriter;
//import java.net.MalformedURLException;
//
//import org.codehaus.xfire.client.XFireProxyFactory;
//import org.codehaus.xfire.fault.XFireFault;
//import org.codehaus.xfire.service.Service;
//import org.codehaus.xfire.xmlbeans.XmlBeansServiceFactory;
//import org.junit.Assert;
//
//import com.lefeng.ordertracer.ws.vo.LefengDocument;
//import com.lefeng.ordertracer.ws.vo.LefengDocument.Lefeng;
//import com.lefeng.ordertracer.ws.vo.LefengDocument.Lefeng.Orders;
//import com.lefeng.ordertracer.ws.vo.LefengDocument.Lefeng.Orders.Order;
//
///**
// * @version 0.1
// * 
// * @author Hefei Li
// * 
// * @since May 27, 2013
// */
//
//public class TracerServiceClient {
//	
//	//private static Logger log = LoggerFactory.getLogger(TracerServiceClient.class);
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		String url = "http://localhost:8080/order_tracer/services/ITracerService";
//		if (args.length == 0) {
//			System.out.println("URL was not supplied. Using default URL - " + url);
//		} else {
//			url = args[0];
//		}
//
//		XmlBeansServiceFactory xsf = new XmlBeansServiceFactory();
//		Service serviceModel = xsf.create(ITracerService.class);
//
//		try {
//			ITracerService client = (ITracerService) new XFireProxyFactory().create(serviceModel, url);
//			
//			LefengDocument reqDoc = generatedData();
//			
////			client.pushOrderRoutes(reqDoc);
//			
//			LefengDocument lfDoc = LefengDocument.Factory.newInstance();
//					
//			lfDoc = client.syscOrder(reqDoc);
//			if(lfDoc != null){
//				Assert.assertNotNull(lfDoc);
//				
//				PrintWriter pw = new PrintWriter("LefengDocument.xml");
//				
//				pw.write(lfDoc.toString().replaceAll("\\r\\n", ""));
//				
//				System.out.println(lfDoc.toString());
//				
//				Orders orders = lfDoc.getLefeng().getOrders();
//			}
//			
//			
//			/*for (Order order : orders.getOrderArray()) {
//				
//				log.info("[OrderNum = " +order.getOrderNum() + ", sponsorId: " +order.getSponsorId());
//			}*/
//			
//		} catch (MalformedURLException e) {
//			e.printStackTrace(); 
//		} catch (XFireFault e) {
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	private static LefengDocument generatedData() {
//		LefengDocument doc = LefengDocument.Factory.newInstance();
//
//		Lefeng lf = Lefeng.Factory.newInstance();
//		
//		Orders orders = Orders.Factory.newInstance();
//
//		orders.setSize(ITEMS_SIZE);
//		
//		for (int i = 0; i < ITEMS_SIZE; i++) {
//			Order order = orders.addNewOrder();
//			
//			//order.setSponsorId("LF-#-00" +i);
//			order.setSponsorId("5604983110000");
//		}	
//		
//		lf.setOrders(orders);
//		
//		doc.setLefeng(lf);
//		
//		return doc;
//	}
//
//	
//	private static final int ITEMS_SIZE = 5;
// }
