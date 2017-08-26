package com.commsen.wedeploy.client;

import java.util.ServiceLoader;

public class SPI {

	public <T> T wireIfNull(T field, Class<T> type) throws WeDeployClientException {
		return field == null ? wire(type) : field;
	}

	public <T> T wire(Class<T> type) throws WeDeployClientException {
		ServiceLoader<T> serviceLoader = ServiceLoader.load(type);
		if (serviceLoader.iterator().hasNext()) {
			return serviceLoader.iterator().next();
		} else {
			throw new WeDeployClientException("Configuration error! No serivice for " + type + " was found!");
		}
	}

}
