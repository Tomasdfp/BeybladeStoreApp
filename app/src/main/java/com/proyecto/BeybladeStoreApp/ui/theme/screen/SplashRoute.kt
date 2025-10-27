package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
// Note: SplashRoute uses an explicit button to continue; no automatic delay needed

@Composable
fun SplashRoute(onContinue: () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(700)) + scaleIn(animationSpec = tween(700))
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.weight(1f))
            // Use app launcher icon as logo (ic_launcher)
            Image(
                painter = painterResource(id = com.proyecto.BeybladeStoreApp.R.mipmap.ic_launcher),
                contentDescription = "Logo",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            androidx.compose.material3.Button(onClick = onContinue) {
                androidx.compose.material3.Text("Entrar")
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
