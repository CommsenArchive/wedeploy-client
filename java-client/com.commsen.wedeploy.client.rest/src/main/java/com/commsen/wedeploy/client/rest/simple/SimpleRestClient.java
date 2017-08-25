package com.commsen.wedeploy.client.rest.simple;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.osgi.service.component.annotations.Component;

import com.commsen.wedeploy.transport.rest.AbstractBaseRestClient;
import com.commsen.wedeploy.transport.rest.WeDeployResponse;
import com.commsen.wedeploy.transport.rest.WeDeployRestClient;

import aQute.bnd.annotation.headers.ProvideCapability;

@Component (
	service = WeDeployRestClient.class,
	property = {
		"http.client=Java",
		"service.ranking:Integer=0"
	} 
)
@ProvideCapability (
	ns="com.commsen.wedeploy.client", 
	value="service=WeDeployRestClient"
)
public class SimpleRestClient extends AbstractBaseRestClient {

	private int connectTimeout;
	private int readTimeout;

	public SimpleRestClient() {
		this(10 * 1000, 300 * 1000);
	}

	public SimpleRestClient(int connectTimeout, int readTimeout) {
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.commsen.wedeploy.data.rest.WeDeployRestClient#call(java.net.URI,
	 * java.lang.String, java.util.Map, java.lang.String)
	 */
	public WeDeployResponse call(final URI uri, final String method, final Map<String, String> headers,
			final String body) throws IOException {

		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			setMethod(connection, method);

			if (headers != null) {
				for (Entry<String, String> header : headers.entrySet()) {
					connection.setRequestProperty(header.getKey(), header.getValue());
				}
			}
			connection.connect();

			if (body != null) {
				OutputStream outputStream = connection.getOutputStream();
				PrintWriter printWriter = new PrintWriter(outputStream);
				printWriter.write(body);
				printWriter.close();
				outputStream.flush();
			}

			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(connection.getInputStream()).useDelimiter("\\A");
			String responseText = scanner.hasNext() ? scanner.next() : "";
			scanner.close();
			return new WeDeployResponse(connection.getResponseCode(), responseText);

		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	private void setMethod(HttpURLConnection connection, String method) throws ProtocolException {

		if ("PATCH" == method) {
			method = "POST";
			connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
		}

		connection.setRequestMethod(method);
	}
}
