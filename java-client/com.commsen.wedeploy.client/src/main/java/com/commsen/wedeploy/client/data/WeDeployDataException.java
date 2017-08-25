package com.commsen.wedeploy.client.data;

import com.commsen.wedeploy.client.WeDeployClientException;

public class WeDeployDataException extends WeDeployClientException {

	private static final long serialVersionUID = -4344819057729488832L;
	
	public WeDeployDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public WeDeployDataException(String message) {
		super(message);
	}

	public WeDeployDataException(Throwable cause) {
		super(cause);
	}

}
