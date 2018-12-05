package com.group3.androidthings.MVVM.VM;

import android.content.Context;

import com.group3.androidthings.MVVM.View.IView;
import com.group3.androidthings.NPNConstants;
import com.group3.androidthings.Network.ApiResponseListener;
import com.group3.androidthings.Network.VolleyRemoteApiClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BaseViewModel<T extends IView> {

    T view;
    protected Context mContext;

    BaseViewModel() {

    }

    public void attach(T view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    protected void requestGETJSONWithURL(String url, ApiResponseListener<JSONObject> listener) {
        VolleyRemoteApiClient.createInstance(mContext);
        Map<String, String> header = new HashMap<>();
        header.put(NPNConstants.apiHeaderKey, NPNConstants.apiHeaderValue);
        VolleyRemoteApiClient.getInstance().getJSON(url, header, listener);
    }

}
