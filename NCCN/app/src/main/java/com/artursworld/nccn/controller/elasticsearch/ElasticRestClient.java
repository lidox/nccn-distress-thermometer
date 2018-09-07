package com.artursworld.nccn.controller.elasticsearch;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.controller.util.Strings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.UsernamePasswordCredentials;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpEntityEnclosingRequestBase;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.auth.BasicScheme;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class ElasticRestClient {

    // private static final String BASE_URL = "http://10.0.2.2:9200/"; // Android uses this IP to access localhost

    private static final String CLASS_NAME = ElasticRestClient.class.getSimpleName();

    public static String get(String ES_INDEX, String ES_TYPE, String apiString, Object params) {
        return doHttpRequest(METHOD.GET, ES_INDEX, ES_TYPE, apiString, params);
    }

    @NonNull
    private static String getBaseUrl() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        // protocol
        String defaultProtocolValue = "http://";
        String protocolValue = Strings.getStringByRId(R.string.c_select_protocol);
        String protocol = sp.getString(protocolValue, defaultProtocolValue);
        // server ip
        String defaultServerIPValue = Strings.getStringByRId(R.string.c_server_ip_elastic_search_default_value);
        String serverIPValue = Strings.getStringByRId(R.string.c_select_server_ip);
        String SERVER_IP = sp.getString(serverIPValue, defaultServerIPValue);
        // port
        String defaultPortValue = 9200 + "";
        String portValue = Strings.getStringByRId(R.string.c_select_server_port);
        String ES_PORT = sp.getString(portValue, defaultPortValue);

        String BASE_URL = protocol + SERVER_IP + ":" + ES_PORT + "/";
        Log.i(CLASS_NAME, "BaseUrl:" + BASE_URL);
        return BASE_URL;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return getBaseUrl() + relativeUrl;
    }

    /**
     * Makes a synchronous post or put request
     *
     * @param httpMethod the HTTP method to choose
     * @param apiString  the REST API to use
     * @param params     the parameters to send
     * @return the response of the request
     */
    private static String doHttpRequest(METHOD httpMethod, String ES_INDEX, String ES_TYPE, String apiString, Object params) {
        String response = null;
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpEntityEnclosingRequestBase postMethod = getHttpMethodConfiguration(httpMethod, ES_INDEX, ES_TYPE, apiString, params);
            postMethod = addBasicAuth(postMethod);

            response = httpClient.execute(postMethod, responseHandler);
            Log.i(CLASS_NAME + " response", response);
        } catch (Exception e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
            response = e.getLocalizedMessage();
        } finally {
            return response;
        }
    }

    private static HttpEntityEnclosingRequestBase addBasicAuth(HttpEntityEnclosingRequestBase postMethod) {
        try {
            String userName = getSharedPreferenceById(R.string.c_elastic_search_username);
            String password = getSharedPreferenceById(R.string.c_elastic_search_password);
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
            Header basicAuthHeader = new BasicScheme(Charset.forName("UTF-8")).authenticate(credentials, postMethod, null);
            postMethod.addHeader(basicAuthHeader);
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Could not add basic authentication! " + e.getLocalizedMessage());
        } finally {
            return postMethod;
        }
    }

    private static String getSharedPreferenceById(int stringId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String defaultValue = Strings.getStringByRId(R.string.placeholder);
        String value = Strings.getStringByRId(stringId);
        String ret = sp.getString(value, defaultValue);
        return ret;
    }

    /**
     * Post using a String as param
     *
     * @param apiString
     * @param params
     * @return
     */
    public static String post(String ES_INDEX, String ES_TYPE, String apiString, String params) {
        return doHttpRequest(METHOD.POST, ES_INDEX, ES_TYPE, apiString, params);
    }

    /**
     * Configuration for a POST request
     *
     * @param apiString
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    @NonNull
    private static HttpEntityEnclosingRequestBase getHttpMethodConfiguration(METHOD httpMethod, String ES_INDEX, String ES_TYPE, String apiString, Object params) {
        HttpEntityEnclosingRequestBase method = null;
        try {
            if (httpMethod == METHOD.POST)
                method = new HttpPost(getAbsoluteUrl(ES_INDEX + "/" + ES_TYPE + "/" + apiString));

            else if (httpMethod == METHOD.PUT)
                method = new HttpPut(getAbsoluteUrl(ES_INDEX + "/" + ES_TYPE + "/" + apiString));

            else if (httpMethod == METHOD.GET) {
                method = new HttpGet(getAbsoluteUrl(ES_INDEX + "/" + ES_TYPE + "/" + apiString));
            }

            method.setHeader("Content-Type", "application/json");
            if (params != null) {
                method.setEntity(new ByteArrayEntity(params.toString().getBytes("UTF8")));
            }

        } catch (Exception e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }
        return method;
    }
}
