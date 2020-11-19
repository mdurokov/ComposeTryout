package com.example.composetryout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.composetryout.ui.purple200
import com.example.composetryout.ui.purple500
import com.example.composetryout.ui.purple700
import com.example.composetryout.ui.teal200

private val DarkColors = darkColors(
        primary = purple200,
        primaryVariant = purple500,
        secondary = purple700
)

private val LightColors = lightColors(
        primary = purple500,
        primaryVariant = purple700,
        secondary = teal200
)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MyScreenContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    ComposeTryoutDarkTheme(true) {

        Surface(color = Color.Yellow){
            content()
        }
    }
}

@Composable
fun Greeting(name: String){
    Surface(color = Color.Yellow) {
        Text(text = "Hello $name!", modifier = Modifier.padding(10.dp))
    }
}

@Composable
fun MyScreenContent(names: List<String> = listOf("Android", "there")) {
    val counterState = remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxHeight()) {
        Column(modifier = Modifier.weight(1f)) {
            for (name in names) {
                Greeting(name = name)
                Divider(color = Color.Black)
            }
        }
        Counter(
                count = counterState.value,
                updateCount = { newCount ->
                    counterState.value = newCount
                }
        )
    }
}

@Composable
fun Counter(count: Int, updateCount: (Int) -> Unit){
    Button(
            onClick = {updateCount(count + 1)},
            colors = ButtonConstants.defaultButtonColors(
                backgroundColor = if (count % 5 > 2) Color.Black else Color.Cyan
            )

    ) {
        Text(text = "I've clicked this $count times")
    }
}

@Composable
fun ComposeTryoutDarkTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
){

}

@Preview(showBackground = true, name = "Text Preview")
@Composable
fun DefaultPreview(){
    MyApp {
        MyScreenContent()
    }
}