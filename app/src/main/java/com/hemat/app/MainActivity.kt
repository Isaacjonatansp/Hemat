package com.hemat.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hemat.app.ui.AppRoot
import com.hemat.app.ui.HomeViewModel
import com.hemat.app.ui.theme.HematTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as HematApp
        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(app.repo, app.lang) as T
            }
        }
        setContent {
            HematTheme {
                val vm: HomeViewModel = viewModel(factory = factory)
                AppRoot(vm)
            }
        }
    }
}
