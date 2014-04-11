package com.thekha.vendor.util;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

public class UploadImage {

	// TODO - Update upload service path and upload_folder.
	private static final String service = "http://www.gotechy.in/demo/thekha/upload.php";

	public static final String upload_folder = "http://www.gotechy.in/demo/thekha/images/";

	public static boolean upload(String filePath) throws ClientProtocolException, IOException {

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost(service);
		File file = new File(filePath);
		MultipartEntity mpEntity = new MultipartEntity();
		ContentBody contentFile = new FileBody(file);
		mpEntity.addPart("userfile", contentFile);
		httppost.setEntity(mpEntity);
		HttpResponse response = httpclient.execute(httppost);
		httpclient.getConnectionManager().shutdown();
		if((response.getStatusLine().toString()).equals("HTTP/1.1 200 OK")){
			return true;
		}else{
			return false;
		}
	}
}
