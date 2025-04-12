package com.example.treasurehunt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.treasurehunt.data.DataSource
import com.example.treasurehunt.data.ScreenList
import com.example.treasurehunt.model.Rule
import com.example.treasurehunt.ui.theme.catamaranFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuleScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.AppTitle)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            /* TODO: Re-route when more pages made */
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = "arrow"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            /* TODO: Re-route when more pages made */
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_menu_24),
                            contentDescription = "arrow"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {/*TODO: Add next route here*/},
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 2.dp)
                                .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            stringResource(R.string.Start)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ScrollableRuleList(
                ruleList = DataSource.loadRules()
            )
        }
    }
}

@Composable
fun ScrollableRuleList(ruleList: List<Rule>) {
    LazyColumn (
        modifier = Modifier.fillMaxWidth()
    ) {
        items(ruleList) { rule ->
            RuleItem(
                rule = rule,
            )
        }
    }
}

@Composable
fun RuleItem(
    rule: Rule,
) {
    Card(
        modifier = Modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = stringResource(rule.rule_number),
                fontWeight = FontWeight.Bold,
                fontFamily = catamaranFamily,
                fontSize = 17.sp
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = stringResource(rule.rule_text),
                fontWeight = FontWeight.Bold,
                fontFamily = catamaranFamily,
                fontSize = 20.sp,
                textAlign = TextAlign.Center

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStart2() {
    RuleScreen(navController = rememberNavController())
}