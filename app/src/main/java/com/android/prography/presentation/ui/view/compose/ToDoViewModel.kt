package com.android.prography.presentation.ui.view.compose

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.android.prography.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor() : BaseViewModel() {
    var text = mutableStateOf("")
    val toDoList = mutableStateListOf<ToDoData>()
    private var key = mutableIntStateOf(-1)

    val onSubmit: (String) -> Unit = { text ->
        val exists = toDoList.find { it.key == key.intValue + 1 } != null

        if (!exists) {
            key.intValue += 1
        } else {
            var keyValue = key.intValue
            for (i in key.intValue until 100) {
                if (toDoList.find { it.key == keyValue + 1 } == null) {
                    key.intValue = keyValue + 1
                    break
                } else {
                    keyValue++
                }
            }
        }
        toDoList.add(
            ToDoData(
                key = key.value, text = text
            )
        )
        this.text.value = ""
    }
    val onToggle: (Int, Boolean) -> Unit = { key, checked ->
        val pos = toDoList.indexOfFirst {
            it.key == key
        }
        toDoList[pos] = toDoList[pos].copy(done = checked)
    }
    val onDelete: (Int) -> Unit = { key ->
        this.key.intValue -= 1
        val pos = toDoList.indexOfFirst {
            it.key == key
        }
        toDoList.removeAt(pos)
    }

    val onEdit: (Int, String) -> Unit = { key, text ->
        val pos = toDoList.indexOfFirst {
            it.key == key
        }
        toDoList[pos] = toDoList[pos].copy(text = text)
    }
}
