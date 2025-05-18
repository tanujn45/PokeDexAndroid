package com.tanujn45.pokedex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.tanujn45.pokedex.R

@Composable
fun Preloader(modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.primary) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.width(150.dp),
            painter = rememberDrawablePainter(
                drawable = getDrawable(
                    LocalContext.current,
                    R.drawable.pika_preloader
                )
            ),
            contentDescription = "Loading..."
        )
//        CircularProgressIndicator(
//            modifier = Modifier.width(64.dp),
//            color = color,
//            trackColor = MaterialTheme.colorScheme.surfaceVariant
//        )
    }
}