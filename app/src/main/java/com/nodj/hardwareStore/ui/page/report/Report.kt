package com.nodj.hardwareStore.ui.page.report

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.api.ApiStatus
import com.nodj.hardwareStore.api.model.ReportRemote
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.ui.UI.ErrorPlaceholder
import com.nodj.hardwareStore.ui.UI.LoadingPlaceholder
import com.nodj.hardwareStore.ui.UI.showToast
import java.util.Date
import java.util.Locale

@Composable
fun Report(
    navController: NavController,
    viewModel: ReportViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val context = LocalContext.current
    if (viewModel.error != 0) {
        showToast(context, stringResource(viewModel.error))
        viewModel.clearError()
    }
    when (viewModel.apiStatus) {
        ApiStatus.DONE -> {
            navController?.let {
                Report(
                    dateState = viewModel.dateState,
                    reportListUiState = viewModel.reportListUiState.reportListUiState,
                    onUpdate = viewModel::updateUiState,
                    getReport = { viewModel.getReport() }
                )
            }
        }

        ApiStatus.LOADING -> LoadingPlaceholder()
        else -> ErrorPlaceholder(
            message = viewModel.apiError,
            onBack = { navController?.popBackStack() }
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Report(
    dateState: DateState,
    reportListUiState:
    List<ReportRemote>,
    onUpdate: (DateState) -> Unit,
    getReport: () -> Unit
) {
    var showStartDatePicker by remember {
        mutableStateOf(false)
    }
    var showEndDatePicker by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.padding(all = 10.dp)) {
        LazyColumn() {
            item {
                Column(
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showStartDatePicker = true }) {
                        if (dateState.startDate == null || dateState.startDate == "null") {
                            Text(text = "Старт периода")
                        } else {
                            Text(text = dateState.startDate)
                        }
                    }
                    if (showStartDatePicker) {
                        DatePickerView(onUpdate = { startDate: String ->
                            onUpdate(dateState.copy(startDate = startDate))
                        }, onDismiss = { showStartDatePicker = false })
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showEndDatePicker = true }) {
                        if (dateState.endDate == null || dateState.endDate == "null") {
                            Text(text = "Конец периода")
                        } else {
                            Text(text = dateState.endDate)
                        }
                    }
                    if (showEndDatePicker) {
                        DatePickerView(onUpdate = { endDate: String ->
                            onUpdate(dateState.copy(endDate = endDate))
                        }, onDismiss = { showEndDatePicker = false })
                    }
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        getReport()
                    }) {
                        Text(text = "Отчёт")
                    }
                }
            }
            items(reportListUiState.size) { index ->
                val product = reportListUiState[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(top = 10.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(10.dp)),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 10.dp),
                            text = "Название: ${product.name}"
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 10.dp),
                            text = "Сумма: ${
                                String.format(
                                    Locale.US,
                                    "%.2f",
                                    product.sum
                                )
                            }"
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 10.dp),
                            text = "Количество купленных товаров: ${product.count}"
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 10.dp),
                            text = "Средняя цена за период: ${
                                String.format(Locale.US, "%.2f", product.sum / product.count)
                            }"
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 10.dp),
                            text = "Текущая цена: ${
                                String.format(
                                    Locale.US,
                                    "%.2f",
                                    product.currentPrice
                                )
                            }"
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(
    onUpdate: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DatePickerDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(onClick = {
                    onUpdate(selectedDate.toString())
                    onDismiss()
                }
                ) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    onDismiss()
                }) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(Date(millis))
}