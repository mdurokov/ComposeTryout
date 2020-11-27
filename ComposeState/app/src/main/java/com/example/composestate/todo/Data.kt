package com.example.composestate.todo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.VectorAsset
import java.util.*

data class ToDoItem(
    val task: String,
    val icon: ToDoIcon = ToDoIcon.Default,
    val id: UUID = UUID.randomUUID()
)

enum class ToDoIcon(val vectorAsset: VectorAsset){
    Square(Icons.Default.CropSquare),
    Done(Icons.Default.Done),
    Event(Icons.Default.Event),
    Privacy(Icons.Default.PrivacyTip),
    Trash(Icons.Default.RestoreFromTrash);

    companion object {
        val Default = Square
    }
}