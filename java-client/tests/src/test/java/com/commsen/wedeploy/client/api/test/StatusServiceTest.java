package com.commsen.wedeploy.client.api.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.commsen.wedeploy.client.BaseTest;
import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.cloud.WeDeployStatusDTO;

public class StatusServiceTest extends BaseTest {

	public StatusServiceTest() throws WeDeployClientException, IOException {
		super();
	}

	@Test
	public void test() throws WeDeployClientException {
		WeDeployStatusDTO result = weDeployStatusService.get();
		System.out.println(result);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.status);
		
		result = weDeployStatusService.get(true);
		System.out.println(result);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.data);
		Assert.assertNotNull(result.auth);
		Assert.assertNotNull(result.email);
		Assert.assertNotNull(result.conqueror);
		Assert.assertNotNull(result.version);
		
		Assert.assertEquals("wedeploy.com", result.domains.get("infrastructure"));
		Assert.assertEquals("wedeploy.io", result.domains.get("service"));
	}

}
