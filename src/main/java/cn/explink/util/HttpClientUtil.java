package cn.explink.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.explink.controller.DeliveryPercentController;

public class HttpClientUtil {
	
	private static Logger logger = LoggerFactory.getLogger(DeliveryPercentController.class);

	/**
     * 模拟表单提交
	 * @return 
     * 
     * @throws IOException
     */
    public static String sendPost(String mailNo, String filePath) throws IOException {
        int timeout = 5000;
        // 认证：用户/密码
        String userName = "mss-user";
        String password = "G&%j^!X;u2oJbRK#h~?M";
        // 要上传的文件的路径,要换下！！！！
        //String filePath = "G:/4ff3f5c978a47.jpg";
        String url = "https://ec.otms.cn/api/carrier/MSS/epod/transport/" + mailNo;

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        //最简单的用户凭证可以是用户名和密码这种形式。UsernamePasswordCredentials这个类可以用来表示这种情况，这种凭据包含明文的用户名和密码。
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(
                        RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build())
                .useSystemProperties().setDefaultCredentialsProvider(credentialsProvider).build();

        try {
            // 把一个普通参数和文件上传给下面这个地址
            HttpPost httpPost = new HttpPost(url);

            // 把文件转换成流对象FileBody
            FileBody bin = new FileBody(new File(filePath));
            // 相当于<input type="file" name="file"/>
            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file", bin).build();

            httpPost.setEntity(reqEntity);

            logger.info("发起请求的页面地址 " + httpPost.getRequestLine());
            // 发起请求 并返回请求的响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                // 打印响应状态
            	logger.info(""+response.getStatusLine());
                // 获取响应对象
                HttpEntity resEntity = response.getEntity();
                String Responseresult = "";
                if (resEntity != null) {
                    // 打印响应长度
                	logger.info("Response content length: " + resEntity.getContentLength());
                    // 打印响应内容
                    Responseresult = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                    logger.info(Responseresult);
                }
                // 销毁
                EntityUtils.consume(resEntity);
                return Responseresult;
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }
}
