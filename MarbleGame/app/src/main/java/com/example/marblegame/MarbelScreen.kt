package com.example.marblegame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val MARBLE_SIZE = 40.dp

@Composable
fun MarbleScreen(viewModel: MarbleViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsState()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF)),
    ) {
        val limitX = (maxWidth - MARBLE_SIZE).value
        val limitY = (maxHeight - MARBLE_SIZE).value

        LaunchedEffect(limitX, limitY) {
            viewModel.setBounds(limitX, limitY)
        }

        SensorReadout(
            gx = state.gx,
            gy = state.gy,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(8.dp),
        )

        Marble(x = state.x, y = state.y)
    }
}

@Composable
private fun Marble(x: Float, y: Float) {
    Box(
        modifier = Modifier
            .offset(x.dp, y.dp)
            .size(MARBLE_SIZE)
            .background(Color.Blue, CircleShape),
    )
}

@Composable
private fun SensorReadout(gx: Float, gy: Float, modifier: Modifier = Modifier) {
    Text(
        text = "gx = %.2f   gy = %.2f".format(gx, gy),
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}