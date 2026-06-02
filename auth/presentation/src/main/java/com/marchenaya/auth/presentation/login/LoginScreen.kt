package com.marchenaya.auth.presentation.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marchenaya.auth.presentation.R
import com.marchenaya.core.presentation.designsystem.EmailIcon
import com.marchenaya.core.presentation.designsystem.Poppins
import com.marchenaya.core.presentation.designsystem.RuniqueTheme
import com.marchenaya.core.presentation.designsystem.components.GradientBackground
import com.marchenaya.core.presentation.designsystem.components.RuniqueActionButton
import com.marchenaya.core.presentation.designsystem.components.RuniquePasswordTextField
import com.marchenaya.core.presentation.designsystem.components.RuniqueTextField
import com.marchenaya.core.presentation.ui.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvent.ShowSnackbar -> {
                keyboardController?.hide()
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
            }

            LoginEvent.Success -> {
                onLoginSuccess()
            }
        }

    }

    LoginScreen(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            when (action) {
                is LoginAction.OnRegisterClick -> onSignUpClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    snackbarHostState: SnackbarHostState,
    onAction: (LoginAction) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        GradientBackground(
            modifier = Modifier
                .consumeWindowInsets(padding)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 32.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.hi_there),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(R.string.runique_welcome_text),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(48.dp))

                RuniqueTextField(
                    state = state.email,
                    startIcon = EmailIcon,
                    endIcon = null,
                    hint = stringResource(R.string.example_email),
                    title = stringResource(R.string.email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(17.dp))

                RuniquePasswordTextField(
                    state = state.password,
                    isPasswordVisible = state.isPasswordVisible,
                    onTogglePasswordVisibility = {
                        onAction(LoginAction.OnTogglePasswordVisibility)
                    },
                    hint = stringResource(R.string.password),
                    title = stringResource(R.string.password),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                RuniqueActionButton(
                    text = stringResource(R.string.login),
                    isLoading = state.isLoggingIn,
                    enabled = state.canLogin
                ) {
                    onAction(LoginAction.OnLoginClick)
                }

                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = Poppins,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        append(stringResource(id = R.string.dont_have_an_account) + " ")

                        val loginLink = LinkAnnotation.Clickable(
                            tag = "clickable_text",
                            linkInteractionListener = {
                                onAction(LoginAction.OnRegisterClick)
                            }
                        )

                        withLink(loginLink) {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = Poppins,
                                    textDecoration = TextDecoration.None
                                )
                            ) {
                                append(stringResource(id = R.string.sign_up))
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(text = annotatedString)
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    RuniqueTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}