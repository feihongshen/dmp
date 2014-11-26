package cn.emay.sdk.test;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import cn.emay.sdk.client.api.Client;

public class SingletonClient {
	private static Client client = null;

	private SingletonClient() {
	}

	public synchronized static Client getClientInit(String softwareSerialNo, String key) {
		try {
			client = new Client(softwareSerialNo, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	public synchronized static Client getClient(String softwareSerialNo, String key) {
		if (client == null) {
			try {
				client = new Client(softwareSerialNo, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	public synchronized static Client getClient() {
		// ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		if (client == null) {
			try {
				// client=new
				// Client(bundle.getString("softwareSerialNo"),bundle.getString("key"));
				client = new Client("0SDK-EMY-0130-XXXXX", "123456");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	public static void main(String str[]) {
		SingletonClient.getClient();
	}
}
