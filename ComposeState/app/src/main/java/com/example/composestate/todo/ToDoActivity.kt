package com.example.composestate.todo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import com.example.composestate.ui.ComposeStateTheme

class ToDoActivity : AppCompatActivity(){
    val toDoViewModel by viewModels<ToDoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeStateTheme {
                Surface {
                    ToDoActivityScreen(toDoViewModel = toDoViewModel)
                }
            }
        }
    }
}

@Composable
private fun ToDoActivityScreen(toDoViewModel: ToDoViewModel){
    ToDoScreen(
        items = toDoViewModel.toDoItems,
        currentlyEditing = toDoViewModel.currentEditItem,
        onAddItem = toDoViewModel::addItem,
        onRemoveItem = toDoViewModel::removeItem,
        onEditDone = toDoViewModel::onEditDone,
        onEditItemChange = toDoViewModel::onEditItemChange,
        onStartEdit = toDoViewModel::onEditItemSelected
    )
}