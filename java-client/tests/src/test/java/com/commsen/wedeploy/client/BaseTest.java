package com.commsen.wedeploy.client;

import java.io.IOException;
import java.util.Properties;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import com.commsen.wedeploy.client.WeDeployClient;
import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.cloud.WeDeployStatusService;
import com.commsen.wedeploy.client.data.WeDeployDataService;
import com.commsen.wedeploy.client.data.WeDeployDataStorage;

public abstract class BaseTest {

	@Rule
	public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

//	protected String dataServiceUrl = "https://data-javaapitest.wedeploy.io/";
	protected WeDeployClient weDeploy = new WeDeployClient();
	protected WeDeployDataService dataService;
	protected WeDeployDataStorage dataStorageWithToken;
	protected WeDeployDataStorage dataStorageWithAutoToken;
	protected WeDeployDataStorage dataStorageNoToken;
	
	protected WeDeployStatusService weDeployStatusService = weDeploy.status();
	
	public BaseTest() throws WeDeployClientException, IOException {
		
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("test.properties"));
		
		String project = properties.getProperty("project");
		String service = properties.getProperty("service");
		String masterToken = properties.getProperty("WEDEPLOY_PROJECT_MASTER_TOKEN");
		
		environmentVariables.set("WEDEPLOY_PROJECT_MASTER_TOKEN", masterToken);

		dataService = weDeploy.data();
		dataStorageNoToken = dataService.connect(project, service, (String)null);
		dataStorageWithToken = dataService.connect(project, service, masterToken);
		dataStorageWithAutoToken = dataService.connect(project, service);
	}

	
	
}