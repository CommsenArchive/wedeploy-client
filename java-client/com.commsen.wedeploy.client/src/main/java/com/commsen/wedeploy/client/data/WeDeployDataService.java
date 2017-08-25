package com.commsen.wedeploy.client.data;

import java.util.Map;

import com.commsen.wedeploy.client.WeDeployClientException;

public interface WeDeployDataService {
	
	WeDeployDataStorage connect (String project, String service, Map<String, Object> properties) throws WeDeployClientException;

	WeDeployDataStorage connect (String project, String service) throws WeDeployClientException;

	WeDeployDataStorage connect (String project, String service, String token) throws WeDeployClientException;
}
