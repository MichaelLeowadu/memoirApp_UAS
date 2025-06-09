package com.android.memoirapp.common.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LogoutDialog(
    onShow: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if(onShow){
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "LOGOUT")
            },
            text = {
                Text("Are you sure you want to logout?")
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("No")
                }
            }
        )
    }

}