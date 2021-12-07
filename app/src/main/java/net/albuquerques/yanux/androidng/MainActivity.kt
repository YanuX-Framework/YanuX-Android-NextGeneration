package net.albuquerques.yanux.androidng

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import net.albuquerques.yanux.androidng.ui.theme.AppTheme
import net.albuquerques.yanux.androidng.ui.theme.TopBar

val Any.TAG: String get() {
    val tag = javaClass.simpleName
    return if (tag.length <= 23) tag else tag.substring(0, 23)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainComponent{
                Row(verticalAlignment = Alignment.CenterVertically){
                    Text("BodyContent")
                    ServiceSwitch()
                }
            }
        }
    }

    @Composable
    fun MainComponent(content: @Composable (PaddingValues) -> Unit) {
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))
        AppTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = { TopBar() },
                content = content,
                //--------------------------------
                //  Unused Parabers (for now!)   |
                //--------------------------------
                //floatingActionButtonPosition = FabPosition.End,
                //floatingActionButton = { FloatingActionButton(onClick = {}){ Text("X") } },
                //drawerContent = { Text(text = "drawerContent") },
                //bottomBar = { BottomAppBar(backgroundColor = materialBlue700) { Text("BottomAppBar") } }
            )
        }
    }

    @Composable
    fun ServiceSwitch() {
        val checkedState = remember { mutableStateOf(false) }
        Switch(
            checked = checkedState.value,
            onCheckedChange = {
                Log.d(TAG, "Checked: $it")
                checkedState.value = it
            }
        )
    }
}

