package com.android.prography.presentation.ui.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.prography.R
import com.android.prography.presentation.ui.view.MainActivity
import com.android.prography.presentation.ui.view.compose.DefaultPreview
import com.android.prography.presentation.ui.view.compose.PhotoList
import com.android.prography.presentation.ui.view.compose.ToDoViewModel
import com.android.prography.presentation.ui.view.compose.TopLevel
import com.example.compose.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.lang.reflect.ParameterizedType

abstract class BaseComposeActivity<VM : BaseViewModel> : ComponentActivity() {

    private val viewModelClass = ((javaClass.genericSuperclass as ParameterizedType?)
        ?.actualTypeArguments
        ?.get(1) as Class<VM>).kotlin

    protected open val viewModel by ViewModelLazy(
        viewModelClass,
        { viewModelStore },
        { defaultViewModelProviderFactory },
        { defaultViewModelCreationExtras },
    )

    private var showLoading by mutableStateOf(false)
/*
    private val loadingDialog by lazy {
        AppCompatDialog(this).apply {
            setContentView(R.layout.item_progress_loading)
            setCancelable(false)
            window?.setDimAmount(0.7f)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()
    }

    private fun setupUi() {
        setContent {
            MyApplicationTheme{
                BasePreview()
            }
        }
    }


    @Composable
    abstract fun ProvideUI()

    @Composable
    private fun handleEvent(context: Activity, event: BaseViewModel.Event) {
        when (event) {
            is BaseViewModel.Event.ShowToast -> {
                ToastMessage(event.message)
            }
            is BaseViewModel.Event.ShowToastRes -> {
                ToastMessage(context.getString(event.message))
            }
            is BaseViewModel.Event.ShowSuccessToast -> {
                SuccessToastMessage(event.message)
            }
            is BaseViewModel.Event.ShowSuccessToastRes -> {
                SuccessToastMessage(context.getString(event.message))
            }
            is BaseViewModel.Event.ShowLoading -> showLoadingDialog()
            is BaseViewModel.Event.HideLoading -> dismissLoadingDialog()
            is BaseViewModel.Event.ExpiredToken -> {
                context.startActivity(Intent(context, MainActivity::class.java))
                context.finishAffinity()
            }
            else -> {}
        }
    }

    @Composable
    fun ObserveLoadingState() {
        LoadingDialog(showDialog = showLoading)
    }

    fun showLoadingDialog() {
        showLoading = true
    }

    fun dismissLoadingDialog() {
        showLoading = false
    }

    // 상태바 색상 설정 함수
    protected fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }
    }
    @Composable
    fun TopSurface(vm: ViewModel = hiltViewModel()) {
        // 여기서 vm을 viewModel값으로 설정하고 싶다는...

        val context = LocalContext.current as Activity
        val eventFlow = viewModel.baseEventFlow.collectAsState()


        Surface(modifier = Modifier.fillMaxSize()){
            LaunchedEffect(eventFlow.value) {
                viewModel.baseEventFlow.collectLatest { event ->
                    handleEvent(context, event)
                }
            }

            ObserveLoadingState()
            ProvideUI()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun BasePreview() {
        MyApplicationTheme {
            TopSurface()
        }
    }
}


@Composable
fun ToastMessage(message: String) {
    val context = LocalContext.current
    LaunchedEffect(message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}


@Composable
fun SuccessToastMessage(message: String) {
    val context = LocalContext.current
    LaunchedEffect(message) {
        Toast.makeText(context, "✅ $message", Toast.LENGTH_SHORT).show()
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