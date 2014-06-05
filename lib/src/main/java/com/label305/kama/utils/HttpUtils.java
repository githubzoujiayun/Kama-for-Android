package com.label305.kama.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.label305.kama.exceptions.JsonKamaException;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("UnusedDeclaration")
public class HttpUtils {

    public static final int HTTP_OK_LOWER = 200;
    public static final int HTTP_OK_UPPER = 299;
    public static final int HTTP_REDIRECT = 302;

    private static final String GZIP = "gzip";
    private static final String CONTENT_ENCODING = "Content-Encoding";

    private HttpUtils() {
    }

    /**
     * Checks if we have a valid Internet Connection on the device.
     *
     * @return true if device has internet
     */
    public static boolean isInternetAvailable(final Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return !(info == null || !info.isConnected());
    }

    public static boolean isSuccessFul(final int statusCode) {
        return statusCode >= HTTP_OK_LOWER && statusCode <= HTTP_OK_UPPER;
    }

    public static boolean isRedirect(final int statusCode) {
        return statusCode == HTTP_REDIRECT;
    }

    public static boolean isGZip(final HttpMessage response) {
        boolean result = false;
        if (response != null) {

            Header contentEncoding = response.getFirstHeader(CONTENT_ENCODING);
            result = contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase(GZIP);
        }
        return result;
    }

    public static byte[] compress(final String s) throws IOException {
        byte[] result;

        ByteArrayOutputStream os = new ByteArrayOutputStream(s.length());
        GZIPOutputStream gos = new GZIPOutputStream(os);
        try {
            gos.write(s.getBytes());
            result = os.toByteArray();
        } finally {
            if (gos != null) {
                gos.close();
            }

            if (os != null) {
                os.close();
            }
        }
        return result;
    }

    private static String decompress(final byte[] compressed) throws IOException {
        final int bufferSize = 32;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        StringBuilder stringBuilder = new StringBuilder();
        byte[] data = new byte[bufferSize];

        GZIPInputStream gis = new GZIPInputStream(is, bufferSize);
        try {
            int bytesRead;
            while ((bytesRead = gis.read(data)) != -1) {
                stringBuilder.append(new String(data, 0, bytesRead));
            }
        } finally {
            if (gis != null) {
                gis.close();
            }

            if (is != null) {
                is.close();
            }
        }
        return stringBuilder.toString();
    }

    public static String getStringFromResponse(final HttpResponse response) throws JsonKamaException {
        String responseString = "";

        try {
            if (response != null) {
                if (isGZip(response)) {
                    responseString = decompress(EntityUtils.toByteArray(response.getEntity()));
                } else {
                    responseString = EntityUtils.toString(response.getEntity());
                }
            }
        } catch (IOException e) {
            throw new JsonKamaException(e);
        } catch (ParseException e) {
            throw new JsonKamaException(e);
        }
        return responseString;
    }
}
