package com.example.composestate.todo

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.composestate.util.generateRandomTodoItem

@Composable
fun ToDoScreen(
    items: List<ToDoItem>,
    currentlyEditing: ToDoItem?,
    onAddItem: (ToDoItem) -> Unit,
    onRemoveItem: (ToDoItem) -> Unit,
    onStartEdit: (ToDoItem) -> Unit,
    onEditItemChange: (ToDoItem) -> Unit,
    onEditDone: () -> Unit

){
    Column {
        val enableTopSelection = currentlyEditing == null
        ToDoItemInputBackground(elevate = enableTopSelection) {
            if(enableTopSelection){
                ToDoItemEntryInput(onItemComplete = onAddItem)
            }else{
                Text(
                    text = "Editing item",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(16.dp)
                            .fillMaxWidth()
                )
            }
        }
        LazyColumnFor(
            items = items,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {toDo ->
            if(currentlyEditing?.id == toDo.id){
                ToDoItemInlineEditor(
                    item = currentlyEditing,
                    onEditItemChange = onEditItemChange,
                    onEditDone = onEditDone,
                    onRemoveItem = { onRemoveItem(toDo) }
                )
            }else {
                ToDoRow(
                        toDo = toDo,
                        onItemClicked = { onStartEdit(it) },
                        modifier = Modifier.fillParentMaxWidth()
                )
            }
        }
        Button(
            onClick = { onAddItem(generateRandomTodoItem()) },
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
        ) {
            Text("Add random item")
        }
    }
}

@Composable
fun ToDoRow(
    toDo: ToDoItem,
    onItemClicked: (ToDoItem) -> Unit,
    modifier: Modifier = Modifier,
    iconAlpha: Float = remember(toDo.id) { randomTint() }
){
    Row(
        modifier = modifier
            .clickable{ onItemClicked(toDo)}
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(toDo.task)
        Icon(
            toDo.icon.vectorAsset,
            tint = AmbientContentColor.current.copy(alpha = iconAlpha)
        )
    }
}

private fun randomTint() = (2..8).random().toFloat()/10

@Composable
fun ToDoItemEntryInput(onItemComplete: (ToDoItem) -> Unit){
    val (text, setText) = remember { mutableStateOf("") }
    val (icon, setIcon) = remember { mutableStateOf(ToDoIcon.Default) }
    val iconsVisible = text.isNotBlank()
    val submit = {
        onItemComplete(ToDoItem(text, icon))
        setIcon(ToDoIcon.Default)
        setText("")
    }
    ToDoItemEntry(
        text = text,
        onTextChange = setText,
        icon = icon,
        onIconChange = setIcon,
        submit = submit,
        iconsVisible = iconsVisible
    ) {
        TodoEditButton(onClick = submit, text = "Add", enabled = text.isNotBlank() )
    }
}

@Composable
fun ToDoItemInlineEditor(
    item: ToDoItem,
    onEditItemChange: (ToDoItem) -> Unit,
    onEditDone: () -> Unit,
    onRemoveItem: () -> Unit
) = ToDoItemEntry(
    text = item.task,
    onTextChange = { onEditItemChange(item.copy(task = it)) },
    icon = item.icon,
    onIconChange = { onEditItemChange(item.copy(icon = it)) },
    submit = onEditDone,
    iconsVisible = true
){
    Row {
        val shrinkButtons = Modifier.widthIn(20.dp)
        TextButton(onClick = onEditDone, modifier = shrinkButtons) {
            Text(
                text = "\uD83D\uDCBE",
                textAlign = TextAlign.End,
                modifier = Modifier.width(30.dp)
            )
        }
        TextButton(onClick = onRemoveItem, modifier = shrinkButtons) {
            Text(
                text = "❌",
                textAlign = TextAlign.End,
                modifier = Modifier.width(30.dp)
            )
        }
    }
}

@Composable
private fun ToDoItemEntry(
    text: String,
    onTextChange: (String) -> Unit,
    icon: ToDoIcon,
    onIconChange: (ToDoIcon) -> Unit,
    submit: () -> Unit,
    iconsVisible: Boolean,
    buttonSlot: @Composable () -> Unit
) {
    Column {
        Row(Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            ToDoInputText(
                    text = text,
                    onTextChange = onTextChange,
                    onImeAction = submit,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(Modifier.align(Alignment.CenterVertically)) { buttonSlot() }
        }
        if (iconsVisible) {
            AnimatedIconRow(icon = icon, onIconChange = onIconChange, Modifier.padding(top = 8.dp))
        } else {
            Spacer(modifier = Modifier.preferredHeight(16.dp))
        }
    }
}

@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        ToDoItem("Learn compose", ToDoIcon.Event),
        ToDoItem("Take the codelab"),
        ToDoItem("Apply state", ToDoIcon.Done),
        ToDoItem("Build dynamic UIs", ToDoIcon.Square)
    )
    ToDoScreen(items,null, {}, {}, {}, {}, {})
}

@Preview
@Composable
fun PreviewTodoRow() {
    val todo = remember { generateRandomTodoItem() }
    ToDoRow(toDo = todo, onItemClicked = {}, modifier = Modifier.fillMaxWidth())
}