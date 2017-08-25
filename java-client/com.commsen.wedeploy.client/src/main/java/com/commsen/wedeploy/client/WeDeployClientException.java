package com.commsen.wedeploy.client;

public class WeDeployClientException extends Exception {

	private static final long serialVersionUID = -6406940181421911300L;

	public WeDeployClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public WeDeployClientException(String message) {
		super(message);
	}

	public WeDeployClientException(Throwable cause) {
		super(cause);
	}


}
