package net.albuquerques.yanux.androidng.beacon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import net.albuquerques.yanux.androidng.MainActivity
import net.albuquerques.yanux.androidng.R
import org.altbeacon.beacon.*

object BeaconScanner {
    private const val TAG = "BeaconScanner"
    var isScanning = false
        private set
    var isInitialized = false
        private set

    private val region = Region("any", null, null, null)
    private val centralRangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d(MainActivity.TAG, "Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            Log.d(TAG, "$beacon about ${beacon.distance} meters away")
        }
    }

    fun init(context: Context) {
        if(!isInitialized) {
            val beaconManager = BeaconManager.getInstanceForApplication(context)
            beaconManager.beaconParsers.clear()
            // iBeacon
            beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
            // Amazfit
            beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:0-3=570102FF,i:20-25"))

            setupForegroundService(context)
            beaconManager.setEnableScheduledScanJobs(false);
            beaconManager.backgroundBetweenScanPeriod = 0;
            beaconManager.backgroundScanPeriod = 1100;

            val regionViewModel = BeaconManager.getInstanceForApplication(context).getRegionViewModel(
                region
            )
            regionViewModel.rangedBeacons.observeForever(centralRangingObserver)

            isInitialized = true;
        }
    }

    private fun setupForegroundService(context: Context) {
        val builder = NotificationCompat.Builder(context, "BeaconReferenceApp")
        builder.setSmallIcon(R.drawable.ic_launcher_background)
        builder.setContentTitle("Scanning for Beacons")
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(pendingIntent);
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "beacon-ref-notification-id",
                "My Notification Name", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "My Notification Channel Description"
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channel.id);
        }
        BeaconManager.getInstanceForApplication(context)
            .enableForegroundServiceScanning(builder.build(), 456);
    }

    fun startScanning(context: Context) {
        if(!isScanning) {
            init(context)
            val beaconManager = BeaconManager.getInstanceForApplication(context)
            beaconManager.startRangingBeacons(region)
            setupForegroundService(context);
        }
        isScanning = true;
    }

    fun stopScanning(context: Context) {
        if(isScanning) {
            val beaconManager = BeaconManager.getInstanceForApplication(context)
            beaconManager.stopRangingBeacons(region)
            beaconManager.disableForegroundServiceScanning()
            isScanning = false;
        }
    }
}