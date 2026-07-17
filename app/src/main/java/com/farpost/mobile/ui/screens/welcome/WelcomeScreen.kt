package com.farpost.mobile.ui.screens.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farpost.mobile.ui.components.FarpostButton
import com.farpost.mobile.ui.components.FarpostButtonSize
import com.farpost.mobile.ui.components.FarpostButtonVariant
import com.farpost.mobile.ui.theme.Brand
import com.farpost.mobile.ui.theme.TextPrimary

@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Wordmark per style guide §6 — no logo asset exists, this text treatment
        // ("far" primary + "post" brand, extrabold, tight tracking) is the real one.
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = TextPrimary, fontWeight = FontWeight.ExtraBold)) { append("far") }
                withStyle(SpanStyle(color = Brand, fontWeight = FontWeight.ExtraBold)) { append("post") }
            },
            style = MaterialTheme.typography.displaySmall,
        )
        Text(
            text = "Sign in to document what you see.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        )
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        )

        if (viewModel.errorMessage != null) {
            Text(
                text = viewModel.errorMessage.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFEF4444),
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        if (viewModel.isSigningIn) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
        } else {
            FarpostButton(
                label = "Sign in",
                onClick = viewModel::signIn,
                variant = FarpostButtonVariant.DarkChip,
                size = FarpostButtonSize.Large,
            )
        }
    }
}
