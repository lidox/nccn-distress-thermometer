package com.artursworld.nccn.controller.elasticsearch;

import android.support.annotation.NonNull;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpEntityEnclosingRequestBase;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class ElasticRestClient {

    private static final String BASE_URL = "http://10.0.2.2:9200/"; // Android uses this IP to access localhost

    private static final String CLASS_NAME = ElasticRestClient.class.getSimpleName();
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    /*
    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Content-Type", "application/json");
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    */

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    /**
     * Makes a synchronous post or put request
     *
     * @param httpMethod the HTTP method to choose
     * @param apiString  the REST API to use
     * @param params     the parameters to send
     * @return the response of the request
     */
    private static String postOrPut(METHOD httpMethod, String ES_INDEX, String ES_TYPE, String apiString, Object params) {
        String response = null;
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpEntityEnclosingRequestBase postMethod = getHttpMethodConfiguration(httpMethod, ES_INDEX, ES_TYPE, apiString, params);
            response = httpClient.execute(postMethod, responseHandler);
            Log.i(CLASS_NAME + " response", response);
        } catch (Exception e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            return response;
        }
    }

    /**
     * Post using a JSON Object as param
     *
     * @param apiString
     * @param params
     * @return
     */
    public static String post(String ES_INDEX, String ES_TYPE, String apiString, JSONObject params) {
        return postOrPut(METHOD.POST, ES_INDEX, ES_TYPE, apiString, params);
    }

    /**
     * Post using a String as param
     *
     * @param apiString
     * @param params
     * @return
     */
    public static String post(String ES_INDEX, String ES_TYPE, String apiString, String params) {
        return postOrPut(METHOD.POST, ES_INDEX, ES_TYPE, apiString, params);
    }

    public static String put(String ES_INDEX, String ES_TYPE, String apiString, JSONObject params) {
        return postOrPut(METHOD.PUT, ES_INDEX, ES_TYPE, apiString, params);
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
    private static HttpEntityEnclosingRequestBase getHttpMethodConfiguration(METHOD httpMethod, String ES_INDEX, String ES_TYPE, String apiString, Object params) throws UnsupportedEncodingException {
        HttpEntityEnclosingRequestBase method = null;

        if (httpMethod == METHOD.POST)
            method = new HttpPost(getAbsoluteUrl(ES_INDEX + "/" + ES_TYPE + "/" + apiString));

        else if (httpMethod == METHOD.PUT)
            method = new HttpPut(getAbsoluteUrl(ES_INDEX + "/" + ES_TYPE + "/" + apiString));

        method.setHeader("Content-Type", "application/json");
        method.setEntity(new ByteArrayEntity(params.toString().getBytes("UTF8")));
        return method;
    }

    //TODO: refactor
    public void getHttpRequest(String url) {
        try {
            ElasticRestClient.get(url, null, new JsonHttpResponseHandler() { // instead of 'get' use twitter/tweet/1
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.i(CLASS_NAME, "onSuccess: " + response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i(CLASS_NAME, "onSuccess: " + response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e(CLASS_NAME, "onFailure");
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                }

                @Override
                public void onRetry(int retryNo) {
                    Log.i(CLASS_NAME, "onRetry " + retryNo);
                    // called when request is retried
                }
            });
        } catch (Exception e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }
    }
}
