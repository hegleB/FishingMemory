package com.qure.designsystem.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class DropDownItem(
    val menuName: String,
    val onClick: () -> Unit,
)

@Composable
fun FMDropdownMenu(
    modifier: Modifier = Modifier,
    showMenu: Boolean = false,
    onDismiss: () -> Unit = { },
    menuItems: List<DropDownItem> = emptyList(),
) {
    DropdownMenu(
        modifier = modifier,
        expanded = showMenu,
        onDismissRequest = { onDismiss() },
    ) {
        menuItems.forEach { (name, onClick) ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = name,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                onClick = { onClick(); onDismiss() },
            )
        }
    }
}