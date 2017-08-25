package com.commsen.wedeploy.client.data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.commsen.wedeploy.client.WeDeployClientException;

public interface WeDeployDataCollection {

	@SuppressWarnings("rawtypes")
	<T> List<Map> read() throws WeDeployClientException;

	@SuppressWarnings("rawtypes")
	<T> List<Map> read(int limit, int offset) throws WeDeployClientException;

	<T> List<WeDeployDataDocument<T>> read(Class<T> type) throws WeDeployClientException;

	<T> List<WeDeployDataDocument<T>> read(Class<T> type, int limit, int offset) throws WeDeployClientException;

	<T> Optional<WeDeployDataDocument<T>> get(String id, Class<T> type) throws WeDeployClientException;

	boolean exists(String id) throws WeDeployClientException;

	<T> WeDeployDataDocument<T> save(T document) throws WeDeployClientException;

	<T> WeDeployDataDocument<T> save(WeDeployDataDocument<T> document) throws WeDeployClientException;
	
	<T> WeDeployDataBatchOperationResult<T> save(List<T> documents) throws WeDeployClientException;

	void delete(String id) throws WeDeployClientException;

	void deleteAll() throws WeDeployClientException;

	<T> WeDeployDataDocument<T> update(String id, T document) throws WeDeployClientException;

}
