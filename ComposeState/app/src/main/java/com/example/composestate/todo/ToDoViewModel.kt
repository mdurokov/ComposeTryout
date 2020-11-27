package com.example.composestate.todo

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ToDoViewModel : ViewModel() {

    private var currentEditPosition by mutableStateOf(-1)
    var toDoItems by mutableStateOf(listOf<ToDoItem>())
        private set
    val currentEditItem: ToDoItem?
        get() = toDoItems.getOrNull(currentEditPosition)

    fun onEditItemSelected(item: ToDoItem){
        currentEditPosition = toDoItems.indexOf(item)
    }

    fun onEditDone(){
        currentEditPosition = -1
    }

    fun onEditItemChange(item: ToDoItem){
        val currentItem = requireNotNull(currentEditItem)
        require(currentItem.id == item.id){
            "You can only change an item with the same id as currentEditItem"
        }
        toDoItems = toDoItems.toMutableList().also {
            it[currentEditPosition] = item
        }
    }

    fun addItem(item: ToDoItem) {
        toDoItems = toDoItems + listOf(item)
    }

    fun removeItem(item: ToDoItem) {
        toDoItems = toDoItems.toMutableList().also {
            it.remove(item)
        }
        onEditDone()
    }
}