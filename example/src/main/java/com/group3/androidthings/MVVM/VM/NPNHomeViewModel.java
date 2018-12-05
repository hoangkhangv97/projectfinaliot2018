package com.group3.androidthings.MVVM.VM;

import com.group3.androidthings.MVVM.View.NPNHomeView;
import com.group3.androidthings.Network.ApiResponseListener;

import org.json.JSONObject;

public class NPNHomeViewModel extends BaseViewModel<NPNHomeView> {
    public void updateToServer(String url)
    {
        requestGETJSONWithURL(url, new ApiResponseListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                view.onSuccessUpdateServer(response);
            }

            @Override
            public void onError(String error) {
                view.onErrorUpdateServer(error);
            }
        });
    }
}
