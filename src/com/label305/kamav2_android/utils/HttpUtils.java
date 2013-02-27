package com.label305.kamav2_android.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.label305.kamav2_android.exceptions.KamaException;

public class HttpUtils {
	/**
	 * Checks if we have a valid Internet Connection on the device.
	 * 
	 * @param ctx
	 * @return True if device has internet
	 * 
	 *         Code from: http://www.androidsnippets.org/snippets/131/
	 */
	public static final boolean haveInternet(Context cxt) {

		NetworkInfo info = (NetworkInfo) ((ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to
			// disable internet while roaming, just return false
			return true;
		}
		return true;
	}

	public static final boolean isGZip(HttpResponse response) {
		if (response == null)
			return false;

		Header contentEncoding = response.getFirstHeader("Content-Encoding");

		return contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip") ? true : false;
	}

	public static final byte[] compress(String string) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());
		GZIPOutputStream gos = new GZIPOutputStream(os);
		gos.write(string.getBytes());
		gos.close();
		byte[] compressed = os.toByteArray();
		os.close();
		return compressed;
	}

	public static final String decompress(byte[] compressed) throws IOException {
		final int BUFFER_SIZE = 32;
		ByteArrayInputStream is = new ByteArrayInputStream(compressed);
		GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
		StringBuilder string = new StringBuilder();
		byte[] data = new byte[BUFFER_SIZE];
		int bytesRead;
		while ((bytesRead = gis.read(data)) != -1) {
			string.append(new String(data, 0, bytesRead));
		}
		gis.close();
		is.close();
		return string.toString();
	}

	String getStringFromResponse(HttpResponse response) throws KamaException {
		String responseString = "";

		try {
			if (response != null) {
				if (isGZip(response))
					responseString = decompress(EntityUtils.toByteArray(response.getEntity()));
				else
					responseString = EntityUtils.toString(response.getEntity());
			}
		} catch(Exception e) {
			throw new KamaException(e);
		}

		return responseString;
	}
}
