package com.commsen.wedeploy.client.rest.okhttp;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commsen.wedeploy.transport.rest.AbstractBaseRestClient;
import com.commsen.wedeploy.transport.rest.WeDeployResponse;
import com.commsen.wedeploy.transport.rest.WeDeployRestClient;

import aQute.bnd.annotation.headers.ProvideCapability;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component (
	service = WeDeployRestClient.class,
	property = {
		"http.client=OkHttp",
		"service.ranking:Integer=100"
	} 
)
@ProvideCapability (
	ns="com.commsen.wedeploy.client", 
	value="service=WeDeployRestClient"
)
public class OkHttpRestClient extends AbstractBaseRestClient {

	Logger log = LoggerFactory.getLogger(OkHttpRestClient.class);
	
	
	OkHttpClient client = new OkHttpClient();
			
//			.Builder()	//
//			.connectTimeout(10, TimeUnit.SECONDS)		//
//			.writeTimeout(10, TimeUnit.SECONDS)			//
//			.readTimeout(30, TimeUnit.SECONDS)			//
//			.build();

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	@Override
	public WeDeployResponse call(URI uri, String method, Map<String, String> headers, String body) throws IOException {

		RequestBody requestBody = null;
		if (body != null) {
			requestBody = RequestBody.create(JSON, body);
		}
		
		Request request = new Request.Builder().url(uri.toURL()).method(method, requestBody).headers(Headers.of(headers)).build();
		log.debug("REST request: {}", request);
		Response response = client.newCall(request).execute();
		log.debug("REST response: {}", response);

		return new WeDeployResponse(response.code(), response.body().string());
	}

}
