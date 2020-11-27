package com.example.composestate

import com.example.composestate.todo.ToDoViewModel
import com.example.composestate.util.generateRandomTodoItem
import org.junit.Test
import com.google.common.truth.Truth.assertThat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ToDoViewModelTest {
    fun whenRemovingItem_updatesList() {
        val viewModel = ToDoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)

        viewModel.removeItem(item1)

        assertThat(viewModel.toDoItems).isEqualTo(listOf(item2))
    }
}