package com.example.materialcomponents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.materialcomponents.ui.MaterialComponentsTheme


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialComponentsTheme {
                LayoutsCodelab()
            }
        }
    }
}

@Composable
fun LayoutsCodelab() {
    val fav = remember { mutableStateOf(false) }
    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(text = "Layouts codelab")
                        },
                        actions = {
                            IconButton(onClick = { fav.value = !fav.value}) {
                                Icon(asset = Icons.Filled.Favorite, tint = if (fav.value) Color.Red else Color.Gray)
                            }
                        }
                )
            }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding).padding(8.dp))
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier){
    MyOwnColumn(modifier.padding(8.dp)) {
        Text("MyOwnColumn")
        Text("places items")
        Text("verticaly")
        Text("We've done it by hand!")
    }
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = Modifier.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]
    val placeableY = firstBaselineToTop.toIntPx() - firstBaseline
    val height = placeable.height + placeableY
    layout(placeable.width, height){
        placeable.placeRelative(0, placeableY)
    }
}

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    children: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        children = children,
    ) { measurables, constraints ->
        val placeables = measurables.map {measurable ->
            measurable.measure(constraints)
        }

        var yPosition = 0

        layout(constraints.maxWidth, constraints.maxHeight){
            placeables.forEach{placeable ->
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height
            }
        }
    }
}

/*@Preview
@Composable
fun TextWithPaddingToBaselinePreview(){
    MaterialComponentsTheme {
        Text(text = "Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}

@Preview
@Composable
fun TextWithNormalPaddingPreview(){
    MaterialComponentsTheme {
        Text(text = "Hi there!", Modifier.padding(32.dp))
    }
}
*/
@Preview
@Composable
fun LayoutsCodelabPreview () {
    MaterialComponentsTheme {
        LayoutsCodelab()
    }
}