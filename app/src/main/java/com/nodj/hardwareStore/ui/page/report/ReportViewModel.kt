package com.nodj.hardwareStore.ui.page.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.model.ReportRemote
import kotlinx.coroutines.launch

class ReportViewModel(
    private val service: MyServerService,
) : ViewModel() {

    var reportListUiState by mutableStateOf(ReportListUiState())
        private set

    var dateState by mutableStateOf(DateState())
        private set


    fun getReport() {
        viewModelScope.launch {
            if (LiveStore.getUserId() != 0 && validateInput(dateState)) {
                reportListUiState =
                    ReportListUiState(
                        service.getReport(
                            dateState.startDate.toString(),
                            dateState.endDate.toString()
                        )
                    )
            }
        }
    }

    fun updateUiState(dateState: DateState) {
        this.dateState = DateState(startDate = dateState.startDate, endDate = dateState.endDate)
    }

    private fun validateInput(uiState: DateState = dateState): Boolean {
        return with(uiState) {
            startDate != null && startDate != "null"
                    && endDate != null && endDate != "null" && stringDateToNumber(startDate) < stringDateToNumber(
                endDate
            )
        }
    }
}

fun stringDateToNumber(dateString: String): Long {
    val parts = dateString.split("-")
    val year = parts[0].toLong()
    val month = parts[1].toLong()
    val day = parts[2].toLong()

    return year * 10000 + month * 100 + day
}

data class DateState(
    val startDate: String? = null,
    val endDate: String? = null,
)

data class ReportListUiState(val reportListUiState: List<ReportRemote> = listOf())