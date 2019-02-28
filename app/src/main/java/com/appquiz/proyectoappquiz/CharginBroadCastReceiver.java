package com.appquiz.proyectoappquiz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Detecta si tiene conexión o no y muestra un toast
 */
public class CharginBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConexion = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if(noConexion){
                Toast.makeText(context, R.string.desconectado, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, R.string.conectado, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
