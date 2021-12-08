package net.albuquerques.yanux.androidng.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import com.google.android.material.composethemeadapter.ThemeParameters
import com.google.android.material.composethemeadapter.createMdcTheme
import net.albuquerques.yanux.androidng.R

@Composable
fun getThemeParameters(): ThemeParameters {
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    return createMdcTheme(context = context, layoutDirection = layoutDirection)
}


@Composable
fun TopBar() {
    val colors = getThemeParameters().colors
    val backgroundColor = colors?.primary ?: Color.Black
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        backgroundColor = backgroundColor
    )
}

@Composable
fun DefaultTemplate(content: @Composable (PaddingValues) -> Unit) {
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
fun AppTheme(content: @Composable() () -> Unit) {
    val (colors, typography, shapes) = getThemeParameters()
    if (colors != null && typography != null && shapes != null) {
        MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}