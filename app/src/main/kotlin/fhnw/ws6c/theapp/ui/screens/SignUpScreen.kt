package fhnw.ws6c.theapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.R
import fhnw.ws6c.theapp.model.AuthModel

@Composable
fun SignUpScreen(authModel: AuthModel) {
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .windowInsetsPadding(WindowInsets.ime) // Adjust for IME
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Hey there,",
                textAlign = TextAlign.Center,
                style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
            )
            Text(
                text = "Create an Account", textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(R.drawable.aloe_happy),
                contentDescription = "PlAAAAnt!",
                modifier = Modifier
                    .size(150.dp)
                    .scale(1F)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (authModel.email.text.isNotEmpty()) {
                email = authModel.email
            }
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    authModel.email = it
                },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        authModel.createAccount(email.text, password.text)
                        keyboardController?.hide()
                    }
                )
            )
            authModel.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    authModel.createAccount(email.text, password.text)
                    keyboardController?.hide()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.onSecondary,
                    Color.Gray,
                    Color.White
                )
            ) {
                Text("Sign Up")
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Already got an account? ",
                    fontSize = 14.sp,
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground))
                ClickableText(text = AnnotatedString("Login"),
                    style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 15.sp),
                    onClick = {
                        authModel.signInScreen()
                    })
            }

        }
    }
}

@Composable
fun LoginScreen(authModel: AuthModel) {
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .windowInsetsPadding(WindowInsets.ime)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Welcome Back,",
                textAlign = TextAlign.Center,
                style = TextStyle(color = MaterialTheme.colorScheme.onBackground))
            Text(
                text = "Login", textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(R.drawable.aloe_happy),
                contentDescription = "PlAAAAnt!",
                modifier = Modifier
                    .size(150.dp)
                    .scale(1F)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (authModel.email.text.isNotEmpty()) {
                email = authModel.email
            }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it
                                authModel.email = it},
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        authModel.signIn(email.text, password.text)
                        keyboardController?.hide()
                    }
                )
            )
            authModel.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Button(
                onClick = { authModel.signIn(email.text, password.text) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.onSecondary,
                    Color.Gray,
                    Color.White
                )
            ) {
                Text("Login")
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("New here? ",
                    fontSize = 14.sp,
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground))
                ClickableText(
                    text = AnnotatedString("Create an account"),
                    style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 15.sp),
                    onClick = { authModel.signUpScreen() })
            }
        }
    }
}