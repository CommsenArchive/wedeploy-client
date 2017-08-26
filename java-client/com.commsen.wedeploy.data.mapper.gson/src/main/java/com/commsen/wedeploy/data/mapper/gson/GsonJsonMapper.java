package com.commsen.wedeploy.data.mapper.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;

import com.commsen.wedeploy.client.data.WeDeployDataBatchOperationResult;
import com.commsen.wedeploy.client.data.WeDeployDataDocument;
import com.commsen.wedeploy.client.data.WeDeployDataMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import aQute.bnd.annotation.headers.BundleCategory;
import aQute.bnd.annotation.headers.BundleDevelopers;
import aQute.bnd.annotation.headers.BundleDocURL;
import aQute.bnd.annotation.headers.Category;
import aQute.bnd.annotation.licenses.ASL_2_0;

@ASL_2_0
@BundleCategory(value=Category.clients)
@BundleDevelopers(name="Milen Dyankov", value="milendyankov@gmail.com")
@BundleDocURL("https://github.com/azzazzel/wedeploy-client/tree/master/java-client#comcommsenwedeploydatamappergson")
@Component
public class GsonJsonMapper implements WeDeployDataMapper {

	private Gson gson;

	private JsonParser parser = new JsonParser();

	public GsonJsonMapper() {
		this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
	}

	public GsonJsonMapper(Gson gson) {
		this.gson = gson;
	}

	@Override
	public <T> T fromJson(String json, Class<T> type) {
		return gson.fromJson(json, type);
	}

	@Override
	public <T> T fromJson(String json, Type type) {
		return gson.fromJson(json, type);
	}

	@Override
	public <T> List<T> listFromJson(String json, Class<T> type) {
		List<T> list = new ArrayList<T>();

		JsonArray array = parser.parse(json).getAsJsonArray();
		for (JsonElement jsonElement : array) {
			list.add(gson.fromJson(jsonElement, type));
		}
		return list;
	}

	@Override
	public <T> WeDeployDataDocument<T> documentFromJson(String json, Class<T> type) {
		JsonObject jsonObject = parser.parse(json).getAsJsonObject();
		String id = jsonObject.get("id").getAsString();
		T object = gson.fromJson(jsonObject, type);
		return new WeDeployDataDocument<T>(id, object);
	}

	@Override
	public <T> List<WeDeployDataDocument<T>> documentsFromJson(String json, Class<T> type) {

		List<WeDeployDataDocument<T>> list = new ArrayList<WeDeployDataDocument<T>>();

		JsonArray array = parser.parse(json).getAsJsonArray();

		for (JsonElement jsonElement : array) {
			String id = jsonElement.getAsJsonObject().get("id").getAsString();
			T object = gson.fromJson(jsonElement, type);
			list.add(new WeDeployDataDocument<T>(id, object));
		}
		return list;

	}

	public <T> WeDeployDataBatchOperationResult<T> updateIdFromResponse(String json, List<T> documents) {

		if (documents == null) {
			throw new IllegalArgumentException("Illegal documents list :" + documents);
		}

		List<WeDeployDataDocument<T>> succesful = new ArrayList<WeDeployDataDocument<T>>();
		List<T> failed = new ArrayList<T>();

		if (documents.size() > 0) {

			JsonArray array = parser.parse(json).getAsJsonObject().get("results").getAsJsonArray();

			int i = 0;
			for (JsonElement jsonElement : array) {
				boolean successful = jsonElement.getAsJsonObject().get("successful").getAsBoolean();
				T doc = documents.get(i++);
				if (successful) {
					String id = jsonElement.getAsJsonObject().get("document").getAsJsonObject().get("id").getAsString();
					succesful.add(new WeDeployDataDocument<T>(id, doc));
				} else {
					failed.add(doc);
				}
			}
		}

		return new WeDeployDataBatchOperationResult<>(succesful, failed);

	}

	@Override
	public String toJson(Object object) {
		if (object instanceof WeDeployDataDocument) {
			@SuppressWarnings("rawtypes")
			WeDeployDataDocument doc = (WeDeployDataDocument) object;
			if ( doc.getId() != null && !doc.getId().trim().isEmpty()) {
				JsonElement tmp = gson.toJsonTree(doc.getObject());
				tmp.getAsJsonObject().addProperty("id", doc.getId());
				return tmp.toString();
			} else {
				return gson.toJson(doc.getObject());
			}
		} else {
			return gson.toJson(object);
		}
	}

}
