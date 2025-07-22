package com.example.treasurehunt.model

sealed class FilterData {

    abstract val isVisible: Boolean
    data class Radius(
        override val isVisible: Boolean,
        val data: Float
    ) : FilterData()

    data class RatingRange(
        override val isVisible: Boolean,
        val data: Pair<Float, Float>
    ) : FilterData()
}
