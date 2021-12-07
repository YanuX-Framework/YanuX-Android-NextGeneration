package net.albuquerques.yanux.androidng.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
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
    val context = LocalContext.current
    val colors = getThemeParameters().colors
    val backgroundColor = colors?.primary ?: Color.Black
    TopAppBar(
        title = { Text(text = context.getString(R.string.app_name)) },
        backgroundColor = backgroundColor
    )
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