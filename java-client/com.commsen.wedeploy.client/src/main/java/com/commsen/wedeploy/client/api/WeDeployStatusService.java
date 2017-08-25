package com.commsen.wedeploy.client.api;

import com.commsen.wedeploy.client.WeDeployClientException;

public interface WeDeployStatusService {

	WeDeployStatusDTO get() throws WeDeployClientException;

	WeDeployStatusDTO get(boolean verbose) throws WeDeployClientException;
}
