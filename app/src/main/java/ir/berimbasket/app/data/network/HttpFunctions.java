package ir.berimbasket.app.data.network;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpFunctions {

    private static final String TAG = HttpFunctions.class.getSimpleName();
    private RequestType requestType;

    public HttpFunctions(RequestType requestType) {
        this.requestType = requestType;
    }

    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            switch (requestType) {
                case GET:
                    reqUrl = reqUrl.replaceAll(" ", "%20");
                    URL url = new URL(reqUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    // read the response
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response = convertStreamToString(in);
                    break;

                case POST:
                    reqUrl = reqUrl.replaceAll(" ", "%20");
                    URL urlPost = new URL(reqUrl);
                    HttpURLConnection connPost = (HttpURLConnection) urlPost.openConnection();
                    connPost.setRequestMethod("GET");
                    InputStream in1 = new BufferedInputStream(connPost.getInputStream());
                    response = convertStreamToString(in1);
                    break;
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public enum RequestType {
        POST,
        GET
    }
}