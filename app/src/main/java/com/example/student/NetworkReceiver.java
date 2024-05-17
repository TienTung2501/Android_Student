package com.example.student;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;


public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
           if(isInternet(context)){
               Toast.makeText(context,"Đã kết nối mạng",Toast.LENGTH_SHORT).show();
           }
           else {
               Toast.makeText(context,"Không có kết nối mạng",Toast.LENGTH_SHORT).show();

           }
       }


    }
    private boolean isInternet(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager==null)
                return false;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                Network network=connectivityManager.getActiveNetwork();
                if(network==null){
                    return  false;
                }
                NetworkCapabilities networkCapabilities=connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities!=null&& networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            }
            else {
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                    return networkInfo!=null&&networkInfo.isConnectedOrConnecting();
            }
    }
}
