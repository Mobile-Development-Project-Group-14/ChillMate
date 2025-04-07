package com.example.chillmate.ui.components

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import java.util.Calendar

@Composable
fun DatePickerDialog(
    context: Context,
    initialDate: Calendar,
    onDateSelected: (Calendar) -> Unit,
    onDismiss: () -> Unit = {}
) {
    DatePickerDialog(
        context,
        { _, year, month, day ->
            Calendar.getInstance().apply {
                set(year, month, day)
                onDateSelected(this)
            }
        },
        initialDate.get(Calendar.YEAR),
        initialDate.get(Calendar.MONTH),
        initialDate.get(Calendar.DAY_OF_MONTH)
    ).apply {
        setButton(DatePickerDialog.BUTTON_POSITIVE, "OK") { _, _ -> }
        setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel") { _, _ -> onDismiss() }
        show()
    }
}