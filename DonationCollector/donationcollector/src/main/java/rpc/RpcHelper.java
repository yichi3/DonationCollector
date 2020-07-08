package rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class RpcHelper {
	// Read JSONArray from request
	public static JSONArray readJSONArray(HttpServletRequest request) throws IOException {
		BufferedReader reader = new BufferedReader(request.getReader());
		StringBuilder requestBody = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
		}
		return new JSONArray(requestBody.toString());
	}

	// Read JSONObject from request
	public static JSONObject readJSONObject(HttpServletRequest request) throws IOException {
		BufferedReader reader = new BufferedReader(request.getReader());
		StringBuilder requestBody = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
		}
		return new JSONObject(requestBody.toString());
	}

	// Writes a JSONArray to http response.
	public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print(array);
	}

	// Writes a JSONObject to http response.
	public static void writeJsonObject(HttpServletResponse response, JSONObject obj) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print(obj);
	}
	
	public static List<String> JSONArrayToList(JSONArray arr) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < arr.length(); i++) {
			list.add(arr.getString(i));
		}
		return list;
	}
}

