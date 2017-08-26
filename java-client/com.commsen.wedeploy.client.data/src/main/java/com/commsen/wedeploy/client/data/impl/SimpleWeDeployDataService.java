package com.commsen.wedeploy.client.data.impl;

import static com.commsen.wedeploy.client.WeDeployConstants.MASTER_TOKEN;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.WeDeployServiceWiring;
import com.commsen.wedeploy.client.data.WeDeployDataMapper;
import com.commsen.wedeploy.client.data.WeDeployDataService;
import com.commsen.wedeploy.client.data.WeDeployDataStorage;
import com.commsen.wedeploy.transport.rest.WeDeployRestClient;

import aQute.bnd.annotation.headers.BundleCategory;
import aQute.bnd.annotation.headers.BundleDevelopers;
import aQute.bnd.annotation.headers.BundleDocURL;
import aQute.bnd.annotation.headers.Category;
import aQute.bnd.annotation.licenses.ASL_2_0;

@ASL_2_0
@BundleCategory(value=Category.clients)
@BundleDevelopers(name="Milen Dyankov", value="milendyankov@gmail.com")
@BundleDocURL("https://github.com/azzazzel/wedeploy-client/tree/master/java-client#comcommsenwedeployclientdata")
@Component
public class SimpleWeDeployDataService implements WeDeployDataService {

	@Reference (
			cardinality = ReferenceCardinality.MANDATORY,
			policy = ReferencePolicy.DYNAMIC,
			policyOption = ReferencePolicyOption.GREEDY
			)
	private volatile WeDeployRestClient restClient;

	@Reference(
			cardinality = ReferenceCardinality.MANDATORY, 
			policy = ReferencePolicy.DYNAMIC, 
			policyOption = ReferencePolicyOption.GREEDY)
	private volatile WeDeployDataMapper dataMapper;
	
	private WeDeployServiceWiring<WeDeployRestClient> wireRest = new WeDeployServiceWiring<WeDeployRestClient>();
	private WeDeployServiceWiring<WeDeployDataMapper> wireDataMapper = new WeDeployServiceWiring<WeDeployDataMapper>();
	
	@Override
	public WeDeployDataStorage connect(String project, String service, Map<String, Object> properties) throws WeDeployClientException {
		restClient = wireRest.ifMissingLoadViaSPI(restClient, WeDeployRestClient.class);
		dataMapper = wireDataMapper.ifMissingLoadViaSPI(dataMapper, WeDeployDataMapper.class);

		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		
		String masterToken = System.getenv("WEDEPLOY_PROJECT_MASTER_TOKEN");
		if (masterToken != null && !masterToken.trim().isEmpty() && !properties.containsKey(MASTER_TOKEN)) {
			properties.put(MASTER_TOKEN, masterToken);
		}
		return new SimpleWeDeployDataStorage(project, service, properties, restClient, dataMapper);
	}

	@Override
	public WeDeployDataStorage connect(String project, String service) throws WeDeployClientException {
		return connect(project, service, (Map<String, Object>)null);
	}

	public WeDeployDataStorage connect(String project, String service, String token) throws WeDeployClientException {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(MASTER_TOKEN, token);
		return connect(project, service, properties);
	}


	
}
