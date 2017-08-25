package com.commsen.wedeploy.client.api;

import static com.commsen.wedeploy.client.WeDeployConstants.API_URI;

import java.io.IOException;
import java.net.URI;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.WeDeployServiceWiring;
import com.commsen.wedeploy.client.data.WeDeployDataException;
import com.commsen.wedeploy.client.data.WeDeployDataMapper;
import com.commsen.wedeploy.mapper.gson.GsonJsonMapper;
import com.commsen.wedeploy.transport.rest.WeDeployResponse;
import com.commsen.wedeploy.transport.rest.WeDeployRestClient;

@Component
public class WeDeployStatusImpl implements WeDeployStatusService {

	@Reference(
			cardinality = ReferenceCardinality.MANDATORY, 
			policy = ReferencePolicy.DYNAMIC, 
			policyOption = ReferencePolicyOption.GREEDY)
	private volatile WeDeployRestClient restClient;

	private volatile WeDeployDataMapper dataMapper = new GsonJsonMapper();

	private WeDeployServiceWiring<WeDeployRestClient> wiring = new WeDeployServiceWiring<WeDeployRestClient>();
	

	@Override
	public WeDeployStatusDTO get() throws WeDeployClientException {
		return get(false);
	}

	
	@Override
	public WeDeployStatusDTO get(boolean verbose) throws WeDeployClientException {

		restClient = wiring.ifMissingLoadViaSPI(restClient, WeDeployRestClient.class);
		
		WeDeployResponse response;
		URI uri = API_URI;
		if (verbose) {
			uri = URI.create(API_URI.toString() + "?options=verbose");
		}
		try {
			response = restClient.get(uri);
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to get status ", e);
		}

		response.checkOK();
		
		return dataMapper.fromJson(response.getOKBody(), WeDeployStatusDTO.class);
	}

}
