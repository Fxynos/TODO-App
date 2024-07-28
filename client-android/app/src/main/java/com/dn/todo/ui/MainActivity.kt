package com.dn.todo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dn.todo.ui.screen.TodoApp
import com.dn.todo.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackbarState = remember { SnackbarHostState() }

            AppTheme(
                dynamicColor = false,
                darkTheme = false
            ) {
                Scaffold(snackbarHost = { SnackbarHost(snackbarState) }) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        TodoApp(snackbarState = snackbarState)
                    }
                }
            }
        }
    }
}