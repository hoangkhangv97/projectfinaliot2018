package com.group3.androidthings.MVVM.View;

import org.json.JSONObject;

public interface NPNHomeView extends IView {
    void onSuccessUpdateServer(JSONObject message);

    void onErrorUpdateServer(String message);

}
