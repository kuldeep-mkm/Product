package com.example.products.reciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager


class ConnectionReceiver : BroadcastReceiver() {

    var Listener: ReceiverListener? = null

    override fun onReceive(context: Context, intent: Intent?) {

        // initialize connectivity manager
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Initialize network info
        val networkInfo = connectivityManager.activeNetworkInfo

        // check condition
        if (Listener != null) {

            // when connectivity receiver
            // listener  not null
            // get connection status
            val isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting

            // call listener method
            Listener!!.onNetworkChange(isConnected)
        }
    }

    interface ReceiverListener {
        // create method
        fun onNetworkChange(isConnected: Boolean)
    }

}