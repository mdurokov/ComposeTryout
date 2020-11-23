package com.example.materialcomponents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlin.math.absoluteValue
import kotlin.math.max


val topics = listOf(
        "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
        "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
        "Religion", "Social sciences", "Technology", "TV", "Writing"
)

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
    ScrollableRow(modifier = modifier) {
        StaggeredGrid(modifier = modifier) {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    children: @Composable() () -> Unit
){
    Layout(modifier = modifier, children = children){ measurables, constraints ->
        val rowWidths = IntArray(rows) { 0 }
        val rowMaxHeights = IntArray(rows) { 0 }
        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)
            val row = index % rows
            rowWidths[row] = rowWidths[row] + placeable.width.absoluteValue
            rowMaxHeights[row] = max(rowMaxHeights[row], placeable.height.absoluteValue)

            placeable
        }

        val width = rowWidths.maxOrNull()
                ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth
        val height = rowMaxHeights.sumBy { it }
                .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))
        val rowY = IntArray(rows) { 0 }

        for(i in 1 until rows){
            rowY[i] = rowY[i-1] + rowMaxHeights[i-1]
        }

        layout(width, height) {
            val rowX = IntArray(rows) {0}
            placeables.forEachIndexed{ index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String){
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.preferredSize(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(modifier = Modifier.preferredWidth(4.dp))
            Text(text)
        }
    }
}

/*@Preview
@Composable
fun ChipPreview(){
    MaterialComponentsTheme {
        Chip(text = "Hi Codelab!")
    }
}*/

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