package com.commsen.wedeploy.client.data.impl;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.data.WeDeployDataBatchOperationResult;
import com.commsen.wedeploy.client.data.WeDeployDataCollection;
import com.commsen.wedeploy.client.data.WeDeployDataDocument;
import com.commsen.wedeploy.client.data.WeDeployDataException;
import com.commsen.wedeploy.client.data.WeDeployDataMapper;
import com.commsen.wedeploy.transport.rest.WeDeployResponse;
import com.commsen.wedeploy.transport.rest.WeDeployRestClient;

public class SimpleWeDeployDataCollection implements WeDeployDataCollection {

	Logger log = LoggerFactory.getLogger(SimpleWeDeployDataCollection.class);

	private URI baseURI;

	private String name;

	private SimpleWeDeployDataStorage dataStorage;
	
	private WeDeployRestClient restClient;

	private WeDeployDataMapper jsonMapper;
	
	
	SimpleWeDeployDataCollection(SimpleWeDeployDataStorage dataStorage, String name) {

		this.dataStorage = dataStorage;
		this.restClient = dataStorage.restClient;
		this.jsonMapper = dataStorage.jsonMapper;
		this.name = name;
		this.baseURI = dataStorage.baseURI;

	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> read() throws WeDeployClientException {
		return read(0, 0);
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> read(int limit, int offset) throws WeDeployClientException {
		return jsonMapper.listFromJson(getAllAsJson(limit, offset), Map.class);
	}

	public <T> List<WeDeployDataDocument<T>> read(Class<T> type) throws WeDeployClientException {
		return read(type, 0, 0);
	}

	public <T> List<WeDeployDataDocument<T>> read(Class<T> type, int limit, int offset) throws WeDeployClientException {
		return jsonMapper.documentsFromJson(getAllAsJson(limit, offset), type);
	}

	@Override
	public <T> Optional<WeDeployDataDocument<T>> get(String id, Class<T> type) throws WeDeployClientException {
		log.debug("Getting documents with id " + id);

		WeDeployResponse response;
		try {
			response = restClient.get(idInPathURI(id), dataStorage.baseHeaders);
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to get collections!", e);
		}

		log.debug("Response: " + response);

		if (404 == response.getCode()) {
			return Optional.empty();
		}

		return Optional.of(jsonMapper.documentFromJson(response.getOKBody(), type));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> WeDeployDataDocument<T> save(T document) throws WeDeployClientException {
		if (document == null) {
			throw new IllegalArgumentException("Illegal document :" + document);
		}

		log.debug("Saving document " + document + " in " + name);

		WeDeployResponse response;
		try {
			response = restClient.post(collectionInPathURI(), dataStorage.baseHeaders, jsonMapper.toJson(document));
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to connect to collection!", e);
		}

		log.debug("Response: " + response);

		return (WeDeployDataDocument<T>) jsonMapper.documentFromJson(response.getOKBody(), document.getClass());
	}

	public <T> WeDeployDataDocument<T> save(WeDeployDataDocument<T> document) throws WeDeployClientException {
		if (document == null) {
			throw new IllegalArgumentException("Illegal document :" + document);
		}

		log.debug("Saving document " + document.getObject() + " in " + name);

		WeDeployResponse response;
		try {
			response = restClient.post(collectionInPathURI(), dataStorage.baseHeaders, jsonMapper.toJson(document));
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to connect to collection!", e);
		}

		log.debug("Response: " + response);

		@SuppressWarnings("unchecked")
		WeDeployDataDocument<T> documentFromJson = (WeDeployDataDocument<T>) jsonMapper.documentFromJson(response.getOKBody(), document.getClass());
		
		return documentFromJson;
	}

	public <T> WeDeployDataBatchOperationResult<T> save(List<T> documents) throws WeDeployClientException {
		if (documents == null || documents.isEmpty()) {
			log.warn("Ingnored request to save an empty list of documents!");
			return WeDeployDataBatchOperationResult.emptyResult();
		}

		log.debug("Saving  " + documents.size() + " documents in " + name);

		WeDeployResponse response;
		try {
			response = restClient.post(collectionInPathURI(), dataStorage.baseHeaders, jsonMapper.toJson(documents));
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to get collections!", e);
		}

		log.debug("Response: " + response);

		return jsonMapper.updateIdFromResponse(response.getOKBody(), documents);

	}

	@Override
	public boolean exists(String id) throws WeDeployClientException {
		return get(id, Map.class).isPresent();
	}

	@Override
	public void delete(String id) throws WeDeployClientException {
		log.debug("Deleting document " + id + "form " + name);

		WeDeployResponse response;
		try {
			response = restClient.delete(idInPathURI(id), dataStorage.baseHeaders);
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to get collections!", e);
		}
		log.debug("Response: " + response);
		response.checkOK();
	}

	public void deleteAll() throws WeDeployClientException {
		log.debug("Deleting all documents in " + name);

		
		WeDeployResponse response;
		try {
			response = restClient.delete(collectionInPathURI(), dataStorage.baseHeaders);
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to get collections!", e);
		}
		log.debug("Response: " + response);
		response.checkOK();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> WeDeployDataDocument<T> update(String id, T document) throws WeDeployClientException {
		log.debug("Updating document " + id + " in " + name);

		WeDeployResponse response;
		try {
			response = restClient.put(idInPathURI(id), dataStorage.baseHeaders, jsonMapper.toJson(document));
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to get collections!", e);
		}

		log.debug("Response: " + response);
		response.checkOK();

		return get(id, (Class<T>) document.getClass()).get();
	}

	private String getAllAsJson(int limit, int offeset) throws WeDeployClientException {
		log.debug("Getting all documents in " + name);

		StringBuilder params = new StringBuilder();

		if (limit > 0) {
			params.append(params.length() > 0 ? "&" : "?");
			params.append("limit=").append(limit);
		}

		if (offeset > 0) {
			params.append(params.length() > 0 ? "&" : "?");
			params.append("offset=").append(offeset);
		}

		WeDeployResponse response;
		try {
			response = restClient.get(URI.create(collectionInPathURI() + params.toString()), dataStorage.baseHeaders);
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to get collections!", e);
		}

		log.debug("Response: " + response);

		return response.getOKBody();
	}

	private URI collectionInPathURI () {
		return URI.create(baseURI + "/" + name);
	}

	private URI idInPathURI (String id) {
		return URI.create(baseURI + "/" + name + "/" + id);
	}
}
