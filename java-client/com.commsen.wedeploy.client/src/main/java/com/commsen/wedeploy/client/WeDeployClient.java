package com.commsen.wedeploy.client;

import com.commsen.wedeploy.client.api.WeDeployStatusService;
import com.commsen.wedeploy.client.data.WeDeployDataService;


public class WeDeployClient {

	WeDeployServiceWiring<WeDeployDataService> wireDataService = new WeDeployServiceWiring<WeDeployDataService>();

	WeDeployServiceWiring<WeDeployStatusService> wireStatusService = new WeDeployServiceWiring<WeDeployStatusService>();

	public WeDeployDataService data() throws WeDeployClientException {
		
		return wireDataService.ifMissingLoadViaSPI(null, WeDeployDataService.class);
	}

	public WeDeployStatusService status() throws WeDeployClientException {
		
		return wireStatusService.ifMissingLoadViaSPI(null, WeDeployStatusService.class);
		
	}

}
