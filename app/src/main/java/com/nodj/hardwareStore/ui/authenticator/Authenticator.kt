package com.nodj.hardwareStore.ui.authenticator


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.db.datastore.PreferencesStore
import kotlinx.coroutines.launch

@Composable
fun Authenticator(
    viewModel: AuthenticatorViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val store = PreferencesStore(context)
    val scope = rememberCoroutineScope()
    val username = store.getUsername().collectAsState(initial = "").value

    LiveStore.user.value = viewModel.user

    fun synchronize() {
        scope.launch {
            if (username == "") {
                LiveStore.user.value = null
                return@launch
            }
            viewModel.findUserByName(username)
        }
    }

    synchronize()
}
