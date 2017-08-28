package com.commsen.wedeploy.client.cloud;

import java.util.Map;

public class WeDeployStatusDTO {

	public String status;
	public String auth;
	public String data;
	public String email;
	public String conqueror;
	public String version;
	public Map<String, String> domains;

	@Override
	public String toString() {
		if (status == null) {
			return "WeDeployStatusDTO [auth=" + auth + ", data=" + data + ", email=" + email + ", conqueror="
					+ conqueror + ", version=" + version + ", domains=" + domains + "]";
		} else {
			return "WeDeployStatusDTO [status=" + status + ", version=" + version + "]";
		}
	}

}