package com.example.treasurehunt.model

import androidx.annotation.StringRes

data class CompletedQuestData(
    @StringRes val questDetail: Int,
    val questPicture: QuestImage.Resource, // TODO: Remove when Image service is integrated
    val questImageID: String
)
