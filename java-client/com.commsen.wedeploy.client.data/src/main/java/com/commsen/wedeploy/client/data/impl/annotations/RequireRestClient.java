package com.commsen.wedeploy.client.data.impl.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import aQute.bnd.annotation.headers.RequireCapability;

@RequireCapability (
	ns="com.commsen.wedeploy.client", 
	filter="(service=WeDeployRestClient)",
	effective="assemble"
)
@Retention(RetentionPolicy.CLASS)
public @interface RequireRestClient {

}
