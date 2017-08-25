package com.commsen.wedeploy.client.data;

import java.lang.reflect.Type;
import java.util.List;

public interface WeDeployDataMapper {


	<T> T fromJson (String json, Class<T>  type);

	<T> T fromJson (String json, Type type);

	<T> List<T> listFromJson (String json, Class<T>  type);
	
	<T> WeDeployDataDocument<T> documentFromJson (String json, Class<T>  type);

	<T> List<WeDeployDataDocument<T>> documentsFromJson (String json, Class<T>  type);

	<T> WeDeployDataBatchOperationResult<T> updateIdFromResponse(String json, List<T> documents);
	
	String toJson (Object object);
	
}
