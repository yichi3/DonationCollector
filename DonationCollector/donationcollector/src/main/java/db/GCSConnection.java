package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.fileupload.FileItem;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GCSConnection {
	private static Storage storage = null;

	static {
		// Authentication
		File credentialsPath = new File(GCSUtil.authKeyPath);
		GoogleCredentials credentials;
		try {
			FileInputStream serviceAccountStream = new FileInputStream(credentialsPath);
			credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
			storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(GCSUtil.projectId).build().getService();
		} catch (IOException e) {
			System.out.println("GCS Authentication Failed");
			e.printStackTrace();
		}
	}

	public static String uploadFile(FileItem file, UUID id) throws IOException {
		// Sanity check
		if (storage == null) {
			System.out.println("GCS Connection Failed");
			return new String();
		}
		
		DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYYMMddHHmmss");
		DateTime dt = DateTime.now(DateTimeZone.UTC);
		String timeString = dt.toString(dtf);
		String itemId = id.toString();
		final String fileName = new StringBuilder(itemId).append(timeString).toString();

		BlobId blobId = BlobId.of(GCSUtil.bucketName, fileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
		storage.create(blobInfo, file.get());

		String imgUrl = new StringBuilder().append(GCSUtil.prefix).append(GCSUtil.bucketName).append("/")
				.append(fileName).toString();

		return imgUrl;
	}
}
