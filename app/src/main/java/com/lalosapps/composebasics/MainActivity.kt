package com.lalosapps.composebasics

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lalosapps.composebasics.ui.theme.ComposeBasicsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MyApp() }
    }
}

@Composable
private fun MyApp() {
    // rememberSaveable will persist our state on config changes and process death (in scalable apps the view model should handle this)
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
    ComposeBasicsTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            if (shouldShowOnboarding) {
                OnboardingScreen(onContinuedClicked = { shouldShowOnboarding = false })
            } else {
                Greetings()
            }
        }
    }
}

@Composable
private fun OnboardingScreen(
    onContinuedClicked: () -> Unit
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinuedClicked
            ) {
                Text(text = "Continue")
            }
        }
    }
}

@Composable
private fun Greetings(names: List<String> = List(1000) { "$it" }) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        item { Text(text = "Header") }
        items(names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
private fun Greeting(name: String) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        // Only for Surface. We are using a Card which gives us a nice look without this line of code
        //.shadow(elevation = 4.dp, clip = true, shape = RoundedCornerShape(8.dp))
    ) {
        CardContent(name)
    }
}

@Composable
private fun CardContent(name: String) {
    // rememberSaveable can be used here to persist the expanded state since LazyColumn recomposes only the elements that are in the screen
    var expanded by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring( // tween, repeatable are other animations
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = "Hello,")
            Text(
                text = name,
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " + "padding theme elit, sed do bouncy. ")
                        .repeat(4)
                )
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}

@Preview("Light Mode", showBackground = true)
@Preview("Dark Mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MyAppPreview() {
    MyApp()
}

@Preview("Greetings: Light Mode", showBackground = true, widthDp = 320)
@Preview("Greetings: Dark Mode", uiMode = UI_MODE_NIGHT_YES, widthDp = 320)
@Composable
private fun GreetingsPreview() {
    ComposeBasicsTheme {
        Greetings()
    }
}