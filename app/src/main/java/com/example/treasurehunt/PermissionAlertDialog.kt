package com.example.treasurehunt

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.treasurehunt.data.PermissionRationale

@Composable
fun PermissionAlertDialogBox(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    rationale: PermissionRationale
) {
    AlertDialog(
        icon = {

        },
        title = {
            Text(text = stringResource(rationale.title))
        },
        text = {
            Text(text = stringResource(rationale.text))
        },
        onDismissRequest = {

        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dimiss")
            }
        }
    )
}