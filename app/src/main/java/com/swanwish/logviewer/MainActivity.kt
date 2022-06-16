package com.swanwish.logviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swanwish.logviewer.ui.theme.LogViewerTheme
import com.swanwish.logviewer.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.refreshCrashLog()

        setContent {
            LogViewerTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Crash LogViewer", color = MaterialTheme.colors.onPrimary)
                            },
                            actions = {
                                // Save Group
                                IconButton(onClick = { viewModel.refreshCrashLog() }) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh",
                                        tint = MaterialTheme.colors.onPrimary
                                    )
                                }
                                IconButton(onClick = { viewModel.cleanCrashLog() }) {
                                    Icon(
                                        imageVector = Icons.Default.Build,
                                        contentDescription = "Clean",
                                        tint = MaterialTheme.colors.onPrimary
                                    )
                                }
                                IconButton(onClick = { throw Exception("crash the app") }) {
                                    Icon(
                                        imageVector = Icons.Default.ExitToApp,
                                        contentDescription = "Crash",
                                        tint = MaterialTheme.colors.onError
                                    )
                                }
                            }
                        )
                    },
                    content = {
                        CrashLogView(
                            modifier = Modifier.padding(it),
                            viewModel = viewModel
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun CrashLogView(modifier: Modifier, viewModel: MainViewModel) {
    val crashLog: String by viewModel.crashLog.observeAsState("")
    val scroll = rememberScrollState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = crashLog.ifEmpty { "-- empty --" },
            style = MaterialTheme.typography.caption,
            modifier = modifier
                .verticalScroll(scroll)
                .fillMaxWidth()
                .padding(10.dp)
        )
    }
}
