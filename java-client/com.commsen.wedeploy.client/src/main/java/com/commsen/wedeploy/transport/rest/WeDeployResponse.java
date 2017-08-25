package com.commsen.wedeploy.transport.rest;

import com.commsen.wedeploy.client.WeDeployClientException;

public class WeDeployResponse {

	private String body;

	private int code;

	public WeDeployResponse(int code, String body) {
		super();
		this.code = code;
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public int getCode() {
		return code;
	}
	
	public boolean is200() {
		return code >=200 && code < 300;
	}

	public boolean hasContent() {
		return !body.trim().isEmpty();
	}

	public boolean hasResponseBody() {
		return is200() && hasContent();
	}

	public boolean hasErrorBody() {
		return !is200() && hasContent();
	}
	
	public String getOKBody () throws WeDeployClientException {
		checkOK();
		return body;
	}

	public void checkOK () throws WeDeployClientException {
		if (!is200()) {
			throw new WeDeployClientException("Error response received: " + this);
		}
	}

	@Override
	public String toString() {
		return "WeDeployResponse [code=" + code + ", body=" + body + "]";
	}
}

