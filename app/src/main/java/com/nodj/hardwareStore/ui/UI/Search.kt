package com.nodj.hardwareStore.ui.UI

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    initValue: String,
    onDone: (String) -> Unit
) {
    val maxLength = 20
    val (value, setValue) = remember { mutableStateOf(initValue) }
    fun clear() {
        setValue("")
        onDone("")
    }

    fun handleClearButtonClick() {
        clear()
    }

    fun handleChange(newValue: String) {
        if (newValue.length > maxLength) {
            return
        } else if (newValue.isEmpty()) {
            clear()
        }
        setValue(newValue)
    }

    fun handleDone() {
        Log.d("simpleName", "Get handleDone")
        onDone(value)
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = { handleChange(it) },
        shape = RoundedCornerShape(15.dp),
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Проверено") },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = { handleDone() }
        ),
        trailingIcon = {
            if (value != "") {
                IconButton(onClick = { handleClearButtonClick() }) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = null
                    )
                }
            }
        }
    )
}