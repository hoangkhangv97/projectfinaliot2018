package com.group3.androidthings.Network;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Le Trong Nhan on 08/06/2018.
 */

public interface RemoteApiClient {
    void getJSON (String requestUrl, Map<String, String> header, ApiResponseListener<JSONObject> apiResponseListener);
}
