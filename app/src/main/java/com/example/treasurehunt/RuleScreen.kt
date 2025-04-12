package com.example.treasurehunt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.treasurehunt.data.DataSource
import com.example.treasurehunt.data.ScreenList
import com.example.treasurehunt.model.Rule


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
                            navController.navigate(ScreenList.ONBOARDING.name) /* TODO: Re-route when more pages made */
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
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
    LazyColumn {
        items(ruleList) { rule ->
            RuleItem(
                rule = rule,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun RuleItem(
    rule: Rule,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column {
            Row {
                Text(
                    text = stringResource(rule.rule_number),
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 5.dp,
                        bottom = 16.dp
                    ),
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = stringResource(rule.rule_text),
                    modifier = Modifier.padding(
                        start = 0.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStart2() {
    RuleScreen(navController = rememberNavController())
}