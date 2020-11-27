package com.example.composestate.util

import com.example.composestate.todo.ToDoIcon
import com.example.composestate.todo.ToDoItem

fun generateRandomTodoItem(): ToDoItem {
    val message = listOf(
        "Learn compose",
        "Learn state",
        "Build dynamic UIs",
        "Learn Unidirectional Data Flow",
        "Integrate LiveData",
        "Integrate ViewModel",
        "Remember to savedState!",
        "Build stateless composables",
        "Use state from stateless composables"
    ).random()
    val icon = ToDoIcon.values().random()
    return ToDoItem(message, icon)
}

fun main() {
    val list = listOf(1, 2, 3)
    var currentCount = 0
    for (item in list) {
        currentCount += item
        println(currentCount)
    }
}