package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.proyecto.BeybladeStoreApp.util.SharedPrefs
import com.proyecto.BeybladeStoreApp.repository.AuthRepository
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp


@Composable
fun SettingsRoute(onBack: () -> Unit = {}, onAdminAdd: () -> Unit = {}, onViewOrders: () -> Unit = {}, onProfile: () -> Unit = {}) {
    val context = LocalContext.current

    val sessionState = produceState(initialValue = null as String?, key1 = context) {
        try {
            val repo = AuthRepository(context)
            value = repo.getSession()
        } catch (t: Throwable) {
            try {
                val dir = java.io.File(context.filesDir, "crash_logs")
                if (!dir.exists()) dir.mkdirs()
                val f = java.io.File(dir, "produce_session_error.txt")
                f.appendText("${java.util.Date()} - ${t.stackTraceToString()}\n")
            } catch (_: Exception) {}
            value = null
        }
    }

    val currentUser = sessionState.value

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Ajustes", style = MaterialTheme.typography.headlineSmall)




        if (currentUser == "admin") {
            Surface(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)) {
                Button(onClick = onAdminAdd, colors = ButtonDefaults.buttonColors()) {
                    Text(text = "Agregar producto (Admin)", fontSize = 16.sp)
                }
            }
        }

        Surface(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)) {
            Button(onClick = onViewOrders) {
                Text(text = "Ver órdenes")
            }
        }

        Surface(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)) {
            Button(onClick = onProfile) {
                Text(text = "Mi perfil")
            }
        }
    }
}

