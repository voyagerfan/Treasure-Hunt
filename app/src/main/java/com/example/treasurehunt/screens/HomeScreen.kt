package com.example.treasurehunt.screens

import android.text.style.AlignmentSpan
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.treasurehunt.R
import com.example.treasurehunt.data.ScreenList
import com.example.treasurehunt.ui.theme.catamaranFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController){
    val scrollState = rememberScrollState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var isClicked by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.AppTitle)
                    )
                },
                actions = {
                    MenuDropdown()
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .wrapContentWidth()
                                .align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_explore_24),
                                contentDescription = "Explore"
                            )

                            Text(
                                text = "Explore",
                                fontWeight = FontWeight.Bold,
                                fontFamily = catamaranFamily,
                                fontSize = 12.sp
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .wrapContentWidth()
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.favorite_24dp),
                                contentDescription = "Favorites",
                                tint = androidx.compose.ui.graphics.Color.Red
                            )
                            Text(
                                text = "Favorites",
                                fontWeight = FontWeight.Bold,
                                fontFamily = catamaranFamily,
                                fontSize = 12.sp
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .wrapContentWidth()
                                .align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.notifications_24dp),
                                contentDescription = "Notifications",
                                tint = androidx.compose.ui.graphics.Color(0xFFFFD700)
                            )
                            Text(
                                text = "Notifications",
                                fontWeight = FontWeight.Bold,
                                fontFamily = catamaranFamily,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenWidth)
            ){
                Row(){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .size(screenWidth/2)
                            .clickable {
                                isClicked = !isClicked
                            /* TODO: Add navigation from here*/
                            }

                    ){
                        Text(
                            text = "Start Game",
                            fontWeight = FontWeight.Bold,
                            fontFamily = catamaranFamily,
                            fontSize = 17.sp
                        )
                        Image(
                            painter = painterResource(R.drawable.start_screen_736),
                            contentDescription = "start new name image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        )

                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .size(screenWidth/2)
                            .clickable {
                                isClicked = !isClicked
                                navController.navigate(route = ScreenList.RULE_SCREEN.name)
                            }
                    ){
                        Text(
                            text = "Rule List",
                            fontWeight = FontWeight.Bold,
                            fontFamily = catamaranFamily,
                            fontSize = 17.sp
                        )
                        Image(
                            painter = painterResource(R.drawable.rule_list_736),
                            contentDescription = "rule list image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        )
                    }
                }

                Row(){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .size(screenWidth/2)
                            .clickable {
                                isClicked = !isClicked
                                /* TODO: Add navigation from here*/
                            }
                    ){
                        Text(
                            text = "Achievements",
                            fontWeight = FontWeight.Bold,
                            fontFamily = catamaranFamily,
                            fontSize = 17.sp
                        )
                        Image(
                            painter = painterResource(R.drawable.achievements_image),
                            contentDescription = "Achievements Image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .size(screenWidth/2)
                            .clickable {
                                isClicked = !isClicked
                                /* TODO: Add navigation from here*/
                            }
                    ){
                        Text(
                            text = "Solved Quests",
                            fontWeight = FontWeight.Bold,
                            fontFamily = catamaranFamily,
                            fontSize = 17.sp
                        )
                        Image(
                            painter = painterResource(R.drawable.history_image),
                            contentDescription = "Solved Quest Image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    //HomeScreen()
}

/*
Attributions
acheivement_image.jpg -> http://www.freepik.com Designed by rawpixel.com
history_image.png -> http://www.freepik.com Designed by studiogstock / Freepik
rule_list_image.jpg -> http://www.freepik.com Designed by Kampus
start_new_mage.jpg -> http://www.freepik.com Designed by macrovector
 */