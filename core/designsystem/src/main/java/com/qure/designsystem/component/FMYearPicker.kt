package com.qure.designsystem.component
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.qure.designsystem.utils.FMPreview

@Composable
fun FMYearPicker(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            android.widget.NumberPicker(context)
        },
    ) { numberPicker ->
        with(numberPicker) {
            setOnValueChangedListener { _, _, newVal ->
                onValueChange(newVal)
            }
            minValue = 1000
            maxValue = 10000
            wrapSelectorWheel = false
            setValue(value)
        }
    }
}

@Preview
@Composable
private fun FMNumberPickerPreview() = FMPreview {
    var state by remember { mutableStateOf(0) }
    FMYearPicker(
        value = state,
        onValueChange = {
            state = it
        }
    )
}
