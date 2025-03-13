package com.android.prography.presentation.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelLazy
import com.android.prography.presentation.ui.view.MainActivity
import com.example.compose.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseComposeActivity<VM : BaseViewModel> : ComponentActivity() {

/*    private val viewModelClass = ((javaClass.genericSuperclass as ParameterizedType?)
        ?.actualTypeArguments
        ?.get(1) as Class<VM>).kotlin

    protected open val viewModel by ViewModelLazy(
        viewModelClass,
        { viewModelStore },
        { defaultViewModelProviderFactory },
        { defaultViewModelCreationExtras },
    )*/

    private var showLoading by mutableStateOf(false)
    private var toastMessage by mutableStateOf<String?>(null) // ✅ 일반 토스트 상태
    private var successToastMessage by mutableStateOf<String?>(null) // ✅ 성공 토스트 상태

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()
    }

    private fun setupUi() {
        setContent {
            enableEdgeToEdge()
            MyApplicationTheme {
                TopSurface()
            }
        }
    }

    // ✅ UI를 제공하는 Composable 함수
    @Composable
    abstract fun ProvideUI(viewModel: VM)

    // ✅ 이벤트를 감지하고 상태를 변경
    private fun handleEvent(event: BaseViewModel.Event) {
        when (event) {
            is BaseViewModel.Event.ShowToast -> {
                toastMessage = event.message
            }
            is BaseViewModel.Event.ShowToastRes -> {
                toastMessage = getString(event.message)
            }
            is BaseViewModel.Event.ShowSuccessToast -> {
                successToastMessage = event.message
            }
            is BaseViewModel.Event.ShowSuccessToastRes -> {
                successToastMessage = getString(event.message)
            }
            is BaseViewModel.Event.ShowLoading -> showLoading = true
            is BaseViewModel.Event.HideLoading -> showLoading = false
            is BaseViewModel.Event.ExpiredToken -> {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
            else -> {}
        }
    }

    @Composable
    fun ObserveToastMessages(context: Activity = LocalContext.current as Activity) {
        toastMessage?.let { message ->
            LaunchedEffect(message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                toastMessage = null // ✅ 한 번 표시 후 상태 초기화
            }
        }

        successToastMessage?.let { message ->
            LaunchedEffect(message) {
                Toast.makeText(context, "✅ $message", Toast.LENGTH_SHORT).show()
                successToastMessage = null // ✅ 한 번 표시 후 상태 초기화
            }
        }
    }

    @Composable
    fun ObserveLoadingState() {
        LoadingDialog(showDialog = showLoading)
    }

    // Method to get the VM class type
    abstract fun getViewModelClass(): KClass<VM>

    // ✅ 상태를 감지하는 TopSurface
    @Composable
    fun TopSurface() {

        val viewModelType = getViewModelClass()
        val viewModel = androidx.lifecycle.viewmodel.compose.viewModel(
            modelClass = viewModelType.java
        ) as VM

        val context = LocalContext.current as Activity
        val eventFlow by viewModel.baseEventFlow.collectAsState()

        LaunchedEffect(eventFlow) {
            viewModel.baseEventFlow.collectLatest { event ->
                handleEvent(event)
            }
        }

        Surface(modifier = Modifier.fillMaxSize()) {
            ObserveToastMessages(context)
            ObserveLoadingState()
            ProvideUI(viewModel)
        }
    }
}

@Composable
fun LoadingDialog(showDialog: Boolean) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("로딩 중...") },
            text = { CircularProgressIndicator() },
            confirmButton = {}
        )
    }
}