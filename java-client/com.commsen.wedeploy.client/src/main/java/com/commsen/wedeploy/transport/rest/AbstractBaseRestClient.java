package com.commsen.wedeploy.transport.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

public abstract class AbstractBaseRestClient implements WeDeployRestClient {

	public AbstractBaseRestClient() {
		super();
	}

	public WeDeployResponse get(final URI uri) throws IOException {
		return call(uri, "GET", Collections.emptyMap(), null);
	}

	public WeDeployResponse get(final URI uri, final Map<String, String> headers) throws IOException {
		return call(uri, "GET", headers, null);
	}

	public WeDeployResponse get(final URI uri, final Map<String, String> headers, final String body) throws IOException {
		return call(uri, "GET", headers, body);
	}

	public WeDeployResponse post(final URI uri, final String body) throws IOException {
		return call(uri, "POST", Collections.emptyMap(), body);
	}

	public WeDeployResponse post(final URI uri, final Map<String, String> headers, final String body) throws IOException {
		return call(uri, "POST", headers, body);
	}

	public WeDeployResponse put(final URI uri, final String body) throws IOException {
		return call(uri, "PUT", Collections.emptyMap(), body);
	}

	public WeDeployResponse put(final URI uri, final Map<String, String> headers, final String body) throws IOException {
		return call(uri, "PUT", headers, body);
	}

	public WeDeployResponse delete(final URI uri) throws IOException {
		return call(uri, "DELETE", Collections.emptyMap(), null);
	}

	public WeDeployResponse delete(final URI uri, Map<String, String> headers) throws IOException {
		return call(uri, "DELETE", headers, null);
	}
	
	public WeDeployResponse delete(final URI uri, final String body) throws IOException {
		return call(uri, "DELETE", Collections.emptyMap(), body);
	}

	public WeDeployResponse delete(final URI uri, final Map<String, String> headers, final String body) throws IOException {
		return call(uri, "DELETE", headers, body);
	}

}