package com.group3.androidthings.Network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class StringRequest extends Request<String> {
    /**
     * Charset for request.
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */

    private Map<String, String> headers;
    private Response.Listener<String> listener;
    private Map<String, String> params;
    private String mJsonRequestBody = null;

    public StringRequest(int method, String url, String requestBody, Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        mJsonRequestBody = requestBody;
    }

    public StringRequest(int method, String url, Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
//        return addCustomRequestHeader(headers != null ? headers : super.getHeaders());
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return Response.success(new String(response.data), null);
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }


    @Override
    public String getBodyContentType() {
//        if (mJsonRequestBody != null)
//            return PROTOCOL_JSON_CONTENT_TYPE;

        return super.getBodyContentType();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mJsonRequestBody != null) {
            try {
                return mJsonRequestBody.getBytes(PROTOCOL_CHARSET);
            } catch (UnsupportedEncodingException uee) {
                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                        mJsonRequestBody, PROTOCOL_CHARSET);
                return null;
            }
        }

        return super.getBody();
    }
}
