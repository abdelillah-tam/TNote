package com.example.tnote.ui.auth

import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tnote.R
import com.example.tnote.ui.customWidgets.BoxLoading
import com.example.tnote.ui.customWidgets.CustomTextField
import com.example.tnote.ui.customWidgets.Hint
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.accompanist.systemuicontroller.SystemUiController

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    modifier: Modifier,
    dependingHeight: Double,
    onUpPressed: (() -> Unit)? = null,
    authViewModel: AuthViewModel = hiltViewModel(),
    onLoggedIn: () -> Unit,
    loading: (Boolean) -> Unit,
    systemUiController: SystemUiController
) {

    val isLoggedIn by authViewModel.loggedIn.collectAsState()

    var emailValue by remember {
        mutableStateOf("")
    }

    var passwordValue by remember {
        mutableStateOf("")
    }

    val isLoading by authViewModel.isLoading.collectAsState()

    val hasError by authViewModel.hasError.collectAsState()

    LaunchedEffect(isLoggedIn, isLoading) {

        if (isLoading) {
            systemUiController.setStatusBarColor(Color.Transparent)
        } else {
            systemUiController.setStatusBarColor(Color.White)
        }

        if (isLoggedIn) {
            onLoggedIn()
        }
    }

    LaunchedEffect(hasError){
        if(hasError){
            authViewModel.load(false)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "TNote Log In",
                        fontSize = (18 * dependingHeight).sp,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (onUpPressed != null) {
                            onUpPressed()
                        }
                    }) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            contentDescription = "Back to previous page"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
            )
        }
    ) { paddingValues ->

        loading(false)

        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding((16 * dependingHeight).dp)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Image(
                painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = modifier
                    .height((300 * dependingHeight).dp)
                    .width((300 * dependingHeight).dp)
            )

            CustomTextField(
                modifier = modifier
                    .padding(vertical = (4 * dependingHeight).dp)
                    .height((90 * dependingHeight).dp),
                value = emailValue,
                onValueChange = { newEmailValue ->
                    emailValue = newEmailValue
                },
                borderColor = Color.Black,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                hint = {
                    Hint(
                        text = "Enter a valid email",
                        fontSize = (20 * dependingHeight).sp
                    )
                },
                cornerSize = (27 * dependingHeight).dp,
                focusedBorder = MaterialTheme.colors.primary
            )

            CustomTextField(
                value = passwordValue,
                modifier = modifier
                    .padding(vertical = (4 * dependingHeight).dp)
                    .height((90 * dependingHeight).dp),
                onValueChange = { newPasswordValue ->
                    passwordValue = newPasswordValue
                },
                borderColor = Color.Black,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                hint = {
                    Hint(
                        text = "Enter Password",
                        fontSize = (20 * dependingHeight).sp
                    )
                },
                cornerSize = (27 * dependingHeight).dp,
                focusedBorder = MaterialTheme.colors.primary
            )

            Text(
                modifier = modifier.align(Alignment.Start),
                text = "Something Wrong!",
                color = MaterialTheme.colors.primary,
                fontSize = if (hasError) (16 * dependingHeight).sp else 0.sp,
            )

            Button(
                modifier = modifier
                    .padding(top = (20 * dependingHeight).dp)
                    .fillMaxWidth()
                    .height((80 * dependingHeight).dp),
                onClick = {
                    authViewModel.load(true)
                    loading(true)
                    authViewModel.login(emailValue, passwordValue)
                },
                shape = RoundedCornerShape((27 * dependingHeight).dp)
            ) {
                Text("Log In", fontSize = (20 * dependingHeight).sp)
            }
        }


    }

    BoxLoading(isLoading)

    fun facebookLogin(loginManager: LoginManager, callbackManager: CallbackManager, activity: Activity) {

        loginManager.logInWithReadPermissions(activity, listOf("email", "public_profile"))

    }
}


