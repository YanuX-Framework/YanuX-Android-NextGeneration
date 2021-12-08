package net.albuquerques.yanux.androidng

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.albuquerques.yanux.androidng.ui.theme.DefaultTemplate
import android.app.AlertDialog;
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import net.albuquerques.yanux.androidng.beacon.BeaconScanner
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser

class MainActivity : ComponentActivity() {
    companion object {
        const val TAG = "MainActivity"
        const val PERMISSION_REQUEST_BACKGROUND_LOCATION = 0
        const val PERMISSION_REQUEST_FINE_LOCATION = 1
    }

    private lateinit var permissions : Array<String>
    private var permissionsGranted = false;

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions -> permissionsGranted = permissions.entries.all { it.value }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                              Manifest.permission.ACCESS_FINE_LOCATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions = permissions.plus(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions = permissions.plus(Manifest.permission.BLUETOOTH_SCAN)
        }


        setContent {
            DefaultTemplate {
                Column(modifier = Modifier.fillMaxSize(),
                       horizontalAlignment = Alignment.CenterHorizontally,
                       verticalArrangement = Arrangement.Center) {
                        ServiceSwitch()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        askPermissions();
    }

    fun askPermissions() {
        for (permission in permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                permissionsGranted = false;
        }

        if (!permissionsGranted) {
            if (!permissions.all{ shouldShowRequestPermissionRationale(it) }) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.permissions_required)
                builder.setMessage(R.string.permissions_required_message)
                builder.setPositiveButton(android.R.string.ok, null)
                builder.setOnDismissListener {
                    activityResultLauncher.launch(permissions)
                }
                builder.show()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.functionality_limited)
                builder.setMessage(R.string.functionality_limited_message)
                builder.setPositiveButton(android.R.string.ok, null)
                builder.setOnDismissListener { }
                builder.show()
            }
        }
    }

    @Composable
    fun ServiceSwitch() {
        val context = LocalContext.current
        val checkedState = remember { mutableStateOf(false) }
        if (permissionsGranted && checkedState.value) BeaconScanner.startScanning(context) else BeaconScanner.stopScanning(context)
        Text(text = if (checkedState.value) stringResource(id = R.string.service_enabled) else stringResource(id = R.string.service_disabled))
        Switch(
            modifier = Modifier.scale(2.0F).padding(8.dp),
            checked = checkedState.value,
            onCheckedChange = {
                Log.d(MainActivity.TAG, "Checked: $it")
                checkedState.value = it
            }
        )
    }
}



