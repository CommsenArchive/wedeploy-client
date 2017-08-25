package com.commsen.wedeploy.transport.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public interface WeDeployRestClient {

	WeDeployResponse get(URI uri) throws IOException;

	WeDeployResponse get(URI uri, Map<String, String> headers) throws IOException;

	WeDeployResponse get(URI uri, Map<String, String> headers, String body) throws IOException;

	WeDeployResponse post(URI uri, String body) throws IOException;

	WeDeployResponse post(URI uri, Map<String, String> headers, String body) throws IOException;

	WeDeployResponse put(URI uri, String body) throws IOException;

	WeDeployResponse put(URI uri, Map<String, String> headers, String body) throws IOException;

	WeDeployResponse delete(URI uri) throws IOException;

	WeDeployResponse delete(URI uri, Map<String, String> headers) throws IOException;

	WeDeployResponse delete(URI uri, String body) throws IOException;

	WeDeployResponse delete(URI uri, Map<String, String> headers, String body) throws IOException;

	WeDeployResponse call(URI uri, String method, Map<String, String> headers, String body) throws IOException;

}