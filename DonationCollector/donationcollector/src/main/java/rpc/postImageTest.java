package rpc;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException; 
import org.apache.commons.fileupload.disk.DiskFileItemFactory; 
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject; 


/**
 * Servlet implementation class postImageTest
 */
public class postImageTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public postImageTest() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("rawtypes")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			final FileItemFactory fileItemFactory = new DiskFileItemFactory();
			final ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
			
			try {
				final List items = uploadHandler.parseRequest(request);
				
				for (Iterator it = items.iterator(); it.hasNext();) {
					final FileItem item = (FileItem) it.next();
					
					if (!item.isFormField()) {
						System.out.println("File Uploaded");
						System.out.println(item.getContentType());
					} else {
						JSONObject obj = new JSONObject(item.getString());
						RpcHelper.writeJsonObject(response, obj);
					}
				}
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
