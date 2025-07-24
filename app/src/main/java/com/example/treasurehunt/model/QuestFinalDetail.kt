package com.example.treasurehunt.model

import androidx.annotation.StringRes

sealed interface QuestFinalDetail {
    data class OnDeviceData(@StringRes val questDetail: Int) : QuestFinalDetail
    data class FetchedData(val questDetail: String) : QuestFinalDetail
}