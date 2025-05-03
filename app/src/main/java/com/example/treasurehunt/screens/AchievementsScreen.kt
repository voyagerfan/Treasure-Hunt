package com.example.treasurehunt.screens


import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.MATCH_DEFAULT_ONLY
import android.content.pm.ResolveInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.treasurehunt.R
import com.example.treasurehunt.ui.theme.catamaranFamily
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen() {
    var showShareSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
    }
    val packageManager = LocalContext.current.packageManager
    val appList: List<ResolveInfo> = packageManager.queryIntentActivities(
        shareIntent,
        MATCH_DEFAULT_ONLY
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = { showShareSheet.value = true }
        ) {
            Icon(
                painter = painterResource(R.drawable.share_24dp),
                contentDescription = "Share Icon",
            )
        }
        if(showShareSheet.value){
            ModalBottomSheet(
                onDismissRequest = { showShareSheet.value = false},
                sheetState = sheetState
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyVerticalGrid (
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        items(appList) { resolveInfo ->
                            ShareItem(resolveInfo, packageManager)
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ShareItem(
    resolveInfo: ResolveInfo,
    packageManager: PackageManager
) {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { /* TODO: Add functionality here */ }
        ) {
            Icon(
                painter = rememberDrawablePainter(resolveInfo.loadIcon(packageManager)),
                contentDescription = resolveInfo.loadLabel(packageManager).toString(),
                tint = Color.Unspecified
            )
        }
        Text(
            text = resolveInfo.loadLabel(packageManager).toString(),
            fontWeight = FontWeight.Medium,
            fontFamily = catamaranFamily,
            fontSize = 10.sp
        )

    }
}

@Preview
@Composable
fun PreviewAchievementScreen() {
    AchievementsScreen()
}