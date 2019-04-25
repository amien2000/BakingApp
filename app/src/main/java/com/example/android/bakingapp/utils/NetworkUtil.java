package com.example.android.bakingapp.utils;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;
import android.widget.Toast;
import android.util.Patterns;

import java.net.URL;

import retrofit2.http.Url;

public class NetworkUtil {
    private Activity mActivity;
    private boolean mInternetConnected;
    private String mUrl;
    private boolean isUrl;

    public NetworkUtil(Activity activity, String url){
        mActivity = activity;
        mUrl = url;
    }

    public boolean internetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mActivity.getSystemService(mActivity.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            mInternetConnected = true;
        } else{
            mInternetConnected = false;
        }
        return mInternetConnected;
    }

    public boolean urlCheck(){
        isUrl = Patterns.WEB_URL.matcher(mUrl.toString().trim()).matches();
        return isUrl;
    }

    public void networkMessage(){
        if(!internetConnection()) {
            Toast.makeText(mActivity,"No internet connection", Toast.LENGTH_LONG).show();
        }else if (!urlCheck()){
            Toast.makeText(mActivity,"No Video Content", Toast.LENGTH_LONG).show();
        }
    }
}



