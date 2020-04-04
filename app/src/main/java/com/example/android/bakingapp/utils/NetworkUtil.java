package com.example.android.bakingapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.widget.RecipeWidgetConfigureActivity;

public class NetworkUtil {
    private Activity mActivity;
    private String mUrl;
    private boolean isUrl;

    public NetworkUtil(Activity activity, String url){
        mActivity = activity;
        mUrl = url;
    }

    public boolean internetConnection() {
        ConnectivityManager manager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    public boolean urlCheck(){
        isUrl = Patterns.WEB_URL.matcher(mUrl.trim()).matches();
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



