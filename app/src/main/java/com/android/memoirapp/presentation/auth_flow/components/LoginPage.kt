package com.android.memoirapp.presentation.auth_flow.components

import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.memoirapp.InsideActivity
import com.android.memoirapp.R
import com.android.memoirapp.common.Routes
import com.android.memoirapp.common.components.BlueBorderWhiteEditTextBox
import com.android.memoirapp.common.components.BlueContainer25
import com.android.memoirapp.common.components.LoadingDialog
import com.android.memoirapp.common.components.PasswordTextField
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavController, modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    val textEmailAddress = remember { mutableStateOf("") }
    val textPassword = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }

    val errorEmail = remember { mutableStateOf(false) }
    val errorPassword = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val loadingData = remember { mutableStateOf(false) }

    LoadingDialog(loadingData.value)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .background(colorResource(R.color.main_blue))
        ){
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = R.drawable.ic_logo_memoir),
                    contentDescription = "Logo Image",
                    modifier = Modifier
                        .size(100.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.app_name),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(24.dp))
                BlueBorderWhiteEditTextBox(
                    textEmailAddress.value,
                    stringResource(R.string.email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    isHintVisible = textEmailAddress.value.isBlank(),
                    onValueChange = {
                        textEmailAddress.value = it
                    },
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                    singleLine = true,
                    onFocusChange = {

                    },
                    enabled = true,
                    isError = errorEmail.value
                )
                Spacer(Modifier.height(24.dp))
                PasswordTextField(
                    textPassword.value,
                    stringResource(R.string.password),
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    isHintVisible = textPassword.value.isBlank(),
                    onValueChange = {
                        textPassword.value = it
                    },
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                    singleLine = true,
                    onFocusChange = {

                    },
                    enabled = true,
                    onVisibleChange = {
                        passwordVisibility.value = it
                    },
                    passwordVisibility = passwordVisibility,
                    isError = errorPassword.value
                )
                Spacer(Modifier.height(16.dp))
                BlueContainer25(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp),
                    text = "Login",
                    onClick = {
                        errorEmail.value = false
                        errorPassword.value = false
                        when{
                            textEmailAddress.value.isBlank() ->{
                                errorEmail.value = true
                                makeToast(context, "Empty Email")
                            }
                            textPassword.value.isBlank() ->{
                                errorPassword.value = true
                                makeToast(context, "Empty Password")
                            }
                            (!Patterns.EMAIL_ADDRESS.matcher(textEmailAddress.value).matches()) -> {
                                errorEmail.value = true
                                makeToast(context, "Invalid Email")
                            }
                            else ->{
                                loadingData.value = true
                                val email = textEmailAddress.value
                                val password = textPassword.value
                                val auth = FirebaseAuth.getInstance()
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnFailureListener {
                                        loadingData.value = false
                                        makeToast(context, "Failed register : ${it.message}")
                                    }
                                    .addOnSuccessListener {
                                        loadingData.value = false
                                        makeToast(context, "Login Success, welcome ${auth.currentUser?.displayName}")
                                        val intent = Intent(context, InsideActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        context.startActivity(intent)
                                    }
                            }
                        }
//                        navController.navigate(Routes.SetProfilePage)
                    })
            }

            Row(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)){
                Text(
                    "Don’t have an account?", fontSize = 12.sp,
                    color = colorResource(R.color.blue),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Sign up", fontSize = 12.sp,
                    color = colorResource(R.color.deep_blue),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            navController.navigate(Routes.RegisterPage)
                        }
                    })
            }
        }
    }
}

@Preview
@Composable
private fun LoginPagePrev() {
    MemoirAppTheme {
        LoginPage(rememberNavController(), Modifier.fillMaxSize())
    }
}

private fun makeToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}