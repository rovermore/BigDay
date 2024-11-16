package com.smallworldfs.moneytransferapp.compose.dialogs

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.colorRedError
import com.smallworldfs.moneytransferapp.compose.widgets.SWTextButton
import com.smallworldfs.moneytransferapp.compose.dialogs.model.SWDatePickerDateUIModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SWDatePickerDialog(
    maxDate: Long = Long.MAX_VALUE,
    minDate: Long = 0L,
    dateSelected: (SWDatePickerDateUIModel) -> Unit
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis,
    )

    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                primary = colorBlueOcean,
                onPrimaryContainer = colorBlueOcean,

            )
        ) {
            DatePickerDialog(
                colors = DatePickerDefaults.colors(
                    selectedDayContentColor = colorBlueOcean,
                    selectedDayContainerColor = colorGreenMain,
                    todayContentColor = colorRedError,
                    todayDateBorderColor = colorBlueOcean,
                ),
                onDismissRequest = {
                    openDialog.value = false
                    dateSelected(
                        getDateSelected(time = state.selectedDateMillis ?: Calendar.getInstance().timeInMillis)
                    )
                },
                confirmButton = {
                    SWTextButton(
                        text = "OK",
                        colorText = colorBlueOcean,
                        clickAction = {
                            openDialog.value = false
                            dateSelected(
                                getDateSelected(time = state.selectedDateMillis ?: Calendar.getInstance().timeInMillis)
                            )
                        }
                    )
                },
                dismissButton = {
                    SWTextButton(
                        text = "CANCEL",
                        colorText = colorBlueOcean,
                        clickAction = {
                            openDialog.value = false
                            dateSelected(
                                getDateSelected(time = state.selectedDateMillis ?: Calendar.getInstance().timeInMillis)
                            )
                        }
                    )
                }
            ) {
                DatePicker(
                    state = state,
                    dateValidator = {
                        val time = it / 1000
                        time in minDate..maxDate
                    }
                )
            }
        }
    }
}

private fun getDateSelected(time: Long) =
    SWDatePickerDateUIModel(
        getYear(time),
        getMonth(time),
        getDay(time)
    )

private fun getYear(time: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    return calendar.get(Calendar.YEAR)
}

private fun getMonth(time: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    return calendar.get(Calendar.MONTH)
}

private fun getDay(time: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    return calendar.get(Calendar.DATE)
}

@Preview(showBackground = true, widthDp = 420)
@Composable
private fun SWDatePickerDialogPreview() {
    SWDatePickerDialog { _ -> }
}
