package com.example.treasurehunt.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.treasurehunt.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartGameScreen(
    onBackArrowPressed: () -> Unit
) {
    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            modifier = Modifier,
            searchBarState = searchBarState,
            textFieldState = textFieldState,
            onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = {
                Text(
                    text = "Find Nearby Quests",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
            )},
            trailingIcon = {
                if (searchBarState.currentValue == SearchBarValue.Expanded) {
                    IconButton(
                        onClick = { scope.launch { searchBarState.animateToCollapsed() } }
                    ) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                } else {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            },
        )
    }
    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = { onBackArrowPressed() }
                ) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                }
                TopSearchBar(
                    scrollBehavior = scrollBehavior,
                    state = searchBarState,
                    inputField = inputField,
                )
                ExpandedFullScreenSearchBar(
                    state = searchBarState,
                    inputField = inputField,
                ) {
                    /*TODO: query results based on textFieldState.text*/
                    /*TODO: create and add list of quest items for now */
                }
            }
        },
        bottomBar = {}
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            /*TODO: ExpandedFullScreenSearchBar handles results, add other content here*/
        }
    }
}

@Composable
fun QuestCard(
    title: String,
    description: String,
    questRating: Double,
    distToDestination: Double? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                onClick = { /*TODO: add functionality*/ },
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp,
            pressedElevation = 5.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(8.dp)
                    .then(
                        Modifier
                            .weight(0.6f)
                            .takeIf { distToDestination != null } ?: Modifier
                    )
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                ) {
                    StarRating(rating = questRating)
                    Text(
                        text = questRating.toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            if(distToDestination != null) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.4f)
                ){
                    Text(
                        text = "$distToDestination km",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
fun StarRating(
    rating: Double
) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .background(color = Color.Gray),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var starFill = rating + 1
        repeat(5) {
            starFill -= 1.0
            Log.d("startFill", "$starFill")
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .size(24.dp)
            ) {
                when {
                    starFill > 1.0 -> {
                        Icon(
                            painter = painterResource(R.drawable.star_rate_24dp),
                            tint = Color.Yellow,
                            contentDescription = "Star"
                        )
                    }
                    starFill <= 0.0 -> {
                        Icon(
                            painter = painterResource(R.drawable.star_rate_24dp),
                            tint = Color.Black,
                            contentDescription = "Star"
                        )
                    }
                    else -> {
                        val partialFill = Math.round(starFill * 10) / 10.0
                        Icon(
                            painter = painterResource(R.drawable.star_rate_24dp),
                            tint = Color.Black,
                            contentDescription = "Star"
                        )
                        Icon(
                            painter = painterResource(R.drawable.star_rate_24dp),
                            tint = Color.Yellow,
                            contentDescription = "Star",
                            modifier = Modifier
                                .size(24.dp)
                                .graphicsLayer { clip = true }
                                .drawWithContent {
                                    val width = (size.width * partialFill).toFloat()
                                    clipRect(0f, 0f, width, size.height) {
                                        this@drawWithContent.drawContent()
                                    }
                                }

                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TestStartRating() {
    StarRating(rating = 3.6)
}

@Preview
@Composable
fun ViewQuestCard() {
    QuestCard(
        title = "Hello",
        description = "World",
        questRating = 3.8,
        distToDestination = 1000.0
    )
}