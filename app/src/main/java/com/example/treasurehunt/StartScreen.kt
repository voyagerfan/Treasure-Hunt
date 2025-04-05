/*
StartScreen.kt

Lamar Petty
OSU
CS 492
 */

package com.example.treasurehunt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.treasurehunt.data.DataSource
import com.example.treasurehunt.model.Rule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(onStartClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.AppTitle)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RuleList(
                ruleList = DataSource.loadRules()
            )
            Button(
                onClick = onStartClick,
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

@Composable
fun RuleList(ruleList: List<Rule>) {
    LazyColumn(
        modifier = Modifier
            .height(650.dp)
    ) {
        items(ruleList) { rule ->
            RuleCard(
                rule = rule,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun RuleCard(
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
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(rule.rule_text),
                    modifier = Modifier.padding(
                        start = 0.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewStart() {
    StartScreen(onStartClick = {})
}
