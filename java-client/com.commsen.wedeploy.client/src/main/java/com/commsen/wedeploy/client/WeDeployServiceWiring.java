package com.commsen.wedeploy.client;

import java.util.ServiceLoader;

public class WeDeployServiceWiring<T> {
	
	public T ifMissingLoadViaSPI (T field, Class<T> type) throws WeDeployClientException {

		if (field == null) {
			ServiceLoader<T> serviceLoader = ServiceLoader.load(type);
			if (serviceLoader.iterator().hasNext()) {
				field = serviceLoader.iterator().next();
			} else {
				throw new WeDeployClientException("Configuration error! No serivice for " + type + " was found!");
			}
		}
		
		return field;
	}

}
