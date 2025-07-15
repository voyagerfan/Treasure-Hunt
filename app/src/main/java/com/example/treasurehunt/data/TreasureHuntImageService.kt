package com.example.treasurehunt.data

interface ImageApi {
    suspend fun fetchImage()
}

class TreasureHuntImageService: ImageApi {
    override suspend fun fetchImage() {
        TODO("Not yet implemented")
    }
}