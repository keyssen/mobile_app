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
    application: @Composable () -> Unit,
    viewModel: AuthenticatorViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val store = PreferencesStore(context)
    val scope = rememberCoroutineScope()
    val user = viewModel.user

    val username = store.getUsername().collectAsState(initial = "").value

    fun setUsername(newUsername: String) {
        scope.launch {
            store.setUsername(newUsername)
        }
    }

    fun clear() {
        scope.launch {
            LiveStore.user.value = null
            viewModel.clear()
        }
    }

    Log.d("username hi-hi 2", username)
    if (username != "" && user != null) {
        Log.d("hi-hi 2", username)
        LiveStore.user.value = user
        setUsername(user.name)
    }
//    if (username == "") {
//        clear()
//    }
//    if (username != "" && LiveStore.user.value == null) {
//        getUser(username)
//    }


    fun synchronize() {
        scope.launch {
            if (username == "") {
                LiveStore.user.value = null
                viewModel.clear()
                return@launch
            }
            if (LiveStore.user.value?.name != username) {
                viewModel.findUserByName(username)
            }
        }
    }

    synchronize()
    application()
}
