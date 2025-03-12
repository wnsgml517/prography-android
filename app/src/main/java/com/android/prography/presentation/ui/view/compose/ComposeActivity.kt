package com.android.prography.presentation.ui.view.compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme{
                DefaultPreview()
            }
        }
    }
}

@Composable
fun TopLevel(viewModel: ToDoViewModel = hiltViewModel()) {
    Column {
        TodoInput(text = viewModel.text.value, onSubmit = viewModel.onSubmit, onTextChange = {
            viewModel.text.value = it
        })
        LazyColumn {
            items(viewModel.toDoList) {
                Todo(
                    toDoData = it,
                    onToggle = viewModel.onToggle,
                    onDelete = viewModel.onDelete,
                    onEdit = viewModel.onEdit
                )
            }
        }
    }
}

@Composable
fun Todo(
    toDoData: ToDoData,
    onToggle: (Int, Boolean) -> Unit = { _, _ -> },
    onEdit: (Int, String) -> Unit = { _, _ -> },
    onDelete: (Int) -> Unit = { _ -> }
) {
    val localContext = LocalContext.current
    var isEditor by remember { mutableStateOf(false) }

    Crossfade(targetState = isEditor) {
        when (it) {
            // 수정 중
            true -> {
                val (text, setText) = remember { mutableStateOf("") }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(value = text, onValueChange = setText)
                    Button(onClick = {
                        isEditor = false
                        onEdit(toDoData.key, text)
                    }) {
                        Text(text = "입력")
                    }
                }
            }
            // 일반
            false -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = toDoData.text, modifier = Modifier
                        .weight(1f)
                        .clickable {
                            Toast
                                .makeText(localContext, toDoData.text, Toast.LENGTH_SHORT)
                                .show()
                        })
                    Checkbox(checked = toDoData.done, onCheckedChange = { checked ->
                        onToggle(toDoData.key, checked)
                    })
                    Button(onClick = { isEditor = true }) {
                        Text(text = "수정")
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(onClick = {
                        onDelete(toDoData.key)
                    }) {
                        Text(text = "삭제")
                    }
                }
            }
        }
    }
}

@Composable
fun TodoInput(text: String, onTextChange: (String) -> Unit, onSubmit: (String) -> Unit = { _ -> }) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(value = text, onValueChange = onTextChange)
        Button(onClick = {
            onSubmit(text)
        }) {
            Text(text = "입력")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        TopLevel()
    }
}

@Preview(showBackground = true)
@Composable
fun TodoInputPreview() {
    MyApplicationTheme {
        Todo(ToDoData(1, "nice"))
    }
}


data class ToDoData(
    val key: Int, val text: String, val done: Boolean = false
)