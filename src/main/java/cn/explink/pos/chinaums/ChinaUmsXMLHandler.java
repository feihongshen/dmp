package cn.explink.pos.chinaums;

import java.util.List;
import java.util.Map;

import cn.explink.b2c.tools.ExptReason;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Reason;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.util.DateTimeUtil;

public class ChinaUmsXMLHandler {

	/**
	 * 验证请求登录的信息
	 * 
	 * @param loginmap
	 * @return
	 */
	public static String checkCreateMACXML_Login(Map loginmap) {
		StringBuffer str = new StringBuffer("");
		str.append("<Transaction_Header>" + "<transaction_id>" + loginmap.get("transaction_id") + "</transaction_id>" + "<requester>" + loginmap.get("requester") + "</requester>" + "<target>"
				+ loginmap.get("target") + "</target>" + "<request_time>" + loginmap.get("request_time") + "</request_time>" + "<version>" + loginmap.get("version") + "</version>"
				+ "</Transaction_Header>" + "<Transaction_Body>" + "<delivery_man>" + loginmap.get("delivery_man") + "</delivery_man>" + "<password>" + loginmap.get("password") + "</password>"
				+ "</Transaction_Body>");
		return str.toString().replace("null", "");
	}

	/**
	 * 创建登陆时要进行加密的字符串 (MAC)
	 * 
	 * @param loginmap
	 * @return String
	 */
	public static String createMACXML_Login(Map loginmap, EmployeeInfo user,int version) {
		StringBuffer str = new StringBuffer("");
		if(version==1){ //西安品信版本 没有rolename
			
			if (user != null) {
				str.append("<transaction><transaction_header>" + "<version>").append(loginmap.get("version")).append("</version>" + "<transtype>").append(loginmap.get("transtype"))
						.append("</transtype>" + "<employno>").append(loginmap.get("employno")).append("</employno>" + "<termid>").append(loginmap.get("termid")).append("</termid>" + "<response_time>")
						.append(loginmap.get("response_time")).append("</response_time>" + "<response_code>").append(loginmap.get("response_code")).append("</response_code>" + "<response_msg>")
						.append(loginmap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("<employname>").append(loginmap.get("employname"))
						.append("</employname>" + "<netcode>").append(loginmap.get("netcode")).append("</netcode>" + "<netname>").append(loginmap.get("netname")).append("</netname>" + "<mobile>")
						.append(loginmap.get("mobile")).append("</mobile>").append("<exceptioncodeversion>").append(loginmap.get("exceptioncodeversion")).append("</exceptioncodeversion>").append("</transaction_body></transaction>");
			} else {
				str.append("<transaction><transaction_header>" + "<version>").append(loginmap.get("version")).append("</version>" + "<transtype>").append(loginmap.get("transtype"))
						.append("</transtype>" + "<employno>").append(loginmap.get("employno")).append("</employno>" + "<termid>").append(loginmap.get("termid")).append("</termid>" + "<response_time>")
						.append(loginmap.get("response_time")).append("</response_time>" + "<response_code>").append(loginmap.get("response_code")).append("</response_code>" + "<response_msg>")
						.append(loginmap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
			}
			
			
		}else{
			if (user != null) {
				str.append("<transaction><transaction_header>" + "<version>").append(loginmap.get("version")).append("</version>" + "<transtype>").append(loginmap.get("transtype"))
						.append("</transtype>" + "<employno>").append(loginmap.get("employno")).append("</employno>" + "<termid>").append(loginmap.get("termid")).append("</termid>" + "<response_time>")
						.append(loginmap.get("response_time")).append("</response_time>" + "<response_code>").append(loginmap.get("response_code")).append("</response_code>" + "<response_msg>")
						.append(loginmap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("<employname>").append(loginmap.get("employname"))
						.append("</employname>" + "<netcode>").append(loginmap.get("netcode")).append("</netcode>" + "<netname>").append(loginmap.get("netname")).append("</netname>" + "<mobile>")
						.append(loginmap.get("mobile")).append("</mobile>" + "<rolename>").append(loginmap.get("rolename")).append("</rolename>").append("<exceptioncodeversion>").append(loginmap.get("exceptioncodeversion")).append("</exceptioncodeversion>").append("</transaction_body></transaction>");
			} else {
				str.append("<transaction><transaction_header>" + "<version>").append(loginmap.get("version")).append("</version>" + "<transtype>").append(loginmap.get("transtype"))
						.append("</transtype>" + "<employno>").append(loginmap.get("employno")).append("</employno>" + "<termid>").append(loginmap.get("termid")).append("</termid>" + "<response_time>")
						.append(loginmap.get("response_time")).append("</response_time>" + "<response_code>").append(loginmap.get("response_code")).append("</response_code>" + "<response_msg>")
						.append(loginmap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
			}
		}
		
		
		return str.toString().replace("null", "");
	}

	/**
	 * 创建响应登陆XML文件
	 * 
	 * @param paymentmap
	 * @return String
	 */
	public static String createXMLMessage_Login(Map loginmap, EmployeeInfo user, String resp_code,int version) {
		StringBuffer str = new StringBuffer("");
		if(version==1){ //西安品信
			if (user != null && resp_code.equals(ChinaUmsExptMessageEnum.Success.getResp_code())) {
				str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(loginmap.get("version")).append("</version>" + "<transtype>")
						.append(loginmap.get("transtype")).append("</transtype>" + "<employno>").append(loginmap.get("employno")).append("</employno>" + "<termid>").append(loginmap.get("termid"))
						.append("</termid>" + "<response_time>").append(loginmap.get("response_time")).append("</response_time>" + "<response_code>").append(loginmap.get("response_code"))
						.append("</response_code>" + "<response_msg>").append(loginmap.get("response_msg")).append("</response_msg>").append("<mac>").append(loginmap.get("mac")).append("</mac>")
						.append("</transaction_header>" + "<transaction_body>").append("<employname>").append(loginmap.get("employname")).append("</employname>" + "<netcode>")
						.append(loginmap.get("netcode")).append("</netcode>" + "<netname>").append(loginmap.get("netname")).append("</netname>" + "<mobile>").append(loginmap.get("mobile"))
						.append("</mobile>").append("<exceptioncodeversion>").append(loginmap.get("exceptioncodeversion")).append("</exceptioncodeversion>").append("</transaction_body></transaction>");
			} else {
				str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(loginmap.get("version")).append("</version>" + "<transtype>")
						.append(loginmap.get("transtype")).append("</transtype>" + "<employno>").append(loginmap.get("employno")).append("</employno>" + "<termid>").append(loginmap.get("termid"))
						.append("</termid>" + "<response_time>").append(loginmap.get("response_time")).append("</response_time>" + "<response_code>").append(loginmap.get("response_code"))
						.append("</response_code>" + "<response_msg>").append(loginmap.get("response_msg")).append("</response_msg>").append("<mac>").append(loginmap.get("mac")).append("</mac>")
						.append("</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
			}
		}else{
			
			if (user != null && resp_code.equals(ChinaUmsExptMessageEnum.Success.getResp_code())) {
				str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(loginmap.get("version")).append("</version>" + "<transtype>")
						.append(loginmap.get("transtype")).append("</transtype>" + "<employno>").append(loginmap.get("employno")).append("</employno>" + "<termid>").append(loginmap.get("termid"))
						.append("</termid>" + "<response_time>").append(loginmap.get("response_time")).append("</response_time>" + "<response_code>").append(loginmap.get("response_code"))
						.append("</response_code>" + "<response_msg>").append(loginmap.get("response_msg")).append("</response_msg>").append("<mac>").append(loginmap.get("mac")).append("</mac>")
						.append("</transaction_header>" + "<transaction_body>").append("<employname>").append(loginmap.get("employname")).append("</employname>" + "<netcode>")
						.append(loginmap.get("netcode")).append("</netcode>" + "<netname>").append(loginmap.get("netname")).append("</netname>" + "<mobile>").append(loginmap.get("mobile"))
						.append("</mobile>" + "<rolename>").append(loginmap.get("rolename")).append("</rolename>").append("<exceptioncodeversion>").append(loginmap.get("exceptioncodeversion")).append("</exceptioncodeversion>").append("</transaction_body></transaction>");
			} else {
				str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(loginmap.get("version")).append("</version>" + "<transtype>")
						.append(loginmap.get("transtype")).append("</transtype>" + "<employno>").append(loginmap.get("employno")).append("</employno>" + "<termid>").append(loginmap.get("termid"))
						.append("</termid>" + "<response_time>").append(loginmap.get("response_time")).append("</response_time>" + "<response_code>").append(loginmap.get("response_code"))
						.append("</response_code>" + "<response_msg>").append(loginmap.get("response_msg")).append("</response_msg>").append("<mac>").append(loginmap.get("mac")).append("</mac>")
						.append("</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
			}
		}
		
		return str.toString().replace("null", "");
	}

	/**
	 * 创建运单查询的MAC字符串
	 * 
	 * @param searchcwbmap
	 * @return String
	 */
	public static String createMACXML_SearchCwb(Map cwbmap, ChinaUmsRespNote chinaUmsRespNote,int version) {
		
		StringBuffer str = new StringBuffer("");
		if(version==1){
			if (chinaUmsRespNote != null && chinaUmsRespNote.getCwbOrder() != null) {
				str.append("<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>").append(cwbmap.get("transtype"))
						.append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid")).append("</termid>" + "<response_time>")
						.append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code")).append("</response_code>" + "<response_msg>")
						.append(cwbmap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("<netcode>").append(cwbmap.get("netcode"))
						.append("</netcode>" + "<netname>").append(cwbmap.get("netname")).append("</netname>" + "<weight>").append(cwbmap.get("weight")).append("</weight>" + "<goodscount>")
						.append(cwbmap.get("goodscount")).append("</goodscount>" + "<cod>").append(cwbmap.get("cod")).append("</cod>" + "<istopay>").append(cwbmap.get("istopay"))
						.append("</istopay>" + "<address>").append(cwbmap.get("address")).append("</address>" + "<people>").append(cwbmap.get("people")).append("</people>" + "<peopletel>")
						.append(cwbmap.get("peopletel")).append("</peopletel>").append("<status>").append(cwbmap.get("status"))
						.append("</status>" + "<memo>").append(cwbmap.get("memo")).append("</memo>" + "<dssn>").append(cwbmap.get("dssn")).append("</dssn>" + "<dsname>").append(cwbmap.get("dsname"))
						.append("</dsname><dsorderno>").append(cwbmap.get("dsorderno")).append("</dsorderno>").append("</transaction_body></transaction>");
			} else {
				str.append("<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>").append(cwbmap.get("transtype"))
						.append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid")).append("</termid>" + "<response_time>")
						.append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code")).append("</response_code>" + "<response_msg>")
						.append(cwbmap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
			}
		}else if(version==2){
			if (chinaUmsRespNote != null && chinaUmsRespNote.getCwbOrder() != null) {
				str.append("<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>").append(cwbmap.get("transtype"))
						.append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid")).append("</termid>" + "<response_time>")
						.append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code")).append("</response_code>" + "<response_msg>")
						.append(cwbmap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("<netcode>").append(cwbmap.get("netcode"))
						.append("</netcode>" + "<netname>").append(cwbmap.get("netname")).append("</netname>" + "<weight>").append(cwbmap.get("weight")).append("</weight>" + "<goodscount>")
						.append(cwbmap.get("goodscount")).append("</goodscount>" + "<cod>").append(cwbmap.get("cod")).append("</cod>" + "<istopay>").append(cwbmap.get("istopay"))
						.append("</istopay>" + "<address>").append(cwbmap.get("address")).append("</address>" + "<people>").append(cwbmap.get("people")).append("</people>" + "<peopletel>")
						.append(cwbmap.get("peopletel")).append("</peopletel>" + "<sqpayway>").append(cwbmap.get("sqpayway")).append("</sqpayway>" + "<status>").append(cwbmap.get("status"))
						.append("</status>" + "<memo>").append(cwbmap.get("memo")).append("</memo>" + "<dssn>").append(cwbmap.get("dssn")).append("</dssn>" + "<dsname>").append(cwbmap.get("dsname"))
						.append("</dsname><dsorderno>").append(cwbmap.get("dsorderno")).append("</dsorderno>")
						.append("<dlvryno>"+cwbmap.get("dlvryno")+"</dlvryno>").append("</transaction_body></transaction>");
						
			} else {
				str.append("<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>").append(cwbmap.get("transtype"))
						.append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid")).append("</termid>" + "<response_time>")
						.append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code")).append("</response_code>" + "<response_msg>")
						.append(cwbmap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
			}
		}
		
		else{
			if (chinaUmsRespNote != null && chinaUmsRespNote.getCwbOrder() != null) {
				str.append("<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>").append(cwbmap.get("transtype"))
						.append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid")).append("</termid>" + "<response_time>")
						.append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code")).append("</response_code>" + "<response_msg>")
						.append(cwbmap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("<netcode>").append(cwbmap.get("netcode"))
						.append("</netcode>" + "<netname>").append(cwbmap.get("netname")).append("</netname>" + "<weight>").append(cwbmap.get("weight")).append("</weight>" + "<goodscount>")
						.append(cwbmap.get("goodscount")).append("</goodscount>" + "<cod>").append(cwbmap.get("cod")).append("</cod>" + "<istopay>").append(cwbmap.get("istopay"))
						.append("</istopay>" + "<address>").append(cwbmap.get("address")).append("</address>" + "<people>").append(cwbmap.get("people")).append("</people>" + "<peopletel>")
						.append(cwbmap.get("peopletel")).append("</peopletel>" + "<sqpayway>").append(cwbmap.get("sqpayway")).append("</sqpayway>" + "<status>").append(cwbmap.get("status"))
						.append("</status>" + "<memo>").append(cwbmap.get("memo")).append("</memo>" + "<dssn>").append(cwbmap.get("dssn")).append("</dssn>" + "<dsname>").append(cwbmap.get("dsname"))
						.append("</dsname><dsorderno>").append(cwbmap.get("dsorderno")).append("</dsorderno>").append("</transaction_body></transaction>");
			} else {
				str.append("<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>").append(cwbmap.get("transtype"))
						.append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid")).append("</termid>" + "<response_time>")
						.append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code")).append("</response_code>" + "<response_msg>")
						.append(cwbmap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
			}
		}
		
		

		return str.toString().replace("null", "");

	}

	/**
	 * 创建运单查询xml文件
	 * 
	 * @param searchcwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_SearchCwb(Map cwbmap, ChinaUmsRespNote chinaUmsRespNote,int version) {
		StringBuffer str = new StringBuffer("");
		if(version==1){
			if (chinaUmsRespNote != null && chinaUmsRespNote.getCwbOrder() != null) {
				str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>")
						.append(cwbmap.get("transtype")).append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid"))
						.append("</termid>" + "<response_time>").append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code"))
						.append("</response_code>" + "<response_msg>").append(cwbmap.get("response_msg")).append("</response_msg>" + "<mac>").append(cwbmap.get("mac"))
						.append("</mac>" + "</transaction_header>" + "<transaction_body>").append("<netcode>").append(cwbmap.get("netcode")).append("</netcode>" + "<netname>")
						.append(cwbmap.get("netname")).append("</netname>" + "<weight>").append(cwbmap.get("weight")).append("</weight>" + "<goodscount>").append(cwbmap.get("goodscount"))
						.append("</goodscount>" + "<cod>").append(cwbmap.get("cod")).append("</cod>" + "<istopay>").append(cwbmap.get("istopay")).append("</istopay>" + "<address>")
						.append(cwbmap.get("address")).append("</address>" + "<people>").append(cwbmap.get("people")).append("</people>" + "<peopletel>").append(cwbmap.get("peopletel"))
						.append("</peopletel>").append("<status>").append(cwbmap.get("status")).append("</status>" + "<memo>")
						.append(cwbmap.get("memo")).append("</memo>" + "<dssn>").append(cwbmap.get("dssn")).append("</dssn>" + "<dsname>").append(cwbmap.get("dsname")).append("</dsname><dsorderno>")
						.append(cwbmap.get("dsorderno")).append("</dsorderno>").append("</transaction_body></transaction>");
			} else {
				str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>")
						.append(cwbmap.get("transtype")).append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid"))
						.append("</termid>" + "<response_time>").append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code"))
						.append("</response_code>" + "<response_msg>").append(cwbmap.get("response_msg")).append("</response_msg>" + "<mac>").append(cwbmap.get("mac"))
						.append("</mac>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
			}
		}else if(version== 2){
		
			if (chinaUmsRespNote != null && chinaUmsRespNote.getCwbOrder() != null) {
				str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>")
						.append(cwbmap.get("transtype")).append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid"))
						.append("</termid>" + "<response_time>").append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code"))
						.append("</response_code>" + "<response_msg>").append(cwbmap.get("response_msg")).append("</response_msg>" + "<mac>").append(cwbmap.get("mac"))
						.append("</mac>" + "</transaction_header>" + "<transaction_body>").append("<netcode>").append(cwbmap.get("netcode")).append("</netcode>" + "<netname>")
						.append(cwbmap.get("netname")).append("</netname>" + "<weight>").append(cwbmap.get("weight")).append("</weight>" + "<goodscount>").append(cwbmap.get("goodscount"))
						.append("</goodscount>" + "<cod>").append(cwbmap.get("cod")).append("</cod>" + "<istopay>").append(cwbmap.get("istopay")).append("</istopay>" + "<address>")
						.append(cwbmap.get("address")).append("</address>" + "<people>").append(cwbmap.get("people")).append("</people>" + "<peopletel>").append(cwbmap.get("peopletel"))
						.append("</peopletel>" + "<sqpayway>").append(cwbmap.get("sqpayway")).append("</sqpayway>" + "<status>").append(cwbmap.get("status")).append("</status>" + "<memo>")
						.append(cwbmap.get("memo")).append("</memo>" + "<dssn>").append(cwbmap.get("dssn")).append("</dssn>" + "<dsname>").append(cwbmap.get("dsname")).append("</dsname><dsorderno>")
						.append(cwbmap.get("dsorderno")).append("</dsorderno>")
						.append("<dlvryno>"+cwbmap.get("dlvryno")+"</dlvryno>")
						.append("</transaction_body></transaction>");
			} else {
				str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>")
						.append(cwbmap.get("transtype")).append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid"))
						.append("</termid>" + "<response_time>").append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code"))
						.append("</response_code>" + "<response_msg>").append(cwbmap.get("response_msg")).append("</response_msg>" + "<mac>").append(cwbmap.get("mac"))
						.append("</mac>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
			}
		}
		
		
		
		else{
			if (chinaUmsRespNote != null && chinaUmsRespNote.getCwbOrder() != null) {
				str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>")
						.append(cwbmap.get("transtype")).append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid"))
						.append("</termid>" + "<response_time>").append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code"))
						.append("</response_code>" + "<response_msg>").append(cwbmap.get("response_msg")).append("</response_msg>" + "<mac>").append(cwbmap.get("mac"))
						.append("</mac>" + "</transaction_header>" + "<transaction_body>").append("<netcode>").append(cwbmap.get("netcode")).append("</netcode>" + "<netname>")
						.append(cwbmap.get("netname")).append("</netname>" + "<weight>").append(cwbmap.get("weight")).append("</weight>" + "<goodscount>").append(cwbmap.get("goodscount"))
						.append("</goodscount>" + "<cod>").append(cwbmap.get("cod")).append("</cod>" + "<istopay>").append(cwbmap.get("istopay")).append("</istopay>" + "<address>")
						.append(cwbmap.get("address")).append("</address>" + "<people>").append(cwbmap.get("people")).append("</people>" + "<peopletel>").append(cwbmap.get("peopletel"))
						.append("</peopletel>" + "<sqpayway>").append(cwbmap.get("sqpayway")).append("</sqpayway>" + "<status>").append(cwbmap.get("status")).append("</status>" + "<memo>")
						.append(cwbmap.get("memo")).append("</memo>" + "<dssn>").append(cwbmap.get("dssn")).append("</dssn>" + "<dsname>").append(cwbmap.get("dsname")).append("</dsname><dsorderno>")
						.append(cwbmap.get("dsorderno")).append("</dsorderno>").append("</transaction_body></transaction>");
			} else {
				str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>")
						.append(cwbmap.get("transtype")).append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid"))
						.append("</termid>" + "<response_time>").append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code"))
						.append("</response_code>" + "<response_msg>").append(cwbmap.get("response_msg")).append("</response_msg>" + "<mac>").append(cwbmap.get("mac"))
						.append("</mac>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
			}
			
		}
		
		
		return str.toString().replace("null", "");

	}

	/**
	 * 创建： 支付结果 撤销交易 派件异常 签收结果 的MAC字符串
	 * 
	 * @param cwbmap
	 * @return String
	 */
	public static String createMACXML_payAmount(Map cwbmap) {
		StringBuffer str = new StringBuffer("");
		str.append("<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>").append(cwbmap.get("transtype"))
				.append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid")).append("</termid>" + "<response_time>")
				.append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code")).append("</response_code>" + "<response_msg>")
				.append(cwbmap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
		return str.toString().replaceAll("null", "");
	}
	/**
	 * 返回的XML信息,去掉mac标签的标签的报文
	 * @param cwbMap
	 * @return
	 */
	public static String createMACXML_cwbSign(Map cwbMap) {
		StringBuffer str = new StringBuffer("");
		str.append("<transaction><transaction_header>" + "<version>").append(cwbMap.get("version")).append("</version>" + "<transtype>").append(cwbMap.get("transtype"))
				.append("</transtype>" + "<employno>").append(cwbMap.get("employno")).append("</employno>" + "<termid>").append(cwbMap.get("termid")).append("</termid>" + "<response_time>")
				.append(cwbMap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbMap.get("response_code")).append("</response_code>" + "<response_msg>")
				.append(cwbMap.get("response_msg")).append("</response_msg>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
		return str.toString().replaceAll("null", "");
	}

	/**
	 * 创建： 响应支付结果 撤销交易 派件异常 签收结果 的XML文件
	 * 
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_payAmount(Map cwbmap) {
		StringBuffer str = new StringBuffer("");
		str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>")
				.append(cwbmap.get("transtype")).append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid"))
				.append("</termid>" + "<response_time>").append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code"))
				.append("</response_code>" + "<response_msg>").append(cwbmap.get("response_msg")).append("</response_msg>" + "<mac>").append(cwbmap.get("mac"))
				.append("</mac>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
		return str.toString().replaceAll("null", "");
	}

	/**
	 * 创建： 响应 派件异常 的XML文件
	 * 
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_toExptFeedBack(Map cwbmap) {
		StringBuffer str = new StringBuffer("");
		str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>")
				.append(cwbmap.get("transtype")).append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid"))
				.append("</termid>" + "<response_time>").append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code"))
				.append("</response_code>" + "<response_msg>").append(cwbmap.get("response_msg")).append("</response_msg>" + "<mac>").append(cwbmap.get("mac"))
				.append("</mac>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
		return str.toString().replaceAll("null", "");
	}

	/**
	 * 创建： 响应 撤销交易 的XML文件
	 * 
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_toBackOut(Map cwbmap) {
		StringBuffer str = new StringBuffer("");
		str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>")
				.append(cwbmap.get("transtype")).append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid"))
				.append("</termid>" + "<response_time>").append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code"))
				.append("</response_code>" + "<response_msg>").append(cwbmap.get("response_msg")).append("</response_msg>" + "<mac>").append(cwbmap.get("mac"))
				.append("</mac>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
		return str.toString().replaceAll("null", "");
	}

}
