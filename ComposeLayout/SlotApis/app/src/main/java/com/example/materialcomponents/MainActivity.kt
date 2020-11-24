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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.layout.layoutId
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
    children: @Composable () -> Unit
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

@Composable
fun ConstraintLayoutContent(){
    ConstraintLayout {
        val (button, text) = createRefs()
        
        Button(
            onClick = {},
            modifier = Modifier.constrainAs(button){
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button")
        }
        Text(
            text = "Text",
            modifier = Modifier.constrainAs(text){
                top.linkTo(button.bottom, margin = 16.dp)
                centerHorizontallyTo(parent)
            }
        )
    }
}

@Composable
fun ConstraintLayoutBarrierContent(){
    ConstraintLayout {
        val (button1, button2, text) = createRefs()

        Button(
            onClick = {},
            modifier = Modifier.constrainAs(button1){
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text(text = "Button 1")
        }
        Text(
                text = "Text",
                modifier = Modifier.constrainAs(text) {
                    top.linkTo(button1.bottom, margin = 16.dp)
                    centerAround(button1.end)
                }
        )

        val barrier = createEndBarrier(button1, text)
        Button(
                onClick = {},
                modifier = Modifier.constrainAs(button2){
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(barrier)
                }
        ) {
            Text(text = "Button 2")
        }
    }
}

@Composable
fun DecoupledConstraintContent(){
    WithConstraints {
        val constraints = if(maxWidth < maxHeight){
            decoupledConstraints(margin = 16.dp)
        }else{
            decoupledConstraints(margin = 32.dp)
        }

        ConstraintLayout(constraints) {
            Button(onClick = {}, Modifier.layoutId("button")) {
                Text("Text")
            }
            Text(text = "Text", Modifier.layoutId("text"))
        }
    }
}

@Preview
@Composable
fun DecoupledConstraintPreview(){
    MaterialTheme {
        DecoupledConstraintContent()
    }
}

@Composable
fun ConstraintDimensionsContent(){
    ConstraintLayout {
        val text = createRef()
        val guideline = createGuidelineFromStart(fraction = 0.5f)
        Text(
            text = "This is very very very very very very very very long text",
            modifier = Modifier.constrainAs(text){
                linkTo(start = guideline, end = parent.end)
                width = Dimension.preferredWrapContent
            }
        )
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet{
    return ConstraintSet{
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button){
            top.linkTo(parent.top, margin)
        }
        constrain(text){
            top.linkTo(button.bottom, margin)
        }
    }
}

@Preview
@Composable
fun ConstraintDimensionsPreview(){
    MaterialComponentsTheme {
        ConstraintDimensionsContent()
    }
}

@Preview
@Composable
fun ConstraintPreview(){
    MaterialComponentsTheme {
        ConstraintLayoutContent()
    }
}

@Preview
@Composable
fun ConstraintBarrierPreview(){
    MaterialComponentsTheme {
        ConstraintLayoutBarrierContent()
    }
}


/*@Preview
@Composable
fun ChipPreview(){
    MaterialComponentsTheme {
        Chip(text = "Hi Codelab!")
    }
}*/

/*fun Modifier.firstBaselineToTop(
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
}*/

/*@Composable
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
}*/

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