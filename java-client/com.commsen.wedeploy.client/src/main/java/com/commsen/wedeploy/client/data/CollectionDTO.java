package com.commsen.wedeploy.client.data;

import java.util.Map;

public class CollectionDTO {

	public String name;
	
	public int size;
	
	public int documents;

	public Map<String, Object> mappings;

	@Override
	public String toString() {
		return "CollectionDTO [name=" + name + ", size=" + size + ", documents=" + documents + ", mappings=" + mappings
				+ "]";
	}
	
	public boolean isValid () {
		return name != null && !name.trim().isEmpty();
	}

	public CollectionDTO validated () throws WeDeployDataException {
		if (!isValid()) {
			throw new WeDeployDataException("Illegal collection name: " + name);
		}
		return this;
	}
	
	public static CollectionDTO from (String name) throws WeDeployDataException {
		CollectionDTO collectionDTO = new CollectionDTO();
		collectionDTO.name = name;
		return collectionDTO.validated();
	}

	public static CollectionDTO from (String name, Map<String, Object> mappings) throws WeDeployDataException {
		CollectionDTO collectionDTO = new CollectionDTO();
		collectionDTO.name = name;
		collectionDTO.mappings = mappings;
		return collectionDTO.validated();
	}

}
