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
//package cn.explink.b2c.lefeng.ws;
//import org.codehaus.xfire.fault.XFireFault;
//
//import com.lefeng.ordertracer.ws.vo.LefengDocument;
//
///**
// * <tt>乐蜂�?/tt>同步物流商订单信�?<code>Web Service</code> 接口</p>
// * 
// * 根据<tt>乐蜂�?/tt>订单标识，返回订单在物流商物流配送环节中的所有的流转节点信息.
// * 
// * @version 0.1
// *
// * @author Hefei Li
// *
// * @since May 27, 2013
// */
//
//public interface ITracerService {
//	
//	/**
//	 * 同步订单操作<p>
//	 * 
//	 * @param document  {@link #syscOrder(com.lefeng.ordertracer.ws.vo.LefengDocument)}
//	 *
//	 * @return
//	 * @throws XFireFault
//	 */
//	public LefengDocument syscOrder(LefengDocument document) throws XFireFault;
//	
//	/**
//	 * 第三方物流商推�?订单配�?路由信息</p>
//	 * 
//	 * @param document {@link #syscOrder(com.lefeng.ordertracer.ws.vo.LefengDocument)}
//	 * @return
//	 * @throws XFireFault
//	 */
//	//String pushOrderRoutes(LefengDocument document) throws XFireFault;
//	
// }
