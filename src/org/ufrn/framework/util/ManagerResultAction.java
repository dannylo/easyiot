package org.ufrn.framework.util;

import java.util.HashMap;
import com.google.gson.Gson;
public class ManagerResultAction {

	public static HashMap<String, String> getValue(String jsonResult) {
		Gson gson = new Gson();
		HashMap<String, String> map = gson.fromJson(jsonResult, HashMap.class);
		
		return map;					
	}
}
