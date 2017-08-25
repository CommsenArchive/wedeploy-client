package com.commsen.wedeploy.client.data;

import java.util.Collections;
import java.util.List;

public class WeDeployDataBatchOperationResult<T> {

	private List<WeDeployDataDocument<T>> succesful; 

	private List<T> failed;
	
	private boolean hasFailures;

	public WeDeployDataBatchOperationResult(List<WeDeployDataDocument<T>> succesful,
			List<T> failed) {
		super();
		this.succesful = succesful;
		this.failed = failed;
		this.hasFailures = failed != null && failed.size() > 0;
	}

	public static final <T> WeDeployDataBatchOperationResult<T> emptyResult() {
		return new WeDeployDataBatchOperationResult<>(Collections.emptyList(), Collections.emptyList());
	}

	public List<WeDeployDataDocument<T>> getSuccesful() {
		return Collections.unmodifiableList(succesful) ;
	}

	public List<T> getFailed() {
		return Collections.unmodifiableList(failed) ;
	}

	public boolean hasFailures() {
		return hasFailures;
	}
	

	
}
