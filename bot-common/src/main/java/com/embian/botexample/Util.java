package com.embian.botexample;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Util {
	private static final int HTTP_TIMEOUT = 2; // 2 seconds

	public static byte[] getAsByteArray(String url) throws IOException {
		Builder builder = RequestConfig.custom();
		
		builder = builder
				.setConnectTimeout(HTTP_TIMEOUT * 1000)
				.setConnectionRequestTimeout(HTTP_TIMEOUT * 1000)
				.setSocketTimeout(HTTP_TIMEOUT * 1000);

		RequestConfig config = builder.build();
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		
		HttpGet request = new HttpGet(url);
		CloseableHttpResponse response = null;
		byte[] content = null;
		
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            content = EntityUtils.toByteArray(entity);
	        }
        	response.close();
        } catch (IOException e) {
        	if (response != null)
        		response.close();
        } finally {
        	client.close();
        }
		
        return content;
	}
}
