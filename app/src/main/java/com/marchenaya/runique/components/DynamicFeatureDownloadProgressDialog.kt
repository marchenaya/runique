package com.marchenaya.runique.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.marchenaya.runique.R
import com.marchenaya.runique.navigation.dynamicfeature.DynamicFeatureManager
import com.marchenaya.runique.navigation.dynamicfeature.InstallStatus
import kotlin.math.roundToInt

@Composable
fun DynamicFeatureDownloadProgressDialog(
    dynamicFeatureManager: DynamicFeatureManager,
    modifier: Modifier = Modifier
) {
    val status = dynamicFeatureManager.status
    if (status is InstallStatus.Idle) return

    val confirmationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) {}

    AlertDialog(
        onDismissRequest = {},
        modifier = modifier,
        title = { Text(text = stringResource(id = status.titleRes())) },
        text = { DialogBody(status = status) },
        confirmButton = {
            if (status is InstallStatus.RequiresUserConfirmation) {
                Button(
                    onClick = {
                        dynamicFeatureManager.startConfirmationDialogForResult(
                            state = status.state,
                            launcher = confirmationLauncher
                        )
                    }
                ) {
                    Text(text = stringResource(id = R.string.dynamic_confirm_download))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = dynamicFeatureManager::cancelInstallModule) {
                Text(text = stringResource(id = R.string.dynamic_cancel))
            }
        }
    )
}

@Composable
private fun DialogBody(
    status: InstallStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (status) {
            is InstallStatus.Downloading -> {
                val percentage =
                    stringResource(R.string.format_percentage, (status.progress * 100).roundToInt())
                val description =
                    stringResource(id = R.string.dynamic_downloading_progress, percentage)
                CircularProgressIndicator(
                    progress = { status.progress },
                    modifier = Modifier.semantics { contentDescription = description }
                )
                Text(text = percentage, style = MaterialTheme.typography.labelSmall)
            }

            is InstallStatus.Failed -> {
                Text(
                    text = stringResource(id = R.string.dynamic_error_code, status.errorCode),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            is InstallStatus.RequiresUserConfirmation, is InstallStatus.Idle -> Unit

            else -> CircularProgressIndicator()
        }
    }
}

private fun InstallStatus.titleRes(): Int = when (this) {
    is InstallStatus.Pending -> R.string.dynamic_status_requesting
    is InstallStatus.Downloading -> R.string.dynamic_status_downloading
    is InstallStatus.Installing -> R.string.dynamic_status_installing
    is InstallStatus.RequiresUserConfirmation -> R.string.dynamic_status_requires_confirmation
    is InstallStatus.Failed -> R.string.dynamic_status_failed
    is InstallStatus.Idle -> R.string.dynamic_status_requesting
}
