package rpc;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

public class RpcHelper {
	// Writes a JSONArray to httpresponse.
	public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException{
		response.setContentType("application/json");
		response.getWriter().print(array);

	}
	
}