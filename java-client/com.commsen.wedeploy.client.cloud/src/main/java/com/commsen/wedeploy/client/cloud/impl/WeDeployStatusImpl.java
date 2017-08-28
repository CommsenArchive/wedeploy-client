package com.commsen.wedeploy.client.api.impl;

import static com.commsen.wedeploy.client.WeDeployConstants.API_URI;

import java.io.IOException;
import java.net.URI;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.SPI;
import com.commsen.wedeploy.client.api.WeDeployStatusDTO;
import com.commsen.wedeploy.client.api.WeDeployStatusService;
import com.commsen.wedeploy.client.data.WeDeployDataException;
import com.commsen.wedeploy.client.data.WeDeployDataMapper;
import com.commsen.wedeploy.transport.rest.WeDeployResponse;
import com.commsen.wedeploy.transport.rest.WeDeployRestClient;

import aQute.bnd.annotation.headers.BundleCategory;
import aQute.bnd.annotation.headers.BundleDevelopers;
import aQute.bnd.annotation.headers.BundleDocURL;
import aQute.bnd.annotation.headers.Category;
import aQute.bnd.annotation.licenses.ASL_2_0;

@ASL_2_0
@BundleCategory(value= Category.clients)
@BundleDevelopers(name="Milen Dyankov", value="milendyankov@gmail.com")
@BundleDocURL("https://github.com/azzazzel/wedeploy-client/tree/master/java-client#comcommsenwedeployclientapi")
@Component
public class WeDeployStatusImpl implements WeDeployStatusService {

	@Reference(
			cardinality = ReferenceCardinality.MANDATORY, 
			policy = ReferencePolicy.DYNAMIC, 
			policyOption = ReferencePolicyOption.GREEDY)
	private volatile WeDeployRestClient restClient;

	@Reference(
			cardinality = ReferenceCardinality.MANDATORY, 
			policy = ReferencePolicy.DYNAMIC, 
			policyOption = ReferencePolicyOption.GREEDY)
	private volatile WeDeployDataMapper dataMapper;

	private SPI spi = new SPI();
	

	@Override
	public WeDeployStatusDTO get() throws WeDeployClientException {
		return get(false);
	}

	
	@Override
	public WeDeployStatusDTO get(boolean verbose) throws WeDeployClientException {

		restClient = spi.wireIfNull(restClient, WeDeployRestClient.class);
		dataMapper = spi.wireIfNull(dataMapper, WeDeployDataMapper.class);
		
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
