package com.example.products.comman

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo


class Constant {

    companion object {


        const val REQUEST_PERMISION_CODE = 1001
        const val REQUEST_FOR_HEIGHTWEIGHT = 1002
        const val REQUEST_FOR_HEALTHISSUE = 1003
        const val REQUEST_CODE_CAMERA = 1004
        const val REQUEST_CODE_GALLERY = 1005
        const val PERMISSION_WRITE_EXTERNAL_STORAGE = 1006


        fun isInternetConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?

            return connectivityManager!!.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!
                .state == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
        }

    }


}