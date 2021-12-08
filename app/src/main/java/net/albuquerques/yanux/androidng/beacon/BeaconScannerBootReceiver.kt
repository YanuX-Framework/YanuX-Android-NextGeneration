package net.albuquerques.yanux.androidng.beacon

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BeaconScannerBootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null) {
            Log.d("BeaconScanningBootReceiver", "Starting Beacon Scanner");
            //BeaconScanner.init(context)
            //BeaconScanner.startScanning(context)
        }
    }
}