package fhnw.ws6c.theapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.R
import fhnw.ws6c.theapp.model.AuthModel

@Composable
fun SignUpScreen(viewModel: AuthModel) {
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Hey there,", textAlign = TextAlign.Center)
            Text(
                text = "Create an Account", textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 28.sp
                )
            )
            Image(
                painter = painterResource(R.drawable.aloe_happy),
                contentDescription = "PlAAAAnt!",
                modifier = Modifier
                    .size(200.dp)
                    .scale(1F)
                    .padding(vertical = 24.dp)
                    .fillMaxWidth().align(Alignment.CenterHorizontally)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Button(
                onClick = { viewModel.createAccount(email.text, password.text) },
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
            Row (horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Text("Already got an account? ", fontSize = 14.sp)
                ClickableText(text = AnnotatedString("Login"),
                    style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 15.sp),
                    onClick = {
                        viewModel.signInScreen()
                })
            }

        }
    }
}

@Composable
fun SignInScreen(viewModel: AuthModel) {
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Welcome Back,", textAlign = TextAlign.Center)
            Text(
                text = "Sign In", textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 28.sp
                )
            )
            Image(
                painter = painterResource(R.drawable.aloe_happy),
                contentDescription = "PlAAAAnt!",
                modifier = Modifier
                    .size(200.dp)
                    .scale(1F)
                    .padding(vertical = 24.dp)
                    .fillMaxWidth().align(Alignment.CenterHorizontally)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Button(
                onClick = { viewModel.signIn(email.text, password.text) },
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
                Text("Sign In")
            }
            Row (horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Text("New here? ", fontSize = 14.sp)
                ClickableText(
                    text = AnnotatedString("Create an account"),
                    style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 15.sp),
                    onClick = { viewModel.signUpScreen() })
            }
        }
    }
}