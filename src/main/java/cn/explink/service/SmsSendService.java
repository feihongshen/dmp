package cn.explink.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.tempuri.WmgwLocator;

import cn.emay.sdk.test.SingletonClient;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.SmsConfigDAO;
import cn.explink.dao.SmsConfigModelDAO;
import cn.explink.dao.SmsManageDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.SmsConfig;
import cn.explink.domain.SmsConfigModel;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.SmsSendManageEnum;
import cn.explink.util.ExcelUtils;
import cn.explink.util.StreamingStatementCreator;

@Service
@DependsOn({ "systemInstallService" })
public class SmsSendService implements SystemConfigChangeListner, ApplicationListener<ContextRefreshedEvent> {
	private static boolean warning = true;// 警告标识，如果发送过警告信息，则记录为false
	private static boolean warning_yimei = true;// 警告标识，如果发送过警告信息，则记录为false 针对亿美
	private static WmgwLocator wmgwLocator = new WmgwLocator();

	@Autowired
	SmsConfigDAO smsConfigDAO;
	@Autowired
	SmsConfigModelDAO smsconfigModelDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	private CamelContext camelContext;

	@Autowired
	SystemInstallService systemInstallService;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	SmsManageDao smsManageDao;
	@Autowired
	JdbcTemplate jdbcTemplate;

	ObjectMapper objectMapper = new ObjectMapper();
	ObjectReader orderFlowReader = this.objectMapper.reader(OrderFlow.class);

	private static Logger logger = LoggerFactory.getLogger(SmsSendService.class);
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private static final String MQ_FROM_URI_ORDER_FLOW = "jms:queue:VirtualTopicConsumers.sms.orderFlow";

	public void init() {
		logger.info("init sms camel routes");
		try {
			final String smsConsumerCount = this.systemInstallService.getParameter("sms.consumerCount", "1");
			this.camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					this.from(MQ_FROM_URI_ORDER_FLOW + "?concurrentConsumers=" + 15).to("bean:smsSendService?method=sendFlow").routeId("短信发送");
				}
			});
		} catch (Exception e) {
			logger.error("camel context start fail", e);
		}

	}

	/**
	 * 存储发送短信的记录，
	 * 
	 * @param recipients
	 *            收信人 未知收件人默认为"未知"
	 * @param mobileIds
	 *            手机号 ,号隔开多个手机号
	 * @param senddetail
	 *            发送内容
	 * @param userid
	 *            操作人id 如果是系统发送 为-1
	 * @param ip
	 *            操作人IP 如果是系统发送 显示为"系统本机"
	 * @param channel
	 *            渠道签名 已知 亿美为【当当】 移动梦网为 【易普联科】
	 */
	private String saveSendSms(String recipients, String mobileIds, String senddetail, Long userid, String ip, int channel) {
		String changeSign = "【易普联科】";
		if (channel == 1) {
			changeSign = "【当当】";
		}

		String ids = "(";// 用于获得插入过的短信记录id，可以进行update
		String num = "" + ((senddetail.length() + 69) / (70 - changeSign.length()));// 获得当前发送短信内容的条数，算法：发送内容字数+69
																					// 除以（70个字为1条-渠道签名字数）
		for (String consigneemobile : mobileIds.split(",")) {
			ids += this.smsManageDao.creSendSms(recipients, consigneemobile, senddetail, num, userid, ip, channel) + ",";
		}
		if (ids.length() > 1) {
			ids = ids.substring(0, ids.length() - 1);
		}
		ids += ")";
		return ids;
	}

	/**
	 * mobileIds手机号可以以英文半角逗号分割，多少个手机号 num就是多少
	 * 
	 * @param mobileIds
	 *            手机号
	 * @param strMsg
	 *            信息
	 * @param num
	 *            信息数
	 * @param customerId
	 *            供货商id 用于分辨短信渠道 0 表示使用默认的短信渠道
	 * @param recipients
	 *            收信人 未知收件人默认为"未知"
	 * @param ip
	 *            操作人IP 如果是系统发送 显示为"系统本机"
	 * @param channel
	 *            渠道签名 已知 亿美为【当当】 移动梦网为 【易普联科】
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String sendSms(String mobileIds, String strMsg, Integer num, long customerId, String recipients, Long userid, String ip) throws UnsupportedEncodingException {

		// strMsg = new
		// String(strMsg.getBytes("ISO-8859-1"),"UTF-8");//web服务端只接受UTF—8方式的编码
		String ids = "(-1)";// 用于标识没有创建发送短信记录。或者创建短信记录失败。
		try {
			int channel = 0;
			if (customerId > 0) {
				try {
					channel = this.customerDAO.getCustomerById(customerId).getSmschannel();
				} catch (Exception e) {
				}
			}
			SmsConfig smsConf = this.smsConfigDAO.getAllSmsConfig(channel);

			ids = this.saveSendSms(recipients, mobileIds, strMsg, userid, ip, channel);

			if ((mobileIds == null) || (mobileIds.trim().length() != 11)) {
				this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "手机号格式不正确", ids);
				return "手机号格式不正确";
			}

			if (smsConf != null) {
				if (smsConf.getIsOpen() == 1) {
					if (smsConf.getChannel() == 1) {// 使用亿美渠道发送短信 针对当当需求
						try {
							int returnValue = SingletonClient.getClient(smsConf.getName(), smsConf.getPassword()).sendSMS(new String[] { mobileIds }, strMsg, "", 5);

							logger.info("亿美sms send return value：{}", returnValue);
							// 获取短信剩余条数，剩余warningSwitch条时发送预警
							String balance = SingletonClient.getClient().getBalance();
							if (balance.equals("27") || balance.equals("303") || balance.equals("305") || balance.equals("999")) {
								logger.info("亿美sms get Balance 异常码：{}", balance);
								this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "亿美返回异常吗：" + balance, ids);
							} else {
								Double strRet = Double.valueOf(balance) * 10;
								logger.info("亿美sms get Balance：{}", strRet);

								if (smsConf.getMonitor() == 1) {// 判断是否打开监控开关
																// flage == 1
									if ((strRet < smsConf.getWarningcount()) && (strRet > 0)) {// 判断是否符合预警条件
																								// count
										if (SmsSendService.warning_yimei) {// 判断是否已经预警过
											SmsSendService.warning_yimei = false;
											String warningMobileIds = smsConf.getPhone(); // 预警手机号
											String warningStrMsg = smsConf.getWarningcontent(); // 预警内容
											returnValue = SingletonClient.getClient().sendSMS(warningMobileIds.split(","), warningStrMsg, "", 5);
											logger.info("亿美sms预警 send return value：{}", returnValue);
											SmsSendService.wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(smsConf.getName(), smsConf.getPassword(), warningMobileIds, warningStrMsg,
													warningMobileIds.split(",").length);
										}
									} else {// 当重新充值以后，将重置预警状态
										SmsSendService.warning_yimei = true;
									}

								} else if (strRet < 1) {
									return "没有开启预警，账户余额不足！";
								}
							}
						} catch (Exception e) {
							logger.error("亿美sms send error");
							// 更新短信发送记录状态
							this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), e.getMessage() == null ? "发送过程出现异常" : e.getMessage().length() > 200 ? e.getMessage() : e
									.getMessage().substring(e.getMessage().length() - 199), ids);
						}
					} else {// 使用默认渠道发短信
						SmsSendService.wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(smsConf.getName(), smsConf.getPassword(), mobileIds, strMsg, num.intValue());
						// 获取短信剩余条数，剩余warningSwitch条时发送预警
						Integer strRet = SmsSendService.wmgwLocator.getwmgwSoap().mongateQueryBalance(smsConf.getName(), smsConf.getPassword());
						if (strRet < 1) {
							this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "发送过程出现异常:短信账户余额不足！", ids);
							SmsSendService.wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(smsConf.getName(), smsConf.getPassword(), mobileIds, strMsg, num.intValue());
							return "短信账户余额不足！";
						}
						if (smsConf.getMonitor() == 1) {// 判断是否打开监控开关 flage == 1
							if (strRet < smsConf.getWarningcount()) {// 判断是否符合预警条件
																		// count
								if (SmsSendService.warning) {// 判断是否已经预警过
									String warningMobileIds = smsConf.getPhone(); // 预警手机号
									String warningStrMsg = smsConf.getWarningcontent(); // 预警内容
									SmsSendService.wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(smsConf.getName(), smsConf.getPassword(), warningMobileIds, warningStrMsg,
											warningMobileIds.split(",").length);
									SmsSendService.warning = false;
								}
							} else {// 当重新充值以后，将重置预警状态
								SmsSendService.warning = true;
							}
						} else if (strRet < 1) {
							return "没有开启预警，账户余额不足！";
						}
					}
				} else {
					this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "短信账户没有开启", ids);
					return "短信账户没有开启";
				}
			} else {
				this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "没有设置短信账户", ids);
				return "没有设置短信账户";
			}
		} catch (RemoteException e) {
			logger.error("接口调用发生异常 ", e);
			logger.error("接口调用发生异常  ids:{}", ids);
			// 更新短信发送记录状态
			this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(),
					e.getMessage() == null ? "接口调用发生异常" : e.getMessage().length() > 200 ? e.getMessage() : e.getMessage().substring(e.getMessage().length() - 199), ids);
			return "接口调用发生异常";
		} catch (ServiceException e) {
			logger.error("接口调用发生异常 ", e);
			logger.error("接口调用发生异常  ids:{}", ids);
			// 更新短信发送记录状态
			this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(),
					e.getMessage() == null ? "接口调用发生异常" : e.getMessage().length() > 200 ? e.getMessage() : e.getMessage().substring(e.getMessage().length() - 199), ids);
			return "接口调用发生异常";
		}
		// 更新短信发送记录状态
		this.smsManageDao.updateSendSmsState(SmsSendManageEnum.SUCCESS.getValue(), "", ids);
		return "发送短信成功";
	}

	/**
	 * mobileIds手机号可以以英文半角逗号分割，多少个手机号 num就是多少
	 * 
	 * @param mobileIds
	 *            手机号
	 * @param strMsg
	 *            信息
	 * @param num
	 *            信息数
	 * @param customerId
	 *            供货商id 用于分辨短信渠道 0 表示使用默认的短信渠道
	 * @param recipients
	 *            收信人 未知收件人默认为"未知"
	 * @param ip
	 *            操作人IP 如果是系统发送 显示为"系统本机"
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String sendSmsInterface(String mobileIds, String strMsg, Integer num, String recipients, Long userid, String ip, String name, String password) throws UnsupportedEncodingException {
		String ids = "(-1)";// 用于标识没有创建发送短信记录。或者创建短信记录失败。
		try {
			// SmsConfig smsConf = smsConfigDAO.getAllSmsConfig(0);

			ids = this.saveSendSms(recipients, mobileIds, strMsg, userid, ip, 0);

			if ((mobileIds == null) || (mobileIds.trim().length() != 11)) {
				this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "手机号格式不正确", ids);
				return "手机号格式不正确";
			}

			// 使用默认渠道发短信
			SmsSendService.wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(name, password, mobileIds, strMsg, num.intValue());
			// 获取短信剩余条数，剩余warningSwitch条时发送预警
			Integer strRet = SmsSendService.wmgwLocator.getwmgwSoap().mongateQueryBalance(name, password);
			if (strRet < 1) {
				this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "发送过程出现异常:短信账户余额不足！", ids);
				SmsSendService.wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(name, password, mobileIds, strMsg, num.intValue());
				return "短信账户余额不足！";
			}
		} catch (RemoteException e) {
			logger.error("接口调用发生异常 ", e);
			logger.error("接口调用发生异常  ids:{}", ids);
			// 更新短信发送记录状态
			this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(),
					e.getMessage() == null ? "接口调用发生异常" : e.getMessage().length() > 200 ? e.getMessage() : e.getMessage().substring(e.getMessage().length() - 199), ids);
			return "接口调用发生异常";
		} catch (ServiceException e) {
			logger.error("接口调用发生异常 ", e);
			logger.error("接口调用发生异常  ids:{}", ids);
			// 更新短信发送记录状态
			this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(),
					e.getMessage() == null ? "接口调用发生异常" : e.getMessage().length() > 200 ? e.getMessage() : e.getMessage().substring(e.getMessage().length() - 199), ids);
			return "接口调用发生异常";
		}
		// 更新短信发送记录状态
		this.smsManageDao.updateSendSmsState(SmsSendManageEnum.SUCCESS.getValue(), "", ids);
		return "发送短信成功";
	}

	/**
	 * 按新模板设置发送短信
	 * 
	 * @param mobileIds
	 * @param num
	 * @param smsConf
	 * @param smsConfigModel
	 * @param customername
	 *            原来是供货商名称 现在变更为公司名称
	 * @param delivername
	 * @param deliverphone
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public int sendSmsByTemplate(String mobileIds, Integer num, SmsConfig smsConf, SmsConfigModel smsConfigModel, String customername, String delivername, String deliverphone, String recipients,
			String receivablefee, CwbOrder co) throws UnsupportedEncodingException {

		// strMsg = new
		// String(strMsg.getBytes("ISO-8859-1"),"UTF-8");//web服务端只接受UTF—8方式的编码
		String ids = "(-1)";// 用于标识没有创建发送短信记录。或者创建短信记录失败。
		try {
			if (smsConf != null) {
				if (smsConf.getIsOpen() == 1) {
					String strMsg = smsConfigModel.getTemplatecontent().replaceAll("customername", customername).replaceAll("delivername", delivername).replaceAll("deliverphone", deliverphone);

					strMsg = this.buildVipshopSmtMessageModel(smsConfigModel, delivername, deliverphone, co, strMsg);
					if (strMsg == null) {
						return 0;
					}

					ids = this.saveSendSms(recipients, mobileIds, strMsg, -1L, "服务器", smsConf.getChannel());
					if ((mobileIds == null) || (mobileIds.trim().length() != 11)) {
						this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "手机号格式不正确", ids);
						return 0;
					}
					SystemInstall sysi = this.systemInstallDAO.getSystemInstall("receivablefee");
					if ("yes".equals(sysi.getValue())) {
						strMsg = strMsg.replaceAll("receivablefee", receivablefee);
					} else {
						strMsg = strMsg.replaceAll("receivablefee", "");
					}

					if (smsConf.getChannel() == 1) {

						try {
							int returnValue = SingletonClient.getClient(smsConf.getName(), smsConf.getPassword()).sendSMS(new String[] { mobileIds }, strMsg, "", 5);// 当当的渠道一定要加签名
							logger.info("亿美sms send return value：{}", returnValue);
							// 获取短信剩余条数，剩余warningSwitch条时发送预警

							String balance = SingletonClient.getClient().getBalance();
							if (balance.equals("27") || balance.equals("303") || balance.equals("305") || balance.equals("999")) {
								logger.info("亿美sms get Balance 异常码：{}", balance);
								this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "亿美返回异常吗：" + balance, ids);
							} else {
								Double strRet = Double.valueOf(balance) * 10;
								logger.info("亿美sms get Balance：{}", strRet);
								if (smsConf.getMonitor() == 1) {// 判断是否打开监控开关
																// flage == 1
									if ((strRet < smsConf.getWarningcount()) && (strRet > 0)) {// 判断是否符合预警条件
																								// count
										if (SmsSendService.warning_yimei) {// 判断是否已经预警过
											SmsSendService.warning_yimei = false;
											String warningMobileIds = smsConf.getPhone(); // 预警手机号
											String warningStrMsg = smsConf.getWarningcontent(); // 预警内容
											returnValue = SingletonClient.getClient().sendSMS(warningMobileIds.split(","), warningStrMsg, "", 5);
											logger.info("亿美sms预警 send return value：{}", returnValue);
										}
									} else {// 当重新充值以后，将重置预警状态
										SmsSendService.warning_yimei = true;
									}
								} else {
									Integer counts = this.getResidualCount(smsConf.getName(), smsConf.getPassword());
									if ((counts == null) || (counts < 1)) {
										logger.error("亿美sms send error");
										// 更新短信发送记录状态
										this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "发送过程出现异常:短信账户余额不足！", ids);
									}
									SmsSendService.wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(smsConf.getName(), smsConf.getPassword(), mobileIds, strMsg, num.intValue());
									return 0;
								}
							}
						} catch (Exception e) {
							logger.error("亿美sms send error");
							// 更新短信发送记录状态
							this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), e.getMessage() == null ? "发送过程出现异常" : e.getMessage().length() > 200 ? e.getMessage() : e
									.getMessage().substring(e.getMessage().length() - 199), ids);
						}

					} else {

						SmsSendService.wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(smsConf.getName(), smsConf.getPassword(), mobileIds, strMsg, num.intValue());
						// 获取短信剩余条数，剩余warningSwitch条时发送预警
						Integer strRet = this.getResidualCount(smsConf.getName(), smsConf.getPassword());
						if ((smsConf.getMonitor() == 1) && (strRet != -500) && (strRet < smsConf.getWarningcount())) {// 判断是否符合预警条件
																														// count
							if (SmsSendService.warning) {// 判断是否已经预警过
								String warningMobileIds = smsConf.getPhone(); // 预警手机号
								String warningStrMsg = smsConf.getWarningcontent(); // 预警内容
								SmsSendService.wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(smsConf.getName(), smsConf.getPassword(), warningMobileIds, warningStrMsg,
										warningMobileIds.split(",").length);
								logger.info("短信发送，余额超过预警！平台返回余额：{},当前设置余额：{}", strRet, smsConf.getWarningcount());
								SmsSendService.warning = false;
							}
						} else {// 当重新充值以后，将重置预警状态
							SmsSendService.warning = true;
						}
					}
				} else {
					this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "短信账户没有开启", ids);
					return 0;
				}
			} else {
				// return "数据表没有数据，没做账户配置";
				this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(), "数据表没有数据，没做账户配置", ids);
				return 0;
			}
			logger.info("短信发送，成功！");
			// 更新短信发送记录状态
			this.smsManageDao.updateSendSmsState(SmsSendManageEnum.SUCCESS.getValue(), "", ids);
			return 1;
		} catch (RemoteException e) {
			logger.error("接口调用发生异常  ids:{}", ids);
			// 更新短信发送记录状态
			this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(),
					e.getMessage() == null ? "接口调用发生异常" : e.getMessage().length() > 200 ? e.getMessage() : e.getMessage().substring(e.getMessage().length() - 199), ids);
			return 0;
		} catch (ServiceException e) {
			logger.error("接口调用发生异常  ids:{}", ids);
			// 更新短信发送记录状态
			this.smsManageDao.updateSendSmsState(SmsSendManageEnum.Failure.getValue(),
					e.getMessage() == null ? "接口调用发生异常" : e.getMessage().length() > 200 ? e.getMessage() : e.getMessage().substring(e.getMessage().length() - 199), ids);
			return 0;
		}
	}

	/**
	 * 构建唯品会上门退模板
	 * 
	 * @param smsConfigModel
	 * @param delivername
	 * @param deliverphone
	 * @param cwb
	 * @param strMsg
	 * @return
	 */
	private String buildVipshopSmtMessageModel(SmsConfigModel smsConfigModel, String delivername, String deliverphone, CwbOrder co, String strMsg) {
		SystemInstall smtmodel = this.systemInstallDAO.getSystemInstall("isSmtMessageModel"); // 是否开启上门退短信模板

		if ((smtmodel != null) && smtmodel.getValue().equals("yes")) {
			String cwb = co.getCwb();
			String tailnumber = cwb.contains("-T") && (cwb.length() > 6) ? cwb.substring(0, cwb.indexOf("-T")) : null;
			if (tailnumber == null) {
				logger.info("单号{}不满足发短信条件，退出", cwb);
				return null;
			}
			if (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				logger.info("单号{}不满足发短信条件,不是上门退退类型，退出", cwb);
				return null;
			}

			strMsg = smsConfigModel.getTemplatecontent().replaceAll("tailnumber", cwb).replaceAll("delivername", delivername).replaceAll("deliverphone", deliverphone);
		} else {
			if (strMsg.contains("tailnumber")) {
				return null;
			}
		}
		return strMsg;
	}

	// 获取短信剩余条数
	public Integer getResidualCount(String name, String password) {
		try {
			return SmsSendService.wmgwLocator.getwmgwSoap().mongateQueryBalance(name, password);
		} catch (RemoteException e) {
			logger.error("获取短信余额异常", e);
		} catch (ServiceException e) {
			logger.error("获取短信余额异常", e);
		}
		return -500;
	}

	/**
	 * 流程调用公共方法
	 * 
	 * @param of
	 * @return
	 */
	public int sendSms(OrderFlow of) {
		CwbOrder order = this.cwbDAO.getCwbByCwb(of.getCwb());
		Branch b = this.branchDAO.getBranchByBranchid(of.getBranchid());
		int channel = 0;// 根据供货商获得需要使用的短信渠道
		if (order.getCustomerid() > 0) {
			try {
				channel = this.customerDAO.getCustomerById(order.getCustomerid()).getSmschannel();
			} catch (Exception e) {
			}
		}

		if ((b.getSitetype() == BranchEnum.KuFang.getValue()) && ((of.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()))) {// 库房的操作

			SmsConfig smsConfig = this.smsConfigDAO.getAllSmsConfig(channel);
			if (smsConfig == null) {
				logger.info("短信发送失败,没创建短信账号，订单号:{}", of.getCwb());
				return 0;
			}
			if (smsConfig.getIsOpen() == 0) {
				logger.info("短信发送失败,短信账号没开启，订单号:{}", of.getCwb());
				return 0;
			}
			SmsConfigModel sms = this.smsconfigModelDAO.getSmsConfigByFlowtype(of.getFlowordertype());
			if (sms == null) {
				logger.info("短信发送失败,没设置 " + of.getFlowordertype() + "环节 规则，订单号:{}", of.getCwb());
				return 0;
			}

			if ((smsConfig != null) && (sms != null)) {
				if ((sms.getCustomerids() == null ? "" : sms.getCustomerids()).indexOf(order.getCustomerid() + "") > -1) {
					List<Customer> clist = this.customerDAO.getCustomerByIdsAndId(sms.getCustomerids(), order.getCustomerid());
					if ((clist == null) || (clist.size() == 0)) {
						logger.info("短信发送失败,没设置该供应商规则，订单号:{}", of.getCwb());
						return 0;
					}
					if (order.getReceivablefee().subtract(sms.getMoney()).longValue() >= 0) {
						Customer c = this.customerDAO.getCustomerById(order.getCustomerid());
						List<User> uList = this.userDAO.getUserByid(order.getDeliverid());
						String realname = "";
						String usermobile = "";
						String receivablefee = order.getReceivablefee().toString();
						if ((uList != null) && (uList.size() > 0)) {
							realname = uList.get(0).getRealname();
							usermobile = uList.get(0).getUsermobile();
						}
						try {
							logger.info("短信发送，订单号:{}", of.getCwb());
							return this.sendSmsByTemplate(order.getConsigneemobile(), 1, smsConfig, sms, c.getCompanyname(), realname, usermobile, order.getConsigneename(), receivablefee, order);
						} catch (UnsupportedEncodingException e) {
							logger.info("短信发送，失败,订单号:{}", of.getCwb());
							return 0;
						}
					} else {
						logger.info("短信发送失败,订单代收金额没有达到设置的规则，订单号:{}", of.getCwb());
						return 0;
					}
				} else {
					logger.info("短信发送失败,没设置该供应商规则，订单号:{}", of.getCwb());
					return 0;
				}
			}

		} else if (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {// 站点的操作
			SmsConfig smsConfig = this.smsConfigDAO.getAllSmsConfig(channel);
			if (smsConfig == null) {
				logger.info("短信发送失败,没创建短信账号，订单号:{}", of.getCwb());
				return 0;
			}
			if (smsConfig.getIsOpen() == 0) {
				logger.info("短信发送失败,短信账号没开启，订单号:{}", of.getCwb());
				return 0;
			}
			SmsConfigModel sms = this.smsconfigModelDAO.getSmsConfigByFlowtype(of.getFlowordertype());
			if (sms == null) {
				logger.info("短信发送失败,没设置 " + of.getFlowordertype() + "环节 规则，订单号:{}", of.getCwb());
				return 0;
			}
			if ((smsConfig != null) && (sms != null)) {
				if ((sms.getBranchids() == null ? "" : sms.getBranchids()).indexOf(of.getBranchid() + "") > -1) {
					List<Branch> bList = this.branchDAO.getBranchByIdsAndId(sms.getBranchids(), of.getBranchid());
					if ((bList == null) || (bList.size() == 0)) {
						logger.info("短信发送失败,没设置该站点规则，订单号:{}", of.getCwb());
						return 0;
					}
					if ((sms.getCustomerids() == null ? "" : sms.getCustomerids()).indexOf(order.getCustomerid() + "") > -1) {
						List<Customer> clist = this.customerDAO.getCustomerByIdsAndId(sms.getCustomerids(), order.getCustomerid());
						if ((clist == null) || (clist.size() == 0)) {
							logger.info("短信发送失败,没设置该供应商规则，订单号:{}", of.getCwb());
							return 0;
						}
						if (order.getReceivablefee().subtract(sms.getMoney()).longValue() >= 0) {
							Customer c = this.customerDAO.getCustomerById(order.getCustomerid());
							List<User> uList = this.userDAO.getUserByid(order.getDeliverid());
							String realname = "";
							String usermobile = "";
							String receivablefee = order.getReceivablefee().toString();
							if ((uList != null) && (uList.size() > 0)) {
								realname = uList.get(0).getRealname();
								usermobile = uList.get(0).getUsermobile();
							}
							try {
								logger.info("短信发送，订单号:{}", of.getCwb());
								return this.sendSmsByTemplate(order.getConsigneemobile(), 1, smsConfig, sms, c.getCompanyname(), realname, usermobile, order.getConsigneename(), receivablefee, order);
							} catch (UnsupportedEncodingException e) {
								logger.info("短信发送失败，订单号:{}", of.getCwb());
								return 0;
							}
						} else {
							logger.info("短信发送失败,订单代收金额没有达到设置的规则，订单号:{}", of.getCwb());
							return 0;
						}
					} else {
						logger.info("短信发送失败,没设置该供应商规则，订单号:{}", of.getCwb());
						return 0;
					}
				} else {
					logger.info("短信发送失败,没设置该站点规则，订单号:{}", of.getCwb());
					return 0;
				}
			}
		} else if (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {// 小件员领货

			SmsConfig smsConfig = this.smsConfigDAO.getAllSmsConfig(channel);
			if (smsConfig == null) {
				logger.info("短信发送失败,没创建短信账号，订单号:{}", of.getCwb());
				return 0;
			}
			if (smsConfig.getIsOpen() == 0) {
				logger.info("短信发送失败,短信账号没开启，订单号:{}", of.getCwb());
				return 0;
			}
			SmsConfigModel sms = this.smsconfigModelDAO.getSmsConfigByFlowtype(of.getFlowordertype());
			if (sms == null) {
				logger.info("短信发送失败,没设置 " + of.getFlowordertype() + "环节 规则，订单号:{}", of.getCwb());
				return 0;
			}
			if ((smsConfig != null) && (sms != null)) {
				if ((sms.getBranchids() == null ? "" : sms.getBranchids()).indexOf(of.getBranchid() + "") > -1) {
					List<Branch> bList = this.branchDAO.getBranchByIdsAndId(sms.getBranchids(), of.getBranchid());
					if ((bList == null) || (bList.size() == 0)) {
						logger.info("短信发送失败,没设置该站点规则，订单号:{}", of.getCwb());
						return 0;
					}
					if ((sms.getCustomerids() == null ? "" : sms.getCustomerids()).indexOf(order.getCustomerid() + "") > -1) {
						List<Customer> clist = this.customerDAO.getCustomerByIdsAndId(sms.getCustomerids(), order.getCustomerid());
						if ((clist == null) || (clist.size() == 0)) {
							logger.info("短信发送失败,没设置该供应商规则，订单号:{}", of.getCwb());
							return 0;
						}
						if (order.getCaramount().subtract(sms.getMoney()).longValue() >= 0) {
							Customer c = this.customerDAO.getCustomerById(order.getCustomerid());
							List<User> uList = this.userDAO.getUserByid(order.getDeliverid());
							String realname = "";
							String usermobile = "";
							String receivablefee = order.getReceivablefee().toString();
							if ((uList != null) && (uList.size() > 0)) {
								realname = uList.get(0).getRealname();
								usermobile = uList.get(0).getUsermobile();
							}
							try {
								logger.info("短信发送，订单号:{}", of.getCwb());
								SystemInstall systemInstall = this.systemInstallDAO.getSystemInstall("isSendSmsAgain");
								if ((systemInstall != null) && systemInstall.getValue().equals("no")) {
									int num = this.smsManageDao.getNumByCwbAndDeliverid(order.getCwb(), order.getDeliverid());
									if (num > 0) {
										logger.info("短信发送失败,领货给小件员已经发过短信,订单号:{}", of.getCwb());
										return 0;
									} else {
										int count = this.sendSmsByTemplate(order.getConsigneemobile(), 1, smsConfig, sms, c.getCompanyname(), realname, usermobile, order.getConsigneename(),
												receivablefee, order);
										if (count == 1) {
											this.smsManageDao.createInfo(order.getCwb(), order.getDeliverid(), order.getFlowordertype(), of.getBranchid());
										}
										return count;
									}
								} else {
									return this.sendSmsByTemplate(order.getConsigneemobile(), 1, smsConfig, sms, c.getCompanyname(), realname, usermobile, order.getConsigneename(), receivablefee,
											order);
								}
							} catch (UnsupportedEncodingException e) {
								logger.info("短信发送失败，订单号:{}", of.getCwb());
								return 0;
							}
						} else {
							logger.info("短信发送失败,订单代收金额没有达到设置的规则，订单号:{}", of.getCwb());
							return 0;
						}
					} else {
						logger.info("短信发送失败,没设置该供应商规则，订单号:{}", of.getCwb());
						return 0;
					}
				} else {
					logger.info("短信发送失败,没设置该站点规则，订单号:{}", of.getCwb());
					return 0;
				}
			}

		} else if (of.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
			SmsConfig smsConfig = this.smsConfigDAO.getAllSmsConfig(channel);
			if (smsConfig == null) {
				logger.info("短信发送失败,没创建短信账号，订单号:{}", of.getCwb());
				return 0;
			}
			if (smsConfig.getIsOpen() == 0) {
				logger.info("短信发送失败,短信账号没开启，订单号:{}", of.getCwb());
				return 0;
			}
			// {"cwbOrder":{"opscwbid":22182,"startbranchid":235,"currentbranchid":0,"nextbranchid":0,"deliverybranchid":235,"backtocustomer_awb":"","cwbflowflag":"1","carrealweight":0.000,"cartype":"","carwarehouse":"234","carsize":"","backcaramount":0.00,"sendcarnum":1,"backcarnum":0,"caramount":38.40,"backcarname":"","sendcarname":"","deliverid":1074,"deliverystate":1,"emailfinishflag":0,"reacherrorflag":0,"orderflowid":0,"flowordertype":36,"cwbreachbranchid":0,"cwbreachdeliverbranchid":0,"podfeetoheadflag":"0","podfeetoheadtime":null,"podfeetoheadchecktime":null,"podfeetoheadcheckflag":"0","leavedreasonid":0,"deliversubscribeday":null,"customerwarehouseid":"0","emaildateid":264,"emaildate":"2013-07-09 13:31:55","serviceareaid":0,"customerid":135,"shipcwb":"","consigneeno":"","consigneename":"庞婷婷","consigneeaddress":"中国江苏南京市江宁区,南京市江宁区汤山街道高庄社区高庄村126号","consigneepostcode":"","consigneephone":"*02584141562","cwbremark":"","customercommand":"","transway":"","cwbprovince":"江苏","cwbcity":"南京市","cwbcounty":"江宁区","receivablefee":38.40,"paybackfee":0.00,"cwb":"6366006105","shipperid":0,"cwbordertypeid":1,"consigneemobile":"15050554768","transcwb":"","destination":"","cwbdelivertypeid":"0","exceldeliver":"","excelbranch":"南京站点","excelimportuserid":1070,"state":1,"printtime":"","commonid":0,"commoncwb":"","signtypeid":0,"podrealname":"","podtime":"","podsignremark":"","modelname":null,"scannum":1,"isaudit":0,"backreason":"","leavedreason":"","paywayid":1,"newpaywayid":"2","tuihuoid":234,"cwbstate":1,"remark1":"","remark2":"","remark3":"","remark4":"","remark5":"","backreasonid":0,"multi_shipcwb":null,"packagecode":"","backreturnreasonid":0,"backreturnreason":"","handleresult":0,"handleperson":0,"handlereason":"","addresscodeedittype":3},"deliveryState":{"id":1903,"cwb":"6366006105","deliveryid":1074,"receivedfee":38.40,"returnedfee":0.00,"businessfee":38.40,"deliverystate":1,"cash":0.00,"pos":38.40,"posremark":"","mobilepodtime":1373349407000,"checkfee":0.00,"checkremark":"","receivedfeeuser":1074,"createtime":"2013-07-09 13:38:19","otherfee":0.00,"podremarkid":0,"deliverstateremark":"","isout":0,"pos_feedback_flag":0,"userid":1074,"gcaid":148,"sign_typeid":1,"sign_man":"庞婷婷","sign_time":"2013-07-09 13:39:48","backreason":null,"leavedreason":null,"deliverybranchid":235,"deliverystateStr":null,"deliverytime":null,"auditingtime":null,"customerid":135,"payupid":0,"issendcustomer":0,"isautolinghuo":0}}
			String floworderdetail = of.getFloworderdetail();
			String deliverystate = floworderdetail.substring(floworderdetail.indexOf("\"deliverystate\":") + 16, floworderdetail.indexOf("\"deliverystate\":") + 17);
			int deliverystateInt = 0;
			if ((deliverystate != null) && !deliverystate.equals("")) {
				try {
					deliverystateInt = Integer.parseInt(deliverystate) + 100;
				} catch (NumberFormatException e) {
					logger.error("短信发送失败,int类型转换异常", e);
					deliverystateInt = 0;
					return 0;
				}
			}
			SmsConfigModel sms = this.smsconfigModelDAO.getSmsConfigByFlowtype(deliverystateInt);
			if (sms == null) {
				logger.info("短信发送失败,没设置 " + of.getFlowordertype() + "环节 规则，订单号:{}", of.getCwb());
				return 0;
			}
			if ((smsConfig != null) && (sms != null)) {
				if ((sms.getBranchids() == null ? "" : sms.getBranchids()).indexOf(of.getBranchid() + "") > -1) {
					List<Branch> bList = this.branchDAO.getBranchByIdsAndId(sms.getBranchids(), of.getBranchid());
					if ((bList == null) || (bList.size() == 0)) {
						logger.info("短信发送失败,没设置该站点规则，订单号:{}", of.getCwb());
						return 0;
					}
					if ((sms.getCustomerids() == null ? "" : sms.getCustomerids()).indexOf(order.getCustomerid() + "") > -1) {
						List<Customer> clist = this.customerDAO.getCustomerByIdsAndId(sms.getCustomerids(), order.getCustomerid());
						if ((clist == null) || (clist.size() == 0)) {
							logger.info("短信发送失败,没设置该供应商规则，订单号:{}", of.getCwb());
							return 0;
						}
						if (order.getReceivablefee().subtract(sms.getMoney()).longValue() >= 0) {
							Customer c = this.customerDAO.getCustomerById(order.getCustomerid());
							List<User> uList = this.userDAO.getUserByid(order.getDeliverid());
							String realname = "";
							String usermobile = "";
							String receivablefee = order.getReceivablefee().toString();
							if ((uList != null) && (uList.size() > 0)) {
								realname = uList.get(0).getRealname();
								usermobile = uList.get(0).getUsermobile();
							}
							try {
								logger.info("短信发送，订单号:{}", of.getCwb());
								return this.sendSmsByTemplate(order.getConsigneemobile(), 1, smsConfig, sms, c.getCompanyname(), realname, usermobile, order.getConsigneename(), receivablefee, order);
							} catch (UnsupportedEncodingException e) {
								logger.info("短信发送失败，订单号:{}", of.getCwb());
								return 0;
							}
						}
					} else {
						logger.info("短信发送失败,没设置该供货商规则，订单号:{}", of.getCwb());
						return 0;
					}
				} else {
					logger.info("短信发送失败,没设置该站点规则，订单号:{}", of.getCwb());
					return 0;
				}
				logger.info("短信发送成功，订单号:{}", of.getCwb());
				return 1;
			}
		}
		logger.info("短信发送失败,没设置模板规则，订单号:{}", of.getCwb());
		return 0;
	}

	public void sendFlow(@Header("orderFlow") String parm, @Header("MessageHeaderUUID") String messageHeaderUUID) throws Exception {
		logger.debug("orderFlow oms 环节信息处理,{}", parm);
		try{
			OrderFlow orderFlow = this.orderFlowReader.readValue(parm);
			this.sendSms(orderFlow);
		} catch (Exception e) {
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".sendFlow")
					.buildExceptionInfo(e.toString()).buildTopic(MQ_FROM_URI_ORDER_FLOW)
					.buildMessageHeader("orderFlow", parm)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
		
	}

	@Override
	public void onChange(Map<String, String> parameters) {
		if (parameters.keySet().contains("sms.consumerCount")) {
			this.init();
			return;
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			this.init();
		}
	}

	private void setSmsSendFields(String[] cloumnName1, String[] cloumnName2, String[] cloumnName3) {

		cloumnName1[0] = "发送时间";
		cloumnName2[0] = "senddate";
		cloumnName3[0] = "string";
		cloumnName1[1] = "收信人姓名";
		cloumnName2[1] = "recipients";
		cloumnName3[1] = "string";
		cloumnName1[2] = "收信人电话";
		cloumnName2[2] = "consigneemobile";
		cloumnName3[2] = "string";
		cloumnName1[3] = "发送内容";
		cloumnName2[3] = "senddetail";
		cloumnName3[3] = "string";
		cloumnName1[4] = "分条";
		cloumnName2[4] = "num";
		cloumnName3[4] = "string";
		cloumnName1[5] = "发送状态";
		cloumnName2[5] = "sendstate";
		cloumnName3[5] = "string";
		cloumnName1[6] = "失败原因";
		cloumnName2[6] = "errorDetail";
		cloumnName3[6] = "string";
		cloumnName1[7] = "发送人用户";
		cloumnName2[7] = "username";
		cloumnName3[7] = "string";
		cloumnName1[8] = "发送人";
		cloumnName2[8] = "realname";
		cloumnName3[8] = "string";
		cloumnName1[9] = "IP";
		cloumnName2[9] = "ip";
		cloumnName3[9] = "string";
	}

	public void excelSmsSendList(List<User> userList, String startSenddate, String stopSenddate, String consigneemobile, int sendstate, int channel, HttpServletRequest request,
			HttpServletResponse response) {
		String[] cloumnName1 = {};
		String[] cloumnName2 = {};
		String[] cloumnName3 = {};
		cloumnName1 = new String[10];
		cloumnName2 = new String[10];
		cloumnName3 = new String[10];
		this.setSmsSendFields(cloumnName1, cloumnName2, cloumnName3);
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;

		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {

			Map<Long, User> userMap = new HashMap<Long, User>();
			for (User u : this.userDAO.getUserForALL()) {
				userMap.put(u.getUserid(), u);
			}

			String sql1 = "SELECT * FROM sms_send_manage WHERE ";
			if (consigneemobile.trim().length() == 0) {
				sql1 += " senddate>='" + startSenddate + "' AND senddate<='" + stopSenddate + "' ";
			} else {
				sql1 += " consigneemobile='" + consigneemobile + "' ";
			}
			if (sendstate != SmsSendManageEnum.ALL.getValue()) {
				sql1 += " AND sendstate='" + sendstate + "'";
			}
			if (channel != -1) {
				sql1 += " AND channel='" + channel + "'";
			}
			final String sql = sql1;
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {

					Map<Long, User> userMap = new HashMap<Long, User>();
					for (User u : SmsSendService.this.userDAO.getUserForALL()) {
						userMap.put(u.getUserid(), u);
					}
					final Map<Long, User> userM = userMap;

					SmsSendService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
						private int count = 0;

						@Override
						public void processRow(ResultSet rs) throws SQLException {
							Row row = sheet.createRow(this.count + 1);
							row.setHeightInPoints(15);

							// System.out.println(ds.getCwb()+":"+System.currentTimeMillis());
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = null;
								try {
									if ("num".equals(cloumnName5[i])) {// 分条数据获取，系统认为，只要发送成功了
																		// 总共有几条信息，就成功几条信息
										if (rs.getInt("sendstate") == SmsSendManageEnum.SUCCESS.getValue()) {
											a = rs.getString("num") + "/" + rs.getString("num");
										} else {
											a = "0/" + rs.getString("num");
										}

									} else if ("sendstate".equals(cloumnName5[i])) {// 获取短信发送状态
										a = SmsSendManageEnum.getText(rs.getInt("sendstate")).getText();
									} else if ("username".equals(cloumnName5[i])) {// 获取短信发送状态
										a = rs.getLong("userid") > -1 ? userM.get(rs.getLong("userid")).getUsername() : "系统自动发送";
									} else if ("realname".equals(cloumnName5[i])) {// 获取短信发送状态
										a = rs.getLong("userid") > -1 ? userM.get(rs.getLong("userid")).getRealname() : "系统自动发送";
									} else {
										a = rs.getObject(cloumnName5[i]);
									}
									if (cloumnName6[i].equals("double")) {
										cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
									} else {
										cell.setCellValue(a == null ? "" : a.toString());
									}
								} catch (Exception e) {
									logger.error("", e);
								}
							}
							this.count++;

						}
					});

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);
			// System.out.println("get end:"+System.currentTimeMillis());

		} catch (Exception e) {
			logger.error("", e);
		}

	}

}
