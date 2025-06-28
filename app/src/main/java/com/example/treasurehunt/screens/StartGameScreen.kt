package com.example.treasurehunt.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.treasurehunt.data.questList
import com.example.treasurehunt.model.QuestItem
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
                    text = "Find Nearby Quests By City",
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

                    LazyColumn {
                        items(questList) { quest ->
                            QuestCard(
                                questItem = quest,
                                distToDestination = 1000.0
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {}
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            StartScreenCollapsableItem(
                title = "Radius"
            ) {
                SearchByRadius { searchRadius -> /* Handle radius search */ }
            }

            StartScreenCollapsableItem(
                title = "Rating"
            ) {
                SearchByStarRating { starRating ->/* Handle rating search */ }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    /* Temporarily route to city search
                    * TODO: hook slider value for ratings and radius
                    * */
                    scope.launch { searchBarState.animateToExpanded() }
                }
            ) {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
/**
 *
 * WIP: A child composable that collapsable content
 *
 * @param collapsableContent The current search query.
 */
@Composable
fun StartScreenCollapsableItem(
    title: String,
    collapsableContent: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        val rotationX by animateFloatAsState(
            targetValue = if (isExpanded) 180f else 0f,
            animationSpec = tween(durationMillis = 600),
            label = "FlipChevronIcon"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(horizontal = 10.dp)
                .clickable { isExpanded = !isExpanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,

                )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.chevron_up_24),
                contentDescription = "chevron icon",
                modifier = Modifier
                    .graphicsLayer { this.rotationX = rotationX }
            )
        }
        HorizontalDivider()
        if (isExpanded) {
            collapsableContent()
        }
    }
}

@Composable
fun SearchByRadius(
    onRadiusSelected: (Float) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var sliderValue by remember { mutableFloatStateOf(0f) }
        Slider(
            modifier = Modifier.padding(horizontal = 20.dp),
            value = sliderValue,
            onValueChange = { sliderValue = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            valueRange = 0f..100f
        )
        Text(text = "%.1f".format(sliderValue) + "km from current location")
        onRadiusSelected(sliderValue)
    }
}

@Composable
fun SearchByStarRating(
    onStarRatingSelected: (Float) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var sliderValue by remember { mutableFloatStateOf(0f) }
        Slider(
            modifier = Modifier.padding(horizontal = 20.dp),
            value = sliderValue,
            onValueChange = { sliderValue = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            valueRange = 0f..5f
        )
        Text(text = "Rating: " + "%.1f".format(sliderValue) + " Stars")
        onStarRatingSelected(sliderValue)
    }
}

@Composable
fun QuestCard(
    questItem: QuestItem,
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
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .then(
                        Modifier
                            .weight(0.6f)
                            .takeIf { distToDestination != null } ?: Modifier
                    )
            ) {
                Text(
                    text = questItem.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = questItem.description,
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                ) {
                    StarRating(rating = questItem.rating)
                    Text(
                        text = questItem.rating.toString(),
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
        questItem = questList[0],
        distToDestination = 1000.0
    )
}