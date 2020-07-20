package db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.fileupload.FileItem;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GCSConnection {
	private static Storage storage = null;
	
	static {
		storage = StorageOptions.newBuilder().setProjectId(GCSUtil.projectId).build().getService();
	}
	
	public static String uploadFile(FileItem file, UUID id) throws IOException {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYYMMddHHmmss");
		DateTime dt = DateTime.now(DateTimeZone.UTC);
		String timeString = dt.toString(dtf);
		String itemId = id.toString();
		final String fileName = new StringBuilder(itemId).append(timeString).toString();
		
		BlobId blobId = BlobId.of(GCSUtil.bucketName, fileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
		storage.create(blobInfo, file.get());
		// should we return download link?
		
		String imgUrl = new StringBuilder()
				.append(GCSUtil.prefix)
				.append(GCSUtil.bucketName)
				.append("/")
				.append(fileName)
				.toString();
		
		return imgUrl;
	}
}
