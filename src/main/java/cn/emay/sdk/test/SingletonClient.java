package cn.emay.sdk.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.emay.sdk.client.api.Client;

public class SingletonClient {
	
	private static Logger logger =LoggerFactory.getLogger(SingletonClient.class);
	
	private static Client client = null;

	private SingletonClient() {
	}

	public synchronized static Client getClientInit(String softwareSerialNo, String key) {
		try {
			client = new Client(softwareSerialNo, key);
		} catch (Exception e) {
			logger.error("", e);
		}
		return client;
	}

	public synchronized static Client getClient(String softwareSerialNo, String key) {
		if (client == null) {
			try {
				client = new Client(softwareSerialNo, key);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return client;
	}

	public synchronized static Client getClient() {
		if (client == null) {
			try {
				client = new Client("0SDK-EMY-0130-XXXXX", "123456");
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return client;
	}

	public static void main(String str[]) {
		SingletonClient.getClient();
	}
}
