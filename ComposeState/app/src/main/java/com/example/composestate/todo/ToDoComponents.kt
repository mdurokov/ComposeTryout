package com.example.composestate.todo

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.TextField
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedIconRow(
    icon: ToDoIcon,
    onIconChange: (ToDoIcon) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    initialVisibility: Boolean = false
){
    val enter = remember { fadeIn(animSpec = TweenSpec(300, easing = FastOutLinearInEasing)) }
    val exit = remember { fadeOut(animSpec = TweenSpec(100, easing = FastOutSlowInEasing)) }
    Box(modifier = modifier.defaultMinSizeConstraints(minHeight = 16.dp)){
        AnimatedVisibility(
            visible = visible,
            initiallyVisible = initialVisibility,
            enter = enter,
            exit = exit
        ) {
            IconRow(icon, onIconChange)
        }
    }
}

@Composable
fun IconRow(
    icon: ToDoIcon,
    onIconChange: (ToDoIcon) -> Unit,
    modifier: Modifier = Modifier
){
    Row(modifier){
        for(toDoIcon in ToDoIcon.values()){
            SelectableIconButton(
                icon = toDoIcon.vectorAsset,
                onIconSelected = { onIconChange(toDoIcon) },
                isSelected = toDoIcon == icon
            )
        }
    }
}

@OptIn(ExperimentalLayout::class)
@Composable
fun SelectableIconButton(
    icon: VectorAsset,
    onIconSelected: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
){
    val tint = if(isSelected){
        MaterialTheme.colors.primary
    }else{
        MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
    }
    TextButton(
        onClick = { onIconSelected() },
        shape = CircleShape,
        modifier = modifier
    ) {
        Column {
            Icon(icon, tint = tint)
            if(isSelected){
                Box(modifier = Modifier
                    .padding(top = 3.dp)
                    .preferredWidth(icon.defaultWidth)
                    .preferredHeight(1.dp)
                    .background(tint)
                )
            }else{
                Spacer(modifier = Modifier.preferredHeight(4.dp))
            }
        }
    }
}

@Composable
fun ToDoItemInputBackground(
    elevate: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
){
    val animatedElevation = animate(if(elevate) 1.dp else 0.dp, TweenSpec(500))
    Surface(
        color = MaterialTheme.colors.onSurface.copy(0.05f),
        elevation = animatedElevation,
        shape = RectangleShape
    ) {
        Row(
            modifier = modifier.animateContentSize(animSpec = TweenSpec(300)),
            children = content
        )
    }
}

@Composable
fun ToDoInputText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {}
) = TextField(
    value = text,
    onValueChange = onTextChange,
    backgroundColor = Color.Transparent,
    maxLines = 1,
    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
    onImeActionPerformed = { action, softKeyboardController ->
        if (action == ImeAction.Done) {
            onImeAction()
            softKeyboardController?.hideSoftwareKeyboard()
        }
    },
    modifier = modifier
)

@Composable
fun TodoEditButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(text)
    }
}

@Preview
@Composable
fun PreviewToDoEditButton() = TodoEditButton({}, "add")

@Preview
@Composable
fun PreviewIconRow() = IconRow(icon = ToDoIcon.Square, onIconChange = {})